package org.kuali.ole.docstore.common.document.content.bib.marc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibMarcRecords {
    private List<BibMarcRecord> records = new ArrayList<BibMarcRecord>();

    public List<BibMarcRecord> getRecords() {
        return records;
    }

    public void setRecords(List<BibMarcRecord> records) {
        this.records = records;
    }
}
