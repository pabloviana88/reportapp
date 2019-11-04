package com.android.sga.reportapp.SQLite.Datos;

public class Datos {
    private int id;
    private String id_reporte;
    private String usuario;
    private String direccion;
    private String latitud;
    private String longitud;
    private String tipo;
    private String fecha;
    private String foto;
    private String folio;
    private String estatus;
    private String colonia;
    private String categoria;
    private String cruzamiento;
    private String descripcion;
    private String hora;
    private int likes;
    private String like_usuario;
    private int comentario;
   // private  String observaciones;

   public Datos(){
   }
    public Datos(int id, String id_reporte, String usuario, String direccion, String latitud, String longitud, String tipo, String fecha, String foto, String folio, String estatus, String colonia, String categoria, String cruzamiento, String descripcion, String hora, int likes, String like_usuario, int comentario) {
        this.id = id;
        this.id_reporte = id_reporte;
        this.usuario = usuario;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipo = tipo;
        this.fecha = fecha;
        this.foto = foto;
        this.folio = folio;
        this.estatus = estatus;
        this.colonia = colonia;
        this.categoria = categoria;
        this.cruzamiento = cruzamiento;
        this.descripcion = descripcion;
        this.hora = hora;
        this.likes=likes;
        this.like_usuario=like_usuario;
        this.comentario=comentario;
       // this.observaciones=observaciones;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCruzamiento() {
        return cruzamiento;
    }

    public void setCruzamiento(String cruzamiento) {
        this.cruzamiento = cruzamiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getLike_usuario() {
        return like_usuario;
    }

    public void setLike_usuario(String like_usuario) {
        this.like_usuario = like_usuario;
    }

    public int getComentario() {
        return comentario;
    }

    public void setComentario(int comentario) {
        this.comentario = comentario;
    }

    //public String getObservaciones() { return observaciones; }

  //  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
