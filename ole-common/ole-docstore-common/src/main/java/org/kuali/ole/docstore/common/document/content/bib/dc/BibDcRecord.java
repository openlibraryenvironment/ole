package org.kuali.ole.docstore.common.document.content.bib.dc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibDcRecord {

    private String schema = null;
    private List<DCValue> dcValues = new ArrayList<DCValue>();

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public List<DCValue> getDcValues() {
        return dcValues;
    }

    public void setDcValues(List<DCValue> dcValues) {
        this.dcValues = dcValues;
    }

    public void addDublinDCValue(DCValue dcValue) {
        this.dcValues.add(dcValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("schema: ");
        sb.append(schema);
        sb.append(";\n");
        for (DCValue dcValue : dcValues) {
            sb.append(dcValue);
            sb.append(";\n");
        }
        return sb.toString();
    }

}
