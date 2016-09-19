package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

public class AsociarPdvActivity extends AppCompatActivity {


    EditText txtmail;
    EditText txtpin;
    UserSessionManager session;
    TextInputLayout tilEmailPDV, tilPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asociar_pdv);
        txtmail = (EditText) findViewById(R.id.txtmail);
        txtpin = (EditText) findViewById(R.id.txtpin);
        tilEmailPDV = (TextInputLayout) findViewById(R.id.til_emailPDV);
        tilPin = (TextInputLayout) findViewById(R.id.til_pin);
        session = new UserSessionManager(getApplicationContext());

    }

    public void Emparejar(View view) {


        String email = txtmail.getText().toString();
        String pin = txtpin.getText().toString();
        if (validar(email, pin)) {
            Map<String, String> jsonBody;
            jsonBody = new HashMap<>();
            jsonBody.put("PDV_email", email.toString());
            jsonBody.put("token", pin.toString());
            jsonBody.put("helper_email", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
            RestApiAdapter restApiAdapter = new RestApiAdapter();

            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
            Call<TokenResponse> tokenResponseCall = endpointsApi.emparejarPDV(jsonBody);
            tokenResponseCall.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                    try {
                        TokenResponse tokenResponse;
                        if (response.code() == 200) {
                            tokenResponse = response.body();
                        } else {
                            Gson gson = new Gson();
                            tokenResponse = gson.fromJson(response.errorBody().string(), TokenResponse.class);
                        }
                        long result = tokenResponse.getResult();

                        if (result == 0) {
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AsociarPdvActivity.this);
                    builder.setMessage("Imposible conectar con el servidor")
                            .setNegativeButton("Ok", null)
                            .create()
                            .show();

                }
            });
        } else {

            // user didn't entered username or password
            Toast.makeText(getApplicationContext(),
                    "Complete los datos",
                    Toast.LENGTH_LONG).show();
        }

    }

    public boolean validar(String email, String pin)

    {

        boolean s = true;

        if (email.trim().length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = email;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                tilEmailPDV.setError(null);
            } else {
                tilEmailPDV.setError("Formato inválido");
                s = false;
            }
        } else {
            tilEmailPDV.setError("Campo vacío");
            s = false;
        }

        if (pin.trim().length() > 0) {
            tilPin.setError(null);

        } else {
            s = false;
            tilPin.setError("Campo vacío");
        }
        return s;


    }
}
