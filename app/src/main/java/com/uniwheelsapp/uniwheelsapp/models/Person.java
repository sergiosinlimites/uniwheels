package com.uniwheelsapp.uniwheelsapp.models;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;
import java.util.List;

public class Person {
    public static final String NAME_KEY = "nombre";
    public static final String LASTNAME_KEY = "apellido";
    public static final String ID_KEY = "cedula";
    public static final String MAIL_KEY = "email";
    public static final String CELLPHONE_KEY = "celular";
    public static final String PHOTO_KEY = "foto";
    public static final String IDPHOTOS_KEY = "fotosCedula";
    public static final String ADDRESS_KEY = "direccion";
    public static final String SCORE_KEY = "calificacion";
    public static final String ENABLED_KEY = "habilitado";
    public static final String PASSWORD_KEY = "password";
    public static final String VALIDCELLPHONE_KEY = "celularValidado";
    public static final String ACTIVE_KEY = "activo";
    public static final String DATE_KEY = "fechaCreacion";

    public Person(){}

    private String nombre;
    private String apellido;
    private Integer cedula;
    private String email;
    private Integer celular;
    private String foto;
    private List<String> fotosCedula;
    private String direccion;
    private Integer calificacion;
    private Boolean habilitado;
    private String password;
    private Boolean celularValidado;
    private Boolean activo;
    private Date fechaCreacion;

    public String getNombre() {
        return nombre;
    }

    @PropertyName("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getCedula() {
        return cedula;
    }

    public void setCedula(Integer cedula) {
        this.cedula = cedula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCelular() {
        return celular;
    }

    public void setCelular(Integer celular) {
        this.celular = celular;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<String> getFotosCedula() {
        return fotosCedula;
    }

    public void setFotosCedula(List<String> fotosCedula) {
        this.fotosCedula = fotosCedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getCelularValidado() {
        return celularValidado;
    }

    public void setCelularValidado(Boolean celularValidado) {
        this.celularValidado = celularValidado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
