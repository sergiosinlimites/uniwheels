package com.uniwheelsapp.uniwheelsapp.models;

public class Lugar {
    private String ciudad;
    private String localidad;
    private String upz;
    private String barrio;

    public Lugar(String ciudad, String localidad, String upz, String barrio) {
        this.ciudad = ciudad;
        this.localidad = localidad;
        this.upz = upz;
        this.barrio = barrio;
    }

    public Lugar() {
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getUpz() {
        return upz;
    }

    public String getBarrio() {
        return barrio;
    }
}
