package org.kuali.ole.ingest.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * ProfileAttributeBo is a business object class for Profile Attribute Bo
 */
public class ProfileAttributeBo extends PersistableBusinessObjectBase {
    private Integer id;
    private String agendaName;
    private String attributeName;
    private String attributeValue;
    private String isSystemValue;
    /**
     * Gets the systemValue attribute.
     * @return  Returns the systemValue.
     */
    public String getSystemValue() {
        return isSystemValue;
    }
    /**
     * Sets the systemValue attribute value.
     * @param systemValue The systemValue to set.
     */
    public void setSystemValue(String systemValue) {
        isSystemValue = systemValue;
    }
    /**
     * Gets the id attribute.
     * @return  Returns the id.
     */
    public Integer getId() {
        return id;
    }
    /**
     * Sets the id attribute value.
     * @param id The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * Gets the agendaName attribute.
     * @return  Returns the agendaName.
     */
    public String getAgendaName() {
        return agendaName;
    }
    /**
     * Sets the agendaName attribute value.
     * @param agendaName The agendaName to set.
     */
    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }
    /**
     * Gets the attributeName attribute.
     * @return  Returns the attributeName.
     */
    public String getAttributeName() {
        return attributeName;
    }
    /**
     * Sets the attributeName attribute value.
     * @param attributeName The attributeName to set.
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    /**
     * Gets the attributeValue attribute.
     * @return  Returns the attributeValue.
     */
    public String getAttributeValue() {
        return attributeValue;
    }
    /**
     * Sets the attributeValue attribute value.
     * @param attributeValue The attributeValue to set.
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     * This method returns concatenated value of id,agendaName,attributeName,attributeValue
     * @return toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("id", id);
        toStringMap.put("agendaName", agendaName);
        toStringMap.put("attributeName", attributeName);
        toStringMap.put("attributeValue", attributeValue);
        return toStringMap;
    }
}
