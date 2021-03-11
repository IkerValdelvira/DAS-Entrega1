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
import com.example.entrega1.Adaptadores.ViewHolder;
import com.example.entrega1.ComunicacionApi;
import com.example.entrega1.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CatalogoFragment extends Fragment {

    EditText editTextBuscador;
    RecyclerView recyclerView;
    AdaptadorRecycler adaptador;
    LinearLayoutManager linearLayout;

    ComunicacionApi comApi;

    String usuario;

    private ListenerFragment elListener;


    public interface ListenerFragment{
        void seleccionarElemento(String pUsuario, String pId);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            elListener = (ListenerFragment) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("La clase " + context.toString() + "debe implementar listenerDelFragment");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_catalogo,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setContentView(R.layout.activity_catalogo);

        editTextBuscador = getView().findViewById(R.id.editTextBuscador);

        recyclerView = getView().findViewById(R.id.recyclerView);
        linearLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayout);


        comApi = new ComunicacionApi(getActivity());


        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        /*
        editTextBuscador = getView().findViewById(R.id.editTextBuscador);
        if(savedInstanceState != null) {
            editTextBuscador.setText(savedInstanceState.getString("tituloBuscado"));
            comApi.getMovieList("titulo", editTextBuscador.getText().toString());
        }
        */

        Button botonBuscar = getView().findViewById(R.id.buttonBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tituloBuscado = editTextBuscador.getText().toString();
                if(tituloBuscado.isEmpty()){
                    Toast aviso = Toast.makeText(getActivity(), getString(R.string.IntroducirTitulo), Toast.LENGTH_LONG);
                    aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    aviso.show();
                }
                else{
                    comApi.getMovieList("titulo", tituloBuscado);
                }
            }
        });
    }

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

    /*
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tituloBuscado", editTextBuscador.getText().toString());
        System.out.println("SAVE");
    }
    */

}
