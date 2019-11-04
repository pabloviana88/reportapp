package com.android.sga.reportapp.gson;

import java.util.ArrayList;

public class apiMisReportes {
    private int totalPag;

    public int getTotalPag() {
        return totalPag;
    }

    public void setTotalPag(int totalPag) {
        this.totalPag = totalPag;
    }

    private ArrayList<datosMisReportes> results;

    public ArrayList<datosMisReportes> getResults() {
        return results;
    }
    public void setResults(ArrayList<datosMisReportes> results) {
        this.results = results;
    }
}
