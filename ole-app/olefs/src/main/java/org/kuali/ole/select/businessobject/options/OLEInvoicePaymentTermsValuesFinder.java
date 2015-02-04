package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.PaymentTermType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: anithaa
 * Date: 7/25/13
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoicePaymentTermsValuesFinder extends KeyValuesBase {

    /*
     * @see org.kuali.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @Override
    public List<KeyValue> getKeyValues() {
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection codes = boService.findAll(PaymentTermType.class);
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        for (Iterator iter = codes.iterator(); iter.hasNext(); ) {
            PaymentTermType pm = (PaymentTermType) iter.next();
            labels.add(new ConcreteKeyValue(pm.getVendorPaymentTermsCode().toString(), pm.getVendorPaymentTermsDescription()));
        }

        return labels;
    }

}



