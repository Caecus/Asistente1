package com.caecus.asistente.restApi.model;

/**
 * Created by GonzaloMorales on 29/08/2016.
 */
public class LoginResponse {

    String token;
    boolean success;

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
