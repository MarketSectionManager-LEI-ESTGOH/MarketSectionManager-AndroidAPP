package com.projeto.msm.model;

import java.io.Serializable;

public class Componente{
    private String designacao;
    private int id;

    public Componente(String designacao, int id) {
        this.designacao = designacao;
        this.id = id;
    }

    public Componente() {
        this.designacao = "Vazio";
        this.id = -1;
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
}
