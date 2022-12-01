package com.uniwheelsapp.uniwheelsapp.usecases.newTravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityNewTravelBinding;
import com.uniwheelsapp.uniwheelsapp.models.ConductorViaje;
import com.uniwheelsapp.uniwheelsapp.models.Lugar;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Preferences;
import com.uniwheelsapp.uniwheelsapp.models.Universidad;
import com.uniwheelsapp.uniwheelsapp.models.Vehiculo;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.PlannedTravelsViewModel;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NewTravelActivity extends AppCompatActivity {

    private ActivityNewTravelBinding binding;

    private NewTravelViewModel viewModel;

    private Person person;

    private int maximosCupos;

    private ArrayAdapter<CharSequence> tiposViajesAdapter, localidadAdapter, upzAdapter, barrioAdapter, universidadAdapter;

    private String tipoViajeSelected, localidadSelected, upzSelected, barrioSelected, universidadSelected;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_travel);
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(NewTravelViewModel.class);

        getUserInfo();

        //Person person = getIntent().getParcelableExtra("person");

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTravelActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewTravelActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTravelActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewTravelActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                Log.d("hehehhe", "adapterView.getId()" + adapterView.getId() + " " + R.id.upzSpinner );
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

        binding.restarCupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.valueOf(binding.cantidadCupos.getText().toString()) > 1){
                    binding.cantidadCupos.setText(String.valueOf(Integer.valueOf(binding.cantidadCupos.getText().toString())-1));
                } else {
                    Toast.makeText(NewTravelActivity.this, "No se pueden añadir 0 cupos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.sumarCupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.valueOf(binding.cantidadCupos.getText().toString()) < maximosCupos){
                    binding.cantidadCupos.setText(String.valueOf(Integer.valueOf(binding.cantidadCupos.getText().toString())+1));
                } else {
                    Toast.makeText(NewTravelActivity.this, "No se pueden añadir más cupos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.finishPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    binding.tipoViaje.setError(null);
                    binding.localidadString.setError(null);
                    binding.upzString.setError(null);
                    binding.barrioString.setError(null);
                    binding.universidadString.setError(null);
                    if (tipoViajeSelected == null || tipoViajeSelected.equals(SELECCIONA_VIAJE)){
                        Toast.makeText(NewTravelActivity.this, "Por favor selecciona un tipo de viaje", Toast.LENGTH_LONG).show();
                        binding.tipoViaje.setError("El tipo de viaje es requerido");
                        binding.tipoViaje.requestFocus();
                    } else if(localidadSelected == null || localidadSelected.equals(SELECCIONA_LOCALIDAD)){
                        Toast.makeText(NewTravelActivity.this, "Por favor selecciona una localidad", Toast.LENGTH_LONG).show();
                        binding.localidadString.setError("La localidad es requerida");
                        binding.localidadString.requestFocus();
                    } else if(upzSelected == null || upzSelected.equals(SELECCIONA_UPZ)){
                        Toast.makeText(NewTravelActivity.this, "Por favor selecciona una UPZ", Toast.LENGTH_LONG).show();
                        binding.upzString.setError("La UPZ es requerida");
                        binding.upzString.requestFocus();
                    } else if(barrioSelected == null || barrioSelected.equals(SELECCIONA_BARRIO)) {
                        Toast.makeText(NewTravelActivity.this, "Por favor selecciona un barrio", Toast.LENGTH_SHORT).show();
                        binding.barrioString.setError("El barrio es requerido");
                        binding.barrioString.requestFocus();
                    } else if(universidadSelected == null || universidadSelected.equals(SELECCIONA_UNI)) {
                        Toast.makeText(NewTravelActivity.this, "Por favor selecciona una universidad", Toast.LENGTH_SHORT).show();
                        binding.universidadString.setError("La universidad es requerida");
                        binding.universidadString.requestFocus();
                    } else if (binding.tarifa.getText().toString().isEmpty() || binding.tarifa.getText().toString().equals("0")) {
                        Toast.makeText(NewTravelActivity.this, "Por favor asigna una tarifa", Toast.LENGTH_SHORT).show();
                        binding.tarifaString.setError("La tarifa es requerida");
                        binding.tarifaString.requestFocus();
                    } else if (binding.cantidadCupos.getText().toString().isEmpty() || binding.cantidadCupos.getText().toString().equals("0")) {
                        Toast.makeText(NewTravelActivity.this, "Por favor asigna una cantidad de cupos", Toast.LENGTH_SHORT).show();
                        binding.cuposString.setError("La cantidad de cupos tiene que ser al menos 1");
                        binding.cuposString.requestFocus();
                    } else {
                        String ciudad = "Bogotá D.C.";
                        String localidad = localidadSelected;
                        String upz = upzSelected;
                        String barrio = barrioSelected;
                        String nombreUniversidad = universidadSelected;
                        String tipoViaje = tipoViajeSelected;
                        int cupos = Integer.parseInt(binding.cantidadCupos.getText().toString());
                        int tarifa = Integer.parseInt(binding.tarifa.getText().toString());
                        Date salida = departureCalendar.getTime();
                        Date llegada = arrivalCalendar.getTime();

                        Lugar lugar = new Lugar(ciudad, localidad, upz, barrio);
                        Universidad universidad = new Universidad(nombreUniversidad);
                        ConductorViaje conductor = new ConductorViaje(person.getEmail(), person.getNombre(), person.getApellido(), person.getFoto(), person.getCalificacion());
                        Vehiculo vehiculo = person.getVehiculo();
                        Viaje viaje = new Viaje(conductor, lugar, universidad, salida, llegada, tarifa, cupos, tipoViaje, vehiculo);

                        Log.d("VIAJE FINAL", viaje.getLlegada().toString());
                        viewModel.createTravel(viaje);
                    }
                } catch (Exception exception){
                    Toast.makeText(NewTravelActivity.this, "Hubo un error al mostrar error", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", exception.toString());
                }
            }
        });

        viewModel.getSuccessfulCreation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    finish();
                } else {
                    Toast.makeText(NewTravelActivity.this, "Ha ocurrido un error al subir el viaje", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getUserInfo() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Preferences.PREFERENCES, MODE_PRIVATE);
        String personString = sharedPreferences.getString(Preferences.USER_INFO, "");
        person = gson.fromJson(personString, Person.class);
        maximosCupos = person.getVehiculo().getCupos();
        if(person == null || !person.getHabilitado()){
            Toast.makeText(this, "No estás habilitado, pronto uno de nuestros administradores verá tu solicitud", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}