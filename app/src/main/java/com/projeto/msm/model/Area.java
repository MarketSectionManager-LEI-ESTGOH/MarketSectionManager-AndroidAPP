package com.projeto.msm.model;

import java.io.Serializable;

public class Area implements Serializable{
    private String designacao;
    private int numero, id;

    public Area(String designacao, int numero, int id) {
        this.designacao = designacao;
        this.numero = numero;
        this.id = id;
    }

    public Area() {
        this.designacao = "Vazio";
        this.numero = -1;
        this.id = -1;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignacao() {
        return designacao;
    }

    public int getNumero() {
        return numero;
    }

    public int getId() {
        return id;
    }

}
