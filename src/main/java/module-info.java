module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.junit.jupiter.api;
    requires java.desktop;

    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.controller;
    opens com.example.project.controller to javafx.fxml;

    // export model package
    exports com.example.project.model;

    opens com.example.project.model to org.junit.platform.commons;
}