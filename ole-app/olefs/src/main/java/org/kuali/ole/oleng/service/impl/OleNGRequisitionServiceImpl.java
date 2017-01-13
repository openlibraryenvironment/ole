package org.kuali.ole.oleng.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.Exchange;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.businessobject.RequisitionAccount;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.OleNoteType;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.businessobject.OleRequisitionNotes;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.DocumentSystemSaveEvent;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.businessobject.VendorContract;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.framework.postprocessor.IDocumentEvent;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by SheikS on 12/17/2015.
 */
@Service("oleNGRequisitionService")
public class OleNGRequisitionServiceImpl extends BusinessObjectServiceHelperUtil implements OleNGRequisitionService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleNGRequisitionServiceImpl.class);

    protected DocumentService documentService;
    private BatchUtil batchUtil;
    private OleNGMemorizeService oleNGMemorizeService;
    private OlePurapService olePurapService;

    @Override
    public OleRequisitionDocument createPurchaseOrderDocument(List<OleOrderRecord> oleOrderRecords, Exchange exchange) throws Exception {

        OleRequisitionDocument newRequisitionDocument = createNewRequisitionDocument();

        populateReqDocWithOrderInformation(newRequisitionDocument,oleOrderRecords, exchange);

        saveRequsitionDocument(newRequisitionDocument);

        routeRequisitionDocument(newRequisitionDocument);

        return newRequisitionDocument;
    }

    @Override
    public OleRequisitionDocument createNewRequisitionDocument() throws Exception {
        String user;
        if (GlobalVariables.getUserSession() == null) {
            user = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC,
                    OLEConstants.SELECT_CMPNT, OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR);
            if(LOG.isDebugEnabled()){
                LOG.debug("createNewRequisitionDocument - user from session"+user);
            }
            GlobalVariables.setUserSession(new UserSession(user));
        }
        return (OleRequisitionDocument) SpringContext.getBean(DocumentService.class).getNewDocument(org.kuali.ole.sys.OLEConstants.FinancialDocumentTypeCodes.REQUISITION);
    }

    @Override
    public OleRequisitionDocument populateReqDocWithOrderInformation(OleRequisitionDocument oleRequisitionDocument,
                                                                     List<OleOrderRecord> oleOrderRecords, Exchange exchange) throws Exception {
        setDocumentValues(oleRequisitionDocument, oleOrderRecords.get(0));

        oleRequisitionDocument.setItems(generateItemList(oleOrderRecords, exchange));

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
        if (oleRequisitionDocument.getOrderType() != null) {
            if (LOG.isDebugEnabled())
                LOG.debug("purchaseOrderType >>>>>>>>>>>" + oleRequisitionDocument.getOrderType().getPurchaseOrderType());
            getDocumentService().routeDocument(oleRequisitionDocument, null, null);
            LOG.debug("After Calling routeDocument >>>>>>>>>>>");
        }
        LOG.debug(IDocumentEvent.BEFORE_PROCESS);
        if (LOG.isInfoEnabled()) {
            LOG.info("Routing is done for Requisition document. Document Number: " + oleRequisitionDocument.getDocumentNumber());
        }
        return  oleRequisitionDocument;
    }

    private List<RequisitionItem> generateItemList(List<OleOrderRecord> oleOrderRecords, Exchange exchange) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        for(int index = 0; index < oleOrderRecords.size(); index++){
            int itemLineNumber = index + 1;
            OleOrderRecord oleOrderRecord = oleOrderRecords.get(index);
            try {
                items.add(createRequisitionItem(oleOrderRecord, itemLineNumber));
            } catch (Exception e) {
                e.printStackTrace();
                getBatchUtil().addOrderFaiureResponseToExchange(e, oleOrderRecord.getRecordIndex(), exchange);
            }
        }
        return items;
    }

    protected RequisitionItem createRequisitionItem(OleOrderRecord oleOrderRecord, int itemLineNumber) throws Exception {
        OleRequisitionItem item = new OleRequisitionItem();
        OleBibRecord oleBibRecord = oleOrderRecord.getOleBibRecord();
        OleTxRecord oleTxRecord = oleOrderRecord.getOleTxRecord();

        item.setItemStatus(oleTxRecord.getItemStatus());
        item.setOleOrderRecord(oleOrderRecord);
        item.setItemLineNumber(itemLineNumber);
        item.setItemUnitOfMeasureCode(getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.UOM));
        item.setItemQuantity(new KualiDecimal(oleTxRecord.getQuantity()));
        if (oleTxRecord.getItemNoOfParts() != null) {
            item.setItemNoOfParts(new KualiInteger(oleTxRecord.getItemNoOfParts()));
        }
        setItemDescription(oleOrderRecord, item);
        item.setItemTypeCode(oleTxRecord.getItemType());
        item.setItemListPrice(new KualiDecimal(oleTxRecord.getListPrice()));
        item.setSingleCopyNumber(oleTxRecord.getSingleCopyNumber());
        setDiscountAndItemUnitPrice(item, oleTxRecord);

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
            item.setRequestSourceTypeId(getOleNGMemorizeService().getRequestSourceTypeId(oleTxRecord.getRequestSourceType()));
        }

        setDonors(item, oleTxRecord);

        if (ObjectUtils.isNotNull(oleBibRecord.getBibUUID())) {
            item.setItemTitleId(oleBibRecord.getBibUUID());
        }
        setItemNotes(item, oleOrderRecord.getOleTxRecord());
        item.setVendorItemPoNumber(oleTxRecord.getVendorItemIdentifier());
        return item;
    }

    private void setDiscountAndItemUnitPrice(OleRequisitionItem item, OleTxRecord oleTxRecord) {
        item.setItemDiscount(new KualiDecimal(oleTxRecord.getDiscount()));
        item.setItemDiscountType(oleTxRecord.getDiscountType());
        if (item.getItemDiscount() != null && item.getItemDiscountType() == null) {
            item.setItemDiscountType(OLEConstants.PERCENTAGE);
        }
        item.setItemUnitPrice(getOlePurapService().calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    private void setItemDescription(OleOrderRecord oleOrderRecord, OleRequisitionItem item) throws Exception {

        Bib bib = oleOrderRecord.getOleBibRecord().getBib();
        String title = bib.getTitle() != null ? bib.getTitle() + "," : " ";
        String author = bib.getAuthor() != null ? bib.getAuthor() + "," : " ";
        String publisher = bib.getPublisher() != null ? bib.getPublisher() + "," : " ";
        String isbn = bib.getIsbn() != null ? bib.getIsbn() + "," : " ";
        String description = title + author
                + publisher + isbn;
        item.setItemDescription(description.substring(0, (description.lastIndexOf(","))));
        item.setItemTitle(bib.getTitle());
        item.setItemAuthor(bib.getAuthor());
        item.setBibUUID(oleOrderRecord.getOleBibRecord().getBibUUID());

    }

    private void setSourceAccountingLinesToReqItem(OleOrderRecord oleOrderRecord, OleRequisitionItem item) {
        if (oleOrderRecord.getOleTxRecord().getFundCode() != null) {
            Map fundCodeMap = new HashMap<>();
            fundCodeMap.put(OLEConstants.OLEEResourceRecord.FUND_CODE, oleOrderRecord.getOleTxRecord().getFundCode());
            List<OleFundCode> fundCodeList = (List) getBusinessObjectService().findMatching(OleFundCode.class, fundCodeMap);
            if (CollectionUtils.isNotEmpty(fundCodeList)) {
                List<PurApAccountingLine> sourceAccountingLines = item.getSourceAccountingLines();
                if (sourceAccountingLines == null) {
                    sourceAccountingLines = new ArrayList<>();
                }
                OleFundCode oleFundCode = fundCodeList.get(0);
                List<OleFundCodeAccountingLine> fundCodeAccountingLineList = oleFundCode.getOleFundCodeAccountingLineList();
                for (OleFundCodeAccountingLine oleFundCodeAccountingLine : fundCodeAccountingLineList) {
                    RequisitionAccount requisitionAccount = new RequisitionAccount();
                    requisitionAccount.setChartOfAccountsCode(oleFundCodeAccountingLine.getChartCode());
                    requisitionAccount.setAccountNumber(oleFundCodeAccountingLine.getAccountNumber());
                    if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getSubAccount())) {
                        requisitionAccount.setSubAccountNumber(oleFundCodeAccountingLine.getSubAccount());
                    }
                    requisitionAccount.setFinancialObjectCode(oleFundCodeAccountingLine.getObjectCode());
                    if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getSubObject())) {
                        requisitionAccount.setFinancialSubObjectCode(oleFundCodeAccountingLine.getSubObject());
                    }
                    if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getProject())) {
                        requisitionAccount.setProjectCode(oleFundCodeAccountingLine.getProject());
                    }
                    if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getOrgRefId())) {
                        requisitionAccount.setOrganizationReferenceId(oleFundCodeAccountingLine.getOrgRefId());
                    }
                    requisitionAccount.setAccountLinePercent(oleFundCodeAccountingLine.getPercentage());
                    requisitionAccount.setDebitCreditCode(OLEConstants.GL_DEBIT_CODE);
                    sourceAccountingLines.add(requisitionAccount);
                }
                item.setSourceAccountingLines(sourceAccountingLines);
            }
        } else {
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
    }

    protected RequisitionDocument setDocumentValues(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) throws Exception {
        requisitionDocument.setStatusCode(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);
        requisitionDocument.setVendorPoNumber(oleOrderRecord.getOleTxRecord().getVendorItemIdentifier());
        requisitionDocument.setChartOfAccountsCode(oleOrderRecord.getOleTxRecord().getChartCode());
        requisitionDocument.setOrganizationCode(oleOrderRecord.getOleTxRecord().getOrgCode());
        requisitionDocument.setDocumentFundingSourceCode(oleOrderRecord.getOleTxRecord().getFundingSource());
        requisitionDocument.setUseTaxIndicator(true);
        setDeliveryDetails(requisitionDocument, oleOrderRecord);
        requisitionDocument.setDeliveryCampusCode(oleOrderRecord.getOleTxRecord().getDeliveryCampusCode());
        setVendorDetails(requisitionDocument, oleOrderRecord);
        requisitionDocument.getDocumentHeader().setDocumentDescription(getDocumentDescription(requisitionDocument, oleOrderRecord));
        requisitionDocument.setPurchaseOrderTransmissionMethodCode(getOleNGMemorizeService().getTransmissionMethodCode(oleOrderRecord.getOleTxRecord().getMethodOfPOTransmission()));//FAX
        requisitionDocument.setPurchaseOrderCostSourceCode(oleOrderRecord.getOleTxRecord().getCostSource());//CON
        requisitionDocument.setRequestorPersonName(getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_NAME));
        requisitionDocument.setRequestorPersonPhoneNumber(getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_PHONE_NUMBER));
        requisitionDocument.setRequestorPersonEmailAddress(getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_EMAIL_ADDRESS));
        requisitionDocument.setOrganizationAutomaticPurchaseOrderLimit(new KualiDecimal(getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.VENDOR_CONTRACT_DEFAULT_APO_LIMIT)));
        requisitionDocument.setPurchaseOrderAutomaticIndicator(Boolean.parseBoolean(getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.PURCHASE_ORDER_AUTOMATIC_INDICATIOR)));
        requisitionDocument.setReceivingDocumentRequiredIndicator(oleOrderRecord.getOleTxRecord().isReceivingRequired());
        requisitionDocument.setPaymentRequestPositiveApprovalIndicator(oleOrderRecord.getOleTxRecord().isPayReqPositiveApprovalReq());
        requisitionDocument.setRequisitionSourceCode(oleOrderRecord.getOleTxRecord().getRequisitionSource());
        setOrderType(requisitionDocument, oleOrderRecord.getOleTxRecord());
        return requisitionDocument;

    }

    private void setOrderType(OleRequisitionDocument requisitionDocument, OleTxRecord oleTxRecord) {
        String key;
        String value;
        if (StringUtils.isNotBlank(oleTxRecord.getOrderType())) {
            key = OLEConstants.PO_TYPE;
            value = oleTxRecord.getOrderType();
        } else {
            key = "purchaseOrderTypeId";
            value = OLEConstants.DEFAULT_ORDER_TYPE_VALUE;
        }
        List<PurchaseOrderType> purchaseOrderTypeDocumentList = getOleNGMemorizeService().getOrderType(key,value);
        if (CollectionUtils.isNotEmpty(purchaseOrderTypeDocumentList)) {
            requisitionDocument.setPurchaseOrderTypeId(purchaseOrderTypeDocumentList.get(0).getPurchaseOrderTypeId());
            requisitionDocument.setOrderType(purchaseOrderTypeDocumentList.get(0));
        }
    }

    public String getDocumentDescription(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) {
        String description = getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.ORDER_IMPORT_REQ_DESC);
        Map<String, String> descMap = new HashMap<>();
        if (requisitionDocument.getVendorDetail().getVendorAliases() != null && requisitionDocument.getVendorDetail().getVendorAliases().size() > 0 &&
                requisitionDocument.getVendorDetail().getVendorAliases().get(0).getVendorAliasName() != null) {
            descMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_NAME, requisitionDocument.getVendorDetail().getVendorAliases().get(0).getVendorAliasName());
        }
        descMap.put(org.kuali.ole.sys.OLEConstants.ORDER_TYP, oleOrderRecord.getOleTxRecord().getOrderType());
        descMap.put(org.kuali.ole.sys.OLEConstants.VND_ITM_ID, oleOrderRecord.getOleTxRecord().getVendorItemIdentifier() != null && !oleOrderRecord.getOleTxRecord().getVendorItemIdentifier().isEmpty() ? oleOrderRecord.getOleTxRecord().getVendorItemIdentifier() + "_" : "");
        description = getOleNGMemorizeService().getOlePurapService().setDocumentDescription(description, descMap);
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

    private void setItemType(OleRequisitionItem item) {
        org.kuali.ole.module.purap.businessobject.ItemType itemType = getOleNGMemorizeService().getPurapItemType("ITEM");
        item.setItemType(itemType);
    }

    private void setDonors(OleRequisitionItem item, OleTxRecord oleTxRecord) {
        List<String> oleDonors = oleTxRecord.getOleDonors();
        if (CollectionUtils.isNotEmpty(oleDonors)) {
            List<OLELinkPurapDonor> oleLinkPurapDonorList = new ArrayList<>();
            for (String donor : oleDonors) {
                OLEDonor oleDonor = getOleNGMemorizeService().getDonorCode(donor);
                if (oleDonor != null) {
                    OLELinkPurapDonor oleLinkPurapDonor = new OLELinkPurapDonor();
                    oleLinkPurapDonor.setDonorCode(donor);
                    oleLinkPurapDonor.setDonorId(oleDonor.getDonorId());
                    oleLinkPurapDonorList.add(oleLinkPurapDonor);
                }
            }
            item.setOleDonors(oleLinkPurapDonorList);
        }
    }

    public void setDeliveryDetails(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) {
        if (LOG.isDebugEnabled())
            LOG.debug("bibInfoBean.getDeliveryBuildingCode----------->" + oleOrderRecord.getOleTxRecord().getBuildingCode());

        if (oleOrderRecord.getOleTxRecord().getDeliveryCampusCode() != null && oleOrderRecord.getOleTxRecord().getBuildingCode() != null && oleOrderRecord.getOleTxRecord().getDeliveryBuildingRoomNumber() != null) {
            Room room = getOleNGMemorizeService().getRoom(oleOrderRecord.getOleTxRecord().getBuildingCode(),
                    oleOrderRecord.getOleTxRecord().getDeliveryCampusCode(),
                    oleOrderRecord.getOleTxRecord().getDeliveryBuildingRoomNumber());
            Building building = getOleNGMemorizeService().getBuildingDetails(oleOrderRecord.getOleTxRecord().getDeliveryCampusCode(), oleOrderRecord.getOleTxRecord().getBuildingCode());
            if (building != null && room != null) {
                requisitionDocument.setDeliveryBuildingCode(building.getBuildingCode());
                requisitionDocument.setDeliveryCampusCode(building.getCampusCode());
                requisitionDocument.setDeliveryBuildingLine1Address(building.getBuildingStreetAddress());
                requisitionDocument.setDeliveryBuildingName(building.getBuildingName());
                requisitionDocument.setDeliveryCityName(building.getBuildingAddressCityName());
                requisitionDocument.setDeliveryStateCode(building.getBuildingAddressStateCode());
                requisitionDocument.setDeliveryPostalCode(building.getBuildingAddressZipCode());
                requisitionDocument.setDeliveryCountryCode(building.getBuildingAddressCountryCode());
                requisitionDocument.setDeliveryBuildingRoomNumber(room.getBuildingRoomNumber());
                requisitionDocument.setDeliveryToName(getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.DELIVERY_TO_NAME));
                requisitionDocument.setBillingCountryCode(building.getBuildingCode());
                requisitionDocument.setBillingLine1Address(building.getBuildingStreetAddress());
                requisitionDocument.setBillingName(building.getBuildingName());
                requisitionDocument.setBillingCityName(building.getBuildingAddressCityName());
                requisitionDocument.setBillingStateCode(building.getBuildingAddressStateCode());
                requisitionDocument.setBillingPostalCode(building.getBuildingAddressZipCode());
                requisitionDocument.setBillingCountryCode(building.getBuildingAddressCountryCode());
                requisitionDocument.setBillingPhoneNumber(getOleNGMemorizeService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_PHN_NBR));

            }
        }
    }

    public void setVendorDetails(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) {
        VendorDetail vendorDetail = null;
        if (StringUtils.isNotBlank(oleOrderRecord.getOleTxRecord().getVendorNumber())) {
            vendorDetail = getOleNGMemorizeService().getVendorService().getVendorDetail(oleOrderRecord.getOleTxRecord().getVendorNumber());
        }else if (StringUtils.isNotBlank(oleOrderRecord.getOleTxRecord().getVendorAliasName())){
            List<VendorAlias> vendorAliasList = getOleNGMemorizeService().getVendorAlias(oleOrderRecord.getOleTxRecord().getVendorAliasName());
            if (CollectionUtils.isNotEmpty(vendorAliasList)){
                vendorDetail = getOleNGMemorizeService().getVendorDetail(vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier(), vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
            }
        }
        if (vendorDetail!=null){
            requisitionDocument.setVendorCustomerNumber(oleOrderRecord.getOleTxRecord().getVendorInfoCustomer());
            requisitionDocument.setVendorNumber(oleOrderRecord.getOleTxRecord().getVendorNumber());
            requisitionDocument.setVendorNumber(vendorDetail.getVendorNumber());
            requisitionDocument.setVendorName(vendorDetail.getVendorName());
            requisitionDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            requisitionDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            requisitionDocument.setVendorDetail(vendorDetail);
            String deliveryCampus = oleOrderRecord.getOleTxRecord().getDeliveryCampusCode();
            Integer headerId = null;
            Integer detailId = null;
            int dashInd = vendorDetail.getVendorNumber().indexOf('-');
            // make sure there's at least one char before and after '-'
            if (dashInd > 0 && dashInd < vendorDetail.getVendorNumber().length() - 1) {
                headerId = new Integer(vendorDetail.getVendorNumber().substring(0, dashInd));
                detailId = new Integer(vendorDetail.getVendorNumber().substring(dashInd + 1));
            }
            VendorAddress vendorAddress = getOleNGMemorizeService().getVendorDefaultAddress(headerId, detailId, VendorConstants.AddressTypes.PURCHASE_ORDER, deliveryCampus);
            setVendorAddress(vendorAddress, requisitionDocument);

            List<VendorContract> vendorContracts = vendorDetail.getVendorContracts();
            for (Iterator<VendorContract> vendorContract = vendorContracts.iterator(); vendorContract.hasNext(); ) {
                requisitionDocument.setVendorContractGeneratedIdentifier((vendorContract.next()).getVendorContractGeneratedIdentifier());
            }
        }

    }

    public void setVendorAddress(VendorAddress vendorAddress, RequisitionDocument requisitionDocument) {

        if (vendorAddress != null) {
            requisitionDocument.setVendorAddressGeneratedIdentifier(vendorAddress.getVendorAddressGeneratedIdentifier());
            requisitionDocument.setVendorAddressInternationalProvinceName(vendorAddress.getVendorAddressInternationalProvinceName());
            requisitionDocument.setVendorLine1Address(vendorAddress.getVendorLine1Address());
            requisitionDocument.setVendorLine2Address(vendorAddress.getVendorLine2Address());
            requisitionDocument.setVendorCityName(vendorAddress.getVendorCityName());
            requisitionDocument.setVendorStateCode(vendorAddress.getVendorStateCode());
            requisitionDocument.setVendorPostalCode(vendorAddress.getVendorZipCode());
            requisitionDocument.setVendorCountryCode(vendorAddress.getVendorCountryCode());
        }

    }

    private void setItemNotes(OleRequisitionItem item, OleTxRecord oleTxRecord) {
        if (StringUtils.isNotBlank(oleTxRecord.getMiscellaneousNote())) {
            setNoteTypeValues(item, oleTxRecord.getMiscellaneousNote(), oleTxRecord.getMiscellaneousNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getReceiptNote())) {
            setNoteTypeValues(item, oleTxRecord.getReceiptNote(), oleTxRecord.getReceiptNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getRequestorNote())) {
            setNoteTypeValues(item, oleTxRecord.getRequestorNote(), oleTxRecord.getRequestorNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getSelectorNote())) {
            setNoteTypeValues(item, oleTxRecord.getSelectorNote(), oleTxRecord.getSelectorNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getSplProcessInstrNote())) {
            setNoteTypeValues(item, oleTxRecord.getSplProcessInstrNote(), oleTxRecord.getSplProcessInstrNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getVendorInstrNote())) {
            setNoteTypeValues(item, oleTxRecord.getVendorInstrNote(), oleTxRecord.getVendorInstrNotes());
        }
    }

    private void setNoteTypeValues(OleRequisitionItem item, String noteType, List<String> noteValues) {
        Map notes = new HashMap();
        notes.put(OLEConstants.NOTE_TYP, noteType);
        List<OleNoteType> noteTypeList = (List) getBusinessObjectService().findMatching(org.kuali.ole.select.businessobject.OleNoteType.class, notes);
        if (noteTypeList != null && noteTypeList.size() > 0) {
            for (int noteCount = 0; noteCount < noteValues.size(); noteCount++) {
                OleRequisitionNotes note = new OleRequisitionNotes();
                note.setNoteTypeId(noteTypeList.get(0).getNoteTypeId());
                note.setNote(noteValues.get(noteCount));
                item.getNotes().add(note);
            }
        }
    }

    public DocumentService getDocumentService() {
        if(null == documentService) {
            documentService = (DocumentService) SpringContext.getService("documentService");
        }
        return documentService;
    }

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }

    public OleNGMemorizeService getOleNGMemorizeService() {
        if(null == oleNGMemorizeService) {
            oleNGMemorizeService = new OleNGMemorizeServiceImpl();
        }
        return oleNGMemorizeService;
    }

    public void setOleNGMemorizeService(OleNGMemorizeService oleNGMemorizeService) {
        this.oleNGMemorizeService = oleNGMemorizeService;
    }

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }
}
