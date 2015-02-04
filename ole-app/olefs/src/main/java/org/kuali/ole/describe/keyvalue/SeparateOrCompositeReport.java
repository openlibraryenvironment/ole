package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleSeperateOrCompositeReport;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SeparateOrCompositeReport used to render the values for SeparateOrCompositeReport dropdown control.
 */
public class SeparateOrCompositeReport extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments seperateOrCompositeReportCode and
     * seperateOrCompositeReportName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleSeperateOrCompositeReport> oleSeperateOrCompositeReports = KRADServiceLocator.getBusinessObjectService().findAll(OleSeperateOrCompositeReport.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleSeperateOrCompositeReport type : oleSeperateOrCompositeReports) {
            options.add(new ConcreteKeyValue(type.getSeperateOrCompositeReportCode(), type.getSeperateOrCompositeReportName()));
        }
        return options;
    }
}
