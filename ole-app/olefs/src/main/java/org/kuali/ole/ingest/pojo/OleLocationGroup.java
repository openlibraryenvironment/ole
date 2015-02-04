package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OleLocationGroup is a business object class for Ole Location Group
 */
public class OleLocationGroup {
    private List<OleLocationIngest> locationGroup;
    /**
     * Gets the locationGroup attribute.
     * @return  Returns the locationGroup.
     */
    public List<OleLocationIngest> getLocationGroup() {
        return locationGroup;
    }
    /**
     * Sets the locationGroup attribute value.
     * @param locationGroup  The locationGroup to set.
     */
    public void setLocationGroup(List<OleLocationIngest> locationGroup) {
        this.locationGroup = locationGroup;
    }
}
