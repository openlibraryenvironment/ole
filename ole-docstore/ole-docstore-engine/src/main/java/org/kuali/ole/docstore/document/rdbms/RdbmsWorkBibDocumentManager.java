package org.kuali.ole.docstore.document.rdbms;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.utility.Constants;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements the DocumentManager interface for [Work-Bib-*] documents.
 *
 * @version %I%, %G%
 * @author: tirumalesh.b
 * Date: 31/8/12 Time: 7:04 PM
 */
@Deprecated
public class RdbmsWorkBibDocumentManager extends RdbmsAbstarctDocumentManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static RdbmsWorkBibDocumentManager ourInstance = null;

    public static RdbmsWorkBibDocumentManager getInstance() {
        if (null == ourInstance) {
            ourInstance = new RdbmsWorkBibDocumentManager();
        }
        return ourInstance;
    }


    public Node storeDocument(RequestDocument requestDocument, Object object, ResponseDocument respDoc) {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        BibRecord bibRecord = new BibRecord();

        modifyAdditionalAttributes(requestDocument);
        AdditionalAttributes attributes = requestDocument.getAdditionalAttributes();
        bibRecord.setContent(requestDocument.getContent().getContent());
        boolean isBibIdFlag = getBibIdFromBibXMLContent(bibRecord);
        if (attributes != null) {
            /*DateFormat df = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");
            Date dateCreated = null;
            try {
                dateCreated = df.parse(attributes.getAttribute(AdditionalAttributes.DATE_ENTERED));
                Date dateEntered = df.parse(attributes.getAttribute(AdditionalAttributes.DATE_ENTERED));
            Date dateStatusUpdated = df.parse(attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON));
            bibRecord.setDateCreated(dateCreated);
            bibRecord.setDateEntered(dateEntered);
            bibRecord.setStatusUpdatedDate(dateStatusUpdated);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }*/
            bibRecord.setCreatedBy(attributes.getAttribute(AdditionalAttributes.CREATED_BY));
            bibRecord.setStatusUpdatedBy(attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_BY));
            bibRecord.setDateCreated(Timestamp.valueOf(attributes.getAttribute(AdditionalAttributes.DATE_ENTERED)));
            //bibRecord.setDateEntered(Timestamp.valueOf(attributes.getAttribute(AdditionalAttributes.DATE_ENTERED)));
            if (StringUtils.isNotEmpty(attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON))) {
                bibRecord.setStatusUpdatedDate(Timestamp.valueOf(attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON)));
            }
            bibRecord.setFassAddFlag(Boolean.valueOf(attributes.getAttribute(AdditionalAttributes.FAST_ADD_FLAG)));
            bibRecord.setFormerId("");
            bibRecord.setSuppressFromPublic(attributes.getAttribute(AdditionalAttributes.SUPRESS_FROM_PUBLIC));
            bibRecord.setStatus(attributes.getAttribute(AdditionalAttributes.STATUS));

            bibRecord.setStaffOnlyFlag(Boolean.valueOf(attributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG)));
        }
        bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.getPrefix(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat()));
        businessObjectService.save(bibRecord);
        String uuid = DocumentUniqueIDPrefix.getPrefixedId(bibRecord.getUniqueIdPrefix(), bibRecord.getBibId());
        if (isBibIdFlag) {
            modifyDocumentContent(requestDocument, bibRecord.getBibId(), businessObjectService);
        }
        requestDocument.setUuid(uuid);
        buildResponseDocument(requestDocument, bibRecord, respDoc);
        return null;
    }


    public ResponseDocument buildResponseDocument(RequestDocument requestDocument, BibRecord bibRecord, ResponseDocument responseDocument) {
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setStatus("Success");
        responseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(bibRecord.getUniqueIdPrefix(), bibRecord.getBibId()));
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setAttribute(AdditionalAttributes.DATE_ENTERED,bibRecord.getDateEntered() != null ? bibRecord.getDateEntered().toString() : "");
        additionalAttributes.setAttribute(AdditionalAttributes.STATUS, bibRecord.getStatus());
        additionalAttributes.setAttribute(AdditionalAttributes.STATUS_UPDATED_ON, "");
        additionalAttributes.setAttribute(AdditionalAttributes.CREATED_BY, "");
        additionalAttributes.setAttribute(AdditionalAttributes.LAST_UPDATED, "");
        if(bibRecord.getFassAddFlag() != null) {
            additionalAttributes.setAttribute(AdditionalAttributes.FAST_ADD_FLAG, bibRecord.getFassAddFlag().toString());
        }
        additionalAttributes.setAttribute(AdditionalAttributes.STATUS, bibRecord.getStatus());
        if (bibRecord.getStaffOnlyFlag() != null) {
            additionalAttributes.setAttribute(AdditionalAttributes.STAFFONLYFLAG, bibRecord.getStaffOnlyFlag().toString());
        } else {
            additionalAttributes.setAttribute(AdditionalAttributes.STAFFONLYFLAG, "false");
        }
        additionalAttributes
                .setAttribute(AdditionalAttributes.SUPRESS_FROM_PUBLIC, bibRecord.getSuppressFromPublic());
        responseDocument.setAdditionalAttributes(additionalAttributes);
        return responseDocument;
    }


    @Override
    public void deleteDocs(RequestDocument requestDocument, Object object) {
        ResponseDocument responseDocument = new ResponseDocument();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        BibRecord bibRecord = new BibRecord();
        Map map = new HashMap();
        map.put("bibId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<BibRecord> bibRecords = (List<BibRecord>) businessObjectService.findMatching(BibRecord.class, map);
        if (bibRecords != null && bibRecords.size() > 0) {
            bibRecord = bibRecords.get(0);
          //  if (bibRecord.getInstanceRecords() != null && bibRecord.getInstanceRecords().size() > 0) {
                InstanceRecord instanceRecord = new InstanceRecord();
                List<InstanceRecord> instanceRecords = null;//(List<InstanceRecord>) bibRecord.getInstanceRecords();
                if (instanceRecords != null && instanceRecords.size() > 0) {
                    for (int i = 0; i < instanceRecords.size(); i++) {
                        instanceRecord = instanceRecords.get(i);
                        if (instanceRecord.getHoldingsRecords() != null && instanceRecord.getHoldingsRecords().size() > 0) {
                            HoldingsRecord holdingsRecord = instanceRecord.getHoldingsRecords().get(0);

                            if (holdingsRecord.getExtentOfOwnerShipRecords() != null && holdingsRecord.getExtentOfOwnerShipRecords().size() > 0) {
                                List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords = holdingsRecord.getExtentOfOwnerShipRecords();
                                for (int j = 0; j < extentOfOwnerShipRecords.size(); j++) {
                                    List<ExtentNoteRecord> extentNoteRecords = extentOfOwnerShipRecords.get(j).getExtentNoteRecords();
                                    if (extentNoteRecords != null && extentNoteRecords.size() > 0) {
                                        businessObjectService.delete(extentNoteRecords);
                                    }
                                }
                                businessObjectService.delete(extentOfOwnerShipRecords);
                            }
                            if (holdingsRecord.getHoldingsNoteRecords() != null && holdingsRecord.getHoldingsNoteRecords().size() > 0) {
                                List<HoldingsNoteRecord> holdingsNoteRecords = holdingsRecord.getHoldingsNoteRecords();
                                businessObjectService.delete(holdingsNoteRecords);
                            }

                            /*if (holdingsRecord.getAccessUriRecords() != null && holdingsRecord.getAccessUriRecords().size() > 0) {
                                List<AccessUriRecord> accessUriRecords = holdingsRecord.getAccessUriRecords();
                                businessObjectService.delete(accessUriRecords);
                            }*/
                            holdingsRecord.setCallNumberTypeId(null);
                            holdingsRecord.setReceiptStatusId(null);
                            businessObjectService.delete(holdingsRecord);
                        }
                        List<ItemRecord> itemRecords = instanceRecord.getItemRecords();
                        for (ItemRecord itemRecord : itemRecords) {
                            if (itemRecord.getFormerIdentifierRecords() != null && itemRecord.getFormerIdentifierRecords().size() > 0) {
                                List<FormerIdentifierRecord> formerIdentifierRecords = itemRecord.getFormerIdentifierRecords();
                                businessObjectService.delete(formerIdentifierRecords);
                            }
                            if (itemRecord.getItemNoteRecords() != null && itemRecord.getItemNoteRecords().size() > 0) {
                                List<ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
                                businessObjectService.delete(itemNoteRecords);
                            }
                            if (itemRecord.getLocationsCheckinCountRecords() != null && itemRecord.getLocationsCheckinCountRecords().size() > 0) {
                                List<LocationsCheckinCountRecord> locationsCheckinCountRecords = itemRecord.getLocationsCheckinCountRecords();
                                businessObjectService.delete(locationsCheckinCountRecords);
                            }
                            itemRecord.setItemStatusId(null);
                            itemRecord.setItemTypeId(null);
                            itemRecord.setTempItemTypeId(null);
                            itemRecord.setStatisticalSearchId(null);
                            businessObjectService.delete(itemRecord);

                        }
                        businessObjectService.delete(instanceRecord);
                    }
                }
           // }
        }
        businessObjectService.delete(bibRecord);
        //requestDocument.setUuid(bibRecord.getBibId());
        buildResponseDocument(requestDocument, bibRecord, responseDocument);
    }

    /**
     * Updating BibStatus fields based on ingest/checking operation is invoked
     *
     * @param requestDocument
     */
    protected void modifyAdditionalAttributes(RequestDocument requestDocument) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = requestDocument.getUser();
        String statusFromReqDoc = "";
        String statusFromNode = "";
        AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
        if (additionalAttributes == null) {
            additionalAttributes = new AdditionalAttributes();
        }
        if (requestDocument.getOperation() != null) {
            if (requestDocument.getOperation().equalsIgnoreCase(Request.Operation.ingest.toString())) {
                statusFromReqDoc = additionalAttributes.getAttribute(AdditionalAttributes.STATUS);
                additionalAttributes.setAttribute(AdditionalAttributes.DATE_ENTERED, dateStr);
                additionalAttributes.setAttribute(AdditionalAttributes.CREATED_BY, user);
                //Add statusUpdatedBy and statusUpdatedOn if input request is having non empty status field
                if (StringUtils.isNotEmpty(statusFromReqDoc)) {
                    additionalAttributes.setAttribute(AdditionalAttributes.STATUS_UPDATED_BY, user);
                    additionalAttributes.setAttribute(AdditionalAttributes.STATUS_UPDATED_ON, dateStr);
                }
            } else if (requestDocument.getOperation().equalsIgnoreCase(Request.Operation.checkIn.toString())) {
                if (requestDocument.getAdditionalAttributes() != null) {
                    statusFromReqDoc = additionalAttributes.getAttribute(Constants.STATUS);
                }
                HashMap<String, String> bibMap = new HashMap<String, String>();
                bibMap.put("bibId", DocumentUniqueIDPrefix.getDocumentId(DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid())));
                List<BibRecord> bibRecordList = (List<BibRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(BibRecord.class, bibMap);
                if (bibRecordList.size() > 0) {
                    BibRecord bibRecord = bibRecordList.get(0);
                    String status = bibRecord.getStatus();
                    if (status == null) {
                        status = "";
                    }

                    if (status != null && !status.equals(statusFromReqDoc)) {
                        additionalAttributes.setAttribute(Constants.STATUS_UPDATED_BY, user);
                        additionalAttributes.setAttribute(Constants.STATUS_UPDATED_ON, dateStr);
                    }

                }
                additionalAttributes.setAttribute(Constants.UPDATED_BY, user);
                additionalAttributes.setAttribute(Constants.DATE_ENTERED, dateStr);
            }
            requestDocument.setAdditionalAttributes(additionalAttributes);
        }
    }


    @Override
    public ResponseDocument checkoutContent(RequestDocument requestDocument, Object object) {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateStr = sdf.format(date);
        ResponseDocument respDoc = new ResponseDocument();
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("bibId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<BibRecord> bibRecords = (List<BibRecord>) businessObjectService.findMatching(BibRecord.class, parentCriteria1);
        if (bibRecords != null && bibRecords.size() > 0) {
            BibRecord bibRecord = bibRecords.get(0);
            String content = bibRecord.getContent();
            AdditionalAttributes additionalAttributes = new AdditionalAttributes();
            if (bibRecord.getFassAddFlag() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.FAST_ADD_FLAG, bibRecord.getFassAddFlag().toString());
            }
            if (bibRecord.getDateEntered() != null && !"".equals(bibRecord.getDateEntered())) {
                additionalAttributes.setAttribute(AdditionalAttributes.DATE_ENTERED, bibRecord.getDateEntered().toString());
            }
            if (bibRecord.getStatus() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.STATUS, bibRecord.getStatus());
            }
            if (bibRecord.getStatusUpdatedDate() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.STATUS_UPDATED_ON, bibRecord.getStatusUpdatedDate().toString());
            }
            if (bibRecord.getSuppressFromPublic() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.SUPRESS_FROM_PUBLIC, bibRecord.getSuppressFromPublic());
            }
            if (bibRecord.getStaffOnlyFlag() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.STAFFONLYFLAG, bibRecord.getStaffOnlyFlag().toString());
            }
            if (bibRecord.getCreatedBy() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.CREATED_BY, bibRecord.getCreatedBy());
            }
            if (bibRecord.getUpdatedBy() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.UPDATED_BY, bibRecord.getUpdatedBy());
            }
            if (bibRecord.getStatusUpdatedBy() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.STATUS_UPDATED_BY, bibRecord.getStatusUpdatedBy());
            }
            if (bibRecord.getDateCreated() != null && !"".equals(bibRecord.getDateCreated())) {
                additionalAttributes.setAttribute(AdditionalAttributes.DATE_ENTERED, bibRecord.getDateCreated().toString());
            }

            if (bibRecord.getDateEntered() != null && !"".equals(bibRecord.getDateEntered())) {
                additionalAttributes.setAttribute(AdditionalAttributes.LAST_UPDATED, bibRecord.getDateEntered().toString());
            }
            Content contentObj = new Content();
            contentObj.setContent(content);
            respDoc.setUuid(requestDocument.getUuid());
            respDoc.setCategory(requestDocument.getCategory());
            respDoc.setType(requestDocument.getType());
            respDoc.setFormat(requestDocument.getFormat());
            respDoc.setContent(contentObj);
            respDoc.setStatus("Success");
            respDoc.setAdditionalAttributes(additionalAttributes);
        } else {
            respDoc.setStatus("Failed");
            respDoc.setStatusMessage("Bib Record Does not exist.");
        }
        return respDoc;
    }


    @Override
    public void checkInContent(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) {
        modifyAdditionalAttributes(requestDocument);
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("bibId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        AdditionalAttributes attributes = requestDocument.getAdditionalAttributes();
        BibRecord bibRecord = businessObjectService.findByPrimaryKey(BibRecord.class, parentCriteria1);
        bibRecord.setContent(requestDocument.getContent().getContent());
        if (attributes != null) {
            bibRecord.setFassAddFlag(Boolean.valueOf(attributes.getAttribute(AdditionalAttributes.FAST_ADD_FLAG)));
            bibRecord.setSuppressFromPublic(attributes.getAttribute(AdditionalAttributes.SUPRESS_FROM_PUBLIC));
            bibRecord.setStatus(attributes.getAttribute(AdditionalAttributes.STATUS));
            /* DateFormat df = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");
            Date dateStatusUpdated = null;
            try {
                dateStatusUpdated = df.parse(attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON));
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            bibRecord.setStatusUpdatedDate(dateStatusUpdated);*/
            bibRecord.setUpdatedBy(attributes.getAttribute(AdditionalAttributes.UPDATED_BY));
            bibRecord.setDateEntered(Timestamp.valueOf(attributes.getAttribute(AdditionalAttributes.DATE_ENTERED)));
            if (attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_BY) != null) {
                bibRecord.setStatusUpdatedBy(attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_BY));
            }
            if (attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON) != null) {
                bibRecord.setStatusUpdatedDate(Timestamp.valueOf(attributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON)));
            }
            bibRecord.setStaffOnlyFlag(Boolean.valueOf(attributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG)));
            bibRecord.setUpdatedBy(attributes.getAttribute(AdditionalAttributes.UPDATED_BY));
        }
        businessObjectService.save(bibRecord);
        requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(bibRecord.getUniqueIdPrefix(), bibRecord.getBibId()));
        buildResponseDocument(requestDocument, bibRecord, responseDocument);
    }

    protected void modifyDocumentContent(RequestDocument doc, String identifier, BusinessObjectService businessObjectService) {

    }

    /*@Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        for (RequestDocument linkDoc : requestDocument.getLinkedRequestDocuments()) {
            DocumentManager documentManager = RdbmsDocumentManagerFactory.getInstance().getDocumentManager(linkDoc);
            documentManager.validateInput(linkDoc, businessObjectService, valuesList);
        }
    }*/

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;

        //To validate req documents
        //validateRequestDocument(requestDocument);

        //To validate linked documents.
        validateLinkedRequestDocument(requestDocument, valuesList, businessObjectService);

    }

    protected void validateRequestDocument(RequestDocument requestDocument) throws OleDocStoreException {

    }

    protected void validateLinkedRequestDocument(RequestDocument requestDocument, List<String> valuesList, BusinessObjectService businessObjectService) throws OleDocStoreException {
        for (RequestDocument linkDoc : requestDocument.getLinkedRequestDocuments()) {
            DocumentManager documentManager = RdbmsDocumentManagerFactory.getInstance().getDocumentManager(linkDoc);
            documentManager.validateInput(linkDoc, businessObjectService, valuesList);
        }
    }


    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        Response response = new Response();
        ResponseDocument responseDocument = new ResponseDocument();
        List<ResponseDocument> responseDocumentList = new ArrayList<ResponseDocument>();
        List<String> instanceIdentifierList = new ArrayList<String>();
        //get instances for bib
        Map bibMap = new HashMap();
        bibMap.put("bibId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<BibInstanceRecord> bibInstanceRecordList = (List<BibInstanceRecord>) businessObjectService
                .findMatching(BibInstanceRecord.class, bibMap);
        for (BibInstanceRecord bibInstanceRecord : bibInstanceRecordList) {
            Map instanceMap = new HashMap();
            instanceMap.put("instanceId", bibInstanceRecord.getInstanceId());
            List<BibInstanceRecord> bibInstanceRecords = (List<BibInstanceRecord>) businessObjectService.findMatching(BibInstanceRecord.class, instanceMap);
            if (bibInstanceRecords.size() > 1) {
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setStatus("failure'");
                responseDocument
                        .setStatusMessage("Instance is bound with more than one bib. So deletion cannot be done");
                return responseDocument;
            }
            boolean exists = checkInstancesOrItemsExistsInOLE(bibInstanceRecord.getInstanceId(), businessObjectService);
            if (exists) {
                responseDocument.setId(requestDocument.getId());
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setStatus("failure");
                responseDocument.setStatusMessage("Instances or Items in use. So deletion cannot be done");
                return responseDocument;
            }
        }
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(requestDocument.getUuid());
        responseDocument.setStatus("success");
        responseDocument.setStatusMessage("success");
        return responseDocument;
    }

