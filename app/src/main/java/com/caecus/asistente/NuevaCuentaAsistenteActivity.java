package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caecus.asistente.restApi.EndpointsApi;
import com.caecus.asistente.restApi.adapter.RestApiAdapter;
import com.caecus.asistente.restApi.model.TokenResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaCuentaAsistenteActivity extends AppCompatActivity {

    private UserSessionManager session;
    private EditText nombretxt;
    private EditText contraseñatxt;
    private EditText telefonotxt;
    private EditText mailtxt;
    private EditText repasstxt;
    private Button registrarbtn;
    private TextInputLayout til_nombre;
    private TextInputLayout til_pass;
    private TextInputLayout til_telefono;
    private TextInputLayout til_repass;
    private TextInputLayout til_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cuenta);
        nombretxt = (EditText) findViewById(R.id.txtnombre);
        contraseñatxt = (EditText) findViewById(R.id.txtpass);
        telefonotxt = (EditText) findViewById(R.id.txttelefono);
        mailtxt = (EditText) findViewById(R.id.txtmail);
        registrarbtn = (Button) findViewById(R.id.btnRegistrar);
        repasstxt = (EditText) findViewById(R.id.txtrepass);
        session = new UserSessionManager(getApplicationContext());
        til_nombre = (TextInputLayout) findViewById(R.id.til_nombre);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_telefono = (TextInputLayout) findViewById(R.id.til_telefono);
        til_pass = (TextInputLayout) findViewById(R.id.til_contraseña);
        til_repass = (TextInputLayout) findViewById(R.id.til_recontraseña);
    }

    public void crearCuenta(final View v) {

        if (validar()) {
            Map<String, String> jsonBody;
            jsonBody = new HashMap<>();
            jsonBody.put("name", nombretxt.getText().toString());
            jsonBody.put("email", mailtxt.getText().toString());
            jsonBody.put("password", contraseñatxt.getText().toString());
            jsonBody.put("helper", "true");
            Log.e("Token", FirebaseInstanceId.getInstance().getToken());
            jsonBody.put("gcmToken", FirebaseInstanceId.getInstance().getToken());
            jsonBody.put("imei", getIMEI());
            jsonBody.put("noPhone", telefonotxt.getText().toString());
            jsonBody.put("callEnable", "true");
            jsonBody.put("strEnable", "true");
            jsonBody.put("geoEnable", "true");
            jsonBody.put("description", "desc");
            RestApiAdapter restApiAdapter = new RestApiAdapter();
            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
            Call<TokenResponse> tokenResponseCall = endpointsApi.register(jsonBody);
            tokenResponseCall.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    try {
                        if (response.body() == null) {
                            Log.d("token", "sin respuesta");
                            return;
                        }
                        TokenResponse tokenResponse = response.body();
                        long result = tokenResponse.getResult();
                        if (result > 0) {
                            String token = tokenResponse.getToken();
                            Toast.makeText(getApplicationContext(),
                                    "Usuario Creado",
                                    Toast.LENGTH_LONG).show();
                            session.createUserLoginSession(token);
                            // Add new Flag to start new Activity

                            finish();
                            Intent intent = new Intent(NuevaCuentaAsistenteActivity.this, MenuAsistenteActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            NuevaCuentaAsistenteActivity.this.startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NuevaCuentaAsistenteActivity.this);
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

                }
            });
        }
    }


    public String getIMEI() {

        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception E) {
            return "Imei no encontrado";
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

        if (telefonotxt.getText().toString().equals("")) {
            til_telefono.setError("Campo vacío");
            s = false;
        } else {
            til_telefono.setError(null);
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
