package com.uniwheelsapp.uniwheelsapp.models;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.type.DateTime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;

public class Viaje {
    private String documentId;
    private ConductorViaje conductor;
    private Vehiculo vehiculo;
    private ArrayList<PasajeroViaje> pasajeros;
    private ArrayList<String> correosPasajeros;
    private Lugar lugar;
    private Universidad universidad;
    private Date salida;
    private Date llegada;
    private int tarifa;
    private int cupos;
    private String tiempoEstimado;
    private String estadoViaje;
    private Date fechaCreacion;
    private String tipoViaje;
    private PasajeroViaje pasajeroViaje;

    public Viaje(){

    }

    public Viaje(ConductorViaje conductor, Lugar lugar, Universidad universidad, Date salida, Date llegada, int tarifa, int cupos, String tipoViaje, Vehiculo vehiculo) {
        this.conductor = conductor;
        this.lugar = lugar;
        this.universidad = universidad;
        this.salida = salida;
        this.llegada = llegada;
        this.tarifa = tarifa;
        this.cupos = cupos;
        this.tipoViaje = tipoViaje;
        this.tiempoEstimado = calcularTiempoEstimado(llegada, salida);
        this.estadoViaje = "SIN COMENZAR";
        this.vehiculo = vehiculo;
    }

    public String getTipoViaje() {
        return tipoViaje;
    }

    public void setTipoViaje(String tipoViaje) {
        this.tipoViaje = tipoViaje;
    }

    public String getDocumentId(){
        return documentId;

    }

    public void setDocumentId(String documentId){
        this.documentId = documentId;
    }

    public ConductorViaje getConductor() {
        return conductor;
    }

    public Date getSalida() {
        return salida;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public Universidad getUniversidad() {
        return universidad;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public void setUniversidad(Universidad universidad) {
        this.universidad = universidad;
    }

    public Date getLlegada() {
        return llegada;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
    }

    public void setLlegada(Date llegada) {
        this.llegada = llegada;
    }

    public ArrayList<PasajeroViaje> getPasajeros() {
        if(pasajeros != null){
            return pasajeros;
        } else {
            return new ArrayList<PasajeroViaje>();
        }
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public int getTarifa() {
        return tarifa;
    }

    public int getCupos() {
        return cupos;
    }

    public String getTiempoEstimado() {
        return tiempoEstimado;
    }

    public String getEstadoViaje() {
        return estadoViaje;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setConductor(ConductorViaje conductor) {
        this.conductor = conductor;
    }

    public void setPasajeros(ArrayList<PasajeroViaje> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public void setTiempoEstimado(String tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public String calcularTiempoEstimado(Date tiempoLlegada, Date tiempoSalida){
        long difference_In_Time
                = tiempoLlegada.getTime() - tiempoSalida.getTime();
        long difference_In_Minutes
                = (difference_In_Time
                / (1000 * 60))
                % 60;
        long difference_In_Hours
                = (difference_In_Time
                / (1000 * 60 * 60))
                % 24;
        Log.d("HORAS", String.valueOf(difference_In_Hours));
        if(difference_In_Hours >= 1){
            return difference_In_Hours + " horas " + difference_In_Minutes + " minutos";
        } else {
            return difference_In_Minutes + " minutos";
        }
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

    public void setPasajero(PasajeroViaje pasajero) {
        this.pasajeroViaje = pasajero;
    }

    public PasajeroViaje getPasajero(){
        return this.pasajeroViaje;
    }
}
