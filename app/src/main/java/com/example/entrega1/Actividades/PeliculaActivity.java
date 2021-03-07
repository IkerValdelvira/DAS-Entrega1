package com.example.entrega1.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entrega1.AlertReceiver;
import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.Dialogos.DialogoAñadirFavoritos;
import com.example.entrega1.Dialogos.DialogoFecha;
import com.example.entrega1.GestorDB;
import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class PeliculaActivity extends AppCompatActivity implements ComunicacionApi.ListenerApi, DialogoFecha.ListenerDelDialogo {

    private String id;
    private String portadaURL;

    private TextView titulo;
    private ImageView portada;
    private TextView adulto;
    private TextView puntuacion;
    private TextView generos;
    private TextView duracion;
    private TextView fecha;
    private TextView idiomaOrig;
    private TextView idiomaDisp;
    private TextView presupuesto;
    private TextView ingresos;
    private TextView productoras;
    private TextView sinopsis;

    private ImageView favoritos;
    private ImageView verMasTarde;

    private ComunicacionApi comApi;

    private GestorDB gestorDB;

    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        titulo = findViewById(R.id.textViewTextoTituloP);
        portada = findViewById(R.id.imageViewPortadaP);
        adulto = findViewById(R.id.textView18P);
        puntuacion = findViewById(R.id.textViewTextoPuntuacionP);
        generos = findViewById(R.id.textViewTextoGenerosP);
        duracion = findViewById(R.id.textViewTextoDuracionP);
        fecha = findViewById(R.id.textViewTextoFechaP);
        idiomaOrig = findViewById(R.id.textViewTextoIdiomaOrigP);
        idiomaDisp = findViewById(R.id.textViewTextoIdiomaDispP);
        presupuesto = findViewById(R.id.textViewTextoPresupuestoP);
        ingresos = findViewById(R.id.textViewTextoIngresosP);
        productoras = findViewById(R.id.textViewTextoProductorasP);
        sinopsis = findViewById(R.id.textViewTextoSinopsisP);

        favoritos = findViewById(R.id.imageViewFavoritos);
        verMasTarde = findViewById(R.id.imageViewVerMasTarde);

        favoritos.setImageResource(R.drawable.star);
        verMasTarde.setImageResource(R.drawable.watchlater);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            comApi = new ComunicacionApi(this);
            comApi.getMovieDetails(extras.getString("id"));
            usuario = extras.getString("usuario");
        }

        gestorDB = new GestorDB (this, "DB", null, 1);

    }

    @Override
    public void alRecogerListaPeliculas(HashMap<String, String[]> pListaPeliculas) {

    }

    @Override
    public void alRecogerInfoPelicula(HashMap<String, String> pInfoPelicula) {
        id = pInfoPelicula.get("id");
        titulo.setText(pInfoPelicula.get("titulo"));
        portadaURL = pInfoPelicula.get("portadaURL");
        Picasso.get().load(portadaURL).into(portada);
        if("false".equals(pInfoPelicula.get("adulto"))){
            adulto.setVisibility(View.INVISIBLE);
        }
        puntuacion.setText(pInfoPelicula.get("puntuacion"));
        puntuacion.setText(pInfoPelicula.get("puntuacion"));
        generos.setText(pInfoPelicula.get("generos"));
        duracion.setText(pInfoPelicula.get("duracion"));
        fecha.setText(pInfoPelicula.get("fecha"));
        idiomaOrig.setText(pInfoPelicula.get("idiomaOrig"));
        idiomaDisp.setText(pInfoPelicula.get("idiomaDisp"));
        presupuesto.setText(pInfoPelicula.get("presupuesto"));
        ingresos.setText(pInfoPelicula.get("ingresos"));
        productoras.setText(pInfoPelicula.get("productoras"));
        sinopsis.setText(pInfoPelicula.get("sinopsis"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sinopsis.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
    }

    public void onClickFavoritos(View v) {
        DialogFragment dialogoAñadirFavoritos = new DialogoAñadirFavoritos(usuario, id, titulo.getText().toString(), portadaURL);
        dialogoAñadirFavoritos.show(getSupportFragmentManager(), "añadir_favoritos");
    }

    public void onClickVerMasTarde(View v) {
        DialogFragment dialogo= new DialogoFecha();
        dialogo.show(getSupportFragmentManager(), "fecha");
    }

    @Override
    public void alPulsarOK(int pYear, int pMonth, int pDayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, pYear);
        c.set(Calendar.MONTH, pMonth);
        c.set(Calendar.DAY_OF_MONTH, pDayOfMonth);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("titulo", titulo.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        String fechaVerMasTarde = pYear + "/" + pMonth + "/" + pDayOfMonth;
        gestorDB.insertarPeliculaVerMasTarde(usuario, id, fechaVerMasTarde, titulo.getText().toString(), portadaURL);

        Toast aviso = Toast.makeText(this, "Película añadida a la lista de 'ver más tarde'.", Toast.LENGTH_LONG);
        aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        aviso.show();
    }
}