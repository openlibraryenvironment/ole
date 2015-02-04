package org.kuali.ole.select.gokb;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by sambasivam on 18/12/14.
 */
public class OleGokbView  extends PersistableBusinessObjectBase {

    private Integer packageId;
    private String packageName;
    private String packageStatus;

    private Integer platformId;
    private String platformName;
    private String platformStatus;
    private String softwarePlatform;

    private Integer tippId;
    private String tippStatus;

    private Integer titleId;
    private String title;
    private Integer publisherId;
    private String issnOnline;
    private String issnPrint;
    private String issnL;
    private String medium;

    private Integer orgId;
    private String orgName;


    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getSoftwarePlatform() {
        return softwarePlatform;
    }

    public void setSoftwarePlatform(String softwarePlatform) {
        this.softwarePlatform = softwarePlatform;
    }

    public Integer getTippId() {
        return tippId;
    }

    public void setTippId(Integer tippId) {
        this.tippId = tippId;
    }

    public String getTippStatus() {
        return tippStatus;
    }

    public void setTippStatus(String tippStatus) {
        this.tippStatus = tippStatus;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public String getIssnOnline() {
        return issnOnline;
    }

    public void setIssnOnline(String issnOnline) {
        this.issnOnline = issnOnline;
    }

    public String getIssnPrint() {
        return issnPrint;
    }

    public void setIssnPrint(String issnPrint) {
        this.issnPrint = issnPrint;
    }

    public String getIssnL() {
        return issnL;
    }

    public void setIssnL(String issnL) {
        this.issnL = issnL;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}
