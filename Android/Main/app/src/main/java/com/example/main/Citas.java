package com.example.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Citas extends AppCompatActivity {

    TextView diaSelec;
    CalendarView calendario;
    FloatingActionButton bot_nuevo;
    private String curDate;
    private int dia, mes, anio;
    private String fecha;
    ListView listView;
    List<ClaseCita> citasExistentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        diaSelec = (TextView) findViewById(R.id.tvDia);
        calendario = (CalendarView) findViewById(R.id.viewcalendario);
        bot_nuevo = (FloatingActionButton) findViewById(R.id.btnNuevo);
        bot_nuevo.setEnabled(false);
        listView = (ListView) findViewById(R.id.listaCitas);
        Intent intent = new Intent(this, NuevaCita.class);

        XMLPullParserHandler parser = new XMLPullParserHandler();
        citasExistentes = parser.parseXML(this);

        ArrayAdapter<ClaseCita> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, citasExistentes);
        listView.setAdapter(adapter);

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                month = month + 1; //Los meses los toma de 0 a 11 por lo que hay que sumar +1
                curDate = "Fecha seleccionada: " + dayOfMonth + "/" + month + "/" + year;
                diaSelec.setText(curDate);
                dia = dayOfMonth;
                mes = month;
                anio = year;
                bot_nuevo.setEnabled(true);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), citasExistentes.get(position).getDescripCita(), Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //ListPartners.remove(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(Citas.this);
                builder.setTitle("Eliminar Cita");
                builder.setMessage("¿Deseas eliminar la cita seleccionada?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String nombreCita = citasExistentes.get(position).getTituloCita();
                            eliminaCita(nombreCita);

                            //Tras eliminar hay que recargar la activity por la lista
                            Toast.makeText(getApplicationContext(), "Cita Eliminada", Toast.LENGTH_SHORT).show();
                            Intent recargar = new Intent(getApplicationContext(), Citas.class);
                            startActivity(recargar);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error al intentar eliminar la cita seleccionada", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });


        bot_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fecha = dia + "/" + mes + "/" + anio;
                intent.putExtra("fechacita", fecha);
                startActivity(intent);
            }
        });
    }

    private static void eliminaCita(String nombreXml) {
        File xmlFile = new File(Environment.getExternalStorageDirectory() + "/Draft/citasGuardadas.xml");
        //System.out.println(xmlFile);
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            NodeList citasXML = document.getElementsByTagName("cita");
            for (int i = 0; i < citasXML.getLength(); i++) {
                Element nombre = (Element) citasXML.item(i);
                Element etiquetaCita = (Element) nombre.getElementsByTagName("titulo").item(0);
                if (etiquetaCita.getTextContent().equals(nombreXml)) {
                    etiquetaCita.getParentNode().getParentNode().removeChild(citasXML.item(i));
                    break;
                }
            }
            guardarXML(document, xmlFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void guardarXML(Document document, File xmlFile) throws TransformerFactoryConfigurationError, TransformerException {
        document.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(Environment.getExternalStorageDirectory() + "/Draft/citasGuardadas.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, streamResult);
    }
}