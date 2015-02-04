package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OleAction is a business object class for Ole Action
 */
public class OleAction {
    private String name;
    private String description;
    private Integer sequenceNumber;
    private List<OleRoute> routes;
    private List<OleAction> oleActions;
    /**
     * Gets the name attribute.
     * @return  Returns the name.
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
     * Gets the routes attribute.
     * @return  Returns the routes.
     */
    public List<OleRoute> getRoutes() {
        return routes;
    }
    /**
     * Sets the routes attribute value.
     * @param routes  The routes to set.
     */
    public void setRoutes(List<OleRoute> routes) {
        this.routes = routes;
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
     * Gets the oleActions attribute.
     * @return  Returns the oleActions.
     */
    public List<OleAction> getOleActions() {
        return oleActions;
    }
    /**
     * Sets the oleActions attribute value.
     * @param oleActions  The oleActions to set.
     */
    public void setOleActions(List<OleAction> oleActions) {
        this.oleActions = oleActions;
    }
}
