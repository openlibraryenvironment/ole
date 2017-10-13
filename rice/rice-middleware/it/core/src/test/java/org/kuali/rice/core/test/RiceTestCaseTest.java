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
package org.kuali.rice.core.test;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.test.RiceTestCase;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Verifies that the RiceTestCase starts up cleanly
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RiceTestCaseTest extends RiceTestCase {

	@Test
	public void testTestCase() throws Exception {
		String testConfigVal = ConfigContext.getCurrentContextConfig().getProperty("rice.test.case.test");
		assertEquals("Test config value should have been properly configured.", "test", testConfigVal);
	}
	
	@Override
	protected List<String> getConfigLocations() {
		return Arrays.asList(new String[]{"classpath:META-INF/krad-test-config.xml"});
	}

	@Override
	protected String getModuleName() {
		return "kns";
	}

}
