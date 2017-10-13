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

import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata;

import java.util.List;

/**
 * A service that helps to print out records that block the inactivation of another BO
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface InactivationBlockingDisplayService {
	/**
	 * Retrieves a List of Strings representing each record that blocks the inactivation of blockedBo
	 *
	 * @param blockedBo
	 * @param inactivationBlockingMetadata the blocking relationship
	 * @return
	 */
	public List<String> listAllBlockerRecords(BusinessObject blockedBo, InactivationBlockingMetadata inactivationBlockingMetadata);
}
