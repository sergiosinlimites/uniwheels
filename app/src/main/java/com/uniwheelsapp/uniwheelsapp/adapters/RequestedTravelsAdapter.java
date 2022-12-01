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

public class RequestedTravelsAdapter extends RecyclerView.Adapter<RequestedTravelsAdapter.ViewHolderRequestedTravels> {

    private ArrayList<Viaje> viajeSolicitados;
    private RequestedTravelsAdapter.RequestedTravelsClickListener clickListener;

    private final String HOGAR_UNI = "Hogar a universidad";
    private final String UNI_HOGAR = "Universidad a hogar";

    public RequestedTravelsAdapter(ArrayList<Viaje> viajeSolicitados, RequestedTravelsAdapter.RequestedTravelsClickListener  clickListener){
        this.viajeSolicitados = viajeSolicitados;
        this.clickListener = clickListener;
    }

    public void updateData(ArrayList<Viaje> viajesPlaneados){
        this.viajeSolicitados = viajesPlaneados;
        notifyDataSetChanged();
    }

    public void removeRequest(Viaje viaje){
        this.viajeSolicitados.remove(viaje);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestedTravelsAdapter.ViewHolderRequestedTravels onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lists_requested_travels, parent, false);
        return new RequestedTravelsAdapter.ViewHolderRequestedTravels(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedTravelsAdapter.ViewHolderRequestedTravels holder, int position) {
        Viaje viaje = viajeSolicitados.get(position);
        holder.travelDay.setText(viaje.getLlegada() != null ? viaje.getLlegada().toString() : "No definido");
        if(viaje.getTipoViaje().equals(UNI_HOGAR)){
            holder.travelDeparturePlace.setText(viaje.getUniversidad() != null ? viaje.getUniversidad().getNombre() : "No definido");
            holder.travelArrivalPlace.setText(viaje.getLugar() != null ? viaje.getLugar().getBarrio() + ", " + viaje.getLugar().getUpz() + ", " + viaje.getLugar().getLocalidad() : "No definido");
        } else if (viaje.getTipoViaje().equals(HOGAR_UNI)){
            holder.travelArrivalPlace.setText(viaje.getUniversidad() != null ? viaje.getUniversidad().getNombre() : "No definido");
            holder.travelDeparturePlace.setText(viaje.getLugar() != null ? viaje.getLugar().getBarrio() + ", " + viaje.getLugar().getUpz() + ", " + viaje.getLugar().getLocalidad() : "No definido");
        }
        holder.travelDepartureTime.setText(viaje.getSalida() != null ? viaje.getSalida().toString() : "No definido");
        holder.travelArrivalTime.setText(viaje.getLlegada() != null ? viaje.getLlegada().toString() : "No definido");

        holder.meetingPoint.setText(viaje.getPasajero().getPuntoEncuentro());

        holder.rejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onRejectRequest(viajeSolicitados.get(holder.getBindingAdapterPosition()));
            }
        });

        holder.acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onAcceptRequest(viajeSolicitados.get(holder.getBindingAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return viajeSolicitados.size();
    }

    public interface RequestedTravelsClickListener {
        void onAcceptRequest(Viaje viaje);
        void onRejectRequest(Viaje viaje);
    }

    public class ViewHolderRequestedTravels extends RecyclerView.ViewHolder {

        TextView travelDay, travelDeparturePlace, travelDepartureTime, travelArrivalPlace, travelArrivalTime, meetingPoint;
        Button rejectRequest, acceptRequest;

        public ViewHolderRequestedTravels(@NonNull View itemView) {
            super(itemView);
            travelDay = itemView.findViewById(R.id.travelDay);
            travelDeparturePlace = itemView.findViewById(R.id.travelDeparturePlace);
            travelDepartureTime = itemView.findViewById(R.id.travelDepartureTime);
            travelArrivalPlace = itemView.findViewById(R.id.travelArrivalPlace);
            travelArrivalTime = itemView.findViewById(R.id.travelArrivalTime);
            meetingPoint = itemView.findViewById(R.id.meetingPoint);
            rejectRequest = itemView.findViewById(R.id.rejectRequest);
            acceptRequest = itemView.findViewById(R.id.acceptRequest);
        }
    }
}
