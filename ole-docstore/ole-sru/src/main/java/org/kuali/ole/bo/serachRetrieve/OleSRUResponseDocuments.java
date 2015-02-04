package org.kuali.ole.bo.serachRetrieve;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/17/12
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUResponseDocuments {

    public List<OleSRUResponseDocument> opacRecords;

    public List<OleSRUResponseDocument> getOpacRecords() {
        return opacRecords;
    }

    public void setOpacRecords(List<OleSRUResponseDocument> opacRecords) {
        this.opacRecords = opacRecords;
    }

    @Override
    public String toString() {
        return "opacRecords=" + opacRecords;

    }
}
