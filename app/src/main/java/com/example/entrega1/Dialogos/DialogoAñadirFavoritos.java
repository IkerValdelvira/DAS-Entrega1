package com.example.entrega1.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

import java.util.ArrayList;

// Diálogo que se muestra antes de añadir una película a una lista de favoritos (tras pulsar el botón 'Añadir a favoritos' de la actividad 'PeliculaActivity')
// Diálogo con listado de opciónes para añadir la película a una lista ya creada o a una nueva lista a crear
public class DialogoAñadirFavoritos extends DialogFragment {

    // Datos de la película y el usuario que se van a utilizar en el diálogo
    private String usuario;
    private String idPelicula;
    private String tituloPelicula;
    private String portadaPelicula;

    private GestorDB gestorDB;                  // Instancia para realizar la comunicación con la base de datos local

    private String[] opciones;                  // Array con las opciones de listas de favoritos
    private ArrayList<String> elegidos;         // Array para almacenar las opciones elegidas

    // Constructor del diálogo
    public DialogoAñadirFavoritos(String pUsuario, String pId, String pTitulo, String pPortada) {
        usuario = pUsuario;
        idPelicula = pId;
        tituloPelicula = pTitulo;
        portadaPelicula = pPortada;
    }

    // Se ejecuta al crearse el diálogo
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        setRetainInstance(true);        // Mantiene la información del dialogo tras rotación del dispositivo

        // Creación del listado de opciones --> Las listas de favoritos disponibles se obtienen de la base de datos local y se guardan el el Array 'opciones'
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.AQueListaFav));
        gestorDB = new GestorDB (getActivity(), "DB", null, 1);
        String[] listas = gestorDB.getListasFavoritos(usuario, idPelicula);
        // Siempre se añade la opción 'Crear nueva lista' como última opción
        if(listas.length == 0) {
            opciones = new String[]{getString(R.string.CrearLista)};
        }
        else {
            opciones = new String[listas.length+1];
            for(int i=0; i<opciones.length-1; i++) {
                opciones[i] = listas[i];
            }
            opciones[opciones.length-1] = getString(R.string.CrearLista);
        }

        elegidos = new ArrayList<>();
        // Se puede elegir más de una opción del listado (tipo checkbox)
        builder.setMultiChoiceItems(opciones, null, new DialogInterface.OnMultiChoiceClickListener() {
            // Se ejecuta al pulsar una opción del listado
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // Se añade o elimina la opción pulsada al Array 'elegidos' dependiendo si ya estaba o no
                if (b == true){
                    elegidos.add(opciones[i]);
                }
                else {
                    elegidos.remove(opciones[i]);
                }
            }
        });

        // Se define el botón 'positivo' --> Añadirá la película a las listas seleccionadas entre las opciones
        builder.setPositiveButton(getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
            // Se ejeucta al pulsar el botón 'positivo'
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Se itera por cada opción (lista de favoritos) seleccionada
                for(String listaElegida : elegidos) {
                    if(getString(R.string.CrearLista).equals(listaElegida)){
                        // Si se ha elegido la opción 'crear nueva lista', se crea un nuevo diálogo DialogoCrearListaFavoritos en el que el usuario introducirá el nombre de la nueva lista
                        DialogFragment dialogoCrearListaFavoritos = new DialogoCrearListaFavoritos(usuario, idPelicula, tituloPelicula, portadaPelicula);
                        dialogoCrearListaFavoritos.show(getActivity().getSupportFragmentManager(), "crear_lista_fav");
                        dialogoCrearListaFavoritos.setCancelable(false);
                    }
                    else {
                        // Se añade la película a la lista de favoritos correspondiente de la base de datos local
                        gestorDB.insertarPeliculaFavoritos(usuario, listaElegida,idPelicula,tituloPelicula,portadaPelicula);
                    }
                }
                if(!elegidos.contains(getString(R.string.CrearLista))){
                    // Si no ha habido que crear una nueva lista, se avisa al usuario mediante un Toast de que la película ha sido añadida a todas las listas
                    // Si ha habido que crear una nueva lista, se le avisará una vez creada esa lista en el diálogo DialogoCrearListaFavoritos
                    Toast.makeText(getActivity(), getString(R.string.PeliculaAnadidaListas), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Se define el botón 'negativo' --> Cancelará el diálogo actual
        builder.setNegativeButton(getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
            // Se ejeucta al pulsar el botón 'negativo'
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), getString(R.string.PeliculaNoAnadidaListas), Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}
