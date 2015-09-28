package org.kuali.ole.deliver.notice;

import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pvsubrah on 9/28/15.
 */
public class OleNoticeContentHandler {

    private BusinessObjectService businessObjectService;

    public String getNoticeContent(String patronId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("patronId", patronId);
        List<OLEDeliverNoticeHistory> matching = (List<OLEDeliverNoticeHistory>) getBusinessObjectService().findMatching(OLEDeliverNoticeHistory.class, map);
        OLEDeliverNoticeHistory noticeHistory = matching.get(0);

        byte[] noticeContent = noticeHistory.getNoticeContent();
        String noticeContentString = new String(noticeContent);
        return noticeContentString;

    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService =  KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
