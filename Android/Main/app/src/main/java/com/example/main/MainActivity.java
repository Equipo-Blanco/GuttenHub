package com.example.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    static final int REQUEST_CODE = 123;

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

        pidePermisos();

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

    public void pidePermisos() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            //Permiso sin conceder
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Crear Dialogo de Alerta
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setTitle("Conceder permisos:");
                builder.setMessage("Leer y Escribir Archivos");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                }, REQUEST_CODE
                        );
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, REQUEST_CODE
                );
            }
        } else {
            //Cuando los permisos ya están concedidos
            //Toast.makeText(getApplicationContext(), "Ya tienes permisos", Toast.LENGTH_SHORT).show();
        }
    }
}