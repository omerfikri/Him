package com.example.him;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class confirmController implements Initializable {

    @FXML
    private Label lbl0;

    @FXML
    private Button no;

    @FXML
    private void yes(){
        Platform.exit();
    }
    @FXML
    private void no(){
        Stage stage = (Stage) no.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl0.setWrapText(true);
    }
}
