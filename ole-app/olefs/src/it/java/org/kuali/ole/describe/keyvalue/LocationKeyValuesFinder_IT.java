package org.kuali.ole.describe.keyvalue;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 8/29/12
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */

public class LocationKeyValuesFinder_IT extends SpringBaseTestCase {

    private List<OleLocation> temp;

    @Test
    @Transactional
    public void testGetKeyValues() throws Exception {
        List<KeyValue> options = new ArrayList<KeyValue>();
        List<String> locations = new ArrayList<String>();

        Collection<OleLocation> oleLocations = KRADServiceLocator.getBusinessObjectService().findAll(OleLocation.class);
        temp = (List<OleLocation>) oleLocations;
        StringBuilder locationString = new StringBuilder();
        StringBuilder locationCode = new StringBuilder();
        OleLocation parentLocation = null;
        Iterator<OleLocation> iterator = oleLocations.iterator();
        while (iterator.hasNext()) {
            OleLocation location = iterator.next();
            if (null == parentLocation) {
                if (null == location.getParentLocationId()) {
                    parentLocation = location;
                    locationString.append(parentLocation.getLocationName());
                    locationCode.append(parentLocation.getLocationCode());
                }
            } else if (parentLocation.getLocationId().equals(location.getParentLocationId())) {
                locationString.append("-" + location.getLocationName());
                locationCode.append("-" + parentLocation.getLocationCode());
                parentLocation = location;

            } else {
                locations.add(locationString.toString());
                options.add(new ConcreteKeyValue(locationCode.toString(), locationString.toString()));
                locationString = new StringBuilder();
                locationCode = new StringBuilder();
                List<OleLocation> oleLocations1 = new ArrayList<OleLocation>();
                if (null != temp) {
                    oleLocations1.addAll(temp);
                }
                oleLocations1.remove(parentLocation);
                temp = oleLocations1;
                parentLocation = null;
                iterator = oleLocations1.iterator();
            }
        }
        locations.add(locationString.toString());
        locations.add(temp.get(0).getLocationName());

        Collections.sort(locations);

        for (Iterator<String> oleLocationIterator = locations.iterator(); oleLocationIterator.hasNext(); ) {
            String locationName = oleLocationIterator.next();
            System.out.println(locationName);

        }
    }
}
