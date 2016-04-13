package org.kuali.ole.deliver.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 4/13/16.
 */
public class RequestNoticeDAO extends LoanHistoryDAO {

    public List<String> getRequestIds(){
        String dbVendor = getProperty("db.vendor");
        String distinctQueryPart = "";
        if (dbVendor.equals("mysql")) {
           distinctQueryPart =  "IF(RQST_ID IS NULL,0,RQST_ID)";
        }else{
            distinctQueryPart = "nvl2(RQST_ID,RQST_ID,0)";
        }
        String query = "SELECT OLE_RQST_ID FROM OLE_DLVR_RQST_T WHERE OLE_RQST_ID NOT IN (SELECT "+distinctQueryPart+" FROM OLE_DLVR_LOAN_NOTICE_T) AND OLE_RQST_TYP_ID IN ('2','4','6');";
        List requestIds = new ArrayList();
        List<Map<String, Object>> queryForList = executeQuery(query);
        for (Iterator<Map<String, Object>> iterator = queryForList.iterator(); iterator.hasNext(); ) {
            Map<String, Object> next = iterator.next();
            Object loan_id = next.get("OLE_RQST_ID");
            String loanId = loan_id.toString();
            requestIds.add(loanId);
        }
        return requestIds;
    }
}
