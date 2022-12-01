package com.uniwheelsapp.uniwheelsapp.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.models.PasajeroViaje;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Vehiculo;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;

import java.util.ArrayList;

public class PlannedTravelsPassengerAdapter extends RecyclerView.Adapter<PlannedTravelsPassengerAdapter.ViewHolderPlannedTravelsPassenger> {
    private ArrayList<Viaje> viajesPlaneados;
    private Person pasajeroViaje;
    private PlannedTravelsPassengerAdapter.PlannedTravelsClickListener clickListener;

    public PlannedTravelsPassengerAdapter(ArrayList<Viaje> viajesPlaneados, Person pasajero, PlannedTravelsPassengerAdapter.PlannedTravelsClickListener clickListener){
        this.viajesPlaneados = viajesPlaneados;
        this.pasajeroViaje = pasajero;
        this.clickListener = clickListener;
    }

    public void updateData(ArrayList<Viaje> viajesPlaneados){
        this.viajesPlaneados = viajesPlaneados;
        notifyDataSetChanged();
    }

    public void cancelTravel(Viaje viaje){
        this.viajesPlaneados.remove(viaje);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlannedTravelsPassengerAdapter.ViewHolderPlannedTravelsPassenger onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lists_planned_travels_passengers, parent, false);
        return new ViewHolderPlannedTravelsPassenger(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannedTravelsPassengerAdapter.ViewHolderPlannedTravelsPassenger holder, int position) {
        Viaje viaje = viajesPlaneados.get(position);
        Vehiculo vehiculo = viaje.getVehiculo();
        holder.conductor.setText(viaje.getConductor().getNombre());
        holder.tipoVehiculo.setText(vehiculo.getTipo());
        holder.matricula.setText(vehiculo.getMatricula());
        holder.modelo.setText(vehiculo.getModelo());
        holder.marca.setText(vehiculo.getMarca());
        holder.dia.setText(viaje.getSalida().toString());
        holder.horaSalida.setText(viaje.getSalida().toString());
        holder.estado.setText(viaje.getPasajeros().get(viaje.getPasajeros().indexOf(pasajeroViaje)).getEstadoSolicitud());
        holder.cancelTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onCancelTravel(viajesPlaneados.get(holder.getBindingAdapterPosition()), pasajeroViaje);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viajesPlaneados.size();
    }

    public interface PlannedTravelsClickListener {
        public void onCancelTravel(Viaje viaje, Person personaViaje);
    }

    public class ViewHolderPlannedTravelsPassenger extends RecyclerView.ViewHolder {

        TextView conductor, tipoVehiculo, matricula, modelo, marca, dia, horaSalida, estado;
        Button cancelTravel;

        public ViewHolderPlannedTravelsPassenger(@NonNull View itemView) {
            super(itemView);
            conductor = itemView.findViewById(R.id.conductor);
            tipoVehiculo = itemView.findViewById(R.id.tipoVehiculo);
            modelo = itemView.findViewById(R.id.modelo);
            marca = itemView.findViewById(R.id.marca);
            dia = itemView.findViewById(R.id.dia);
            horaSalida = itemView.findViewById(R.id.horaSalida);
            estado = itemView.findViewById(R.id.estado);
            cancelTravel = itemView.findViewById(R.id.cancelTravel);

        }
    }
}
