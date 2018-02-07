package org.kuali.ole.deliver;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.FineNoticeEmailContentFormatter;
import org.kuali.ole.deliver.service.NoticeMailContentFormatter;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 7/27/15.
 */
public class PatronBillGenerator {
    private static final Logger LOG = Logger.getLogger(PatronBillGenerator.class);

    private BusinessObjectService businessObjectService;
    private OlePatronHelperServiceImpl olePatronHelperService;
    private CircDeskLocationResolver circDeskLocationResolver;
    private PatronBillHelperService patronBillHelperService;

    public String generatePatronBillPayment(OleLoanDocument oleLoanDocument, String feeTypeName, Double fineAmount, Timestamp dueDate,boolean isRenew) throws  Exception {
        long begin = System.currentTimeMillis();
        StringBuffer contentForSendMail = new StringBuffer();

        PatronBillPayment patronBillPayment;
        OlePaymentStatus olePaymentStatus = getPaymentStatus();

        FeeType feeType = new FeeType();
        OleFeeType oleFeeType = getFeeTypeId(feeTypeName);
        if(null != oleFeeType){
            feeType.setFeeType(oleFeeType.getFeeTypeId());
            feeType.setOleFeeType(oleFeeType);
        }
        feeType.setFeeAmount(new KualiDecimal(fineAmount));
        feeType.setItemBarcode(oleLoanDocument.getItemId());
        feeType.setItemUuid(oleLoanDocument.getItemUuid());
        getPatronBillHelperService().setFeeTypeInfo(feeType,oleLoanDocument.getItemUuid());

        feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
        feeType.setBalFeeAmount(new KualiDecimal(fineAmount));
        feeType.setFeeSource(OLEConstants.SYSTEM);
        if(isRenew){
            feeType.setDueDate(oleLoanDocument.getPastDueDate()!=null ? new Timestamp(oleLoanDocument.getPastDueDate().getTime()):dueDate);
            feeType.setRenewalDate(new Timestamp(System.currentTimeMillis()));
        }else{
            feeType.setDueDate(dueDate);
        }
        if(oleLoanDocument.isOverrideCheckInTime()){
            feeType.setOverrideCheckInDate(oleLoanDocument.getCheckInDate());
            feeType.setCheckInDate(new Timestamp(System.currentTimeMillis()));
        }else{
            feeType.setCheckInDate(oleLoanDocument.getCheckInDate());
        }

        feeType.setCheckOutDate(oleLoanDocument.getCreateDate());
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        feeTypes.add(feeType);

        Date billdate = new Date();

        patronBillPayment = new PatronBillPayment();
        patronBillPayment.setBillDate(oleLoanDocument.getCheckInDate() != null ? new java.sql.Date(oleLoanDocument.getCheckInDate().getTime()) : new java.sql.Date(billdate.getTime()));
        patronBillPayment.setFeeType(feeTypes);
        //commented for jira OLE-5675
        patronBillPayment.setPatronId(oleLoanDocument.getPatronId());
        patronBillPayment.setProxyPatronId(oleLoanDocument.getProxyPatronId());
        patronBillPayment.setTotalAmount(new KualiDecimal(fineAmount));
        patronBillPayment.setUnPaidBalance(new KualiDecimal(fineAmount));

        PatronBillPayment patronBillPayments = null;
        try {
            patronBillPayments = getBusinessObjectService().save(patronBillPayment);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put(OLEConstants.OlePatron.PATRON_ID, oleLoanDocument.getPatronId());

        String patronMail = "";
        try {
            patronMail = getOlePatronHelperService().getPatronHomeEmailId(oleLoanDocument.getOlePatron().getEntity().getEntityTypeContactInfos().get(0));
        } catch (Exception e) {
            LOG.error("Exception while getting patron home mail id", e);
        }

        String emailSubject = null;

        if(feeTypeName.equalsIgnoreCase(OLEConstants.LOST_ITEM_PROCESSING_FEE) || feeTypeName.equalsIgnoreCase(OLEConstants.OVERDUE_FINE)){
            oleLoanDocument.setFeeTypeName(feeTypeName);
            oleLoanDocument.setFineBillNumber(patronBillPayments.getBillNumber());
            oleLoanDocument.setFineAmount(fineAmount);
            oleLoanDocument.setFineItemDue(feeType.getDueDate());

            List<OleLoanDocument> oleLoanDocuments = new ArrayList<>();
            oleLoanDocuments.add(oleLoanDocument);

            String noticeType = null;

            if(feeTypeName.equalsIgnoreCase(OLEConstants.LOST_ITEM_PROCESSING_FEE)){
                noticeType = OLEConstants.LOST_ITEM_PROCESSING_FEE_NOTICE;
            }else if(feeTypeName.equalsIgnoreCase(OLEConstants.OVERDUE_FINE)){
                noticeType = OLEConstants.OVERDUE_FINE_NOTICE;
            }

            Map<String,String> noticeTypeMap = new HashMap<>();
            noticeTypeMap.put("noticeType",noticeType);

            List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBos = (List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeTypeMap);

            OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = null;

            if(CollectionUtils.isNotEmpty(oleNoticeContentConfigurationBos) && oleNoticeContentConfigurationBos.size() > 0){
                oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBos.get(0);

                if(oleNoticeContentConfigurationBo != null){
                    emailSubject = oleNoticeContentConfigurationBo.getNoticeSubjectLine();
                    NoticeMailContentFormatter noticeMailContentFormatter = new FineNoticeEmailContentFormatter();
                    contentForSendMail.append(noticeMailContentFormatter.generateMailContentForPatron(oleLoanDocuments,oleNoticeContentConfigurationBo));
                }
            }
        }else{
            emailSubject = feeTypeName;
            OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
            contentForSendMail.append(oleDeliverBatchService.generateMailContentFromPatronBill(oleLoanDocument, oleLoanDocument.getOlePatron(), feeTypeName, String.valueOf(new KualiDecimal(fineAmount)), patronBillPayment));
        }

        OleMailer oleMail = GlobalResourceLoader.getService("oleMailer");
        String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleLoanDocument.getItemLocation());
        if (replyToEmail != null) {
            oleMail.sendEmail(new EmailFrom(replyToEmail), new EmailTo(patronMail),
                              new EmailSubject((emailSubject != null ? emailSubject: "" )), new EmailBody(contentForSendMail.toString()), true);
        } else {
            oleMail.sendEmail(new EmailFrom(getParameter(OLEParameterConstants.NOTICE_FROM_MAIL)), new EmailTo(patronMail),
                              new EmailSubject((emailSubject != null ? emailSubject: "" )), new EmailBody(contentForSendMail.toString()), true);
        }
        if (LOG.isInfoEnabled()){
            LOG.info("Mail send successfully to " + patronMail);
        }
        String billNumber = patronBillPayments.getBillNumber();
        long end = System.currentTimeMillis();
        return billNumber;
    }

    private OlePaymentStatus getPaymentStatus() {
        LOG.debug("Inside the getPaymentStatus method");
        Map statusMap = new HashMap();
        statusMap.put("paymentStatusName", OLEConstants.PAYMENT_STATUS_OUTSTANDING);
        List<OlePaymentStatus> olePaymentStatusList = (List<OlePaymentStatus>) getBusinessObjectService().findMatching(OlePaymentStatus.class, statusMap);
        return olePaymentStatusList != null && olePaymentStatusList.size() > 0 ? olePaymentStatusList.get(0) : null;
    }

    private OleFeeType getFeeTypeId(String feeTypeName) {
        LOG.debug("Inside the getOverdueFeeTypeId method");
        Map feeMap = new HashMap();
        feeMap.put("feeTypeName", feeTypeName);
        List<OleFeeType> oleFeeTypes = (List<OleFeeType>) getBusinessObjectService().findMatching(OleFeeType.class, feeMap);
        return oleFeeTypes != null && oleFeeTypes.size() > 0 ? oleFeeTypes.get(0) : null;
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if(parameter==null){
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter!=null?parameter.getValue():null;
    }

    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
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
}
