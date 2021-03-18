package com.example.entrega1.Actividades;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.entrega1.Adaptadores.AdaptadorListViewVerMasTarde;
import com.example.entrega1.Dialogos.DialogoQuitarVerMasTarde;
import com.example.entrega1.GestorDB;
import com.example.entrega1.Modelos.Pelicula;
import com.example.entrega1.R;

import java.util.Locale;

// Actividad que muestra las lista de películas 'ver más tarde' mediante un ListView personalizado
public class VerMasTardeActivity extends AppCompatActivity implements DialogoQuitarVerMasTarde.ListenerdelDialogo {

    // ListView personalizado para mostrar las películas 'ver mas tarde'
    private ListView listView;
    private AdaptadorListViewVerMasTarde adaptadorListView;
    private String[] ids;
    private String[] portadas;
    private String[] titulos;
    private String[] fechas;

    private GestorDB gestorDB;          // Instancia para realizar la comunicación con la base de datos local

    private String usuario;             // Nombre del usuario que ha creado la actividad

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

        setContentView(R.layout.activity_ver_mas_tarde);

        listView = findViewById(R.id.listViewVMT);          // Inicialización del elemento 'listView' del layout 'activity_ver_mas_tarde.xml'

        gestorDB = new GestorDB (this, "DB", null, 1);      // Inicialización de la instancia para la comunicación con la base de datos local 'DB'

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Inicialización del nombre de usuario obtenido a través del Bundle asociado al Intent que ha creado la actividad
            usuario = extras.getString("usuario");
            // En el caso de obtener un 'id_notificación' con valor 1, significa que se ha creado esta actividad a través de un PendingIntent de una acción de una notificación
            // Cuando el usuario ha pulsado en la acción 'Quitar de Ver Más Tarde' de la notificación local
            int id_notificacion = extras.getInt("id_notificacion");
            if(id_notificacion == 1) {
                // Se cancela la notificación con ese id
                NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(id_notificacion);
                // Se elimina la película correspondiente a los extras del PendingIntent de la tabla de VerMasTarde de la base de datos local
                String pelicula = extras.getString("pelicula");
                int anyo = extras.getInt("anyo");
                int mes = extras.getInt("mes");
                int dia = extras.getInt("dia");
                gestorDB.eliminarPeliculaVMT(usuario, pelicula, anyo + "/" + mes + "/" + dia);
            }
        }

        Pelicula[] peliculas = gestorDB.getPeliculasVMT(usuario);   // Se llama al método para obtener las películas de 'ver más tarde' del usuario
        // Se crean los Arrays necesarios para el adaptador del ListView para guardar la siguiente información de cada película: id, URL de la portada, título y fecha de 'ver más tarde'
        ids = new String[peliculas.length];
        portadas = new String[peliculas.length];
        titulos = new String[peliculas.length];
        fechas = new String[peliculas.length];
        for(int i=0; i<peliculas.length; i++) {
            ids[i] = peliculas[i].getId();
            portadas[i] = peliculas[i].getPortadaURL();
            titulos[i] = peliculas[i].getTitulo();
            fechas[i] = peliculas[i].getFechaVerMasTarde();
        }
        adaptadorListView = new AdaptadorListViewVerMasTarde(usuario, VerMasTardeActivity.this,ids,portadas,titulos,fechas);
        listView.setAdapter(adaptadorListView);

        // Listener al hacer click en un fila (película) del ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Se ejecuta al hacer click en un fila (película) del ListView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                // Se crea una nueva actividad 'PeliculaActivity' con la película seleccionada
                Intent i = new Intent (VerMasTardeActivity.this, PeliculaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id", ids[position]);
                i.putExtra("usuario", usuario);
                view.getContext().startActivity(i);
            }
        });
    }

    // Método sobrescrito de la interfaz 'DialogoQuitarVerMasTarde.ListenerdelDialogo' --> Se ejecuta al eliminar una película de 'ver más tarde'
    @Override
    public void alBorrarPelicula() {
        Pelicula[] peliculas = gestorDB.getPeliculasVMT(usuario);       // Se llama al método para obtener las películas actualizadas de 'ver más tarde' del usuario
        // Se vuelve a crear un adaptador para el ListView con las películas actualizadas --> Actualización del ListView personalizado tras borrar una película v
        ids = new String[peliculas.length];
        portadas = new String[peliculas.length];
        titulos = new String[peliculas.length];
        fechas = new String[peliculas.length];
        for(int i=0; i<peliculas.length; i++) {
            ids[i] = peliculas[i].getId();
            portadas[i] = peliculas[i].getPortadaURL();
            titulos[i] = peliculas[i].getTitulo();
            fechas[i] = peliculas[i].getFechaVerMasTarde();
        }
        adaptadorListView = new AdaptadorListViewVerMasTarde(usuario, VerMasTardeActivity.this,ids,portadas,titulos,fechas);
        listView.setAdapter(adaptadorListView);
    }

}