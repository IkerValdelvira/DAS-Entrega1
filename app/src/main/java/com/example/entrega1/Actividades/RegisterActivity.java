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

import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    ImageView registerImage;
    EditText username;
    EditText password;
    EditText paswword2;

    GestorDB gestorDB;

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

        setContentView(R.layout.activity_register);

        registerImage = findViewById(R.id.imageViewRegister);
        registerImage.setImageResource(R.drawable.registro);
        username = findViewById(R.id.editTextUsuarioR);
        password = findViewById(R.id.editTextContraseñaR);
        paswword2 = findViewById(R.id.editTextContraseñaR2);

        gestorDB = new GestorDB (this, "DB", null, 1);
    }

    public void onClickRegistro(View v) {
        String usuario = username.getText().toString();
        String contrasena1 = password.getText().toString();
        String contrasena2 = paswword2.getText().toString();
        if(usuario.isEmpty() || contrasena1.isEmpty() || contrasena2.isEmpty()) {
            Toast.makeText(this, getString(R.string.RellenarCampos), Toast.LENGTH_SHORT).show();
        }
        else if(!contrasena1.equals(contrasena2)) {
            Toast.makeText(this, getString(R.string.PassDiferentes), Toast.LENGTH_SHORT).show();
        }
        else {
            if (gestorDB.existeUsuario(usuario)) {
                Toast.makeText(this, getString(R.string.UsuarioRepetido), Toast.LENGTH_SHORT).show();
            }
            else {
                gestorDB.insertarUsuario(usuario, contrasena1);
                Toast.makeText(this, getString(R.string.UsuarioRegistrado), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}