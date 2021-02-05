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
    TextView tvCoste, tvPresupuesto;
    EditText etCantidad;
    EditText etComercial;
    EditText etPartner;
    String[] productos;
    String[] precios;
    int cantidad;
    float precio;
    String presupuestoA = ""; //El presupuesto sin guardar aún
    String productoSeleccionado;
    float total;
    ArrayList<LineasAlbaran> productosPresupuesto = new ArrayList<LineasAlbaran>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_nuevo_pedido, container, false);
        btnConfirmar = view.findViewById(R.id.btn_confirmar);
        btnAgregar = view.findViewById(R.id.btn_agregar);
        listaProds = view.findViewById(R.id.spn_productos);
        tvCoste = view.findViewById(R.id.tv_coste);
        etCantidad = view.findViewById(R.id.et_cantidad);
        tvPresupuesto = view.findViewById(R.id.tv_presupuesto);
        etComercial = view.findViewById(R.id.et_comercial);
        etPartner = view.findViewById(R.id.et_partner);

        productos = getResources().getStringArray(R.array.productos);
        precios = getResources().getStringArray(R.array.precios);

        ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, productos);
        adaptador1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tvCoste.setText("Coste: 69.00€");
        etCantidad.setText("1");

        listaProds.setAdapter(adaptador1);
        listaProds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvCoste.setText("Coste: " + precios[i] + "€");
                etCantidad.setText("1");
                if (i < precios.length) {
                    precio = Float.parseFloat(precios[i]);
                    productoSeleccionado = productos[i];
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

            LineasAlbaran prod1 = new LineasAlbaran(productoSeleccionado, cantidad, precio, total);
            System.out.println(prod1);
            productosPresupuesto.add(prod1);
        });

        return view;
    }

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

        //SENTENCIAS SQL:

        //INSERT INTO CABECERA_ALBARANES (ID_ALBARANCABECERA, ID_PARTNER, ID_COMERCIAL) VALUES(1, 1, 1)
        //INSERT INTO LINEAS_ALBARAN (ID_ALBARANLINEA, ID_ARTICULO, CANTIDAD, PRECIO, ID_ALBARANCABECERA, IMPORTE) VALUES(1, 1, 10, 5, 1, 50)

        //Obtener los valores de variables e insertar en las sentencias SQL aprovechando una instancia del ArrayList de clase LineasAlbaran

        //Agregar Fechas del pedido!
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
                db.execSQL("INSERT INTO CABECERA_ALBARANES " +
                        "                 VALUES (" + idAlbaran + ", 'FALTA SPINNER PARTNER', 'FALTA SPINNER COMERCIAL', 'FECHA ALBARAN', 'FECHA ENVIO 2 dias mas que fecAlb', 'fecEntrega 7 dias', 'Dirección envio', 'Total Factura' )");

                //Inserción del pedido - Parte 2 : LINEAS ALBARÁN
                //Incorporar Lista con un bucle ir leyendo e insertando
                db.execSQL("");

                Toast.makeText(getContext(), "Pedido Creado correctamente", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //Cerramos la conexión con la base de datos
        db.close();


        //LO DE ABAJO ES PARA XML

        String comercial, partner;
        String nombrePresup;
        comercial = obtieneDato(etComercial);
        partner = obtieneDato(etPartner);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String fecha = df.format(Calendar.getInstance().getTime());
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
        public LineasAlbaran(int _idLinea, int _idArticulo, int cant, float pUnitario, float cost, int _idAlb) {
            this.idLinea = _idLinea;
            this.idArticulo = _idArticulo;
            this.cantidad = cant;
            this.precioUnitario = pUnitario;
            this.coste = cost; //Importe precio X cantidad con IVA aplicado
            this.idAlbaran = _idAlb;
        }

        public String getNombreProd() {
            return nombreProd;
        }

        public int getCantidad() {
            return cantidad;
        }

        public float getPrecioUnitario() {
            return precioUnitario;
        }

        public float getCoste() {
            return coste;
        }

        @Override
        public String toString() {
            return "* Producto " + nombreProd + " " + cantidad + "uds" + ", PVP: " + precioUnitario + ", coste: " + coste + " €";
        }
    }
}