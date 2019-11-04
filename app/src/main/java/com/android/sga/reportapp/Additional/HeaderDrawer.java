package com.android.sga.reportapp.Additional;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sga.reportapp.Login.LoginActivity;
import com.android.sga.reportapp.MainActivity;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.gson.apiFacebook;
import com.android.sga.reportapp.gson.apiService;
import com.android.sga.reportapp.gson.datoFacebook;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HeaderDrawer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);
    }
}
