package org.kuali.ole.bo.serachRetrieve;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/2/13
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUData {

    private String bibId;
    private List<String> instanceIds;

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public List<String> getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(List<String> instanceIds) {
        this.instanceIds = instanceIds;
    }
}
