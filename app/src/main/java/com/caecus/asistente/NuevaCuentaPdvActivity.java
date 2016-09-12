package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.caecus.asistente.restApi.EndpointsApi;
import com.caecus.asistente.restApi.adapter.RestApiAdapter;
import com.caecus.asistente.restApi.model.TokenResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaCuentaPdvActivity extends AppCompatActivity {

    UserSessionManager session;
    EditText nombretxt;
    EditText contraseñatxt;
    EditText mailtxt;
    EditText repasstxt;
    private TextInputLayout til_nombre;
    private TextInputLayout til_pass;
    private TextInputLayout til_telefono;
    private TextInputLayout til_repass;
    private TextInputLayout til_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cuenta_pdv);
        nombretxt = (EditText) findViewById(R.id.txtnombre);
        contraseñatxt = (EditText) findViewById(R.id.txtpass);
        mailtxt = (EditText) findViewById(R.id.txtmail);
        repasstxt = (EditText) findViewById(R.id.txtrepass);
        session = new UserSessionManager(getApplicationContext());
        til_nombre = (TextInputLayout) findViewById(R.id.til_nombre);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_pass = (TextInputLayout) findViewById(R.id.til_contraseña);
        til_repass = (TextInputLayout) findViewById(R.id.til_recontraseña);
    }


    public void crearCuenta(final View v) {

        if (validar()) {
            Map<String, String> jsonBody;

            jsonBody = new HashMap<>();
            // jsonBody.put("usernameHelper", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
            jsonBody.put("caecusName", nombretxt.getText().toString());
            jsonBody.put("caecusEmail", mailtxt.getText().toString());
            jsonBody.put("caecusPassword", contraseñatxt.getText().toString());
            // jsonBody.put("helper", "false");
            RestApiAdapter restApiAdapter = new RestApiAdapter();

            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
            Call<TokenResponse> tokenResponseCall = endpointsApi.registerPDV(jsonBody);

            tokenResponseCall.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    try {

                        Log.e("Error", response.toString());
                        TokenResponse tokenResponse;
                        if (response.code() == 200) {
                            tokenResponse = response.body();
                        } else {
                            Gson gson = new Gson();
                            tokenResponse = gson.fromJson(response.errorBody().string(), TokenResponse.class);
                        }

                        long result = tokenResponse.getResult();
                        if (response.body() == null) {
                            Log.d("token", "sin respuesta");
                            // return;
                        }


                        if (result > 0) {
                            Toast.makeText(getApplicationContext(),
                                    "Cuentra PDV creada",
                                    Toast.LENGTH_LONG).show();
                            // Add new Flag to start new Activity
                            finish();
                            Intent intent = new Intent(NuevaCuentaPdvActivity.this, MenuPdvActivity.class);
                            NuevaCuentaPdvActivity.this.startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NuevaCuentaPdvActivity.this);
                            builder.setMessage("Error")
                                    .setNegativeButton("Reintentar", null)
                                    .create()
                                    .show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Log.e("Error", "Probando de nuevo");
                }
            });
        }
    }


    public boolean validar() {

        boolean s = true;

        if (nombretxt.getText().toString().equals("")) {
            til_nombre.setError("Campo vacío");
            s = false;
        } else {
            til_nombre.setError(null);
        }

        if (mailtxt.getText().toString().equals("")) {
            til_email.setError("Campo vacío");
            s = false;
        } else {
            if (!isEmailValid(mailtxt.getText().toString())) {
                til_email.setError("Formato inválido");
                s = false;

            } else
                til_email.setError(null);
        }
        if (contraseñatxt.getText().toString().equals("")) {
            til_pass.setError("Campo vacío");
            s = false;
        } else {
            til_pass.setError(null);
        }
        if (!contraseñatxt.getText().toString().equals(repasstxt.getText().toString())) {
            til_repass.setError("Contraseña no coincide");
            s = false;
        } else {
            til_repass.setError(null);
        }
        return s;

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
