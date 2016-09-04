package com.caecus.asistente.Entidades;

/**
 * Created by Alicia on 15/06/2016.
 */
public class Asistente {
    private String id;
    private String token;
    private String nombre;
    private String apellido;
    //private String telefono;
    private String mail;
    private String contraseña;
    private String esAsistente;


    public Asistente(String id, String token, String nombre, String apellido, String telefono, String mail, String contraseña, String esAsistente) {

        this.id = id;
        this.token = token;
        this.nombre = nombre;
        this.apellido = apellido;
      //  this.telefono = telefono;
        this.mail = mail;
        this.contraseña = contraseña;
        this.esAsistente= esAsistente;
    }

    public Asistente() {
    }

    public String getEsAsistente() {
        return esAsistente;
    }

    public void setEsAsistente(String esAsistente) {
        this.esAsistente = esAsistente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

  /*//  public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String numero) {
        this.telefono = telefono;
    }*/

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
