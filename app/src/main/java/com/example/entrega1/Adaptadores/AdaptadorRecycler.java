package com.example.entrega1.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

public class AdaptadorRecycler extends RecyclerView.Adapter<ViewHolder> {

    private String[] ids;
    private String[] portadasURL;
    private String[] titulos;
    private String[] generos;
    private String[] fechas;
    private String[] puntuaciones;
    private String[] idiomas;
    private String[] sinopsis;
    private boolean[] seleccionados;

    public AdaptadorRecycler(String[] pIds, String[] pPortadasURL, String[] pTitulos, String[] pGeneros, String[] pFechas, String[] pPuntuaciones, String[] pIdiomas, String[] pSinopsis) {
        ids = pIds;
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
        View layoutDeCadaItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,null);
        ViewHolder viewHolder = new ViewHolder(layoutDeCadaItem);
        viewHolder.seleccion = seleccionados;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id = ids[position];
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
