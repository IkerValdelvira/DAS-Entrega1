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

public class DialogoQuitarVerMasTarde extends DialogFragment {

    private String usuario;
    private String idPelicula;
    private String fechaPelicula;
    private String tituloPelicula;

    GestorDB gestorDB;

    ListenerdelDialogo miListener;

    public DialogoQuitarVerMasTarde(String pUsuario, String pIdPelicula, String pFechaPelicula, String pTituloPelicula) {
        usuario = pUsuario;
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
        builder.setTitle(getString(R.string.EliminarPeliculaLista));
        builder.setMessage(getString(R.string.SeguroQuitarLista));

        builder.setPositiveButton(getString(R.string.Si), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gestorDB.eliminarPeliculaVMT(usuario, idPelicula, fechaPelicula);
                Toast aviso = Toast.makeText(getActivity(), getString(R.string.PeliculaEliminadaLista), Toast.LENGTH_LONG);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
                miListener.alBorrarPelicula();
            }
        });

        builder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast aviso = Toast.makeText(getActivity(), getString(R.string.NoEliminadoPeliculaLista), Toast.LENGTH_LONG);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
            }
        });

        return builder.create();
    }

}
