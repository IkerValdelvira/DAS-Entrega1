package com.example.entrega1.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.entrega1.GestorDB;
import com.example.entrega1.R;

public class DialogoCrearListaFavoritos extends DialogFragment {

    private String idPelicula;
    private String tituloPelicula;
    private String portadaPelicula;

    private GestorDB gestorDB;


    public DialogoCrearListaFavoritos(String pId, String pTitulo, String pPortada) {
        idPelicula = pId;
        tituloPelicula = pTitulo;
        portadaPelicula = pPortada;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Crear nueva lista de favoritos");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.anadir_lista_fav,null);
        builder.setView(view);

        EditText editTextNombre = view.findViewById(R.id.ediTextNombre);
        GestorDB gestorDB = new GestorDB (getActivity(), "DB", null, 1);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombre = editTextNombre.getText().toString();
                gestorDB.insertarPeliculaFavoritos(nombre, idPelicula, tituloPelicula, portadaPelicula);
                Toast aviso = Toast.makeText(getActivity(), "Lista '" + nombre + "' creada correctamente.", Toast.LENGTH_SHORT);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
                aviso = Toast.makeText(getActivity(), "Película añadida a las listas seleccionadas.", Toast.LENGTH_LONG);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast aviso = Toast.makeText(getActivity(), "No se ha creado una nueva lista de favoritos.", Toast.LENGTH_SHORT);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
            }
        });

        return builder.create();
    }

}