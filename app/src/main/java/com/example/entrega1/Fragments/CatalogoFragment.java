package com.example.entrega1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega1.Adaptadores.AdaptadorRecycler;
import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.R;

import java.util.HashMap;

// Fragment que representa el catálogo de películas que el usuario puede buscar
// Se utiliza en la actividad CatalogoActivity
public class CatalogoFragment extends Fragment {

    // Elementos necesarios del layout 'fragment_catalogo.xml'
    private EditText editTextBuscador;
    private RecyclerView recyclerView;

    private AdaptadorRecycler adaptador;            // Adaptador del RecyclerView
    private LinearLayoutManager linearLayout;       // Layout para el RecyclerView

    private ComunicacionApi comApi;                 // Instancia para realizar la comunicación con el API 'themoviedb'

    private String usuario;                         // Nombre del usuario que ha creado la actividad

    // Interfaz del listener para que las acciones del fragment se ejecuten en la actividad que lo contiene (CatalogoActivity)
    private ListenerFragment elListener;
    public interface ListenerFragment{
        void seleccionarElemento(String pUsuario, String pId);
    }

    // Une el listener con los métodos implementados en la actividad
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            elListener = (ListenerFragment) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("La clase " + context.toString() + "debe implementar listenerDelFragment");
        }
    }

    // Enlaza la clase del fragment con su layout correspondiente 'fragment_catalogo.xml'
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_catalogo,container,false);
        return v;
    }

    // Se ejecuta cuando se ha creado la actividad relacionada con ese fragment (CatalogoActivity)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Se inicializan los elementos necesarios del layoout
        editTextBuscador = getView().findViewById(R.id.editTextBuscador);
        recyclerView = getView().findViewById(R.id.recyclerView);
        linearLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);       // Los elementos se muestran de forma lineal vertical
        recyclerView.setLayoutManager(linearLayout);

        // Inicialización de la instancia para la comunicacíon con el API 'themoviedb'
        comApi = new ComunicacionApi(getActivity());

        // Inicialización del nombre de usuario obtenido a través del Bundle asociado al Intent que ha creado la actividad
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        Button botonBuscar = getView().findViewById(R.id.buttonBuscar);
        // Listener 'onClick' del botón para buscar películas relacionadas con un título
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            // Se ejecuta al pulsar el botón para buscar películas relacionadas con un título
            @Override
            public void onClick(View v) {
                String tituloBuscado = editTextBuscador.getText().toString();
                if(tituloBuscado.isEmpty()){
                    Toast.makeText(getActivity(), getString(R.string.IntroducirTitulo), Toast.LENGTH_SHORT).show();
                }
                else{
                    comApi.getMovieList("titulo", tituloBuscado);       // Se obtienen las películas relacionadas con el titulo buscado mediante el API 'themoviedb'
                }
            }
        });
    }

    // Añade la lista de películas obtenidas al adaptador del RecyclerView
    // Este método se llama desde CatalogoActivity
    public void setListaPeliculas(HashMap<String, String[]> movieList) {
        String[] ids = movieList.get("ids");
        String[] portadasURL = movieList.get("portadasURL");
        String[] titulos = movieList.get("titulos");
        String[] generos = movieList.get("generos");
        String[] fechas = movieList.get("fechas");
        String[] puntuaciones = movieList.get("puntuaciones");
        String[] idiomas = movieList.get("idiomas");
        String[] sinopsis = movieList.get("sinopsis");

        adaptador = new AdaptadorRecycler(elListener,usuario,ids,portadasURL,titulos,generos,fechas,puntuaciones,idiomas,sinopsis);
        recyclerView.setAdapter(adaptador);
    }

    // Devuelve el EditText para introducir un título
    public EditText getEditTextBuscador() {
        return editTextBuscador;
    }

    // Establece un título en el EditText del buscador
    public void setTextBuscador(String pTitulo) {
        editTextBuscador.setText(pTitulo);
    }

}
