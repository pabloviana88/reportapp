package com.android.sga.reportapp.gson;

import java.util.ArrayList;

public class Rescomentarios {
    public ArrayList<Comentarios> results;
    public Integer cantidad;


    public ArrayList<Comentarios> getResults() {
        return results;
    }

    public void setResults(ArrayList<Comentarios> results) {
        this.results = results;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
