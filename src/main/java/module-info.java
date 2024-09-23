module passvault.passvault {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens passvault.passvault.controllers to javafx.fxml;
    exports passvault.passvault.controllers to javafx.graphics, javafx.fxml;
}