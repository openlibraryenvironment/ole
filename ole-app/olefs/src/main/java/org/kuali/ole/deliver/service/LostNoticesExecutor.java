package org.kuali.ole.deliver.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.executors.LoanNoticesExecutor;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.docstore.common.document.DocstoreDocument;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.ItemStatus;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 4/8/15.
 */
public class LostNoticesExecutor extends LoanNoticesExecutor {
    private static final Logger LOG = Logger.getLogger(LostNoticesExecutor.class);
    private NoticeMailContentFormatter noticeMailContentFormatter;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;
    private PatronBillHelperService patronBillHelperService;
    String note= null;


    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if (itemOlemlRecordProcessor == null) {
            itemOlemlRecordProcessor = SpringContext.getBean(ItemOlemlRecordProcessor.class);
        }
        return itemOlemlRecordProcessor;
    }

    public LostNoticesExecutor(Map lostMap) {
        super(lostMap);
        if(lostMap.get(OLEConstants.BILL_NOTE) != null) {
            note = lostMap.get(OLEConstants.BILL_NOTE).toString();
        }

    }

    @Override
    protected String getNoticeType() {
        return OLEConstants.NOTICE_LOST;
    }

    @Override
    protected void postProcess(List<OleLoanDocument> loanDocuments) {
        List<String> itemUUIDS = new ArrayList<String>();
        for (Iterator<OleLoanDocument> iterator = loanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument loanDocument = iterator.next();
            for (OLEDeliverNotice oleDeliverNotice : loanDocument.getDeliverNotices()) {
                LOG.info("LostNoticesExecutor thread id---->" + Thread.currentThread().getId() + "current thread---->" + Thread.currentThread() + "Loan id-->" + loanDocument.getLoanId() + "notice id--->" + oleDeliverNotice.getId());
                Timestamp toBeSendDate = oleDeliverNotice.getNoticeToBeSendDate();
                if (oleDeliverNotice.getNoticeType().equals(OLEConstants.NOTICE_LOST) && (toBeSendDate.compareTo(getSendToDate(OLEConstants.LOST_NOTICE_TO_DATE)) <
                        0 || loanDocument.isManualBill())) {
                    try {
                        itemUUIDS.add(loanDocument.getItemUuid());
                    } catch (Exception e) {
                        Log.info(e.getStackTrace());
                    }
                }
            }
        }
        itemStatusBulkUpdate(itemUUIDS);
    }

    @Override
    protected void preProcess(List<OleLoanDocument> loanDocuments) {
        for (Iterator<OleLoanDocument> iterator = loanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument loanDocument = iterator.next();
            Map fineAmounts = getFineAmounts(loanDocument);
            String patronBillPayment = getPatronBillPayment(loanDocument, fineAmounts);
            loanDocument.setRepaymentFeePatronBillId(patronBillPayment);
        }
    }

    private Map getFineAmounts(OleLoanDocument oleLoanDocument) {
        Map fineAmounts = new HashMap<>();
        for (Iterator<OLEDeliverNotice> iterator = oleLoanDocument.getDeliverNotices().iterator(); iterator.hasNext(); ) {
            OLEDeliverNotice deliverNotice = iterator.next();
            if (deliverNotice.getNoticeType().equals(OLEConstants.NOTICE_LOST)) {
                if (deliverNotice.getReplacementFeeAmount() != null) {
                    fineAmounts.put(OLEConstants.REPLACEMENT_FEE, deliverNotice.getReplacementFeeAmount());
                }
                if (deliverNotice.getLostItemProcessingFeeAmount() != null) {
                    fineAmounts.put(OLEConstants.LOST_ITEM_PROCESSING_FEE, deliverNotice.getLostItemProcessingFeeAmount());
                }
                break;
            }
        }
        return fineAmounts;
    }

    private String getPatronBillPayment(OleLoanDocument oleLoanDocument, Map<String, BigDecimal> fineAmounts) {
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        BigDecimal totalAmount = new BigDecimal(0);
        for (String feeTypeName : fineAmounts.keySet()) {
            BigDecimal fineAmount = fineAmounts.get(feeTypeName);
            if (StringUtils.isNotBlank(feeTypeName) && fineAmount != null && fineAmount.intValue() > 0) {
                FeeType feeType = getFeeType(oleLoanDocument, fineAmount, feeTypeName);
                feeTypes.add(feeType);
                totalAmount = totalAmount.add(fineAmount);
            }
        }

        if (CollectionUtils.isNotEmpty(feeTypes)) {
            java.util.Date billdate = new java.util.Date();
            PatronBillPayment patronBillPayment = new PatronBillPayment();
            patronBillPayment.setManualProcessBill(oleLoanDocument.isManualBill());
            patronBillPayment.setBillDate(oleLoanDocument.getCheckInDate() != null ? new java.sql.Date(oleLoanDocument.getCheckInDate().getTime()) : new java.sql.Date(billdate.getTime()));
            patronBillPayment.setFeeType(feeTypes);
            //commented for jira OLE-5675
            patronBillPayment.setPatronId(oleLoanDocument.getPatronId());
            patronBillPayment.setProxyPatronId(oleLoanDocument.getProxyPatronId());
            patronBillPayment.setTotalAmount(new KualiDecimal(totalAmount));
            patronBillPayment.setUnPaidBalance(new KualiDecimal(totalAmount));
            oleLoanDocument.setReplacementBill(totalAmount);
            if (null != oleLoanDocument.getItemLostNote()) {
                patronBillPayment.setNote(oleLoanDocument.getItemLostNote());
            }
            PatronBillPayment patronBillPayments = getBusinessObjectService().save(patronBillPayment);
            return patronBillPayments.getBillNumber();
        }
        return null;
    }

    private FeeType getFeeType(OleLoanDocument oleLoanDocument, BigDecimal fineAmount, String feeTypeName) {
        FeeType feeType = new FeeType();
        if(note != null) {
            feeType.setGeneralNote(note);
        }
        feeType.setFeeType(getFeeTypeId(feeTypeName));
        feeType.setFeeAmount(new KualiDecimal(fineAmount));
        feeType.setItemBarcode(oleLoanDocument.getItemId());
        feeType.setItemUuid(oleLoanDocument.getItemUuid());
        getPatronBillHelperService().setFeeTypeInfo(feeType, oleLoanDocument.getItemUuid());
        feeType.setPaymentStatus(getOlePaymentStatus().getPaymentStatusId());
        feeType.setBalFeeAmount(new KualiDecimal(fineAmount));
        feeType.setFeeSource(OLEConstants.SYSTEM);
        feeType.setDueDate(oleLoanDocument.getLoanDueDate());
        feeType.setCheckInDate(oleLoanDocument.getCheckInDate());
        feeType.setCheckOutDate(oleLoanDocument.getCreateDate());
        feeType.setManualProcessBill(oleLoanDocument.isManualBill());
        if (null != oleLoanDocument.getItemLostNote()) {
            feeType.setGeneralNote(oleLoanDocument.getItemLostNote());
        }
        return feeType;
    }


    private String getFeeTypeId(String feeTypeName) {
        Map feeMap = new HashMap();
        feeMap.put("feeTypeName", feeTypeName);
        List<OleFeeType> oleFeeTypes = (List<OleFeeType>) getBusinessObjectService().findMatching(OleFeeType.class, feeMap);
        return oleFeeTypes != null && oleFeeTypes.size() > 0 ? oleFeeTypes.get(0).getFeeTypeId() : null;
    }

    public OlePaymentStatus getOlePaymentStatus() {
        Map statusMap = new HashMap();
        statusMap.put("paymentStatusName", OLEConstants.PAYMENT_STATUS_OUTSTANDING);
        List<OlePaymentStatus> olePaymentStatusList = (List<OlePaymentStatus>) getBusinessObjectService().findMatching(OlePaymentStatus.class, statusMap);
        return olePaymentStatusList != null && olePaymentStatusList.size() > 0 ? olePaymentStatusList.get(0) : null;
    }


    @Override
    public List<OLEDeliverNotice> buildNoticesForDeletion() {
        List<OLEDeliverNotice> oleDeliverNotices = new ArrayList<>();

        for (OleLoanDocument loanDocument : loanDocuments) {

            if (loanDocument.getItemTypeName() != null) {
                loanDocument.setItemType(getItemTypeCodeByName(loanDocument.getItemTypeName()));
            }
            Timestamp lostNoticetoSendDate = getSendToDate(OLEConstants.LOST_NOTICE_TO_DATE);
            for (OLEDeliverNotice oleDeliverNotice : loanDocument.getDeliverNotices()) {
                LOG.info("LostNoticesExecutor thread id---->" + Thread.currentThread().getId() + "current thread---->" + Thread.currentThread() + "Loan id-->" + loanDocument.getLoanId() + "notice id--->" + oleDeliverNotice.getId());
                Timestamp toBeSendDate = oleDeliverNotice.getNoticeToBeSendDate();
                if (oleDeliverNotice.getNoticeType().equals(OLEConstants.NOTICE_LOST) && (toBeSendDate.compareTo(lostNoticetoSendDate) < 0 || loanDocument.isManualBill())) {
                    try {
                        //itemUUIDS.add(loanDocument.getItemUuid());
                        oleDeliverNotices.add(oleDeliverNotice);
                    } catch (Exception e) {
                        Log.info(e.getStackTrace());
                    }
                }
            }
        }
        return oleDeliverNotices;
    }
    @Override
    public void setOleNoticeContentConfigurationBo() {
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String,String> noticeConfigurationMap = new HashMap<String,String>();
        noticeConfigurationMap.put("noticeType",OLEConstants.NOTICE_LOST);
        noticeConfigurationMap.put("noticeName", noticeContentConfigName);
        oleNoticeContentConfigurationBoList= (List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeConfigurationMap);
        if(oleNoticeContentConfigurationBoList!=null && oleNoticeContentConfigurationBoList.size()>0){
            oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
        }else{
            oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
            oleNoticeContentConfigurationBo.setNoticeTitle("LOST");
            oleNoticeContentConfigurationBo.setNoticeBody("Item is Lost");
            oleNoticeContentConfigurationBo.setNoticeFooterBody("");
            oleNoticeContentConfigurationBo.setNoticeType(OLEConstants.NOTICE_LOST);
        }
    }

    @Override
    public void setOleNoticeContentConfigurationBo(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        this.oleNoticeContentConfigurationBo=oleNoticeContentConfigurationBo;
    }

    @Override
    public String generateMailContent(List<OleLoanDocument> oleLoanDocuments) {
        String mailContent = getNoticeMailContentFormatter().generateMailContentForPatron(oleLoanDocuments,oleNoticeContentConfigurationBo);
        return mailContent;
    }

    private NoticeMailContentFormatter getNoticeMailContentFormatter() {
        if (null == noticeMailContentFormatter) {
            noticeMailContentFormatter = new ReplacementBillNoticeEmailContentFormattter();
        }
        return noticeMailContentFormatter;
    }

    public void setNoticeMailContentFormatter(NoticeMailContentFormatter noticeMailContentFormatter) {
        this.noticeMailContentFormatter = noticeMailContentFormatter;
    }


    private void itemStatusBulkUpdate(List<String> itemUUIDs) {
        List<org.kuali.ole.docstore.common.document.Item> items = null;
        DocstoreLocalClient docstoreLocalClient = new DocstoreLocalClient();
        try {
            items = docstoreLocalClient.retrieveItems(itemUUIDs);
            //items = docstoreLocalClient.retrieveItems(itemUUIDs);
        } catch (Exception e) {
            StringBuffer itemUUIDsBuffer = new StringBuffer();
            for (String itemUUID : itemUUIDs) {
                itemUUIDsBuffer.append(itemUUID + ",");
            }
            LOG.info("Exception occured while retrieving the item for updating the item status to the following itemIds : " + itemUUIDsBuffer.toString());
            LOG.error(e, e);
        }

        BibTrees bibTrees = new BibTrees();
        for (org.kuali.ole.docstore.common.document.Item item : items) {
            Item oleItem = (Item) item.getContentObject();
            String itemXml = null;
            try {
                itemXml = buildItemContentWithItemStatus(oleItem, OLEConstants.ITEM_STATUS_LOST);
            } catch (Exception e) {
                LOG.info("Exception occured while updating the item status for the item id : " + item.getId() + "and barcode : " + item.getBarcode());
                LOG.error(e, e);
            }
            if (itemXml != null) {
                item.setContent(itemXml);
                item.setOperation(DocstoreDocument.OperationType.UPDATE);
                BibTree bibTree = new BibTree();
                bibTree.setBib(item.getHolding().getBib());
                HoldingsTree holdingsTree = new HoldingsTree();
                holdingsTree.setHoldings(item.getHolding());
                holdingsTree.getItems().add(item);
                bibTree.getHoldingsTrees().add(holdingsTree);
                bibTrees.getBibTrees().add(bibTree);
            }
        }
        try {
            docstoreLocalClient.processBibTrees(bibTrees);
        } catch (Exception e) {
            LOG.error(e, e);
            StringBuffer itemUUIDsBuffer = new StringBuffer();
            for (String itemUUID : itemUUIDs) {
                itemUUIDsBuffer.append(itemUUID + ",");
            }
            LOG.info("Exception occured while updating item status to the following itemIds : " + itemUUIDsBuffer.toString());
        }

    }

    public String buildItemContentWithItemStatus(org.kuali.ole.docstore.common.document.content.instance.Item oleItem, String itemStatus) throws Exception {
        LOG.debug("Inside the buildItemContentWithItemStatus method");
        ItemStatus itemStatus1 = new ItemStatus();
        itemStatus1.setCodeValue(itemStatus);
        itemStatus1.setFullValue(itemStatus);
        oleItem.setItemStatus(itemStatus1);
        oleItem.setItemStatusEffectiveDate(String.valueOf(new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE).format(new Date())));
        String itemContent = getItemOlemlRecordProcessor().toXML(oleItem);
        return itemContent;
    }

    public PatronBillHelperService getPatronBillHelperService() {
        if(patronBillHelperService==null){
            patronBillHelperService=new PatronBillHelperService();
        }
        return patronBillHelperService;
    }

    public void setPatronBillHelperService(PatronBillHelperService patronBillHelperService) {
        this.patronBillHelperService = patronBillHelperService;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}