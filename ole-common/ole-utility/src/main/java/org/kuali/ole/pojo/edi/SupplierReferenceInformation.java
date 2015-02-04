package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierReferenceInformation {
    private List<SupplierLineItemReference> supplierLineItemReference = new ArrayList<SupplierLineItemReference>();

    public void addSupplierLineItemReference(SupplierLineItemReference supplierLineItemReference) {
        if (!this.supplierLineItemReference.contains(supplierLineItemReference)) {
            this.supplierLineItemReference.add(supplierLineItemReference);
        }
    }

    public List<SupplierLineItemReference> getSupplierLineItemReference() {
        return supplierLineItemReference;
    }

    public void setSupplierLineItemReference(List<SupplierLineItemReference> supplierLineItemReference) {
        this.supplierLineItemReference = supplierLineItemReference;
    }
}
