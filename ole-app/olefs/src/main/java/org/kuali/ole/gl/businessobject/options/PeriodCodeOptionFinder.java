/*
 * Copyright 2006 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.gl.businessobject.options;

import java.util.ArrayList;
import java.util.List;

import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.UniversityDate;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.valuefinder.ValueFinder;

/**
 * An implementation of ValueFinder that allows the selection of a period code
 */
public class PeriodCodeOptionFinder extends KeyValuesBase implements ValueFinder {

    /**
     * Returns this default value of this ValueFinder, in this case the current period code
     * @return the key of the default Key/Value pair
     * @see org.kuali.rice.krad.valuefinder.ValueFinder#getValue()
     */
    public String getValue() {
        UniversityDate ud = SpringContext.getBean(UniversityDateService.class).getCurrentUniversityDate();
        return ud.getUniversityFiscalAccountingPeriod();
    }

    /**
     * Returns a list of possible options for this ValueFinder; here, each of the fiscal periods
     * @return a List of key/value pair options
     * @see org.kuali.rice.kns.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */
    public List getKeyValues() {
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH1, OLEConstants.MONTH1));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH2, OLEConstants.MONTH2));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH3, OLEConstants.MONTH3));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH4, OLEConstants.MONTH4));

        labels.add(new ConcreteKeyValue(OLEConstants.MONTH5, OLEConstants.MONTH5));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH6, OLEConstants.MONTH6));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH7, OLEConstants.MONTH7));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH8, OLEConstants.MONTH8));

        labels.add(new ConcreteKeyValue(OLEConstants.MONTH9, OLEConstants.MONTH9));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH10, OLEConstants.MONTH10));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH11, OLEConstants.MONTH11));
        labels.add(new ConcreteKeyValue(OLEConstants.MONTH12, OLEConstants.MONTH12));

        labels.add(new ConcreteKeyValue(OLEConstants.MONTH13, OLEConstants.MONTH13));
        labels.add(new ConcreteKeyValue(OLEConstants.PERIOD_CODE_ANNUAL_BALANCE, OLEConstants.PERIOD_CODE_ANNUAL_BALANCE));
        labels.add(new ConcreteKeyValue(OLEConstants.PERIOD_CODE_BEGINNING_BALANCE, OLEConstants.PERIOD_CODE_BEGINNING_BALANCE));
        labels.add(new ConcreteKeyValue(OLEConstants.PERIOD_CODE_CG_BEGINNING_BALANCE, OLEConstants.PERIOD_CODE_CG_BEGINNING_BALANCE));

        return labels;
    }

}
