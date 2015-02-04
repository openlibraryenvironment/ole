package org.kuali.ole.select.gokb;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by premkumarv on 12/8/14.
 */
public class OleGokbOrganization extends PersistableBusinessObjectBase {

    private Integer gokbOrganizationId;
    private  String organizationName;
    private  String variantName;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;


    public Integer getGokbOrganizationId() {
        return gokbOrganizationId;
    }

    public void setGokbOrganizationId(Integer gokbOrganizationId) {
        this.gokbOrganizationId = gokbOrganizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
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
