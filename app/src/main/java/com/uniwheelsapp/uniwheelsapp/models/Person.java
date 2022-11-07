package com.uniwheelsapp.uniwheelsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;
import java.util.List;

public class Person implements Parcelable {
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
    public static final String USER_TYPE = "tipo";

    public Person(){}

    private String nombre;
    private String apellido;
    private int cedula;
    private String email;
    private int celular;
    private String foto;
    private List<String> fotosCedula;
    private String direccion;
    private float calificacion;
    private Boolean habilitado;
    private String password;
    private Boolean celularValidado;
    private Boolean activo;
    private Date fechaCreacion;
    private String tipo;

    protected Person(Parcel in) {
        nombre = in.readString();
        apellido = in.readString();
        cedula = in.readInt();
        email = in.readString();
        celular = in.readInt();
        foto = in.readString();
        fotosCedula = in.createStringArrayList();
        direccion = in.readString();
        calificacion = in.readInt();
        byte tmpHabilitado = in.readByte();
        habilitado = tmpHabilitado == 0 ? null : tmpHabilitado == 1;
        password = in.readString();
        byte tmpCelularValidado = in.readByte();
        celularValidado = tmpCelularValidado == 0 ? null : tmpCelularValidado == 1;
        byte tmpActivo = in.readByte();
        activo = tmpActivo == 0 ? null : tmpActivo == 1;
        tipo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeInt(cedula);
        dest.writeString(email);
        dest.writeInt(celular);
        dest.writeString(foto);
        dest.writeStringList(fotosCedula);
        dest.writeString(direccion);
        dest.writeFloat(calificacion);
        dest.writeByte((byte) (habilitado == null ? 0 : habilitado ? 1 : 2));
        dest.writeString(password);
        dest.writeByte((byte) (celularValidado == null ? 0 : celularValidado ? 1 : 2));
        dest.writeByte((byte) (activo == null ? 0 : activo ? 1 : 2));
        dest.writeString(tipo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

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

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCelular() {
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

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
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

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

}
