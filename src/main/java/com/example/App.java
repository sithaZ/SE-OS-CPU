package com.example;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image; // Import the Image class
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/FXML/MainPage.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1000, 700);

        URL cssUrl = getClass().getResource("/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Warning: Stylesheet 'styles.css' not found in resources.");
        }
        
    
        Image icon = new Image(getClass().getResourceAsStream("/image/IconImage.jpg"));
        stage.getIcons().add(icon);
      

        stage.setTitle("CPU Scheduling Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setRoot(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'setRoot'");
    }
}