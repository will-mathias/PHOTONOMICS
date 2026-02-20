package org.example.photonomics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class CalculatorApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(CalculatorApp.class.getResource("calculatorUI.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Solar Cost Calculator");
            stage.setScene(scene);
            stage.setMinWidth(650);
            stage.setMinHeight(800);
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












