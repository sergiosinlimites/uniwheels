package com.uniwheelsapp.uniwheelsapp.models;

import java.util.ArrayList;
import java.util.Date;

public class Viaje {
    private Person conductor;
    private ArrayList<Person> pasajeros;
    private String puntoSalida;
    private String puntoLlegada;
    private Date fechaSalida;
    private Date fechaLlegada;
    private Number tarifa;
    private Number cupos;
    private Number tiempoEstimado;
    private String estadoViaje;
    private Date fechaCreacion;

    public Person getConductor() {
        return conductor;
    }

    public ArrayList<Person> getPasajeros() {
        return pasajeros;
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

    public Number getTarifa() {
        return tarifa;
    }

    public Number getCupos() {
        return cupos;
    }

    public Number getTiempoEstimado() {
        return tiempoEstimado;
    }

    public String getEstadoViaje() {
        return estadoViaje;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setConductor(Person conductor) {
        this.conductor = conductor;
    }

    public void setPasajeros(ArrayList<Person> pasajeros) {
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

    public void setTarifa(Number tarifa) {
        this.tarifa = tarifa;
    }

    public void setCupos(Number cupos) {
        this.cupos = cupos;
    }

    public void setTiempoEstimado(Number tiempoEstimado) {
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
