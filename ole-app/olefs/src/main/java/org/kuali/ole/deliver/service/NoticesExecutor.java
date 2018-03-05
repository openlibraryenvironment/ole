package org.kuali.ole.deliver.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.util.NoticeUtil;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 4/8/15.
 */
public abstract class NoticesExecutor implements Runnable {

    private static final Logger LOG = Logger.getLogger(NoticesExecutor.class);
    private BusinessObjectService businessObjectService;
    private ParameterValueResolver parameterResolverInstance;
    private OleMailer oleMailer;
    private CircDeskLocationResolver circDeskLocationResolver;
    private DocstoreClientLocator docstoreClientLocator;
    private DocstoreUtil docstoreUtil;
    private NoticeUtil noticeUtil;
    private Map<String,String> itemTypeMap;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = (DocstoreClientLocator) SpringContext.getService("docstoreClientLocator");

        }
        return docstoreClientLocator;
    }


    public DocstoreUtil getDocstoreUtil() {
        if (docstoreUtil == null) {
            docstoreUtil = (DocstoreUtil)SpringContext.getService("docstoreUtil");
        }
        return docstoreUtil;
    }
    public ParameterValueResolver getParameterResolverInstance() {
        if (null == parameterResolverInstance) {
            parameterResolverInstance = ParameterValueResolver.getInstance();
        }
        return parameterResolverInstance;
    }



    public CircDeskLocationResolver getCircDeskLocationResolver() {
        if (null == circDeskLocationResolver) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public OleMailer getOleMailer() {
        if (null == oleMailer) {
            oleMailer = GlobalResourceLoader.getService("oleMailer");
        }
        return oleMailer;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public BusinessObjectService getBusinessObjectService(){
        return KRADServiceLocator.getBusinessObjectService();
    }

    public void setOleMailer(OleMailer oleMailer) {
        this.oleMailer = oleMailer;
    }

    public NoticeUtil getNoticeUtil() {
        if(noticeUtil == null){
            noticeUtil = new NoticeUtil();
        }
        return noticeUtil;
    }

    public void setNoticeUtil(NoticeUtil noticeUtil) {
        this.noticeUtil = noticeUtil;
    }



    @Override
    public void run() {

    }

  //  public abstract  void sendMail(String mailContent,String mailSubject);

    public void deleteNotices(List<OLEDeliverNotice> oleDeliverNotices) {
        getBusinessObjectService().delete(oleDeliverNotices);
    }

    public List<OLEDeliverNoticeHistory> saveOLEDeliverNoticeHistory(List<OLEDeliverNotice> oleDeliverNotices, String mailContent) {
        List<OLEDeliverNoticeHistory> oleDeliverNoticeHistoryList = new ArrayList<OLEDeliverNoticeHistory>();
        for (OLEDeliverNotice oleDeliverNotice : oleDeliverNotices) {
            OLEDeliverNoticeHistory oleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
            oleDeliverNoticeHistory.setLoanId(oleDeliverNotice.getLoanId());
            oleDeliverNoticeHistory.setNoticeType(oleDeliverNotice.getNoticeType());
            oleDeliverNoticeHistory.setNoticeSentDate(new Timestamp(new Date().getTime()));
            oleDeliverNoticeHistory.setPatronId(oleDeliverNotice.getPatronId());
            oleDeliverNoticeHistory.setNoticeSendType(oleDeliverNotice.getNoticeSendType());
            oleDeliverNoticeHistory.setNoticeContent(mailContent.getBytes());
            oleDeliverNoticeHistory.setRequestId(oleDeliverNotice.getRequestId());
            oleDeliverNoticeHistoryList.add(oleDeliverNoticeHistory);
        }
        getBusinessObjectService().save(oleDeliverNoticeHistoryList);

        return oleDeliverNoticeHistoryList;
    }

    public String getPatronHomeEmailId(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception {
        String emailId = "";
        if (entityTypeContactInfoBo.getEmailAddresses() != null) {
            for (int j = 0; j < entityTypeContactInfoBo.getEmailAddresses().size(); j++) {
                if (entityTypeContactInfoBo.getEmailAddresses().get(j).getDefaultValue()) {
                    emailId = (entityTypeContactInfoBo.getEmailAddresses().get(j).getEmailAddress());
                    break;
                }
            }
        }
        return emailId;
    }

    public String sendMailsToPatron(String emailAddress, String noticeContent, String itemLocation,String mailSubject) {
        String fromAddress = getCircDeskLocationResolver().getReplyToEmail(itemLocation);

        if (fromAddress == null) {
            fromAddress = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants
                    .NOTICE_FROM_MAIL);
        }
        try {
            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                fromAddress = OLEConstants.KUALI_MAIL;
            }
            if (emailAddress != null && !emailAddress.isEmpty()) {
                noticeContent = noticeContent.replace('[', ' ');
                noticeContent = noticeContent.replace(']', ' ');
                if (!noticeContent.trim().equals("")) {
                    OleMailer oleMailer = getOleMailer();
                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(emailAddress), new EmailSubject(mailSubject), new EmailBody(noticeContent), true);
                }
            } else {
            }
        } catch (Exception e) {
        }

        return noticeContent;
    }



    public String getItemTypeCodeByName(String itemTypeName) {
        String itemTypeCode = "";
        List<OleInstanceItemType> instanceItemTypeList = null;
        Map<String, String> instanceItemTypeMap = new HashMap<String, String>();
        instanceItemTypeMap.put("instanceItemTypeName", itemTypeName);
        instanceItemTypeList = (List<OleInstanceItemType>) getBusinessObjectService().findMatching(OleInstanceItemType.class, instanceItemTypeMap);
        if (instanceItemTypeList != null && instanceItemTypeList.size() > 0) {
            itemTypeCode = instanceItemTypeList.get(0).getInstanceItemTypeCode();
        }
        return itemTypeCode;
    }

    public Map<String,String> getItemTypeNameAndDesc(){;

        List<OleInstanceItemType> instanceItemTypeList = (List<OleInstanceItemType>) getBusinessObjectService().findAll(OleInstanceItemType.class);
        if(CollectionUtils.isNotEmpty(instanceItemTypeList) && instanceItemTypeList.size() > 0){
            itemTypeMap = new HashMap<String,String>();
            for(OleInstanceItemType instanceItemType : instanceItemTypeList)
            itemTypeMap.put(instanceItemType.getInstanceItemTypeName(),instanceItemType.getInstanceItemTypeDesc());
        }

        return itemTypeMap;
    }

    public Timestamp getSendToDate(String noticeToDate) {
        String lostNoticeToDate;
        lostNoticeToDate = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, noticeToDate);
        Timestamp lostNoticetoSendDate = new Timestamp(System.currentTimeMillis());
        if (!StringUtils.isEmpty(lostNoticeToDate)) {
            lostNoticetoSendDate = new Timestamp(new Date(lostNoticeToDate).getTime());
        }
        return lostNoticetoSendDate;
    }


    public boolean setItemInformations(OleDeliverRequestBo oleDeliverRequestBo) {
        ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
        LOG.info("Inside isItemAvailableInDocStore");
        boolean available = false;
        Map<String, String> itemMap = new HashMap<String, String>();
        LocationValuesBuilder locationValuesBuilder = new LocationValuesBuilder();
        String holdingsId = "";
        String bibTitle="";
        String bibAuthor="";
        try {
            try {
                org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                SearchResponse searchResponse = null;
                if(StringUtils.isNotBlank(oleDeliverRequestBo.getItemId())) {
                     search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleDeliverRequestBo.getItemId()), ""));
                } else {
                    search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ID, oleDeliverRequestBo.getItemUuid()), ""));
                }
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "holdingsIdentifier"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Title_display"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Author_display"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(),"CallNumberPrefix_display"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(),"TemporaryItemTypeFullValue_search"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(),"ItemTypeFullValue_display"));
                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        String fieldName = searchResultField.getFieldName();
                        String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                        if (fieldName.equalsIgnoreCase("holdingsIdentifier") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                            holdingsId = fieldValue;
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Title_display") &&!fieldValue.isEmpty()) {
                            bibTitle = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Author_display") &&!fieldValue.isEmpty()) {
                            bibAuthor = searchResultField.getFieldValue();
                        } else  if (searchResultField.getFieldName().equalsIgnoreCase("id") &&!fieldValue.isEmpty()){
                            oleDeliverRequestBo.setItemUuid(fieldValue);
                        }else  if (searchResultField.getFieldName().equalsIgnoreCase("CallNumberPrefix_display") &&!fieldValue.isEmpty()){
                            oleDeliverRequestBo.setCallNumberPrefix(fieldValue);
                        }else if (searchResultField.getFieldName().equalsIgnoreCase("TemporaryItemTypeFullValue_search")) {
                            oleDeliverRequestBo.setItemTypeName(searchResultField.getFieldValue());
                            oleDeliverRequestBo.setItemTypeDesc(itemTypeMap.get(oleDeliverRequestBo.getItemTypeName()));
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemTypeFullValue_display") &&
                                (oleDeliverRequestBo.getItemTypeName() == null || oleDeliverRequestBo.getItemTypeName().isEmpty())) {
                            oleDeliverRequestBo.setItemTypeName(searchResultField.getFieldValue());
                            oleDeliverRequestBo.setItemTypeDesc(itemTypeMap.get(oleDeliverRequestBo.getItemTypeName()));
                        }
                    }
                }
            } catch (Exception ex) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Item Exists");
                LOG.error(OLEConstants.ITEM_EXIST + ex);
            }
            OleItemSearch itemSearchList = getDocstoreUtil().getOleItemSearchList(oleDeliverRequestBo.getItemUuid());
            if (asrHelperService.isAnASRItem(itemSearchList.getShelvingLocation())) {
                oleDeliverRequestBo.setAsrFlag(true);
            } else {
                oleDeliverRequestBo.setAsrFlag(false);
            }
            if (itemSearchList != null) {
                oleDeliverRequestBo.setTitle(itemSearchList.getTitle());
                oleDeliverRequestBo.setAuthor(itemSearchList.getAuthor());
                String[] callNum = itemSearchList.getCallNumber().split(OLEConstants.DELIMITER_DASH);
                if(callNum!=null && callNum.length>0){
                    oleDeliverRequestBo.setCallNumber(callNum[callNum.length-1]);
                }
                oleDeliverRequestBo.setItemType(itemSearchList.getItemType());
                oleDeliverRequestBo.setItemLocation(itemSearchList.getShelvingLocation());
            }
            if(org.apache.commons.lang.StringUtils.isNotEmpty(bibTitle)){
                oleDeliverRequestBo.setTitle(bibTitle);
            }
            if(org.apache.commons.lang.StringUtils.isNotEmpty(bibAuthor)){
                oleDeliverRequestBo.setAuthor(bibAuthor);
            }
            LoanProcessor loanProcessor = new LoanProcessor();
            String itemXml = loanProcessor.getItemXML(oleDeliverRequestBo.getItemUuid());
            Item oleItem = loanProcessor.getItemPojo(itemXml);
            oleDeliverRequestBo.setOleItem(oleItem);
            oleDeliverRequestBo.setCopyNumber(oleItem.getCopyNumber());
            oleDeliverRequestBo.setEnumeration(oleItem.getEnumeration());
            oleDeliverRequestBo.setChronology(oleItem.getChronology());
            oleDeliverRequestBo.setItemStatus(oleItem.getItemStatus().getCodeValue());
            oleDeliverRequestBo.setClaimsReturnedFlag(oleItem.isClaimsReturnedFlag());
            locationValuesBuilder.getLocation(oleItem, oleDeliverRequestBo, holdingsId);
            available = true;
        } catch (Exception e) {
            LOG.error(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC) + e);
        }
        return available;
    }


}
