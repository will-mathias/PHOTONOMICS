package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CalculatorController {

    @FXML private TextField energyUsageField;
    @FXML private TextField HouseholdIncomeField;
    @FXML private ChoiceBox<String> regionDropDown;
    @FXML private VBox outputBox;

    @FXML
    void initialize() {
        // Add dummy regions for testing
        regionDropDown.getItems().addAll("Bomi", "Bong", "Gbarpolu");
    }

    @FXML
    protected void onCalculateButtonClick() {
        outputBox.getChildren().clear();
        String energy = energyUsageField.getText();
        String income = HouseholdIncomeField.getText();
        String region = regionDropDown.getValue();

        Label result = new Label("Energy: " + energy + "\nIncome: " + income + "\nRegion: " + region);
        outputBox.getChildren().add(result);
    }
}





