package org.kuali.ole.select.document.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.InstanceCollection;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.model.xstream.work.instance.oleml.WorkHoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.model.xstream.work.instance.oleml.WorkInstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.model.xstream.work.instance.oleml.WorkItemOlemlRecordProcessor;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.bo.OleVendorAccountInfo;
import org.kuali.ole.select.businessobject.OleCopies;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.select.service.WebClientService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;


/**
 * This class...
 */
public class OleDocstoreHelperServiceImpl extends BusinessObjectServiceHelperUtil implements OleDocstoreHelperService {

    private ConfigurationService kualiConfigurationService;
    private WebClientService webClientService;

    private final String UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING = "docAction=checkIn&stringContent=";
    private final String CHECKOUT_DOCSTORE_RECORD_QUERY_STRING = "docAction=checkOut&uuid=";
    private final String CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING = "docAction=ingestContent&stringContent=";
    private WorkItemOlemlRecordProcessor workItemOlemlRecordProcessor;
    private WorkHoldingOlemlRecordProcessor workHoldingOlemlRecordProcessor;
    private WorkInstanceOlemlRecordProcessor workInstanceOlemlRecordProcessor;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
            .getLogger(OleDocstoreHelperServiceImpl.class);
    private static final String DOCSTORE_URL = "docstore.url";
    private DocstoreClientLocator docstoreClientLocator;
    int copyCount = 0;
    boolean copyFlag = false;
    boolean newCopyFlag = false;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    private DocstoreUtil docstoreUtil=new DocstoreUtil();
    private DataCarrierService dataCarrierService;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    public String rollbackData(String bibiUUID) {
        RequestHandler requestHandler = new RequestHandler();
        Request request = new Request();
        request.setUser("mock_user");
        request.setOperation("deleteWithLinkedDocs");
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId(bibiUUID);
        requestDocument.setCategory(OLEConstants.BIB_CATEGORY_WORK);
        requestDocument.setType(OLEConstants.BIB_TYPE_BIBLIOGRAPHY);
        requestDocument.setFormat(OLEConstants.BIB_FORMAT_MARC);
        Content content = new Content();
        content.setContent("");
        requestDocument.setContent(content);
        requestDocument.setLinkedRequestDocuments(Collections.<RequestDocument>emptyList());
        request.setRequestDocuments(Arrays.asList(requestDocument));
        String rollBackXml = requestHandler.toXML(request);
        return rollbackDataFromXml(rollBackXml);
    }


