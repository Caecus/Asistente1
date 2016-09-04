package com.caecus.asistente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuPdvActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pdv);
    }

    public void irNuevaCuentaPDV(View V)
    {
        Intent i = new Intent(this, NuevaCuentaPdvActivity.class);
        this.startActivity(i);

    }

    public void irEmparejarPDV(View V)
    {
        Intent i = new Intent(this, AsociarPdvActivity.class);
        this.startActivity(i);

    }

}
