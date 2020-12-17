package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TabHost;

public class pedidos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        TabHost tabs = findViewById(R.id.tabhost1);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.textView1);
        spec.setIndicator("Nuevo Pedido");
        tabs.addTab(spec);

        TabHost.TabSpec spec2 = tabs.newTabSpec("mitab2");
        spec2.setContent(R.id.textVw);
        spec2.setIndicator("Pedidos Existentes");
        tabs.addTab(spec2);


    }
}