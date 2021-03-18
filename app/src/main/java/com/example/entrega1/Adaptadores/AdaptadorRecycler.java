
package com.example.entrega1.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega1.Fragments.CatalogoFragment;
import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

// Adaptador para el RecyclerView del catálogo de películas
public class AdaptadorRecycler extends RecyclerView.Adapter<ViewHolder> {

    // Listener del fragmente de catálogo de películas --> Se utiliza para ejecutar un método del fragment del catálogo (CatalogoFragment) cuando se pulse en un item del RecyclerView
    private CatalogoFragment.ListenerFragment listenerFragment;

    // Datos que se quieren mostrar
    private String[] ids;
    private String[] portadasURL;
    private String[] titulos;
    private String[] generos;
    private String[] fechas;
    private String[] puntuaciones;
    private String[] idiomas;
    private String[] sinopsis;
    private boolean[] seleccionados;            // Array de booleanos para indicar qué elementos se han elegido

    private String usuario;                     // Nombre de usuario actual

    // Constructor del adaptador
    public AdaptadorRecycler(CatalogoFragment.ListenerFragment pListenerFragment, String pUsuario, String[] pIds, String[] pPortadasURL, String[] pTitulos, String[] pGeneros, String[] pFechas, String[] pPuntuaciones, String[] pIdiomas, String[] pSinopsis) {
        listenerFragment = pListenerFragment;
        usuario = pUsuario;
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

    // 'Infla' el layout definido para cada elemento (item_layout.xml) y crea y devuelve una instancia de ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutDeCadaItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,null);
        ViewHolder viewHolder = new ViewHolder(layoutDeCadaItem);
        viewHolder.seleccion = seleccionados;
        return viewHolder;
    }

    // Asigna a los atributos del ViewHolder los valores a mostrar para una posición concreta
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id = ids[position];
        Picasso.get().load(portadasURL[position]).into(holder.portada);         // Picasso: librería para añadir una imagen desde una URL a un ImageView
        holder.titulo.setText(titulos[position]);
        holder.generos.setText(generos[position]);
        holder.fecha.setText(fechas[position]);
        holder.puntuacion.setText(puntuaciones[position]);
        holder.idioma.setText(idiomas[position]);
        holder.sinopsis.setText(sinopsis[position]);

        // Listener 'onClick' para cada View
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            // Se ejecuta al pulsar en un itemView (cuando se pulsa en un CardView que contiene la información de una película)
            @Override
            public void onClick(View view) {
                // Llama al método del fragment del catálogo (CatalogoFragment) para realizar una acción con el elemento seleccionado
                listenerFragment.seleccionarElemento(usuario, holder.id);
            }
        });
    }

    // Devuelve la cantidad de datos total a mostrar
    @Override
    public int getItemCount() {
        return titulos.length;
    }
}