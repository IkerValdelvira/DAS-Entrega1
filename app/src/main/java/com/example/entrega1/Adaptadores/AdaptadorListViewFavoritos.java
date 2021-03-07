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
import com.example.entrega1.Dialogos.DialogoQuitarFavoritos;
import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

public class AdaptadorListViewFavoritos extends BaseAdapter{

    private FavoritosActivity contexto;
    private LayoutInflater inflater;
    private String listaSeleccionada;
    private String[] ids;
    private String[] portadas;
    private String[] titulos;

    private String usuario;

    public AdaptadorListViewFavoritos(String pUsuario, FavoritosActivity pContext, String pListaSeleccionada, String[] pIds, String[] pPortadas, String[] pTitulos)  {
        usuario = pUsuario;
        contexto = pContext;
        listaSeleccionada = pListaSeleccionada;
        ids = pIds;
        portadas = pPortadas;
        titulos = pTitulos;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public Object getItem(int i) {
        return titulos[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fila_favoritos,null);
        ImageView portadaFav = (ImageView) view.findViewById(R.id.imageViewPortadaFav);
        TextView tituloFav = (TextView) view.findViewById(R.id.textViewFechaFav);
        Button botonFav = (Button) view.findViewById(R.id.buttonFav);
        botonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogoQuitarFavoritos = new DialogoQuitarFavoritos(usuario, listaSeleccionada, ids[i], titulos[i]);
                dialogoQuitarFavoritos.show(contexto.getSupportFragmentManager(), "quitar_favoritos");
            }
        });

        tituloFav.setText(titulos[i]);
        Picasso.get().load(portadas[i]).into(portadaFav);
        return view;
    }

}
