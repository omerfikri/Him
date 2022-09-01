package com.example.him;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class confirm1Controller {

    @FXML
    private Button ok;
    @FXML
    public void shut(){
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }
}
