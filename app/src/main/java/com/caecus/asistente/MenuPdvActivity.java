package com.caecus.asistente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuPdvActivity extends AppCompatActivity {


    private ListView list;
    private String[] pdv = {"Juan", "Marcos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pdv);
        list = (ListView)findViewById(R.id.pdvList);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pdv);
        list.setAdapter(adaptador);


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
