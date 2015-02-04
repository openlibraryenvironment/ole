package org.kuali.ole.select.businessobject;

import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.module.purap.businessobject.InvoiceItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderAccount;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 12/13/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoiceOffsetAccountingLineVendor extends OLEInvoiceOffsetAccountingLine {

    public static String vendorName;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
