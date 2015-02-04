package org.kuali.ole.ingest.action;

import org.apache.log4j.Logger;
import org.kuali.ole.describe.service.MockDiscoveryHelperService;
import org.kuali.ole.describe.service.MockDocstoreHelperService;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockCreateBibAction implements Action {
    private MockDocstoreHelperService docstoreHelperService;
    private MockDiscoveryHelperService discoveryHelperService;

    private static final Logger LOG = Logger.getLogger(MockCreateBibAction.class);

    /**
     *  This method takes the initial request when creating the BibAction.
     * @param executionEnvironment
     */
    @Override
    public void execute(ExecutionEnvironment executionEnvironment) {
        LOG.info(" Called CreateBibAction ---------------> ");
        /*DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        List<ProfileAttributeBo> profileAttributes = (List<ProfileAttributeBo>) dataCarrierService.getData(OLEConstants.PROFILE_ATTRIBUTE_LIST);
        BibliographicRecord bibliographicRecord = (BibliographicRecord) dataCarrierService.getData(OLEConstants.REQUEST_BIB_RECORD);

        DocstoreHelperService docstoreHelperService = new DocstoreHelperService();
        try {
            String responseFromDocstore = docstoreHelperService.persistNewToDocstoreForIngest(bibliographicRecord, profileAttributes);
            ResponseHandler responseHander = new ResponseHandler();
            Response response = responseHander.toObject(responseFromDocstore);
            String instanceUUID = getUUID(response, OLEConstants.INSTANCE_DOC_TYPE);

            List bibInfo = getDiscoveryHelperService().getBibInformationFromInsatnceId(instanceUUID);

            OleBibRecord oleBibRecord = new OleBibRecord();
            Map<String, List> bibFieldValues = (Map<String, List>) bibInfo.get(0);
            oleBibRecord.setBibAssociatedFieldsValueMap(bibFieldValues);
            oleBibRecord.setLinkedInstanceId(instanceUUID);
            oleBibRecord.setBibUUID(getUUID(response, OLEConstants.BIB_DOC_TYPE));
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.OLE_BIB_RECORD, oleBibRecord);
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.BIB_CREATION_FLAG, true);
        } catch (Exception e) {
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.OLE_BIB_RECORD, null);
        }*/
    }

    /**
     *   This method simulate the executionEnvironment.
     * @param executionEnvironment
     */

    @Override
    public void executeSimulation(ExecutionEnvironment executionEnvironment) {
        execute(executionEnvironment);
    }

    /**
     *      This method gets the uuid based on docType
     * @param response
     * @param docType
     * @return  uuid
     */
    private String getUUID(Response response, String docType) {
        List<ResponseDocument> documents = response.getDocuments();
        return getUUID(documents, docType);
    }

    /**
     *  This method gets the uuid based on List of documents and docType.
     * @param documents
     * @param docType
     * @return   uuid
     */
    private String getUUID(List<ResponseDocument> documents, String docType) {
        for (Iterator<ResponseDocument> iterator = documents.iterator(); iterator.hasNext(); ) {
            ResponseDocument responseDocument = iterator.next();
            if (responseDocument.getType().equals(docType)) {
                return responseDocument.getUuid();
            } else {
                return getUUID(responseDocument.getLinkedDocuments(), docType);
            }
        }
        return null;
    }

    public MockDocstoreHelperService getDocstoreHelperService() {
        return docstoreHelperService;
    }

    public void setDocstoreHelperService(MockDocstoreHelperService docstoreHelperService) {
        this.docstoreHelperService = docstoreHelperService;
    }

    public MockDiscoveryHelperService getDiscoveryHelperService() {
        return discoveryHelperService;
    }

    public void setDiscoveryHelperService(MockDiscoveryHelperService discoveryHelperService) {
        this.discoveryHelperService = discoveryHelperService;
    }
}
