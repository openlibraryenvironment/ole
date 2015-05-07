package org.kuali.ole.ncip;

import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.core.Assert;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.ncip.service.impl.OLECirculationServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.sql.Date;

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
        String outputString = oleCirculationService.lookupUser("2761886", "dev2",null,false);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        Assert.isNotNull(outputString.contains("<code>000</code>"));
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
        Assert.isNotNull(outputString.contains("<code>000</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void placeRequestTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.placeRequest("OLE-ASR", "10002", "5001", "Copy Request", "API", "API", "100001", "", null);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        Assert.isNotNull(outputString.contains("<code>021</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void overridePlaceRequestTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.overridePlaceRequest("OLE-ASR", "10002", "5001", "Copy Request", "API", "API", "100001", "", null);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  : " + timeTaken);
        Assert.isNotNull(outputString.contains("<code>021</code>"));
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
        Assert.isNotNull(outputString.contains("<code>007</code>"));
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
        Assert.isNotNull(outputString.contains("<code>003</code>"));
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
        Assert.isNotNull(outputString.contains("<code>003</code>"));
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
        Assert.isNotNull(outputString.contains("<code>021</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void checkInItemTest(){
        LOG.info("Inside Lookup user test");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.checkInItem("2761886", "10002", "201", "N", false);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken  for CheckIn  : " + timeTaken);
        Assert.isNotNull(outputString.contains("<code>024</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void checkOutItemTest(){
        LOG.info("Inside checkOutItemTest");
        Long startingTime = System.currentTimeMillis();
        String outputString = oleCirculationService.checkOutItem("2761886", "10002", "201", false);
        Long endTime = System.currentTimeMillis();
        Long timeTaken = endTime-startingTime;
        LOG.info("The Total Time Taken for CheckOut  : " + timeTaken);
        Assert.isNotNull(outputString.contains("<code>000</code>"));
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
        Assert.isNotNull(outputString.contains("<code>000</code>"));
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
        Assert.isNotNull(outputString.contains("<code>000</code>"));
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
        Assert.isNotNull(outputString.contains("<code>007</code>"));
        LOG.info("Output String : " + outputString);
    }

    @Test
    public void runCheckoutAndCheckin(){
        LOG.info("Inside runCheckoutAndCheckin user test");
        checkOutItemTest();
        checkInItemTest();
    }
}
