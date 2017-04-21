package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.apache.commons.net.ntp.TimeStamp;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by rajeshgp on 23/1/17.
 */
public class BibDeletionRecord extends PersistableBusinessObjectBase
                    implements Serializable {

    private String id;
    private String bibId;
    private String holdingId;
    private String itemId;
    private String bibIdIndicator;
    private String holdingIdIndicator;
    private String itemIdIndicator;
    private String content;
    private Timestamp dateUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(String holdingId) {
        this.holdingId = holdingId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBibIdIndicator() {
        return bibIdIndicator;
    }

    public void setBibIdIndicator(String bibIdIndicator) {
        this.bibIdIndicator = bibIdIndicator;
    }

    public String getHoldingIdIndicator() {
        return holdingIdIndicator;
    }

    public void setHoldingIdIndicator(String holdingIdIndicator) {
        this.holdingIdIndicator = holdingIdIndicator;
    }

    public String getItemIdIndicator() {
        return itemIdIndicator;
    }

    public void setItemIdIndicator(String itemIdIndicator) {
        this.itemIdIndicator = itemIdIndicator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public String toString() {
        if(bibIdIndicator==null){
            return bibId ;
        }
        else if(holdingIdIndicator==null){
            return bibId+ ","+bibIdIndicator ;
        }
        else if(itemIdIndicator==null){
            return bibId+ ","+bibIdIndicator+"," + holdingId + "," + holdingIdIndicator;
        } else {
            return bibId+ ","+bibIdIndicator+"," + holdingId + "," + holdingIdIndicator + "," + itemId + "," +itemIdIndicator ;
        }
    }
}
