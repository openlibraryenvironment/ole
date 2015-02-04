package org.kuali.ole.bo.serachRetrieve;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 5/27/13
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUResponseRecords {
    private List<OleSRUResponseRecord> oleSRUResponseRecordList;

    public List<OleSRUResponseRecord> getOleSRUResponseRecordList() {
        return oleSRUResponseRecordList;
    }

    public void setOleSRUResponseRecordList(List<OleSRUResponseRecord> oleSRUResponseRecordList) {
        this.oleSRUResponseRecordList = oleSRUResponseRecordList;
    }

    @Override
    public String toString() {
        return "OleSRUResponseRecords{" +
                "oleSRUResponseRecordList=" + oleSRUResponseRecordList +
                '}';
    }
}
