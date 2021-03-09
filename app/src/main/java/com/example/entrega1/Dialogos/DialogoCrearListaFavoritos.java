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

    private String usuario;
    private String idPelicula;
    private String tituloPelicula;
    private String portadaPelicula;

    private GestorDB gestorDB;


    public DialogoCrearListaFavoritos(String pUsuario, String pId, String pTitulo, String pPortada) {
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
        builder.setTitle(getString(R.string.CrearNuevaLista));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.anadir_lista_fav,null);
        builder.setView(view);

        EditText editTextNombre = view.findViewById(R.id.ediTextNombre);
        GestorDB gestorDB = new GestorDB (getActivity(), "DB", null, 1);

        builder.setPositiveButton(getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombre = editTextNombre.getText().toString();
                gestorDB.insertarPeliculaFavoritos(usuario, nombre, idPelicula, tituloPelicula, portadaPelicula);
                Toast aviso = Toast.makeText(getActivity(), getString(R.string.ListaCreada), Toast.LENGTH_SHORT);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
                aviso = Toast.makeText(getActivity(), getString(R.string.PeliculaAnadidaListas), Toast.LENGTH_LONG);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
            }
        });

        builder.setNegativeButton(getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast aviso = Toast.makeText(getActivity(), getString(R.string.NoCreadoLista), Toast.LENGTH_SHORT);
                aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                aviso.show();
            }
        });

        return builder.create();
    }

}
