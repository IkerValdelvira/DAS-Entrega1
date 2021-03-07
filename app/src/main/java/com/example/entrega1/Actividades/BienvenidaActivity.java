package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.R;

public class BienvenidaActivity extends AppCompatActivity {

    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }
    }

    public void onClickCatalogo(View v) {
        Intent i = new Intent(this, CatalogoActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void onClickFavoritos(View v) {
        Intent i = new Intent (this, FavoritosActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void onClickVerMasTarde(View v) {
        Intent i = new Intent (this, VerMasTardeActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }
}