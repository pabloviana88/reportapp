package com.android.sga.reportapp.Additional;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sga.reportapp.Adapters.AdapterHistorial;
import com.android.sga.reportapp.Adapters.Adaptercomentar;
import com.android.sga.reportapp.Adapters.viewPegerAdapter;
import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.SQLite.ConexionSQLiteHelper;
import com.android.sga.reportapp.gson.Comentarios;
import com.android.sga.reportapp.gson.Historial;
import com.android.sga.reportapp.gson.Imagen;
import com.android.sga.reportapp.gson.Imagenrespuesta;
import com.android.sga.reportapp.gson.Likes;
import com.android.sga.reportapp.gson.Rescomentarios;
import com.android.sga.reportapp.gson.Reslikes;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class DetalleReporte extends AppCompatActivity implements OnMapReadyCallback   {
    TextView tipo,folio, direccion, descripcion, usuario, latitud, longitud,estatus, fecha, hora;
    private static final String TAG = "FOTOS";
    private viewPegerAdapter viewPegerAdapter;
    Integer i;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ArrayList<Imagen> misobjetos = new ArrayList<>();
    private ArrayList<Historial>misobjetos1=new ArrayList<>();
    private ImageView[] dots;
    private Integer[] imagenes;
    Context cx;
    private String tipoimage;
    private String id_reporte,_latitud,_longitud;
    private GoogleMap mMap;
    String direccion_completa;
    RelativeLayout relativeLayout;
    Button boton_cancel_oculto;
    Button btncomentario;
    Button btnhistorial;
    Button btnmegusta, btnregresar;
    TextView cont_comentario, _txtcont;
    ConexionSQLiteHelper conn;
    //me gusta
    Integer conlikes;
    String user_likes="";
    String contlikes;
    LinearLayout vistaprincipal;
    LinearLayout vistacomentario;
    LinearLayout vistaHistorial;
    LinearLayout divisionliner;
    public static boolean validadorlike=false;
    public Integer contcomentario;
    public  String idfbrecuperado="", idusermailrecuperado="";
    public RecyclerView recyclerView,recyclerView1;
    public Adaptercomentar adaptercomentar;
    public AdapterHistorial adapterHistorial;
    ///
    public Button btninsertcoment, btnatras2;

    public TextView txtinsertcoment,titulohistorial,txtdivision;
    Typeface Light , Bold;
    private MultiAutoCompleteTextView comentarios;

    ////////////////
    //Historial// Nuevo 23/09/2019
    public TextView hestatus,hfecha,hdescripcion,hobservaciones;
    public View vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detalle_reporte);
        Light = TypefacesUtils.get(this, "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(this, "fonts/MavenPro-Bold.ttf");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewcomentario);
        recyclerView1=(RecyclerView)findViewById(R.id.recyclerViewhistorial) ;

        adaptercomentar=new Adaptercomentar(this);
        recyclerView.setAdapter(adaptercomentar);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
       // recyclerView.setLayoutManager(layoutManager);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adaptercomentar);
        //es lo nuevo para la historial.

        adapterHistorial=new AdapterHistorial(this);
        recyclerView1.setAdapter(adapterHistorial);
        recyclerView1.setHasFixedSize(true);
        GridLayoutManager layoutManager1=new GridLayoutManager(this,1);

        //nuevo adapter HIstorial

        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration1=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration1.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView1.addItemDecoration(dividerItemDecoration1);
        recyclerView1.setAdapter(adapterHistorial);

        DividerItemDecoration dividerItemDecoration3=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration3.setDrawable(getResources().getDrawable(R.drawable.division));

        txtdivision=(TextView)findViewById(R.id.txtdivision);

     //  txtdivision=(TextView)findViewById(R.id.txtdivision);////////
      //  hestatus=(TextView)findViewById(R.id.txtHfecha);
     //   hestatus=(TextView)findViewById(R.id.txtHdescripcion);
     //   hestatus=(TextView)findViewById(R.id.txtHobservaciones);

        TextView tv_folio=(TextView)findViewById(R.id.tv_folio_detalle); tv_folio.setTypeface(Bold);
        folio=(TextView)findViewById(R.id.txtDFolio); folio.setTypeface(Light);
        TextView tv_estatus=(TextView)findViewById(R.id.tv_estatus_detalle); tv_estatus.setTypeface(Bold);
        estatus=(TextView)findViewById(R.id.txtDestatus); estatus.setTypeface(Light);

        TextView tv_fecha=(TextView)findViewById(R.id.tv_fecha_detalle); tv_fecha.setTypeface(Bold);
        fecha = (TextView) findViewById(R.id.txtDFecha); fecha.setTypeface(Light);
        TextView tv_tipo=(TextView)findViewById(R.id.tv_tipo_detalle); tv_tipo.setTypeface(Bold);
        tipo = (TextView) findViewById(R.id.txtDTipo); tipo.setTypeface(Light);

        TextView tv_direccion=(TextView)findViewById(R.id.tv_dreccion_detalle); tv_direccion.setTypeface(Bold);
        direccion = (TextView) findViewById(R.id.txtDcalle); direccion.setTypeface(Light);
        TextView tv_descripcion=(TextView)findViewById(R.id.tv_descripcion_detalle); tv_descripcion.setTypeface(Bold);
        descripcion = (TextView) findViewById(R.id.txtDdescripcion); descripcion.setTypeface(Light);

        boton_cancel_oculto=(Button)findViewById(R.id.btn_cancelar_oculto);
        btncomentario=(Button)findViewById(R.id.btncomentario); btncomentario.setTypeface(Bold);
       // btnhistorial=(Button)findViewById(R.id.btnhistorial); btnhistorial.setTypeface(Light);
        comentarios=(MultiAutoCompleteTextView) findViewById(R.id.multicomentario); comentarios.setTypeface(Light);
        comentarios.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        btnmegusta=(Button)findViewById(R.id.btn_megusta);
        cont_comentario=(TextView)findViewById(R.id.txtcoment);
        _txtcont =(TextView)findViewById(R.id.txtcont);
        vistaprincipal=(LinearLayout)findViewById(R.id.vistaDetallereporte);
        vistacomentario=(LinearLayout)findViewById(R.id.vistalistacomentarios);
        //vistaHistorial=(LinearLayout)findViewById(R.id.recyclerViewhistorial) ;
        btnregresar=(Button)findViewById(R.id.btn_regresar);
       // btnatras2=(Button)findViewById(R.id.btntatras2);
        titulohistorial=(TextView)findViewById(R.id.txtTituloHisotorial);
        Intent intent = getIntent();
        //esta variable sirve para identificar que id de repores de se deve cargar
        id_reporte = intent.getExtras().getString("id_reporte");
        String _folio=intent.getExtras().getString("folio");
        String _tipo = intent.getExtras().getString("tipo");
        String _categoria=intent.getExtras().getString("categoria");
        String _direccion = intent.getExtras().getString("direccion");
        String _cruzamiento=intent.getExtras().getString("cruzamiento");
        String _colonia=intent.getExtras().getString("colonia");
        String _descripcion = intent.getExtras().getString("descripcion");
        String _usuario = intent.getExtras().getString("usuario");
        _latitud = intent.getExtras().getString("latitud");
        _longitud = intent.getExtras().getString("longitud");
        String _fecha = intent.getExtras().getString("fecha");
        String _estatus=intent.getExtras().getString("estatus");
        tipoimage=intent.getExtras().getString("foto");
        String _hora = intent.getExtras().getString("hora");
        conlikes =intent.getExtras().getInt("likes");
        user_likes=intent.getExtras().getString("likes_usuario");

        btninsertcoment=(Button)findViewById(R.id.btncomentar) ;

        folio.setText(_folio);

        tipo.setText(_categoria);
        tipo.setAllCaps(true);
        descripcion.setText(_descripcion);
        if(_cruzamiento==null){
            direccion.setText(_direccion+", "+_colonia);
            direccion_completa=_direccion+", "+_colonia;
        }
        else {
            direccion.setText(_direccion+","+_cruzamiento+","+_colonia);
            direccion_completa=_direccion+" "+_cruzamiento+","+_colonia;
        }
        fecha.setText(_fecha);
        estatus.setText(_estatus);
        estatus.setAllCaps(false);
        contlikes=String.valueOf(conlikes);

        _txtcont.setText(contlikes);
        conn = new ConexionSQLiteHelper(this, "db5_reportApp", null, 1);
        consultadatos();
        obtenerDatos();
    //   obtenerHistorial(); //lo nuevo que agregue.
        initMaps();
        Traercomentarios();
        btn_actividad();

    }
    private void obtenerDatos(){
       apiService service=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("id",id_reporte);
        Call<Imagenrespuesta> imagenrespuestaCall=service.ObtenerListaimagen(map);
        imagenrespuestaCall.enqueue(new Callback<Imagenrespuesta>() {
            @Override
            public void onResponse(Call<Imagenrespuesta> call, Response<Imagenrespuesta> response) {
                if(response.isSuccessful()){
                    Imagenrespuesta imagenRespuesta=response.body();
                    ArrayList<Imagen> ListaImagen=imagenRespuesta.getResults();
                    misobjetos=ListaImagen;
                    if(misobjetos==null){
                        viewPager=(ViewPager)findViewById(R.id.viewPager);
                        relativeLayout=(RelativeLayout)findViewById(R.id.relative_layout);
                        relativeLayout.setVisibility(View.GONE);
                        boton_cancel_oculto.setVisibility(View.VISIBLE);
                    }else{
                        initviewPager(misobjetos);
                    }
                }
            }
            @Override
            public void onFailure(Call<Imagenrespuesta> call, Throwable t) {
                Log.e(TAG,"onFailure"+ t.getMessage());
            }
        });
    }


    private  void Traercomentarios(){
        apiService apIservices=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("id_reporte",id_reporte);
        Call<Rescomentarios>call=apIservices.Listacomentarios(map);
        call.enqueue(new Callback<Rescomentarios>() {
            @Override
            public void onResponse(Call<Rescomentarios> call, Response<Rescomentarios> response) {
                Rescomentarios rescomentarios=response.body();
                ArrayList<Comentarios>comentarios=rescomentarios.getResults();
                contcomentario=rescomentarios.getCantidad();
                if (contcomentario!=0 || contcomentario>0)
                {
                    adapterHistorial.adicionarHistorial(comentarios);
                            //.adicionarAdaptercomentar(comentarios);
                }
                String _contadorcoment=String.valueOf(contcomentario);
                cont_comentario.setText(_contadorcoment);
            }
            @Override
            public void onFailure(Call<Rescomentarios> call, Throwable t) {
                Log.e(TAG,"onFailure"+ t.getMessage());
            }
        });
    }
