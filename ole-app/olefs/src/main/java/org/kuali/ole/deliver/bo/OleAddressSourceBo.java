package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OleAddressSourceContract;
import org.kuali.ole.deliver.api.OleAddressSourceDefinition;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * OleAddressSourceBo provides OleAddressSourceBo information through getter and setter.
 */
public class OleAddressSourceBo extends PersistableBusinessObjectBase implements OleAddressSourceContract {

    private String oleAddressSourceId;
    private String oleAddressSourceCode;
    private String oleAddressSourceName;
    private String oleAddressSourceDesc;
    private boolean active;

    /**
     * Gets the value of oleAddressSourceId property
     *
     * @return oleAddressSourceId
     */
    public String getOleAddressSourceId() {
        return oleAddressSourceId;
    }

    /**
     * Sets the value for oleAddressSourceId property
     *
     * @param oleAddressSourceId
     */
    public void setOleAddressSourceId(String oleAddressSourceId) {
        this.oleAddressSourceId = oleAddressSourceId;
    }

    /**
     * Gets the value of oleAddressSourceCode property
     *
     * @return oleAddressSourceCode
     */
    public String getOleAddressSourceCode() {
        return oleAddressSourceCode;
    }

    /**
     * Sets the value for oleAddressSourceCode property
     *
     * @param oleAddressSourceCode
     */
    public void setOleAddressSourceCode(String oleAddressSourceCode) {
        this.oleAddressSourceCode = oleAddressSourceCode;
    }

    /**
     * Gets the value of oleAddressSourceName property
     *
     * @return oleAddressSourceName
     */
    public String getOleAddressSourceName() {
        return oleAddressSourceName;
    }

    /**
     * Sets the value for oleAddressSourceName property
     *
     * @param oleAddressSourceName
     */
    public void setOleAddressSourceName(String oleAddressSourceName) {
        this.oleAddressSourceName = oleAddressSourceName;
    }

    /**
     * Gets the value of oleAddressSourceDesc property
     *
     * @return oleAddressSourceDesc
     */
    public String getOleAddressSourceDesc() {
        return oleAddressSourceDesc;
    }

    /**
     * Sets the value for oleAddressSourceDesc property
     *
     * @param oleAddressSourceDesc
     */
    public void setOleAddressSourceDesc(String oleAddressSourceDesc) {
        this.oleAddressSourceDesc = oleAddressSourceDesc;
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


    /**
     * This method converts the PersistableBusinessObjectBase OlePatronNotes into immutable object OlePatronNotesDefinition
     *
     * @param bo
     * @return OlePatronNotesDefinition
     */
    public static OleAddressSourceDefinition to(org.kuali.ole.deliver.bo.OleAddressSourceBo bo) {
        if (bo == null) {
            return null;
        }
        return OleAddressSourceDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OlePatronNotesDefinition into PersistableBusinessObjectBase OlePatronNotes
     *
     * @param imOleAddressSource
     * @return bo
     */
    public static org.kuali.ole.deliver.bo.OleAddressSourceBo from(OleAddressSourceDefinition imOleAddressSource) {
        if (imOleAddressSource == null) {
            return null;
        }

        org.kuali.ole.deliver.bo.OleAddressSourceBo bo = new org.kuali.ole.deliver.bo.OleAddressSourceBo();
        bo.oleAddressSourceCode = imOleAddressSource.getOleAddressSourceCode();
        bo.oleAddressSourceDesc = imOleAddressSource.getOleAddressSourceDesc();
        bo.oleAddressSourceId = imOleAddressSource.getOleAddressSourceId();
        bo.oleAddressSourceName = imOleAddressSource.getOleAddressSourceName();

        return bo;
    }

    @Override
    public String getId() {
        return this.getOleAddressSourceId();
    }
}
