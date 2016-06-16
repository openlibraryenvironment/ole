package org.kuali.ole.select.bo;


import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by govindarajank on 12/4/16.
 */
public class OleAgreementRecord extends PersistableBusinessObjectBase{

    private String agreementId;

    private String licenseTitle;

    private String contractNumber;

    private String agreementStatusId;

    private String codingStatus;

    private String licensor;

    private String licensee;

    private Date startDate;

    private Date endDate;

    private Integer purchaseOrderId;

    private String licenseeSite;

    private String generalNotes;

    private String feeSchedule;

    private String inflationCap;

    private String paymentTerms;

    private String newTitleAccess;

    private String cancellationRights;

    private String additionalTerms;

    private String noticePeriodTermination;

    private boolean perpectualAccess;

    private String perpectualAccessNotes;

    private List<String> authorizedUsers;

    private String governingLaw;

    private String authorizedUsersGenNotes;

    private boolean depositInIR;

    private boolean fairUse;

    private boolean rightsGranted;

    private boolean illPrint;

    private boolean illElectronic;

    private boolean illLoansame;

    private boolean illNonProfit;

    private boolean illSameCuntRest;

    private String illNotes;

    private boolean libResCMSys;

    private boolean libRes;

    private String cmNotes;

    private boolean scholShar;

    private boolean textMining;

    private boolean perfomanceRights;

    private boolean streamingRights;

    private String multimediaRightsNote;

    private String notesOnAPCAgreement;

    private boolean apcOffsetAgreement;

    private String startDateStr;

    private String endDateStr;

    private String authUsers;

    private OleAgreementStatus oleAgreementStatus;

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getLicenseTitle() {
        return licenseTitle;
    }

