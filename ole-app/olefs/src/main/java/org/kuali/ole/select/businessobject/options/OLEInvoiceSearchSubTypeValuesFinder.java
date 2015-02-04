package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.select.businessobject.OleInvoiceSubType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class OLEInvoiceSearchSubTypeValuesFinder extends KeyValuesBase {
     /*
     * @see org.kuali.keyvalues.KeyValuesFinder#getKeyValues()
     */

    @Override
    public List<KeyValue> getKeyValues() {
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection codes = boService.findAll(OleInvoiceSubType.class);
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        for (Iterator iter = codes.iterator(); iter.hasNext(); ) {
            OleInvoiceSubType pm = (OleInvoiceSubType) iter.next();
            labels.add(new ConcreteKeyValue(pm.getInvoiceSubType(), pm.getInvoiceSubType()));
        }
        return labels;
    }
}
