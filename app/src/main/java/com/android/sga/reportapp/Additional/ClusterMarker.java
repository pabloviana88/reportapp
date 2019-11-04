package com.android.sga.reportapp.Additional;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

public class ClusterMarker implements ClusterItem {
    private LatLng posicion;
    private String titulo;
    private String snippet;
    private int icon;

    public ClusterMarker(LatLng posicion, String titulo, String snippet, int icon) {
        this.posicion = posicion;
        this.titulo = titulo;
        this.snippet = snippet;
        this.icon = icon;
    }
    public ClusterMarker() {

    }
    @Override
    public LatLng getPosition() {
        return posicion;
    }

    public void setPosicion(LatLng posicion) {
        this.posicion = posicion;
    }
    @Override
    public String getTitle() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
