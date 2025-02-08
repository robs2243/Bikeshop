package de.hssfds.bikeshop;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Fahrrad {

    // JavaFX Properties für die Attribute
    private final DoubleProperty preis;
    private final DoubleProperty akku;
    private final DoubleProperty drehmoment;
    private final StringProperty produktname;
    private final IntegerProperty zustand;

    // Default constructor needed by Jackson
    public Fahrrad() {
        this(0.0, 0.0, 0.0, "", 0);
    }

    // Konstruktor, der alle Attribute setzt
    public Fahrrad(double akku, double drehmoment, double preis, String produktname, int zustand) {
        this.preis = new SimpleDoubleProperty(preis);
        this.akku = new SimpleDoubleProperty(akku);
        this.drehmoment = new SimpleDoubleProperty(drehmoment);
        this.produktname = new SimpleStringProperty(produktname);
        this.zustand = new SimpleIntegerProperty(zustand);
    }

    // Getter, Setter und Property-Methode für 'preis'
    public double getPreis() {
        return preis.get();
    }

    public void setPreis(double preis) {
        this.preis.set(preis);
    }

    public DoubleProperty preisProperty() {
        return preis;
    }

    // Getter, Setter und Property-Methode für 'akku'
    public double getAkku() {
        return akku.get();
    }

    public void setAkku(double akku) {
        this.akku.set(akku);
    }

    public DoubleProperty akkuProperty() {
        return akku;
    }

    // Getter, Setter und Property-Methode für 'drehmoment'
    public double getDrehmoment() {
        return drehmoment.get();
    }

    public void setDrehmoment(double drehmoment) {
        this.drehmoment.set(drehmoment);
    }

    public DoubleProperty drehmomentProperty() {
        return drehmoment;
    }

    // Getter, Setter und Property-Methode für 'produktname'
    public String getProduktname() {
        return produktname.get();
    }

    public void setProduktname(String produktname) {
        this.produktname.set(produktname);
    }

    public StringProperty produktnameProperty() {
        return produktname;
    }

    // Getter, Setter und Property-Methode für 'zustand'
    public int getZustand() {
        return zustand.get();
    }

    public void setZustand(int zustand) {
        this.zustand.set(zustand);
    }

    public IntegerProperty zustandProperty() {
        return zustand;
    }
}

