package org.kuali.ole.describe.controller;

import org.apache.struts.mock.MockHttpSession;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.ImportBibSearch;
import org.kuali.ole.describe.bo.ImportBibUserPreferences;
import org.kuali.ole.describe.form.ImportBibForm;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.rice.krad.uif.UifParameters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;


/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 18/12/12
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ImportBibController_IT extends KFSTestCaseBase {

    @Mock
    private BindingResult       mockResult;
    @Mock
    private HttpServletRequest  mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private ModelAndView        mockModelView;
    @Mock
    private ImportBibForm mockForm;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        System.setProperty("app.environment", "local");
    }



    @Test
    @Transactional
    public void testLoad() throws Exception {
        mockForm = new ImportBibForm();
        byte[] fileContent = FileCopyUtils.copyToByteArray(getClass().getResourceAsStream("aaUnicodeBib.mrc"));
        ImportBibSearch importBibSearch = new ImportBibSearch();
        importBibSearch.setLocationFile(new MockMultipartFile("aaUnicodeBib.mrc", "aaUnicodeBib.mrc", "UTF_8", fileContent));
        mockForm.setImportBibSearch(importBibSearch);
        MockImportBibController importBibController = new MockImportBibController();
        Mockito.when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        ModelAndView modelAndView = importBibController.load(mockForm, mockResult, mockRequest, mockResponse);
    }

    @Test
    @Transactional
    public void testClearSearch() throws Exception {
        mockForm = new ImportBibForm();
        ImportBibSearch importBibSearch = new ImportBibSearch();
        SearchParams searchParams = new SearchParams();
        mockForm.setImportBibSearch(importBibSearch);
        mockForm.setSearchParams(searchParams);
        MockImportBibController importBibController = new MockImportBibController();
        ModelAndView modelAndView = importBibController.clearSearch(mockForm, mockResult, mockRequest, mockResponse);
    }


    @Test
    @Transactional
    public void testClearFile() throws Exception {
        mockForm = new ImportBibForm();
        ImportBibSearch importBibSearch = new ImportBibSearch();
        mockForm.setImportBibSearch(importBibSearch);
        MockImportBibController importBibController = new MockImportBibController();
        ModelAndView modelAndView = importBibController.clearFile(mockForm, mockResult, mockRequest, mockResponse);
    }

    @Test
    @Transactional
    public void testActionLink() throws Exception {
        BibMarcRecords marcRecords = getMarcRec();
        Mockito.when(mockForm.getActionParamaterValue((UifParameters.SELECTED_LINE_INDEX))).thenReturn("1");
        Mockito.when(mockForm.getBibMarcRecordList()).thenReturn(marcRecords.getRecords());
        MockImportBibController importBibController = new MockImportBibController();
        ModelAndView modelAndView = importBibController.actionLink(mockForm, mockResult, mockRequest, mockResponse);
    }

    @Test
    @Transactional
    public void testlocalNext() throws Exception {
        mockForm = new ImportBibForm();
        MockImportBibController importBibController = new MockImportBibController();
        byte[] fileContent = FileCopyUtils.copyToByteArray(getClass().getResourceAsStream("aaUnicodeBib.mrc"));
        ImportBibSearch importBibSearch = new ImportBibSearch();
        importBibSearch.setLocationFile(new MockMultipartFile("aaUnicodeBib.mrc", "aaUnicodeBib.mrc", "UTF_8", fileContent));
        mockForm.setImportBibSearch(importBibSearch);
        Mockito.when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        importBibController.load(mockForm, mockResult, mockRequest, mockResponse);
        ModelAndView modelAndView = importBibController.next(mockForm, mockResult, mockRequest, mockResponse);
    }
    @Ignore
    @Test
    @Transactional
    public void testUserPrefLoad() throws Exception {
        mockForm = new ImportBibForm();
        BibMarcRecords marcRecords = getMarcRec();
        ImportBibUserPreferences importBibUserPreferences = new ImportBibUserPreferences();
        setUserPref(importBibUserPreferences);
        mockForm.setImportBibUserPreferences(importBibUserPreferences);
        mockForm.setNewBibMarcRecord(marcRecords.getRecords().get(0));
        mockForm.setExistingBibMarcRecord(marcRecords.getRecords().get(1));
        MockImportBibController importBibController = new MockImportBibController();
        Mockito.when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        ModelAndView modelAndView = importBibController.loadUserPref(mockForm, mockResult, mockRequest, mockResponse);
    }

    // To-do, Need to be modified this IT based on current Import Bib functionality
    @Test
    @Transactional
    public void testUserPrefNewImport() throws Exception {
        /*mockForm = new ImportBibForm();
        BibMarcRecords marcRecords = getMarcRec();
        ImportBibUserPreferences importBibUserPreferences = new ImportBibUserPreferences();
        setUserPref(importBibUserPreferences);
        mockForm.setImportBibUserPreferences(importBibUserPreferences);
        mockForm.setNewBibMarcRecord(marcRecords.getRecords().get(0));
        mockForm.setExistingBibMarcRecord(marcRecords.getRecords().get(1));
        Mockito.when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        MockImportBibController importBibController = new MockImportBibController();
//        Mockito.when(mockGlobalVariables.getUserSession()).thenReturn(new UserSession("user"));
//        Mockito.when(GlobalVariables.getUserSession().getLoggedInUserPrincipalName()).thenReturn("user");
        ModelAndView modelAndView = importBibController.userPrefNext(mockForm, mockResult, mockRequest, mockResponse);
        WorkBibMarcRecord marcRec = mockForm.getNewBibMarcRecord();
        for(DataField df :  marcRec.getDataFields()){
            assertNotSame("This field is in Admin removal tags and not available in marc rec", "049", df.getTag());
            assertNotSame("This field is in user removal tags and not available in marc rec", "035", df.getTag());
            assertNotNull("This field is in admin Protected tags and should be available in marc rec", "040".equalsIgnoreCase(df.getTag()));
            assertNotNull("This field is in user Protected tags and should be available in marc rec", "012".equalsIgnoreCase(df.getTag()));
        }*/
    }


    // To-do, Need to be modified this IT based on current Import Bib functionality
    @Test
    @Transactional
    public void testUserPrefOverLayImport() throws Exception {
        /*mockForm = new ImportBibForm();
        BibMarcRecords marcRecords = getMarcRec();
        ImportBibUserPreferences importBibUserPreferences = new ImportBibUserPreferences();
        setUserPref(importBibUserPreferences);
        importBibUserPreferences.setImportType("overLay");
        mockForm.setImportBibUserPreferences(importBibUserPreferences);
        mockForm.setNewBibMarcRecord(marcRecords.getRecords().get(0));
        mockForm.setExistingBibMarcRecord(marcRecords.getRecords().get(1));
        mockForm.setUuid("1234");
        Mockito.when(mockRequest.getSession()).thenReturn( new MockHttpSession());
        MockImportBibController importBibController = new MockImportBibController();
        ModelAndView modelAndView = importBibController.userPrefNext(mockForm, mockResult, mockRequest, mockResponse);
        WorkBibMarcRecord marcRec = mockForm.getNewBibMarcRecord();
        for(DataField df :  marcRec.getDataFields()){
            assertNotSame("This field is in Admin removal tags and should not be available in marc rec", "049", df.getTag());
            assertNotSame("This field is in user removal tags and should not be available in marc rec" , "035", df.getTag());
            assertNotNull("This field is in admin Protected tags and should be available in marc rec", "040".equalsIgnoreCase(df.getTag()));
            assertNotNull("This field is in user Protected tags and should be available in marc rec", "012".equalsIgnoreCase(df.getTag()));
            if(df.getTag().equalsIgnoreCase("040")){
                for(int i=0; i<=df.getSubFields().size(); i++){
                   assertEquals( df.getSubFields().get(0).getValue(), "CIN");
                   assertEquals( df.getSubFields().get(1).getValue(), "m.c.");
                   assertEquals( df.getSubFields().get(2).getValue(), "OCL");
                }
            }
        }*/
    }


    // To-do, Need to be modified this IT based on current Import Bib functionality
    @Test
    @Transactional
    public void testUserPrefOverLayMatchPointImport() throws Exception {
       /* mockForm = new ImportBibForm();
        BibMarcRecords marcRecords = getMarcRec();
        ImportBibUserPreferences importBibUserPreferences = new ImportBibUserPreferences();
        setUserPref(importBibUserPreferences);
        importBibUserPreferences.setImportType("overLayMatchPoint");
        mockForm.setImportBibUserPreferences(importBibUserPreferences);
        mockForm.setNewBibMarcRecord(marcRecords.getRecords().get(0));
        Mockito.when(mockRequest.getSession()).thenReturn(new MockHttpSession());
        mockForm.setImportBibConfirmReplace(new ImportBibConfirmReplace());
        MockImportBibController importBibController = new MockImportBibController();
        ModelAndView modelAndView = importBibController.userPrefNext(mockForm, mockResult, mockRequest, mockResponse);
        WorkBibMarcRecord marcRec = mockForm.getNewBibMarcRecord();
        for(DataField df :  marcRec.getDataFields()){
            assertNotSame("This field is in Admin removal tags and not available in marc rec", "049", df.getTag());
            assertNotSame("This field is in user removal tags and not available in marc rec" , "035", df.getTag());
            assertNotNull("This field is in admin Protected tags and should be available in marc rec", "040".equalsIgnoreCase(df.getTag()));
            assertNotNull("This field is in user Protected tags and should be available in marc rec", "012".equalsIgnoreCase(df.getTag()));
        }*/
    }
    // To-do, Need to be modified this IT based on current Import Bib functionality
    @Test
    @Transactional
    public void testUserPrefTagsValidation() throws Exception {
        /*mockForm = new ImportBibForm();
        BibMarcRecords marcRecords = getMarcRec();
        ImportBibUserPreferences importBibUserPreferences = new ImportBibUserPreferences();
        setUserPref(importBibUserPreferences);
        importBibUserPreferences.setImportType("newImport");
        importBibUserPreferences.setRemovalTags("070,035");
        mockForm.setImportBibUserPreferences(importBibUserPreferences);
        mockForm.setNewBibMarcRecord(marcRecords.getRecords().get(0));
        mockForm.setUuid(marcRecords.getRecords().get(0).getControlFields().get(0).getValue());
        Mockito.when(mockRequest.getSession()).thenReturn( new MockHttpSession());
        MockImportBibController importBibController = new MockImportBibController();
        ModelAndView modelAndView = importBibController.userPrefNext(mockForm, mockResult, mockRequest, mockResponse);
        WorkBibMarcRecord marcRec = mockForm.getNewBibMarcRecord();
        for(DataField df :  marcRec.getDataFields()){
            assertNotSame("This field is in Admin removal tags and not available in marc rec", "049", df.getTag());
            assertNotSame("This field is in user removal tags and not available in marc rec" , "035", df.getTag());
            assertNotNull("This field is in admin Protected tags and should be available in marc rec", "040".equalsIgnoreCase(df.getTag()));
            assertNotNull("This field is in user Protected tags and should be available in marc rec", "012".equalsIgnoreCase(df.getTag()));
        }*/
    }

    @Test
    @Transactional
    public void testUserPrefCallNumValidation() throws Exception {
        mockForm = new ImportBibForm();
        BibMarcRecords marcRecords = getMarcRec();
        ImportBibUserPreferences importBibUserPreferences = new ImportBibUserPreferences();
        setUserPref(importBibUserPreferences);
        importBibUserPreferences.setImportType("newImport");
        importBibUserPreferences.setCallNumberSource2("020a");
        mockForm.setImportBibUserPreferences(importBibUserPreferences);
        mockForm.setNewBibMarcRecord(marcRecords.getRecords().get(0));
        Mockito.when(mockRequest.getSession()).thenReturn( new MockHttpSession());
        MockImportBibController importBibController = new MockImportBibController();
        ModelAndView modelAndView = importBibController.userPrefNext(mockForm, mockResult, mockRequest, mockResponse);
    }

    @Test
    @Transactional
    public void testLoadImportBibSearch() throws Exception {
        mockForm = new ImportBibForm();
        BibMarcRecords marcRecords = getMarcRec();
        ImportBibUserPreferences importBibUserPreferences = new ImportBibUserPreferences();
        setUserPref(importBibUserPreferences);
        importBibUserPreferences.setImportType("newImport");
        importBibUserPreferences.setCallNumberSource2("020a");
        mockForm.setImportBibUserPreferences(importBibUserPreferences);
        mockForm.setNewBibMarcRecord(marcRecords.getRecords().get(0));
        mockForm.setUuid("123456");
        Mockito.when(mockRequest.getSession()).thenReturn( new MockHttpSession());
        MockImportBibController importBibController = new MockImportBibController();
        ModelAndView modelAndView = importBibController.userPrefNext(mockForm, mockResult, mockRequest, mockResponse);
    }





    private BibMarcRecords getMarcRec() throws Exception {
        URL resource = getClass().getResource("sample-marc-mrc.xml");
        File file = new File(resource.toURI());
        String xmlContent = new FileUtil().readFile(file);
        BibMarcRecordProcessor workBibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords marcRecords = workBibMarcRecordProcessor.fromXML(xmlContent);
        return marcRecords;
    }

    private void setUserPref(ImportBibUserPreferences importBibUserPreferences) {
        importBibUserPreferences.setPrefId(1);
        importBibUserPreferences.setPrefId1("1");
        importBibUserPreferences.setPrefName("adminImport");
        importBibUserPreferences.setImportType("newImport");
        importBibUserPreferences.setPermLocation("B-MUSIC/BMUCIRCDESK");
        importBibUserPreferences.setTempLocation("B-SWAIN/BSWCIRCDESK");
        importBibUserPreferences.setAdminRemovalTags("030,045,049");
        importBibUserPreferences.setRemovalTags("070,035");
        importBibUserPreferences.setAdminProtectedTags("040,650");
        importBibUserPreferences.setProtectedTags("651,998,012");
        importBibUserPreferences.setShelvingScheme("LCC");
        importBibUserPreferences.setCallNumberSource1("050");
        importBibUserPreferences.setCallNumberSource2("020");
        importBibUserPreferences.setCallNumberSource3("300");
        importBibUserPreferences.setImportStatus("None");
    }


    private class MockImportBibController
            extends ImportBibController {
        @Override
        protected ModelAndView callSuper(BindingResult result, HttpServletRequest request, HttpServletResponse response,
                                         ImportBibForm importBibForm) {
            return mockModelView;
        }
    }
}