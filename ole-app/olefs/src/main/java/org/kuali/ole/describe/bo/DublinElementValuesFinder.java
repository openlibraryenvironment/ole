package org.kuali.ole.describe.bo;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * DublinElementValuesFinder used to render element values for Dublin Editor
 */
public class DublinElementValuesFinder extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,,
     * ConcreteKeyValue has two argument MethodCode and
     * MethodName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        options.add(new ConcreteKeyValue("default", "Select an Element"));
        options.add(new ConcreteKeyValue("dc:contributor", "Contributor"));
        options.add(new ConcreteKeyValue("dc:coverage", "Coverage"));
        options.add(new ConcreteKeyValue("dc:creator", "Creator"));
        options.add(new ConcreteKeyValue("dc:date", "Date"));
        options.add(new ConcreteKeyValue("dc:description", "Description"));
        options.add(new ConcreteKeyValue("dc:format", "Format"));
        options.add(new ConcreteKeyValue("dc:identifier", "identifier"));
        options.add(new ConcreteKeyValue("dc:language", "Language"));
        options.add(new ConcreteKeyValue("dc:publisher", "Publisher"));
        options.add(new ConcreteKeyValue("dc:relation", "Relation"));
        options.add(new ConcreteKeyValue("dc:rights", "Rights"));
        options.add(new ConcreteKeyValue("dc:source", "Source"));
        options.add(new ConcreteKeyValue("dc:subject", "Subject"));
        options.add(new ConcreteKeyValue("dc:title", "Title"));
        options.add(new ConcreteKeyValue("dc:type", "Type"));
        return options;
    }
}
