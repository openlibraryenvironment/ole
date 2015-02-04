package org.kuali.ole.docstore.common.document.content.bib.marc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/19/12
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class Collection {
    private List<BibMarcRecord> collection = new ArrayList<BibMarcRecord>();

    public List<BibMarcRecord> getRecords() {
        return collection;
    }

    public void setRecords(List<BibMarcRecord> records) {
        this.collection = records;
    }
}
