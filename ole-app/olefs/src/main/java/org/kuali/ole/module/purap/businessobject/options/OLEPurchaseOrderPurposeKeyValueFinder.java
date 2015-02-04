package org.kuali.ole.module.purap.businessobject.options;

import org.kuali.ole.select.bo.OlePurchaseOrderPurpose;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sambasivam on 29/9/14.
 */
public class OLEPurchaseOrderPurposeKeyValueFinder extends KeyValuesBase {


    @Override
    public List<KeyValue> getKeyValues() {
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection codes = KRADServiceLocator.getBusinessObjectService().findAll(OlePurchaseOrderPurpose.class);
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        for (Iterator iter = codes.iterator(); iter.hasNext(); ) {
            OlePurchaseOrderPurpose olePurchaseOrderPurpose = (OlePurchaseOrderPurpose) iter.next();
            labels.add(new ConcreteKeyValue(olePurchaseOrderPurpose.getPurchaseOrderPurposeId().toString(), olePurchaseOrderPurpose.getPurchaseOrderPurposeCode()));
        }


        return labels;

    }
}
