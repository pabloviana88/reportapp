package com.android.sga.reportapp.Fragments;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.android.sga.reportapp.Adapters.RecyclerViewAdapterDatosCache;
import com.android.sga.reportapp.Additional.ClusterMarker;
import com.android.sga.reportapp.Additional.MyClusterManagerRender;
import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.MainActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.SQLite.Conexion2SQLiteHelper;
import com.android.sga.reportapp.SQLite.ConexionSQLiteHelper;
import com.android.sga.reportapp.SQLite.Datos.Datos;
import com.android.sga.reportapp.SQLite.Utilidades;
import com.android.sga.reportapp.SQLite.Utilidades2;
import com.android.sga.reportapp.gson.Comentarios;
import com.android.sga.reportapp.gson.Rescomentarios;
import com.android.sga.reportapp.gson.apiRespuesta;
import com.android.sga.reportapp.gson.datosReporte;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    View rootView;
    private GoogleMap mMap;
    private static int PETICION_PERMISO_LOCALIZACION = 101;
    double lat = 0.0 , lng = 0.0, latitudoObtnerPosicion, longitudObtnerPosicion;
    CameraPosition camera;
    Button miubicacion , mimenu,reportarProblema;
    String mensaje1, usuariologueado="", usuariofacebook="", usuariolomail;
    String latitudBusqueda="", longitudBusqueda="";
    ArrayList<datosReporte> ListaReporte ,dataset;
    int i = 0,count=0 ;
    private static final String TAG = "REPORTES";
    RecyclerViewAdapterDatosCache ListaAdapterCache;
    String contartReportes;
    private Marker markerMap;
    Datos e;
    RecyclerView recyclerView;
    ArrayList<Datos> listaInformacionCache= new ArrayList<>(), dataInformacionCache; //importante
    ConexionSQLiteHelper conn;
    Conexion2SQLiteHelper conn2;
    ConnectivityManager cm;
    NetworkInfo ni;
    boolean tipoConexion1=false, tipoConexion2=false;
    Typeface Light , Bold;
    public Integer contcomentario;
    private ClusterManager<ClusterMarker> fClusterManager;
    private MyClusterManagerRender myClusterManagerRender;
    private ArrayList<ClusterMarker> mclusterMarkers = new ArrayList<>();
    boolean isScrolling=false;
    int item_actual, total_items, scroll_out_item;

    private int page_number=0; //numero de pagina
    private int item_count=1; // recuento de elementos
    //variables de paging
    private boolean isLoading= true; //esta cargando
    private int pastVisibleItem, visibleItemCount,totalItemCount, previous_total=0; //elementos visibles pasados, recuento de elementos visibles, recuento total de elementos, total anterior
    private int view_threshold =1 ; //ver umbral


    public MapFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView= inflater.inflate(R.layout.fragment_map,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Light = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Bold.ttf");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        miubicacion = (Button)rootView.findViewById(R.id.gps);
        mimenu = (Button)rootView.findViewById(R.id.menu);
        reportarProblema = (Button)rootView.findViewById(R.id.reportarproblema);

        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
       recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewDatos);
        recyclerView.setLayoutManager(layoutManager1);
        ListaAdapterCache = new RecyclerViewAdapterDatosCache(getActivity(), getActivity());
        recyclerView.setAdapter(ListaAdapterCache);
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
        //recyclerView.scrollToPosition(1);
        recyclerView.setHasFixedSize(true);
       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount=layoutManager1.getChildCount();
                totalItemCount=layoutManager1.getItemCount();
                pastVisibleItem=layoutManager1.findFirstVisibleItemPosition();

             //   recyclerView.scrollToPosition(visibleItemCount);

               *//* if(dy>=0){
                    if (isLoading){
                     //   if(totalItemCount>previous_total){
                            isLoading=false;
                         //   previous_total=totalItemCount;
                       // }
                    }
                    if(!isLoading && (totalItemCount-visibleItemCount)>=(pastVisibleItem+view_threshold)){
                        page_number++;
                        //realizar_paginacion();
                        Toast.makeText(getActivity(),"pagina" + page_number, Toast.LENGTH_LONG).show();
                        isLoading=true;

                    }
                }*//*

                if(isLoading && (visibleItemCount + pastVisibleItem <totalItemCount)){
                    isLoading=false;
                    page_number++;
                    realizar_paginacion();

                }


            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading=true;
                }
            }
        });*/

        cm=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        ni=cm.getActiveNetworkInfo();
        //cuando ya cuentas con tu adaptador
        conn=new ConexionSQLiteHelper(getActivity(),"db5_reportApp", null,1);
        conn2=new Conexion2SQLiteHelper(getActivity(),"db4_reportApp", null,1);
        //EVENTS
        miubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { miUbicacion(); }
        });
        mimenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { /*Desplegar menu*/ ((MainActivity)getActivity()).openDrawer(); }
        });
        reportarProblema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { /*Desplegar formulary*/ ((MainActivity)getActivity()).opeFragmentFormulary(); }
        });

        recuperarIdFacebook(); //Recuperar ID usuario de FACEBOOK
        recuperarIdUserMail(); //Recuperar ID usuario de CORREO

        if (usuariofacebook.length()==0){
            //ASignamos el valor ID a la varible USUARIO LOGUEADO
            usuariologueado=usuariolomail; }
        else {
            usuariologueado=usuariofacebook; }

        MainActivity.recuperarcontador2(getApplicationContext());

        return rootView;
    }

    private void realizar_paginacion() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),"pagina" + page_number, Toast.LENGTH_LONG).show();
               // ListaAdapterCache.adicionarListaDatos(Math.floor(Math.random())*100);
                //ListaAdapterCache.adicionarListaDatos(dataInformacionCache);
              //  ListaAdapterCache.notifyDataSetChanged();


            }
        },500);

    }

    private void recorrerDatos(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<1;i++ ){
                    //dataInformacionCache.add(Math.floor(Math.random() * 100 )+ "");
                    dataInformacionCache.add(e);
                    ListaAdapterCache.notifyDataSetChanged();

                }

            }
        },500);
    }
    /*
    * PINTAR MAPA Y SUS FUNCIONES
    * MAPAS
    * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true); //HABILITANDO EL RADAR
        mMap.getUiSettings().setMyLocationButtonEnabled(false); //DESHABILITANDO
        mMap.getUiSettings().setCompassEnabled(false); //DESHABILITANDO BRUJULA
        miUbicacion(); //FUNCION DE UBICAR MI POSICION

        if(ni!=null){
            ConnectivityManager connectivityManager1=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi=connectivityManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            ConnectivityManager connectivityManager2=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobile=connectivityManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mWifi.isConnected()){
                tipoConexion1=true;
            }
            if (mMobile.isConnected()){
                tipoConexion2=true;
            }
            if (tipoConexion1==true || tipoConexion1==true){
                totalDatosReportes();
                totalDatosReportesMis();
            }
            else {
                Toast.makeText(getActivity(),"Sin datos", Toast.LENGTH_LONG).show();
                consultarMarcadoresCache();
                consultarListaDatosCache();
                MainActivity.recuperarcontador2(getApplicationContext());
            }
        }
        else {
            Toast.makeText(getActivity(),"Sin conexión", Toast.LENGTH_LONG).show();
            consultarMarcadoresCache();
            consultarListaDatosCache();
            MainActivity.recuperarcontador2(getApplicationContext());
        }
    }

    //FUNCION PARA UBICAR MI POSICION ACTUAL
    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PETICION_PERMISO_LOCALIZACION);
            return;
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);//llamo la funcion para actualizar mi ubicacion
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,0,locListener);
        }
    }
    //FUNCION DE ACTUALIZAR MI POSICION
    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            AgregarMarcador(lat, lng);
        }
    }
    //POSICIONAR LA CAMARA DEL MAPS EN MI POSICION
    private void CameraInit(){
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    }
    //FUNCION PARA AGREGAR MI POSICION POR MARCADOR EN EL MAPS
    private void AgregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        camera= new CameraPosition.Builder()
                .target(coordenadas)
                .zoom(12) //limite -->21(muy cerka la camara)
                .bearing(0)//rotaciones al este -tenienoo en cuenta 0-365º
                .tilt(50)// limite 90
                .build();
        CameraInit();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                   ListaAdapterCache.setFilterCache(dataInformacionCache); //DEVUELVE EL TOTAL DE DATOS
            }
        });


       mMap.setOnMarkerClickListener(new MarkerManager(mMap){
            @Override
            public boolean onMarkerClick(final Marker marker) {
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                                 @Override
                                                 public void onCameraIdle() {
                                                     obtnerPosicion();
                                                     filtroDatos(latitudoObtnerPosicion,longitudObtnerPosicion);
                                                     mMap.setOnCameraIdleListener(fClusterManager);
                                                     fClusterManager.cluster();

                                                 }
                                             }
                );
                return super.onMarkerClick(marker);
            }

        });
    }
    //CONTROL PARA GPS
    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
           // setLocation(location);
           // ActualizarUbicacion(location);
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {  }
        @Override
        public void onProviderEnabled(String s) {
            mensaje1 = ("Tu GPS esta activado");
            Mensaje();
        }
        @Override
        public void onProviderDisabled(String s) {
            mensaje1 = ("Tu GPS esta desactivado");
            locationStart();
            Mensaje();
        }
    };
    //señala el estado del GPS (Activaod o desactivado)
    public void Mensaje() {
        Toast toast = Toast.makeText(getActivity(), mensaje1, Toast.LENGTH_LONG);
        toast.show();
    }
    //activar los servicios del gps en caso de que esyte apagado
    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
    }
    //OBTENER POSICION PARA FILTRO
    public  void obtnerPosicion(){
        CameraPosition camPos=mMap.getCameraPosition();
        LatLng coordenadas=camPos.target;
        latitudoObtnerPosicion= coordenadas.latitude;
        longitudObtnerPosicion= coordenadas.longitude;
        //   Toast.makeText(getActivity(),"Lat: "+ latitudoObtnerPosicion + "Long: "+ longitudObtnerPosicion, Toast.LENGTH_SHORT).show();
    }
    //FUNCION PARA FILTRO : CON PARAMETROS
    public void filtroDatos(double latitudoParam, double longitudParam){
        String latFolio=String.valueOf(latitudoParam);
        String lonFolio=String.valueOf(longitudParam);
        latitudBusqueda=latFolio;
        longitudBusqueda=lonFolio;
        ArrayList<Datos>ListaReporte=filtro(dataInformacionCache,latFolio, lonFolio);
        if(ListaReporte.size()>0){
            ListaAdapterCache.setFilterForMarker(ListaReporte);
        }
        else {
            ListaAdapterCache.setFilterCache(dataInformacionCache);
        }
    }
    private ArrayList<Datos>filtro(ArrayList<Datos>datosreporte, String latFolio, String longFolio){
        ArrayList<Datos>listaFiltrada=new ArrayList<>();
        try {
            latFolio=latitudBusqueda;
            longFolio=longitudBusqueda;
            for(Datos datoreporte:datosreporte){
                String datolatitud=datoreporte.getLatitud();
                String datolongitud=datoreporte.getLongitud();
                if (datolatitud.contains(latFolio)&&datolongitud.contains(longFolio)){
                    listaFiltrada.add(datoreporte);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaFiltrada;
    }

    /*
    *
    * *******ALMACENAMIENTOS CACHES ****
    * SHARED PREFERENCES --- DATOS MINIMOS
    * SQLITE ----- DATOS  DE CANTIDADES MAYORES
    *
    * */

    //FUNCION DE RECUPERAR ID FB ALMACENADO EN CACHE
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
    private void obtenerInformacionCache(){
        dataInformacionCache=listaInformacionCache;
        ListaAdapterCache.adicionarListaDatos(listaInformacionCache);
    }

   /*
   * SERVIOS WEB - RETROFIT
   * */
   //CONSUMO DE DATOS-RETROFIT
   private  void Traercomentarios(String id_report){
       apiService apIservices=apiService.retrofi.create(apiService.class);
       Map<String,Object> map=new HashMap<>();

       //original ID **  id_reporte
       map.put("id_reporte",id_report); //hay que obtener esta parte importante
       Call<Rescomentarios>call=apIservices.Listacomentarios(map);
       call.enqueue(new Callback<Rescomentarios>() {
           @Override
           public void onResponse(Call<Rescomentarios> call, Response<Rescomentarios> response) {
               Rescomentarios rescomentarios=response.body();
               ArrayList<Comentarios>comentarios=rescomentarios.getResults();
               contcomentario=rescomentarios.getCantidad();
              /* if (contcomentario!=0 || contcomentario>0)
               {
                   adaptercomentar.adicionarAdaptercomentar(comentarios);
               }*/
               String _contadorcoment=String.valueOf(contcomentario);
               //Aqui almacenamos cache
             //  cont_comentario.setText(_contadorcoment);
           }
           @Override
           public void onFailure(Call<Rescomentarios> call, Throwable t) {
               Log.e(TAG,"onFailure"+ t.getMessage());
           }
       });
   }
    public void totalDatosReportes(){
        fClusterManager= new ClusterManager<ClusterMarker>(getApplicationContext(),mMap);
        myClusterManagerRender= new MyClusterManagerRender(getApplicationContext(),mMap,fClusterManager);
        fClusterManager.setRenderer(myClusterManagerRender);

        String usuario_logueado=usuariologueado;
        apiService service=apiService.retrofi.create(apiService.class);
        final Map<String,Object> map=new HashMap<>();
        map.put("usuario", usuario_logueado);
        Call<apiRespuesta> apirespuestaCall=service.obtenerReportes(map);
        apirespuestaCall.enqueue(new Callback<apiRespuesta>() {
            @Override
            public void onResponse(Call<apiRespuesta> call, Response<apiRespuesta> response) {
                if(response.isSuccessful()) {
                    limpiarInformacionCache();
                    double lat_x = 0.0, lon_x = 0.0;
                    int id;
                    String lat_1 = "", lon_1 = "", dir = "", col = "", cat = "", like="", folio="", estatus="";
                    apiRespuesta apiRespuesta = response.body();
                    count=apiRespuesta.getTotalPag();
                    if (count>0) {
                        ListaReporte = apiRespuesta.getResults();
                        dataset = ListaReporte;
                        //  ListaAdapter.adicionarListaDatos(ListaReporte);   items  comentado
                        for (i = 0; i < ListaReporte.size(); i++) {

                            datosReporte p = ListaReporte.get(i);
                            id=p.getId();
                            lat_1 = p.getLatitud();
                            lat_x = Double.valueOf(lat_1).doubleValue();
                            lon_1 = p.getLongitud();
                            lon_x = Double.valueOf(lon_1).doubleValue();
                            dir = p.getDireccion();
                            col = p.getColonia();
                            cat = p.getCategoria();
                            estatus=p.getEstatus();
                          if (estatus.equals("REPORTADO") || estatus.equals("RECIBIDO") || estatus.equals("PROCESO") || estatus.equals("ATENDIDO")){

                              almanecenarCache(id,p.getId_reporte(),p.getUsuario(),p.getDireccion(),p.getLatitud(),p.getLongitud(),p.getTipo(),p.getFecha(),p.getFoto(),p.getFolio(),p.getEstatus(),p.getColonia(),p.getCategoria(),p.getCruzamiento(),p.getDescripcion(),p.getHora(), p.getLikes(), p.getLike_usuario(),p.getComentarios());
                              LatLng coordenadas = new LatLng(lat_x, lon_x);



                           if (cat.equals("ARBOL CAIDO")) {
                               int icono = R.drawable.ic_pin_otro_o;
                               ClusterMarker arbol_caido= new ClusterMarker(coordenadas,col,dir,icono);
                               fClusterManager.addItem(arbol_caido);
                               mclusterMarkers.add(arbol_caido);

                            } else if (cat.equals("BASURA")) {
                               int icono = R.drawable.ic_pin_mapa_basura;
                               ClusterMarker basura= new ClusterMarker(coordenadas,col,dir,icono);
                               fClusterManager.addItem(basura);
                               mclusterMarkers.add(basura);

                            } else if (cat.equals("ALCANTARILLA")) {
                               int icono = R.drawable.ic_pin_otro_o;
                               ClusterMarker alcantarilla= new ClusterMarker(coordenadas,col,dir,icono);
                               fClusterManager.addItem(alcantarilla);
                               mclusterMarkers.add(alcantarilla);

                            } else if (cat.equals("BACHES")) {
                               int icono = R.drawable.ic_pin_mapa_bache;
                               ClusterMarker bache= new ClusterMarker(coordenadas,col,dir,icono);
                               fClusterManager.addItem(bache);
                               mclusterMarkers.add(bache);

                            } else if (cat.equals("ALUMBRADO")) {
                               int icono = R.drawable.ic_pin_mapa_alumbrado;
                               ClusterMarker alumbrado= new ClusterMarker(coordenadas,col,dir,icono);
                               fClusterManager.addItem(alumbrado);
                               mclusterMarkers.add(alumbrado);

                            } else if (cat.equals("FUGA DE AGUA")) {
                               int icono = R.drawable.ic_pin_otro_o;
                               ClusterMarker agua= new ClusterMarker(coordenadas,col,dir,icono);
                               fClusterManager.addItem(agua);
                               mclusterMarkers.add(agua);

                            }
                            mMap.setOnCameraIdleListener(fClusterManager);
                            fClusterManager.cluster();

                          }
                        }
                    }
                    else {
                        Toast.makeText(getActivity(),"No hay reportes",Toast.LENGTH_LONG).show();
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

    public void totalDatosReportesMis(){
        String usuario_logueado=usuariologueado;
        apiService service=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("usuario", usuario_logueado);
        map.put("user_consulta","YES");  // con este se consulta los especificos
        Call<apiRespuesta> apirespuestaCall=service.obtenerReportes(map);
        apirespuestaCall.enqueue(new Callback<apiRespuesta>() {
            @Override
            public void onResponse(Call<apiRespuesta> call, Response<apiRespuesta> response) {
                if(response.isSuccessful()) {
                    limpiarInformacionCache2();
                    int id;
                    String estatus="";
                    apiRespuesta apiRespuesta = response.body();
                    count=apiRespuesta.getTotalPag();
                    contartReportes=Integer.toString(count);
                    MainActivity.gravarContador(getApplicationContext(),contartReportes);
                    MainActivity.recuperarcontador2(getApplicationContext());
                    if(count>0){
                        ListaReporte = apiRespuesta.getResults();
                        dataset = ListaReporte;
                        for (i = 0; i < ListaReporte.size(); i++){
                            datosReporte p = ListaReporte.get(i);
                            id=p.getId();
                            estatus=p.getEstatus();
                            if (estatus.equals("REPORTADO") || estatus.equals("RECIBIDO") || estatus.equals("PROCESO") || estatus.equals("ATENDIDO")){
                                almanecenarCache2(id,p.getId_reporte(),p.getUsuario(),p.getDireccion(),p.getLatitud(),p.getLongitud(),p.getTipo(),p.getFecha(),p.getFoto(),p.getFolio(),p.getEstatus(),p.getColonia(),p.getCategoria(),p.getCruzamiento(),p.getDescripcion(),p.getHora(), p.getLikes(), p.getLike_usuario());
                            }


                        }

                    }
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

    /*
    * CACHE -SQLITE - SOLO PRUEBAS
    *
    * */
    public void almanecenarCache(
            int id, String id_reporte,String usuario,String direccion, String latitud,String longitud,
            String tipo, String fecha,String foto, String folio,String estatus, String colonia, String categoria,String cruzamiento,String descripcion, String hora,int likess, String usuario_like ,int comentarios  ){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getActivity(),"db5_reportApp", null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(Utilidades.CAMPO_ID, id);
        values.put(Utilidades.CAMPO_ID_REPORTES, id_reporte);
        values.put(Utilidades.CAMPO_USUARIO, usuario);
        values.put(Utilidades.CAMPO_DIRECCION, direccion);
        values.put(Utilidades.CAMPO_LATITUD, latitud);
        values.put(Utilidades.CAMPO_LONGITUD, longitud);
        values.put(Utilidades.CAMPO_TIPO, tipo);
        values.put(Utilidades.CAMPO_FECHA, fecha);
        values.put(Utilidades.CAMPO_FOTO, foto);
        values.put(Utilidades.CAMPO_FOLIO, folio);
        values.put(Utilidades.CAMPO_STATUS, estatus);
        values.put(Utilidades.CAMPO_COLONIA, colonia);
        values.put(Utilidades.CAMPO_CATEGORIA, categoria);
        values.put(Utilidades.CAMPO_CRUZAMIENTO, cruzamiento);
        values.put(Utilidades.CAMPO_DESCRIPCION, descripcion);
        values.put(Utilidades.CAMPO_HORA, hora);
        values.put(Utilidades.CAMPO_LIKES, likess);
        values.put(Utilidades.CAMPO_USUARIO_LIKE,usuario_like);
        values.put(Utilidades.CAMPO_COMENTARIO,comentarios);

        Long idResultante=db.insert(Utilidades.TABLA_DATOS, Utilidades.CAMPO_ID, values);
     //   Toast.makeText(getActivity(), "Idregistro: " +idResultante, Toast.LENGTH_SHORT).show();
        db.close();
    }
    public void almanecenarCache2(
            int id, String id_reporte,String usuario,String direccion, String latitud,String longitud,
            String tipo, String fecha,String foto, String folio,String estatus, String colonia, String categoria,String cruzamiento,String descripcion, String hora,int likess, String usuario_like ){

        Conexion2SQLiteHelper conn=new Conexion2SQLiteHelper(getActivity(),"db4_reportApp", null,1);
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
        values.put(Utilidades2.CAMPO_USUARIO_LIKE, usuario_like);
        Long idResultante=db.insert(Utilidades2.TABLA_DATOS, Utilidades2.CAMPO_ID, values);
        //  Toast.makeText(getActivity(), "Idregistro: " +idResultante, Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void consultarMarcadoresCache(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String latitudMarcador="",longitudMarcador="",categoriaMarcador="", direccionMarcador="", coloniaMarcador="";
        double latitudMarker=0.0, longitudMarker=0.0;

        fClusterManager= new ClusterManager<ClusterMarker>(getApplicationContext(),mMap);
        myClusterManagerRender= new MyClusterManagerRender(getApplicationContext(),mMap,fClusterManager);
        fClusterManager.setRenderer(myClusterManagerRender);

        Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_DATOS, null);

        try{

            while (cursor.moveToNext()){
                direccionMarcador=cursor.getString(3);
                latitudMarcador=cursor.getString(4);
                longitudMarcador=cursor.getString(5);
                coloniaMarcador=cursor.getString(11);
                categoriaMarcador=cursor.getString(12);
                latitudMarker=Double.valueOf(latitudMarcador).doubleValue();
                longitudMarker=Double.valueOf(longitudMarcador).doubleValue();

                LatLng coordenadas = new LatLng(latitudMarker, longitudMarker);


                if (categoriaMarcador.equals("ARBOL CAIDO")) {
                    int icono = R.drawable.ic_pin_otro_o;
                    ClusterMarker arbol_caido= new ClusterMarker(coordenadas,coloniaMarcador,direccionMarcador,icono);
                    fClusterManager.addItem(arbol_caido);
                    mclusterMarkers.add(arbol_caido);

                } else if (categoriaMarcador.equals("BASURA")) {
                    int icono = R.drawable.ic_pin_mapa_basura;
                    ClusterMarker basura= new ClusterMarker(coordenadas,coloniaMarcador,direccionMarcador,icono);
                    fClusterManager.addItem(basura);
                    mclusterMarkers.add(basura);

                } else if (categoriaMarcador.equals("ALCANTARILLA")) {
                    int icono = R.drawable.ic_pin_otro_o;
                    ClusterMarker alcantarilla= new ClusterMarker(coordenadas,coloniaMarcador,direccionMarcador,icono);
                    fClusterManager.addItem(alcantarilla);
                    mclusterMarkers.add(alcantarilla);

                } else if (categoriaMarcador.equals("BACHES")) {
                    int icono = R.drawable.ic_pin_mapa_bache;
                    ClusterMarker bache= new ClusterMarker(coordenadas,coloniaMarcador,direccionMarcador,icono);
                    fClusterManager.addItem(bache);
                    mclusterMarkers.add(bache);

                } else if (categoriaMarcador.equals("ALUMBRADO")) {
                    int icono = R.drawable.ic_pin_mapa_alumbrado;
                    ClusterMarker alumbrado= new ClusterMarker(coordenadas,coloniaMarcador,direccionMarcador,icono);
                    fClusterManager.addItem(alumbrado);
                    mclusterMarkers.add(alumbrado);

                } else if (categoriaMarcador.equals("FUGA DE AGUA")) {
                    int icono = R.drawable.ic_pin_otro_o;
                    ClusterMarker agua= new ClusterMarker(coordenadas,coloniaMarcador,direccionMarcador,icono);
                    fClusterManager.addItem(agua);
                    mclusterMarkers.add(agua);

                }
                mMap.setOnCameraIdleListener(fClusterManager);
                fClusterManager.cluster();



            }
           // cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error en la consulta",Toast.LENGTH_SHORT).show();
        }

    }

    public void consultarListaDatosCache(){
        SQLiteDatabase db=conn.getReadableDatabase();

        Datos datos=null;
        listaInformacionCache=new ArrayList<Datos>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_DATOS, null);

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
            datos.setComentario(cursor.getInt(18));


            listaInformacionCache.add(datos);
        }
        //obntenerListaDatos();
        obtenerInformacionCache();
    }
    private void limpiarInformacionCache(){
        SQLiteDatabase db=conn.getReadableDatabase();
        db.delete(Utilidades.TABLA_DATOS,"",null);

    }
    private void limpiarInformacionCache2(){
        SQLiteDatabase db=conn2.getReadableDatabase();
        db.delete(Utilidades2.TABLA_DATOS,"",null);
    }
}
