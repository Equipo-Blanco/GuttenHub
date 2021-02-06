package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DetallePresupuesto extends AppCompatActivity {

    List<clasePresupuesto> datosPresupuesto;
    TextView tvInfoPresupuesto, tvPresPartner, tvPresComercial,tvTotal, tvTitulo, tvFecha;
    String cabecera[] = new String[4];
    float totalPresupuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_presupuesto);

        totalPresupuesto = 0.0f;
        DecimalFormat df = new DecimalFormat("#.##");
        tvInfoPresupuesto = (TextView) findViewById(R.id.tv_datos_presupuesto);
        tvTitulo = (TextView) findViewById(R.id.tv_titulo_detalle_presup);
        tvPresComercial = (TextView) findViewById(R.id.tv_pres_comercial);
        tvPresPartner = (TextView) findViewById(R.id.tv_pres_partner);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        tvFecha = (TextView) findViewById(R.id.tv_Fecha);

        String archivoPresup;

        Bundle extras = getIntent().getExtras();
        archivoPresup = extras.getString("nombrexml");

        getCabecera(archivoPresup); //Obtiene ID, Partner y Comercial y los almacena en el array

        XMLPPHPresupuestos parser = new XMLPPHPresupuestos();
        datosPresupuesto = parser.parseXML(this, archivoPresup);

        String contenido = "***************************\n";
        for (clasePresupuesto info : datosPresupuesto) {
            totalPresupuesto = totalPresupuesto + info.getCoste();
            contenido = contenido + info + "\n";
            System.out.println("*************************" + contenido);
        }

        contenido = contenido + "\n***************************";

        tvInfoPresupuesto.setText(contenido);
        tvTitulo.setText("ID Albarán: " +cabecera[2]);
        tvPresPartner.setText("Partner: " +cabecera[0]);
        tvPresComercial.setText("Comercial: "+cabecera[1]);
        tvTotal.setText("TOTAL: " + df.format(totalPresupuesto) +"€");
        tvFecha.setText("Fecha Albarán: " +cabecera[3]);

    }

    private void getCabecera(String archivo) {

        try {
            File inputFile = new File("/storage/emulated/0//DraftPresupuestos/" + archivo);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("presupuesto");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    cabecera[0] = eElement.getElementsByTagName("partner").item(0).getTextContent();
                    cabecera[1] = eElement.getElementsByTagName("comercial").item(0).getTextContent();
                    cabecera[2] = eElement.getElementsByTagName("id").item(0).getTextContent();
                    cabecera[3] = eElement.getElementsByTagName("fecha").item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}