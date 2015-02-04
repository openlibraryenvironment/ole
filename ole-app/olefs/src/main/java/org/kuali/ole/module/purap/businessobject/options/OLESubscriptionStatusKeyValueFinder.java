package org.kuali.ole.module.purap.businessobject.options;

import org.kuali.ole.select.bo.OleSubscriptionStatus;
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
 * Created by jgupta11 on 28/10/14.
 */
public class OLESubscriptionStatusKeyValueFinder extends KeyValuesBase {


    @Override
    public List<KeyValue> getKeyValues() {
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection codes = KRADServiceLocator.getBusinessObjectService().findAll(OleSubscriptionStatus.class);
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        for (Iterator iter = codes.iterator(); iter.hasNext(); ) {
            OleSubscriptionStatus OleSubscriptionStatus = (org.kuali.ole.select.bo.OleSubscriptionStatus) iter.next();
            labels.add(new ConcreteKeyValue(OleSubscriptionStatus.getSubscriptionStatusId().toString(), OleSubscriptionStatus.getSubscriptionStatusCode()));
        }

        return labels;

    }
}
