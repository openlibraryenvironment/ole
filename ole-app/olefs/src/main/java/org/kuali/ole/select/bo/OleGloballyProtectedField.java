package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * This  OleGloballyProtectedField is the Business Object Class for  Globally Protected Field Maintenance Document
 */
public class OleGloballyProtectedField extends PersistableBusinessObjectBase {

    private String oleGloballyProtectedFieldId;
    private String tag;
    private String firstIndicator;
    private String secondIndicator;
    private String subField;
    private boolean activeIndicator;

    /**
     * Gets the oleGloballyProtectedFieldId attribute.
     *
     * @return Returns the oleGloballyProtectedFieldId
     */
    public String getOleGloballyProtectedFieldId() {
        return oleGloballyProtectedFieldId;
    }

    /**
     * Sets the oleGloballyProtectedFieldId attribute value.
     *
     * @param oleGloballyProtectedFieldId The oleGloballyProtectedFieldId to set.
     */
    public void setOleGloballyProtectedFieldId(String oleGloballyProtectedFieldId) {
        this.oleGloballyProtectedFieldId = oleGloballyProtectedFieldId;
    }

    /**
     * Gets the tag attribute.
     *
     * @return Returns the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the tag attribute value.
     *
     * @param tag The tag to set.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Gets the firstIndicator attribute.
     *
     * @return Returns the firstIndicator
     */
    public String getFirstIndicator() {
        return firstIndicator;
    }

    /**
     * Sets the firstIndicator attribute value.
     *
     * @param firstIndicator The firstIndicator to set.
     */
    public void setFirstIndicator(String firstIndicator) {
        this.firstIndicator = firstIndicator;
    }

    /**
     * Gets the secondIndicator attribute.
     *
     * @return Returns the secondIndicator
     */
    public String getSecondIndicator() {
        return secondIndicator;
    }

    /**
     * Sets the secondIndicator attribute value.
     *
     * @param secondIndicator The secondIndicator to set.
     */
    public void setSecondIndicator(String secondIndicator) {
        this.secondIndicator = secondIndicator;
    }

    /**
     * Gets the subField attribute.
     *
     * @return Returns the subField
     */
    public String getSubField() {
        return subField;
    }

    /**
     * Sets the subField attribute value.
     *
     * @param subField The subField to set.
     */
    public void setSubField(String subField) {
        this.subField = subField;
    }

    /**
     * Gets the activeIndicator attribute.
     *
     * @return Returns the activeIndicator
     */
    public boolean isActiveIndicator() {
        return activeIndicator;
    }

    /**
     * Sets the activeIndicator attribute value.
     *
     * @param activeIndicator The activeIndicator to set.
     */
    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    /**
     * Gets the toStringMap attribute.
     *
     * @return Returns the toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("oleGloballyProtectedFieldId", oleGloballyProtectedFieldId);
        toStringMap.put("tag", tag);
        toStringMap.put("firstIndicator", firstIndicator);
        toStringMap.put("secondIndicator", secondIndicator);
        toStringMap.put("subField", subField);
        toStringMap.put("activeIndicator", activeIndicator);
        return toStringMap;
    }
}
