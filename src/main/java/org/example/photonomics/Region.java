package org.example.photonomics;

public class Region {

    private final String regionName;
    private final double peakSunHours;
    private final double laborCostPerWatt;
    private final double hardwareCostPerWatt;
    private final double avgPermitCost;
    private final double utilityRatePerKWh;
    private final double lossFactor;

    public Region(String regionName, double peakSunHours, double laborCostPerWatt,
                  double hardwareCostPerWatt, double avgPermitCost,
                  double utilityRatePerKWh, double lossFactor) {
        this.regionName = regionName;
        this.peakSunHours = peakSunHours;
        this.laborCostPerWatt = laborCostPerWatt;
        this.hardwareCostPerWatt = hardwareCostPerWatt;
        this.avgPermitCost = avgPermitCost;
        this.utilityRatePerKWh = utilityRatePerKWh;
        this.lossFactor = lossFactor;
    }

    public String getRegionName() { return regionName; }
    public double getPeakSunHours() { return peakSunHours; }
    public double getLaborCostPerWatt() { return laborCostPerWatt; }
    public double getHardwareCostPerWatt() { return hardwareCostPerWatt; }
    public double getAvgPermitCost() { return avgPermitCost; }
    public double getUtilityRatePerKWh() { return utilityRatePerKWh; }
    public double getLossFactor() { return lossFactor; }

    @Override
    public String toString() { return regionName; }
}
