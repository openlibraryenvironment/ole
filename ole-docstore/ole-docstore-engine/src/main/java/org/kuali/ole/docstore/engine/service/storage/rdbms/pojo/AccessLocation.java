package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/27/13
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessLocation extends PersistableBusinessObjectBase implements Serializable {

    private String accessLocationId;
    private String code;
    private String value;

    public String getAccessLocationId() {
        return accessLocationId;
    }

    public void setAccessLocationId(String accessLocationId) {
        this.accessLocationId = accessLocationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
