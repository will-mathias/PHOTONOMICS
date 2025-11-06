package org.example.photonomics;

public class Region {
    private String regionName;
    private double peakSunHours;
    private double HardwareCostPerWatt;
    private double laborCostPerWatt;
    private double avgPermitCost;
    private double utilityRatePerKWh;

    public Region(String regionName, double peakSunHours, double hardwareCostPerWatt, double laborCostPerWatt, double avgPermitCost, double utilityRatePerKWh) {
        this.regionName = regionName;
        this.peakSunHours = peakSunHours;
        this.HardwareCostPerWatt = hardwareCostPerWatt;
        this.laborCostPerWatt = laborCostPerWatt;
        this.avgPermitCost = avgPermitCost;
        this.utilityRatePerKWh = utilityRatePerKWh;
    }

    public String getRegionName() {
        return regionName;
    }

    public double getPeakSunHours() {
        return peakSunHours;
    }

    public double getHardwareCostPerWatt() {
        return HardwareCostPerWatt;
    }

    public double getLaborCostPerWatt() {
        return laborCostPerWatt;
    }

    public double getAvgPermitCost() {
        return avgPermitCost;
    }

    public double getUtilityRatePerKWh() {
        return utilityRatePerKWh;
    }
}
