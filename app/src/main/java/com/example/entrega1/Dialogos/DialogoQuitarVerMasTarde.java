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

public class DialogoQuitarVerMasTarde extends DialogFragment {

    private String idPelicula;
    private String fechaPelicula;
    private String tituloPelicula;

    GestorDB gestorDB;

    ListenerdelDialogo miListener;

    public DialogoQuitarVerMasTarde(String pIdPelicula, String pFechaPelicula, String pTituloPelicula) {
        tituloPelicula = pTituloPelicula;
        idPelicula = pIdPelicula;
        fechaPelicula = pFechaPelicula;
    }

    public interface ListenerdelDialogo {
        void alBorrarPelicula();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        miListener = (ListenerdelDialogo)getActivity();

        gestorDB = new GestorDB (getActivity(), "DB", null, 1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Eliminar película de la lista");
        builder.setMessage("¿Estás seguro de que quieres quitar la película '" + tituloPelicula + "' de la lista de 'ver más tarde'?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gestorDB.eliminarPeliculaVMT(idPelicula, fechaPelicula);
                Toast aviso = Toast.makeText(getActivity(), "La película '" + tituloPelicula + "' se ha eliminado de la lista.", Toast.LENGTH_LONG);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
                miListener.alBorrarPelicula();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast aviso = Toast.makeText(getActivity(), "No se ha eliminado la película de la lista.", Toast.LENGTH_LONG);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
            }
        });

        return builder.create();
    }

}
