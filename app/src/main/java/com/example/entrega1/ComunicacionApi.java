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

// Clase encargada de realizar la comunicación con el API 'themoviedb'
public class ComunicacionApi {

    private Context contextoActividad;      // Instancia del contexto de la actividad desde la que se crea una instancia 'ComunicacionApi'

    // Interfaz del listener para que la información recibida del API se utilice en la actividad desde donde creado la instancia 'ComunicacionApi'
    private ListenerApi miListener;
    public interface ListenerApi {
        void alRecogerListaPeliculas(HashMap<String,String[]> pListaPeliculas);
        void alRecogerInfoPelicula(HashMap<String,String> pPelicula);
    }

    // Contructor de la clase
    public ComunicacionApi(Context pContext){
        contextoActividad = pContext;
        miListener = (ListenerApi)pContext;
    }

    // Método para realizar peticiones HTTP al API 'themoviedb' para obtener una lista de películas relacionadas con un título o género
    public void getMovieList(String pModo, String pFiltro) {
        // Se utiliza la librería Volley para realizar solicitudes HTTP desde una aplicación Android
        // Se instancia un 'RequestQueue' para la petición
        RequestQueue queue = Volley.newRequestQueue(contextoActividad);

        String url = "";
        if("titulo".equals(pModo)){
            // Si se buscar por título, se usa la siguiente URL
            url ="https://api.themoviedb.org/3/search/movie?api_key=4755eb5203850a5be306380eb262c096&query=" + pFiltro;
        }
        else if("genero".equals(pModo)){
            // Si se buscar por género, se usa la siguiente URL
            url ="https://api.themoviedb.org/3/discover/movie?api_key=4755eb5203850a5be306380eb262c096&sort_by=popularity.desc&page=1&with_genres=" + pFiltro;
        }

        // Se solicita una respuesta JSON a la URL especificada
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    // Se ejecuta al recibir la respuesta HTTP en formato JSON
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Se accede al objeto JSON donde se encuentra la información solicitada
                            JSONArray results = response.getJSONArray("results");

                            // Se va a guardar la información necesaria extraida del objeto JSON por cada película recibida
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
                                        // Los géneros se identifican en el API 'themoviedb' con un 'genreID'
                                        // Se transforma el 'genreID' en el nombre del género correspondiente
                                        case 28:  generos += contextoActividad.getString(R.string.Accion) + " - ";
                                            break;
                                        case 12:  generos += contextoActividad.getString(R.string.Aventura) + " - ";
                                            break;
                                        case 16:  generos += contextoActividad.getString(R.string.Animacion) + " - ";
                                            break;
                                        case 35:  generos += contextoActividad.getString(R.string.Comedia) + " - ";
                                            break;
                                        case 80:  generos += contextoActividad.getString(R.string.Crimen) + " - ";
                                            break;
                                        case 99:  generos += contextoActividad.getString(R.string.Documental) + " - ";
                                            break;
                                        case 18:  generos += contextoActividad.getString(R.string.Drama) + " - ";
                                            break;
                                        case 10751:  generos += contextoActividad.getString(R.string.Familia) + " - ";
                                            break;
                                        case 14:  generos += contextoActividad.getString(R.string.Fantasia) + " - ";
                                            break;
                                        case 36:  generos += contextoActividad.getString(R.string.Historia) + " - ";
                                            break;
                                        case 27:  generos += contextoActividad.getString(R.string.Terror) + " - ";
                                            break;
                                        case 10402:  generos += contextoActividad.getString(R.string.Musica) + " - ";
                                            break;
                                        case 9648:  generos += contextoActividad.getString(R.string.Misterio) + " - ";
                                            break;
                                        case 10749:  generos += contextoActividad.getString(R.string.Romance) + " - ";
                                            break;
                                        case 878:  generos += contextoActividad.getString(R.string.CienciaFiccion) + " - ";
                                            break;
                                        case 10770:  generos += contextoActividad.getString(R.string.TVPelicula) + " - ";
                                            break;
                                        case 53:  generos += contextoActividad.getString(R.string.Thriller) + " - ";
                                            break;
                                        case 10752:  generos += contextoActividad.getString(R.string.Guerra) + " - ";
                                            break;
                                        case 37:  generos += contextoActividad.getString(R.string.Western) + " - ";
                                            break;
                                    }
                                }
                                if(generos.isEmpty()) {
                                    generos = contextoActividad.getString(R.string.SinCategorizar);
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

                            // Llama al método 'alRecogerListaPeliculas' del listener que se ejecutará en la actividad que a creado una instancia 'ComunicacionApi'
                            miListener.alRecogerListaPeliculas(resultado);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    // Se ejecuta en caso de error al hacer la petición HTTP al API 'themoviedb'
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(contextoActividad, contextoActividad.getString(R.string.AlgoNoFunciono), Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Se añade la petición al 'RequestQueue'
        queue.add(jsonObjectRequest);
    }

    // Método para realizar peticiones HTTP al API 'themoviedb' para obtener la información detallada de una película
    public void getMovieDetails(String pIdPelicula) {
        // Se utiliza la librería Volley para realizar solicitudes HTTP desde una aplicación Android
        // Se instancia un 'RequestQueue' para la petición
        RequestQueue queue = Volley.newRequestQueue(contextoActividad);

        // Se usa la siguiente URL con el id de la película
        String url ="https://api.themoviedb.org/3/movie/" + pIdPelicula + "?api_key=4755eb5203850a5be306380eb262c096";

        // Se solicita una respuesta JSON a la URL especificada
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    // Se ejecuta al recibir la respuesta HTTP en formato JSON
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Se va a guardar la información necesaria extraida del objeto JSON para la película solicitada
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
                                generos = contextoActividad.getString(R.string.SinCategorizar);
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
                                idiomaDisp = contextoActividad.getString(R.string.NoHayInformacion);
                            }
                            else {
                                idiomaDisp = idiomaDisp.substring(0,idiomaDisp.length()-2);
                            }
                            resultado.put("idiomaDisp", idiomaDisp);

                            int presupuesto = response.getInt("budget");
                            if(presupuesto == 0) {
                                resultado.put("presupuesto", contextoActividad.getString(R.string.NoHayInformacion));
                            }
                            else{
                                resultado.put("presupuesto", String.valueOf(presupuesto) + " USD");
                            }

                            int ingresos = response.getInt("revenue");
                            if(ingresos == 0) {
                                resultado.put("ingresos", contextoActividad.getString(R.string.NoHayInformacion));
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
                                productoras = contextoActividad.getString(R.string.NoHayInformacion);
                            }
                            else {
                                productoras = productoras.substring(0,productoras.length()-2);
                            }
                            resultado.put("productoras", productoras);

                            String sinopsis = response.getString("overview");
                            resultado.put("sinopsis", sinopsis);

                            // Llama al método 'alRecogerInfoPelicula' del listener que se ejecutará en la actividad que a creado una instancia 'ComunicacionApi'
                            miListener.alRecogerInfoPelicula(resultado);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            // Se ejecuta en caso de error al hacer la petición HTTP al API 'themoviedb'
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contextoActividad, contextoActividad.getString(R.string.AlgoNoFunciono), Toast.LENGTH_LONG).show();
            }
        });

        // Se añade la petición al 'RequestQueue'
        queue.add(jsonObjectRequest);
    }

}
