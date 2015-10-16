package org.kuali.ole.deliver.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 10/16/15.
 */
public class OLECirculationDeskChangeKeyValue extends CirculationDeskChangeKeyValue {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<>();
        options.add(new ConcreteKeyValue("", ""));
        options.addAll(super.getKeyValues());
        return options;
    }
}
