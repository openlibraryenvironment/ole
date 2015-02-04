package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/19/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEContentType extends PersistableBusinessObjectBase {
    private String oleContentTypeId;
    private String oleContentTypeName;
    private String oleContentTypeDescription;
    private boolean active;

    public String getOleContentTypeId() {
        return oleContentTypeId;
    }

    public void setOleContentTypeId(String oleContentTypeId) {
        this.oleContentTypeId = oleContentTypeId;
    }

    public String getOleContentTypeName() {
        return oleContentTypeName;
    }

    public void setOleContentTypeName(String oleContentTypeName) {
        this.oleContentTypeName = oleContentTypeName;
    }

    public String getOleContentTypeDescription() {
        return oleContentTypeDescription;
    }

    public void setOleContentTypeDescription(String oleContentTypeDescription) {
        this.oleContentTypeDescription = oleContentTypeDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
