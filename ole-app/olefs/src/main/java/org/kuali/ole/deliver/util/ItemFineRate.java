package org.kuali.ole.deliver.util;

/**
 * Created by pvsubrah on 7/21/15.
 */
public class ItemFineRate {
    private Double fineRate;
    private Double maxFine;
    private String interval;

    public Double getMaxFine() {
        return maxFine;
    }

    public void setMaxFine(Double maxFine) {
        this.maxFine = maxFine;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Double getFineRate() {
        return fineRate;
    }

    public void setFineRate(Double fineRate) {
        this.fineRate = fineRate;
    }
}
