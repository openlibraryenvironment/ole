package org.kuali.ole.select.gokb;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by premkumarv on 12/8/14.
 */
public class OleGokbPackage extends PersistableBusinessObjectBase {

    private Integer gokbPackageId;
    private String packageName;
    private String variantName;
    private String status;
    private String packageScope;
    private String breakable;
    private String fixed;
    private String availability;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;



    public Integer getGokbPackageId() {
        return gokbPackageId;
    }

    public void setGokbPackageId(Integer gokbPackageId) {
        this.gokbPackageId = gokbPackageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPackageScope() {
        return packageScope;
    }

    public void setPackageScope(String packageScope) {
        this.packageScope = packageScope;
    }

    public String getBreakable() {
        return breakable;
    }

    public void setBreakable(String breakable) {
        this.breakable = breakable;
    }

    public String getFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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
