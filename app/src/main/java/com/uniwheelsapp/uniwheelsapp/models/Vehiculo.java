package com.uniwheelsapp.uniwheelsapp.models;

public class Vehiculo {
    private String matricula;
    private String tipo;
    private int cupos;
    private String marca;
    private String modelo;
    private int anio;
    private boolean habilitado;

    public Vehiculo() {
    }

    public Vehiculo(String matricula, String tipo, int cupos, String marca, String modelo, int anio, boolean habilitado) {
        this.matricula = matricula;
        this.tipo = tipo;
        this.cupos = cupos;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.habilitado = habilitado;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
