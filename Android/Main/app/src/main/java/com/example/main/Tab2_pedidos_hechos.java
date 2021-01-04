package com.example.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
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

public class Tab2_pedidos_hechos extends Fragment {
    private static final String TAG = "Tab2Fragment";
    ListView listaPresupuestos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_pedidos_hechos, container, false);
        listaPresupuestos = view.findViewById(R.id.lv_presupuestos);

        File rutaPresupuestos = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DraftPresupuestos");
        String archivos[] = rutaPresupuestos.list();
        System.out.println("List of files and directories in the specified directory:");
        for (int i = 0; i < archivos.length; i++) {
            System.out.println(archivos[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, archivos);
        listaPresupuestos.setAdapter(adapter);

        Intent intentPresup = new Intent(getContext(), DetallePresupuesto.class);

        listaPresupuestos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String elegido = archivos[i];
                intentPresup.putExtra("nombrexml", elegido);
                startActivity(intentPresup);

            }
        });

        return view;
    }
}