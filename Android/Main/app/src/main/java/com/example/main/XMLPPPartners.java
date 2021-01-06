package com.example.main;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLPPPartners {

    private static final String PARTN_PARTNER = "nombre";
    private static final String PARTN_COMERCIAL = "comercial";
    private static final String PARTN_TELEFONO = "telefono";
    private static final String PARTN_MAIL = "mail";


    private clasePartner partnerActual = null;
    private String currentTag = null;
    List<clasePartner> ListaPartners = new ArrayList<>();

    public List<clasePartner> parseXML(Context context) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            InputStream stream = new FileInputStream(new File("/storage/emulated/0/Draft/partnersGuardados.xml"));
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
        return ListaPartners;
    }

    private void handleText(String text) {
        String xmlText = text;
        if (partnerActual != null && currentTag != null) {
            if (currentTag.equals(PARTN_PARTNER)) {
                partnerActual.setnPartner(xmlText);
                System.out.println("PARTNER ******************************************" +xmlText);
            } else if (currentTag.equals(PARTN_COMERCIAL)) {
                partnerActual.setnComercial(xmlText);
            } else if (currentTag.equals(PARTN_MAIL)) {
                partnerActual.setnMail(xmlText);
            } else if (currentTag.equals(PARTN_TELEFONO)) {
                partnerActual.setnTelefono(xmlText);
            }
        }
    }

    private void handleStartTag(String name) {
        if (name.equals("partner")) {
            partnerActual = new clasePartner();
            ListaPartners.add(partnerActual);
        } else {
            currentTag = name;
        }
    }
}
