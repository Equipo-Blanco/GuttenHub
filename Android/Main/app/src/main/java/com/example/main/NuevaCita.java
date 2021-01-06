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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
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
                // File newxmlfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Draft/citasGuardadas.xml");
                File newxmlfile = new File(Environment.getExternalStorageDirectory() + "/Draft/citasGuardadas.xml");
                XmlSerializer serializer = Xml.newSerializer();
                FileOutputStream fileos = null;

                //Necesario para el DOM:
                // String filePath = "/storage/emulated/0/Draft/citasGuardadas.xml";
                String filePath = Environment.getExternalStorageDirectory() + "/Draft/citasGuardadas.xml";
                System.out.println(filePath);
                //File xmlFile = new File(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder;

                if (!newxmlfile.exists()) {
                    newxmlfile.getParentFile().mkdir();
                    System.out.println("CREANDO FILE **************************************************>");

                    try {
                        newxmlfile.createNewFile();
                        serializer.setOutput(fileos, "UTF-8");
                        serializer.startDocument(null, true);
                        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                        serializer.startTag(null, "citas");

                        serializer.startTag(null, "cita");

                        serializer.startTag(null, "fecha");
                        serializer.text(fecha);
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
                        Toast.makeText(getApplicationContext(), "Datos creados", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        Log.e("IOException", "Excepción al crear nuevo archivo");
                    }
                } else {
                    try {
                        System.out.println("MODIFICANDO FILE **************************************************>");
                        dBuilder = dbFactory.newDocumentBuilder();
                        System.out.println("Paso 1 **************************************************>");
                        Document doc = dBuilder.parse(newxmlfile);
                        System.out.println("Paso 2 **************************************************>");
                        doc.getDocumentElement().normalize();
                        System.out.println("Paso 3 **************************************************>");
                        addElement(doc, fecha, titulo, descrip);
                        writeXMLFile(doc);
                        Toast.makeText(getApplicationContext(), "Información actualizada", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException | TransformerException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private void writeXMLFile(Document doc) throws TransformerFactoryConfigurationError, TransformerException {
        doc.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        //StreamResult result = new StreamResult(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Draft/citasGuardadas.xml"));
        StreamResult result = new StreamResult(new File(Environment.getExternalStorageDirectory() + "/Draft/citasGuardadas.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        Toast.makeText(this, "Se ha actualizado correctamente el XML", Toast.LENGTH_SHORT).show();
    }

    private void addElement(Document doc, String fecha, String titulo, String descrip) {
        Node root = doc.getDocumentElement();

        Element nuevaCita = doc.createElement("cita");

        Element tituloCita = doc.createElement("titulo");
        tituloCita.appendChild(doc.createTextNode(titulo));
        nuevaCita.appendChild(tituloCita);
        System.out.println("titulo");

        Element fechaCita = doc.createElement("fecha");
        fechaCita.appendChild(doc.createTextNode(fecha));
        nuevaCita.appendChild(fechaCita);
        System.out.println("fecha");

        Element descripCita = doc.createElement("descrip");
        descripCita.appendChild(doc.createTextNode(descrip));
        nuevaCita.appendChild(descripCita);
        System.out.println("descrip");
        root.appendChild(nuevaCita);
        Toast.makeText(this, "Se han introducido datos correctamente el XML", Toast.LENGTH_SHORT).show();


    }
}