package com.android.sga.reportapp.Fragments;


import android.app.DatePickerDialog;
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
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sga.reportapp.Additional.BlurImage;
import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.MainActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.gson.apiFacebook;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.gson.apiUpdateUser;
import com.android.sga.reportapp.gson.datoFacebook;
import com.android.sga.reportapp.gson.datoUpdateUser;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    View rootView;
    Button useratras, userlisto;
    ImageView img_degradado;
    CircleImageView img, editarfoto;
    ProgressDialog progreso;
    String idfbcuenta="", idusermailcuenta="";
    TextView mail_fb;
    EditText nombreFacebook, lastnameuser,nacimiento_fb, celular_facebook;
    private int BLUR_PRECENTAGE = 70;
    Target target;
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//diretorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta de directorio
    String imageFileName="", mPath="", fnacimiento="";
    File newFile;
    Intent intent_fotouno;
    Uri ouputfotouno;
    private Bitmap bitmapfoto1;
    private static final int COD_FOTO = 20;
    private static final int COD_SELECCIONA = 10;
    LinearLayout contenedorPrincipal;
    private final int MY_RERMISSIONS = 100;
    Drawable casillasTxt;
    private int dia, mes,ano;
    Calendar calendarioHoy;
    private static final String TAG = "LOGREPORTAPP";
    ConnectivityManager cm;
    NetworkInfo ni;
    boolean tipoConexion1=false, tipoConexion2=false;
    Typeface Light , Bold;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_account,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Light = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Bold.ttf");
        contenedorPrincipal=(LinearLayout)rootView.findViewById(R.id.layoudcuenta);
        useratras = (Button)rootView.findViewById(R.id.useratras);
        userlisto = (Button)rootView.findViewById(R.id.userlisto);
        img=(CircleImageView)rootView.findViewById(R.id.perfil_facebook);
        editarfoto=(CircleImageView)rootView.findViewById(R.id.editarFoto);
        img_degradado=(ImageView) rootView.findViewById(R.id.imagen_degradado);
        TextView etiqueta_c=(TextView)rootView.findViewById(R.id.etiqueta_cuenta);  etiqueta_c.setTypeface(Light);
        TextView etiqueta_n=(TextView)rootView.findViewById(R.id.etiqueta_nombre);  etiqueta_n.setTypeface(Bold);
        nombreFacebook=(EditText)rootView.findViewById(R.id.name_facebook); nombreFacebook.setTypeface(Light);
        TextView etiqueta_a=(TextView)rootView.findViewById(R.id.etiqueta_apellido);  etiqueta_a.setTypeface(Bold);
        lastnameuser=(EditText)rootView.findViewById(R.id.lastname_facebook); lastnameuser.setTypeface(Light);
        TextView etiqueta_m=(TextView)rootView.findViewById(R.id.etiqueta_correo);  etiqueta_m.setTypeface(Bold);
        mail_fb=(TextView)rootView.findViewById(R.id.mail_facebook); mail_fb.setTypeface(Light);
        TextView etiqueta_na=(TextView)rootView.findViewById(R.id.etiqueta_nacimiento);  etiqueta_na.setTypeface(Bold);
        nacimiento_fb=(EditText)rootView.findViewById(R.id.fnacimiento_facebook); nacimiento_fb.setTypeface(Light);
        TextView etiqueta_ce=(TextView)rootView.findViewById(R.id.etiqueta_celular);  etiqueta_ce.setTypeface(Bold);
        celular_facebook=(EditText)rootView.findViewById(R.id.celular_facebook); celular_facebook.setTypeface(Light);
        casillasTxt=getActivity().getResources().getDrawable(R.drawable.vista_bordersinfondo);

        calendarioHoy= Calendar.getInstance();
        ano=calendarioHoy.get(Calendar.YEAR);
        mes=calendarioHoy.get(Calendar.MONTH);
        dia=calendarioHoy.get(Calendar.DAY_OF_MONTH);

        nacimiento_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();

            }
        });

        cm=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        ni=cm.getActiveNetworkInfo();
        if(ni!=null) {
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

                recuperarIdFacebook();
                recuperarIdUserMail();
                if (idfbcuenta.length()==0){
                    nombreFacebook.setEnabled(true);
                    nombreFacebook.setBackground(casillasTxt);
                    lastnameuser.setEnabled(true);
                    lastnameuser.setBackground(casillasTxt);
                    nacimiento_fb.setEnabled(true);
                    nacimiento_fb.setBackground(casillasTxt);
                    editarfoto.setVisibility(View.VISIBLE);
                    obtenerInfoUsuario(); //Solo para FOTO de usuario
                    recuperarUsuarioCache(); //Almacenados en cache

                }
                else {
                    obtenerDatosFacebook();
                    recuperarUsuarioCache();
                }

            }
            else {
                Toast.makeText(getActivity(),"Sin datos", Toast.LENGTH_LONG).show();
                recuperarIdFacebook();
                recuperarIdUserMail();
                if (idfbcuenta.length()==0){
                    nombreFacebook.setEnabled(true);
                    nombreFacebook.setBackground(casillasTxt);
                    lastnameuser.setEnabled(true);
                    lastnameuser.setBackground(casillasTxt);
                    nacimiento_fb.setEnabled(true);
                    nacimiento_fb.setBackground(casillasTxt);
                    editarfoto.setVisibility(View.VISIBLE);
                    recuperarUsuarioCache(); //Almacenados en cache
                }
                else {
                    recuperarUsuarioCache();
                }
            }
        }
        else {
            Toast.makeText(getActivity(),"Sin conexión", Toast.LENGTH_LONG).show();
            recuperarIdFacebook();
            recuperarIdUserMail();
            if (idfbcuenta.length()==0){
                nombreFacebook.setEnabled(true);
                nombreFacebook.setBackground(casillasTxt);
                lastnameuser.setEnabled(true);
                lastnameuser.setBackground(casillasTxt);
                nacimiento_fb.setEnabled(true);
                nacimiento_fb.setBackground(casillasTxt);
                editarfoto.setVisibility(View.VISIBLE);
                recuperarUsuarioCache(); //Almacenados en cache
            }
            else {
                recuperarUsuarioCache();
            }
        }

        useratras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).opeFragmentMapTotal();
            }
        });
        userlisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        recuperarIdFacebook();
                        if(idfbcuenta.length()==0){
                            actualizarDatosUsuario();
                            obtenerInfoUsuario();
                        }else {
                            actualizarDatosFacebook();
                            obtenerDatosFacebook();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(),"Sin datos, no se pudo actualizar el usuario", Toast.LENGTH_LONG).show();
                       }
                }
                else {
                    Toast.makeText(getActivity(),"Sin conexión, no se pudo actualizar el usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
        editarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Tomar foto", "Elegir de Galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Elige un opcion:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if (options[seleccion] == "Tomar foto") {
                            abrirCamara();
                        } else {
                            if (options[seleccion] == "Elegir de Galeria") {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), COD_SELECCIONA);
                            } else if (options[seleccion] == "Cancelar") {
                                dialog.dismiss();
                            }
                        }
                    }
                });
                builder.show();

            }
        });

        if (mayRequestStoragePermission()) {
            editarfoto.setEnabled(true);
        } else {
            editarfoto.setEnabled(false);
        }

        return rootView;
    }

    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mesActual=month+1;
                String diaFormateado=(dayOfMonth<10)?"0"+String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado=(mesActual<10)?"0"+String.valueOf(mesActual):String.valueOf(mesActual);
                String anoFormateado=(year<10)?"0"+String.valueOf(year):String.valueOf(year);
                //nacimiento_fb.setText(month+"/"+diaFormateado+"/"+year);
                fnacimiento=mesFormateado+"/"+diaFormateado+"/"+year;
                String fechaRecibida=diaFormateado+mesFormateado+anoFormateado;
                try {
                    Date fechaConcreta= new SimpleDateFormat("ddMMyyyy").parse(fechaRecibida);
                    String fechaNueva= new SimpleDateFormat("dd'/'MMM'/'yyyy").format(fechaConcreta);
                    nacimiento_fb.setText(fechaNueva);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },ano, mes,dia);
        recogerFecha.show();

    }

    public void recuperarIdFacebook(){
        SharedPreferences preferences=getActivity().getSharedPreferences(LoginActivity.STRING_DATAFB, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAFB_IDFB, "");
        idfbcuenta=d;//098203010380939101
    }

    public void recuperarIdUserMail() {
        SharedPreferences preferences = getActivity().getSharedPreferences(LoginActivity.STRING_DATAMAIL, MODE_PRIVATE);
        String d = preferences.getString(LoginActivity.DATAMAIL_IDMAIL, "");
        idusermailcuenta = d;
    }

    private void obtenerDatosFacebook(){
        recuperarIdFacebook();
        String idfbccount=idfbcuenta ;
        apiService service=apiService.retrofi.create(apiService.class);
        Map<String,Object> map=new HashMap<>();
        map.put("id_usuario",idfbccount);
        Call<apiFacebook> apiFacebookCall=service.obtenerDatosFacebookId(map);
        apiFacebookCall.enqueue(new Callback<apiFacebook>() {
            @Override
            public void onResponse(Call<apiFacebook> call, Response<apiFacebook> response) {
                String nacimiento="", imge="";
                apiFacebook apifacebook=response.body();
                ArrayList<datoFacebook> ListaDatos=apifacebook.getResults();
                for (int i=0;i<ListaDatos.size();i++){
                    datoFacebook p=ListaDatos.get(i);
                    imge=p.getFoto();
                    //CARGA DE PERFIL
                    Picasso.with(getApplicationContext()).load(imge).resize(500,500).into(img);
                    //CARGA DE FONDO DEGRADADO O EFECTO GAUSSIANO
                    target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            img_degradado.setImageBitmap(BlurImage.fastblur(bitmap, 1f, BLUR_PRECENTAGE));
                        }
                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            img_degradado.setImageResource(R.drawable.ic_camara);
                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    };
                    Picasso.with(getApplicationContext())
                            .load(imge)
                            .error(R.drawable.ic_camara)
                            .placeholder(R.drawable.ic_camara)
                            .resize(img_degradado.getWidth(),img_degradado.getHeight())
                            .into(target);
                    img_degradado.setTag(target);

                    nacimiento=p.getCumpleanos();
                    fnacimiento=nacimiento;
                    String fechaRecibida=fnacimiento;
                    try {
                        Date fechaConcreta= new SimpleDateFormat("dd'/'MMM'/'yyyy").parse(fechaRecibida);
                        String fechaNueva= new SimpleDateFormat("MM'/'dd'/'yyyy").format(fechaConcreta);
                        fnacimiento=fechaNueva;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    nacimiento_fb.setText(nacimiento);
                }
            }
            @Override
            public void onFailure(Call<apiFacebook> call, Throwable t) {
                Log.d("error",t.toString());
            }
        });

    }

      public void obtenerInfoUsuario(){
        recuperarIdUserMail();//1719239173
        String usuario_logueado=idusermailcuenta;
        apiService service=apiService.retrofi.create(apiService.class);
        final Map<String,Object> map=new HashMap<>();
        map.put("id_usuario",usuario_logueado);
        Call<apiFacebook> apiFacebookCall=service.obtenerDatoUsuario(map);
        apiFacebookCall.enqueue(new Callback<apiFacebook>() {
            @Override
            public void onResponse(Call<apiFacebook> call, Response<apiFacebook> response) {
                if(response.isSuccessful()){
                String image="", nacimiento="";
                apiFacebook apifacebook=response.body();
                ArrayList<datoFacebook>ListaDatos=apifacebook.getResults();
                for (int i=0;i<ListaDatos.size();i++){
                    datoFacebook p=ListaDatos.get(i);
                    image=p.getFoto();
                    //CARGA DE PERFIL
                    Picasso.with(getApplicationContext()).load(image).resize(500,500).into(img);
                    //CARGA DE FONDO DEGRADADO O EFECTO GAUSSIANO
                    target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            img_degradado.setImageBitmap(BlurImage.fastblur(bitmap, 1f, BLUR_PRECENTAGE));
                        }
                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            img_degradado.setImageResource(R.drawable.ic_camara);
                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    };
                    Picasso.with(getApplicationContext())
                            .load(image)
                            .error(R.drawable.ic_camara)
                            .resize(img_degradado.getWidth(),img_degradado.getHeight())
                            .placeholder(R.drawable.ic_camara)
                            .into(target);
                    img_degradado.setTag(target);

                    nacimiento=p.getCumpleanos();
                    fnacimiento=nacimiento;
                    String fechaRecibida=fnacimiento;
                    try {
                        Date fechaConcreta= new SimpleDateFormat("dd'/'MMM'/'yyyy").parse(fechaRecibida);
                        String fechaNueva= new SimpleDateFormat("MM'/'dd'/'yyyy").format(fechaConcreta);
                        fnacimiento=fechaNueva;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    nacimiento_fb.setText(nacimiento);
                }
            }
                else{
                    Log.e(TAG,"onResponse:"+response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<apiFacebook> call, Throwable t) {

            }
        });
    }
    public void recuperarUsuarioCache(){
        SharedPreferences preferences=getActivity().getSharedPreferences(MainActivity.STRING_DATAUSER, MODE_PRIVATE);
        String nombre=preferences.getString(MainActivity.DATAUSER_NAMEUSER, "");
        String apellido=preferences.getString(MainActivity.DATAUSER_LASTENAMEUSER, "");
        String correo=preferences.getString(MainActivity.DATAUSER_EMAILUSER, "");
        String nacimiento=preferences.getString(MainActivity.DATAUSER_BIRTHDAYUSER, "");
        String celular=preferences.getString(MainActivity.DATAUSER_PHONEUSER, "");
        nombreFacebook.setText(nombre);
        lastnameuser.setText(apellido);
        mail_fb.setText(correo);
        nacimiento_fb.setText(nacimiento);//nacimiento
        celular_facebook.setText(celular);
    }
    public void gravarUsuarioCache(Context c,String nombre, String apellido, String nacimiento, String celular){
        SharedPreferences preferences=c.getSharedPreferences(MainActivity.STRING_DATAUSER,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(MainActivity.DATAUSER_NAMEUSER, nombre);
        editor.putString(MainActivity.DATAUSER_LASTENAMEUSER, apellido);
        editor.putString(MainActivity.DATAUSER_BIRTHDAYUSER, nacimiento);
        editor.putString(MainActivity.DATAUSER_PHONEUSER, celular);
        editor.commit();
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
            Snackbar.make(contenedorPrincipal, "Los permisos son necesarios para poder usar la aplicacion ",
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
                editarfoto.setEnabled(true);
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

    private void abrirCamara() {
        File file = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN); //creo un directorio en mi almacenamiento interno si no existe
        boolean isCreada = file.exists();
        if (!isCreada) isCreada = file.mkdirs(); //sino existe se crea la direccion de carpeta
        if (isCreada) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            imageFileName = "JPEG_" + timeStamp + ".jpg";
            mPath = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                    + File.separator + imageFileName;//indicamos la ruta de almacenamiento
            newFile = new File(mPath); //construimos el archivo
            intent_fotouno = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getApplicationContext().getPackageName() + ".provider";
                ouputfotouno = FileProvider.getUriForFile(getActivity(), authorities, newFile);
                intent_fotouno.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotouno);

            } else {
                ouputfotouno = Uri.fromFile(newFile);
                intent_fotouno.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotouno); //indicar un tipo URI (imgo video)
            }

            startActivityForResult(intent_fotouno, COD_FOTO); //llamara la funcion onActivityResult
        }
    }
    //FUNCION DE ACTUALIZAR TELEFONO DE USUARIO FACEBOOK ALMACENADO
    private void actualizarDatosFacebook(){
        recuperarIdFacebook();
        String idfbccount=idfbcuenta ;
        progreso=new ProgressDialog(getActivity());
        progreso.setMessage("Actualizando datos...");
        progreso.show();
        apiService service=apiService.retrofi.create(apiService.class);
        RequestBody idfbbody=RequestBody.create(MultipartBody.FORM, idfbccount);
        RequestBody tipousuariobody=RequestBody.create(MultipartBody.FORM, "facebook");
        String celular=celular_facebook.getText().toString();
        RequestBody celuarbody=RequestBody.create(MultipartBody.FORM, celular);
        MultipartBody.Part imagePart_uno = null;
        Call<ResponseBody>call=service.updateDatosFacebook(
                idfbbody,
                tipousuariobody,
                celuarbody
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("results", "Actualizacion correcta"+ response.body().toString());
                progreso.hide();
               // Toast.makeText(getActivity(), "Se ha actualizado tus datos",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("error",t.toString());
             //   Toast.makeText(getActivity(), "Fallo la actualizacion",Toast.LENGTH_SHORT).show();
                progreso.hide();

            }
        });
    }
    private void actualizarDatosUsuario(){
        recuperarIdUserMail();//972379179317389193
        String idfbccount=idusermailcuenta;
        progreso=new ProgressDialog(getActivity());
        progreso.setMessage("Actualizando datos...");
        progreso.show();
        apiService service=apiService.retrofi.create(apiService.class);
        RequestBody idfbbody=RequestBody.create(MultipartBody.FORM, idfbccount);
        String nombre=nombreFacebook.getText().toString();
        String apellido=lastnameuser.getText().toString();
        String fechaRecibida=fnacimiento;
        String celular=celular_facebook.getText().toString();
        RequestBody nombrebody=RequestBody.create(MultipartBody.FORM, nombre);
        RequestBody apellidobody=RequestBody.create(MultipartBody.FORM, apellido);
        RequestBody fnacimientobody=RequestBody.create(MultipartBody.FORM, fechaRecibida);
        RequestBody celuarbody=RequestBody.create(MultipartBody.FORM, celular);
        MultipartBody.Part imagePart_uno = null;
        if (bitmapfoto1 != null) {
            File photo_uno = newFile;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapfoto1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
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
        Call<apiUpdateUser>call=service.updateDatosCorreo(
                idfbbody,
                nombrebody,
                apellidobody,
                fnacimientobody,
                celuarbody,
                imagePart_uno);
        call.enqueue(new Callback<apiUpdateUser>() {
            @Override
            public void onResponse(Call<apiUpdateUser> call, Response<apiUpdateUser> response) {
                if(response.isSuccessful()) {
                    Log.d("results", "Actualizacion correcta"+ response.body().toString());
                    String nombre = "", apellido = "",nacimiento = "", telefono = "", sesion="", perfil="";
                    apiUpdateUser apiupdateuser = response.body();
                    ArrayList<datoUpdateUser> ListaDatos = apiupdateuser.getResults();
                    for (int i = 0; i < ListaDatos.size(); i++) {
                        datoUpdateUser p = ListaDatos.get(i);
                        nombre =p.getNombre();
                        apellido = p.getApellido();
                        nacimiento = p.getCumpleanos();//formatear
                        telefono = p.getTelefono();
                        sesion=p.getSesion();
                        String fechaRecibida=nacimiento;
                        try {
                            Date fechaConcreta= new SimpleDateFormat("yyyy-MM-dd").parse(fechaRecibida);
                            String fechaNueva= new SimpleDateFormat("dd'/'MMM'/'yyyy").format(fechaConcreta);
                            nacimiento=fechaNueva;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        gravarUsuarioCache(getApplicationContext(),nombre,apellido,nacimiento,telefono);
                        recuperarUsuarioCache();
                        MainActivity.recuperarUsuarioCacheNameLastName(getApplicationContext());


                        //para perfil cargar
                        perfil=p.getFoto();
                        if(perfil.equals("http://terceracto.com.mx/reportapp//N/A")){
                            //nada
                           // Toast.makeText(getApplicationContext(), "no se actualizoFoto",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            MainActivity.perfil(getApplicationContext(),perfil);
                            Picasso.with(getApplicationContext()).load(perfil).resize(500,500).into(img);
                            //CARGA DE FONDO DEGRADADO O EFECTO GAUSSIANO
                            target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    img_degradado.setImageBitmap(BlurImage.fastblur(bitmap, 1f, BLUR_PRECENTAGE));
                                }
                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    img_degradado.setImageResource(R.drawable.ic_camara);
                                }
                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }
                            };
                            Picasso.with(getApplicationContext())
                                    .load(perfil)
                                    .error(R.drawable.ic_camara)
                                    .resize(img_degradado.getWidth(),img_degradado.getHeight())
                                    .placeholder(R.drawable.ic_camara)
                                    .into(target);
                            img_degradado.setTag(target);
                        }
                    }
                    progreso.hide();

                }
                //Toast.makeText(getActivity(), "Se ha actualizado tus datos",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<apiUpdateUser> call, Throwable t) {
                Log.d("error",t.toString());
                Toast.makeText(getApplicationContext(), "Fallo la actualizacion",Toast.LENGTH_SHORT).show();
                progreso.hide();

            }
        });
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COD_SELECCIONA:
                if (resultCode == RESULT_OK) {
                    ouputfotouno = data.getData();
                    try {
                        bitmapfoto1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ouputfotouno);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(bitmapfoto1);
                }
                break;
            case COD_FOTO:
                if (resultCode == RESULT_OK) {
                    bitmapfoto1 = BitmapFactory.decodeFile(mPath);
                    img.setImageBitmap(bitmapfoto1);
                }
                break;
            default:
                break;
        }
    }

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
}
