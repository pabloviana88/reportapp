package com.android.sga.reportapp.gson;

import java.util.ArrayList;

public class Reslikes {
    public ArrayList<Likes> results;
    public int total;
    public boolean check;

    public ArrayList<Likes> getResults() {
        return results;
    }

    public void setResults(ArrayList<Likes> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
