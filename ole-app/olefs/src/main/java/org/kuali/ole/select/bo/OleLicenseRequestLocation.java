package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleLicenseRequestLocation is the business object class for License Request Document Location Maintenance Document.
 */
public class OleLicenseRequestLocation extends PersistableBusinessObjectBase {

    private String id;
    private String name;
    private String description;
    private boolean active;

    /**
     * Gets the id attribute.
     *
     * @return Returns the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id attribute value.
     *
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name attribute.
     *
     * @return Returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name attribute value.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description attribute.
     *
     * @return Returns the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description attribute value.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the active attribute.
     *
     * @return Returns the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active attribute value.
     *
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the toStringMap attribute.
     *
     * @return Returns the toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("id", id);
        return toStringMap;
    }
}
