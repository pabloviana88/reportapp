package com.android.sga.reportapp.Additional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.sga.reportapp.Additional.Acerca_de_tc;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.utileries.TypefacesUtils;

public class Acerca_de extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_acerca_de);
        Typeface Light = TypefacesUtils.get(this, "fonts/MavenPro-Regular.ttf");
        Typeface Bold = TypefacesUtils.get(this, "fonts/MavenPro-Bold.ttf");
        TextView etiqueta_acerca=(TextView)findViewById(R.id.etiqueta_ad);
        TextView tv_reportapp=(TextView)findViewById(R.id.tv_reportapp);
        TextView tv_version=(TextView)findViewById(R.id.tv_version);
        TextView tv_desarrollado=(TextView)findViewById(R.id.tv_desarrollado);
        TextView tv_sga=(TextView)findViewById(R.id.tv_sga);
        Button b_terminos=(Button)findViewById(R.id.button_cuenta);
        etiqueta_acerca.setTypeface(Light);
        tv_reportapp.setTypeface(Bold);
        tv_version.setTypeface(Light);
        tv_desarrollado.setTypeface(Light);
        tv_sga.setTypeface(Light);
        b_terminos.setTypeface(Light);
    }

    public void terminoscondiciones(View view) {
        Intent intent= new Intent(this, Acerca_de_tc.class);
        startActivity(intent);
    }
    public void retroceso(View view) {
        onBackPressed();
    }
}
