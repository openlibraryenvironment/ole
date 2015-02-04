package org.kuali.ole.vnd.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by angelind on 12/22/14.
 */
public class OLEVendorAssociation extends PersistableBusinessObjectBase {

    private String vendorAssociationId;

    private Integer vendorHeaderGeneratedIdentifier;

    private Integer vendorDetailAssignedIdentifier;

    private String associatedEntityId;

    private String vendorRoleId;

    private OLEVendorRole oleVendorRole;

    public String getVendorAssociationId() {
        return vendorAssociationId;
    }

    public void setVendorAssociationId(String vendorAssociationId) {
        this.vendorAssociationId = vendorAssociationId;
    }

    public Integer getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(Integer vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }

    public Integer getVendorDetailAssignedIdentifier() {
        return vendorDetailAssignedIdentifier;
    }

    public void setVendorDetailAssignedIdentifier(Integer vendorDetailAssignedIdentifier) {
        this.vendorDetailAssignedIdentifier = vendorDetailAssignedIdentifier;
    }

    public String getAssociatedEntityId() {
        return associatedEntityId;
    }

    public void setAssociatedEntityId(String associatedEntityId) {
        this.associatedEntityId = associatedEntityId;
    }

    public String getVendorRoleId() {
        return vendorRoleId;
    }

    public void setVendorRoleId(String vendorRoleId) {
        this.vendorRoleId = vendorRoleId;
    }

    public OLEVendorRole getOleVendorRole() {
        return oleVendorRole;
    }

    public void setOleVendorRole(OLEVendorRole oleVendorRole) {
        this.oleVendorRole = oleVendorRole;
    }
}
