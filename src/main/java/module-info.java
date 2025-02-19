module de.hssfds.bikeshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;
    requires com.fasterxml.jackson.databind;


    opens de.hssfds.bikeshop to javafx.fxml;
    exports de.hssfds.bikeshop;
}