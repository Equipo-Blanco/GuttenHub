package com.example.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button bot_partners;
    Button bot_citas;
    Button bot_pedidos;
    Button bot_envios;
    ImageButton bot_mapa;
    ImageButton bot_correo;
    ImageButton bot_llamada;
    TextView telf;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Vistas a controlar por código
        bot_partners = (Button) findViewById(R.id.BtnPartners);
        bot_citas = (Button) findViewById(R.id.BtnCitas);
        bot_envios = (Button) findViewById(R.id.BtnEnvios);
        bot_pedidos = (Button) findViewById(R.id.BtnPedidos);
        bot_mapa = (ImageButton) findViewById(R.id.BtnMapa);
        bot_correo = (ImageButton) findViewById(R.id.BtnCorreo);
        bot_llamada = (ImageButton) findViewById(R.id.BtnLlamar);
        telf = (TextView) findViewById(R.id.TxTel);

        //Intents para moverse por la app
        Intent intentPartner = new Intent(this, partners.class);
        Intent intentCitas = new Intent(this, citas.class);
        Intent intentEnvios = new Intent(this, envios.class);
        Intent intentPedidos = new Intent(this, pedidos.class);
        Intent intentMaps = new Intent(this, mapa.class);

        bot_llamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                int num = Integer.parseInt(telf.getText().toString());
                intent.setData(Uri.parse("tel:" + num));
                startActivity(intent);
            }
        });


        bot_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") ||
                            info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                if (best != null)
                    intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(intent);
            }
        });

        bot_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentMaps);
            }
        });

        bot_partners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentPartner);
            }
        });

        bot_envios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentEnvios);
            }
        });

        bot_citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentCitas);
            }
        });

        bot_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentPedidos);
            }
        });
    }
}