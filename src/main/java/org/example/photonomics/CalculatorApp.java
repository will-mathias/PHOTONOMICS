package org.example.photonomics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CalculatorApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CalculatorApp.class.getResource("calculatorUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 1000);
        stage.setTitle("Solar Cost Calculator");
        stage.setScene(scene);
        stage.show();
    }
}
