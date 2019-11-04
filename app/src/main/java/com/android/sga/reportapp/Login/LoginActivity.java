package com.android.sga.reportapp.Login;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sga.reportapp.Additional.user_registration;
import com.android.sga.reportapp.MainActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.gson.apiUsuario;
import com.android.sga.reportapp.gson.datoUsuario;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private LinearLayout l_login_visible, l_login_desplegable;
    Button cancelarCession,confimar,sesion ;
    TextView name_fb,mail_fb, tokensito ,recuperar_contrasena, registrar_usuario;
    EditText celular,mail,password;
    //ImageView Perfil;
    CircleImageView Perfil;
    ProgressDialog progressDialog,progreso;
    private static final int COD_SELECCIONA =10 ;
    Uri ouputfotouno;
    private Bitmap bitmapfoto1;
    String ape="" ,f_nac="",gen="",url_img,tokenFacebook, id_facebok_unic="";
    private boolean valorEstadoLogin;
    public static final String STRING_DATAFB= "DATAFB";
    public static final String DATAFB_IDFB="DATAFB_IDFB";
    public static final String STRING_DATAMAIL= "DATAMAIL";
    public static final String DATAMAIL_IDMAIL="DATAMAIL_IDMAIL";
    Typeface Light , Bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Light = TypefacesUtils.get(this, "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(this, "fonts/MavenPro-Bold.ttf");
        TextView et_is=(TextView)findViewById(R.id.etiqueta_is); et_is.setTypeface(Light);
        //VERIFICARDOR DE SESION CUANDO ES "VERDADERO" SE EJECUTA 22/SEPT/2018, DE LO CONTRARIO SE MUESTRA LA PANTALLA DE "LOGUEO"
        if(MainActivity.recuperar_sesion(LoginActivity.this)){
           // Toast.makeText(LoginActivity.this,"CON ESTADO",Toast.LENGTH_SHORT).show();
            Intent LOGINTRUE= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(LOGINTRUE);
            finish(); //MATAR EL INTENT DESPUES DE EJECUTARSE
        }
        else{
            //Toast.makeText(LoginActivity.this,"SIN ESTADO",Toast.LENGTH_SHORT).show();
        }

       Intent intent = getIntent();
        valorEstadoLogin= intent.getExtras().getBoolean("iniciado"); //SIEMPRE LLEGARA UN FALSE DE MAPSACTIVITY

        mail=(EditText)findViewById(R.id.editText_email); mail.setTypeface(Light);
        password=(EditText)findViewById(R.id.editText_password); password.setTypeface(Light);
        sesion=(Button)findViewById(R.id.boton_ingresar); sesion.setTypeface(Bold);
        recuperar_contrasena=(TextView)findViewById(R.id.tv_recuperar_password); recuperar_contrasena.setTypeface(Bold);
        registrar_usuario=(TextView)findViewById(R.id.texto_registrar_usuario); registrar_usuario.setTypeface(Bold);
        TextView etiqueda_cd=(TextView)findViewById(R.id.etiqueta_cd); etiqueda_cd.setTypeface(Light);
        TextView etiqueta_nombre=(TextView)findViewById(R.id.etiquueta_nombre); etiqueta_nombre.setTypeface(Bold);
        TextView etiqueta_correo=(TextView)findViewById(R.id.etiqueta_correo); etiqueta_correo.setTypeface(Bold);
        TextView etiqueta_celular=(TextView)findViewById(R.id.etiqueta_celular); etiqueta_celular.setTypeface(Bold);

        cancelarCession=(Button)findViewById(R.id.logout_cession);
        confimar=(Button)findViewById(R.id.confimarDatosFB); confimar.setTypeface(Light);
        Perfil=(CircleImageView)findViewById(R.id.perfil_facebook);
        l_login_visible=(LinearLayout)findViewById(R.id.login_contenedor);
        l_login_desplegable=(LinearLayout)findViewById(R.id.login_contenedor_despegable);
        tokensito=(TextView) findViewById(R.id.token);

        name_fb=(TextView)findViewById(R.id.name_facebook); name_fb.setTypeface(Light);
        mail_fb=(TextView)findViewById(R.id.mail_facebook); mail_fb.setTypeface(Light);
        celular=(EditText)findViewById(R.id.celular_facebook); celular.setTypeface(Light);
        callbackManager = CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.loginButton); loginButton.setTypeface(Bold);

        //ASIGNANDOLE PERMISO DE API FB
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_gender"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                progressDialog= new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Obteniendo datos...");
                progressDialog.show();
                GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                       tokenFacebook= AccessToken.getCurrentAccessToken().getToken();
                      tokensito.setText(tokenFacebook);
                        progressDialog.dismiss();
                        Log.d("Response", response.toString());
                       getData(object); //FUNCION PARA OBTENER DATOS DE LA API DE FB

                    }
                });
                Bundle parametros= new Bundle();
                parametros.putString("fields", "id,first_name,last_name,email,birthday,gender");
                request.setParameters(parametros);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),R.string.error_login,Toast.LENGTH_SHORT).show();
            }
        });

        //EVENTO PARA RECUPERAR LOS VALORES DE LA API FB
        loginButton.setOnClickListener(this);

        //EVENTO PARA CANCELAR LA SESSION DE FB ANTES DE ALMACENAR
        cancelarCession.setOnClickListener(this);

        //EVENTO PARA ALMACENAR LOS DATOS EN LA BD
        confimar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDatosFacebook(); //FUNCION PARA ALMACENAR DATOS EXTRAIDO DE LA API FACEBOOK
                gravarIdFacebook(LoginActivity.this,id_facebok_unic); //FUNCION PARA ALMACENAR ID FACEBOOK EXTRAIDO DE LA API FACEBOOKS
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //EVENTO PARA SUBIR DE FOTO DE PERFIL
        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Cambiar perfil", "Cancelar"};
                final AlertDialog.Builder builder =new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Elige un opcion:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                            if (options[seleccion] == "Cambiar perfil") {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), COD_SELECCIONA);
                            } else if (options[seleccion] == "Cancelar") { dialog.dismiss(); }

                    }
                });
                builder.show();
            }
        });

        //EVENTO PARA LOGUEO O INICIAR SESSION CON CORREO Y PASSWORD
        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valorEstadoLogin=true; //VALOR TRUE QUE SE GUARDARA
                iniciar_sesion();//FUNCION VERIRIFICAR DATOS CORRECTOS Y LOGUEAR DE LA APP
            }
        });

    }

    //FUNCION PARA ALMACENAR EL ID FACEBOOK
    public static void gravarIdFacebook(Context u, String id_fb){
        SharedPreferences preferences=u.getSharedPreferences(STRING_DATAFB,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(DATAFB_IDFB,id_fb);
        editor.commit();
    }
    //FUNCION PARA ALMACENAR EL ID USUARIO POR CORREO
    public static void gravarIdUsuarioMail(Context u, String id_uc){
        SharedPreferences preferences=u.getSharedPreferences(STRING_DATAMAIL,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(DATAMAIL_IDMAIL,id_uc);
        editor.commit();
    }
    //FUNCION PARA VERIFICAR CORREO Y CONTRASEÑA E INICIO DE SESSION EN LA APP
    public void iniciar_sesion(){
        String correo=mail.getText().toString();
        String contrasena=password.getText().toString();

        apiService service=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("correo",correo);
        map.put("password",contrasena);

        Call<apiUsuario>apiUsuarioCall=service.obtenerDatosUsuario(map);
        apiUsuarioCall.enqueue(new Callback<apiUsuario>() {
            @Override
            public void onResponse(Call<apiUsuario> call, Response<apiUsuario> response) {
                String idUsuario="", sesionUsuario="";
                apiUsuario usuario=response.body();
                ArrayList<datoUsuario> datoUsuarioArrayList=usuario.getResults();
                for (int i=0;i<datoUsuarioArrayList.size();i++){
                    datoUsuario p=datoUsuarioArrayList.get(i);
                    idUsuario=p.getId_usuario();
                    sesionUsuario=p.getSesion();
                    if(idUsuario.equals("2")&&sesionUsuario.equals("NO")){
                        Toast.makeText(LoginActivity.this, "Error de password",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(idUsuario.equals("1")&&sesionUsuario.equals("NO")) {
                            Toast.makeText(LoginActivity.this, "Error de correo",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (sesionUsuario.equals("YES"))
                           // Toast.makeText(LoginActivity.this, "correo y contraseña son correctas",Toast.LENGTH_SHORT).show();
                            MainActivity.guardar_sesion(LoginActivity.this, valorEstadoLogin);//GUARDO LA SESSION "TRUE"
                            gravarIdUsuarioMail(LoginActivity.this,idUsuario ); //GUARDO EN CACHE LA ID USUARIO DE CORREO
                            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("id_usuario", idUsuario); //ENVIO ID USUARIO
                            intent.putExtra("sesion",sesionUsuario); //ENVIO SESSIION USUARIO : YES
                            intent.putExtra("esActivado",valorEstadoLogin); //ENVIO SESSION DE PROVENIENTE DE MAPS :TRUE
                            startActivity(intent);
                            finish();
                        }
                     }
                }
            }
            @Override
            public void onFailure(Call<apiUsuario> call, Throwable t) {
            }
        });

    }

    //FUNCION PAR EXTRAER LOS DATOS DE LA API FACEBOOK
    private void getData(JSONObject object) {
        try {
            URL profile_picture= new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=500&height=500");
            Picasso.with(this).load(profile_picture.toString()).into(Perfil);
            url_img=profile_picture.toString();
            id_facebok_unic=object.getString("id");//GUARDAR EN CACHE?
            name_fb.setText(object.getString("first_name"));
            ape=object.getString("last_name");
            mail_fb.setText(object.getString("email"));
            f_nac=object.get("birthday").toString();
            gen=object.getString("gender");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }


    //EVENTO DE LOGUEO O CANCELACION
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                if(l_login_desplegable.getVisibility()==View.GONE){
                    l_login_desplegable.setVisibility(View.VISIBLE);
                    l_login_visible.setVisibility(View.GONE);
                }
                break;
            case R.id.logout_cession:
                LoginManager.getInstance().logOut();
                Perfil.setImageBitmap(null);
                if(l_login_visible.getVisibility()==View.GONE){
                    l_login_visible.setVisibility(View.VISIBLE);
                    l_login_desplegable.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    //ALMACENAMIENTO DE DATOS OBTENIDOS DE LA API FB
    public void insertarDatosFacebook(){
        progreso=new ProgressDialog(LoginActivity.this);
        progreso.setMessage("Guardando datos...");
        progreso.show();
        apiService service=apiService.retrofi.create(apiService.class);
       // String tukeiko=tokensito.getText().toString();
       // String id_facebook=id_fb.getText().toString();// enviar en la base de datos
        String nombre=name_fb.getText().toString();  //enviar en la la base de datos
        String apellido=ape;  //enviar en la base de datos
        String correo=mail_fb.getText().toString(); //enviar en la base de datos
        String telefono=celular.getText().toString(); //enviar en la base datos
        String f_nacimiento=f_nac; //enviar en la base datos
        String genero=gen; //enviar en la base datos
        String url_imagen=url_img; //enviar en la base datos
        String tipo="Facebook";
       // RequestBody tokenfacebook_body=RequestBody.create(MultipartBody.FORM, tukeiko);
        RequestBody id_facebook_body=RequestBody.create(MultipartBody.FORM, id_facebok_unic);
        RequestBody nombre_body=RequestBody.create(MultipartBody.FORM, nombre);
        RequestBody apellido_body=RequestBody.create(MultipartBody.FORM, apellido);
        RequestBody correo_body=RequestBody.create(MultipartBody.FORM, correo);
        RequestBody telefono_body=RequestBody.create(MultipartBody.FORM, telefono);
        RequestBody f_nacimientobody=RequestBody.create(MultipartBody.FORM, f_nacimiento);
        RequestBody genero_body=RequestBody.create(MultipartBody.FORM, genero);
        RequestBody url_imagen_body=RequestBody.create(MultipartBody.FORM,url_imagen );
        RequestBody tipo_body=RequestBody.create(MultipartBody.FORM,tipo );
        final Call<ResponseBody> call=service.insertaDatosFacebook(
               // tokenfacebook_body,
                id_facebook_body,
                nombre_body,
                apellido_body,
                correo_body,
                f_nacimientobody,
                genero_body,
                url_imagen_body,
                tipo_body
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progreso.hide();
               // Toast.makeText(LoginActivity.this, "Datos guardados",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("error",t.toString());
                progreso.hide();
                //Toast.makeText(LoginActivity.this, "Fallo al guardar datos",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //EVENTO DE DESPLIEGUE DEL FORMULARIO DE REGISTRO CON CORREO Y DATOS
    public void evento_registrar(View view) {
        Intent intent= new Intent(this, user_registration.class);
        startActivity(intent);
    }
}
