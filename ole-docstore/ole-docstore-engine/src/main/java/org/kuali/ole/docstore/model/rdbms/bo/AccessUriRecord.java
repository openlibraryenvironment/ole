package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/8/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessUriRecord extends PersistableBusinessObjectBase
        implements Serializable {
    private String accessUriId;
    private String text;
    private String holdingsId;

    public String getAccessUriId() {
        return accessUriId;
    }

    public void setAccessUriId(String accessUriId) {
        this.accessUriId = accessUriId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }
}
