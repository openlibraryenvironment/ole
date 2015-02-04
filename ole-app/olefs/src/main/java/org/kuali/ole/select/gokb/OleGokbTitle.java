package org.kuali.ole.select.gokb;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by premkumarv on 12/8/14.
 */
public class OleGokbTitle extends PersistableBusinessObjectBase {

    private Integer gokbTitleId;
    private String titleName;
    private String variantName;
    private String medium;
    private String pureQa;
    private String issnOnline;
    private String issnPrint;
    private String issnL;
    private Integer oclcNumber=0;
    private String doi;
    private Integer proprietaryId=0;
    private String suncat;
    private String lccn;
    private Integer publisherId=0;
    private Integer imprint=0;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;

    public Integer getGokbTitleId() {
        return gokbTitleId;
    }

    public void setGokbTitleId(Integer gokbTitleId) {
        this.gokbTitleId = gokbTitleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getPureQa() {
        return pureQa;
    }

    public void setPureQa(String pureQa) {
        this.pureQa = pureQa;
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

    public Integer getOclcNumber() {
        return oclcNumber;
    }

    public void setOclcNumber(Integer oclcNumber) {
        this.oclcNumber = oclcNumber;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public Integer getProprietaryId() {
        return proprietaryId;
    }

    public void setProprietaryId(Integer proprietaryId) {
        this.proprietaryId = proprietaryId;
    }

    public String getSuncat() {
        return suncat;
    }

    public void setSuncat(String suncat) {
        this.suncat = suncat;
    }

    public String getLccn() {
        return lccn;
    }

    public void setLccn(String lccn) {
        this.lccn = lccn;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getImprint() {
        return imprint;
    }

    public void setImprint(Integer imprint) {
        this.imprint = imprint;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
