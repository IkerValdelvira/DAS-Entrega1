package com.example.entrega1.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.Dialogos.DialogoFecha;
import com.example.entrega1.Fragments.CatalogoFragment;
import com.example.entrega1.Fragments.PeliculaFragmentHoriz;
import com.example.entrega1.Fragments.PeliculaFragmentVert;
import com.example.entrega1.R;

import java.util.HashMap;
import java.util.Locale;

/*
    Actividad que representa el catálogo de películas que el usuario puede buscar.
    Esta actividad está formada por fragments:
        - si el dispositivo está en orientación de retrato (vertical), tiene un único fragment con la lista de películas del catálogo: CatalogoFragment
        - si el dispositivo está en orientación apaisada (horizontal), tiene dos fragments: la lista de películas (CatalogoFragment) y la información de la película seleccionada (PelículaFragmentHoriz)
*/
public class CatalogoActivity extends AppCompatActivity implements ComunicacionApi.ListenerApi, CatalogoFragment.ListenerFragment, DialogoFecha.ListenerDelDialogo {

    CatalogoFragment catalogoFragment;                  // Fragment para el catálogo de películas
    PeliculaFragmentVert peliculaFragmentVert;          // Fragment para la información de la película seleccionada en orientacion retrato
    PeliculaFragmentHoriz peliculaFragmentHoriz;        // Fragment para la información de la película seleccionada en orientacion apaisada

    ComunicacionApi comApi;                             // Instancia para realizar la comunicación con el API 'themoviedb'

    String usuario;                                     // Nombre del usuario que ha creado la actividad

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

        setContentView(R.layout.activity_catalogo);

        // Inicialización del fragment del catálogo de películas según la orientación actual del dispositivo
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            catalogoFragment = (CatalogoFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentCatalogoHoriz);
        }
        else{
            catalogoFragment = (CatalogoFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentCatalogoVert);
        }

        // Inicialización de la instancia para la comunicacíon con el API 'themoviedb'
        comApi = new ComunicacionApi(this);

        // Acceso al las preferencias para obtener el valor de 'generopref' --> Se obtiene la lista de películas de ese género del API 'themoviedb'
        String genero = prefs.getString("generopref", "-1");
        if (!"-1".equals(genero)){
            comApi.getMovieList("genero", genero);
        }

        // Inicialización del nombre de usuario obtenido a través del Bundle asociado al Intent que ha creado la actividad
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

    }

    // Método sobrescrito de la interfaz 'CatalogoFragment.ListenerFragment' --> Se ejecuta al seleccionar una película del catálogo
    @Override
    public void seleccionarElemento(String pUsuario, String pId) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            // Si la orientación es apaisada (horizontal), se obtiene el fragment 'PeliculaFragmentHoriz' y se le solicitan los detalles de la película seleccionada al api 'themoviedb'
            peliculaFragmentHoriz = (PeliculaFragmentHoriz) getSupportFragmentManager().findFragmentById(R.id.fragmentPeliculaHoriz);
            comApi.getMovieDetails(pId);
        }
        else{
            // Si la orientación es de retrato (vertical), se crea una nueva actividad 'PeliculaActivity' con la película seleccionada
            Intent i= new Intent(this, PeliculaActivity.class);
            i.putExtra("usuario", pUsuario);
            i.putExtra("id", pId);
            startActivity(i);
        }
    }

    // Método sobrescrito de la interfaz 'ComunicacionApi.ListenerApi' --> Se ejecuta tras la llamada 'comApi.getMovieList(genero)'
    @Override
    public void alRecogerListaPeliculas(HashMap<String,String[]> movieList) {
        // Llama al método del fragment con el catálogo (CatalogoFragment) para mostrar las películas recibidas del API 'themoviedb'
        catalogoFragment.setListaPeliculas(movieList);
    }

    // Método sobrescrito de la interfaz 'ComunicacionApi.ListenerApi' --> Se ejecuta tras la llamada 'comApi.getMovieDetails(id)'
    @Override
    public void alRecogerInfoPelicula(HashMap<String, String> pPelicula) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            // Si la orientación es apaisada (horizontal), llama al método del fragment 'PeliculaFragmentHoriz' para mostrar
            // los detalles de la película seleccionada recibidos del API 'themoviedb'
            peliculaFragmentHoriz.setPelicula(usuario, pPelicula);
        }
    }

    // Método sobrescrito de la interfaz 'DialogoFecha.ListenerDelDialogo' --> Se ejecuta tras seleccionar una fecha en el diálogo 'DialogoFecha'
    @Override
    public void alPulsarOK(int pYear, int pMonth, int pDayOfMonth) {
        // Llama al método del fragment 'PeliculaFragmentHoriz' para crear una notificación para la fecha seleccionada
        peliculaFragmentHoriz.notificacionVMT(pYear,pMonth,pDayOfMonth);
    }

    // Se ejecuta siempre antes de destruirse la actividad
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Se guarda en un Bundle el String escrito en EditText del fragment con el catálogo de películas (CatalogFragment)
        // Se quiere conservar el título de la película que se está buscando tras un cambio de orientación del dispositivo
        outState.putString("tituloBuscado", catalogoFragment.getEditTextBuscador().getText().toString());
    }

    // Se ejecuta siempre después de onStart() antes de presentar la actividad en pantalla
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Se recupera el String 'tituloBuscado' guardado antes de la destrucción de la actividad y se reesstablece en el EditText del fragment 'CatalogoFragment'
        // Se obtiene la lista de películas que contienen el 'tituloBuscado' del API 'themoviedb'
        if(savedInstanceState != null) {
            String tituloBuscado = savedInstanceState.getString("tituloBuscado");
            if(!tituloBuscado.isEmpty()){
                comApi.getMovieList("titulo", tituloBuscado);
                catalogoFragment.setTextBuscador(tituloBuscado);
            }
        }
    }
}