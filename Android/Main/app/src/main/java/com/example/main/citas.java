package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class citas extends AppCompatActivity {

    TextView diaSelec;
    CalendarView calendario;
    FloatingActionButton bot_nuevo;
    private String curDate;
    private int dia, mes, anio;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        diaSelec = (TextView) findViewById(R.id.tvDia);
        calendario = (CalendarView) findViewById(R.id.viewcalendario);
        bot_nuevo = (FloatingActionButton) findViewById(R.id.BtnNuevo);
        bot_nuevo.setEnabled(false);
        Intent intent = new Intent(this, NuevaCita.class);

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
                fecha = dia +"/" +mes +"/" +anio;
                intent.putExtra("fechacita", fecha);
                startActivity(intent);
            }
        });
    }
}