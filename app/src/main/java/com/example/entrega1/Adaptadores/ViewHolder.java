package com.example.entrega1.Adaptadores;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega1.R;

// ViewHolder para el RecyclerView que muestra el catálogo de películas
public class ViewHolder extends RecyclerView.ViewHolder {

    // Atributos públicos para acceder a ellos desde el adaptador (AdaptadorRecycler)
    public String id;           // id de la película
    // Elementos del CardView que representa una película en el catálogo
    public ImageView portada;
    public TextView titulo;
    public TextView generos;
    public TextView fecha;
    public TextView puntuacion;
    public TextView idioma;
    public TextView sinopsis;
    public boolean[] seleccion;     // Array de booleanos para indicar qué elementos se han elegido

    // Contructor del ViewHolder
    public ViewHolder(@NonNull View itemView){
        super(itemView);

        // Inicialización de los elementos definidos en el layout CardView (item_layout.xml)
        portada = itemView.findViewById(R.id.imageViewPortada);
        titulo = itemView.findViewById(R.id.textViewTitulo);
        generos = itemView.findViewById(R.id.textViewGeneros);
        fecha = itemView.findViewById(R.id.textViewFechaTexto);
        puntuacion = itemView.findViewById(R.id.textViewPuntuacionTexto);
        idioma = itemView.findViewById(R.id.textViewIdiomaTexto);
        sinopsis = itemView.findViewById(R.id.textViewTextoSinopsis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sinopsis.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);       // Se justifica el texto dentro del TextView 'sinopsis'
        }
    }

}
