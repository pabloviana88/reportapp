package com.android.sga.reportapp.Adapters;

import android.widget.Filter;

import com.android.sga.reportapp.gson.datosReporte;

import java.util.ArrayList;

public class CustomFilter extends Filter {
    RecyclerViewAdapterDatos adapter;
    ArrayList<datosReporte> filterList;

    public CustomFilter(RecyclerViewAdapterDatos adapter, ArrayList<datosReporte> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint !=null && constraint.length()>0){
            constraint=constraint.toString().toLowerCase();
            ArrayList<datosReporte> filteredeDat = new ArrayList<>();
            for (int i= 0; i<filterList.size(); i++){
                //aqui sale
                if (filterList.get(i).getDireccion().toLowerCase().contains(constraint)){
                    filteredeDat.add(filterList.get(i));
                }
            }
            results.count=filteredeDat.size();
            results.values=filteredeDat;
        }
        else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.dataset=(ArrayList<datosReporte>) results.values;
        adapter.notifyDataSetChanged();
    }
}
