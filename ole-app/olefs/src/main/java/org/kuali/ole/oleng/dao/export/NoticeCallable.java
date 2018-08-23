package org.kuali.ole.oleng.dao.export;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.common.util.CollectionUtils;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.NoticeSolrInputDocumentGenerator;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.executors.LoanNoticesExecutor;
import org.kuali.ole.deliver.notice.util.NoticeUtil;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;
import org.kuali.ole.oleng.handler.DeliverNoticeHandler;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by govindarajank on 25/4/18.
 */
public abstract class NoticeCallable implements Callable {

    private BusinessObjectService businessObjectService;
    private ParameterValueResolver parameterResolverInstance;
    private OleMailer oleMailer;
    private CircDeskLocationResolver circDeskLocationResolver;
    private DocstoreClientLocator docstoreClientLocator;
    private DocstoreUtil docstoreUtil;
    private static final Logger LOG = Logger.getLogger(LoanNoticesExecutor.class);
    protected List<OleLoanDocument> loanDocuments;
    protected String noticeContentConfigName;
    protected Map<String,String> fieldLabelMap = new HashMap<String,String>();
    protected OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo;
    private SolrRequestReponseHandler solrRequestReponseHandler;
    private NoticeSolrInputDocumentGenerator noticeSolrInputDocumentGenerator;
    protected OleNGBatchNoticeResponse oleNGBatchNoticeResponse;
    private DeliverNoticeHandler deliverNoticeHandler;


