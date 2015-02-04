package org.kuali.ole.select.businessobject;

import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.module.purap.businessobject.InvoiceItem;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderAccount;
import org.kuali.ole.sys.businessobject.OriginationCode;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 12/13/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoiceOffsetAccountingLine extends InvoiceAccount {
    private String subFundGroupCode = "CLRREV";
    private String vendorName;
    OLEInvoiceOffsetAccountingLineVendor vendor;

    public OLEInvoiceOffsetAccountingLine(){

    }

    public String getSubFundGroupCode() {
        return subFundGroupCode;
    }

    public void setSubFundGroupCode(String subFundGroupCode) {
        this.subFundGroupCode = subFundGroupCode;
    }

    public String getVendorName() {
        return OLEInvoiceOffsetAccountingLineVendor.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
