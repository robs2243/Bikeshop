package de.hssfds.bikeshop;

public class Fahrrad {

    private double preis;
    private double akku;
    private double drehmoment;
    private String produktname;
    private int zustand;

    public Fahrrad(double preis, double akku, double drehmoment, String produktname, int zustand) {
        this.preis = preis;
        this.akku = akku;
        this.drehmoment = drehmoment;
        this.produktname = produktname;
        this.zustand = zustand;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public double getAkku() {
        return akku;
    }

    public void setAkku(double akku) {
        this.akku = akku;
    }

    public double getDrehmoment() {
        return drehmoment;
    }

    public void setDrehmoment(double drehmoment) {
        this.drehmoment = drehmoment;
    }

    public String getProduktname() {
        return produktname;
    }

    public void setProduktname(String produktname) {
        this.produktname = produktname;
    }

    public int getZustand() {
        return zustand;
    }

    public void setZustand(int zustand) {
        this.zustand = zustand;
    }



}
