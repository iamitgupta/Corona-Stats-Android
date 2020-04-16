package com.corona.beans;

import java.io.Serializable;

public class AdapterBean implements Serializable{

    private Area area;

    private String name;

    private String confirmed;

    private String death;

    private String recovered;


    public AdapterBean(){}

    public AdapterBean(Area area,String name,String confirmed,String death,String recovered){
        this.area = area;
        this.name=name;
        this.confirmed=confirmed;
        this.death=death;
        this.recovered=recovered;
    }


    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    @Override
    public String toString() {
        return "AdapterBean{" +
                "name='" + name + '\'' +
                ", confirmed=" + confirmed +
                ", death=" + death +
                ", recovered=" + recovered +
                '}';
    }
}
