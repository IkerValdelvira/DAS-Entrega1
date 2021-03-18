package com.example.entrega1.Modelos;

// Clase que guarda la información necesaria para gestionar una película
public class Pelicula {

    // Atributos necesarios para gestionar una película
    private String id;
    private String titulo;
    private String portadaURL;
    private String fechaVerMasTarde;

    // Primer constructor de la clase
    public Pelicula(String pId, String pTitulo, String pPortadaURL) {
        id = pId;
        titulo = pTitulo;
        portadaURL = pPortadaURL;
    }

    // Segundo constructor de la clase
    public Pelicula(String pId, String pTitulo, String pPortadaURL, String pFechaVMT) {
        id = pId;
        titulo = pTitulo;
        portadaURL = pPortadaURL;
        fechaVerMasTarde = pFechaVMT;
    }

    // Getters y setters de la clase
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPortadaURL() {
        return portadaURL;
    }

    public void setPortadaURL(String portadaURL) {
        this.portadaURL = portadaURL;
    }

    public String getFechaVerMasTarde() {
        return fechaVerMasTarde;
    }

    public void setFechaVerMasTarde(String fechaVerMasTarde) { this.fechaVerMasTarde = fechaVerMasTarde; }
}
