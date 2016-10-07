package org.kuali.ole.gobi.processor;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.helper.BatchBibImportHelper;
import org.kuali.ole.batch.impl.BatchProcessBibImportServiceImpl;
import org.kuali.ole.batch.ingest.BatchProcessBibImport;
import org.kuali.ole.batch.service.BatchProcessBibImportService;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.OrderBibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class GobiBibRecordProcessorTest extends OLETestCaseBase {


    @Mock
    private OLEBatchProcessJobDetailsBo mockOLEOleBatchProcessJobDetailsBo;

    @Mock
    private OLEBatchProcessProfileBo mockOleBatchProcessProfileBo;

    @Mock
    private OLEBatchProcessDefinitionDocument mockOleBatchProcessDefinitionDocument;

    @Mock
    private OLEBatchProcessJobDetailsBo mockOleBatchProcessJobDetailsBo;
    @Mock
    private BatchProcessBibImport mockBatchProcessBibImport;

    MarcXMLConverter marcXMLConverter = new MarcXMLConverter();

    BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();

    @Mock
    private MatchingProfile mockMatchingProfile;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createBibFromOrder() throws Exception {
        stubRecordToOLEBatchProcessJobDetailsBo();
        stubRecordToOLEBatchProcessDefinitionDocument();
        Mockito.when(mockMatchingProfile.isNoMatchBibs_addBibs()).thenReturn(true);

        Mockito.when(mockOleBatchProcessProfileBo.getMatchingProfileObj()).thenReturn(mockMatchingProfile);
        Mockito.when(mockOleBatchProcessProfileBo.getDataToImport()).thenReturn(OLEConstants.OLEBatchProcess.DATA_TO_IMPORT_BIB_INSTANCE);


        OLEBatchBibImportStatistics bibImportStatistics = new OLEBatchBibImportStatistics();
//        BatchProcessBibImport batchProcessBibImport = new BatchProcessBibImport();
        BibMarcRecords bibMarcRecords = getBibMarcRecords();
        OLEBatchBibImportDataObjects oleBatchBibImportDataObjects = new OLEBatchBibImportDataObjects();
        List<BibMarcRecord> records = bibMarcRecords.getRecords();

        BatchBibImportHelper batchBibImportHelper = new BatchBibImportHelper();
        bibImportStatistics.setTotalCount(records.size());
        List<OrderBibMarcRecord> orderBibMarcRecords = new ArrayList<>();
        for(BibMarcRecord record:records){
            OrderBibMarcRecord orderBibMarcRecord = new OrderBibMarcRecord();
            orderBibMarcRecord.setBibMarcRecord(record);
            orderBibMarcRecords.add(orderBibMarcRecord);
        }
        assertTrue(CollectionUtils.isNotEmpty(orderBibMarcRecords));
        oleBatchBibImportDataObjects = batchBibImportHelper.processOrderBatch(orderBibMarcRecords, mockOleBatchProcessProfileBo, bibImportStatistics,"ole-quickstart");

        BatchProcessBibImportService batchProcessBibImportService = (BatchProcessBibImportServiceImpl) GlobalResourceLoader.getService("batchProcessBibImportServiceImpl");
        List<OrderBibMarcRecord> orderBibMarcRecordList = batchProcessBibImportService.saveOderBatch(orderBibMarcRecords, oleBatchBibImportDataObjects, bibImportStatistics);
        assertNotNull(orderBibMarcRecords);
        assertTrue(CollectionUtils.isNotEmpty(orderBibMarcRecordList));
    }

    private void stubRecordToOLEBatchProcessDefinitionDocument() {

    }

    private void stubRecordToOLEBatchProcessJobDetailsBo() {
        mockOleBatchProcessJobDetailsBo.setBatchProfileName("YBP-profileName");
    }

    public String readFileContent(String path) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader(getFilePath(path)));
        String line=null;
        String fullContent = "";
        while ((line=br.readLine())!=null)
        {
            fullContent += line;
        }
        return fullContent;
    }

    public String getFilePath(String classpathRelativePath)  {
        try {
            Resource rsrc = new ClassPathResource(classpathRelativePath);
            return rsrc.getFile().getAbsolutePath();
        } catch(Exception e){
            System.out.println("Error : while accessing file "+e);
        }
        return null;
    }

    public BibMarcRecords getBibMarcRecords() throws IOException {
        String marcXMLContent = marcXMLConverter.convert(readFileContent("org/kuali/ole/describe/controller/validMRCFileWith4Record.mrc"));
        marcXMLContent = marcXMLContent.replace(
                "collection xmlns=\"http://www.loc.gov/MARC21/slim\" xmlns=\"http://www.loc.gov/MARC21/slim",
                "collection xmlns=\"http://www.loc.gov/MARC21/slim");
        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(marcXMLContent);
        return bibMarcRecords;
    }



}