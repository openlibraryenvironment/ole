package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleEncodingLevel;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * EncodingLevel used to render the values for EncodingLevel dropdown control.
 */
public class EncodingLevel extends KeyValuesBase {
    /**
     * This method returns the List of  ConcreteKeyValues,
     * ConcreteKeyValue has two arguments encodingLevelCode and
     * encodingLevelName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleEncodingLevel> oleEncodingLevels = KRADServiceLocator.getBusinessObjectService().findAll(OleEncodingLevel.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleEncodingLevel type : oleEncodingLevels) {
            options.add(new ConcreteKeyValue(type.getEncodingLevelCode(), type.getEncodingLevelName()));
        }
        return options;
    }
}
