package com.android.sga.reportapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.MainActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.Adapters.RecyclerViewAdapterTipo;
import com.android.sga.reportapp.SQLite.ConexionSQLiteHelper;
import com.android.sga.reportapp.gson.apiRespuesta;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.gson.datosReporte;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FormularyFragment extends Fragment implements OnMapReadyCallback {
    View rootView;
    private GoogleMap mMap;
    private static int PETICION_PERMISO_LOCALIZACION = 101;
    private Geocoder geocoder;
    double lat = 0.0, lng = 0.0;
    private List<Address> addresses;
    CameraPosition camera;
    String mensaje1, direccion_nueva = "", campolatitud = "", campolongitud = "", imageFileName, imageFileName2, imageFileName3, imageFileName4, mPath, mPath2, mPath3, mPath4;
    Button miretroceso, miubicacion, mimenu, openmisreportes, openmimapa, compartirfb, compartirtw;
    TextView campoDireccion, folioinsertado, campoTipo;
    ArrayList<String> mNames = new ArrayList<>();
    List mImagesList = new ArrayList();
    LinearLayout linearLayout, vista_categoria, vista_fotos, vista_fotos2, vista_fotos3, vista_fotos4, vista_editardireccion, vista_finalizado;
    CoordinatorLayout vista_formulario;
    LinearLayout vista_formulario2;
    Button cancelarFoto, cancelarFoto2, cancelarFoto3, cancelarFoto4, aceptarDireccion, aceptarReporte, cancelarReporte, insertarReporteFinal;
    ImageView picture, picture2, picture3, picture4, editarDirecccion, mapa_compartir;
    CircleImageView circleFcamara, circleFcamara2, circleFcamara3, circleFcamara4, circleFgaleria, circleFgaleria2, circleFgaleria3, circleFgaleria4;
    EditText mcampoDireccion, mcampoColonia, mcampoCruzamientos, mcampoCP, mcampoDireccion2, mcampoColonia2, mcampoCruzamientos2, mcampoDescripcion;
    private final int MY_RERMISSIONS = 100;
    private static final String CARPETA_PRINCIPAL = "ReportApp/";//diretorio principal
    private static final String CARPETA_IMAGEN = "Imagenes";//donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta de directorio
    File image, newFile, newFile2, newFile3, newFile4;
    Intent intent_fotouno, intent_fotodos, intent_fototres, intent_fotocuatro;
    Uri ouputfotouno, ouputfotodos, ouputfototres, ouputfotocuatro;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_SELECCIONA_2 = 11;
    private static final int COD_SELECCIONA_3 = 12;
    private static final int COD_SELECCIONA_4 = 13;
    private static final int COD_FOTO = 20;
    private static final int COD_FOTO_2 = 21;
    private static final int COD_FOTO_3 = 22;
    private static final int COD_FOTO_4 = 23;
    private Bitmap bitmapfoto1, bitmapfoto2, bitmapfoto3, bitmapfoto4, mapa_estatico;
    Bitmap rotatedBitmap1, rotatedBitmap2, rotatedBitmap3, rotatedBitmap4;
    Bitmap bitmap22;
    Uri urimapa;
    String mapa_estatico_compartir;
    File fle;
    ProgressDialog progreso;
    private Retrofit retrofit = null;
    ArrayList<datosReporte> ListaReporte, dataset;
    int i = 0, count = 0;
    private static final String TAG = "REPORTES";
    String usuariologueado = "", usuariofacebook = "", usuariolomail;
    String tipoUsuario = "";
    File f;
    Uri fotoMap;
    CheckBox checkbox, checkbox2, checkbox3;
    ConnectivityManager cm;
    NetworkInfo ni;
    boolean tipoConexion1 = false, tipoConexion2 = false;
    public static final String STRING_DIRECCION = "STRING_DIRECCION", DATA_DIRECCION = "DATA_DIRECCION";
    String direccionCache = "";
    ConexionSQLiteHelper conn;
    public static final String STRING_MODO = "STRING_REPORTES", DATA_MODO = "DATA_REPORTES", STRING_MODO2 = "STRING_MISREPORTES", DATA_MODO2 = "DATA_MISREPORTES";
    String contartReportes = "";
    Typeface Light, Bold;
    FileOutputStream fileOutputStream;

    public FormularyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_formulary, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Light = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Bold.ttf");

        //FacebookSdk.sdkInitialize(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        TextView etiqueta_ub = (TextView) rootView.findViewById(R.id.tv_seleccionUbicacion);
        etiqueta_ub.setTypeface(Bold);
        TextView etiqueta_foto = (TextView) rootView.findViewById(R.id.tv_seleccionCategoria);
        etiqueta_foto.setTypeface(Bold);
        TextView etiqueta_cat = (TextView) rootView.findViewById(R.id.tv_agregarFoto);
        etiqueta_cat.setTypeface(Bold);
        TextView etiqueta_selecfoto = (TextView) rootView.findViewById(R.id.seleccionarimagenes);
        etiqueta_selecfoto.setTypeface(Bold);
        TextView etiqueta_selecfoto2 = (TextView) rootView.findViewById(R.id.seleccionarimagenes2);
        etiqueta_selecfoto2.setTypeface(Bold);
        TextView etiqueta_selecfoto3 = (TextView) rootView.findViewById(R.id.seleccionarimagenes3);
        etiqueta_selecfoto3.setTypeface(Bold);
        TextView etiqueta_selecfoto4 = (TextView) rootView.findViewById(R.id.seleccionarimagenes4);
        etiqueta_selecfoto4.setTypeface(Bold);
        TextView etiqueta_reporteenviado = (TextView) rootView.findViewById(R.id.txt_reporteexitoso);
        etiqueta_reporteenviado.setTypeface(Light);
        TextView etiqueta_consulta = (TextView) rootView.findViewById(R.id.txt_consulta);
        etiqueta_consulta.setTypeface(Light);
        TextView etiqueta_compartir = (TextView) rootView.findViewById(R.id.txt_compartir);
        etiqueta_compartir.setTypeface(Light);


        miubicacion = (Button) rootView.findViewById(R.id.gps);
        mimenu = (Button) rootView.findViewById(R.id.menu);
        miretroceso = (Button) rootView.findViewById(R.id.atras);
        openmisreportes = (Button) rootView.findViewById(R.id.openMisReportes);
        openmisreportes.setTypeface(Light);
        openmimapa = (Button) rootView.findViewById(R.id.openMiMapa);
        openmimapa.setTypeface(Light);
        compartirfb = (Button) rootView.findViewById(R.id.compartirenfb);
        compartirtw = (Button) rootView.findViewById(R.id.compartirentw);
        campoDireccion = (TextView) rootView.findViewById(R.id.mapdireccion);
        folioinsertado = (TextView) rootView.findViewById(R.id.folioinsertado);
        campoTipo = (TextView) rootView.findViewById(R.id.campoTipo);
        vista_categoria = (LinearLayout) rootView.findViewById(R.id.vistacategoria);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.layoutformulary);
        vista_fotos = (LinearLayout) rootView.findViewById(R.id.vistafotos);
        vista_fotos2 = (LinearLayout) rootView.findViewById(R.id.vistafotos2);
        vista_fotos3 = (LinearLayout) rootView.findViewById(R.id.vistafotos3);
        vista_fotos4 = (LinearLayout) rootView.findViewById(R.id.vistafotos4);
        vista_editardireccion = (LinearLayout) rootView.findViewById(R.id.vistaeditardireccion);
        vista_finalizado = (LinearLayout) rootView.findViewById(R.id.finalizado);
        vista_formulario = (CoordinatorLayout) rootView.findViewById(R.id.l_map_form);
        vista_formulario2 = (LinearLayout) rootView.findViewById(R.id.l_desplegable);
        cancelarFoto = (Button) rootView.findViewById(R.id.cancelarfoto);
        cancelarFoto.setTypeface(Bold);
        cancelarFoto2 = (Button) rootView.findViewById(R.id.cancelarfoto2);
        cancelarFoto2.setTypeface(Bold);
        cancelarFoto3 = (Button) rootView.findViewById(R.id.cancelarfoto3);
        cancelarFoto3.setTypeface(Bold);
        cancelarFoto4 = (Button) rootView.findViewById(R.id.cancelarfoto4);
        cancelarFoto4.setTypeface(Bold);
        aceptarDireccion = (Button) rootView.findViewById(R.id.aceptardireccion);
        checkbox = (CheckBox) rootView.findViewById(R.id.checkBox);
        checkbox.setTypeface(Light);
        checkbox2 = (CheckBox) rootView.findViewById(R.id.checkBox2);
        checkbox2.setTypeface(Light);
        checkbox3 = (CheckBox) rootView.findViewById(R.id.checkBox3);
        checkbox3.setTypeface(Light);
        aceptarReporte = (Button) rootView.findViewById(R.id.aceptar);
        aceptarReporte.setTypeface(Light);
        cancelarReporte = (Button) rootView.findViewById(R.id.cancelacion_reporte);
        insertarReporteFinal = (Button) rootView.findViewById(R.id.insertarreportefinal);
        insertarReporteFinal.setTypeface(Light);
        picture = (ImageView) rootView.findViewById(R.id.setPicture);
        picture2 = (ImageView) rootView.findViewById(R.id.setPicture2);
        picture3 = (ImageView) rootView.findViewById(R.id.setPicture3);
        picture4 = (ImageView) rootView.findViewById(R.id.setPicture4);
        mapa_compartir=(ImageView)rootView.findViewById(R.id.mapa_compartir);
        circleFcamara = (CircleImageView) rootView.findViewById(R.id.circleCamera);
        circleFcamara2 = (CircleImageView) rootView.findViewById(R.id.circleCamera2);
        circleFcamara3 = (CircleImageView) rootView.findViewById(R.id.circleCamera3);
        circleFcamara4 = (CircleImageView) rootView.findViewById(R.id.circleCamera4);
        circleFgaleria = (CircleImageView) rootView.findViewById(R.id.circleGaleria);
        circleFgaleria2 = (CircleImageView) rootView.findViewById(R.id.circleGaleria2);
        circleFgaleria3 = (CircleImageView) rootView.findViewById(R.id.circleGaleria3);
        circleFgaleria4 = (CircleImageView) rootView.findViewById(R.id.circleGaleria4);
        editarDirecccion = (ImageView) rootView.findViewById(R.id.editarcalle);
        mcampoDireccion = (EditText) rootView.findViewById(R.id.campoDireccion);
        mcampoDireccion2 = (EditText) rootView.findViewById(R.id.campoDireccion2);
        mcampoDireccion2.setTypeface(Light);
        mcampoColonia = (EditText) rootView.findViewById(R.id.campoColonia);
        mcampoColonia2 = (EditText) rootView.findViewById(R.id.campoColonia2);
        mcampoColonia2.setTypeface(Light);
        mcampoCruzamientos = (EditText) rootView.findViewById(R.id.campoCruzamientos);
        mcampoCruzamientos2 = (EditText) rootView.findViewById(R.id.campoCruzamientos2);
        mcampoCruzamientos2.setTypeface(Light);
        mcampoDescripcion = (EditText) rootView.findViewById(R.id.campoDescricion);
        mcampoDescripcion.setTypeface(Light);
        mcampoCP = (EditText) rootView.findViewById(R.id.campoCP);
        retrofit = new Retrofit.Builder().baseUrl("http://terceracto.com.mx/reportapp/services/").addConverterFactory(GsonConverterFactory.create()).build();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();

        conn = new ConexionSQLiteHelper(getActivity(), "db3_reportApp", null, 1);
        //PERMISOS
        //Permisos para versiones superiores de 6.0
        if (mayRequestStoragePermission()) {
            picture.setEnabled(true);
            picture2.setEnabled(true);
            picture3.setEnabled(true);
            picture4.setEnabled(true);
        } else {
            picture.setEnabled(false);
            picture2.setEnabled(false);
            picture3.setEnabled(false);
            picture4.setEnabled(false);
        }

        recuperarIdFacebook(); //funcion recuperar datos almacenados
        recuperarIdUserMail();
        if (usuariofacebook.length() == 0) {
            usuariologueado = usuariolomail;
            tipoUsuario = "Correo";

        } else {
            usuariologueado = usuariofacebook;
            tipoUsuario = "Facebook";
        }
        //LOAD FUNCTIONS
        loadImagesCategory();
        //EVENTS
        miubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miUbicacion();
            }
        });
        mimenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Desplegar menu
                ((MainActivity) getActivity()).openDrawer();
            }
        });
        miretroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Desplegar menu
                ((MainActivity) getActivity()).opeFragmentMap();
            }
        });
        openmimapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Desplegar menu
                ((MainActivity) getActivity()).opeFragmentMap();
            }
        });
        openmisreportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chekeo de internet
                if (ni != null) {
                    ConnectivityManager connectivityManager1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connectivityManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    ConnectivityManager connectivityManager2 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mMobile = connectivityManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mWifi.isConnected()) {
                        tipoConexion1 = true;
                    }
                    if (mMobile.isConnected()) {
                        tipoConexion2 = true;
                    }
                    if (tipoConexion1 == true || tipoConexion1 == true) {
                        ((MainActivity) getActivity()).opeFragmentMisReportes();
                    } else {
                        ((MainActivity) getActivity()).opeFragmentMisReportes();
                    }

                } else {
                    ((MainActivity) getActivity()).opeFragmentMisReportes();
                }


            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_fotos.getVisibility() == View.GONE) {
                    vista_fotos.setVisibility(View.VISIBLE);
                    if(vista_categoria.getVisibility() == View.VISIBLE  || vista_fotos2.getVisibility() == View.VISIBLE  || vista_fotos3.getVisibility() == View.VISIBLE || vista_fotos4.getVisibility() == View.VISIBLE ){
                        vista_fotos2.setVisibility(View.GONE);
                        vista_fotos3.setVisibility(View.GONE);
                        vista_fotos4.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.GONE);
                    }

                }
            }
        });
        picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_fotos2.getVisibility() == View.GONE) {
                    vista_fotos2.setVisibility(View.VISIBLE);
                    if(vista_categoria.getVisibility() == View.VISIBLE || vista_fotos.getVisibility() == View.VISIBLE  || vista_fotos3.getVisibility() == View.VISIBLE || vista_fotos4.getVisibility() == View.VISIBLE ){
                        vista_fotos.setVisibility(View.GONE);
                        vista_fotos3.setVisibility(View.GONE);
                        vista_fotos4.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.GONE);
                    }

                }
            }
        });
        picture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_fotos3.getVisibility() == View.GONE) {
                    vista_fotos3.setVisibility(View.VISIBLE);
                    if(vista_categoria.getVisibility() == View.VISIBLE || vista_fotos.getVisibility() == View.VISIBLE  || vista_fotos2.getVisibility() == View.VISIBLE || vista_fotos4.getVisibility() == View.VISIBLE ){
                        vista_fotos.setVisibility(View.GONE);
                        vista_fotos2.setVisibility(View.GONE);
                        vista_fotos4.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.GONE);
                    }
                }
            }
        });
        picture4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_fotos4.getVisibility() == View.GONE) {
                    vista_fotos4.setVisibility(View.VISIBLE);
                    if(vista_categoria.getVisibility() == View.VISIBLE || vista_fotos.getVisibility() == View.VISIBLE  || vista_fotos2.getVisibility() == View.VISIBLE || vista_fotos3.getVisibility() == View.VISIBLE ){
                        vista_fotos.setVisibility(View.GONE);
                        vista_fotos2.setVisibility(View.GONE);
                        vista_fotos3.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.GONE);
                    }
                }
            }
        });
        editarDirecccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_editardireccion.getVisibility() == View.GONE) {
                    vista_editardireccion.setVisibility(View.VISIBLE);
                    vista_categoria.setVisibility(View.GONE);
                }
            }
        });
        cancelarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_fotos.getVisibility() == View.VISIBLE) {
                    vista_fotos.setVisibility(View.GONE);
                    if(vista_fotos2.getVisibility() == View.GONE  || vista_fotos3.getVisibility() == View.GONE || vista_fotos4.getVisibility() == View.GONE ){
                        vista_categoria.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        cancelarFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_fotos2.getVisibility() == View.VISIBLE) {
                    vista_fotos2.setVisibility(View.GONE);
                    if(vista_fotos.getVisibility() == View.GONE  || vista_fotos3.getVisibility() == View.GONE || vista_fotos4.getVisibility() == View.GONE ){
                        vista_categoria.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        cancelarFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_fotos3.getVisibility() == View.VISIBLE) {
                    vista_fotos3.setVisibility(View.GONE);
                    if(vista_fotos2.getVisibility() == View.GONE  || vista_fotos3.getVisibility() == View.GONE || vista_fotos4.getVisibility() == View.GONE ){
                        vista_categoria.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        cancelarFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_fotos4.getVisibility() == View.VISIBLE) {
                    vista_fotos4.setVisibility(View.GONE);
                    if(vista_fotos2.getVisibility() == View.GONE  || vista_fotos3.getVisibility() == View.GONE || vista_fotos4.getVisibility() == View.GONE ){
                        vista_categoria.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        aceptarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vista_editardireccion.getVisibility() == View.VISIBLE) {
                    vista_editardireccion.setVisibility(View.GONE);
                    vista_categoria.setVisibility(View.VISIBLE);
                }
            }
        });
        aceptarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //almacenr en base de datos
                //CapturaDeMaps();
                CapturaDeMapaStatico();
                if (vista_formulario2.getVisibility() == View.GONE) {
                    if(campoDireccion.getText()!="Mover para buscar..." || campoDireccion.getText()!="Unnamed Road" ){

                        vista_formulario2.setVisibility(View.VISIBLE);
                        vista_formulario.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(getActivity(), "No es una dirección válida", Toast.LENGTH_LONG).show();
                    }


                }

            }
        });
        cancelarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view= getActivity().getCurrentFocus();
                if (view!= null){
                    InputMethodManager teclado= (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    teclado.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
                if (vista_formulario.getVisibility() == View.GONE) {

                    vista_formulario.setVisibility(View.VISIBLE);
                    vista_formulario2.setVisibility(View.GONE);
                }

            }
        });
        insertarReporteFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verificar si hay conexion a red de lo contrario no se manda el reporte
                if (ni != null) {
                    ConnectivityManager connectivityManager1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connectivityManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    ConnectivityManager connectivityManager2 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mMobile = connectivityManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mWifi.isConnected()) {
                        tipoConexion1 = true;
                    }
                    if (mMobile.isConnected()) {
                        tipoConexion2 = true;
                    }
                    if (tipoConexion1 == true || tipoConexion1 == true) {
                        //CUANDO EXISTA UN CONEXION SE EJECUTA LA FUNCION
                        if (!mcampoDescripcion.getText().toString().isEmpty()) {
                            // CapturaDeMaps();
                            String modo = "ACTIVADO";
                            String modo2 = "ACTIVADO";
                            activadorReporte(getApplicationContext(), modo);
                            activadorMiReporte(getApplicationContext(), modo2);
                            View view= getActivity().getCurrentFocus();
                            if (view!= null){
                                InputMethodManager teclado= (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                teclado.hideSoftInputFromWindow(view.getWindowToken(),0);
                            }
                            insertarReporte(); //FUNCION PARA INSERTAR REPORTE 21/06/2019
                            //totalDatosReportesActualizar();


                            //para probar twitter
                           /* if (vista_finalizado.getVisibility() == View.GONE) {
                                vista_finalizado.setVisibility(View.VISIBLE);
                                vista_formulario2.setVisibility(View.GONE);
                            }*/

                        } else {
                            Toast.makeText(getActivity(), "Introdusca una descripción para enviar su reporte", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Sin datos, no se puede enviar su reporte", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Sin conexión, no se puede enviar su reporte", Toast.LENGTH_LONG).show();
                }

            }
        });

        circleFcamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });
        circleFgaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), COD_SELECCIONA);
            }
        });
        circleFcamara2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara2();
            }
        });
        circleFgaleria2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), COD_SELECCIONA_2);
            }
        });
        circleFcamara3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara3();
            }
        });
        circleFgaleria3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), COD_SELECCIONA_3);
            }
        });

        circleFcamara4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara4();
            }
        });
        circleFgaleria4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), COD_SELECCIONA_4);
            }
        });

        compartirfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CapturaDeMaps();
                SharePhoto fotoReporte = new SharePhoto
                        .Builder()
                        .setBitmap(rotatedBitmap1)
                        .build();

                SharePhoto mapaReporte = new SharePhoto
                        .Builder()
                        .setBitmap(mapa_estatico)
                        .build();

                ShareContent contenido = new SharePhotoContent
                        .Builder()
                        .addPhoto(fotoReporte)
                        .addPhoto(mapaReporte)
                        .build();
                ShareDialog.show(getActivity(), contenido);

            }
        });


        compartirtw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CapturaDeMaps();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Compartiendo desde ReportApp");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // intent.setDataAndType(ouputfotouno, intent.getType());
                intent.setType("image/*");
                // ArrayList<Uri> uris = new ArrayList<Uri>();
                //uris.add(ouputfotouno);
                //uris.add(fotoMap);
                //intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
                //intent.putExtra(Intent.EXTRA_STREAM,ouputfotouno);
                //intent.putExtra(Intent.EXTRA_STREAM, fotoMap);
                intent.putExtra(Intent.EXTRA_STREAM, fotoMap);
                intent.setPackage("com.twitter.android");
                startActivity(intent);

            }
        });

        return rootView;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void recuperarIdFacebook() {
        SharedPreferences preferences = getActivity().getSharedPreferences(LoginActivity.STRING_DATAFB, MODE_PRIVATE);
        String d = preferences.getString(LoginActivity.DATAFB_IDFB, "");
        usuariofacebook = d;
    }

    public void recuperarIdUserMail() {
        SharedPreferences preferences = getActivity().getSharedPreferences(LoginActivity.STRING_DATAMAIL, MODE_PRIVATE);
        String d = preferences.getString(LoginActivity.DATAMAIL_IDMAIL, "");
        usuariolomail = d;
    }

    public static void activadorReporte(Context c, String modo) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_MODO, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DATA_MODO, modo);
        editor.commit();
    }

    public static void activadorMiReporte(Context c, String modo) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_MODO2, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DATA_MODO2, modo);
        editor.commit();
    }

    public void almacenarDireccionCache(String direccionCache) {
        SharedPreferences preferences = getActivity().getSharedPreferences(STRING_DIRECCION, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DATA_DIRECCION, direccionCache);
        editor.commit();
    }

    public void recuperarDireccionCache() {
        SharedPreferences preferences = getActivity().getSharedPreferences(STRING_DIRECCION, MODE_PRIVATE);
        String d = preferences.getString(DATA_DIRECCION, "");
        direccionCache = d;
    }

    public void actualizarNumReporte() {
        String usuario = usuariologueado;
        apiService service = apiService.retrofi.create(apiService.class);
        Map<String, Object> map = new HashMap<>();
        map.put("usuario", usuario);
        map.put("user_consulta", "YES");
        Call<apiRespuesta> apirespuestaCall = service.obtenerReportes(map);
        apirespuestaCall.enqueue(new Callback<apiRespuesta>() {
            @Override
            public void onResponse(Call<apiRespuesta> call, Response<apiRespuesta> response) {
                if (response.isSuccessful()) {
                    int id;
                    String folio = "";
                    apiRespuesta apiRespuesta = response.body();
                    count = apiRespuesta.getTotalPag();
                    contartReportes = Integer.toString(count);
                    MainActivity.gravarContador(getApplicationContext(), contartReportes);
                    MainActivity.recuperarcontador2(getApplicationContext());
                    if (count > 0) {
                        ListaReporte = apiRespuesta.getResults();
                        dataset = ListaReporte;
                        for (i = 0; i < ListaReporte.size(); i++) {
                            datosReporte p = ListaReporte.get(i);
                            if (i == 0) {
                                folio = p.getFolio();
                                folioinsertado.setText("FOLIO #MIDF" + folio);
                            }
                        }

                    }
                } else {
                    Log.e(TAG, "onResponse:" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<apiRespuesta> call, Throwable t) {
            }
        });

    }

    public void insertarReporte() {
        progreso = new ProgressDialog(getActivity());
        progreso.setMessage("Enviando...");
        progreso.show();
        apiService service = apiService.retrofi.create(apiService.class);
        String categoria = campoTipo.getText().toString();
        String descripcion = mcampoDescripcion.getText().toString();
        String direccion = mcampoDireccion.getText().toString();
        String cruzamientos = mcampoCruzamientos2.getText().toString();
        String colonia = mcampoColonia.getText().toString();
        String cp = mcampoCP.getText().toString();
        String latitudreporte = campolatitud;
        String longitudreporte = campolongitud;
        String usuario = usuariologueado;
        String usuariotipo = tipoUsuario;

        RequestBody categoriabody = RequestBody.create(MultipartBody.FORM, categoria);
        RequestBody descripcionbody = RequestBody.create(MultipartBody.FORM, descripcion);
        RequestBody direccionbody = RequestBody.create(MultipartBody.FORM, direccion);
        RequestBody cruzamientosbody = RequestBody.create(MultipartBody.FORM, cruzamientos);
        RequestBody coloniabody = RequestBody.create(MultipartBody.FORM, colonia);
        RequestBody cpbody = RequestBody.create(MultipartBody.FORM, cp);
        RequestBody latitudbody = RequestBody.create(MultipartBody.FORM, latitudreporte);
        RequestBody longitudbody = RequestBody.create(MultipartBody.FORM, longitudreporte);
        RequestBody usuariobody = RequestBody.create(MultipartBody.FORM, usuario);
        RequestBody usuariotipobody = RequestBody.create(MultipartBody.FORM, usuariotipo);
         MultipartBody.Part imagePart_uno = null;
        MultipartBody.Part imagePart_dos = null;
        MultipartBody.Part imagePart_tres = null;
        MultipartBody.Part imagePart_cuatro = null;
        if (rotatedBitmap1 != null) {
            File photo_uno = newFile;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatedBitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytesArray = stream.toByteArray();

            if (photo_uno != null) {
                RequestBody imagebodyuno = RequestBody.create(MediaType.parse("image/*"), bytesArray);
                imagePart_uno = MultipartBody.Part.createFormData("foto1", photo_uno.getName(), imagebodyuno);
            } else {
                File img1_galeria = new File(getPath(ouputfotouno));
                RequestBody imagebodyuno = RequestBody.create(MediaType.parse("image/*"), img1_galeria);
                imagePart_uno = MultipartBody.Part.createFormData("foto1", img1_galeria.getName(), imagebodyuno);
            }
        }
        if (rotatedBitmap2 != null) {
            File photo_dos = newFile2;
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            rotatedBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            byte[] bytesArray2 = stream2.toByteArray();
            if (photo_dos != null) {
                RequestBody imagebodydos = RequestBody.create(MediaType.parse("image/*"), bytesArray2);
                imagePart_dos = MultipartBody.Part.createFormData("foto2", photo_dos.getName(), imagebodydos);
            } else {
                File img2_galeria = new File(getPath(ouputfotodos));
                RequestBody imagebodydos = RequestBody.create(MediaType.parse("image/*"), img2_galeria);
                imagePart_dos = MultipartBody.Part.createFormData("foto2", img2_galeria.getName(), imagebodydos);
            }
        }
        if (rotatedBitmap3 != null) {
            File photo_tres = newFile3;
            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            rotatedBitmap3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
            byte[] bytesArray3 = stream3.toByteArray();
            if (photo_tres != null) {
                RequestBody imagebodytres = RequestBody.create(MediaType.parse("image/*"), bytesArray3);
                imagePart_tres = MultipartBody.Part.createFormData("foto3", photo_tres.getName(), imagebodytres);
            } else {
                File img3_galeria = new File(getPath(ouputfototres));
                RequestBody imagebodytres = RequestBody.create(MediaType.parse("image/*"), img3_galeria);
                imagePart_tres = MultipartBody.Part.createFormData("foto3", img3_galeria.getName(), imagebodytres);
            }
        }
        if (rotatedBitmap4 != null) {
            File photo_cuatro = newFile4;
            ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
            rotatedBitmap4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
            byte[] bytesArray4 = stream4.toByteArray();
            if (photo_cuatro != null) {
                RequestBody imagebodycuatro = RequestBody.create(MediaType.parse("image/*"), bytesArray4);
                imagePart_cuatro = MultipartBody.Part.createFormData("foto4", photo_cuatro.getName(), imagebodycuatro);
            } else {
                File img4_galeria = new File(getPath(ouputfotocuatro));
                RequestBody imagebodycuatro = RequestBody.create(MediaType.parse("image/*"), img4_galeria);
                imagePart_cuatro = MultipartBody.Part.createFormData("foto4", img4_galeria.getName(), imagebodycuatro);
            }
        }
        final Call<ResponseBody> call = service.insertarFormulario(
                categoriabody,
                direccionbody,
                descripcionbody,
                cruzamientosbody,
                coloniabody,
                cpbody,
                latitudbody,
                longitudbody,
                usuariobody,
                usuariotipobody,
                imagePart_uno,
                imagePart_dos,
                imagePart_tres,
                imagePart_cuatro);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("results", "Creacion de fomularion correcta" + response.body().toString());
                actualizarNumReporte();
                progreso.hide();
                Toast.makeText(getActivity(), "Se ha enviado su reporte con éxito", Toast.LENGTH_SHORT).show();
                if (vista_finalizado.getVisibility() == View.GONE) {
                    vista_finalizado.setVisibility(View.VISIBLE);
                    vista_formulario2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("error", t.toString());
                Toast.makeText(getActivity(), "Fallo al envio de su reporte, intente de nuevo", Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        });

    }

    //FUNCION PARA EXTRAER LA CADENA DE UN URI
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

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
        if (ni != null) {
            ConnectivityManager connectivityManager1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connectivityManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            ConnectivityManager connectivityManager2 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobile = connectivityManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mWifi.isConnected()) {
                tipoConexion1 = true;
            }
            if (mMobile.isConnected()) {
                tipoConexion2 = true;
            }
            if (tipoConexion1 == true || tipoConexion1 == true) {
                //Cargar Datos , almacenar cache, consultar
                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        double latitud_center_map = mMap.getCameraPosition().target.latitude;
                        double longitud_center_map = mMap.getCameraPosition().target.longitude;
                        campolatitud = (String.valueOf(latitud_center_map));
                        campolongitud = (String.valueOf(longitud_center_map));

                        try {
                            addresses = geocoder.getFromLocation(latitud_center_map, longitud_center_map, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addresses==null){
                            Toast.makeText(getActivity(), "Cargando... ubicación", Toast.LENGTH_LONG).show();
                            recuperarDireccionCache();
                            campoDireccion.setText(direccionCache);
                        }
                        else {
                            direccion_nueva = addresses.get(0).getAddressLine(0);
                            String calle = addresses.get(0).getThoroughfare();
                            String numero = addresses.get(0).getSubThoroughfare();
                            String colonia = addresses.get(0).getSubLocality();
                            String cp = addresses.get(0).getPostalCode();

                        if (calle == null && numero == null && colonia == null) {
                            campoDireccion.setText("Mover para buscar...");
                            mcampoDireccion.setText("Sin dirección");
                            mcampoDireccion2.setText("Sin dirección");
                            mcampoColonia.setText("Sin colonia");
                            mcampoColonia2.setText("Sin colonia");
                            mcampoCP.setText("Sin CP");
                            almacenarDireccionCache("Mover para buscar...");
                        } else {
                            if (calle != null && numero == null && colonia == null) {
                                campoDireccion.setText(calle);
                                mcampoDireccion.setText(calle);
                                mcampoDireccion2.setText(calle);
                                mcampoColonia.setText("Sin colonia");
                                mcampoColonia2.setText("Sin colonia");
                                mcampoCP.setText(cp);
                                almacenarDireccionCache(calle);
                            } else {
                                if (calle != null && numero == null && colonia != null) {
                                    campoDireccion.setText(calle + " S/N ," + colonia);
                                    mcampoDireccion.setText(calle + " S/N");
                                    mcampoColonia.setText(colonia);
                                    mcampoDireccion2.setText(calle + " S/N");
                                    mcampoColonia2.setText(colonia);
                                    mcampoCP.setText(cp);
                                    almacenarDireccionCache(calle + " S/N ," + colonia);
                                } else {
                                    if (calle != null && numero != null && colonia == null) {
                                        campoDireccion.setText(calle + " " + numero);
                                        mcampoDireccion.setText(calle + " " + numero);
                                        mcampoColonia.setText("Sin colonia");
                                        mcampoDireccion2.setText(calle + " " + numero);
                                        mcampoColonia2.setText("Sin colonia");
                                        mcampoCP.setText(cp);
                                        almacenarDireccionCache(calle + " " + numero);
                                    } else {
                                        if (calle == null && numero == null & colonia != null) {
                                            campoDireccion.setText("Mover para buscar...");
                                            mcampoDireccion.setText("Sin dirección");
                                            mcampoColonia.setText("Sin colonia");
                                            mcampoDireccion2.setText("Sin dirección");
                                            mcampoColonia2.setText("Sin colonia");
                                            mcampoCP.setText(cp);
                                            almacenarDireccionCache("Mover para buscar...");
                                        } else {
                                            if (calle == null && numero != null & colonia != null) {
                                                campoDireccion.setText("Mover para buscar...");
                                                mcampoDireccion.setText("Sin dirección");
                                                mcampoColonia.setText("Sin colonia");
                                                mcampoDireccion2.setText("Sin dirección");
                                                mcampoColonia2.setText("Sin colonia");
                                                mcampoCP.setText(cp);
                                                almacenarDireccionCache("Mover para buscar...");
                                            } else {
                                                campoDireccion.setText(calle + " " + numero + ", " + colonia);
                                                mcampoColonia.setText(colonia);
                                                mcampoDireccion.setText(calle + " " + numero);
                                                mcampoColonia2.setText(colonia);
                                                mcampoDireccion2.setText(calle + " " + numero);
                                                mcampoCP.setText(cp);
                                                almacenarDireccionCache(calle + " " + numero + ", " + colonia);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Sin datos", Toast.LENGTH_LONG).show();
                recuperarDireccionCache();
                campoDireccion.setText(direccionCache);
            }
        } else {
            Toast.makeText(getActivity(), "Sin conexión", Toast.LENGTH_LONG).show();
            recuperarDireccionCache();
            campoDireccion.setText(direccionCache);
        }
    }

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETICION_PERMISO_LOCALIZACION);
            return;
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);//llamo la funcion para actualizar mi ubicacion
        }
    }

    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            AgregarMarcador(lat, lng);
        }
    }

    //POSICIONAR LA CAMARA DEL MAPS EN MI POSICION
    private void CameraInit() {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    }

    //FUNCION PARA AGREGAR MI POSICION POR MARCADOR EN EL MAPS
    private void AgregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        camera = new CameraPosition.Builder()
                .target(coordenadas)
                .zoom(16) //limite -->21(muy cerka la camara)
                .bearing(0)//rotaciones al este -tenienoo en cuenta 0-365º
                .tilt(50)// limite 90
                .build();
        CameraInit();
    }

    //FUNCION PARA ABRIR CAMARA
    private void abrirCamara() {
        File file = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN); //creo un directorio en mi almacenamiento interno si no existe
        boolean isCreada = file.exists();
        if (!isCreada) isCreada = file.mkdirs(); //sino existe se crea la direccion de carpeta
        if (isCreada) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            imageFileName = "FOTO_" + timeStamp + ".jpg";
            mPath = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                    + File.separator + imageFileName;//indicamos la ruta de almacenamiento
            newFile = new File(mPath); //construimos el archivo
            intent_fotouno = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getActivity().getPackageName() + ".provider";
                ouputfotouno = FileProvider.getUriForFile(getActivity(), authorities, newFile);
                intent_fotouno.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotouno);
            } else {
                ouputfotouno = Uri.fromFile(newFile);
                intent_fotouno.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotouno); //indicar un tipo URI (imgo video)
            }
            startActivityForResult(intent_fotouno, COD_FOTO); //llamara la funcion onActivityResult
        }
    }

    private void abrirCamara2() {
        File file = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada = file.exists();
        if (!isCreada) isCreada = file.mkdirs(); //crea la direccion
        if (isCreada) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            imageFileName2 = "FOTO_" + timeStamp + ".jpg";
            mPath2 = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                    + File.separator + imageFileName2;//indicamos la ruta de almacenamiento
            newFile2 = new File(mPath2); //construimos el archivo
            intent_fotodos = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//Accede ala camara de nuestro dispositivo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getActivity().getPackageName() + ".provider";
                ouputfotodos = FileProvider.getUriForFile(getActivity(), authorities, newFile2);
                intent_fotodos.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotodos);

            } else {
                ouputfotodos = Uri.fromFile(newFile2);//Pasamos nuestro file a Uri,
                intent_fotodos.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotodos); //y con la orden de almacenar en el dispositivo la imagen y el uri con la ruta dela imagen
            }
            startActivityForResult(intent_fotodos, COD_FOTO_2); //se abre una actividad con nuestra camara y almacena el resultado para procesarla
        }
    }

    private void abrirCamara3() {
        File file = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada = file.exists();
        if (!isCreada) isCreada = file.mkdirs(); //crea la direccion
        if (isCreada) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            imageFileName3 = "FOTO_" + timeStamp + ".jpg";
            mPath3 = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                    + File.separator + imageFileName3;//indicamos la ruta de almacenamiento
            newFile3 = new File(mPath3); //construimos el archivo
            intent_fototres = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getActivity().getPackageName() + ".provider";
                ouputfototres = FileProvider.getUriForFile(getActivity(), authorities, newFile3);
                intent_fototres.putExtra(MediaStore.EXTRA_OUTPUT, ouputfototres);

            } else {
                ouputfototres = Uri.fromFile(newFile3);
                intent_fototres.putExtra(MediaStore.EXTRA_OUTPUT, ouputfototres); //indicar un tipo curi (imgo video)
            }
            startActivityForResult(intent_fototres, COD_FOTO_3); //llamara la funcion onActivityResult
        }
    }

    private void abrirCamara4() {
        File file = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada = file.exists();
        if (!isCreada) isCreada = file.mkdirs(); //crea la direccion
        if (isCreada) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            imageFileName4 = "FOTO_" + timeStamp + ".jpg";
            mPath4 = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                    + File.separator + imageFileName4;//indicamos la ruta de almacenamiento
            newFile4 = new File(mPath4); //construimos el archivo
            intent_fotocuatro = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getActivity().getPackageName() + ".provider";
                ouputfotocuatro = FileProvider.getUriForFile(getActivity(), authorities, newFile4);
                intent_fotocuatro.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotocuatro);

            } else {
                ouputfotocuatro = Uri.fromFile(newFile4);
                intent_fotocuatro.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotocuatro); //indicar un tipo curi (imgo video)
            }
            startActivityForResult(intent_fotocuatro, COD_FOTO_4); //llamara la funcion onActivityResult
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
        outState.putString("file_path", mPath2);
        outState.putString("file_path", mPath3);
        outState.putString("file_path", mPath4);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //mPath = savedInstanceState.getString("file_path");
        //mPath2 = savedInstanceState.getString("file_path");
        //mPath3 = savedInstanceState.getString("file_path");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COD_SELECCIONA:
                if (resultCode == RESULT_OK) {
                    ouputfotouno = data.getData();
                    //rotatedBitmap1=null;
                    try {
                        rotatedBitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ouputfotouno);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                   /* try {
                        ExifInterface ei = new ExifInterface(getPath(ouputfotouno));
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap1 = rotateImage(bitmapfoto1, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap1 = rotateImage(bitmapfoto1, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap1 = rotateImage(bitmapfoto1, 270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default: rotatedBitmap1 = bitmapfoto1; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    picture.setPadding(0,0,0,0);
                    picture.setImageBitmap(rotatedBitmap1);
                   // picture.setImageBitmap(Bitmap.createScaledBitmap(bitmapfoto1, 90, 90, false));
                    if (vista_fotos.getVisibility() == View.VISIBLE) {
                        vista_fotos.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.VISIBLE);
                    }
                    checkbox3.setChecked(true);
                    if (checkbox.isChecked() & checkbox3.isChecked()) {
                        aceptarReporte.setEnabled(true);
                    }
                }
                break;
            case COD_FOTO:
                if (resultCode == RESULT_OK) {
                    bitmapfoto1 = BitmapFactory.decodeFile(mPath);
                    rotatedBitmap1 = null;
                    try {
                        ExifInterface ei = new ExifInterface(mPath);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap1 = rotateImage(bitmapfoto1, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap1 = rotateImage(bitmapfoto1, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap1 = rotateImage(bitmapfoto1, 270);
                                break;
                             case ExifInterface.ORIENTATION_NORMAL:
                                 default: rotatedBitmap1 = bitmapfoto1; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    picture.setPadding(0,0,0,0);
                    //picture.setImageBitmap(bitmapfoto1);
                    picture.setImageBitmap(rotatedBitmap1);
                    if (vista_fotos.getVisibility() == View.VISIBLE) {
                        vista_fotos.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.VISIBLE);
                    }
                    checkbox3.setChecked(true);
                    if (checkbox.isChecked() & checkbox3.isChecked()) {
                        aceptarReporte.setEnabled(true);
                    }
                }
                break;
            case COD_SELECCIONA_2:
                if (resultCode == RESULT_OK) {
                    ouputfotodos = data.getData();
                    rotatedBitmap2=null;
                    try {
                        rotatedBitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ouputfotodos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                 /*   try {
                        ExifInterface ei = new ExifInterface(getPath(ouputfotodos));
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap2 = rotateImage(bitmapfoto2, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap2 = rotateImage(bitmapfoto2, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap2 = rotateImage(bitmapfoto2, 270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default: rotatedBitmap2 = bitmapfoto2; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    picture2.setPadding(0,0,0,0);
                    picture2.setImageBitmap(rotatedBitmap2);
                    if (vista_fotos2.getVisibility() == View.VISIBLE) {
                        vista_fotos2.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.VISIBLE);
                    }
                    checkbox3.setChecked(true);
                    if (checkbox.isChecked() & checkbox3.isChecked()) {
                        aceptarReporte.setEnabled(true);
                    }
                }
                break;
            case COD_FOTO_2:
                if (resultCode == RESULT_OK) {
                    bitmapfoto2 = BitmapFactory.decodeFile(mPath2);
                    rotatedBitmap2 = null;
                    try {
                        ExifInterface ei = new ExifInterface(mPath2);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap2 = rotateImage(bitmapfoto2, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap2 = rotateImage(bitmapfoto2, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap2 = rotateImage(bitmapfoto2, 270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default: rotatedBitmap2 = bitmapfoto2; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    picture2.setPadding(0,0,0,0);
                //    picture2.setImageBitmap(bitmapfoto2);
                    picture2.setImageBitmap(rotatedBitmap2);
                    if (vista_fotos2.getVisibility() == View.VISIBLE) {
                        vista_fotos2.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.VISIBLE);
                    }
                    checkbox3.setChecked(true);
                    if (checkbox.isChecked() & checkbox3.isChecked()) {
                        aceptarReporte.setEnabled(true);
                    }
                }
                break;
            case COD_SELECCIONA_3:
                if (resultCode == RESULT_OK) {
                    ouputfototres = data.getData();
                    rotatedBitmap3=null;
                    try {
                        rotatedBitmap3 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ouputfototres);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*try {
                        ExifInterface ei = new ExifInterface(getPath(ouputfototres));
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap3 = rotateImage(bitmapfoto3, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap3 = rotateImage(bitmapfoto3, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap3 = rotateImage(bitmapfoto3, 270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default: rotatedBitmap3 = bitmapfoto3; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

*/

                    picture3.setPadding(0,0,0,0);
                    picture3.setImageBitmap(rotatedBitmap3);
                    if (vista_fotos3.getVisibility() == View.VISIBLE) {
                        vista_fotos3.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.VISIBLE);
                    }
                    checkbox3.setChecked(true);
                    if (checkbox.isChecked() & checkbox3.isChecked()) {
                        aceptarReporte.setEnabled(true);
                    }
                }
                break;
            case COD_FOTO_3:
                if (resultCode == RESULT_OK) {
                    bitmapfoto3 = BitmapFactory.decodeFile(mPath3);
                    rotatedBitmap3 = null;
                    try {
                        ExifInterface ei = new ExifInterface(mPath3);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap3 = rotateImage(bitmapfoto3, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap3 = rotateImage(bitmapfoto3, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap3 = rotateImage(bitmapfoto3, 270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default: rotatedBitmap3 = bitmapfoto3; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    picture3.setPadding(0,0,0,0);
                   // picture3.setImageBitmap(bitmapfoto3);
                    picture3.setImageBitmap(rotatedBitmap3);
                    if (vista_fotos3.getVisibility() == View.VISIBLE) {
                        vista_fotos3.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.VISIBLE);
                    }
                    checkbox3.setChecked(true);
                    if (checkbox.isChecked() & checkbox3.isChecked()) {
                        aceptarReporte.setEnabled(true);
                    }
                }
                break;

            case COD_SELECCIONA_4:
                if (resultCode == RESULT_OK) {
                    ouputfotocuatro = data.getData();
                    rotatedBitmap4 = null;
                    try {
                        rotatedBitmap4 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ouputfotocuatro);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*try {
                        ExifInterface ei = new ExifInterface(getPath(ouputfotocuatro));
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap4 = rotateImage(bitmapfoto4, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap4 = rotateImage(bitmapfoto4, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap4 = rotateImage(bitmapfoto4, 270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default: rotatedBitmap4 = bitmapfoto4; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    picture4.setPadding(0,0,0,0);
                    picture4.setImageBitmap(rotatedBitmap4);
                    if (vista_fotos4.getVisibility() == View.VISIBLE) {
                        vista_fotos4.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.VISIBLE);
                    }
                    checkbox3.setChecked(true);
                    if (checkbox.isChecked() & checkbox3.isChecked()) {
                        aceptarReporte.setEnabled(true);
                    }
                }
                break;
            case COD_FOTO_4:
                if (resultCode == RESULT_OK) {
                    bitmapfoto4 = BitmapFactory.decodeFile(mPath4);

                    rotatedBitmap4 = null;
                    try {
                        ExifInterface ei = new ExifInterface(mPath4);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap4 = rotateImage(bitmapfoto4, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap4 = rotateImage(bitmapfoto4, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap4 = rotateImage(bitmapfoto4, 270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default: rotatedBitmap4 = bitmapfoto4; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    picture4.setPadding(0,0,0,0);
                   // picture4.setImageBitmap(bitmapfoto4);
                    picture4.setImageBitmap(rotatedBitmap4);
                    if (vista_fotos4.getVisibility() == View.VISIBLE) {
                        vista_fotos4.setVisibility(View.GONE);
                        vista_categoria.setVisibility(View.VISIBLE);
                    }
                    checkbox3.setChecked(true);
                    if (checkbox.isChecked() & checkbox3.isChecked()) {
                        aceptarReporte.setEnabled(true);
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean mayRequestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(linearLayout, "Los permisos son necesarios para poder usar la aplicacion ",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_RERMISSIONS);
                }
            }).show();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_RERMISSIONS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_RERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permisos aceptados", Toast.LENGTH_SHORT).show();
                picture.setEnabled(true);
            } else {
                showExplanation();
            }
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permiso denegado");
        builder.setMessage("Para usar las fuciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("packeage", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
    }

    //FUNCION PARA CARGAR IMAGENES DE CATEGORIAS
    private void loadImagesCategory() {
        mImagesList.add(R.drawable.otro_o);
        mNames.add("ALCANTARILLA");
        mImagesList.add(R.drawable.bache_o);
        mNames.add("BACHES");
        mImagesList.add(R.drawable.alumbrado_o);
        mNames.add("ALUMBRADO");
        mImagesList.add(R.drawable.otro_o);
        mNames.add("ARBOL CAIDO");
        mImagesList.add(R.drawable.basura_o);
        mNames.add("BASURA");
        mImagesList.add(R.drawable.otro_o);
        mNames.add("FUGA DE AGUA");
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewCategoria);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapterTipo adapter = new RecyclerViewAdapterTipo(getActivity(), mNames, mImagesList, campoTipo, checkbox, checkbox3, aceptarReporte);
        recyclerView.setAdapter(adapter);
    }

    private void CapturaDeMapaStatico(){
        String latitudreporte = campolatitud; //oculto
        String longitudreporte = campolongitud; //oculto
        //URL de mapa estatico
        mapa_estatico_compartir = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitudreporte + "," + longitudreporte + "&zoom=14&size=900x270&scale=2&maptype=terrain&markers=color:0x811BDA%7Clabel:%7C" + latitudreporte + "," + longitudreporte + "&key=AIzaSyDfaDAjV_yzES1oxJiXWaktGuyKTzM8NcE";

        Picasso.with(getActivity())
                .load(mapa_estatico_compartir)
                .into(mapa_compartir);

        Picasso.with(getActivity())
                .load(mapa_estatico_compartir)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mapa_estatico = bitmap; //obtener el bitmap del String

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });

    }
    private void CapturaDeMaps() {
        String latitudreporte = campolatitud; //oculto
        String longitudreporte = campolongitud; //oculto
        //URL de mapa estatico
        mapa_estatico_compartir = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitudreporte + "," + longitudreporte + "&zoom=14&size=900x270&scale=2&maptype=terrain&markers=color:0x811BDA%7Clabel:%7C" + latitudreporte + "," + longitudreporte + "&key=AIzaSyDfaDAjV_yzES1oxJiXWaktGuyKTzM8NcE";

        Picasso.with(getActivity())
                .load(mapa_estatico_compartir)
                .into(mapa_compartir);

        Picasso.with(getActivity())
                .load(mapa_estatico_compartir)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mapa_estatico = bitmap; //obtener el bitmap del String

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()); //Nombre de la imagen guardada

        f= new File(Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                + File.separator + "MAPA_" + timeStamp + ".jpg");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mapa_estatico.compress(Bitmap.CompressFormat.JPEG,100,bytes); //DIBUJO LA IMAGEN

            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", e.getMessage());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getActivity().getPackageName() + ".provider";
                fotoMap = FileProvider.getUriForFile(getActivity(), authorities, f);
            } else {
                fotoMap = Uri.fromFile(f);
            }
    }


}