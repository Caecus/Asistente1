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
import android.widget.TextView;
import android.widget.Toast;

import com.caecus.asistente.restApi.EndpointsApi;
import com.caecus.asistente.restApi.adapter.RestApiAdapter;
import com.caecus.asistente.restApi.model.TokenResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    UserSessionManager session;
    private TextView usuario;
    private TextView pass;
    static private TextInputLayout tilEmail;
    private TextInputLayout tilContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (TextView) findViewById(R.id.txtuser);
        pass = (TextView) findViewById(R.id.txtpass);
        tilEmail = (TextInputLayout) findViewById(R.id.til_usuario);
        tilContraseña = (TextInputLayout) findViewById(R.id.til_contraseña);
        session = new UserSessionManager(getApplicationContext());

    }


    public void Login(View view) {
        String email = usuario.getText().toString();
        String password = pass.getText().toString();
        if (validar(email, password)) {
            Map<String, String> jsonBody;
            jsonBody = new HashMap<>();
            jsonBody.put("account", usuario.getText().toString());
            jsonBody.put("password", pass.getText().toString());
            jsonBody.put("imei", getIMEI());
           // Log.e("FCM", FirebaseInstanceId.getInstance().getId());
            jsonBody.put("tokenPush", FirebaseInstanceId.getInstance().getId());

            RestApiAdapter restApiAdapter = new RestApiAdapter();
            EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
            Call<TokenResponse> tokenResponseCall = endpointsApi.login(jsonBody);
            tokenResponseCall.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    try {
                        TokenResponse tokenResponse;
                        if (response.code() == 200) {
                            tokenResponse = response.body();
                        } else {
                            Gson gson = new Gson();
                            //Log.e("error", response.errorBody().string());
                            tokenResponse = gson.fromJson(response.errorBody().string(), TokenResponse.class);
                        }
                        long result = tokenResponse.getResult();
                        String token = tokenResponse.getToken();
                        //Tiene que ser mayor?
                        if (result > 0) {
                            Toast.makeText(getApplicationContext(),
                                    "Sesion iniciada",
                                    Toast.LENGTH_LONG).show();
                            session.createUserLoginSession(token);
                            // Add new Flag to start new Activity
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MenuAsistenteActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            LoginActivity.this.startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("Usuario/Contraseña incorrectos")
                                    .setNegativeButton("Reintentar", null)
                                    .create()
                                    .show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Imposible conectar con el servidor")
                            .setNegativeButton("Ok", null)
                            .create()
                            .show();

                }
            });
        }
    }

    public boolean validar(String email, String contraseña)

    {
        boolean s = true;

        if (email.trim().length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = email;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                tilEmail.setError(null);
            } else {
                tilEmail.setError("Formato inválido");
                s = false;
            }
        } else {
            tilEmail.setError("Campo vacío");
            s = false;
        }

        if (contraseña.trim().length() > 0) {
            tilContraseña.setError(null);

        } else {
            s = false;
            tilContraseña.setError("Campo vacío");
        }
        return s;


    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            tilEmail.setError(null);
            return true;
        } else {
            tilEmail.setError("Email inválido");
            return false;
        }
    }

    public void irNuevaCuenta(View V) {
        Intent i = new Intent(this, NuevaCuentaAsistenteActivity.class);
        this.startActivity(i);
    }

    public String getIMEI() {

        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception E) {
            return "Imei no encontrado";
        }

    }

}
