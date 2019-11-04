package com.android.sga.reportapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sga.reportapp.Fragments.AccountFragment;
import com.android.sga.reportapp.Fragments.ConfigurationFragment;
import com.android.sga.reportapp.Fragments.FormularyFragment;
import com.android.sga.reportapp.Fragments.MapFragment;
import com.android.sga.reportapp.Fragments.MyReportFragment;
import com.android.sga.reportapp.Fragments.NotificationFragment;
import com.android.sga.reportapp.Fragments.ReportFragment;
import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.gson.apiFacebook;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.gson.datoFacebook;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bolts.Bolts;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView=null;
    public static DrawerLayout drawerLayout=null;
    public static final String STRING_PREFERENCES= "aplicacionreportapp", ESTADO_SESSION= "estado.sesion";;
    private boolean estadoInicial;//DEFAULT : FALSE
    private String user_logueado="", usuariologueado="";//DEFAULT : " VACIO"
    String idfbrecuperado="" ,  contartReportes;
    String idusermailrecuperado="";
    int count=0;
    View hView ,hItem0;
    public static TextView nombrecompleto;
    public static TextView count_reports;
    public static String contadorReportes;
    public static CircleImageView imgfb2;
    CircleImageView editarusario ;
    Button ocultar;
    private static final String TAG = "TOKEN";
    private static final String STRING_SESION= "SESSION", ESTADO_SESION="ESTADO.SESION";
    public static final String STRING_DATACOUNT= "DATACOUNT", DATACOUNT_IDCOUNT="DATACOUNT_IDCOUNT";
    public static final String STRING_DATAUSER= "DATAUSER", DATAUSER_NAMEUSER="DATAUSER_NAMEUSER", DATAUSER_LASTENAMEUSER="DATAUSER_LASTNAMEUSER", DATAUSER_EMAILUSER="DATAUSER_EMAILUSER",DATAUSER_BIRTHDAYUSER="DATAUSER_BIRTHDAYUSER", DATAUSER_PHONEUSER="DATAUSER_PHONEUSER";
    ConnectivityManager cm;
    NetworkInfo ni;
    boolean tipoConexion1=false, tipoConexion2=false;
    Typeface Light , Bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Light = TypefacesUtils.get(this, "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(this, "fonts/MavenPro-Bold.ttf");
        /*CONSEGUIR EL ANCHO Y ALTO DE LA TODA LA PANTALLA*/
        Display display=getWindowManager().getDefaultDisplay();
        int ancho=display.getWidth();
        int alto=display.getHeight();
        /*---------------*/
        navigationView= (NavigationView)findViewById(R.id.navigation_view);
        navigationView.getLayoutParams().height=alto; //LE ASIGNAMOS EL TOTAL ALTO AL NAVIGARTIONVIEW
        navigationView.getLayoutParams().width=ancho; //LE ASIGNAMOS EL TOTAL ANCHO AL NAVIGARTIONVIEW

        count_reports=(TextView)MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.drawermisreportes));
        count_reports.setTypeface(Bold);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        hView = navigationView.getHeaderView(0);
        nombrecompleto = (TextView) hView.findViewById(R.id.name_facebook);
        nombrecompleto.setTypeface(Bold); //fuente nuevo
        cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        ni=cm.getActiveNetworkInfo();
        if(ni!=null) {
            ConnectivityManager connectivityManager1=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi=connectivityManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            ConnectivityManager connectivityManager2=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobile=connectivityManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mWifi.isConnected()){
                tipoConexion1=true;
            }
            if (mMobile.isConnected() && mMobile.isAvailable()){
                tipoConexion2=true;
            }
            if (tipoConexion1==true || tipoConexion1==true){
                //Cargar Datos , almacenar cache, consultar
                /*RECUPERANDO ID DE USUARIO (FACEBOOK O CORREO)*/
                recuperarIdFacebook();
                recuperarIdUserMail();
                if (idfbrecuperado.length()==0){
                    user_logueado=idusermailrecuperado;
                    obtenerInfoUsuario();
                }
                else {
                    user_logueado=idfbrecuperado;
                    obtenerDatosFacebook();

                }

            }else {
                Toast.makeText(this,"Sin datos", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this,"Sin conexión", Toast.LENGTH_LONG).show();
            recuperarUsuarioCacheNameLastName(this);
        }

        recuperarUsuarioCacheNameLastName(this);


        /*CARGA EL FRAGMENTO DE MAPA*/
        MapFragment mapFragment =new MapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.commit();
        /*---------------------*/

        imgfb2 =(CircleImageView)hView.findViewById(R.id.perfil_facebook2);
        editarusario =(CircleImageView)hView.findViewById(R.id.editarUsuario);
        ocultar=(Button) hView.findViewById(R.id.ocultarmenu);
        ocultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { /*CIERRA EL DRAWER*/ closeDrawer(); }
        });
        editarusario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CARGAL EL FRAGMENTO DE CONFIGURACION USUARIO*/
                AccountFragment accountFragment = new AccountFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, accountFragment);
                fragmentTransaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        imgfb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CARGAL EL FRAGMENTO DE CONFIGURACION USUARIO*/
                AccountFragment accountFragment = new AccountFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, accountFragment);
                fragmentTransaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);//quitar la tinta
       //  KeyHashes();

        //A) INICIO CON DATOS "NULL"
        //B)    RECUPERO DATOS DEL LOGIN :TRUE
        Intent intent = getIntent();
        String Mi_id_reporte = intent.getExtras().getString("id_usuario"); //NULL O ID_USUARIO
        String Mi_sesion = intent.getExtras().getString("sesion"); //NULL O YES
        estadoInicial= intent.getExtras().getBoolean("esActivado");//NULL O TRUE
        //EN LA FUNCION SI ES :FALSE , DE LO CONTRARIO : TRUE Y GUARDA LA SESION
        if(!recuperar_sesion(MainActivity.this)){
            //SI NO EXISTE ALGUNA SESSION SE EJECUTA LA CONDICIONAL
            if (Mi_sesion==null && AccessToken.getCurrentAccessToken()==null){
                //ASIGNANDOLE UN VALOR VERDADERO Y GUARDA EL NUEVO VALOR
                estadoInicial=false;
                guardar_sesion(MainActivity.this, estadoInicial);//YA ESTA GUARDADA LA SESSION ANTES DE IR AL LOGIN : TRUE
                Intent intentP = new Intent(this, LoginActivity.class);
                intentP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intentP.putExtra("iniciado", estadoInicial);//ENVIO EL VALOR DE LA SESSION GUARDADA EN LOGIN .TRUE
                startActivity(intentP);
                finish();
            }
            ValidandoID();
        }
        else{
            if(Mi_id_reporte!=null){
                user_logueado=Mi_id_reporte;// ID_USUARIO A GUARDAR
                // guardar_infoSesion();// SE GUARDA EL ID USUARIO EN LA VARIABLE Y YA EXISTE EL LOGUEO
                grabar_usuario(MainActivity.this,user_logueado);
                ValidandoID(); //FUNCION DE PABLO NO SE QUE HACE *****
            }
            else{
                ValidandoID();
            }
        }

    }


    public void ValidandoID() {
          final String token = FirebaseInstanceId.getInstance().getToken();
        recuperarIdFacebook();
        String id_facebook = idfbrecuperado;
        recuperarIdUserMail();
        String id_cuenta = idusermailrecuperado;
        if ((id_facebook.isEmpty()) || (id_facebook == "")) {
            if (id_cuenta != null) {
                  EnviarloServer(id_cuenta, token);
                //   gravartokenid(this,token,"");
            }
        } else {
             EnviarloServer(id_facebook, token);
            // gravartokenid(this,"",token);
        }
    }

    public  void EnviarloServer(String id_usuario,String token) {
        apiService service = apiService.retrofi.create(apiService.class);
        Map<String, Object> map = new HashMap<>();
        map.put("id_usuario", id_usuario);
        map.put("token", token);
        Call<ResponseBody> apirespuestaCall = service.sendmessage(map);
        apirespuestaCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id= menuItem.getItemId();
        if (id==R.id.drawerreportes){
            /*CARGA EL FRAGMENTO REPORTES TOTALES*/
            ReportFragment reportFragment= new ReportFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, reportFragment);
            fragmentTransaction.commit();
        }else if (id==R.id.drawermisreportes){
            /*CARGA EL FRAGMENTO MIS REPORTES*/
            MyReportFragment myReportFragment = new MyReportFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, myReportFragment);
            fragmentTransaction.commit();
        }else if(id==R.id.drawernotificacion){
            /*CARGAR EL FRAGMENTO NOTIFICACION*/
            NotificationFragment notificationFragment=new NotificationFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, notificationFragment);
            fragmentTransaction.commit();
        }
        else if(id==R.id.drawerconfiguracion){
            /*CARGAR EL FRAGMENTO CONFIGURACION*/
            ConfigurationFragment configurationFragment=new ConfigurationFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, configurationFragment);
            fragmentTransaction.commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
     *
     * CACHE -PREFERENCES
     *
     * */
    //FUNCION PARA ALMACENAR VALOR DE SESSION : TRUE O FALSE
    public static void guardar_sesion(Context g, boolean e){
        SharedPreferences preferences=g.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(ESTADO_SESSION,e).apply();
    }
    //FUNCION PARA RECUPERAR SESSION :TRUE O FALSE
    public static boolean recuperar_sesion(Context c){
        SharedPreferences preferences=c.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getBoolean(ESTADO_SESSION,false);
    }
    //FUNCION PARA ALMACENAR NOMBRE DE USUARIO
    public static void grabar_usuario(Context u,String id_usuario){
        SharedPreferences preferences=u.getSharedPreferences(STRING_SESION,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(ESTADO_SESION,id_usuario);
        editor.commit();
    }

    //FUNCION DE RECUPERAR ID FB
    public void recuperarIdFacebook(){
        SharedPreferences preferences=getSharedPreferences(LoginActivity.STRING_DATAFB, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAFB_IDFB, "");
        idfbrecuperado=d;
    }
    //FUNCION DE RECUPERAR UD USER DE CORREO
    public void recuperarIdUserMail(){
        SharedPreferences preferences=getSharedPreferences(LoginActivity.STRING_DATAMAIL, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAMAIL_IDMAIL, "");
        idusermailrecuperado=d;
    }

    //FUNCION PARA CAMBIAR VALOR DE SESSION : TRUE O FALSE
    public static void cambiarEstadoSession(Context c,boolean changed){
        SharedPreferences preferences=c.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(ESTADO_SESSION,changed).apply();
    }
    //FUNCION DE RECUPERAR CONTADOR
    public static void recuperarcontador2(Context c){
        SharedPreferences preferences=c.getSharedPreferences(STRING_DATACOUNT, MODE_PRIVATE);
        String contador=preferences.getString(DATACOUNT_IDCOUNT, "");
        contadorReportes=contador;
        count_reports.setText(contadorReportes);
        count_reports.setGravity(Gravity.CENTER);
        //count_reports.setTypeface(null,Typeface.BOLD);
        count_reports.setTextColor(c.getResources().getColor(R.color.purple));
    }
    //FUNCION PARA ALMACENAR CONTADOR REPORTES
    public static void gravarContador(Context u, String contador){
        SharedPreferences preferences=u.getSharedPreferences(STRING_DATACOUNT,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(DATACOUNT_IDCOUNT, contador);
        editor.commit();
    }

    //FUNCION QUE PERMITE GRAVAR DATOS DEL USUARIO INICIANDO LA APP
    public void gravarUsuarioCache(String nombre, String apellido, String correo, String nacimiento, String celular){
        SharedPreferences preferences=getSharedPreferences(STRING_DATAUSER,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(MainActivity.DATAUSER_NAMEUSER, nombre);
        editor.putString(MainActivity.DATAUSER_LASTENAMEUSER, apellido);
        editor.putString(MainActivity.DATAUSER_EMAILUSER, correo);
        editor.putString(MainActivity.DATAUSER_BIRTHDAYUSER, nacimiento);
        editor.putString(MainActivity.DATAUSER_PHONEUSER, celular);
        editor.commit();
    }
    public static void recuperarUsuarioCacheNameLastName(Context c){
        SharedPreferences preferences=c.getSharedPreferences(STRING_DATAUSER, MODE_PRIVATE);
        String nombre=preferences.getString(DATAUSER_NAMEUSER, "");
        String apellido=preferences.getString(DATAUSER_LASTENAMEUSER, "");
        nombrecompleto.setText(nombre+" "+apellido);
    }
    /*
    *
    * FUNCIONES DE DRAWERS Y CAMBIOS DE FRAGMENTOS
    *
    * */
    //ABRE EL DRAWER
    public void openDrawer(){ drawerLayout.openDrawer(Gravity.START); }
    //CIERRA EL DRAWER
    public static void closeDrawer(){ drawerLayout.closeDrawer(GravityCompat.START); }

    //ABRE EL FRAGMENTO FORMULARIO
    public void opeFragmentFormulary (){
        FormularyFragment formularyFragment= new FormularyFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, formularyFragment);
        fragmentTransaction.commit();
    }
    //ABRE EL FRAGMENTO MAPA
    public void opeFragmentMap (){
        MapFragment mapFragment= new MapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.commit();
    }
    public void opeFragmentMapTotal (){
        MapFragment mapFragment= new MapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.commit();
        drawerLayout.openDrawer(Gravity.START);
    }
    //ABRBE EL FRAGMENTO MIS REPORTES
    public void opeFragmentMisReportes (){
        MyReportFragment misreportFragment= new MyReportFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, misreportFragment);
        fragmentTransaction.commit();
    }
    /*
    * OBTENCION DE KEYHASEH PARA REGISTRO EN FB -LOGIN FB
    *
    * */
    //Con este metodo se obtiene el KEYHASHES
    public String KeyHashes(){
        PackageInfo info;
        String KeyHashes=null;
        try{
            info=getPackageManager().getPackageInfo("com.android.sga.reportapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                KeyHashes=new String(Base64.encode(md.digest(),0));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return KeyHashes;
    }
    /*
    *
    * WEB SERVICE - RETROFIT
    *
    * */
    //FUNCION DE OBTENER DATOS DE USUARIO CON FACEBOOK ALMACENADO
    private void obtenerDatosFacebook(){
        recuperarIdFacebook();
        String idfbccount=idfbrecuperado ;
        apiService service=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("id_usuario",idfbccount);
        Call<apiFacebook> apiFacebookCall=service.obtenerDatosFacebookId(map);
        apiFacebookCall.enqueue(new Callback<apiFacebook>() {
            @Override
            public void onResponse(Call<apiFacebook> call, Response<apiFacebook> response) {

                if(response.isSuccessful()) {
                    String nombre = "", apellido = "", correo = "", nacimiento = "", telefono = "", genero = "", imge = "";
                    apiFacebook apifacebook = response.body();
                    ArrayList<datoFacebook> ListaDatos = apifacebook.getResults();
                    for (int i = 0; i < ListaDatos.size(); i++) {
                        datoFacebook p = ListaDatos.get(i);
                        imge = p.getFoto();
                        Glide.with(getApplicationContext()).load(imge).into(imgfb2);
                        nombre = p.getNombre();
                        apellido = p.getApellido();
                        correo = p.getCorreo();
                        nacimiento = p.getCumpleanos();
                        telefono = p.getTelefono();
                        gravarUsuarioCache(nombre, apellido, correo, nacimiento, telefono);
                        recuperarUsuarioCacheNameLastName(getApplicationContext());
                    }
                }else {
                    Log.e(TAG,"onResponse:"+response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<apiFacebook> call, Throwable t) {
                Log.d("error",t.toString());
            }
        });
    }
    //FUNCION PARA OBTENER DATOS DE USUARIO LOGUEADO CON CORREO
    public void obtenerInfoUsuario(){
        recuperarIdUserMail();//1719239173
        String usuario_logueado=idusermailrecuperado;
        apiService service=apiService.retrofi.create(apiService.class);
        final Map<String,Object> map=new HashMap<>();
        map.put("id_usuario",usuario_logueado);
        Call<apiFacebook> apiFacebookCall=service.obtenerDatoUsuario(map);
        apiFacebookCall.enqueue(new Callback<apiFacebook>() {
            @Override
            public void onResponse(Call<apiFacebook> call, Response<apiFacebook> response) {
                if(response.isSuccessful()) {
                String image="", imageDegradado="", nombre="",apellido="",correo="", nacimiento="", telefono="", genero="", imge="";
                apiFacebook apifacebook=response.body();
                ArrayList<datoFacebook>ListaDatos=apifacebook.getResults();
                for (int i=0;i<ListaDatos.size();i++){
                    datoFacebook p=ListaDatos.get(i);
                    image=p.getFoto();
                    Glide.with(getApplicationContext()).load(image).into(imgfb2);
                    nombre=p.getNombre(); //---- importante
                    apellido=p.getApellido();
                    correo=p.getCorreo();
                    nacimiento=p.getCumpleanos();
                    telefono=p.getTelefono();
                    gravarUsuarioCache(nombre,apellido,correo,nacimiento,telefono);
                    recuperarUsuarioCacheNameLastName(getApplicationContext());
                }
                }
                 else {
                    Log.e(TAG,"onResponse:"+response.errorBody());
                }

            }
            @Override
            public void onFailure(Call<apiFacebook> call, Throwable t) {

            }
        });
    }
    public static void perfil(Context c, String imge){
        Glide.with(c.getApplicationContext()).load(imge).into(imgfb2);
    }
}