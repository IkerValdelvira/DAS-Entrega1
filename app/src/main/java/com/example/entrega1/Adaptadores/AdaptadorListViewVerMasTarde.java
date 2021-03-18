package com.example.entrega1.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.entrega1.Actividades.FavoritosActivity;
import com.example.entrega1.Actividades.VerMasTardeActivity;
import com.example.entrega1.Dialogos.DialogoQuitarFavoritos;
import com.example.entrega1.Dialogos.DialogoQuitarVerMasTarde;
import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

// Adaptador para la ListView personalizada de las peliculas 'ver más tarde'
public class AdaptadorListViewVerMasTarde extends BaseAdapter{

    private VerMasTardeActivity contexto;           // Contexto de la actividad que va a mostrar el ListView personalizado: VerMasTardeActivity
    private LayoutInflater inflater;                // Inflater para el layout que represente una fila de la lista
    // Datos que se quieren mostrar
    private String[] ids;
    private String[] portadas;
    private String[] titulos;
    private String[] fechas;

    private String usuario;                         // Nombre de usuario actual

    // Constructor del adaptador
    public AdaptadorListViewVerMasTarde(String pUsuario, VerMasTardeActivity pContext, String[] pIds, String[] pPortadas, String[] pTitulos, String[] pFechas)  {
        usuario = pUsuario;
        contexto = pContext;
        ids = pIds;
        portadas = pPortadas;
        titulos = pTitulos;
        fechas = pFechas;
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
        view = inflater.inflate(R.layout.fila_ver_mas_tarde,null);          // Se indica el layout para cada elemento: 'fila_ver_mas_tarde.xml'
        // Se obtienen los elementos del layout
        ImageView portadaVMT = view.findViewById(R.id.imageViewPortadaVMT);
        TextView tituloVMT = view.findViewById(R.id.textViewTituloVMT);
        TextView fechaVMT = view.findViewById(R.id.textViewFechaVMT);
        Button botonQuitarVMT = view.findViewById(R.id.buttonVMT);
        // Listener 'onClick' del botón del layout para quitar la película de la lista 'ver mas tarde'
        botonQuitarVMT.setOnClickListener(new View.OnClickListener() {
            // Se ejecuta al pulsar el botón del layout para quitar la película de la lista de 'ver mas tarde'
            @Override
            public void onClick(View v) {
                // Se crea un diálogo preguntando si se quiere quitar realmente la película de la lista 'ver mas tarde': DialogoQuitarFavoritos
                DialogFragment dialogoQuitarVerMasTarde = new DialogoQuitarVerMasTarde(usuario, ids[i], fechas[i]);
                dialogoQuitarVerMasTarde.show(contexto.getSupportFragmentManager(), "quitar_ver_mas_tarde");
            }
        });

        // Se asigna a cada variable el contenido que se quiere mostrar en ese elemento: título, portada de la película y fecha de 'ver más tarde'
        tituloVMT.setText(titulos[i]);
        Picasso.get().load(portadas[i]).into(portadaVMT);           // Picasso: librería para añadir una imagen desde una URL a un ImageView
        fechaVMT.setText(fechas[i]);
        return view;
    }

}
