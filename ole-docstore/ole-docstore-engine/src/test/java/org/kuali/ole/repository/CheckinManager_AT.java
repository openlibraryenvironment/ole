package org.kuali.ole.repository;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecords;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 3/9/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class CheckinManager_AT
        extends BaseTestCase {
    private static final Logger LOG = LoggerFactory
            .getLogger(CheckinManager_AT.class);
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator
            .getIngestNIndexHandlerService();

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void checkInRecord() throws Exception {
        CheckoutManager checkoutManager = new CheckoutManager();
        NodeHandler nodeHandler = new NodeHandler();
        String author = null;
        String instIdValue = null;
        // input record
        URL resource = getClass().getResource("/org/kuali/ole/repository/request.xml");
        File file = new File(resource.toURI());
        String fileContent = FileUtils.readFileToString(file);

        ResponseDocument responseDocument = new ResponseDocument();
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(fileContent);
        // Ingest & Index input rec
        Response xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        responseDocument = xmlResponse.getDocuments().get(0);
        String uuid = responseDocument.getUuid();
        Session session = RepositoryManager.getRepositoryManager().getSession("ole-khuntley", "batchIngest");
        instIdValue = "6cecc116-a5c6-4240-994a-ba652a8ecde4";
        //content of marc rec
        String checkedOutContent = checkoutManager.checkOut(uuid, "mockUser", "checkout");
        assertNotNull(checkedOutContent);
        //updating marc content
        List<WorkBibMarcRecord> updatedMarcRecs = new ArrayList<WorkBibMarcRecord>();
        WorkBibMarcRecordProcessor workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();
        workBibMarcRecordProcessor.fromXML(checkedOutContent);
        WorkBibMarcRecords workBibMarcRecords = workBibMarcRecordProcessor.fromXML(checkedOutContent);
        for (Iterator<WorkBibMarcRecord> iterator = workBibMarcRecords.getRecords().iterator(); iterator.hasNext(); ) {
            WorkBibMarcRecord rec = iterator.next();
            DataField dataField = rec.getDataFieldForTag("100");
            for (SubField subField : dataField.getSubFields()) {
                author = subField.getValue();
                author = author + "Updated version for author";
                subField.setValue(author);
            }
            updatedMarcRecs.add(rec);
        }
        workBibMarcRecords = new WorkBibMarcRecords();
        workBibMarcRecords.setRecords(updatedMarcRecs);
        workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();
        String updatedContent = workBibMarcRecordProcessor.toXml(workBibMarcRecords);
        updatedContent = updatedContent.replace("list", "collection");
        RequestDocument updatedReqDoc = new RequestDocument();
        buildRequestDoc(updatedReqDoc, responseDocument, updatedContent, instIdValue);
        //checking in record with updated content and links
        CheckinManager checkinManager = new CheckinManager();
        String updatedVersion = checkinManager.updateContent(updatedReqDoc);
        checkedOutContent = checkoutManager.checkOut(uuid, "mockUser", "checkout");
        assertNotNull(checkedOutContent);
        Assert.assertEquals("Content matches with version updated record", checkedOutContent, updatedContent);
        Node bibNode = nodeHandler.getNodeByUUID(session, uuid);
        String instanceIdentifier = bibNode.getProperty("instanceIdentifier").getValue().getString();
       // Assert.assertEquals("Instance Identifier matches Docstore value", instanceIdentifier, instIdValue);
        // getting instance identifier value from Solr
        QueryResponse queryResponse = ServiceLocator.getIndexerService().searchBibRecord(responseDocument.getCategory(),
                responseDocument.getType(),
                responseDocument.getFormat(),
                "id", uuid,
                "instanceIdentifier");
        //LOG.info("queryResponse" + queryResponse);
        List solrInstIdList = new ArrayList();
        solrInstIdList = (List) queryResponse.getResults().get(0).getFieldValue("instanceIdentifier");
        Assert.assertEquals("Instance Identifier matches search result", instIdValue, solrInstIdList.get(0));
    }


    private void buildRequestDoc(RequestDocument updatedReqDoc, ResponseDocument responseDocument,
                                 String updatedContent, String instIdValue) {
        updatedReqDoc.setCategory(responseDocument.getCategory());
        updatedReqDoc.setType(responseDocument.getType());
        updatedReqDoc.setFormat(responseDocument.getFormat());
        updatedReqDoc.setId(responseDocument.getUuid());
        Content updatedCont = new Content();
        updatedCont.setContent(updatedContent);
        updatedReqDoc.setContent(updatedCont);
        AdditionalAttributes addAtt = new AdditionalAttributes();
        addAtt.setDateEntered("12-12-2-2010");
        addAtt.setLastUpdated("12-12-2-2010");
        addAtt.setFastAddFlag("true");
        addAtt.setSupressFromPublic("false");
        updatedReqDoc.setAdditionalAttributes(addAtt);
        updatedReqDoc.setUuid(responseDocument.getUuid());

    }
}
