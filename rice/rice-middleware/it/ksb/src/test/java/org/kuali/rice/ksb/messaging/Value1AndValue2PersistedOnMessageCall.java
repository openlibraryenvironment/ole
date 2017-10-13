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

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;
import org.kuali.rice.ksb.util.KSBConstants;

/**
 * verify that value1 and value2 are preserved when passed into message helper and making an async call.  
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class Value1AndValue2PersistedOnMessageCall extends KSBTestCase {
    
    @Test public void testCallingQueueAsnyc() throws Exception {
	KSBTestUtils.setMessagingToAsync();
	ConfigContext.getCurrentContextConfig().putProperty(KSBConstants.Config.MESSAGING_OFF, "true");
	
	QName serviceName = QName.valueOf("{testAppsSharedTopic}sharedTopic");
	String value1 = "value1";
	String value2 = "value2";
	KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, null, null, value1, value2);
	testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));
	
	PersistedMessageBO message = KSBServiceLocator.getMessageQueueService().getNextDocuments(null).get(0);
	assertEquals("value1 incorrectly saved", value1, message.getValue1());
	assertEquals("value2 incorrectly saved", value2, message.getValue2());
	
    }

}
