package com.android.sga.reportapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.sga.reportapp.R;
import com.android.sga.reportapp.gson.Imagen;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class viewPegerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Imagen> dataset=new ArrayList<>();

    public viewPegerAdapter(Context context, ArrayList<Imagen> imagen1){
        this.context=context;
        dataset=imagen1;
    }
    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.activity_reporte_custom,null);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
        // imageView.setImageResource(imagenes[position]);
         Imagen imagen=dataset.get(position);

            Glide.with(context)
                    .load(imagen.getFoto())
                    .centerCrop()
                    // .fitCenter()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .into(imageView);



        ViewPager vp=(ViewPager)container;
        vp.addView(view,0);
        return view;
        //  return super.instantiateItem(container, position);
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp=(ViewPager)container;
        View view=(View)object;
        vp.removeView(view);
    }

}
