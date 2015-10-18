package org.kuali.ole.deliver.service;

import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;

/**
 * Created by pvsubrah on 4/8/15.
 */
public class ReplacementBillNoticeEmailContentFormattter extends NoticeMailContentFormatter {
    @Override
    protected void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo) {
        oleNoticeBo.setNoticeTitle("Lost");
        oleNoticeBo.setBillNumber(oleLoanDocument.getRepaymentFeePatronBillId());
        oleNoticeBo.setFeeType("Replacement Bill");
        oleNoticeBo.setFeeAmount(oleLoanDocument.getReplacementBill());
    }

}
