package org.example.photonomics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CalculatorApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(CalculatorApp.class.getResource("calculatorUI.fxml"));
            Scene scene = new Scene(loader.load());

            stage.setTitle("🌞 Photonomics Solar Cost Calculator");
            stage.setScene(scene);

            // Fixed min size, resizable
            stage.setWidth(650);
            stage.setHeight(800);
            stage.setMinWidth(600);
            stage.setMinHeight(700);

            // Standard OS window with X, minimize, maximize
            stage.initStyle(StageStyle.DECORATED);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot load FXML. Make sure calculatorUI.fxml exists in resources.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}












