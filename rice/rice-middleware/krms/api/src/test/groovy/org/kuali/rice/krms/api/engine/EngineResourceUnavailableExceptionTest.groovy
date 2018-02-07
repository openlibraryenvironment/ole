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
package org.kuali.rice.krms.api.engine;

import static org.junit.Assert.*

import org.junit.Test
import org.kuali.rice.core.api.exception.RiceRuntimeException

/**
 * Tests the {@link EngineResourceUnavailableException} 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class EngineResourceUnavailableExceptionTest {

	@Test
	void testEngineResourceUnavailableException() {
		// ensure it's part of the RiceRuntimeException hierarchy
		assert RiceRuntimeException.class.isAssignableFrom(EngineResourceUnavailableException.class);
		new EngineResourceUnavailableException();
		new EngineResourceUnavailableException("message");
		new EngineResourceUnavailableException("message", new IllegalArgumentException());
		new EngineResourceUnavailableException(new IllegalArgumentException());
	}
	
}
