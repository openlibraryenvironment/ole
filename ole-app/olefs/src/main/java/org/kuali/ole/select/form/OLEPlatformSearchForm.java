package org.kuali.ole.select.form;

import org.kuali.ole.select.bo.OLESearchCondition;
import org.kuali.ole.select.bo.OLESearchParams;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 9/9/14.
 * OLEPlatformSearchForm is the Form class for Platform Search.
 */
public class OLEPlatformSearchForm extends UifFormBase {

    private OLESearchParams oleSearchParams = new OLESearchParams();

    private List<OLEPlatformRecordDocument> platformDocumentList = new ArrayList<>();

    public OLESearchParams getOleSearchParams() {
        return oleSearchParams;
    }

    public void setOleSearchParams(OLESearchParams oleSearchParams) {
        this.oleSearchParams = oleSearchParams;
    }

    public List<OLEPlatformRecordDocument> getPlatformDocumentList() {
        return platformDocumentList;
    }

    public void setPlatformDocumentList(List<OLEPlatformRecordDocument> platformDocumentList) {
        this.platformDocumentList = platformDocumentList;
    }

    public OLEPlatformSearchForm() {
        List<OLESearchCondition> searchConditions = new ArrayList<OLESearchCondition>();
        searchConditions = getOleSearchParams().getSearchFieldsList();
        searchConditions.add(new OLESearchCondition());
        searchConditions.add(new OLESearchCondition());
    }
}
