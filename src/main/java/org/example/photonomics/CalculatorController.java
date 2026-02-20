package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class CalculatorController {

    private static final double DEFAULT_INTEREST = 0.08;
    private static final int DEFAULT_LOAN_YEARS = 7;

    @FXML private TextField energyUsageField;
    @FXML private TextField HouseholdIncomeField;
    @FXML private TextField loanTermField;
    @FXML private TextField interestRateField;
    @FXML private ChoiceBox<Region> regionDropDown;
    @FXML private VBox outputBox;

    private Region[] regions;

    @FXML
    void initialize() {
        loadRegions();
        regionDropDown.getItems().addAll(regions);
    }

    private void loadRegions() {
        regions = new Region[]{
                new Region("Bomi", 4.6, 1.80, 0.55, 250, 0.35, 0.1),
                new Region("Bong", 5.1, 1.60, 0.55, 220, 0.33, 0.1),
                new Region("Gbarpolu", 4.9, 1.75, 0.45, 180, 0.31, 0.1),
                new Region("Grand Bassa", 4.7, 1.55, 0.58, 280, 0.34, 0.1),
                new Region("Montserrado", 4.5, 1.50, 0.60, 350, 0.35, 0.1)
                // Add others as needed
        };
    }

    @FXML
    void onCalculateButtonClick() {
        outputBox.getChildren().clear();

        try {
            double energyUsage = Double.parseDouble(energyUsageField.getText());
            double income = Double.parseDouble(HouseholdIncomeField.getText());
            int loanYears = Integer.parseInt(loanTermField.getText());
            double interest = Double.parseDouble(interestRateField.getText()) / 100.0;
            Region region = regionDropDown.getValue();

            if (region == null) throw new IllegalArgumentException("Select a region");

            double systemKW = energyUsage / (region.getPeakSunHours() * 30);
            double totalCost = (systemKW * 1000) * (region.getLaborCostPerWatt() + region.getHardwareCostPerWatt())
                    + region.getAvgPermitCost();
            double monthlySaving = energyUsage * region.getUtilityRatePerKWh();
            double paybackYears = (monthlySaving > 0) ? totalCost / monthlySaving / 12 : Double.POSITIVE_INFINITY;
            double monthlyLoan = calculateMonthlyLoan(totalCost, interest, loanYears);
            double netCashFlow = monthlySaving - monthlyLoan;

            displayResults(systemKW, totalCost, monthlySaving, paybackYears, income, monthlyLoan, netCashFlow, interest, loanYears);

        } catch (NumberFormatException e) {
            showAlert("Invalid input. Please enter numeric values.");
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
    }

    private double calculateMonthlyLoan(double principal, double annualRate, int years) {
        if (annualRate <= 0) return principal / (years * 12);
        double r = annualRate / 12;
        int n = years * 12;
        return principal * (r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
    }

    private void displayResults(double systemKW, double totalCost, double monthlySaving, double paybackYears,
                                double income, double monthlyLoan, double netCashFlow, double interest, int loanYears) {

        HBox costBox = new HBox(15);
        costBox.setPadding(new Insets(5));
        costBox.setAlignment(Pos.CENTER);
        costBox.getChildren().addAll(
                new Label(String.format("System Size: %.2f kW", systemKW)),
                new Label(String.format("Total Cost: $%.2f", totalCost)),
                new Label(String.format("Monthly Saving: $%.2f", monthlySaving))
        );

        Label netLabel = new Label(String.format("Net Cash Flow: $%.2f", netCashFlow));
        netLabel.setStyle(netCashFlow >= 0 ? "-fx-text-fill: green;" : "-fx-text-fill: orange;");

        // Financial chart
        LineChart<Number, Number> chart = createChart(totalCost, monthlySaving, monthlyLoan, loanYears);

        outputBox.getChildren().addAll(costBox, netLabel, chart);
    }

    private LineChart<Number, Number> createChart(double totalCost, double savings, double monthlyLoan, int years) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Cumulative $");
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setCreateSymbols(false);
        chart.setPrefHeight(250);

        XYChart.Series<Number, Number> savingsSeries = new XYChart.Series<>();
        savingsSeries.setName("Cumulative Savings");
        XYChart.Series<Number, Number> loanSeries = new XYChart.Series<>();
        loanSeries.setName("Cumulative Loan");

        double cumSavings = 0, cumLoan = 0;
        int totalMonths = Math.min(years * 12, 120);
        for (int month = 0; month <= totalMonths; month++) {
            if (month > 0) {
                cumSavings += savings;
                if (month <= years * 12) cumLoan += monthlyLoan;
            }
            savingsSeries.getData().add(new XYChart.Data<>(month, cumSavings));
            loanSeries.getData().add(new XYChart.Data<>(month, cumLoan));
        }
        chart.getData().addAll(savingsSeries, loanSeries);
        return chart;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}


