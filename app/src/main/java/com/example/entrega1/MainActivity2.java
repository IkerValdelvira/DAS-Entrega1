package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

    }

    public void onClickBuscar (View v) throws IOException {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.themoviedb.org/3/search/movie?api_key=4755eb5203850a5be306380eb262c096&query=Titanic";

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for(int i=0; i<results.length(); i++) {
                                JSONObject movie = results.getJSONObject(i);
                                String adult = movie.getString("adult");
                                System.out.println("ADULT: " + adult);
                                JSONArray generos = movie.getJSONArray("genre_ids");
                                System.out.println("GENEROS: " + generos.toString());
                                int id = movie.getInt("id");
                                System.out.println("ID: " + id);
                                String idioma = movie.getString("original_language");
                                System.out.println("IDIOMA ORIGINAL: " + idioma);
                                String titulo = movie.getString("title");
                                System.out.println("TITULO: " + titulo);
                                String sinopsis = movie.getString("overview");
                                System.out.println("SINOPSIS: " + sinopsis);
                                double popularidad = movie.getDouble("popularity");
                                System.out.println("POPULARIDAD: " + popularidad);
                                // Puede ser null
                                String portadaURL = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                                System.out.println("PORTADA URL: " + portadaURL);
                                String fecha = movie.getString("release_date");
                                System.out.println("FECHA: " + fecha);
                                double puntuacion = movie.getDouble("vote_average");
                                System.out.println("PUNTUACION: " + puntuacion);

                                textView.setText(textView.getText().toString() + adult);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        /*
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        */

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

}