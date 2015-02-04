package org.kuali.ole.bo.serachRetrieve;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/17/12
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUCirculationDocument {
    public String availableNow;
    public String availabilityDate;
    public String availableThru;
    public String restrictions;
    public String itemId;
    public String renewable;
    public String onHold;
    public String enumAndChron;
    public String midspine;
    public String temporaryLocation;

    public String getAvailableNow() {
        return availableNow;
    }

    public void setAvailableNow(String availableNow) {
        this.availableNow = availableNow;
    }

    public String getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(String availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public String getAvailableThru() {
        return availableThru;
    }

    public void setAvailableThru(String availableThru) {
        this.availableThru = availableThru;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRenewable() {
        return renewable;
    }

    public void setRenewable(String renewable) {
        this.renewable = renewable;
    }

    public String getOnHold() {
        return onHold;
    }

    public void setOnHold(String onHold) {
        this.onHold = onHold;
    }

    public String getEnumAndChron() {
        return enumAndChron;
    }

    public void setEnumAndChron(String enumAndChron) {
        this.enumAndChron = enumAndChron;
    }

    public String getMidspine() {
        return midspine;
    }

    public void setMidspine(String midspine) {
        this.midspine = midspine;
    }

    public String getTemporaryLocation() {
        return temporaryLocation;
    }

    public void setTemporaryLocation(String temporaryLocation) {
        this.temporaryLocation = temporaryLocation;
    }

    @Override
    public String toString() {
        return "OleSRUCirculationDocument{" +
                "availableNow='" + availableNow + '\'' +
                ", availabilityDate='" + availabilityDate + '\'' +
                ", availableThru='" + availableThru + '\'' +
                ", restrictions='" + restrictions + '\'' +
                ", itemId='" + itemId + '\'' +
                ", renewable='" + renewable + '\'' +
                ", onHold='" + onHold + '\'' +
                ", enumAndChron='" + enumAndChron + '\'' +
                ", midspine='" + midspine + '\'' +
                ", temporaryLocation='" + temporaryLocation + '\'' +
                '}';
    }
}
