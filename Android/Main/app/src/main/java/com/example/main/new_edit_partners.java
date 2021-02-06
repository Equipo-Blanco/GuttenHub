package com.example.main;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
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
    EditText et_telefono;
    EditText et_mailComerc;
    EditText et_Contacto;
    EditText et_Direccion;
    Spinner sp_partners;

    int[] idComercial; //Almacenará los id de comercial para su uso con el Spinner
    String[] nombreComercial; //Almacenará los nombres de Comerciales para su uso con Spinner
    int comercial; //Almacenará el id de comercial que se insertará en la BDD
    boolean[] camposRellenos = new boolean[]{false, false, false, false, false};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_partners);

        bot_guardaPartner = (Button) findViewById(R.id.btn_guardaPartner);
        et_partner = (EditText) findViewById(R.id.etNombre);
        et_telefono = (EditText) findViewById(R.id.etTelefono);
        et_mailComerc = (EditText) findViewById(R.id.etMail);
        et_Contacto = (EditText) findViewById(R.id.etxtContacto);
        et_Direccion = (EditText) findViewById(R.id.etxtDireccion);
        sp_partners = (Spinner) findViewById(R.id.spin_partners);


        bot_guardaPartner.setEnabled(false);

        //volcar datos de Partners en el Spinner
        tablasSQLHelper usdbh = new tablasSQLHelper(getApplicationContext(), "DBDraft", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        if (db != null) {
            try {
                int pos = 0;
                //Contabilizar el numero de partners, para inicializar el array que los recogerá
                //Esto ofrece gran compatibilidad de cara al futuro, ya que si se insertasen mas comerciales, al ejecutar esta
                //sentencia, serviría para incluir los nuevos sin modificar el código
                Cursor c = db.rawQuery("SELECT COUNT(ID_COMERCIAL) AS TOTAL FROM COMERCIALES", null);
                //Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        pos = c.getInt(0);
                        //System.out.println(codigo + " " +nombre);
                    } while (c.moveToNext());
                }
                System.out.println(pos + "********************************************************************");
                int i = 0;
                idComercial = new int[pos];
                nombreComercial = new String[pos];

                Cursor c2 = db.rawQuery("SELECT ID_COMERCIAL, NOMBRE, APELLIDOS, EMPRESA FROM COMERCIALES", null);

                if (c2.moveToFirst()) {
                    do {
                        idComercial[i] = c2.getInt(0);
                        System.out.println(c2.getString(1) + "************************************");
                        nombreComercial[i] = c2.getString(1) + " " + c2.getString(2) + " - " + c2.getString(3);
                        i++;
                    } while (c2.moveToNext());
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        //Elemento ArrayAdapter, que permite coger un Array como fuente de información
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombreComercial);

        //Creamos nuestro Spinner
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_partners.setAdapter(adaptador);

        sp_partners.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        comercial = idComercial[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );


        et_partner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_partner.getText().length() > 0) {
                    camposRellenos[0] = true;
                } else {
                    camposRellenos[0] = false;
                }
                compruebaCampos(camposRellenos);
            }
        });
        et_telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_telefono.getText().length() > 0) {
                    camposRellenos[1] = true;
                } else {
                    camposRellenos[1] = false;
                    bot_guardaPartner.setEnabled(false);
                }
                compruebaCampos(camposRellenos);
            }
        });
        et_mailComerc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_mailComerc.getText().length() > 0) {
                    camposRellenos[2] = true;
                } else {
                    camposRellenos[2] = false;
                    bot_guardaPartner.setEnabled(false);
                }
                compruebaCampos(camposRellenos);
            }
        });
        et_Contacto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_Contacto.getText().length() > 0) {
                    camposRellenos[3] = true;
                } else {
                    camposRellenos[3] = false;
                    bot_guardaPartner.setEnabled(false);
                }
                compruebaCampos(camposRellenos);
            }
        });
        et_Direccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_Direccion.getText().length() > 0) {
                    camposRellenos[4] = true;
                } else {
                    bot_guardaPartner.setEnabled(false);
                    camposRellenos[4] = false;
                }
                compruebaCampos(camposRellenos);
            }
        });

        //Almacenar información al rellenar el formulario
        bot_guardaPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String partner;
                String mail;
                String telefono;
                String contacto;
                String direccion;

                //Validamos datos y asignamos valores
                partner = getDatos(et_partner);  //Empresa Partner
                mail = getDatos(et_mailComerc); // Mail
                telefono = getDatos(et_telefono); // Teléfono del partner
                contacto = getDatos(et_Contacto); //Nombre de la persona de contacto
                direccion = getDatos(et_Direccion); // Dirección de la empresa Partner


                // Parte SQL
                tablasSQLHelper usdbh = new tablasSQLHelper(getApplicationContext(), "DBDraft", null, 1);
                SQLiteDatabase db = usdbh.getWritableDatabase();
                if (db != null) {
                    //Insertamos los datos en la tabla Usuarios
                    try {
                        int maximo = 0;

                        Cursor c = db.rawQuery("SELECT MAX(ID_PARTNER) AS MAXIMO FROM PARTNERS", null);
                        //Nos aseguramos de que existe al menos un registro
                        if (c.moveToFirst()) {
                            //Recorremos el cursor hasta que no haya más registros
                            do {
                                maximo = c.getInt(0);
                                //System.out.println(codigo + " " +nombre);
                            } while (c.moveToNext());
                        }

                        maximo = maximo + 1;

                        db.execSQL("INSERT INTO PARTNERS (ID_PARTNER, ID_COMERCIAL, EMPRESA, DIRECCION, CONTACTO, TELEFONO, EMAIL) " +
                                "                 VALUES (" + maximo + ", " + comercial + ", '" + partner + "', '" + direccion + "', '" + contacto + "', " + telefono + ", '" + mail + "')");

                        Toast.makeText(getApplicationContext(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                //Cerramos la conexión con la base de datos
                db.close();


                //Parte XML, en principio la dejamos por los xml que hay que exportar para Visual Studio
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
                        serializer.text(String.valueOf(comercial));
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
                        aniadirElementos(doc, partner, mail, telefono, String.valueOf(comercial));
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
                Toast.makeText(
                        getApplicationContext(), "Partner almacenado correctamente", Toast.LENGTH_SHORT).show();
                Intent volver = new Intent(getApplicationContext(), Partners.class);

                startActivity(volver);
            }
        });
    }

    private void compruebaCampos(boolean[] camposRellenos) {
        int camposOk = 0;
        boolean faltaAlguno = false;

        for (int i = 0; i < camposRellenos.length; i++) {
            if (camposRellenos[i] == false) {
                faltaAlguno = true;
            } else {

            }
        }

        System.out.println("**********************************-----" + camposOk + "***************");

        if (faltaAlguno == false){
            //Todos los campos han sido rellenados
            bot_guardaPartner.setEnabled(true);
        } else {
            //Toast.makeText(this, "Falta algún campo por rellenar", Toast.LENGTH_SHORT).show();
            bot_guardaPartner.setEnabled(false);
        }
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
            dato = "Sin Especificar";
        }
        return dato;
    }
}
