package com.android.sga.reportapp.Additional;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.sga.reportapp.R;
import com.android.sga.reportapp.utileries.TypefacesUtils;

public class Acerca_de_tc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_acerca_de_tc);
        Typeface Light = TypefacesUtils.get(this, "fonts/MavenPro-Regular.ttf");
        Typeface Bold = TypefacesUtils.get(this, "fonts/MavenPro-Bold.ttf");
        TextView etiqueta_legales=(TextView)findViewById(R.id.etiqueta_legales);
        TextView legales2=(TextView)findViewById(R.id.tv_legales2);
        etiqueta_legales.setTypeface(Bold);
        legales2.setTypeface(Light);
    }
    public void retroceso(View view) {
        onBackPressed();
    }

}
