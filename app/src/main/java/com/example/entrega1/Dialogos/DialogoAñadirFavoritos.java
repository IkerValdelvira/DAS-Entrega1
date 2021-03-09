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

public class DialogoAñadirFavoritos extends DialogFragment {

    private String usuario;
    private String idPelicula;
    private String tituloPelicula;
    private String portadaPelicula;

    private GestorDB gestorDB;

    private String[] opciones;
    private ArrayList<String> elegidos;

    public DialogoAñadirFavoritos(String pUsuario, String pId, String pTitulo, String pPortada) {
        usuario = pUsuario;
        idPelicula = pId;
        tituloPelicula = pTitulo;
        portadaPelicula = pPortada;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.AQueListaFav));
        gestorDB = new GestorDB (getActivity(), "DB", null, 1);
        String[] listas = gestorDB.getListasFavoritos(usuario, idPelicula);
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

        builder.setPositiveButton(getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(String listaElegida : elegidos) {
                    if(getString(R.string.CrearLista).equals(listaElegida)){
                        DialogFragment dialogoCrearListaFavoritos = new DialogoCrearListaFavoritos(usuario, idPelicula, tituloPelicula, portadaPelicula);
                        dialogoCrearListaFavoritos.show(getActivity().getSupportFragmentManager(), "crear_lista_fav");
                        dialogoCrearListaFavoritos.setCancelable(false);
                    }
                    else {
                        gestorDB.insertarPeliculaFavoritos(usuario, listaElegida,idPelicula,tituloPelicula,portadaPelicula);
                    }
                }
                if(!elegidos.contains(getString(R.string.CrearCuenta))) {
                    Toast aviso = Toast.makeText(getActivity(), getString(R.string.PeliculaAnadidaListas), Toast.LENGTH_LONG);
                    aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    aviso.show();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast aviso = Toast.makeText(getActivity(), getString(R.string.PeliculaNoAnadidaListas), Toast.LENGTH_LONG);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
            }
        });

        return builder.create();
    }

}
