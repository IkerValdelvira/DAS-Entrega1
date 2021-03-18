package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

import java.util.Locale;

// Actividad con la que se inicia la aplicación y representa el login de un usuario ya registrado
public class LoginActivity extends AppCompatActivity {

    // Elementos necesarios del layout 'activity_login.xml'
    ImageView loginImage;
    EditText username;
    EditText password;
    TextView register;

    GestorDB gestorDB;      // Instancia para realizar la comunicación con la base de datos local

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

        setContentView(R.layout.activity_login);

        //this.deleteDatabase("DB");

        // Inicialización de los elementos del layout 'activity_login.xml'
        loginImage = findViewById(R.id.imageViewLogin);
        loginImage.setImageResource(R.drawable.login);
        username = findViewById(R.id.editTextUsuario);
        password = findViewById(R.id.editTextContraseña);
        register = findViewById(R.id.textViewRegistrarse);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);    // El texto del TextView se subraya

        gestorDB = new GestorDB(this, "DB", null, 1);       // Inicialización de la instancia para la comunicación con la base de datos local 'DB'

    }

    // Listener 'onClick' del botón de login del layout 'acitvity_login.xml'
    public void onClickLogin(View v) {
        String usuario = username.getText().toString();
        String contrasena = password.getText().toString();
        // Se comprueba que todos los EditText (usuario y contraseña) estén rellenos
        if(usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.RellenarCampos), Toast.LENGTH_SHORT).show();
        }
        // Se comprueba que el usuario existe en la base de datos local
        else if (!gestorDB.loginCorrecto(usuario, contrasena)) {
            Toast.makeText(this, getString(R.string.UserPassIncorrectos), Toast.LENGTH_SHORT).show();
        }
        // Si se cumple lo anterior, se crea una nueva actividad 'MainActivity' con el usuario logueado
        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
            finish();
        }
    }

    // Listener 'onClick' del TextView para registrarse del layout 'acitvity_login.xml'
    public void onClickRegistrarse(View v) {
        // Se crea una nueva actividad 'RegisterActivity'
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}