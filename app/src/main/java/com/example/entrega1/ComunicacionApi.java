package com.example.entrega1;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ComunicacionApi {

    private Context contextoActividad;
    private ListenerApi miListener;

    public ComunicacionApi(Context pContext){
        contextoActividad = pContext;
        miListener = (ListenerApi)pContext;
    }


    public interface ListenerApi {
        void alRecogerListaPeliculas(HashMap<String,String[]> pListaPeliculas);
    }

    public void getMovieList(String pTituloBuscado) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextoActividad);
        String url ="https://api.themoviedb.org/3/search/movie?api_key=4755eb5203850a5be306380eb262c096&query=" + pTituloBuscado;

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");

                            HashMap<String,String[]> resultado = new HashMap<>();
                            String[] portadasURLArray = new String[results.length()];
                            String[] titulosArray = new String[results.length()];
                            String[] generosArray = new String[results.length()];
                            String[] fechasArray = new String[results.length()];
                            String[] puntuacionesArray = new String[results.length()];
                            String[] idiomasArray = new String[results.length()];
                            String[] sinopsisArray = new String[results.length()];

                            for(int i=0; i<results.length(); i++) {
                                JSONObject movie = results.getJSONObject(i);

                                String portadaURL = movie.getString("poster_path");
                                if(!"null".equals(portadaURL)) {
                                    portadaURL = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                                }
                                else{
                                    portadaURL = "https://uxwing.com/wp-content/themes/uxwing/download/07-design-and-development/image-not-found.png";
                                }
                                portadasURLArray[i] = portadaURL;

                                String titulo = movie.getString("title");
                                titulosArray[i] = titulo;

                                JSONArray generos = movie.getJSONArray("genre_ids");
                                generosArray[i] = generos.toString();

                                String fecha = movie.getString("release_date");
                                fechasArray[i] = fecha;

                                double puntuacion = movie.getDouble("vote_average");
                                puntuacionesArray[i] = String.valueOf(puntuacion);

                                String idioma = movie.getString("original_language");
                                idiomasArray[i] = idioma;

                                String sinopsis = movie.getString("overview");
                                sinopsisArray[i] = sinopsis;
                            }

                            resultado.put("portadasURL", portadasURLArray);
                            resultado.put("titulos", titulosArray);
                            resultado.put("generos", generosArray);
                            resultado.put("fechas", fechasArray);
                            resultado.put("puntuaciones", puntuacionesArray);
                            resultado.put("idiomas", idiomasArray);
                            resultado.put("sinopsis", sinopsisArray);

                            miListener.alRecogerListaPeliculas(resultado);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast aviso = Toast.makeText(contextoActividad, "Algo no funcionó.\nVuelve a intentarlo.", Toast.LENGTH_LONG);
                        aviso.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                        aviso.show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

}
