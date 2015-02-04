package org.kuali.ole.docstore.document.rdbms;


import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.document.AbstractDocumentManager;
import org.kuali.ole.docstore.document.DocumentManager;

import org.kuali.ole.docstore.model.rdbms.bo.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.OleUuidCheckWebService;
import org.kuali.ole.docstore.service.OleWebServiceProvider;
import org.kuali.ole.docstore.service.impl.OleWebServiceProviderImpl;
import org.kuali.ole.pojo.OleException;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.jcr.RepositoryException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/26/13
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RdbmsAbstarctDocumentManager extends AbstractDocumentManager {

    private BusinessObjectService businessObjectService;

    @Override
    public List<ResponseDocument> ingest(List<RequestDocument> requestDocuments, Object object) throws OleDocStoreException {

        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            ResponseDocument responseDocument = new ResponseDocument();
            this.ingest(requestDocument, object, responseDocument);
            responseDocuments.add(responseDocument);
        }
        return responseDocuments;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    @Override
    public ResponseDocument ingest(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException {

        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        storeDocument(requestDocument, object, respDoc);
        storeLinkDocuments(requestDocument, object, respDoc);

        return respDoc;
    }


    public void storeLinkDocuments(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException {

        List<ResponseDocument> linkedResponseDocumentList = new ArrayList<ResponseDocument>();
        if (respDoc.getLinkedDocuments() != null && respDoc.getLinkedDocuments().size() > 0) {
            linkedResponseDocumentList.addAll(respDoc.getLinkedDocuments());
        }
        respDoc.setLinkedDocuments(linkedResponseDocumentList);
        for (RequestDocument linkedRequestDocument : requestDocument.getLinkedRequestDocuments()) {
            ResponseDocument linkedResponseDocument = new ResponseDocument();
            linkedResponseDocument.setUuid(respDoc.getUuid());
            linkedResponseDocumentList.add(linkedResponseDocument);
            DocumentManager documentManager = BeanLocator.getDocstoreFactory()
                    .getDocumentManager(linkedRequestDocument.getCategory(), linkedRequestDocument.getType(), linkedRequestDocument.getFormat());
            documentManager.addResourceId(linkedRequestDocument, respDoc);
            String createdBy = requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.CREATED_BY);
            String createdDate = requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.DATE_ENTERED);
            if (linkedRequestDocument != null && linkedRequestDocument.getAdditionalAttributes() != null) {
                linkedRequestDocument.getAdditionalAttributes().setAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY, createdBy);
                linkedRequestDocument.getAdditionalAttributes().setAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED, createdDate);
            } else {
                AdditionalAttributes additionalAttributes = new AdditionalAttributes();
                additionalAttributes.setAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY, createdBy);
                additionalAttributes.setAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED, createdDate);
                linkedRequestDocument.setAdditionalAttributes(additionalAttributes);
            }

            //linkedRequestDocument.setAdditionalAttributes(additionalAttributes);

            documentManager.storeDocument(linkedRequestDocument, object, linkedResponseDocument);
        }
    }

    @Override
    public List<ResponseDocument> delete(List<RequestDocument> requestDocuments, Object object) throws Exception {
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            ResponseDocument responseDocument = this.delete(requestDocument, object);
            responseDocuments.add(responseDocument);
        }
        return responseDocuments;
    }

    @Override
    public ResponseDocument delete(RequestDocument requestDocument, Object object) throws Exception {
        ResponseDocument responseDocument = new ResponseDocument();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        responseDocument = deleteVerify(requestDocument , object);
        if("success".equals(responseDocument.getStatus())) {
            deleteDocs(requestDocument, object);
        }
        return responseDocument;
    }


    public abstract void deleteDocs(RequestDocument requestDocument, Object object);

    @Override
    public List<ResponseDocument> checkout(List<RequestDocument> requestDocuments, Object object) throws OleDocStoreException {
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            ResponseDocument responseDocument = new ResponseDocument();
            this.checkout(requestDocument, object);
            responseDocuments.add(responseDocument);
        }
        return responseDocuments;
    }

    @Override
    public ResponseDocument checkout(RequestDocument requestDocument, Object object) throws OleDocStoreException {

        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        ResponseDocument respDoc = checkoutContent(requestDocument, object);

        return respDoc;
    }

    public abstract ResponseDocument checkoutContent(RequestDocument requestDocument, Object object);

    @Override
    public List<ResponseDocument> checkin(List<RequestDocument> requestDocuments, Object object) throws OleDocStoreException {
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            ResponseDocument responseDocument = new ResponseDocument();
            this.checkin(requestDocument, object, responseDocument);
            responseDocuments.add(responseDocument);
        }
        return responseDocuments;
    }

    @Override
    public ResponseDocument checkin(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException {

        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        if (requestDocument.getId() != null || requestDocument.getId().trim().length() > 0) {
            requestDocument.setUuid(requestDocument.getId());
        }
        checkInContent(requestDocument, object, respDoc);
//        AdditionalAttributes attributes = requestDocument.getAdditionalAttributes();
//        respDoc.setAdditionalAttributes(attributes);
        return respDoc;
    }

    public abstract void checkInContent(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException;


    @Override
    public ResponseDocument buildResponseDocument(RequestDocument requestDocument) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void bulkIngest(BulkProcessRequest bulkProcessRequest, List<RequestDocument> requestDocuments) throws OleDocStoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void index(List<RequestDocument> requestDocuments, boolean commit) throws OleDocStoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseDocument bind(RequestDocument requestDocument, Object object, String operation) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        return new RdbmsWorkInstanceDocumentManager().bind(requestDocument, object, operation);
    }

    @Override
    public ResponseDocument unbind(RequestDocument requestDocument, Object object, String operation) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocument, Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected boolean checkInstancesOrItemsExistsInOLE(String instanceIdentifier, Object object) throws Exception {
        String uuidsNotInOle = null;
        List<String> instanceOrItemIdentifiersList = new ArrayList<String>();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        Map instanceMap = new HashMap();
        instanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(instanceIdentifier));
        InstanceRecord oleInstanceRecord = getBusinessObjectService().findByPrimaryKey(InstanceRecord.class, instanceMap);
        List<ItemRecord> oleItemRecords = null;
        if (oleInstanceRecord != null && oleInstanceRecord.getItemRecords() != null) {
            oleItemRecords = oleInstanceRecord.getItemRecords();

            for (ItemRecord oleItemRecord : oleItemRecords) {
                instanceOrItemIdentifiersList.add(oleItemRecord.getItemId());
            }
        }
        instanceOrItemIdentifiersList.add(instanceIdentifier);
        return checkInstancesOrItemsExistsInOLE(instanceOrItemIdentifiersList);
    }

    protected boolean checkInstancesOrItemsExistsInOLE(List<String> uuidsList) {
        String uuidsNotInOle = null;
        String serviceURL = ConfigContext.getCurrentContextConfig().getProperty("uuidCheckServiceURL");
        OleWebServiceProvider oleWebServiceProvider = new OleWebServiceProviderImpl();
        OleUuidCheckWebService oleUuidCheckWebService = (OleUuidCheckWebService) oleWebServiceProvider
                .getService("org.kuali.ole.docstore.service.OleUuidCheckWebService", "oleUuidCheckWebService",
                        serviceURL);
        StringBuilder uuidsSB = new StringBuilder();
        for (String uuid : uuidsList) {
            //uuidsSB.append(requestDocument.getId()).append(",");
            uuidsSB.append(uuid).append(",");
        }
        uuidsNotInOle = oleUuidCheckWebService.checkUuidExsistence(uuidsSB.substring(0, uuidsSB.length() - 1));
        String[] uuids = StringUtils.split(uuidsNotInOle, ",");
        if (uuids.length == uuidsList.size()) {
            return false;
        } else {
            return true;
        }
    }
}
