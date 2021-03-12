package com.example.entrega1.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.entrega1.AlertReceiver;
import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.Dialogos.DialogoAñadirFavoritos;
import com.example.entrega1.Dialogos.DialogoFecha;
import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

import java.util.Calendar;
import java.util.HashMap;

public class PeliculaFragmentHoriz extends Fragment {

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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pelicula_horiz,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titulo = getView().findViewById(R.id.textViewTextoTituloP);
        //portada = getView().findViewById(R.id.imageViewPortadaP);
        adulto = getView().findViewById(R.id.textView18P);
        puntuacion = getView().findViewById(R.id.textViewTextoPuntuacionP);
        generos = getView().findViewById(R.id.textViewTextoGenerosP);
        duracion = getView().findViewById(R.id.textViewTextoDuracionP);
        fecha = getView().findViewById(R.id.textViewTextoFechaP);
        idiomaOrig = getView().findViewById(R.id.textViewTextoIdiomaOrigP);
        idiomaDisp = getView().findViewById(R.id.textViewTextoIdiomaDispP);
        presupuesto = getView().findViewById(R.id.textViewTextoPresupuestoP);
        ingresos = getView().findViewById(R.id.textViewTextoIngresosP);
        productoras = getView().findViewById(R.id.textViewTextoProductorasP);
        sinopsis = getView().findViewById(R.id.textViewTextoSinopsisP);

        favoritos = getView().findViewById(R.id.imageViewFavoritos);
        verMasTarde = getView().findViewById(R.id.imageViewVerMasTarde);

        favoritos.setImageResource(R.drawable.star);
        verMasTarde.setImageResource(R.drawable.watchlater);

        /*
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            comApi = new ComunicacionApi(getActivity());
            comApi.getMovieDetails(extras.getString("id"));
            usuario = extras.getString("usuario");
        }
        */

        gestorDB = new GestorDB (getActivity(), "DB", null, 1);

        ImageView imageViewFavoritos = getView().findViewById(R.id.imageViewFavoritos);
        imageViewFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogoAñadirFavoritos = new DialogoAñadirFavoritos(usuario, id, titulo.getText().toString(), portadaURL);
                dialogoAñadirFavoritos.show(getActivity().getSupportFragmentManager(), "añadir_favoritos");
            }
        });

        ImageView imageViewVerMasTarde = getView().findViewById(R.id.imageViewVerMasTarde);
        imageViewVerMasTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogo= new DialogoFecha();
                dialogo.show(getActivity().getSupportFragmentManager(), "fecha");
            }
        });
    }



    public void setPelicula(String pUsuario, HashMap<String, String> pInfoPelicula) {
        getView().findViewById(R.id.scrollViewPeliculaFragmentHoriz).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.imageViewFavoritos).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.imageViewVerMasTarde).setVisibility(View.VISIBLE);

        usuario = pUsuario;
        id = pInfoPelicula.get("id");
        titulo.setText(pInfoPelicula.get("titulo"));
        portadaURL = pInfoPelicula.get("portadaURL");
        //Picasso.get().load(portadaURL).into(portada);
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

    public void notificacionVMT(int pYear, int pMonth, int pDayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, pYear);
        c.set(Calendar.MONTH, pMonth);
        c.set(Calendar.DAY_OF_MONTH, pDayOfMonth);

        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("titulo", titulo.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        String fechaVerMasTarde = pYear + "/" + pMonth + "/" + pDayOfMonth;
        gestorDB.insertarPeliculaVerMasTarde(usuario, id, fechaVerMasTarde, titulo.getText().toString(), portadaURL);

        Toast aviso = Toast.makeText(getActivity(), getString(R.string.AñadidaVMT), Toast.LENGTH_LONG);
        aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        aviso.show();
    }

}
