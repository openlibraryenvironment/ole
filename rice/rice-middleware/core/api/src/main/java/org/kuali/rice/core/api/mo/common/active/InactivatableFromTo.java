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

import java.sql.Timestamp;

/**
 * This interface can be used to identify a model object which has an
 * "active range" such that an object is only active with a certain
 * temporal range.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface InactivatableFromTo extends Inactivatable {

    /**
	 * Gets the date for which the record become active. Can be null.
	 *
	 * @return Timestamp of active from date
	 */
	DateTime getActiveFromDate();

	/**
	 * Gets the date for which the record become inactive. Can be null.
	 *
	 * @return Timestamp of active to date
	 */
	DateTime getActiveToDate();

    /**
     * Returns if the record is active for a given Time.
     * If the activeAsOfDate is null will return null.
     *
     * @return true if active false if not
     */
    boolean isActive(DateTime activeAsOfDate);


}
