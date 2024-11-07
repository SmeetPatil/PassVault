module passvault.passvault {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jakarta.mail;
    requires com.google.api.client.json.jackson2;
    requires com.google.api.client;
    requires google.api.client;
    requires com.google.api.client.auth;
    requires google.api.services.gmail.v1.rev110;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires java.desktop;

    opens passvault.passvault.controllers to javafx.fxml;
    exports passvault.passvault.controllers to javafx.graphics, javafx.fxml;
    exports passvault.passvault.utils to javafx.fxml, javafx.graphics;
    opens passvault.passvault.utils to javafx.fxml;
}
