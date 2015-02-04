package org.kuali.ole.docstore.model.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 7/19/13
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkEHoldingsDocument extends OleDocument {

    private String accessStatus;
    private String platForm;
    private String imprint;
    private String statisticalCode;
    private String location;
    private String bibIdentifier;
    private String instanceIdentifier;
    private String holdingsIdentifier;
    private String localId;
    private String url;
    private String eResourceName;
    private String staffOnly;
    private String subscriptionStatus;
    private List<String> coverageDates = new ArrayList<>();
    private Timestamp currentSubscriptionStartDate;
    private Timestamp currentSubscriptionEndDate;
    private Timestamp initialSubscriptionStartDate;
    private Timestamp cancellationDecisionDate;
    private Timestamp cancellationEffectiveDate;
    private String cancellationReason;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getImprint() {
        return imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public String getStatisticalCode() {
        return statisticalCode;
    }

    public void setStatisticalCode(String statisticalCode) {
        this.statisticalCode = statisticalCode;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    public void setHoldingsIdentifier(String holdingsIdentifier) {
        this.holdingsIdentifier = holdingsIdentifier;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String geteResourceName() {
        return eResourceName;
    }

    public void seteResourceName(String eResourceName) {
        this.eResourceName = eResourceName;
    }

    public String getStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(String staffOnly) {
        this.staffOnly = staffOnly;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public List<String> getCoverageDates() {
        return coverageDates;
    }

    public void setCoverageDates(List<String> coverageDates) {
        this.coverageDates = coverageDates;
    }

    public Timestamp getCurrentSubscriptionStartDate() {
        return currentSubscriptionStartDate;
    }

    public void setCurrentSubscriptionStartDate(Timestamp currentSubscriptionStartDate) {
        this.currentSubscriptionStartDate = currentSubscriptionStartDate;
    }

    public Timestamp getCurrentSubscriptionEndDate() {
        return currentSubscriptionEndDate;
    }

    public void setCurrentSubscriptionEndDate(Timestamp currentSubscriptionEndDate) {
        this.currentSubscriptionEndDate = currentSubscriptionEndDate;
    }

    public Timestamp getInitialSubscriptionStartDate() {
        return initialSubscriptionStartDate;
    }

    public void setInitialSubscriptionStartDate(Timestamp initialSubscriptionStartDate) {
        this.initialSubscriptionStartDate = initialSubscriptionStartDate;
    }

    public Timestamp getCancellationDecisionDate() {
        return cancellationDecisionDate;
    }

    public void setCancellationDecisionDate(Timestamp cancellationDecisionDate) {
        this.cancellationDecisionDate = cancellationDecisionDate;
    }

    public Timestamp getCancellationEffectiveDate() {
        return cancellationEffectiveDate;
    }

    public void setCancellationEffectiveDate(Timestamp cancellationEffectiveDate) {
        this.cancellationEffectiveDate = cancellationEffectiveDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
