package org.kuali.ole.deliver.service;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;

import java.math.BigDecimal;

/**
 * Created by pvsubrah on 4/8/15.
 */
public class ReplacementBillNoticeEmailContentFormattter extends NoticeMailContentFormatter {
    @Override
    protected void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo) {
        oleNoticeBo.setNoticeTitle("Lost");
        oleNoticeBo.setBillNumber(oleLoanDocument.getRepaymentFeePatronBillId());
        oleNoticeBo.setFeeType(OLEConstants.REPLACEMENT_FEE);
       if(oleLoanDocument.getReplacementBill() != null) {
           BigDecimal feeamount = oleLoanDocument.getReplacementBill();
           feeamount = feeamount.setScale(2, BigDecimal.ROUND_HALF_UP);
           oleNoticeBo.setFeeAmount("$" + feeamount.toString());
       }
    }

}
