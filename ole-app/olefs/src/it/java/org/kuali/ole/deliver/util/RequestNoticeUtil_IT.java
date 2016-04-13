package org.kuali.ole.deliver.util;

import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;

/**
 * Crea;ted by maheswarang on 4/13/16.
 */
public class RequestNoticeUtil_IT extends OLETestCaseBase {
   private RequestNoticeUtil requestNoticeUtil;

    public RequestNoticeUtil getRequestNoticeUtil(){
        if(requestNoticeUtil!=null){
            requestNoticeUtil = new RequestNoticeUtil();
        }
        return requestNoticeUtil;
    }

    @Test
    public void populateNoticeTableForExistingRequestsTest(){
        getRequestNoticeUtil().populateNoticeTableForExistingRequests();
    }

}
