package org.kuali.ole.select.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;

import java.util.List;

/**
 * Created by maheswarang on 10/15/15.
 */
public class OLEEResourceNotifierKeyValues extends OLEEResourceWorkflowSelectorKeyValues {
    @Override
    public List<KeyValue> getKeyValues() {
      List<KeyValue> keyValues = super.getKeyValues();
        keyValues.add(new ConcreteKeyValue("mail","mail"));
        return keyValues;
    }
    }

