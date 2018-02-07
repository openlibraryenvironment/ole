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
package org.kuali.rice.ksb.messaging.exceptionhandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.api.bus.support.JavaServiceConfiguration;
import org.kuali.rice.ksb.api.messaging.AsynchronousCall;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.PersistedMessagePayload;
import org.kuali.rice.ksb.test.KSBTestCase;
import org.kuali.rice.ksb.util.KSBConstants;


public class DefaultMessageExceptionHandlerTest extends KSBTestCase {
    
    private PersistedMessageBO setupMessage(Integer retriesAttempted, Integer serviceMaxRetries) throws Exception {
        PersistedMessageBO message = new PersistedMessageBO();
        message.setRetryCount(retriesAttempted);
        //ServiceInfo serviceInfo = new ServiceInfo();
        //serviceInfo.setServiceDefinition(new JavaServiceDefinition());
        //serviceInfo.getServiceDefinition().setRetryAttempts(serviceMaxRetries);
        JavaServiceConfiguration.Builder builder = JavaServiceConfiguration.Builder.create();
        builder.setRetryAttempts(serviceMaxRetries);
        AsynchronousCall methodCall = new AsynchronousCall(new Class[0], new Object[0], builder.build(), "", null, null);
        message.setPayload(new PersistedMessagePayload(methodCall, message));
        message.setMethodCall(methodCall);
        return message;
    }
    
    private void setMaxRetries(String maxRetries) {
    	ConfigContext.getCurrentContextConfig().putProperty(KSBConstants.Config.ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_OVERRIDE_KEY, maxRetries);
    }
    
    @Test public void testGetGlobalMaxRetryAttempts() throws Exception {
        DefaultMessageExceptionHandler exceptionHandler = new DefaultMessageExceptionHandler();
        
        this.setMaxRetries("0");
        
        
        //  test non-numeric
        this.setMaxRetries("B");
        assertNull("Method should return null if app constant is non-numeric.", exceptionHandler.getGlobalMaxRetryAttempts());
        
        //  test large negative number
        this.setMaxRetries("-10");
        assertNull("Method should return null if app constant is negative number.", exceptionHandler.getGlobalMaxRetryAttempts());
        
        //  test -1
        this.setMaxRetries("-1");
        assertNull("Method should return null if app constant is negative number.", exceptionHandler.getGlobalMaxRetryAttempts());
        
        //  test 0
        this.setMaxRetries("0");
        assertEquals("Method should return app constant value if app constant is numeric and greater than or equal to zero.", new Integer(0), exceptionHandler.getGlobalMaxRetryAttempts());
        
        //  test 1
        this.setMaxRetries("1");
        assertEquals("Method should return app constant value if app constant is numeric and greater than or equal to zero.", new Integer(1), exceptionHandler.getGlobalMaxRetryAttempts());
        
        //  test 5
        this.setMaxRetries("5");
        assertEquals("Method should return app constant value if app constant is numeric and greater than or equal to zero.", new Integer(5), exceptionHandler.getGlobalMaxRetryAttempts());
    }
    
    @Test public void testIsInException() throws Exception {
        DefaultMessageExceptionHandler exceptionHandler = new DefaultMessageExceptionHandler();
        PersistedMessageBO message = null;
        
        this.setMaxRetries("-10");

        message = setupMessage(0, 1);
        assertFalse(exceptionHandler.isInException(message));
        
        message.setRetryCount(1);
        assertTrue(exceptionHandler.isInException(message));
        
        message.setRetryCount(2);
        assertTrue(exceptionHandler.isInException(message));
        
        this.setMaxRetries("5");
        
        message.setRetryCount(4);
        assertFalse(exceptionHandler.isInException(message));
        
        message.setRetryCount(5);
        assertTrue(exceptionHandler.isInException(message));
        
        message.setRetryCount(6);
        assertTrue(exceptionHandler.isInException(message));
        
        this.setMaxRetries("0");
        
        message.setRetryCount(0);
        assertTrue(exceptionHandler.isInException(message));
        
        message.setRetryCount(1);
        assertTrue(exceptionHandler.isInException(message));
        
        this.setMaxRetries("-1");
        
        message.setRetryCount(0);
        assertFalse(exceptionHandler.isInException(message));
        
        message = setupMessage(1, 5);
        assertFalse(exceptionHandler.isInException(message));
        
        this.setMaxRetries("B");
        
        message.setRetryCount(0);
        assertFalse(exceptionHandler.isInException(message));
    }

}
