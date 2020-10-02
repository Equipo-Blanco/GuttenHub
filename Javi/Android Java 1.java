
//El codigo xml es el del ejercicio anterior

package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        TextView texto = new TextView(this);
        texto.setText("Hello, Android");
        setContentView(texto);
    }
}