package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Pedidos extends AppCompatActivity {

    private static final String TAG = "MainPedidos";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    Button bot_catalogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        Log.d(TAG, "OnCreate: Starting.");
        bot_catalogo = (Button) findViewById(R.id.btn_catalogo);

        Intent intento = new Intent(this, Catalogo.class);

        bot_catalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intento);
            }
        });

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        //Prepara los ViewPager con sus secciones

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        setUpViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1_NuevoPedido(), "Nuevo Pedido");
        adapter.addFragment(new Tab2_pedidos_hechos(), "Pedidos Existentes");

        viewPager.setAdapter(adapter);
    }
}