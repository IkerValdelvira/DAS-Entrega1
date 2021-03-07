package com.example.entrega1.Actividades;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega1.Adaptadores.AdaptadorRecycler;
import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.R;

import java.util.HashMap;

public class CatalogoActivity extends AppCompatActivity implements ComunicacionApi.ListenerApi {

    EditText editTextBuscador;
    RecyclerView recyclerView;
    AdaptadorRecycler adaptador;
    LinearLayoutManager linearLayout;

    ComunicacionApi comApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        editTextBuscador = findViewById(R.id.editTextBuscador);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayout);

        comApi = new ComunicacionApi(this);

    }

    public void onClickBuscar(View v) {
        String tituloBuscado = editTextBuscador.getText().toString();
        if(tituloBuscado.isEmpty()){
            Toast aviso = Toast.makeText(this, "Introduce un título en el buscador.", Toast.LENGTH_LONG);
            aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            aviso.show();
        }
        else{
            comApi.getMovieList(tituloBuscado);
        }
    }

    @Override
    public void alRecogerListaPeliculas(HashMap<String,String[]> movieList) {
        String[] ids = movieList.get("ids");
        String[] portadasURL = movieList.get("portadasURL");
        String[] titulos = movieList.get("titulos");
        String[] generos = movieList.get("generos");
        String[] fechas = movieList.get("fechas");
        String[] puntuaciones = movieList.get("puntuaciones");
        String[] idiomas = movieList.get("idiomas");
        String[] sinopsis = movieList.get("sinopsis");

        adaptador = new AdaptadorRecycler(ids,portadasURL,titulos,generos,fechas,puntuaciones,idiomas,sinopsis);
        recyclerView.setAdapter(adaptador);
    }

    @Override
    public void alRecogerInfoPelicula(HashMap<String, String> pListaPeliculas) {

    }

}