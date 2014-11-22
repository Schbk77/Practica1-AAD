package com.example.serj.myjukebox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Disco>{

    private Context contexto;
    private ArrayList<Disco> discos;
    private int recurso;
    private static LayoutInflater i;

    public static class ViewHolder {
        public TextView tv1, tv2, tv3;
        public ImageView iv;
    }

    public Adaptador(Context context, int resource, ArrayList<Disco> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.recurso = resource;
        this.discos = objects;
        this.i = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh =  null;

        if(convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tv1 = (TextView)convertView.findViewById(R.id.tvTitulo);
            vh.tv2 = (TextView)convertView.findViewById(R.id.tvArtista);
            vh.tv3 = (TextView)convertView.findViewById(R.id.tvAnio);
            vh.iv = (ImageView)convertView.findViewById(R.id.ivCaratula);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tv1.setText(discos.get(position).getTitulo());
        vh.tv2.setText(discos.get(position).getArtista());
        vh.tv3.setText(discos.get(position).getAnio());
        try{
            Bitmap myBitmap = BitmapFactory.decodeResource(contexto.getResources(), Integer.parseInt(discos.get(position).getCaratula()));
            vh.iv.setImageBitmap(myBitmap);
        }catch (NumberFormatException e){

        }


        return convertView;
    }
}
