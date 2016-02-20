package org.kuali.ole.deliver.bo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;

import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

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
                grandTotal = grandTotal.add(feeType.getBalFeeAmount());
                paidamt = paidamt.add(feeType.getPaidAmount());
                /*if(feeType.getFeeAmount()!=null && feeType.getBalFeeAmount()!=null && (feeType.getFeeAmount()).compareTo(feeType.getPaidAmount())>0){
                    unpaidamt = (feeType.getFeeAmount()).subtract(feeType.getPaidAmount());
                }*/
                unpaidamt = unpaidamt.add(feeType.getBalFeeAmount());
            }
            patronBillPayment.setPaidAmount(paidamt);
            patronBillPayment.setUnPaidBalance(unpaidamt);
            //getBusinessObjectService().save(patronBillPayment);
        }
        return grandTotal;
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


    public void setFeeTypeInfo(FeeType feeType, String itemUUID){
        String location = null;
        try {
            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID);
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
            OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(item.getHolding().getContent());
            if(itemContent!=null && itemContent.getLocation()!=null && itemContent.getLocation().getLocationLevel()!=null) {
                location = item.getLocationName();
            }
            if(location==null){
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
    private OlePaymentStatus getPaymentStatus(String paymentStatus) {
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
                    if (((feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_OUTSTANDING) || feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_PARTIALLY))
                            && paymentAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) > 0)) {
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

    public KualiDecimal itemWiseTransaction(List<FeeType> feeTypes, KualiDecimal paymentAmount, String paymentMethod, String fullyPaidStatus, String partiallyPaidStatus, String forgiveErrorNote,String transactionNumber,String transactionNote,String paidByUser,List<OleItemLevelBillPayment> currentSessionTransactions) {
        KualiDecimal payAmt = paymentAmount;
        LOG.debug("Inside itemWiseTransaction");
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        KualiDecimal unPaidAmount;
        for (FeeType feeType : feeTypes) {
            if (feeType.isActiveItem() && ((feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_OUTSTANDING) || feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_PARTIALLY))
                    && paymentAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) > 0)) {
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
                            feeType.setActiveItem(false);
                            patronBillPayment.setPaymentAmount(payAmt.subtract(paymentAmount));
                            payAmt = paymentAmount;
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
                    oleItemLevelBillPayment.setAmount(feeType.getBalFeeAmount());
                    oleItemLevelBillPayment.setCreatedUser(operatorId);
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
                        oleItemLevelBillPayment.setAmount(patronFeeType.getBalFeeAmount());
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

    public KualiDecimal getSumOfSelectedFeeTypes(List<FeeType>  feeTypes){
        KualiDecimal sum=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(FeeType feeType:feeTypes){
            if (feeType.isActiveItem()) {
                sum = sum.add(feeType.getBalFeeAmount());
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
}