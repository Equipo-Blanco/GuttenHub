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
import java.util.ArrayList;

public class Tab1_NuevoPedido extends Fragment {
    private static final String TAG = "Tab1Fragment";
    Button btnConfirmar;
    Button btnAgregar;
    Spinner listaProds;
    TextView tvCoste, tvPresupuesto;
    EditText etCantidad;
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

    public void generaPresupuesto() {
        File newxmlfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DraftPresupuestos/presupuesto.xml");
        XmlSerializer serializer = Xml.newSerializer();
        FileOutputStream fileos = null;

        newxmlfile.getParentFile().mkdir();
        System.out.println("CREANDO FILE **************************************************>");

        try {
            fileos = new FileOutputStream(newxmlfile);
            System.out.println("A **************************************************>");
            newxmlfile.createNewFile();
            System.out.println("B **************************************************>");
            serializer.setOutput(fileos, "UTF-8");
            System.out.println("C **************************************************>");
            serializer.startDocument(null, true);
            System.out.println("D **************************************************>");
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            System.out.println("***************************************************<PRESUPUESTO>");
            serializer.startTag(null, "presupuesto");

            for (int i = 0; i < productosPresupuesto.size(); i++) {
                serializer.startTag(null, "producto");
                serializer.text(productosPresupuesto.get(i).getNombreProd());
                System.out.println("ASDAS*****************************ASDSA" + productosPresupuesto.get(i).getNombreProd());
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