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
package org.kuali.rice.location.api.campus;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * <p>CampusTypeContract interface.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface CampusTypeContract extends Versioned, GloballyUnique, Inactivatable, Coded {
	
	/**
	 * This is the name for the CampusType.
	 *
	 * <p>
	 * It is a name a campus type.
	 * </p>
	 *
	 * @return name for CampusType.
	 */
	String getName();
}
