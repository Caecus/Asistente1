package com.caecus.asistente;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AvisoActivity extends AppCompatActivity {


    TextView mensaje2;
    TextView nombre;
    double lat;
    double lng;
    String telefono;
    Button llamar;
    int cod;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);
        UserSessionManager session = new UserSessionManager(getApplicationContext());
        nombre = (TextView) findViewById(R.id.txtnombre);
        mensaje2 = (TextView) findViewById(R.id.txtDireccion);
        llamar = (Button) findViewById(R.id.btnLlamar);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        try {
            lat = extras.getDouble("LAT");
            lng = extras.getDouble("LNG");
            telefono = extras.getString("TEL");
            name = extras.getString("NAME");

        }
        catch (Exception e){}
        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(telefono);
            }
        });


        nombre.setText(name);
        mensaje2.setText(obtenerDireccion(lat, lng));

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
        } catch (Exception e) {
        }

    }

    public void call(String n) {
        String s = "tel:" + n + "";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(s));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }


    public String obtenerDireccion(double lat, double lng) {
        String s = "No disponible";
        if (lat != 0.0 && lng != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        lat, lng, 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    s = "Se encuentra en: \n" + DirCalle.getAddressLine(0) + "\n" + DirCalle.getAddressLine(1) + ", " + DirCalle.getAddressLine(2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;

    }

    public void verMapa(View view) {
        Intent intent = new Intent(this, MapaUbicacionActivity.class);
        intent.putExtra("EXTRA_LAT", lat);
        intent.putExtra("EXTRA_LNG", lng);
        startActivity(intent);
    }

    public void Alerta(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AvisoActivity.this);
        builder.setMessage("Puede ayudar a" + name)
                .setNegativeButton("No", null)
                .setPositiveButton("Si", null)
                .create()
                .show();
    }



}
