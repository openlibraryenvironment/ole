package org.kuali.ole.ingest.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * MatchBo is a business object class for Match Bo
 */
public class MatchBo  extends PersistableBusinessObjectBase {
    private Integer id;
    private String agendaName;
    private String termName;
    private String docType;
    private String incomingField;
    private String existingField;
    /**
     * Gets the id attribute.
     * @return  Returns the id.
     */
    public Integer getId() {
        return id;
    }
    /**
     * Sets the id attribute value.
     * @param id  The id to set.
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
     * @param agendaName  The agendaName to set.
     */
    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }
    /**
     * Gets the termName attribute.
     * @return  Returns the termName.
     */
    public String getTermName() {
        return termName;
    }
    /**
     * Sets the termName attribute value.
     * @param termName  The termName to set.
     */
    public void setTermName(String termName) {
        this.termName = termName;
    }
    /**
     * Gets the docType attribute.
     * @return  Returns the docType.
     */
    public String getDocType() {
        return docType;
    }
    /**
     * Sets the docType attribute value.
     * @param docType  The docType to set.
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }
    /**
     * Gets the incomingField attribute.
     * @return  Returns the incomingField.
     */
    public String getIncomingField() {
        return incomingField;
    }
    /**
     * Sets the incomingField attribute value.
     * @param incomingField  The incomingField to set.
     */
    public void setIncomingField(String incomingField) {
        this.incomingField = incomingField;
    }
    /**
     * Gets the existingField attribute.
     * @return  Returns the existingField.
     */
    public String getExistingField() {
        return existingField;
    }
    /**
     * Sets the existingField attribute value.
     * @param existingField  The existingField to set.
     */
    public void setExistingField(String existingField) {
        this.existingField = existingField;
    }

    /**
     *
     * @return     LinkedHashMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("id", id);
        toStringMap.put("agendaName", agendaName);
        toStringMap.put("termName", termName);
        toStringMap.put("docType", docType);
        toStringMap.put("incomingField", incomingField);
        toStringMap.put("existingField", existingField);
        return toStringMap;
    }
}
