package com.example.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
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

public class Partners extends AppCompatActivity {

    private ListView LvPartners;
    private TextView tvTfno;
    private TextView tvNombrePartner;
    private TextView tvCorreo;
    private TextView tvComAso;
    private Button bot_Nuevo;
    List<clasePartner> ListPartners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        LvPartners = (ListView) findViewById(R.id.lvPartners);
        tvTfno = (TextView) findViewById(R.id.tvTfno);
        tvCorreo = (TextView) findViewById(R.id.tvCorreo);
        tvComAso = (TextView) findViewById(R.id.tvComAso);
        bot_Nuevo = (Button) findViewById(R.id.btnNuevo);
        tvNombrePartner = (TextView) findViewById(R.id.tv_nombrePartner);

        XMLPPPartners parser = new XMLPPPartners();
        ListPartners = parser.parseXML(this);

        //debugs de prueba
        for (clasePartner p : ListPartners) {
            System.out.println("*****************Lista Partners*****************************");
            System.out.println(p.toString());
        }

        ArrayAdapter<clasePartner> adapter = new ArrayAdapter<clasePartner>(this, android.R.layout.simple_expandable_list_item_1, ListPartners);
        LvPartners.setAdapter(adapter);

        LvPartners.setOnItemClickListener((parent, view, position, id) -> {
            tvNombrePartner.setText(ListPartners.get(position).getnPartner());
            tvCorreo.setText(ListPartners.get(position).getnMail());
            tvTfno.setText(ListPartners.get(position).getnTelefono());
            tvComAso.setText(ListPartners.get(position).getnComercial());
        });

        LvPartners.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Partners.this);
                builder.setTitle("Eliminar Partner");
                builder.setMessage("¿Deseas eliminar el partner seleccionado?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String nombrePartner = ListPartners.get(position).getnPartner();
                            System.out.println("Nombre Partner " + nombrePartner);
                            eliminaPartner(nombrePartner);
                            Toast.makeText(getApplicationContext(), "Partner Eliminado", Toast.LENGTH_SHORT).show();

                            //Tras eliminar hay que recargar la activity para actualizar la lista
                            Intent recargar = new Intent(getApplicationContext(), Partners.class);
                            startActivity(recargar);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error al intentar eliminar el partner seleccionado", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getApplicationContext(), "Partner Eliminado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });

        Intent intent = new Intent(this, new_edit_partners.class);
        bot_Nuevo.setOnClickListener(view -> startActivity(intent));
    }

    private static void eliminaPartner(String nombreXml) {
        File xmlFile = new File(Environment.getExternalStorageDirectory() + "/Draft/partnersGuardados.xml");
        //System.out.println(xmlFile);
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            NodeList partnersXml = document.getElementsByTagName("partner");
            for (int i = 0; i < partnersXml.getLength(); i++) {
                Element nombre = (Element) partnersXml.item(i);
                Element etiquetaNombre = (Element) nombre.getElementsByTagName("nombre").item(0);
                if (etiquetaNombre.getTextContent().equals(nombreXml)) {
                    etiquetaNombre.getParentNode().getParentNode().removeChild(partnersXml.item(i));
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
        StreamResult streamResult = new StreamResult(new File(Environment.getExternalStorageDirectory() + "/Draft/partnersGuardados.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, streamResult);
    }
}