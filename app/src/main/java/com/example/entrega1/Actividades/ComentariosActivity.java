package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.stream.Collectors;

public class ComentariosActivity extends AppCompatActivity {

    private String usuario;
    private String id;
    private ImageView portada;
    private TextView titulo;
    private EditText comentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        portada = findViewById(R.id.imageViewPortadaC);
        titulo = findViewById(R.id.textViewTituloC);
        comentarios = findViewById(R.id.editTextComentarios);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
            id = extras.getString("id");
            Picasso.get().load(extras.getString("portada")).into(portada);
            titulo.setText(extras.getString("titulo"));
        }

        try {
            BufferedReader ficheroInterno = new BufferedReader(new InputStreamReader(openFileInput(usuario + "_" + id + ".txt")));
            String texto = ficheroInterno.lines().collect(Collectors.joining());
            texto = texto.replaceAll("<br />", "\n");
            ficheroInterno.close();
            comentarios.setText(texto);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.NingunComentario), Toast.LENGTH_LONG).show();
        }
    }

    public void onClickGuardar(View v) {
        try {
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