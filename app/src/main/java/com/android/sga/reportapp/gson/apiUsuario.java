package com.android.sga.reportapp.gson;

import java.util.ArrayList;

public class apiUsuario {
    public ArrayList<datoUsuario> getResults() {
        return results;
    }

    public void setResults(ArrayList<datoUsuario> results) {
        this.results = results;
    }

    private ArrayList<datoUsuario> results;
}
