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
package org.kuali.rice.core.api.criteria;

import org.kuali.rice.core.api.util.jaxb.EnumStringAdapter;

/**
 * Defines possible directives for how a query is requested to produce count values in it's results.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public enum CountFlag {

	/**
	 * Indicates that no row count should be returned with the query results.
	 */
	NONE,
	
	/**
	 * Indicates that the row count of the query should be returned with the query results.
	 */
	INCLUDE,
	
	/**
	 * Indicates that *only* the row count should be returned with the query results.  The
	 * result should not include the actual rows returned from the query.
	 */
	ONLY;

	/**
	 * Returns the value of the count flag.
	 * 
	 * @return the flag
	 */
	public String getFlag() {
		return toString();
	}
	
	static final class Adapter extends EnumStringAdapter<CountFlag> {
		
		protected Class<CountFlag> getEnumClass() {
			return CountFlag.class;
		}
		
	}
	
}
