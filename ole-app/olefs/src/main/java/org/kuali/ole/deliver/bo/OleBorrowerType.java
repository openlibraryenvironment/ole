package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OleBorrowerTypeContract;
import org.kuali.ole.deliver.api.OleBorrowerTypeDefinition;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleBorrowerType provides OleBorrowerType information through getter and setter.
 */
public class OleBorrowerType extends PersistableBusinessObjectBase implements OleBorrowerTypeContract {
    private String borrowerTypeId;
    private String borrowerTypeCode;
    private String borrowerTypeDescription;
    private String borrowerTypeName;
    private boolean active;

    /**
     * Gets the value of borrowerTypeId property
     *
     * @return borrowerTypeId
     */
    public String getBorrowerTypeId() {
        return borrowerTypeId;
    }

    /**
     * Sets the value for borrowerTypeId property
     *
     * @param borrowerTypeId
     */
    public void setBorrowerTypeId(String borrowerTypeId) {
        this.borrowerTypeId = borrowerTypeId;
    }

    /**
     * Gets the value of borrowerTypeCode property
     *
     * @return borrowerTypeCode
     */
    public String getBorrowerTypeCode() {
        return borrowerTypeCode;
    }

    /**
     * Sets the value for borrowerTypeCode property
     *
     * @param borrowerTypeCode
     */
    public void setBorrowerTypeCode(String borrowerTypeCode) {
        this.borrowerTypeCode = borrowerTypeCode;
    }

    /**
     * Gets the value of borrowerTypeDescription property
     *
     * @return borrowerTypeDescription
     */
    public String getBorrowerTypeDescription() {
        return borrowerTypeDescription;
    }

    /**
     * Sets the value for borrowerTypeDescription property
     *
     * @param borrowerTypeDescription
     */
    public void setBorrowerTypeDescription(String borrowerTypeDescription) {
        this.borrowerTypeDescription = borrowerTypeDescription;
    }

    /**
     * Gets the value of borrowerTypeName property
     *
     * @return borrowerTypeName
     */
    public String getBorrowerTypeName() {
        return borrowerTypeName;
    }

    /**
     * Sets the value for borrowerTypeName property
     *
     * @param borrowerTypeName
     */
    public void setBorrowerTypeName(String borrowerTypeName) {
        this.borrowerTypeName = borrowerTypeName;
    }

    /**
     * Gets the boolean value of active property
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the boolean value for active property
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("borrowerTypeId", borrowerTypeId);
        return toStringMap;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OleBorrowerType into immutable object OleBorrowerTypeDefinition
     *
     * @param bo(OleBorrowerType)
     * @return OleBorrowerTypeDefinition
     */
    static OleBorrowerTypeDefinition to(OleBorrowerType bo) {
        if (bo == null) {
            return null;
        }
        return OleBorrowerTypeDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OleBorrowerTypeDefinition into PersistableBusinessObjectBase OleBorrowerType
     *
     * @param im(OleBorrowerTypeDefinition)
     * @return bo(OleBorrowerType)
     */
    static OleBorrowerType from(OleBorrowerTypeDefinition im) {
        if (im == null) {
            return null;
        }

        OleBorrowerType bo = new OleBorrowerType();
        bo.borrowerTypeId = im.getBorrowerTypeId();
        bo.borrowerTypeCode = im.getBorrowerTypeCode();
        bo.borrowerTypeName = im.getBorrowerTypeName();
        bo.borrowerTypeDescription = im.getBorrowerTypeDescription();
        bo.versionNumber = im.getVersionNumber();
        return bo;
    }

    /**
     * Gets the value of borrowerTypeId property
     *
     * @return borrowerTypeId
     */
    @Override
    public String getId() {
        return this.getBorrowerTypeId();
    }
}
