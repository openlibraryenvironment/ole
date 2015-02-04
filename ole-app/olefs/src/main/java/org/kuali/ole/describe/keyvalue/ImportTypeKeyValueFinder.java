package org.kuali.ole.describe.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 28/12/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportTypeKeyValueFinder
        extends UifKeyValuesFinderBase {

    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        List<KeyValue> options = new ArrayList<KeyValue>();
        options.add(new ConcreteKeyValue("newImport", "New Import"));
        options.add(new ConcreteKeyValue("overLay", "Replace Currently Selected Record"));
        options.add(new ConcreteKeyValue("overLayMatchPoint", "Replace Based On Match Point"));
        return options;
    }
}
