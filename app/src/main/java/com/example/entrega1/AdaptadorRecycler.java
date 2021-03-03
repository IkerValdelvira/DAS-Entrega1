package com.example.entrega1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class AdaptadorRecycler extends RecyclerView.Adapter<ViewHolder> {

    private String[] portadasURL;
    private String[] titulos;
    private String[] generos;
    private String[] fechas;
    private String[] puntuaciones;
    private String[] idiomas;
    private String[] sinopsis;
    private boolean[] seleccionados;

    public AdaptadorRecycler(String[] pPortadasURL, String[] pTitulos, String[] pGeneros, String[] pFechas, String[] pPuntuaciones, String[] pIdiomas, String[] pSinopsis) {
        portadasURL = pPortadasURL;
        titulos = pTitulos;
        generos = pGeneros;
        fechas = pFechas;
        puntuaciones = pPuntuaciones;
        idiomas = pIdiomas;
        sinopsis = pSinopsis;
        seleccionados = new boolean[titulos.length];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elLayoutDeCadaItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,null);
        ViewHolder evh = new ViewHolder(elLayoutDeCadaItem);
        evh.seleccion = seleccionados;
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(portadasURL[position]).into(holder.portada);
        holder.titulo.setText(titulos[position]);
        holder.generos.setText(generos[position]);
        holder.fecha.setText(fechas[position]);
        holder.puntuacion.setText(puntuaciones[position]);
        holder.idioma.setText(idiomas[position]);
        holder.sinopsis.setText(sinopsis[position]);
    }

    @Override
    public int getItemCount() {
        return titulos.length;
    }
}
