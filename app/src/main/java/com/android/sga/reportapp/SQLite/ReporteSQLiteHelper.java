package com.android.sga.reportapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class ReporteSQLiteHelper extends SQLiteOpenHelper {
    /*String sqlCreste="CREATE TABLE Reporte (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
            ",id_reporte TEXT,folio TEXT,tipo TEXT,descripcion TEXT,direccion TEXT,cruzamiento TEXT,colonia TEXT,latitud TEXT," +
            "longitud TEXT,fecha TEXT,estatus TEXT,hora TEXT,usuario TEXT,foto TEXT)";*/

    String sqlCache="CREATE TABLE Reportes (" +
            "id INTEGER," +
            "id_reporte TEXT," +
            "usuario TEXT," +
            "direccion TEXT," +
            "latitud TEXT," +
            "longitud TEXT," +
            "tipo TEXT," +
            "fecha TEXT," +
            "foto TEXT," +
            "folio TEXT," +
            "estatus TEXT," +
            "colonia TEXT," +
            "categoria TEXT," +
            "cruzamiento TEXT," +
            "descripcion TEXT," +
            "hora TEXT)";

    //No se mueva nada se deja tal como se crea.
    public ReporteSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL(sqlCreste);
        db.execSQL(sqlCache);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //se elimina la version anterio de la tabla
        db.execSQL("DROP TABLE IF EXISTS Reportes");

        //Se crea l nueva version de la tabla
        db.execSQL(sqlCache);

    }
}
