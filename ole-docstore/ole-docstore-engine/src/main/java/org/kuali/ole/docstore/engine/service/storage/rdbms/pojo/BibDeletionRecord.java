package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.apache.commons.net.ntp.TimeStamp;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by rajeshgp on 23/1/17.
 */
public class BibDeletionRecord extends PersistableBusinessObjectBase
                    implements Serializable {


    private String bibId;
    private Timestamp dateUpdated;

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
