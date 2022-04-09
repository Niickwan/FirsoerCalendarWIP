module com.example.firsoer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.firsoer to javafx.fxml;
    exports com.example.firsoer;
}