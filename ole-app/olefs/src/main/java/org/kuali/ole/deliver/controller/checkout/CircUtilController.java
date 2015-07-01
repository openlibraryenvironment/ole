package org.kuali.ole.deliver.controller.checkout;

import org.apache.log4j.Logger;
import org.kie.api.runtime.rule.Agenda;
import org.kie.api.runtime.rule.AgendaGroup;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.deliver.drools.CustomAgendaFilter;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsEngine;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.util.NoticeInfo;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.ItemStatus;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 6/4/15.
 */
public class CircUtilController {
    private static final Logger LOG = Logger.getLogger(CircUtilController.class);
    private BusinessObjectService businessObjectService;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;
    private DocstoreClientLocator docstoreClientLocator;

    public void fireRulesForPatron(List<Object> facts, String[] expectedRules, String agendaGroup) {
        StatefulKnowledgeSession session = DroolsEngine.getInstance().getSession();
        Agenda agenda = session.getAgenda();
        AgendaGroup group = agenda.getAgendaGroup(agendaGroup);
        group.setFocus();
        for (Iterator<Object> iterator = facts.iterator(); iterator.hasNext(); ) {
            Object fact = iterator.next();
            session.insert(fact);
        }

        if (null!= expectedRules && expectedRules.length > 0) {
            session.fireAllRules(new CustomAgendaFilter(expectedRules));
        } else {
            session.fireAllRules();
        }
        session.dispose();
    }

    public List<OLEDeliverNotice> processNotices(NoticeInfo noticeInfo, OleLoanDocument currentLoanDocument){
        List<OLEDeliverNotice> deliverNotices = new ArrayList<>();

        Map<String, Map<String, Object>> noticeInfoForTypeMap = noticeInfo.getNoticeInfoForTypeMap();

        if (null != noticeInfoForTypeMap) {
            for (Iterator<String> iterator = noticeInfoForTypeMap.keySet().iterator(); iterator.hasNext(); ) {
                String noticeType = iterator.next();
                if (noticeType.equalsIgnoreCase(OLEConstants.COURTESY_NOTICE)) {
                    processCourtseyNotices(noticeInfo, deliverNotices, currentLoanDocument);
                } else if (noticeType.equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)) {
                    Integer numOverDueNoticeToBeSent = Integer.parseInt((String) noticeInfo.getNoticeInfoForTypeMap().get
                            (OLEConstants.OVERDUE_NOTICE).get(DroolsConstants.NUMBER_OF_OVERDUE_NOTICES_TO_BE_SENT));
                    int count = 0;
                    for (count = 0; count < numOverDueNoticeToBeSent; count++) {
                        processOverdueNotices(noticeInfo, deliverNotices, count, currentLoanDocument);
                    }
                    processLostNotices(noticeInfo, deliverNotices, count, currentLoanDocument);
                }
            }
        }

