package com.uniwheelsapp.uniwheelsapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.uniwheelsapp.uniwheelsapp.R;

public class MeetingPointDialog extends AppCompatDialogFragment {

    private EditText editTextMeetingPoint;
    private MeetingPointListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_punto_encuentro, null);
        builder.setView(view)
                .setTitle("Solicitar punto de encuentro")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Solicitar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String solicitud = editTextMeetingPoint.getText().toString();
                        listener.terminarSolicitud(solicitud);
                    }
                });
        editTextMeetingPoint = view.findViewById(R.id.puntoEncuentro);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MeetingPointListener) context;
        } catch (ClassCastException exception){
            throw new ClassCastException("debe implementar MeetingPointListener");
        }
    }

    public interface MeetingPointListener {
        void terminarSolicitud(String solicitud);
    }
}
