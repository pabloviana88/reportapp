package com.android.sga.reportapp.Adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sga.reportapp.Additional.DetalleReporte;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.SQLite.Datos.Datos;
import com.android.sga.reportapp.gson.DatosGenerales;
import com.android.sga.reportapp.gson.datosReporte;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class RecycleViewAdapterReportsCache extends RecyclerView.Adapter<RecycleViewAdapterReportsCache.VieHolder> {
    private ArrayList<DatosGenerales> dataset=new ArrayList<>();
    private ArrayList<datosReporte> dataset2;
    private ArrayList<Datos> dataset3;
    private Context context;
    public Context ctx;
    public RecycleViewAdapterReportsCache(Context context, Context ctx)
    {
        this.context=context;
        this.ctx=ctx;
        this.dataset=new ArrayList<>();
        this.dataset2=new ArrayList<>();
        this.dataset3=new ArrayList<>();
    }
    public  void adicionarListaDatos(ArrayList<Datos> listDatosgenerales) {
        //dataset.addAll(listDatosgenerales);
        dataset3.addAll(listDatosgenerales);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecycleViewAdapterReportsCache.VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte,parent,false);
        VieHolder vieHolder=new VieHolder(view,ctx,dataset3);
        return vieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterReportsCache.VieHolder holder, int position) {
        Typeface Light = TypefacesUtils.get(context, "fonts/MavenPro-Regular.ttf");
        Typeface Bold = TypefacesUtils.get(context, "fonts/MavenPro-Bold.ttf");
        Datos p=dataset3.get(position);
        holder.tipo.setText(p.getCategoria());
        holder.tipo.setTypeface(Bold);
        holder.Direccion.setText(p.getDireccion()+", "+p.getColonia());
        holder.Direccion.setTypeface(Light);
        holder.Descripcion.setText(p.getFecha());
        holder.Descripcion.setTypeface(Light);
        holder.Estatus.setText(p.getEstatus());
        holder.Estatus.setTypeface(Bold);
        Glide.with(context)
                .load(p.getFoto())
                .fitCenter()
                //.centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .into(holder.fotoimageView);

    }

    @Override
    public int getItemCount() {
        return dataset3.size();
    }

/*
    public  void adicionarLitDatosreporte(ArrayList<DatosGenerales> listaDatosGenerales) {
        dataset.addAll(listaDatosGenerales);
        notifyDataSetChanged();
    }
*/
    public class VieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView fotoimageView;
        private TextView tipo;
        private TextView Direccion;
        private TextView Descripcion;
        private TextView Estatus;

        public VieHolder(View itemView) {
            super(itemView);
        }

        Context ctx;
        ArrayList<Datos> listaDatosgenerales=new ArrayList<>();

        public VieHolder(View itemView, Context ctx, ArrayList<Datos> Listapokemon){
            super(itemView);
            this.listaDatosgenerales=Listapokemon;
            this.ctx=ctx;
            itemView.setOnClickListener(this);
            fotoimageView=(ImageView)itemView.findViewById(R.id.fotoImageView);
            tipo=(TextView) itemView.findViewById(R.id.txtTipo);
            Direccion=(TextView) itemView.findViewById(R.id.txtDireccion);
            Descripcion=(TextView)itemView.findViewById(R.id.txtFecha);
            Estatus=(TextView)itemView.findViewById(R.id.txtestatus);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
          Datos datosGenerales =this.listaDatosgenerales.get(position);

            Intent intent= new Intent(this.ctx,DetalleReporte.class);
            //  intent.putExtra("id",pokemon.getId());
            intent.putExtra("id_reporte",datosGenerales.getId_reporte());
            intent.putExtra("folio",datosGenerales.getFolio());
            intent.putExtra("categoria",datosGenerales.getCategoria());
            intent.putExtra("direccion",datosGenerales.getDireccion());
            intent.putExtra("cruzamiento",datosGenerales.getCruzamiento());
            intent.putExtra("colonia",datosGenerales.getColonia());
            intent.putExtra("descripcion",datosGenerales.getDescripcion());
            intent.putExtra("usuario",datosGenerales.getUsuario());
            intent.putExtra("latitud",datosGenerales.getLatitud());
            intent.putExtra("longitud",datosGenerales.getLongitud());
            intent.putExtra("fecha",datosGenerales.getFecha());
            intent.putExtra("estatus",datosGenerales.getEstatus());
            intent.putExtra("hora",datosGenerales.getHora());
            intent.putExtra("foto",datosGenerales.getFoto());
            intent.putExtra("likes",datosGenerales.getLikes());
            intent.putExtra("likes_usuario",datosGenerales.getLike_usuario());
            intent.putExtra("tipo",datosGenerales.getTipo());


            this.ctx.startActivity(intent);

        }}

    }
