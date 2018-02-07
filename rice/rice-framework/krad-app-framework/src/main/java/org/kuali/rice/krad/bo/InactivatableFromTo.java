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
package org.kuali.rice.krad.bo;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;

import java.sql.Timestamp;

/**
 * Business objects that have effective dating (from to dates) should implement this interface. This
 * translates the effective dates in terms of active/inactive status so the features built for
 * {@link MutableInactivatable} in the frameworks can be taken advantage of
 */
public interface InactivatableFromTo extends MutableInactivatable {

	/**
	 * Sets the date for which record will be active
	 * 
	 * @param from
	 *            - Timestamp value to set
	 */
	public void setActiveFromDate(Timestamp from);
	
	/**
	 * Gets the date for which the record become active
	 *
	 * @return Timestamp
	 */
	public Timestamp getActiveFromDate();

	/**
	 * Sets the date for which record will be active to
	 * 
	 * @param from
	 *            - Timestamp value to set
	 */
	public void setActiveToDate(Timestamp to);
	
	/**
	 * Gets the date for which the record become inactive
	 *
	 * @return Timestamp
	 */
	public Timestamp getActiveToDate();

	/**
	 * Gets the date for which the record is being compared to in determining active/inactive
	 * 
	 * @return Timestamp
	 */
	public Timestamp getActiveAsOfDate();

	/**
	 * Sets the date for which the record should be compared to in determining active/inactive, if
	 * not set then the current date will be used
	 * 
	 * @param activeAsOfDate
	 *            - Timestamp value to set
	 */
	public void setActiveAsOfDate(Timestamp activeAsOfDate);

}
