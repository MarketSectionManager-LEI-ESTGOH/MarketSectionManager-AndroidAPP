package com.projeto.msm.model;

import java.io.Serializable;
import java.math.BigInteger;

public class User implements Serializable {
    private String email, nome;
    private int num_interno;
    private int tipo;
    //private BigInteger id;
    private String password;
    private int id;

    public User(){
        this.id = -1;
        this.email = null;
        this.num_interno = -1;
        this.tipo = -1;
        this.nome = null;
        this.password = null;
    }

    public User(int id, String email, String nome, int num_interno, int tipo){
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

    public int getTipo(){
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

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", num_interno=" + num_interno +
                ", tipo=" + tipo +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
