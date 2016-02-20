package org.kuali.ole.deliver.controller;

import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.service.impl.DataConnectionServiceImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 2/15/16.
 */
public class OLEDeliverNoticeHistory_IT extends OLETestCaseBase {

    private BusinessObjectService service ;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        service = KRADServiceLocator.getBusinessObjectService();
    }

    @Test
    public void testNotice(){
        OLEDeliverNoticeHistory hist= new OLEDeliverNoticeHistory();
/*        String content = "<html><title>Recall Notice</title></html>";
        hist.setLoanId("5");
        hist.setNoticeContent(content.getBytes());
        service.save(hist);*/
        Map<String,String> criteriaMap = new HashMap<String,String>();
        criteriaMap.put("requestId","1");
        List<OLEDeliverNoticeHistory> deliverNoticeHistoryList =   (List<OLEDeliverNoticeHistory>)service.findMatching(OLEDeliverNoticeHistory.class,criteriaMap);
        if(deliverNoticeHistoryList.size()>0){
            for(OLEDeliverNoticeHistory oleDeliverNoticeHistory : deliverNoticeHistoryList){
                System.out.println(new String(oleDeliverNoticeHistory.getNoticeContent()));
            }
        }
    }

    @Test
    public void testHistoryFromDb() throws Exception{
        DataConnectionServiceImpl dataBaseConnectionService = new DataConnectionServiceImpl();
        ResultSet resultSet =  dataBaseConnectionService.getResults("select * from OLE_DLVR_LOAN_NOTICE_HSTRY_T");
        System.out.println("message printed");
        while(resultSet.next()) {
            System.out.println(resultSet.getString("ID"));
            System.out.println(resultSet.getString("LOAN_ID"));
            System.out.println(resultSet.getTimestamp("NTC_SNT_DT"));
            System.out.println(resultSet.getString("PTRN_ID"));
            java.sql.Clob clob = resultSet.getClob("NTC_CNTNT");
            //  String content= dataBaseConnectionService.clobToString(clob);
            //System.out.println(content);
        }
        System.out.println("message ended");
    }

}
