package com.uniwheelsapp.uniwheelsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;

import java.util.ArrayList;

public class PlannedTravelsAdapter extends RecyclerView.Adapter<PlannedTravelsAdapter.ViewHolderPlannedTravels> {

    private ArrayList<Viaje> viajesPlaneados;
    private PlannedTravelsClickListener clickListener;

    private final String HOGAR_UNI = "Hogar a universidad";
    private final String UNI_HOGAR = "Universidad a hogar";

    public PlannedTravelsAdapter(ArrayList<Viaje> viajesPlaneados, PlannedTravelsClickListener clickListener){
        this.viajesPlaneados = viajesPlaneados;
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
    public ViewHolderPlannedTravels onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lists_planned_travels, parent, false);
        return new ViewHolderPlannedTravels(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlannedTravels holder, int position) {
        Viaje viaje = viajesPlaneados.get(position);
        holder.travelDay.setText(viaje.getLlegada() != null ? viaje.getLlegada().toString() : "No definido");
        holder.travelQuota.setText(String.valueOf(viaje.getCupos()));
        holder.travelPassengersNumber.setText(String.valueOf(viaje.getPasajeros().size()));

        holder.travelDepartureTime.setText(viaje.getSalida() != null ? viaje.getSalida().toString() : "No definido");
        holder.travelArrivalTime.setText(viaje.getLlegada() != null ? viaje.getLlegada().toString() : "No definido");

        if(viaje.getTipoViaje().equals(UNI_HOGAR)){
            holder.travelDeparturePlace.setText(viaje.getUniversidad() != null ? viaje.getUniversidad().getNombre() : "No definido");
            holder.travelArrivalPlace.setText(viaje.getLugar() != null ? viaje.getLugar().getBarrio() + ", " + viaje.getLugar().getUpz() + ", " + viaje.getLugar().getLocalidad() : "No definido");
        } else if (viaje.getTipoViaje().equals(HOGAR_UNI)){
            holder.travelArrivalPlace.setText(viaje.getUniversidad() != null ? viaje.getUniversidad().getNombre() : "No definido");
            holder.travelDeparturePlace.setText(viaje.getLugar() != null ? viaje.getLugar().getBarrio() + ", " + viaje.getLugar().getUpz() + ", " + viaje.getLugar().getLocalidad() : "No definido");
        }

        holder.cancelTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onCancelTravel(viajesPlaneados.get(holder.getBindingAdapterPosition()));
            }
        });

        holder.detailsTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onSeeDetailsFromTravel(viajesPlaneados.get(holder.getBindingAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return viajesPlaneados.size();
    }

    public class ViewHolderPlannedTravels extends RecyclerView.ViewHolder {

        TextView travelDay, travelQuota, travelPassengersNumber, travelDeparturePlace,
                travelDepartureTime, travelArrivalPlace, travelArrivalTime;
        Button cancelTravel, detailsTravel;

        public ViewHolderPlannedTravels(@NonNull View itemView) {
            super(itemView);
            travelDay = itemView.findViewById(R.id.travelDay);
            travelQuota = itemView.findViewById(R.id.travelQuota);
            travelPassengersNumber = itemView.findViewById(R.id.travelPassengersNumber);
            travelDeparturePlace = itemView.findViewById(R.id.travelDeparturePlace);
            travelDepartureTime = itemView.findViewById(R.id.travelDepartureTime);
            travelArrivalPlace = itemView.findViewById(R.id.travelArrivalPlace);
            travelArrivalTime = itemView.findViewById(R.id.travelArrivalTime);
            cancelTravel = itemView.findViewById(R.id.solicitarViaje);
            detailsTravel = itemView.findViewById(R.id.detailsTravel);
        }
    }

    public interface PlannedTravelsClickListener {
        public void onCancelTravel(Viaje viaje);
        public void onSeeDetailsFromTravel(Viaje viaje);
    }
}
