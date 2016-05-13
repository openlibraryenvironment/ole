package org.kuali.ole.deliver.service;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.sys.context.SpringContext;

import java.util.*;

/**
 * Created by maheswarang on 4/13/16.
 */
public class RequestNoticeDAO extends LoanHistoryDAO {


    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;
    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        if (oleLoanDocumentDaoOjb == null) {
            oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return oleLoanDocumentDaoOjb;
    }

    public List<String> getRequestIds() {
        String dbVendor = getProperty("db.vendor");
        String query = "";
        if (dbVendor.equals("mysql")) {
            query = "SELECT OLE_RQST_ID FROM OLE_DLVR_RQST_T WHERE OLE_RQST_ID NOT IN (SELECT IF(RQST_ID IS NULL,0,RQST_ID) FROM OLE_DLVR_LOAN_NOTICE_T) AND OLE_RQST_TYP_ID IN ('2','4','6')";
        } else {
            String rqstId = getOleLoanDocumentDaoOjb().getRequestId();
            rqstId = rqstId.substring(0, rqstId.length() - 1);
            if (rqstId != null) {
                query = "SELECT OLE_RQST_ID FROM OLE_DLVR_RQST_T WHERE OLE_RQST_ID NOT IN (" + rqstId + ")  AND OLE_RQST_TYP_ID IN ('2','4','6')";
            }
        }
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
