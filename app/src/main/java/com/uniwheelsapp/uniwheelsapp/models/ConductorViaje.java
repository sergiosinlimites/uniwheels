package com.uniwheelsapp.uniwheelsapp.models;

public class ConductorViaje {
    private String correo;
    private String nombre;
    private String apellido;
    private String foto;
    private int celular;
    private float calificacion;

    public ConductorViaje(String correo, int celular, String nombre, String apellido, String foto, float calificacion) {
        this.correo = correo;
        this.celular = celular;
        this.nombre = nombre;
        this.apellido = apellido;
        this.foto = foto;
        this.calificacion = calificacion;
    }

    public ConductorViaje() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }
}