/*    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        Response response = new Response();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        if (null == businessObjectService) {
            throw new OleDocStoreException("Invalid businessObjectService.");
        }
        ResponseDocument responseDocument = new ResponseDocument();
        List<ResponseDocument> responseDocumentList = new ArrayList<ResponseDocument>();
        List<String> bibIdentifierList = new ArrayList<String>();
        List<String> instanceIdentifierList = new ArrayList<String>();
        Map bibMap = new HashMap();
        bibMap.put("bibId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<BibInstanceRecord> bibInstanceRecordList = (List<BibInstanceRecord>) businessObjectService
                .findMatching(BibInstanceRecord.class, bibMap);
        for (BibInstanceRecord bibInstanceRecord : bibInstanceRecordList) {
            instanceIdentifierList.add(bibInstanceRecord.getHoldingsId());
            Map instanceMap = new HashMap();
            instanceMap.put("instanceId", bibInstanceRecord.getHoldingsId());
            List<BibInstanceRecord> bibInstanceRecords = (List<BibInstanceRecord>) businessObjectService
                    .findMatching(BibInstanceRecord.class, instanceMap);
            for (BibInstanceRecord record : bibInstanceRecords) {
                bibIdentifierList.add(record.getBibId());

            }

        }
        for (String instanceIdentifierValue : instanceIdentifierList) {
            if (bibIdentifierList.size() > 1) {
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setStatus("failure'");
                responseDocument
                        .setStatusMessage("Instance is bound with more than one bib. So deletion cannot be done");
                return responseDocument;
            }
            boolean exists = checkInstancesOrItemsExistsInOLE(instanceIdentifierValue, object);
            if (exists) {
                responseDocument.setId(requestDocument.getId());
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setStatus("failure");
                responseDocument.setStatusMessage("Instances or Items in use. So deletion cannot be done");
                return responseDocument;
            }

            responseDocument.setId(requestDocument.getId());
            responseDocument.setCategory(requestDocument.getCategory());
            responseDocument.setType(requestDocument.getType());
            responseDocument.setFormat(requestDocument.getFormat());
            responseDocument.setUuid(requestDocument.getUuid());
            responseDocument.setStatus("success");
            responseDocument.setStatusMessage("success");
        }
        return responseDocument;

    }*/

    protected boolean validateIdField (String bibId) {
        if (StringUtils.isNotEmpty(bibId)) {
            String idPattern = "[0-9]+";
            Matcher match = Pattern.compile(idPattern).matcher(bibId);
            return match.matches();
        }
        return false;
    }

    protected boolean getBibIdFromBibXMLContent(BibRecord bibRecord) {
        return true;
    }
}
