package com.example.entrega1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.entrega1.Modelos.Alarma;
import com.example.entrega1.Modelos.Pelicula;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

// Clase encargada de realizar la comunicación con la base datos local SQLite
public class GestorDB extends SQLiteOpenHelper {

    // Constructor de la clase
    public GestorDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Se ejecuta cuando hay que crear la base de datos porque no existe
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se crea la tabla Usuarios: utilizada para guardar credenciales de los usuarios registrados en la aplicación
        db.execSQL("CREATE TABLE Usuarios ('Usuario' VARCHAR(255) NOT NULL PRIMARY KEY, 'Contrasena' VARCHAR(255))");
        // Se crea la tabla ListasFavoritos: utilizada para guardar las películas de una lista de favoritos para un usuario
        db.execSQL("CREATE TABLE ListasFavoritos ('Usuario' VARCHAR(255), 'Nombre' VARCHAR(255) NOT NULL, 'IdPelicula' INTEGER NOT NULL, 'Titulo' VARCHAR(255), 'Portada' VARCHAR(500), PRIMARY KEY(Usuario,Nombre,IdPelicula))");
        // Se crea la tabla VerMasTarde: utilizada para guardar las películas 'ver más tarde' para un usuario
        db.execSQL("CREATE TABLE VerMasTarde ('Usuario' VARCHAR(255), 'IdPelicula' INTEGER NOT NULL, 'Fecha' VARCHAR(255) NOT NULL, 'Titulo' VARCHAR(255), 'Portada' VARCHAR(500), PRIMARY KEY(Usuario,IdPelicula,Fecha))");
        // Se crea la tabla AlarmasPendientes: utilizadas para guardar las alarmas pendientes y poder recuperarlas tras un reinicio del sistema
        db.execSQL("CREATE TABLE AlarmasPendientes ('Usuario' VARCHAR(255), 'IdPelicula' INTEGER NOT NULL, 'Anyo' INT NOT NULL, 'Mes' INT NOT NULL, 'Dia' INT NOT NULL, 'Titulo' VARCHAR(255), PRIMARY KEY(Usuario,IdPelicula,Anyo,Mes,Dia))");
    }

    // Se ejecutacuando la versión de la base de datos que queremos usar y la que existe no coinciden --> No se utiliza
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // Devuelve los nombres de las listas de favoritos (tabla 'ListasFavoritos') de un usuario
    public ArrayList<String> getListasFavoritos(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT Nombre FROM ListasFavoritos WHERE Usuario = '" + username + "'",null);
        ArrayList<String> nombres = new ArrayList<>();
        while (c.moveToNext()){
            nombres.add(c.getString(0));
        }
        c.close();
        db.close();
        return nombres;
    }

    // Devuelve los nombres de las listas de favoritos (tabla 'ListasFavoritos') de un usuario exceptuando las listas en las que se encuentra la película obtenida en los parámetros
    public String[] getListasFavoritos(String username, String idPelicula) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Nombre FROM ListasFavoritos WHERE Usuario = '" + username + "' EXCEPT SELECT Nombre FROM ListasFavoritos WHERE Usuario = '" + username + "' AND IdPelicula = " + Integer.parseInt(idPelicula),null);
        String[] nombres = new String[c.getCount()];
        int i = 0;
        while (c.moveToNext()){
            nombres[i] = c.getString(0);
            i++;
        }
        c.close();
        db.close();
        return nombres;
    }

    // Inserta la información de una película en una lista de favoritos (tabla 'ListasFavoritos') de un usuario si no existe hasta el momento
    public void insertarPeliculaFavoritos(String username, String lista, String id, String titulo, String portada) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ListasFavoritos WHERE Usuario = '" + username + "' AND Nombre = '" + lista + "' AND IdPelicula = " + Integer.parseInt(id),null);
        if(c.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Usuario", username);
            contentValues.put("Nombre", lista);
            contentValues.put("IdPelicula", id);
            contentValues.put("Titulo", titulo);
            contentValues.put("Portada", portada);
            db.insert("ListasFavoritos", null, contentValues);
        }
        c.close();
        db.close();
    }

    // Devuelve la información de las películas de las lista de favorito (tabla 'ListasFavoritos') solicitada de un usuario
    public Pelicula[] getPeliculasListaFav(String username, String lista) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT IdPelicula, Titulo, Portada FROM ListasFavoritos WHERE Usuario = '" + username + "' AND Nombre = '" + lista + "'",null);
        Pelicula[] peliculas = new Pelicula[c.getCount()];
        int i = 0;
        while (c.moveToNext()){
            Pelicula pelicula = new Pelicula(String.valueOf(c.getInt(0)), c.getString(1), c.getString(2));
            peliculas[i] = pelicula;
            i++;
        }
        c.close();
        db.close();
        return peliculas;
    }

    // Elimina una película de una lista de favoritos (tabla 'ListasFavoritos') de un usuario
    public void eliminarPeliculaLista(String username, String lista, String idPelicula) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM ListasFavoritos WHERE Usuario = '" + username + "' AND Nombre = '" + lista + "' AND IdPelicula = " + Integer.parseInt(idPelicula));
        db.close();
    }

    // Inserta la información de una película en la lista 'ver más tarde' (tabla 'ListasFavoritos') de un usuario si no existe hasta el momento
    public void insertarPeliculaVerMasTarde(String username, String id, String fecha, String titulo, String portada) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VerMasTarde WHERE Usuario = '" + username + "' AND IdPelicula = " + Integer.parseInt(id) + " AND Fecha = '" + fecha + "'",null);
        if(c.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Usuario", username);
            contentValues.put("IdPelicula", id);
            contentValues.put("Fecha", fecha);
            contentValues.put("Titulo", titulo);
            contentValues.put("Portada", portada);
            db.insert("VerMasTarde", null, contentValues);
        }
        c.close();
        db.close();
    }

    // Inserta la información de una alarma pendiente en la tabla 'AlarmasPendientes' de un usuario si no existe hasta el momento
    public void insertarAlarma(String username, String id, int anyo, int mes, int dia, String titulo, String fecha) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VerMasTarde WHERE Usuario = '" + username + "' AND IdPelicula = " + Integer.parseInt(id) + " AND Fecha = '" + fecha + "'",null);
        if(c.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Usuario", username);
            contentValues.put("IdPelicula", id);
            contentValues.put("Anyo", anyo);
            contentValues.put("Mes", mes);
            contentValues.put("Dia", dia);
            contentValues.put("Titulo", titulo);
            db.insert("AlarmasPendientes", null, contentValues);
        }
        c.close();
        db.close();
    }

    // Devuelve la información de las películas de la lista 'ver más tarde' (tabla 'VerMasTarde') de un usuario
    public Pelicula[] getPeliculasVMT(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT IdPelicula, Fecha, Titulo, Portada FROM VerMasTarde WHERE Usuario = '" + username + "'",null);
        Pelicula[] peliculas = new Pelicula[c.getCount()];
        int i = 0;
        while (c.moveToNext()){
            Pelicula pelicula = new Pelicula(String.valueOf(c.getInt(0)), c.getString(2), c.getString(3), c.getString(1));
            peliculas[i] = pelicula;
            i++;
        }
        c.close();
        db.close();
        return peliculas;
    }

    // Elimina una película de la lista 'ver más tarde' (tabla 'VerMasTarde') de un usuario
    public void eliminarPeliculaVMT(String username, String idPelicula, String fechaPelicula) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM VerMasTarde WHERE Usuario = '" + username + "' AND IdPelicula = " + Integer.parseInt(idPelicula) + " AND Fecha = '" + fechaPelicula + "'");
        db.close();
    }

    // Elimina una alarma de la tabla 'AlarmasPendientes' de un usuario
    public void eliminarAlarma(String username, String idPelicula, int anyo, int mes, int dia) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM AlarmasPendientes WHERE Usuario = '" + username + "' AND IdPelicula = " + Integer.parseInt(idPelicula) + " AND Anyo = " + anyo + " AND Mes = " + mes + " AND Dia = " + dia);
        db.close();
    }

    // Comprueba si ya existe un usuario con el nombre recibido por parámetro en la tabla 'Usuarios'
    public Boolean existeUsuario(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Usuario FROM Usuarios WHERE Usuario = '" + username + "'",null);
        if(c.getCount() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    // Inserta un nuevo usuario en la tabla Usuarios
    public void insertarUsuario(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Usuario", username);
        contentValues.put("Contrasena", password);
        db.insert("Usuarios", null, contentValues);
    }

    // Comprueba si existe un usuario con el nombre y contraseña recibidos por parámetros en la tabla 'Usuarios'
    public boolean loginCorrecto(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Usuario FROM Usuarios WHERE Usuario = '" + username + "' AND Contrasena = '" + password + "'",null);
        if(c.getCount() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    // Devuelve la información de las alarmas pendientes (tabla 'AlarmasPendientes')
    public ArrayList<Alarma> getAlarmasPendientes() {
        ArrayList<Alarma> resultado = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM AlarmasPendientes",null);
        while (c.moveToNext()){
            Alarma alarma = new Alarma(c.getString(0), String.valueOf(c.getInt(1)), c.getInt(2), c.getInt(3), c.getInt(4), c.getString(5));
            resultado.add(alarma);
        }
        return resultado;
    }
}
