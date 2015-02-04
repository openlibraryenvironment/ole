package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEPackageScope;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/26/13
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPackageScopeKeyValues extends KeyValuesBase {

    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEPackageScope> olePackageScopes = KRADServiceLocator.getBusinessObjectService().findAll(OLEPackageScope.class);
        for (OLEPackageScope olePackageScope : olePackageScopes) {
            if (olePackageScope.isActive()) {
                keyValues.add(new ConcreteKeyValue(olePackageScope.getOlePackageScopeId(), olePackageScope.getOlePackageScopeName()));
            }
        }
        return keyValues;
    }
}
