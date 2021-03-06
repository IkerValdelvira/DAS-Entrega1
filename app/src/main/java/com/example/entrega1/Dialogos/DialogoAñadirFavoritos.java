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

import java.util.ArrayList;

public class DialogoAñadirFavoritos extends DialogFragment {

    private String idPelicula;
    private String tituloPelicula;
    private String portadaPelicula;

    private GestorDB gestorDB;

    private String[] opciones;
    private ArrayList<String> elegidos;

    public DialogoAñadirFavoritos(String pId, String pTitulo, String pPortada) {
        idPelicula = pId;
        tituloPelicula = pTitulo;
        portadaPelicula = pPortada;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿A qué lista de favoritos deseas añadir la película '" + tituloPelicula + "'?");
        gestorDB = new GestorDB (getActivity(), "DB", null, 1);
        String[] listas = gestorDB.getListasFavoritos(idPelicula);
        if(listas.length == 0) {
            opciones = new String[]{"Crear nueva lista"};
        }
        else {
            opciones = new String[listas.length+1];
            for(int i=0; i<opciones.length-1; i++) {
                opciones[i] = listas[i];
            }
            opciones[opciones.length-1] = "Crear nueva lista";
        }
        elegidos = new ArrayList<>();
        builder.setMultiChoiceItems(opciones, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b == true){
                    elegidos.add(opciones[i]);
                }
                else if (elegidos.contains(i)){
                    elegidos.remove(opciones[i]);
                }
            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(String listaElegida : elegidos) {
                    if("Crear nueva lista".equals(listaElegida)){
                        DialogFragment dialogoCrearListaFavoritos = new DialogoCrearListaFavoritos(idPelicula, tituloPelicula, portadaPelicula);
                        dialogoCrearListaFavoritos.show(getActivity().getSupportFragmentManager(), "crear_lista_fav");
                        dialogoCrearListaFavoritos.setCancelable(false);
                    }
                    else {
                        gestorDB.insertarPelicula(listaElegida,idPelicula,tituloPelicula,portadaPelicula);
                    }
                }
                if(!elegidos.contains("Crear nueva lista")) {
                    Toast aviso = Toast.makeText(getActivity(), "Película añadida a las listas seleccionadas.", Toast.LENGTH_LONG);
                    aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    aviso.show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast aviso = Toast.makeText(getActivity(), "La película '" + tituloPelicula + "' no se ha añadido a ninguna lista de favoritos.", Toast.LENGTH_LONG);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
            }
        });

        return builder.create();
    }

}
