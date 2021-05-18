package com.projeto.msm.model;

import java.io.Serializable;
import java.math.BigInteger;

public class Rastreabilidade implements Serializable{

    String fornecedor, origem;
    int lote, num_interno;
    int user_id;

    public Rastreabilidade() {
        this.lote = -1;
        this.fornecedor = null;
        this.origem = null;
        this.num_interno = -1;
        this.user_id = -1;
    }

    public Rastreabilidade(int lote, String fornecedor, String origem, int num_interno) {
        this.lote = lote;
        this.fornecedor = fornecedor;
        this.origem = origem;
        this.num_interno = num_interno;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public String getOrigem() {
        return origem;
    }

    public int getLote() {
        return lote;
    }

    public int getNum_interno() {
        return num_interno;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Rastreabilidade{" +
                "fornecedor='" + fornecedor + '\'' +
                ", origem='" + origem + '\'' +
                ", lote=" + lote +
                ", num_interno=" + num_interno +
                ", user_id=" + user_id +
                '}';
    }
}
