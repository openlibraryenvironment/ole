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
package edu.sampleu.travel.infrastructure;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Initializes the Travel App Spring context.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TravelServiceLocator {
    private static final Logger LOG = Logger.getLogger(TravelServiceLocator.class);

    private static final String STANDARD_CONTEXT = "classpath:SampleAppBeans.xml";
    private static final String TEST_CONTEXT = "classpath:SampleAppBeans-test.xml";

    private static ConfigurableApplicationContext appContext;

	public static synchronized void initialize(boolean test) {
		if (appContext == null) {
		    final String[] resources;
		    // check if we are running in unit tests, and if so, add the test context resource
		    if (test) {
		        resources = new String[] { STANDARD_CONTEXT, TEST_CONTEXT };
		    } else {
		        resources = new String[] { STANDARD_CONTEXT };
		    }
		    LOG.info("Loading contexts: " + StringUtils.join(resources, ", "));
			appContext = new ClassPathXmlApplicationContext(resources);
		}
	}

	public static synchronized ConfigurableApplicationContext getAppContext() {
		return appContext;
	}	
}
