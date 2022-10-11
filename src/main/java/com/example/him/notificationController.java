package com.example.him;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;


public class notificationController {

    @FXML
    private Button okey;
    static String message;
    static boolean isclick=true;

    public static void notification(String s){
        Stage stage = new Stage();
        message = s;
        if(isclick) {
            try {
                Parent part = FXMLLoader.load(notificationController.class.getResource("notification.fxml"));
                Scene scene = new Scene(part);
                stage.setScene(scene);
                stage.setTitle("Yeni Bildirim");
                //  stage.initStyle(StageStyle.UNDECORATED);
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);
                stage.show();
                isclick = false;

            } catch (Exception e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }else{
            stage.setAlwaysOnTop(true);
        }
    }
    @FXML
    public void ok(){
        Stage stage = (Stage) okey.getScene().getWindow();
        String title = "Yeni Bildirim Geldi";
        try {
            Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("info.png")));

            String os = System.getProperty("os.name");
            if (os.contains("Linux")) {
                ProcessBuilder builder = new ProcessBuilder(
                        "zenity",
                        "--notification",
                        "--title=" + title,
                        "--text=" + message);
                builder.inheritIO().start();
            } else if (os.contains("Mac")) {
                ProcessBuilder builder = new ProcessBuilder(
                        "osascript", "-e",
                        "display notification \"" + message + "\""
                                + " with title \"" + title + "\"");
                builder.inheritIO().start();
            } else if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();

                TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);

                trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isclick = true;
        stage.close();
    }
}
