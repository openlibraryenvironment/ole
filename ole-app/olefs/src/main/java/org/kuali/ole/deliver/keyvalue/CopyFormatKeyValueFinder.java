package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.CopyFormat;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: anithaa
 * Date: 2/12/14
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CopyFormatKeyValueFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<CopyFormat> copyFormats = KRADServiceLocator.getBusinessObjectService().findAll(CopyFormat.class);
        keyValues.add(new ConcreteKeyValue(" ", " "));
        for (CopyFormat copyFormat : copyFormats) {
            if (copyFormat.isActive()) {
                keyValues.add(new ConcreteKeyValue(copyFormat.getCopyFormatId(), copyFormat.getName()));
            }
        }
        return keyValues;
    }
}
