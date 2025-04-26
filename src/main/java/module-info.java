module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.junit.jupiter.api;

    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.controller;
    opens com.example.project.controller to javafx.fxml;
}