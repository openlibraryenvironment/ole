package org.kuali.ole.deliver.bo;


import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * PatronBillMaintenance provides Patron bill  information through getter and setter.
 */

public class PatronBillPayment extends PersistableBusinessObjectBase {


    private KualiDecimal totalAmount;
    private String billNumber;
    private Integer viewBillNumber;
    private Date billDate;
    private FeeType patronFeeType;
    private String patronId;
    private String proxyPatronId;
    private String freeTextNote;
    private List<FeeType> feeType = new ArrayList<FeeType>();
    private KualiDecimal unPaidBalance = new KualiDecimal(0);
    private String paymentMethod;
    private KualiDecimal paymentAmount;
    private String paymentOperatorId;
    private String paymentMachineId;
    private Date payDate;
    //Need to retrieve from services
    private String machineId;
    private String note;
    private String operatorId = "test";
    private String proxyPatronName;
    private List<org.kuali.ole.deliver.bo.SystemGeneratedBill> sysGeneratedBill = new ArrayList<org.kuali.ole.deliver.bo.SystemGeneratedBill>();
    private boolean reviewed;
    private String firstName;
    private String middleName;
    private String lastName;
    private String patronName;
    private boolean selectBill;
    private String paymentStatusCode;
    private String paymentStatusName;
    private String errorMessage;
    private boolean zeroFeeAmount;
    private boolean requiredFeeAmount;
    private KualiDecimal paidAmount = new KualiDecimal(0);
    private Timestamp lastTransactionDate;
    private KualiDecimal creditIssued = new KualiDecimal(0);
    private KualiDecimal creditRemaining = new KualiDecimal(0);
    private boolean manualProcessBill = false;
    private String patronBarcode;
    private String patronTypeId;
    private String feeTypeId;
    private String itemBarcode;
    private String paymentStatusId;
    private KualiDecimal fineAmountFrom;
    private KualiDecimal fineAmountTo;

    private OlePatronDocument olePatron = new OlePatronDocument();

    public KualiDecimal getPaidAmount() {
       /* if(totalAmount!=null && unPaidBalance!=null && totalAmount.compareTo(unPaidBalance)>0){
            paidAmount = totalAmount.subtract(unPaidBalance);
        }*/
        return paidAmount;
    }

    public String getPatronName() {
        if(olePatron!=null){
            this.patronName = olePatron.getEntity().getNames().get(0).getFirstName() + " " + olePatron.getEntity().getNames().get(0).getLastName();
        }
        return patronName;
    }



