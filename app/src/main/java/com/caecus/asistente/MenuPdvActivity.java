package com.caecus.asistente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuPdvActivity extends AppCompatActivity {

    ListView pdvList;
    ArrayAdapter<String> pdvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pdv);

    }

    /*  @Override
      public View onCreateView(String name, Context context, AttributeSet attrs) {
          return super.onCreateView(name, context, attrs);
          pdvList = (ListView) findViewById(R.id.pdvList);
          String[] pdvNames = {
                  "Marcos Rogriguez",
                  "Juan Panzetta"
          };
          pdvAdapter = new ArrayAdapter(
                  getActivity(),
                  android.R.layout.simple_list_item_1,
                  pdvNames);

      }
  */
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
