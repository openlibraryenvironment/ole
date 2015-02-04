package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.model.bo.OleDocument;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pj7789
 * Date: 26/11/12
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibDocumentSearchResult
        extends Bib {

    private boolean selectedMarc;
    private boolean selectedExternalMarc;
    private List<String> bibUuidsList;
    private String displayField;
    private String uuid;
    private HoldingsTree holdingsTree;


    public boolean isSelectedMarc() {
        return selectedMarc;
    }

    public boolean isSelectedExternalMarc() {
        return selectedExternalMarc;
    }

    public void setSelectedExternalMarc(boolean selectedExternalMarc) {
        this.selectedExternalMarc = selectedExternalMarc;
    }

    public void setSelectedMarc(boolean selectedMarc) {
        this.selectedMarc = selectedMarc;
    }

    public List<String> getBibUuidsList() {
        return bibUuidsList;
    }

    public void setBibUuidsList(List<String> bibUuidsList) {
        this.bibUuidsList = bibUuidsList;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public HoldingsTree getHoldingsTree() {
        return holdingsTree;
    }

    public void setHoldingsTree(HoldingsTree holdingsTree) {
        this.holdingsTree = holdingsTree;
    }
}
