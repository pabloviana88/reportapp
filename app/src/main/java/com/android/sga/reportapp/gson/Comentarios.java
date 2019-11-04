package com.android.sga.reportapp.gson;

public class Comentarios {

   public String id_comentario;
   public String comentarios;
   private String usuario;
   public String img_usuario;
   public String tiempo;

    public String getId_comentario() {
        return id_comentario;
    }

    public void setId_comentario(String id_comentario) {
        this.id_comentario = id_comentario;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getImg_usuario() {
        return img_usuario;
    }

    public void setImg_usuario(String img_usuario) {
        this.img_usuario = img_usuario;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
}
