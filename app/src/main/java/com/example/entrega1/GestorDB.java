package com.example.entrega1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GestorDB extends SQLiteOpenHelper {

    public GestorDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ListasFavoritos ('Nombre' VARCHAR(255) NOT NULL, 'IdPelicula' INTEGER NOT NULL, 'Titulo' VARCHAR(255), 'Portada' VARCHAR(500), PRIMARY KEY(Nombre,IdPelicula))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<String> getListasFavoritos() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT Nombre FROM ListasFavoritos",null);
        ArrayList<String> nombres = new ArrayList<>();
        while (c.moveToNext()){
            nombres.add(c.getString(0));
        }
        c.close();
        db.close();
        return nombres;
    }

    public String[] getListasFavoritos(String idPelicula) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Nombre FROM ListasFavoritos EXCEPT SELECT Nombre FROM ListasFavoritos WHERE idPelicula = '" + idPelicula + "'",null);
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

    public void insertarPelicula(String lista, String idPelicula, String tituloPelicula, String portadaPelicula) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ListasFavoritos WHERE Nombre = '" + lista + "' AND IdPelicula = " + Integer.parseInt(idPelicula),null);
        if(c.getCount() == 0) {
            db.execSQL("INSERT INTO ListasFavoritos VALUES ('" + lista + "', " + Integer.parseInt(idPelicula) + ", '" + tituloPelicula + "', '" + portadaPelicula + "')");
        }
        c.close();
        db.close();
    }

    public String[] getIdsListaFav(String lista) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT IdPelicula FROM ListasFavoritos WHERE Nombre = '" + lista + "'",null);
        String[] portadas = new String[c.getCount()];
        int i = 0;
        while (c.moveToNext()){
            portadas[i] = c.getString(0);
            i++;
        }
        c.close();
        db.close();
        return portadas;
    }

    public String[] getPortadasListaFav(String lista) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Portada FROM ListasFavoritos WHERE Nombre = '" + lista + "'",null);
        String[] portadas = new String[c.getCount()];
        int i = 0;
        while (c.moveToNext()){
            portadas[i] = c.getString(0);
            i++;
        }
        c.close();
        db.close();
        return portadas;
    }

    public String[] getTitulosListaFav(String lista) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Titulo FROM ListasFavoritos WHERE Nombre = '" + lista + "'",null);
        String[] titulos = new String[c.getCount()];
        int i = 0;
        while (c.moveToNext()){
            titulos[i] = c.getString(0);
            i++;
        }
        c.close();
        db.close();
        return titulos;
    }

    public void eliminarPeliculaLista(String lista, String idPelicula) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM ListasFavoritos WHERE Nombre = '" + lista + "' AND IdPelicula = " + Integer.parseInt(idPelicula));
        db.close();
    }
}
