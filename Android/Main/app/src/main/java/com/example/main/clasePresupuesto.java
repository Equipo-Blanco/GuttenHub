package com.example.main;

public class clasePresupuesto {

    private String producto;
    private int cantidad;
    private float precio;
    private float coste;
    private String comercial;
    private String partner;

    public clasePresupuesto(String producto, int cantidad, float precio, float coste, String comercial, String partner) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.coste = coste;
        this.comercial = comercial;
        this.partner = partner;
    }

    public clasePresupuesto() {

    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public float getCoste() {
        return coste;
    }

    public void setCoste(float coste) {
        this.coste = coste;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getPartner() {
        return partner;
    }

    public void setComercial(String comercial) {
        this.comercial = comercial;
    }

    public String getComercial() {
        return comercial;
    }

    @Override
    public String toString() { //FALTA MOSTRAR EL COMERCIAL Y PARTNER
        return "\nArtículo: " + producto + "\n  - Precio: " + precio + "€\n  - Cantidad: " + cantidad + "uds.\n  - Coste: " + coste + "€";
    }
}
