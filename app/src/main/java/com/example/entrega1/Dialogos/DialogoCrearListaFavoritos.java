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

// Diálogo que se muestra antes de crear una nueva lista de favoritos (tras pulsar la opción 'Crear nueva lista' en el diálogo 'DialogoAñadirFavoritos')
// Diálogo con diseño personalizado para introducir el nombre de la nueva lista a crear
public class DialogoCrearListaFavoritos extends DialogFragment {

    // Datos de la película y el usuario que se van a utilizar en el diálogo
    private String usuario;
    private String idPelicula;
    private String tituloPelicula;
    private String portadaPelicula;

    private GestorDB gestorDB;          // Instancia para realizar la comunicación con la base de datos local

    // Constructor del diálogo
    public DialogoCrearListaFavoritos(String pUsuario, String pId, String pTitulo, String pPortada) {
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

        // Creación del diálogo con diseño personalizado mediante el layout 'anadir_lista_fav.xml'
        // El usuario introducirá el nombre de la nueva lista de favoritos en un EditText
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.CrearNuevaLista));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.anadir_lista_fav,null);
        builder.setView(view);

        EditText editTextNombre = view.findViewById(R.id.ediTextNombre);
        gestorDB = new GestorDB (getActivity(), "DB", null, 1);

        // Se define el botón 'positivo' --> Creará la nueva lista e insertará la película en ella
        builder.setPositiveButton(getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
            // Se ejeucta al pulsar el botón 'positivo'
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombre = editTextNombre.getText().toString();
                if(!nombre.isEmpty()){
                    // Si el nombre no está vacío, se crea la lista y se inserta la película en la base de datos local
                    gestorDB.insertarPeliculaFavoritos(usuario, nombre, idPelicula, tituloPelicula, portadaPelicula);
                    Toast.makeText(getActivity(), getString(R.string.ListaCreada), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), getString(R.string.PeliculaAnadidaListas), Toast.LENGTH_SHORT).show();
                }
                else {
                    // Si el nombre está vacío se vuelve a crear el diálogo
                    Toast.makeText(getActivity(), getString(R.string.RellenarCampos), Toast.LENGTH_SHORT).show();
                    DialogFragment dialogoCrearListaFavoritos = new DialogoCrearListaFavoritos(usuario, idPelicula, tituloPelicula, portadaPelicula);
                    dialogoCrearListaFavoritos.show(getActivity().getSupportFragmentManager(), "crear_lista_fav");
                    dialogoCrearListaFavoritos.setCancelable(false);
                }

            }
        });

        // Se define el botón 'negativo' --> Cancelará el diálogo actual
        builder.setNegativeButton(getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
            // Se ejeucta al pulsar el botón 'negativo'
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
