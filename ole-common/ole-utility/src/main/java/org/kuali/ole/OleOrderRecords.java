package org.kuali.ole;


import org.kuali.ole.pojo.OleOrderRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: maheswarang
 * Date: 4/10/12
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleOrderRecords {
    private List<OleOrderRecord> records = new ArrayList<OleOrderRecord>();

    public List<OleOrderRecord> getRecords() {
        return records;
    }

    public void setRecords(List<OleOrderRecord> records) {
        this.records = records;
    }
}
