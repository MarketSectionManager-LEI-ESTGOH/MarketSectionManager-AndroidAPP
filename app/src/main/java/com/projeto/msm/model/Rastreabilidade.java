package com.projeto.msm.model;

import java.io.Serializable;
import java.math.BigInteger;

public class Rastreabilidade implements Serializable{

    String fornecedor, origem;
    int lote, num_interno;

    public Rastreabilidade() {
        this.lote = -1;
        this.fornecedor = null;
        this.origem = null;
        this.num_interno = -1;
    }

    public Rastreabilidade(int lote, String fornecedor, String origem, int num_interno) {
        this.lote = lote;
        this.fornecedor = fornecedor;
        this.origem = origem;
        this.num_interno = num_interno;
    }

    @Override
    public String toString() {
        return "Rastreabilidade{" +
                "fornecedor='" + fornecedor + '\'' +
                ", origem='" + origem + '\'' +
                ", lote=" + lote +
                ", num_interno=" + num_interno +
                '}';
    }
}
