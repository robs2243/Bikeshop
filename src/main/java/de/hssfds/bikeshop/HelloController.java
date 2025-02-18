package de.hssfds.bikeshop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class HelloController {

    ArrayList<String> meineBilder = new ArrayList<>();
    ArrayList<Fahrrad> fahrradListe = new ArrayList<>();
    int i;

    @FXML
    private TableView<TabellenZeile> tV_tabelle;

    @FXML
    private TableColumn<TabellenZeile, String> tC_eigenschaften;

    @FXML
    private TableColumn<TabellenZeile, String> tC_wert;

    @FXML
    private TextArea tA_response;

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

        try {
            getJpgPaths();
        }
        catch(Exception ex) {

            statusLabel.setText("Fehler beim Laden der Bilder!");
        }

        Image imgBuffer = new Image("file:" + meineBilder.getFirst()); // ab JDK21 muss "file:" vor dem Pfad stehe
        bild1.setImage(imgBuffer);

        //zähler für die Bilder und Räder
        i = 0;

        setStatusLabel(i);

        //erstelle 5 Fahrräder und speichere sie in der fahrradListe
        fahrradListe.add(new Fahrrad(600, 250, 50, "SloppyJoe", 50));
        fahrradListe.add(new Fahrrad(1000, 500, 70, "EasyRider", 20));
        fahrradListe.add(new Fahrrad(2500, 1000, 120, "Brutalist", 75));

        tabelle(0);

    }

    private ArrayList<String> StringArrayToJSON(ArrayList<Fahrrad> fahrradListe) {

        //erstelle JSON-Strings mit jackson für die Fahrräder und speichere sie in der fahrradListeJSON
        ArrayList<String> fahrradListeJSON = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for(Fahrrad fahrrad : fahrradListe) {
            try {
                fahrradListeJSON.add(objectMapper.writeValueAsString(fahrrad));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fahrradListeJSON;
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

        //push one bike to firebase mit pushJSONtoDB
        //Firebasepusher.pushJSONtoDB("fahrrad1", fahrradListeJSON.get(0), tf_token.getText());
        tabelle(i);

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

        tabelle(i);
    }

    @FXML
    protected void inDBschreiben() {
        Firebasepusher.pushToFirebase(tf_key.getText(), tf_value.getText(), tf_token.getText());
    }

    @FXML
    protected void ausDBlesen() {
        String[] response = Firebasepusher.getFromFirebase(tf_key.getText(), tf_token.getText());
        if(response[0].equals("200")) {
            statusLabel.setText("Lesen erfolgreich!");
            tA_response.setText("Status Code: " + response[0] + "\nResponse Body: " + response[1]);
        }
        else{
            tA_response.setText("Fehler beim Lesen der Daten.\nStatus Code: " + response[0] + "\nResponse Body: " + response[1]);
            statusLabel.setText("Error!");
        }
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

    @FXML
    protected void saveJSONinDB() {

        ArrayList<String> fahrradListeJSON = StringArrayToJSON(fahrradListe);
        for(int i = 0; i < fahrradListeJSON.size(); i++) {
            Firebasepusher.pushJSONtoDB("fahrrad" + i, fahrradListeJSON.get(i), tf_token.getText());
        }
    }

    @FXML
    protected void loadJSONfromDB() {

        String[] response = Firebasepusher.getFromFirebase("", tf_token.getText());
        if(response[0].equals("200")) {
            statusLabel.setText("Lesen erfolgreich!");
            tA_response.setText("Status Code: " + response[0] + "\nResponse Body: " + response[1]);
        }
        else{
            tA_response.setText("Fehler beim Lesen der Daten.\nStatus Code: " + response[0] + "\nResponse Body: " + response[1]);
            statusLabel.setText("Error!");
        }

        fahrradListe = jsonToFahrradList(response[1]);
        tabelle(i);

    }

    protected ArrayList<Fahrrad> jsonToFahrradList(String json) {

        ArrayList<Fahrrad> fahrradList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert the JSON string into a Map whose keys are strings and values are Fahrrad objects.
            Map<String, Fahrrad> map = objectMapper.readValue(
                    json, new TypeReference<Map<String, Fahrrad>>() {}
            );
            // Create an ArrayList from the map values.
            fahrradList.addAll(map.values());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fahrradList;
    }


    protected void tabelle(int i) {

        // TableView und Spalte als editierbar setzen
        tV_tabelle.setEditable(true);
        tC_wert.setEditable(true);


        // Setze die CellValueFactories (hier greift PropertyValueFactory auf die Methoden eigenschaftProperty() und wertProperty())
        tC_eigenschaften.setCellValueFactory(new PropertyValueFactory<>("eigenschaft"));
        tC_wert.setCellValueFactory(new PropertyValueFactory<>("wert"));

        // Für die Spalte 'wert' setzen wir eine CellFactory, die Editieren (TextField) ermöglicht
        tC_wert.setCellFactory(TextFieldTableCell.forTableColumn());

        // Hole das Fahrrad-Modell (angenommen, fahrradListe enthält bereits Fahrrad-Objekte)
        Fahrrad meinFahrrad = fahrradListe.get(i);

        // Erstelle die einzelnen Zeilen – jeweils eine Tabellenzeile pro Eigenschaft des Fahrrads
        TabellenZeile zeileAkku = new TabellenZeile("Akku", String.valueOf(meinFahrrad.getAkku()));
        TabellenZeile zeileDrehmoment = new TabellenZeile("Drehmoment", String.valueOf(meinFahrrad.getDrehmoment()));
        TabellenZeile zeilePreis = new TabellenZeile("Preis", String.valueOf(meinFahrrad.getPreis()));
        TabellenZeile zeileProduktname = new TabellenZeile("Produktname", meinFahrrad.getProduktname());
        TabellenZeile zeileZustand = new TabellenZeile("Zustand", String.valueOf(meinFahrrad.getZustand()));

        // Erstelle einen Converter für numerische Werte (Double und Integer)
        NumberStringConverter converter = new NumberStringConverter();

        // Richte das bidirektionale Binding ein:
        // Die "wert"-Property der Tabellenzeile wird mit der entsprechenden Property im Fahrrad-Objekt verbunden.
        Bindings.bindBidirectional(zeilePreis.wertProperty(), meinFahrrad.preisProperty(), converter);
        Bindings.bindBidirectional(zeileAkku.wertProperty(), meinFahrrad.akkuProperty(), converter);
        Bindings.bindBidirectional(zeileDrehmoment.wertProperty(), meinFahrrad.drehmomentProperty(), converter);
        // Bei Strings ist kein Converter nötig:
        zeileProduktname.wertProperty().bindBidirectional(meinFahrrad.produktnameProperty());
        // Auch bei Integer-Werten verwenden wir den Converter (der hier auch für Integer funktioniert)
        Bindings.bindBidirectional(zeileZustand.wertProperty(), meinFahrrad.zustandProperty(), converter);

        // Füge die Zeilen in eine ObservableList ein
        ObservableList<TabellenZeile> daten = FXCollections.observableArrayList(
                zeilePreis, zeileAkku, zeileDrehmoment, zeileProduktname, zeileZustand
        );

        // Setze die Daten in die TableView
        tV_tabelle.setItems(daten);

        tC_eigenschaften.setSortable(true);
        tC_eigenschaften.setSortType(TableColumn.SortType.ASCENDING);
        tV_tabelle.getSortOrder().clear();  // Clear any previous sort order
        tV_tabelle.getSortOrder().add(tC_eigenschaften);
        tV_tabelle.sort();  // Force the table to re-sort immediately

    }

}

