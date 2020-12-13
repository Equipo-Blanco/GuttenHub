package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bot_partners;
    Button bot_citas;
    Button bot_pedidos;
    Button bot_envios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bot_partners = (Button) findViewById(R.id.BtnPartners);

        Intent intentPartner = new  Intent(this, partners.class);

        bot_partners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentPartner);
            }
        });
    }
}