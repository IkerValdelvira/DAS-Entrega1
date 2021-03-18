package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.Dialogos.DialogoFecha;
import com.example.entrega1.Fragments.PeliculaFragmentVert;
import com.example.entrega1.R;

import java.util.HashMap;
import java.util.Locale;

// Actividad que muestra los detalles de una película seleccionada y da opciones de marcarla como favorita o 'ver más tarde'
// Esta actividad está formada un fragment
public class PeliculaActivity extends AppCompatActivity implements ComunicacionApi.ListenerApi, DialogoFecha.ListenerDelDialogo {

    PeliculaFragmentVert peliculaFragmentVert;          // Fragment que contiene los detalles de la película y las opciones a realizar

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

        setContentView(R.layout.activity_pelicula);

        // Inicialización del fragment que contiene los detalles de la película y las opciones a realizar
        peliculaFragmentVert = (PeliculaFragmentVert) getSupportFragmentManager().findFragmentById(R.id.fragmentPeliculaVert);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");                      // Inicialización del nombre de usuario obtenido a través del Bundle asociado al Intent que ha creado la actividad
            comApi = new ComunicacionApi(this);                     // Inicialización de la instancia para la comunicacíon con el API 'themoviedb'
            comApi.getMovieDetails(extras.getString("id"));             // Se le solicitan los detalles de la película seleccionada al api 'themoviedb'
        }
    }

    // Método sobrescrito de la interfaz 'ComunicacionApi.ListenerApi' --> No se ejecuta en ningún momento en esta actividad
    @Override
    public void alRecogerListaPeliculas(HashMap<String, String[]> pListaPeliculas) {}

    // Método sobrescrito de la interfaz 'ComunicacionApi.ListenerApi' --> Se ejecuta tras la llamada 'comApi.getMovieDetails(id)'
    @Override
    public void alRecogerInfoPelicula(HashMap<String, String> pInfoPelicula) {
        // Llama al método del fragment 'PeliculaFragmentVert' para mostrar los detalles de la película seleccionada recibidos del API 'themoviedb'
        peliculaFragmentVert.setPelicula(usuario, pInfoPelicula);
    }

    // Método sobrescrito de la interfaz 'DialogoFecha.ListenerDelDialogo' --> Se ejecuta tras seleccionar una fecha en el diálogo 'DialogoFecha'
    @Override
    public void alPulsarOK(int pYear, int pMonth, int pDayOfMonth) {
        // Llama al método del fragment 'PeliculaFragmentVert' para crear una notificación para la fecha seleccionada
        peliculaFragmentVert.notificacionVMT(pYear,pMonth,pDayOfMonth);
    }
}