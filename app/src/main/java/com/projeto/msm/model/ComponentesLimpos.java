package com.projeto.msm.model;

public class ComponentesLimpos {

    private String designacao;
    private String area_designacao;
    private String last_date;

    public ComponentesLimpos(String last_date, String area_designacao, String designacao) {
        this.last_date = last_date;
        this.area_designacao = area_designacao;
        this.designacao = designacao;
    }

    public ComponentesLimpos() {
        this.last_date = null;
        this.area_designacao = null;
        this.designacao = null;
    }

    public String getData() {
        return last_date;
    }

    public void setData(String data) {
        this.last_date = data;
    }

    public String getArea() {
        return area_designacao;
    }

    public void setArea(String area) {
        this.area_designacao = area;
    }

    public String getComponente() {
        return designacao;
    }

    public void setComponente(String componente) {
        this.designacao = componente;
    }
}
