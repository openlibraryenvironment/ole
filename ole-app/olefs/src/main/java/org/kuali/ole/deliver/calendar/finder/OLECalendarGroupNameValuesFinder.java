package org.kuali.ole.deliver.calendar.finder;

import org.kuali.ole.deliver.calendar.bo.OleCalendarGroup;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 7/7/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECalendarGroupNameValuesFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleCalendarGroup> oleCalendarGroups = KRADServiceLocator.getBusinessObjectService().findAll(OleCalendarGroup.class);
        keyValues.add(new ConcreteKeyValue(" ", " "));
        for (OleCalendarGroup oleCalendarGroup : oleCalendarGroups) {
            if(oleCalendarGroup.isActive())
                keyValues.add(new ConcreteKeyValue(oleCalendarGroup.getCalendarGroupId(), oleCalendarGroup.getCalendarGroupName()));
        }

        return keyValues;
    }
}
