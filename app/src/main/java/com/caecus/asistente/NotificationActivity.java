package com.caecus.asistente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NotificationActivity extends AppCompatActivity {

    int cod;
    String name;
    String ayudante;
    double lat;
    double lng;
    String tel;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        session = new UserSessionManager(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            Intent i = getIntent();
            Bundle extras = i.getExtras();
            cod = extras.getInt("COD");
            name = extras.getString("NAME");
            ayudante = extras.getString("AYUDANTE");
            tel = extras.getString("TEL");
            lat = extras.getDouble("LAT");
            lng = extras.getDouble("LNG");

            if (cod == 1) {
                Alerta(name);
            }
           // if (cod == 2) {
             //   avisoAsistencia(name, ayudante);

            //}


        } catch (Exception e) {
        }






    }


    public void Alerta(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
        builder.setMessage("¿Puede ayudar a " + name + " ?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        declinarAviso();
                    }})
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        aceptAviso();
                    }})
                .create()
                .show();
    }

    public void declinarAviso()
    {
        session.changeState(session.STATE_AVAILABLE);
        Toast.makeText(getApplicationContext(),
                "Peticion declinada",
                Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(NotificationActivity.this, MenuAsistenteActivity.class);
        NotificationActivity.this.startActivity(intent);
    }

    public void aceptAviso()
    {
        session.changeState(session.STATE_INPROGRESS);
        finish();

        Intent i = new Intent(NotificationActivity.this, AvisoActivity.class);
        i.putExtra("TEL", tel);
        i.putExtra("LAT", lat);
        i.putExtra("LNG", lng);
        i.putExtra("NAME", name);
        NotificationActivity.this.startActivity(i);
    }







}
