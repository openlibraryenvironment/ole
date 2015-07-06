package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayabharathreddy on 7/3/15.
 */
public class OLEBatchProcessGokbBibValuesFinder extends KeyValuesBase {

    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("GOKb UID","GOKb UID"));
        keyValues.add(new ConcreteKeyValue("Name","Name"));
        keyValues.add(new ConcreteKeyValue("VariantName","VariantName"));
        keyValues.add(new ConcreteKeyValue("TI ISSN (Online)","TI ISSN (Online)"));
        keyValues.add(new ConcreteKeyValue("TI ISSN (Print)","TI ISSN (Print)"));
        keyValues.add(new ConcreteKeyValue("TI ISSN-L","TI ISSN-L"));
        keyValues.add(new ConcreteKeyValue("OCLC Number","OCLC Number"));
        keyValues.add(new ConcreteKeyValue("TI DOI","TI DOI"));
        keyValues.add(new ConcreteKeyValue("TI Publisher ID","TI Publisher ID"));
        keyValues.add(new ConcreteKeyValue("TI Proprietary ID","TI Proprietary ID"));
        keyValues.add(new ConcreteKeyValue("TI SUNCAT","TI SUNCAT"));
        keyValues.add(new ConcreteKeyValue("TI LCCN","TI LCCN"));
        return keyValues;
    }
}