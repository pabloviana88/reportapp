package com.android.sga.reportapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sga.reportapp.R;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapterTipo extends RecyclerView.Adapter<RecyclerViewAdapterTipo.ViewHolder> {
    private ArrayList<String> mNames = new ArrayList<>();
    List mImageList = new ArrayList();
    private Context mContext;
    private TextView recuperado;
    private CheckBox checkBox;
    private CheckBox checkBox3;
    private Button listo;
    public final static String STRING_CHECK= "repotapp";
    public final static String ESTADO_CHECK= "estado.check";


    public RecyclerViewAdapterTipo(Context context,ArrayList<String> names,List mImageListita,TextView recuperadito,CheckBox checado,CheckBox checado3,Button liston){
        mNames = names;
        mImageList=mImageListita;
        mContext = context;
        recuperado=recuperadito;
        checkBox= checado;
        checkBox3=checado3;
        listo=liston;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterTipo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new RecyclerViewAdapterTipo.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterTipo.ViewHolder holder, final int position) {
        Typeface Light = TypefacesUtils.get(mContext, "fonts/MavenPro-Regular.ttf");
        Typeface Bold = TypefacesUtils.get(mContext, "fonts/MavenPro-Bold.ttf");
        Glide.with(mContext).load(mImageList.get(position))
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .into(holder.imageView_tipo);
        holder.textView_tipo.setText(mNames.get(position));
        holder.textView_tipo.setTypeface(Light);

        RecyclerViewAdapterTipo.guardar_check(mContext,false);//RESET A VALOR: FALSE
        holder.imageView_tipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //asignarle el nombre de la categoria
              //  recuperado.setText(mNames.get(position));

                if(!RecyclerViewAdapterTipo.recuperar_check(mContext)){
                    if (position==0){
                        if(holder.clic.getVisibility()==View.GONE){
                            holder.clic.setVisibility(View.VISIBLE); //MOSTRAR SELECCION
                            recuperado.setText(mNames.get(position));//CATEGORIA : ALCANTARILLA
                            RecyclerViewAdapterTipo.guardar_check(mContext, true);
                            checkBox.setChecked(true); //ACTIVAR EL CHECK
                            if(checkBox3.isChecked()) listo.setEnabled(true);

                        }
                    }
                    if (position==1){
                        if(holder.clic.getVisibility()==View.GONE){
                            holder.clic.setVisibility(View.VISIBLE); //MOSTRAR SELECCION
                            recuperado.setText(mNames.get(position));//CATEGORIA : BACHE
                            RecyclerViewAdapterTipo.guardar_check(mContext, true);
                            checkBox.setChecked(true); //ACTIVAR EL CHECK
                            if(checkBox3.isChecked()) listo.setEnabled(true);
                        }
                    }
                    if (position==2){
                        if(holder.clic.getVisibility()==View.GONE){
                            holder.clic.setVisibility(View.VISIBLE); //MOSTRAR SELECCION
                            recuperado.setText(mNames.get(position));//CATEGORIA : ALUMBRADO
                            RecyclerViewAdapterTipo.guardar_check(mContext, true);
                            checkBox.setChecked(true); //ACTIVAR EL CHECK
                            if(checkBox3.isChecked()) listo.setEnabled(true);

                        }
                    }
                    if (position==3){
                        if(holder.clic.getVisibility()==View.GONE){
                            holder.clic.setVisibility(View.VISIBLE); //MOSTRAR SELECCION
                            recuperado.setText(mNames.get(position));//CATEGORIA :
                            RecyclerViewAdapterTipo.guardar_check(mContext, true);
                            checkBox.setChecked(true); //ACTIVAR EL CHECK
                            if(checkBox3.isChecked()) listo.setEnabled(true);
                        }
                    }
                    if (position==4){
                        if(holder.clic.getVisibility()==View.GONE){
                            holder.clic.setVisibility(View.VISIBLE); //MOSTRAR SELECCION
                            recuperado.setText(mNames.get(position));//CATEGORIA :
                            RecyclerViewAdapterTipo.guardar_check(mContext, true);
                            checkBox.setChecked(true); //ACTIVAR EL CHECK
                            if(checkBox3.isChecked()) listo.setEnabled(true);
                        }
                    }
                    if (position==5){
                        if(holder.clic.getVisibility()==View.GONE){
                            holder.clic.setVisibility(View.VISIBLE); //MOSTRAR SELECCION
                            recuperado.setText(mNames.get(position));//CATEGORIA :
                            RecyclerViewAdapterTipo.guardar_check(mContext, true);
                            checkBox.setChecked(true); //ACTIVAR EL CHECK
                            if(checkBox3.isChecked()) listo.setEnabled(true);
                        }
                    }
                }
                else {
                    if (position==0){
                        if(holder.clic.getVisibility()==View.VISIBLE) {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                            RecyclerViewAdapterTipo.guardar_check(mContext, false);
                            checkBox.setChecked(false); //DESACTIVAR EL CHECK
                            listo.setEnabled(false);//DESHABILITAR EL BOTON
                            }
                            else {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                        }
                    }
                    if (position==1){
                        if(holder.clic.getVisibility()==View.VISIBLE) {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                            RecyclerViewAdapterTipo.guardar_check(mContext, false);
                            checkBox.setChecked(false); //DESACTIVAR EL CHECK
                            listo.setEnabled(false);//DESHABILITAR EL BOTON
                        }
                        else {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                        }
                    }
                    if (position==2){
                        if(holder.clic.getVisibility()==View.VISIBLE) {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                            RecyclerViewAdapterTipo.guardar_check(mContext, false);
                            checkBox.setChecked(false); //DESACTIVAR EL CHECK
                            listo.setEnabled(false);//DESHABILITAR EL BOTON

                        }else {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                        }
                    }
                    if (position==3){
                        if(holder.clic.getVisibility()==View.VISIBLE) {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                            RecyclerViewAdapterTipo.guardar_check(mContext, false);
                            checkBox.setChecked(false); //DESACTIVAR EL CHECK
                            listo.setEnabled(false);//DESHABILITAR EL BOTON

                        }else {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                        }
                    }
                    if (position==4){
                        if(holder.clic.getVisibility()==View.VISIBLE) {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                            RecyclerViewAdapterTipo.guardar_check(mContext, false);
                            checkBox.setChecked(false); //DESACTIVAR EL CHECK
                            listo.setEnabled(false);//DESHABILITAR EL BOTON

                        }else {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                        }
                    }
                    if (position==5){
                        if(holder.clic.getVisibility()==View.VISIBLE) {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                            RecyclerViewAdapterTipo.guardar_check(mContext, false);
                            checkBox.setChecked(false); //DESACTIVAR EL CHECK
                            listo.setEnabled(false);//DESHABILITAR EL BOTON

                        }else {
                            holder.clic.setVisibility(View.GONE); //OCULTARA
                        }
                    }

                }

                if(RecyclerViewAdapterTipo.recuperar_check(mContext)&& checkBox3.isChecked()){ listo.setEnabled(true); }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_tipo;
        TextView textView_tipo;
        LinearLayout l_tipo;
        Button clic;
        boolean activo; //entrada como false
        SegmentedButtonGroup sbg;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView_tipo = itemView.findViewById(R.id.image_view_tipo);
            textView_tipo = itemView.findViewById(R.id.name_tipo);
            l_tipo=itemView.findViewById(R.id.selec_tipo);
            clic=itemView.findViewById(R.id.seleccion_tipo);
            sbg=itemView.findViewById(R.id.segmentedButtonGroup);



        }

    }

    private static boolean recuperar_check(Context c) {
        SharedPreferences preferences=c.getSharedPreferences(STRING_CHECK, MODE_PRIVATE);
        return preferences.getBoolean(ESTADO_CHECK,false);

    }
    //FUNCION PARA ALMACENAR VALOR DE SESSION : TRUE O FALSE
    public static void guardar_check(Context g, boolean e){
        SharedPreferences preferences=g.getSharedPreferences(STRING_CHECK, MODE_PRIVATE);
        preferences.edit().putBoolean(ESTADO_CHECK,e).apply();
    }


}
