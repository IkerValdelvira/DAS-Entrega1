package com.example.entrega1.Adaptadores;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrega1.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public String id;
    public ImageView portada;
    public TextView titulo;
    public TextView generos;
    public TextView fecha;
    public TextView puntuacion;
    public TextView idioma;
    public TextView sinopsis;
    public boolean[] seleccion;
    private String usuario;

    public ViewHolder(String pUsuario, @NonNull View itemView){
        super(itemView);

        usuario = pUsuario;
        portada = itemView.findViewById(R.id.imageViewPortada);
        titulo = itemView.findViewById(R.id.textViewTitulo);
        generos = itemView.findViewById(R.id.textViewGeneros);
        fecha = itemView.findViewById(R.id.textViewFechaTexto);
        puntuacion = itemView.findViewById(R.id.textViewPuntuacionTexto);
        idioma = itemView.findViewById(R.id.textViewIdiomaTexto);
        sinopsis = itemView.findViewById(R.id.textViewTextoSinopsis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sinopsis.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        /*
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pelicula2 pelicula = new Pelicula2(usuario, id);
                //elListener.seleccionarElemento(pelicula);
                System.out.println(id);

                int orientation = view.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                    //EL OTRO FRAGMENT EXISTE
                    //PeliculaFragment peliculaFragment = (PeliculaFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentPeliculaHoriz);
                    //peliculaFragment.setPelicula(pelicula);
                    System.out.println("HORIZONTAL");
                }
                else{
                    //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
                    Intent i= new Intent(view.getContext(), PeliculaActivity2.class);
                    i.putExtra("id", id);
                    i.putExtra("usuario", usuario);
                    view.getContext().startActivity(i);
                }


                Intent i = new Intent (view.getContext(), PeliculaActivity.class);
                i.putExtra("id", id);
                i.putExtra("usuario", usuario);
                view.getContext().startActivity(i);

            }
        });
        */
    }

}
