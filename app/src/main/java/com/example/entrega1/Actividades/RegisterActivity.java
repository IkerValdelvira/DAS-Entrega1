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
import android.widget.Toast;

import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

import java.util.Locale;

// Actividad que representa el registro de un nuevo usuario en la aplicación
public class RegisterActivity extends AppCompatActivity {

    // Elementos necesarios del layout 'activity_register.xml'
    ImageView registerImage;
    EditText username;
    EditText password;
    EditText paswword2;

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

        setContentView(R.layout.activity_register);

        // Inicialización de los elementos del layout 'activity_register.xml'
        registerImage = findViewById(R.id.imageViewRegister);
        registerImage.setImageResource(R.drawable.registro);
        username = findViewById(R.id.editTextUsuarioR);
        password = findViewById(R.id.editTextContraseñaR);
        paswword2 = findViewById(R.id.editTextContraseñaR2);

        gestorDB = new GestorDB (this, "DB", null, 1);      // Inicialización de la instancia para la comunicación con la base de datos local 'DB'
    }

    // Listener 'onClick' del botón de registro del layout 'acitvity_register.xml'
    public void onClickRegistro(View v) {
        String usuario = username.getText().toString();
        String contrasena1 = password.getText().toString();
        String contrasena2 = paswword2.getText().toString();
        // Se comprueba que todos los EditText (usuario, contraseña y repetición de la contraseña) estén rellenos
        if(usuario.isEmpty() || contrasena1.isEmpty() || contrasena2.isEmpty()) {
            Toast.makeText(this, getString(R.string.RellenarCampos), Toast.LENGTH_SHORT).show();
        }
        // Se comprueba que la contraseña y la repetición de la contraseña sean iguales
        else if(!contrasena1.equals(contrasena2)) {
            Toast.makeText(this, getString(R.string.PassDiferentes), Toast.LENGTH_SHORT).show();
        }
        else {
            // Se comprueba si existe un usuario con los datos introducidos en la base de datos local
            if (gestorDB.existeUsuario(usuario)) {
                // Si el usuario existe, se crea un Toast notificándolo
                Toast.makeText(this, getString(R.string.UsuarioRepetido), Toast.LENGTH_SHORT).show();
            }
            else {
                // Si el usuario no existe, se inserta en la base de datos local y se destruye la actividad actual
                gestorDB.insertarUsuario(usuario, contrasena1);
                Toast.makeText(this, getString(R.string.UsuarioRegistrado), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}