package com.android.sga.reportapp.gson;

import java.util.ArrayList;

public class apiRespuesta {
    private int totalPag;
    private ArrayList<datosReporte> results;

    public int getTotalPag() {
        return totalPag;
    }

    public void setTotalPag(int totalPag) {
        this.totalPag = totalPag;
    }

    public ArrayList<datosReporte> getResults() {
        return results;
    }
    public void setResults(ArrayList<datosReporte> results) {
        this.results = results;
    }
}
