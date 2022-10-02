package com.example.him;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
