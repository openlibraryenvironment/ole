package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLELogType extends PersistableBusinessObjectBase {

    private String logTypeId;

    private String logTypeName;

    public String getLogTypeId() {
        return logTypeId;
    }

    public void setLogTypeId(String logTypeId) {
        this.logTypeId = logTypeId;
    }

    public String getLogTypeName() {
        return logTypeName;
    }

    public void setLogTypeName(String logTypeName) {
        this.logTypeName = logTypeName;
    }
}
