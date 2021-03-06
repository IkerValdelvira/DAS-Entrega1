package com.example.entrega1.Adaptadores;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega1.Actividades.PeliculaActivity;
import com.example.entrega1.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public String id;
    public ImageView portada;
    public TextView titulo;
    public TextView generos;
    public TextView fecha;
    public TextView puntuacion;
    public TextView idioma;
    public TextView sinopsis;
    public boolean[] seleccion;

    public ViewHolder(@NonNull View itemView){
        super(itemView);
        portada = itemView.findViewById(R.id.imageViewPortada);
        titulo = itemView.findViewById(R.id.textViewTitulo);
        generos = itemView.findViewById(R.id.textViewGeneros);
        fecha = itemView.findViewById(R.id.textViewFechaTexto);
        puntuacion = itemView.findViewById(R.id.textViewPuntuacionTexto);
        idioma = itemView.findViewById(R.id.textViewIdiomaTexto);
        sinopsis = itemView.findViewById(R.id.textViewTextoSinopsis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sinopsis.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (view.getContext(), PeliculaActivity.class);
                i.putExtra("id", id);
                view.getContext().startActivity(i);
            }
        });
    }

}
