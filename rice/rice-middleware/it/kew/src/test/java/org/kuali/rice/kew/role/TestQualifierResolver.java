/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.role;

import org.kuali.rice.kew.engine.RouteContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A test implementatino of a QualifierResolver which returns qualifications for chart
 * and org as "BL-BUS" and "IN-MED"
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class TestQualifierResolver implements QualifierResolver {
	
	public List<Map<String, String>> resolve(RouteContext context) {
		List<Map<String, String>> resolved = new ArrayList<Map<String, String>>();
		
		Map<String, String> qualifications = new HashMap<String, String>();
		qualifications.put("chart", "BL");
		qualifications.put("org", "BUS");
		resolved.add(qualifications);
		
		qualifications = new HashMap<String, String>();
		qualifications.put("chart", "IN");
		qualifications.put("org", "MED");
		resolved.add(qualifications);
		
		return resolved;
	}

}
