package com.example.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
                try {
                    guardaCita(fecha);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void guardaCita(String _fecha) throws Exception {
        String titulo = et_titulo.getText().toString();
        String descripcion = et_descripcion.getText().toString();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element elemRaiz = doc.createElement("citas");
        doc.appendChild(elemRaiz);

        Element elemDia = doc.createElement("cita");
        elemRaiz.appendChild(elemDia);

        Element elemFecha = doc.createElement("fecha");
        elemFecha.appendChild(doc.createTextNode(_fecha));
        elemDia.appendChild(elemFecha);

        Element elemTitulo = doc.createElement("titulo");
        elemTitulo.appendChild(doc.createTextNode(titulo));
        elemDia.appendChild(elemTitulo);

        Element elemDescrip = doc.createElement("descripcion");
        elemDescrip.appendChild(doc.createTextNode(descripcion));
        elemDia.appendChild(elemDescrip);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        Files.createFile(Paths.get(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DRAFT"));
        StreamResult result = new StreamResult(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "DRAFT" + "/" + "citas1.xml"));

        transformer.transform(source, result);
    }

}