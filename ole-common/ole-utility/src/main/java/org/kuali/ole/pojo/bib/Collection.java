package org.kuali.ole.pojo.bib;

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
    private List<BibliographicRecord> collection = new ArrayList<BibliographicRecord>();

    public List<BibliographicRecord> getRecords() {
        return collection;
    }

    public void setRecords(List<BibliographicRecord> records) {
        this.collection = records;
    }
}
