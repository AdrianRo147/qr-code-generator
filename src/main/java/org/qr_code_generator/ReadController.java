package org.qr_code_generator;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ReadController {
    private Stage stage;
    private Scene scene;
    FileChooser fChooser = new FileChooser();
    Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.OK);

    public static String readQRcode(String path) throws FileNotFoundException, IOException, NotFoundException
    {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
        Result res = new MultiFormatReader().decode(binaryBitmap);
        return res.getText();
    }

    public void switchToGenScene(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/qr_code_generator/generate.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void generate(ActionEvent e) throws IOException, NotFoundException {
        File file = fChooser.showOpenDialog(stage);
        if (file != null) {
            System.out.println("Decoded text: " + readQRcode(file.getAbsolutePath()));
            Image img = new Image(file.getAbsolutePath());
            ImageView imgView = new ImageView(img);
            alert.setGraphic(imgView);
            alert.setContentText("Content: " + readQRcode(file.getAbsolutePath()));
            alert.show();
        }
    }
}
