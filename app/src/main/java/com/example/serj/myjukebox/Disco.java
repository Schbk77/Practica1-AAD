package com.example.serj.myjukebox;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.Collator;
import java.util.Locale;

public class Disco implements Comparable<Disco>, Parcelable{

    private String titulo;
    private String artista;
    private String anio;
    private String genero;
    private String caratula;

    public Disco() {}

    public Disco(Parcel p){
        this.titulo = p.readString();
        this.artista = p.readString();
        this.anio = p.readString();
        this.genero = p.readString();
        this.caratula = p.readString();
    }

    public Disco(String titulo, String artista, String anio, String genero, String caratula) {
        this.titulo = titulo;
        this.artista = artista;
        this.anio = anio;
        this.genero = genero;
        this.caratula = caratula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    @Override
    public int compareTo(Disco disco) {
        Collator coll = Collator.getInstance(Locale.getDefault());
        int ct = coll.compare(this.titulo, disco.titulo);
        if(ct < 0){
            return -1;
        }else if(ct > 0){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        Disco disco = (Disco) o;

        if (artista != null ? !artista.equals(disco.artista) : disco.artista != null) return false;
        if (titulo != null ? !titulo.equals(disco.titulo) : disco.titulo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = titulo != null ? titulo.hashCode() : 0;
        result = 31 * result + (artista != null ? artista.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Disco{" +
                "titulo='" + titulo + '\'' +
                ", artista='" + artista + '\'' +
                ", anio='" + anio + '\'' +
                ", genero='" + genero + '\'' +
                ", caratula=" + caratula +
                '}';
    }

    public static final Parcelable.Creator<Disco> CREATOR =new Parcelable.Creator<Disco>(){

        @Override
        public Disco createFromParcel(Parcel source) {
            return new Disco();
        }

        @Override
        public Disco[] newArray(int size) {
            return new Disco[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(titulo);
        parcel.writeString(artista);
        parcel.writeString(anio);
        parcel.writeString(genero);
        parcel.writeString(caratula);
    }
}