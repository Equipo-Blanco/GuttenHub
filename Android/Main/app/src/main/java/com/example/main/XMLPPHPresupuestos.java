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

public class XMLPPHPresupuestos {
    private static final String PRESUP_PRODUCTO = "producto";
    private static final String PREUSP_CANTIDAD = "cantidad";
    private static final String PRESUP_PRECIO = "precio";
    private static final String PRESUP_COSTE = "coste";
    private static final String PRESUP_PARTNER = "partner";
    private static final String PRESUP_COMERCIAL = "comercial";


    private clasePresupuesto presupActual = null;
    private String currentTag = null;
    List<clasePresupuesto> ListaPresupuestos = new ArrayList<>();

    public List<clasePresupuesto> parseXML(Context context, String nombreArchivo) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            InputStream stream = new FileInputStream(new File("/storage/emulated/0//DraftPresupuestos/" + nombreArchivo));
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
        return ListaPresupuestos;
    }

    private void handleText(String text) {
        String xmlText = text;
        if (presupActual != null && currentTag != null) {
            if (currentTag.equals(PRESUP_PRODUCTO)) {
                presupActual.setProducto(xmlText);
            } else if (currentTag.equals(PREUSP_CANTIDAD)) {
                presupActual.setCantidad(Integer.parseInt(xmlText));
            } else if (currentTag.equals(PRESUP_PRECIO)) {
                presupActual.setPrecio(Float.parseFloat(xmlText));
            } else if (currentTag.equals(PRESUP_COSTE)){
                presupActual.setCoste(Float.parseFloat(xmlText));
            }else if (currentTag.equals(PRESUP_PARTNER)){
                presupActual.setPartner(xmlText);
            }else if (currentTag.equals(PRESUP_COMERCIAL)){
                presupActual.setComercial(xmlText);
            }
        }
    }

    private void handleStartTag(String name) {
        if (name.equals("articulo")) {
            presupActual = new clasePresupuesto();
            ListaPresupuestos.add(presupActual);
        } else {
            currentTag = name;
        }
    }
}
