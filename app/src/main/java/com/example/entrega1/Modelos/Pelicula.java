package com.example.entrega1.Modelos;

public class Pelicula {

    private String id;
    private String titulo;
    private String portadaURL;
    private String fechaVerMasTarde;

    public Pelicula(String pId, String pTitulo, String pPortadaURL) {
        id = pId;
        titulo = pTitulo;
        portadaURL = pPortadaURL;
    }

    public Pelicula(String pId, String pTitulo, String pPortadaURL, String pFechaVMT) {
        id = pId;
        titulo = pTitulo;
        portadaURL = pPortadaURL;
        fechaVerMasTarde = pFechaVMT;
    }

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

    public void setFechaVerMasTarde(String fechaVerMasTarde) {
        this.fechaVerMasTarde = fechaVerMasTarde;
    }
}
