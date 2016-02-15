package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenchulakshmig on 11/5/14.
 */
public class OLEPlatformRecordDocumentKeyValues extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEPlatformRecordDocument> olePlatformRecordDocuments;
        olePlatformRecordDocuments = KRADServiceLocator.getBusinessObjectService().findAll(OLEPlatformRecordDocument.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OLEPlatformRecordDocument olePlatformRecordDocument : olePlatformRecordDocuments) {
            keyValues.add(new ConcreteKeyValue(olePlatformRecordDocument.getName(), olePlatformRecordDocument.getName()));
        }
        return keyValues;
    }
}
