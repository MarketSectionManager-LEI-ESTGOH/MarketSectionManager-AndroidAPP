package com.projeto.msm.model;

public class Produto {

    int n_interno;
    String nome;
    String marca;

    public Produto() {
        this.n_interno = -1;
        this.nome = null;
        this.marca = null;
    }

    public Produto(int n_interno, String nome, String marca) {
        this.n_interno = n_interno;
        this.nome = nome;
        this.marca = marca;
    }

    public int getN_interno() {
        return n_interno;
    }

    public void setN_interno(int n_interno) {
        this.n_interno = n_interno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "n_interno=" + n_interno +
                ", nome='" + nome + '\'' +
                ", marca='" + marca + '\'' +
                '}';
    }
}
