package com.example.entrega1.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.entrega1.Adaptadores.AdaptadorListViewFavoritos;
import com.example.entrega1.Adaptadores.AdaptadorListViewVerMasTarde;
import com.example.entrega1.Dialogos.DialogoQuitarFavoritos;
import com.example.entrega1.Dialogos.DialogoQuitarVerMasTarde;
import com.example.entrega1.GestorDB;
import com.example.entrega1.Modelos.Pelicula;
import com.example.entrega1.R;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

public class VerMasTardeActivity extends AppCompatActivity implements DialogoQuitarVerMasTarde.ListenerdelDialogo {

    private ListView listView;

    private AdaptadorListViewVerMasTarde adaptadorListView;
    private String[] ids;
    private String[] portadas;
    private String[] titulos;
    private String[] fechas;

    private GestorDB gestorDB;

    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mas_tarde);

        listView = findViewById(R.id.listViewVMT);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        gestorDB = new GestorDB (this, "DB", null, 1);

        Pelicula[] peliculas = gestorDB.getPeliculasVMT(usuario);
        ids = new String[peliculas.length];
        portadas = new String[peliculas.length];
        titulos = new String[peliculas.length];
        fechas = new String[peliculas.length];
        for(int i=0; i<peliculas.length; i++) {
            ids[i] = peliculas[i].getId();
            portadas[i] = peliculas[i].getPortadaURL();
            titulos[i] = peliculas[i].getTitulo();
            fechas[i] = peliculas[i].getFechaVerMasTarde();
        }
        adaptadorListView = new AdaptadorListViewVerMasTarde(usuario, VerMasTardeActivity.this,ids,portadas,titulos,fechas);
        listView.setAdapter(adaptadorListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent i = new Intent (VerMasTardeActivity.this, PeliculaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id", ids[position]);
                i.putExtra("usuario", usuario);
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public void alBorrarPelicula() {
        Pelicula[] peliculas = gestorDB.getPeliculasVMT(usuario);
        ids = new String[peliculas.length];
        portadas = new String[peliculas.length];
        titulos = new String[peliculas.length];
        fechas = new String[peliculas.length];
        for(int i=0; i<peliculas.length; i++) {
            ids[i] = peliculas[i].getId();
            portadas[i] = peliculas[i].getPortadaURL();
            titulos[i] = peliculas[i].getTitulo();
            fechas[i] = peliculas[i].getFechaVerMasTarde();
        }
        adaptadorListView = new AdaptadorListViewVerMasTarde(usuario, VerMasTardeActivity.this,ids,portadas,titulos,fechas);
        listView.setAdapter(adaptadorListView);
    }

}