package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class CalculatorController {

    @FXML private TextField energyUsageField;
    @FXML private TextField HouseholdIncomeField; // Matches FXML exactly
    @FXML private TextField loanTermField;
    @FXML private TextField interestRateField;
    @FXML private ChoiceBox<String> regionDropDown;
    @FXML private VBox outputBox;

    // Define regions for demonstration
    private Region[] regions;

    @FXML
    void initialize() {
        initRegions();
        for (Region region : regions) {
            regionDropDown.getItems().add(region.getRegionName());
        }
    }

    @FXML
    protected void onCalculateButtonClick() {
        // For demonstration, just show a simple message
        outputBox.getChildren().clear();
        Label label = new Label("Calculation executed successfully!");
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        outputBox.getChildren().add(label);
    }

    private void initRegions() {
        regions = new Region[] {
                new Region("Bomi", 4.6, 1.80, 0.55, 250, 0.35),
                new Region("Bong", 5.1, 1.60, 0.55, 220, 0.33)
                // Add the rest of the regions as needed
        };
    }

    private static class Region {
        private final String name;
        private final double peakSunHours, laborCost, hardwareCost, avgPermitCost, utilityRate;

        public Region(String name, double peakSunHours, double laborCost, double hardwareCost, double avgPermitCost, double utilityRate) {
            this.name = name;
            this.peakSunHours = peakSunHours;
            this.laborCost = laborCost;
            this.hardwareCost = hardwareCost;
            this.avgPermitCost = avgPermitCost;
            this.utilityRate = utilityRate;
        }

        public String getRegionName() { return name; }
        public double getPeakSunHours() { return peakSunHours; }
        public double getLaborCostPerWatt() { return laborCost; }
        public double getHardwareCostPerWatt() { return hardwareCost; }
        public double getAvgPermitCost() { return avgPermitCost; }
        public double getUtilityRatePerKWh() { return utilityRate; }
    }
}
