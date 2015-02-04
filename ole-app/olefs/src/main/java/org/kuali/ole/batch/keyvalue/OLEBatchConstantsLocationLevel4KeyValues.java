package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 3/15/14
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchConstantsLocationLevel4KeyValues extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues = getLocationsByLocationLevel(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_COLLECTION);
        return keyValues;
    }

    private List<KeyValue> getLocationsByLocationLevel(String locationLevel) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleLocation> oleLocations = KRADServiceLocator.getBusinessObjectService().findAll(OleLocation.class);
        for (OleLocation oleLocation : oleLocations) {
            if (locationLevel.equalsIgnoreCase(oleLocation.getLevelCode()))
                keyValues.add(new ConcreteKeyValue(oleLocation.getLocationCode(), oleLocation.getLocationCode()));
        }
        return keyValues;
    }
}
