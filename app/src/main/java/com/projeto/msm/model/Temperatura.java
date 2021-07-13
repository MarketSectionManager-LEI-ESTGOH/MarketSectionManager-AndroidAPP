package com.projeto.msm.model;

public class Temperatura {

    private String data_hora;
    private String designacao;
    private float temperatura;
    private int area_frigorifica_id;

    public Temperatura() {
        this.data_hora = null;
        this.temperatura = -1;
        this.area_frigorifica_id = -1;
    }

    public Temperatura(String data_hora, int temperatura, int area_frigorifica_id, String designacao) {
        this.data_hora = data_hora;
        this.temperatura = temperatura;
        this.area_frigorifica_id = area_frigorifica_id;
        this.designacao = designacao;
    }

    public String getData_hora() {
        return data_hora;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public int getArca_frigorifica_id() {
        return area_frigorifica_id;
    }

    public String getDesignacao() {
        return designacao;
    }
    
    @Override
    public String toString() {
        return "Temperatura{" +
                "data_hora='" + data_hora + '\'' +
                ", temperatura=" + temperatura +
                ", designacao=" + designacao +
                ", arca_frigorifica_id=" + area_frigorifica_id +
                '}';
    }
}
