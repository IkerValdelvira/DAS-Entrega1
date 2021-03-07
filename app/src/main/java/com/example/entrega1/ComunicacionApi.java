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
        void alRecogerInfoPelicula(HashMap<String,String> pListaPeliculas);
    }

    public void getMovieList(String pModo, String pFiltro) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextoActividad);
        String url = "";
        if("titulo".equals(pModo)){
            url ="https://api.themoviedb.org/3/search/movie?api_key=4755eb5203850a5be306380eb262c096&query=" + pFiltro;
        }
        else if("genero".equals(pModo)){
            url ="https://api.themoviedb.org/3/discover/movie?api_key=4755eb5203850a5be306380eb262c096&sort_by=popularity.desc&page=1&with_genres=" + pFiltro;
        }

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");

                            HashMap<String,String[]> resultado = new HashMap<>();
                            String[] idsArray = new String[results.length()];
                            String[] portadasURLArray = new String[results.length()];
                            String[] titulosArray = new String[results.length()];
                            String[] generosArray = new String[results.length()];
                            String[] fechasArray = new String[results.length()];
                            String[] puntuacionesArray = new String[results.length()];
                            String[] idiomasArray = new String[results.length()];
                            String[] sinopsisArray = new String[results.length()];

                            for(int i=0; i<results.length(); i++) {
                                JSONObject movie = results.getJSONObject(i);

                                int id = movie.getInt("id");
                                idsArray[i] = String.valueOf(id);

                                String portadaURL = movie.getString("poster_path");
                                if(!"null".equals(portadaURL)) {
                                    portadaURL = "https://image.tmdb.org/t/p/w500" + portadaURL;
                                }
                                else{
                                    portadaURL = "https://uxwing.com/wp-content/themes/uxwing/download/07-design-and-development/image-not-found.png";
                                }
                                portadasURLArray[i] = portadaURL;

                                String titulo = movie.getString("title");
                                titulosArray[i] = titulo;

                                JSONArray generosIDs = movie.getJSONArray("genre_ids");
                                String generos = "";
                                for(int j=0; j<generosIDs.length(); j++) {
                                    int genreID = generosIDs.getInt(j);
                                    switch (genreID)
                                    {
                                        case 28:  generos += "Action" + " - ";
                                            break;
                                        case 12:  generos += "Adventure" + " - ";
                                            break;
                                        case 16:  generos += "Animation" + " - ";
                                            break;
                                        case 35:  generos += "Comedy" + " - ";
                                            break;
                                        case 80:  generos += "Crime" + " - ";
                                            break;
                                        case 99:  generos += "Documentary" + " - ";
                                            break;
                                        case 18:  generos += "Drama" + " - ";
                                            break;
                                        case 10751:  generos += "Family" + " - ";
                                            break;
                                        case 14:  generos += "Fantasy" + " - ";
                                            break;
                                        case 36:  generos += "History" + " - ";
                                            break;
                                        case 27:  generos += "Horror" + " - ";
                                            break;
                                        case 10402:  generos += "Music" + " - ";
                                            break;
                                        case 9648:  generos += "Mystery" + " - ";
                                            break;
                                        case 10749:  generos += "Romance" + " - ";
                                            break;
                                        case 878:  generos += "Science Fiction" + " - ";
                                            break;
                                        case 10770:  generos += "TV Movie" + " - ";
                                            break;
                                        case 53:  generos += "Thriller" + " - ";
                                            break;
                                        case 10752:  generos += "War" + " - ";
                                            break;
                                        case 37:  generos += "Western" + " - ";
                                            break;
                                    }
                                }
                                if(generos.isEmpty()) {
                                    generos = "Sin categorizar.";
                                }
                                else {
                                    generos = generos.substring(0,generos.length()-3);
                                }
                                generosArray[i] = generos;

                                String fecha = movie.getString("release_date");
                                fechasArray[i] = fecha;

                                double puntuacion = movie.getDouble("vote_average");
                                puntuacionesArray[i] = String.valueOf(puntuacion);

                                String idioma = movie.getString("original_language");
                                idiomasArray[i] = idioma;

                                String sinopsis = movie.getString("overview");
                                sinopsisArray[i] = sinopsis;
                            }

                            resultado.put("ids", idsArray);
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

    public void getMovieDetails(String pIdPelicula) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextoActividad);
        String url ="https://api.themoviedb.org/3/movie/" + pIdPelicula + "?api_key=4755eb5203850a5be306380eb262c096";

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            HashMap<String,String> resultado = new HashMap<>();

                            resultado.put("id", pIdPelicula);

                            String titulo = response.getString("title");
                            resultado.put("titulo", titulo);

                            String portadaURL = response.getString("poster_path");
                            if(!"null".equals(portadaURL)) {
                                portadaURL = "https://image.tmdb.org/t/p/w500" + portadaURL;
                            }
                            else{
                                portadaURL = "https://uxwing.com/wp-content/themes/uxwing/download/07-design-and-development/image-not-found.png";
                            }
                            resultado.put("portadaURL", portadaURL);

                            boolean adulto = response.getBoolean("adult");
                            resultado.put("adulto", String.valueOf(adulto));

                            double puntuacion = response.getDouble("vote_average");
                            resultado.put("puntuacion", String.valueOf(puntuacion));

                            JSONArray generosArray = response.getJSONArray("genres");
                            String generos = "";
                            for(int i=0; i<generosArray.length(); i++) {
                                generos += generosArray.getJSONObject(i).getString("name") + " - ";
                            }
                            if(generos.isEmpty()) {
                                generos = "Sin categorizar.";
                            }
                            else {
                                generos = generos.substring(0,generos.length()-3);
                            }
                            resultado.put("generos", generos);

                            String duracion = response.getString("runtime");
                            resultado.put("duracion", duracion + "min");

                            String fecha = response.getString("release_date");
                            resultado.put("fecha", fecha);

                            String idiomaOrig = response.getString("original_language");
                            resultado.put("idiomaOrig", idiomaOrig);

                            JSONArray idiomaDispArray = response.getJSONArray("spoken_languages");
                            String idiomaDisp = "";
                            for(int i=0; i<idiomaDispArray.length(); i++) {
                                idiomaDisp += idiomaDispArray.getJSONObject(i).getString("name") + ", ";
                            }
                            if(idiomaDisp.isEmpty()) {
                                idiomaDisp = "No hay información.";
                            }
                            else {
                                idiomaDisp = idiomaDisp.substring(0,idiomaDisp.length()-2);
                            }
                            resultado.put("idiomaDisp", idiomaDisp);

                            int presupuesto = response.getInt("budget");
                            if(presupuesto == 0) {
                                resultado.put("presupuesto", "No hay información.");
                            }
                            else{
                                resultado.put("presupuesto", String.valueOf(presupuesto) + " USD");
                            }

                            int ingresos = response.getInt("revenue");
                            if(ingresos == 0) {
                                resultado.put("ingresos", "No hay información.");
                            }
                            else{
                                resultado.put("ingresos", String.valueOf(ingresos) + " USD");
                            }

                            JSONArray productorasArray = response.getJSONArray("production_companies");
                            String productoras = "";
                            for(int i=0; i<productorasArray.length(); i++) {
                                productoras += productorasArray.getJSONObject(i).getString("name") + " (" + productorasArray.getJSONObject(i).getString("origin_country") + "), ";
                            }
                            if(productoras.isEmpty()) {
                                productoras = "No hay información.";
                            }
                            else {
                                productoras = productoras.substring(0,productoras.length()-2);
                            }
                            resultado.put("productoras", productoras);

                            String sinopsis = response.getString("overview");
                            resultado.put("sinopsis", sinopsis);

                            miListener.alRecogerInfoPelicula(resultado);

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
