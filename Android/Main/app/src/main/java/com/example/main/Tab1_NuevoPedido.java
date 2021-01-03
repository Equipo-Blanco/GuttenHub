package com.example.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Tab1_NuevoPedido extends Fragment {
    private static final String TAG = "Tab1Fragment";
    Button btnConfirmar;
    Spinner listaProds;
    TextView tvCoste;
    EditText etCantidad;
    String[] productos;
    String[] precios;
    int cantidad;
    float precio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_nuevo_pedido, container, false);
        btnConfirmar = view.findViewById(R.id.btn_confirmar);
        listaProds = view.findViewById(R.id.spn_productos);
        tvCoste = view.findViewById(R.id.tv_coste);
        etCantidad = view.findViewById(R.id.et_cantidad);

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
                tvCoste.setText("Coste: " + precios[i]+"€");
                etCantidad.setText("1");
                if(i<precios.length) {
                    precio = Float.parseFloat(precios[i]);
                }else{
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
                if(etCantidad.length()>0) {
                    cantidad = Integer.parseInt(etCantidad.getText().toString());
                    float total = cantidad * precio;
                    tvCoste.setText("Coste: " + total+"€");
                }else{
                    tvCoste.setText("0");
                }
            }
        });


        btnConfirmar.setOnClickListener((view1) -> {
            Toast.makeText(getActivity(), "Presupuesto generado", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}