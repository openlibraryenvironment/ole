package org.kuali.ole;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.common.XMLFormatter;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.docstore.engine.service.storage.DocstoreRDBMSStorageService;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by jayabharathreddy on 5/6/15.
 */
public class DocstoreProcess_IT extends DocstoreTestCaseBase {
    DocstoreRDBMSStorageService ds = new DocstoreRDBMSStorageService();
    private BusinessObjectService businessObjectService;
    private DocstoreTestHelper docstoreTestHelper = null;

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    @Before
    public void setUp() throws Exception {
        docstoreTestHelper = new DocstoreTestHelper();
    }


    @Test
    public void processBibTrees() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/bibTrees.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }

        int total = 0;
        long startTime = System.currentTimeMillis();
        for (int j = 0; j < 1; j++) {
            BibTrees bibTrees;
            bibTrees = (BibTrees) BibTrees.deserialize(input);
            total=total+bibTrees.getBibTrees().size();
            List<BibRecord> bibRecords = new ArrayList<BibRecord>();
            for (BibTree bibTree : bibTrees.getBibTrees()) {
                BibRecord bibRecord = new BibRecord();
                docstoreTestHelper.createBibRecord(bibTree, bibRecord);
                bibRecords.add(bibRecord);
            }
            getBusinessObjectService().save(bibRecords);
            List<BibRecord> bibRecords1 = new ArrayList<BibRecord>();
            for (int i = 0; i < bibRecords.size(); i++) {
                BibRecord bibRecord = bibRecords.get(i);
                assertNotNull(bibRecord.getBibId());
                assertNotNull(bibRecord.getBibInfoRecord().getBibId());
                assertNotNull(bibRecord.getHoldingsRecords().get(0).getBibId());
                assertNotNull(bibRecord.getHoldingsRecords().get(0).getHoldingsId());
                assertNotNull(bibRecord.getHoldingsRecords().get(0).getItemRecords().get(0).getItemId());
                Bib bib = bibTrees.getBibTrees().get(i).getBib();
                boolean isBibIdFlag = docstoreTestHelper.getBibIdFromBibXMLContent(bibRecord);
                bib.setId(DocumentUniqueIDPrefix.getPrefixedId(bibRecord.getUniqueIdPrefix(), bibRecord.getBibId()));
                bib.setContent(bibRecord.getContent());
                if (isBibIdFlag) {
                    bibRecord.getHoldingsRecords().clear();
                    bibRecords1.add(docstoreTestHelper.modifyDocumentContent(bib, bibRecord));
                }
            }
            getBusinessObjectService().save(bibRecords1);
        }
        long endTime = System.currentTimeMillis();
        long totalTime=endTime - startTime;
       System.out.println("TotalNumer of Trees :: "+total+" in :: "+totalTime+"ms" );
       System.out.println("Total number of records :: "+total*3);
    }

    @Test
    public void retrieveBib() {
        processBibTrees();
        Bib bib = ds.retrieveBib("wbm-0000000027");
        String bibXml = bib.serialize(bib);
        assertNotNull(bibXml);
        System.out.println(XMLFormatter.prettyPrint(bibXml));
    }

    @Test
    public void createBib() {
        String input = "";
        File file = null;
        BibTree bibTree = new BibTree();
        try {
            file = new File(getClass().getResource("/documents/bibdocTree.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }

        bibTree = (BibTree) bibTree.deserialize(input);
        BibRecord bibRecord = new BibRecord();
        docstoreTestHelper.createBibRecord(bibTree, bibRecord);
        getBusinessObjectService().save(bibRecord);

        assertNotNull(bibRecord.getBibId());
        assertNotNull(bibRecord.getBibInfoRecord().getBibId());
        assertNotNull(bibRecord.getHoldingsRecords().get(0).getBibId());
        assertNotNull(bibRecord.getHoldingsRecords().get(0).getHoldingsId());
        assertNotNull(bibRecord.getHoldingsRecords().get(0).getItemRecords().get(0).getItemId());

        Bib bib = bibTree.getBib();
        boolean isBibIdFlag = docstoreTestHelper.getBibIdFromBibXMLContent(bibRecord);
        bib.setId(DocumentUniqueIDPrefix.getPrefixedId(bibRecord.getUniqueIdPrefix(), bibRecord.getBibId()));
        bib.setContent(bibRecord.getContent());
        if (isBibIdFlag) {
            docstoreTestHelper.modifyDocumentContent(bib, bibRecord);
            getBusinessObjectService().save(bibRecord);
        }
    }
}
