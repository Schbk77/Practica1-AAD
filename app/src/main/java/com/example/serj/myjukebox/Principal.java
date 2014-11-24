package com.example.serj.myjukebox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

    /**********************************************************************************************/
    /**************************************VARIABLES***********************************************/
    /**********************************************************************************************/

    private ArrayList<Disco> discos;               //Variable donde almaceno la biblioteca de discos
    private Adaptador ad;                          //Adaptador para objetos de tipo Disco
    private ListView lv;                           //ListView del layout
    private final int EDITAR_DISCO = 1;            //Código de la actividad Editar

    /**********************************************************************************************/
    /**************************************ON...***************************************************/
    /**********************************************************************************************/

    //PANTALLA PRINCIPAL____________________________________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inicializa los componentes del layout principal
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        initComponents();
    }

    //ACTION BAR____________________________________________________________________________________
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menu de la ActionBar
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Realiza la acción que se elija del menú de la ActionBar
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            return anadir();
        }
        return super.onOptionsItemSelected(item);
    }

    //MENU CONTEXTUAL_______________________________________________________________________________
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //Infla el menu que se visualiza al hacer longClick en un elemento del ListView
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuopciones, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Realiza la acción que se elija del menu contextual
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion = info.position;
        if(id == R.id.opEditar){
            return editar(posicion);
        }else if(id == R.id.opBorrar) {
            return borrar(posicion);
        }
        return super.onContextItemSelected(item);
    }

    //RESULTADO LANZAR INTENT_______________________________________________________________________
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Segun el resultado de otra actividad, realiza una acción determinada
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            int aux = data.getIntExtra(getString(R.string.tagPosicion), 0);
            String titulo = data.getStringExtra(getString(R.string.tagTitulo));
            String artista = data.getStringExtra(getString(R.string.tagArtista));
            String anio = data.getStringExtra(getString(R.string.tagAnio));
            String genero = data.getStringExtra(getString(R.string.tagGenero));
            String caratula = data.getStringExtra(getString(R.string.tagCaratula));
            Disco d = new Disco(titulo, artista, anio, genero, caratula);
            switch (requestCode){
                case EDITAR_DISCO:{
                    //Controla registros únicos y modifica los datos del disco
                    if(!discos.contains(d) || !discos.get(aux).getCaratula().equals(caratula)){
                        discos.set(aux, d);
                        guardarXML();
                        ad.notifyDataSetChanged();
                        tostada(getString(R.string.discomodificado), this);
                    }else{
                        tostada(getString(R.string.discoExiste), this);
                    }
                }
            }
        }
    }

    //ORIENTACIÓN___________________________________________________________________________________
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //Recupera el Bundle guardado
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Guarda en el Bundle con el cambio de estado
        super.onSaveInstanceState(outState);
    }

    /**********************************************************************************************/
    /***************************************EDICIÓN************************************************/
    /**********************************************************************************************/

    private boolean anadir(){
        //Método que crea un AlertDialog con un layout personalizado y añade un disco nuevo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View entradaTexto = inflater.inflate(R.layout.dialog_anadir, null);
        builder.setTitle(getString(R.string.nuevodisco));
        builder.setView(entradaTexto);
        final EditText et1 = (EditText)entradaTexto.findViewById(R.id.etTitulo);
        final EditText et2 = (EditText)entradaTexto.findViewById(R.id.etArtista);
        final EditText et3 = (EditText)entradaTexto.findViewById(R.id.etAnio);
        final EditText et4 = (EditText)entradaTexto.findViewById(R.id.etGenero);
        builder.setPositiveButton(getString(R.string.btanadir), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Controla que los campos del EditText no estén vacios
                if (!((String)et1.getText().toString()).isEmpty() &&
                        !((String)et2.getText().toString()).isEmpty() &&
                        !((String)et3.getText().toString()).isEmpty() &&
                        !((String)et4.getText().toString()).isEmpty()) {
                    Disco nuevoDisco = new Disco();
                    nuevoDisco.setTitulo(et1.getText().toString());
                    nuevoDisco.setArtista(et2.getText().toString());
                    nuevoDisco.setAnio(et3.getText().toString());
                    nuevoDisco.setGenero(et4.getText().toString());
                    nuevoDisco.setCaratula(R.drawable.vinilo+"");
                    //Controla registros únicos y añade un Disco nuevo
                    if(!discos.contains(nuevoDisco)){
                        discos.add(nuevoDisco);
                        guardarXML();
                        ad.notifyDataSetChanged();
                    }else {
                        tostada(getString(R.string.discoExiste), getApplicationContext());
                    }
                }else{
                    tostada(getString(R.string.tostadaaniadirerror), getApplicationContext());
                }
            }
        });
        builder.setNegativeButton(getString(R.string.btcancelar),null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public boolean borrar(final int pos){
        //Método que crea un AlertDialog que nos permite borrar un Disco
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.dialog_message);
        alert.setTitle(R.string.dialog_title);
        alert.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                discos.remove(pos);
                guardarXML();
                ad.notifyDataSetChanged();
                tostada(getString(R.string.discoborrado), getApplicationContext());
            }
        });
        alert.setNegativeButton(R.string.no,null);
        AlertDialog dialog = alert.create();
        dialog.show();
        return true;
    }

    public boolean editar(final int pos){
        //Método que lanza una actividad para obtener un resultado
        Intent nuevoIntent = new Intent(this, Editar.class);
        Bundle b = new Bundle();
        b.putString(getString(R.string.tagTitulo), discos.get(pos).getTitulo());
        b.putString(getString(R.string.tagArtista), discos.get(pos).getArtista());
        b.putString(getString(R.string.tagAnio), discos.get(pos).getAnio());
        b.putString(getString(R.string.tagGenero), discos.get(pos).getGenero());
        b.putString(getString(R.string.tagCaratula), discos.get(pos).getCaratula());
        b.putInt(getString(R.string.tagPosicion), pos);
        nuevoIntent.putExtras(b);
        startActivityForResult(nuevoIntent, EDITAR_DISCO);
        return true;
    }

    public void mostrar(final int pos){
        //Método que lanza una actividad pasando datos
        Intent nuevoIntent = new Intent(this, Mostrar.class);
        Bundle b = new Bundle();
        b.putString(getString(R.string.tagTitulo), discos.get(pos).getTitulo());
        b.putString(getString(R.string.tagArtista), discos.get(pos).getArtista());
        b.putString(getString(R.string.tagAnio), discos.get(pos).getAnio());
        b.putString(getString(R.string.tagGenero), discos.get(pos).getGenero());
        b.putString(getString(R.string.tagCaratula), discos.get(pos).getCaratula());
        nuevoIntent.putExtras(b);
        startActivity(nuevoIntent);
    }

    /**********************************************************************************************/
    /*****************************************XML**************************************************/
    /**********************************************************************************************/

    private void guardarXML (){
        //Actualiza el archivo .xml guardado en la memoria externa privada
        //Recoge los datos almacenados en el ArrayList para actualizar el .xml
        File file = new File(getExternalFilesDir(null), "archivo.xml");
        FileOutputStream fosxml = null;
        try {
            fosxml = new FileOutputStream(file);
        }catch (FileNotFoundException e){
            tostada("No existe el archivo", this);
        }
        XmlSerializer docxml = Xml.newSerializer();
        try {
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, true);
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "discos");

            for(int i = 0; i<discos.size(); i++){
                docxml.startTag(null, "disco");
                docxml.startTag(null, "titulo");
                docxml.text(discos.get(i).getTitulo());
                docxml.endTag(null, "titulo");
                docxml.startTag(null, "artista");
                docxml.text(discos.get(i).getArtista());
                docxml.endTag(null, "artista");
                docxml.startTag(null, "anio");
                docxml.text(discos.get(i).getAnio());
                docxml.endTag(null, "anio");
                docxml.startTag(null, "genero");
                docxml.text(discos.get(i).getGenero());
                docxml.endTag(null, "genero");
                docxml.startTag(null, "caratula");
                docxml.text(discos.get(i).getCaratula());
                docxml.endTag(null, "caratula");
                docxml.endTag(null, "disco");
            }
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        }catch (IOException e){

        }
    }

    private void leerXML (){
        //Lee desde el archivo .xml guardado en la memoria externa privada
        //Guarda en el ArrayList los Discos almacenados en el archivo
        Disco d = new Disco();
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
    }

    /**********************************************************************************************/
    /***********************************MÉTODOS AUXILIARES*****************************************/
    /**********************************************************************************************/

    private void initComponents() {
        //Inicializa el ArrayList con los datos del archivo .xml
        //Añade el Adaptador al ListView
        discos = new ArrayList<Disco>();
        leerXML();
        lv = (ListView)findViewById(R.id.lvLista);
        ad = new Adaptador(this, R.layout.lista, discos);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrar(i);
            }
        });
        registerForContextMenu(lv);
    }

    //Método para mostrar una tostada con el string que queramos en cualquier Clase
    public static void tostada(String s, Context c){
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }

    //Método para saber si un String es un Integer
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

    /*public void crearXMLysetDefaultCDs() {

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
    }*/
}