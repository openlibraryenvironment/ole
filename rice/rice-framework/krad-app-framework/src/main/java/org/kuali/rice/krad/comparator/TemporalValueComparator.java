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

import org.kuali.rice.core.web.format.DateFormatter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class TemporalValueComparator implements Comparator, Serializable {
    private static final TemporalValueComparator theInstance = new TemporalValueComparator();
    
    public TemporalValueComparator() {
    }
    
    public static TemporalValueComparator getInstance() {
        return theInstance;
    }
    
    public int compare(Object o1, Object o2) {

        // null guard. non-null value is greater. equal if both are null
        if (null == o1 || null == o2) {
            return null == o1 && null == o2 ? 0 : null == o1 ? -1 : 1;
        }

        String s1 = (String) o1;
        String s2 = (String) o2;

        DateFormatter f1 = new DateFormatter();

        Date d1 = (Date) f1.convertFromPresentationFormat(s1);
        Date d2 = (Date) f1.convertFromPresentationFormat(s2);
        
        if (null == d1 || null == d2) {
            return null == d1 && null == d2 ? 0 : null == d1 ? -1 : 1;
        }

        return d1.equals(d2) ? 0 : d1.before(d2) ? -1 : 1;
    }
}
