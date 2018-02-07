/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.comparator;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.io.Serializable;
import java.util.Comparator;

public class NumericValueComparator implements Serializable, Comparator {

    static final long serialVersionUID = 3449202365486147519L;

    private static final NumericValueComparator theInstance = new NumericValueComparator();
    
    public NumericValueComparator() {
    }
    
    public static NumericValueComparator getInstance() {
        return theInstance;
    }
    
    public int compare(Object o1, Object o2) {

        // null guard. non-null value is greater. equal if both are null
        if (null == o1 || null == o2) {
            return (null == o1 && null == o2) ? 0 : ((null == o1) ? -1 : 1);
        }

        String numericCompare1 = (String) o1;
        String numericCompare2 = (String) o2;

        numericCompare1 = StringUtils.replace(numericCompare1, ",", "");
        numericCompare1 = StringUtils.replace(numericCompare1, "$", "");
        numericCompare1 = StringUtils.replace(numericCompare1, "&nbsp;", "");
        numericCompare2 = StringUtils.replace(numericCompare2, ",", "");
        numericCompare2 = StringUtils.replace(numericCompare2, "$", "");
        numericCompare2 = StringUtils.replace(numericCompare2, "&nbsp;", "");

        // handle negatives
        if (StringUtils.contains(numericCompare1, "(")) {
            numericCompare1 = StringUtils.replace(numericCompare1, "(", "");
            numericCompare1 = StringUtils.replace(numericCompare1, ")", "");
            numericCompare1 = "-" + numericCompare1;
        }

        if (StringUtils.contains(numericCompare2, "(")) {
            numericCompare2 = StringUtils.replace(numericCompare2, "(", "");
            numericCompare2 = StringUtils.replace(numericCompare2, ")", "");
            numericCompare2 = "-" + numericCompare2;
        }

        KualiDecimal k1 = null;
        try {
            k1 = new KualiDecimal(numericCompare1);
        }
        catch (Throwable t) {
            k1 = KualiDecimal.ZERO;
        }

        KualiDecimal k2 = null;
        try {
            k2 = new KualiDecimal(numericCompare2);
        }
        catch (Throwable t) {
            k2 = KualiDecimal.ZERO;
        }

        double d1 = k1.doubleValue();
        double d2 = k2.doubleValue();

        return (d1 == d2) ? 0 : ((d1 < d2) ? -1 : 1);
    }
}
