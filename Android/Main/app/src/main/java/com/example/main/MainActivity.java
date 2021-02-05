package com.example.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    Button bot_partners;
    Button bot_citas;
    Button bot_pedidos;
    Button bot_envios;
    ImageButton bot_mapa;
    ImageButton bot_correo;
    ImageButton bot_llamada;
    TextView telf, mail;
    Spinner spin_delegaciones;
    static final int REQUEST_CODE = 123;
    String email;
    String asunto;
    String mensaje;

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
        mail = (TextView) findViewById(R.id.TxEmail);
        spin_delegaciones = (Spinner) findViewById(R.id.spnDelegaciones);

        //SQL: creación de tablas
        tablasSQLHelper miDb = new tablasSQLHelper(this, "DBDraft", null, 1);
        SQLiteDatabase db = miDb.getWritableDatabase();


        //Intents para moverse por la app
        Intent intentPartner = new Intent(this, Partners.class);
        Intent intentCitas = new Intent(this, Citas.class);
        Intent intentEnvios = new Intent(this, Envios.class);
        Intent intentPedidos = new Intent(this, Pedidos.class);
        Intent intentMaps = new Intent(this, Mapa.class);

        pidePermisos();

        final String[] datos = new String[]{"Gipuzkoa", "Bizkaia", "Araba", "Malaga", "Madrid", "Barcelona"};
        final String[] telefonos = new String[]{"943123456", "946881171", "945007660", "951926010", "915094134", "932075839"};
        final String[] correos = new String[]{"draftgipuzkoa@draft.com", "draftbizkaia@draft.com", "draftaraba@draft.com", "draftmalaga@draft.com", "draftmadrid@draft.com", "draftbarcelona@draft.com"};

        //Elemento ArrayAdapter, que permite coger un Array como fuente de información
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos);

        //Creamos nuestro Spinner
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_delegaciones.setAdapter(adaptador);

        spin_delegaciones.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        mail.setText(correos[position]);
                        telf.setText(telefonos[position]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

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
                enviaMail();
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

    public void enviaMail() {
        try {
            email = "mail@draft.com";
            asunto = "Asunto mail";
            mensaje = "Hola buenos dias";
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, asunto);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (Throwable t) {
            Toast.makeText(this, "Ha ocurrido un error, vuelve a intentarlo: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }
}