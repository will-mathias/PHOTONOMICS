package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class HelloController {
    @FXML
    TextField energyUsageField;
    @FXML
    TextField HouseholdIncomeField;
    @FXML
    ChoiceBox<String> regionDropDown;

    @FXML
    void initialize() {
        initDropdown();

    }

    private void initDropdown() {
        String[] regions = {
                "Bomi",
                "Bong",
                "Gbarpolu",
                "Grand Bassa",
                "Grand Cape Mount",
                "Grand Gedeh",
                "Grand Kru",
                "Lofa",
                "Margibi",
                "Maryland",
                "Montserrado",
                "Nimba",
                "Rivercess",
                "River Gee",
                "Sinoe"
        };

        for (String region : regions) {
            regionDropDown.getItems().add(region);
        }
    }

    @FXML
    protected void onCalculateButtonClick() {
        System.out.println("Calculating!");
    }
}
