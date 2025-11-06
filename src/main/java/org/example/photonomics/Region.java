package org.example.photonomics;

public class Region {
    private final String regionName;
    private final double peakSunHours;
    private final double HardwareCostPerWatt;
    private final double laborCostPerWatt;
    private final double avgPermitCost;
    private final double utilityRatePerKWh;

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
