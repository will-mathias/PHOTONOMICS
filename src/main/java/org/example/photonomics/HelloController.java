package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    TextField energyUsageField;
    @FXML
    TextField HouseholdIncomeField;
    @FXML
    ChoiceBox<String> regionDropDown;

    @FXML
    protected void onCalculateButtonClick() {
        System.out.println("Calculating!");
    }
}
