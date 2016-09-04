package com.caecus.asistente.Entidades;

/**
 * Created by GonzaloMorales on 19/07/2016.
 */
public class Dispositivo {
    private String mail;
    private String imei;
    private String telefono;
    private String isCallEnabled;
    private String isGeoEnabled;
    private String isStrEnabled;


    public Dispositivo(String mail, String imei, String telefono, String isCallEnabled, String isGeoEnabled, String isStrEnabled) {
        this.mail = mail;
        this.imei = imei;
        this.telefono = telefono;
        this.isCallEnabled = isCallEnabled;
        this.isGeoEnabled = isGeoEnabled;
        this.isStrEnabled = isStrEnabled;
    }

    public Dispositivo() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIsCallEnabled() {
        return isCallEnabled;
    }

    public void setIsCallEnabled(String isCallEnabled) {
        this.isCallEnabled = isCallEnabled;
    }

    public String getIsGeoEnabled() {
        return isGeoEnabled;
    }

    public void setIsGeoEnabled(String isGeoEnabled) {
        this.isGeoEnabled = isGeoEnabled;
    }

    public String getIsStrEnabled() {
        return isStrEnabled;
    }

    public void setIsStrEnabled(String isStrEnabled) {
        this.isStrEnabled = isStrEnabled;
    }
}
