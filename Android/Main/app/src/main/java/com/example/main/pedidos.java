package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class pedidos extends AppCompatActivity {
    Button bot_catalogo;
    Button bot_confirmar;
    TextView tv_partner;
    TextView tv_comercial;
    TextView tv_id;
    EditText et_nombrePartner;
    EditText et_nombreComercial;
    ListView listaProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //Vistas
        bot_catalogo = (Button) findViewById(R.id.btn_Catalogo);
        listaProd = (ListView) findViewById(R.id.lv_productos);
        bot_confirmar = (Button) findViewById(R.id.btn_confirmar);
        tv_partner = (TextView) findViewById(R.id.tv_titpartner);
        tv_comercial = (TextView) findViewById(R.id.tv_comercial);
        tv_id = (TextView) findViewById(R.id.tv_idpresup);
        et_nombrePartner = (EditText) findViewById(R.id.et_partner);
        et_nombreComercial = (EditText) findViewById(R.id.et_comercial);
        TabHost tabs = findViewById(R.id.tabhost1);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tv_titpartner);
        spec.setContent(R.id.et_partner);
        spec.setContent(R.id.tv_comercial);
        spec.setContent(R.id.et_comercial);
        spec.setContent(R.id.tv_idpresup);
        spec.setContent(R.id.lv_productos);
        spec.setContent(R.id.btn_confirmar);
        spec.setIndicator("Nuevo Pedido");
        tabs.addTab(spec);

        TabHost.TabSpec spec2 = tabs.newTabSpec("mitab2");
        spec2.setContent(R.id.textVw);
        spec2.setIndicator("Pedidos Existentes");
        tabs.addTab(spec2);

        Intent intentCatalogo = new Intent(this, Catalogo.class);

        bot_catalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentCatalogo);
            }
        });

    }
}