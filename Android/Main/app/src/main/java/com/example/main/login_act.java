package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login_act extends AppCompatActivity {
    EditText etUsr;
    EditText etPwd;
    Button btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_act);

        etUsr = (EditText) findViewById((R.id.etUsr));
        etPwd = (EditText) findViewById((R.id.etPwd));
        btnLog = (Button) findViewById(R.id.btnLog);

        //SQL: creación de tablas
        tablasSQLHelper miDb = new tablasSQLHelper(this, "DBDraft", null, 1);
        SQLiteDatabase db = miDb.getWritableDatabase();

        btnLog.setOnClickListener(view -> {
            try {
                if (etUsr.getText().length() > 0 || etPwd.getText().length() > 0) {
                    String usr = etUsr.getText().toString();
                    String pwd = etPwd.getText().toString();

                    Cursor cur = db.rawQuery("SELECT NOMBRE, CONTRASEN FROM LOGINUSUARIOS WHERE NOMBRE='" + usr + "' AND CONTRASEN='" + pwd + "'", null);
                    if (cur.moveToFirst()) {
                        aceptar();
                    } else {
                        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Debe completar ambos campos para iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    public void aceptar() {
        String a = etUsr.getText().toString();

        Intent back = new Intent(this, MainActivity.class);
        back.putExtra("username", a);
        setResult(MainActivity.RESULT_OK, back);
        finish();
    }
}