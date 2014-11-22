package com.example.serj.myjukebox;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Principal extends Activity {
    private ArrayList<Disco> discos;                    //Variable donde almaceno la biblioteca de discos
    private Adaptador ad;                               //Adaptador para objetos de tipo Disco

    //PANTALLA PRINCIPAL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        initComponents();
        crearXMLysetDefaultCDs();//---------------------------------------------------------------crea discos por defecto
        //crearXML();
        leerXML();//--------------------------------------------------------------------------------leer xml si existe

    }

    //ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            tostada("Añadir", this);
            //return anadir();----------------------------------------------------------------------añadir al xml y arraylist
        }
        return super.onOptionsItemSelected(item);
    }

    //MENU CONTEXTUAL
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuopciones, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        if(id == R.id.opEditar){
            tostada("Editar:"+info.position, this);
            //return editar(info.position);---------------------------------------------------------editar item segun posicion
        }else if(id == R.id.opBorrar) {
            tostada("Borrar:"+info.position, this);
            //return borrar(info.position);---------------------------------------------------------borrar item segun posicion
        }
        return super.onContextItemSelected(item);
    }

    public void crearXML(){
        try {
            FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"archivo.xml"));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "discos");
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void leerXML (){
        Disco d = null;
        String etiqueta;
        XmlPullParser lectorxml = Xml.newPullParser();
        try {
            lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"archivo.xml")),"utf-8");
            int evento = lectorxml.getEventType();

            while (evento != XmlPullParser.END_DOCUMENT){
                if(evento == XmlPullParser.START_TAG){
                    etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("disco")==0){
                        d = new Disco();
                    }
                    if(etiqueta.compareTo("titulo")==0){
                        d.setTitulo(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo("artista")==0){
                        d.setArtista(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo("anio")==0){
                        d.setAnio(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo("genero")==0){
                        d.setGenero(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo("caratula")==0){
                        d.setCaratula(lectorxml.nextText());

                    }
                }else if(evento == XmlPullParser.END_TAG){
                    etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("disco")==0){
                        discos.add(d);
                    }
                }
                evento = lectorxml.next();


            }



        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("Size: ",((Integer)discos.size()).toString());
        ad.notifyDataSetChanged();
    }

    public void editarDeXML(){}

    public void borrarDeXML(){}

    public void crearXMLysetDefaultCDs() {

        try {
            FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"archivo.xml"));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "discos");

            docxml.startTag(null, "disco");
                docxml.startTag(null, "titulo");
                    docxml.text("Lonerism");
                docxml.endTag(null, "titulo");
                docxml.startTag(null, "artista");
                    docxml.text("Tame Impala");
                docxml.endTag(null, "artista");
                docxml.startTag(null, "anio");
                    docxml.text("2012");
                docxml.endTag(null, "anio");
                docxml.startTag(null, "genero");
                    docxml.text("Psychedelic");
                docxml.endTag(null, "genero");
                docxml.startTag(null, "caratula");
                    docxml.text(R.drawable.lnrsm+"");
                docxml.endTag(null, "caratula");
            docxml.endTag(null, "disco");

            docxml.startTag(null, "disco");
            docxml.startTag(null, "titulo");
            docxml.text("Led Zeppelin III");
            docxml.endTag(null, "titulo");
            docxml.startTag(null, "artista");
            docxml.text("Led Zeppelin");
            docxml.endTag(null, "artista");
            docxml.startTag(null, "anio");
            docxml.text("1970");
            docxml.endTag(null, "anio");
            docxml.startTag(null, "genero");
            docxml.text("Rock");
            docxml.endTag(null, "genero");
            docxml.startTag(null, "caratula");
            docxml.text(R.drawable.lziii+"");
            docxml.endTag(null, "caratula");
            docxml.endTag(null, "disco");

            docxml.startTag(null, "disco");
            docxml.startTag(null, "titulo");
            docxml.text("OK Computer");
            docxml.endTag(null, "titulo");
            docxml.startTag(null, "artista");
            docxml.text("Radiohead");
            docxml.endTag(null, "artista");
            docxml.startTag(null, "anio");
            docxml.text("1997");
            docxml.endTag(null, "anio");
            docxml.startTag(null, "genero");
            docxml.text("Rock");
            docxml.endTag(null, "genero");
            docxml.startTag(null, "caratula");
            docxml.text(R.drawable.ok+"");
            docxml.endTag(null, "caratula");
            docxml.endTag(null, "disco");

            docxml.startTag(null, "disco");
            docxml.startTag(null, "titulo");
            docxml.text("Ramones");
            docxml.endTag(null, "titulo");
            docxml.startTag(null, "artista");
            docxml.text("Ramones");
            docxml.endTag(null, "artista");
            docxml.startTag(null, "anio");
            docxml.text("1976");
            docxml.endTag(null, "anio");
            docxml.startTag(null, "genero");
            docxml.text("Punk");
            docxml.endTag(null, "genero");
            docxml.startTag(null, "caratula");
            docxml.text(R.drawable.rmns+"");
            docxml.endTag(null, "caratula");
            docxml.endTag(null, "disco");

            docxml.startTag(null, "disco");
            docxml.startTag(null, "titulo");
            docxml.text("Revolver");
            docxml.endTag(null, "titulo");
            docxml.startTag(null, "artista");
            docxml.text("The Beatles");
            docxml.endTag(null, "artista");
            docxml.startTag(null, "anio");
            docxml.text("1966");
            docxml.endTag(null, "anio");
            docxml.startTag(null, "genero");
            docxml.text("Rock");
            docxml.endTag(null, "genero");
            docxml.startTag(null, "caratula");
            docxml.text(R.drawable.rvlvr+"");
            docxml.endTag(null, "caratula");
            docxml.endTag(null, "disco");


            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        tostada("XML Creado en:" + getExternalFilesDir(null).toString(), this);
    }


    private void initComponents() {
        discos = new ArrayList<Disco>();
        final ListView lv = (ListView)findViewById(R.id.lvLista);
        ad = new Adaptador(this, R.layout.lista, discos);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tostada("Clic:", getApplicationContext());
                //mostrarDisco(i);------------------------------------------------------------------mostrar datos del disco
            }
        });
        registerForContextMenu(lv);
    }

    public static void tostada(String s, Context c){
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }
}