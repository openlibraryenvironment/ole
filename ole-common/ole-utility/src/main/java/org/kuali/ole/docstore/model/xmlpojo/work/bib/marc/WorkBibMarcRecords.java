package org.kuali.ole.docstore.model.xmlpojo.work.bib.marc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 12/12/11
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibMarcRecords {
    private List<WorkBibMarcRecord> records = new ArrayList<WorkBibMarcRecord>();

    public List<WorkBibMarcRecord> getRecords() {
        return records;
    }

    public void setRecords(List<WorkBibMarcRecord> records) {
        this.records = records;
    }
}
