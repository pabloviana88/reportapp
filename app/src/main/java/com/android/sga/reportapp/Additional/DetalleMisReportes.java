package com.android.sga.reportapp.Additional;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.sga.reportapp.Adapters.viewPegerAdapter;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.gson.Imagen;
import com.android.sga.reportapp.gson.Imagenrespuesta;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalleMisReportes extends AppCompatActivity implements OnMapReadyCallback {
    TextView tipo,folio, direccion, descripcion,mensaje, usuario, latitud, longitud,estatus, fecha, hora;
    private static final String TAG = "FOTOS";
    private com.android.sga.reportapp.Adapters.viewPegerAdapter viewPegerAdapter;
    Integer i;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ArrayList<Imagen> misobjetos = new ArrayList<>();
    private ArrayList<String> misobjetos1 = new ArrayList<>();
    private String foto;
    private ImageView[] dots;
    private Integer[] imagenes;
    Context cx;
    private String id_reporte,_latitud,_longitud;
    private GoogleMap mMap;
    String direccion_completa;
    RelativeLayout relativeLayout;
    Button boton_cancel_oculto;
    Typeface Light,Bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detalle_mis_reportes);
        Light = TypefacesUtils.get(this, "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(this, "fonts/MavenPro-Bold.ttf");
        mensaje=(TextView)findViewById(R.id.txtMessage); mensaje.setTypeface(Light);
        TextView tv_folio=(TextView)findViewById(R.id.tv_folio_detellate_not); tv_folio.setTypeface(Bold);
        folio=(TextView)findViewById(R.id.txtDFolio); folio.setTypeface(Light);
        TextView tv_estatus=(TextView)findViewById(R.id.tv_estatus_detalle_not); tv_estatus.setTypeface(Bold);
        estatus=(TextView)findViewById(R.id.txtDestatus); estatus.setTypeface(Light);
        TextView tv_tipo=(TextView)findViewById(R.id.tv_tipo_detalle_not); tv_tipo.setTypeface(Bold);
        tipo = (TextView) findViewById(R.id.txtDTipo); tipo.setTypeface(Light);
        TextView tv_fecha=(TextView)findViewById(R.id.tv_fecha_detalle_not); tv_fecha.setTypeface(Bold);
        fecha = (TextView) findViewById(R.id.txtDFecha); fecha.setTypeface(Light);
        TextView tv_direccion=(TextView)findViewById(R.id.tv_direccion_detalle_not); tv_direccion.setTypeface(Bold);
        direccion = (TextView) findViewById(R.id.txtDcalle); direccion.setTypeface(Light);
        TextView tv_descripcion=(TextView)findViewById(R.id.tv_descripcion_detalle_not); tv_descripcion.setTypeface(Bold);
        descripcion = (TextView) findViewById(R.id.txtDdescripcion); descripcion.setTypeface(Light);
        boton_cancel_oculto=(Button)findViewById(R.id.btn_cancelar_oculto);

        Intent intent = getIntent();
        //esta variable sirve para identificar que id de repores de se deve cargar
        id_reporte = intent.getExtras().getString("id_reporte");
        String _folio=intent.getExtras().getString("folio");
        String _categoria=intent.getExtras().getString("categoria");
        String _tipo = intent.getExtras().getString("tipo");
        String _direccion = intent.getExtras().getString("direccion");
        String _cruzamiento=intent.getExtras().getString("cruzamiento");
        String _colonia=intent.getExtras().getString("colonia");
        String _descripcion = intent.getExtras().getString("descripcion");
        String _usuario = intent.getExtras().getString("usuario");
        _latitud = intent.getExtras().getString("latitud");
        _longitud = intent.getExtras().getString("longitud");
        String _fecha = intent.getExtras().getString("fecha");
        String _estatus=intent.getExtras().getString("estatus");

        String _hora = intent.getExtras().getString("hora");
        foto=intent.getExtras().getString("foto");
        //covertir folio a String
        folio.setText(_folio);
        tipo.setText(_categoria);
        descripcion.setText(_descripcion);
        if(_cruzamiento==null){
            direccion.setText(_direccion+", "+_colonia);
            direccion_completa=_direccion+", "+_colonia;
        }else {
            direccion.setText(_direccion+","+_cruzamiento+","+_colonia);
            direccion_completa=_direccion+" "+_cruzamiento+","+_colonia;
        }

        if(_estatus.equals("REPORTADO"))
            // mensaje.setText("Tu reporte ha sido "+_estatus+" y en breve");
            mensaje.setText("Tu reporte ha sido enviado correctamente");
        else if (_estatus.equals("RECIBIDO"))
            mensaje.setText("Tu reporte ha sido recibido y se encuentra en verificación");
        else if(_estatus.equals("PROCESO"))
            mensaje.setText("Tu reporte se encuentra en proceso de atención");
        else if(_estatus.equals("ATENDIDO"))
            mensaje.setText("Tu reporte ha sido atendido");
        else if(_estatus.equals("CANCELADO"))
            mensaje.setText("Tu reporte fue cancelado");
        fecha.setText(_fecha);
        estatus.setText(_estatus);

          obtenerDatos();
           initMaps();
    }
    private void obtenerDatos(){
        apiService service=apiService.retrofi.create(apiService.class);
        Map<String,Object> map= new HashMap<>();
        map.put("id",id_reporte);
        Call<Imagenrespuesta> imagenrespuestaCall=service.ObtenerListaimagen(map);
        imagenrespuestaCall.enqueue(new Callback<Imagenrespuesta>() {
            @Override
            public void onResponse(Call<Imagenrespuesta> call, Response<Imagenrespuesta> response) {
                if(response.isSuccessful()){
                    Imagenrespuesta imagenRespuesta=response.body();
                    ArrayList<Imagen>ListaImagen=imagenRespuesta.getResults();
                    misobjetos=ListaImagen;
                    if(misobjetos==null){
                        viewPager=(ViewPager)findViewById(R.id.viewPager);
                        relativeLayout=(RelativeLayout)findViewById(R.id.relative_layout);
                        relativeLayout.setVisibility(View.GONE);
                        boton_cancel_oculto.setVisibility(View.VISIBLE);
                    }else{
                        initviewPager(misobjetos);}
                }
            }
            @Override
            public void onFailure(Call<Imagenrespuesta> call, Throwable t) {
                Log.e(TAG,"onFailure"+ t.getMessage());
            }
        });

    }
    private void initviewPager(ArrayList<Imagen>imagen1) {
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        sliderDotspanel=(LinearLayout)findViewById(R.id.SliderDots);
        //slider
        com.android.sga.reportapp.Adapters.viewPegerAdapter viewPegerAdapter=new viewPegerAdapter(this,imagen1);
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
        CameraPosition camera= new CameraPosition.Builder()
                .target(sydney)
                .zoom(16) //limite -->21(muy cerka la camara)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
    }
    public void cancelar(View view) {
        onBackPressed();
    }
}
