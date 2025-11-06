package org.example.photonomics;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField energyUsageField;
    @FXML
    private TextField HouseholdIncomeField;
    @FXML
    private ChoiceBox<String> regionDropDown;

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
        String selectedRegion = regionDropDown.getValue();
        Region region = findRegionByName(selectedRegion);
        if (region != null) {
            System.out.println("Calculating for region: " + region.getRegionName());
        }
        else {
            System.out.println("No region selected or region not found.");
        }
    }

    // Helper method to find a Region by its name
    private Region findRegionByName(String name) {
        if (name == null) {
            return null;
        }
        for (Region region : regions) {
            if (region.getRegionName().equals(name)) {
                return region;
            }
        }
        return null;
    }
}