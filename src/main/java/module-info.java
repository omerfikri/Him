module com.example.him {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires java.desktop;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.him to javafx.fxml;
    exports com.example.him;
}