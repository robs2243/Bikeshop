package de.hssfds.bikeshop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class HelloController {

    ArrayList<String> meineBilder = new ArrayList<>();
    ArrayList<Fahrrad> fahrradListe = new ArrayList<>();
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

        fahrradListe.add(new Fahrrad(600, 250, 50, "SloppyJoe", 50));
        fahrradListe.add(new Fahrrad(1000, 500, 70, "EasyRider", 20));
        fahrradListe.add(new Fahrrad(2500, 1000, 120, "Brutalist", 75));

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
}

