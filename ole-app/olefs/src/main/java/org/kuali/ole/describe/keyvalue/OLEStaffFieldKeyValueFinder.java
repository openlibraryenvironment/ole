package org.kuali.ole.describe.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: juliyamonicas
 * Date: 4/5/14
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEStaffFieldKeyValueFinder extends UifKeyValuesFinderBase {

    public static final Logger LOG = LoggerFactory.getLogger(DocFormatKeyValueFinder.class);

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> options = new ArrayList<KeyValue>();
        options.add(new ConcreteKeyValue("Y", "Yes"));
        options.add(new ConcreteKeyValue("N", "No"));
        return options;
    }
}
