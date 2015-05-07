package org.kuali.ole.deliver.service;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.web.format.CurrencyFormatter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

/**
 * Created by pvsubrah on 4/8/15.
 */
public class ReplacementBillNoticeEmailContentFormattter extends NoticeMailContentFormatter {
    @Override
    protected String generateCustomHTML(OleLoanDocument oleLoanDocument) {
        StringBuffer customHTMLForReplacementBill=new StringBuffer();

        customHTMLForReplacementBill.append("<HTML>");
        customHTMLForReplacementBill.append("<TITLE><CENTER><h2>" + OLEConstants.REPLACEMENT_FEE + " Bill </h2></CENTER></TITLE>");
        customHTMLForReplacementBill.append("<HEAD><TR><TD><CENTER><h2>" + OLEConstants.REPLACEMENT_FEE + "<h2></CENTER></TD></TR></HEAD>");
        customHTMLForReplacementBill.append("<BODY>");
        customHTMLForReplacementBill.append("<TABLE width=\"100%\">");

        customHTMLForReplacementBill.append("<TR><TD>Bill Number</TD><TD>:</TD><TD>" + oleLoanDocument.getRepaymentFeePatronBillId() + "</TD></TR>");
        customHTMLForReplacementBill.append("<TR><TD>Fee Type</TD><TD>:</TD><TD>" + OLEConstants.REPLACEMENT_FEE + "</TD></TR>");
        customHTMLForReplacementBill.append("<TR><TD>Fee Amount</TD><TD>:</TD><TD>" + CurrencyFormatter
                .getSymbolForCurrencyPattern() + oleLoanDocument.getReplacementBill() + "</TD></TR>");

        customHTMLForReplacementBill.append("</TABLE>");
        customHTMLForReplacementBill.append("</BODY>");
        customHTMLForReplacementBill.append("</HTML>");

        return customHTMLForReplacementBill.toString();
    }

}
