package com.android.sga.reportapp.SQLite;

import android.media.CamcorderProfile;

public class Utilidades {
    //CONSTANTES CAMPOS TABLA USUARIO
    public static final String TABLA_DATOS="datos";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_ID_REPORTES="id_reporte";
    public static final String CAMPO_USUARIO="usuario";
    public static final String CAMPO_DIRECCION="direccion";
    public static final String CAMPO_LATITUD="latitud";
    public static final String CAMPO_LONGITUD="longitud";
    public static final String CAMPO_TIPO="tipo";
    public static final String CAMPO_FECHA="fecha";
    public static final String CAMPO_FOTO="foto";
    public static final String CAMPO_FOLIO="folio";
    public static final String CAMPO_STATUS="estatus";
    public static final String CAMPO_COLONIA="colonia";
    public static final String CAMPO_CATEGORIA="categoria";
    public static final String CAMPO_CRUZAMIENTO="cruzamiento";
    public static final String CAMPO_DESCRIPCION="descripcion";
    public static final String CAMPO_HORA="hora";
    public static final String CAMPO_LIKES="likes";
    public static final String CAMPO_USUARIO_LIKE="usuario_like";
    public static final String CAMPO_COMENTARIO="comentario";
   // public static final String CAMPO_OBSERVACIONES="observaciones";


    public static final String CREAR_TABLA_DATOS="CREATE TABLE "+ TABLA_DATOS+"("
            +CAMPO_ID+" INTEGER, "+CAMPO_ID_REPORTES+" TEXT, "+CAMPO_USUARIO+" TEXT, "
            +CAMPO_DIRECCION+" TEXT, "+CAMPO_LATITUD+" TEXT, "+CAMPO_LONGITUD+" TEXT, "
            +CAMPO_TIPO+" TEXT, "+CAMPO_FECHA+" TEXT, "+CAMPO_FOTO+" TEXT, "
            +CAMPO_FOLIO+" TEXT, "+CAMPO_STATUS+" TEXT, "+CAMPO_COLONIA+" TEXT, "
            +CAMPO_CATEGORIA+" TEXT, "+CAMPO_CRUZAMIENTO+" TEXT, "+CAMPO_DESCRIPCION+" TEXT, "
            +CAMPO_HORA+" TEXT, "+CAMPO_LIKES+" INTEGER, "+CAMPO_USUARIO_LIKE+" TEXT, "+CAMPO_COMENTARIO+" INTERGER)";


}
