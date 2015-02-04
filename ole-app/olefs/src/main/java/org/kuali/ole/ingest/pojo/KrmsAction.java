package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * KrmsAction is a business object class for Krms Action
 */
public class KrmsAction {
    private String name;
    private String description;
    private Integer sequenceNumber;
    private String serviceName;
    private List<KrmsAction> krmsActions;

    /**
     * Gets the oleAgendaList attribute.
     * @return  Returns the oleAgendaList.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name attribute value.
     * @param name  The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the description attribute.
     * @return  Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description attribute value.
     * @param description  The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Gets the sequenceNumber attribute.
     * @return  Returns the sequenceNumber.
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     * Sets the sequenceNumber attribute value.
     * @param sequenceNumber  The sequenceNumber to set.
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    /**
     * Gets the serviceName attribute.
     * @return  Returns the serviceName.
     */
    public String getServiceName() {
        return serviceName;
    }
    /**
     * Sets the serviceName attribute value.
     * @param serviceName  The serviceName to set.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    /**
     * Gets the krmsActions attribute.
     * @return  Returns the krmsActions.
     */
    public List<KrmsAction> getKrmsActions() {
        return krmsActions;
    }
    /**
     * Sets the krmsActions attribute value.
     * @param krmsActions  The krmsActions to set.
     */
    public void setKrmsActions(List<KrmsAction> krmsActions) {
        this.krmsActions = krmsActions;
    }
}
