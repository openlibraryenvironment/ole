package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleLicenseRequestStatus is the business object class for License Request Status Maintenance Document.
 */
public class OleLicenseRequestStatus extends PersistableBusinessObjectBase {

    private String code;
    private String name;
    private String description;
    private boolean active;

    /**
     * Gets the code attribute.
     *
     * @return Returns the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code attribute value.
     *
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
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
     * Gets the toStringMapper attribute.
     *
     * @return Returns the toStringMapper
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("code", code);
        toStringMap.put("name", name);
        toStringMap.put("description", description);
        toStringMap.put("active", active);
        return toStringMapper();
    }
}
