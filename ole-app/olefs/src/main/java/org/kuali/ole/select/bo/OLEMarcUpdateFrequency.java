package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by maheswarang on 5/8/15.
 */
public class OLEMarcUpdateFrequency extends PersistableBusinessObjectBase{
    private String marcUpdateFrequencyId;
    private String marcUpdateFrequencyCode;
    private String marcUpdateFrequencyName;
    private String marcUpdateFrequencyDescription;
    private Integer marcUpdateFrequency;
    private boolean active;

    public String getMarcUpdateFrequencyId() {
        return marcUpdateFrequencyId;
    }

    public void setMarcUpdateFrequencyId(String marcUpdateFrequencyId) {
        this.marcUpdateFrequencyId = marcUpdateFrequencyId;
    }

    public String getMarcUpdateFrequencyCode() {
        return marcUpdateFrequencyCode;
    }

    public void setMarcUpdateFrequencyCode(String marcUpdateFrequencyCode) {
        this.marcUpdateFrequencyCode = marcUpdateFrequencyCode;
    }

    public String getMarcUpdateFrequencyName() {
        return marcUpdateFrequencyName;
    }

    public String getMarcUpdateFrequencyDescription() {
        return marcUpdateFrequencyDescription;
    }

    public void setMarcUpdateFrequencyDescription(String marcUpdateFrequencyDescription) {
        this.marcUpdateFrequencyDescription = marcUpdateFrequencyDescription;
    }

    public void setMarcUpdateFrequencyName(String marcUpdateFrequencyName) {
        this.marcUpdateFrequencyName = marcUpdateFrequencyName;


    }

    public Integer getMarcUpdateFrequency() {
        return marcUpdateFrequency;
    }

    public void setMarcUpdateFrequency(Integer marcUpdateFrequency) {
        this.marcUpdateFrequency = marcUpdateFrequency;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
