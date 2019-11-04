package com.android.sga.reportapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.sga.reportapp.Additional.Acerca_de;
import com.android.sga.reportapp.Additional.NotificationsActivity;
import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.MainActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.facebook.login.LoginManager;

import static android.content.Context.MODE_PRIVATE;
import static com.android.sga.reportapp.MainActivity.DATAUSER_LASTENAMEUSER;
import static com.android.sga.reportapp.MainActivity.DATAUSER_NAMEUSER;
import static com.android.sga.reportapp.MainActivity.STRING_DATAUSER;
import static com.android.sga.reportapp.MainActivity.cambiarEstadoSession;
import static com.android.sga.reportapp.MainActivity.drawerLayout;
import static com.android.sga.reportapp.MainActivity.grabar_usuario;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigurationFragment extends Fragment {
    View Manager1;
    Button useratras, ir_notificacion, ir_acercade, cuenta;
    public static Button salircomo;
    String idfbrecuperado="" ;
    String idusermailrecuperado="";
    Typeface Light , Bold;

    public ConfigurationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Manager1=inflater.inflate(R.layout.fragment_configuration,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Light = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(getActivity(), "fonts/MavenPro-Bold.ttf");
        TextView etiqueta_co=(TextView)Manager1.findViewById(R.id.etiqueta_configuracion); etiqueta_co.setTypeface(Light);
        useratras = (Button)Manager1.findViewById(R.id.useratras);
        cuenta = (Button)Manager1.findViewById(R.id.button_cuenta); cuenta.setTypeface(Light);
        ir_notificacion = (Button)Manager1.findViewById(R.id.button_notificaciones); ir_notificacion.setTypeface(Light);
        ir_acercade = (Button)Manager1.findViewById(R.id.button_acerca_de); ir_acercade.setTypeface(Light);
        salircomo=(Button)Manager1.findViewById(R.id.button_logout); salircomo.setTypeface(Light);
        recuperarUsuarioCacheNameLastName(getActivity());
        useratras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).opeFragmentMapTotal();
            }
        });
        cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountFragment accountFragment = new AccountFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction= getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, accountFragment);
                fragmentTransaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        ir_notificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), NotificationsActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });
        ir_acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), Acerca_de.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });

        salircomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarIdFacebook();
                recuperarIdUserMail();

                if (idfbrecuperado.length()==0){
                    //FUNCION CAMBIO DE VARIABLE E IR A INICIAR SESSION
                    cambiarEstadoSession(getActivity(),false); //metodo de mapsActivity
                    grabar_usuario(getActivity(),null); //metodo de mapsActvity
                    LoginActivity.gravarIdUsuarioMail(getActivity(),null); //metodo de loginActivity
                    /*LANZANDO AL INTENT DE LOGIN*/
                    Intent intentMaps= new Intent(getActivity(), LoginActivity.class);
                    intentMaps.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentMaps);
                    getActivity().finish();//DESTRUIR LA ACTIVIDAD ANTERIOR
                }else {
                    LoginManager.getInstance().logOut(); //CIERRE DE SESION DE FACEBOOK
                    LoginActivity.gravarIdFacebook(getActivity(), null);
                    /*LANZANDO LA INTENT DE LOGIN*/
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();//DESTRUIR LA ACTIVIDAD
                }


            }
        });
        return  Manager1;

    }
    //FUNCION DE RECUPERAR ID FB
    public void recuperarIdFacebook(){
        SharedPreferences preferences=getActivity().getSharedPreferences(LoginActivity.STRING_DATAFB, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAFB_IDFB, "");
        idfbrecuperado=d;
    }
    //FUNCION DE RECUPERAR UD USER DE CORREO
    public void recuperarIdUserMail(){
        SharedPreferences preferences=getActivity().getSharedPreferences(LoginActivity.STRING_DATAMAIL, MODE_PRIVATE);
        String d=preferences.getString(LoginActivity.DATAMAIL_IDMAIL, "");
        idusermailrecuperado=d;
    }

    public static void recuperarUsuarioCacheNameLastName(Context c){
        SharedPreferences preferences=c.getSharedPreferences(STRING_DATAUSER, MODE_PRIVATE);
        String nombre=preferences.getString(DATAUSER_NAMEUSER, "");
        salircomo.setText("  Cerrar sesión como "+nombre);
    }

}
