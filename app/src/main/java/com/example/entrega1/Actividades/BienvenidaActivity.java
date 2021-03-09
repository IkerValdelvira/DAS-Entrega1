package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.Preferencias;
import com.example.entrega1.R;

public class BienvenidaActivity extends AppCompatActivity implements Preferencias.ListenerPreferencias{

    private String usuario;

    private Button botonPreferencias;
    private View preferencias;
    private int prefsVisibles;

    private TextView nombreBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        botonPreferencias = findViewById(R.id.buttonPreferencias);
        botonPreferencias.setText(getString(R.string.MostrarPreferencias));
        preferencias = findViewById(R.id.fragmentPreferencias);
        preferencias.setVisibility(View.INVISIBLE);
        prefsVisibles = 0;

        nombreBienvenida = findViewById(R.id.textViewBienvenida);
        //SharedPreferences prefs = getSharedPreferences(usuario, Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nombre = prefs.getString("nombre", "-1");
        if ("-1".equals(nombre)){
            nombreBienvenida.setText(getString(R.string.Bienvenido) + " " + usuario + "!!!");
        }
        else{
            nombreBienvenida.setText(getString(R.string.Bienvenido) + " " + nombre + "!!!");
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

    public void onClickPreferencias(View v) {
        if(prefsVisibles == 0){
            botonPreferencias.setText(getString(R.string.OcultarPreferencias));
            preferencias.setVisibility(View.VISIBLE);
            prefsVisibles = 1;
        }
        else {
            botonPreferencias.setText(getString(R.string.MostrarPreferencias));
            preferencias.setVisibility(View.INVISIBLE);
            prefsVisibles = 0;
        }
    }

    @Override
    public void alCambiarNombre() {
        //SharedPreferences prefs = getSharedPreferences(usuario, Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nombre = prefs.getString("nombre", "-1");
        nombreBienvenida.setText(getString(R.string.Bienvenido) + " " + nombre + "!!!");
    }
}