package com.example.adolforangelmarcelino.fbapi.Adpaters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adolforangelmarcelino.fbapi.Models.Datum;
import com.example.adolforangelmarcelino.fbapi.R;

import java.util.List;


/**
 * Created by adolforangelmarcelino on 18/02/18.
 */

public class Albumsadapter extends RecyclerView.Adapter<Albumsadapter.ViewHolder>{
    private List<Datum> listAlbuns;
    Context context;
    LayoutInflater layoutInflater;


    public Albumsadapter(List<Datum> list, Context context) {
        this.listAlbuns = list;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_albun_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(listAlbuns.get(position));


    }

    @Override
    public int getItemCount() {
        return listAlbuns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView albun, tamano, description;
        public ViewHolder(View view){
            super(view);
            albun=view.findViewById(R.id.albun);
            tamano=view.findViewById(R.id.tamano);
            description = view.findViewById(R.id.description);
        }

        public void setData(Datum datum){
            albun.setText(datum.getName());
            tamano.setText(datum.getCount().toString());
            description.setText(datum.getDescription());
        }

    }
}
