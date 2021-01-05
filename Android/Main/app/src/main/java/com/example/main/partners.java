package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class partners extends AppCompatActivity implements View.OnClickListener {

    private ListView LvPartners;
    private TextView tvPartners;
    private TextView tvNombre;
    private TextView tvTfno;
    private TextView tvCorreo;
    private TextView tvComAso;
    private Button BtnNuevo;
    private Button BtnEditar;
    private Button BtnBorrar;

    private String partners[] = {"Nombre1, Nombre2, Nombre3, Nombre4, Nombre5, Nombre6, Nombre7"};
    private String telefonos[] = {"681393521, 681393522, 681393523, 681393524, 681393525, 681393526, 681393527"};
    private String correos[] = {"correo1@draft.com, correo2@draft.com, correo3@draft.com, correo4@draft.com, correo5@draft.com, correo6@draft.com, correo7@draft.com"};
    private String comerciales[] = {"Comercial1, Comercial2, Comercial3, Comercial4, Comercial5, Comercial6, Comercial7"};
    //Tienes estos en el Main:
    //final String[] datos = new String[]{"Gipuzkoa", "Bizkaia", "Araba", "Malaga", "Madrid", "Barcelona"};
    //final String[] telefonos = new String[]{"943123456", "946881171", "945007660", "951926010", "915094134", "932075839"};
    //final String[] correos = new String[]{"draftgipuzkoa@draft.com", "draftbizkaia@draft.com", "draftaraba@draft.com", "draftmalaga@draft.com", "draftmadrid@draft.com", "draftbarcelona@draft.com"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        LvPartners = (ListView) findViewById(R.id.LvPartners);
        tvPartners = (TextView) findViewById(R.id.tvPartners);
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvTfno = (TextView) findViewById(R.id.tvTfno);
        tvCorreo = (TextView) findViewById(R.id.tvCorreo);
        tvComAso = (TextView) findViewById(R.id.tvComAso);
        BtnNuevo = (Button) findViewById(R.id.BtnNuevo);
        BtnEditar = (Button) findViewById(R.id.BtnEditar);
        BtnBorrar = (Button) findViewById(R.id.BtnBorrar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_partners, partners);
        LvPartners.setAdapter(adapter);

        LvPartners.setOnItemClickListener((parent, view, position, id) -> {
            tvNombre.setText(LvPartners.getItemAtPosition(position).toString());
            tvTfno.setText("Telefono: " + telefonos[position]);
            tvCorreo.setText("Correo: " + correos[position]);
            tvComAso.setText("Comerciales asociados: " + comerciales[position]);
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == BtnNuevo.getId()) {
            discord();
        } else if (v.getId() == BtnEditar.getId()) {
            int i = LvPartners.getCheckedItemPosition();
            String nombre=partners[i];
            String telefono=telefonos[i];
            String correo = correos[i];
            String comercial=comerciales[i];
            discord(nombre, telefono, correo, comercial);
        } else if (v.getId() == BtnBorrar.getId()) {

        }

    }

    public void discord(){
        Intent carry = new Intent(this, new_edit_partners.class);
        startActivity(carry);
    }

    public void discord(String nombre, String telefono, String correo, String comercial){
        Intent carry = new Intent(this, new_edit_partners.class);
        carry.putExtra("nombre", nombre);
        carry.putExtra("telefono", telefono);
        carry.putExtra("correo", correo);
        carry.putExtra("comercial", comercial);
        startActivity(carry);
    }

}