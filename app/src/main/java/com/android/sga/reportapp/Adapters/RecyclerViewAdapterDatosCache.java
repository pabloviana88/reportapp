package com.android.sga.reportapp.Adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.sga.reportapp.Additional.DetalleReporte;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.SQLite.Datos.Datos;
import com.android.sga.reportapp.gson.datosReporte;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;

import java.util.ArrayList;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;

import static android.graphics.Color.WHITE;

public class RecyclerViewAdapterDatosCache extends RecyclerView.Adapter<RecyclerViewAdapterDatosCache.VieHolder> {
    ArrayList<datosReporte> dataset=new ArrayList<>();
   // ArrayList<Datos> datasetCache;
    ArrayList<datosReporte> filterList;
    ArrayList<Datos> dataset2;
    private Context context_u;
    private Context ctx;
    CustomFilter filter;

    public RecyclerViewAdapterDatosCache(Context context, Context ctx){
        this.context_u=context;
        this.ctx=ctx;
        dataset=new ArrayList<>();
        dataset2=new ArrayList<>();
    }
    @NonNull
    @Override
    public VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datos,parent,false);
        VieHolder vieHolder=new VieHolder(view,ctx,dataset2);
        return vieHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull VieHolder holder, final int position) {
        Typeface Light = TypefacesUtils.get(context_u, "fonts/MavenPro-Regular.ttf");
        Typeface Bold = TypefacesUtils.get(context_u, "fonts/MavenPro-Bold.ttf");

        Datos p=dataset2.get(position);
      //  Datos p=datasetCache.get(position);
        if(p.getCategoria().equals("ALCANTARILLA")){holder.iconoCategoria.setImageResource(R.drawable.otro_o);}
        else if (p.getCategoria().equals("BACHES")){holder.iconoCategoria.setImageResource(R.drawable.ra_tipo_bache);}
        else if (p.getCategoria().equals("ALUMBRADO")){ holder.iconoCategoria.setImageResource(R.drawable.ra_tipo_alumbrado);}
        else if (p.getCategoria().equals("ARBOL CAIDO")){holder.iconoCategoria.setImageResource(R.drawable.otro_o);}
        else if (p.getCategoria().equals("BASURA")){holder.iconoCategoria.setImageResource(R.drawable.ra_tipo_basura);}
        else if (p.getCategoria().equals("FUGA DE AGUA")){holder.iconoCategoria.setImageResource(R.drawable.otro_o);}

        holder.Categoria.setText(p.getCategoria());
        holder.Categoria.setTypeface(Bold);
        holder.Direccion.setText(p.getDireccion()+", "+p.getColonia());
        holder.Direccion.setTypeface(Light);
        holder.Fecha.setText(p.getFecha());
        holder.Fecha.setTypeface(Light);
        holder.Folio.setText(p.getFolio());
        holder.Estatus.setText(p.getEstatus());
        holder.reportado.setTypeface(Light);
        holder.recibido.setTypeface(Light);
        holder.atendido.setTypeface(Light);
        holder.proceso.setTypeface(Light);
        holder.contador_comentarios.setText(String.valueOf(p.getComentario()));
        holder.contador_like.setText(String.valueOf(p.getLikes()));

        if(holder.Estatus.getText().equals("REPORTADO")){
            holder.p_reportado.setBackgroundResource(R.drawable.vista_bordeadoizq2); //seleccion
            holder.p_reportado.setTextColor(Color.parseColor("#FFFFFF")); //letra seleccion

            holder.p_recibido.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.p_recibido.setTextColor(Color.parseColor("#811BDA"));

            holder.p_proceso.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.p_proceso.setTextColor(Color.parseColor("#811BDA"));

            holder.p_atendido.setBackgroundResource(R.drawable.vista_bordeadoder);
            holder.p_atendido.setTextColor(Color.parseColor("#811BDA"));

        }
        if(holder.Estatus.getText().equals("RECIBIDO")){

            holder.p_reportado.setBackgroundResource(R.drawable.vista_bordeadoizq);
            holder.p_reportado.setTextColor(Color.parseColor("#811BDA"));

            holder.p_recibido.setBackgroundColor(Color.parseColor("#811BDA")); //seleccion
            holder.p_recibido.setTextColor(Color.parseColor("#FFFFFF"));//letra seleccion

            holder.p_proceso.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.p_proceso.setTextColor(Color.parseColor("#811BDA"));

            holder.p_atendido.setBackgroundResource(R.drawable.vista_bordeadoder);
            holder.p_atendido.setTextColor(Color.parseColor("#811BDA"));
        }
        if(holder.Estatus.getText().equals("PROCESO")){
            holder.p_reportado.setBackgroundResource(R.drawable.vista_bordeadoizq);
            holder.p_reportado.setTextColor(Color.parseColor("#811BDA"));

            holder.p_recibido.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.p_recibido.setTextColor(Color.parseColor("#811BDA"));

            holder.p_proceso.setBackgroundColor(Color.parseColor("#811BDA")); //seleccion
            holder.p_proceso.setTextColor(Color.parseColor("#FFFFFF"));//letra seleccion

            holder.p_atendido.setBackgroundResource(R.drawable.vista_bordeadoder);
            holder.p_atendido.setTextColor(Color.parseColor("#811BDA"));
        }
        if(holder.Estatus.getText().equals("ATENDIDO")){
            holder.p_reportado.setBackgroundResource(R.drawable.vista_bordeadoizq);
            holder.p_reportado.setTextColor(Color.parseColor("#811BDA"));

            holder.p_recibido.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.p_recibido.setTextColor(Color.parseColor("#811BDA"));

            holder.p_proceso.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.p_proceso.setTextColor(Color.parseColor("#811BDA"));

            holder.p_atendido.setBackgroundResource(R.drawable.vista_bordeadoder2); // color de seleccion
            holder.p_atendido.setTextColor(Color.parseColor("#FFFFFF")); //color de letra seleccion
        }


        Glide.with(context_u)
                .load(p.getFoto())
                .fitCenter()
                //.centerCrop()
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
            intent.putExtra("id_reporte",dataset2.get(pos).getId_reporte());
            intent.putExtra("folio",dataset2.get(pos).getFolio());
            intent.putExtra("categoria",dataset2.get(pos).getCategoria());
            intent.putExtra("direccion",dataset2.get(pos).getDireccion());
            intent.putExtra("cruzamiento",dataset2.get(pos).getCruzamiento());
            intent.putExtra("colonia",dataset2.get(pos).getColonia());
            intent.putExtra("descripcion",dataset2.get(pos).getDescripcion());
            intent.putExtra("usuario",dataset2.get(pos).getUsuario());
            intent.putExtra("latitud",dataset2.get(pos).getLatitud());
            intent.putExtra("longitud",dataset2.get(pos).getLongitud());
            intent.putExtra("fecha",dataset2.get(pos).getFecha());
            intent.putExtra("estatus",dataset2.get(pos).getEstatus());
            intent.putExtra("hora",dataset2.get(pos).getHora());
            intent.putExtra("foto",dataset2.get(pos).getFoto());
            intent.putExtra("likes",dataset2.get(pos).getLikes());
            intent.putExtra("likes_usuario",dataset2.get(pos).getLike_usuario());
            v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset2.size();
    }

  /*  @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new CustomFilter(this,filterList);
        }

        return filter;
    }*/


    public class VieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView FotoimageView;
        private TextView Categoria;
        private TextView Direccion;
        private TextView Fecha;
        private TextView Folio;
        private TextView Estatus;
        private SegmentedButtonGroup sbg;
        private Button reportado, recibido, proceso, atendido;
        private Button p_reportado, p_recibido, p_proceso, p_atendido;
        private ImageView iconoCategoria;
        private TextView contador_comentarios;
        private TextView contador_like;
        Context ctx;
        private LinearLayout LinearItemDatos;
        private RadioRealButton RadioRealButtonRecibido, RadioRealButtonProceso, RadioRealButtonReportado,RadioRealButtonAtendido;
       // ArrayList<datosReporte> listadatito=new ArrayList<>();
        ArrayList<Datos> listadatito2=new ArrayList<>();
       // ArrayList<Datos>listadatitoCache;
        ItemClickListener itemClickListener;
        public VieHolder(View itemView, Context ctx, ArrayList<Datos> dataset) {
            super(itemView);
          // this.listadatito=dataset;
            this.listadatito2=dataset;
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
            reportado=(Button)itemView.findViewById(R.id.reportado);
            recibido=(Button)itemView.findViewById(R.id.recibido);
            proceso=(Button)itemView.findViewById(R.id.en_proceso);
            atendido=(Button)itemView.findViewById(R.id.atendido);
            iconoCategoria=(ImageView)itemView.findViewById(R.id.icono_categoria);
            LinearItemDatos=(LinearLayout)itemView.findViewById(R.id.layout_datos);
            contador_comentarios=(TextView)itemView.findViewById(R.id.tv_comentario);
            contador_like=(TextView)itemView.findViewById(R.id.tv_megusta);
            RadioRealButtonReportado= (RadioRealButton)itemView.findViewById(R.id.RealButtonReportado);
            RadioRealButtonProceso= (RadioRealButton)itemView.findViewById(R.id.RealButtonProceso);
            RadioRealButtonRecibido= (RadioRealButton)itemView.findViewById(R.id.RealButtonrecibido);
            RadioRealButtonAtendido= (RadioRealButton)itemView.findViewById(R.id.RealButtonAtendido);

            p_reportado=(Button)itemView.findViewById(R.id.prueba_reportado);
            p_recibido=(Button)itemView.findViewById(R.id.prueba_recibido);
            p_proceso=(Button)itemView.findViewById(R.id.prueba_proceso);
            p_atendido=(Button)itemView.findViewById(R.id.prueba_atendido);


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
    public void adicionarListaDatos(ArrayList<Datos> listaDat) {
        dataset2.addAll(listaDat);
        notifyDataSetChanged();
    }
   /* public void adicionarListaDatosCache(ArrayList<Datos> listaDat) {
        datasetCache.addAll(listaDat);
        notifyDataSetChanged();
    }*/
    public void setFilterCache(ArrayList<Datos> listareporte){
        this.dataset2=new ArrayList<>();
        this.dataset2.addAll(listareporte);
        notifyDataSetChanged();
    }
    public void setFilterForMarker(ArrayList<Datos> listareporte){
        this.dataset2=new ArrayList<>();
        this.dataset2.addAll(listareporte);
        notifyDataSetChanged();
    }
}
