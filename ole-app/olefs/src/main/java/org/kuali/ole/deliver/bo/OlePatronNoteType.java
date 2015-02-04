package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OlePatronNoteTypeContract;
import org.kuali.ole.deliver.api.OlePatronNoteTypeDefinition;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OlePatronNoteType provides OlePatronNoteType information through getter and setter.
 */
public class OlePatronNoteType extends PersistableBusinessObjectBase implements OlePatronNoteTypeContract {

    private String patronNoteTypeId;
    private String patronNoteTypeCode;
    private String patronNoteTypeName;
    private boolean active;

    /**
     * Gets the value of patronNoteId property
     *
     * @return patronNoteId
     */
    public String getPatronNoteTypeId() {
        return patronNoteTypeId;
    }

    /**
     * Sets the value for patronNoteTypeId property
     *
     * @param patronNoteTypeId
     */
    public void setPatronNoteTypeId(String patronNoteTypeId) {
        this.patronNoteTypeId = patronNoteTypeId;
    }

    /**
     * Gets the value of patronNoteTypeCode property
     *
     * @return patronNoteTypeCode
     */
    public String getPatronNoteTypeCode() {
        return patronNoteTypeCode;
    }

    /**
     * Sets the value for patronNoteTypeCode property
     *
     * @param patronNoteTypeCode
     */
    public void setPatronNoteTypeCode(String patronNoteTypeCode) {
        this.patronNoteTypeCode = patronNoteTypeCode;
    }

    /**
     * Gets the value of patronNoteTypeName property
     *
     * @return patronNoteTypeName
     */
    public String getPatronNoteTypeName() {
        return patronNoteTypeName;
    }

    /**
     * Sets the value for patronNoteTypeName property
     *
     * @param patronNoteTypeName
     */
    public void setPatronNoteTypeName(String patronNoteTypeName) {
        this.patronNoteTypeName = patronNoteTypeName;
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
        toStringMap.put("patronNoteTypeId", patronNoteTypeId);
        return toStringMap;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OlePatronNoteType into immutable object OlePatronNoteTypeDefinition
     *
     * @param bo
     * @return OlePatronNoteTypeDefinition
     */
    static OlePatronNoteTypeDefinition to(OlePatronNoteType bo) {
        if (bo == null) {
            return null;
        }
        return OlePatronNoteTypeDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OlePatronNoteTypeDefinition into PersistableBusinessObjectBase OlePatronNoteType
     *
     * @param im
     * @return bo(OlePatronNoteType)
     */
    static OlePatronNoteType from(OlePatronNoteTypeDefinition im) {
        if (im == null) {
            return null;
        }

        OlePatronNoteType bo = new OlePatronNoteType();
        bo.patronNoteTypeId = im.getPatronNoteTypeId();
        bo.patronNoteTypeCode = im.getPatronNoteTypeCode();
        bo.patronNoteTypeName = im.getPatronNoteTypeName();
        return bo;
    }

    /**
     * Gets the value of patronNoteId property
     *
     * @return patronNoteId
     */
    @Override
    public String getId() {
        return this.getPatronNoteTypeId();
    }
}
