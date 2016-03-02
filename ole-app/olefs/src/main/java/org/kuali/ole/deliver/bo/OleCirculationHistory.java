package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/26/12
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCirculationHistory extends PersistableBusinessObjectBase {
    private String oleCirculationHistoryId;
    private String loanId;
    private String circulationPolicyId;
    private String patronId;
    private String patronTypeId;
    private String affiliationId;
    private String deptId;
    private String otherAffiliation;
    private String statisticalCategory;
    private String itemId;
    private String bibTitle;
    private String bibAuthor;
    private String bibEdition;
    private String bibPublication;
    private Date bibPublicationDate;
    private String bibIsbn;
    private String proxyPatronId;
    private Timestamp dueDate;
    private Date pastDueDate;
    private Date createDate;
    private Timestamp modifyDate;
    private String circulationLocationId;
    private String operatorCreateId;
    private String operatorModifyId;
    private String machineId;
    private String overrideOperatorId;
    private String numberOfRenewals;
    private String numberOfOverdueNoticesSent;
    private Date overdueNoticeDate;
    private String oleRequestId;
    private String repaymentFeePatronBillId;
    private Date checkInDate;
    private String checkInOperatorId;
    private String checkInMachineId;
    private String itemUuid;
    private String itemLocation;
    private String holdingsLocation;
    private String itemTypeId;
    private String temporaryItemTypeId;
    private Timestamp overrideCheckInDateTime;

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public String getOleCirculationHistoryId() {
        return oleCirculationHistoryId;
    }

    public void setOleCirculationHistoryId(String oleCirculationHistoryId) {
        this.oleCirculationHistoryId = oleCirculationHistoryId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getCirculationPolicyId() {
        return circulationPolicyId;
    }

    public void setCirculationPolicyId(String circulationPolicyId) {
        this.circulationPolicyId = circulationPolicyId;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getPatronTypeId() {
        return patronTypeId;
    }

    public void setPatronTypeId(String patronTypeId) {
        this.patronTypeId = patronTypeId;
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getOtherAffiliation() {
        return otherAffiliation;
    }

    public void setOtherAffiliation(String otherAffiliation) {
        this.otherAffiliation = otherAffiliation;
    }

    public String getStatisticalCategory() {
        return statisticalCategory;
    }

    public void setStatisticalCategory(String statisticalCategory) {
        this.statisticalCategory = statisticalCategory;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBibTitle() {
        return bibTitle;
    }

    public void setBibTitle(String bibTitle) {
        this.bibTitle = bibTitle;
    }

    public String getBibAuthor() {
        return bibAuthor;
    }

    public void setBibAuthor(String bibAuthor) {
        this.bibAuthor = bibAuthor;
    }

    public String getBibEdition() {
        return bibEdition;
    }

    public void setBibEdition(String bibEdition) {
        this.bibEdition = bibEdition;
    }

    public String getBibPublication() {
        return bibPublication;
    }

    public void setBibPublication(String bibPublication) {
        this.bibPublication = bibPublication;
    }

    public Date getBibPublicationDate() {
        return bibPublicationDate;
    }

    public void setBibPublicationDate(Date bibPublicationDate) {
        this.bibPublicationDate = bibPublicationDate;
    }

    public String getBibIsbn() {
        return bibIsbn;
    }

    public void setBibIsbn(String bibIsbn) {
        this.bibIsbn = bibIsbn;
    }

    public String getProxyPatronId() {
        return proxyPatronId;
    }

    public void setProxyPatronId(String proxyPatronId) {
        this.proxyPatronId = proxyPatronId;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPastDueDate() {
        return pastDueDate;
    }

    public void setPastDueDate(Date pastDueDate) {
        this.pastDueDate = pastDueDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCirculationLocationId() {
        return circulationLocationId;
    }

    public void setCirculationLocationId(String circulationLocationId) {
        this.circulationLocationId = circulationLocationId;
    }

    public String getOperatorCreateId() {
        return operatorCreateId;
    }

    public void setOperatorCreateId(String operatorCreateId) {
        this.operatorCreateId = operatorCreateId;
    }

    public String getOperatorModifyId() {
        return operatorModifyId;
    }

    public void setOperatorModifyId(String operatorModifyId) {
        this.operatorModifyId = operatorModifyId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getOverrideOperatorId() {
        return overrideOperatorId;
    }

    public void setOverrideOperatorId(String overrideOperatorId) {
        this.overrideOperatorId = overrideOperatorId;
    }

    public String getNumberOfRenewals() {
        return numberOfRenewals;
    }

    public void setNumberOfRenewals(String numberOfRenewals) {
        this.numberOfRenewals = numberOfRenewals;
    }

    public String getNumberOfOverdueNoticesSent() {
        return numberOfOverdueNoticesSent;
    }

    public void setNumberOfOverdueNoticesSent(String numberOfOverdueNoticesSent) {
        this.numberOfOverdueNoticesSent = numberOfOverdueNoticesSent;
    }

    public Date getOverdueNoticeDate() {
        return overdueNoticeDate;
    }

    public void setOverdueNoticeDate(Date overdueNoticeDate) {
        this.overdueNoticeDate = overdueNoticeDate;
    }

    public String getOleRequestId() {
        return oleRequestId;
    }

    public void setOleRequestId(String oleRequestId) {
        this.oleRequestId = oleRequestId;
    }

    public String getRepaymentFeePatronBillId() {
        return repaymentFeePatronBillId;
    }

    public void setRepaymentFeePatronBillId(String repaymentFeePatronBillId) {
        this.repaymentFeePatronBillId = repaymentFeePatronBillId;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckInOperatorId() {
        return checkInOperatorId;
    }

    public void setCheckInOperatorId(String checkInOperatorId) {
        this.checkInOperatorId = checkInOperatorId;
    }

    public String getCheckInMachineId() {
        return checkInMachineId;
    }

    public void setCheckInMachineId(String checkInMachineId) {
        this.checkInMachineId = checkInMachineId;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getHoldingsLocation() {
        return holdingsLocation;
    }

    public void setHoldingsLocation(String holdingsLocation) {
        this.holdingsLocation = holdingsLocation;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getTemporaryItemTypeId() {
        return temporaryItemTypeId;
    }

    public void setTemporaryItemTypeId(String temporaryItemTypeId) {
        this.temporaryItemTypeId = temporaryItemTypeId;
    }

    public Timestamp getOverrideCheckInDateTime() {
        return overrideCheckInDateTime;
    }

    public void setOverrideCheckInDateTime(Timestamp overrideCheckInDateTime) {
        this.overrideCheckInDateTime = overrideCheckInDateTime;
    }
}