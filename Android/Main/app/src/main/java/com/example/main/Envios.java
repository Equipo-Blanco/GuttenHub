package com.example.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Envios extends AppCompatActivity {
    EditText et_destinatario;
    EditText et_asunto;
    EditText et_mensaje;
    Button bot_enviar;
    Button bot_adjuntar;
    String email;
    String asunto;
    String mensaje;
    TextView tv_Adjunto;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios);

        et_destinatario = findViewById(R.id.etDestinatario);
        et_asunto = findViewById(R.id.etAsunto);
        et_mensaje = findViewById(R.id.etMensaje);
        bot_adjuntar = findViewById(R.id.btnAdjuntar);
        tv_Adjunto = findViewById(R.id.tvAdjunto);

        bot_enviar = findViewById(R.id.btnEnviar);
        bot_enviar.setEnabled(false);
        et_destinatario.setText("draft@draft.com");

        bot_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMail();
            }
        });
        //attachment button listener
        bot_adjuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionaArchivo();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            URI = data.getData();
            tv_Adjunto.setText(URI.getLastPathSegment());
            tv_Adjunto.setVisibility(View.VISIBLE);
        }
    }

    public void enviarMail() {
        try {
            email = getDatos(et_destinatario);
            asunto = getDatos(et_asunto);
            mensaje = getDatos(et_mensaje);

            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, asunto);
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void seleccionaArchivo() {
        Intent intent = new Intent();
        intent.setType("text/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        bot_enviar.setEnabled(true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
    }

    //Obtiene texto de los campos y valida
    public String getDatos(EditText etxt) {
        String dato;

        if (etxt.length() > 0) {
            dato = etxt.getText().toString();
        } else {
            dato = "";
        }
        return dato;
    }
}