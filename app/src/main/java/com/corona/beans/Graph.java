package com.corona.beans;

public class Graph {
    private String date;
    private long confirmed;
    private long death;
    private long recovered;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(long confirmed) {
        this.confirmed = confirmed;
    }

    public long getDeath() {
        return death;
    }

    public void setDeath(long death) {
        this.death = death;
    }

    public long getRecovered() {
        return recovered;
    }

    public void setRecovered(long recovered) {
        this.recovered = recovered;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "date='" + date + '\'' +
                ", confirmed=" + confirmed +
                ", death=" + death +
                ", recovered=" + recovered +
                '}';
    }
}
