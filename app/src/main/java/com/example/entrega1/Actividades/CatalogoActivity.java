package com.example.entrega1.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.EditText;

import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.Dialogos.DialogoFecha;
import com.example.entrega1.Fragments.CatalogoFragment;
import com.example.entrega1.Fragments.PeliculaFragmentHoriz;
import com.example.entrega1.Fragments.PeliculaFragmentVert;
import com.example.entrega1.R;

import java.util.HashMap;
import java.util.Locale;

public class CatalogoActivity extends AppCompatActivity implements ComunicacionApi.ListenerApi, CatalogoFragment.ListenerFragment, DialogoFecha.ListenerDelDialogo {

    CatalogoFragment catalogoFragment;
    PeliculaFragmentVert peliculaFragmentVert;
    PeliculaFragmentHoriz peliculaFragmentHoriz;

    ComunicacionApi comApi;

    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma", "es");

        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        setContentView(R.layout.activity_catalogo);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            catalogoFragment = (CatalogoFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentCatalogoHoriz);
        }
        else{
            catalogoFragment = (CatalogoFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentCatalogoVert);
        }

        comApi = new ComunicacionApi(this);
        //SharedPreferences prefs = getSharedPreferences(usuario, Context.MODE_PRIVATE);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String genero = prefs.getString("generopref", "-1");
        if (!"-1".equals(genero)){
            comApi.getMovieList("genero", genero);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }



    }

    @Override
    public void seleccionarElemento(String pUsuario, String pId) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            //EL OTRO FRAGMENT EXISTE
            peliculaFragmentHoriz = (PeliculaFragmentHoriz) getSupportFragmentManager().findFragmentById(R.id.fragmentPeliculaHoriz);
            comApi.getMovieDetails(pId);
        }
        else{
            //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
            Intent i= new Intent(this, PeliculaActivity.class);
            i.putExtra("usuario", pUsuario);
            i.putExtra("id", pId);
            startActivity(i);
        }
    }

    @Override
    public void alRecogerListaPeliculas(HashMap<String,String[]> movieList) {
        catalogoFragment.setListaPeliculas(movieList);
    }

    @Override
    public void alRecogerInfoPelicula(HashMap<String, String> pPelicula) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            peliculaFragmentHoriz.setPelicula(usuario, pPelicula);
        }
        else{
            peliculaFragmentVert.setPelicula(usuario, pPelicula);
        }
    }

    @Override
    public void alPulsarOK(int pYear, int pMonth, int pDayOfMonth) {
        peliculaFragmentHoriz.notificacionVMT(pYear,pMonth,pDayOfMonth);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tituloBuscado", catalogoFragment.getEditTextBuscador().getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            String tituloBuscado = savedInstanceState.getString("tituloBuscado");
            comApi.getMovieList("titulo", tituloBuscado);
            catalogoFragment.setTextBuscador(tituloBuscado);
        }
    }
}