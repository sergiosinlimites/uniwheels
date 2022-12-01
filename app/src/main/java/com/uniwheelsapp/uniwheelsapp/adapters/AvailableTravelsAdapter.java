package com.uniwheelsapp.uniwheelsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uniwheelsapp.uniwheelsapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uniwheelsapp.uniwheelsapp.models.Viaje;

import java.util.ArrayList;

public class AvailableTravelsAdapter extends RecyclerView.Adapter<AvailableTravelsAdapter.ViewHolderAvailableTravels> {

    private ArrayList<Viaje> viajesDisponibles;
    private AvailableTravelsAdapter.AvailableTravelsClickListener clickListener;

    private final String HOGAR_UNI = "Hogar a universidad";
    private final String UNI_HOGAR = "Universidad a hogar";

    public AvailableTravelsAdapter(ArrayList<Viaje> viajesDisponibles, AvailableTravelsAdapter.AvailableTravelsClickListener clickListener){
        this.viajesDisponibles = viajesDisponibles;
        this.clickListener = clickListener;
    }

    public void updateData(ArrayList<Viaje> viajesPlaneados){
        this.viajesDisponibles = viajesPlaneados;
        notifyDataSetChanged();
    }

    public void removeOne(Viaje viaje){
        this.viajesDisponibles.remove(viaje);
    }

    @NonNull
    @Override
    public AvailableTravelsAdapter.ViewHolderAvailableTravels onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lists_available_travels, parent, false);
        return new AvailableTravelsAdapter.ViewHolderAvailableTravels(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableTravelsAdapter.ViewHolderAvailableTravels holder, int position) {
        Viaje viaje = viajesDisponibles.get(position);
        holder.conductor.setText(viaje.getConductor() != null ? viaje.getConductor().getNombre(): "No definido");
        holder.tipoVehiculo.setText(viaje.getVehiculo() != null ? viaje.getVehiculo().getTipo() : "No definido");
        if(viaje.getTipoViaje().equals(UNI_HOGAR)){
            holder.lugarSalida.setText(viaje.getUniversidad() != null ? viaje.getUniversidad().getNombre() : "No definido");
            holder.lugarLlegada.setText(viaje.getLugar() != null ? viaje.getLugar().getBarrio() + ", " + viaje.getLugar().getUpz() + ", " + viaje.getLugar().getLocalidad() : "No definido");
        } else if (viaje.getTipoViaje().equals(HOGAR_UNI)){
            holder.lugarLlegada.setText(viaje.getUniversidad() != null ? viaje.getUniversidad().getNombre() : "No definido");
            holder.lugarSalida.setText(viaje.getLugar() != null ? viaje.getLugar().getBarrio() + ", " + viaje.getLugar().getUpz() + ", " + viaje.getLugar().getLocalidad() : "No definido");
        }
        holder.horaSalida.setText(viaje.getSalida() != null ? viaje.getSalida().toString() : "No definido");
        holder.horaLlegada.setText(viaje.getLlegada() != null ? viaje.getLlegada().toString() : "No definido");
        holder.tiempoEstimado.setText(viaje.getTiempoEstimado() != null ? viaje.getTiempoEstimado() : "No definido");
        holder.cuposRestantes.setText(String.valueOf(viaje.getCupos()));
        holder.solicitarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onSeeDetailsFromTravel(viaje);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viajesDisponibles.size();
    }

    public interface AvailableTravelsClickListener {
        public void onSeeDetailsFromTravel(Viaje viaje);
    }

    public class ViewHolderAvailableTravels extends RecyclerView.ViewHolder {

        TextView conductor, tipoVehiculo, lugarSalida, horaSalida, lugarLlegada, horaLlegada, tiempoEstimado, cuposRestantes;
        Button solicitarViaje;

        public ViewHolderAvailableTravels(@NonNull View itemView) {
            super(itemView);
            conductor = itemView.findViewById(R.id.conductor);
            tipoVehiculo = itemView.findViewById(R.id.tipoVehiculo);
            lugarSalida = itemView.findViewById(R.id.lugarSalida);
            horaSalida = itemView.findViewById(R.id.horaSalida);
            lugarLlegada = itemView.findViewById(R.id.lugarLlegada);
            horaLlegada = itemView.findViewById(R.id.dia);
            tiempoEstimado = itemView.findViewById(R.id.tiempoEstimado);
            cuposRestantes = itemView.findViewById(R.id.cuposRestantes);
            solicitarViaje = itemView.findViewById(R.id.solicitarViaje);
        }
    }
}
