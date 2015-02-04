package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 9/9/14.
 */
public class OLEPlatformSearchFinder extends UifKeyValuesFinderBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> platformSearchOptions = new ArrayList<KeyValue>();

        platformSearchOptions.add(new ConcreteKeyValue(OLEConstants.PLATFORM_NAME, OLEConstants.PLATFORM_NAME_VALUE));
        platformSearchOptions.add(new ConcreteKeyValue(OLEConstants.GOKB_ID, OLEConstants.PLATFORM_GOKB_ID));
        platformSearchOptions.add(new ConcreteKeyValue(OLEConstants.OLE_PLATFORM_ID, OLEConstants.PLATFORM_OLE_ID));
        platformSearchOptions.add(new ConcreteKeyValue(OLEConstants.PLATFORM_PROVIDER_NAME, OLEConstants.PLATFORM_PROVIDER_NAME_VALUE));
        platformSearchOptions.add(new ConcreteKeyValue(OLEConstants.STATUS_NAME, OLEConstants.PLATFORM_STATUS));

        return platformSearchOptions;
    }
}
