package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class citas extends AppCompatActivity {

    TextView diaSelec;
    CalendarView calendario;
    FloatingActionButton bot_nuevo;
    private String curDate;
    private int dia, mes, anio;
    private String fecha;
    ListView listView;
    List<ClaseCita> citasExistentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        diaSelec = (TextView) findViewById(R.id.tvDia);
        calendario = (CalendarView) findViewById(R.id.viewcalendario);
        bot_nuevo = (FloatingActionButton) findViewById(R.id.BtnNuevo);
        bot_nuevo.setEnabled(false);
        listView = (ListView) findViewById(R.id.listaCitas);
        Intent intent = new Intent(this, NuevaCita.class);

        XMLPullParserHandler parser = new XMLPullParserHandler();
        citasExistentes = parser.parseXML(this);

        ArrayAdapter<ClaseCita> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, citasExistentes);
        listView.setAdapter(adapter);

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                month = month + 1; //Los meses los toma de 0 a 11 por lo que hay que sumar +1
                curDate = "Fecha seleccionada: " + dayOfMonth + "/" + month + "/" + year;
                diaSelec.setText(curDate);
                dia = dayOfMonth;
                mes = month;
                anio = year;
                bot_nuevo.setEnabled(true);
            }
        });

        bot_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fecha = dia + "/" + mes + "/" + anio;
                intent.putExtra("fechacita", fecha);
                startActivity(intent);
            }
        });
    }
}