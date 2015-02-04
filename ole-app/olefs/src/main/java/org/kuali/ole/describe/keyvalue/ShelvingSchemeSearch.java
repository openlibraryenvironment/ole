package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 7/2/13
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShelvingSchemeSearch extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleShelvingScheme> oleShelvingSchemes = KRADServiceLocator.getBusinessObjectService().findAll(OleShelvingScheme.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleShelvingScheme type : oleShelvingSchemes) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getShelvingSchemeCode(), type.getShelvingSchemeName()));
            }
        }
        return options;
    }
}
