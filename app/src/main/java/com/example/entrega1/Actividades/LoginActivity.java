package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

public class LoginActivity extends AppCompatActivity {

    ImageView loginImage;
    EditText username;
    EditText password;
    TextView register;

    GestorDB gestorDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //this.deleteDatabase("DB");

        loginImage = findViewById(R.id.imageViewLogin);
        username = findViewById(R.id.editTextUsuario);
        password = findViewById(R.id.editTextContraseña);
        register = findViewById(R.id.textViewRegistrarse);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        gestorDB = new GestorDB(this, "DB", null, 1);

    }

    public void onClickLogin(View v) {
        String usuario = username.getText().toString();
        String contrasena = password.getText().toString();
        if(usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
        }
        else if (!gestorDB.loginCorrecto(usuario, contrasena)) {
            Toast.makeText(this, "Usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, BienvenidaActivity.class);
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