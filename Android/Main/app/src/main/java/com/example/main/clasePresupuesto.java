package com.example.main;

public class clasePresupuesto {

    //Atributos empleados para ayudar al PullParser a leer el XML creando lista de objetos de esta clase
    private String producto;
    private int cantidad;
    private float precio;
    private float coste;
    private String comercial;
    private String partner;
    private String id;
    private String fecha;

    public clasePresupuesto(String producto, int cantidad, float precio, float coste, String comercial, String partner, String id, String _fecha) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.coste = coste;
        this.comercial = comercial;
        this.partner = partner;
        this.id = id;
        this.fecha = _fecha;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "\nArtículo: " + producto + "\n  - Precio: " + precio + "€\n  - Cantidad: " + cantidad + "uds.\n  - Coste: " + coste + "€";
    }
}
