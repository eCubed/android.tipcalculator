package net.idfernando.android.tipcalculator;

/**
 * Created by Iz on 12/9/2016.
 */

public class TipEntry {

    private double percentage;
    private double initialChargeAmount;

    public TipEntry(double percentage, double initialChargeAmount) {
        this.percentage = percentage;
        this.initialChargeAmount = initialChargeAmount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double calculateTipAmount() {
        return percentage * initialChargeAmount;
    }

    public double getInitialChargeAmount() {
        return initialChargeAmount;
    }

    public void setInitialChargeAmount(double initialChargeAmount) {
        this.initialChargeAmount = initialChargeAmount;
    }

    public double calculateTotalAmount(){
        return initialChargeAmount + calculateTipAmount();
    }


}
