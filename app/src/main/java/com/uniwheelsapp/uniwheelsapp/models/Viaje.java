package com.uniwheelsapp.uniwheelsapp.models;

import java.util.ArrayList;
import java.util.Date;

public class Viaje {
    private String documentId;
    private String conductor;
    private ArrayList<String> pasajeros;
    private String puntoSalida;
    private String puntoLlegada;
    private Date fechaSalida;
    private Date fechaLlegada;
    private int tarifa;
    private int cupos;
    private int tiempoEstimado;
    private String estadoViaje;
    private Date fechaCreacion;

    public String getDocumentId(){
        return documentId;
    }

    public void setDocumentId(String documentId){
        this.documentId = documentId;
    }

    public String getConductor() {
        return conductor;
    }

    public ArrayList<String> getPasajeros() {
        if(pasajeros != null){
            return pasajeros;
        } else {
            return new ArrayList<String>();
        }
    }

    public String getPuntoSalida() {
        return puntoSalida;
    }

    public String getPuntoLlegada() {
        return puntoLlegada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public int getTarifa() {
        return tarifa;
    }

    public int getCupos() {
        return cupos;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public String getEstadoViaje() {
        return estadoViaje;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public void setPasajeros(ArrayList<String> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public void setPuntoSalida(String puntoSalida) {
        this.puntoSalida = puntoSalida;
    }

    public void setPuntoLlegada(String puntoLlegada) {
        this.puntoLlegada = puntoLlegada;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public void setEstadoViaje(String estadoViaje) {
        this.estadoViaje = estadoViaje;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void modificarCupos(){
        // do something
    }
}
