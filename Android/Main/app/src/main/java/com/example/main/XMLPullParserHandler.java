package com.example.main;

import android.content.Context;
import android.os.Environment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLPullParserHandler {
    private static final String CITA_FECHA = "fecha";
    private static final String CITA_TITULO = "titulo";
    private static final String CITA_DESCRIPCION = "descrip";


    private ClaseCita citaActual = null;
    private String currentTag = null;
    List<ClaseCita> ListaCitas = new ArrayList<>();

    public List<ClaseCita> parseXML(Context context) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            //InputStream stream = context.getAssets().open("citasGuardadas.xml");
            //InputStream stream = new FileInputStream(new File( "/storage/emulated/0/Draft/citasGuardadas.xml"));
            InputStream stream = new FileInputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Draft/citasGuardadas.xml"));
            xpp.setInput(stream, null);

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTag(xpp.getName());
                } else if (eventType == XmlPullParser.END_TAG) {
                    currentTag = null;
                } else if (eventType == XmlPullParser.TEXT) {
                    handleText(xpp.getText());
                }
                eventType = xpp.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return ListaCitas;
    }

    private void handleText(String text) {
        String xmlText = text;
        if (citaActual != null && currentTag != null) {
            if (currentTag.equals(CITA_FECHA)) {
                citaActual.setFechaCita(xmlText);
            } else if (currentTag.equals(CITA_TITULO)) {
                citaActual.setTituloCita(xmlText);
            } else if (currentTag.equals(CITA_DESCRIPCION)) {
                citaActual.setDescripCita(xmlText);
            }
        }
    }

    private void handleStartTag(String name) {
        if (name.equals("cita")) {
            citaActual = new ClaseCita();
            ListaCitas.add(citaActual);
        } else {
            currentTag = name;
        }
    }
}
