package org.kuali.ole.deliver.service;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;

import java.math.BigDecimal;

/**
 * Created by rajeshgp on 5/10/17.
 */
public class FineNoticeEmailContentFormatter extends NoticeMailContentFormatter {

    @Override
    protected void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo) {
        oleNoticeBo.setBillNumber(oleLoanDocument.getFineBillNumber());
        oleNoticeBo.setFeeType(oleLoanDocument.getFeeTypeName());
        if(oleLoanDocument.getFineAmount() != null) {
            BigDecimal feeamount = new BigDecimal(oleLoanDocument.getFineAmount());
            feeamount = feeamount.setScale(2,BigDecimal.ROUND_HALF_UP);
            oleNoticeBo.setFeeAmount("$" + feeamount.toString());
        }
        oleNoticeBo.setFineItemDueDate(oleLoanDocument.getFineItemDue().toString());
    }
}
