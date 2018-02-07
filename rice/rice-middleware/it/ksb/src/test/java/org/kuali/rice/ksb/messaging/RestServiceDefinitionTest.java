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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.kuali.rice.ksb.api.bus.support.JavaServiceDefinition;
import org.kuali.rice.ksb.api.bus.support.RestServiceDefinition;
import org.kuali.rice.ksb.test.KSBTestCase;

/**
 * Tests equality between RESTServiceDefinition objects
 * 
 * @author James Renfro
 * @since 1.3
 *
 */
public class RestServiceDefinitionTest extends KSBTestCase {
    
    private RestServiceDefinition restDefinition;
    private RestServiceDefinition sameExactRestDefinition;
    private RestServiceDefinition otherRestDefinition;
    private RestServiceDefinition otherNameRestDefinition;
    private RestServiceDefinition otherServiceRestDefinition;
    private RestServiceDefinition singleResourceDefinition;
    private JavaServiceDefinition javaServiceDefinition;

    public void setUp() throws Exception {
    	super.setUp();
    	
    	String a = "a";
    	String b = "b";
    	String c = "c";
    	Long l = Long.valueOf(123l);
    	
    	List<Object> restResources = new ArrayList<Object>();
    	restResources.add(a);
    	restResources.add(b);
    	
    	List<Object> sameExactRestResources = new ArrayList<Object>();
    	sameExactRestResources.add(a);
    	sameExactRestResources.add(b);
    	
    	// It's the type that matters, not the value
    	List<Object> functionallySameResources = new ArrayList<Object>();
    	functionallySameResources.add(b);
    	functionallySameResources.add(c);
    	
    	List<Object> otherRestResources = new ArrayList<Object>();
    	otherRestResources.add(l);
    	otherRestResources.add(b);
    	
    	Object service = new ArrayList<Object>();
    	
        this.restDefinition = new RestServiceDefinition();
        this.restDefinition.setLocalServiceName("restServiceName");
        this.restDefinition.setResources(restResources);
        this.restDefinition.validate();
        
        this.sameExactRestDefinition = new RestServiceDefinition();
        this.sameExactRestDefinition.setLocalServiceName("restServiceName");
        this.sameExactRestDefinition.setResources(sameExactRestResources);
        this.sameExactRestDefinition.validate();
                
        this.otherRestDefinition = new RestServiceDefinition();
        this.otherRestDefinition.setLocalServiceName("restServiceName");
        this.otherRestDefinition.setResources(otherRestResources);
        this.otherRestDefinition.validate();
        
        this.otherNameRestDefinition = new RestServiceDefinition();
        this.otherNameRestDefinition.setLocalServiceName("anotherRestServiceName");
        this.otherNameRestDefinition.setResources(sameExactRestResources);
        this.otherNameRestDefinition.validate();
        
        this.otherServiceRestDefinition = new RestServiceDefinition();
        this.otherServiceRestDefinition.setLocalServiceName("restServiceName");
        this.otherServiceRestDefinition.setService(service);
        this.otherServiceRestDefinition.setResources(restResources);
        this.otherServiceRestDefinition.validate();
        
        this.singleResourceDefinition = new RestServiceDefinition();
        this.singleResourceDefinition.setLocalServiceName("restServiceName");
        this.singleResourceDefinition.setService(service);
        this.singleResourceDefinition.validate();
        
        javaServiceDefinition = new JavaServiceDefinition();
        javaServiceDefinition.setBusSecurity(Boolean.FALSE);
        javaServiceDefinition.setLocalServiceName("restServiceName");
        javaServiceDefinition.setService(service);
        javaServiceDefinition.validate();
    }
    
    @Test
    public void testIsSameSuccessWithSameDefinition() {
        assertTrue(this.restDefinition.equals(this.restDefinition));
    }
    
    @Test
    public void testIsSameSuccessWithDifferentDefinition() throws Exception {
        assertTrue(this.restDefinition.equals(sameExactRestDefinition));
    }
    
    @Test
    public void testIsSameFailureWithDifferentServiceClass() throws Exception {
        assertFalse(this.restDefinition.equals(otherRestDefinition));
    }
    
    @Test
    public void testIsSameFailureWithDifferentDefinitionOfSameResources() throws Exception {
    	assertFalse(this.restDefinition.equals(otherNameRestDefinition));
    }
    
    @Test
    public void testIsSameFailureWithDifferentService() throws Exception {
    	assertFalse(this.restDefinition.equals(otherServiceRestDefinition));
    }
    
    @Test
    public void testIsSameFailureWithSingleResourceService() throws Exception {
    	assertFalse(this.restDefinition.equals(singleResourceDefinition));
    }
    
    @Test
    public void testIsSameFailureWithDifferentServiceDefinitionType() throws Exception {
        assertFalse(this.otherServiceRestDefinition.equals(javaServiceDefinition));
    }

}
