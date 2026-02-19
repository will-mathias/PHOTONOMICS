package org.example.photonomics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CalculatorApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/photonomics/calculator.fxml")
        );

        Scene scene = new Scene(loader.load());

        // Limit window size to 90% of screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double maxWidth = screenBounds.getWidth() * 0.9;
        double maxHeight = screenBounds.getHeight() * 0.9;

        stage.setScene(scene);
        stage.setTitle("Photonomics Calculator");

        // Set window size based on scene's preferred size but limit it
        stage.setWidth(Math.min(scene.getWidth(), maxWidth));
        stage.setHeight(Math.min(scene.getHeight(), maxHeight));

        // Optional: prevent resizing beyond screen
        stage.setMaxWidth(maxWidth);
        stage.setMaxHeight(maxHeight);

        stage.show();
    }
}

