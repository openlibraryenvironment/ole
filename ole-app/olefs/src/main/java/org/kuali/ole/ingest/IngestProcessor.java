package org.kuali.ole.ingest;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleOrderRecordHandler;
import org.kuali.ole.OleOrderRecords;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OrderImportHelperBo;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.converter.OLEEDIConverter;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.service.OleExposedWebService;
import org.kuali.ole.select.service.impl.OleExposedWebServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.*;

/**
 * IngestProcessor converts the marcFileContent in to marcXmlContent,ediFileContent in to ediXMLContent and also
 * creates Requisition and Purchase Order based on oleOrderRecordXml
 */
public class IngestProcessor extends AbstractIngestProcessor {

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }
    //DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);

    /**
     * This method modify the marcFileContent in to marcXmlContent.
     *
     * @param marcFileContent
     * @return modifiedXMLContent.
     */
    @Override
    public String preProcessMarc(String marcFileContent, OLEBatchProcessJobDetailsBo job) {
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        String marcXMLContent = null;
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        try {
            marcXMLContent = marcXMLConverter.convert(marcFileContent);
        }
        catch(Exception ex){
            ex.getMessage();
            List<String> reasonForFailure = new ArrayList<>();
            reasonForFailure.add("Unable to parse the marc file. Allowed format is UTF-8");
            reasonForFailure.add("======================================================");
            reasonForFailure.add(ex.getMessage());
            //dataCarrierService.addData(OLEConstants.FAILURE_REASON,reasonForFailure);
            orderImportHelperBo.setFailureReason(reasonForFailure);
        }
        //TODO: hack to get rid of the extra xmlns entry. Not sure why the second entry gets generated when calling marc4J in ole-docstore-utility.
        //TODO: the duplicate entry does not get genereated if its run directly in the ole-docstore-utilty project.
        String modifiedXMLContent =
                marcXMLContent.
                        replace("collection xmlns=\"http://www.loc.gov/MARC21/slim\" xmlns=\"http://www.loc.gov/MARC21/slim",
                                "collection xmlns=\"http://www.loc.gov/MARC21/slim");
        return modifiedXMLContent;
    }

    /**
     * This method converts the ediFileContent in to ediXMLContent.
     *
     * @param ediFileContent
     * @return ediXMLContent.
     */
    @Override
    public String preProcessEDI(String ediFileContent) {
        String ediXMLContent = null;
        OLEEDIConverter oleEDIConverter = new OLEEDIConverter();
        try {
            ediXMLContent = oleEDIConverter.convertToXML(ediFileContent);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        }
        return ediXMLContent;
    }

    /**
     * This method creates Requisition and Purchase Order based on oleOrderRecordXml.
     * The oleExposedWebService will call the createReqAndPO method in oleRice1 and creates the requisition and PO.
     */
    @Override
    public void postProcess(OLEBatchProcessJobDetailsBo job) {
        OleOrderRecords oleOrderRecords = new OleOrderRecords();
        try {
            List<OleOrderRecord> oleOrderRecordList = getOleOrderRecordList();
            oleOrderRecords.setRecords(oleOrderRecordList);
            OleOrderRecordHandler oleEditorResponseHandler = new OleOrderRecordHandler();
            String oleOrderRecordXml = oleEditorResponseHandler.toXML(oleOrderRecords);
            OleExposedWebServiceImpl oleExposedWebService = (OleExposedWebServiceImpl) SpringContext.getBean(OleExposedWebService.class);
            oleExposedWebService.createReqAndPO(oleOrderRecordXml, job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method builds the request to Rollback the LinkedDocs for failure transactions.
     *
     * @param oleOrderRecords
     * @return xml
     */
    private String buildRequestForRollback(OleOrderRecords oleOrderRecords) {
        String xml = null;
        for (Iterator<OleOrderRecord> iterator = oleOrderRecords.getRecords().iterator(); iterator.hasNext(); ) {
            OleOrderRecord oleOrderRecord = iterator.next();
            OleBibRecord oleBibRecord = oleOrderRecord.getOleBibRecord();
            String bibUUID = oleBibRecord.getBibUUID();

            RequestHandler requestHandler = new RequestHandler();
            Request request = new Request();
            request.setOperation("deleteWithLinkedDocs");
            RequestDocument requestDocument = new RequestDocument();
            requestDocument.setId(bibUUID);
            request.setRequestDocuments(Arrays.asList(requestDocument));
            xml = requestHandler.toXML(request);
        }
        return xml;
    }

    /**
     * Gets the oleExposedWebService url from PropertyUtil.
     *
     * @return url.
     */
    public String getURL() {
        String url = ConfigContext.getCurrentContextConfig().getProperty("oleExposedWebService.url");
        return url;
    }

}
