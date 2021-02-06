package com.example.main;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Tab1_NuevoPedido extends Fragment {
    private static final String TAG = "Tab1Fragment";
    Button btnConfirmar;
    Button btnAgregar;
    Spinner listaProds;
    TextView tvCoste, tvPresupuesto, tvDatosPartner;
    EditText etCantidad;
    Spinner spComerciales;
    Spinner spPartners;

    String[] productos;
    String[] precios;
    String[] partners;
    String[] comerciales;
    int cantidad, idPartner, idComercial, idArtic;
    int idLinea = 0;
    float precio;
    float totAlb=0;
    String presupuestoA = ""; //El presupuesto sin guardar aún
    String productoSeleccionado;
    String direccionEnvio;
    float total;
    ArrayList<LineasAlbaran> productosPresupuesto = new ArrayList<LineasAlbaran>();
    ArrayList<LineasAlbaran> lineasAlbaranSQL = new ArrayList<LineasAlbaran>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_nuevo_pedido, container, false);
        btnConfirmar = view.findViewById(R.id.btn_confirmar);
        btnAgregar = view.findViewById(R.id.btn_agregar);
        listaProds = view.findViewById(R.id.spn_productos);
        tvCoste = view.findViewById(R.id.tv_coste);
        tvDatosPartner = view.findViewById(R.id.tv_DatosPartner);
        etCantidad = view.findViewById(R.id.et_cantidad);
        tvPresupuesto = view.findViewById(R.id.tv_presupuesto);
        spComerciales = view.findViewById(R.id.spn_comerciales);
        spPartners = view.findViewById(R.id.spn_partners);

        productos = getResources().getStringArray(R.array.productos);
        precios = getResources().getStringArray(R.array.precios);

        ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, productos);
        adaptador1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tvCoste.setText("Coste: 69.00€");
        etCantidad.setText("1");

        insertaProductosXML();  //Método que recorre el XML catálogo y los inserta en la BDD


        //Inicialización y creación de los Arrays empleados en los Spinner mediante sendas consultas a la base de datos

        tablasSQLHelper usdbh = new tablasSQLHelper(getContext(), "DBDraft", null, 1);
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
                comerciales = new String[pos];

                //Extraer los comerciales para pasarlos al array y luego cargarlos en el Spinner
                Cursor c2 = db.rawQuery("SELECT ID_COMERCIAL, NOMBRE, APELLIDOS, EMPRESA FROM COMERCIALES", null);

                if (c2.moveToFirst()) {
                    do {
                        System.out.println(c2.getString(1) + "************************************");
                        comerciales[i] = c2.getString(1) + " " + c2.getString(2) + " - " + c2.getString(3);
                        i++;
                    } while (c2.moveToNext());
                }

                //Contar los partners para inicializar el array
                Cursor c3 = db.rawQuery("SELECT COUNT(ID_PARTNER) FROM PARTNERS", null);
                i = 0;
                if (c3.moveToFirst()) {
                    do {
                        // System.out.println(c3.getString(1) + "************************************");
                        pos = c3.getInt(0);
                        i++;
                    } while (c3.moveToNext());
                }
                partners = new String[pos];

                Cursor c4 = db.rawQuery("SELECT EMPRESA FROM PARTNERS", null);
                i = 0;
                if (c4.moveToFirst()) {
                    do {
                        System.out.println(c4.getString(0) + "********************************");
                        partners[i] = c4.getString(0);
                        i++;
                    } while (c4.moveToNext());
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        //Cargar y mostrar los datos extraídos de la BDD en el Spinner de Comerciales

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, comerciales);

        //Creamos nuestro Spinner
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComerciales.setAdapter(adaptador);

        spComerciales.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        idComercial = position + 1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );


        //Cargar y mostrar los datos extraídos de la BDD en el Spinner de Partners

        ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, partners);

        //Creamos nuestro Spinner
        adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPartners.setAdapter(adaptador2);

        spPartners.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        idPartner = position + 1;
                        String info = "";
                        try {
                            Cursor c5 = db.rawQuery("SELECT CONTACTO, DIRECCION, TELEFONO, EMAIL FROM PARTNERS WHERE ID_PARTNER = " + (position + 1), null);
                            int i = 0;
                            if (c5.moveToFirst()) {
                                do {
                                    info = "Contacto: " + c5.getString(0) + "\n" +
                                            "Dirección: " + c5.getString(1) + "\n" +
                                            "Teléfono: " + c5.getInt(2) + "\n" +
                                            "Email: " + c5.getString(3);
                                    direccionEnvio = c5.getString(1);
                                    tvDatosPartner.setText(info);
                                } while (c5.moveToNext());
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );


        listaProds.setAdapter(adaptador1);
        listaProds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvCoste.setText("Coste: " + precios[i] + "€");
                etCantidad.setText("1");
                if (i < precios.length) {
                    precio = Float.parseFloat(precios[i]);
                    productoSeleccionado = productos[i];
                    idArtic = i + 1;
                } else {
                    precio = 1.0f;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etCantidad.length() > 0) {
                    cantidad = Integer.parseInt(etCantidad.getText().toString());
                    total = cantidad * precio;
                    tvCoste.setText("Coste: " + total + "€");
                } else {
                    tvCoste.setText("0");
                }
            }
        });

        btnConfirmar.setOnClickListener((view1) -> {
            Toast.makeText(getActivity(), "Presupuesto generado", Toast.LENGTH_SHORT).show();

            for (LineasAlbaran a : productosPresupuesto) {
                System.out.println(a);
            }
            generaPresupuesto();

            //Recarga la actividad por el problema del array
            getActivity().finish();
            startActivity(getActivity().getIntent());
        });

        btnAgregar.setOnClickListener((view1) -> {
            Toast.makeText(getActivity(), "Articulo agregado", Toast.LENGTH_SHORT).show();
            String linea = productoSeleccionado + " x" + cantidad + " - " + total;
            presupuestoA = presupuestoA + linea + "\n";
            tvPresupuesto.setText(presupuestoA);

            //Versión del ArrayList para XML, más simple
            LineasAlbaran prod1 = new LineasAlbaran(productoSeleccionado, cantidad, precio, total);
            System.out.println(prod1);
            productosPresupuesto.add(prod1);

            idLinea++;

            //Versión del ArrayList para SQL, más completa public LineasAlbaran(int _idLinea, int _idArticulo, int cant, float pUnitario, float cost)
            LineasAlbaran prodSQL = new LineasAlbaran(idLinea, idArtic, cantidad, precio, total);
            lineasAlbaranSQL.add(prodSQL);
        });

        return view;
    }

    //Método que recorre el XML catálogo y los inserta en la BDD
    private void insertaProductosXML() {
        tablasSQLHelper usdbh = new tablasSQLHelper(getContext(), "DBDraft", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        if (db != null) {
            //Insertamos los datos tomados del XML Catalogo en la tabla ARTICULOS
            try {
                for (int i = 0; i < productos.length; i++) {
                    db.execSQL("INSERT INTO ARTICULOS " +
                            " VALUES (" + (i + 1) + ", '" + productos[i] + "', 1, " + Float.parseFloat(precios[i]) + ", " + (Float.parseFloat(precios[i]) * 1.20) + ", 100, 10, 'imagen.jpg')");
                }

                Toast.makeText(getContext(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //Cerramos la conexión con la base de datos
        //db.close();
    }

    //Método que valida campos de texto / EditText
    public String obtieneDato(EditText view) {
        String texto;

        if (view.length() > 0) {
            texto = view.getText().toString();
        } else {
            texto = "Sin especificar";
        }

        return texto;
    }

    public void generaPresupuesto() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String fecha = df.format(Calendar.getInstance().getTime());

        //Obtener los valores de variables e insertar en las sentencias SQL aprovechando una instancia del ArrayList de clase LineasAlbaran

        tablasSQLHelper usdbh = new tablasSQLHelper(getContext(), "DBDraft", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        if (db != null) {
            //Insertamos los datos en la tabla Usuarios
            try {
                int idAlbaran = 0; //Inicializar a 0

                Cursor c = db.rawQuery("SELECT MAX(ID_ALBARANCABECERA) AS MAXIMO FROM CABECERA_ALBARANES", null);
                //Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        idAlbaran = c.getInt(0);
                        //System.out.println(codigo + " " +nombre);
                    } while (c.moveToNext());
                }

                idAlbaran = idAlbaran + 1;

                //Inserción del pedido - Parte 1: CABECERA ALBARÁN
                db.execSQL("INSERT INTO CABECERA_ALBARANES (ID_ALBARANCABECERA, ID_PARTNER, ID_COMERCIAL, FECHA_ALBARAN, DIRECCION_ENVIO, TOTAL_FACTURA) " +
                        "     VALUES (" + idAlbaran + ", " + idPartner + ", " + idComercial + ", '" + fecha + "', '" + direccionEnvio + "', 0 )");

                //Inserción del pedido - Parte 2 : LINEAS ALBARÁN
                //Incorporar Lista con un bucle ir leyendo e insertando
                for (int i = 0; i < lineasAlbaranSQL.size(); i++) {
                    lineasAlbaranSQL.get(i).setIdAlbaran(idAlbaran);
                    int idLinea = i + 1;
                    int iva = lineasAlbaranSQL.get(i).getIVA();
                    int idart = lineasAlbaranSQL.get(i).getIdArticulo();
                    int cant = lineasAlbaranSQL.get(i).getCantidad();
                    float prec = lineasAlbaranSQL.get(i).getPrecioUnitario();
                    float impte = lineasAlbaranSQL.get(i).getCoste();
                    totAlb = totAlb + impte;

                    db.execSQL("INSERT INTO LINEAS_ALBARAN VALUES (" + idLinea + ", " + iva + ", " + idart + ", "+ cant +", "+ prec +", "+ idAlbaran+", "+ impte +")");

                }
                    db.execSQL("UPDATE CABECERA_ALBARANES SET TOTAL_FACTURA =" +totAlb +" WHERE ID_ALBARANCABECERA =" +idAlbaran);

                Toast.makeText(getContext(), "Pedido Creado correctamente", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //Cerramos la conexión con la base de datos
        // db.close();


        //LO DE ABAJO ES PARA XML

        String comercial, partner;
        String nombrePresup;
        comercial = ""; // *************************************************************************
        partner = "";  // **************************************************************************

        int id = (int) (Math.random() * 5000) - 1;
        nombrePresup = fecha + partner + "-" + id;

        File newxmlfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DraftPresupuestos/" + nombrePresup + ".xml");
        XmlSerializer serializer = Xml.newSerializer();
        FileOutputStream fileos = null;

        newxmlfile.getParentFile().mkdir();

        try {
            fileos = new FileOutputStream(newxmlfile);
            newxmlfile.createNewFile();
            serializer.setOutput(fileos, "UTF-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "presupuesto");

            serializer.startTag(null, "partner");
            serializer.text(partner);
            serializer.endTag(null, "partner");

            serializer.startTag(null, "comercial");
            serializer.text(comercial);
            serializer.endTag(null, "comercial");

            serializer.startTag(null, "id");
            serializer.text(String.valueOf(id));
            serializer.endTag(null, "id");

            for (int i = 0; i < productosPresupuesto.size(); i++) {
                serializer.startTag(null, "articulo");
                serializer.startTag(null, "producto");
                serializer.text(productosPresupuesto.get(i).getNombreProd());
                serializer.endTag(null, "producto");

                serializer.startTag(null, "cantidad");
                serializer.text("" + productosPresupuesto.get(i).getCantidad());
                System.out.println(productosPresupuesto.get(i).getCantidad());
                serializer.endTag(null, "cantidad");

                serializer.startTag(null, "precio");
                serializer.text("" + productosPresupuesto.get(i).getPrecioUnitario());
                serializer.endTag(null, "precio");

                serializer.startTag(null, "coste");
                serializer.text("" + productosPresupuesto.get(i).getCoste());
                serializer.endTag(null, "coste");
                serializer.endTag(null, "articulo");
            }

            serializer.endTag(null, "presupuesto");
            serializer.endDocument();
            serializer.flush();
            fileos.close();
            Toast.makeText(getContext(), "XML PRESUPUESTO CREADO", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("IOException", "Excepción al crear nuevo archivo");
        }
    }

    class LineasAlbaran {
        //Esto consolida las líneas de albarán
        String nombreProd;
        int idLinea;
        int cantidad;
        int idArticulo;
        int idAlbaran;
        float precioUnitario;
        float coste;
        final int IVA = 21;

        //Constructor empleado para el XML
        public LineasAlbaran(String nombre, int cant, float pUnitario, float cost) {
            this.nombreProd = nombre;
            this.cantidad = cant;
            this.precioUnitario = pUnitario;
            this.coste = cost;
        }

        //Constructor empleado para la Base de Datos, con más detalle
        public LineasAlbaran(int _idLinea, int _idArticulo, int cant, float pUnitario, float cost) {
            this.idLinea = _idLinea;
            this.idArticulo = _idArticulo;
            this.cantidad = cant;
            this.precioUnitario = pUnitario;
            this.coste = cost; //Importe precio X cantidad con IVA aplicado
            this.idAlbaran = 0;
        }

        public String getNombreProd() {
            return nombreProd;
        }

        public int getIdArticulo() {
            return idArticulo;
        }

        public int getCantidad() {
            return cantidad;
        }

        public int getIVA() {
            return IVA;
        }

        public float getPrecioUnitario() {
            return precioUnitario;
        }

        public float getCoste() {
            return coste;
        }

        public void setIdAlbaran(int idalb) {
            this.idAlbaran = idalb;
        }

        @Override
        public String toString() {
            return "* Producto " + nombreProd + " " + cantidad + "uds" + ", PVP: " + precioUnitario + ", coste: " + coste + " €";
        }
    }
}