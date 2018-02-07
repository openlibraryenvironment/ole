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
package org.kuali.rice.core.api.mo.common.active;

import org.joda.time.DateTime;

public final class InactivatableFromToUtils {

    private InactivatableFromToUtils() {
        throw new UnsupportedOperationException("do not call");
    }

    public static boolean isActive(DateTime activeFromDate, DateTime activeToDate, DateTime activeAsOfDate) {
        long asOfDate = System.currentTimeMillis();
        if (activeAsOfDate != null) {
            asOfDate = activeAsOfDate.getMillis();
        }

        return computeActive(activeFromDate, activeToDate, asOfDate);
    }

    private static boolean computeActive(DateTime activeFromDate, DateTime activeToDate, long asOfDate) {
        return (activeFromDate == null || asOfDate >= activeFromDate.getMillis()) &&
                (activeToDate == null || asOfDate < activeToDate.getMillis());
    }
}
