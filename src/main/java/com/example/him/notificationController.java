package com.example.him;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class notificationController {


    @FXML
    private Button okey;

    Integer sayi=0;


    @FXML
    public void ok(){
        Stage stage = (Stage) okey.getScene().getWindow();
        sayi=0;
        stage.close();
    }
}
