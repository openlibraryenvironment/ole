package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/19/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEContentTypes extends PersistableBusinessObjectBase {

    private String oleContentTypesId;
    private String oleERSIdentifier;
    private String oleContentTypeId;
    private OLEContentType oleContentType;

    public String getOleContentTypesId() {
        return oleContentTypesId;
    }

    public void setOleContentTypesId(String oleContentTypesId) {
        this.oleContentTypesId = oleContentTypesId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getOleContentTypeId() {
        return oleContentTypeId;
    }

    public void setOleContentTypeId(String oleContentTypeId) {
        this.oleContentTypeId = oleContentTypeId;
    }

    public OLEContentType getOleContentType() {
        return oleContentType;
    }

    public void setOleContentType(OLEContentType oleContentType) {
        this.oleContentType = oleContentType;
    }
}
