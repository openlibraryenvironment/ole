package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleTypeOfOwnership;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: poornima
 * Date: 28/8/12
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TypeOfOwnership extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments shelvingOrderCode and
     * shelvingOrderName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleTypeOfOwnership> typeOfOwnerships = KRADServiceLocator.getBusinessObjectService().findAll(OleTypeOfOwnership.class);
      //   options.add(new ConcreteKeyValue("", ""));
        for (OleTypeOfOwnership type : typeOfOwnerships) {
            options.add(new ConcreteKeyValue(type.getTypeOfOwnershipCode(), type.getTypeOfOwnershipName()));
        }
        return options;
    }
}
