package com.projeto.msm.model;

import java.math.BigInteger;

public class User {
    private String email, nome;
    private int num_interno;
    private boolean tipo;
    //private BigInteger id;
    private String password;
    private int id;

    public User(){
        this.id = -1;
        this.email = null;
        this.num_interno = -1;
        this.tipo = false;
        this.nome = null;
        this.password = null;
    }

    public User(int id, String email, String nome, int num_interno, boolean tipo){
        this.id = id;
        this.email = email;
        this.num_interno = num_interno;
        this.tipo = tipo;
        this.nome = nome;
    }

    public String getName(){
        return (this.nome);
    }

    public String getEmail(){
        return (this.email);
    }

    public int getnumInterno(){
        return (this.num_interno);
    }

    public boolean getTipo(){
        return (this.tipo);
    }

    public int getId(){
        return (this.id);
    }

    public void setNum(int num_interno){
        this.num_interno = num_interno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
