package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/20/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibHoldingsRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String bibHoldingsId;
    private String bibId;
    private String holdingsId;

    private List<HoldingsRecord> holdingsRecords;
    private List<BibRecord> bibRecords;
    private Timestamp updatedDate;


    public String getBibHoldingsId() {
        return bibHoldingsId;
    }

    public void setBibHoldingsId(String bibHoldingsId) {
        this.bibHoldingsId = bibHoldingsId;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public List<HoldingsRecord> getHoldingsRecords() {
        return holdingsRecords;
    }

    public void setHoldingsRecords(List<HoldingsRecord> holdingsRecords) {
        this.holdingsRecords = holdingsRecords;
    }

    public List<BibRecord> getBibRecords() {
        return bibRecords;
    }

    public void setBibRecords(List<BibRecord> bibRecords) {
        this.bibRecords = bibRecords;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
