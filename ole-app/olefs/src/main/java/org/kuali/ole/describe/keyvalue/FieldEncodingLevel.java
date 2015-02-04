package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleFieldEncodingLevel;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * FieldEncodingLevel used to render the values for FieldEncodingLevel dropdown control.
 */
public class FieldEncodingLevel extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,,
     * ConcreteKeyValue has two arguments fieldEncodingLevelCode and
     * fieldEncodingLevelName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleFieldEncodingLevel> oleFieldEncodingLevels = KRADServiceLocator.getBusinessObjectService().findAll(OleFieldEncodingLevel.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleFieldEncodingLevel type : oleFieldEncodingLevels) {
            options.add(new ConcreteKeyValue(type.getFieldEncodingLevelCode(), type.getFieldEncodingLevelName()));
        }
        return options;
    }
}