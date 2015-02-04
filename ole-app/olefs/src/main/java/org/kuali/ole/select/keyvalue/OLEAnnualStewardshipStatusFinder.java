package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 12/10/13
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAnnualStewardshipStatusFinder extends UifKeyValuesFinderBase {
    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        List<KeyValue> statusOptions = new ArrayList<KeyValue>();
        statusOptions.add(new ConcreteKeyValue(OLEConstants.RECIEVED, OLEConstants.RECIEVED));
        statusOptions.add(new ConcreteKeyValue(OLEConstants.PAID, OLEConstants.PAID));
        statusOptions.add(new ConcreteKeyValue(OLEConstants.RECEIVED_PAID, OLEConstants.RECEIVED_PAID));
        return statusOptions;
    }
}
