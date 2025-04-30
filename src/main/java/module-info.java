module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;
    requires java.sql;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    opens com.example.project to javafx.fxml;
    opens com.example.project.controller to javafx.fxml;

    exports com.example.project;
    exports com.example.project.controller;
    exports com.example.project.model;
}