    public void setPaidAmount(KualiDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentStatusName() {
        return paymentStatusName;
    }

    public void setPaymentStatusName(String paymentStatusName) {
        this.paymentStatusName = paymentStatusName;
    }

    public String getPaymentStatusCode() {

        for (FeeType feeTypeObj : feeType) {
            if (feeTypeObj.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_OUTSTANDING_CODE)) {
                paymentStatusName = feeTypeObj.getOlePaymentStatus().getPaymentStatusName();
                return feeTypeObj.getPaymentStatusCode();
            } else if (feeTypeObj.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_PART_CODE)) {
                paymentStatusName = feeTypeObj.getOlePaymentStatus().getPaymentStatusName();
                return feeTypeObj.getPaymentStatusCode();
            } else if (feeTypeObj.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_ERROR_CODE)) {
                paymentStatusName = feeTypeObj.getOlePaymentStatus().getPaymentStatusName();
                return feeTypeObj.getPaymentStatusCode();
            } else if (feeTypeObj.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_FORGIVEN_CODE)) {
                paymentStatusName = feeTypeObj.getOlePaymentStatus().getPaymentStatusName();
                return feeTypeObj.getPaymentStatusCode();
            } else if (feeTypeObj.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_FULL_CODE)) {
                paymentStatusName = feeTypeObj.getOlePaymentStatus().getPaymentStatusName();
                return feeTypeObj.getPaymentStatusCode();
            } else if (feeTypeObj.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_CANCEL_CODE)) {
                paymentStatusName = feeTypeObj.getOlePaymentStatus().getPaymentStatusName();
                return feeTypeObj.getPaymentStatusCode();
            } else {
                paymentStatusName = feeTypeObj.getOlePaymentStatus().getPaymentStatusName();
                return feeTypeObj.getPaymentStatusCode();
            }
        }
        return paymentStatusCode != null ? paymentStatusCode : "";
    }

    public void setPaymentStatusCode(String paymentStatusCode) {
        this.paymentStatusCode = paymentStatusCode;
    }

    public boolean isSelectBill() {
        return selectBill;
    }

    public void setSelectBill(boolean selectBill) {
        this.selectBill = selectBill;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the value of note property
     *
     * @return barcode
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value for note property
     *
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Gets the value of Machine property
     *
     * @return barcode
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * Sets the value for machineId property
     *
     * @param machineId
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * Gets the value of operator property
     *
     * @return operatorId
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * Sets the value for machineId property
     *
     * @param operatorId
     */
    public void setOperatorId(String operatorId) {
        if (operatorId == null || (operatorId != null && operatorId.isEmpty()))
            operatorId = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
        this.operatorId = operatorId;
    }

    /**
     * Gets the value of date property
     *
     * @return payDate
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * Sets the value for machineId property
     *
     * @param payDate
     */
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    /**
     * Gets the value of machine id property
     *
     * @return paymentMachineId
     */
    public String getPaymentMachineId() {
        return paymentMachineId;
    }

    /**
     * Sets the value for machineId property
     *
     * @param paymentMachineId
     */
    public void setPaymentMachineId(String paymentMachineId) {
        this.paymentMachineId = paymentMachineId;
    }

    /**
     * Gets the value of payment operator id property
     *
     * @return paymentOperatorId
     */
    public String getPaymentOperatorId() {
        return paymentOperatorId;
    }

    /**
     * Sets the value for machineId property
     *
     * @param paymentOperatorId
     */
    public void setPaymentOperatorId(String paymentOperatorId) {
        this.paymentOperatorId = paymentOperatorId;
    }

    /**
     * Gets the value of payment method property
     *
     * @return paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the value for machineId property
     *
     * @param paymentMethod
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets the value of patron id property
     *
     * @return patronId
     */
    public String getPatronId() {
        return patronId;
    }

    /**
     * Sets the value for machineId property
     *
     * @param patronId
     */
    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    /**
     * Gets the value of proxy patron property
     *
     * @return proxyPatronId
     */
    public String getProxyPatronId() {
        return proxyPatronId;
    }

    /**
     * Sets the value for machineId property
     *
     * @param proxyPatronId
     */
    public void setProxyPatronId(String proxyPatronId) {
        this.proxyPatronId = proxyPatronId;
    }

    /**
     * Gets the entity of fee type property
     *
     * @return patronFeeType
     */
    public FeeType getPatronFeeType() {
        return patronFeeType;
    }

    /**
     * Sets the value for machineId property
     *
     * @param patronFeeType
     */
    public void setPatronFeeType(FeeType patronFeeType) {
        this.patronFeeType = patronFeeType;
    }

    /**
     * Gets the list of feetype property
     * @return feeType
     *//*
    public List<FeeType> getFeeType() {
        return feeType;
    }
    *//**
     * Sets the value for machineId property
     * @param feeType
     *//*
    public void setFeeType(List<FeeType> feeType) {
        this.feeType = feeType;
    }*/

    /**
     * Gets the value of bill date property
     *
     * @return billDate
     */
    public Date getBillDate() {
        return billDate;
    }

    /**
     * Sets the value for machineId property
     *
     * @param billDate
     */
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    /**
     * Gets the value of bill number property
     *
     * @return billNumber
     */
    public String getBillNumber() {
        return billNumber;
    }

    /**
     * Sets the value for machineId property
     *
     * @param billNumber
     */
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    /**
     * Gets the value of free text note property
     *
     * @return freeTextNote
     */
    public String getFreeTextNote() {
        return freeTextNote;
    }

    /**
     * Sets the value for machineId property
     *
     * @param freeTextNote
     */
    public void setFreeTextNote(String freeTextNote) {
        this.freeTextNote = freeTextNote;
    }

    /**
     * Gets the value of total amount property
     *
     * @return totalAmount
     */
    public KualiDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the value for machineId property
     *
     * @param totalAmount
     */
    public void setTotalAmount(KualiDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Gets the value of payment amount property
     *
     * @return paymentAmount
     */
    public KualiDecimal getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the value for machineId property
     *
     * @param paymentAmount
     */
    public void setPaymentAmount(KualiDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the value of unpaid balance property
     *
     * @return unPaidBalance
     */
    public KualiDecimal getUnPaidBalance() {
        return unPaidBalance;
    }

    /**
     * Sets the value for machineId property
     *
     * @param unPaidBalance
     */
    public void setUnPaidBalance(KualiDecimal unPaidBalance) {
        this.unPaidBalance = unPaidBalance;
    }

    public List<org.kuali.ole.deliver.bo.SystemGeneratedBill> getSysGeneratedBill() {
        return sysGeneratedBill;
    }

    public void setSysGeneratedBill(List<SystemGeneratedBill> sysGeneratedBill) {
        this.sysGeneratedBill = sysGeneratedBill;
    }

    public String getProxyPatronName() {
        return proxyPatronName;
    }

    public void setProxyPatronName(String proxyPatronName) {
        this.proxyPatronName = proxyPatronName;
    }


    public List<FeeType> getFeeType() {
        return feeType;
    }

    public void setFeeType(List<FeeType> feeType) {
        this.feeType = feeType;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OlePatronDocument getOlePatron() {
        if (null == olePatron || StringUtils.isEmpty(olePatron.getOlePatronId())) {
            String patronId = getPatronId();
            if (StringUtils.isNotEmpty(patronId)) {
                Map<String, String> parameterMap = new HashMap<>();
                parameterMap.put("olePatronId", patronId);
                olePatron = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, parameterMap);
            }
        }
        return olePatron;
    }

    public void setOlePatron(OlePatronDocument olePatron) {
        this.olePatron = olePatron;
    }

    /**
     * Gets the value of billNumber property
     *
     * @return billNumber
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("billNumber", billNumber);
        return toStringMap;
    }

    public boolean isZeroFeeAmount() {
        return zeroFeeAmount;
    }

    public void setZeroFeeAmount(boolean zeroFeeAmount) {
        this.zeroFeeAmount = zeroFeeAmount;
    }

    public boolean isRequiredFeeAmount() {
        return requiredFeeAmount;
    }

    public void setRequiredFeeAmount(boolean requiredFeeAmount) {
        this.requiredFeeAmount = requiredFeeAmount;
    }

    public Timestamp getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(Timestamp lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Integer getViewBillNumber() {
        return Integer.parseInt(this.getBillNumber());
    }

    public void setViewBillNumber(Integer viewBillNumber) {
        this.viewBillNumber = viewBillNumber;
    }

    public KualiDecimal getCreditIssued() {
        return creditIssued;
}

    public void setCreditIssued(KualiDecimal creditIssued) {
        this.creditIssued = creditIssued;
    }

    public KualiDecimal getCreditRemaining() {
        return creditRemaining;
    }

    public void setCreditRemaining(KualiDecimal creditRemaining) {
        this.creditRemaining = creditRemaining;
    }

    public boolean isManualProcessBill() {
        return manualProcessBill;
    }

    public void setManualProcessBill(boolean manualProcessBill) {
        this.manualProcessBill = manualProcessBill;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getPatronTypeId() {
        return patronTypeId;
    }

    public void setPatronTypeId(String patronTypeId) {
        this.patronTypeId = patronTypeId;
    }

    public String getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(String feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(String paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public KualiDecimal getFineAmountFrom() {
        return fineAmountFrom;
    }

    public void setFineAmountFrom(KualiDecimal fineAmountFrom) {
        this.fineAmountFrom = fineAmountFrom;
    }

    public KualiDecimal getFineAmountTo() {
        return fineAmountTo;
    }

    public void setFineAmountTo(KualiDecimal fineAmountTo) {
        this.fineAmountTo = fineAmountTo;
    }
}