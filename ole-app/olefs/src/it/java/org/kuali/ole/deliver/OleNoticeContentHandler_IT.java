package org.kuali.ole.deliver;

import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.notice.OleNoticeContentHandler;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pvsubrah on 9/28/15.
 */
public class OleNoticeContentHandler_IT extends OLETestCaseBase {

    @Test
    public void saveAndRetrieveNoticeContent() throws Exception {
        String sampleNoticeContent =
                "<HTML><TITLE>null</TITLE><HEAD></HEAD><BODY><HTML><TITLE>null</TITLE><HEAD></HEAD><BODY><TABLE></BR></BR><TR><TD>Patron Name:</TD><TD>FirtName null</TD></TR><TR><TD>Address:</TD><TD></TD></TR><TR><TD>Email:</TD><TD></TD></TR><TR><TD>Phone #:</TD><TD></TD></TR><TR><TD></TABLE><TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR><TABLE width=\"100%\"><TR><TD><CENTER>null</CENTER></TD></TR><TR><TD><p>null</p></TD></TR><TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE><table><TR><TD>Title:</TD><TD></TD></TR><TR><TD>Author:</TD><TD></TD></TR><TR><TD>Volume/Issue/Copy #:</TD><TD>/ /</TD></TR><TR><TD>Item was due:</TD><TD></TD></TR><TR><TD>Library shelving location:</TD><TD></TD></TR><TR><TD>Call #:</TD><TD></TD></TR><TR><TD>Item_Barcode:</TD><TD></TD></TR><TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></table>\n";

        OLEDeliverNoticeHistory oleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
        oleDeliverNoticeHistory.setItemBarcode("123");
        oleDeliverNoticeHistory.setNoticeContent(sampleNoticeContent.getBytes());
        oleDeliverNoticeHistory.setLoanId("12");
        oleDeliverNoticeHistory.setPatronId("123");


        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        OLEDeliverNoticeHistory savedDeliverNoticeHistory = businessObjectService.save(oleDeliverNoticeHistory);
        assertNotNull(savedDeliverNoticeHistory.getId());

        OleNoticeContentHandler oleNoticeContentHandler = new OleNoticeContentHandler();
        String noticeContent = oleNoticeContentHandler.getNoticeContent("123");
        System.out.println(noticeContent);
    }

}
