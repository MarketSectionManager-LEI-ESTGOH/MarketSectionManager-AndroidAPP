package com.projeto.msm.model;

import java.io.Serializable;

public class Componente{
    private String designacao;
    private int id;
    private String data;

    public Componente(String designacao, int id, String data) {
        this.designacao = designacao;
        this.id = id;
        this.data = data;
    }

    public Componente() {
        this.designacao = "Vazio";
        this.id = -1;
        this.data = "00-00-00";
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignacao() {
        return designacao;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Componente{" +
                "designacao='" + designacao + '\'' +
                ", id=" + id +
                ", data='" + data + '\'' +
                '}';
    }
}
