package de.hssfds.bikeshop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class HelloController {

    ArrayList<String> meineBilder = new ArrayList<>();
    int i;

    @FXML
    private Label statusLabel;
    @FXML
    private ImageView bild1;

    public void initialize() {

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
        }
        else {

            i = meineBilder.size()-1;
            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            setStatusLabel(i);
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

        }
        else {

            i = 0;
            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            setStatusLabel(i);
        }
    }

    protected void setStatusLabel(int index) {
        statusLabel.setText("Pfad: " + meineBilder.get(index));
    }

}