    public void setLicenseTitle(String licenseTitle) {
        this.licenseTitle = licenseTitle;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getAgreementStatusId() {
        return agreementStatusId;
    }

    public void setAgreementStatusId(String agreementStatusId) {
        this.agreementStatusId = agreementStatusId;
    }

    public String getCodingStatus() {
        return codingStatus;
    }

    public void setCodingStatus(String codingStatus) {
        this.codingStatus = codingStatus;
    }

    public String getLicensor() {
        return licensor;
    }

    public void setLicensor(String licensor) {
        this.licensor = licensor;
    }

    public String getLicensee() {
        return licensee;
    }

    public void setLicensee(String licensee) {
        this.licensee = licensee;
    }

    public Date getStartDate() {
        try{
            String date=new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            startDate= RiceConstants.getDefaultDateFormat().parse(date);
        }catch (Exception e){
            e.getMessage();
        }
        return startDate;
    }

    public void setAuthUsers(String authUsers){
      this.authUsers=authUsers;
    }

    public String getAuthUsers(){
        return authUsers;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getLicenseeSite() {
        return licenseeSite;
    }

    public void setLicenseeSite(String licenseeSite) {
        this.licenseeSite = licenseeSite;
    }

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public String getFeeSchedule() {
        return feeSchedule;
    }

    public void setFeeSchedule(String feeSchedule) {
        this.feeSchedule = feeSchedule;
    }

    public String getInflationCap() {
        return inflationCap;
    }

    public void setInflationCap(String inflationCap) {
        this.inflationCap = inflationCap;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getNewTitleAccess() {
        return newTitleAccess;
    }

    public void setNewTitleAccess(String newTitleAccess) {
        this.newTitleAccess = newTitleAccess;
    }

    public String getCancellationRights() {
        return cancellationRights;
    }

    public void setCancellationRights(String cancellationRights) {
        this.cancellationRights = cancellationRights;
    }

    public String getAdditionalTerms() {
        return additionalTerms;
    }

    public void setAdditionalTerms(String additionalTerms) {
        this.additionalTerms = additionalTerms;
    }

    public String getNoticePeriodTermination() {
        return noticePeriodTermination;
    }

    public void setNoticePeriodTermination(String noticePeriodTermination) {
        this.noticePeriodTermination = noticePeriodTermination;
    }

    public boolean isPerpectualAccess() {
        return perpectualAccess;
    }

    public void setPerpectualAccess(boolean perpectualAccess) {
        this.perpectualAccess = perpectualAccess;
    }

    public String getPerpectualAccessNotes() {
        return perpectualAccessNotes;
    }

    public void setPerpectualAccessNotes(String perpectualAccessNotes) {
        this.perpectualAccessNotes = perpectualAccessNotes;
    }

    public List<String> getAuthorizedUsers() {
        if(authUsers!=null)
            authorizedUsers = Arrays.asList(authUsers.split(","));
        return authorizedUsers;
    }

    public void setAuthorizedUsers(List<String> authorizedUsers) {
        this.authUsers="";
        if(authorizedUsers.size()>0){
            for(String users:authorizedUsers){
                setAuthUsers(this.authUsers += users+",");
            }
        }
        this.authorizedUsers=authorizedUsers;
    }

    public String getGoverningLaw() {
        return governingLaw;
    }

    public void setGoverningLaw(String governingLaw) {
        this.governingLaw = governingLaw;
    }

    public String getAuthorizedUsersGenNotes() {
        return authorizedUsersGenNotes;
    }

    public void setAuthorizedUsersGenNotes(String authorizedUsersGenNotes) {
        this.authorizedUsersGenNotes = authorizedUsersGenNotes;
    }

    public boolean isDepositInIR() {
        return depositInIR;
    }

    public void setDepositInIR(boolean depositInIR) {
        this.depositInIR = depositInIR;
    }

    public boolean isFairUse() {
        return fairUse;
    }

    public void setFairUse(boolean fairUse) {
        this.fairUse = fairUse;
    }

    public boolean isRightsGranted() {
        return rightsGranted;
    }

    public void setRightsGranted(boolean rightsGranted) {
        this.rightsGranted = rightsGranted;
    }

    public boolean isIllPrint() {
        return illPrint;
    }

    public void setIllPrint(boolean illPrint) {
        this.illPrint = illPrint;
    }

    public boolean isIllElectronic() {
        return illElectronic;
    }

    public void setIllElectronic(boolean illElectronic) {
        this.illElectronic = illElectronic;
    }

    public boolean isIllLoansame() {
        return illLoansame;
    }

    public void setIllLoansame(boolean illLoansame) {
        this.illLoansame = illLoansame;
    }

    public boolean isIllNonProfit() {
        return illNonProfit;
    }

    public void setIllNonProfit(boolean illNonProfit) {
        this.illNonProfit = illNonProfit;
    }

    public boolean isIllSameCuntRest() {
        return illSameCuntRest;
    }

    public void setIllSameCuntRest(boolean illSameCuntRest) {
        this.illSameCuntRest = illSameCuntRest;
    }

    public String getIllNotes() {
        return illNotes;
    }

    public void setIllNotes(String illNotes) {
        this.illNotes = illNotes;
    }

    public boolean isLibResCMSys() {
        return libResCMSys;
    }

    public void setLibResCMSys(boolean libResCMSys) {
        this.libResCMSys = libResCMSys;
    }

    public boolean isLibRes() {
        return libRes;
    }

    public void setLibRes(boolean libRes) {
        this.libRes = libRes;
    }

    public String getCmNotes() {
        return cmNotes;
    }

    public void setCmNotes(String cmNotes) {
        this.cmNotes = cmNotes;
    }

    public boolean isScholShar() {
        return scholShar;
    }

    public void setScholShar(boolean scholShar) {
        this.scholShar = scholShar;
    }

    public boolean isTextMining() {
        return textMining;
    }

    public void setTextMining(boolean textMining) {
        this.textMining = textMining;
    }

    public boolean isPerfomanceRights() {
        return perfomanceRights;
    }

    public void setPerfomanceRights(boolean perfomanceRights) {
        this.perfomanceRights = perfomanceRights;
    }

    public boolean isStreamingRights() {
        return streamingRights;
    }

    public void setStreamingRights(boolean streamingRights) {
        this.streamingRights = streamingRights;
    }

    public String getMultimediaRightsNote() {
        return multimediaRightsNote;
    }

    public void setMultimediaRightsNote(String multimediaRightsNote) {
        this.multimediaRightsNote = multimediaRightsNote;
    }

    public String getNotesOnAPCAgreement() {
        return notesOnAPCAgreement;
    }

    public void setNotesOnAPCAgreement(String notesOnAPCAgreement) {
        this.notesOnAPCAgreement = notesOnAPCAgreement;
    }

    public boolean isApcOffsetAgreement() {
        return apcOffsetAgreement;
    }

    public void setApcOffsetAgreement(boolean apcOffsetAgreement) {
        this.apcOffsetAgreement = apcOffsetAgreement;
    }

    public OleAgreementStatus getOleAgreementStatus() {
        return oleAgreementStatus;
    }

    public void setOleAgreementStatus(OleAgreementStatus oleAgreementStatus) {
        this.oleAgreementStatus = oleAgreementStatus;
    }

    public String getStartDateStr() {
        if(startDate!=null) {
            try {
                startDateStr = RiceConstants.getDefaultDateFormat().format(startDate);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return startDateStr;

    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }


    public String getEndDateStr() {

        if(endDate!=null) {
            try {
                endDateStr = RiceConstants.getDefaultDateFormat().format(endDate);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

}