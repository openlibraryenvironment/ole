package org.kuali.ole.deliver.processor;

import junit.framework.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OleLoanFastAdd;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.ncip.service.impl.OLECirculationHelperServiceImpl;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.UserSession;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hemalathas on 4/21/15.
 */
public class LoanProcessor_IT extends OLETestCaseBase{

    private static final Logger LOG = Logger.getLogger(LoanProcessor_IT.class);

    private String PATRON_BARCODE="6010570002487301";
    private String CHECKOUT_ITEM_BARCODE = "9093";
    private String CHECKIN_ITEM_BARCODE = "8086";
    private String PATRON_ID ="41752075D";
    private String ITEM_BARCODE="345";
    private String OPERATOR_ID="olequickstart";
    private String ALTER_DUE_DATE="2015-04-01"; //yyyy-mm-dd
    private String ALTER_DUE_TIME="04:00"; //24hr format

    @Test
    public void testCheckOut() throws Exception {
        List<OleLoanDocument> loanList = checkOutItem(PATRON_BARCODE, "dev2", CHECKOUT_ITEM_BARCODE, "123", "American History", "Chetan", "BOOK", "B-EDUC/BED-STACKS", null,null, "BL-EDUC");
        for (Iterator<OleLoanDocument> iterator = loanList.iterator(); iterator.hasNext(); ) {
            OleLoanDocument loanDocument = iterator.next();
            Timestamp loanDueDate = loanDocument.getLoanDueDate();
            assertNotNull(loanDueDate);
            assertEquals(loanDocument.getItemStatus(), OLEConstants.ITEM_STATUS_CHECKEDOUT);
            LOG.info("loanDueDate------->" + loanDueDate);
        }
    }

    private List<OleLoanDocument> checkOutItem(String patronBarcode, String operatorId, String itemBarcode, String callNumber, String title, String author, String itemType, String itemLocation, String dateExpires, String requestType, String pickUpLocation)throws Exception{
        LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getBean("loanProcessor");
        //TODO: Data setup
        String itemIdentifier = createItem(patronBarcode,operatorId,itemBarcode,callNumber,title,author,itemType,itemLocation,dateExpires,requestType,pickUpLocation);
        OleLoanForm oleLoanForm = buildLoanForm(itemIdentifier, itemBarcode);
        OleLoanDocument oleLoanDocument = buildLoanDocument();
        //TODO: Method to test
        GlobalVariables.setUserSession(new UserSession("dev2"));
        OleLoanForm loanForm = loanProcessor.processLoan(oleLoanForm, oleLoanDocument);
        //TODO: Assert the expected behaviour
        List<OleLoanDocument> loanList = loanForm.getLoanList();
        return loanList;
    }









