package com.example.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Tab2_pedidos_hechos extends Fragment {
    private static final String TAG = "Tab2Fragment";
    ListView listaPresupuestos;
    Button bot_actualizar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_pedidos_hechos, container, false);
        listaPresupuestos = view.findViewById(R.id.lv_presupuestos);
        bot_actualizar = view.findViewById(R.id.btn_actualizar);


        File rutaPresupuestos = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DraftPresupuestos");
        List<String> archiv = Arrays.asList(rutaPresupuestos.list());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, archiv);
        listaPresupuestos.setAdapter(adapter);

        Intent intentPresup = new Intent(getContext(), DetallePresupuesto.class);

        listaPresupuestos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String elegido = archiv.get(i);
                intentPresup.putExtra("nombrexml", elegido);
                startActivity(intentPresup);

            }
        });

        bot_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Lista actualizada", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        return view;
    }
}