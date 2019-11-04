package com.android.sga.reportapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sga.reportapp.R;
import com.android.sga.reportapp.gson.Comentarios;
import com.android.sga.reportapp.utileries.TypefacesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class Adaptercomentar extends RecyclerView.Adapter<Adaptercomentar.VieHolder>{
    private ArrayList<Comentarios> dataset=new ArrayList<>();
    private Context context;
    public Adaptercomentar(Context context)
    {
        this.context=context;
        this.dataset=new ArrayList<>();

    }
    public void adicionarAdaptercomentar(ArrayList<Comentarios> Listacomentario) {
        dataset.addAll(Listacomentario);
        notifyDataSetChanged();
        //dataset.clear();
    }
    @NonNull
    @Override
    public Adaptercomentar.VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario,parent,false);
        Adaptercomentar.VieHolder vieHolder=new Adaptercomentar.VieHolder(view,dataset);
        return vieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptercomentar.VieHolder holder, int position) {
        Typeface Light = TypefacesUtils.get(context, "fonts/MavenPro-Regular.ttf");
        Typeface Bold = TypefacesUtils.get(context, "fonts/MavenPro-Bold.ttf");
        Comentarios p=dataset.get(position);
        holder.nombre.setText(p.getUsuario());
        holder.nombre.setTypeface(Bold);
        holder.fecha.setText(p.getTiempo());
        holder.fecha.setTypeface(Light);
        holder.comentario.setText(p.getComentarios());
        holder.comentario.setTypeface(Light);

        Glide.with(context)
                .load(p.img_usuario)
                //.centerCrop()
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .into(holder.fotoimageView);

    }

    @Override
    public int getItemCount() { return dataset.size(); }

    public void remover(){
        dataset.clear();
    }

    public class VieHolder extends RecyclerView.ViewHolder {
        ArrayList<Comentarios> listaDatoscomentario=new ArrayList<>();
        private ImageView fotoimageView;
        private TextView nombre;
        private TextView fecha;
        private TextView comentario;

        public VieHolder(@NonNull View itemView,ArrayList<Comentarios> Listacomentario) {
            super(itemView);
            this.listaDatoscomentario=Listacomentario;

            fotoimageView=(ImageView)itemView.findViewById(R.id.fotoImageView);
            nombre=(TextView) itemView.findViewById(R.id.txtNomcomen);
            fecha=(TextView) itemView.findViewById(R.id.txtFechcomen);
            comentario=(TextView)itemView.findViewById(R.id.txtcomentariouser);
        }
    }
}
