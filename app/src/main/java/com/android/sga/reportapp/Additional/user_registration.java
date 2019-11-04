package com.android.sga.reportapp.Additional;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.utileries.TypefacesUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

public class user_registration extends AppCompatActivity {
    TextView txt_registro;
    Button alta_usuario;
    EditText alta_nombre, alta_apellidos, alta_correo, alta_celular,alta_fecha_nac, alta_contrasena, alta_confirmar_contrasena;
    ProgressDialog procesando;
    RadioButton alta_hombre, alta_mujer;
    LinearLayout contenedorPrincipal;
    CircleImageView img;
    private int dia, mes,ano;
    private static final int TIPO_DIALOG=0;
    private static DatePickerDialog.OnDateSetListener selector_fecha;
    String genero="";
    private static final int COD_SELECCIONA =10 ;
    private static final int COD_FOTO = 20;
    private final int MY_RERMISSIONS = 100;
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//diretorio principal
    private static final String CARPETA_IMAGEN = "perfil";//donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta de directorio
    String imageFileName;
    String mPath;
    File newFile;
    Intent intent_fotouno;
    Uri ouputfotouno;
    private Bitmap bitmapfoto1;
    String fechanacimiento="";
    Bitmap rotatedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        contenedorPrincipal = (LinearLayout) findViewById(R.id.linearPricipalRegistro);
        txt_registro=(TextView)findViewById(R.id.texto_registro);
        alta_usuario=(Button)findViewById(R.id.boton_alta_usuario);
        alta_nombre=(EditText)findViewById(R.id.alta_nombre);
        alta_apellidos=(EditText)findViewById(R.id.alta_apellidos);
        alta_correo=(EditText)findViewById(R.id.alta_correo);
        alta_celular=(EditText)findViewById(R.id.alta_celular);
        alta_fecha_nac=(EditText)findViewById(R.id.alta_fecha_nacimiento);
        alta_contrasena=(EditText)findViewById(R.id.alta_contrasena);
        alta_confirmar_contrasena=(EditText)findViewById(R.id.alta_confirmar_contrasena);
        alta_hombre=(RadioButton)findViewById(R.id.alta_hombre);
        alta_mujer=(RadioButton)findViewById(R.id.alta_mujer);
        img=(CircleImageView)findViewById(R.id.perfil_facebook);

        Calendar calendarioHoy= Calendar.getInstance();
        ano=calendarioHoy.get(Calendar.YEAR);
        mes=calendarioHoy.get(Calendar.MONTH);
        dia=calendarioHoy.get(Calendar.DAY_OF_MONTH);
        //mostrar_fecha();
        selector_fecha= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ano=year;
                mes=month+1;
                dia=dayOfMonth;
                String diaFormateado=(dia<10)?"0"+String.valueOf(dia):String.valueOf(dia);
                String mesFormateado=(mes<10)?"0"+String.valueOf(mes):String.valueOf(mes);
                String anoFormateado=(ano<10)?"0"+String.valueOf(ano):String.valueOf(ano);
                fechanacimiento=mesFormateado+"/"+diaFormateado+"/"+anoFormateado;
                String fechaRecibida=diaFormateado+mesFormateado+anoFormateado;
                try {
                    Date fechaConcreta= new SimpleDateFormat("ddMMyyyy").parse(fechaRecibida);
                    String fechaNueva= new SimpleDateFormat("dd'/'MMM'/'yyyy").format(fechaConcreta);
                    alta_fecha_nac.setText(fechaNueva);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
               // mostrar_fecha();
            }
        };
        alta_hombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alta_mujer.setChecked(false);
            }
        });
        alta_mujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alta_hombre.setChecked(false);
            }
        });
        alta_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alta_hombre.isChecked()) genero="male";
                else if (alta_mujer.isChecked()) genero="female";

                if (!alta_nombre.getText().toString().isEmpty()&&
                        !alta_apellidos.getText().toString().isEmpty()&&
                        !alta_correo.getText().toString().isEmpty()&&
                        !alta_fecha_nac.getText().toString().isEmpty()&&
                        !alta_contrasena.getText().toString().isEmpty()&&
                        !alta_confirmar_contrasena.getText().toString().isEmpty()) {

                         String c1=alta_contrasena.getText().toString();
                         String c2=alta_confirmar_contrasena.getText().toString();

                         if (c1.equals(c2)){
                             //oculta el teclado e inserta los datos
                             View view= getCurrentFocus();
                             if (view!= null){
                                 InputMethodManager teclado= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                 teclado.hideSoftInputFromWindow(view.getWindowToken(),0);
                             }
                            insertarDatosPersonales();
                         }
                        else {
                            Toast.makeText(user_registration.this, "Contraseñas diferentes, corregir", Toast.LENGTH_LONG).show();
                        }
                } else {
                    Toast.makeText(user_registration.this, "LLenar todos los campos de usuario", Toast.LENGTH_LONG).show();
                }
            }
        });

