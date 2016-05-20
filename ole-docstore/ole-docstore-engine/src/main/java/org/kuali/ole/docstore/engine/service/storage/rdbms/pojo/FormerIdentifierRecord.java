package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/15/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class FormerIdentifierRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String formerId;
    private String value;
    private String type;
    private String itemId;
    private Timestamp updatedDate;

    public String getFormerId() {
        return formerId;
    }

    public void setFormerId(String formerId) {
        this.formerId = formerId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
