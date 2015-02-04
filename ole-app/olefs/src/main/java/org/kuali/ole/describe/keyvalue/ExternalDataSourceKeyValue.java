package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.describe.bo.ExternalDataSourceConfig;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 11/12/12
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExternalDataSourceKeyValue
        extends KeyValuesBase {

    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments External Data Source Id and name
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<ExternalDataSourceConfig> externalDataSourceNames = KRADServiceLocator.getBusinessObjectService()
                .findAll(
                        ExternalDataSourceConfig.class);
        options.add(new ConcreteKeyValue("", ""));
        for (ExternalDataSourceConfig type : externalDataSourceNames) {
            options.add(new ConcreteKeyValue(type.getId().toString(), type.getName()));
        }
        return options;
    }
}
