package com.android.sga.reportapp.gson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface apiService {
    //En la nueva version la url debe terminar con '/'
    Gson gson=new GsonBuilder().setLenient().create();
    public static final Retrofit retrofi= new Retrofit.Builder()
            .baseUrl("http://terceracto.com.mx/reportapp/services/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    @GET("datos.php")
    Call<apiRespuesta>obtenerListaReporte();

    @FormUrlEncoded
    @POST("datos.php")
    Call<apiMisReportes>obtenerMisReportes(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("datos.php")
    Call<apiRespuesta>obtenerReportes(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("eliminar_like.php/")
    Call<Reslikes>Eliminarlike(@FieldMap Map<String,Object>map);

    @FormUrlEncoded
    @POST("datos.php")
    Call<apiRespuesta>obtenerReportesTotales();

    @Multipart
    @POST("login_ios.php")
    Call<ResponseBody> insertaDatosFacebook(
            //@Part ("token") RequestBody token,
            @Part("id_usuario") RequestBody idfacebook,
            @Part("nombre") RequestBody nombre,
            @Part("apellido") RequestBody apellido,
            @Part("correo") RequestBody correo,
            @Part("cumpleanos") RequestBody fnacimiento,
            @Part("genero") RequestBody genero,
            @Part("foto") RequestBody url_image,
            @Part("tipo") RequestBody tipo
    );

    @FormUrlEncoded
    @POST("login_email.php")
    Call<apiUsuario>obtenerDatosUsuario(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("login_ios.php")
    Call<apiFacebook>obtenerDatosFacebookId(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("user_dates.php")
    Call<apiFacebook>obtenerDatoUsuario(@FieldMap Map<String, Object> map);

    @Multipart
    @POST("register_user.php")
    Call<ResponseBody> insertaDatosPersonales(
            @Part("nombre") RequestBody nombre,
            @Part("apellido") RequestBody apellido,
            @Part("correo") RequestBody correo,
            @Part("telefono") RequestBody telefono,
            @Part("cumpleanos") RequestBody fnacimiento,
            @Part("genero") RequestBody genero,
            @Part("password") RequestBody contrasena,
            @Part MultipartBody.Part photo_perfil,
            @Part("tipo") RequestBody tipo
    );


    @Multipart
    @POST("insert_reporte.php")
    Call<ResponseBody> insertarFormulario(
            @Part("categoria") RequestBody categoria,
            @Part("direccion") RequestBody direccion,
            @Part("descripcion") RequestBody descripcion,
            @Part("cruzamientos") RequestBody cruzamientos,
            @Part("colonia") RequestBody colonia,
            @Part("cp") RequestBody cp,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud,
            @Part("usuario") RequestBody usuario,
            @Part("tipo") RequestBody tipo,
            @Part MultipartBody.Part photo_uno,
            @Part MultipartBody.Part photo_dos,
            @Part MultipartBody.Part photo_tres,
            @Part MultipartBody.Part photo_cuatro
    );

    @GET("datos.php")
    Call<ResDatosgenerales> ListDatosgenerales();

    @FormUrlEncoded
    @POST("datos.php")
    Call<ResDatosgenerales> ListDatosgenerales2(@FieldMap Map<String,Object> map);

    @FormUrlEncoded
    @POST("historial.php")
    Call<DatosGenerales> ListHistorial(@FieldMap Map<String,Object> map);

    @FormUrlEncoded
    @POST("fotos.php/")
    Call<Imagenrespuesta>ObtenerListaimagen(@FieldMap Map<String,Object> map);

    @Multipart
    @POST("insert_comentario.php")
    Call<ResponseBody> insertarcomentario(
            @Part("id_reporte") RequestBody id_reporte,
            @Part("id_usuario") RequestBody id_usuario,
            @Part("comentario") RequestBody comentario
    );
    @Multipart
    @POST("update_usuario.php")
    Call<apiUpdateUser> updateDatosCorreo(
            @Part("id_usuario") RequestBody idfacebook,
            @Part("nombre") RequestBody nombre,
            @Part("apellido") RequestBody apellido,
            @Part("fnacimiento") RequestBody nacimiento,
            @Part("telefono") RequestBody telefono,
            @Part MultipartBody.Part photo_perfil
    );
    @Multipart
    @POST("update_usuario.php")
    Call<ResponseBody> updateDatosFacebook(
            @Part("id_usuario") RequestBody idfacebook,
            @Part("tipo_usuario") RequestBody tipousuario,
            @Part("telefono") RequestBody telefono
    );


    @FormUrlEncoded
    @POST("comentarios_datos.php/")
    Call<Rescomentarios>Listacomentarios(@FieldMap Map<String,Object> map);

    @Multipart
    @POST("insert_gusta.php")
    Call<Reslikes> insertmegusta(
            @Part("id_reporte") RequestBody id_reporte,
            @Part("id_usuario") RequestBody id_usuario
    );

    @Multipart
    @POST("datos_likes.php")
    Call<Reslikes> consultamegusta(
            @Part("id_reporte") RequestBody id_reporte,
            @Part("id_usuario") RequestBody id_usuario
    );

    @FormUrlEncoded
    @POST("tokenprueba.php/")
    Call<ResponseBody>sendmessage(@FieldMap Map<String,Object>map);

    /*




    @Multipart
    @POST("getusuario.php")
    Call<apiFacebook>obtenerInfoFB(
            @Part("idfacebook") RequestBody id_face
    );








*/
}
