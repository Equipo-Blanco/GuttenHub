package com.example.main;

public class ClaseCita {

    private String fechaCita;
    private String tituloCita;
    private String descripCita;

    public ClaseCita(String fechaCita, String tituloCita, String descripCita) {
        this.fechaCita = fechaCita;
        this.tituloCita = tituloCita;
        this.descripCita = descripCita;
    }

    public ClaseCita() {

    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getTituloCita() {
        return tituloCita;
    }

    public void setTituloCita(String tituloCita) {
        this.tituloCita = tituloCita;
    }

    public String getDescripCita() {
        return descripCita;
    }

    public void setDescripCita(String descripCita) {
        this.descripCita = descripCita;
    }

    @Override
    public String toString() {
        return "Título: " + tituloCita +
                "\nFecha: " + fechaCita;
    }
}
