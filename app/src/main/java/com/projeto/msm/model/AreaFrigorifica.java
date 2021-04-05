package com.projeto.msm.model;

public class AreaFrigorifica {
    private String designacao, fabricante;
    private int numero, id, tem_min, tem_max;

    public AreaFrigorifica() {
        this.designacao = null;
        this.fabricante = null;
        this.numero = -1;
        this.id = -1;
        this.tem_min = -1;
        this.tem_max = -1;
    }

    public AreaFrigorifica(String designacao, String fabricante, int numero, int id, int tem_min, int tem_max) {
        this.designacao = designacao;
        this.fabricante = fabricante;
        this.numero = numero;
        this.id = id;
        this.tem_min = tem_min;
        this.tem_max = tem_max;
    }

    public String getDesignacao() {
        return designacao;
    }

    public String getFabricante() {
        return fabricante;
    }

    public int getNumero() {
        return numero;
    }

    public int getId() {
        return id;
    }

    public int getTem_min() {
        return tem_min;
    }

    public int getTem_max() {
        return tem_max;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTem_min(int tem_min) {
        this.tem_min = tem_min;
    }

    public void setTem_max(int tem_max) {
        this.tem_max = tem_max;
    }

    @Override
    public String toString() {
        return "AreaFrigorifica{" +
                "designacao='" + designacao + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", numero=" + numero +
                ", id=" + id +
                ", tem_min=" + tem_min +
                ", tem_max=" + tem_max +
                '}';
    }
}
