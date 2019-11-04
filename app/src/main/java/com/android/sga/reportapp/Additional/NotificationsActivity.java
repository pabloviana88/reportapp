package com.android.sga.reportapp.Additional;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;

import com.android.sga.reportapp.R;
import com.android.sga.reportapp.utileries.TypefacesUtils;

public class NotificationsActivity extends AppCompatActivity {
    Typeface Light , Bold;
    Switch notificacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Light = TypefacesUtils.get(this, "fonts/MavenPro-Regular.ttf");
        Bold = TypefacesUtils.get(this, "fonts/MavenPro-Bold.ttf");
        TextView etiqueta_notif=(TextView)findViewById(R.id.etiqueta_act_notif); etiqueta_notif.setTypeface(Light);
        notificacion=(Switch)findViewById(R.id.sw_notificacion); notificacion.setTypeface(Light);


    }
    public void retroceso(View view) {
        onBackPressed();
    }
}
