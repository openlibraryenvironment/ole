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
package org.kuali.rice.ksb.messaging;

import org.junit.Ignore;
import org.junit.Test;


/**
 * This test is currently being ignored.  See KULRICE-1852 for details.
 */
@Ignore
public class DistributedTopicNoJtaTest extends DistributedTopicTest {

	@Test public void testSuccessfullyCallingSyncTopics() throws Exception {
		super.testSuccessfullyCallingSyncTopics();
	}
	
	@Test public void testCallingAsyncTopics() throws Exception {
		super.testCallingAsyncTopics();
	}
	
	@Override
	protected boolean disableJta() {
		return true;
	}
	
}
