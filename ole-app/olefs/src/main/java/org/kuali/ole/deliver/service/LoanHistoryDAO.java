package org.kuali.ole.deliver.service;

import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 3/17/16.
 */
public class LoanHistoryDAO extends LoanWithNoticesDAO {
    public List<String> getLoanIds(){
    String query = "SELECT LOAN_TRAN_ID FROM OLE_DLVR_LOAN_T WHERE LOAN_TRAN_ID NOT IN (SELECT DISTINCT(LOAN_TRAN_ID) FROM OLE_DLVR_CIRC_RECORD);";
    List loanIds = new ArrayList();
    List<Map<String, Object>> queryForList = executeQuery(query);
    for (Iterator<Map<String, Object>> iterator = queryForList.iterator(); iterator.hasNext(); ) {
        Map<String, Object> next = iterator.next();
        Object loan_id = next.get("loan_tran_id");
        String loanId = loan_id.toString();
        loanIds.add(loanId);
    }
    return loanIds;
    }
}
