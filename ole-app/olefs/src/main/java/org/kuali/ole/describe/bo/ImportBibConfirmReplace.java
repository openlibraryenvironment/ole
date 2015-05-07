package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 14/12/12
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportBibConfirmReplace {

    private Bib overLayMarcRecord;
    private String importTypeOverLay;
    private String importTypeOclcNum;
    private String matchPoint;
    private HoldingsTree holdingsTree;

    public HoldingsTree getHoldingsTree() {
        return holdingsTree;
    }

    public void setHoldingsTree(HoldingsTree holdingsTree) {
        this.holdingsTree = holdingsTree;
    }

    public Bib getOverLayMarcRecord() {
        return overLayMarcRecord;
    }

    public void setOverLayMarcRecord(Bib overLayMarcRecord) {
        this.overLayMarcRecord = overLayMarcRecord;
    }

    public String getImportTypeOverLay() {
        return importTypeOverLay;
    }

    public void setImportTypeOverLay(String importTypeOverLay) {
        this.importTypeOverLay = importTypeOverLay;
    }

    public String getImportTypeOclcNum() {
        return importTypeOclcNum;
    }

    public void setImportTypeOclcNum(String importTypeOclcNum) {
        this.importTypeOclcNum = importTypeOclcNum;
    }

    public String getMatchPoint() {
        return matchPoint;
    }

    public void setMatchPoint(String matchPoint) {
        this.matchPoint = matchPoint;
    }
}
