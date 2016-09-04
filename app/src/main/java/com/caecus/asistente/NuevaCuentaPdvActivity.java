package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.caecus.asistente.restApi.EndpointsApi;
import com.caecus.asistente.restApi.adapter.RestApiAdapter;
import com.caecus.asistente.restApi.model.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaCuentaPdvActivity extends AppCompatActivity {

    UserSessionManager session;
    EditText nombretxt;
    EditText contraseñatxt;
    EditText mailtxt;
    EditText repasstxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cuenta_pdv);
        nombretxt = (EditText) findViewById(R.id.txtnombre);
        contraseñatxt = (EditText) findViewById(R.id.txtpass);
        mailtxt = (EditText) findViewById(R.id.txtmail);
        repasstxt = (EditText) findViewById(R.id.txtrepass);
        session = new UserSessionManager(getApplicationContext());
    }


    public void crearCuenta(final View v) {


      /*  com.android.volley.Response.Listener<JSONObject> responseListener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // VolleyLog.v("Response: %n %s", response.getJSONObject("token"));
                try {
                    boolean success = response.getBoolean("success");

                    // if (token != null) {
                    if (success) {
                        Toast.makeText(getApplicationContext(),
                                "Cuentra PDV creada",
                                Toast.LENGTH_LONG).show();
                        // Add new Flag to start new Activity
                        finish();
                        Intent intent = new Intent(NuevaCuentaPDV.this, MenuPDV.class);
                        NuevaCuentaPDV.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NuevaCuentaPDV.this);
                        builder.setMessage("Error")
                                .setNegativeButton("Reintentar", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };
*/

        if (validar()) {
            Map<String, String> jsonBody;

            jsonBody = new HashMap<>();
            jsonBody.put("usernameHelper", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
            jsonBody.put("name", nombretxt.getText().toString());
            jsonBody.put("email", mailtxt.getText().toString());
            jsonBody.put("password", contraseñatxt.getText().toString());
            jsonBody.put("helper", "false");
            RestApiAdapter restApiAdapter = new RestApiAdapter();

            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
            Call<LoginResponse> loginResponseCall = endpointsApi.registerPDV(jsonBody);
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    boolean success = loginResponse.isSuccess();
                    if (success) {
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

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                }
            });


            //  RegisterPDVRequest registerPDVRequest = new RegisterPDVRequest(jsonBody, responseListener);
            // RequestQueue queue = Volley.newRequestQueue(NuevaCuentaPDV.this);
            // queue.add(registerPDVRequest);


        }

    }


    public boolean validar() {
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
