package com.example.serj.myjukebox;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class Editar extends Activity {

    private String titulo, artista, anio, genero, caratula;
    private int aux;
    private EditText et1, et2, et3, et4;
    private ImageButton ib;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editar);
        initComponents();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.tagCaratula), caratula);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        caratula = savedInstanceState.getString(getString(R.string.tagCaratula));
        setImagen(caratula);
    }

    private void initComponents(){
        et1 = (EditText)findViewById(R.id.etEditarTitulo);
        et2 = (EditText)findViewById(R.id.etEditarArtista);
        et3 = (EditText)findViewById(R.id.etEditarAnio);
        et4 = (EditText)findViewById(R.id.etEditarGenero);
        ib = (ImageButton)findViewById(R.id.ibEditarCaratula);
        getExtras();
        ImageButton cargarImagen = (ImageButton)findViewById(R.id.ibEditarCaratula);
        cargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lanza el selector de imagenes
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    private void getExtras(){
        Bundle b = getIntent().getExtras();
        if(b != null){
            titulo = b.getString(getString(R.string.tagTitulo));
            artista = b.getString(getString(R.string.tagArtista));
            anio = b.getString(getString(R.string.tagAnio));
            genero = b.getString(getString(R.string.tagGenero));
            caratula = b.getString(getString(R.string.tagCaratula));
            aux = b.getInt(getString(R.string.tagPosicion));
        }
        et1.setText(titulo);
        et2.setText(artista);
        et3.setText(anio);
        et4.setText(genero);
        setImagen(caratula);
    }

    public void guardarCambios(View view){
        if (!((String)et1.getText().toString()).isEmpty() &&
                !((String)et2.getText().toString()).isEmpty() &&
                !((String)et3.getText().toString()).isEmpty() &&
                !((String)et4.getText().toString()).isEmpty()) {
            titulo = et1.getText().toString();
            artista = et2.getText().toString();
            anio = et3.getText().toString();
            genero = et4.getText().toString();

            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.tagPosicion), aux);
            bundle.putString(getString(R.string.tagTitulo), titulo);
            bundle.putString(getString(R.string.tagArtista), artista);
            bundle.putString(getString(R.string.tagAnio), anio);
            bundle.putString(getString(R.string.tagGenero), genero);
            bundle.putString(getString(R.string.tagCaratula), caratula);
            i.putExtras(bundle);
            setResult(RESULT_OK, i);
            finish();
        }else{
            Principal.tostada(getString(R.string.tostadaaniadirerror), this);
        }
    }

    //Recoge la imagen escogida en el Intent del selector de imagenes de galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            caratula = cursor.getString(columnIndex);
            cursor.close();
            Bitmap myBitmap = BitmapFactory.decodeFile(caratula);
            ib.setImageBitmap(myBitmap);
        }
    }

    private void setImagen(String caratula){
        try{
            if(Principal.isInteger(caratula)){
                Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), Integer.parseInt(caratula));
                ib.setImageBitmap(myBitmap);
            }else{
                Bitmap bitmap = BitmapFactory.decodeFile(caratula);
                ib.setImageBitmap(bitmap);
            }
        }catch (NumberFormatException e){}
    }
}
