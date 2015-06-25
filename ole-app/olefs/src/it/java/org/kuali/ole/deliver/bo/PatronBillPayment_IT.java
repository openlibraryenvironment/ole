package org.kuali.ole.deliver.bo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/6/12
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */

public class PatronBillPayment_IT extends KFSTestCaseBase {

    private BusinessObjectService boService ;

    /**
     *  Gets the businessObjectService attribute.
     * @return  Returns the businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == boService) {
            boService = KRADServiceLocator.getBusinessObjectService();
        }
        return boService;
    }
    @Test
    @Transactional
    public void testSaveAndSearch() {
        PatronBillPayment patronBillPayment = new PatronBillPayment();
        FeeType feeType = new FeeType();
        patronBillPayment.setBillDate(new java.sql.Date(new Date().getTime()));
        patronBillPayment.setBillNumber("9999");
        patronBillPayment.setPatronId("Mock PatronId");
       /* patronBillPayment.setFirstName("Mock FirstName");
        patronBillPayment.setLastName("Mock LastName");*/
        patronBillPayment.setOperatorId("Mock OperatorId");
        //patronBillPayment.setMachineId("Mock MachineId");   //commented for jira OLE-5675
        feeType.setFeeType("Mock Fine");
        feeType.setBillNumber("9999");
        feeType.setFeeAmount(new KualiDecimal(100.00));
        patronBillPayment.setTotalAmount(new KualiDecimal(100.00));
        //OlePaymentStatus olePaymentStatus = getPaymentStatus();
        OlePaymentStatus olePaymentStatus = new OlePaymentStatus();
        olePaymentStatus.setPaymentStatusId("1");
        feeType.setPaymentStatusCode("1");
        feeType.setPaymentStatus("1");
        feeType.setOlePaymentStatus(olePaymentStatus);
/*        if(olePaymentStatus!=null){
            patronBillPayment.setOlePaymentStatus(olePaymentStatus);
            patronBillPayment.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
        }*/
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(patronBillPayment);
        boService.save(feeType);
        PatronBillPayment patronBillPaymentService = boService.findBySinglePrimaryKey(PatronBillPayment.class,patronBillPayment.getBillNumber());
        assertEquals("Mock PatronId",patronBillPaymentService.getPatronId());
       // assertEquals("Mock LastName",patronBillPaymentService.getLastName());
    }
    private OlePaymentStatus getPaymentStatus(){
        Map statusMap = new HashMap();
        statusMap.put("paymentStatusName", "Outstanding");
        List<OlePaymentStatus> olePaymentStatusList = (List<OlePaymentStatus>)getBusinessObjectService().findMatching(OlePaymentStatus.class,statusMap);
        return olePaymentStatusList!=null && olePaymentStatusList.size()>0 ? olePaymentStatusList.get(0): null;
    }
  }
