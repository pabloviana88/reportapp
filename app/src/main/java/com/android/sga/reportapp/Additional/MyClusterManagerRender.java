package com.android.sga.reportapp.Additional;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.sga.reportapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MyClusterManagerRender extends DefaultClusterRenderer<ClusterMarker> {
   // private final IconGenerator iconGenerator;
    private final ImageView imageView;
    private int imageWidth;
    private int imageHeight;

    public MyClusterManagerRender(Context context,
                                  GoogleMap map,
                                  ClusterManager<ClusterMarker> clusterManager) {
        super(context, map, clusterManager);


       // iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        // imageWidth = (int) context.getResources().getDimension(R.dimen.custom_marke_image);
       // imageHeight = (int) context.getResources().getDimension(R.dimen.custom_marke_image);
       // imageView.setLayoutParams(new ViewGroup.LayoutParams(imageWidth,imageHeight));
      //  int padding= (int) context.getResources().getDimension(R.dimen.custom_marke_padding);
        //imageView.setPadding(padding,padding,padding,padding);
      //  iconGenerator.setContentView(imageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterMarker item, MarkerOptions markerOptions) {
       // super.onBeforeClusterItemRendered(item, markerOptions);

       // imageView.setImageResource(item.getIcon());
       // Bitmap icon= iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(item.getIcon())).title(item.getTitle());
    }


}
