package com.example.serj.myjukebox;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Mostrar extends Activity {
    private String titulo, artista, anio, genero, caratula;
    private ImageView iv;
    private TextView tv1, tv2, tv3, tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        initComponents();
    }

    private void initComponents(){
        iv = (ImageView)findViewById(R.id.ivMostrarCaratula);
        tv1 = (TextView)findViewById(R.id.tvMostrarTitulo);
        tv2 = (TextView)findViewById(R.id.tvMostrarArtista);
        tv3 = (TextView)findViewById(R.id.tvMostrarAnio);
        tv4 = (TextView)findViewById(R.id.tvMostrarGenero);
        getExtras();
    }

    private void getExtras(){
        Bundle b = getIntent().getExtras();
        if(b != null){
            titulo = b.getString(getString(R.string.tagTitulo));
            artista = b.getString(getString(R.string.tagArtista));
            anio = b.getString(getString(R.string.tagAnio));
            genero = b.getString(getString(R.string.tagGenero));
            caratula = b.getString(getString(R.string.tagCaratula));
        }
        tv1.setText(titulo);
        tv2.setText(artista);
        tv3.setText(anio);
        tv4.setText(genero);
        try{
            if(Principal.isInteger(caratula)){
                Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), Integer.parseInt(caratula));
                iv.setImageBitmap(myBitmap);
            }else{
                Bitmap bitmap = BitmapFactory.decodeFile(caratula);
                iv.setImageBitmap(bitmap);
            }
        }catch (NumberFormatException e){}
    }
}
