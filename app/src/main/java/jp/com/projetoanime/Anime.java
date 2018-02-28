package jp.com.projetoanime;

import java.io.Serializable;

/**
 * Created by João Paulo on 09/02/2018.
 */

public class Anime implements Serializable{

    private String nome;

    private int ep;

    private String notas;

    private int temp;

    private String image;


    public Anime(String nome, int ep, String notas, int temp, String image) {
        this.nome = nome;
        this.ep = ep;
        this.notas = notas;
        this.temp = temp;
        this.image = image;
    }

    public Anime() {};

    public String getNome() {
        return nome;
    }

    public int getEp() {
        return ep;
    }

    public String getNotas() {
        return notas;
    }

    public int getTemp() {
        return temp;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEp(int ep) {
        this.ep = ep;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
