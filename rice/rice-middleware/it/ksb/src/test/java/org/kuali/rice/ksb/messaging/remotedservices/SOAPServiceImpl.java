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
package org.kuali.rice.ksb.messaging.remotedservices;

import org.kuali.rice.core.api.config.CoreConfigHelper;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SOAPServiceImpl implements SOAPService {
	
	public static int CALL_COUNT = 0;
	
	public String doTheThing(String param) {
		CALL_COUNT++;
		System.out.println("!!!TestHarnessSharedTopic called with M.E " + CoreConfigHelper.getApplicationId() + " !!! ");
		ServiceCallInformationHolder.stuff.put("TestHarnessCalled", Boolean.TRUE);
		return param;
	}

}
