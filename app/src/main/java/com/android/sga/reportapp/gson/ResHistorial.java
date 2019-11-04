package com.android.sga.reportapp.gson;

import java.util.ArrayList;

public class ResHistorial {
    public ArrayList<Historial> results;
    public int total;

    public ArrayList<Historial> getResults() {
        return results;
    }

    public void setResults(ArrayList<Historial> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
