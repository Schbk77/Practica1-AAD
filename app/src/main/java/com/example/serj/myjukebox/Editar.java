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
    private ArrayList<Disco> discos;
    private int aux;
    private EditText et1, et2, et3, et4;
    private ImageButton ib;
    private static int RESULT_LOAD_IMAGE = 1;
    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editar);
        initComponents();
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
            titulo = b.getString("titulo");
            artista = b.getString("artista");
            anio = b.getString("anio");
            genero = b.getString("genero");
            caratula = b.getString("caratula");
            discos = b.getParcelableArrayList("ArrayList");
            aux = b.getInt("pos");
        }
        et1.setText(titulo);
        et2.setText(artista);
        et3.setText(anio);
        et4.setText(genero);
        try{
            Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), Integer.parseInt(caratula));
            ib.setImageBitmap(myBitmap);
        }catch (NumberFormatException e){}
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
            if(picturePath != null){
                caratula = picturePath;
            }
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("pos", aux);
            bundle.putString("titulo", titulo);
            bundle.putString("artista", artista);
            bundle.putString("anio", anio);
            bundle.putString("genero", genero);
            bundle.putString("caratula", caratula);
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
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap myBitmap = BitmapFactory.decodeFile(picturePath);
            ib.setImageBitmap(myBitmap);
        }
    }
}
