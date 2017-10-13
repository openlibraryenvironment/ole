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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.bo.InactivatableFromTo;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Provides methods for retrieval of business objects implementing InactivateableFromTo and needing effective dating logic
 * 
 * @see org.kuali.rice.kns.bo.InactivateableFromTo
 */
public interface InactivateableFromToService {

	/**
	 * Performs search on given class and criteria and returns only results that active based on the active to/from dates and the current
	 * date
	 * 
	 * @param clazz
	 *            - InactivateableFromTo class to search
	 * @param fieldValues
	 *            - Search key values
	 * @return List of InactivateableFromTo instances that match search criteria and are active
	 */
	public List<InactivatableFromTo> findMatchingActive(Class<? extends InactivatableFromTo> clazz, Map fieldValues);

	/**
	 * Performs search on given class and criteria and returns only results that active based on the active to/from dates and the given
	 * active as of date
	 * 
	 * @param clazz
	 *            - InactivateableFromTo class to search
	 * @param fieldValues
	 *            - Search key values
	 * @param activeAsOfDate
	 *            - Date to compare to for determining active status
	 * @return List of InactivateableFromTo instances that match search criteria and are active as of the given date
	 */
	public List<InactivatableFromTo> findMatchingActiveAsOfDate(Class<? extends InactivatableFromTo> clazz,
			Map fieldValues, Date activeAsOfDate);

	/**
	 * Removes instances from the given list that are inactive based on the current date
	 * 
	 * @param filterList
	 *            - List of InactivateableFromTo instances to filter
	 * @return List of InactivateableFromTo instances from the given list that are active as of the current date
	 */
	public List<InactivatableFromTo> filterOutNonActive(List<InactivatableFromTo> filterList);

	/**
	 * Removes instances from the given list that are inactive based on the given date
	 * 
	 * @param filterList
	 *            - List of InactivateableFromTo instances to filter
	 * @param activeAsOfDate
	 *            - Date to compare to for determining active status
	 * @return List of InactivateableFromTo instances from the given list that are active as of the given date
	 */
	public List<InactivatableFromTo> filterOutNonActive(List<InactivatableFromTo> filterList, Date activeAsOfDate);

	/**
	 * Performs search on given class and criteria and returns that are active and most current. That is if two records are active the more
	 * current one will be the one with a later active begin date
	 * 
	 * @param clazz
	 *            - InactivateableFromTo class to search
	 * @param fieldValues
	 *            - Search key values
	 * @return List of InactivateableFromTo instances that match search criteria and are current
	 */
	public List<InactivatableFromTo> findMatchingCurrent(Class<? extends InactivatableFromTo> clazz,
			Map fieldValues);

	/**
	 * Performs search on given class and criteria and returns that are active and most current based on the given date. That is if two
	 * records are active the more current one will be the one with a later active begin date
	 * 
	 * @param clazz
	 *            - InactivateableFromTo class to search
	 * @param fieldValues
	 *            - Search key values
	 * @param currentAsOfDate
	 *            - Date to compare to for determining active and current status
	 * @return List of InactivateableFromTo instances that match search criteria and are current
	 */
	public List<InactivatableFromTo> findMatchingCurrent(Class<? extends InactivatableFromTo> clazz,
			Map fieldValues, Date currentAsOfDate);

	/**
	 * Removes instances from the given list that are not current based on the current date
	 * 
	 * @param filterList
	 *            - List of InactivateableFromTo instances to filter
	 * @return List of InactivateableFromTo instances from the given list that are current as of the current date
	 */
	public List<InactivatableFromTo> filterOutNonCurrent(List<InactivatableFromTo> filterList);

	/**
	 * Removes instances from the given list that are not current based on the given date
	 * 
	 * @param filterList
	 *            - List of InactivateableFromTo instances to filter
	 * @param currentAsOfDate
	 *            - Date to compare to for determining active and current status
	 * @return List of InactivateableFromTo instances from the given list that are current as of the given date
	 */
	public List<InactivatableFromTo> filterOutNonCurrent(List<InactivatableFromTo> filterList, Date currentAsOfDate);

}
