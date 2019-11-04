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

import com.android.sga.reportapp.Additional.DetalleMisReportes;
import com.android.sga.reportapp.R;
import com.android.sga.reportapp.SQLite.Datos.Datos;
import com.android.sga.reportapp.gson.DatosGenerales;
import com.android.sga.reportapp.gson.datosReporte;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class RecyclerViewAdapterNotificationCache extends RecyclerView.Adapter<RecyclerViewAdapterNotificationCache.VieHolder>{
    private ArrayList<DatosGenerales> dataset=new ArrayList<>();
    private ArrayList<datosReporte> dataset2;
    private ArrayList<Datos> dataset3;
    private Context context;
    private Context ctx;

    public RecyclerViewAdapterNotificationCache(Context context, Context ctx){
        this.context=context;
        this.ctx=ctx;
        dataset=new ArrayList<>();
        dataset2=new ArrayList<>();
        this.dataset3=new ArrayList<>();
    }
    @NonNull
    @Override
    public RecyclerViewAdapterNotificationCache.VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion,parent,false);
        VieHolder vieHolder=new VieHolder(view,ctx,dataset3);
        return vieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterNotificationCache.VieHolder holder, int position) {
        Typeface Light = TypefacesUtils.get(context, "fonts/MavenPro-Regular.ttf");
        Typeface Bold = TypefacesUtils.get(context, "fonts/MavenPro-Bold.ttf");

        Datos p=dataset3.get(position);
        //  String tipos=String.valueOf(holder.tipo).toString();
        holder.nombreN.setText("Reporte #"+p.getFolio());
        holder.nombreN.setTypeface(Bold);
       // holder.tipo.setText(String.valueOf("# "+p.getFolio()));
        holder.tipo.setText(p.getEstatus());
        holder.tipo.setTypeface(Bold);
        holder.Rfecha.setText(p.getFecha());
        holder.Rfecha.setTypeface(Light);
        //holder.EstatusN.setText("Tu reporte ha sido recibo "+p.getEstatus().toUpperCase());
        if (p.getEstatus().equals("REPORTADO")){
            holder.EstatusN.setText("Tu reporte ha sido enviado correctamente");
            /*holder.EstatusN.setTypeface(Light);*/}
        else if (p.getEstatus().equals("RECIBIDO")){
            holder.EstatusN.setText("Tu reporte ha sido recibido y se encuentra en verificación");
            /*holder.EstatusN.setTypeface(Light);*/}
        else if (p.getEstatus().equals("PROCESO")){
            holder.EstatusN.setText("Tu reporte se encuentra en proceso de atención");
            /*holder.EstatusN.setTypeface(Light);*/}
        else  if (p.getEstatus().equals("ATENDIDO")){
            holder.EstatusN.setText("Tu reporte ha sido atendido");
            /*holder.EstatusN.setTypeface(Light);*/}
        else if (p.getEstatus().equals("CANCELADO")){
            holder.EstatusN.setText("Tu reporte fue cancelado");
            /*holder.EstatusN.setTypeface(Light);*/}

            Glide.with(context)
                    .load(p.getFoto())
                    //.centerCrop()
                    .fitCenter()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .into(holder.fotoimageView);

    }

    @Override
    public int getItemCount() {
        return dataset3.size();

    }

    public void adicionarListaDatos(ArrayList<Datos> listadatos) {
       // dataset.addAll(listadatos);
        dataset3.addAll(listadatos);
        notifyDataSetChanged();
    }

    public class VieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView fotoimageView;
        private TextView tipo;
        private TextView EstatusN;
        private TextView Rfecha;
        private TextView nombreN;
        private String tipos;


        Context ctx;
        ArrayList<Datos> listaDatosgenarales=new ArrayList<>();

        public VieHolder(View view, Context ctx, ArrayList<Datos> Listadatos){
            super(view);
            this.listaDatosgenarales=Listadatos;
            this.ctx=ctx;
            itemView.setOnClickListener(this);
            fotoimageView=(ImageView)itemView.findViewById(R.id.fotoImageViewN);
            nombreN=(TextView)itemView.findViewById(R.id.nombreN);
            tipo=(TextView) itemView.findViewById(R.id.FolioN);
            Rfecha=(TextView)itemView.findViewById(R.id.Rfecha);
//el ultimo cambio
            EstatusN=(TextView)itemView.findViewById(R.id.estatusN);

        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
           Datos pokemon=this.listaDatosgenarales.get(position);

            Intent intent= new Intent(this.ctx,DetalleMisReportes.class);
            intent.putExtra("id",pokemon.getId());
            intent.putExtra("id_reporte",pokemon.getId_reporte());
            intent.putExtra("folio",pokemon.getFolio());
            intent.putExtra("categoria",pokemon.getCategoria());
            intent.putExtra("tipo",pokemon.getTipo());
            intent.putExtra("direccion",pokemon.getDireccion());
            intent.putExtra("cruzamiento",pokemon.getCruzamiento());
            intent.putExtra("colonia",pokemon.getColonia());
            intent.putExtra("descripcion",pokemon.getDescripcion());
            intent.putExtra("usuario",pokemon.getUsuario());
            intent.putExtra("latitud",pokemon.getLatitud());
            intent.putExtra("longitud",pokemon.getLongitud());
            intent.putExtra("fecha",pokemon.getFecha());
            intent.putExtra("estatus",pokemon.getEstatus());
            intent.putExtra("hora",pokemon.getHora());
            intent.putExtra("foto",pokemon.getFoto());
            this.ctx.startActivity(intent);


        }
    }
}
