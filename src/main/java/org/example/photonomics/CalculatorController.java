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
        // Dummy regions for now
        regionDropDown.getItems().addAll(
                "Bomi",
                "Bong",
                "Gbarpolu",
                "Grand Bassa",
                "Grand Cape Mount"
        );
    }

    @FXML
    protected void onCalculateButtonClick() {
        outputBox.getChildren().clear();

        String energy = energyUsageField.getText();
        String income = HouseholdIncomeField.getText();
        String region = regionDropDown.getValue();

        Label result = new Label(
                "Energy Usage: " + energy + " kWh\n" +
                        "Household Income: $" + income + "\n" +
                        "Region: " + region
        );

        outputBox.getChildren().add(result);
    }
}
