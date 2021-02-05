package com.example.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

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
    private TextView tvDirecc;
    private TextView tvNombrePartner;
    private TextView tvCorreo;
    private TextView tvComAso;
    private Button bot_Nuevo;
    List<clasePartner> ListPartners;
    int[] idcomercial;
    String[] empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        LvPartners = (ListView) findViewById(R.id.lvPartners);
        tvTfno = (TextView) findViewById(R.id.tvTfno);
        tvCorreo = (TextView) findViewById(R.id.tvCorreo);
        tvComAso = (TextView) findViewById(R.id.tvComAso);
        bot_Nuevo = (Button) findViewById(R.id.btnNuevo);
        tvDirecc = (TextView) findViewById(R.id.tvDireccion);
        tvNombrePartner = (TextView) findViewById(R.id.tv_nombrePartner);

        //cargaComerciales(ListaPartners);

        //XMLPPPartners parser = new XMLPPPartners();
        //ListPartners = parser.parseXML(this);


        //Rellenar Lista desde XML
        //for (clasePartner p : ListPartners) {
        //    System.out.println("*****************Lista Partners*****************************");
        //    System.out.println(p.toString());
        // }

        tablasSQLHelper usdbh = new tablasSQLHelper(getApplicationContext(), "DBDraft", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        String resultado = "";
        ArrayList<String> ListaPartners = new ArrayList<>();
        if (db != null) {
            try {
                int pos = 0;
                //Contabilizar el numero de partners, para inicializar el array que los recogerá
                Cursor c = db.rawQuery("SELECT COUNT(ID_PARTNER) AS TOTAL FROM PARTNERS", null);
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
                idcomercial = new int[pos];
                empresa = new String[pos];

                Cursor c2 = db.rawQuery("SELECT ID_COMERCIAL, EMPRESA FROM PARTNERS", null);

                if (c2.moveToFirst()) {
                    do {
                        idcomercial[i] = c2.getInt(0);
                        System.out.println(c2.getString(1) + "************************************");
                        empresa[i] = c2.getString(1);
                        i++;
                    } while (c2.moveToNext());
                }

                //ListaPartners = new ArrayList<String>();
                for (String emp : empresa) {
                    System.out.println(emp);
                    ListaPartners.add(emp);
                }
                //db.execSQL("");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //Cerramos la base de datos
        //db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ListaPartners);
        LvPartners.setAdapter(adapter);

        LvPartners.setOnItemClickListener((parent, view, position, id) -> {
            //Reinicio/limpiado de los campos de texto
            tvDirecc.setText("");
            tvNombrePartner.setText("");
            tvCorreo.setText("");
            tvTfno.setText("");
            tvComAso.setText("");

            //Debugeo informativo en consola
            System.out.println(LvPartners.getItemAtPosition(position) + "*****************************");

            try {
                int pos = 0;
                Cursor c = db.rawQuery("SELECT EMPRESA, DIRECCION, CONTACTO, TELEFONO, EMAIL FROM PARTNERS WHERE EMPRESA ='" + LvPartners.getItemAtPosition(position).toString() +"'", null);
                //Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        tvNombrePartner.setText(c.getString(0));
                        tvDirecc.setText("Dirección: " + c.getString(1));
                        tvComAso.setText("Contacto Comercial: " + c.getString(2));
                        tvTfno.setText("Teléfono: " + c.getString(3));
                        tvCorreo.setText("Email: " + c.getString(4));
                        //System.out.println(codigo + " " +nombre);
                    }while(c.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


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

    private void cargaComerciales(List<String> lisPartn) {

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