package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.entrega1.Adaptadores.AdaptadorListView;
import com.example.entrega1.Dialogos.DialogoQuitarFavoritos;
import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

public class FavoritosActivity extends AppCompatActivity implements DialogoQuitarFavoritos.ListenerdelDialogo {

    private Spinner spinner;
    private ArrayAdapter<String> adaptadorSpinner;
    private int posSeleccionada;

    private ListView listView;

    private AdaptadorListView adaptadorListView;
    private String[] ids;
    private String[] portadas;
    private String[] titulos;

    private GestorDB gestorDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        spinner = findViewById(R.id.spinnerFavoritos);
        listView = findViewById(R.id.listViewFav);

        gestorDB = new GestorDB (this, "DB", null, 1);
        ArrayList<String> listas = gestorDB.getListasFavoritos();
        adaptadorSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listas);
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptadorSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String listaSeleccionada = listas.get(position);
                posSeleccionada = position;
                ids = gestorDB.getIdsListaFav(listaSeleccionada);
                portadas = gestorDB.getPortadasListaFav(listaSeleccionada);
                titulos = gestorDB.getTitulosListaFav(listaSeleccionada);
                adaptadorListView = new AdaptadorListView(FavoritosActivity.this,listaSeleccionada,ids,portadas,titulos);
                listView.setAdapter(adaptadorListView);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        Intent i = new Intent (FavoritosActivity.this, PeliculaActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("id", ids[position]);
                        view.getContext().startActivity(i);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    @Override
    public void alBorrarPelicula() {
        ArrayList<String> listas = gestorDB.getListasFavoritos();
        adaptadorSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listas);
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptadorSpinner);

        //spinner.setAdapter(adaptadorSpinner);
        if(listas.size() == 0) {
            listView.setAdapter(null);
        }
        else if(posSeleccionada == listas.size()) {
            spinner.setSelection(0);
        }
        else{
            spinner.setSelection(posSeleccionada);
        }
    }
}