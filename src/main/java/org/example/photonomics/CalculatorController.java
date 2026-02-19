package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class CalculatorController {

    private static final double DEFAULT_ANNUAL_INTEREST_RATE = 0.08; // 8%
    private static final int DEFAULT_LOAN_TERM_YEARS = 7;

    @FXML private TextField energyUsageField;
    @FXML private TextField HouseholdIncomeField;
    @FXML private TextField loanTermField;
    @FXML private TextField interestRateField;
    @FXML private ChoiceBox<String> regionDropDown;
    @FXML private VBox outputBox;

    private Region[] regions;

    @FXML
    void initialize() {
        initRegions();
        initDropdown();
    }

    private void initRegions() {
        regions = new Region[] {
                new Region("Bomi", 4.6, 1.80, 0.55, 250, 0.35),
                new Region("Bong", 5.1, 1.60, 0.55, 220, 0.33),
                new Region("Montserrado", 4.5, 1.50, 0.60, 350, 0.35),
                new Region("Margibi", 4.6, 1.55, 0.58, 300, 0.35),
                new Region("Nimba", 5.2, 1.65, 0.50, 200, 0.32)
                // add more regions as needed
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
        boolean inputValid = true;
        double householdIncome = 0;
        double energyUsage = 0;
        int loanTermYears = DEFAULT_LOAN_TERM_YEARS;
        double annualInterestRate = DEFAULT_ANNUAL_INTEREST_RATE;
        Region selectedRegion = null;

        // Validate inputs
        try { householdIncome = Double.parseDouble(HouseholdIncomeField.getText().trim()); if(householdIncome<=0)throw new NumberFormatException(); }
        catch(NumberFormatException e){inputValid=false; errorDialogue("Input Error","Invalid Household Income","Enter a positive number.");}

        try { energyUsage = Double.parseDouble(energyUsageField.getText().trim()); if(energyUsage<=0)throw new NumberFormatException(); }
        catch(NumberFormatException e){inputValid=false; errorDialogue("Input Error","Invalid Energy Usage","Enter a positive number.");}

        try { loanTermYears = Integer.parseInt(loanTermField.getText().trim()); if(loanTermYears<=0||loanTermYears>30)throw new NumberFormatException(); }
        catch(NumberFormatException e){inputValid=false; errorDialogue("Input Error","Invalid Loan Term","Enter between 1-30 years.");}

        try { annualInterestRate = Double.parseDouble(interestRateField.getText().trim())/100.0; if(annualInterestRate<0||annualInterestRate>0.5)throw new NumberFormatException(); }
        catch(NumberFormatException e){inputValid=false; errorDialogue("Input Error","Invalid Interest Rate","Enter between 0-50%.");}

        try { selectedRegion = findRegionByName(regionDropDown.getValue()); }
        catch(IllegalArgumentException e){inputValid=false; errorDialogue("Selection Error","No Region Selected","Please select a region.");}

        if(!inputValid) return;

        // Calculations
        double peakSunHours = selectedRegion.getPeakSunHours();
        double laborCostPerWatt = selectedRegion.getLaborCostPerWatt();
        double hardwareCostPerWatt = selectedRegion.getHardwareCostPerWatt();
        double avgPermitCost = selectedRegion.getAvgPermitCost();
        double utilityRate = selectedRegion.getUtilityRatePerKWh();

        double adjustedSystemSizeKW = energyUsage / (peakSunHours * 30);
        double totalCost = (adjustedSystemSizeKW*1000)*(laborCostPerWatt+hardwareCostPerWatt) + avgPermitCost;
        double monthlySaving = energyUsage * utilityRate;
        double payBackYears = totalCost / monthlySaving /12.0;
        double investmentRatio = calculateInvestmentRatio(totalCost, householdIncome);
        double budgetImpact = calculateBudgetImpact(monthlySaving, householdIncome);
        double monthlyLoanPayment = calculateMonthlyLoanPayment(totalCost, annualInterestRate, loanTermYears);
        double netMonthlyCashFlow = monthlySaving - monthlyLoanPayment;

        outputResults(totalCost, monthlySaving, payBackYears, householdIncome,
                investmentRatio, budgetImpact, monthlyLoanPayment, netMonthlyCashFlow,
                annualInterestRate, loanTermYears, adjustedSystemSizeKW);
    }

    private Region findRegionByName(String name){
        if(name==null) throw new IllegalArgumentException();
        for(Region r:regions) if(r.getRegionName().equals(name)) return r;
        throw new IllegalArgumentException();
    }

    private double calculateInvestmentRatio(double totalCost, double monthlyIncome){
        return (totalCost / (monthlyIncome*12)) * 100;
    }

    private double calculateBudgetImpact(double monthlySavings, double monthlyIncome){
        return (monthlySavings / monthlyIncome)*100;
    }

    private double calculateMonthlyLoanPayment(double principal, double annualRate, int years){
        if(annualRate==0) return principal/(years*12);
        double r = annualRate/12;
        int n = years*12;
        return principal*(r*Math.pow(1+r,n)/(Math.pow(1+r,n)-1));
    }

    // Output results in the responsive layout
    private void outputResults(double totalCost, double monthlySavings, double paybackYears,
                               double monthlyIncome, double investmentRatio, double budgetImpact,
                               double monthlyLoanPayment, double netMonthlyCashFlow,
                               double annualInterestRate, int loanTermYears, double systemSizeKW){

        outputBox.getChildren().clear();
        outputBox.setSpacing(10);
        outputBox.setPadding(new Insets(8));

        Label title = new Label("SOLAR INVESTMENT ANALYSIS");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        outputBox.getChildren().add(title);

        // System Info
        HBox systemInfo = createMetricCard("System Size", String.format("%.2f kW", systemSizeKW),
                "Total Cost", String.format("$%.2f", totalCost),
                "Monthly Savings", String.format("$%.2f", monthlySavings));
        outputBox.getChildren().add(systemInfo);

        // Payback
        HBox paybackInfo = createMetricCard("Payback Period", String.format("%.1f years", paybackYears),
                "Annual Savings", String.format("$%.2f", monthlySavings*12),
                "25-Year Savings", String.format("$%.2f", monthlySavings*12*25));
        outputBox.getChildren().add(paybackInfo);

        // Affordability
        Label affTitle = new Label("AFFORDABILITY ANALYSIS");
        affTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        outputBox.getChildren().add(affTitle);

        HBox affBox = createMetricCard("Investment Ratio", String.format("%.1f%%", investmentRatio),
                "Budget Impact", String.format("%.1f%%", budgetImpact),
                "Monthly Income", String.format("$%.2f", monthlyIncome));
        outputBox.getChildren().add(affBox);

        // Financing
        Label financeTitle = new Label(String.format("FINANCING OPTION (%d-Year Loan at %.1f%%)",
                loanTermYears, annualInterestRate*100));
        financeTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        outputBox.getChildren().add(financeTitle);

        HBox financeBox = createMetricCard("Monthly Payment", String.format("$%.2f", monthlyLoanPayment),
                "Interest Rate", String.format("%.1f%%", annualInterestRate*100),
                "Total Interest", String.format("$%.2f", (monthlyLoanPayment*loanTermYears*12)-totalCost));
        outputBox.getChildren().add(financeBox);

        // Net cash flow
        VBox cashFlowBox = new VBox(3);
        cashFlowBox.setAlignment(Pos.CENTER);
        cashFlowBox.setPadding(new Insets(10));
        cashFlowBox.setStyle("-fx-background-color:#f0f8ff; -fx-border-color:#4a90e2; -fx-border-width:2px; -fx-border-radius:5px; -fx-background-radius:5px;");

        Label cashFlowLabel = new Label("💡 NET MONTHLY CASH FLOW");
        cashFlowLabel.setStyle("-fx-font-size:12px; -fx-font-weight:bold;");

        Label cashFlowAmount = new Label(String.format("$%.2f", netMonthlyCashFlow));
        cashFlowAmount.setStyle(String.format("-fx-font-size:18px; -fx-font-weight:bold; -fx-text-fill:%s;",
                netMonthlyCashFlow>0?"green":"orange"));

        cashFlowBox.getChildren().addAll(cashFlowLabel, cashFlowAmount);

        if(netMonthlyCashFlow>0){
            Label conclusion = new Label(String.format(
                    "✓ The solar system pays for its financing from month 1, leaving you with $%.2f extra each month.",
                    netMonthlyCashFlow));
            conclusion.setWrapText(true);
            conclusion.setStyle("-fx-text-fill:green;-fx-font-size:12px;");
            conclusion.setMaxWidth(600);
            cashFlowBox.getChildren().add(conclusion);
        } else {
            Label warning = new Label(
                    "⚠ Monthly loan payments exceed savings. Consider a longer loan term or larger down payment.");
            warning.setWrapText(true);
            warning.setStyle("-fx-text-fill:orange;-fx-font-size:12px;");
            warning.setMaxWidth(600);
            cashFlowBox.getChildren().add(warning);
        }

        outputBox.getChildren().add(cashFlowBox);

        // Financial chart
        LineChart<Number, Number> chart = createFinancialChart(totalCost, monthlySavings, monthlyLoanPayment, loanTermYears);
        outputBox.getChildren().add(chart);
    }

    private HBox createMetricCard(String label1,String value1,String label2,String value2,String label3,String value3){
        HBox container = new HBox(8);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(8));
        container.setStyle("-fx-background-color:#fff;-fx-border-color:#ccc;-fx-border-width:1px;-fx-border-radius:5px;-fx-background-radius:5px;");

        VBox card1 = createMetricBox(label1,value1);
        VBox card2 = createMetricBox(label2,value2);
        VBox card3 = createMetricBox(label3,value3);

        container.getChildren().addAll(card1, createSeparator(), card2, createSeparator(), card3);
        return container;
    }

    private VBox createMetricBox(String label,String value){
        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(120);
        box.setPadding(new Insets(6));

        Label labelText = new Label(label);
        labelText.setStyle("-fx-font-size:9px;-fx-text-fill:#666;");
        labelText.setWrapText(true);

        Label valueText = new Label(value);
        valueText.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:#333;");
        valueText.setWrapText(true);

        box.getChildren().addAll(labelText,valueText);
        return box;
    }

    private VBox createSeparator(){
        VBox sep = new VBox();
        sep.setPrefWidth(1);
        sep.setStyle("-fx-background-color:#ddd;");
        return sep;
    }

    private LineChart<Number, Number> createFinancialChart(double totalCost, double monthlySavings, double monthlyLoanPayment, int loanTermYears){
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Months");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Cumulative Amount ($)");

        LineChart<Number, Number> chart = new LineChart<>(xAxis,yAxis);
        chart.setTitle("Savings vs. Loan Payments Over Time");
        chart.setCreateSymbols(false);
        chart.setPrefHeight(250);
        chart.setLegendVisible(true);

        XYChart.Series<Number, Number> savingsSeries = new XYChart.Series<>();
        savingsSeries.setName("Cumulative Savings");
        XYChart.Series<Number, Number> loanSeries = new XYChart.Series<>();
        loanSeries.setName("Cumulative Loan Payments");
        XYChart.Series<Number, Number> netSeries = new XYChart.Series<>();
        netSeries.setName("Net Position");

        int maxMonths = Math.max(loanTermYears*12,(int)Math.ceil(totalCost/monthlySavings));
        maxMonths = Math.min(maxMonths,120); // limit for display

        double cumulativeSavings=0;
        double cumulativeLoan=0;
        for(int m=0;m<=maxMonths;m++){
            if(m>0){
                cumulativeSavings+=monthlySavings;
                if(m<=loanTermYears*12) cumulativeLoan+=monthlyLoanPayment;
            }
            double net=cumulativeSavings-cumulativeLoan;
            savingsSeries.getData().add(new XYChart.Data<>(m,cumulativeSavings));
            loanSeries.getData().add(new XYChart.Data<>(m,cumulativeLoan));
            netSeries.getData().add(new XYChart.Data<>(m,net));
        }

        chart.getData().addAll(savingsSeries,loanSeries,netSeries);
        return chart;
    }
}



