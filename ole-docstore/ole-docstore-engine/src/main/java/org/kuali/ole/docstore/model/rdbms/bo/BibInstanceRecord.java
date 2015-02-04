package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/1/13
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibInstanceRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String bibInstanceId;
    private String bibId;
    private String instanceId;

    private List<InstanceRecord> instanceRecords;
    private List<BibRecord> bibRecords;

    public List<InstanceRecord> getInstanceRecords() {
        return instanceRecords;
    }

    public void setInstanceRecords(List<InstanceRecord> instanceRecords) {
        this.instanceRecords = instanceRecords;
    }

    public List<BibRecord> getBibRecords() {
        return bibRecords;
    }

    public void setBibRecords(List<BibRecord> bibRecords) {
        this.bibRecords = bibRecords;
    }

    public String getBibInstanceId() {
        return bibInstanceId;
    }

    public void setBibInstanceId(String bibInstanceId) {
        this.bibInstanceId = bibInstanceId;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
