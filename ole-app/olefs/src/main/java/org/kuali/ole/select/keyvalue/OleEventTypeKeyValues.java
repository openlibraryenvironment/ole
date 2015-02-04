package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OleEventType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 9/24/12
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleEventTypeKeyValues extends KeyValuesBase {

    private boolean blankOption;


    public boolean isBlankOption() {
        return blankOption;
    }

    public void setBlankOption(boolean blankOption) {
        this.blankOption = blankOption;
    }

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleEventType> oleEventTypes = KRADServiceLocator.getBusinessObjectService().findAll(OleEventType.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OleEventType oleEventType : oleEventTypes) {
            keyValues.add(new ConcreteKeyValue(oleEventType.getEventTypeName(), oleEventType.getEventTypeName()));
        }
        return keyValues;
    }
}
