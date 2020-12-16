package com.example.main;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import static android.content.Intent.ACTION_CREATE_DOCUMENT;

public class NuevaCita extends AppCompatActivity {

    private TextView tituloCita;
    private EditText et_titulo;
    private EditText et_descripcion;
    private Button bot_guardar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cita);

        tituloCita = (TextView) findViewById(R.id.tv_titulo);
        et_titulo = (EditText) findViewById(R.id.et_titulo);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        bot_guardar = (Button) findViewById(R.id.BtnGuardaCita);

        Bundle extras = getIntent().getExtras();
        String fecha = extras.getString("fechacita");

        tituloCita.setText("Cita para el día " + fecha);


        bot_guardar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String titulo;
                String descrip;
                //Comprueba y valida si se ha introducido alguna información en el titulo
                //si está vacío pone un texto por defecto.
                if (et_titulo.getText().length() > 0) {
                    titulo = et_titulo.getText().toString();
                } else {
                    titulo = "Sin titulo";
                }
                //Comprueba y valida si se ha introducido alguna información en descripción
                //si está vacío pone un texto por defecto.
                if (et_descripcion.getText().length() > 0) {
                    descrip = et_descripcion.getText().toString();
                } else {
                    descrip = "Sin descripción";
                }

                File newxmlfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/citas/KheArpehta.xml");

                if (!newxmlfile.exists()) {
                    newxmlfile.getParentFile().mkdir();
                }
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {
                    Log.e("IOException", "Exception in create new File(");
                }

                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile, true);

                } catch (FileNotFoundException e) {
                    Log.e("FileNotFoundException", e.toString());
                }
                XmlSerializer serializer = Xml.newSerializer();

                try {
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                    serializer.startTag(null, "citas");

                    serializer.startTag(null, "cita");

                    serializer.startTag(null, "fecha");
                    serializer.text(fecha);
                    //serializer.attribute(null, "attribute", "value");
                    serializer.endTag(null, "fecha");

                    serializer.startTag(null, "titulo");
                    serializer.text(titulo);
                    serializer.endTag(null, "titulo");

                    serializer.startTag(null, "descrip");
                    serializer.text(descrip);
                    serializer.endTag(null, "descrip");

                    serializer.endTag(null, "cita");
                    serializer.endTag(null, "citas");
                    serializer.endDocument();
                    serializer.flush();
                    fileos.close();
                    //TextView tv = (TextView)findViewById(R.);

                } catch (Exception e) {
                    Log.e("Exception", "Exception occured in writing");
                }
            }
        });
    }
}