package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleAcquisitionMethod;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * AcquisitionMethod used to render the values for AcquisitionMethod dropdown control.
 */
public class AcquisitionMethod extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,,
     * ConcreteKeyValue has two argument acquisitionMethodCode and
     * acquisitionMethodName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleAcquisitionMethod> oleAcquisitionMethods = KRADServiceLocator.getBusinessObjectService().findAll(OleAcquisitionMethod.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleAcquisitionMethod type : oleAcquisitionMethods) {
            options.add(new ConcreteKeyValue(type.getAcquisitionMethodCode(), type.getAcquisitionMethodName()));
        }
        return options;
    }
}
