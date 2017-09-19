package org.kuali.ole.deliver;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.api.OlePatronDefintionHelper;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.ncip.service.impl.OLECirculationHelperServiceImpl;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/2/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestDocumentHelperServiceimpl_IT extends KFSTestCaseBase {
    private BusinessObjectService businessObjectService;
    private OleDeliverRequestDocumentHelperServiceImpl service;
    private OlePatronDocument olePatronDocument;
    private EntityBo entityBo = new EntityBo();
    private EntityNameBo entityNameBo = new EntityNameBo();
    private EntityAddressBo entityAddressBo = new EntityAddressBo();
    private EntityTypeContactInfoBo entityTypeContactInfoBo = new EntityTypeContactInfoBo();
    private EntityEmailBo entityEmailBo = new EntityEmailBo();
    private EntityPhoneBo entityPhoneBo = new EntityPhoneBo();


    @Before
    public void setUp() throws Exception {
        super.setUp();
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
        service = new OleDeliverRequestDocumentHelperServiceImpl();
        olePatronDocument = getOlePatronDocument();

    }

    public OlePatronDocument getOlePatronDocument() {
        OlePatronDefinition olePatronDefinition = OlePatronDefintionHelper.create();
        OlePatronDocument olePatronDocument = OlePatronDocument.from(olePatronDefinition);
        return olePatronDocument;
    }


    @Test
    public void validateRequestTypeTest() {
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("1");
        oleDeliverRequestBo.setRequestTypeId("2");
        boolean isValid = service.validateRequestType(oleDeliverRequestBo);
        assertFalse(isValid);

    }


    @Test
    public void validateDeliveryPrivilegeTest() {
        if (olePatronDocument != null) {
            olePatronDocument.setDeliveryPrivilege(true);
            //olePatronDocument = changeId(olePatronDocument);
            businessObjectService.save(olePatronDocument);
            OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
            oleDeliverRequestBo.setRequestId("13");
            oleDeliverRequestBo.setRequestTypeId("1");
            oleDeliverRequestBo.setOlePatron(olePatronDocument);
            boolean isValid = service.validateDeliveryPrivilege(oleDeliverRequestBo);
            assertTrue(isValid);
        }
    }


    @Test
    public void validatePagingPrivilegeTest() {
        if (olePatronDocument != null) {
            olePatronDocument.setOlePatronId("114");
            olePatronDocument.setPagingPrivilege(true);
            //olePatronDocument = changeId(olePatronDocument);
            businessObjectService.save(olePatronDocument);
            OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
            oleDeliverRequestBo.setRequestId("21");
            oleDeliverRequestBo.setRequestTypeId("1");
            oleDeliverRequestBo.setOlePatron(olePatronDocument);
            boolean isValid = service.validatePagingPrivilege(oleDeliverRequestBo);
            assertTrue(isValid);
        }
    }


    @Test
    public void validateProxyPatronTest() {
        if (olePatronDocument != null) {
            olePatronDocument.setOlePatronId("108");
            olePatronDocument.setPagingPrivilege(true);
            //olePatronDocument = changeId(olePatronDocument);
            businessObjectService.save(olePatronDocument);
            OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
            oleDeliverRequestBo.setRequestId("11");
            oleDeliverRequestBo.setRequestTypeId("1");
            oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
            oleDeliverRequestBo.setProxyBorrowerId(olePatronDocument.getProxyPatronId());
            oleDeliverRequestBo.setOlePatron(olePatronDocument);
            assertTrue(service.isValidProxyPatron(oleDeliverRequestBo));
        }
    }


    @Test
    @Transactional
    public void isRequestAlreadyRaisedByPatronTest() throws Exception{
        if (olePatronDocument != null) {
            olePatronDocument.setOlePatronId("101");
            olePatronDocument.setPagingPrivilege(true);
            //olePatronDocument = changeId(olePatronDocument);
            olePatronDocument.getEntity().getEntityTypeContactInfos().get(0).getPhoneNumbers().get(0).setId("20222");
            businessObjectService.save(olePatronDocument);
            OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
            oleDeliverRequestBo.setRequestId("3");
            oleDeliverRequestBo.setRequestTypeId("1");
            oleDeliverRequestBo.setItemId("600011");
            oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
            oleDeliverRequestBo.setOlePatron(olePatronDocument);
            oleDeliverRequestBo.setItemUuid("1123");
            businessObjectService.save(oleDeliverRequestBo);
            OleDeliverRequestBo oleDeliverRequestBo1 = new OleDeliverRequestBo();
            oleDeliverRequestBo1.setRequestId("5");
            oleDeliverRequestBo1.setRequestTypeId("1");
            oleDeliverRequestBo1.setItemId("1");
            oleDeliverRequestBo1.setItemUuid("1123");
            oleDeliverRequestBo1.setBorrowerId(olePatronDocument.getOlePatronId());
            oleDeliverRequestBo1.setOlePatron(olePatronDocument);
            boolean isRequestRaised = service.isRequestAlreadyRaisedByPatron(oleDeliverRequestBo1);
            assertTrue(isRequestRaised);

        }
    }

    /*
    @Test
    @Transactional
    public void isPatronRecordExpiredTest(){
        if(olePatronDocument!=null){
            olePatronDocument.setOlePatronId("111");
            OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
            oleDeliverRequestBo.setRequestId("17");
            oleDeliverRequestBo.setRequestTypeId("1");
            olePatronDocument.setExpirationDate(new Date(2013, 5, 5));
            olePatronDocument=changeId(olePatronDocument);
            businessObjectService.save(olePatronDocument);
            oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
            oleDeliverRequestBo.setOlePatron(olePatronDocument);
            boolean isValid=    service.isPatronRecordExpired(oleDeliverRequestBo);
            assertFalse(isValid);
        }   }



    //working
    @Test
    @Transactional
    public void isItemEligibleTest(){
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("1");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setItemStatus("AVAILABLE");

       assertTrue(service.isItemEligible(oleDeliverRequestBo));

    }
    
    @Test
    @Transactional
    public void processRequesterTest(){
        olePatronDocument.setOlePatronId("106");
        olePatronDocument=changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("9");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setRequestCreator("Patron");
        oleDeliverRequestBo.setBorrowerId("1");
        oleDeliverRequestBo.setProxyBorrowerId("2");

        assertNull(service.processRequester(oleDeliverRequestBo).getProxyBorrowerId());
    }
    
    @Test
    @Transactional
    public void processRequestTypeTest(){
        olePatronDocument.setOlePatronId("103");
        olePatronDocument=changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("5");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setPickUpLocationId("1");
        oleDeliverRequestBo = service.processRequestType(oleDeliverRequestBo);

        assertEquals("2",oleDeliverRequestBo.getRequestTypeId());
    }

    
    @Test
    @Transactional
    public void processPatronTest(){
        olePatronDocument.setOlePatronId("115");
        olePatronDocument=changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setCirculationDeskId("22");
        oleCirculationDesk.setCirculationDeskCode("Mock Circulation Desk Code");
        oleCirculationDesk.setCirculationDeskPublicName("Mock Circulation Desk Public Name");
        oleCirculationDesk.setCirculationDeskStaffName("Mock Circulation Desk Staff Name");
        oleCirculationDesk.setActive(true);
        businessObjectService.save(oleCirculationDesk);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("22");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setPickUpLocationId("1");
        oleDeliverRequestBo.setItemUuid("1123");
        oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_PATRON);
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        oleDeliverRequestBo.setBorrowerBarcode(olePatronDocument.getBarcode()
        );
        oleDeliverRequestBo = service.processPatron(oleDeliverRequestBo);
    }
    
    @Test
    @Transactional
    public void deleteRequestTest(){
        olePatronDocument.setOlePatronId("113");
        olePatronDocument=changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setCirculationDeskId("20");
        oleCirculationDesk.setCirculationDeskCode("Mock Circulation Desk Code");
        oleCirculationDesk.setCirculationDeskPublicName("Mock Circulation Desk Public Name");
        oleCirculationDesk.setCirculationDeskStaffName("Mock Circulation Desk Staff Name");
        oleCirculationDesk.setActive(true);
        businessObjectService.save(oleCirculationDesk);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("20");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setRequestTypeCode("Copy Request");
        oleDeliverRequestBo.setPickUpLocationId("1");
        oleDeliverRequestBo.setItemId("1");
        oleDeliverRequestBo.setItemUuid("1123");
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        businessObjectService.save(oleDeliverRequestBo);
         service.deleteRequest("20", "1","1","1");
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put("requestId",oleDeliverRequestBo.getRequestId());
        requestMap.put("itemId",oleDeliverRequestBo.getItemId());
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class,requestMap);
        assertTrue(oleDeliverRequestBoList.size() == 0);

    }


  /*  @Test
    @Transactional
    public void getRequestedItemsTest(){
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("1");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setPickUpLocationId("1");
        oleDeliverRequestBo.setItemId("1");
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        OleDeliverRequestBo oleDeliverRequestBo1 = new OleDeliverRequestBo();
        oleDeliverRequestBo1.setRequestId("2");
        oleDeliverRequestBo1.setRequestTypeId("1");
        oleDeliverRequestBo1.setPickUpLocationId("1");
        oleDeliverRequestBo1.setItemId("2");
        oleDeliverRequestBo1.setBorrowerId(olePatronDocument.getOlePatronId());
        businessObjectService.save(oleDeliverRequestBo);
        businessObjectService.save(oleDeliverRequestBo1);

        List<OleDeliverRequestBo> oleDeliverRequestBoList = service.getRequestedItems(olePatronDocument.getOlePatronId());

        assertTrue(oleDeliverRequestBoList.size()==2);
    }*/

    @Test
    public void isRequestedRaisedTest() {
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setCirculationDeskId("14");
        oleCirculationDesk.setCirculationDeskCode("Mock Circulation Desk Code");
        oleCirculationDesk.setCirculationDeskPublicName("Mock Circulation Desk Public Name");
        oleCirculationDesk.setCirculationDeskStaffName("Mock Circulation Desk Staff Name");
        oleCirculationDesk.setActive(true);
        businessObjectService.save(oleCirculationDesk);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("26");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setPickUpLocationId("1");
        oleDeliverRequestBo.setItemId("1");
        oleDeliverRequestBo.setItemUuid("1123");
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        OleDeliverRequestBo oleDeliverRequestBo1 = new OleDeliverRequestBo();
        oleDeliverRequestBo1.setRequestId("27");
        oleDeliverRequestBo1.setRequestTypeId("1");
        oleDeliverRequestBo1.setPickUpLocationId("1");
        oleDeliverRequestBo1.setItemId("1");
        oleDeliverRequestBo1.setItemUuid("1123");
        oleDeliverRequestBo1.setBorrowerId(olePatronDocument.getOlePatronId());
        businessObjectService.save(oleDeliverRequestBo);
        boolean isRaised = service.isRequestRaised(oleDeliverRequestBo1);
        assertTrue(isRaised);
    }

    @Test
    public void isItemAvailableTest() {
        olePatronDocument.setOlePatronId("110");
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("16");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setPickUpLocationId("1");
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        oleDeliverRequestBo.setRequestTypeCode("Recall/Delivery Request");
        boolean available = service.isItemAvailable(oleDeliverRequestBo);

        assertTrue(available);
    }

    @Test
    public void isAlreadyLoanedTest() {
        olePatronDocument.setOlePatronId("105");
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("8");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setPickUpLocationId("1");
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        boolean available = service.isAlreadyLoaned(oleDeliverRequestBo);

        assertFalse(available);
    }

    @Test
    public void canRaiseRequestTest() {
        olePatronDocument.setOlePatronId("116");
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("23");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setPickUpLocationId("1");
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        oleDeliverRequestBo.setRequestTypeCode("Recall/Delivery Request");
        boolean available = service.canRaiseRequest(oleDeliverRequestBo);
        assertFalse(available);
    }

    @Test
    @Transactional
    public void reOrderQueuePositionTest() {
        olePatronDocument.setOlePatronId("112");
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("28");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setItemUuid("1123");
        oleDeliverRequestBo.setBorrowerQueuePosition(1);
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        Map<String, String> requestTypeMap = new HashMap<String, String>();
        requestTypeMap.put("requestTypeId", oleDeliverRequestBo.getRequestTypeId());
        List<OleDeliverRequestType> oleDeliverRequestTypes = (List<OleDeliverRequestType>) businessObjectService.findMatching(OleDeliverRequestType.class, requestTypeMap);
        oleDeliverRequestBo.setOleDeliverRequestType(oleDeliverRequestTypes.get(0));
        businessObjectService.save(oleDeliverRequestBo);
        OleDeliverRequestBo oleDeliverRequestBo1 = new OleDeliverRequestBo();
        oleDeliverRequestBo1.setRequestId("29");
        oleDeliverRequestBo1.setRequestTypeId("5");
        oleDeliverRequestBo.setItemUuid("1123");
        oleDeliverRequestBo1.setBorrowerQueuePosition(2);
        oleDeliverRequestBo1.setBorrowerId(olePatronDocument.getOlePatronId());
        Map<String, String> requestTypeMap1 = new HashMap<String, String>();
        requestTypeMap.put("requestTypeId", oleDeliverRequestBo1.getRequestTypeId());
        List<OleDeliverRequestType> oleDeliverRequestTypes1 = (List<OleDeliverRequestType>) businessObjectService.findMatching(OleDeliverRequestType.class, requestTypeMap1);
        oleDeliverRequestBo1.setOleDeliverRequestType(oleDeliverRequestTypes1.get(0));
        oleDeliverRequestBo1.setItemUuid("1123");
        businessObjectService.save(oleDeliverRequestBo1);

        OleDeliverRequestBo oleDeliverRequestBo2 = new OleDeliverRequestBo();
        oleDeliverRequestBo2.setRequestTypeId("3");
        oleDeliverRequestBo.setItemUuid("1123");
        oleDeliverRequestBo2.setBorrowerId(olePatronDocument.getOlePatronId());
        Map<String, String> requestTypeMap2 = new HashMap<String, String>();
        requestTypeMap.put("requestTypeId", oleDeliverRequestBo2.getRequestTypeId());
        List<OleDeliverRequestType> oleDeliverRequestTypes2 = (List<OleDeliverRequestType>) businessObjectService.findMatching(OleDeliverRequestType.class, requestTypeMap2);
        oleDeliverRequestBo2.setOleDeliverRequestType(oleDeliverRequestTypes2.get(0));
        oleDeliverRequestBo2.setItemUuid("1123");
        OleDeliverRequestBo oleDeliverRequestBo3 = service.reOrderQueuePosition(oleDeliverRequestBo2);


    }

    @Test
    public void validateQueuePositionTest() {
        olePatronDocument.setOlePatronId("104");
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("6");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setBorrowerQueuePosition(1);
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        oleDeliverRequestBoList.add(oleDeliverRequestBo);

        OleDeliverRequestBo oleDeliverRequestBo1 = new OleDeliverRequestBo();
        oleDeliverRequestBo1.setRequestId("7");
        oleDeliverRequestBo1.setRequestTypeId("5");
        oleDeliverRequestBo1.setBorrowerQueuePosition(2);
        oleDeliverRequestBo1.setBorrowerId(olePatronDocument.getOlePatronId());
        oleDeliverRequestBoList.add(oleDeliverRequestBo1);

        OleDeliverRequestBo oleDeliverRequestBo2 = new OleDeliverRequestBo();
        oleDeliverRequestBo2.setRequestTypeId("3");
        oleDeliverRequestBo2.setBorrowerId(olePatronDocument.getOlePatronId());
        oleDeliverRequestBo1.setBorrowerQueuePosition(3);
        oleDeliverRequestBoList.add(oleDeliverRequestBo2);
        String valid = service.validateQueuePosition(oleDeliverRequestBoList);

        assertTrue(valid.equals(OLEConstants.OleDeliverRequest.REORDER_SUCCESS));


    }

    public String createItem(String patronBarcode, String operatorId, String itemBarcode, String callNumber, String title, String author, String itemType, String itemLocation, String dateExpires, String requestType, String pickUpLocation) throws Exception{
        OLECirculationHelperServiceImpl oleCirculationHelperServiceImpl = new OLECirculationHelperServiceImpl();
        String itemIdentifier = oleCirculationHelperServiceImpl.acceptItem(itemBarcode,callNumber,title,author,itemType,itemLocation,operatorId);
        LOG.info("item--------->"+itemIdentifier);
        return itemIdentifier;
    }

    @Test
    @Transactional
    public void cancelDocumentTest() throws Exception{
        GlobalVariables.setUserSession(new UserSession("dev2"));
        olePatronDocument.setOlePatronId("102");
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("4");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setBorrowerQueuePosition(1);
        oleDeliverRequestBo.setItemId("900005");
        String Uuid = createItem("6010570002790936","dev2","900005","123", "American History", "Chetan", "BOOK", "B-EDUC/BED-STACKS", null,null, "BL-EDUC");
        oleDeliverRequestBo.setItemUuid(Uuid);

        OleDeliverRequestType oleDeliverRequestType = new OleDeliverRequestType();
        oleDeliverRequestType.setRequestTypeId("1");
        oleDeliverRequestType.setRequestTypeCode("10101");

        oleDeliverRequestBo.setOleDeliverRequestType(oleDeliverRequestType);
        oleDeliverRequestBo.setCreateDate(new Timestamp(System.currentTimeMillis()));
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        oleDeliverRequestBo.setOlePatron(olePatronDocument);



        businessObjectService.save(oleDeliverRequestBo);

        service.cancelDocument(oleDeliverRequestBo);

        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("requestId", oleDeliverRequestBo.getRequestId());

        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);

        assertTrue(oleDeliverRequestBoList.size() == 0);
    }


    @Test
    @Transactional
    public void isRequestRaisedTest() {
        olePatronDocument.setOlePatronId("109");
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("24");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setItemId("1");
        oleDeliverRequestBo.setItemUuid("1123");
        oleDeliverRequestBo.setBorrowerQueuePosition(1);
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
        businessObjectService.save(oleDeliverRequestBo);
        assertTrue(service.isRequestRaised(oleDeliverRequestBo));

    }

    @Test
    @Transactional
    public void processItemTypeTest() {
        olePatronDocument.setOlePatronId("107");
        //olePatronDocument = changeId(olePatronDocument);
        businessObjectService.save(olePatronDocument);
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setRequestId("10");
        oleDeliverRequestBo.setRequestTypeId("1");
        oleDeliverRequestBo.setItemId("1");
        oleDeliverRequestBo.setBorrowerQueuePosition(1);
        oleDeliverRequestBo.setItemType("BOOK");
        oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());

        OleDeliverRequestBo oleDeliverRequestBo1 = service.processItemType(oleDeliverRequestBo);
        assertEquals("Book", oleDeliverRequestBo1.getItemTypeName());
    }

    private OlePatronDocument changeId(OlePatronDocument olePatronDocument) {
        OLEPatronHelper.idValue = OLEPatronHelper.idValue + 1;
        int id = OLEPatronHelper.idValue;
        List<EntityAddressBo> addressList = new ArrayList<>();
        List<EntityEmailBo> emailList = new ArrayList<>();
        List<EntityPhoneBo> phoneList = new ArrayList<>();
        List<EntityPhoneBo> contactPhoneList = new ArrayList<>();
        List<EntityTypeContactInfoBo> contactList = new ArrayList<>();
        olePatronDocument.setOlePatronId(String.valueOf(id));
        olePatronDocument.getEntity().setId(String.valueOf(id));
        for (EntityTypeContactInfoBo entityTypeContactInfoBo1 : olePatronDocument.getEntity().getEntityTypeContactInfos()) {
            entityTypeContactInfoBo = entityTypeContactInfoBo1;
            entityTypeContactInfoBo.setEntityId(String.valueOf(id));
            contactList.add(entityTypeContactInfoBo);
        }
        olePatronDocument.getEntity().setEntityTypeContactInfos(contactList);
        for (EntityAddressBo entityAddressBo1 : olePatronDocument.getAddresses()) {
            entityAddressBo = entityAddressBo1;
            entityAddressBo.setId(String.valueOf(id));
            entityAddressBo.setEntityId(String.valueOf(id));
            addressList.add(entityAddressBo);
        }
        for (EntityEmailBo entityEmailBo1 : olePatronDocument.getEmails()) {
            entityEmailBo = entityEmailBo1;
            entityEmailBo.setEntityId(String.valueOf(id));
            entityEmailBo.setId(String.valueOf(id));
            emailList.add(entityEmailBo);
        }
        olePatronDocument.getName().setId(String.valueOf(id));
        for (EntityPhoneBo entityPhoneBo1 : olePatronDocument.getPhones()) {
            entityPhoneBo = entityPhoneBo1;
            entityPhoneBo.setId(String.valueOf(id));
            entityPhoneBo.setEntityId(String.valueOf(id));
            phoneList.add(entityPhoneBo);
        }

        List<OlePatronNotes> olePatronNotesList = new ArrayList<>();
        for (OlePatronNotes OlePatronNotes : olePatronDocument.getNotes()) {
            OlePatronNotes.setPatronNoteId(String.valueOf(id));
            OlePatronNotes.setOlePatronId(String.valueOf(id));
            olePatronNotesList.add(OlePatronNotes);
        }
        List<OlePatronLostBarcode> olePatronLostBarcodeList = new ArrayList<>();
        for (OlePatronLostBarcode olePatronLostBarcode : olePatronDocument.getLostBarcodes()) {
            olePatronLostBarcode.setOlePatronLostBarcodeId(String.valueOf(id));
            olePatronLostBarcode.setOlePatronId(String.valueOf(id));
            olePatronLostBarcodeList.add(olePatronLostBarcode);
        }

        olePatronDocument.getOleBorrowerType().setBorrowerTypeId(String.valueOf(id));

        List<OlePatronAffiliation> olePatronAffiliationList = new ArrayList<>();
        for (OlePatronAffiliation olePatronAffiliation : olePatronDocument.getPatronAffiliations()) {
            olePatronAffiliation.setEntityAffiliationId(String.valueOf(id));
            olePatronAffiliation.setEntityId(String.valueOf(id));
            olePatronAffiliationList.add(olePatronAffiliation);
        }
        List<OleProxyPatronDocument> oleProxyPatronDocumentList = new ArrayList<>();
        for (OleProxyPatronDocument oleProxyPatronDocument : olePatronDocument.getOleProxyPatronDocumentList()) {
            oleProxyPatronDocument.setProxyPatronId(String.valueOf(id));
            oleProxyPatronDocument.setOleProxyPatronDocumentId(String.valueOf(id));
            oleProxyPatronDocument.setOlePatronId(String.valueOf(id));
            oleProxyPatronDocumentList.add(oleProxyPatronDocument);
        }
        List<OlePatronLocalIdentificationBo> olePatronLocalIdentificationBoList = new ArrayList<>();
        for (OlePatronLocalIdentificationBo olePatronLocalIdentificationBo : olePatronDocument.getOlePatronLocalIds()) {
            olePatronLocalIdentificationBo.setPatronLocalSeqId(String.valueOf(id));
            olePatronLocalIdentificationBo.setLocalId(String.valueOf(id));
            olePatronLocalIdentificationBo.setOlePatronId(String.valueOf(id));
            olePatronLocalIdentificationBoList.add(olePatronLocalIdentificationBo);
        }
        List<OleAddressBo> oleAddressBoList = new ArrayList<>();
        for (OleAddressBo oleAddressBo : olePatronDocument.getOleAddresses()) {
            oleAddressBo.setOleAddressId(String.valueOf(id));
            oleAddressBo.setOlePatronId(String.valueOf(id));
            oleAddressBoList.add(oleAddressBo);
        }
        olePatronDocument.setAddresses(addressList);
        olePatronDocument.setEmails(emailList);
        olePatronDocument.setPhones(phoneList);
        olePatronDocument.setNotes(olePatronNotesList);
        olePatronDocument.setLostBarcodes(olePatronLostBarcodeList);
        olePatronDocument.setPatronAffiliations(olePatronAffiliationList);
        olePatronDocument.setOleProxyPatronDocuments(oleProxyPatronDocumentList);
        olePatronDocument.setOlePatronLocalIds(olePatronLocalIdentificationBoList);
        olePatronDocument.setOleAddresses(oleAddressBoList);
        return olePatronDocument;
    }


}





