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

public class AdaptadorListViewVerMasTarde extends BaseAdapter{

    private VerMasTardeActivity contexto;
    private LayoutInflater inflater;
    private String[] ids;
    private String[] portadas;
    private String[] titulos;
    private String[] fechas;

    public AdaptadorListViewVerMasTarde(VerMasTardeActivity pContext, String[] pIds, String[] pPortadas, String[] pTitulos, String[] pFechas)  {
        contexto = pContext;
        ids = pIds;
        portadas = pPortadas;
        titulos = pTitulos;
        fechas = pFechas;
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
        view = inflater.inflate(R.layout.fila_ver_mas_tarde,null);
        ImageView portadaVMT = view.findViewById(R.id.imageViewPortadaVMT);
        TextView tituloVMT = view.findViewById(R.id.textViewTituloVMT);
        TextView fechaVMT = view.findViewById(R.id.textViewFechaVMT);
        Button botonVMT = view.findViewById(R.id.buttonVMT);
        botonVMT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(ids[i] + "   " + titulos[i]);
                DialogFragment dialogoQuitarVerMasTarde = new DialogoQuitarVerMasTarde(ids[i], fechas[i], titulos[i]);
                dialogoQuitarVerMasTarde.show(contexto.getSupportFragmentManager(), "quitar_ver_mas_tarde");
            }
        });

        tituloVMT.setText(titulos[i]);
        Picasso.get().load(portadas[i]).into(portadaVMT);
        fechaVMT.setText(fechas[i]);
        return view;
    }

}
