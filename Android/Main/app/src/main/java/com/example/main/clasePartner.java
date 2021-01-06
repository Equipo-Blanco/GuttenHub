package com.example.main;

public class clasePartner {
    private String nPartner;
    private String nComercial;
    private String nMail;
    private String nTelefono;

    public clasePartner() {
    }

    public clasePartner(String nPartner, String nComercial, String nMail, String nTelefono) {
        this.nPartner = nPartner;
        this.nComercial = nComercial;
        this.nMail = nMail;
        this.nTelefono = nTelefono;
    }

    public String getnPartner() {
        return nPartner;
    }

    public void setnPartner(String nPartner) {
        this.nPartner = nPartner;
    }

    public String getnComercial() {
        return nComercial;
    }

    public void setnComercial(String nComercial) {
        this.nComercial = nComercial;
    }

    public String getnMail() {
        return nMail;
    }

    public void setnMail(String nMail) {
        this.nMail = nMail;
    }

    public String getnTelefono() {
        return nTelefono;
    }

    public void setnTelefono(String nTelefono) {
        this.nTelefono = nTelefono;
    }

    @Override
    public String toString() {
        return nPartner;
    }
}
