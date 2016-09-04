package com.caecus.asistente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.caecus.asistente.firebase.NotificacionIDTokenService;

import java.util.HashMap;

public class MenuAsistenteActivity extends AppCompatActivity {

    UserSessionManager session;
    Button btnLogout;
    TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_asistente);
        session = new UserSessionManager(getApplicationContext());
        btnLogout = (Button) findViewById(R.id.btnLogout);
        username = (TextView) findViewById(R.id.txtxUserName);





        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name
        // String name = user.get(UserSessionManager.KEY_NAME);

        // get email
        String token = user.get(UserSessionManager.KEY_TOKEN);
        if (token != null)
            username.setText(token);

        if (session.checkLogin())
            finish();


        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(),
                        "Sesion cerrada",
                        Toast.LENGTH_LONG).show();
                // Clear the User session data
                // and redirect user to LoginActivity
                session.logoutUser();
            }
        });
    }




    public void irAMenuPDV(View V)
    {
        Intent i = new Intent(this, MenuPdvActivity.class);
        this.startActivity(i);

    }

    public void irAAvisos(View V)
    {
        Intent i = new Intent(this, AvisoActivity.class);
        this.startActivity(i);

    }
}
