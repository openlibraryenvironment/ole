package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleAction is business object class for Action Maintenance Document
 */
public class OleAction extends PersistableBusinessObjectBase {

    private Integer actionId;
    private String actionCode;
    private String actionName;
    private String actionDescription;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the actionId attribute.
     *
     * @return Returns the actionId
     */
    public Integer getActionId() {
        return actionId;
    }

    /**
     * Sets the actionId attribute value.
     *
     * @param actionId The actionId to set.
     */
    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    /**
     * Gets the actionCode attribute.
     *
     * @return Returns the actionCode
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * Sets the actionCode attribute value.
     *
     * @param actionCode The actionCode to set.
     */
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    /**
     * Gets the actionName attribute.
     *
     * @return Returns the actionName
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Sets the actionName attribute value.
     *
     * @param actionName The actionName to set.
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * Gets the actionDescription attribute.
     *
     * @return Returns the actionDescription
     */
    public String getActionDescription() {
        return actionDescription;
    }

    /**
     * Sets the actionDescription attribute value.
     *
     * @param actionDescription The actionDescription to set.
     */
    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    /**
     * Gets the source attribute.
     *
     * @return Returns the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source attribute value.
     *
     * @param source The source to set.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the sourceDate attribute.
     *
     * @return Returns the sourceDate
     */
    public Date getSourceDate() {
        return sourceDate;
    }

    /**
     * Sets the sourceDate attribute value.
     *
     * @param sourceDate The sourceDate to set.
     */
    public void setSourceDate(Date sourceDate) {
        this.sourceDate = sourceDate;
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
        toStringMap.put("actionId", actionId);
        return toStringMap;
    }
}
