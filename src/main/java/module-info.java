module org.qr_code_generator.module {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires core;

    opens org.qr_code_generator;

    exports org.qr_code_generator;
}