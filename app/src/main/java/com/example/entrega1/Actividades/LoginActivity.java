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

public class LoginActivity extends AppCompatActivity {

    ImageView loginImage;
    EditText username;
    EditText password;
    TextView register;

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

        setContentView(R.layout.activity_login);

        //this.deleteDatabase("DB");

        loginImage = findViewById(R.id.imageViewLogin);
        username = findViewById(R.id.editTextUsuario);
        password = findViewById(R.id.editTextContraseña);
        register = findViewById(R.id.textViewRegistrarse);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        gestorDB = new GestorDB(this, "DB", null, 1);

        loginImage.setImageResource(R.drawable.login);

    }

    public void onClickLogin(View v) {
        String usuario = username.getText().toString();
        String contrasena = password.getText().toString();
        if(usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.RellenarCampos), Toast.LENGTH_SHORT).show();
        }
        else if (!gestorDB.loginCorrecto(usuario, contrasena)) {
            Toast.makeText(this, getString(R.string.UserPassIncorrectos), Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
            finish();
        }
    }

    public void onClickRegistrarse(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}