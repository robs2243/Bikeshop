module de.hssfds.bikeshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens de.hssfds.bikeshop to javafx.fxml;
    exports de.hssfds.bikeshop;
}