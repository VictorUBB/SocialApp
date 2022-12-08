module com.example.lab_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.lab_gui to javafx.fxml;
    exports com.example.lab_gui;
    exports com.example.lab_gui.controller;
    opens com.example.lab_gui.controller to javafx.fxml;
    opens com.example.lab_gui.entities to javafx.base;
}