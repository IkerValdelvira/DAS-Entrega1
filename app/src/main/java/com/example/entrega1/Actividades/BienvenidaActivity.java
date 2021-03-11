package com.example.entrega1.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entrega1.Preferencias;
import com.example.entrega1.R;

import java.util.Locale;

public class BienvenidaActivity extends AppCompatActivity implements Preferencias.ListenerPreferencias {

    private String usuario;

    private View preferencias;
    private int prefsVisibles;

    private ImageView logo;
    private TextView nombreBienvenida;

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

        setContentView(R.layout.activity_bienvenida);

        setSupportActionBar(findViewById(R.id.toolbarPrincipal));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        logo = findViewById(R.id.imageViewLogo);
        logo.setImageResource(R.drawable.logo);

        preferencias = findViewById(R.id.fragmentPreferencias);
        prefsVisibles = 0;
        if (savedInstanceState != null) {
            prefsVisibles = savedInstanceState.getInt("prefsVisibles");
        }
        if(prefsVisibles == 0){
            preferencias.setVisibility(View.INVISIBLE);
        }
        else {
            preferencias.setVisibility(View.VISIBLE);
        }

        nombreBienvenida = findViewById(R.id.textViewBienvenida);
        //SharedPreferences prefs = getSharedPreferences(usuario, Context.MODE_PRIVATE);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nombre = prefs.getString("nombre", "-1");
        if ("-1".equals(nombre)){
            nombreBienvenida.setText(getString(R.string.Bienvenido) + " " + usuario + "!!!");
        }
        else{
            nombreBienvenida.setText(getString(R.string.Bienvenido) + " " + nombre + "!!!");
        }

    }

    public void onClickCatalogo(View v) {
        //Intent i = new Intent(this, CatalogoActivity.class);
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

    @Override
    public void alCambiarNombre() {
        //SharedPreferences prefs = getSharedPreferences(usuario, Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nombre = prefs.getString("nombre", "-1");
        nombreBienvenida.setText(getString(R.string.Bienvenido) + " " + nombre + "!!!");
    }

    @Override
    public void alCambiarIdioma() {
        //SharedPreferences prefs = getSharedPreferences(usuario, Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma", "es");
        cambiarLocalización(idioma);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.opcionFav) {
            Intent i = new Intent (this, FavoritosActivity.class);
            i.putExtra("usuario", usuario);
            startActivity(i);
        }
        else if(id == R.id.opcionVMT) {
            Intent i = new Intent (this, VerMasTardeActivity.class);
            i.putExtra("usuario", usuario);
            startActivity(i);
        }
        else if(id == R.id.opcionPreferencias) {
            if(prefsVisibles == 0){
                preferencias.setVisibility(View.VISIBLE);
                prefsVisibles = 1;
            }
            else {
                preferencias.setVisibility(View.INVISIBLE);
                prefsVisibles = 0;
            }
        }
        else if(id == R.id.opcionCerrarSesion) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void cambiarLocalización(String pLocalizacion) {
        Locale nuevaloc = new Locale(pLocalizacion);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("prefsVisibles", prefsVisibles);
    }
}