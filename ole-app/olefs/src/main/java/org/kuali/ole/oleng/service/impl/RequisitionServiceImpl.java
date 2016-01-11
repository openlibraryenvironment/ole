package org.kuali.ole.oleng.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.RequisitionAccount;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.oleng.service.RequisitionService;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.batch.service.RequisitionCreateDocumentService;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.service.impl.OleReqPOCreateDocumentServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/17/2015.
 */
@Service("requisitionService")
public class RequisitionServiceImpl implements RequisitionService {


    private BusinessObjectService businessObjectService;
    private OlePurapService olePurapService;
    private OleReqPOCreateDocumentServiceImpl oleReqPOCreateDocumentService;
    protected RequisitionCreateDocumentService requisitionCreateDocumentService;
    private PlatformTransactionManager transactionManager;
    
    @Override
    public OleRequisitionDocument createPurchaseOrderDocument(final OleOrderRecord oleOrderRecord) throws Exception {

        final OleRequisitionDocument requisitionDocument = getOleReqPOCreateDocumentService().createRequisitionDocument();

        setDocumentValues(requisitionDocument, oleOrderRecord);

        requisitionDocument.setItems(generateItemList(oleOrderRecord,requisitionDocument));


        requisitionDocument.setPurchaseOrderTypeId(OLEConstants.DEFAULT_ORDER_TYPE_VALUE);

        requisitionDocument.setApplicationDocumentStatus(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);

        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());

        try {
            template.execute(new TransactionCallback<Object>() {

                @Override
                public Object doInTransaction(TransactionStatus status) {
                    try {
                        getRequisitionCreateDocumentService().saveRequisitionDocuments(requisitionDocument);;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            this.transactionManager = null;
        }

        return requisitionDocument;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }

    private List<RequisitionItem> generateItemList(OleOrderRecord oleOrderRecord, OleRequisitionDocument requisitionDocument) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        int itemLineNumber = 1;
        items.add(createRequisitionItem(oleOrderRecord, itemLineNumber));
        return items;
    }

    protected RequisitionItem createRequisitionItem(OleOrderRecord oleOrderRecord, int itemLineNumber) throws Exception {
        OleRequisitionItem item = new OleRequisitionItem();
        item.setOleOrderRecord(oleOrderRecord);
        item.setItemLineNumber(itemLineNumber);
        item.setItemUnitOfMeasureCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.UOM));
        item.setItemQuantity(new KualiDecimal(oleOrderRecord.getOleTxRecord().getQuantity()));
        if(oleOrderRecord.getOleTxRecord().getItemNoOfParts()!= null){
            item.setItemNoOfParts(new KualiInteger(oleOrderRecord.getOleTxRecord().getItemNoOfParts()));
        }
        setItemDescription(oleOrderRecord,item);
        item.setItemUnitPrice(new BigDecimal(oleOrderRecord.getOleTxRecord().getListPrice()));
        item.setItemTypeCode(oleOrderRecord.getOleTxRecord().getItemType());
        item.setItemListPrice(new KualiDecimal(oleOrderRecord.getOleTxRecord().getListPrice()));
        if (ObjectUtils.isNotNull(oleOrderRecord.getOleBibRecord().getBibUUID())) {
            item.setItemTitleId(oleOrderRecord.getOleBibRecord().getBibUUID());
        }
        item.setBibTree(oleOrderRecord.getBibTree());
        item.setLinkToOrderOption(oleOrderRecord.getLinkToOrderOption());
        org.kuali.ole.module.purap.businessobject.ItemType itemType = getBusinessObjectService().findBySinglePrimaryKey(org.kuali.ole.module.purap.businessobject.ItemType.class, "ITEM");
        item.setItemType(itemType);
        setSourceAccountingLinesToReqItem(oleOrderRecord, item);
        setItemDescription(oleOrderRecord, item);
        return item;
    }

    private void setItemDescription(OleOrderRecord oleOrderRecord, OleRequisitionItem item) throws Exception {

        String title = oleOrderRecord.getOleBibRecord().getBib().getTitle() != null ? oleOrderRecord.getOleBibRecord().getBib().getTitle()+ "," : "";
        String author = oleOrderRecord.getOleBibRecord().getBib().getAuthor() != null ? oleOrderRecord.getOleBibRecord().getBib().getAuthor()+ "," : "";
        String publisher = oleOrderRecord.getOleBibRecord().getBib().getPublisher() != null ? oleOrderRecord.getOleBibRecord().getBib().getPublisher()+ "," : "";
        String isbn = oleOrderRecord.getOleBibRecord().getBib().getIsbn() != null ? oleOrderRecord.getOleBibRecord().getBib().getIsbn() + ",": "";
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
        String description =  getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.ORDER_IMPORT_REQ_DESC);
        Map<String,String> descMap = new HashMap<>();
        if(requisitionDocument.getVendorDetail().getVendorAliases() != null && requisitionDocument.getVendorDetail().getVendorAliases().size() > 0 && requisitionDocument.getVendorDetail().getVendorAliases().get(0).getVendorAliasName() != null){
            descMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_NAME,requisitionDocument.getVendorDetail().getVendorAliases().get(0).getVendorAliasName());
        }
        descMap.put(org.kuali.ole.sys.OLEConstants.ORDER_TYP,oleOrderRecord.getOleTxRecord().getOrderType());
        descMap.put(org.kuali.ole.sys.OLEConstants.VND_ITM_ID,oleOrderRecord.getOleTxRecord().getVendorItemIdentifier() != null && !oleOrderRecord.getOleTxRecord().getVendorItemIdentifier().isEmpty() ? oleOrderRecord.getOleTxRecord().getVendorItemIdentifier() + "_" : "");
        description = getOlePurapService().setDocumentDescription(description,descMap);
        if (!description.equals("") && description != null) {
            description = description.substring(0, description.lastIndexOf("_"));
        }
        if(description.startsWith("_")){
            description = description.substring(1);
        }
        if(description.length() > 255) {
            //TODO : failure Report
        }
        return description;
    }



    public OleReqPOCreateDocumentServiceImpl getOleReqPOCreateDocumentService() {
        if(null == oleReqPOCreateDocumentService) {
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

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public RequisitionCreateDocumentService getRequisitionCreateDocumentService() {
        if (requisitionCreateDocumentService == null) {
            requisitionCreateDocumentService = SpringContext.getBean(RequisitionCreateDocumentService.class);
        }
        return requisitionCreateDocumentService;
    }

    public void setRequisitionCreateDocumentService(RequisitionCreateDocumentService requisitionCreateDocumentService) {
        if(null == requisitionCreateDocumentService) {
            requisitionCreateDocumentService = SpringContext.getBean(RequisitionCreateDocumentService.class);
        }
        this.requisitionCreateDocumentService = requisitionCreateDocumentService;
    }
}
