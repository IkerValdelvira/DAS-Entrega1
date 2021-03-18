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

import com.example.entrega1.Fragments.Preferencias;
import com.example.entrega1.R;

import java.util.Locale;

// Actividad principal de la aplicación en la que se muestran las principales acciones y la selección de las preferencias (mediante un fragment 'Preferencias')
public class MainActivity extends AppCompatActivity implements Preferencias.ListenerPreferencias {

    private String usuario;                         // Nombre del usuario que ha creado la actividad

    // View para la gestión de las preferencias
    private View preferencias;
    private int prefsVisibles;

    // Elementos necesarios del layout 'activity_main.xml'
    private ImageView logo;
    private TextView nombreBienvenida;

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

        setContentView(R.layout.activity_main);

        // Asignación del ActionBar personalizado
        setSupportActionBar(findViewById(R.id.toolbarPrincipal));

        // Inicialización del nombre de usuario obtenido a través del Bundle asociado al Intent que ha creado la actividad
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        logo = findViewById(R.id.imageViewLogo);
        logo.setImageResource(R.drawable.logo);

        // Inicializacíon del fragment que contiene la selección de las preferencias
        preferencias = findViewById(R.id.fragmentPreferencias);
        prefsVisibles = 0;
        if (savedInstanceState != null) {
            // Se recupera el estado del fragment de las preferencias (visible o invisible) tras la destrucción de la actividad
            prefsVisibles = savedInstanceState.getInt("prefsVisibles");
        }

        if(prefsVisibles == 0 && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // Si la orientación del dispositivo es de retrato (vertical) y el fragment de las preferencias era invisible, se mantiene invisible
            preferencias.setVisibility(View.INVISIBLE);
        }
        else if(prefsVisibles == 1 && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // Si la orientación del dispositivo es de retrato (vertical) y el fragment de las preferencias era visible, se mantiene visible
            preferencias.setVisibility(View.VISIBLE);
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            // Si la orientación del dispositivo es apaisada (horizonta), el fragment de las preferencias va a estar siempre visible al crear la actividad
            preferencias.setVisibility(View.VISIBLE);
            prefsVisibles = 1;
        }

        nombreBienvenida = findViewById(R.id.textViewBienvenida);
        nombreBienvenida.setText(getString(R.string.Bienvenido) + " " + usuario + "!!!");

    }

    // Listener 'onClick' del botón para ver el catálogo de películas del layout 'acitvity_main.xml'
    public void onClickCatalogo(View v) {
        // Se crea una nueva actividad 'CatalogoActivity' con el usuario actual
        Intent i = new Intent(this, CatalogoActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    // Listener 'onClick' del botón para ver las listas de favoritos del layout 'acitvity_main.xml'
    public void onClickFavoritos(View v) {
        // Se crea una nueva actividad 'FavoritosActivity' con el usuario actual
        Intent i = new Intent (this, FavoritosActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    // Listener 'onClick' del botón para ver la lista de 'ver más tarde' del layout 'acitvity_main.xml'
    public void onClickVerMasTarde(View v) {
        // Se crea una nueva actividad 'VerMasTardeActivity' con el usuario actual
        Intent i = new Intent (this, VerMasTardeActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    // Método sobrescrito de la interfaz 'Preferencias.ListenerPreferencias' --> Se ejecuta al cambiar la preferencia 'idioma'
    @Override
    public void alCambiarIdioma() {
        // Se destruye la actividad y se vuelve a crear --> Al crearse de nuevo se establecerá la nueva localización en el método 'onCreate'
        finish();
        startActivity(getIntent());
    }

    // Método sobrescrito para asignar el fichero 'menu.xml' con la definición del menú a la Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    // Método sobrescrito para reaccionar ante la interacción del usuario con cualquiera de las opciones del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.opcionFav) {
            // Si se ha pulsado en la 'opcionFav', se crea una nueva actividad 'FavoritosActivity' con el usuario actual
            Intent i = new Intent (this, FavoritosActivity.class);
            i.putExtra("usuario", usuario);
            startActivity(i);
        }
        else if(id == R.id.opcionVMT) {
            // Si se ha pulsado en la 'opcionVMT', se crea una nueva actividad 'VerMasTardeActivity' con el usuario actual
            Intent i = new Intent (this, VerMasTardeActivity.class);
            i.putExtra("usuario", usuario);
            startActivity(i);
        }
        else if(id == R.id.opcionPreferencias) {
            // Si se ha pulsado en la 'opcionPreferencias', se muestra/oculta el fragment con las preferencias
            if(prefsVisibles == 0 ){
                preferencias.setVisibility(View.VISIBLE);
                prefsVisibles = 1;
            }
            else {
                preferencias.setVisibility(View.INVISIBLE);
                prefsVisibles = 0;
            }
        }
        else if(id == R.id.opcionCerrarSesion) {
            // Si se ha pulsado en la 'opcionCerrarSesion', se crea una nueva actividad 'LoginActivity' y se destruye la actividad actual
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // Se ejecuta siempre antes de destruirse la actividad
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Se guarda en un Bundle la variable que indica si el fragment de las preferencias estaba visible o invisible
        outState.putInt("prefsVisibles", prefsVisibles);
    }
}