package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.caecus.asistente.restApi.adapter.RestApiAdapter;
import com.caecus.asistente.restApi.model.TokenResponse;
import com.caecus.asistente.restApi.EndpointsApi;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaCuentaAsistenteActivity extends AppCompatActivity {

    UserSessionManager session;
    EditText nombretxt;
    EditText contraseñatxt;
    EditText telefonotxt;
    EditText mailtxt;
    EditText repasstxt;
    Button registrarbtn;

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
    }


    public void crearCuenta(final View v) {

        if (validar()) {
            Map<String, String> jsonBody;
            jsonBody = new HashMap<>();
            jsonBody.put("name", nombretxt.getText().toString());
            jsonBody.put("email", mailtxt.getText().toString());
            jsonBody.put("password", contraseñatxt.getText().toString());
            jsonBody.put("helper", "true");
            jsonBody.put("gcmToken", FirebaseInstanceId.getInstance().getToken());
            Log.e("FDf", FirebaseInstanceId.getInstance().getToken());
            jsonBody.put("imei", getIMEI());
            Log.e("Cuenta", getIMEI());
            jsonBody.put("noPhone", telefonotxt.getText().toString());
            Log.e("Cuenta", telefonotxt.getText().toString());
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
                        String token = tokenResponse.getMessage();
                        Log.d("token", token);
                        // if (token != null) {
                        if (token != null) {
                            Toast.makeText(getApplicationContext(),
                                    "Usuario Creado",
                                    Toast.LENGTH_LONG).show();


                            session.createUserLoginSession(token);


                            // Add new Flag to start new Activity


                            finish();
                            Intent intent = new Intent(NuevaCuentaAsistenteActivity.this, MenuAsistenteActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                            intent.putExtra("token", token);
                            //intent.putExtra("apellido", apellido);
                            // intent.putExtra("email", email);
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

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String imei = tm.getDeviceId();
        return imei;
    }

    public boolean validar() {
        //boolean ban = true;
        if (nombretxt.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Nombre vacío",
                    Toast.LENGTH_LONG).show();
            return false;
        }


        if (mailtxt.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Email vacío",
                    Toast.LENGTH_LONG).show();
            //ban= false;
            return false;
        }
        if (telefonotxt.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(),
                    "Telefono vacia",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (contraseñatxt.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Contraseña vacía",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (!contraseñatxt.getText().toString().equals(repasstxt.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "Contraseña no coincide",
                    Toast.LENGTH_LONG).show();
            return false;
        }


        return true;

    }
}
