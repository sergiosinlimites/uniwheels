package com.uniwheelsapp.uniwheelsapp.models;

public class PasajeroViaje {

    private String correo;
    private String nombre;
    private String apellido;
    private String foto;
    private float calificacion;

    private int celular;
    private String puntoEncuentro;
    private String estadoSolicitud;

    public PasajeroViaje() {
    }

    public PasajeroViaje(String correo, String nombre, String apellido, String foto, int celular, float calificacion, String puntoEncuentro, String estadoSolicitud) {
        this.correo = correo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.foto = foto;
        this.celular = celular;
        this.calificacion = calificacion;
        this.puntoEncuentro = puntoEncuentro;
        this.estadoSolicitud = estadoSolicitud;
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

    public int getCelular() {
        return celular;
    }

    public void setCelular(int celular) {
        this.celular = celular;
    }

    public String getPuntoEncuentro() {
        return puntoEncuentro;
    }

    public void setPuntoEncuentro(String puntoEncuentro) {
        this.puntoEncuentro = puntoEncuentro;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }
}
