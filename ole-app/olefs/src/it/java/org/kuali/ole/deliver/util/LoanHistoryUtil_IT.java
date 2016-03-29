package org.kuali.ole.deliver.util;

import junit.framework.Assert;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.bo.OleCirculationHistory;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.processor.LoanProcessor_IT;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 3/2/16.
 */
public class LoanHistoryUtil_IT extends OLETestCaseBase {

    private LoanHistoryUtil loanHistoryUtil;
    private BusinessObjectService businessObjectService;

    public LoanHistoryUtil getLoanHistoryUtil() {
        if(loanHistoryUtil == null){
            loanHistoryUtil = new LoanHistoryUtil();
        }
        return loanHistoryUtil;
    }

    public BusinessObjectService getBusinessObjectService(){
        if(businessObjectService ==null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    @Test
    public void populateCirculationHistoryTable() throws Exception{
    getLoanHistoryUtil().populateCirculationHistoryTable();
  //  getLoanHistoryUtil().getLoans();
    }


}
