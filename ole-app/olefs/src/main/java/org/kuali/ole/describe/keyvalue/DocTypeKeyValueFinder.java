package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 3/5/14
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocTypeKeyValueFinder extends UifKeyValuesFinderBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();

        Collection<DocTypeConfig> docTypeConfigs = KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(DocTypeConfig.class, new HashMap<String, Object>(),"name",true);
        for (DocTypeConfig type : docTypeConfigs) {
            options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
        }
        return options;
    }

}
