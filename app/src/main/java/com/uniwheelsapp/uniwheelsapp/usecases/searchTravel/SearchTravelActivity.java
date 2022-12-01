package com.uniwheelsapp.uniwheelsapp.usecases.searchTravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.adapters.AvailableTravelsAdapter;
import com.uniwheelsapp.uniwheelsapp.adapters.PlannedTravelsPassengerAdapter;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivitySearchTravelBinding;
import com.uniwheelsapp.uniwheelsapp.dialogs.MeetingPointDialog;
import com.uniwheelsapp.uniwheelsapp.models.PasajeroViaje;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Preferences;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.usecases.newTravel.NewTravelActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchTravelActivity extends AppCompatActivity implements AvailableTravelsAdapter.AvailableTravelsClickListener, MeetingPointDialog.MeetingPointListener {

    private ActivitySearchTravelBinding binding;

    private SearchTravelViewModel viewModel;

    private Person person;
    private Viaje viajeEnProceso;

    private ArrayAdapter<CharSequence> tiposViajesAdapter, localidadAdapter, upzAdapter, barrioAdapter, universidadAdapter;

    private String tipoViajeSelected, localidadSelected, upzSelected, barrioSelected, universidadSelected;

    private AvailableTravelsAdapter adapter;

    private Calendar departureCalendar = Calendar.getInstance();
    private Calendar arrivalCalendar = Calendar.getInstance();

    private final String SELECCIONA_VIAJE = "Selecciona un tipo de viaje";
    private final String SELECCIONA_LOCALIDAD = "Selecciona la localidad";
    private final String SELECCIONA_UPZ = "Selecciona la UPZ";
    private final String SELECCIONA_BARRIO = "Selecciona el barrio";
    private final String SELECCIONA_UNI = "Selecciona la universidad";
    private final String PLACE_TO_UNI = "Hogar a universidad";
    private final String UNI_TO_PLACE = "Universidad a hogar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_travel);
        View view = binding.getRoot();
        setContentView(view);
        getUserInfo();
        startRecyclerView();

        viewModel = new ViewModelProvider(this).get(SearchTravelViewModel.class);

        // Inicialización spinner localidad
        tiposViajesAdapter = ArrayAdapter.createFromResource(this, R.array.tiposViajes, R.layout.spinner_layout);
        localidadAdapter = ArrayAdapter.createFromResource(this, R.array.localidades, R.layout.spinner_layout);
        universidadAdapter = ArrayAdapter.createFromResource(this, R.array.universidadesBogota, R.layout.spinner_layout);

        // Especificar el layout
        tiposViajesAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        localidadAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        universidadAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

        // Set el adapter
        binding.tipoViajeSpinner.setAdapter(tiposViajesAdapter);
        binding.localidadSpinner.setAdapter(localidadAdapter);
        binding.universidadSpinner.setAdapter(universidadAdapter);

        binding.tipoViajeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoViajeSelected = binding.tipoViajeSpinner.getSelectedItem().toString();
                int parentID = adapterView.getId();
                if(parentID == R.id.tipoViajeSpinner) {
                    if (tipoViajeSelected.equals(SELECCIONA_VIAJE)) {
                        binding.layoutHogar.setVisibility(View.GONE);
                    } else if (tipoViajeSelected.equals(PLACE_TO_UNI)){
                        binding.lugarText.setText("Ingresa los datos del lugar de partida");
                        binding.universidadText.setText("Ingresa los datos del lugar de destino");
                        binding.layoutHogar.setVisibility(View.VISIBLE);
                    } else if (tipoViajeSelected.equals(UNI_TO_PLACE)){
                        binding.lugarText.setText("Ingresa los datos del lugar de destino");
                        binding.universidadText.setText("Ingresa los datos del lugar de partida");
                        binding.layoutHogar.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.departureCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchTravelActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        departureCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        departureCalendar.set(Calendar.MONTH, month);
                        departureCalendar.set(Calendar.YEAR, year);
                        binding.departureDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        binding.departureTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SearchTravelActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        departureCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        departureCalendar.set(Calendar.MINUTE, minute);
                        binding.departureTime.setText(hourOfDay+":"+minute);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        binding.arrivalCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchTravelActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        arrivalCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        arrivalCalendar.set(Calendar.MONTH, month);
                        arrivalCalendar.set(Calendar.YEAR, year);
                        binding.arrivalDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        binding.arrivalTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SearchTravelActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        arrivalCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        arrivalCalendar.set(Calendar.MINUTE, minute);
                        binding.arrivalTime.setText(hourOfDay+":"+minute);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        binding.localidadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Seleccionar las posibles UPZ a partir de la localidad
                localidadSelected = binding.localidadSpinner.getSelectedItem().toString();

                int parentID = adapterView.getId();
                if(parentID == R.id.localidadSpinner){
                    switch (localidadSelected){
                        case SELECCIONA_LOCALIDAD:
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upzsDefault, R.layout.spinner_layout);
                            break;
                        case "Usaquén":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Chapinero":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Santa Fe":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "San Cristóbal":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Usme":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Tunjuelito":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Bosa":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Kennedy":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Fontibón":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Engativá":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Suba":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Barrios Unidos":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Teusaquillo":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Los Mártires":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Antonio Nariño":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Puente Aranda":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "La Candelaria":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Rafael Uribe Uribe":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Ciudad Bolívar":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        case "Sumapaz":
                            upzAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.upz_localidad_usaquen, R.layout.spinner_layout);
                            break;
                        default: break;
                    }
                    barrioAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.barriosDefault, R.layout.spinner_layout);
                    barrioAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    binding.barrioSpinner.setAdapter(barrioAdapter);

                    upzAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    binding.upzSpinner.setAdapter(upzAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.upzSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Seleccionar las posibles UPZ a partir de la localidad
                upzSelected = binding.upzSpinner.getSelectedItem().toString();
                Log.d("UPZ SELECCIONADA", "onItemSelected: " +upzSelected);
                int parentID = adapterView.getId();
                if(parentID == R.id.upzSpinner) {
                    switch (upzSelected) {
                        case SELECCIONA_UPZ:
                            barrioAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.barriosDefault, R.layout.spinner_layout);
                            break;
                        case "Paseo de Los Libertadores":
                            barrioAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.barrios_upz_paseo_de_los_libertadores, R.layout.spinner_layout);
                            break;
                        case "Verbenal":
                            barrioAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.barrios_upz_verbenal, R.layout.spinner_layout);
                            break;
                        case "La Uribe":
                            barrioAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.barrios_upz_la_uribe, R.layout.spinner_layout);
                            break;
                        default: break;
                    }
                    barrioAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    binding.barrioSpinner.setAdapter(barrioAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.barrioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                barrioSelected = binding.barrioSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.universidadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                universidadSelected = binding.universidadSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.searchTravels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date salida = departureCalendar.getTime();
                Date llegada = arrivalCalendar.getTime();

                viewModel.getTravelsFromDate();
                // tipoViajeSelected, localidadSelected, upzSelected, barrioSelected, universidadSelected;
            }
        });

        viewModel.getViajes().observe(this, new Observer<ArrayList<Viaje>>() {
            @Override
            public void onChanged(ArrayList<Viaje> viajes) {
                ArrayList<Viaje> posiblesViajes = new ArrayList<>();
                Log.d("TAMAÑO INICIAL", String.valueOf(viajes.size()));
                Boolean continua = true;
                for(Viaje viajeLista : viajes){
                    Log.d("tipoViajeSelected", tipoViajeSelected + " " +  String.valueOf(tipoViajeSelected != null && !tipoViajeSelected.isEmpty() && !tipoViajeSelected.equals(SELECCIONA_VIAJE) && !viajeLista.getTipoViaje().equals(tipoViajeSelected)));
                    Log.d("localidadSelected", localidadSelected + " " + String.valueOf(localidadSelected != null && !localidadSelected.isEmpty() && !localidadSelected.equals(SELECCIONA_LOCALIDAD) && !viajeLista.getLugar().getLocalidad().equals(localidadSelected)));
                    Log.d("upzSelected",upzSelected + " " +  String.valueOf(upzSelected != null && !upzSelected.isEmpty() && !upzSelected.equals(SELECCIONA_UPZ) && !viajeLista.getLugar().getUpz().equals(upzSelected)));
                    Log.d("barrioSelected", barrioSelected + "  " + String.valueOf(barrioSelected != null && !barrioSelected.isEmpty() && !barrioSelected.equals(SELECCIONA_BARRIO) && !viajeLista.getLugar().getBarrio().equals(barrioSelected)));
                    Log.d("universidadSelected", universidadSelected + " " + String.valueOf(universidadSelected != null && !universidadSelected.isEmpty() && !universidadSelected.equals(SELECCIONA_UNI) && !viajeLista.getUniversidad().equals(universidadSelected)));
                    Log.d("espera", viajeLista.getDocumentId());

                    if(
                        (tipoViajeSelected != null && !tipoViajeSelected.isEmpty() && !tipoViajeSelected.equals(SELECCIONA_VIAJE) && !viajeLista.getTipoViaje().equals(tipoViajeSelected)) ||
                        (localidadSelected != null && !localidadSelected.isEmpty() && !localidadSelected.equals(SELECCIONA_LOCALIDAD) && !viajeLista.getLugar().getLocalidad().equals(localidadSelected)) ||
                        (upzSelected != null && !upzSelected.isEmpty() && !upzSelected.equals(SELECCIONA_UPZ) && !viajeLista.getLugar().getUpz().equals(upzSelected)) ||
                        (barrioSelected != null && !barrioSelected.isEmpty() && !barrioSelected.equals(SELECCIONA_BARRIO) && !viajeLista.getLugar().getBarrio().equals(barrioSelected)) ||
                        (universidadSelected != null && !universidadSelected.isEmpty() && !universidadSelected.equals(SELECCIONA_UNI) && !viajeLista.getUniversidad().equals(universidadSelected))
                    ){
                       continua = false;
                    }
                    for(PasajeroViaje pasajeroViaje : viajeLista.getPasajeros()){
                        //Log.d("CORREO PASAJERO", pasajeroViaje.getCorreo());
                        if(pasajeroViaje.getCorreo().equals(person.getEmail())){
                            continua = false;
                        }
                    }
                    if(continua){
                        //Log.d("posibles viajes", viajeLista.getDocumentId());
                        posiblesViajes.add(viajeLista);
                    }
                }
                adapter.updateData(posiblesViajes);
            }
        });
    }

    private void startRecyclerView() {
        ArrayList<Viaje> viajesPlaneados = new ArrayList<>();
        binding.listaViajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AvailableTravelsAdapter(viajesPlaneados, this);
        binding.listaViajes.setAdapter(adapter);
    }

    private void getUserInfo() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Preferences.PREFERENCES, MODE_PRIVATE);
        String personString = sharedPreferences.getString(Preferences.USER_INFO, "");
        person = gson.fromJson(personString, Person.class);
        if(person == null || !person.getHabilitado()){
            Toast.makeText(this, "No estás habilitado, pronto uno de nuestros administradores verá tu solicitud", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onSeeDetailsFromTravel(Viaje viaje) {
        viajeEnProceso = viaje;
        MeetingPointDialog meetingPointDialog = new MeetingPointDialog();
        meetingPointDialog.show(getSupportFragmentManager(), "Solicitar punto de encuentro");
    }

    @Override
    public void terminarSolicitud(String solicitud) {
        PasajeroViaje nuevoPasajero = new PasajeroViaje(person.getEmail(), person.getNombre(), person.getApellido(), person.getFoto(), person.getCelular(), person.getCalificacion(), solicitud, "EN REVISIÓN");
        ArrayList<PasajeroViaje> pasajeros = viajeEnProceso.getPasajeros();
        for(PasajeroViaje pasajeroViaje : pasajeros){
            if(pasajeroViaje.getCorreo().equals(nuevoPasajero.getCorreo()) && !pasajeroViaje.getEstadoSolicitud().equals("RECHAZADA")){
                Toast.makeText(this, "Ya has solicitado este viaje", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Log.d("LOS PASAJEROS",String.valueOf(pasajeros == null));
        if(pasajeros == null){
            pasajeros = new ArrayList<>();
        }
        pasajeros.add(nuevoPasajero);
        Log.d("PASAJERO", pasajeros.get(0).getCorreo());
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("pasajeros", pasajeros);
        viewModel.updatePassengers(viajeEnProceso.getDocumentId(), updateData);
        viajeEnProceso.setDocumentId(null);
        adapter.removeOne(viajeEnProceso);
        viajeEnProceso = null;
    }
}