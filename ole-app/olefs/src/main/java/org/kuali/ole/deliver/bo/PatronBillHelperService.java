package org.kuali.ole.deliver.bo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.controller.checkin.CheckinBaseController;
import org.kuali.ole.deliver.controller.checkin.CheckinItemController;
import org.kuali.ole.deliver.controller.checkin.CheckinUIController;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.form.PatronBillForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.template.PatronBillContentFormatter;
import org.kuali.ole.template.PatronBillItemView;
import org.kuali.ole.template.PatronBillViewBo;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/18/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class PatronBillHelperService {

    private static final Logger LOG = Logger.getLogger(PatronBillHelperService.class);

    private DocstoreClientLocator docstoreClientLocator;

    private List<String> billIds=new ArrayList<String>();

    private OleMailer oleMailer;
    private CircDeskLocationResolver circDeskLocationResolver;
    private static String billNumbers="";
    private Map billMap;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;

    private Map<String, CheckinUIController> checkinUIControllerMap = new HashMap<>();


    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public List<String> getBillIds() {
        return billIds;
    }

    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    private BusinessObjectService businessObjectService;

    /**
     * This method will retrieve patron bill payment object
     *
     * @return Returns the PatronBill List
     */
    public List<PatronBillPayment> getBillPayment(String patronId) {
        LOG.debug("Initialized getService Method");
        Map patronIdMap = new HashMap();
        patronIdMap.put("patronId", patronId);
        List<PatronBillPayment> patronBillPaymentList = (List<PatronBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class, patronIdMap);

        return sortById(patronBillPaymentList);
    }

    public KualiDecimal populateGrandTotal(List<PatronBillPayment> patronBillPaymentList) {
        KualiDecimal grandTotal = OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
            KualiDecimal paidamt = OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
            KualiDecimal unpaidamt = OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
            List<FeeType> feeTypes = patronBillPayment.getFeeType();
            for (FeeType feeType : feeTypes) {
                if (!feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAYMENT_SUSPENDED)) {
                    grandTotal = grandTotal.add(feeType.getBalFeeAmount());
                    paidamt = paidamt.add(feeType.getPaidAmount());
                /*if(feeType.getFeeAmount()!=null && feeType.getBalFeeAmount()!=null && (feeType.getFeeAmount()).compareTo(feeType.getPaidAmount())>0){
                    unpaidamt = (feeType.getFeeAmount()).subtract(feeType.getPaidAmount());
                }*/
                    unpaidamt = unpaidamt.add(feeType.getBalFeeAmount());
                }
            }
            patronBillPayment.setPaidAmount(paidamt);
            patronBillPayment.setUnPaidBalance(unpaidamt);
            //getBusinessObjectService().save(patronBillPayment);
        }
        return grandTotal;
    }

    public KualiDecimal populateCreditRemainingTotal(List<PatronBillPayment> patronBillPaymentList) {
        KualiDecimal grandTotal = OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
            List<FeeType> feeTypes = patronBillPayment.getFeeType();
            for (FeeType feeType : feeTypes) {
                grandTotal = grandTotal.add(feeType.getCreditRemaining().negated());
            }
        }
        return grandTotal.negated();
    }



    public List<PatronBillPayment> sortById(List<PatronBillPayment> patronBillPaymentList) {
        List<PatronBillPayment> outPatronBillPayments = new ArrayList<PatronBillPayment>();
        List<PatronBillPayment> partPatronBillPayments = new ArrayList<PatronBillPayment>();
        List<PatronBillPayment> restPatronBillPayments = new ArrayList<PatronBillPayment>();
        List<PatronBillPayment> categorizedPatronBillPayments = new ArrayList<PatronBillPayment>();

        for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
            if (patronBillPayment.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_OUTSTANDING_CODE)) {
                outPatronBillPayments.add(patronBillPayment);
            } else if (patronBillPayment.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_PART_CODE)) {
                partPatronBillPayments.add(patronBillPayment);
            } else {
                restPatronBillPayments.add(patronBillPayment);
            }
        }
        categorizedPatronBillPayments.addAll(outPatronBillPayments);
        categorizedPatronBillPayments.addAll(partPatronBillPayments);

        Collections.sort(categorizedPatronBillPayments, new Comparator<PatronBillPayment>() {
            public int compare(PatronBillPayment patronBillPayment1, PatronBillPayment patronBillPayment2) {
                return Integer.parseInt(patronBillPayment1.getBillNumber()) > Integer.parseInt(patronBillPayment2.getBillNumber()) ? 1 : -1;
            }
        });
        Collections.sort(restPatronBillPayments, new Comparator<PatronBillPayment>() {
            public int compare(PatronBillPayment patronBillPayment1, PatronBillPayment patronBillPayment2) {
                return Integer.parseInt(patronBillPayment1.getBillNumber()) > Integer.parseInt(patronBillPayment2.getBillNumber()) ? 1 : -1;
            }
        });
        categorizedPatronBillPayments.addAll(restPatronBillPayments);
        for (PatronBillPayment patronBillPayment : categorizedPatronBillPayments) {
            List<FeeType> enabledFeeType = new ArrayList<FeeType>();
            List<FeeType> disableFeeType = new ArrayList<FeeType>();
            List<FeeType> newFeeTypeList = new ArrayList<FeeType>();
            for (FeeType feeType : patronBillPayment.getFeeType()) {
                KualiDecimal KualiDecimal = OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;

                if (feeType.getBalFeeAmount().intValue() > 0) {
                    enabledFeeType.add(feeType);
                } else {
                    disableFeeType.add(feeType);
                }
            }
            newFeeTypeList.addAll(enabledFeeType);
            newFeeTypeList.addAll(disableFeeType);
            patronBillPayment.getFeeType().clear();
            patronBillPayment.setFeeType(newFeeTypeList);
        }

        return categorizedPatronBillPayments;
    }

    public List<FeeType> getFeeTypeList(List<PatronBillPayment> patronBillPaymentList) {
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        List<String> itemUuids = new ArrayList<String>();
        LoanProcessor loanProcessor = new LoanProcessor();
        try {
            for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
                List<FeeType> feeTypeList = patronBillPayment.getFeeType();
                if (!feeTypeList.isEmpty()) {
                    List<OleItemLevelBillPayment> oleItemLevelBillPayments = new ArrayList<>();
                    for (FeeType feeType : feeTypeList) {
                        oleItemLevelBillPayments.addAll(feeType.getItemLevelBillPaymentList());
                    }
                    Collections.sort(oleItemLevelBillPayments, new Comparator<OleItemLevelBillPayment>() {
                        public int compare(OleItemLevelBillPayment oleItemLevelBillPayment1, OleItemLevelBillPayment oleItemLevelBillPayment2) {
                            return Integer.parseInt(oleItemLevelBillPayment1.getPaymentId()) > Integer.parseInt(oleItemLevelBillPayment2.getPaymentId()) ? 1 : -1;
                        }
                    });
                    if (!oleItemLevelBillPayments.isEmpty()) {
                        patronBillPayment.setLastTransactionDate(oleItemLevelBillPayments.get(oleItemLevelBillPayments.size() - 1).getPaymentDate());
                    }
                }
                feeTypes.addAll(feeTypeList);
            }
            for (FeeType feeType : feeTypes) {
                if (feeType.getFeeTypes() != null) {
                    feeType.getFeeTypes().add(feeType);
                }

                List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList();
                if (!oleItemLevelBillPayments.isEmpty()) {
                    Collections.sort(oleItemLevelBillPayments, new Comparator<OleItemLevelBillPayment>() {
                        public int compare(OleItemLevelBillPayment oleItemLevelBillPayment1, OleItemLevelBillPayment oleItemLevelBillPayment2) {
                            return Integer.parseInt(oleItemLevelBillPayment1.getPaymentId()) > Integer.parseInt(oleItemLevelBillPayment2.getPaymentId()) ? 1 : -1;
                        }
                    });
                    feeType.setLastTransactionDate(oleItemLevelBillPayments.get(oleItemLevelBillPayments.size() - 1).getPaymentDate());
                }
            }


        } catch (Exception e) {
            LOG.error("Exception while getting fee type list", e);
        }
        return feeTypes;
    }

    public List<FeeType> getOpenFeeTypeList(List<PatronBillPayment> patronBillPaymentList) {
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        List<FeeType> openBillList = new ArrayList<FeeType>();
        List<String> itemUuids = new ArrayList<String>();
        LoanProcessor loanProcessor = new LoanProcessor();
        try {
            for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
                List<FeeType> feeTypeList = patronBillPayment.getFeeType();
                if (!feeTypeList.isEmpty()) {
                    List<OleItemLevelBillPayment> oleItemLevelBillPayments = new ArrayList<>();
                    for (FeeType feeType : feeTypeList) {
                            oleItemLevelBillPayments.addAll(feeType.getItemLevelBillPaymentList());

                    }
                    Collections.sort(oleItemLevelBillPayments, new Comparator<OleItemLevelBillPayment>() {
                        public int compare(OleItemLevelBillPayment oleItemLevelBillPayment1, OleItemLevelBillPayment oleItemLevelBillPayment2) {
                            return Integer.parseInt(oleItemLevelBillPayment1.getPaymentId()) > Integer.parseInt(oleItemLevelBillPayment2.getPaymentId()) ? 1 : -1;
                        }
                    });
                    if (!oleItemLevelBillPayments.isEmpty()) {
                        patronBillPayment.setLastTransactionDate(oleItemLevelBillPayments.get(oleItemLevelBillPayments.size() - 1).getPaymentDate());
                    }
                }

                feeTypes.addAll(feeTypeList);
            }
            for (FeeType feeType : feeTypes) {
                if (feeType.getFeeTypes() != null) {
                    feeType.getFeeTypes().add(feeType);
                }

                List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList();
                if (!oleItemLevelBillPayments.isEmpty()) {
                    Collections.sort(oleItemLevelBillPayments, new Comparator<OleItemLevelBillPayment>() {
                        public int compare(OleItemLevelBillPayment oleItemLevelBillPayment1, OleItemLevelBillPayment oleItemLevelBillPayment2) {
                            return Integer.parseInt(oleItemLevelBillPayment1.getPaymentId()) > Integer.parseInt(oleItemLevelBillPayment2.getPaymentId()) ? 1 : -1;
                        }
                    });
                    feeType.setLastTransactionDate(oleItemLevelBillPayments.get(oleItemLevelBillPayments.size() - 1).getPaymentDate());
                }
            }


        } catch (Exception e) {
            LOG.error("Exception while getting fee type list", e);
        }
        for(FeeType feeType : feeTypes) {
            if(feeType.getBalFeeAmount().isGreaterThan(KualiDecimal.ZERO) || feeType.getCreditRemaining().negated().isGreaterThan(KualiDecimal.ZERO)) {
                openBillList.add(feeType);
            }
        }

        return openBillList;
    }

    public List<FeeType> getClosedFeeTypeList(List<PatronBillPayment> patronBillPaymentList) {
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        List<FeeType> closedBillList = new ArrayList<FeeType>();
        List<String> itemUuids = new ArrayList<String>();
        LoanProcessor loanProcessor = new LoanProcessor();
        try {
            for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
                List<FeeType> feeTypeList = patronBillPayment.getFeeType();
                if (!feeTypeList.isEmpty()) {
                    List<OleItemLevelBillPayment> oleItemLevelBillPayments = new ArrayList<>();
                    for (FeeType feeType : feeTypeList) {
                            oleItemLevelBillPayments.addAll(feeType.getItemLevelBillPaymentList());

                    }
                    Collections.sort(oleItemLevelBillPayments, new Comparator<OleItemLevelBillPayment>() {
                        public int compare(OleItemLevelBillPayment oleItemLevelBillPayment1, OleItemLevelBillPayment oleItemLevelBillPayment2) {
                            return Integer.parseInt(oleItemLevelBillPayment1.getPaymentId()) > Integer.parseInt(oleItemLevelBillPayment2.getPaymentId()) ? 1 : -1;
                        }
                    });
                    if (!oleItemLevelBillPayments.isEmpty()) {
                        patronBillPayment.setLastTransactionDate(oleItemLevelBillPayments.get(oleItemLevelBillPayments.size() - 1).getPaymentDate());
                    }
                }

                feeTypes.addAll(feeTypeList);
            }
            for (FeeType feeType : feeTypes) {
                /*if (feeType.getFeeTypes() != null) {
                    feeType.getFeeTypes().add(feeType);
                }*/

                List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList();
                if (!oleItemLevelBillPayments.isEmpty()) {
                    Collections.sort(oleItemLevelBillPayments, new Comparator<OleItemLevelBillPayment>() {
                        public int compare(OleItemLevelBillPayment oleItemLevelBillPayment1, OleItemLevelBillPayment oleItemLevelBillPayment2) {
                            return Integer.parseInt(oleItemLevelBillPayment1.getPaymentId()) > Integer.parseInt(oleItemLevelBillPayment2.getPaymentId()) ? 1 : -1;
                        }
                    });
                    feeType.setLastTransactionDate(oleItemLevelBillPayments.get(oleItemLevelBillPayments.size() - 1).getPaymentDate());
                }
            }


        } catch (Exception e) {
            LOG.error("Exception while getting fee type list", e);
        }

        for(FeeType feeType : feeTypes) {
            if(feeType.getBalFeeAmount().isZero() && feeType.getCreditRemaining().negated().isZero()) {
                closedBillList.add(feeType);
            }
        }
        return closedBillList;
    }


    public void setFeeTypeInfo(FeeType feeType, String itemUUID){
        String location = null;
        try {
            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID);
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
            OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(item.getHolding().getContent());
            if(itemContent!=null && itemContent.getLocation()!=null && itemContent.getLocation().getLocationLevel()!=null) {
                location = item.getLocation();
            }
            if(StringUtils.isBlank(location)){
                if(oleHoldings!=null && oleHoldings.getLocation()!=null && oleHoldings.getLocation().getLocationLevel()!=null) {
                    location = item.getHolding().getLocationName();
                }
            }
            feeType.setItemCallNumber(getItemCallNumber(itemContent.getCallNumber(),oleHoldings.getCallNumber()));
            feeType.setItemTitle(item.getHolding().getBib().getTitle());
            feeType.setItemAuthor(item.getHolding().getBib().getAuthor());
            if(itemContent.getTemporaryItemType()!=null && itemContent.getTemporaryItemType().getCodeValue()!=null){
                feeType.setItemType(itemContent.getTemporaryItemType().getCodeValue());
            }else{
                feeType.setItemType(itemContent.getItemType().getCodeValue());
            }
            feeType.setItemCopyNumber(itemContent.getCopyNumber());
            feeType.setItemChronology(itemContent.getChronology());
            feeType.setItemEnumeration(itemContent.getEnumeration());
            feeType.setItemOwnLocation(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves Item call number.
     *
     * @param itemCallNumber,holdingCallNumber
     * @return
     * @throws Exception
     */
    public String getItemCallNumber(CallNumber itemCallNumber,CallNumber holdingCallNumber) throws Exception {
        LOG.debug("Inside the getItemCallNumber method");
        String callNumber = "";

        if (itemCallNumber != null && StringUtils.isNotBlank(itemCallNumber.getType())) {
            callNumber += itemCallNumber.getType() + OLEConstants.DELIMITER_DASH;
        }else if(holdingCallNumber != null && StringUtils.isNotBlank(holdingCallNumber.getType())){
            callNumber += holdingCallNumber.getType() + OLEConstants.DELIMITER_DASH;
        }
        if (itemCallNumber != null && StringUtils.isNotBlank(itemCallNumber.getPrefix())) {
            callNumber += itemCallNumber.getPrefix() + OLEConstants.DELIMITER_DASH;
        }else if(holdingCallNumber != null && StringUtils.isNotBlank(holdingCallNumber.getPrefix())){
            callNumber += holdingCallNumber.getPrefix() + OLEConstants.DELIMITER_DASH;
        }
        if (itemCallNumber != null && StringUtils.isNotBlank(itemCallNumber.getNumber())) {
            callNumber += itemCallNumber.getNumber();
        }else if(holdingCallNumber != null && StringUtils.isNotBlank(holdingCallNumber.getNumber())){
            callNumber += holdingCallNumber.getNumber();
        }

        return callNumber;
    }
    /**
     * This method will retrieve paymentStatusName based on paymentStatusId
     *
     * @return paymentStatusName
     */
    public OlePaymentStatus getPaymentStatus(String paymentStatus) {
        LOG.debug("Inside the getPaymentStatus method");
        Map statusMap = new HashMap();
        statusMap.put("paymentStatusCode", paymentStatus);
        List<OlePaymentStatus> olePaymentStatusList = (List<OlePaymentStatus>) getBusinessObjectService().findMatching(OlePaymentStatus.class, statusMap);
        return olePaymentStatusList != null && olePaymentStatusList.size() > 0 ? olePaymentStatusList.get(0) : null;
    }


    public OlePatronDocument getPatronDetails(String patronId) {
        OlePatronDocument olePatronDocument = new OlePatronDocument();
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("olePatronId", patronId);
        List<OlePatronDocument> olePatronDocumentList = new ArrayList<OlePatronDocument>();
        olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, criteria);
        olePatronDocument.setOlePatronId(olePatronDocumentList.get(0).getOlePatronId());
        olePatronDocument.setBarcode(olePatronDocumentList.get(0).getBarcode());
        olePatronDocument.setFirstName(olePatronDocumentList.get(0).getEntity().getNames().get(0).getFirstName());
        olePatronDocument.setLastName(olePatronDocumentList.get(0).getEntity().getNames().get(0).getLastName());
        olePatronDocument.setBorrowerType(olePatronDocumentList.get(0).getOleBorrowerType().getBorrowerTypeName());
        return olePatronDocument;
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

    public KualiDecimal billWiseTransaction(List<PatronBillPayment> patronBillPayments, KualiDecimal paymentAmount, String paymentMethod, String fullyPaidStatus, String partiallyPaidStatus, boolean acceptFlag, String forgiveErrorNote,String transactionNumber,String transactionNote,String paidByUser,List<OleItemLevelBillPayment> currentSessionTransactions) {
        KualiDecimal payAmt = paymentAmount;
        LOG.debug("Inside billWiseTransaction");
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        LOG.debug("Inside billWiseCancellation");
        List<String> billId = new ArrayList<String>();
        for (PatronBillPayment patronBillPayment : patronBillPayments) {
            if (patronBillPayment.isSelectBill() || acceptFlag) {
                billId.add(patronBillPayment.getBillNumber());
            }

        }
        List<PatronBillPayment> patronBillPaymentsList = (List<PatronBillPayment>) getBusinessObjectService().findAll(PatronBillPayment.class);
        KualiDecimal unPaidAmount;
        for (PatronBillPayment patronBillPayment : patronBillPaymentsList) {
            if (billId.contains(patronBillPayment.getBillNumber()) || acceptFlag) {
                List<FeeType> feeTypes = patronBillPayment.getFeeType();
                boolean saveFlag = false;
                for (FeeType feeType : feeTypes) {
                    if (((feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_OUTSTANDING) || feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_PARTIALLY)
                    || feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_FEE_PAR_TRANS)) && paymentAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) > 0)) {
                        if (paymentAmount.compareTo(feeType.getBalFeeAmount()) >= 0) {
                            unPaidAmount = patronBillPayment.getUnPaidBalance().subtract(feeType.getBalFeeAmount());
                            paymentAmount = paymentAmount.subtract(feeType.getBalFeeAmount());
                            OlePaymentStatus olePaymentStatus = getPaymentStatus(fullyPaidStatus);
                            feeType.setOlePaymentStatus(olePaymentStatus);
                            feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                            List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                                    ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(feeType.getBalFeeAmount());
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                            oleItemLevelBillPayment.setNote(forgiveErrorNote);
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                            currentSessionTransactions.add(oleItemLevelBillPayment);
                            feeType.setBalFeeAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                            feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.ERROR)) {
                                feeType.setErrorNote(forgiveErrorNote);
                            }
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.FORGIVE)) {
                                feeType.setForgiveNote(forgiveErrorNote);
                            }
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.PAY_TRANSFERRED)) {
                                feeType.setTransferNote(forgiveErrorNote);
                            }
                            patronBillPayment.setPaymentAmount(payAmt.subtract(paymentAmount));
                            payAmt = paymentAmount;
                        } else {
                            unPaidAmount = patronBillPayment.getUnPaidBalance().subtract(paymentAmount);
                            KualiDecimal updatedFeeAmount = feeType.getBalFeeAmount().subtract(paymentAmount);
                            KualiDecimal transAmount = paymentAmount;
                            paymentAmount = paymentAmount.subtract(feeType.getBalFeeAmount());
                            OlePaymentStatus olePaymentStatus = getPaymentStatus(partiallyPaidStatus);
                            feeType.setOlePaymentStatus(olePaymentStatus);
                            feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                            List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                                    ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(transAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) < 0 ? transAmount : transAmount);
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                            oleItemLevelBillPayment.setNote(forgiveErrorNote);
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                            currentSessionTransactions.add(oleItemLevelBillPayment);
                            feeType.setBalFeeAmount(updatedFeeAmount);
                            feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.ERROR)) {
                                feeType.setErrorNote(forgiveErrorNote);
                            }
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.FORGIVE)) {
                                feeType.setForgiveNote(forgiveErrorNote);
                            }
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.PAY_TRANSFERRED)) {
                                feeType.setTransferNote(forgiveErrorNote);
                            }
                            patronBillPayment.setPaymentAmount(payAmt);
                        }
                        if (unPaidAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) <= 0) {
                            patronBillPayment.setSelectBill(false);
                        }
                        patronBillPayment.setUnPaidBalance(unPaidAmount);
                        saveFlag = true;
                        if (LOG.isDebugEnabled()){
                            LOG.debug("unPaidAmount" + patronBillPayment.getUnPaidBalance());
                        }
                    }
                }
                if (saveFlag) {
                    patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                    patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                    patronBillPayment.setPaymentMethod(paymentMethod);
                    if (patronBillPayment.getPaymentMethod().equals(OLEConstants.FORGIVE) || patronBillPayment.getPaymentMethod().equals(OLEConstants.CANCEL) || patronBillPayment.getPaymentMethod().equals(OLEConstants.ERROR)) {
                        if(patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.FORGIVE)){
                            patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern()+patronBillPayment.getPaymentAmount() +" "+ OLEConstants.FORGIVE_MESSAGE);
                        }
                        if(patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.CANCEL)){
                            patronBillPayment.setFreeTextNote(OLEConstants.CANCEL_MESSAGE);
                        }
                        if(patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.ERROR)){
                            patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern()+patronBillPayment.getPaymentAmount()+OLEConstants.ERROR_MESSAGE);
                        }
                    } else {
                        patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern()+patronBillPayment.getPaymentAmount() + " paid through " + patronBillPayment.getPaymentMethod());
                        patronBillPayment.setNote(null);
                    }
                    getBusinessObjectService().save(patronBillPayment);
                }
            }
        }
        return paymentAmount;
    }

    public KualiDecimal itemWiseTransaction(List<FeeType> feeTypes, KualiDecimal paymentAmount, String paymentMethod, String fullyPaidStatus, String partiallyPaidStatus, String forgiveErrorNote,String transferNote,String transactionNumber,String transactionNote,String paidByUser,List<OleItemLevelBillPayment> currentSessionTransactions) {
        KualiDecimal payAmt = paymentAmount;
        LOG.debug("Inside itemWiseTransaction");
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        KualiDecimal unPaidAmount;
        for (FeeType feeType : feeTypes) {
            if (feeType.isActiveItem() && ((feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_OUTSTANDING) || feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_PARTIALLY) ||
                    feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_FEE_PAR_TRANS)) && paymentAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) > 0)) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("billNumber", feeType.getBillNumber());
                PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);
                List<FeeType> patronFeeTypes = patronBillPayment.getFeeType();
                for (FeeType patronFeeType : patronFeeTypes) {
                    if (patronFeeType.getId().equalsIgnoreCase(feeType.getId())) {
                        if (paymentAmount.compareTo(patronFeeType.getBalFeeAmount()) >= 0) {
                            unPaidAmount = patronBillPayment.getUnPaidBalance().subtract(feeType.getBalFeeAmount());
                            paymentAmount = paymentAmount.subtract(patronFeeType.getBalFeeAmount());
                            OlePaymentStatus olePaymentStatus = getPaymentStatus(fullyPaidStatus);
                            patronFeeType.setOlePaymentStatus(olePaymentStatus);
                            patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                            List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                    ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(patronFeeType.getBalFeeAmount());
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                            if(StringUtils.isNotEmpty(forgiveErrorNote)) {
                                oleItemLevelBillPayment.setNote(forgiveErrorNote);
                            } else if(StringUtils.isNotEmpty(transferNote)) {
                                oleItemLevelBillPayment.setNote(transferNote);
                            }
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                            currentSessionTransactions.add(oleItemLevelBillPayment);
                            patronFeeType.setBalFeeAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                            patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.ERROR)) {
                                patronFeeType.setErrorNote(forgiveErrorNote);
                            }
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.FORGIVE)) {
                                patronFeeType.setForgiveNote(forgiveErrorNote);
                            }
                            if(transferNote != null) {
                                patronFeeType.setTransferNote(transferNote);
                            }
                            feeType.setActiveItem(false);
                            patronBillPayment.setPaymentAmount(payAmt.subtract(paymentAmount));
                            payAmt = paymentAmount;
                            if(patronFeeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.REPLACEMENT_FEE)){
                                changeToLostReplace(patronFeeType);
                            }
                        } else {
                            unPaidAmount = patronBillPayment.getUnPaidBalance().subtract(paymentAmount);
                            KualiDecimal updatedFeeAmount = patronFeeType.getBalFeeAmount().subtract(paymentAmount);
                            KualiDecimal transAmount = paymentAmount;
                            paymentAmount = paymentAmount.subtract(patronFeeType.getBalFeeAmount());
                            OlePaymentStatus olePaymentStatus = getPaymentStatus(partiallyPaidStatus);
                            patronFeeType.setOlePaymentStatus(olePaymentStatus);
                            patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                            List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                    ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(transAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) < 0 ? transAmount : transAmount);
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                            oleItemLevelBillPayment.setNote(forgiveErrorNote);
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                            currentSessionTransactions.add(oleItemLevelBillPayment);
                            patronFeeType.setBalFeeAmount(updatedFeeAmount);
                            patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.ERROR)) {
                                patronFeeType.setErrorNote(forgiveErrorNote);
                            }
                            if (paymentMethod!=null && paymentMethod.equalsIgnoreCase(OLEConstants.FORGIVE)) {
                                patronFeeType.setForgiveNote(forgiveErrorNote);
                            }
                            if(transferNote != null) {
                                patronFeeType.setTransferNote(transferNote);
                            }
                            patronBillPayment.setPaymentAmount(payAmt);
                        }
                        //feeType = patronFeeType;
                        patronBillPayment.setUnPaidBalance(unPaidAmount);
                        if (LOG.isDebugEnabled()){
                            LOG.debug("unPaidAmount" + patronBillPayment.getUnPaidBalance());
                        }
                        patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                        patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                        patronBillPayment.setPaymentMethod(paymentMethod);
                        if (patronBillPayment.getPaymentMethod().equals(OLEConstants.FORGIVE) || patronBillPayment.getPaymentMethod().equals(OLEConstants.CANCEL) || patronBillPayment.getPaymentMethod().equals(OLEConstants.ERROR)) {
                            if(patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.FORGIVE)){
                                patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern()+patronBillPayment.getPaymentAmount()+" "+OLEConstants.FORGIVE_MESSAGE);
                            }
                            if(patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.CANCEL)){
                                patronBillPayment.setFreeTextNote(OLEConstants.CANCEL_MESSAGE_AMT+" "+CurrencyFormatter.getSymbolForCurrencyPattern()+patronBillPayment.getPaymentAmount());
                            }
                            if(patronBillPayment.getPaymentMethod().equalsIgnoreCase(OLEConstants.ERROR)){
                                patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern()+patronBillPayment.getPaymentAmount()+OLEConstants.ERROR_MESSAGE);
                            }
                        } else {
                            patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern() + patronBillPayment.getPaymentAmount() + " paid through " + patronBillPayment.getPaymentMethod());
                            patronBillPayment.setNote(null);
                        }
                        getBusinessObjectService().save(patronBillPayment);
                    }
                }
            }
        }
        return paymentAmount;
    }

    public void billWiseCancellation(List<PatronBillPayment> patronBillPayments, String cancellationNote,List<OleItemLevelBillPayment> currentSessionTransactions) {
        LOG.debug("Inside billWiseCancellation");
        String billId = null;
        for (PatronBillPayment patronBillPayment : patronBillPayments) {
            if (patronBillPayment.isSelectBill()) {
                billId = patronBillPayment.getBillNumber();
            }

        }
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        List<PatronBillPayment> patronBillPaymentsList = (List<PatronBillPayment>) getBusinessObjectService().findAll(PatronBillPayment.class);
        for (PatronBillPayment patronBillPayment : patronBillPaymentsList) {
            if (patronBillPayment.getBillNumber().equalsIgnoreCase(billId)) {
                List<FeeType> feeTypes = patronBillPayment.getFeeType();
                for (FeeType feeType : feeTypes) {
                    OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.CANCELLED);
                    feeType.setOlePaymentStatus(olePaymentStatus);
                    feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                    if (cancellationNote != null) {
                        feeType.setCancellationNote(cancellationNote);
                    }
                    List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                            ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                    OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                    oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                    oleItemLevelBillPayment.setPaymentMode(OLEConstants.FEE_CANCELLED);
                    oleItemLevelBillPayment.setAmount(feeType.getBalFeeAmount());
                    oleItemLevelBillPayment.setCreatedUser(operatorId);
                    oleItemLevelBillPayment.setNote(cancellationNote);
                    oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                    currentSessionTransactions.add(oleItemLevelBillPayment);
                    feeType.setBalFeeAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                    feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                }
                patronBillPayment.setUnPaidBalance(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                if (LOG.isDebugEnabled()){
                    LOG.debug("unPaidAmount" + patronBillPayment.getUnPaidBalance());
                }
                patronBillPayment.setFreeTextNote(OLEConstants.CANCEL_MESSAGE);
                patronBillPayment.setNote(null);
                patronBillPayment.setSelectBill(false);
                patronBillPayment.setPaymentMethod(OLEConstants.CANCEL);
                patronBillPayment.setPaymentAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                getBusinessObjectService().save(patronBillPaymentsList);

            }

        }
        patronBillPayments.clear();
        patronBillPayments.addAll(patronBillPaymentsList);
    }

    public void itemWiseCancellation(List<FeeType> feeTypes, String cancellationNote,List<OleItemLevelBillPayment> currentSessionTransactions) {
        LOG.debug("Inside itemWiseCancellation");
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        for (FeeType feeType : feeTypes) {
            if (feeType.isActiveItem()) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("billNumber", feeType.getBillNumber());
                PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);
                List<FeeType> patronFeeTypes = patronBillPayment.getFeeType();
                for (FeeType patronFeeType : patronFeeTypes) {
                    if (patronFeeType.getId().equalsIgnoreCase(feeType.getId())) {
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.CANCELLED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        if (cancellationNote != null) {
                            patronFeeType.setCancellationNote(cancellationNote);
                        }
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.FEE_CANCELLED);
                        oleItemLevelBillPayment.setAmount(patronFeeType.getBalFeeAmount());
                        oleItemLevelBillPayment.setNote(cancellationNote);
                        BigDecimal billValue=patronFeeType.getBalFeeAmount().bigDecimalValue();
                        if(billValue==null){
                            billValue=OLEConstants.BIGDECIMAL_DEF_VALUE;
                        }
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        patronFeeType.setBalFeeAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                        patronFeeType.setActiveItem(false);
                        patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        patronBillPayment.setUnPaidBalance(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                        if (LOG.isDebugEnabled()){
                            LOG.debug("unPaidAmount" + patronBillPayment.getUnPaidBalance());
                        }
                        patronBillPayment.setFreeTextNote(OLEConstants.CANCEL_MESSAGE_AMT+CurrencyFormatter.getSymbolForCurrencyPattern()+billValue);
                        patronBillPayment.setNote(null);
                        patronBillPayment.setPaymentMethod(OLEConstants.CANCEL);
                        patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                        patronBillPayment.setPaymentAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                        patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                        getBusinessObjectService().save(patronBillPayment);
                    }
                }
            }
        }
    }

    public boolean isSelectedPaidPatronBillFullyPaid(List<PatronBillPayment> patronBillPayments){
        billIds.clear();
        boolean isSuccess=true;
        for(PatronBillPayment patronBillPayment:patronBillPayments){
            if(patronBillPayment.getUnPaidBalance().floatValue()==OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE.floatValue() && patronBillPayment.isSelectBill()){
               isSuccess=false;
                if(!billIds.contains(patronBillPayment.getBillNumber()))
                    billIds.add(patronBillPayment.getBillNumber());
            }

        }
      return isSuccess;
    }

    public  boolean isSelectedFeeTypeFullyPaid(List<FeeType> feeTypes){
       billIds.clear();
       boolean isSuccess=true;
        for(FeeType feeType:feeTypes){
            if(feeType.getBalFeeAmount().floatValue()==OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE.floatValue() && feeType.isActiveItem()){
                isSuccess=false;
                if(!billIds.contains(feeType.getBillNumber()))
                   billIds.add(feeType.getBillNumber());
            }
        }
        return isSuccess;
    }

    public KualiDecimal getSumOfSelectedPatronBills(List<PatronBillPayment>  patronBillPayments){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(PatronBillPayment patronBillPayment:patronBillPayments){
            if (patronBillPayment.isSelectBill()) {
                sum = sum.add(patronBillPayment.getUnPaidBalance());
            }
        }
        return sum;
    }

    public KualiDecimal getSumOfSelectedCreditPatronBills(List<PatronBillPayment>  patronBillPayments){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(PatronBillPayment patronBillPayment:patronBillPayments){
            if (patronBillPayment.isSelectBill()) {
                sum = sum.add(patronBillPayment.getCreditRemaining().negated());
            }
        }
        return sum;
    }

    public KualiDecimal getSumOfSelectedFeeTypes(List<FeeType>  feeTypes){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(FeeType feeType:feeTypes){
            if (feeType.isActiveItem()) {
                sum = sum.add(feeType.getBalFeeAmount());
            }
        }
        return sum;
    }

    public KualiDecimal getSumOfSelectedCreditFeeTypes(List<FeeType>  feeTypes){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(FeeType feeType:feeTypes){
            if (feeType.isActiveItem()) {
                sum = sum.add(feeType.getCreditRemaining().negated());
            }
        }
        return sum;
    }


    public List<FeeType> getUpdateItemDetailsToFeeTypeList(List<FeeType> feeTypes) {
        try {
            for (FeeType feeType : feeTypes) {
                if(feeType.getFeeTypes()!=null){
                    feeType.getFeeTypes().add(feeType);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while updating item details to feeTypeList", e);
        }
        return feeTypes;
    }

    public List validPaymentStatusForCredit() {
        List validPaymentStatusList = new ArrayList();
        validPaymentStatusList.add("PAY_FULL");
        validPaymentStatusList.add("PAY_PAR");
        validPaymentStatusList.add("PAY_FEE_FULLY_TRANSFERED");
        validPaymentStatusList.add("PAY_FEE_PARTIALLY_TRANSFERED");
        return validPaymentStatusList;
    }

    public KualiDecimal itemWiseCreditTransaction(List<FeeType> feeTypes, KualiDecimal paymentAmount, String paymentMethod,String transactionNumber,String transactionNote,List<OleItemLevelBillPayment> currentSessionTransactions,String creditNote) {
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        KualiDecimal remainingPaymentAmount = paymentAmount;
        KualiDecimal amountCredited = paymentAmount;
        for (FeeType feeType : feeTypes) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("billNumber", feeType.getBillNumber());
            PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);

            List<FeeType> patronFeeTypes = patronBillPayment.getFeeType();
            for (FeeType patronFeeType : patronFeeTypes) {
                if (feeType.isActiveItem() && patronFeeType.getId().equalsIgnoreCase(feeType.getId())) {
                    if (remainingPaymentAmount.compareTo(patronFeeType.getPaidAmount()) >= 0) {
                        paymentAmount = paymentAmount.subtract(patronFeeType.getPaidAmount());
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_FULL_CRDT_ISSUED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setCreditIssued(patronFeeType.getPaidAmount().negated());
                        patronFeeType.setCreditRemaining(patronFeeType.getPaidAmount().negated());
                        patronBillPayment.setCreditIssued(patronBillPayment.getCreditIssued().add(patronFeeType.getPaidAmount().negated()));
                        patronBillPayment.setCreditRemaining(patronBillPayment.getCreditRemaining().add(patronFeeType.getPaidAmount().negated()));
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                        oleItemLevelBillPayment.setAmount(patronFeeType.getPaidAmount().negated());
                        oleItemLevelBillPayment.setNote(creditNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        patronFeeType.setCreditNote(creditNote);
                        patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setActiveItem(false);
                        patronBillPayment.setPaymentAmount(paymentAmount);
                        remainingPaymentAmount = paymentAmount;

                    } else if (remainingPaymentAmount.isGreaterThan(KualiDecimal.ZERO)) {
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_PAR_CRDT_ISSUED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        patronFeeType.setCreditIssued(remainingPaymentAmount.negated());
                        patronFeeType.setCreditRemaining(remainingPaymentAmount.negated());
                        patronBillPayment.setCreditIssued(remainingPaymentAmount.negated());
                        patronBillPayment.setCreditRemaining(remainingPaymentAmount.negated());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                        oleItemLevelBillPayment.setAmount(remainingPaymentAmount.negated());
                        oleItemLevelBillPayment.setNote(creditNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        patronFeeType.setCreditNote(creditNote);
                        patronBillPayment.setPaymentAmount(KualiDecimal.ZERO);
                        remainingPaymentAmount = KualiDecimal.ZERO;
                    }

                    patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern() + amountCredited + " credited through " + patronBillPayment.getPaymentMethod());
                    patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                    patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                    patronBillPayment.setPaymentMethod(paymentMethod);
                    patronBillPayment.setNote(null);
                    getBusinessObjectService().save(patronBillPayment);
                }
            }
        }
        return paymentAmount;
    }


    public KualiDecimal billWiseCreditTransaction(List<PatronBillPayment> patronBillPayments, KualiDecimal paymentAmount, String paymentMethod,String transactionNumber,String transactionNote, List<OleItemLevelBillPayment> currentSessionTransactions,String creditNote) {
        KualiDecimal remainingPaymentAmount = paymentAmount;
        KualiDecimal amountCredited = paymentAmount;
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        List<String> billId = new ArrayList<String>();
        for (PatronBillPayment patronBillPayment : patronBillPayments) {
            if (patronBillPayment.isSelectBill()) {
                billId.add(patronBillPayment.getBillNumber());
            }
        }
        List<PatronBillPayment> patronBillPaymentsList = (List<PatronBillPayment>) getBusinessObjectService().findAll(PatronBillPayment.class);
        for (PatronBillPayment patronBillPayment : patronBillPaymentsList) {
            if (billId.contains(patronBillPayment.getBillNumber()) ) {
                List<FeeType> feeTypes = patronBillPayment.getFeeType();
                boolean saveFlag = false;
                for (FeeType feeType : feeTypes) {
                    if (remainingPaymentAmount.compareTo(feeType.getPaidAmount()) >= 0) {
                        paymentAmount = paymentAmount.subtract(feeType.getPaidAmount());
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_FULL_CRDT_ISSUED);
                        feeType.setOlePaymentStatus(olePaymentStatus);
                        feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        feeType.setCreditIssued(feeType.getPaidAmount().negated());
                        patronBillPayment.setCreditIssued(feeType.getPaidAmount().negated());
                        feeType.setCreditRemaining(feeType.getPaidAmount().negated());
                        patronBillPayment.setCreditRemaining(feeType.getPaidAmount().negated());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                                ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setAmount(feeType.getPaidAmount().negated());
                        oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                        oleItemLevelBillPayment.setNote(creditNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setCreditNote(creditNote);
                        remainingPaymentAmount = paymentAmount;
                        if(feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.REPLACEMENT_FEE)) {
                            changeToLostReplace(feeType);
                        }
                    } else if (remainingPaymentAmount.isGreaterThan(KualiDecimal.ZERO)) {
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_PAR_CRDT_ISSUED);
                        feeType.setOlePaymentStatus(olePaymentStatus);
                        feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        feeType.setCreditIssued(paymentAmount.negated());
                        patronBillPayment.setCreditIssued(paymentAmount.negated());
                        feeType.setCreditRemaining(paymentAmount.negated());
                        patronBillPayment.setCreditRemaining(paymentAmount.negated());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                                ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                        oleItemLevelBillPayment.setAmount(remainingPaymentAmount.negated());
                        oleItemLevelBillPayment.setNote(creditNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setCreditNote(creditNote);
                        remainingPaymentAmount = KualiDecimal.ZERO;
                    }
                    saveFlag = true;
                }
                if (saveFlag) {
                    patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern() + amountCredited + " credited through " + patronBillPayment.getPaymentMethod());
                    patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                    patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                    patronBillPayment.setPaymentMethod(paymentMethod);
                    getBusinessObjectService().save(patronBillPayment);
                }
            }
        }
        return paymentAmount;
    }


    public KualiDecimal itemWiseTransferCreditTransaction(List<FeeType> feeTypes, KualiDecimal transferAmount, String paymentMethod,String transactionNumber,String transactionNote,List<OleItemLevelBillPayment> currentSessionTransactions,String transferNote) {
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        KualiDecimal remainingPaymentAmount = transferAmount;
        for (FeeType feeType : feeTypes) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("billNumber", feeType.getBillNumber());
            PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);

            List<FeeType> patronFeeTypes = patronBillPayment.getFeeType();
            for (FeeType patronFeeType : patronFeeTypes) {
                if (feeType.isActiveItem() && patronFeeType.getId().equalsIgnoreCase(feeType.getId())) {
                    if (remainingPaymentAmount.compareTo(patronFeeType.getCreditRemaining().negated()) >= 0) {
                        transferAmount = transferAmount.subtract(patronFeeType.getCreditRemaining().negated());
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_CRDT_FULLY_TRANSFERRED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setCreditRemaining(KualiDecimal.ZERO);
                        patronFeeType.setCreditIssued(patronFeeType.getCreditIssued());
                        patronBillPayment.setCreditIssued(patronFeeType.getCreditIssued());
                        patronBillPayment.setCreditRemaining(KualiDecimal.ZERO);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                        oleItemLevelBillPayment.setAmount(patronFeeType.getPaidAmount().negated());
                        oleItemLevelBillPayment.setNote(transferNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        patronFeeType.setTransferNote(transferNote);
                        patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setActiveItem(false);
                        patronBillPayment.setPaymentAmount(transferAmount);
                        remainingPaymentAmount = transferAmount;

                    } else if (remainingPaymentAmount.isGreaterThan(KualiDecimal.ZERO)) {
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_CRDT_PAR_TRANSFERRED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        patronBillPayment.setCreditIssued(patronBillPayment.getCreditIssued());
                        patronFeeType.setCreditIssued(patronFeeType.getCreditIssued());
                        patronBillPayment.setCreditRemaining((patronBillPayment.getCreditRemaining().negated().subtract(remainingPaymentAmount)).negated());
                        patronFeeType.setCreditRemaining((patronFeeType.getCreditRemaining().negated().subtract(remainingPaymentAmount)).negated());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                        oleItemLevelBillPayment.setAmount(remainingPaymentAmount.negated());
                        oleItemLevelBillPayment.setNote(transferNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        patronFeeType.setTransferNote(transferNote);
                        patronBillPayment.setPaymentAmount(KualiDecimal.ZERO);
                        remainingPaymentAmount = KualiDecimal.ZERO;
                    }
                    patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                    patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                    patronBillPayment.setPaymentMethod(paymentMethod);
                    patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern() + patronBillPayment.getPaymentAmount() + " credited through " + patronBillPayment.getPaymentMethod());
                    patronBillPayment.setNote(null);
                    getBusinessObjectService().save(patronBillPayment);
                }
            }
        }
        return transferAmount;
    }


    public KualiDecimal billWiseTransferCreditTransaction(List<PatronBillPayment> patronBillPayments, KualiDecimal transferAmount, String paymentMethod,String transactionNumber,String transactionNote, List<OleItemLevelBillPayment> currentSessionTransactions,String transferNote) {
        KualiDecimal remainingPaymentAmount = transferAmount;
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        List<String> billId = new ArrayList<String>();
        for (PatronBillPayment patronBillPayment : patronBillPayments) {
            if (patronBillPayment.isSelectBill()) {
                billId.add(patronBillPayment.getBillNumber());
            }
        }
        List<PatronBillPayment> patronBillPaymentsList = (List<PatronBillPayment>) getBusinessObjectService().findAll(PatronBillPayment.class);
        for (PatronBillPayment patronBillPayment : patronBillPaymentsList) {
            if (billId.contains(patronBillPayment.getBillNumber()) ) {
                List<FeeType> feeTypes = patronBillPayment.getFeeType();
                boolean saveFlag = false;
                for (FeeType feeType : feeTypes) {
                    if (remainingPaymentAmount.compareTo(feeType.getPaidAmount()) >= 0) {
                        transferAmount = transferAmount.subtract(feeType.getPaidAmount());
                        OlePaymentStatus olePaymentStatus = getPaymentStatus("PAY_FULL_CRDT_ISSUED");
                        feeType.setOlePaymentStatus(olePaymentStatus);
                        feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        patronBillPayment.setCreditIssued(feeType.getCreditIssued());
                        patronBillPayment.setCreditRemaining((patronBillPayment.getCreditRemaining().negated().subtract(remainingPaymentAmount)).negated());
                        feeType.setCreditRemaining((feeType.getCreditRemaining().negated().subtract(remainingPaymentAmount)).negated());
                        feeType.setCreditIssued(feeType.getCreditIssued());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                                ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setAmount(feeType.getPaidAmount().negated());
                        oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        oleItemLevelBillPayment.setNote(transferNote);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setTransferNote(transferNote);
                        remainingPaymentAmount = transferAmount;
                    } else if (remainingPaymentAmount.isGreaterThan(KualiDecimal.ZERO)) {
                        KualiDecimal updatedFeeAmount = feeType.getBalFeeAmount().subtract(transferAmount);
                        transferAmount = transferAmount.subtract(feeType.getBalFeeAmount());
                        OlePaymentStatus olePaymentStatus = getPaymentStatus("PAY_PAR_CRDT_ISSUED");
                        feeType.setOlePaymentStatus(olePaymentStatus);
                        feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        feeType.setCreditIssued(feeType.getCreditIssued());
                        patronBillPayment.setCreditIssued(patronBillPayment.getCreditIssued());
                        feeType.setCreditRemaining((feeType.getCreditRemaining().negated().subtract(remainingPaymentAmount)).negated());
                        patronBillPayment.setCreditRemaining((patronBillPayment.getCreditRemaining().negated().subtract(remainingPaymentAmount)).negated());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                                ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                        oleItemLevelBillPayment.setAmount(remainingPaymentAmount.negated());
                        oleItemLevelBillPayment.setNote(transferNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setTransferNote(transferNote);
                        remainingPaymentAmount = KualiDecimal.ZERO;
                    }
                    saveFlag = true;
                }
                if (saveFlag) {
                    patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                    patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                    patronBillPayment.setPaymentMethod(paymentMethod);
                    getBusinessObjectService().save(patronBillPayment);
                }
            }
        }
        return transferAmount;
    }


    public KualiDecimal getSumOfSelectedFeePaidAmount(List<FeeType>  feeTypes){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(FeeType feeType:feeTypes){
            if (feeType.isActiveItem()) {
                sum = sum.add(feeType.getPaidAmount());
            }
        }
        return sum;
    }

    public boolean validateInputFields(PatronBillForm patronBillForm, KualiDecimal paymentAmount,List<PatronBillPayment> patronBillPayments) {
        boolean valid = true;

        if (patronBillForm.getPaidAmount() == null) {
            valid = false;
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.PAID_AMT_EMPTY);
        }

        return valid;
    }

    public List getValidPaymentStatuForCredit() {
        List validPaymentStatusList = new ArrayList();
        validPaymentStatusList.add("PAY_FULL");
        validPaymentStatusList.add("PAY_PAR");
        validPaymentStatusList.add("PAY_FEE_FULLY_TRANSFERED");
        validPaymentStatusList.add("PAY_FEE_PARTIALLY_TRANSFERED");
        return validPaymentStatusList;
    }
    public boolean validatePaymentStatusForFeeType(PatronBillForm patronBillForm) {
        for(FeeType feeType : patronBillForm.getFeeTypes()) {
            if(feeType.isActiveItem() &&  !getValidPaymentStatuForCredit().contains(feeType.getPaymentStatusCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.NOT_ELIGIBLE_FOR_CREDIT, new String[]{feeType.getOlePaymentStatus().getPaymentStatusName()});
                return false;
            }
        }
        return true;
    }

    public boolean validatePaymentStatusForPatronBillPayment(PatronBillForm patronBillForm) {
        for (PatronBillPayment patronBillPayment : patronBillForm.getPatronBillPaymentList()) {
            if (patronBillPayment.isSelectBill() && !getValidPaymentStatuForCredit().contains(patronBillPayment.getPaymentStatusCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.NOT_ELIGIBLE_FOR_CREDIT, new String[]{patronBillPayment.getPaymentStatusName()});
                return false;
            }
        }
        return true;
    }




    public boolean validatePaymentStatusForDebitFeeType(PatronBillForm patronBillForm) {
        for(FeeType feeType : patronBillForm.getFeeTypes()) {
            if(feeType.isActiveItem() &&  feeType.getBalFeeAmount().isLessEqual(KualiDecimal.ZERO)) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NOT_ELIGIBLE_FOR_DEBIT);
                return false;
            }
        }
        return true;
    }

    public boolean validatePaymentStatusForDebitPatronBillPayment(PatronBillForm patronBillForm) {
        for (PatronBillPayment patronBillPayment : patronBillForm.getPatronBillPaymentList()) {
            if (patronBillPayment.isSelectBill() && patronBillPayment.getUnPaidBalance().isLessEqual(KualiDecimal.ZERO)) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NOT_ELIGIBLE_FOR_DEBIT);
                return false;
            }
        }
        return true;
    }


    public KualiDecimal getSumOfSelectedPaidPatronBills(List<PatronBillPayment>  patronBillPayments){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(PatronBillPayment patronBillPayment:patronBillPayments){
            if (patronBillPayment.isSelectBill()) {
                sum = sum.add(patronBillPayment.getPaidAmount());
            }
        }
        return sum;
    }

    public KualiDecimal getSumOfPaidPatronBills(List<PatronBillPayment>  patronBillPayments){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(PatronBillPayment patronBillPayment:patronBillPayments){
                sum = sum.add(patronBillPayment.getPaidAmount());
        }
        return sum;
    }

    public boolean checkOutstandingCreditAmountForPatronBillPayment(PatronBillForm patronBillForm,List<PatronBillPayment>  patronBillPayments) {
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        boolean valid = true;
        for(PatronBillPayment patronBillPayment:patronBillPayments){
            if (patronBillPayment.isSelectBill() && !patronBillPayment.getCreditRemaining().negated().isGreaterThan(KualiDecimal.ZERO)) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.REFUND_NOT_APPLICABLE);
                valid &=  false;
            }
            if (patronBillPayment.isSelectBill()) {
                sum = sum.add(patronBillPayment.getCreditRemaining().negated());
            }
        }
        if(patronBillForm.getTransferAmount().isGreaterThan(sum)) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.REFUND_AMT_NOT_APPLICABLE);
            valid &=false;
        }
        return valid;
    }

    public boolean checkOutstandingCreditAmountForFeeType(PatronBillForm patronBillForm,List<FeeType> feeTypes) {
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        boolean valid = true;
        for(FeeType feeType : feeTypes) {
            if(feeType.isActiveItem() &&  !feeType.getCreditRemaining().negated().isGreaterThan (KualiDecimal.ZERO)) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.REFUND_NOT_APPLICABLE);
                return false;
            }
            if(feeType.isActiveItem()) {
                sum = sum.add(feeType.getCreditRemaining().negated());
            }
        }
        if(patronBillForm.getTransferAmount().isGreaterThan(sum)) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.REFUND_AMT_NOT_APPLICABLE);
            valid &=false;
        }
        return valid;
    }

    public OleCirculationDesk getDefaultCirculationDesk(String principleId) {
        LOG.debug("Inside the getDefaultCirculationDesk method");
        Map barMap = new HashMap();
        barMap.put(OLEConstants.OPTR_ID, principleId);
        List<OleCirculationDeskDetail> oleCirculationDeskDetails = (List<OleCirculationDeskDetail>) getBusinessObjectService().findMatching(OleCirculationDeskDetail.class, barMap);
        for (OleCirculationDeskDetail oleCirculationDeskDetail : oleCirculationDeskDetails) {
            if (oleCirculationDeskDetail.isDefaultLocation()) {
                return oleCirculationDeskDetail.getOleCirculationDesk();
            }
        }
        return null;
    }

    public List getEligibleFeeTypes() {
        List feeTypeCodeList = new ArrayList();
        List<FeeType> feeTypes = new ArrayList<>();
        OleCirculationDesk oleCirculationDesk = getDefaultCirculationDesk(GlobalVariables.getUserSession().getPrincipalId());
        List<OleCirculationDeskFeeType> eligibleFeeTypes = oleCirculationDesk.getOleCirculationDeskFeeTypeList();
        if(eligibleFeeTypes.size() > 0) {
            for(OleCirculationDeskFeeType oleCirculationDeskFeeType : eligibleFeeTypes) {
                feeTypeCodeList.add(oleCirculationDeskFeeType.getFeeTypeCode());
            }
        }
        return feeTypeCodeList;
    }

    public boolean checkOutstsndingBills(String patronId) {
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
        List feeTypeCodeList = getEligibleFeeTypes();
        List<FeeType> feeTypes = new ArrayList<>();
        if(feeTypeCodeList.size() > 0) {
            Criteria criteria = new Criteria();
            criteria.addGreaterThan("balFeeAmount", new KualiDecimal(0));
            criteria.addEqualTo("patronBillPayment.patronId", patronId);
            criteria.addIn("oleFeeType.feeTypeCode", feeTypeCodeList);
            feeTypes = oleLoanDocumentDaoOjb.getFeeTypeDocument(criteria);
        }
        if(feeTypes!= null && feeTypes.size() > 0) {
            return true;
        }
        return false;
    }

    public KualiDecimal refundItemTypeBills(String billnumbers,String patronId,KualiDecimal refundAmount,String transactionNote,String transactionNumber,String paymentMethod,List<OleItemLevelBillPayment> currentSessionTransactions) {

        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
        List feeTypeCodeList = getEligibleFeeTypes();
        List<FeeType> feeTypes = new ArrayList<>();
        billMap = new HashMap();
        if(feeTypeCodeList.size() > 0) {
            Criteria criteria = new Criteria();
            criteria.addGreaterThan("balFeeAmount", new KualiDecimal(0));
            criteria.addEqualTo("patronBillPayment.patronId", patronId);
            criteria.addIn("oleFeeType.feeTypeCode", feeTypeCodeList);
            feeTypes = oleLoanDocumentDaoOjb.getFeeTypeDocument(criteria);
        }
        KualiDecimal refundedAmount = KualiDecimal.ZERO;
        Collections.sort(feeTypes,PatronBillHelperService.sortPatronBill());
        if(feeTypes!= null && feeTypes.size() > 0) {
            String operatorId = GlobalVariables.getUserSession().getPrincipalId();
            KualiDecimal unPaidAmount = KualiDecimal.ZERO;
            KualiDecimal payAmt = refundAmount;
                    for (FeeType patronFeeType : feeTypes) {
                        billNumbers = billNumbers+patronFeeType.getBillNumber()+",";
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("billNumber", patronFeeType.getBillNumber());
                        PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);
                            if (refundAmount.compareTo(patronFeeType.getBalFeeAmount()) >= 0) {
                                unPaidAmount = patronBillPayment.getUnPaidBalance().subtract(patronFeeType.getBalFeeAmount());
                                refundAmount = refundAmount.subtract(patronFeeType.getBalFeeAmount());
                                refundedAmount = refundedAmount.add(patronFeeType.getBalFeeAmount());
                                OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_FULLY_PAID_CRDT);
                                patronFeeType.setOlePaymentStatus(olePaymentStatus);
                                patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                                List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                        ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                                OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                                oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                                oleItemLevelBillPayment.setAmount(patronFeeType.getBalFeeAmount());
                                oleItemLevelBillPayment.setCreatedUser(operatorId);
                                String note = "Credit applied from bill number "+billnumbers;
                                oleItemLevelBillPayment.setNote(note);
                                oleItemLevelBillPayment.setTransactionNote(transactionNote);
                                oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                                oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                                billMap.put(oleItemLevelBillPayment.getPaymentDate().toString(),patronBillPayment.getBillNumber());
                                oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                                currentSessionTransactions.add(oleItemLevelBillPayment);
                                patronFeeType.setBalFeeAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
                                patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                                patronFeeType.setActiveItem(false);
                                patronBillPayment.setPaymentAmount(payAmt.subtract(refundAmount));
                                payAmt = refundAmount;
                            } else if(refundAmount.isGreaterThan(KualiDecimal.ZERO)){
                                unPaidAmount = patronBillPayment.getUnPaidBalance().subtract(refundAmount);
                                KualiDecimal updatedFeeAmount = patronFeeType.getBalFeeAmount().subtract(refundAmount);
                                KualiDecimal transAmount = refundAmount;
                                refundAmount = refundAmount.subtract(patronFeeType.getBalFeeAmount());
                                //refundAmount = KualiDecimal.ZERO;
                                refundedAmount = refundedAmount.add(patronFeeType.getBalFeeAmount());
                                OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_PAR_PAID_CRDT);
                                patronFeeType.setOlePaymentStatus(olePaymentStatus);
                                patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                                List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                        ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                                OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                                oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));

                                oleItemLevelBillPayment.setAmount(transAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) < 0 ? transAmount : transAmount);
                                oleItemLevelBillPayment.setCreatedUser(operatorId);
                                String note = "Credit applied from bill number "+billnumbers;
                                oleItemLevelBillPayment.setNote(note);
                                oleItemLevelBillPayment.setTransactionNote(transactionNote);
                                oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                                billMap.put(oleItemLevelBillPayment.getPaymentDate().toString(),patronBillPayment.getBillNumber());
                                oleItemLevelBillPayment.setPaymentMode(paymentMethod);
                                oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                                currentSessionTransactions.add(oleItemLevelBillPayment);
                                patronFeeType.setBalFeeAmount(updatedFeeAmount);
                                patronFeeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                                patronBillPayment.setPaymentAmount(payAmt);
                            }
                            patronBillPayment.setUnPaidBalance(unPaidAmount);
                            if (LOG.isDebugEnabled()){
                                LOG.debug("unPaidAmount" + patronBillPayment.getUnPaidBalance());
                            }
                            patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                            patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                            patronBillPayment.setPaymentMethod(paymentMethod);
                            patronBillPayment.setFreeTextNote(CurrencyFormatter.getSymbolForCurrencyPattern() + patronBillPayment.getPaymentAmount() + " paid through " + patronBillPayment.getPaymentMethod());
                            patronBillPayment.setNote(null);
                            getBusinessObjectService().save(patronBillPayment);
                    }
        }
        return refundAmount;
    }

    public KualiDecimal updateRemainingCreditAmountForItem(KualiDecimal refundedAmount,List<FeeType> feeTypes,String transactionNote,String transactionNumber,List<OleItemLevelBillPayment> currentSessionTransactions,String refundNote) {
        KualiDecimal creditRefunded = refundedAmount;
        KualiDecimal sumOfCreditRemaining = KualiDecimal.ZERO;
        boolean saveFlag = false;
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        for (FeeType feeType : feeTypes) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("billNumber", feeType.getBillNumber());
            PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);

            List<FeeType> patronFeeTypes = patronBillPayment.getFeeType();
            for (FeeType patronFeeType : patronFeeTypes) {
                if (feeType.isActiveItem() && patronFeeType.getId().equalsIgnoreCase(feeType.getId())) {
                    if (patronFeeType.getFeeAmount().subtract(creditRefunded).isGreaterEqual(patronFeeType.getCreditRemaining().negated())) {
                        KualiDecimal transAmount = patronFeeType.getCreditRemaining().negated();
                        sumOfCreditRemaining = sumOfCreditRemaining.add(patronFeeType.getCreditRemaining().negated());
                        creditRefunded = creditRefunded.subtract(patronFeeType.getCreditRemaining().negated());
                        patronFeeType.setCreditRemaining(KualiDecimal.ZERO);
                        patronBillPayment.setCreditRemaining(KualiDecimal.ZERO);
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_FULLY_REFUNDED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        patronFeeType.setRefundNote(refundNote);
                        List<OleItemLevelBillPayment> paidTransactions = getPaidOutstandingBillTransaction(currentSessionTransactions);
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        for(int i=0;i<paidTransactions.size();i++) {
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(paidTransactions.get(i).getAmount());
                            //oleItemLevelBillPayment.setAmount(transAmount);
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            if(billMap != null) {
                                String note = "Refund issued to bill number :"+billMap.get(paidTransactions.get(i).getPaymentDate().toString());
                                oleItemLevelBillPayment.setNote(note);
                            }
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_REFUNDED_APPLIED);
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        }

                    } else if(patronFeeType.getFeeAmount().subtract(creditRefunded).isGreaterThan(KualiDecimal.ZERO)){
                        creditRefunded = patronFeeType.getFeeAmount().subtract(creditRefunded);
                        KualiDecimal transactionAmount = creditRefunded;
                        sumOfCreditRemaining = sumOfCreditRemaining.add(patronFeeType.getCreditRemaining().negated());
                        patronFeeType.setCreditRemaining(((patronFeeType.getCreditRemaining().negated()).subtract(creditRefunded)).negated());
                        patronBillPayment.setCreditRemaining(((patronFeeType.getCreditRemaining().negated()).subtract(creditRefunded)).negated());
                        creditRefunded = KualiDecimal.ZERO;
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_PAR_REFUNDED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setRefundNote(refundNote);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        List<OleItemLevelBillPayment> paidTransactions = getPaidOutstandingBillTransaction(currentSessionTransactions);
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        for(int i=0;i<paidTransactions.size();i++) {
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(paidTransactions.get(i).getAmount());
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            if(billMap != null) {
                                String note = "Refund issued to bill number :"+billMap.get(paidTransactions.get(i).getPaymentDate().toString());
                                oleItemLevelBillPayment.setNote(note);
                            }
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_REFUNDED_APPLIED);
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        }
                    } else if (creditRefunded.isZero()) {
                        patronBillPayment.setCreditRemaining(KualiDecimal.ZERO);
                        patronFeeType.setCreditRemaining(KualiDecimal.ZERO);
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_FULLY_REFUNDED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        patronFeeType.setRefundNote(refundNote);
                        List<OleItemLevelBillPayment> paidTransactions = getPaidOutstandingBillTransaction(currentSessionTransactions);
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        for(int i=0;i<paidTransactions.size();i++) {
                            OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                            oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            oleItemLevelBillPayment.setAmount(paidTransactions.get(i).getAmount());
                            oleItemLevelBillPayment.setCreatedUser(operatorId);
                            if(billMap != null) {
                                String note = "Refund issued to bill number :"+billMap.get(paidTransactions.get(i).getPaymentDate().toString());
                                oleItemLevelBillPayment.setNote(note);
                            }
                            oleItemLevelBillPayment.setTransactionNote(transactionNote);
                            oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                            oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_REFUNDED_APPLIED);
                            oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        }
                    }
                    saveFlag = true;
                }

            }
            if (saveFlag) {
                patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                patronBillPayment.setPaymentMethod("");
                getBusinessObjectService().save(patronBillPayment);
            }

        }
        return sumOfCreditRemaining;

    }


    public List<OleItemLevelBillPayment> getPaidOutstandingBillTransaction( List<OleItemLevelBillPayment>  oleItemLevelBillPaymentList) {
        List<OleItemLevelBillPayment> paidTransactions = new ArrayList<OleItemLevelBillPayment>();
        for(OleItemLevelBillPayment oleItemLevelBillPayment : oleItemLevelBillPaymentList) {
            if(oleItemLevelBillPayment.getPaymentMode().equalsIgnoreCase(OLEConstants.PAY_APPL_CRDT)) {
                paidTransactions.add(oleItemLevelBillPayment);
            }
        }
        return paidTransactions;
    }


    public void refundToPatron(KualiDecimal refundedAmount,List<FeeType> feeTypes,String transactionNote,String transactionNumber,List<OleItemLevelBillPayment> currentSessionTransactions,String refundNote) {
        KualiDecimal creditRefunded = refundedAmount;
        boolean saveFlag = false;
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        for (FeeType feeType : feeTypes) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("billNumber", feeType.getBillNumber());
            PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);

            List<FeeType> patronFeeTypes = patronBillPayment.getFeeType();
            for (FeeType patronFeeType : patronFeeTypes) {
                if (feeType.isActiveItem() && patronFeeType.getId().equalsIgnoreCase(feeType.getId())) {
                    if (creditRefunded.isGreaterEqual(patronFeeType.getCreditRemaining().negated())) {
                        KualiDecimal transAmount = patronFeeType.getCreditRemaining();
                        creditRefunded = creditRefunded.subtract(patronFeeType.getCreditRemaining().negated());
                        patronFeeType.setCreditRemaining(KualiDecimal.ZERO);
                        patronBillPayment.setCreditRemaining(KualiDecimal.ZERO);
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_FULLY_REFUNDED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        patronFeeType.setRefundNote(refundNote);
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setAmount(transAmount.negated());
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                       if(billMap != null) {
                            String note = "Refund issued to bill number :"+billMap.get(oleItemLevelBillPayment.getPaymentDate().toString());
                            oleItemLevelBillPayment.setNote(note);
                        }
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_REFUNDED_ISSUED);
                        oleItemLevelBillPayment.setNote(refundNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                    } else if(creditRefunded.isGreaterThan(KualiDecimal.ZERO)){
                        KualiDecimal transactionAmount = creditRefunded;
                        patronFeeType.setCreditRemaining(((patronFeeType.getCreditRemaining().negated()).subtract(creditRefunded)).negated());
                        patronBillPayment.setCreditRemaining(((patronFeeType.getCreditRemaining().negated()).subtract(creditRefunded)).negated());
                        creditRefunded = KualiDecimal.ZERO;
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_PAR_REFUNDED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setRefundNote(refundNote);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setAmount(transactionAmount);
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        if(billMap != null) {
                            String note = "Refund issued to bill number :"+billMap.get(oleItemLevelBillPayment.getPaymentDate().toString());
                            oleItemLevelBillPayment.setNote(note);
                        }
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_REFUNDED_ISSUED);
                        oleItemLevelBillPayment.setNote(refundNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                    }
                    saveFlag = true;
                }

            }
            if (saveFlag) {
                patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                patronBillPayment.setPaymentMethod("");
                getBusinessObjectService().save(patronBillPayment);
            }

        }

    }

    public String getPatronMailContent(PatronBillForm patronBillForm) {
        List<FeeType> feeTypes = patronBillForm.getFeeTypes();
        PatronBillContentFormatter patronBillContentFormatter = new PatronBillContentFormatter();
        List<PatronBillItemView> patronBillItemViewList = new ArrayList<>();
        for(FeeType feeType : feeTypes) {
            if(feeType.isActiveItem()) {
                PatronBillItemView patronBillItemView = new PatronBillItemView();
                patronBillItemView.setItemBarcode(feeType.getItemBarcode());
                patronBillItemView.setFeeType(feeType.getFeeType());
                if(patronBillForm.getRefundAmountToPatron() != null) {
                    patronBillItemView.setRefundAmount(patronBillForm.getRefundAmountToPatron().toString());
                } else {
                    patronBillItemView.setRefundAmount(patronBillForm.getAmountDetails().getAmountRemaining().toString());
                }

                patronBillItemView.setBillNumber(feeType.getBillNumber());
                patronBillItemViewList.add(patronBillItemView);
            }
        }
        PatronBillViewBo patronBillViewBo = new PatronBillViewBo();
        patronBillViewBo.setPatronId(patronBillForm.getPatronId());
        patronBillViewBo.setPatronName(patronBillForm.getOlePatronDocument().getPatronName());
        if(patronBillForm.getPatronAddressType().equalsIgnoreCase("default")) {
            patronBillViewBo.setPatronAddress(patronBillForm.getDefaultPatronAddress());
        } else {
            patronBillViewBo.setPatronAddress(patronBillForm.getUserEnteredPatronAddress());
        }

        patronBillViewBo.setPatronBillItemViewList(patronBillItemViewList);
        return patronBillContentFormatter.generateHTML(patronBillViewBo);
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


    public static Comparator<FeeType> sortPatronBill()
    {
        Comparator comp = new Comparator<FeeType>(){
            @Override
            public int compare(FeeType fee1, FeeType fee2)
            {
                return fee1.getBalFeeAmount().compareTo(fee2.getBalFeeAmount());
            }
        };
        return comp;
    }


    public void cancelBill(KualiDecimal cancelAmount,List<PatronBillPayment> patronBillPayments,String transactionNote,String transactionNumber,List<OleItemLevelBillPayment> currentSessionTransactions,String cancelCreditNote) {

        KualiDecimal amountCancelled = cancelAmount;
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        List<String> billId = new ArrayList<String>();
        for (PatronBillPayment patronBillPayment : patronBillPayments) {
            if (patronBillPayment.isSelectBill()) {
                billId.add(patronBillPayment.getBillNumber());
            }
        }
        List<PatronBillPayment> patronBillPaymentsList = (List<PatronBillPayment>) getBusinessObjectService().findAll(PatronBillPayment.class);
        for (PatronBillPayment patronBillPayment : patronBillPaymentsList) {
            if (billId.contains(patronBillPayment.getBillNumber()) ) {
                List<FeeType> feeTypes = patronBillPayment.getFeeType();
                boolean saveFlag = false;
                for (FeeType feeType : feeTypes) {
                    if (amountCancelled.compareTo(feeType.getCreditRemaining().negated()) >= 0) {
                        amountCancelled = amountCancelled.subtract(feeType.getCreditRemaining().negated());
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_CRDT_FULLY_CANCELLED);
                        feeType.setOlePaymentStatus(olePaymentStatus);
                        feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        patronBillPayment.setCreditIssued(feeType.getCreditIssued());
                        patronBillPayment.setCreditRemaining(KualiDecimal.ZERO);
                        feeType.setCreditRemaining(KualiDecimal.ZERO);
                        feeType.setCreditIssued(feeType.getCreditIssued());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                                ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setAmount(feeType.getPaidAmount().negated());
                        oleItemLevelBillPayment.setNote(cancelCreditNote);
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_CRDT_CANCELLED);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setCancelCreditNote(cancelCreditNote);
                    } else if (amountCancelled.isGreaterThan(KualiDecimal.ZERO)) {
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_CRDT_PAR_CANCELLED);
                        feeType.setOlePaymentStatus(olePaymentStatus);
                        feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        feeType.setCreditIssued(feeType.getCreditIssued());
                        patronBillPayment.setCreditIssued(patronBillPayment.getCreditIssued());
                        feeType.setCreditRemaining((feeType.getCreditRemaining().negated().subtract(amountCancelled)).negated());
                        patronBillPayment.setCreditRemaining((patronBillPayment.getCreditRemaining().negated().subtract(amountCancelled)).negated());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = feeType.getItemLevelBillPaymentList() != null && feeType.getItemLevelBillPaymentList().size() > 0
                                ? feeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_CRDT_CANCELLED);
                        oleItemLevelBillPayment.setAmount(amountCancelled.negated());
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        oleItemLevelBillPayment.setNote(cancelCreditNote);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                        feeType.setItemLevelBillPaymentList(oleItemLevelBillPayments);
                        feeType.setCancelCreditNote(cancelCreditNote);
                        amountCancelled = KualiDecimal.ZERO;
                    }
                    saveFlag = true;
                }
                if (saveFlag) {
                    patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                    patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                    patronBillPayment.setPaymentMethod(OLEConstants.PAY_CRDT_CANCELLED);
                    getBusinessObjectService().save(patronBillPayment);
                }
            }
        }


    }

    public void cancelItemBill(KualiDecimal cancelAmount,List<FeeType> feeTypes,String transactionNote,String transactionNumber,List<OleItemLevelBillPayment> currentSessionTransactions,String cancelNote) {
        KualiDecimal amountCancelled = cancelAmount;
        boolean saveFlag = false;
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        for (FeeType feeType : feeTypes) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("billNumber", feeType.getBillNumber());
            PatronBillPayment patronBillPayment = getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, map);

            List<FeeType> patronFeeTypes = patronBillPayment.getFeeType();
            for (FeeType patronFeeType : patronFeeTypes) {
                if (feeType.isActiveItem() && patronFeeType.getId().equalsIgnoreCase(feeType.getId())) {
                    if (amountCancelled.isGreaterEqual(patronFeeType.getCreditRemaining().negated())) {
                        KualiDecimal transAmount = patronFeeType.getCreditRemaining();
                        amountCancelled = amountCancelled.subtract(patronFeeType.getCreditRemaining().negated());
                        patronFeeType.setCreditRemaining(KualiDecimal.ZERO);
                        patronBillPayment.setCreditRemaining(KualiDecimal.ZERO);
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_CRDT_FULLY_CANCELLED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        patronFeeType.setCancelCreditNote(cancelNote);
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setAmount(transAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) < 0 ? transAmount : transAmount);
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_CRDT_CANCELLED);
                        oleItemLevelBillPayment.setNote(cancelNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                    } else if(amountCancelled.isGreaterThan(KualiDecimal.ZERO)){
                        patronFeeType.setCreditRemaining(((patronFeeType.getCreditRemaining().negated()).subtract(amountCancelled)).negated());
                        patronBillPayment.setCreditRemaining(((patronFeeType.getCreditRemaining().negated()).subtract(amountCancelled)).negated());
                        amountCancelled = KualiDecimal.ZERO;
                        OlePaymentStatus olePaymentStatus = getPaymentStatus(OLEConstants.PAY_CRDT_PAR_CANCELLED);
                        patronFeeType.setOlePaymentStatus(olePaymentStatus);
                        patronFeeType.setCancelCreditNote(cancelNote);
                        patronFeeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = patronFeeType.getItemLevelBillPaymentList() != null && patronFeeType.getItemLevelBillPaymentList().size() > 0
                                ? patronFeeType.getItemLevelBillPaymentList() : new ArrayList<OleItemLevelBillPayment>();
                        OleItemLevelBillPayment oleItemLevelBillPayment = new OleItemLevelBillPayment();
                        oleItemLevelBillPayment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                        oleItemLevelBillPayment.setAmount(amountCancelled.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) < 0 ? amountCancelled : amountCancelled);
                        oleItemLevelBillPayment.setCreatedUser(operatorId);
                        oleItemLevelBillPayment.setTransactionNote(transactionNote);
                        oleItemLevelBillPayment.setTransactionNumber(transactionNumber);
                        oleItemLevelBillPayment.setPaymentMode(OLEConstants.PAY_CRDT_CANCELLED);
                        oleItemLevelBillPayment.setNote(cancelNote);
                        oleItemLevelBillPayments.add(oleItemLevelBillPayment);
                        currentSessionTransactions.add(oleItemLevelBillPayment);
                    }
                    saveFlag = true;
                }

            }
            if (saveFlag) {
                patronBillPayment.setPaymentOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                patronBillPayment.setPayDate(new java.sql.Date((new java.util.Date()).getTime()));
                patronBillPayment.setPaymentMethod("");
                getBusinessObjectService().save(patronBillPayment);
            }

        }

}

    public String getBillNumbers(List<FeeType> feeTypes) {
        String billNumbers="";
        for(FeeType feeType : feeTypes) {
            if(feeType.isActiveItem()) {
                billNumbers +=feeType.getBillNumber();
                billNumbers +=",";
            }
        }
        return billNumbers.substring(0,billNumbers.length()-1);
    }


    public boolean validateForTransferCredit(List<FeeType>  feeTypes){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(FeeType feeType:feeTypes){
            if (feeType.isActiveItem()) {
               if(feeType.getTransferNote() != null) {
                   return true;
               }
            }
        }
        return false;
    }

    public Map<String, CheckinUIController> getCheckinUIControllerMap() {
        return checkinUIControllerMap;
    }

    public void setCheckinUIControllerMap(Map<String, CheckinUIController> checkinUIControllerMap) {
        this.checkinUIControllerMap = checkinUIControllerMap;
    }

    public CheckinBaseController getCheckinUIController(CheckinForm checkinForm) {
        if (!getCheckinUIControllerMap().containsKey(checkinForm.getFormKey())) {
            CheckinUIController checkinUIController = new CheckinUIController();
            getCheckinUIControllerMap().put(checkinForm.getFormKey(), checkinUIController);
            return checkinUIController;
        } else {
            return getCheckinUIControllerMap().get(checkinForm.getFormKey());
        }
    }

    public void changeToLostReplace(FeeType feeType){
        CheckinItemController checkinItemController = new CheckinItemController();
        boolean isRequestExists = false;
        CheckinForm checkinForm = new CheckinForm();
        checkinForm.setItemBarcode(feeType.getItemBarcode());
        checkinForm.setCustomDueDateMap(new Date());
        checkinForm.setSelectedCirculationDesk("1");
        checkinItemController.getCheckinUIController(checkinForm).checkin(checkinForm);
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(checkinForm.getSelectedCirculationDesk());
        OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc((ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord"), oleCirculationDesk);
        OleItemSearch oleItemSearch = new DocstoreUtil().getOleItemSearchList(oleItemRecordForCirc.getItemUUID());
        try {
            OleLoanDocument loanDocument = (OleLoanDocument) checkinForm.getDroolsExchange().getContext().get("oleLoanDocument");
            if (loanDocument!=null){
                if (getOleDeliverRequestDocumentHelperService().getRequestByItem(loanDocument.getItemId()).size() > 0) {
                    isRequestExists = true;
                }
                if(isRequestExists){
                    new OleDeliverRequestDocumentHelperServiceImpl().cancelPendingRequestForClaimsReturnedItem(loanDocument.getItemUuid());
                    loanDocument.setDeliverNotices(null);
                }
                loanDocument.setItemStatus(OLEConstants.ITEM_STATUS_LOST_AND_PAID);
                oleItemRecordForCirc.setItemStatusToBeUpdatedTo(OLEConstants.ITEM_STATUS_LOST_AND_PAID);
                checkinItemController.getCheckinUIController(checkinForm).updateLoanDocument(loanDocument, oleItemSearch, (ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord"));
                checkinItemController.getCheckinUIController(checkinForm).updateItemStatusAndCircCount(oleItemRecordForCirc);
            }
        }catch (Exception e){

        }
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = (OleDeliverRequestDocumentHelperServiceImpl) SpringContext.getService("oleDeliverRequestDocumentHelperService");
        }
        return oleDeliverRequestDocumentHelperService;
    }

    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

}