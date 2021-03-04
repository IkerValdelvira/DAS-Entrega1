package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PeliculaActivity extends AppCompatActivity implements ComunicacionApi.ListenerApi {

    TextView titulo;
    ImageView portada;
    TextView adulto;
    TextView puntuacion;
    TextView generos;
    TextView duracion;
    TextView fecha;
    TextView idiomaOrig;
    TextView idiomaDisp;
    TextView presupuesto;
    TextView ingresos;
    TextView productoras;
    TextView sinopsis;

    ImageView favoritos;
    ImageView verMasTarde;

    ComunicacionApi comApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        titulo = findViewById(R.id.textViewTextoTituloP);
        portada = findViewById(R.id.imageViewPortadaP);
        adulto = findViewById(R.id.textView18P);
        puntuacion = findViewById(R.id.textViewTextoPuntuacionP);
        generos = findViewById(R.id.textViewTextoGenerosP);
        duracion = findViewById(R.id.textViewTextoDuracionP);
        fecha = findViewById(R.id.textViewTextoFechaP);
        idiomaOrig = findViewById(R.id.textViewTextoIdiomaOrigP);
        idiomaDisp = findViewById(R.id.textViewTextoIdiomaDispP);
        presupuesto = findViewById(R.id.textViewTextoPresupuestoP);
        ingresos = findViewById(R.id.textViewTextoIngresosP);
        productoras = findViewById(R.id.textViewTextoProductorasP);
        sinopsis = findViewById(R.id.textViewTextoSinopsisP);

        favoritos = findViewById(R.id.imageViewFavoritos);
        verMasTarde = findViewById(R.id.imageViewVerMasTarde);

        favoritos.setImageResource(R.drawable.star);
        verMasTarde.setImageResource(R.drawable.watchlater);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            comApi = new ComunicacionApi(this);
            comApi.getMovieDetails(extras.getString("id"));
        }

    }

    @Override
    public void alRecogerListaPeliculas(HashMap<String, String[]> pListaPeliculas) {

    }

    @Override
    public void alRecogerInfoPelicula(HashMap<String, String> pListaPeliculas) {
        titulo.setText(pListaPeliculas.get("titulo"));
        Picasso.get().load(pListaPeliculas.get("portadaURL")).into(portada);
        if("false".equals(pListaPeliculas.get("adulto"))){
            adulto.setVisibility(View.INVISIBLE);
        }
        puntuacion.setText(pListaPeliculas.get("puntuacion"));
        puntuacion.setText(pListaPeliculas.get("puntuacion"));
        generos.setText(pListaPeliculas.get("generos"));
        duracion.setText(pListaPeliculas.get("duracion"));
        fecha.setText(pListaPeliculas.get("fecha"));
        idiomaOrig.setText(pListaPeliculas.get("idiomaOrig"));
        idiomaDisp.setText(pListaPeliculas.get("idiomaDisp"));
        presupuesto.setText(pListaPeliculas.get("presupuesto"));
        ingresos.setText(pListaPeliculas.get("ingresos"));
        productoras.setText(pListaPeliculas.get("productoras"));
        sinopsis.setText(pListaPeliculas.get("sinopsis"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sinopsis.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
    }

    public void onClickFavoritos(View v) {

    }

    public void onClickVerMasTarde(View v) {

    }
}