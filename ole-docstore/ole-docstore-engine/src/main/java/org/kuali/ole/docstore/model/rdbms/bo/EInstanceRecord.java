package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 7/19/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class EInstanceRecord extends PersistableBusinessObjectBase implements Serializable {

    private String eInstanceIdentifier;
    private String uniqueIdPrefix;
    private String bibId;
    private EHoldingsRecord eHoldingsRecord;

    public String geteInstanceIdentifier() {
        return eInstanceIdentifier;
    }

    public void seteInstanceIdentifier(String eInstanceIdentifier) {
        this.eInstanceIdentifier = eInstanceIdentifier;
    }

    public String getUniqueIdPrefix() {
        return uniqueIdPrefix;
    }

    public void setUniqueIdPrefix(String uniqueIdPrefix) {
        this.uniqueIdPrefix = uniqueIdPrefix;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public EHoldingsRecord geteHoldingsRecord() {
        return eHoldingsRecord;
    }

    public void seteHoldingsRecord(EHoldingsRecord eHoldingsRecord) {
        this.eHoldingsRecord = eHoldingsRecord;
    }
}
