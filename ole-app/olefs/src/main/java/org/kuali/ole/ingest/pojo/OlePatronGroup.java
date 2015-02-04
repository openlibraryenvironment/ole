package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OlePatronGroup is a business object class for Ole Patron Group Document
 */
public class OlePatronGroup {
    private List<OlePatron> patronGroup;
    /**
     * Gets the patronGroup attribute.
     * @return  Returns the patronGroup.
     */
    public List<OlePatron> getPatronGroup() {
        return patronGroup;
    }
    /**
     * Sets the patronGroup attribute value.
     * @param patronGroup The patronGroup to set.
     */
    public void setPatronGroup(List<OlePatron> patronGroup) {
        this.patronGroup = patronGroup;
    }
}
