package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 3/5/14
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEGlobalDocTypeKeyValueFinder extends UifKeyValuesFinderBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        options.add(new ConcreteKeyValue("", ""));
        options.add(new ConcreteKeyValue("holdings", "Holdings"));
        options.add(new ConcreteKeyValue("item", "Items"));
        options.add(new ConcreteKeyValue("eHoldings", "EHoldings"));
        return options;
    }

}
