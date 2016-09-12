package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.caecus.asistente.restApi.EndpointsApi;
import com.caecus.asistente.restApi.adapter.RestApiAdapter;
import com.caecus.asistente.restApi.model.TokenResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAsistenteActivity extends AppCompatActivity {

    UserSessionManager session;
    Button btnLogout;
    int cod;
    String name;
    String ayudante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu_asistente);
        session = new UserSessionManager(getApplicationContext());
        btnLogout = (Button) findViewById(R.id.btnLogout);


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name
        // String name = user.get(UserSessionManager.KEY_NAME);

        // get email
        Log.e("Error", user.get(UserSessionManager.KEY_TOKEN));

        //    username.setText(token);

        if (session.checkLogin())
            finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            Intent i = getIntent();
            Bundle extras = i.getExtras();
            cod = extras.getInt("COD");
            name = extras.getString("NAME");
            if (cod == 1) {
                Alerta(name);
            }
            if (cod == 2) {
                ayudante = extras.getString("AYUDANTE");
                avisoAsistencia(name, ayudante);

            }
        } catch (Exception e) {
        }

    }

    public void logout(View v) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<TokenResponse> tokenResponseCall = endpointsApi.logout(session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
        tokenResponseCall.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                TokenResponse tokenResponse = response.body();
                long result = tokenResponse.getResult();

                if (result == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Sesion cerrada",
                            Toast.LENGTH_LONG).show();
                    session.logoutUser();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuAsistenteActivity.this);
                    builder.setMessage("Error")
                            .setNegativeButton("Reintentar", null)
                            .create()
                            .show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });


    }

    public void irAMenuPDV(View V) {
        Intent i = new Intent(this, MenuPdvActivity.class);
        this.startActivity(i);

    }

    public void irAAvisos(View V) {
        Intent i = new Intent(this, AvisoActivity.class);
        this.startActivity(i);

    }

    public void Alerta(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAsistenteActivity.this);
        builder.setMessage("¿Puede ayudar a " + name + " ?")
                .setNegativeButton("No", null)
                .setPositiveButton("Si", null)
                .create()
                .show();
    }

    public void avisoAsistencia(String name, String ayudante) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAsistenteActivity.this);
        builder.setMessage(name + " ha sido asistido por " + ayudante)
                .setPositiveButton("OK", null)
                .create()
                .show();

    }



}