        return deliverNotices;
    }

    private void processLostNotices(NoticeInfo noticeInfo, List<OLEDeliverNotice> deliverNotices, int count, OleLoanDocument currentLoanDocument) {
        OLEDeliverNotice lostNotice = new OLEDeliverNotice();
        lostNotice.setNoticeType(OLEConstants.NOTICE_LOST);
        lostNotice.setNoticeSendType(DroolsConstants.EMAIL);
        lostNotice.setPatronId(currentLoanDocument.getPatronId());
        lostNotice.setNoticeToBeSendDate(calculateNoticeToBeSentDate(Integer.parseInt((String) noticeInfo
                .getNoticeInfoForTypeMap().get(OLEConstants.OVERDUE_NOTICE).get(DroolsConstants
                        .INTERVAL_TO_GENERATE_NOTICE_FOR_OVERDUE)), currentLoanDocument.getLoanDueDate(), count + 1));
        deliverNotices.add(lostNotice);
        lostNotice.setReplacementFeeAmount(BigDecimal.valueOf(Double.parseDouble((String) noticeInfo
                .getNoticeInfoForTypeMap().get(OLEConstants.OVERDUE_NOTICE).get(DroolsConstants
                        .REPLACEMENT_BILL_AMT))));
    }

    private void processOverdueNotices(NoticeInfo noticeInfo, List<OLEDeliverNotice> deliverNotices, int count, OleLoanDocument currentLoanDocument) {
        OLEDeliverNotice overdueNotice = new OLEDeliverNotice();
        overdueNotice.setNoticeToBeSendDate(calculateNoticeToBeSentDate(Integer.parseInt((String) noticeInfo
                        .getNoticeInfoForTypeMap().get(OLEConstants.OVERDUE_NOTICE).get(DroolsConstants.INTERVAL_TO_GENERATE_NOTICE_FOR_OVERDUE)),
                currentLoanDocument.getLoanDueDate(), count + 1));
        overdueNotice.setNoticeSendType(DroolsConstants.EMAIL);
        overdueNotice.setNoticeType(OLEConstants.OVERDUE_NOTICE);
        overdueNotice.setPatronId(currentLoanDocument.getPatronId());
        deliverNotices.add(overdueNotice);
    }

    private void processCourtseyNotices(NoticeInfo noticeInfo, List<OLEDeliverNotice> deliverNotices, OleLoanDocument currentLoanDocument) {
        OLEDeliverNotice courtseyNotice = new OLEDeliverNotice();
        String loanId = currentLoanDocument.getLoanId();
        courtseyNotice.setNoticeType(OLEConstants.COURTESY_NOTICE);
        courtseyNotice.setNoticeSendType(DroolsConstants.EMAIL);
        courtseyNotice.setNoticeToBeSendDate(calculateNoticeToBeSentDate(-Integer.parseInt((String) noticeInfo
                        .getNoticeInfoForTypeMap().get(OLEConstants.COURTESY_NOTICE).get(DroolsConstants.INTERVAL_TO_GENERATE_NOTICE_FOR_COURTSEY)),
                currentLoanDocument.getLoanDueDate(), 1));
        courtseyNotice.setLoanId(loanId);
        courtseyNotice.setPatronId(currentLoanDocument.getPatronId());
        deliverNotices.add(courtseyNotice);
    }

    private Timestamp calculateNoticeToBeSentDate(Integer interval, Timestamp dueDate, Integer count) {
        Timestamp noticeToBeSentDate;

        noticeToBeSentDate = interval != null && dueDate != null ?
                DateUtil.addDays(dueDate, interval * count) : null;
        return noticeToBeSentDate;
    }

    public ItemRecord getItemRecordByBarcode(String itemBarcode){
        ItemRecord itemRecord = null;
        HashMap<String, String> criteriaMap = new HashMap<>();
        criteriaMap.put("barCode", itemBarcode);
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class,
                criteriaMap);
        if (null != itemRecords && !itemRecords.isEmpty()) {
            itemRecord = itemRecords.get(0);
        }

        return itemRecord;
    }

    public String convertDateToString(Date date ,String format){
        LOG.info("Date Format : " + format + "Date : " + date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateValue ="";
        try{
            dateValue = simpleDateFormat.format(date);
        }catch(Exception e){
            LOG.error(e,e);
        }
        LOG.info("Formatted Date : " + dateValue);
        return dateValue;
    }

    public String convertToString(Timestamp date) {
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date itemDate = null;
        try {
            itemDate = format2.parse(date.toString());
        } catch (ParseException e) {
            LOG.error("format string to Date " + e);
        }
        return format1.format(itemDate).toString();
    }

    public String getLoginUserId() {
        return GlobalVariables.getUserSession().getPrincipalId();
    }


    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public CircDeskLocationResolver getCircDeskLocationResolver() {
        return new CircDeskLocationResolver();
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if(itemOlemlRecordProcessor == null){
            itemOlemlRecordProcessor = SpringContext.getBean(ItemOlemlRecordProcessor.class);
        }
        return itemOlemlRecordProcessor;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


    public void rollBackSavedLoanRecord(String barcode) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("itemId", barcode);
        List<OleLoanDocument> oleLoanDocument = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, criteria);
        if (oleLoanDocument.size() > 0) {
            KRADServiceLocator.getBusinessObjectService().delete(oleLoanDocument.get(0));
        }
    }

    public OleLoanDocument updateLoanDocumentWithItemInformation(ItemRecord itemRecord, OleLoanDocument oleLoanDocument) {

        OleItemSearch oleItemSearch = new DocstoreUtil().getOleItemSearchList(itemRecord.getItemId());
        oleLoanDocument.setTitle(oleItemSearch.getTitle());
        oleLoanDocument.setAuthor(oleItemSearch.getAuthor());
        oleLoanDocument.setLocation(oleItemSearch.getShelvingLocation());
        oleLoanDocument.setItemCallNumber(oleItemSearch.getCallNumber());
        oleLoanDocument.setItemCopyNumber(oleItemSearch.getCopyNumber());
        oleLoanDocument.setChronology(oleItemSearch.getChronology());
        oleLoanDocument.setEnumeration(oleItemSearch.getEnumeration());
        oleLoanDocument.setItemStatus(oleItemSearch.getItemStatus());
        oleLoanDocument.setItemUuid(oleItemSearch.getItemUUID());
        oleLoanDocument.setInstanceUuid(oleItemSearch.getInstanceUUID());
        oleLoanDocument.setBibUuid(oleItemSearch.getBibUUID());
        return oleLoanDocument;
    }



    public Boolean updateItemInfoInSolr(OleLoanDocument oleLoanDocument, String itemUUID) {
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = getExistingItemFromSolr(itemUUID);
        if (oleItem != null) {
            oleItem.setCurrentBorrower(oleLoanDocument.getPatronId());
            oleItem.setProxyBorrower(oleLoanDocument.getProxyPatronId());
            oleItem.setCheckOutDateTime(convertDateToString(oleLoanDocument.getCreateDate(), "MM/dd/yyyy HH:mm:ss"));
            if (oleLoanDocument.getLoanDueDate() != null) {
                oleItem.setDueDateTime(convertToString(oleLoanDocument.getLoanDueDate()));
            } else {
                oleItem.setDueDateTime("");
            }
            oleItem.setNumberOfRenew(Integer.parseInt(oleLoanDocument.getNumberOfRenewals()));
            try {
                updateItem(oleItem);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }



    private org.kuali.ole.docstore.common.document.content.instance.Item getExistingItemFromSolr(String itemUUID) {
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        try {
            Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID);
            return itemOlemlRecordProcessor.fromXML(item.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public void updateItem(org.kuali.ole.docstore.common.document.content.instance.Item oleItem) throws Exception {
        try {
            String itemUuid = oleItem.getItemIdentifier();
            String itemXmlContent = buildItemContent(oleItem);
            Item item = new ItemOleml();
            item.setId(itemUuid);
            item.setContent(itemXmlContent);
            item.setCategory(OLEConstants.WORK_CATEGORY);
            item.setType(DocType.ITEM.getCode());
            item.setFormat(OLEConstants.OLEML_FORMAT);
            item.setStaffOnly(oleItem.isStaffOnlyFlag());
            getDocstoreClientLocator().getDocstoreClient().updateItem(item);
        } catch (Exception e) {
            LOG.error(OLEConstants.ITM_STS_TO_DOC_FAIL + e, e);
            throw new Exception(OLEConstants.ITM_STS_TO_DOC_FAIL);
        }
    }



    public String buildItemContent(org.kuali.ole.docstore.common.document.content.instance.Item oleItem) throws Exception {
        ItemStatus itemStatus = new ItemStatus();
        itemStatus.setCodeValue(OLEConstants.ITEM_STATUS_CHECKEDOUT);
        itemStatus.setFullValue(OLEConstants.ITEM_STATUS_CHECKEDOUT);
        oleItem.setItemStatus(itemStatus);
        oleItem.setItemStatusEffectiveDate(String.valueOf(new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE).format(new Date())));
        String itemContent = getItemOlemlRecordProcessor().toXML(oleItem);
        return itemContent;
    }

}
