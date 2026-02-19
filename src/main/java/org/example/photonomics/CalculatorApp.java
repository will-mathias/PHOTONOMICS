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

            System.out.println("FXML URL: " + loader.getLocation());

            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.setTitle("Photonomics Calculator");

            // Fit window to 90% of screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setWidth(Math.min(800, screenBounds.getWidth() * 0.9));
            stage.setHeight(Math.min(600, screenBounds.getHeight() * 0.9));

            stage.show();

            System.out.println("Window shown successfully");

        } catch (Exception e) {
            System.out.println("Failed to load FXML:");
            e.printStackTrace();
        }
    }
}