    private String rollbackDataFromXml(String xmlForRollback) {

        String response = "";
        String queryString = kualiConfigurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_POST_DATA_DELETE_KEY) + URLEncoder.encode(xmlForRollback);
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String contentType = kualiConfigurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_CONTENT_TYPE_KEY);
        try {
            response = webClientService.sendRequest(docstoreURL, contentType, queryString + queryString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }


    public ConfigurationService getConfigurationService() {
        return kualiConfigurationService;
    }


    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }


    public WebClientService getWebClientService() {
        return webClientService;
    }


    public void setWebClientService(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    public void performDocstoreUpdateForRequisitionAndPOItem(PurchaseOrderDocument purchaseOrderDocument, OlePurchaseOrderItem singleItem, BibTree bibTree, String documentTypeName, String note) throws Exception {
        List<OleCopies> copies = singleItem.getCopies();
        List<OleCopy> copyList = singleItem.getCopyList();
        List<OLELinkPurapDonor> oleDonors = singleItem.getOleDonors();
        String itemTypeDescription = singleItem.getItemTypeDescription();
        String itemTitleId = singleItem.getItemTitleId();
        String poLineItemId = singleItem.getItemIdentifier() != null ? singleItem.getItemIdentifier().toString() : null;
        String poNumber = purchaseOrderDocument.getPurapDocumentIdentifier() != null ? purchaseOrderDocument.getPurapDocumentIdentifier().toString() : null;
        String reqsInitiatorName = getREQSInitiatorName(purchaseOrderDocument);
        if (singleItem.getLinkToOrderOption() != null) {
            if (singleItem.getLinkToOrderOption().equals(OLEConstants.EB_PRINT) || singleItem.getLinkToOrderOption().equals(OLEConstants.EB_ELECTRONIC)) {
                performDocstoreCRUDOperationForExistingBib(poNumber, singleItem.getLinkToOrderOption(), bibTree, copyList, oleDonors, poLineItemId, itemTypeDescription, singleItem.getItemStatus(), itemTitleId, singleItem, documentTypeName, note, reqsInitiatorName);
            } else if (singleItem.getLinkToOrderOption().equals(OLEConstants.NB_ELECTRONIC) && copyList != null && copyList.size() > 0) {
                if (documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT) ||
                        documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT) ||
                        documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT) ||
                        documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT) ||
                        documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REMOVE_HOLD_DOCUMENT) ||
                        documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PAYMENT_HOLD_DOCUMENT)){
                    if (copyList.size()>1) {
                        updateEInstanceList(copyList, oleDonors);
                    }
                    else {
                        updateEInstance(copyList.get(0), oleDonors);
                    }

                }   else {
                        if (copyList.size()>1) {
                            createEInstanceList(copyList, oleDonors, bibTree, reqsInitiatorName);
                        }
                        else {
                            createEInstance(copyList.get(0), oleDonors, bibTree, reqsInitiatorName);
                        }
                }
            } else if (singleItem.getLinkToOrderOption().equals(OLEConstants.NB_PRINT)) {
                performDocstoreCRUDOperationForItemNew(poNumber, copies, copyList, oleDonors, itemTypeDescription, itemTitleId, bibTree, poLineItemId, singleItem.getItemStatus(), singleItem.getItemLocation(), documentTypeName, note, singleItem, reqsInitiatorName);
            } else if (singleItem.getLinkToOrderOption().equals(org.kuali.ole.OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT) || singleItem.getLinkToOrderOption().equals(org.kuali.ole.OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI)) {
                performDocstoreCRUDOperationFoROrderRecordImportMarcOnlyPrint(poNumber, copyList, singleItem, oleDonors, bibTree, poLineItemId, reqsInitiatorName);
            } else if (singleItem.getLinkToOrderOption().equals(org.kuali.ole.OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC) || singleItem.getLinkToOrderOption().equals(org.kuali.ole.OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI_ELECTRONIC)) {
                performDocstoreCRUDOperationFoROrderRecordImportMarcOnlyElectronic(copyList, oleDonors, bibTree, reqsInitiatorName);
            }
        }
    }

    private String getREQSInitiatorName(PurchaseOrderDocument purchaseOrderDocument) {
        String reqInitiatorId = "";
        String reqInitiatorName = "";
        OleRequisitionDocument oleRequisitionDocument = (OleRequisitionDocument) KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleRequisitionDocument.class,purchaseOrderDocument.getRequisitionIdentifier());
        if(oleRequisitionDocument != null && oleRequisitionDocument.getDocumentHeader() != null && oleRequisitionDocument.getDocumentHeader().getWorkflowDocument() != null){
            reqInitiatorId = oleRequisitionDocument.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId();
            if(reqInitiatorId != null && !reqInitiatorId.equalsIgnoreCase("")){
                if(KimApiServiceLocator.getPersonService() != null && KimApiServiceLocator.getPersonService().getPerson(reqInitiatorId) != null){
                    reqInitiatorName = KimApiServiceLocator.getPersonService().getPerson(reqInitiatorId).getPrincipalName();
                }
            }
        }
        return reqInitiatorName;
    }

    private void performDocstoreCRUDOperationFoROrderRecordImportMarcOnlyPrint(String poNumber, List<OleCopy> oleCopyList, OlePurchaseOrderItem singleItem,
                                                                               List<OLELinkPurapDonor> oleDonors, BibTree bibTree, String poLineItemId, String initiatorName) throws Exception {
        if (oleCopyList != null) {
            boolean holdingsExists = false;
            for (OleCopy oleCopy : oleCopyList) {
                if (oleCopy != null && oleCopy.getInstanceId() != null) {
                    holdingsExists = true;
                    Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleCopy.getInstanceId());
                    org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                    oleHoldings.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                    oleHoldings.setLocation(setHoldingDetails(oleCopy).getLocation());
                    holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
                    getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
                    if (oleCopy.getItemUUID() != null) {
                        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleCopy.getItemUUID());
                        Item itemContent = new ItemOlemlRecordProcessor().fromXML(item.getContent());
                        List<DonorInfo> donorInfoList = setDonorInfoToItem(singleItem.getOleDonors(), new ArrayList<DonorInfo>());
                        itemContent.setDonorInfo(donorInfoList);
                        setItemDetails(itemContent, oleCopy, singleItem, oleDonors, poNumber);
                        item.setContent(new ItemOlemlRecordProcessor().toXML(itemContent));
                        item.setId(itemContent.getItemIdentifier());
                        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                    } else {
                        Item item = new Item();
                        setItemDetails(item, oleCopy, singleItem, oleDonors, poNumber);
                        org.kuali.ole.docstore.common.document.Item itemDocument = (org.kuali.ole.docstore.common.document.Item) getDataCarrierService().getData("reqItemId:" + oleCopy.getReqItemId() + ":item");
                        if(null == itemDocument) {
                            itemDocument = new org.kuali.ole.docstore.common.document.Item();
                        }
                        itemDocument.setContent(new ItemOlemlRecordProcessor().toXML(item));
                        itemDocument.setCreatedBy(initiatorName);
                        itemDocument.setCategory(OLEConstants.ITEM_CATEGORY);
                        itemDocument.setType(OLEConstants.ITEM_TYPE);
                        itemDocument.setFormat(OLEConstants.ITEM_FORMAT);
                        if (StringUtils.isNotBlank(oleCopy.getInstanceId())) {
                            itemDocument.setHolding(holdings);
                            getDocstoreClientLocator().getDocstoreClient().createItem(itemDocument);
                        }
                        oleCopy.setItemUUID(itemDocument.getId());
                    }
                }
            }
            if (!holdingsExists) {
                OleCopyHelperService oleCopyHelperService = new OleCopyHelperServiceImpl();
                HashMap<String, List<OleCopy>> copyListBasedOnLocation = oleCopyHelperService.getCopyListBasedOnLocation(oleCopyList, singleItem.getItemTitleId());
                Iterator<Map.Entry<String, List<OleCopy>>> entries = copyListBasedOnLocation.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, List<OleCopy>> entry = entries.next();
                    List<OleCopy> copyList = entry.getValue();
                    createOleHoldingsTree(poNumber, bibTree, copyList, poLineItemId, oleDonors, oleCopyList, singleItem.getItemTypeDescription(), singleItem.getItemStatus(), singleItem, initiatorName);
                }
            }
        }

        //Removing the dummy holdings/item object from dataCarrierService.
        for (Iterator<OleCopy> iterator = oleCopyList.iterator(); iterator.hasNext(); ) {
            OleCopy oleCopy = iterator.next();
            getDataCarrierService().removeData("reqItemId:" + oleCopy.getReqItemId() + ":holdings");
            getDataCarrierService().removeData("reqItemId:" + oleCopy.getReqItemId() + ":item");
        }
    }

    private void performDocstoreCRUDOperationFoROrderRecordImportMarcOnlyElectronic(List<OleCopy> oleCopyList,List<OLELinkPurapDonor> oleDonors, BibTree bibTree, String initiatorName) throws Exception {
        if (oleCopyList != null && oleCopyList.size() > 0) {
            OleCopy oleCopy = oleCopyList.get(0);
            if (oleCopy != null) {
                if (oleCopy.getInstanceId() != null) {
                    updateEInstance(oleCopy,oleDonors);
                } else {
                    createEInstance(oleCopy, oleDonors, bibTree, initiatorName);
                }
            }
        }
        //Removing the dummy eholdings object from dataCarrierService.
        for (Iterator<OleCopy> iterator = oleCopyList.iterator(); iterator.hasNext(); ) {
            OleCopy oleCopy = iterator.next();
            getDataCarrierService().removeData("reqItemId:" + oleCopy.getReqItemId() + ":holdings");
        }
    }

    public void setItemDetails(Item itemContent, OleCopy oleCopy, OlePurchaseOrderItem singleItem, List<OLELinkPurapDonor> oleDonors, String poNumber) {
        if (StringUtils.isBlank(itemContent.getEnumeration())) {
            itemContent.setEnumeration(oleCopy.getEnumeration());
        }
        if (itemContent.getItemStatus() == null ||
                (itemContent.getItemStatus() != null && itemContent.getItemStatus().getCodeValue() == null && itemContent.getItemStatus().getFullValue() == null)) {
            ItemStatus itemStatus = new ItemStatus();
            itemStatus.setCodeValue(singleItem.getItemStatus());
            itemStatus.setFullValue(singleItem.getItemStatus());
            itemContent.setItemStatus(itemStatus);
        }
        itemContent.setPurchaseOrderLineItemIdentifier(poNumber);
        if (singleItem != null) {
            itemContent.setVendorLineItemIdentifier(singleItem.getVendorItemPoNumber());
            if (singleItem.getExtendedPrice() != null) {
                itemContent.setPrice(singleItem.getExtendedPrice().toString());
            }
            itemContent.setFund(populateFundCodes(singleItem.getSourceAccountingLines()));
        }
        List<OLELinkPurapDonor> oleReqDonors = new ArrayList<>();
        List<DonorInfo> donorInfoList = new ArrayList<>();
        boolean flag = true;
        for (OLELinkPurapDonor reqDonorInfo : oleDonors) {
            if (itemContent.getDonorInfo() != null && itemContent.getDonorInfo().size() > 0) {
                for (DonorInfo itemDonorInfo : itemContent.getDonorInfo()) {
                    if (itemDonorInfo.getDonorCode().equals(itemDonorInfo.getDonorCode())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    oleReqDonors.add(reqDonorInfo);
                }
            }
        }
        if (itemContent.getDonorInfo() != null && itemContent.getDonorInfo().size() > 0) {
            donorInfoList = setDonorInfoToItem(oleReqDonors, itemContent.getDonorInfo());
        } else {
            donorInfoList = setDonorInfoToItem(oleDonors, itemContent.getDonorInfo());
        }
        itemContent.setDonorInfo(donorInfoList);
    }

    private void performDocstoreCRUDOperationForExistingBib(String poNumber, String linkToOrderOption, BibTree bibTree, List<OleCopy> oleCopyList, List<OLELinkPurapDonor> oleDonors, String poLineItemId, String itemTypeDescription, String itemStatusValue, String itemTitleId, OlePurchaseOrderItem singleItem, String documentTypeName, String note, String initiatorName) throws Exception {
        if (linkToOrderOption.equals(OLEConstants.EB_PRINT)) {
            OleCopyHelperService oleCopyHelperService = new OleCopyHelperServiceImpl();
            HashMap<String, List<OleCopy>> copyListBasedOnLocation = oleCopyHelperService.getCopyListBasedOnLocation(oleCopyList, itemTitleId);
            Iterator<Map.Entry<String, List<OleCopy>>> entries = copyListBasedOnLocation.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, List<OleCopy>> entry = entries.next();
                List<OleCopy> copyList = entry.getValue();
                List<OleCopy> newCopyList = new ArrayList<>();
                performUpdateForPODocuments(poNumber, bibTree, documentTypeName, poLineItemId, note, singleItem, copyList, oleCopyList, newCopyList, oleDonors, itemTypeDescription, itemStatusValue, initiatorName);
            }
        } else if (linkToOrderOption.equals(OLEConstants.EB_ELECTRONIC)) {
            if (documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT) ||
                    documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT) ||
                    documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT) ||
                    documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT) ||
                    documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REMOVE_HOLD_DOCUMENT) ||
                    documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PAYMENT_HOLD_DOCUMENT)) {

                if (oleCopyList.size() > 1) {
                    updateEInstanceList(oleCopyList, oleDonors);
                } else {
                    updateEInstance(oleCopyList.get(0), oleDonors);
                }

            } else {
                if (oleCopyList.size() > 1) {
                    createEInstanceList(oleCopyList, oleDonors, bibTree, initiatorName);
                } else {
                    createEInstance(oleCopyList.get(0), oleDonors, bibTree, initiatorName);
                }

             /*   updateEInstance(oleCopyList.get(0), oleDonors);
            }   else {
            createEInstance(oleCopyList.get(0),oleDonors,bibTree, initiatorName);
        }*/
                //createEInstance(oleCopyList.get(0),oleDonors,bibTree);
            }
        }

    }

    private void createEInstance(OleCopy oleCopy, List<OLELinkPurapDonor> oleDonors, BibTree bibTree, String initiatorName)throws Exception{
        boolean create = true;
        Holdings eHoldings = (Holdings) getDataCarrierService().getData("reqItemId:" + oleCopy.getReqItemId() + ":holdings");
        Map<String, List<HoldingsDetails>> bibHoldingsDetailsMap = new HashMap<>();
        if(StringUtils.isNotBlank(oleCopy.getBibId())) {
            String bibId = oleCopy.getBibId();
            bibHoldingsDetailsMap = getBibHoldingsDetailsMap(bibId, EHoldings.ELECTRONIC);
            String holdingsUUID = getLocationMatchedHoldingsId(bibHoldingsDetailsMap, oleCopy.getLocation(), bibId);
            if(StringUtils.isNotBlank(holdingsUUID)) {
                eHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsUUID);
                oleCopy.setInstanceId(holdingsUUID);
                Map map = new HashMap();
                map.put(org.kuali.ole.OLEConstants.INSTANCE_ID, holdingsUUID);
                List<OleCopy> oleCopyList = (List) getBusinessObjectService().findMatching(OleCopy.class, map);
                if(CollectionUtils.isEmpty(oleCopyList)) {
                    create = false;
                }
            }
        }

        if(null == eHoldings) {
            eHoldings = new EHoldings();
        }

        List<DonorInfo> donorInfoList = new ArrayList<>();
        HoldingsTree holdingsTree = new HoldingsTree();
        org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = setHoldingDetails(oleCopy);
        donorInfoList = setDonorInfoToItem(oleDonors, oleHoldings.getDonorInfo());
        oleHoldings.setDonorInfo(donorInfoList);
        eHoldings.setCategory(DocCategory.WORK.getCode());
        eHoldings.setType(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode());
        eHoldings.setFormat(org.kuali.ole.docstore.common.document.content.enums.DocFormat.OLEML.getCode());
        eHoldings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        eHoldings.setContentObject(oleHoldings);
        eHoldings.setCreatedBy(initiatorName);
        Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibTree.getBib().getId());
        eHoldings.setBib(bib);
        holdingsTree.setHoldings(eHoldings);
        if (create) {
            oleHoldings.setHoldingsIdentifier(null);
            getDocstoreClientLocator().getDocstoreClient().createHoldingsTree(holdingsTree);
            oleCopy.setInstanceId(holdingsTree.getHoldings().getId());
        } else {
            oleHoldings.setHoldingsIdentifier(oleCopy.getInstanceId());
            getDocstoreClientLocator().getDocstoreClient().updateHoldings(eHoldings);
        }
    }

    private void createEInstanceList(List<OleCopy> oleCopiesList, List<OLELinkPurapDonor> oleDonors, BibTree bibTree, String initiatorName)throws Exception{
        boolean create = true;
        for(OleCopy oleCopy : oleCopiesList) {
            Holdings eHoldings = (Holdings) getDataCarrierService().getData("reqItemId:" + oleCopy.getReqItemId() + ":holdings");
            Map<String, List<HoldingsDetails>> bibHoldingsDetailsMap = new HashMap<>();
            if (StringUtils.isNotBlank(oleCopy.getBibId())) {
                String bibId = oleCopy.getBibId();
                bibHoldingsDetailsMap = getBibHoldingsDetailsMap(bibId, EHoldings.ELECTRONIC);
                String holdingsUUID = getLocationMatchedHoldingsId(bibHoldingsDetailsMap, oleCopy.getLocation(), bibId);
                if (StringUtils.isNotBlank(holdingsUUID)) {
                    eHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsUUID);
                    oleCopy.setInstanceId(holdingsUUID);
                    Map map = new HashMap();
                    map.put(org.kuali.ole.OLEConstants.INSTANCE_ID, holdingsUUID);
                    List<OleCopy> oleCopyList = (List) getBusinessObjectService().findMatching(OleCopy.class, map);
                    if (CollectionUtils.isEmpty(oleCopyList)) {
                        create = false;
                    }
                }
            }

            if (null == eHoldings) {
                eHoldings = new EHoldings();
            }

            List<DonorInfo> donorInfoList = new ArrayList<>();
            HoldingsTree holdingsTree = new HoldingsTree();
            org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = setHoldingDetails(oleCopy);
            donorInfoList = setDonorInfoToItem(oleDonors, oleHoldings.getDonorInfo());
            oleHoldings.setDonorInfo(donorInfoList);
            eHoldings.setCategory(DocCategory.WORK.getCode());
            eHoldings.setType(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode());
            eHoldings.setFormat(org.kuali.ole.docstore.common.document.content.enums.DocFormat.OLEML.getCode());
            eHoldings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
            eHoldings.setContentObject(oleHoldings);
            eHoldings.setCreatedBy(initiatorName);
            Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibTree.getBib().getId());
            eHoldings.setBib(bib);
            holdingsTree.setHoldings(eHoldings);
            if (create) {
                oleHoldings.setHoldingsIdentifier(null);
                getDocstoreClientLocator().getDocstoreClient().createHoldingsTree(holdingsTree);
                oleCopy.setInstanceId(holdingsTree.getHoldings().getId());
            } else {
                oleHoldings.setHoldingsIdentifier(oleCopy.getInstanceId());
                getDocstoreClientLocator().getDocstoreClient().updateHoldings(eHoldings);
            }
        }
    }

    private void updateEInstance(OleCopy oleCopy, List<OLELinkPurapDonor> oleDonors) throws Exception{
        Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleCopy.getInstanceId());
        org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        oleHoldings.setLocation(setHoldingDetails(oleCopy).getLocation());
        List<OLELinkPurapDonor> oleReqDonors = new ArrayList<>();
        List<DonorInfo> donorInfoList = new ArrayList<>();
        boolean flag = true;
        for (OLELinkPurapDonor reqDonorInfo : oleDonors) {
            if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() > 0) {
                for (DonorInfo donorInfo : oleHoldings.getDonorInfo()) {
                    if (donorInfo.getDonorCode().equals(donorInfo.getDonorCode())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    oleReqDonors.add(reqDonorInfo);
                }
            }
        }
        if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() > 0) {
            donorInfoList = setDonorInfoToItem(oleReqDonors, oleHoldings.getDonorInfo());
        } else {
            donorInfoList = setDonorInfoToItem(oleDonors, oleHoldings.getDonorInfo());
        }
        oleHoldings.setDonorInfo(donorInfoList);
        donorInfoList = setDonorInfoToItem(oleDonors, new ArrayList<DonorInfo>());
        oleHoldings.setDonorInfo(donorInfoList);
        holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
    }

    private void updateEInstanceList(List<OleCopy> oleCopyList, List<OLELinkPurapDonor> oleDonors) throws Exception{
        for(OleCopy oleCopy : oleCopyList) {
            Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleCopy.getInstanceId());
            org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
            oleHoldings.setLocation(setHoldingDetails(oleCopy).getLocation());
            List<OLELinkPurapDonor> oleReqDonors = new ArrayList<>();
            List<DonorInfo> donorInfoList = new ArrayList<>();
            boolean flag = true;
            for (OLELinkPurapDonor reqDonorInfo : oleDonors) {
                if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() > 0) {
                    for (DonorInfo donorInfo : oleHoldings.getDonorInfo()) {
                        if (donorInfo.getDonorCode().equals(donorInfo.getDonorCode())) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        oleReqDonors.add(reqDonorInfo);
                    }
                }
            }
            if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() > 0) {
                donorInfoList = setDonorInfoToItem(oleReqDonors, oleHoldings.getDonorInfo());
            } else {
                donorInfoList = setDonorInfoToItem(oleDonors, oleHoldings.getDonorInfo());
            }
            oleHoldings.setDonorInfo(donorInfoList);
            donorInfoList = setDonorInfoToItem(oleDonors, new ArrayList<DonorInfo>());
            oleHoldings.setDonorInfo(donorInfoList);
            holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
            getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
        }
    }

    /*private void performDocstoreCRUDOperationForEInstance(BibTree bibTree, OleCopy oleCopy, List<OLELinkPurapDonor> oleDonors) throws Exception {
        String holdingsId = null;
        List<DonorInfo> donorInfoList = new ArrayList<>();
        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
            if (holdingsTree.getHoldings().getHoldingsType().equals(OLEConstants.ELECTRONIC)) {
                holdingsId = holdingsTree.getHoldings().getId();
                break;
            }
        }
        if (holdingsId != null) {
            Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsId);
            OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdings.getContent());
            org.kuali.ole.docstore.common.document.content.instance.Location holdingLocation = new org.kuali.ole.docstore.common.document.content.instance.Location();
            org.kuali.ole.docstore.common.document.content.instance.LocationLevel holdingLocationLevel = new org.kuali.ole.docstore.common.document.content.instance.LocationLevel();
            String holdingLocationLevelCode = getLocationLevelCode(oleCopy);
            if (null != oleCopy.getLocation()) {
                holdingLocation.setLocationLevel(setLocationLevels(holdingLocationLevel, holdingLocationLevelCode,
                        oleCopy.getLocation()));
            }
            holdingLocation.setPrimary(OLEConstants.LOCATION_PRIMARY);
            holdingLocation.setStatus(OLEConstants.LOCATION_STATUS);
            oleHoldings.setLocation(holdingLocation);
            List<OLELinkPurapDonor> oleReqDonors = new ArrayList<>();
            for (OLELinkPurapDonor reqDonorInfo : oleDonors) {
                if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() > 0) {
                    boolean flag = true;
                    for (DonorInfo eHoldingsDonorInfo : oleHoldings.getDonorInfo()) {
                        if (reqDonorInfo.getDonorCode().equals(eHoldingsDonorInfo.getDonorCode())) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        oleReqDonors.add(reqDonorInfo);
                    }
                }
            }
            if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() > 0) {
                donorInfoList = setDonorInfoToItem(oleReqDonors, oleHoldings.getDonorInfo());
            } else {
                donorInfoList = setDonorInfoToItem(oleDonors, oleHoldings.getDonorInfo());
            }
            oleHoldings.setDonorInfo(donorInfoList);
            Holdings eHoldings = new EHoldings();
            eHoldings.setId(holdings.getId());
            eHoldings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
            eHoldings.setBib(bibTree.getBib());
            oleCopy.setInstanceId(eHoldings.getId());
            getDocstoreClientLocator().getDocstoreClient().updateHoldings(eHoldings);
        }
        else {
            createEInstance(oleCopy,oleDonors,bibTree);
        }
    }*/

    private void performDocstoreCRUDOperationForItemNew(String poNumber,  List<OleCopies> copies ,List<OleCopy> copyList , List<OLELinkPurapDonor> oleDonors , String itemTypeDescription, String itemTitleId,
                                                         BibTree bibTree,String poLineItemId,String itemStatusValue, String itemLocation,
                                                         String documentTypeName, String note, OlePurchaseOrderItem singleItem, String initiatorName) throws Exception {
        List<org.kuali.ole.docstore.common.document.Item> itemDocuments = bibTree.getHoldingsTrees() != null && bibTree.getHoldingsTrees().size() > 0 && bibTree.getHoldingsTrees().get(0).getItems() != null
                ? bibTree.getHoldingsTrees().get(0).getItems() : new ArrayList<org.kuali.ole.docstore.common.document.Item>();
        OleCopyHelperService oleCopyHelperService =  new OleCopyHelperServiceImpl();
        HashMap<String, List<OleCopy>> copyListBasedOnLocation = oleCopyHelperService.getCopyListBasedOnLocation(copyList, itemTitleId);
        Iterator<Map.Entry<String, List<OleCopy>>> entries = copyListBasedOnLocation.entrySet().iterator();
        List<OleCopy> newCopyList = new ArrayList<>();
        String location=null;
        boolean copyFlag = false;
        if(bibTree.getHoldingsTrees().size()>0){
        OleHoldings oleHolding=new HoldingOlemlRecordProcessor().fromXML(bibTree.getHoldingsTrees().get(0).getHoldings().getContent());
        StringBuffer locationName=new StringBuffer("");
        location=docstoreUtil.getLocation(oleHolding.getLocation(),locationName);
        }
        int count = 0;
        while (entries.hasNext()) {
            Map.Entry<String, List<OleCopy>> entry = entries.next();
            List<OleCopy> oleCopyList = entry.getValue();
            count++;
            if (oleCopyList != null && oleCopyList.size() > 0) {
                OleCopy copy = oleCopyList.get(0);
                if(copyListBasedOnLocation.size()==1 && oleCopyList.size() == 1 && !oleCopyList.get(0).getLocation().equalsIgnoreCase(itemLocation)){
                    //copy.setLocation(itemLocation);
                    updateOleHolding(bibTree.getHoldingsTrees().get(0).getHoldings().getId(),bibTree, copy);
                    updateOleItem(poNumber, itemDocuments.get(0).getId(),poLineItemId, singleItem);
                }
                else {
                    performUpdateForPODocuments(poNumber, bibTree, documentTypeName, poLineItemId, note, singleItem, oleCopyList, copyList, newCopyList, oleDonors, itemTypeDescription, itemStatusValue ,initiatorName);

                    /*if (bibTree.getHoldingsTrees().size()>0 && (location == null || location.isEmpty())
                            && count == 1) {
                        updateOleHolding(bibTree.getHoldingsTrees().get(0).getHoldings().getId(), bibTree, copy);
                        int i = 0;
                        for (OleCopy oleCopy : copyList) {
                            if (oleCopy.getLocation().equals(copy.getLocation())) {
                                if (i == 0) {
                                    org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemDocuments.get(0).getId());
                                    Item itemContent=new ItemOlemlRecordProcessor().fromXML(item.getContent());
                                    if (item.getItemType() != null) {
                                        ItemType docstoreItemType = new ItemType();
                                        docstoreItemType.setCodeValue(itemTypeDescription);
                                        docstoreItemType.setFullValue(itemTypeDescription);
                                        itemContent.setItemType(docstoreItemType);
                                    }
                                    itemContent.setEnumeration(oleCopy.getEnumeration());
                                    itemContent.setCopyNumber(oleCopy.getCopyNumber());
                                    ItemStatus itemStatus = new ItemStatus();
                                    itemStatus.setCodeValue(itemStatusValue);
                                    itemStatus.setFullValue(itemStatusValue);
                                    itemContent.setItemStatus(itemStatus);
                                    itemContent.setPurchaseOrderLineItemIdentifier(poNumber);
                                    if (singleItem != null) {
                                        itemContent.setVendorLineItemIdentifier(singleItem.getVendorItemPoNumber());
                                        if (singleItem.getExtendedPrice() != null) {
                                            itemContent.setPrice(singleItem.getExtendedPrice().toString());
                                        }
                                        itemContent.setFund(populateFundCodes(singleItem.getSourceAccountingLines()));
                                    }
                                    oleCopy.setInstanceId(bibTree.getHoldingsTrees().get(0).getHoldings().getId());
                                    oleCopy.setItemUUID(itemContent.getItemIdentifier());
                                    List<OLELinkPurapDonor> oleReqDonors=new ArrayList<>();
                                    List<DonorInfo> donorInfoList = new ArrayList<>();
                                    boolean flag = true;
                                    for (OLELinkPurapDonor reqDonorInfo : oleDonors) {
                                        if (itemContent.getDonorInfo() != null && itemContent.getDonorInfo().size() > 0) {
                                            for (DonorInfo itemDonorInfo : itemContent.getDonorInfo()) {
                                                if (itemDonorInfo.getDonorCode().equals(itemDonorInfo.getDonorCode())) {
                                                    flag = false;
                                                    break;
                                                }
                                            }
                                            if (flag) {
                                                oleReqDonors.add(reqDonorInfo);
                                            }
                                        }
                                    }
                                    if (itemContent.getDonorInfo() != null && itemContent.getDonorInfo().size() > 0) {
                                        donorInfoList = setDonorInfoToItem(oleReqDonors, itemContent.getDonorInfo());
                                    } else {
                                        donorInfoList = setDonorInfoToItem(oleDonors, itemContent.getDonorInfo());
                                    }
                                    itemContent.setDonorInfo(donorInfoList);
                                    item.setContent(new ItemOlemlRecordProcessor().toXML(itemContent));
                                    item.setId(itemContent.getItemIdentifier());
                                    getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                                }else {
                                    Item item = setItemDetails(oleCopy,itemTypeDescription);
                                    if (item.getItemStatus()!=null){
                                        ItemStatus itemStatus = new ItemStatus();
                                        itemStatus.setCodeValue(itemStatusValue);
                                        itemStatus.setFullValue(itemStatusValue);
                                        item.setItemStatus(itemStatus);
                                    }
                                    List<DonorInfo> donorInfoList = setDonorInfoToItem(oleDonors,new ArrayList<DonorInfo>());
                                    item.setDonorInfo(donorInfoList);
                                    item.setPurchaseOrderLineItemIdentifier(poNumber);
                                    if (singleItem != null) {
                                        item.setVendorLineItemIdentifier(singleItem.getVendorItemPoNumber());
                                        if (singleItem.getExtendedPrice() != null) {
                                            item.setPrice(singleItem.getExtendedPrice().toString());
                                        }
                                        item.setFund(populateFundCodes(singleItem.getSourceAccountingLines()));
                                    }
                                    org.kuali.ole.docstore.common.document.Item itemDocument = new org.kuali.ole.docstore.common.document.Item();
                                    itemDocument.setContent(new ItemOlemlRecordProcessor().toXML(item));
                                    itemDocument.setCategory(OLEConstants.ITEM_CATEGORY);
                                    itemDocument.setType(OLEConstants.ITEM_TYPE);
                                    itemDocument.setFormat(OLEConstants.ITEM_FORMAT);
                                    itemDocument.setHolding(bibTree.getHoldingsTrees().get(0).getHoldings());
                                    try {

                                        getDocstoreClientLocator().getDocstoreClient().createItem(itemDocument);
                                        oleCopy.setItemUUID(itemDocument.getId());
                                        oleCopy.setInstanceId(itemDocument.getHolding().getId());

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }
                            i++;
                        }
                    } else {
                    }*/
                }
            }
        }
    }


    private void performUpdateForPODocuments(String poNumber, BibTree bibTree, String documentTypeName, String poLineItemId, String note, OlePurchaseOrderItem singleItem, List<OleCopy> oleCopyList, List<OleCopy> copyList, List<OleCopy> newCopyList, List<OLELinkPurapDonor> oleDonors, String itemTypeDescription, String itemStatusValue, String initiatorName) throws Exception {
        boolean isLocationAvailable = false;
        if (bibTree.getHoldingsTrees().size() > 0 && documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT)) {
            updateRecordForPOVoidDocument(poNumber, bibTree, poLineItemId, note, oleCopyList, singleItem);
            isLocationAvailable = true;
        }
        if (bibTree.getHoldingsTrees().size() > 0 && documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT)) {
            updateRecordForPOReOpenDocument(poNumber, bibTree, poLineItemId, copyList, singleItem);
            isLocationAvailable = true;
        }
        if (bibTree.getHoldingsTrees().size() > 0 && documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT)) {
            if (oleCopyList.get(0).getItemUUID() == null && oleCopyList.get(0).getInstanceId() == null && copyList.size() > 1) {
                updateRecordForPOAmendmentDocument(poNumber, bibTree, copyList, poLineItemId, oleDonors, oleCopyList, itemTypeDescription, itemStatusValue, singleItem, initiatorName);
            } else {
                for (OleCopy oleCopy : oleCopyList) {
                    if (oleCopy.getItemUUID() == null) {
                        newCopyList.add(oleCopy);
                    } else {
                        updateOlePOAItem(poNumber, oleCopy.getItemUUID(), singleItem, copyList);
                    }
                    this.copyFlag = true;
                }
                createOleHoldingsTree(poNumber, bibTree, newCopyList, poLineItemId, oleDonors, oleCopyList, itemTypeDescription, itemStatusValue, singleItem, initiatorName);
            }
            isLocationAvailable = true;
        }
        if (bibTree.getHoldingsTrees().size() > 0 && (documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT) ||
                documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REMOVE_HOLD_DOCUMENT) ||
                documentTypeName.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PAYMENT_HOLD_DOCUMENT))) {
            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleCopyList.get(0).getItemUUID());
            Item itemContent=new ItemOlemlRecordProcessor().fromXML(item.getContent());
            if (StringUtils.isNotEmpty(poNumber)) {
                itemContent.setPurchaseOrderLineItemIdentifier(poNumber);
                item.setContent(new ItemOlemlRecordProcessor().toXML(itemContent));
                item.setId(itemContent.getItemIdentifier());
                getDocstoreClientLocator().getDocstoreClient().updateItem(item);
            }
            isLocationAvailable = true;
        }
        if (!isLocationAvailable) {
            if (bibTree.getHoldingsTrees().size() == 1) {
                if (bibTree.getBib().isStaffOnly()) {
                    bibTree.getBib().setStaffOnly(false);
                    getDocstoreClientLocator().getDocstoreClient().updateBib(bibTree.getBib());
                }
            }
            createOleHoldingsTree(poNumber, bibTree, copyList, poLineItemId, oleDonors, oleCopyList, itemTypeDescription, itemStatusValue, singleItem, initiatorName);
        }
    }
    /**
     * This method will set values to Item Object and returns it to update or create Item at Docstore.
     *
     * @param oleCopy
     * @param itemTypeDescription
     * @return Item
     */
    public Item setItemDetails(OleCopy oleCopy, String itemTypeDescription) {
        Item item = null;
        boolean newItem = false;
        org.kuali.ole.docstore.common.document.Item itemDocument = (org.kuali.ole.docstore.common.document.Item) getDataCarrierService().getData("reqItemId:" + oleCopy.getReqItemId() + ":item");
        if(null != itemDocument) {
            String content = itemDocument.getContent();
            if(StringUtils.isNotBlank(content)) {
                item = (Item) new ItemOleml().deserializeContent(content);
            }
        }
        if(item == null) {
            item = new Item();
            newItem = true;
        }
        /*
         * Location itemLocation = new Location(); LocationLevel locationLevel = new LocationLevel(); String locationLevelCode =
         * OLEConstants.LOCATION_LEVEL_CODE_INSTITUTION + "/" + OLEConstants.LOCATION_LEVEL_CODE_LIBRARY; if (null !=
         * copies.getLocationCopies()) { itemLocation.setLocationLevel(setLocationLevels(locationLevel, locationLevelCode,
         * copies.getLocationCopies())); } itemLocation.setPrimary(OLEConstants.LOCATION_PRIMARY);
         * itemLocation.setStatus(OLEConstants.LOCATION_STATUS); item.setLocation(itemLocation);
         */
        ItemType docstoreItemType = new ItemType();
        docstoreItemType.setCodeValue(itemTypeDescription);
        docstoreItemType.setFullValue(itemTypeDescription);
        item.setItemType(docstoreItemType);
        item.setEnumeration(oleCopy.getEnumeration());
        if(newItem) {
            item.setCopyNumber(oleCopy.getCopyNumber());
        }
        return item;
    }


    /**
     * /**
     * This method will set values to OleHoldings Object and returns it to update or create OleHoldings at Docstore.
     *
     * @param copy
     * @return OleHoldings
     */
    public OleHoldings setHoldingDetails(OleCopy copy) throws Exception{
        OleHoldings oleHoldings = null;
        Holdings pHoldings = (Holdings) getDataCarrierService().getData("reqItemId:" + copy.getReqItemId() + ":holdings");
        if(null != pHoldings) {
            String content = pHoldings.getContent();
            if(StringUtils.isNotBlank(content)) {
                oleHoldings = new HoldingOlemlRecordProcessor().fromXML(content);
            }
        }
        if(null == oleHoldings) {
            oleHoldings = new OleHoldings();
        }
        org.kuali.ole.docstore.common.document.content.instance.Location holdingLocation = new org.kuali.ole.docstore.common.document.content.instance.Location();
        org.kuali.ole.docstore.common.document.content.instance.LocationLevel holdingLocationLevel = new org.kuali.ole.docstore.common.document.content.instance.LocationLevel();
        String holdingLocationLevelCode = getLocationLevelCode(copy);
        /*if (locationCopiesSplit.length == 3) {
            holdingLocationLevelCode = OLEConstants.LOCATION_LEVEL_CODE_INSTITUTION + "/"
                    + OLEConstants.LOCATION_LEVEL_CODE_CAMPUS + "/" + OLEConstants.LOCATION_LEVEL_CODE_LIBRARY;
        } else {
            holdingLocationLevelCode = OLEConstants.LOCATION_LEVEL_CODE_INSTITUTION + "/"
                    + OLEConstants.LOCATION_LEVEL_CODE_LIBRARY;
        }*/
        /*
         * holdingLocationLevelCode = OLEConstants.LOCATION_LEVEL_CODE_INSTITUTION + "/" + OLEConstants.LOCATION_LEVEL_CODE_LIBRARY;
         */
        if (null != copy.getLocation()) {
            holdingLocation.setLocationLevel(setLocationLevels(holdingLocationLevel, holdingLocationLevelCode,
                    copy.getLocation()));
        }
        holdingLocation.setPrimary(OLEConstants.LOCATION_PRIMARY);
        holdingLocation.setStatus(OLEConstants.LOCATION_STATUS);
        oleHoldings.setLocation(holdingLocation);
        return oleHoldings;
    }