    @Test
    public void renewLostItem() throws Exception{
        /*LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getBean("loanProcessor");
        BibliographicRecord bibliographicRecord= getBibliographicRecord(loanProcessor);
        assertNotNull(bibliographicRecord);

        org.kuali.ole.docstore.common.document.content.instance.Item itemRecord=getItem();
        assertNotNull(itemRecord);
        OleHoldings oleHolding = getHoldingRecord(itemRecord);
        assertNotNull(oleHolding);
        businessObjectService.save(asrItem);*/




        LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getBean("loanProcessor");
        Map map=new HashMap<>();
        map.put("itemId",CHECKOUT_ITEM_BARCODE);
        map.put("patronId", PATRON_ID);
        List<OleLoanDocument> oleLoanDocumentList=(List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,map);
        OleLoanForm loanForm = new OleLoanForm();
        //loanForm.setItemUuid("wio-1");
        loanForm.setBorrowerTypeId("7");
        loanForm.setBorrowerType("UnderGrad");
        loanForm.setNonCirculatingFlag(false);
        //loanForm.setInstanceUuid("wio-1");
        loanForm.setLoanList(new ArrayList<OleLoanDocument>());
        loanForm.setPatronName("Salinda Lample");
        loanForm.setPatronBarcode(PATRON_BARCODE);
        loanForm.setBorrowerCode("UGRAD");
        loanForm.setCirculationDesk("1");
        loanForm.setPatronId(PATRON_ID);
        loanForm.setItem(CHECKOUT_ITEM_BARCODE);


        OleLoanDocument oleLoanDocument = new OleLoanDocument();
        oleLoanDocument.setPatronId(PATRON_ID);
        oleLoanDocument.setOperatorsCirculationLocation("B-EDUC/BED-TEACHMAT#B-EDUC/BED-STACKS#");
        oleLoanDocument.setCirculationLocationId("1");
        oleLoanDocument.setBorrowerTypeId("7");
        oleLoanDocument.setBorrowerTypeName("UnderGrad");
        oleLoanDocument.setBorrowerTypeCode("UGRAD");
        oleLoanDocument.setItemUuid(loanForm.getItemUuid());
        if(oleLoanDocumentList!=null && oleLoanDocumentList.size()>0) {
            oleLoanDocument.setLoanId(oleLoanDocumentList.get(0).getLoanId());
            oleLoanDocument.setVersionNumber(oleLoanDocumentList.get(0).getVersionNumber());
            GlobalVariables.setUserSession(new UserSession("dev2"));
            OleLoanForm oleLoanForm = loanProcessor.processLoan(loanForm, oleLoanDocument);
            // oleLoanForm.getItemUuid()
            String noOfRenewalCountStringBefore = oleLoanDocument.getNumberOfRenewals();
            assertNotNull(oleLoanForm.getMessage());
            oleLoanDocument.setIndefiniteCheckFlag(false);
            oleLoanDocument.setRenewNotFlag(false);
            oleLoanDocument.setRenewalItemFlag(true);
            loanProcessor.overrideSaveLoanForRenewal(oleLoanDocument);
            assertEquals(oleLoanDocument.getItemLoanStatus(), "LOANED");
            String noOfRenewalCountStringAfter = oleLoanDocument.getNumberOfRenewals();
            int noOfRenewalCountBefore = Integer.parseInt(noOfRenewalCountStringBefore);
            int noOfRenewalCountAfter = Integer.parseInt(noOfRenewalCountStringAfter);
            assertEquals((noOfRenewalCountBefore + 1), noOfRenewalCountAfter);
        }
    }

    private OleLoanDocument buildLoanDocument(){
        OleLoanDocument oleLoanDocument = new OleLoanDocument();
        oleLoanDocument.setPatronId(PATRON_ID);
        oleLoanDocument.setOperatorsCirculationLocation("B-EDUC/BED-TEACHMAT#B-EDUC/BED-STACKS#");
        oleLoanDocument.setCirculationLocationId("1");
        oleLoanDocument.setBorrowerTypeId("7");
        oleLoanDocument.setBorrowerTypeName("UnderGrad");
        oleLoanDocument.setBorrowerTypeCode("UGRAD");
        return oleLoanDocument;
    }

    private OleLoanForm buildLoanForm(String itemIdentifier,String itemBarcode){
        OleLoanForm loanForm = new OleLoanForm();
        loanForm.setItemUuid(itemIdentifier);
        loanForm.setBorrowerTypeId("7");
        loanForm.setBorrowerType("UnderGrad");
        loanForm.setNonCirculatingFlag(false);
        loanForm.setInstanceUuid(itemIdentifier);
        loanForm.setLoanList(new ArrayList<OleLoanDocument>());
        loanForm.setPatronName("Andrew Wample");
        loanForm.setPatronBarcode(PATRON_BARCODE);
        loanForm.setBorrowerCode("UGRAD");
        loanForm.setCirculationDesk("1");
        loanForm.setPatronId(PATRON_ID);
        loanForm.setItem(itemBarcode);
        return loanForm;
    }

    public String createItem(String patronBarcode, String operatorId, String itemBarcode, String callNumber, String title, String author, String itemType, String itemLocation, String dateExpires, String requestType, String pickUpLocation) throws Exception{
        OLECirculationHelperServiceImpl oleCirculationHelperServiceImpl = new OLECirculationHelperServiceImpl();
        String itemIdentifier = oleCirculationHelperServiceImpl.acceptItem(itemBarcode,callNumber,title,author,itemType,itemLocation,operatorId);
        LOG.info("item--------->"+itemIdentifier);
        return itemIdentifier;
    }

