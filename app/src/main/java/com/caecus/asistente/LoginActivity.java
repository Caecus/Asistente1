package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caecus.asistente.restApi.EndpointsApi;
import com.caecus.asistente.restApi.adapter.RestApiAdapter;
import com.caecus.asistente.restApi.model.TokenResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    UserSessionManager session;
    private TextView usuario;
    private TextView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (TextView) findViewById(R.id.txtuser);
        pass = (TextView) findViewById(R.id.txtpass);
        session = new UserSessionManager(getApplicationContext());
    }

    public void Login(View view) {
        String email = usuario.getText().toString();
        String password = pass.getText().toString();
        if (email.trim().length() > 0 && password.trim().length() > 0) {
            Map<String, String> jsonBody;
            jsonBody = new HashMap<>();
            jsonBody.put("account", usuario.getText().toString());
            jsonBody.put("password", pass.getText().toString());
            RestApiAdapter restApiAdapter = new RestApiAdapter();

            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
            Call<TokenResponse> tokenResponseCall = endpointsApi.login(jsonBody);
            tokenResponseCall.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    Log.d("respuesta", "si");
                    TokenResponse tokenResponse = response.body();
                    Log.d("respuesta", tokenResponse.toString());
                    //JSONObject jsonResponse = response;
                    // boolean success = response.getBoolean("success");
                    String token = tokenResponse.getToken();

                    //boolean success = loginResponse.isSuccess();
                    //  String token = loginResponse.getToken();
                    // Log.d("token", token);

                    if (token != null) {
                        // String token = loginResponse.getToken();
                        //if (success) {
                        Toast.makeText(getApplicationContext(),
                                "Sesion iniciada",
                                Toast.LENGTH_LONG).show();

                        // String nombre = response.getString("nombre");
                        // String token = response.getString("token");
                        // String email = response.getString("email");
                        session.createUserLoginSession(token);
                        // Add new Flag to start new Activity
                        finish();
                        Intent intent = new Intent(LoginActivity.this, MenuAsistenteActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("token", token);
                        LoginActivity.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Usuario/Contraseña incorrectos")
                                .setNegativeButton("Reintentar", null)
                                .create()
                                .show();
                    }

                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(),
                    "Ingrese email y contraseña",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void irNuevaCuenta(View V) {
        Intent i = new Intent(this, NuevaCuentaAsistenteActivity.class);
        this.startActivity(i);
    }
}
