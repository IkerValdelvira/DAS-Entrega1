package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.Dialogos.DialogoFecha;
import com.example.entrega1.Fragments.PeliculaFragmentVert;
import com.example.entrega1.R;

import java.util.HashMap;

public class PeliculaActivity extends AppCompatActivity implements ComunicacionApi.ListenerApi, DialogoFecha.ListenerDelDialogo {

    PeliculaFragmentVert peliculaFragmentVert;

    ComunicacionApi comApi;

    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        peliculaFragmentVert = (PeliculaFragmentVert) getSupportFragmentManager().findFragmentById(R.id.fragmentPeliculaVert);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            comApi = new ComunicacionApi(this);
            comApi.getMovieDetails(extras.getString("id"));
            usuario = extras.getString("usuario");
        }
    }

    @Override
    public void alRecogerListaPeliculas(HashMap<String, String[]> pListaPeliculas) {

    }

    @Override
    public void alRecogerInfoPelicula(HashMap<String, String> pInfoPelicula) {
        peliculaFragmentVert.setPelicula(usuario, pInfoPelicula);
    }

    @Override
    public void alPulsarOK(int pYear, int pMonth, int pDayOfMonth) {
        peliculaFragmentVert.notificacionVMT(pYear,pMonth,pDayOfMonth);
    }
}