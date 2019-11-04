package com.android.sga.reportapp.Fragments;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.sga.reportapp.Adapters.RecyclerViewAdapterNotificationCache;
import com.android.sga.reportapp.Fragments.FormularyFragment;
import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.MainActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.SQLite.ConexionSQLiteHelper;
import com.android.sga.reportapp.SQLite.Datos.Datos;
import com.android.sga.reportapp.SQLite.Utilidades;
import com.android.sga.reportapp.SQLite.Utilidades2;
import com.android.sga.reportapp.gson.apiRespuesta;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.gson.datosReporte;
import com.android.sga.reportapp.utileries.TypefacesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.android.sga.reportapp.Fragments.FormularyFragment.DATA_MODO;
import static com.android.sga.reportapp.Fragments.FormularyFragment.STRING_MODO;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    Button useratras;
    private RecyclerView recyclerView;
    RecyclerViewAdapterNotificationCache ListaAdapterCache;
    String estado="" ,usuariologueado="", usuariofacebook="",usuariolomail="";
    int count=0, i = 0;
    ArrayList<datosReporte> ListaReporte ,dataset;
    private static final String TAG = "REPORTES";
    View Manager1;
    ConexionSQLiteHelper conn;
    ArrayList<Datos> listaInformacionCache= new ArrayList<>(), dataInformacionCache; //importante
    Typeface Light , Bold;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Manager1=inflater.inflate(R.layout.fragment_notification,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Light = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Bold.ttf");
        TextView etiqueta_no=(TextView)Manager1.findViewById(R.id.etiqueta_notificacion); etiqueta_no.setTypeface(Light);
        useratras = (Button)Manager1.findViewById(R.id.useratras);
        recyclerView=(RecyclerView)Manager1.findViewById(R.id.recyclerView);
        ListaAdapterCache=new RecyclerViewAdapterNotificationCache(getActivity(),getActivity());
        recyclerView.setAdapter(ListaAdapterCache);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),1);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(ListaAdapterCache);

        recuperarIdFacebook(); //Recuperar ID usuario de FACEBOOK
        recuperarIdUserMail(); //Recuperar ID usuario de CORREO
        if (usuariofacebook.length()==0){
            usuariologueado=usuariolomail; }
        else {
            usuariologueado=usuariofacebook; }

        //cuando ya cuentas con tu adaptador
        conn=new ConexionSQLiteHelper(getActivity(),"db4_reportApp", null,1);
        recuperaraAtivadorReporte();

        if (estado.equals("ACTIVADO")){
            totalDatosReportes();
            FormularyFragment.activadorReporte(getActivity(),null);
        }
        else {
            consultarListaDatosCache();
        }

        useratras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).opeFragmentMapTotal();
            }
        });
        return Manager1;
    }
    public void recuperarIdFacebook(){
        SharedPreferences preferences=getActivity().getSharedPreferences(LoginActivity.STRING_DATAFB, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAFB_IDFB, "");
        usuariofacebook=d;
    }
    //FUNCION DE RECUPERAR ID USUARIO-CORREO ALMACENADO EN CACHE
    public void recuperarIdUserMail(){
        SharedPreferences preferences=getActivity().getSharedPreferences(LoginActivity.STRING_DATAMAIL, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAMAIL_IDMAIL, "");
        usuariolomail=d;
    }

    public void recuperaraAtivadorReporte(){
        SharedPreferences preferences=getActivity().getSharedPreferences(STRING_MODO, MODE_PRIVATE);
        String d=preferences.getString(DATA_MODO, "");
        estado=d;
    }
    private void limpiarInformacionCache(){
        SQLiteDatabase db=conn.getReadableDatabase();
        db.delete(Utilidades.TABLA_DATOS,"",null);
    }
    public void consultarListaDatosCache(){
        SQLiteDatabase db=conn.getReadableDatabase();

        Datos datos=null;
        listaInformacionCache=new ArrayList<Datos>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ Utilidades2.TABLA_DATOS +" WHERE "+Utilidades2.CAMPO_STATUS+ "!='REPORTADO' ", null);



        while (cursor.moveToNext()){
            datos=new Datos();
            datos.setId(cursor.getInt(0));
            datos.setId_reporte(cursor.getString(1));
            datos.setUsuario(cursor.getString(2));
            datos.setDireccion(cursor.getString(3));
            datos.setLatitud(cursor.getString(4));
            datos.setLongitud(cursor.getString(5));
            datos.setTipo(cursor.getString(6));
            datos.setFecha(cursor.getString(7));
            datos.setFoto(cursor.getString(8));
            datos.setFolio(cursor.getString(9));
            datos.setEstatus(cursor.getString(10));
            datos.setColonia(cursor.getString(11));
            datos.setCategoria(cursor.getString(12));
            datos.setCruzamiento(cursor.getString(13));
            datos.setDescripcion(cursor.getString(14));
            datos.setHora(cursor.getString(15));
            datos.setLikes(cursor.getInt(16));
            datos.setLike_usuario(cursor.getString(17));
            //datos.setComentario(cursor.getInt(18));

            listaInformacionCache.add(datos);
        }
        //obntenerListaDatos();
        obtenerInformacionCache();
    }
    private void obtenerInformacionCache(){
        dataInformacionCache=listaInformacionCache;
        ListaAdapterCache.adicionarListaDatos(listaInformacionCache);
    }

    public void almanecenarCache(
            int id, String id_reporte,String usuario,String direccion, String latitud,String longitud,
            String tipo, String fecha,String foto, String folio,String estatus, String colonia, String categoria,String cruzamiento,String descripcion, String hora,int likess, String usuario_like, int comentarios ){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getActivity(),"db4_reportApp", null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(Utilidades2.CAMPO_ID, id);
        values.put(Utilidades2.CAMPO_ID_REPORTES, id_reporte);
        values.put(Utilidades2.CAMPO_USUARIO, usuario);
        values.put(Utilidades2.CAMPO_DIRECCION, direccion);
        values.put(Utilidades2.CAMPO_LATITUD, latitud);
        values.put(Utilidades2.CAMPO_LONGITUD, longitud);
        values.put(Utilidades2.CAMPO_TIPO, tipo);
        values.put(Utilidades2.CAMPO_FECHA, fecha);
        values.put(Utilidades2.CAMPO_FOTO, foto);
        values.put(Utilidades2.CAMPO_FOLIO, folio);
        values.put(Utilidades2.CAMPO_STATUS, estatus);
        values.put(Utilidades2.CAMPO_COLONIA, colonia);
        values.put(Utilidades2.CAMPO_CATEGORIA, categoria);
        values.put(Utilidades2.CAMPO_CRUZAMIENTO, cruzamiento);
        values.put(Utilidades2.CAMPO_DESCRIPCION, descripcion);
        values.put(Utilidades2.CAMPO_HORA, hora);
        values.put(Utilidades2.CAMPO_LIKES, likess);
        values.put(Utilidades2.CAMPO_USUARIO_LIKE,usuario_like);
      //  values.put(Utilidades2.CAMPO_COMENTARIO,comentarios);

        Long idResultante=db.insert(Utilidades2.TABLA_DATOS, Utilidades2.CAMPO_ID, values);
        db.close();
    }

    public void totalDatosReportes(){
        String usuario_logueado=usuariologueado;
        apiService service=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("usuario", usuario_logueado);
        map.put("user_consulta","YES");
        Call<apiRespuesta> apirespuestaCall=service.obtenerReportes(map);
        apirespuestaCall.enqueue(new Callback<apiRespuesta>() {
            @Override
            public void onResponse(Call<apiRespuesta> call, Response<apiRespuesta> response) {
                if(response.isSuccessful()) {
                    limpiarInformacionCache();
                    int id;
                    apiRespuesta apiRespuesta = response.body();
                    count=apiRespuesta.getTotalPag();
                    if (count>0) {
                        ListaReporte = apiRespuesta.getResults();
                        dataset = ListaReporte;
                        for (i = 0; i < ListaReporte.size(); i++) {
                            datosReporte p = ListaReporte.get(i);
                            id=p.getId();
                            almanecenarCache(id,p.getId_reporte(),p.getUsuario(),p.getDireccion(),p.getLatitud(),p.getLongitud(),p.getTipo(),p.getFecha(),p.getFoto(),p.getFolio(),p.getEstatus(),p.getColonia(),p.getCategoria(),p.getCruzamiento(),p.getDescripcion(),p.getHora(), p.getLikes(), p.getLike_usuario(), p.getComentarios());
                        }
                    }
                    consultarListaDatosCache();
                }
                else {
                    Log.e(TAG,"onResponse:"+response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<apiRespuesta> call, Throwable t) {
            }
        });
    }

}
