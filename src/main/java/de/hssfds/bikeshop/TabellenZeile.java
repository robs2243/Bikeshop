package de.hssfds.bikeshop;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TabellenZeile {

    private final StringProperty eigenschaft;
    private final StringProperty wert;

    public TabellenZeile(String eigenschaft, String wert) {
        this.eigenschaft = new SimpleStringProperty(eigenschaft);
        this.wert = new SimpleStringProperty(wert);
    }

    // Getter und Setter für 'eigenschaft'
    public String getEigenschaft() {
        return eigenschaft.get();
    }

    public void setEigenschaft(String eigenschaft) {
        this.eigenschaft.set(eigenschaft);
    }

    public StringProperty eigenschaftProperty() {
        return eigenschaft;
    }

    // Getter und Setter für 'wert'
    public String getWert() {
        return wert.get();
    }

    public void setWert(String wert) {
        this.wert.set(wert);
    }

    public StringProperty wertProperty() {
        return wert;
    }
}
