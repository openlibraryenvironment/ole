package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sundarr
 * Date: 7/2/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerialsReceivingRecordSearchByKeyValueFinder extends KeyValuesBase {

    /**
     * This method will populate the code as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        keyValues.add(new ConcreteKeyValue(OLEConstants.TITLE_SEARCH, "Title"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.ISSN_SEARCH, "ISSN"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.SERIAL_SEARCH, "Serials Receiving Record No"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.LOCALID_SEARCH, "Local Identifier"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.PO_SEARCH, "Purchase Order No"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.JOURNAL_TITLE_SEARCH, "Journal Title"));
        return keyValues;
    }
}