package com.projeto.msm.model;

public class AreaFrigorifica {
    private String designacao, fabricante, d_t_limpeza, d_t_adicao;
    private int numero, id, tem_min, tem_max, user_limpeza;

    public AreaFrigorifica() {
        this.d_t_adicao = null;
        this.d_t_limpeza = null;
        this.user_limpeza = -1;
        this.designacao = null;
        this.fabricante = null;
        this.numero = -1;
        this.id = -1;
        this.tem_min = -1;
        this.tem_max = -1;
    }

    public AreaFrigorifica(String designacao, String fabricante, int numero, int id, int tem_min, int tem_max, String d_t_limpeza, int user_limpeza, String d_t_adicao) {
        this.d_t_adicao = d_t_adicao;
        this.d_t_limpeza = d_t_limpeza;
        this.user_limpeza = user_limpeza;
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

    public String getD_t_limpeza() {
        return d_t_limpeza;
    }

    public int getUser_limpeza() {
        return user_limpeza;
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

    public void setD_t_limpeza(String d_t_limpeza) {
        this.d_t_limpeza = d_t_limpeza;
    }

    public void setUser_limpeza(int user_limpeza) {
        this.user_limpeza = user_limpeza;
    }

    @Override
    public String toString() {
        return "AreaFrigorifica{" +
                "designacao='" + designacao + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", d_t_limpeza='" + d_t_limpeza + '\'' +
                ", numero=" + numero +
                ", id=" + id +
                ", tem_min=" + tem_min +
                ", tem_max=" + tem_max +
                ", user_limpeza=" + user_limpeza +
                '}';
    }
}
