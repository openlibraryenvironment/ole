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
package org.kuali.rice.ksb.messaging;

import org.springframework.beans.factory.FactoryBean;

/**
 * This class can be used to create a KSBClientProxy.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KSBClientProxyFactoryBean implements FactoryBean {

    private Class<Object> serviceEndpointInterface;
    private String serviceQName;
    
    public Object getObject() throws Exception {
        return KSBClientProxy.newInstance(serviceQName, serviceEndpointInterface);
    }

    public Class<Object> getObjectType() {
        return serviceEndpointInterface;
    }

    public boolean isSingleton() {
        return false;
    }

    public Class<Object> getServiceEndpointInterface() {
        return serviceEndpointInterface;
    }

    public void setServiceEndpointInterface(Class<Object> serviceInterface) {
        this.serviceEndpointInterface = serviceInterface;
    }

    public String getServiceQName() {
        return serviceQName;
    }

    public void setServiceQName(String serviceQName) {
        this.serviceQName = serviceQName;
    }

}