// para cargar o tomar foto en el imageview
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Tomar foto", "Elegir de Galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(user_registration.this);
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
        //permiso para habilitar tomar foto o galeria
        //Permisos para versiones superiores de 6.0
        if (mayRequestStoragePermission()) {
            img.setEnabled(true);
        } else {
            img.setEnabled(false);
        }

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case 0:
                return new DatePickerDialog(this,selector_fecha,ano, mes,dia);
        }
        return null;
    }

    public void mostrar_calendario(View control){
        showDialog(TIPO_DIALOG);
    }

    public void mostrar_fecha(){
        alta_fecha_nac.setText(mes+"/"+dia+"/"+ano);//formato de fecha de facebook
        //actualizacion
    }

    public void insertarDatosPersonales() {
        procesando=new ProgressDialog(user_registration.this);
        procesando.setMessage("Guardando datos...");
        procesando.show();
        apiService service=apiService.retrofi.create(apiService.class);
        // String id_cuenta="439934366515534"; //morro lo genera automatico
        String nombre=alta_nombre.getText().toString().trim();
        String apellido=alta_apellidos.getText().toString().trim();
        String correo=alta_correo.getText().toString().trim();
        String sexo=genero;
        String celular=alta_celular.getText().toString().trim();
        String fecha_nacimiento=fechanacimiento;
        String contrasena=alta_contrasena.getText().toString();
        String tipo="Correo";
        RequestBody nombre_body=RequestBody.create(MultipartBody.FORM, nombre);
        RequestBody apellido_body=RequestBody.create(MultipartBody.FORM, apellido);
        RequestBody correo_body=RequestBody.create(MultipartBody.FORM, correo);
        RequestBody telefono_body=RequestBody.create(MultipartBody.FORM, celular);
        RequestBody f_nacimientobody=RequestBody.create(MultipartBody.FORM, fecha_nacimiento);
        RequestBody genero_body=RequestBody.create(MultipartBody.FORM, sexo);
        RequestBody contrasena_body=RequestBody.create(MultipartBody.FORM, contrasena);
        RequestBody tipo_body=RequestBody.create(MultipartBody.FORM, tipo);
        MultipartBody.Part imagePart_uno = null;
        if (rotatedBitmap != null) {
            File photo_uno = newFile;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytesArray = stream.toByteArray();

            if (photo_uno != null) {
                RequestBody imagebodyuno = RequestBody.create(MediaType.parse("image/*"), bytesArray);
                imagePart_uno = MultipartBody.Part.createFormData("foto", photo_uno.getName(), imagebodyuno);
            } else {
                File img1_galeria = new File(getPath(ouputfotouno));
                RequestBody imagebodyuno = RequestBody.create(MediaType.parse("image/*"), img1_galeria);
                imagePart_uno = MultipartBody.Part.createFormData("foto", img1_galeria.getName(), imagebodyuno);
            }
        }
        final Call<ResponseBody> call=service.insertaDatosPersonales(
                nombre_body,
                apellido_body,
                correo_body,
                telefono_body,
                f_nacimientobody,
                genero_body,
                contrasena_body,
                imagePart_uno,
                tipo_body

        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("results", "Guadado datos personales"+ response.body().toString());
                procesando.hide();
                Toast.makeText(user_registration.this, "Usuario dado de alta",Toast.LENGTH_SHORT).show();
                //irLogin();
                img.setImageResource(R.drawable.usuario_o);
                alta_nombre.setText("");
                alta_apellidos.setText("");
                alta_correo.setText("");
                alta_celular.setText("");
                alta_fecha_nac.setText("");
                alta_contrasena.setText("");
                alta_confirmar_contrasena.setText("");

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("error",t.toString());
                procesando.hide();
                Toast.makeText(user_registration.this, "Fallo al dar de alta al usuario",Toast.LENGTH_SHORT).show();
            }
        });


    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public void atras(View view) {
        onBackPressed();
    }
    private void irLogin() {
        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean mayRequestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED)) {
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
    //Funcion abrir Camara para Foto 1
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
                ouputfotouno = FileProvider.getUriForFile(this, authorities, newFile);
                intent_fotouno.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotouno);

            } else {
                ouputfotouno = Uri.fromFile(newFile);
                intent_fotouno.putExtra(MediaStore.EXTRA_OUTPUT, ouputfotouno); //indicar un tipo URI (imgo video)
            }

            startActivityForResult(intent_fotouno, COD_FOTO); //llamara la funcion onActivityResult
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }
    //recuperar despues de la funcion tomar foto
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPath = savedInstanceState.getString("file_path");

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COD_SELECCIONA:
                if (resultCode == RESULT_OK) {
                    ouputfotouno = data.getData();
                    try {
                        rotatedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ouputfotouno);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(rotatedBitmap);
                }
                break;
            case COD_FOTO:
                if (resultCode == RESULT_OK) {
                    bitmapfoto1 = BitmapFactory.decodeFile(mPath);
                    rotatedBitmap = null;
                    try {
                        ExifInterface ei = new ExifInterface(mPath);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap = rotateImage(bitmapfoto1, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap = rotateImage(bitmapfoto1, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap = rotateImage(bitmapfoto1, 270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default: rotatedBitmap = bitmapfoto1; }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(rotatedBitmap);
                    //img.setImageBitmap(bitmapfoto1);
                }
                break;
            default:
                break;
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();
        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;
            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);
            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);
        } else {
            return bitmap;
        }
    }

}
