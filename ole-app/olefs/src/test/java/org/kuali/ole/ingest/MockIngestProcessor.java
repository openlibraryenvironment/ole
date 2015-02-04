package org.kuali.ole.ingest;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleOrderRecordHandler;
import org.kuali.ole.OleOrderRecords;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.converter.OLEEDIConverter;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/27/12
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockIngestProcessor extends AbstractIngestProcessor {

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    @Override
    public String preProcessMarc(String marcFileContent, OLEBatchProcessJobDetailsBo job) {
        String marcXMLContent = null;
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        marcXMLContent = marcXMLConverter.convert(marcFileContent);

        //TODO: hack to get rid of the extra xmlns entry. Not sure why the second entry gets generated when calling marc4J in ole-docstore-utility.
        //TODO: the duplicate entry does not get genereated if its run directly in the ole-docstore-utilty project.
        String modifiedXMLContent =
                marcXMLContent.
                        replace("collection xmlns=\"http://www.loc.gov/MARC21/slim\" xmlns=\"http://www.loc.gov/MARC21/slim",
                                "collection xmlns=\"http://www.loc.gov/MARC21/slim");
        return modifiedXMLContent;
    }

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

    @Override
    public void postProcess(OLEBatchProcessJobDetailsBo job) {
        OleOrderRecords oleOrderRecords = null;
        try {
            oleOrderRecords = new OleOrderRecords();
            List<EngineResults> engineResults = getEngineResults();
            List<OleOrderRecord> oleOrderRecordList = new ArrayList();
            for (Iterator<EngineResults> iterator = engineResults.iterator(); iterator.hasNext(); ) {
                EngineResults results = iterator.next();
                OleOrderRecord oleOrderRecord = (OleOrderRecord) results.getAttribute(OLEConstants.OLE_ORDER_RECORD);
                oleOrderRecordList.add(oleOrderRecord);
            }
            oleOrderRecords.setRecords(oleOrderRecordList);
            OleOrderRecordHandler oleEditorResponseHandler = new OleOrderRecordHandler();
            String oleOrderRecordXml = oleEditorResponseHandler.toXML(oleOrderRecords);
            /*OleWebServiceProvider oleWebServiceProvider = GlobalResourceLoader.getService(OLEConstants.OLE_WEB_SERVICE_PROVIDER);
            OleExposedWebService oleExposedWebService =
                    (OleExposedWebService) oleWebServiceProvider.
                            getService("org.kuali.ole.describe.service.OleExposedWebService", "oleExposedWebService", getURL());
            oleExposedWebService.createReqAndPO(oleOrderRecordXml);*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //String xmlForRollback = buildRequestForRollback(oleOrderRecords);
            /*List<String> bibIds = new ArrayList<>();
            for (Iterator<OleOrderRecord> iterator = oleOrderRecords.getRecords().iterator(); iterator.hasNext(); ) {
                OleOrderRecord oleOrderRecord = iterator.next();
                OleBibRecord oleBibRecord = oleOrderRecord.getOleBibRecord();
                bibIds.add(oleBibRecord.getBibUUID());

            }*/
            //MockDocstoreHelperService docstoreHelperService = GlobalResourceLoader.getService(OLEConstants.DOCSTORE_HELPER_SERVICE);
            try {
               /* if (bibIds.size() > 0) {
                    getDocstoreClientLocator().getDocstoreClient().deleteBibs(bibIds);
                }*/
                //docstoreHelperService.rollbackData(xmlForRollback);
            } catch (Exception e1) {
                System.out.println(e1.getMessage());
            }
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
            /*OleBibRecord oleBibRecord = oleOrderRecord.getOleBibRecord();
            String bibUUID = oleBibRecord.getBibUUID();*/

            RequestHandler requestHandler = new RequestHandler();
            Request request = new Request();
            request.setOperation("deleteWithLinkedDocs");
            RequestDocument requestDocument = new RequestDocument();
            //requestDocument.setId(bibUUID);
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
