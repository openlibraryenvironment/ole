package org.kuali.ole.deliver.notice.noticeFormatters;

import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 6/29/15.
 */
public class RecallRequestEmailContentFormatter extends RequestEmailContentFormatter {

    @Override
    protected void processCustomNoticeInfo(OleDeliverRequestBo oleDeliverRequestBo, OleNoticeBo oleNoticeBo) {

    }

    @Override
    protected OlePatronDocument getOlePatron(OleDeliverRequestBo oleDeliverRequestBo) {
        Map<String, String> loanMap = new HashMap<>();
        loanMap.put("itemId", oleDeliverRequestBo.getItemId());
        List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
        if (oleLoanDocumentList.size() > 0) {
            OleLoanDocument oleLoanDocument = oleLoanDocumentList.get(0);
            if (oleLoanDocument != null) {
                return oleLoanDocument.getOlePatron();
            }
        }
        return null;
    }
}
