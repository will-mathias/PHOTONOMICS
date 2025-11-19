package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class CalculatorController {
    // Default loan constants
    private static final double DEFAULT_ANNUAL_INTEREST_RATE = 0.08; // 8%
    private static final int DEFAULT_LOAN_TERM_YEARS = 7;

    @FXML
    private TextField energyUsageField;
    @FXML
    private TextField HouseholdIncomeField;
    @FXML
    private TextField loanTermField;
    @FXML
    private TextField interestRateField;
    @FXML
    private ChoiceBox<String> regionDropDown;
    @FXML
    private VBox outputBox;

    private Region[] regions;

    @FXML
    void initialize() {
        initRegions();
        initDropdown();
    }

    // Initialize the regions with their respective data
    private void initRegions() {
        regions = new Region[] {
            new Region("Bomi", 4.6, 1.80, 0.55, 250, 0.35),
            new Region("Bong", 5.1, 1.60, 0.55, 220, 0.33),
            new Region("Gbarpolu", 4.9, 1.75, 0.45, 180, 0.31),
            new Region("Grand Bassa", 4.7, 1.55, 0.58, 280, 0.34),
            new Region("Grand Cape Mount", 4.7, 1.65, 0.50, 230, 0.34),
            new Region("Grand Gedeh", 5.2, 1.70, 0.45, 200, 0.32),
            new Region("Grand Kru", 4.9, 1.80, 0.45, 180, 0.31),
            new Region("Lofa", 5.0, 1.70, 0.50, 180, 0.31),
            new Region("Margibi", 4.6, 1.55, 0.58, 300, 0.35),
            new Region("Maryland", 5.0, 1.75, 0.45, 190, 0.31),
            new Region("Montserrado", 4.5, 1.50, 0.60, 350, 0.35),
            new Region("Nimba", 5.2, 1.65, 0.50, 200, 0.32),
            new Region("Rivercess", 4.8, 1.70, 0.48, 190, 0.32),
            new Region("River Gee", 5.1, 1.75, 0.45, 190, 0.31),
            new Region("Sinoe", 4.9, 1.70, 0.48, 190, 0.32)
        };
    }

    private void initDropdown() {
        for (Region region : regions) {
            regionDropDown.getItems().add(region.getRegionName());
        }
    }

    @FXML
    protected void onCalculateButtonClick() {
        performCalculations();
    }

    private static void errorDialogue(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void performCalculations() {
        // Validate inputs
        boolean inputValid = true;
        double householdIncome = 0;
        double energyUsage = 0;
        int loanTermYears = DEFAULT_LOAN_TERM_YEARS;
        double annualInterestRate = DEFAULT_ANNUAL_INTEREST_RATE;
        Region selectedRegion = null;

        try {
            householdIncome = Double.parseDouble(HouseholdIncomeField.getText().trim());
            if (householdIncome <= 0) {
                throw new NumberFormatException("Income must be positive");
            }
        } catch (NumberFormatException e) {
            inputValid = false;
            errorDialogue("Input Error", "Invalid Household Income Input",
                    "Please enter a valid positive number for household income.");
        }

        try {
            energyUsage = Double.parseDouble(energyUsageField.getText().trim());
            if (energyUsage <= 0) {
                throw new NumberFormatException("Energy usage must be positive");
            }
        } catch (NumberFormatException e) {
            inputValid = false;
            errorDialogue("Input Error", "Invalid Energy Usage Input",
                    "Please enter a valid positive number for energy usage.");
        }

        try {
            loanTermYears = Integer.parseInt(loanTermField.getText().trim());
            if (loanTermYears <= 0 || loanTermYears > 30) {
                throw new NumberFormatException("Loan term must be between 1 and 30 years");
            }
        } catch (NumberFormatException e) {
            inputValid = false;
            errorDialogue("Input Error", "Invalid Loan Term",
                    "Please enter a valid loan term between 1 and 30 years.");
        }

        try {
            annualInterestRate = Double.parseDouble(interestRateField.getText().trim()) / 100.0;
            if (annualInterestRate < 0 || annualInterestRate > 0.5) {
                throw new NumberFormatException("Interest rate must be between 0 and 50%");
            }
        } catch (NumberFormatException e) {
            inputValid = false;
            errorDialogue("Input Error", "Invalid Interest Rate",
                    "Please enter a valid interest rate between 0 and 50%.");
        }

        try {
            selectedRegion = findRegionByName(regionDropDown.getValue());
        } catch (IllegalArgumentException e) {
            inputValid = false;
            errorDialogue("Selection Error", "No Region Selected",
                    "Please select a region from the dropdown.");
        }

        if (!inputValid) {
            return;
        }

        // Perform calculations for cost estimation
        double peakSunHours = selectedRegion.getPeakSunHours();
        double laborCostPerWatt = selectedRegion.getLaborCostPerWatt();
        double hardwareCostPerWatt = selectedRegion.getHardwareCostPerWatt();
        double avgPermitCost = selectedRegion.getAvgPermitCost();

        double adjustedSystemSize = Math.ceil(energyUsage / peakSunHours);

        double totalCost = ((adjustedSystemSize * 1000) * (laborCostPerWatt + hardwareCostPerWatt) + avgPermitCost);
        double monthlySaving = energyUsage * selectedRegion.getUtilityRatePerKWh();
        double payBackMonths = totalCost / monthlySaving;
        double payBackYears = payBackMonths / 12;

        // Calculate financial investment metrics with user-provided loan parameters
        double investmentRatio = calculateInvestmentRatio(totalCost, householdIncome);
        double budgetImpact = calculateBudgetImpact(monthlySaving, householdIncome);
        double monthlyLoanPayment = calculateMonthlyLoanPayment(totalCost, annualInterestRate, loanTermYears);
        double netMonthlyCashFlow = monthlySaving - monthlyLoanPayment;

        // Display results with graph
        outputResults(totalCost, monthlySaving, payBackYears, householdIncome,
                     investmentRatio, budgetImpact, monthlyLoanPayment, netMonthlyCashFlow,
                     annualInterestRate, loanTermYears);
    }

    // Calculate investment ratio as percentage of annual income
    private double calculateInvestmentRatio(double totalCost, double monthlyIncome) {
        double annualIncome = monthlyIncome * 12;
        return (totalCost / annualIncome) * 100;
    }

    // Calculate monthly budget impact as percentage
    private double calculateBudgetImpact(double monthlySavings, double monthlyIncome) {
        return (monthlySavings / monthlyIncome) * 100;
    }

    // Calculate monthly loan payment using amortization formula with custom parameters
    private double calculateMonthlyLoanPayment(double principal, double annualRate, int years) {
        if (annualRate == 0) {
            // If interest rate is 0, just divide principal by number of months
            return principal / (years * 12);
        }

        double monthlyInterestRate = annualRate / 12;
        int totalPayments = years * 12;

        double numerator = monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPayments);
        double denominator = Math.pow(1 + monthlyInterestRate, totalPayments) - 1;

        return principal * (numerator / denominator);
    }

    // Helper method to find a Region by its name
    private Region findRegionByName(String selectedName) {
        if (selectedName == null) {
            System.out.println("No region selected or region not found.");
            throw new IllegalArgumentException();
        }
        for (Region region : regions) {
            if (region.getRegionName().equals(selectedName)) {
                System.out.println("Calculating for region: " + selectedName);
                return region;
            }
        }
        throw new IllegalArgumentException();
    }

    private void outputResults(double totalCost, double monthlySavings, double paybackYears,
                              double monthlyIncome, double investmentRatio, double budgetImpact,
                              double monthlyLoanPayment, double netMonthlyCashFlow,
                              double annualInterestRate, int loanTermYears) {
        // Clear previous results
        outputBox.getChildren().clear();
        outputBox.setSpacing(10);
        outputBox.setPadding(new Insets(8));

        // Title
        Label titleLabel = new Label("SOLAR INVESTMENT ANALYSIS");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        outputBox.getChildren().add(titleLabel);

        // System Cost & Savings Row
        HBox systemInfoBox = createMetricCard("System Cost", String.format("$%.2f", totalCost),
                                              "Monthly Savings", String.format("$%.2f", monthlySavings),
                                              "Payback Period", String.format("%.1f years", paybackYears));
        outputBox.getChildren().add(systemInfoBox);

        // Affordability Analysis Row
        Label affordabilityTitle = new Label("AFFORDABILITY ANALYSIS");
        affordabilityTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        outputBox.getChildren().add(affordabilityTitle);

        HBox affordabilityBox = createMetricCard("Investment Ratio", String.format("%.1f%%", investmentRatio),
                                                 "Budget Impact", String.format("%.1f%%", budgetImpact),
                                                 "Monthly Income", String.format("$%.2f", monthlyIncome));
        outputBox.getChildren().add(affordabilityBox);

        // Financing Option Row - use actual values from user input
        Label financingTitle = new Label(String.format("FINANCING OPTION (%d-Year Loan at %.1f%%)",
                                         loanTermYears, annualInterestRate * 100));
        financingTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        outputBox.getChildren().add(financingTitle);

        HBox financingBox = createMetricCard("Loan Payment", String.format("$%.2f/mo", monthlyLoanPayment),
                                            "Monthly Savings", String.format("$%.2f/mo", monthlySavings),
                                            "Loan Term", String.format("%d years", loanTermYears));
        outputBox.getChildren().add(financingBox);

        // Net Monthly Cash Flow (The "Aha!" Moment)
        VBox cashFlowBox = new VBox(3);
        cashFlowBox.setAlignment(Pos.CENTER);
        cashFlowBox.setPadding(new Insets(10));
        cashFlowBox.setStyle("-fx-background-color: #f0f8ff; -fx-border-color: #4a90e2; " +
                           "-fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label cashFlowLabel = new Label("💡 NET MONTHLY CASH FLOW");
        cashFlowLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        Label cashFlowAmount = new Label(String.format("$%.2f", netMonthlyCashFlow));
        cashFlowAmount.setStyle(String.format("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: %s;",
                                            netMonthlyCashFlow > 0 ? "green" : "orange"));

        cashFlowBox.getChildren().addAll(cashFlowLabel, cashFlowAmount);

        if (netMonthlyCashFlow > 0) {
            Label conclusionLabel = new Label(String.format(
                "✓ The solar system pays for its own financing from month 1, " +
                "leaving you with an extra $%.2f every month!",
                netMonthlyCashFlow));
            conclusionLabel.setWrapText(true);
            conclusionLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px; -fx-text-alignment: center;");
            conclusionLabel.setMaxWidth(400);
            cashFlowBox.getChildren().add(conclusionLabel);
        } else {
            Label warningLabel = new Label(
                "⚠ Monthly loan payments exceed savings. Consider a longer loan term or larger down payment.");
            warningLabel.setWrapText(true);
            warningLabel.setStyle("-fx-text-fill: orange; -fx-font-size: 12px; -fx-text-alignment: center;");
            warningLabel.setMaxWidth(400);
            cashFlowBox.getChildren().add(warningLabel);
        }

        outputBox.getChildren().add(cashFlowBox);

        // Add the financial graph with custom loan parameters
        Label chartTitle = new Label("FINANCIAL PROJECTION OVER TIME");
        chartTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        outputBox.getChildren().add(chartTitle);

        LineChart<Number, Number> chart = createFinancialChart(totalCost, monthlySavings, monthlyLoanPayment, loanTermYears);
        outputBox.getChildren().add(chart);
    }

    private HBox createMetricCard(String label1, String value1, String label2, String value2, String label3, String value3) {
        HBox container = new HBox(8);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(8));
        container.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; " +
                          "-fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        VBox card1 = createMetricBox(label1, value1);
        VBox card2 = createMetricBox(label2, value2);
        VBox card3 = createMetricBox(label3, value3);

        container.getChildren().addAll(card1, createSeparator(), card2, createSeparator(), card3);
        return container;
    }

    private VBox createMetricBox(String label, String value) {
        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(120);
        box.setPadding(new Insets(6));

        Label labelText = new Label(label);
        labelText.setStyle("-fx-font-size: 9px; -fx-text-fill: #666666;");
        labelText.setWrapText(true);

        Label valueText = new Label(value);
        valueText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        valueText.setWrapText(true);

        box.getChildren().addAll(labelText, valueText);
        return box;
    }

    private VBox createSeparator() {
        VBox separator = new VBox();
        separator.setPrefWidth(1);
        separator.setStyle("-fx-background-color: #dddddd;");
        return separator;
    }

    private LineChart<Number, Number> createFinancialChart(double totalCost, double monthlySavings,
                                                          double monthlyLoanPayment, int loanTermYears) {
        // Create axes
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Months");
        xAxis.setTickLabelFont(javafx.scene.text.Font.font(9));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Cumulative Amount ($)");
        yAxis.setTickLabelFont(javafx.scene.text.Font.font(9));

        // Create chart
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Savings vs. Loan Payments Over Time");
        chart.setCreateSymbols(false);
        chart.setPrefHeight(250);
        chart.setMinHeight(250);
        chart.setMaxHeight(250);
        chart.setLegendVisible(true);

        // Create data series
        XYChart.Series<Number, Number> savingsSeries = new XYChart.Series<>();
        savingsSeries.setName("Cumulative Savings");

        XYChart.Series<Number, Number> loanSeries = new XYChart.Series<>();
        loanSeries.setName("Cumulative Loan Payments");

        XYChart.Series<Number, Number> netSeries = new XYChart.Series<>();
        netSeries.setName("Net Position (Savings - Loan)");

        // Calculate data points for up to 10 years or until loan is paid off
        int maxMonths = Math.max(loanTermYears * 12, (int) Math.ceil(totalCost / monthlySavings));
        maxMonths = Math.min(maxMonths, 120); // Cap at 10 years for display

        double cumulativeSavings = 0;
        double cumulativeLoanPayments = 0;
        int breakEvenMonth = -1;

        for (int month = 0; month <= maxMonths; month++) {
            if (month > 0) {
                cumulativeSavings += monthlySavings;
                // Only add loan payments if within loan term
                if (month <= loanTermYears * 12) {
                    cumulativeLoanPayments += monthlyLoanPayment;
                }
            }

            double netPosition = cumulativeSavings - cumulativeLoanPayments;

            savingsSeries.getData().add(new XYChart.Data<>(month, cumulativeSavings));
            loanSeries.getData().add(new XYChart.Data<>(month, cumulativeLoanPayments));
            netSeries.getData().add(new XYChart.Data<>(month, netPosition));

            // Find break-even point (when net position becomes positive)
            if (breakEvenMonth == -1 && netPosition >= 0 && month > 0) {
                breakEvenMonth = month;
            }
        }

        // Add series to chart
        chart.getData().addAll(savingsSeries, loanSeries, netSeries);

        // Add break-even information
        if (breakEvenMonth > 0) {
            Label breakEvenLabel = new Label(String.format(
                "✓ Break-even point: Month %d (%.1f years) - After this, you're in profit!",
                breakEvenMonth, breakEvenMonth / 12.0));
            breakEvenLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            outputBox.getChildren().add(breakEvenLabel);
        }

        return chart;
    }
}
