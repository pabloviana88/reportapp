package com.android.sga.reportapp.gson;

public class DatosGenerales {
    private int id;
    private String id_reporte;
    private String folio;
    private String tipo;
    private String descripcion;
    private String direccion;
    private String cruzamiento;
    private String colonia;
    private String latitud;
    private String longitud;
    private String fecha;
    private String estatus;
    private String hora;
    private String usuario;
    private String foto;
    private int number;

    public DatosGenerales(String id_reporte, String folio, String tipo, String descripcion, String direccion,
                          String cruzamiento, String colonia, String latitud,
                          String longitud, String fecha, String estatus, String hora, String usuario, String foto) {


        this.id_reporte = id_reporte;
        this.folio = folio;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.cruzamiento = cruzamiento;
        this.colonia = colonia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.estatus = estatus;
        this.hora = hora;
        this.usuario = usuario;
        this.foto = foto;
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

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCruzamiento() {
        return cruzamiento;
    }

    public void setCruzamiento(String cruzamiento) {
        this.cruzamiento = cruzamiento;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
