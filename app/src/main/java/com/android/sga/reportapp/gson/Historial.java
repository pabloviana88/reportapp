package com.android.sga.reportapp.gson;

public class Historial {
    private int id;
    private String id_reporte;
    private String estado;
    private String fecha;
    private String descripcion;
    private String Observaciones;

    public Historial(int id, String id_reporte, String estado, String fecha, String descripcion, String observaciones) {
        this.id = id;
        this.id_reporte = id_reporte;
        this.estado = estado;
        this.fecha = fecha;
        this.descripcion = descripcion;
        Observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_reporte() {
        return id_reporte;
    }

    public void setId_reporte(String id_reporte) {
        this.id_reporte = id_reporte;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }
}
