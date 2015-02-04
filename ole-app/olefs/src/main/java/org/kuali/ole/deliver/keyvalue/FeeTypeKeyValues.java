package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.OleFeeType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * OleNoteTypeKeyValues returns FeeTypeId and FeeTypeName for OleFeeType
 */
public class FeeTypeKeyValues extends KeyValuesBase {

    /**
     * This method will populate the feeTypeId as a key and feeTypeName as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleFeeType> oleFeeType = KRADServiceLocator.getBusinessObjectService().findAll(OleFeeType.class);
        //keyValues.add(new ConcreteKeyValue(" ", " "));
        for (OleFeeType feeType : oleFeeType) {
            keyValues.add(new ConcreteKeyValue(feeType.getFeeTypeId(), feeType.getFeeTypeName()));
        }
        return keyValues;
    }
}
