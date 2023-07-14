package org.txt_to_qr;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Main {

    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }

    public static void main(String[] args) throws WriterException, IOException, NotFoundException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JTextField label = new JTextField("text");
        label.setColumns(16);

        JButton button = new JButton("Generate");

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(button);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("QR Code Generator");
        frame.setPreferredSize(new Dimension(300, 300));
        frame.show();
        frame.pack();
        frame.setVisible(true);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        String charset = "UTF-8";

        button.addActionListener(e ->
        {
            System.out.println("Text to generate: " + label.getText());

            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            if (label.getText().equals(null) || label.getText().equals("")) {
                JOptionPane.showMessageDialog(frame, "Please enter text you want to generate into QR Code!", "ERROR: Empty text", JOptionPane.ERROR_MESSAGE);
                System.out.println("Cannot generate QR Code, no text was entered");
            } else {
                try {
                    int userSelection = fileChooser.showSaveDialog(frame);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        String filePath = fileToSave.getAbsolutePath();
                        if (!filePath.endsWith(".png") && !filePath.endsWith(".jpg")) {
                            filePath = filePath + ".png";
                        }
                        System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                        generateQRcode(label.getText(), filePath, charset, hashMap, 200, 200);//increase or decrease height and width accodingly
                        System.out.println("QR Code created successfully into " + filePath);
                    }
                } catch (WriterException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}