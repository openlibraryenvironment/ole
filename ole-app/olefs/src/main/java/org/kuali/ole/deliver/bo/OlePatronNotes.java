package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OlePatronNotesContract;
import org.kuali.ole.deliver.api.OlePatronNotesDefinition;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OlePatronNotes provides OlePatronNotes information through getter and setter.
 */
public class OlePatronNotes extends PersistableBusinessObjectBase implements OlePatronNotesContract {

    private String patronNoteId;
    private String olePatronId;
    private String patronNoteTypeId;
    private String patronNoteText;
    private boolean active;
    private OlePatronNoteType olePatronNoteType;
    private OlePatronDocument olePatron;
    private boolean selected;

    /**
     * Gets the value of patronNoteId property
     *
     * @return patronNoteId
     */
    public String getPatronNoteId() {
        return patronNoteId;
    }

    /**
     * Sets the value for patronNoteId property
     *
     * @param patronNoteId
     */
    public void setPatronNoteId(String patronNoteId) {
        this.patronNoteId = patronNoteId;
    }

    /**
     * Gets the value of olePatronId property
     *
     * @return olePatronId
     */
    public String getOlePatronId() {
        return olePatronId;
    }

    /**
     * Sets the value for olePatronId property
     *
     * @param olePatronId
     */
    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    /**
     * Gets the value of patronNoteTypeId property
     *
     * @return patronNoteTypeId
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
     * Gets the value of patronNoteText property
     *
     * @return patronNoteText
     */
    public String getPatronNoteText() {
        return patronNoteText;
    }

    /**
     * Sets the value for patronNoteText property
     *
     * @param patronNoteText
     */
    public void setPatronNoteText(String patronNoteText) {
        this.patronNoteText = patronNoteText;
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

    /**
     * Gets the value of olePatronNoteType which is of type OlePatronNoteType
     *
     * @return olePatronNoteType(OlePatronNoteType)
     */
    public OlePatronNoteType getOlePatronNoteType() {
        return olePatronNoteType;
    }

    /**
     * Sets the value for olePatronNoteType which is of type OlePatronNoteType
     *
     * @param olePatronNoteType(OlePatronNoteType)
     *
     */
    public void setOlePatronNoteType(OlePatronNoteType olePatronNoteType) {
        this.olePatronNoteType = olePatronNoteType;
    }

    /**
     * Gets the value of olePatron which is of type OlePatronDocument
     *
     * @return olePatron(OlePatronDocument)
     */
    public OlePatronDocument getOlePatron() {
        return olePatron;
    }

    /**
     * Sets the value for olePatron which is of type OlePatronDocument
     *
     * @param olePatron(OlePatronDocument)
     */
    public void setOlePatron(OlePatronDocument olePatron) {
        this.olePatron = olePatron;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("patronNoteId", patronNoteId);
        return toStringMap;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OlePatronNotes into immutable object OlePatronNotesDefinition
     *
     * @param bo
     * @return OlePatronNotesDefinition
     */
    public static OlePatronNotesDefinition to(OlePatronNotes bo) {
        if (bo == null) {
            return null;
        }
        return OlePatronNotesDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OlePatronNotesDefinition into PersistableBusinessObjectBase OlePatronNotes
     *
     * @param im
     * @return bo
     */
    public static OlePatronNotes from(OlePatronNotesDefinition im) {
        if (im == null) {
            return null;
        }

        OlePatronNotes bo = new OlePatronNotes();
        bo.patronNoteId = im.getPatronNoteId();
        bo.olePatronId = im.getOlePatronId();
        //bo.olePatron = OlePatronDocument.from(im.getOlePatron());
        bo.olePatronNoteType = OlePatronNoteType.from(im.getOlePatronNoteType());
        bo.patronNoteTypeId = im.getOlePatronNoteType().getPatronNoteTypeId();
        bo.patronNoteText = im.getPatronNoteText();
        bo.versionNumber = im.getVersionNumber();
        return bo;
    }

    /**
     * Gets the value of patronNoteId property
     *
     * @return patronNoteId
     */
    @Override
    public String getId() {
        return this.getPatronNoteId();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