//    public WorkBibDocument performDocstoreUpdation(String itemTitleId, WorkBibDocument workBibDocument)
//            throws Exception {
//        List<WorkInstanceDocument> workInstanceDocuments = workBibDocument.getWorkInstanceDocumentList();
//        for (WorkInstanceDocument workInstanceDocument : workInstanceDocuments) {
//
//            if (!itemTitleId.equalsIgnoreCase(workInstanceDocument.getInstanceIdentifier())) {
//                deleteDocstoreRecord(OleSelectConstant.DOCSTORE_TYPE_INSTANCE,
//                        workInstanceDocument.getInstanceIdentifier());
//            } else {
//                if (workInstanceDocument.getItemDocumentList().size() > 1) {
//                    for (int i = 1; i < workInstanceDocument.getItemDocumentList().size(); i++) {
//                        deleteDocstoreRecord(OleSelectConstant.DOCSTORE_TYPE_ITEM, workInstanceDocument
//                                .getItemDocumentList().get(i).getItemIdentifier());
//                    }
//                }
//            }
//        }
//        List<String> itemTitleIdsList = new ArrayList<String>();
//        itemTitleIdsList.add(itemTitleId);
//        List<WorkBibDocument> workBibDocuments = getWorkBibDocuments(itemTitleIdsList);
//        return workBibDocuments.get(0);
//    }

    public String deleteDocstoreRecord(String docType, String uuid) throws IOException {
        String docstoreRestfulURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(
                OLEConstants.OLE_DOCSTORE_RESTFUL_URL);
        docstoreRestfulURL = docstoreRestfulURL.concat("/") + uuid;
        HttpClient httpClient = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(docstoreRestfulURL);
        NameValuePair nvp1 = new NameValuePair(OLEConstants.IDENTIFIER_TYPE, OLEConstants.UUID);
        NameValuePair nvp2 = new NameValuePair(OLEConstants.OPERATION, OLEConstants.DELETE);
        NameValuePair category = new NameValuePair(OLEConstants.DOC_CATEGORY, OLEConstants.BIB_CATEGORY_WORK);
        NameValuePair type = new NameValuePair(OLEConstants.DOC_TYPE, docType);
        NameValuePair format = new NameValuePair(OLEConstants.DOC_FORMAT, OLEConstants.BIB_FORMAT_OLEML);
        deleteMethod.setQueryString(new NameValuePair[]{nvp1, nvp2, category, type, format});
        int statusCode = httpClient.executeMethod(deleteMethod);
        InputStream inputStream = deleteMethod.getResponseBodyAsStream();
        return IOUtils.toString(inputStream);
    }

    /**
     * Method to generate Request XML and ingest Instance record to docstore
     *
     * @param content
     * @param uuid
     * @param format
     * @return Docstore response for Ingesting New Instance Record
     * @throws Exception
     */
    public String instanceRecordCallToDocstore(String content, String uuid, String format) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = null;
        String xmlContent = buildInstanceRequestDocXML(content, uuid, format);
        queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xmlContent, "UTF-8");
        return postData(docstoreURL, queryString);
    }

    /**
     * Method to generate Request xml for Ingesting Instance record
     *
     * @param xmlContent
     * @param uuid
     * @param format
     * @return Request XML content
     */
    private String buildInstanceRequestDocXML(String xmlContent, String uuid, String format) {
        Request requestObject = new Request();
        RequestDocument requestDocument = new RequestDocument();
        if (null == uuid) {
            requestDocument.setId("1");
            requestObject.setOperation(OLEConstants.INGEST_OPERATION);
        } else {
            requestDocument.setId(uuid);
            requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
        }
        requestObject.setUser("editor");
        requestDocument.setCategory(OLEConstants.BIB_CATEGORY_WORK);
        requestDocument.setType(OLEConstants.BIB_TYPE_INSTANCE);
        requestDocument.setFormat(OLEConstants.BIB_FORMAT_OLEML);

        requestDocument.setContent(new Content(xmlContent));

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String requestXml = requestHandler.toXML(requestObject);
        return requestXml;
    }

    public Response createInstanceForBib(InstanceCollection instanceCollection) {
        String instanceXMLString = getWorkInstanceOlemlRecordProcessor().toXML(instanceCollection);
        Response responseObject = null;
        try {
            String response = instanceRecordCallToDocstore(instanceXMLString, null, OLEConstants.BIB_TYPE_INSTANCE);
            responseObject = new ResponseHandler().toObject(response);
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        return responseObject;
    }

    /*
     * public String createInstanceForBibRecord(String bibUuid, String docType, String xmlContent) throws Exception { String
     * docstoreURL = getConfigurationService().getPropertyValueAsString(OLEConstants.DOCSTORE_APP_URL_KEY); Request requestObject =
     * new Request(); requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession()
     * .getPrincipalId() : ""); requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION); RequestDocument requestDocument = new
     * RequestDocument(); requestDocument.setId(bibUuid); requestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_WORK);
     * requestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_BIB);
     * requestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_MARC); RequestDocument linkedRequestDocument = new
     * RequestDocument(); linkedRequestDocument.setId(OLEConstants.NEW_ITEM_ID);
     * linkedRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_WORK);
     * linkedRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_INSTANCE);
     * linkedRequestDocument.setFormat(OLEConstants.BIB_FORMAT_OLEML); linkedRequestDocument.setContent(new Content(xmlContent));
     * List<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
     * linkedRequestDocuments.add(linkedRequestDocument); requestDocument.setLinkedRequestDocuments(linkedRequestDocuments);
     * ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>(); requestDocuments.add(requestDocument);
     * requestObject.setRequestDocuments(requestDocuments); RequestHandler requestHandler = new RequestHandler(); String xml =
     * requestHandler.toXML(requestObject); String queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING +
     * URLEncoder.encode(xml, "UTF-8"); return postData(docstoreURL, queryString); }
     */

    /*
     * public InstanceCollection getInstanceCollection(String instanceUUID) throws Exception { String responseFromDocstore =
     * getDocstoreData(instanceUUID); InstanceCollection instanceCollection = new
     * WorkInstanceOlemlRecordProcessor().fromXML(responseFromDocstore); return instanceCollection; }
     */

    public String updateInstanceToDocstore(InstanceCollection instanceCollection) throws Exception {
        String instanceXMLString = getWorkInstanceOlemlRecordProcessor().toXML(instanceCollection);
        String instanceUUID = instanceCollection.getInstance().iterator().next().getInstanceIdentifier();
        String response = updateInstanceRecord(instanceUUID, OLEConstants.BIB_TYPE_INSTANCE, instanceXMLString);
        return response;
    }

    public WorkInstanceOlemlRecordProcessor getWorkInstanceOlemlRecordProcessor() {
        if (workInstanceOlemlRecordProcessor == null) {
            workInstanceOlemlRecordProcessor = new WorkInstanceOlemlRecordProcessor();
        }
        return workInstanceOlemlRecordProcessor;
    }

    public String getDocstoreData(String uuid) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = CHECKOUT_DOCSTORE_RECORD_QUERY_STRING + uuid;
        String responseFromDocstore = postData(docstoreURL, queryString);
        Response response = new ResponseHandler().toObject(responseFromDocstore);
        String responseContent = getResponseContent(response);
        return responseContent;
    }

    public Response getDocstoreResponse(String uuid) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = CHECKOUT_DOCSTORE_RECORD_QUERY_STRING + uuid;
        String responseFromDocstore = postData(docstoreURL, queryString);
        Response response = new ResponseHandler().toObject(responseFromDocstore);
        return response;
    }

    public String getResponseContent(Response response) {
        String responseString = null;
        List<ResponseDocument> responseDocumentList = response.getDocuments();
        for (ResponseDocument responseDocument : responseDocumentList) {
            Content contentObj = responseDocument.getContent();
            responseString = contentObj.getContent();
        }
        return responseString;
    }

    /**
     * This method takes List of UUids as parameter and creates a LinkedHashMap with instance as key and id as value. and calls
     * Docstore's QueryServiceImpl class getWorkBibRecords method and return workBibDocument for passed instance Id.
     *
     * @param instanceIdsList
     * @return List<WorkBibDocument>
     */
