package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleRecordType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RecordType used to render the values for RecordType dropdown control.
 */
public class RecordType extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments recordTypeCode and
     * recordTypeName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleRecordType> oleRecordTypes = KRADServiceLocator.getBusinessObjectService().findAll(OleRecordType.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleRecordType type : oleRecordTypes) {
            options.add(new ConcreteKeyValue(type.getRecordTypeCode(), type.getRecordTypeName()));
        }
        return options;
    }
}
