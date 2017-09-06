package org.kuali.ole.ncip;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.ncip.service.impl.NonSip2CheckinItemServiceImplImpl;
import org.kuali.ole.ncip.service.impl.VuFindCheckoutItemServiceImplImpl;
import org.kuali.ole.ncip.service.impl.VuFindLookupUserServiceImpl;
import org.kuali.ole.sys.context.SpringContext;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by sheiksalahudeenm on 22/4/15.
 */
public class OLECirculationService_IT  extends OLETestCaseBase {

    Logger LOG = Logger.getLogger(OLECirculationService_IT.class);

    private OLECirculationService  oleCirculationService = null; //new OLECirculationServiceImpl();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        oleCirculationService = (OLECirculationService) SpringContext.getBean("oleCirculationService");
    }

    @Test
    public void lookupUserTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        Map lookupUserParameters = new HashMap();
        lookupUserParameters.put("patronBarcode", "2761886");
        lookupUserParameters.put("operatorId", "dev2");
        String outputString = new VuFindLookupUserServiceImpl().lookupUser(lookupUserParameters);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>000</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void getCheckedOutItemsTest() throws Exception {
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.getCheckedOutItems("2761886", "dev2");
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>000</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void placeRequestTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.placeRequest("OLE-ASR", "10002", "5001", null, "Copy Request", "API", "API", "100001", "", null, "Test Note");
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>021</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void overridePlaceRequestTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.overridePlaceRequest("OLE-ASR", "10002", "5001", "Copy Request", "API", "API", "100001", "", null, "Test Note");
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>021</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void cancelRequestTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.cancelRequest("10002", "6010570003043558", "5001");
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>007</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void renewItemTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.renewItem("6010570003043558", "dev2", "5001", false);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>003</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void renewItemListTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.renewItemList("6010570003043558", "dev2","5001,5002,5003",false);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>003</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void acceptItemTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.acceptItem("2761886", "10002", "202", "X","Title","Author","Book","",null,"","");
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>021</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void checkInItemTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();

        Map checkinParameters = new HashMap();
        checkinParameters.put("operatorId", "10002");
        checkinParameters.put("itemBarcode", "201");
        checkinParameters.put("deleteIndicator", "N");
        String outputString = new NonSip2CheckinItemServiceImplImpl().checkinItem(checkinParameters);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  for CheckIn  : " + timeTaken);
        assertNotNull(outputString.contains("<code>024</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void checkOutItemTest(){
        LOG.info("Inside checkOutItemTest");
        Long startingTime = System.currentTimeMillis();
        Map checkoutParameters = new HashMap();
        checkoutParameters.put("patronBarcode", "2761886");
        checkoutParameters.put("operatorId", "10002");
        checkoutParameters.put("itemBarcode", "201");
        String outputString = new VuFindCheckoutItemServiceImplImpl().checkoutItem(checkoutParameters);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken for CheckOut  : " + timeTaken);
        assertNotNull(outputString.contains("<code>000</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void getFineTest() throws Exception {
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.getFine("1109687", "10002");
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>000</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void getHoldsTest() throws Exception {
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.getHolds("6010570003043558", "dev2");
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>000</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void cancelRequestsTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.cancelRequests("dev2","1");
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        assertNotNull(outputString.contains("<code>007</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void runCheckoutAndCheckin(){
        LOG.info("Inside runCheckoutAndCheckin user test");
        checkOutItemTest();
        checkInItemTest();
    }
}
