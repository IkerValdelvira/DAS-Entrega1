package com.example.entrega1.Modelos;

// Clase que guarda la información necesaria para gestionar una alarma
public class Alarma {

    // Atributos necesarios para gestionar una alarma
    private String usuario;
    private String idPelicula;
    private int anyo;
    private int mes;
    private int dia;
    private String titulo;

    // Constructor de la clase
    public Alarma(String usuario, String idPelicula, int anyo, int mes, int dia, String titulo) {
        this.usuario = usuario;
        this.idPelicula = idPelicula;
        this.anyo = anyo;
        this.mes = mes;
        this.dia = dia;
    }

    // Getters y setters de la clase
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
