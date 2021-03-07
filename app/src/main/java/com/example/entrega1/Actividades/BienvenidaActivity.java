package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.entrega1.R;

public class BienvenidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
    }

    public void onClickCatalogo(View v) {
        Intent i = new Intent(this, CatalogoActivity.class);
        startActivity(i);
    }

    public void onClickFavoritos(View v) {
        Intent i = new Intent (this, FavoritosActivity.class);
        startActivity(i);
    }

    public void onClickVerMasTarde(View v) {
        Intent i = new Intent (this, VerMasTardeActivity.class);
        startActivity(i);
    }
}