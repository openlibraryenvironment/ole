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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;

import org.junit.Test;
import org.kuali.rice.ksb.api.bus.support.JavaServiceDefinition;
import org.kuali.rice.ksb.api.bus.support.SoapServiceDefinition;
import org.kuali.rice.ksb.test.KSBTestCase;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 *
 */
public class SoapServiceDefinitionTest extends KSBTestCase {
    
    private SoapServiceDefinition soapDefinition;

    public void setUp() throws Exception {
    	super.setUp();
        this.soapDefinition = new SoapServiceDefinition();
        this.soapDefinition.setLocalServiceName("testServiceName");
        this.soapDefinition.setEndpointUrl(new URL("http://www.rutgers.edu"));
        this.soapDefinition.setService(new ArrayList<String>());
        this.soapDefinition.validate();
    }
    
    @Test
    public void testIsSameSuccessWithSameDefinition() {
        assertTrue(this.soapDefinition.equals(this.soapDefinition));
    }
    
    @Test
    public void testIsSameSuccessWithDifferentDefinition() throws Exception {
        final SoapServiceDefinition soapServiceDefinition = new SoapServiceDefinition();
        soapServiceDefinition.setLocalServiceName("testServiceName");
        soapServiceDefinition.setEndpointUrl(new URL("http://www.rutgers.edu"));
        soapServiceDefinition.setService(new ArrayList<String>());
        soapServiceDefinition.validate();
    	
        assertTrue(this.soapDefinition.equals(soapServiceDefinition));
    }
    
    @Test
    public void testIsSameFailureWithDifferentClass() throws Exception {
        final JavaServiceDefinition javaServiceDefinition = new JavaServiceDefinition();
        javaServiceDefinition.setBusSecurity(Boolean.FALSE);
        javaServiceDefinition.setLocalServiceName("testServiceName");
        javaServiceDefinition.setEndpointUrl(new URL("http://www.rutgers.edu"));
        javaServiceDefinition.setService(new ArrayList<String>());
        javaServiceDefinition.validate();
        assertFalse(this.soapDefinition.equals(javaServiceDefinition));
    }
}
