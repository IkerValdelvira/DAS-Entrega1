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

import com.example.entrega1.Alarmas.AlarmReceiver;
import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.Dialogos.DialogoAñadirFavoritos;
import com.example.entrega1.Dialogos.DialogoFecha;
import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

import java.util.Calendar;
import java.util.HashMap;

// Fragment que muestra los detalles de una película seleccionada y da opciones de marcarla como favorita o 'ver más tarde'
// Se utiliza en la actividad CatalogoActivity cuando el dispositivo esta en orientación apaisada (horizontal)
public class PeliculaFragmentHoriz extends Fragment {

    // Datos de la película y elementos necesarios del layout 'fragment_pelicula_horiz.xml'
    private String id;
    private String portadaURL;
    private TextView titulo;
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

    private GestorDB gestorDB;          // Instancia para realizar la comunicación con la base de datos local

    private String usuario;             // Nombre del usuario que ha creado la actividad

    // Enlaza la clase del fragment con su layout correspondiente 'fragment_pelicula_horiz.xml'
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pelicula_horiz,container,false);
        return v;
    }

    // Se ejecuta cuando se ha creado la actividad relacionada con ese fragment (CatalogoActivity con el dispositivo en orientación apaisada)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Se inicializan los elementos necesarios del layoout
        titulo = getView().findViewById(R.id.textViewTextoTituloP);
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

        gestorDB = new GestorDB (getActivity(), "DB", null, 1);

        // Listener 'onClick' del ImageView para añadir la película a una lista de favoritos
        favoritos.setOnClickListener(new View.OnClickListener() {
            // Se ejecuta al pulsar el ImageView para añadir la película a una lista de favoritos
            @Override
            public void onClick(View v) {
                // Se crea un diálogo DialogoAñadirFavoritos con las opciones de listas de favoritos para añadir la película actual
                DialogFragment dialogoAñadirFavoritos = new DialogoAñadirFavoritos(usuario, id, titulo.getText().toString(), portadaURL);
                dialogoAñadirFavoritos.show(getActivity().getSupportFragmentManager(), "añadir_favoritos");
            }
        });

        // Listener 'onClick' del ImageView para añadir la película a la lista 'ver más tarde'
        verMasTarde.setOnClickListener(new View.OnClickListener() {
            // Se ejecuta al pulsar el ImageView para añadir la película a la lista 'ver más tarde'
            @Override
            public void onClick(View v) {
                // Se crea un diálogo DialogoFecha para seleccionar la fecha de cuándo se quiere programar la película
                DialogFragment dialogo= new DialogoFecha();
                dialogo.show(getActivity().getSupportFragmentManager(), "fecha");
            }
        });
    }

    // Añade la información de la película seleccionada a los elementos del layout 'fragment_pelicula_horiz.xml'
    // Este método se llama desde CatalogoActivity
    public void setPelicula(String pUsuario, HashMap<String, String> pInfoPelicula) {
        // En un principio si no hay ninguna película del catálogo seleccionada los elementos del layout son invisibles
        getView().findViewById(R.id.scrollViewPeliculaFragmentHoriz).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.imageViewFavoritos).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.imageViewVerMasTarde).setVisibility(View.VISIBLE);

        usuario = pUsuario;
        id = pInfoPelicula.get("id");
        titulo.setText(pInfoPelicula.get("titulo"));
        portadaURL = pInfoPelicula.get("portadaURL");
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
            sinopsis.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);       // Se justifica el texto dentro del TextView 'sinopsis'
        }
    }

    // Programa una alarma broadcast mediante AlarmManager que creará una notificación en la fecha recibida
    // Este método se llama desde CatalogoActivity después de pulsar en 'Añadir a ver más tarde' y elegir una fecha
    public void notificacionVMT(int pYear, int pMonth, int pDayOfMonth) {
        // Se crea un calendario con la fecha recibida
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, pYear);
        c.set(Calendar.MONTH, pMonth);
        c.set(Calendar.DAY_OF_MONTH, pDayOfMonth);
        String fechaVerMasTarde = pYear + "/" + pMonth + "/" + pDayOfMonth;

        // Se programa una nueva alarma (AlarmManager) con la información de la película y la fecha recibida
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.setAction("alarma");
        intent.putExtra("usuario", usuario);
        intent.putExtra("id", id);
        intent.putExtra("titulo", titulo.getText().toString());
        intent.putExtra("anyo", pYear);
        intent.putExtra("mes", pMonth);
        intent.putExtra("dia", pDayOfMonth);

        // PendingIntent que lanza un broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Se le indica al AlarmManager cuándo quiere lanzar el PendingIntent
        //  - RTC_WAKEUP: lanza la alarma a la hora especificada despertando el dispositivo
        //  - setExactAndAllowWhileIdle: la alarma funciona en modo descanso (Doze) y con exactitud
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        gestorDB.insertarPeliculaVerMasTarde(usuario, id, fechaVerMasTarde, titulo.getText().toString(), portadaURL);           // Se inserta la información de la película para 'ver mas tarde' en la base de datos local
        gestorDB.insertarAlarma(usuario, id, pYear, pMonth, pDayOfMonth, titulo.getText().toString(), fechaVerMasTarde);        // Se inserta la información de la alarma creada en la base de datos local --> Se utilizará al reiniciar el dispositivo para restablecer las alarmas pendientes

        Toast.makeText(getActivity(), getString(R.string.AñadidaVMT), Toast.LENGTH_SHORT).show();
    }

}
