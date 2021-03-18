package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.stream.Collectors;

// Actividad para escribir/leer en/desde ficheros internos de texto los comentarios de la película seleccionada.
public class ComentariosActivity extends AppCompatActivity {

    private String usuario;             // Nombre del usuario que ha creado la actividad
    private String id;                  // id de la película seleccionada

    // Elementos necesarios del layout 'activity_comentarios.xml'
    private ImageView portada;
    private TextView titulo;
    private EditText comentarios;

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

        setContentView(R.layout.activity_comentarios);

        // Inicialización de los elementos necesarios del layout 'activity_comentarios.xml'
        portada = findViewById(R.id.imageViewPortadaC);
        titulo = findViewById(R.id.textViewTituloC);
        comentarios = findViewById(R.id.editTextComentarios);

        // Inicialización de los datos obtenidos a través del Bundle asociado al Intent que ha creado la actividad
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
            id = extras.getString("id");
            Picasso.get().load(extras.getString("portada")).into(portada);      // Librería para añadir una imagen desde una URL a un ImageView
            titulo.setText(extras.getString("titulo"));
        }

        try {
            // Se intenta leer el fichero correspondiente al usuario e id de la película seleccionada para mostrar el texto guardado en la actividad
            // Se leen ficheros externos de la memoria interna del dispositivo
            BufferedReader fichero = new BufferedReader(new InputStreamReader(openFileInput(usuario + "_" + id + ".txt")));
            String texto = fichero.lines().collect(Collectors.joining());
            texto = texto.replaceAll("<br />", "\n");       // Se reemplazan saltos de línea '\n' por '<br />' para poder identificarlos en el fichero de texto
            fichero.close();
            comentarios.setText(texto);
        } catch (IOException e) {
            // Si no existe el fichero se muestra un Toast comunicandole al usuario que todavía no se han guardado comentarios
            Toast.makeText(this, getString(R.string.NingunComentario), Toast.LENGTH_LONG).show();
        }
    }

    // Listener 'onClick' del botón de guardado de comentarios del layout 'acitvity_comentarios.xml'
    public void onClickGuardar(View v) {
        try {
            // Se abre en modo escritura el fichero correspondiente al usuario e id de la película seleccionada y se escribe el texto introducido por el usuario en el EditText
            // Se escriben ficheros externos en la memoria interna del dispositivo
            OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput(usuario + "_" + id + ".txt", Context.MODE_PRIVATE));
            String texto = comentarios.getText().toString();
            texto = texto.replaceAll("\n", "<br />");
            fichero.write(texto);
            fichero.close();
            Toast.makeText(this, getString(R.string.ComentarioGuardado), Toast.LENGTH_LONG).show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}