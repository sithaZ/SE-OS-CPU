package com.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomePageController {

    @FXML
    private Button playButton;

    @FXML
    private Button exitButton;

    @FXML
    private void handlePlayButtonAction(ActionEvent event) throws IOException {
 
        App.setRoot("MainPage");
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {

        Platform.exit();
    }
}
