package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.caecus.asistente.restApi.EndpointsApi;
import com.caecus.asistente.restApi.adapter.RestApiAdapter;
import com.caecus.asistente.restApi.model.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsociarPdvActivity extends AppCompatActivity {


    EditText txtmail;
    EditText txtpin;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asociar_pdv);
        txtmail = (EditText) findViewById(R.id.txtmail);
        txtpin= (EditText) findViewById(R.id.txtpin);
        session = new UserSessionManager(getApplicationContext());

    }

    public void Emparejar (View view)
    {

        final String email = txtmail.getText().toString();
        final String pin = txtpin.getText().toString();

        if(email.trim().length() > 0 && pin.trim().length() > 0) {
            Map<String, String> jsonBody;
            jsonBody = new HashMap<>();
            jsonBody.put("PDV_email", email.toString());
            jsonBody.put("token", pin.toString());
            jsonBody.put("helper_email", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
            RestApiAdapter restApiAdapter = new RestApiAdapter();

            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
            Call<LoginResponse> loginResponseCall = endpointsApi.emparejarPDV(jsonBody);
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    boolean success = loginResponse.isSuccess();
                    if (success) {
                        Toast.makeText(getApplicationContext(),
                                "PDV asociado",
                                Toast.LENGTH_LONG).show();
                        // Add new Flag to start new Activity
                        finish();
                        Intent intent = new Intent(AsociarPdvActivity.this, MenuPdvActivity.class);
                        AsociarPdvActivity.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AsociarPdvActivity.this);
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
        }
        else{

            // user didn't entered username or password
            Toast.makeText(getApplicationContext(),
                    "Complete los datos",
                    Toast.LENGTH_LONG).show();
        }

    }
}
