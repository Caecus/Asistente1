package com.caecus.asistente;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.caecus.asistente.Entidades.Aviso;
import com.caecus.asistente.restApi.EndpointsApi;
import com.caecus.asistente.restApi.adapter.RestApiAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class AvisoActivity extends AppCompatActivity {


    TextView mensaje1;
    TextView mensaje2;
    TextView nombre;
    double lat;
    double lng;
    Button llamar;
    Aviso aviso;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);
        UserSessionManager session = new UserSessionManager(getApplicationContext());
        nombre = (TextView) findViewById(R.id.nombre);
        mensaje1 = (TextView) findViewById(R.id.mensaje_id);
        mensaje2 = (TextView) findViewById(R.id.mensaje_id2);
        llamar = (Button) findViewById(R.id.btnLlamar);
        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(aviso.getTelefono());
            }
        });
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        try {
            lat = extras.getDouble("LAT");
            lng = extras.getDouble("LNG");

        }
        catch (Exception e){}


//        nombre.setText(aviso.getNombre());
        mensaje2.setText(obtenerDireccion(lat, lng));

    }

    public void call(int n) {
        String s = "tel:" + n + "";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(s));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }


    public String obtenerDireccion(double lat, double lng) {
        String s = "No disponible aca";
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



}