    @Test
    public void testRenewItem(){
        LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getService("loanProcessor");
        Map map=new HashMap<>();
        map.put("itemId",ITEM_BARCODE);
        map.put("patronId", PATRON_ID);
        List<OleLoanDocument> oleLoanDocumentList=(List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,map);
        Map patronMap=new HashMap<>();
        patronMap.put("olePatronId", PATRON_ID);
        OlePatronDocument olePatronDocument=null;
        List<OlePatronDocument> olePatronDocuments=(List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
        if(CollectionUtils.isNotEmpty(olePatronDocuments)){
            olePatronDocument=olePatronDocuments.get(0);
        }
        assertNotNull(olePatronDocument);
        if(CollectionUtils.isNotEmpty(oleLoanDocumentList) && oleLoanDocumentList.size()==1){
            OleLoanDocument oleLoanDocument=oleLoanDocumentList.get(0);
            oleLoanDocument.setRenewalItemFlag(true);
            Timestamp loanDueDate=oleLoanDocument.getLoanDueDate();
            String noOfRenewalCountStringBefore=oleLoanDocument.getNumberOfRenewals();
            oleLoanDocument.setBorrowerTypeCode(olePatronDocument.getBorrowerTypeCode());
            try {
                loanProcessor.addLoan(oleLoanDocument.getPatronBarcode(), oleLoanDocument.getItemId(), oleLoanDocument,  OPERATOR_ID);
                loanProcessor.overrideSaveLoanForRenewal(oleLoanDocument);
            } catch (Exception e){
                e.printStackTrace();
            }
            // if renew is success the due date will change and the no of renewal count should be increased by 1
            Timestamp currentLoanDueDate=oleLoanDocument.getLoanDueDate();
            String noOfRenewalCountStringAfter=oleLoanDocument.getNumberOfRenewals();
            assertNotEquals(currentLoanDueDate, loanDueDate);
            int noOfRenewalCountBefore=Integer.parseInt(noOfRenewalCountStringBefore);
            int noOfRenewalCountAfter=Integer.parseInt(noOfRenewalCountStringAfter);
            assertEquals((noOfRenewalCountBefore+1),noOfRenewalCountAfter);
        }
    }

    @Test
    public  void testAlterDueDate() throws Exception{
        List<OleLoanDocument> alterDueDateList=new ArrayList<OleLoanDocument>();
        LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getService("loanProcessor");
        Map map=new HashMap<>();
        map.put("itemId",ITEM_BARCODE);
        map.put("patronId", PATRON_ID);
        List<OleLoanDocument> oleLoanDocumentList=(List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,map);
        Map patronMap=new HashMap<>();
        patronMap.put("olePatronId", PATRON_ID);
        OlePatronDocument olePatronDocument=null;
        List<OlePatronDocument> olePatronDocuments=(List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
        if(CollectionUtils.isNotEmpty(olePatronDocuments)){
            olePatronDocument=olePatronDocuments.get(0);
        }
        assertNotNull(olePatronDocument);
        if(CollectionUtils.isNotEmpty(oleLoanDocumentList) && oleLoanDocumentList.size()==1){
            OleLoanDocument oleLoanDocument=oleLoanDocumentList.get(0);
            oleLoanDocument.setBorrowerTypeCode(olePatronDocument.getBorrowerTypeCode());
            alterDueDateList.add(oleLoanDocument);
            // <code>GregorianCalendar(year + 1900, month, date)</code>
            // month jan-00,feb-01 ...
            String [] dates=ALTER_DUE_DATE.split("-");
            int year=Integer.parseInt(dates[0]);
            int month=Integer.parseInt(dates[1])-1;
            int date=Integer.parseInt(dates[2]);
            Timestamp expectedDueDate=makeTimestamp(year,month,date,23,59,59,0);
            Timestamp userDueDate=makeTimestamp(year,month,date);
            //Date expectedDate=new Date(year,month,date,hour,min,sec);
            Date userEditDate=new Date(userDueDate.getTime());
            oleLoanDocument.setLoanDueDateToAlter(userEditDate);
            try {
                loanProcessor.updateLoan(alterDueDateList,PATRON_ID,false, false,oleLoanDocument.getBorrowerTypeCode());
            } catch (Exception e){
               e.printStackTrace();
            }
            assertTrue(expectedDueDate.equals(oleLoanDocument.getLoanDueDate()));
        }
    }

    @Test
    public  void testAlterDueDateForIndefinite() throws Exception{
        List<OleLoanDocument> alterDueDateList=new ArrayList<OleLoanDocument>();
        LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getService("loanProcessor");
        Map map=new HashMap<>();
        map.put("itemId",ITEM_BARCODE);
        map.put("patronId", PATRON_ID);
        List<OleLoanDocument> oleLoanDocumentList=(List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,map);
        Map patronMap=new HashMap<>();
        patronMap.put("olePatronId", PATRON_ID);
        OlePatronDocument olePatronDocument=null;
        List<OlePatronDocument> olePatronDocuments=(List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
        if(CollectionUtils.isNotEmpty(olePatronDocuments)){
            olePatronDocument=olePatronDocuments.get(0);
        }
        assertNotNull(olePatronDocument);
        if(CollectionUtils.isNotEmpty(oleLoanDocumentList) && oleLoanDocumentList.size()==1){
            OleLoanDocument oleLoanDocument=oleLoanDocumentList.get(0);
            oleLoanDocument.setBorrowerTypeCode(olePatronDocument.getBorrowerTypeCode());
            alterDueDateList.add(oleLoanDocument);
            // <code>GregorianCalendar(year + 1900, month, date)</code>
            // month jan-00,feb-01 ...
            String [] dates=ALTER_DUE_DATE.split("-");
            int year=Integer.parseInt(dates[0]);
            int month=Integer.parseInt(dates[1])-1;
            int date=Integer.parseInt(dates[2]);
            Timestamp expectedDueDate=null;
            oleLoanDocument.setLoanDueDateToAlter(null);
            try {
                loanProcessor.updateLoan(alterDueDateList,PATRON_ID,false, false,oleLoanDocument.getBorrowerTypeCode());
            } catch (Exception e){
                e.printStackTrace();
            }
            assertTrue(expectedDueDate.equals(oleLoanDocument.getLoanDueDate()));
        }
    }

    @Test
    public  void testAlterDueDateTime(){
        List<OleLoanDocument> alterDueDateList=new ArrayList<OleLoanDocument>();
        LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getService("loanProcessor");
        Map map=new HashMap<>();
        map.put("itemId",ITEM_BARCODE);
        map.put("patronId", PATRON_ID);
        List<OleLoanDocument> oleLoanDocumentList=(List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,map);
        Map patronMap=new HashMap<>();
        patronMap.put("olePatronId", PATRON_ID);
        OlePatronDocument olePatronDocument=null;
        List<OlePatronDocument> olePatronDocuments=(List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
        if(CollectionUtils.isNotEmpty(olePatronDocuments)){
            olePatronDocument=olePatronDocuments.get(0);
        }
        assertNotNull(olePatronDocument);
        if(CollectionUtils.isNotEmpty(oleLoanDocumentList) && oleLoanDocumentList.size()==1){
            OleLoanDocument oleLoanDocument=oleLoanDocumentList.get(0);
            oleLoanDocument.setBorrowerTypeCode(olePatronDocument.getBorrowerTypeCode());
            alterDueDateList.add(oleLoanDocument);
            String [] dates=ALTER_DUE_DATE.split("-");
            String [] times=ALTER_DUE_TIME.split(":");
            int year=Integer.parseInt(dates[0]);
            int month=Integer.parseInt(dates[1])-1;
            int date=Integer.parseInt(dates[2]);
            int hour=Integer.parseInt(times[0]);
            int min=Integer.parseInt(times[1]);
            int sec=0;
            //Date userEditDate=new Date(year,month,date);
            //Date expectedDate=new Date(year,month,date,hour,min);
            Timestamp expectedDueDate=makeTimestamp(year,month,date,hour,min,0,0);
            Timestamp userDueDate=makeTimestamp(year,month,date);
            Date userEditDate=new Date(userDueDate.getTime());
            oleLoanDocument.setLoanDueDateToAlter(userEditDate);
            oleLoanDocument.setLoanDueDateTimeToAlter(ALTER_DUE_TIME);
            try {
                loanProcessor.updateLoan(alterDueDateList,PATRON_ID,false, false,oleLoanDocument.getBorrowerTypeCode());
            } catch (Exception e){
                e.printStackTrace();
            }
            assertTrue(expectedDueDate.equals(oleLoanDocument.getLoanDueDate()));

        }
    }

    public static Timestamp makeTimestamp(int year, int month, int day, int hour, int minute,
                                          int second,int millisecond) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);

        // now convert GregorianCalendar object to Timestamp object
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp makeTimestamp(int year, int month, int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        return new Timestamp(cal.getTimeInMillis());
    }

    @Test
    public void testCheckIn() throws Exception {
        LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getBean("loanProcessor");
        OleLoanDocument oleLoanDocument = new OleLoanDocument();
        DocstoreUtil docstoreUtil = new DocstoreUtil();
        boolean isItemExists = false;
        boolean isLoaned = false;
        List<OleLoanDocument> oleLoanDocumentList = checkOutItem(PATRON_BARCODE, "dev2", CHECKIN_ITEM_BARCODE, "123", "American History", "Chetan", "BOOK", "B-EDUC/BED-STACKS", null, null, "BL-EDUC");
        if (docstoreUtil.isItemAvailableInDocStore(CHECKIN_ITEM_BARCODE)) {
            //TODO : Data Setup
            if (oleLoanDocumentList != null && oleLoanDocumentList.size() > 0) {
                oleLoanDocument = oleLoanDocumentList.get(0);
                Map<String, String> map = new HashMap<>();
                map.put("olePatronId", oleLoanDocument.getPatronId());
                OlePatronDocument olePatronDocument = null;
                olePatronDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, map);
                if (olePatronDocument != null) {
                    oleLoanDocument.setOlePatron(olePatronDocument);
                }
                oleLoanDocument.setCheckOut(false);
                oleLoanDocument.setCirculationLocationId("1");
                oleLoanDocument.setCirculationPolicyId("Check out Circulation Policy Set 1");
                if (oleLoanDocument.getOlePatron() != null) {
                    oleLoanDocument.setBorrowerTypeCode(oleLoanDocument.getOlePatron().getBorrowerTypeCode());
                }
                //TODO : Method to Test
                loanProcessor.returnLoan(CHECKIN_ITEM_BARCODE, oleLoanDocument);
                LOG.info("Item Status After Return : " + oleLoanDocument.getItemStatusCode());
                //TODO : Assert the expected behaviour
                // If the checkIn is success the item loaned record will be deleted from the OleLoanDocument and also the item status will be changed from LOANED
                Map<String, String> criteria = new HashMap<>();
                criteria.put("itemId", CHECKIN_ITEM_BARCODE);
                List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) loanProcessor.getBusinessObjectService().findMatching(OleLoanDocument.class, criteria);
                boolean isEmpty = true;
                if(!oleLoanDocument.getItemStatusCode().equalsIgnoreCase(OLEConstants.ITEM_STATUS_CHECKEDOUT)){
                    if(oleLoanDocuments != null && oleLoanDocuments.size() > 0){
                        loanProcessor.returnLoan(oleLoanDocument);
                    }
                } else if (oleLoanDocuments != null && oleLoanDocuments.size() > 0) {
                    isEmpty = false;
                }
                assertTrue(isEmpty);
                assertNotEquals(oleLoanDocument.getItemStatusCode(), OLEConstants.ITEM_STATUS_CHECKEDOUT);
            } else {
                assertTrue(isLoaned);
            }
        } else {
            assertTrue(isItemExists);
        }
    }
}


