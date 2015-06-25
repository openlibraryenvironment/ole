package org.kuali.ole.deliver;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.bo.OleLoanFastAdd;
import org.kuali.ole.deliver.processor.LoanProcessor;

import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/5/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */

@Transactional
public class OleDeliveryDocstoreService_IT extends KFSTestCaseBase {


   /* private DocstoreHelperService docstoreHelperService;

    @Mock
    private DocstoreHelperService mockDocstoreHelperService;*/

    private LoanProcessor loanProcessor;
    private String mockResponse;

    @After
    public void tearDown() throws Exception {
        //docstoreHelperService = null;
        loanProcessor = null;
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setUp();
       // docstoreHelperService = new DocstoreHelperService();
        loanProcessor = new LoanProcessor();
        GlobalVariables.setUserSession(new UserSession("admin"));
        GlobalVariables.getUserSession().getPrincipalId();
    }

    @Test
    @Transactional
    public void getOleItemSearchList() throws Exception {

        mockResponse = FileUtils.readFileToString(new File(getClass().getResource("sampleIngestResponse.xml").toURI()));

        BibliographicRecord bibliographicRecord=getBibliographicRecord();
        assertNotNull(bibliographicRecord);
        Item itemRecord=getItem();
        assertNotNull(itemRecord);
        OleHoldings oleHolding = getHoldingRecord(itemRecord);
        assertNotNull(oleHolding);
        //String result = docstoreHelperService.persistNewToDocstoreForIngest(bibliographicRecord, itemRecord,oleHolding);

       /* Mockito.when(mockDocstoreHelperService.persistNewToDocstoreForIngest(bibliographicRecord, itemRecord,oleHolding)).thenReturn(mockResponse);
        assertNotNull(mockDocstoreHelperService.persistNewToDocstoreForIngest(bibliographicRecord, itemRecord, oleHolding));*/

        Map searchCriteria = new HashMap();
        searchCriteria.put("itemBarCode","123456789");
        searchCriteria.put("title","Asd");
        searchCriteria.put("author","");
        searchCriteria.put("publisher","");
        searchCriteria.put("callNumber","");
        searchCriteria.put("itemType","");
        assertNotNull(searchCriteria);
        
       // List results = docstoreHelperService.getOleItemSearchList(searchCriteria);

        List<OleItemSearch> oleItemSearchList = new ArrayList<>();
        OleItemSearch oleItemSearch = new OleItemSearch();
        oleItemSearch.setAuthor("mockAuthor");
        oleItemSearch.setCopyNumber("c1");
        oleItemSearch.setItemStatus("Available");

        OleItemSearch oleItemSearch1 = new OleItemSearch();
        oleItemSearch1.setAuthor("mockAuthor1");
        oleItemSearch1.setItemType("book");
        oleItemSearch1.setItemStatus("InHold");

        oleItemSearchList.add(oleItemSearch);
        oleItemSearchList.add(oleItemSearch1);
        /*Mockito.when(mockDocstoreHelperService.getOleItemSearchList(searchCriteria)).thenReturn(oleItemSearchList);
        assertNotNull(mockDocstoreHelperService.getOleItemSearchList(searchCriteria));*/
    }

    @Test
    @Transactional
    public void persistNewToDocstoreForIngest() throws Exception{
        mockResponse = FileUtils.readFileToString(new File(getClass().getResource("sampleIngestResponse.xml").toURI()));

        BibliographicRecord bibliographicRecord=getBibliographicRecord();
        assertNotNull(bibliographicRecord);
        Item itemRecord=getItem();
        assertNotNull(itemRecord);
        OleHoldings oleHolding = getHoldingRecord(itemRecord);
        assertNotNull(oleHolding);
        //String result = docstoreHelperService.persistNewToDocstoreForIngest(bibliographicRecord, itemRecord,oleHolding);
       /* Mockito.when(mockDocstoreHelperService.persistNewToDocstoreForIngest(bibliographicRecord, itemRecord,oleHolding)).thenReturn(mockResponse);
        assertNotNull(mockDocstoreHelperService.persistNewToDocstoreForIngest(bibliographicRecord, itemRecord,oleHolding));*/
       // assertNotNull(result);
    }

    public Item getItem(){

        OleLoanFastAdd oleLoanFastAdd = new OleLoanFastAdd();
        oleLoanFastAdd.setCirculationLocation("Fort Wayne Library");
        oleLoanFastAdd.setShelvingLocation("Stacks");
        oleLoanFastAdd.setItemType("BOOK");
        oleLoanFastAdd.setCallNumber("123456");
        oleLoanFastAdd.setCallNumberType("Test");
        oleLoanFastAdd.setCopyNumber("54321");
        oleLoanFastAdd.setEnumeration("2345678");
        oleLoanFastAdd.setCheckinNote("Test Check Note");
        oleLoanFastAdd.setNote("Test Note");
        oleLoanFastAdd.setBarcode("123456789");
        oleLoanFastAdd.setItemStatus("Available");
        oleLoanFastAdd.setCopyNumber("44444444");

        assertNotNull(oleLoanFastAdd);

        Item itemRecord = loanProcessor.getItemRecord(oleLoanFastAdd);
        return itemRecord;
    }

    public OleHoldings getHoldingRecord(Item itemRecord){
        OleHoldings oleHoldings = new OleHoldings();
        oleHoldings.setCallNumber(itemRecord.getCallNumber());
        oleHoldings.setLocation(itemRecord.getLocation());
        oleHoldings.setPrimary("true");
        oleHoldings.setExtension(itemRecord.getExtension());
        return oleHoldings;
    }

   public BibliographicRecord getBibliographicRecord(){

       String title = "The Legend";
       String author="William";
       assertNotNull(title);
       BibliographicRecord bibliographicRecord = loanProcessor.getBibliographicRecord(title,author);

       return bibliographicRecord;

   }
}