    public NoticeCallable(Map loanNoticeMap){
        this.loanDocuments = (List<OleLoanDocument>) loanNoticeMap.get(OLEConstants.LOAN_DOCUMENTS);
        this.noticeContentConfigName = (String) loanNoticeMap.get(OLEConstants.NOTICE_CONTENT_CONFIG_NAME);
        this.deliverNoticeHandler = (DeliverNoticeHandler) loanNoticeMap.get("DeliverNoticeHandler");
        //this.oleBatchNoticeExport = new BatchNoticeResponse();
    }

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = (DocstoreClientLocator) SpringContext.getService("docstoreClientLocator");

        }
        return docstoreClientLocator;
    }

    @Override
    public Object call() throws Exception {
        LOG.info("NoticesExecutor thread id---->"+Thread.currentThread().getId()+"current thread---->"+Thread.currentThread());

       try{
        //1. Pre process
            preProcess(loanDocuments);
            //2. Determine the correct NoticeConfigurationBo
            setOleNoticeContentConfigurationBo();
            //3. generate email content
            String mailContent = generateMailContent(loanDocuments);
            if (StringUtils.isNotBlank(mailContent) && !mailContent.contains("FreeMarker template error")) {
                preProcess(loanDocuments);
                //4. Generate notices
                List<OLEDeliverNotice> oleDeliverNotices = buildNoticesForDeletion();
                //5. Save loan document
                saveLoanDocument();

                //6. Delete notices
                deleteNotices(oleDeliverNotices);

                //7. update notice history
                saveOLEDeliverNoticeHistory(oleDeliverNotices, mailContent);
                //8. send mail
                sendMail(mailContent);
                //9. Index the mail content for solr search
                getSolrRequestReponseHandler().updateSolr(CollectionUtils.singletonList(
                        getNoticeSolrInputDocumentGenerator().getSolrInputDocument(
                                buildMapForIndexToSolr(getNoticeType(), mailContent, loanDocuments))));
                //10. Post process
                postProcess(loanDocuments);

                //11. Prepare Success Response
                prepareNoticeResponses(loanDocuments,getNoticeType(),getSendToDate(deliverNoticeHandler.getNoticeTypeSendDate(getNoticeType())), true);

            } else {
                // Prepare Failure Response
                prepareNoticeResponses(loanDocuments,getNoticeType(),getSendToDate(deliverNoticeHandler.getNoticeTypeSendDate(getNoticeType())), false);
            }
        }catch (Exception e){
            e.printStackTrace();
            prepareNoticeResponses(loanDocuments,getNoticeType(),getSendToDate(deliverNoticeHandler.getNoticeTypeSendDate(getNoticeType())), false);
        }
        return oleNGBatchNoticeResponse;
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

    public void deleteNotices(List<OLEDeliverNotice> oleDeliverNotices) {
        getBusinessObjectService().delete(oleDeliverNotices);
    }

    public void saveOLEDeliverNoticeHistory(List<OLEDeliverNotice> oleDeliverNotices, String mailContent) {
        List<OLEDeliverNoticeHistory> oleDeliverNoticeHistoryList = new ArrayList<OLEDeliverNoticeHistory>();
        String numberOfRecords = getParameter(OLEConstants.NUMBER_OF_ITEM_INFO);
        List<List<OLEDeliverNotice>> slicedList = (List<List<OLEDeliverNotice>>) splitListToSubList(oleDeliverNotices, Integer.valueOf(numberOfRecords).intValue());
        for (List<OLEDeliverNotice> oleDeliverNoticeList : slicedList) {
            for (OLEDeliverNotice oleDeliverNotice : oleDeliverNoticeList) {
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
            oleDeliverNoticeHistoryList.clear();
        }
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if(parameter==null){
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter!=null?parameter.getValue():"50";
    }


    public List splitListToSubList(List<OLEDeliverNotice> parentList, int childListSize) {
        List<List<OLEDeliverNotice>> childList = new ArrayList<List<OLEDeliverNotice>>();
        List<OLEDeliverNotice> tempList = new ArrayList<OLEDeliverNotice>();
        int count = 0;
        if (parentList != null) {
            for (OLEDeliverNotice obj : parentList) {
                if (count < childListSize) {
                    count = count + 1;
                    tempList.add(obj);
                } else {
                    childList.add(tempList);
                    tempList = new ArrayList<OLEDeliverNotice>();
                    tempList.add(obj);
                    count = 1;
                }
            }
            if (tempList.size() <= childListSize) {
                childList.add(tempList);
            }
        }
        return childList;
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

    public void saveLoanDocument() {
        getBusinessObjectService().save(loanDocuments);
    }

    public NoticeSolrInputDocumentGenerator getNoticeSolrInputDocumentGenerator() {
        if (null == noticeSolrInputDocumentGenerator) {
            noticeSolrInputDocumentGenerator = new NoticeSolrInputDocumentGenerator();
        }
        return noticeSolrInputDocumentGenerator;
    }

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if (null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    protected abstract String getNoticeType();

    public void sendMail(String mailContent) {
        OlePatronDocument olePatron = loanDocuments.get(0).getOlePatron();
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatron.getEntity()
                    .getEntityTypeContactInfos().get(0);
            String emailAddress = getPatronHomeEmailId(entityTypeContactInfoBo) != null ?
                    getPatronHomeEmailId(entityTypeContactInfoBo) : "";

            if (loanDocuments.size() == 1) {
                sendMailsToPatron(emailAddress, mailContent, loanDocuments.get(0).getItemLocation(),oleNoticeContentConfigurationBo.getNoticeSubjectLine());
            } else {
                sendMailsToPatron(emailAddress, mailContent, null,oleNoticeContentConfigurationBo.getNoticeSubjectLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map buildMapForIndexToSolr(String noticeType, String noticeContent, List<OleLoanDocument> oleLoanDocuments) {
        Map parameterMap = new HashMap();
        parameterMap.put("DocType", noticeType);
        parameterMap.put("DocFormat", "Email");
        parameterMap.put("noticeType", noticeType);
        parameterMap.put("noticeContent", noticeContent);
        String patronBarcode = oleLoanDocuments.get(0).getOlePatron().getBarcode();
        String patronId = oleLoanDocuments.get(0).getOlePatron().getOlePatronId();
        parameterMap.put("patronBarcode", patronBarcode);
        Date dateSent = new Date();
        parameterMap.put("dateSent", dateSent);
        parameterMap.put("uniqueId", patronId + dateSent.getTime());
        List<String> itemBarcodes = new ArrayList<>();
        for (Iterator<OleLoanDocument> iterator = oleLoanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            String itemBarcode = oleLoanDocument.getItemId();
            itemBarcodes.add(itemBarcode);
        }
        parameterMap.put("itemBarcodes",itemBarcodes);
        return parameterMap;
    }

    public void prepareNoticeResponses(List<OleLoanDocument> oleLoanDocuments, String noticeType , Timestamp noticeToBeSendDate,boolean isNoticeSent){
        oleNGBatchNoticeResponse = new OleNGBatchNoticeResponse();
        int noticeCount = deliverNoticeHandler.getTotalNoticesCount(oleLoanDocuments,noticeType,noticeToBeSendDate);
        if(isNoticeSent){
            oleNGBatchNoticeResponse.setNoOfSuccessNotice(noticeCount);
        }else{
            oleNGBatchNoticeResponse.setNoOfFailureNotice(noticeCount);
            oleNGBatchNoticeResponse.setFailureLoanAndNoticeResponses(deliverNoticeHandler.getFailureLoanAndNoticeResponses(oleLoanDocuments,noticeType,noticeToBeSendDate));
        }
    }


    protected abstract void postProcess(List<OleLoanDocument> loanDocuments);
    protected abstract void preProcess(List<OleLoanDocument> loanDocuments);
    public abstract List<OLEDeliverNotice> buildNoticesForDeletion();
    public abstract String generateMailContent(List<OleLoanDocument> oleLoanDocuments);
    public abstract void setOleNoticeContentConfigurationBo();
    public abstract void setOleNoticeContentConfigurationBo(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo);

}
