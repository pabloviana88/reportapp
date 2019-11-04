package com.android.sga.reportapp.Adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sga.reportapp.Additional.DetalleReporte;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.SQLite.Datos.Datos;
import com.android.sga.reportapp.gson.datosReporte;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class RecyclerViewAdapterDatos extends RecyclerView.Adapter<RecyclerViewAdapterDatos.VieHolder> implements Filterable {
    ArrayList<datosReporte> dataset=new ArrayList<>();
   // ArrayList<Datos> datasetCache;
    ArrayList<datosReporte> filterList;
    private Context context_u;
    private Context ctx;
    CustomFilter filter;

    public RecyclerViewAdapterDatos(Context context, Context ctx){
        this.context_u=context;
        this.ctx=ctx;
        dataset=new ArrayList<>();
        dataset=new ArrayList<>();
    }
    @NonNull
    @Override
    public VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datos,parent,false);
        VieHolder vieHolder=new VieHolder(view,ctx,dataset);
        return vieHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull VieHolder holder, final int position) {
        datosReporte p=dataset.get(position);
      //  Datos p=datasetCache.get(position);
        if(p.getCategoria().equals("ALCANTARILLA")){holder.iconoCategoria.setImageResource(R.drawable.ic_pin_mapa_alcantarilla);}
        else if (p.getCategoria().equals("BACHES")){holder.iconoCategoria.setImageResource(R.drawable.ic_pin_mapa_bache);}
        else if (p.getCategoria().equals("ALUMBRADO")){ holder.iconoCategoria.setImageResource(R.drawable.ic_pin_mapa_alumbrado);}
        else if (p.getCategoria().equals("ARBOL CAIDO")){holder.iconoCategoria.setImageResource(R.drawable.ic_pin_mapa_arbol);}
        else if (p.getCategoria().equals("BASURA")){holder.iconoCategoria.setImageResource(R.drawable.ic_pin_mapa_basura);}
        else if (p.getCategoria().equals("FUGA DE AGUA")){holder.iconoCategoria.setImageResource(R.drawable.ic_pin_mapa_fuga);}

        holder.Categoria.setText(p.getCategoria());
        holder.Direccion.setText(p.getDireccion()+", "+p.getColonia());
        holder.Fecha.setText(p.getFecha());
        holder.Folio.setText(p.getFolio());
        holder.Estatus.setText(p.getEstatus());
        if (p.getEstatus().equals("REPORTADO")){
            holder.sbg.setPosition(0,0);

        }else if(p.getEstatus().equals("RECIBIDO")){
            holder.sbg.setPosition(1,0);

        }else if(p.getEstatus().equals("PROCESO")){
            holder.sbg.setPosition(2,0);

        }else if(p.getEstatus().equals("ATENDIDO")){
            holder.sbg.setPosition(3,0);

        }

        Glide.with(context_u)
                .load(p.getFoto())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .into(holder.FotoimageView);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
           //     Snackbar.make(v, dataset.get(pos).getCategoria(), Snackbar.LENGTH_LONG).show();
                 Intent intent= new Intent(v.getContext(),DetalleReporte.class);
            //  intent.putExtra("id",pokemon.getId());
            intent.putExtra("id_reporte",dataset.get(pos).getId_reporte());
            intent.putExtra("folio",dataset.get(pos).getFolio());
            intent.putExtra("tipo",dataset.get(pos).getTipo());
            intent.putExtra("direccion",dataset.get(pos).getDireccion());
            intent.putExtra("cruzamiento",dataset.get(pos).getCruzamiento());
            intent.putExtra("colonia",dataset.get(pos).getColonia());
            intent.putExtra("descripcion",dataset.get(pos).getDescripcion());
            intent.putExtra("usuario",dataset.get(pos).getUsuario());
            intent.putExtra("latitud",dataset.get(pos).getLatitud());
            intent.putExtra("longitud",dataset.get(pos).getLongitud());
            intent.putExtra("fecha",dataset.get(pos).getFecha());
            intent.putExtra("estatus",dataset.get(pos).getEstatus());
            intent.putExtra("hora",dataset.get(pos).getHora());
            intent.putExtra("foto",dataset.get(pos).getFoto());
            v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new CustomFilter(this,filterList);
        }

        return filter;
    }


    public class VieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView FotoimageView;
        private TextView Categoria;
        private TextView Direccion;
        private TextView Fecha;
        private TextView Folio;
        private TextView Estatus;
        private SegmentedButtonGroup sbg;
        private ImageView iconoCategoria;
        Context ctx;
        private LinearLayout LinearItemDatos;
        ArrayList<datosReporte> listadatito=new ArrayList<>();
       // ArrayList<Datos>listadatitoCache;
        ItemClickListener itemClickListener;
        public VieHolder(View itemView, Context ctx, ArrayList<datosReporte> dataset) {
            super(itemView);
           this.listadatito=dataset;
            //this.listadatitoCache=dataset;
            this.ctx=ctx;
            itemView.setOnClickListener(this);
            FotoimageView=(ImageView)itemView.findViewById(R.id.img_foto);
            Categoria=(TextView) itemView.findViewById(R.id.tv_tipo);
            Direccion=(TextView) itemView.findViewById(R.id.tv_direccion);
            Fecha=(TextView)itemView.findViewById(R.id.tv_fecha);
            Folio=(TextView)itemView.findViewById(R.id.tv_folio);
            Estatus=(TextView)itemView.findViewById(R.id.tv_estatus);
            sbg=(SegmentedButtonGroup)itemView.findViewById(R.id.segmentedButtonGroup);
            iconoCategoria=(ImageView)itemView.findViewById(R.id.icono_categoria);
            LinearItemDatos=(LinearLayout)itemView.findViewById(R.id.layout_datos);

        }
        @Override
        public void onClick(View v) {
           // int position=getAdapterPosition();
           // datosReporte datosreporte =this.listadatito.get(getLayoutPosition());
            /*Intent intent= new Intent(this.ctx,DetalleReporte.class);
            //  intent.putExtra("id",pokemon.getId());
            intent.putExtra("id_reporte",datosreporte.getId_reporte());
            intent.putExtra("folio",datosreporte.getFolio());
            intent.putExtra("tipo",datosreporte.getTipo());
            intent.putExtra("direccion",datosreporte.getDireccion());
            intent.putExtra("cruzamiento",datosreporte.getCruzamiento());
            intent.putExtra("colonia",datosreporte.getColonia());
            intent.putExtra("descripcion",datosreporte.getDescripcion());
            intent.putExtra("usuario",datosreporte.getUsuario());
            intent.putExtra("latitud",datosreporte.getLatitud());
            intent.putExtra("longitud",datosreporte.getLongitud());
            intent.putExtra("fecha",datosreporte.getFecha());
            intent.putExtra("estatus",datosreporte.getEstatus());
            intent.putExtra("hora",datosreporte.getHora());
            intent.putExtra("foto",datosreporte.getFoto());
            this.ctx.startActivity(intent);*/

            this.itemClickListener.onItemClick(v, getLayoutPosition());

        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }

    }
    public void adicionarListaDatos(ArrayList<datosReporte> listaDat) {
        dataset.addAll(listaDat);
        notifyDataSetChanged();
    }
   /* public void adicionarListaDatosCache(ArrayList<Datos> listaDat) {
        datasetCache.addAll(listaDat);
        notifyDataSetChanged();
    }*/
    public void setFilter(ArrayList<datosReporte> listareporte){
        this.dataset=new ArrayList<>();
        this.dataset.addAll(listareporte);
        notifyDataSetChanged();
    }
    public void setFilterForMarker(ArrayList<datosReporte> listareporte){
        this.dataset=new ArrayList<>();
        this.dataset.addAll(listareporte);
        notifyDataSetChanged();
    }
}
