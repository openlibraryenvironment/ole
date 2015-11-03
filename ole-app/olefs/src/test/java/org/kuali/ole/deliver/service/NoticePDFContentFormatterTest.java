package org.kuali.ole.deliver.service;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by premkb on 4/8/15.
 */
public class NoticePDFContentFormatterTest {

    @Mock
    private OlePatronHelperServiceImpl mockOlePatronHelperServiceImpl;

    @Mock
    private LoanProcessor mockLoanProcessor;
    @Mock
    private BusinessObjectService mockBusinessObjectService;
    @Mock
    private ParameterValueResolver mockParameterResolverInstance;
    @Mock
    NoticesExecutor mockDeliverNoticesExecutor;
    @Mock
    private NoticePDFContentFormatter mockNoticePDFContentFormatter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateOverDueNoticeContent() throws Exception {
        OleLoanDocument oleLoanDocument = new OleLoanDocument();
        //oleLoanDocument.setItemId("113");
        mockNoticePDFContentFormatter = new MockNoticePDFContentFormatter();

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEParameterConstants.OVERDUE_TITLE)).thenReturn("TITLE");
        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT))
                .thenReturn
                        ("CONTENT");
        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.DEFAULT_CIRCULATION_DESK))
                .thenReturn
                        ("BL_EDUC");
        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.NOTICE_FROM_MAIL))
                .thenReturn
                        ("dev.ole@kuali.org");
        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,
                "OVERDUE_NOTICE_TO_DATE"))
                .thenReturn
                        ("dd/MM/yyyy hh:mm a;");
        List<OleLoanDocument> loanDocuments = new ArrayList<>();
        Map overdueMap = new HashMap();
        overdueMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, "Overdue");
        overdueMap.put(OLEConstants.LOAN_DOCUMENTS, loanDocuments);
        mockDeliverNoticesExecutor = new OverdueNoticesExecutor(overdueMap);

        OlePatronDocument olePatron = new OlePatronDocument();
        olePatron.setBarcode("123125");
        EntityBo entity = new EntityBo();
        ArrayList<EntityNameBo> entityNameBos = new ArrayList<EntityNameBo>();
        EntityNameBo entityNameBo = new EntityNameBo();
        entityNameBo.setFirstName("FirstName");
        entityNameBos.add(entityNameBo);
        entity.setNames(entityNameBos);
        ArrayList<EntityTypeContactInfoBo> entityTypeContactInfos = new ArrayList<EntityTypeContactInfoBo>();
        entityTypeContactInfos.add(new EntityTypeContactInfoBo());
        entity.setEntityTypeContactInfos(entityTypeContactInfos);
        olePatron.setEntity(entity);

        oleLoanDocument.setOlePatron(olePatron);


        mockNoticePDFContentFormatter.setOlePatronHelperService(mockOlePatronHelperServiceImpl);
        mockNoticePDFContentFormatter.setSimpleDateFormat(new SimpleDateFormat());
        mockNoticePDFContentFormatter.setUrlProperty("http://localhost:8080/olefs");
        mockNoticePDFContentFormatter.setParameterResolverInstance(mockParameterResolverInstance);


        String fileName = mockNoticePDFContentFormatter.generateOverDueNoticeContent(oleLoanDocument);
        assertNotNull(fileName);
        boolean isFileExist= FileUtils.fileExists(fileName);
        assertTrue(isFileExist);
    }

    private class MockNoticePDFContentFormatter extends NoticePDFContentFormatter {
        @Override
        public String createPDFFile(String title, String itemId) throws Exception {
            String pdfLocationSystemParam=System.getProperty("user.home")+"/kuali/main/"+System.getProperty("environment")+"/olefs-webapp/work/staging/";
            boolean locationExist = new File(pdfLocationSystemParam).exists();
            boolean fileCreated = false;
            try {
                if (!locationExist) {
                    fileCreated = new File(pdfLocationSystemParam).mkdirs();
                    if (!fileCreated) {
                        throw new RuntimeException("Unable to create directory :" + pdfLocationSystemParam);
                    }
                }
            } catch (Exception e) {
                throw e;
            }

            if (title == null || title.trim().isEmpty()) {
                title = OLEConstants.ITEM_TITLE;
            }
            title = title.replaceAll(" ", "_");

            if (itemId == null || itemId.trim().isEmpty()) {
                itemId = OLEConstants.ITEM_ID;
            }
            String fileName = pdfLocationSystemParam + title + "_" + itemId + "_" + (new Date(System.currentTimeMillis())).toString().replaceAll(":", "_") + ".pdf";
            return fileName;
        }
    }
}
