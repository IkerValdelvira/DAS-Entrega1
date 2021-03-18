package com.example.entrega1.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.entrega1.Actividades.ComentariosActivity;
import com.example.entrega1.Actividades.FavoritosActivity;
import com.example.entrega1.Dialogos.DialogoQuitarFavoritos;
import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

// Adaptador para la ListView personalizada de las peliculas favoritas
public class AdaptadorListViewFavoritos extends BaseAdapter{

    private FavoritosActivity contexto;         // Contexto de la actividad que va a mostrar el ListView personalizado: FavoritosActivity
    private LayoutInflater inflater;            // Inflater para el layout que represente una fila de la lista
    private String listaSeleccionada;           // Nombre de la lista que se va a mostrar
    // Datos que se quieren mostrar
    private String[] ids;
    private String[] portadas;
    private String[] titulos;

    private String usuario;                     // Nombre de usuario actual

    // Constructor del adaptador
    public AdaptadorListViewFavoritos(String pUsuario, FavoritosActivity pContext, String pListaSeleccionada, String[] pIds, String[] pPortadas, String[] pTitulos)  {
        usuario = pUsuario;
        contexto = pContext;
        listaSeleccionada = pListaSeleccionada;
        ids = pIds;
        portadas = pPortadas;
        titulos = pTitulos;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Método sobrescrito de la clase BaseAdapter --> Devuelve el número de elementos
    @Override
    public int getCount() {
        return titulos.length;
    }

    // Método sobrescrito de la clase BaseAdapter --> Devuelve el elemento i
    @Override
    public Object getItem(int i) {
        return titulos[i];
    }

    // Método sobrescrito de la clase BaseAdapter --> Devuelve el identificador del elemento i
    @Override
    public long getItemId(int i) {
        return i;
    }

    // Método sobrescrito de la clase BaseAdapter --> Devuelve cómo se visualiza un elemento
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fila_favoritos,null);      // Se indica el layout para cada elemento: 'fila_favoritos.xml'
        // Se obtienen los elementos del layout
        ImageView portadaFav = (ImageView) view.findViewById(R.id.imageViewPortadaFav);
        TextView tituloFav = (TextView) view.findViewById(R.id.textViewTituloFav);
        Button botonQuitarFav = (Button) view.findViewById(R.id.buttonQuitarFav);
        // Listener 'onClick' del botón del layout para quitar la película de la lista de favoritos
        botonQuitarFav.setOnClickListener(new View.OnClickListener() {
            // Se ejecuta al pulsar el botón del layout para quitar la película de la lista de favoritos
            @Override
            public void onClick(View v) {
                // Se crea un diálogo preguntando si se quiere quitar realmente la película de la lista de favoritos seleccionada: DialogoQuitarFavoritos
                DialogFragment dialogoQuitarFavoritos = new DialogoQuitarFavoritos(usuario, listaSeleccionada, ids[i]);
                dialogoQuitarFavoritos.show(contexto.getSupportFragmentManager(), "quitar_favoritos");
            }
        });
        Button botonComentariosFav = (Button) view.findViewById(R.id.buttonComentariosFav);
        // Listener 'onClick' del botón del layout para escribir/leer comentarios de la película de la lista de favoritos
        botonComentariosFav.setOnClickListener(new View.OnClickListener() {
            // Se ejecuta al pulsar el botón del layout para escribir/leer comentarios de la película de la lista de favoritos
            @Override
            public void onClick(View v) {
                // Se crea una nueva actividad 'ComentariosActivity' con la película seleccionada
                Intent intent = new Intent(contexto, ComentariosActivity.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("id", ids[i]);
                intent.putExtra("portada", portadas[i]);
                intent.putExtra("titulo", titulos[i]);
                contexto.startActivity(intent);
            }
        });

        // Se asigna a cada variable el contenido que se quiere mostrar en ese elemento: título y portada de la película
        tituloFav.setText(titulos[i]);
        Picasso.get().load(portadas[i]).into(portadaFav);       // Picasso: librería para añadir una imagen desde una URL a un ImageView
        return view;
    }

}
