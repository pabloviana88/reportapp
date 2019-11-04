package com.android.sga.reportapp.Adapters;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.sga.reportapp.R;
import com.android.sga.reportapp.gson.Comentarios;
import com.android.sga.reportapp.gson.Historial;
import com.android.sga.reportapp.utileries.TypefacesUtils;

import java.util.ArrayList;

public class AdapterHistorial extends RecyclerView.Adapter<AdapterHistorial.VieHolder> {
    private ArrayList<Comentarios> dataset=new ArrayList<>();
    private Context context;

    public AdapterHistorial(Context context)
    {
        this.context=context;
        this.dataset=new ArrayList<>();

    }
    public void adicionarHistorial(ArrayList<Comentarios> ListaHistorial) {
        dataset.addAll(ListaHistorial);
        notifyDataSetChanged();
        //dataset.clear();
    }
    @NonNull
    @Override
    public VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemhistorial,parent,false);
        AdapterHistorial.VieHolder vieHolder=new AdapterHistorial.VieHolder(view,dataset);
        return vieHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterHistorial.VieHolder holder, int position) {
        Typeface Light = TypefacesUtils.get(context, "fonts/MavenPro-Regular.ttf");
        Typeface Bold = TypefacesUtils.get(context, "fonts/MavenPro-Bold.ttf");
        Comentarios p=dataset.get(position);
        holder.estaus.setText(p.getId_comentario());
        holder.estaus.setTypeface(Light);
        holder.fecha.setText(p.getImg_usuario());
        holder.fecha.setTypeface(Light);
        holder.descripcion.setText(p.getComentarios());
        holder.descripcion.setTypeface(Light);
        holder.observaciones.setText(p.getTiempo());
        holder.observaciones.setTypeface(Light);
    }

    @Override
    public int getItemCount() { return dataset.size(); }

    public void remover(){
        dataset.clear();
    }
    public class VieHolder extends  RecyclerView.ViewHolder {
        ArrayList<Comentarios> listaDatosHistorial=new ArrayList<>();
        // private ImageView fotoimageView;
        private TextView estaus;
        private TextView fecha;
        private TextView descripcion;
        private TextView observaciones;

        public VieHolder(@NonNull View itemView,ArrayList<Comentarios> ListaHistorial) {
            super(itemView);
            this.listaDatosHistorial=ListaHistorial;
         //   fotoimageView=(ImageView)itemView.findViewById(R.id.fotoImageView);
            estaus=(TextView) itemView.findViewById(R.id.txtHestatus);
            fecha=(TextView) itemView.findViewById(R.id.txtHfecha);
            descripcion=(TextView)itemView.findViewById(R.id.txtHdescripcion);
            observaciones=(TextView)itemView.findViewById(R.id.txtHobservaciones);
        }
    }
}
