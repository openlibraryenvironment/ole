package org.kuali.ole.deliver;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.api.EntityHelper;
import org.kuali.ole.deliver.api.OleLoanDefintionHelper;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.api.OlePatronDefintionHelper;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.service.OleCirculationPolicyService;
import org.kuali.ole.service.OleCirculationPolicyServiceImpl;
import org.kuali.ole.service.OlePatronService;
import org.kuali.ole.service.OlePatronServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/6/12
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleLoanDocument_IT extends SpringBaseTestCase{

    private OlePatronService olePatronService;
    private OleLoanDefintionHelper oleLoanDefintionHelper;
    private BusinessObjectService businessObjectService;
    private OleCirculationPolicyService oleCirculationPolicyService;
    private final String CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING = "docAction=ingestContent&stringContent=";
    private static final String DOCSTORE_URL = "docstore.url";



    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        olePatronService = new OlePatronServiceImpl();
        oleLoanDefintionHelper = new OleLoanDefintionHelper();
        businessObjectService = KRADServiceLocator.getBusinessObjectService();

        oleCirculationPolicyService = new OleCirculationPolicyServiceImpl();
    }



    public String testCreateNewRecordToDocstore() throws Exception {
        System.setProperty("app.environment", "local");

        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        URL resource = getClass().getResource("ingest_loan.xml");
        File file = new File(resource.toURI());
        String xmlContent = new FileUtil().readFile(file);
        String queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xmlContent);

        Mockito.when(postData(queryString, xmlContent)).thenReturn("response");
        String responseFromDocstore =  postData(queryString, xmlContent);
        assertNotNull(postData(queryString, xmlContent));
        return responseFromDocstore;
    }
    // To-do, Need to be modified this IT based on current loan and return functionality

    @Test
    @Transactional
    public void createLoan() throws Exception{
        /*String responsexml = testCreateNewRecordToDocstore();
        OlePatronDefinition olePatronDefinition = OlePatronDefintionHelper.create();
        OlePatronDefinition patron = olePatronService.createPatron(olePatronDefinition);
        assertNotNull(patron);
        assertNotNull(patron.getOlePatronId());

      // searchPatron(patron);

        List<OleLocation> oleLocationList = new ArrayList<OleLocation>();
        OleLocation oleLocation = new OleLocation();
        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId1());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode1());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName1());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId1());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId1());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId2());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode2());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName2());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId2());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId2());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId3());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode3());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName3());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId3());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId3());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId4());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode4());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName4());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId4());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId4());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        for(int i=0;i<oleLocationList.size();i++){

            businessObjectService.save(oleLocationList.get(i));
        }
        assertNotNull(oleLocation);

        OleLoanStatus oleLoanStatus = new OleLoanStatus();
        oleLoanStatus.setLoanStatusId(oleLoanDefintionHelper.getLoanStatusId());
        oleLoanStatus.setLoanStatusCode(oleLoanDefintionHelper.getLoanStatusCode());
        oleLoanStatus.setLoanStatusName(oleLoanDefintionHelper.getLoanStatusName());
        oleLoanStatus.setVersionNumber(oleLoanDefintionHelper.getLoanStatus_VERSION_NUMBER());
        businessObjectService.save(oleLoanStatus);
        assertNotNull(oleLoanStatus);

        OleLoanTermUnit oleLoanTermUnit = new OleLoanTermUnit();
        oleLoanTermUnit.setLoanTermUnitId(oleLoanDefintionHelper.getLoanTermUnitId());
        oleLoanTermUnit.setLoanTermUnitCode(oleLoanDefintionHelper.getLoanTermUnitCode());
        oleLoanTermUnit.setLoanTermUnitName(oleLoanDefintionHelper.getLoanTermUnitName());
        oleLoanTermUnit.setVersionNumber(oleLoanDefintionHelper.getLoanTermUnit_VERSION_NUMBER());
        businessObjectService.save(oleLoanTermUnit);
        assertNotNull(oleLoanStatus);

        OleCirculationDesk oleCirculationDesk = getCirculationLocation();


        LoanProcessor loanProcessor = new LoanProcessor();
        OleLoanDocument oleLoanDocument = new OleLoanDocument();
        oleLoanDocument.setLoanStatusId(oleLoanStatus.getLoanStatusId());
        oleLoanDocument.setPatronId(patron.getOlePatronId());

        List<OlePatronDocument> proxyPatron = (List<OlePatronDocument>)oleCirculationPolicyService.isProxyPatron(oleLoanDocument.getPatronId());
        if(proxyPatron != null && proxyPatron.size() >0){
          oleLoanDocument.setProxyPatronId(patron.getOleProxyPatronDocuments().get(0).getProxyPatronId());
          oleLoanDocument.setRealPatronBarcode(proxyPatron.get(0).getBarcode());
        }
        oleLoanDocument.setItemId("221117885");
        oleLoanDocument.setLoanPeriodDate(oleLoanDefintionHelper.getLoanPeriodDate());
        oleLoanDocument.setLoanPeriod(oleLoanDefintionHelper.getLoanPeriod());
        oleLoanDocument.setLoanTermUnitId(oleLoanTermUnit.getLoanTermUnitId());
        oleLoanDocument.setLoanDueDate(new Timestamp(oleLoanDefintionHelper.getDueDate().getTime()));
        oleLoanDocument.setLoanOperatorId(EntityHelper.create().getId());
        oleLoanDocument.setLoanApproverId(EntityHelper.create().getId());
        oleLoanDocument.setBorrowerTypeId(OlePatronDefintionHelper.create().getBorrowerType());
        oleLoanDocument.setPatronBarcode(OlePatronDefintionHelper.create().getBarcode());
        OleBorrowerType oleBorrowerType = loanProcessor.getborrowerTypeName(oleLoanDocument.getBorrowerTypeId());
        oleLoanDocument.setBorrowerTypeName(oleBorrowerType!=null && oleBorrowerType.getBorrowerTypeName()!=null?oleBorrowerType.getBorrowerTypeName():null);
        oleLoanDocument.setBorrowerTypeCode(oleBorrowerType != null && oleBorrowerType.getBorrowerTypeCode() != null ? oleBorrowerType.getBorrowerTypeCode() : null);
        oleLoanDocument.setCirculationLocationId(oleCirculationDesk.getLocationId());
        GlobalVariables.setUserSession(new UserSession("admin"));
        oleLoanDocument = loanProcessor.addLoan(oleLoanDocument.getPatronBarcode(),oleLoanDocument.getItemId(),oleLoanDocument);
        OleLoanDocument createLoanDocument = null;
        if(oleLoanDocument.getErrorMessage()== null){
            createLoanDocument = businessObjectService.findBySinglePrimaryKey(OleLoanDocument.class,oleLoanDocument.getLoanId());
            assertNotNull(createLoanDocument);
            //assertNotNull(createLoanDocument.getProxyPatronId());
       } else {
            loanProcessor.saveLoan(oleLoanDocument);
            createLoanDocument = businessObjectService.findBySinglePrimaryKey(OleLoanDocument.class,oleLoanDocument.getLoanId());
            assertNotNull(createLoanDocument);
        }

        OleLoanDocument alterLoanDocument = null;
       if(createLoanDocument!=null){
          alterLoanDocument = alterDueDate(createLoanDocument,loanProcessor);
       }

        OleLoanDocument checkAlterLoanDocument = null;
      //  if(alterLoanDocument.getErrorMessage()== null){
             checkAlterLoanDocument = businessObjectService.findBySinglePrimaryKey(OleLoanDocument.class,alterLoanDocument.getLoanId());
            assertNotNull(checkAlterLoanDocument);
            assertFalse("Test Case for alter due date pass",checkAlterLoanDocument.getLoanDueDate().equals(checkAlterLoanDocument.getPastDueDate()));
        //}
        OleLoanDocument claimsReturnLoanDocument = null;
       //  if(checkAlterLoanDocument != null){
            claimsReturnLoanDocument = claimsReturn(checkAlterLoanDocument,loanProcessor);
       //  }
         OleLoanDocument checkClaimsReturn = null;
      //  if(claimsReturnLoanDocument.getErrorMessage()== null){
            checkClaimsReturn = businessObjectService.findBySinglePrimaryKey(OleLoanDocument.class,claimsReturnLoanDocument.getLoanId());
            assertNotNull(checkClaimsReturn);
            assertFalse("Test Case for alter due date pass",checkClaimsReturn.getClaimsReturnNote().equals(null));
      //  }



        rollbackData(responsexml);  */

    }

    private void searchPatron(OlePatronDefinition olePatronDefinition) throws Exception{
        LoanProcessor loanProcessor = new LoanProcessor();
        OleLoanDocument oleLoanDocument = loanProcessor.getLoanDocument(olePatronDefinition.getBarcode(),null,false,false);
        assertNotNull(oleLoanDocument);
        assertNotNull(oleLoanDocument.getPatronName());
    }

    private OleCirculationDesk getCirculationLocation() {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
        oleCirculationDesk.setCirculationDeskCode("code");
        oleCirculationDesk.setCirculationDeskPublicName("publicName");
        oleCirculationDesk.setCirculationDeskStaffName("staffName");
        oleCirculationDesk.setActive(true);
        oleCirculationDesk.setLocationId("1");
        businessObjectService.save(oleCirculationDesk);
        OleCirculationDesk oleCirculationDeskService = businessObjectService.findBySinglePrimaryKey(OleCirculationDesk.class,oleCirculationDesk.getCirculationDeskId());
        assertEquals("code",oleCirculationDeskService.getCirculationDeskCode());
        assertEquals("publicName",oleCirculationDeskService.getCirculationDeskPublicName());
        return oleCirculationDeskService;
    }


    private OleLoanDocument claimsReturn(OleLoanDocument loanDocument,LoanProcessor loanProcessor) throws Exception{
        List<OleLoanDocument>  claimsReturnList = new ArrayList<OleLoanDocument>();
        List<OleLoanDocument>  loanDocumentList = new ArrayList<OleLoanDocument>();

        loanDocumentList.add(loanDocument);
        claimsReturnList = loanProcessor.setListValues(loanDocumentList,null,true,"Claims return note summary test",false);
        if(claimsReturnList != null && claimsReturnList.size() >0){
            loanProcessor.updateLoan(claimsReturnList,loanDocument.getPatronId(),false,false,null);
        }
        return claimsReturnList.get(0);
    }


    private OleLoanDocument alterDueDate(OleLoanDocument loanDocument,LoanProcessor loanProcessor) throws Exception{

        List<OleLoanDocument>  alterDueDateList = new ArrayList<OleLoanDocument>();
        List<OleLoanDocument>  loanDocumentList = new ArrayList<OleLoanDocument>();
        loanDocument.setCheckNo(true);
        loanDocumentList.add(loanDocument);
        alterDueDateList = loanProcessor.setListValues(loanDocumentList,null,false,null,false);
        if(alterDueDateList!=null && alterDueDateList.size() >0){
            alterDueDateList.get(0).setLoanDueDate(oleCirculationPolicyService.calculateLoanDueDate("10-D"));
            loanProcessor.updateLoan(alterDueDateList,loanDocument.getPatronId(),false,false,null);

        }

        return alterDueDateList.get(0);
    }


    public void rollbackData(String responseFromDocstore) throws Exception {
        System.setProperty("app.environment", "local");

        assertNotNull(responseFromDocstore);

        ResponseHandler responseHandler = new ResponseHandler();
        Response response = responseHandler.toObject(responseFromDocstore);

        RequestHandler requestHandler = new RequestHandler();
        Request request = new Request();
        request.setUser("mock_user");
        request.setOperation("deleteWithLinkedDocs");
        RequestDocument requestDocument = new RequestDocument();
        String bibiUUID = getUUID(response, "bibliographic");
        requestDocument.setId(bibiUUID);
        requestDocument.setCategory("work");
        requestDocument.setType("bibliographic");
        requestDocument.setFormat("marc");
        Content content = new Content();
        content.setContent("");
        requestDocument.setContent(content);
        requestDocument.setLinkedRequestDocuments(Collections.<RequestDocument>emptyList());
        request.setRequestDocuments(Arrays.asList(requestDocument));
        String rollBackXml = requestHandler.toXML(request);



    }

    private String getUUID(Response response, String docType) {
        List<ResponseDocument> documents = response.getDocuments();
        return getUUID(documents, docType);
    }

    private String getUUID(List<ResponseDocument> documents, String docType) {
        String uuid = null;
        for (Iterator<ResponseDocument> iterator = documents.iterator(); iterator.hasNext(); ) {
            ResponseDocument responseDocument = iterator.next();
            if (responseDocument.getType().equals(docType)) {
                uuid = responseDocument.getUuid();
            } else {
                uuid = getUUID(responseDocument.getLinkedDocuments(), docType);
            }
        }
        return uuid;
    }

    // To-do, Need to be modified this IT based on current loan and return functionality

    @Test
    @Transactional
    public void returnLoan() throws Exception{
       /* String responsexml = testCreateNewRecordToDocstore();
        OlePatronDefinition olePatronDefinition = OlePatronDefintionHelper.create();
        OlePatronDefinition patron = olePatronService.createPatron(olePatronDefinition);
        assertNotNull(patron);
        assertNotNull(patron.getOlePatronId());

        List<OleLocation> oleLocationList = new ArrayList<OleLocation>();
        OleLocation oleLocation = new OleLocation();
        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId1());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode1());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName1());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId1());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId1());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId2());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode2());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName2());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId2());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId2());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId3());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode3());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName3());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId3());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId3());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        oleLocation.setLocationId(oleLoanDefintionHelper.getLocationId4());
        oleLocation.setLocationCode(oleLoanDefintionHelper.getLocationCode4());
        oleLocation.setLocationName(oleLoanDefintionHelper.getLocationName4());
        oleLocation.setLevelId(oleLoanDefintionHelper.getLevelId4());
        oleLocation.setParentLocationId(oleLoanDefintionHelper.getParentLocationId4());
        oleLocation.setVersionNumber(oleLoanDefintionHelper.getLocation_VERSION_NUMBER());
        oleLocationList.add(oleLocation);

        for(int i=0;i<oleLocationList.size();i++){

            businessObjectService.save(oleLocationList.get(i));
        }
        assertNotNull(oleLocation);

        OleLoanStatus oleLoanStatus = new OleLoanStatus();
        oleLoanStatus.setLoanStatusId(oleLoanDefintionHelper.getLoanStatusId());
        oleLoanStatus.setLoanStatusCode(oleLoanDefintionHelper.getLoanStatusCode());
        oleLoanStatus.setLoanStatusName(oleLoanDefintionHelper.getLoanStatusName());
        oleLoanStatus.setVersionNumber(oleLoanDefintionHelper.getLoanStatus_VERSION_NUMBER());
        businessObjectService.save(oleLoanStatus);
        assertNotNull(oleLoanStatus);

        OleLoanTermUnit oleLoanTermUnit = new OleLoanTermUnit();
        oleLoanTermUnit.setLoanTermUnitId(oleLoanDefintionHelper.getLoanTermUnitId());
        oleLoanTermUnit.setLoanTermUnitCode(oleLoanDefintionHelper.getLoanTermUnitCode());
        oleLoanTermUnit.setLoanTermUnitName(oleLoanDefintionHelper.getLoanTermUnitName());
        oleLoanTermUnit.setVersionNumber(oleLoanDefintionHelper.getLoanTermUnit_VERSION_NUMBER());
        businessObjectService.save(oleLoanTermUnit);
        assertNotNull(oleLoanTermUnit);

        LoanProcessor loanProcessor = new LoanProcessor();
        OleLoanDocument oleLoanDocument = new OleLoanDocument();
        oleLoanDocument.setLoanStatusId(oleLoanStatus.getLoanStatusId());
        oleLoanDocument.setPatronId(patron.getOlePatronId());
        oleLoanDocument.setItemId("224567886");
        oleLoanDocument.setLoanPeriodDate(oleLoanDefintionHelper.getLoanPeriodDate());
        oleLoanDocument.setLoanPeriod(oleLoanDefintionHelper.getLoanPeriod());
        oleLoanDocument.setLoanTermUnitId(oleLoanTermUnit.getLoanTermUnitId());
        oleLoanDocument.setLoanDueDate(new Timestamp(oleLoanDefintionHelper.getDueDate().getTime()));
        oleLoanDocument.setLoanOperatorId(EntityHelper.create().getId());
        oleLoanDocument.setLoanApproverId(EntityHelper.create().getId());
        oleLoanDocument.setBorrowerTypeId(OlePatronDefintionHelper.create().getBorrowerType());
        oleLoanDocument.setPatronBarcode(OlePatronDefintionHelper.create().getBarcode());
        OleBorrowerType oleBorrowerType = loanProcessor.getborrowerTypeName(oleLoanDocument.getBorrowerTypeId());
        oleLoanDocument.setBorrowerTypeName(oleBorrowerType!=null && oleBorrowerType.getBorrowerTypeName()!=null?oleBorrowerType.getBorrowerTypeName():null);
        oleLoanDocument.setBorrowerTypeCode(oleBorrowerType!=null && oleBorrowerType.getBorrowerTypeCode()!=null?oleBorrowerType.getBorrowerTypeCode():null);
        oleLoanDocument.setCirculationLocationId("1");
        GlobalVariables.setUserSession(new UserSession("admin"));
        loanProcessor.addLoan(oleLoanDocument.getPatronBarcode(),oleLoanDocument.getItemId(),oleLoanDocument);

        OleLoanDocument returnLoanDocument = loanProcessor.getOleLoanDocumentUsingItemBarcode("224567886");
        assertNotNull(returnLoanDocument);

        returnLoanDocument.setCheckInDate(new Timestamp(new Date().getTime()));
        returnLoanDocument.setCirculationLocationId("1");
        returnLoanDocument.setDamagedCheckInOption(false);
        returnLoanDocument = loanProcessor.returnLoan("224567886",returnLoanDocument);
        assertFalse(returnLoanDocument.isContinueCheckIn());
        returnLoanDocument.setContinueCheckIn(true);
        returnLoanDocument.setFineRate(null);
        loanProcessor.returnLoan(returnLoanDocument);
        OleLoanDocument returnedLoanDocument = loanProcessor.getOleLoanDocumentUsingItemBarcode("224567886");
        assertNull(returnedLoanDocument);

        rollbackData(responsexml);
        */
    }

    public  String postData(String target, String content) throws Exception {
        String response = "";
        URL url = new URL(target);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        Writer w = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        w.write(content);
        w.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String temp;
        while ((temp = in.readLine()) != null) {
            response += temp + "\n";
        }
        in.close();
        return response;
    }

}
