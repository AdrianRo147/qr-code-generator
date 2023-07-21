package org.qr_code_generator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/qr_code_generator/generate.fxml"));
        Scene scene = new Scene(root);

        stage.setMinHeight(400);
        stage.setMinWidth(400);
        stage.setTitle("QR Code Generator");
        stage.setScene(scene);
        stage.show();
    }
}