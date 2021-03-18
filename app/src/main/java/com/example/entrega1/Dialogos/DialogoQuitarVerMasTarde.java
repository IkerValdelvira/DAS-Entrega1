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

// Diálogo que se muestra antes de quitar una película de la lista 'ver más tarde' (tras pulsar el botón 'Quitar' de una elemento de la ListView de la actividad 'VerMasTardeActivity')
// Diálogo tipo Alert
public class DialogoQuitarVerMasTarde extends DialogFragment {

    // Datos de la película y el usuario que se van a utilizar en el diálogo
    private String usuario;
    private String idPelicula;
    private String fechaPelicula;

    GestorDB gestorDB;          // Instancia para realizar la comunicación con la base de datos local

    // Interfaz del listener para que las acciones del diálogo se ejecuten en la actividad que llamó al diálogo (VerMasTardeActivity)
    ListenerdelDialogo miListener;
    public interface ListenerdelDialogo {
        void alBorrarPelicula();
    }

    // Constructor del diálogo
    public DialogoQuitarVerMasTarde(String pUsuario, String pIdPelicula, String pFechaPelicula) {
        usuario = pUsuario;
        idPelicula = pIdPelicula;
        fechaPelicula = pFechaPelicula;
    }

    // Se ejecuta al crearse el diálogo
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        setRetainInstance(true);    // Mantiene la información del dialogo tras rotación del dispositivo

        miListener = (ListenerdelDialogo)getActivity();         // Se referencia a la implementación de la actividad

        gestorDB = new GestorDB (getActivity(), "DB", null, 1);

        // Se crea el diálogo tipo Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.EliminarPeliculaLista));
        builder.setMessage(getString(R.string.SeguroQuitarLista));

        // Se define el botón 'positivo' --> Eliminará la película de la lista de favoritos
        builder.setPositiveButton(getString(R.string.Si), new DialogInterface.OnClickListener() {
            // Se ejeucta al pulsar el botón 'positivo'
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gestorDB.eliminarPeliculaVMT(usuario, idPelicula, fechaPelicula);       // Elimina la película de la lista de favoritos de la base de datos local
                Toast.makeText(getActivity(), getString(R.string.PeliculaEliminadaLista), Toast.LENGTH_LONG).show();
                miListener.alBorrarPelicula();          // Llama al método 'alBorrarPelicula' del listener que se ejecutará en la actividad
            }
        });

        // Se define el botón 'negativo' --> Cancelará el diálogo actual
        builder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
            // Se ejeucta al pulsar el botón 'negativo'
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), getString(R.string.NoEliminadoPeliculaLista), Toast.LENGTH_LONG).show();
            }
        });

        return builder.create();
    }

}