//    public List<WorkBibDocument> getWorkBibDocuments(List<String> instanceIdsList) {
//        List<LinkedHashMap<String, String>> instanceIdMapList = new ArrayList<LinkedHashMap<String, String>>();
//        for (String instanceId : instanceIdsList) {
//            LinkedHashMap<String, String> instanceIdMap = new LinkedHashMap<String, String>();
//            instanceIdMap.put(DocType.BIB.getDescription(), instanceId);
//            instanceIdMapList.add(instanceIdMap);
//        }
//
//        QueryService queryService = QueryServiceImpl.getInstance();
//        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
//        try {
//            workBibDocuments = queryService.getWorkBibRecords(instanceIdMapList);
//        } catch (Exception ex) {
//            // TODO Auto-generated catch block
//            ex.printStackTrace();
//        }
//        return workBibDocuments;
//    }

    /**
     * This method takes locationLevelCode and locationLevelName as parameters and split level name and returns as location level.
     *
     * @param locationLevel
     * @param locationLevelCode
     * @param locationLevelName
     * @return
     */
    public org.kuali.ole.docstore.common.document.content.instance.LocationLevel setLocationLevels(org.kuali.ole.docstore.common.document.content.instance.LocationLevel locationLevel, String locationLevelCode,
                                           String locationLevelName) {

        String[] levelNames = locationLevelName.split("/");
        String[] levels = locationLevelCode.split("/");
        locationLevel.setName(levelNames[0]);
        locationLevel.setLevel(levels[0]);
        String levlName = "";
        String levl = "";
        if (locationLevelName.contains("/") && locationLevelCode.contains("/")) {
            levlName = locationLevelName.replace(levelNames[0] + "/", "");
            levl = locationLevelCode.replace(levels[0] + "/", "");
        } else {
            levlName = locationLevelName.replace(levelNames[0], "");
            levl = locationLevelCode.replace(levels[0], "");
        }
        if ((levlName != null && !levlName.equals("")) && (levl != null && !levl.equals(""))) {
            org.kuali.ole.docstore.common.document.content.instance.LocationLevel newLocationLevel = new org.kuali.ole.docstore.common.document.content.instance.LocationLevel();
            locationLevel.setLocationLevel(setLocationLevels(newLocationLevel, levl, levlName));
        }
        return locationLevel;
    }

    public Response createItemToDocstore(String instanceUuid, org.kuali.ole.docstore.common.document.content.instance.Item item) {
        String oleItemXMLString = new ItemOlemlRecordProcessor().toXML(item);
        Response responseObject = null;
        try {
            String response = createItemForInstanceRecord(instanceUuid, OLEConstants.ITEM_DOC_TYPE, oleItemXMLString);
            responseObject = new ResponseHandler().toObject(response);
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        return responseObject;
    }

    public String updateOleHoldingToDocstore(OleHoldings oleHoldings) throws Exception {
        String oleHoldingXMLString = new HoldingOlemlRecordProcessor().toXML(oleHoldings);
        String oleHoldingUUID = oleHoldings.getHoldingsIdentifier();

        String response = updateInstanceRecord(oleHoldingUUID, OLEConstants.HOLDING_DOC_TYPE, oleHoldingXMLString);
        return response;
    }

    public String updateOleItemToDocstore(Item item) throws Exception {
        String oleItemXMLString = new ItemOlemlRecordProcessor().toXML(item);
        // String itemXMLString = new UpdateDocstoreRecord().getResponseFromWorkItem(item);
        String oleItemUUID = item.getItemIdentifier();
        if (LOG.isDebugEnabled()) {
            LOG.debug("oleItemUUID---------->" + oleItemUUID);
        }
        String response = updateInstanceRecord(oleItemUUID, OLEConstants.ITEM_DOC_TYPE, oleItemXMLString);
        return response;
    }

    private WorkItemOlemlRecordProcessor getWorkItemOlemlRecordProcessor() {
        if (workItemOlemlRecordProcessor == null) {
            workItemOlemlRecordProcessor = new WorkItemOlemlRecordProcessor();
        }
        return workItemOlemlRecordProcessor;
    }

    public WorkHoldingOlemlRecordProcessor getWorkHoldingOlemlRecordProcessor() {
        if (workHoldingOlemlRecordProcessor == null) {
            workHoldingOlemlRecordProcessor = new WorkHoldingOlemlRecordProcessor();
        }
        return workHoldingOlemlRecordProcessor;
    }

    public String updateInstanceRecord(String uuid, String docType, String xmlContent) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        Request requestObject = new Request();
        requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession()
                .getPrincipalId() : "");
        requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
        RequestDocument requestDocument = new RequestDocument();

        requestDocument.setId(uuid);
        requestDocument.setCategory(OLEConstants.BIB_CATEGORY_WORK);
        requestDocument.setType(docType); // docType should be either holdings or item
        requestDocument.setFormat(OLEConstants.BIB_FORMAT_OLEML);
        requestDocument.setContent(new Content(xmlContent));

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);

        String queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xml, "UTF-8");

        return postData(docstoreURL, queryString);
    }

    public static String postData(String target, String content) throws Exception {
        String response = "";
        URL url = new URL(target);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        Writer w = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        w.write(content);
        w.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String temp;
        while ((temp = in.readLine()) != null) {
            response += temp + "\n";
        }
        in.close();
        return response;
    }

    /**
     * Method to add NEW ITEM for existing Instance record
     *
     * @param instanceUuid
     * @param docType
     * @param xmlContent
     * @return Docstore XML response with success/failure status
     * @throws Exception
     */
    public String createItemForInstanceRecord(String instanceUuid, String docType, String xmlContent) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        Request requestObject = new Request();
        requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession()
                .getPrincipalId() : "");
        requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
        RequestDocument requestDocument = new RequestDocument();

        requestDocument.setId(instanceUuid);
        requestDocument.setCategory(OLEConstants.BIB_CATEGORY_WORK);
        requestDocument.setType(OLEConstants.BIB_TYPE_INSTANCE);
        requestDocument.setFormat(OLEConstants.BIB_FORMAT_OLEML);


        RequestDocument linkedRequestDocument = new RequestDocument();
        linkedRequestDocument.setId(OLEConstants.NEW_ITEM_ID);
        linkedRequestDocument.setCategory(OLEConstants.BIB_CATEGORY_WORK);
        linkedRequestDocument.setType(docType);
        linkedRequestDocument.setFormat(OLEConstants.BIB_FORMAT_OLEML);
        linkedRequestDocument.setContent(new Content(xmlContent));

        List<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
        linkedRequestDocuments.add(linkedRequestDocument);
        requestDocument.setLinkedRequestDocuments(linkedRequestDocuments);


        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);

        String queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xml, "UTF-8");

        return postData(docstoreURL, queryString);

    }


    public String getItemLocations(String location) {
        String[] locations = location.split("/");
        StringBuffer locationString = new StringBuffer();
        for (int i = locations.length - 1; i >= 1; i--) {
            if (i == 1) {
                locationString = locationString.append(locations[i]);
            } else {
                locationString = locationString.append(locations[i] + "/");
            }
        }
        return locationString.toString();
    }



    @Override
    public void createOrUpdateDocStoreBasedOnLocation(PurchaseOrderDocument document, PurApItem item, String currentDocumentTypeName, String note) {
        OlePurchaseOrderItem olePurchaseOrderItem = (OlePurchaseOrderItem) item;
        try {
            List<String> itemTitleIdsList = new ArrayList<String>();
            List<BibTree> bibTrees= new ArrayList<BibTree>();
            itemTitleIdsList.add(olePurchaseOrderItem.getItemTitleId());
            bibTrees = getBibTreeDocuments(itemTitleIdsList);
            for (BibTree bibTree : bibTrees) {
                if (null != olePurchaseOrderItem.getItemTitleId()) {
                    performDocstoreUpdateForRequisitionAndPOItem(document, olePurchaseOrderItem,
                            bibTree, currentDocumentTypeName, note);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private List<BibTree> getBibTreeDocuments(List<String> instanceIdsList) {
        List<LinkedHashMap<String, String>> instanceIdMapList = new ArrayList<LinkedHashMap<String, String>>();
        for (String instanceId : instanceIdsList) {
            LinkedHashMap<String, String> instanceIdMap = new LinkedHashMap<String, String>();
            instanceIdMap.put(DocType.BIB.getDescription(), instanceId);
            instanceIdMapList.add(instanceIdMap);
        }
        List<BibTree> bibTrees = new ArrayList<BibTree>();
        try {
            bibTrees = getWorkBibRecords(instanceIdMapList);
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        return bibTrees;
    }
    public List<BibTree> getWorkBibRecords(List<LinkedHashMap<String, String>> uuidsMapList) throws Exception {
        List<BibTree> bibTres = new ArrayList<BibTree>();
        BibTree bibTree=new BibTree();
        for (LinkedHashMap<String, String> uuidsMap : uuidsMapList) {
            if (uuidsMap.containsKey(DocType.BIB.getDescription())) {
                String bibId = uuidsMap.get(DocType.BIB.getDescription());
                if (LOG.isDebugEnabled()) {
                    LOG.debug(" bibId ---------------> " + bibId);
                }
                if (StringUtils.isNotBlank(bibId)) {
                    bibTree=getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId.toString());
                }
            }
            bibTres.add(bibTree);
        }
        return bibTres;
    }
    /**
     * Populate location levels.
     *
     * @throws Exception
     */
    public String getLocationLevelCode( OleCopy copy) throws Exception {
        LOG.debug("Inside the getLocationLevelCode method");
        StringBuffer location = new StringBuffer();
        StringBuffer locationLevelCode = new StringBuffer();
        String[] locationCopiesSplit = copy.getLocation().split("/");
        for(String locationCode : locationCopiesSplit){
            OleLocation oleLocation =getLocationByLocationCode(locationCode);
            if(oleLocation!=null){
                OleLocationLevel oleLocationLevel =  oleLocation.getOleLocationLevel();
                String levelCode = oleLocationLevel!=null && oleLocationLevel.getLevelCode()!=null ? oleLocationLevel.getLevelCode() : "" ;
                setLocation(locationLevelCode,location,levelCode,oleLocation.getLocationCode(),oleLocation.getLocationName());
            }
        }
        return locationLevelCode.toString();
    }

    /**
     * sets the value for location levels in Loan Document.
     *
     * @param levelCode
     * @param locationCode
     * @throws Exception
     */
    private void setLocation(StringBuffer locationLevelCode,StringBuffer location, String levelCode, String locationCode, String locationName) throws Exception {
        LOG.debug("Inside the setLocation method");
        if (locationCode != null) {
            if (levelCode.equalsIgnoreCase(org.kuali.ole.OLEConstants.OLEBatchProcess.LOCATION_LEVEL_SHELVING)) {
                location.append(locationName);
                locationLevelCode.append(levelCode);
            } else if (levelCode.equalsIgnoreCase(org.kuali.ole.OLEConstants.OLEBatchProcess.LOCATION_LEVEL_COLLECTION)) {
                location.append(locationName + "/");
                locationLevelCode.append(levelCode + "/");
            } else if (levelCode.equalsIgnoreCase(org.kuali.ole.OLEConstants.OLEBatchProcess.LOCATION_LEVEL_LIBRARY)) {
                location.append(locationName + "/");
                locationLevelCode.append(levelCode + "/");
            } else if (levelCode.equalsIgnoreCase(org.kuali.ole.OLEConstants.OLEBatchProcess.LOCATION_LEVEL_INSTITUTION)) {
                location.append(locationName + "/");
                locationLevelCode.append(levelCode + "/");
            } else if (levelCode.equalsIgnoreCase(org.kuali.ole.OLEConstants.OLEBatchProcess.LOCATION_LEVEL_CAMPUS)) {
                location.append(locationName + "/");
                locationLevelCode.append(levelCode + "/");
            }
        }
    }

    /**
     * This method returns location using location code.
     *
     * @param locationCode
     * @return
     * @throws Exception
     */
    private OleLocation getLocationByLocationCode(String locationCode) throws Exception {
        LOG.debug("Inside the getLocationByLocationCode method");
        Map barMap = new HashMap();
        barMap.put(org.kuali.ole.OLEConstants.LOC_CD, locationCode);
        List<OleLocation> matchingLocation = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class, barMap);
        return matchingLocation.size()>0 ? matchingLocation.get(0) : null;
    }


    /*@Override
    public void updateItemNote(PurApItem item, String note) {
        OleRequisitionItem oleRequisitionItem = (OleRequisitionItem) item;
        try {
            List<String> itemTitleIdsList = new ArrayList<String>();
            List<BibTree> bibTrees = new ArrayList<BibTree>();
            itemTitleIdsList.add(oleRequisitionItem.getItemTitleId());
            bibTrees = getBibTreeDocuments(itemTitleIdsList);
            for (BibTree bibTree : bibTrees) {
                if (null != oleRequisitionItem.getItemTitleId()) {
                    performDocstoreUpdateForRequisitionItemRecord(oleRequisitionItem,
                            bibTree, note);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void performDocstoreUpdateForRequisitionItemRecord(OleRequisitionItem oleRequisitionItem , BibTree bibTree, String note) throws Exception {
        List<OleCopy> copyList = oleRequisitionItem.getCopyList();
        String itemTitleId = oleRequisitionItem.getItemTitleId();
        OleCopyHelperService oleCopyHelperService =  new OleCopyHelperServiceImpl();
        HashMap<String, List<OleCopy>> copyListBasedOnLocation = oleCopyHelperService.getCopyListBasedOnLocation(copyList, itemTitleId);
        Iterator<Map.Entry<String, List<OleCopy>>> entries = copyListBasedOnLocation.entrySet().iterator();
        int count = 0;
        if (copyList.size() == 0) {
            OleCopy oleCopy = new OleCopy();
            oleCopy.setLocation(oleRequisitionItem.getItemLocation());
            if (bibTree.getHoldingsTrees().size() == 1) {
                updateOleHoldingStaffOnly(bibTree,oleCopy,true);
                bibTree.getBib().setStaffOnly(true);
                getDocstoreClientLocator().getDocstoreClient().updateBib(bibTree.getBib());
            }
            for (int items=0; items < bibTree.getHoldingsTrees().size(); items++) {
                updateOleItemStaffOnly(bibTree.getHoldingsTrees().get(items).getItems().get(0).getId(),null,note,true);
                oleCopy.setReqDocNum(null);
                oleCopy.setReqItemId(null);
            }
        }
        while (entries.hasNext()) {
            Map.Entry<String, List<OleCopy>> entry = entries.next();
            List<OleCopy> oleCopyList = entry.getValue();
            count++;
            if (oleCopyList != null && oleCopyList.size() > 0) {
                if (bibTree.getHoldingsTrees().size() == 1) {
                    OleCopy copy = oleCopyList.get(0);
                    updateOleHoldingStaffOnly(bibTree,copy,true);
                    bibTree.getBib().setStaffOnly(true);
                    getDocstoreClientLocator().getDocstoreClient().updateBib(bibTree.getBib());
                }
                for (OleCopy oleCopy : copyList) {
                    for (int items=0; items < bibTree.getHoldingsTrees().size(); items++) {
                        for (int singleItem=0; singleItem < bibTree.getHoldingsTrees().get(items).getItems().size(); singleItem++) {
                            if (oleCopy.getItemUUID().equalsIgnoreCase(bibTree.getHoldingsTrees().get(items).getItems().get(singleItem).getId())) {
                                if (!bibTree.getHoldingsTrees().get(items).getItems().get(singleItem).isStaffOnly()) {
                                    updateOleItemStaffOnly(oleCopy.getItemUUID(),null,note,true);
                                    oleCopy.setReqDocNum(null);
                                    oleCopy.setReqItemId(null);
                                }
                            }

                        }
                    }
                }
            }
        }

    }*/

    public List<DonorInfo> setDonorInfoToItem(List<OLELinkPurapDonor> oleDonors,List<DonorInfo> donorInfoList){
        for (OLELinkPurapDonor oleLinkPurapDonor : oleDonors) {
            DonorInfo donorInfo = new DonorInfo();
            donorInfo.setDonorCode(oleLinkPurapDonor.getDonorCode());
            Map donorMap = new HashMap();
            donorMap.put(OLEConstants.DONOR_CODE, oleLinkPurapDonor.getDonorCode());
            OLEDonor oleDonor = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEDonor.class, donorMap);
            if (oleDonor != null) {
                if (StringUtils.isNotEmpty(oleDonor.getDonorNote()))
                    donorInfo.setDonorNote(oleDonor.getDonorNote());
                if (StringUtils.isNotEmpty(oleDonor.getDonorPublicDisplay()))
                    donorInfo.setDonorPublicDisplay(oleDonor.getDonorPublicDisplay());
            }
            donorInfoList.add(donorInfo);
        }
        return donorInfoList;
    }

    @Override
    public void updateItemLocation(PurchaseOrderDocument document, PurApItem item) {
        OlePurchaseOrderItem olePurchaseOrderItem = (OlePurchaseOrderItem) item;
        try {
            List<String> itemTitleIdsList = new ArrayList<String>();
            List<BibTree> bibTrees= new ArrayList<BibTree>();
            String poLineItemId =  olePurchaseOrderItem.getItemIdentifier()!=null?olePurchaseOrderItem.getItemIdentifier().toString():null;
            itemTitleIdsList.add(olePurchaseOrderItem.getItemTitleId());
            bibTrees = getBibTreeDocuments(itemTitleIdsList);
            for (BibTree bibTree : bibTrees) {
                if (null != olePurchaseOrderItem.getItemTitleId()) {
                    if (olePurchaseOrderItem.isItemLocationChangeFlag()) {
                        if (StringUtils.isNotBlank(olePurchaseOrderItem.getItemLocation()) && olePurchaseOrderItem.getCopyList().size() == 1) {
                            if( bibTree.getHoldingsTrees().size()>0 ){
                                OleCopy copy = olePurchaseOrderItem.getCopyList().get(0);
                                copy.setLocation(olePurchaseOrderItem.getItemLocation());
                                updateOleHolding(copy.getInstanceId(),bibTree, copy);
                                updateOleItem(document.getPurapDocumentIdentifier().toString(), olePurchaseOrderItem.getCopyList().get(0).getItemUUID(),poLineItemId, olePurchaseOrderItem);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void updateOleHoldingStaffOnly(String holdingId, BibTree bibTree, OleCopy copy,boolean staffOnly) throws Exception {
        org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = setHoldingDetails(copy);
        oleHoldings.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
        oleHoldings.setHoldingsIdentifier(holdingId);
        Holdings newHoldings=new PHoldings();
        newHoldings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        newHoldings.setStaffOnly(staffOnly);
        newHoldings.setId(holdingId);
        newHoldings.setBib(bibTree.getBib());
        getDocstoreClientLocator().getDocstoreClient().updateHoldings(newHoldings);
    }

    private void updateOleHolding(String holdingId, BibTree bibTree, OleCopy copy) throws Exception {
        org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = setHoldingDetails(copy);
        oleHoldings.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
        oleHoldings.setHoldingsIdentifier(holdingId);
        Holdings holdings=new PHoldings();
        holdings.setId(holdingId);
        holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        holdings.setBib(bibTree.getBib());
        getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
    }

    private void updateOleItem(String poNumber, String itemId, String poLineItemId, OlePurchaseOrderItem singleItem) throws Exception {
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemId);
        Item itemContent=new ItemOlemlRecordProcessor().fromXML(item.getContent());
        List<DonorInfo> donorInfoList = setDonorInfoToItem(singleItem.getOleDonors(), new ArrayList<DonorInfo>());
        itemContent.setDonorInfo(donorInfoList);
        itemContent.setPurchaseOrderLineItemIdentifier(poNumber);
        if (singleItem != null) {
            itemContent.setVendorLineItemIdentifier(singleItem.getVendorItemPoNumber());
            if (singleItem.getExtendedPrice() != null) {
                itemContent.setPrice(singleItem.getExtendedPrice().toString());
            }
            itemContent.setFund(populateFundCodes(singleItem.getSourceAccountingLines()));
        }
        itemContent.setItemIdentifier(itemId);
        item.setContent(new ItemOlemlRecordProcessor().toXML(itemContent));
        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
    }

    private void updateOlePOAItem(String poNumber, String itemId, OlePurchaseOrderItem singleItem,List<OleCopy> copyList) throws Exception {
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemId);
        Item itemContent=new ItemOlemlRecordProcessor().fromXML(item.getContent());
        if (copyList.size() == 1) {
            itemContent.setEnumeration(copyList.get(0).getEnumeration());
            itemContent.setCopyNumber(copyList.get(0).getCopyNumber());
        } else {
            for (int i = 0; i < copyList.size(); i++) {
                if (itemContent.getItemIdentifier().equals(copyList.get(i).getItemUUID())) {
                    itemContent.setEnumeration(copyList.get(i).getEnumeration());
                    itemContent.setCopyNumber(copyList.get(i).getCopyNumber());
                    break;
                }
            }
        }
        List<DonorInfo> donorInfoList = setDonorInfoToItem(singleItem.getOleDonors(), new ArrayList<DonorInfo>());
        itemContent.setDonorInfo(donorInfoList);
        itemContent.setPurchaseOrderLineItemIdentifier(poNumber);
        if (singleItem != null) {
            itemContent.setVendorLineItemIdentifier(singleItem.getVendorItemPoNumber());
            if (singleItem.getExtendedPrice() != null) {
                itemContent.setPrice(singleItem.getExtendedPrice().toString());
            }
            itemContent.setFund(populateFundCodes(singleItem.getSourceAccountingLines()));
        }
        itemContent.setItemIdentifier(itemId);
        item.setContent(new ItemOlemlRecordProcessor().toXML(itemContent));
        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
    }

    private void updateOleItemStaffOnly(String poNumber, String itemId, String poLineItemId, String note, boolean staffOnly, OlePurchaseOrderItem singleItem) throws Exception {
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemId);
        Item itemContent=new ItemOlemlRecordProcessor().fromXML(item.getContent());
        if (StringUtils.isNotEmpty(poNumber)) {
            itemContent.setPurchaseOrderLineItemIdentifier(poNumber);
        }
        if (singleItem != null) {
            itemContent.setVendorLineItemIdentifier(singleItem.getVendorItemPoNumber());
            if (singleItem.getExtendedPrice() != null) {
                itemContent.setPrice(singleItem.getExtendedPrice().toString());
            }
            itemContent.setFund(populateFundCodes(singleItem.getSourceAccountingLines()));
        }
        itemContent.setItemIdentifier(itemId);
        if (StringUtils.isNotEmpty(note)) {
            List<Note> noteList = itemContent.getNote()!=null ? itemContent.getNote() : (List<Note>) new ArrayList<Note>();
            Note noteObj = new Note();
            noteObj.setType(OLEConstants.NON_PUBLIC);
            noteObj.setValue(note);
            noteList.add(noteObj);
            itemContent.setNote(noteList);
        }
        item.setContent(new ItemOlemlRecordProcessor().toXML(itemContent));
        item.setStaffOnly(staffOnly);
        item.setId(itemContent.getItemIdentifier());
        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
    }

    private void updateRecordForPOVoidDocument(String poNumber, BibTree bibTree, String poLineItemId, String note ,List<OleCopy> copyList, OlePurchaseOrderItem singleItem) throws Exception {
        OleCopy copy = copyList.get(0);
        if (bibTree.getHoldingsTrees().size() == 1 && copyList.size()==1) {
            bibTree.getBib().setStaffOnly(true);
            getDocstoreClientLocator().getDocstoreClient().updateBib(bibTree.getBib());
        }
        if (copyList.size()==1) {
            updateOleHoldingStaffOnly(copy.getInstanceId(),bibTree,copy,true);
        }
        for (OleCopy oleCopy : copyList) {
            updateOleItemStaffOnly(poNumber, oleCopy.getItemUUID(),poLineItemId,note,true, singleItem);
            oleCopy.setReqDocNum(null);
            oleCopy.setReqItemId(null);
        }
    }

    private void updateRecordForPOReOpenDocument(String poNumber, BibTree bibTree, String poLineItemId, List<OleCopy> copyList, OlePurchaseOrderItem singleItem) throws Exception {
        OleCopy copy = copyList.get(0);
        if (bibTree.getBib().getId().equalsIgnoreCase(OLEConstants.TRUE)) {
            bibTree.getBib().setStaffOnly(false);
            getDocstoreClientLocator().getDocstoreClient().updateBib(bibTree.getBib());
        }
        if (copyList.size()==1) {
            updateOleHoldingStaffOnly(copy.getInstanceId(),bibTree,copy,false);
        }
        for (OleCopy oleCopy : copyList) {
            updateOleItemStaffOnly(poNumber, oleCopy.getItemUUID(),poLineItemId,null,false, singleItem);
            oleCopy.setReqDocNum(null);
            oleCopy.setReqItemId(null);
        }
    }

    private void updateRecordForPOAmendmentDocument(String poNumber, BibTree bibTree,List<OleCopy> copyList, String poLineItemId,List<OLELinkPurapDonor> oleDonors,List<OleCopy> oleCopyList,
                                                    String itemTypeDescription,String itemStatusValue, OlePurchaseOrderItem singleItem, String initiatorName) throws Exception {
        OleCopy copy = oleCopyList.get(0);
        if (copyList.size()>1) {
            if (!this.copyFlag) {
                List <OleCopy> newCopyList = new ArrayList<>();
                int j=0;
                for (OleCopy newCopy : oleCopyList) {
                    if (!this.newCopyFlag) { // existing record's location  is updated with First copy from copyList
                        String holdingsId = null;
                        String itemId = null;
                        if (bibTree.getHoldingsTrees().size() > 0) {
                            List<HoldingsTree>  holdingsTree = bibTree.getHoldingsTrees();
                            for (HoldingsTree holdings : holdingsTree) {
                                List<org.kuali.ole.docstore.common.document.Item> itemList = holdings.getItems();
                                ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
                                for(org.kuali.ole.docstore.common.document.Item item : itemList) {
                                    org.kuali.ole.docstore.common.document.content.instance.Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
                                    if(itemContent.getPurchaseOrderLineItemIdentifier() != null) {
                                        if(itemContent.getPurchaseOrderLineItemIdentifier().equals(poNumber)) {
                                            holdingsId =  holdings.getHoldings().getId();
                                            itemId = item.getId();
                                        }
                                    }
                                }
                            }
                        }
                        updateOleHolding(holdingsId, bibTree, newCopy);
                        if (bibTree.getHoldingsTrees().get(0).getItems().size()==1) {
                            updateOlePOAItem(poNumber, itemId, singleItem, oleCopyList);
                            newCopy.setInstanceId(holdingsId);
                            newCopy.setItemUUID(itemId);
                            for (int copyCnt=0; copyCnt<oleCopyList.size(); copyCnt++) {
                                if (!newCopy.getCopyId().equals(oleCopyList.get(copyCnt).getCopyId())) {
                                    newCopyList.add(oleCopyList.get(copyCnt));
                                }
                            }
                            this.copyCount++;
                        }
                        else {
                            int itemRecord=0;
                            for (int copyRecord=0;copyRecord<oleCopyList.size();) {
                                if ( itemRecord<bibTree.getHoldingsTrees().get(0).getItems().size()) {
                                    updateOlePOAItem(poNumber, bibTree.getHoldingsTrees().get(0).getItems().get(itemRecord).getId(), singleItem,oleCopyList);
                                    oleCopyList.get(copyRecord).setInstanceId(bibTree.getHoldingsTrees().get(0).getHoldings().getId());
                                    oleCopyList.get(copyRecord).setItemUUID(bibTree.getHoldingsTrees().get(0).getItems().get(itemRecord).getId());
                                    this.copyCount++;itemRecord++;copyRecord++;
                                }
                                if(itemRecord==bibTree.getHoldingsTrees().get(0).getItems().size()){
                                    if (copyRecord < oleCopyList.size()) {
                                        newCopyList.add(oleCopyList.get(copyRecord));
                                        copyRecord++;
                                    }
                                }
                            }
                        }
                        this.newCopyFlag = true;
                    } else {
                        if (newCopyList.size()>0) {

                            /*List<OleCopy> newCopies = new ArrayList();
                            for (int oleCopy=0; oleCopy<newCopyList.size();) {
                                newCopies.add(newCopyList.get(oleCopy));
                                createOleHoldingsTree(bibTree,copyList,poLineItemId,oleDonors,newCopies,itemTypeDescription,itemStatusValue);
                                newCopies.clear();
                                oleCopy++;
                            }*/
                            createOleHoldingsTree(poNumber, bibTree,newCopyList,poLineItemId,oleDonors,oleCopyList,itemTypeDescription,itemStatusValue,singleItem, initiatorName);
                        }
                        if (this.copyCount==oleCopyList.size()) {
                            break;
                        }
                    }
                }
                this.copyFlag = true;
            } else {  // new record's are created with its respective locations.
                createOleHoldingsTree(poNumber, bibTree,copyList,poLineItemId,oleDonors,oleCopyList,itemTypeDescription,itemStatusValue, singleItem, initiatorName);
            }
        }
    }

    public void createOleHoldingsTree(String poNumber, BibTree bibTree,List<OleCopy> copyList, String poLineItemId,List<OLELinkPurapDonor> oleDonors,List<OleCopy> oleCopyList,
                                        String itemTypeDescription,String itemStatusValue, OlePurchaseOrderItem singleItem, String initiatorName) throws Exception {
        OleCopy copy = oleCopyList.get(0);
        Holdings pHoldings = (Holdings) getDataCarrierService().getData("reqItemId:" + copy.getReqItemId() + ":holdings");
        if (StringUtils.isNotBlank(copy.getInstanceId())) {
            pHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(copy.getInstanceId());
        } else {
            Map<String, List<HoldingsDetails>> bibHoldingsDetailsMap = new HashMap<>();
            if (StringUtils.isNotBlank(copy.getBibId())) {
                String bibId = copy.getBibId();
                bibHoldingsDetailsMap = getBibHoldingsDetailsMap(bibId, PHoldings.PRINT);
                String holdingsUUID = getLocationMatchedHoldingsId(bibHoldingsDetailsMap, copy.getLocation(), bibId);
                if (StringUtils.isNotBlank(holdingsUUID)) {
                    pHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsUUID);
                    copy.setInstanceId(holdingsUUID);
                }
            }
        }

        if (null == pHoldings) {
            pHoldings = new PHoldings();
        }
        List<org.kuali.ole.docstore.common.document.Item> itemList = new ArrayList<org.kuali.ole.docstore.common.document.Item>();
        org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = setHoldingDetails(copy);
        oleHoldings.setHoldingsIdentifier(null);
        oleHoldings.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
        List<Integer> copyIdList = new ArrayList<>();
        for (OleCopy oleCopy : copyList) {
            if (oleCopy.getLocation().equals(copy.getLocation())) {
                Item item = setItemDetails(oleCopy, itemTypeDescription);
                ItemStatus itemStatus = new ItemStatus();
                itemStatus.setCodeValue(itemStatusValue);
                itemStatus.setFullValue(itemStatusValue);
                item.setItemStatus(itemStatus);
                item.setPurchaseOrderLineItemIdentifier(poNumber);
                if (singleItem != null) {
                    item.setVendorLineItemIdentifier(singleItem.getVendorItemPoNumber());
                    if (singleItem.getExtendedPrice() != null) {
                        item.setPrice(singleItem.getExtendedPrice().toString());
                    }
                    item.setFund(populateFundCodes(singleItem.getSourceAccountingLines()));
                }
                List<DonorInfo> donorInfoList = setDonorInfoToItem(oleDonors, new ArrayList<DonorInfo>());
                item.setDonorInfo(donorInfoList);
                org.kuali.ole.docstore.common.document.Item itemDocument = (org.kuali.ole.docstore.common.document.Item) getDataCarrierService().getData("reqItemId:" + oleCopy.getReqItemId() + ":item");
                if (null == itemDocument) {
                    itemDocument = new org.kuali.ole.docstore.common.document.Item();
                }
                itemDocument.setContent(new ItemOlemlRecordProcessor().toXML(item));
                itemDocument.setCreatedBy(initiatorName);
                itemDocument.setCategory(OLEConstants.ITEM_CATEGORY);
                itemDocument.setType(OLEConstants.ITEM_TYPE);
                itemDocument.setFormat(OLEConstants.ITEM_FORMAT);
                if (StringUtils.isNotBlank(copy.getInstanceId())) {
                    itemDocument.setHolding(pHoldings);
                    getDocstoreClientLocator().getDocstoreClient().createItem(itemDocument);
                } else {
                    itemList.add(itemDocument);
                }
                copyIdList.add(oleCopy.getCopyId());
            }
        }
        HoldingsTree holdingsTree = new HoldingsTree();
        if (StringUtils.isBlank(copy.getInstanceId())) {
            holdingsTree.getItems().addAll(itemList);
            Holdings holdings = (Holdings) getDataCarrierService().getData("reqItemId:" + copy.getReqItemId() + ":holdings");
            if (null == holdings) {
                holdings = new PHoldings();
            }
            holdings.setCategory(DocCategory.WORK.getCode());
            holdings.setType(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode());
            holdings.setFormat(org.kuali.ole.docstore.common.document.content.enums.DocFormat.OLEML.getCode());
            holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
            holdings.setCreatedBy(initiatorName);
            Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibTree.getBib().getId());
            holdings.setBib(bib);
            holdingsTree.setHoldings(holdings);
            getDocstoreClientLocator().getDocstoreClient().createHoldingsTree(holdingsTree);
        } else {
            holdingsTree = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsTree(copy.getInstanceId());
        }
        Map copyMap = new HashMap();
        copyMap.put(org.kuali.ole.OLEConstants.DOC_NUM, singleItem.getDocumentNumber());
        OlePurchaseOrderDocument po = getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class, copyMap);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        int i = 0;
        for (org.kuali.ole.docstore.common.document.Item item : holdingsTree.getItems()) {
            org.kuali.ole.docstore.common.document.content.instance.Item itemXML = itemOlemlRecordProcessor.fromXML(item.getContent());
            KualiInteger itemPoId = new KualiInteger(itemXML.getPurchaseOrderLineItemIdentifier());
            KualiInteger poID = new KualiInteger(po.getPurapDocumentIdentifier());
            if (itemPoId.equals(poID)) {
              for (OleCopy oleCopy : copyList) {
                  if (copyIdList != null && copyIdList.size() > i && copyIdList.get(i).equals(oleCopy.getCopyId())) {
                      oleCopy.setInstanceId(holdingsTree.getHoldings().getId());
                      oleCopy.setItemUUID(item.getId());
                      this.copyCount++;
                      if (LOG.isDebugEnabled()) {
                        LOG.debug("Instance UUID" + holdingsTree.getHoldings().getId() + "****** Item UUID" + item.getId());
                      }
                  }
              }
              i++;
            }
        }
    }

    private String getLocationMatchedHoldingsId(Map<String, List<HoldingsDetails>> bibHoldingsDetailsMap, String location, String bibId) {
        List<HoldingsDetails> holdingsDetailses = bibHoldingsDetailsMap.get(bibId);
        if(CollectionUtils.isNotEmpty(holdingsDetailses)) {
            for (Iterator<HoldingsDetails> iterator = holdingsDetailses.iterator(); iterator.hasNext(); ) {
                HoldingsDetails holdingsDetails = iterator.next();
                String holdingsLocation = holdingsDetails.getLocation();
                if(StringUtils.isNotBlank(holdingsLocation) && StringUtils.isNotBlank(location) &&
                        location.equalsIgnoreCase(holdingsLocation)) {
                    return holdingsDetails.getHoldingsUUID();
                }
            }
        }
        return null;
    }

    private Map<String, List<HoldingsDetails>> getBibHoldingsDetailsMap(String bibUUID, String type) {
        Map<String, List<HoldingsDetails>> bibDetailsMap = new HashMap<>();
        String bibId = DocumentUniqueIDPrefix.getDocumentId(bibUUID);
        BibRecord matchedRecord = getBusinessObjectService().findBySinglePrimaryKey(BibRecord.class, bibId);
        if(null != matchedRecord) {
            List<HoldingsDetails> holdingsDetailses = new ArrayList<>();
            List<HoldingsRecord> holdingsRecords = matchedRecord.getHoldingsRecords();
            if(CollectionUtils.isNotEmpty(holdingsRecords)) {
                for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                    HoldingsRecord holdingsRecord = iterator.next();
                    String holdingsType = holdingsRecord.getHoldingsType();
                    if (StringUtils.isNotBlank(holdingsType) && holdingsType.equalsIgnoreCase(type)) {
                        HoldingsDetails holdingsDetails = new HoldingsDetails();
                        holdingsDetails.setHoldingsId(holdingsRecord.getHoldingsId());
                        holdingsDetails.setLocation(holdingsRecord.getLocation());
                        holdingsDetails.setBibId(bibUUID);
                        String holdingsUUID = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, String.valueOf(holdingsRecord.getHoldingsId()));
                        holdingsDetails.setHoldingsUUID(holdingsUUID);
                        holdingsDetailses.add(holdingsDetails);
                    }
                }
            }
            bibDetailsMap.put(bibUUID,holdingsDetailses);
        }
        return bibDetailsMap;
    }

    private String populateFundCodes(List<PurApAccountingLine> purApAccountingLines) {
        StringBuffer fundCode = new StringBuffer();
        List fundCodeList = new ArrayList();
        if (purApAccountingLines != null && purApAccountingLines.size() > 0) {
            for (PurApAccountingLine accountingLine : purApAccountingLines) {
                Map map = new HashMap();
                map.put(org.kuali.ole.OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
                map.put(org.kuali.ole.OLEConstants.OBJECT_CODE, accountingLine.getFinancialObjectCode());
                OleVendorAccountInfo oleVendorAccountInfo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleVendorAccountInfo.class, map);
                if (oleVendorAccountInfo != null && !fundCodeList.contains(oleVendorAccountInfo.getVendorRefNumber())) {
                    fundCodeList.add(oleVendorAccountInfo.getVendorRefNumber());
                    fundCode.append(oleVendorAccountInfo.getVendorRefNumber());
                    fundCode.append(org.kuali.ole.OLEConstants.COMMA);
                    fundCode.append(' ');
                }
            }
        }
        if (fundCode.length() > 0) {
            fundCode.deleteCharAt(fundCode.length() - 2);
            return fundCode.toString();
        }
        return null;
    }

    public boolean isValidLocation(String location) {
        if(StringUtils.isNotBlank(location)) {
            List<String> locationList = LocationValuesBuilder.retrieveLocationDetailsForSuggest(location);
            if (locationList != null && locationList.size() > 0) {
                for (String locationValue : locationList) {
                    if (locationValue.equalsIgnoreCase(location)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public DataCarrierService getDataCarrierService() {
        if(dataCarrierService == null){
            dataCarrierService = SpringContext.getBean(DataCarrierService.class);
        }
        return dataCarrierService;
    }

    class HoldingsDetails {
        private String holdingsId;
        private String holdingsUUID;
        private String location;
        private String bibId;

        public String getHoldingsId() {
            return holdingsId;
        }

        public void setHoldingsId(String holdingsId) {
            this.holdingsId = holdingsId;
        }

        public String getHoldingsUUID() {
            return holdingsUUID;
        }

        public void setHoldingsUUID(String holdingsUUID) {
            this.holdingsUUID = holdingsUUID;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getBibId() {
            return bibId;
        }

        public void setBibId(String bibId) {
            this.bibId = bibId;
        }
    }

}