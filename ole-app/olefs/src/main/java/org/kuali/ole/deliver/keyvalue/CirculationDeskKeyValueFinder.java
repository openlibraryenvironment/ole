package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/13/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CirculationDeskKeyValueFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("active", "true");
        /*  List<String> myList = new CopyOnWriteArrayList<String>()<String>();*/
        Collection<OleCirculationDesk> oleCirculationDesks = KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, userMap);
        options.add(new ConcreteKeyValue("", ""));
        for (OleCirculationDesk oleCirculationDesk : oleCirculationDesks) {

            options.add(new ConcreteKeyValue(oleCirculationDesk.getCirculationDeskId(), oleCirculationDesk.getCirculationDeskCode()));

        }
        return options;
    }
}
