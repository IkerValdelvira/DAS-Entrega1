package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.entrega1.Adaptadores.AdaptadorListViewFavoritos;
import com.example.entrega1.Dialogos.DialogoQuitarFavoritos;
import com.example.entrega1.GestorDB;
import com.example.entrega1.Modelos.Pelicula;
import com.example.entrega1.R;

import java.util.ArrayList;
import java.util.Locale;

// Actividad que muestra las listas de películas favoritas del usuario mediante ListView-s personalizados
public class FavoritosActivity extends AppCompatActivity implements DialogoQuitarFavoritos.ListenerdelDialogo {

    // Spinner para la selección del ListView personalizado que se quiere mostrar
    private Spinner spinner;
    private ArrayAdapter<String> adaptadorSpinner;
    private int posSeleccionada;

    // ListView personalizado para mostrar las películas favoritas de una lista del usuario
    private ListView listView;
    private AdaptadorListViewFavoritos adaptadorListView;
    private String[] ids;
    private String[] portadas;
    private String[] titulos;

    private GestorDB gestorDB;          // Instancia para realizar la comunicación con la base de datos local

    private String usuario;             // Nombre del usuario que ha creado la actividad

    // Se ejecuta al crearse la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Acceso al las preferencias para obtener el valor de 'idioma'
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma", "es");

        // Crear nueva localización con el idioma recogido de las preferencias (necesario para mantener el idioma tras cambio de orientacion del dispositivo)
        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        setContentView(R.layout.activity_favoritos);

        // Inicialización de los elementos 'spinner' y 'listView' del layout 'activity_favoritos.xml'
        spinner = findViewById(R.id.spinnerFavoritos);
        listView = findViewById(R.id.listViewFav);

        // Inicialización del nombre de usuario obtenido a través del Bundle asociado al Intent que ha creado la actividad
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        gestorDB = new GestorDB (this, "DB", null, 1);      // Inicialización de la instancia para la comunicación con la base de datos local 'DB'
        ArrayList<String> listas = gestorDB.getListasFavoritos(usuario);                 // Se llama al método para obtener los nombres de las listas de favoritos del usuario

        // Inicialización el adaptador del spinner con los nombres de las listas de favoritos recibidos de la base de datos
        adaptadorSpinner = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_selected_layout, listas);
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptadorSpinner);

        // Listener al seleccionar un elemento del Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Se ejecuta al seleccionar un elemento del Spinner
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String listaSeleccionada = listas.get(position);    // Se obtiene el nombre de la lista seleccionada
                posSeleccionada = position;
                Pelicula[] peliculas = gestorDB.getPeliculasListaFav(usuario, listaSeleccionada);   // Se obtienen las películas dentro de la lista seleccionada desde la base de datos local
                // Se crean los Arrays necesarios para el adaptador del ListView para guardar la siguiente información de cada película: id, URL de la portada y título
                ids = new String[peliculas.length];
                portadas = new String[peliculas.length];
                titulos = new String[peliculas.length];
                for(int i=0; i<peliculas.length; i++) {
                    ids[i] = peliculas[i].getId();
                    portadas[i] = peliculas[i].getPortadaURL();
                    titulos[i] = peliculas[i].getTitulo();
                }
                adaptadorListView = new AdaptadorListViewFavoritos(usuario, FavoritosActivity.this,listaSeleccionada,ids,portadas,titulos);
                listView.setAdapter(adaptadorListView);

                // Listener al hacer click en un fila (película) del ListView
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    // Se ejecuta al hacer click en un fila (película) del ListView
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        // Se crea una nueva actividad 'PeliculaActivity' con la película seleccionada
                        Intent i = new Intent (FavoritosActivity.this, PeliculaActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("usuario", usuario);
                        i.putExtra("id", ids[position]);
                        view.getContext().startActivity(i);
                    }
                });
            }

            // Se ejecuta cuando no hay ningún elemento del Spinner seleccionado --> No se hace nada
            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
        });
    }

    // Método sobrescrito de la interfaz 'DialogoQuitarFavoritos.ListenerdelDialogo' --> Se ejecuta al eliminar una película de una lista de favoritos
    @Override
    public void alBorrarPelicula(String pUsuario, String pIdPelicula) {
        // Se borra el fichero externo de la memoria interna del dipositivo correspondiente a la película y usuario que contenía los comentarios escritos
        this.deleteFile(pUsuario + "_" + pIdPelicula + ".txt");

        // Se actualiza el adaptador del Spinner --> En caso de no haber más películas en una lista, se eliminaría el nombre de esa lista del Spinner
        ArrayList<String> listas = gestorDB.getListasFavoritos(usuario);
        adaptadorSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listas);
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptadorSpinner);

        if(listas.size() == 0) {
            // Si no hay ninguna lista más, se vacía el ListView
            listView.setAdapter(null);
        }
        else if(posSeleccionada == listas.size()) {
            // Si la lista que se estaba mostrando se ha quedado sin películas y era la última lista, se muestra la primera lista
            spinner.setSelection(0);
        }
        else{
            // En caso contrario, se sigue mostrando la misma lista
            spinner.setSelection(posSeleccionada);
        }
    }
}