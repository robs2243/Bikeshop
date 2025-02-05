module de.hssfds.bikeshop {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.hssfds.bikeshop to javafx.fxml;
    exports de.hssfds.bikeshop;
}