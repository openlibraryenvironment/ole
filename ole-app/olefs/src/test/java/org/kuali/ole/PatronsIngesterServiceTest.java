package org.kuali.ole;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.OlePatronXMLSchemaValidator;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.rice.core.api.impex.xml.FileXmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 7/29/15.
 */
public class PatronsIngesterServiceTest {

    @Mock
    private FileUtil mockFileUtil;

    @Mock
    private File mockFile;

    @Mock
    private OlePatronXMLSchemaValidator mockOlePatronXMLSchemaValidator;

    @Mock
    private OlePatronIngestSummaryRecord mockOlePatronIngestSummaryRecord;

    private OlePatronConverterService mockOlePatronConverterService;

    @Mock
    ByteArrayInputStream mockByteArrayInputStream;

    @Before
    public void setUp() throws Exception {
        mockOlePatronConverterService = new MockOlePatronConverterService();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void ingest() throws Exception {
       PatronsIngesterService patronsIngesterService = new MockPatronsIngesterService();
        Mockito.when(mockFile.getName()).thenReturn("test-file");
        String mockContent = "mockContent";
        Mockito.when(mockFileUtil.readFile(mockFile)).thenReturn(mockContent);
        //Mockito.when(mockOlePatronXMLSchemaValidator.validateContentsAgainstSchema(mockByteArrayInputStream)).thenReturn(true);
        Map validateResultMap = new HashMap();
        validateResultMap.put(OLEConstants.OlePatron.PATRON_XML_ISVALID, true);
        validateResultMap.put(OLEConstants.OlePatron.PATRON_POLLERSERVICE_ERROR_MESSAGE,null);
        Mockito.when(mockOlePatronXMLSchemaValidator.validateContentsAgainstSchema(mockByteArrayInputStream)).thenReturn(validateResultMap);
        ArrayList<OlePatronDocument> successOlePatronDocuments = new ArrayList<>();

        patronsIngesterService.setFileUtil(mockFileUtil);
        patronsIngesterService.setOlePatronConverterService(mockOlePatronConverterService);
        patronsIngesterService.setOlePatronXMLSchemaValidator(mockOlePatronXMLSchemaValidator);

        ArrayList<XmlDocCollection> xmlDocCollections = new ArrayList<>();
        FileXmlDocCollection fileXmlDocCollection = null; //new OleFileXmlDocCollection(mockFile);
        xmlDocCollections.add(fileXmlDocCollection);

        patronsIngesterService.ingest(xmlDocCollections);
    }

    class MockPatronsIngesterService extends PatronsIngesterService {
        @Override
        protected ByteArrayInputStream getByteArrayInputStream(String fileContent) {
            return mockByteArrayInputStream;
        }

        @Override
        protected OlePatronIngestSummaryRecord getOlePatronIngestSummaryRecord() {
            return mockOlePatronIngestSummaryRecord;
        }
    }

    class MockOlePatronConverterService extends  OlePatronConverterService {
        private OlePatronIngestSummaryRecord olePatronIngestSummaryRecord;

        @Override
        public List<OlePatronDocument> persistPatronFromFileContent(String fileContent, boolean addUnMatchedPatronFlag, String fileName, OlePatronIngestSummaryRecord olePatronIngestSummaryRecord, String addressSource, String principalName) throws IOException, URISyntaxException {
            this.olePatronIngestSummaryRecord = olePatronIngestSummaryRecord;
            olePatronIngestSummaryRecord.setFailureRecords("filed records");
            List<OlePatronDocument> olePatronDocuments = new ArrayList<>();
            olePatronDocuments.add(new OlePatronDocument());
            return olePatronDocuments;
        }
    }

}
