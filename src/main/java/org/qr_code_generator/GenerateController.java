package org.qr_code_generator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class GenerateController {

    @FXML private TextField input_txt;

    private Stage stage;
    private Scene scene;
    FileChooser fChooser = new FileChooser();
    Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.OK);
    Alert alert2 = new Alert(Alert.AlertType.NONE, "", ButtonType.OK);

    private Image getImage(BufferedImage img){
        //converting to a good type, read about types here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        newImg.createGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

        //converting the BufferedImage to an IntBuffer
        int[] type_int_agrb = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
        IntBuffer buffer = IntBuffer.wrap(type_int_agrb);

        //converting the IntBuffer to an Image, read more about it here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer(newImg.getWidth(), newImg.getHeight(), buffer, pixelFormat);
        return new WritableImage(pixelBuffer);
    }

    public static void saveQRcode(String data, String path, String charset, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }

    public static BufferedImage viewQRcode(String data, String charset, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }


    public void switchToReadScene(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/qr_code_generator/read.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void saveBtn(ActionEvent e) throws IOException, WriterException {
        fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        System.out.println("Text to generate: " + input_txt.getText());
        File file = fChooser.showSaveDialog(stage);
        saveQRcode(input_txt.getText(), file.getAbsolutePath(), "UTF-8", 200, 200);
        if (file != null) {
            System.out.println("QR Code created successfully into " + file.getAbsolutePath());
            alert.setContentText("QR Code successfully saved to " + file.getAbsolutePath());
            alert.show();
        }
    }

    public void viewBtn(ActionEvent e) throws IOException, WriterException {
        Image image = getImage(viewQRcode(input_txt.getText(), "UTF-8", 200, 200));
        ImageView imageView = new ImageView(image);
        alert2.setGraphic(imageView);
        alert2.setContentText("QR Code successfully generated!");
        alert2.show();
    }
}