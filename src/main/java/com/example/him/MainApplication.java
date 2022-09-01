package com.example.him;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        MainController hello = new MainController();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1150,650);
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Haber İzleme Modülü");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();

        scene.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ESCAPE){
                hello.down();
            }
        });
        stage.setOnCloseRequest(e -> {
            e.consume();
            hello.down();
        });
        stage.setOnHiding(e -> {
            Platform.setImplicitExit(false);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}