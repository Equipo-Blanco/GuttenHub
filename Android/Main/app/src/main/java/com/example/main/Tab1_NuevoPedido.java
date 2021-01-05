package com.example.main;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
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
    ArrayList<ProductoPedido> productosPresupuesto = new ArrayList<ProductoPedido>();

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

            for (ProductoPedido a : productosPresupuesto) {
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

            ProductoPedido prod1 = new ProductoPedido(productoSeleccionado, cantidad, precio, total);
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

        String comercial, partner;
        String nombrePresup;
        comercial = obtieneDato(etComercial);
        partner = obtieneDato(etPartner);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String fecha = df.format(Calendar.getInstance().getTime());
        int id = (int) (Math.random() * 50)-1;
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

    class ProductoPedido {
        String nombreProd;
        int cantidad;
        float precioUnitario;
        float coste;

        public ProductoPedido(String nombre, int cant, float pUnitario, float cost) {
            this.nombreProd = nombre;
            this.cantidad = cant;
            this.precioUnitario = pUnitario;
            this.coste = cost;
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