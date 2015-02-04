package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OleSourceContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * OleSourceBo provides OleSourceBo information through getter and setter.
 */
public class OleSourceBo extends PersistableBusinessObjectBase implements OleSourceContract {

    private String oleSourceId;
    private String oleSourceCode;
    private String oleSourceName;
    private String oleSourceDesc;
    private boolean active;

    /**
     * Gets the value of oleSourceId property
     *
     * @return oleSourceId
     */
    public String getOleSourceId() {
        return oleSourceId;
    }

    /**
     * Sets the value for oleSourceId property
     *
     * @param oleSourceId
     */
    public void setOleSourceId(String oleSourceId) {
        this.oleSourceId = oleSourceId;
    }

    /**
     * Gets the value of oleSourceCode property
     *
     * @return oleSourceCode
     */
    public String getOleSourceCode() {
        return oleSourceCode;
    }

    /**
     * Sets the value for oleSourceCode property
     *
     * @param oleSourceCode
     */
    public void setOleSourceCode(String oleSourceCode) {
        this.oleSourceCode = oleSourceCode;
    }

    /**
     * Gets the value of oleSourceName property
     *
     * @return oleSourceName
     */
    public String getOleSourceName() {
        return oleSourceName;
    }

    /**
     * Sets the value for oleSourceName property
     *
     * @param oleSourceName
     */
    public void setOleSourceName(String oleSourceName) {
        this.oleSourceName = oleSourceName;
    }

    /**
     * Gets the value of oleSourceDesc property
     *
     * @return oleSourceDesc
     */
    public String getOleSourceDesc() {
        return oleSourceDesc;
    }

    /**
     * Sets the value for oleSourceDesc property
     *
     * @param oleSourceDesc
     */
    public void setOleSourceDesc(String oleSourceDesc) {
        this.oleSourceDesc = oleSourceDesc;
    }

    /**
     * Gets the value of active property
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value for active property
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    @Override
    public String getId() {
        return this.getOleSourceId();
    }
}
