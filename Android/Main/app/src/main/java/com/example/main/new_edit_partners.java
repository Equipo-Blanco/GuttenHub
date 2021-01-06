package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class new_edit_partners extends AppCompatActivity {
    Button bot_guardaPartner;
    EditText et_partner;
    EditText et_comercial;
    EditText et_telefono;
    EditText et_mailComerc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_partners);

        bot_guardaPartner = (Button) findViewById(R.id.btn_guardaPartner);
        et_partner = (EditText) findViewById(R.id.etNombre);
        et_comercial = (EditText) findViewById(R.id.etNomComercial);
        et_telefono = (EditText) findViewById(R.id.etTelefono);
        et_mailComerc = (EditText)findViewById(R.id.etMail);

        bot_guardaPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String partner ;
                String comercial;
                String mail;
                String telefono;

                //Validamos datos y asignamos valores
                partner=getDatos(et_partner);;
                comercial= getDatos(et_comercial);
                mail= getDatos(et_mailComerc);
                telefono= getDatos(et_telefono);

                File newxmlfile = new File(Environment.getExternalStorageDirectory() + "/Draft/partnersGuardados.xml");
                XmlSerializer serializer = Xml.newSerializer();
                FileOutputStream fileos = null;

                //Necesario para el DOM:
                String filePath = Environment.getExternalStorageDirectory() + "/Draft/partnersGuardados.xml";
                System.out.println(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder;

                if (!newxmlfile.exists()) {
                    newxmlfile.getParentFile().mkdir();


                    try {
                        fileos = new FileOutputStream(newxmlfile);
                        newxmlfile.createNewFile();
                        serializer.setOutput(fileos, "UTF-8");
                        serializer.startDocument(null, true);
                        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                        serializer.startTag(null, "agenda");

                        serializer.startTag(null, "partner");

                        serializer.startTag(null, "nombre");
                        serializer.text(partner);
                        serializer.endTag(null, "nombre");

                        serializer.startTag(null, "mail");
                        serializer.text(mail);
                        serializer.endTag(null, "mail");

                        serializer.startTag(null, "telefono");
                        serializer.text(telefono);
                        serializer.endTag(null, "telefono");

                        serializer.startTag(null, "comercial");
                        serializer.text(comercial);
                        serializer.endTag(null, "comercial");

                        serializer.endTag(null, "partner");
                        serializer.endTag(null, "agenda");
                        serializer.endDocument();
                        serializer.flush();
                        fileos.close();
                        Toast.makeText(getApplicationContext(), "Datos creados", Toast.LENGTH_SHORT).show();
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
                        aniadirElementos(doc, partner, mail, telefono, comercial);
                        escribirXML(doc);
                        //Toast.makeText(getApplicationContext(), "Información actualizada", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException | TransformerException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), "Partner almacenado correctamente", Toast.LENGTH_SHORT).show();
                Intent volver = new Intent(getApplicationContext(), Partners.class);
                startActivity(volver);
            }
        });
    }

    private void escribirXML(Document doc) throws TransformerFactoryConfigurationError, TransformerException {
        doc.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(Environment.getExternalStorageDirectory() + "/Draft/partnersGuardados.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        //Toast.makeText(this, "Se ha actualizado correctamente el XML", Toast.LENGTH_SHORT).show();
    }

    private void aniadirElementos(Document doc, String partner, String mail, String telefono, String comercial) {
        Node root = doc.getDocumentElement();

        Element nuevoComercial = doc.createElement("partner");

        Element ePartner = doc.createElement("nombre");
        ePartner.appendChild(doc.createTextNode(partner));
        nuevoComercial.appendChild(ePartner);
        System.out.println("titulo");

        Element eMail = doc.createElement("mail");
        eMail.appendChild(doc.createTextNode(mail));
        nuevoComercial.appendChild(eMail);
        System.out.println("fecha");

        Element eTelefono = doc.createElement("telefono");
        eTelefono.appendChild(doc.createTextNode(telefono));
        nuevoComercial.appendChild(eTelefono);
        System.out.println("descrip");

        Element eComercial = doc.createElement("comercial");
        eComercial.appendChild(doc.createTextNode(comercial));
        nuevoComercial.appendChild(eComercial);

        root.appendChild(nuevoComercial);
        Toast.makeText(this, "Se han introducido los datos correctamente", Toast.LENGTH_SHORT).show();


    }

    //Obtiene texto de los campos y valida
    public String getDatos(EditText etxt) {
        String dato;

        if (etxt.length() > 0) {
            dato = etxt.getText().toString();
        } else {
            dato = "";
        }
        return dato;
    }
}
