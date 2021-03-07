package com.example.entrega1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.entrega1.Modelos.Pelicula;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GestorDB extends SQLiteOpenHelper {

    public GestorDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuarios ('Usuario' VARCHAR(255) NOT NULL PRIMARY KEY, 'Contrasena' VARCHAR(255))");
        db.execSQL("CREATE TABLE ListasFavoritos ('Usuario' VARCHAR(255), 'Nombre' VARCHAR(255) NOT NULL, 'IdPelicula' INTEGER NOT NULL, 'Titulo' VARCHAR(255), 'Portada' VARCHAR(500), PRIMARY KEY(Nombre,IdPelicula))");
        db.execSQL("CREATE TABLE VerMasTarde ('Usuario' VARCHAR(255), 'IdPelicula' INTEGER NOT NULL, 'Fecha' VARCHAR(255) NOT NULL, 'Titulo' VARCHAR(255), 'Portada' VARCHAR(500), PRIMARY KEY(IdPelicula,Fecha))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

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

    public void insertarPeliculaFavoritos(String username, String lista, String id, String titulo, String portada) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ListasFavoritos WHERE Usuario = '" + username + "' AND Nombre = '" + lista + "' AND IdPelicula = " + Integer.parseInt(id),null);
        if(c.getCount() == 0) {
            db.execSQL("INSERT INTO ListasFavoritos VALUES ('" + username + "', '" + lista + "', " + Integer.parseInt(id) + ", '" + titulo + "', '" + portada + "')");
        }
        c.close();
        db.close();
    }

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

    public void eliminarPeliculaLista(String username, String lista, String idPelicula) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM ListasFavoritos WHERE Usuario = '" + username + "' AND Nombre = '" + lista + "' AND IdPelicula = " + Integer.parseInt(idPelicula));
        db.close();
    }

    public void insertarPeliculaVerMasTarde(String username, String id, String fecha, String titulo, String portada) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VerMasTarde WHERE Usuario = '" + username + "' AND IdPelicula = " + Integer.parseInt(id) + " AND Fecha = '" + fecha + "'",null);
        if(c.getCount() == 0) {
            db.execSQL("INSERT INTO VerMasTarde VALUES ('" + username + "', " + Integer.parseInt(id) + ", '" + fecha + "', '" + titulo + "', '" + portada + "')");
        }
        c.close();
        db.close();
    }

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

    public void eliminarPeliculaVMT(String username, String idPelicula, String fechaPelicula) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM VerMasTarde WHERE IdPelicula = " + Integer.parseInt(idPelicula) + " AND Fecha = '" + fechaPelicula + "'");
        db.close();
    }

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

    public void insertarUsuario(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Usuarios VALUES ('" + username + "', '" + password + "')");
    }

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
}
