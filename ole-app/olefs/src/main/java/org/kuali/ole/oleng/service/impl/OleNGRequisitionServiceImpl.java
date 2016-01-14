package org.kuali.ole.oleng.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.businessobject.RequisitionAccount;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.batch.service.RequisitionCreateDocumentService;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.service.impl.OleReqPOCreateDocumentServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.DocumentSystemSaveEvent;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.framework.postprocessor.IDocumentEvent;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/17/2015.
 */
@Service("oleNGRequisitionService")
public class OleNGRequisitionServiceImpl extends BusinessObjectServiceHelperUtil implements OleNGRequisitionService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleNGRequisitionServiceImpl.class);

    private OlePurapService olePurapService;
    private OleReqPOCreateDocumentServiceImpl oleReqPOCreateDocumentService;
    protected RequisitionCreateDocumentService requisitionCreateDocumentService;
    private DocstoreClientLocator docstoreClientLocator;
    private ConfigurationService kualiConfigurationService;

    protected DocumentService documentService;

    @Override
    public OleRequisitionDocument createPurchaseOrderDocument(final OleOrderRecord oleOrderRecord) throws Exception {

        final OleRequisitionDocument requisitionDocument = getOleReqPOCreateDocumentService().createRequisitionDocument();

        setDocumentValues(requisitionDocument, oleOrderRecord);

        requisitionDocument.setItems(generateItemList(oleOrderRecord, requisitionDocument));


        requisitionDocument.setPurchaseOrderTypeId(OLEConstants.DEFAULT_ORDER_TYPE_VALUE);

        requisitionDocument.setApplicationDocumentStatus(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);

        getRequisitionCreateDocumentService().saveRequisitionDocuments(requisitionDocument);

        return requisitionDocument;
    }

    @Override
    public OleRequisitionDocument createNewRequisitionDocument() throws Exception {
        String user;
        if (GlobalVariables.getUserSession() == null) {
            user = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC,
                    OLEConstants.SELECT_CMPNT,OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR);
            if(LOG.isDebugEnabled()){
                LOG.debug("createNewRequisitionDocument - user from session"+user);
            }
            GlobalVariables.setUserSession(new UserSession(user));
        }
        return (OleRequisitionDocument) SpringContext.getBean(DocumentService.class).getNewDocument(org.kuali.ole.sys.OLEConstants.FinancialDocumentTypeCodes.REQUISITION);
    }

    @Override
    public OleRequisitionDocument setValueToRequisitionDocuemnt(OleRequisitionDocument oleRequisitionDocument, OleOrderRecord oleOrderRecord) throws Exception {
        setDocumentValues(oleRequisitionDocument, oleOrderRecord);

        oleRequisitionDocument.setItems(generateItemList(oleOrderRecord, oleRequisitionDocument));

        oleRequisitionDocument.setPurchaseOrderTypeId(OLEConstants.DEFAULT_ORDER_TYPE_VALUE);

        oleRequisitionDocument.setApplicationDocumentStatus(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);

        return oleRequisitionDocument;
    }

    @Override
    public OleRequisitionDocument saveRequsitionDocument(OleRequisitionDocument oleRequisitionDocument) {
        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("Calling saveRequisitionDocuments in OleNGRequisitionServiceImpl >>>>" + oleRequisitionDocument.getDocumentNumber());
            }
            try {
                getDocumentService().saveDocument(oleRequisitionDocument, DocumentSystemSaveEvent.class);
            } catch (Exception e) {
                LOG.error("Exection while saving requisition document" + e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("Error persisting document # " + oleRequisitionDocument.getDocumentHeader().getDocumentNumber() + " " + e.getMessage(), e);
            throw new RuntimeException("Error persisting document # " + oleRequisitionDocument.getDocumentHeader().getDocumentNumber() + " " + e.getMessage(), e);
        }
        return oleRequisitionDocument;
    }

    @Override
    public OleRequisitionDocument routeRequisitionDocument(OleRequisitionDocument oleRequisitionDocument) throws Exception {
        oleRequisitionDocument.populateDocumentForRouting();
        String purchaseOrderType = "";
        if (oleRequisitionDocument.getPurchaseOrderTypeId() != null) {
            Map purchaseOrderTypeIdMap = new HashMap();
            purchaseOrderTypeIdMap.put("purchaseOrderTypeId", oleRequisitionDocument.getPurchaseOrderTypeId());
            List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) getBusinessObjectService().findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);
            if (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0) {
                PurchaseOrderType purchaseOrderTypeDoc = (PurchaseOrderType) purchaseOrderTypeDocumentList.get(0);
                purchaseOrderType = purchaseOrderTypeDoc.getPurchaseOrderType();
            }
            if (LOG.isDebugEnabled())
                LOG.debug("purchaseOrderType >>>>>>>>>>>" + purchaseOrderType);
            getDocumentService().routeDocument(oleRequisitionDocument, null, null);
            LOG.debug("After Calling routeDocument >>>>>>>>>>>");
        }
        LOG.debug(IDocumentEvent.BEFORE_PROCESS);
        if (LOG.isInfoEnabled()) {
            LOG.info("Routing is done for Requisition document. Document Number: " + oleRequisitionDocument.getDocumentNumber());
        }
        return  oleRequisitionDocument;
    }

    private List<RequisitionItem> generateItemList(OleOrderRecord oleOrderRecord, OleRequisitionDocument requisitionDocument) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        int itemLineNumber = 1;
        items.add(createRequisitionItem(oleOrderRecord, itemLineNumber));
        return items;
    }

    protected RequisitionItem createRequisitionItem(OleOrderRecord oleOrderRecord, int itemLineNumber) throws Exception {
        OleRequisitionItem item = new OleRequisitionItem();
        OleBibRecord oleBibRecord = oleOrderRecord.getOleBibRecord();
        OleTxRecord oleTxRecord = oleOrderRecord.getOleTxRecord();

        item.setItemStatus(oleTxRecord.getItemStatus());
        item.setOleOrderRecord(oleOrderRecord);
        item.setItemLineNumber(itemLineNumber);
        item.setItemUnitOfMeasureCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.UOM));
        item.setItemQuantity(new KualiDecimal(oleTxRecord.getQuantity()));
        if (oleTxRecord.getItemNoOfParts() != null) {
            item.setItemNoOfParts(new KualiInteger(oleTxRecord.getItemNoOfParts()));
        }
        setItemDescription(oleOrderRecord, item);
        item.setItemUnitPrice(new BigDecimal(oleTxRecord.getListPrice()));
        item.setItemTypeCode(oleTxRecord.getItemType());
        item.setItemListPrice(new KualiDecimal(oleTxRecord.getListPrice()));
        if (ObjectUtils.isNotNull(oleBibRecord.getBibUUID())) {
            item.setItemTitleId(oleBibRecord.getBibUUID());
        }
        item.setLinkToOrderOption(oleOrderRecord.getLinkToOrderOption());

        setItemType(item);

        setSourceAccountingLinesToReqItem(oleOrderRecord, item);
        item.setItemLocation(oleTxRecord.getDefaultLocation());

        if(!StringUtils.isBlank(oleTxRecord.getFormatTypeId())){
            item.setFormatTypeId(Integer.parseInt(oleTxRecord.getFormatTypeId()));
        }
        if(oleTxRecord.getRequestSourceType() != null){
            item.setRequestSourceTypeId(getRequestSourceTypeId(oleTxRecord.getRequestSourceType()));
        }

        setDonors(item, oleTxRecord);

        if (ObjectUtils.isNotNull(oleBibRecord.getBibUUID())) {
            item.setItemTitleId(oleBibRecord.getBibUUID());
        }
        item.setVendorItemPoNumber(oleTxRecord.getVendorItemIdentifier());
        return item;
    }

    private void setItemDescription(OleOrderRecord oleOrderRecord, OleRequisitionItem item) throws Exception {

        String title = oleOrderRecord.getOleBibRecord().getBib().getTitle() != null ? oleOrderRecord.getOleBibRecord().getBib().getTitle() + "," : "";
        String author = oleOrderRecord.getOleBibRecord().getBib().getAuthor() != null ? oleOrderRecord.getOleBibRecord().getBib().getAuthor() + "," : "";
        String publisher = oleOrderRecord.getOleBibRecord().getBib().getPublisher() != null ? oleOrderRecord.getOleBibRecord().getBib().getPublisher() + "," : "";
        String isbn = oleOrderRecord.getOleBibRecord().getBib().getIsbn() != null ? oleOrderRecord.getOleBibRecord().getBib().getIsbn() + "," : "";
        String description = title + author
                + publisher + isbn;
        item.setItemDescription(description.substring(0, (description.lastIndexOf(","))));
        item.setItemTitle(oleOrderRecord.getOleBibRecord().getBib().getTitle());
        item.setItemAuthor(oleOrderRecord.getOleBibRecord().getBib().getAuthor());
        item.setBibUUID(oleOrderRecord.getOleBibRecord().getBibUUID());

    }

    private void setSourceAccountingLinesToReqItem(OleOrderRecord oleOrderRecord, OleRequisitionItem item) {
        RequisitionAccount requisitionAccount = new RequisitionAccount();
        requisitionAccount.setChartOfAccountsCode(oleOrderRecord.getOleTxRecord().getItemChartCode());
        requisitionAccount.setAccountNumber(oleOrderRecord.getOleTxRecord().getAccountNumber());
        requisitionAccount.setFinancialObjectCode(oleOrderRecord.getOleTxRecord().getObjectCode());
        requisitionAccount.setDebitCreditCode(org.kuali.ole.OLEConstants.GL_DEBIT_CODE);
        if (oleOrderRecord.getOleTxRecord().getListPrice() != null) {
            requisitionAccount.setAmount(new KualiDecimal(oleOrderRecord.getOleTxRecord().getListPrice()));
        }
        if (oleOrderRecord.getOleTxRecord().getPercent() != null) {
            requisitionAccount.setAccountLinePercent(new BigDecimal(oleOrderRecord.getOleTxRecord().getPercent()));
        }
        if (oleOrderRecord.getOleTxRecord().getAccountNumber() != null) {
            List<PurApAccountingLine> sourceAccountingLines = item.getSourceAccountingLines();
            if (sourceAccountingLines.size() == 0) {
                sourceAccountingLines = new ArrayList<>(0);
            }
            sourceAccountingLines.add(requisitionAccount);
            item.setSourceAccountingLines(sourceAccountingLines);
        }
    }

    protected RequisitionDocument setDocumentValues(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) throws Exception {
        requisitionDocument.setStatusCode(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);
        requisitionDocument.setVendorPoNumber(oleOrderRecord.getOleTxRecord().getVendorItemIdentifier());
        requisitionDocument.setChartOfAccountsCode(oleOrderRecord.getOleTxRecord().getChartCode());
        requisitionDocument.setOrganizationCode(oleOrderRecord.getOleTxRecord().getOrgCode());
        requisitionDocument.setDocumentFundingSourceCode(oleOrderRecord.getOleTxRecord().getFundingSource());
        requisitionDocument.setUseTaxIndicator(true);
        getOleReqPOCreateDocumentService().setDeliveryDetails(requisitionDocument, oleOrderRecord);
        requisitionDocument.setDeliveryCampusCode(oleOrderRecord.getOleTxRecord().getDeliveryCampusCode());
        getOleReqPOCreateDocumentService().setVendorDetails(requisitionDocument, oleOrderRecord);
        requisitionDocument.getDocumentHeader().setDocumentDescription(getDocumentDescription(requisitionDocument, oleOrderRecord));
        requisitionDocument.setPurchaseOrderTransmissionMethodCode(getOleReqPOCreateDocumentService().getTransmissionMethodCode(oleOrderRecord.getOleTxRecord().getMethodOfPOTransmission()));//FAX
        requisitionDocument.setPurchaseOrderCostSourceCode(oleOrderRecord.getOleTxRecord().getCostSource());//CON
        requisitionDocument.setRequestorPersonName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_NAME));
        requisitionDocument.setRequestorPersonPhoneNumber(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_PHONE_NUMBER));
        requisitionDocument.setRequestorPersonEmailAddress(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_EMAIL_ADDRESS));
        requisitionDocument.setOrganizationAutomaticPurchaseOrderLimit(new KualiDecimal(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.VENDOR_CONTRACT_DEFAULT_APO_LIMIT)));
        requisitionDocument.setPurchaseOrderAutomaticIndicator(Boolean.parseBoolean(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.PURCHASE_ORDER_AUTOMATIC_INDICATIOR)));
        requisitionDocument.setReceivingDocumentRequiredIndicator(oleOrderRecord.getOleTxRecord().isReceivingRequired());
        requisitionDocument.setPaymentRequestPositiveApprovalIndicator(oleOrderRecord.getOleTxRecord().isPayReqPositiveApprovalReq());
        requisitionDocument.setRequisitionSourceCode(oleOrderRecord.getOleTxRecord().getRequisitionSource());
        return requisitionDocument;

    }

    public String getDocumentDescription(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) {
        String description = getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.ORDER_IMPORT_REQ_DESC);
        Map<String, String> descMap = new HashMap<>();
        if (requisitionDocument.getVendorDetail().getVendorAliases() != null && requisitionDocument.getVendorDetail().getVendorAliases().size() > 0 && requisitionDocument.getVendorDetail().getVendorAliases().get(0).getVendorAliasName() != null) {
            descMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_NAME, requisitionDocument.getVendorDetail().getVendorAliases().get(0).getVendorAliasName());
        }
        descMap.put(org.kuali.ole.sys.OLEConstants.ORDER_TYP, oleOrderRecord.getOleTxRecord().getOrderType());
        descMap.put(org.kuali.ole.sys.OLEConstants.VND_ITM_ID, oleOrderRecord.getOleTxRecord().getVendorItemIdentifier() != null && !oleOrderRecord.getOleTxRecord().getVendorItemIdentifier().isEmpty() ? oleOrderRecord.getOleTxRecord().getVendorItemIdentifier() + "_" : "");
        description = getOlePurapService().setDocumentDescription(description, descMap);
        if (!description.equals("") && description != null) {
            description = description.substring(0, description.lastIndexOf("_"));
        }
        if (description.startsWith("_")) {
            description = description.substring(1);
        }
        if (description.length() > 255) {
            //TODO : failure Report
        }
        return description;
    }


    private Integer getRequestSourceTypeId(String requestSourceType){
        Map<String,String> requestSourceMap = new HashMap<>();
        requestSourceMap.put(OLEConstants.OLEBatchProcess.REQUEST_SRC,requestSourceType);
        List<OleRequestSourceType> requestSourceList = (List) getBusinessObjectService().findMatching(OleRequestSourceType.class, requestSourceMap);
        if(requestSourceList != null && requestSourceList.size() > 0){
            return requestSourceList.get(0).getRequestSourceTypeId();
        }
        return null;
    }

    private void setItemType(OleRequisitionItem item) {
        org.kuali.ole.module.purap.businessobject.ItemType itemType = getBusinessObjectService().findBySinglePrimaryKey(org.kuali.ole.module.purap.businessobject.ItemType.class, "ITEM");
        item.setItemType(itemType);
    }

    private void setDonors(OleRequisitionItem item, OleTxRecord oleTxRecord) {
        List<String> oleDonors = oleTxRecord.getOleDonors();
        if (CollectionUtils.isNotEmpty(oleDonors)) {
            List<OLELinkPurapDonor> oleLinkPurapDonorList = new ArrayList<>();
            for (String donor : oleDonors) {
                Map map = new HashMap();
                map.put(OLEConstants.DONOR_CODE, donor);
                OLEDonor oleDonor = getBusinessObjectService().findByPrimaryKey(OLEDonor.class, map);
                OLELinkPurapDonor oleLinkPurapDonor = new OLELinkPurapDonor();
                oleLinkPurapDonor.setDonorCode(donor);
                if (oleDonor != null) {
                    oleLinkPurapDonor.setDonorId(oleDonor.getDonorId());
                }
                oleLinkPurapDonorList.add(oleLinkPurapDonor);
            }
            item.setOleDonors(oleLinkPurapDonorList);
        }
    }


    public OleReqPOCreateDocumentServiceImpl getOleReqPOCreateDocumentService() {
        if (null == oleReqPOCreateDocumentService) {
            oleReqPOCreateDocumentService = (OleReqPOCreateDocumentServiceImpl) SpringContext.getService("oleReqPOCreateDocumentService");
        }
        return oleReqPOCreateDocumentService;
    }

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public RequisitionCreateDocumentService getRequisitionCreateDocumentService() {
        if (requisitionCreateDocumentService == null) {
            requisitionCreateDocumentService = SpringContext.getBean(RequisitionCreateDocumentService.class);
        }
        return requisitionCreateDocumentService;
    }

    public void setRequisitionCreateDocumentService(RequisitionCreateDocumentService requisitionCreateDocumentService) {
        if (null == requisitionCreateDocumentService) {
            requisitionCreateDocumentService = SpringContext.getBean(RequisitionCreateDocumentService.class);
        }
        this.requisitionCreateDocumentService = requisitionCreateDocumentService;
    }


    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public DocumentService getDocumentService() {
        if(null == documentService) {
            documentService = (DocumentService) SpringContext.getService("documentService");
        }
        return documentService;
    }
}
