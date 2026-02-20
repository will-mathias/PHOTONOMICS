package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CalculatorController {

    @FXML private TextField energyUsageField;
    @FXML private TextField householdIncomeField;
    @FXML private TextField loanTermField;
    @FXML private TextField interestRateField;
    @FXML private ChoiceBox<Region> regionDropDown;
    @FXML private VBox outputBox;

    private Region[] regions;

    @FXML
    public void initialize() {

        regions = new Region[]{
                new Region("Bomi", 1.0),
                new Region("Bong", 1.1),
                new Region("Gbarpolu", 0.9),
                new Region("Grand Bassa", 1.05),
                new Region("Grand Cape Mount", 1.2)
        };

        regionDropDown.getItems().addAll(regions);
    }

    @FXML
    protected void onCalculateButtonClick() {

        outputBox.getChildren().clear();

        try {
            double energyUsage = Double.parseDouble(energyUsageField.getText());
            double income = Double.parseDouble(householdIncomeField.getText());
            int loanTerm = Integer.parseInt(loanTermField.getText());
            double interestRate = Double.parseDouble(interestRateField.getText());

            Region selectedRegion = regionDropDown.getValue();

            if (selectedRegion == null) {
                outputBox.getChildren().add(new Label("Please select a region."));
                return;
            }

            double systemCost = energyUsage * 50;
            double totalInterest = systemCost * (interestRate / 100) * loanTerm;
            double totalCost = systemCost + totalInterest;

            Label result = new Label(
                    "Region: " + selectedRegion.getName() + "\n" +
                            "Estimated System Cost: $" + systemCost + "\n" +
                            "Total Cost with Loan: $" + totalCost
            );

            outputBox.getChildren().add(result);

        } catch (NumberFormatException e) {
            outputBox.getChildren().add(new Label("Please enter valid numbers."));
        }
    }
}