//Es para Traer el historial del reporte.
    //eliminar la lista que esta y descomentar el servicio para poder consumir la json que asiganada a la funcion.
    private  void obtenerHistorial(){
        misobjetos1.add(new Historial(1,"50","Reportado","16/10/2016","bache aleman","este estara en proceso"));
        misobjetos1.add(new Historial(2,"51","Reportado","16/10/2016","bache aleman","este estara en proceso"));
        misobjetos1.add(new Historial(3,"52","Reportado","16/10/2016","bache aleman","este estara en proceso"));
       // adapterHistorial.adicionarHistorial(misobjetos1);



       /* apiService apIservices=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("id_reporte",id_reporte);
        Call<Rescomentarios>call=apIservices.Listacomentarios(map);
        call.enqueue(new Callback<Rescomentarios>() {
            @Override
            public void onResponse(Call<Rescomentarios> call, Response<Rescomentarios> response) {
                Rescomentarios rescomentarios=response.body();
                ArrayList<Comentarios>comentarios=rescomentarios.getResults();
             *//*   contcomentario=rescomentarios.getCantidad();


                if (contcomentario!=0 || contcomentario>0)
                {
                    adaptercomentar.adicionarAdaptercomentar(comentarios);
                }
                String _contadorcoment=String.valueOf(contcomentario);
                cont_comentario.setText(_contadorcoment);*//*
            }
            @Override
            public void onFailure(Call<Rescomentarios> call, Throwable t) {
                Log.e(TAG,"onFailure"+ t.getMessage());
            }
        });
*/
    }




    public void insercomentario(String idusuarios){
        apiService service1=apiService.retrofi.create(apiService.class);
        String comentario=comentarios.getText().toString();
        RequestBody id_reporte_body=RequestBody.create(MultipartBody.FORM, id_reporte);
        RequestBody id_usuario_body=RequestBody.create(MultipartBody.FORM, idusuarios);
        RequestBody comentario_body=RequestBody.create(MultipartBody.FORM, comentario);
       final Call<ResponseBody> call=service1.insertarcomentario(
               id_reporte_body,
              id_usuario_body,
               comentario_body
       );
       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              // Toast.makeText(DetalleReporte.this, "Datos guardados",Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               Log.d("error",t.toString());

           }
       });
    }


    public void insermegusta(){
        apiService service1=apiService.retrofi.create(apiService.class);
        String idusuarios=TraIDusuarios();
        final String usuariox=idusuarios;
        RequestBody id_reporte_body=RequestBody.create(MultipartBody.FORM, id_reporte);
        RequestBody id_usuario_body=RequestBody.create(MultipartBody.FORM, idusuarios);

        final Call<Reslikes>call=service1.insertmegusta(
                id_reporte_body,
                id_usuario_body
        );

        call.enqueue(new Callback<Reslikes>() {
            @Override
            public void onResponse(Call<Reslikes> call, Response<Reslikes> response) {
                Reslikes reslikes=response.body();
                ArrayList<Likes>likes=reslikes.results;
                for (int i=0;i<likes.size();i++) {
                    Likes p = likes.get(i);
                    user_likes=p.getCheck();
                    String sesion=p.getSesion();
                    conlikes=p.getTotal();
                }
                Buscareporte(user_likes,conlikes);
                btnmegusta.setActivated(true);
                btnmegusta.setTextColor(Color.parseColor("#FFFFFF"));
                contlikes=String.valueOf(conlikes);// incremento de mas lo elemine  porque uso una variable que tengo declarado desde el inicio.
                _txtcont.setText(contlikes);
            }

            @Override
            public void onFailure(Call<Reslikes> call, Throwable t) {
                Log.d("error",t.toString());

            }
        });


    }

    public void recuperarIdFacebook(){
        SharedPreferences preferences=getSharedPreferences(LoginActivity.STRING_DATAFB, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAFB_IDFB, "");
        idfbrecuperado=d;
    }
    public void recuperarIdUserMail(){
        SharedPreferences preferences=getSharedPreferences(LoginActivity.STRING_DATAMAIL, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAMAIL_IDMAIL, "");
        idusermailrecuperado=d;
    }
    public  String  TraIDusuarios(){
        recuperarIdFacebook();
        String id_facebook=idfbrecuperado ;
        recuperarIdUserMail();
        String id_cuenta=idusermailrecuperado ;
        if((id_facebook.isEmpty())||(id_facebook=="")){
            if (id_cuenta!=null) {
                return id_cuenta;
            }
        }
        return id_facebook;
    }

    public void eliminarlike() {
        String iduser=TraIDusuarios();
        apiService service=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("id_reporte",id_reporte);
        map.put("id_usuario",iduser);
        Call<Reslikes>call=service.Eliminarlike(map);
        call.enqueue(new Callback<Reslikes>() {
            @Override
            public void onResponse(Call<Reslikes> call, Response<Reslikes> response) {
                Reslikes reslikes=response.body();
                ArrayList<Likes>likes=reslikes.results;
                for (int i=0;i<likes.size();i++) {
                    Likes p = likes.get(i);
                    user_likes=p.getCheck();
                    String sesion=p.getSesion();
                    conlikes=p.getTotal();
                }

                Buscareporte(user_likes,conlikes);
                btnmegusta.setActivated(false);
                btnmegusta.setTextColor(Color.parseColor("#811BDA"));
                // btnmegusta.setActivated(true);
                contlikes=String.valueOf(conlikes);// incremento de mas lo elemine  porque uso una variable que tengo declarado desde el inicio.
                _txtcont.setText(contlikes);
            }
            @Override
            public void onFailure(Call<Reslikes> call, Throwable t) {
                Log.d("error",t.toString());
            }
        });

    }


    private void initviewPager(ArrayList<Imagen> imagen1){
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        sliderDotspanel=(LinearLayout)findViewById(R.id.SliderDots);
        viewPegerAdapter viewPegerAdapter=new viewPegerAdapter(this,imagen1);
        viewPager.setAdapter(viewPegerAdapter);
        dotscount=viewPegerAdapter.getCount();//slider
        dots=new ImageView[dotscount];//slider0
        for (int i=0; i<dotscount;i++)
        {
            dots[i]=new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            sliderDotspanel.addView(dots[i],params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int i=0; i<dotscount;i++)
                {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void initMaps(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Double lat=Double.valueOf(_latitud).doubleValue();
        Double lon=Double.valueOf(_longitud).doubleValue();
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(direccion_completa)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_mapa)));
        // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        CameraPosition camera= new CameraPosition.Builder()
                .target(sydney)
                .zoom(16) //limite -->21(muy cerka la camara)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
    }

    public void cancelar(View view) {
        onBackPressed();
    }

    public void comentario(View view) {
        comentarios.setText(comentarios.getText().toString().trim());
        if (!comentarios.getText().toString().isEmpty()) {
            //insercomentario();

            ValidandoID();
            adaptercomentar.remover();
            Traercomentarios();
            view= getCurrentFocus();
            if (view!= null){
                InputMethodManager teclado= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                teclado.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
            //Toast.makeText(DetalleReporte.this, "Comentario Exitoso", Toast.LENGTH_SHORT).show();

        }
        else
        {
            if (view!= null){
                InputMethodManager teclado= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                teclado.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
            comentarios.setHint("Debes escribir un comentario");
            Toast.makeText(DetalleReporte.this, "Debes escribir un comentario", Toast.LENGTH_SHORT).show();
        }

        // comentarios.setText(" ");
        comentarios.setText("");
        comentarios.setHint("Debes escribir un comentario");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,1);
        layoutParams.setMargins(0, 0, 8, 0);
        comentarios.setLayoutParams(layoutParams);
    }

    public  void ValidandoID() {
        recuperarIdFacebook();
        String id_facebook=idfbrecuperado ;
        recuperarIdUserMail();
        String id_cuenta=idusermailrecuperado ;

        if((id_facebook.isEmpty())||(id_facebook=="")){
            if (id_cuenta!=null){
                insercomentario(id_cuenta);
            }
        }
        else{
            insercomentario(id_facebook);
        }
    }

    public void consultadatos() {
        try {
            SQLiteDatabase db=conn.getReadableDatabase(); //get the database that was created in this instance
            Cursor c = db.rawQuery("select * from datos where id_reporte=?", new String[]{id_reporte});
            if (c.moveToLast()) {
                conlikes=c.getInt(16);
                contlikes=String.valueOf(conlikes);// incremento de mas lo elemine  porque uso una variable que tengo declarado desde el inicio.
                _txtcont.setText(contlikes);
                user_likes=c.getString(17);
                // Toast.makeText(this, "Modified Successfully" + prueba + prueba1, Toast.LENGTH_SHORT).show();

            }
        }

        catch (Exception e) {
            Log.e(TAG, "errorrrrr !!");
            e.printStackTrace();

        }

    }

    public void Buscareporte(String cambio,Integer _likes) {
        try {
            SQLiteDatabase db=conn.getWritableDatabase();
            ContentValues cv = new ContentValues();
            String prueba="usuario_like";
            String prueba1="likes";
            cv.put(prueba,cambio);
            cv.put(prueba1,_likes);
            int c = db.update("datos",cv,  "id_reporte=?",new String[]{id_reporte});
            if (c>0) {
                //   Toast.makeText(getApplicationContext(), "Modified Successfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "errorrrrr !!");
            e.printStackTrace();
        }
    }

    public void megusta(View view){
        if (user_likes.equals("NO")) {
            insermegusta();
        }
        else {
            eliminarlike();
            //Toast.makeText(DetalleReporte.this, "Ya Tienes me gusta", Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_actividad() {
        if (user_likes.equals("YES")) {
            btnmegusta.setActivated(true);
            btnmegusta.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            btnmegusta.setEnabled(true);
        }

    }
    public void regresar(View view) {
        vistaprincipal.setVisibility(View.VISIBLE);
        vistacomentario.setVisibility(View.GONE);
       // vistaHistorial.setVisibility(View.GONE);
    }
/*public void regresa_2(View view)
{
    vistaprincipal.setVisibility(View.VISIBLE);
    vistacomentario.setVisibility(View.GONE);
    vistaHistorial.setVisibility(View.GONE);
}*/
    public void eventcomentario(View view){
        if (vistaprincipal.getVisibility() == View.VISIBLE)
        {
            vistaprincipal.setVisibility(View.GONE);
            vistacomentario.setVisibility(View.VISIBLE);
          //  vistaHistorial.setVisibility(View.GONE);
        }
    }
  /* public void eventoHistoria(View view)
   {
       if(vistaprincipal.getVisibility()==View.VISIBLE)
       {
           vistaprincipal.setVisibility(View.GONE);
           vistacomentario.setVisibility(View.GONE);
           vistaHistorial.setVisibility(View.VISIBLE);
       }
   }*/
}
