package de.hssfds.bikeshop;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class HelloController {

    ArrayList<String> meineBilder = new ArrayList<>();
    ArrayList<Fahrrad> fahrradListe = new ArrayList<>();
    ArrayList<String> JSONobject = new ArrayList<>();

    int i;

    @FXML
    private TextField tf_preis;

    @FXML
    private TextField tf_akku;

    @FXML
    private TextField tf_drehmoment;

    @FXML
    private TextField tf_produktname;

    @FXML
    private TextField tf_zustand;

    @FXML
    private TextField tf_mail;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_token;

    @FXML
    private TextField tf_key;

    @FXML
    private TextField tf_value;

    @FXML
    private Label statusLabel;
    @FXML
    private ImageView bild1;

    public void initialize() {

        fahrradListe.add(new Fahrrad(600, 250, 50, "SloppyJoe", 50, 101));
        fahrradListe.add(new Fahrrad(1000, 500, 70, "EasyRider", 20, 102));
        fahrradListe.add(new Fahrrad(2500, 1000, 120, "Brutalist", 75, 103));

        try {
            getJpgPaths();
        }
        catch(Exception ex) {

            statusLabel.setText("Fehler beim Laden der Bilder!");
        }

        Image imgBuffer = new Image("file:" + meineBilder.getFirst()); // ab JDK21 muss "file:" vor dem Pfad stehe
        bild1.setImage(imgBuffer);

        i = 0;

        setStatusLabel(i);
        setTextFields(fahrradListe.get(i));

        addListeners();

    }

    private void addListeners() {

        tf_preis.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                fahrradListe.get(i).setPreis(Double.parseDouble(tf_preis.getText()));
            }
        });

        tf_akku.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                fahrradListe.get(i).setAkku(Double.parseDouble(tf_akku.getText()));
            }
        });

        tf_drehmoment.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                fahrradListe.get(i).setDrehmoment(Double.parseDouble(tf_drehmoment.getText()));
            }
        });

        tf_produktname.
                focusedProperty().
                addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        fahrradListe.get(i).setProduktname(tf_produktname.getText());
                    }
                });

        tf_zustand.
                focusedProperty().
                addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        fahrradListe.get(i).setZustand(Integer.parseInt(tf_zustand.getText()));
                    }
                });

    }

    public void getJpgPaths() throws URISyntaxException, NullPointerException {

        URL resourceUrl = getClass().getResource("/pics");
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource folder 'pics' not " +
                    "found on the classpath");
        }
        File folder = new File(resourceUrl.toURI());
        File[] files = folder.listFiles();
        if (files == null) {
            throw new NullPointerException("Files-Object is Null!");
        }

        for(File file : files) {
            meineBilder.add(file.getPath());
        }
    }

    @FXML
    protected void bildVor() {

        i++;

        Image imgBuffer;

        if (i < meineBilder.size()-1) {

            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            setStatusLabel(i);
            setTextFields(fahrradListe.get(i));
        }
        else {

            i = meineBilder.size()-1;
            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            setStatusLabel(i);
            setTextFields(fahrradListe.get(i));
        }

    }

    @FXML
    protected void bildZuruck() {

        i--;

        Image imgBuffer;

        if (i > 0) {

            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            setStatusLabel(i);
            setTextFields(fahrradListe.get(i));

        }
        else {

            i = 0;
            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            setStatusLabel(i);
            setTextFields(fahrradListe.get(i));
        }
    }

    @FXML
    protected void btn_speichern() {

        //get Fahrrad with index i

        Fahrrad currentBike = fahrradListe.get(i);

        //set new values
        currentBike.setPreis(Double.parseDouble(tf_preis.getText()));
        currentBike.setAkku(Double.parseDouble(tf_akku.getText()));
        currentBike.setDrehmoment(Double.parseDouble(tf_drehmoment.getText()));
        currentBike.setProduktname(tf_produktname.getText());
        currentBike.setZustand(Integer.parseInt(tf_zustand.getText()));

    }

    protected void setTextFields(Fahrrad currentBike) {

        tf_preis.setText(String.valueOf(currentBike.getPreis()));
        tf_akku.setText(String.valueOf(currentBike.getAkku()));
        tf_drehmoment.setText(String.valueOf(currentBike.getDrehmoment()));
        tf_produktname.setText(currentBike.getProduktname());
        tf_zustand.setText(String.valueOf(currentBike.getZustand()));

    }


    @FXML
    protected void inDBschreiben() {
        Firebasepusher.pushToFirebase(tf_key.getText(), tf_value.getText(), tf_token.getText());
    }

    @FXML
    protected void ausDBlesen() {
        String[] response = Firebasepusher.getFromFirebase(tf_key.getText(), tf_token.getText());
        statusLabel.setText("Status Code: " + response[0] + " Response Body: " + response[1]);
    }


    protected void setStatusLabel(int index) {
        statusLabel.setText("Pfad: " + meineBilder.get(index));
    }

    @FXML
    protected void setToken() {
        String email = tf_mail.getText();
        String password = tf_password.getText();
        tf_token.setText(Firebasepusher.generateToken(email, password));
    }

    private ArrayList<String> StringArrayToJSON(ArrayList<Fahrrad> fahrradListe) {
        // Erstelle eine neue Liste, die später die JSON-Darstellungen der Fahrräder enthält.
        ArrayList<String> fahrradListeJSON = new ArrayList<>();

        // Erzeugung eines ObjectMapper-Objekts aus der Jackson-Bibliothek.
        // Der ObjectMapper übernimmt die Serialisierung (Umwandlung eines Objekts in einen JSON-String).
        ObjectMapper objectMapper = new ObjectMapper();

        // Iteriere über jedes Fahrrad in der übergebenen Liste.
        for (Fahrrad fahrrad : fahrradListe) {
            try {
                // Serialisiere das Fahrrad-Objekt in einen JSON-String.
                String jsonString = objectMapper.writeValueAsString(fahrrad);
                // Füge den erzeugten JSON-String der Ergebnisliste hinzu.
                fahrradListeJSON.add(jsonString);
            } catch (Exception e) {
                // Falls ein Fehler während der Serialisierung auftritt, wird dieser abgefangen und der Stacktrace ausgegeben.
                e.printStackTrace();
            }
        }
        // Rückgabe der Liste mit den JSON-Strings.
        return fahrradListeJSON;
    }


    @FXML
    protected void saveInFirebase() {
        // Konvertiere die Liste der Fahrrad-Objekte in eine Liste von JSON-Strings.
        ArrayList<String> fahrradListeJSON = StringArrayToJSON(fahrradListe);

        // Iteriere über alle JSON-Strings.
        for (int i = 0; i < fahrradListeJSON.size(); i++) {
            // Rufe die Methode pushJSONtoDB auf, um jedes JSON-Dokument in die Firebase-Datenbank zu speichern.
            // Als Parameter werden:
            // 1. Die ID des Fahrrads als String (vermutlich als Schlüssel in der DB)
            // 2. Der JSON-String, der das Fahrrad repräsentiert
            // 3. Ein Token (aus einem Textfeld tf_token), das vermutlich für Authentifizierungszwecke dient
            Firebasepusher.pushJSONtoDB(
                    String.valueOf(fahrradListe.get(i).getId()),
                    fahrradListeJSON.get(i),
                    tf_token.getText()
            );
        }
    }

    @FXML
    protected void loadFromFirebase() {

        String[] response = Firebasepusher.getFromFirebase("", tf_token.getText());
        String jsonAntwort = response[1];
        JSONparser(jsonAntwort);
    }

    private void JSONparser(String json) {

        ArrayList<Fahrrad> fahrradListeJSONparser = new ArrayList<>();
        Map<Integer, Fahrrad> fahrradMap = JsonFahrradParser.parse(json);
        Set<Integer> allKeys = fahrradMap.keySet();
        for(Integer bikeId : allKeys) {
            fahrradListeJSONparser.add(fahrradMap.get(bikeId));
        }
        fahrradListe = fahrradListeJSONparser;
        setTextFields(fahrradListe.get(i));

    }


}

