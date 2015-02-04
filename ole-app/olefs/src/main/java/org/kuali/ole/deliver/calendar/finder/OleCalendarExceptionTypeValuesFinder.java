package org.kuali.ole.deliver.calendar.finder;

import org.kuali.ole.deliver.calendar.bo.OleCalendarExceptionType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dileepp
 * Date: 12/4/13
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarExceptionTypeValuesFinder extends KeyValuesBase{
    @Override
    public List<KeyValue> getKeyValues() {

        List<OleCalendarExceptionType> codes = (List<OleCalendarExceptionType>) SpringContext.getBean(KeyValuesService.class).findAll(
                OleCalendarExceptionType.class);
        // copy the list of codes before sorting, since we can't modify the results from this method
        if (codes == null) {
            codes = new ArrayList<OleCalendarExceptionType>(0);
        } else {
            codes = new ArrayList<OleCalendarExceptionType>(codes);
        }
        // sort using comparator.
        Collections.sort(codes, new Comparator() {
            @Override
            public int compare(Object object1, Object object2) {
                OleCalendarExceptionType oleCalendarExceptionType1 = (OleCalendarExceptionType) object1;
                OleCalendarExceptionType oleCalendarExceptionType2 = (OleCalendarExceptionType) object2;
                return oleCalendarExceptionType1.getExceptionTypeName().compareTo(oleCalendarExceptionType2.getExceptionTypeName());
            }
        });

        List<KeyValue> labels = new ArrayList<KeyValue>();
        labels.add(new ConcreteKeyValue("", ""));

        for (OleCalendarExceptionType oleCalendarExceptionType : codes) {
                    labels.add(new ConcreteKeyValue(oleCalendarExceptionType.getExceptionTypeName(),oleCalendarExceptionType.getExceptionTypeName()));
        }

        return labels;
    }
}

