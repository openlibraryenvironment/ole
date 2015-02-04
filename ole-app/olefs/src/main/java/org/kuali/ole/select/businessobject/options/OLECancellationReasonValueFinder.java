package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.select.bo.OLECancellationReason;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 12/10/13
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECancellationReasonValueFinder extends KeyValuesBase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLECancellationReasonValueFinder.class);

    @Override
    public List getKeyValues() {
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection codes = boService.findAll(OLECancellationReason.class);
        Iterator iterator = codes.iterator();
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        while (iterator.hasNext()) {
            OLECancellationReason oleCancellationReason = (OLECancellationReason) iterator.next();
            labels.add(new ConcreteKeyValue(oleCancellationReason.getCancelReasonName(), oleCancellationReason
                    .getCancelReasonName()));
        }
        return labels;
    }

}
