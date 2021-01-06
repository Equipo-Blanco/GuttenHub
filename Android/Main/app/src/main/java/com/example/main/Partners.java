package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class Partners extends AppCompatActivity {

    private ListView LvPartners;
    private TextView tvTfno;
    private TextView tvNombrePartner;
    private TextView tvCorreo;
    private TextView tvComAso;
    private Button bot_Nuevo;
    private Button bot_Editar;
    List<clasePartner> ListPartners;

    private String partners[] = {"DECATHLON", "Sport4U", "FORUM", "Murica Sports", "Deportes Manolo", "EA Sports", "Touchdown SL"};
    private String telefonos[] = {"681393521", "681393522", "681393523", "681393524", "681393525", "681393526", "681393527"};
    private String correos[] = {"sergio@draft.com", "hodei@draft.com", "edgar@draft.com", "lovecraft@draft.com", "roland@draft.com", "rita@draft.com", "jackson@draft.com"};
    private String comerciales[] = {"Sergio Luhía", "Hodei Aguirre", "Edgar Allan Poe", "H.P. Lovecraft", "Roland Banks", "Rita Young", "Jackson Teller"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        LvPartners = (ListView) findViewById(R.id.lvPartners);
        tvTfno = (TextView) findViewById(R.id.tvTfno);
        tvCorreo = (TextView) findViewById(R.id.tvCorreo);
        tvComAso = (TextView) findViewById(R.id.tvComAso);
        bot_Nuevo = (Button) findViewById(R.id.btnNuevo);
        bot_Editar = (Button) findViewById(R.id.btnEditar);
        tvNombrePartner = (TextView) findViewById(R.id.tv_nombrePartner);

        XMLPPPartners parser = new XMLPPPartners();
        ListPartners = parser.parseXML(this);

        for (clasePartner p : ListPartners){
            System.out.println("****************************A*******************");
            System.out.println(p.toString());
        }

        ArrayAdapter<clasePartner> adapter = new ArrayAdapter<clasePartner>(this, android.R.layout.simple_expandable_list_item_1, ListPartners);
        LvPartners.setAdapter(adapter);

        LvPartners.setOnItemClickListener((parent, view, position, id) -> {
            tvNombrePartner.setText(ListPartners.get(position).getnPartner());
            tvCorreo.setText(ListPartners.get(position).getnMail());
            tvTfno.setText(ListPartners.get(position).getnTelefono());
            tvComAso.setText(ListPartners.get(position).getnComercial());
        });

        Intent intent = new Intent(this, new_edit_partners.class);
        bot_Nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }
}