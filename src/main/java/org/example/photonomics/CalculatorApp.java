package org.example.photonomics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

public class CalculatorApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/photonomics/calculator.fxml")
            );
            if (loader.getLocation() == null) {
                System.out.println("FXML file not found! Check the path.");
            }

            Scene scene = new Scene(loader.load());

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double maxWidth = screenBounds.getWidth() * 0.9;
            double maxHeight = screenBounds.getHeight() * 0.9;

            stage.setScene(scene);
            stage.setTitle("Photonomics Calculator");

            // Initial size
            stage.setWidth(Math.min(scene.getWidth(), maxWidth));
            stage.setHeight(Math.min(scene.getHeight(), maxHeight));

            // Prevent oversized window
            stage.setMaxWidth(maxWidth);
            stage.setMaxHeight(maxHeight);

            stage.show();
        } catch (Exception e) {
            System.out.println("Failed to load FXML:");
            e.printStackTrace();
        }
    }
}



