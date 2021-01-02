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

public class partners extends AppCompatActivity implements View.OnClickListener{

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

        LvPartners.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvNombre.setText(LvPartners.getItemAtPosition(position).toString());
                tvTfno.setText("Telefono: " + telefonos[position]);
                tvCorreo.setText("Correo: " + correos[position]);
                tvComAso.setText("Comerciales asociados: " + comerciales[position]);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == BtnNuevo.getId()){

        } else if (v.getId() == BtnEditar.getId()){

        } else if(v.getId() == BtnBorrar.getId()){

        }

    }

    public void discord(){
        Intent carry = new Intent(this, new_edit_partners.class);

        if (t1.length()>0){
            String texto = t1.getText().toString();
            System.out.println(texto);
            carry.putExtra(BOX, texto);
            startActivity(carry);

        } else {
            tostada("Introduce texto");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==7) {
            if(resultCode== activity2.RESULT_OK){
                String resultado=data.getStringExtra(BOX);
                txt.setText(resultado);
            }
        }

    }

}