package org.kuali.ole.select.bo;

import org.kuali.ole.select.businessobject.OleFormatType;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/19/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEFormatTypeList extends PersistableBusinessObjectBase {

    private String oleFormatTypesId;
    private String oleERSIdentifier;
    private String formatTypeId;
    private OleFormatType formatTypeName;

    public String getOleFormatTypesId() {
        return oleFormatTypesId;
    }

    public void setOleFormatTypesId(String oleFormatTypesId) {
        this.oleFormatTypesId = oleFormatTypesId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getFormatTypeId() {
        return formatTypeId;
    }

    public void setFormatTypeId(String formatTypeId) {
        this.formatTypeId = formatTypeId;
    }

    public OleFormatType getFormatTypeName() {
        return formatTypeName;
    }

    public void setFormatTypeName(OleFormatType formatTypeName) {
        this.formatTypeName = formatTypeName;
    }
}
