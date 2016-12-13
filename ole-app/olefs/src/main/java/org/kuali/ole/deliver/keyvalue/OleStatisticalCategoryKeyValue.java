package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.OleStatisticalCategoryBo;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OleStatisticalCategoryKeyValue returns OleStatisticalCategoryId and OleStatisticalCategoryName for OleStatisticalCategoryBo.
 */
public class OleStatisticalCategoryKeyValue extends KeyValuesBase {
    /**
     * This method will populate the code as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleStatisticalCategoryBo> oleStatisticalCategoryBos = KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OleStatisticalCategoryBo.class,"oleStatisticalCategoryName",true);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OleStatisticalCategoryBo oleStatisticalCategoryType : oleStatisticalCategoryBos) {
            if (oleStatisticalCategoryType.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleStatisticalCategoryType.getOleStatisticalCategoryId(), oleStatisticalCategoryType.getOleStatisticalCategoryName()));
            }
        }
        return keyValues;
    }
}
