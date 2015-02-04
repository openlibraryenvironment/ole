package org.kuali.ole.bo.serachRetrieve;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/17/12
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUResponseDocument {

    public OleSRUResponseRecordData oleSRUResponseRecordData;
    public OleSRUResponseRecordData getOleSRUResponseRecordData() {
        return oleSRUResponseRecordData;
    }

    public void setOleSRUResponseRecordData(OleSRUResponseRecordData oleSRUResponseRecordData) {
        this.oleSRUResponseRecordData = oleSRUResponseRecordData;
    }

    @Override
    public String toString() {
        return "OleSRUResponseDocument{" +
                "oleSRUResponseRecordData=" + oleSRUResponseRecordData +
                '}';
    }
}
