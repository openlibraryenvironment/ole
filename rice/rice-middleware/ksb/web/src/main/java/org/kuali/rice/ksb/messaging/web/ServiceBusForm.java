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
package org.kuali.rice.ksb.messaging.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;


/**
 * Struts ActionForm for the {@link ServiceBusAction}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ServiceBusForm extends ActionForm {

    private static final long serialVersionUID = -6662210181549552182L;
    
	private String methodToCall;
    private List<ServiceConfiguration> publishedServices = new ArrayList<ServiceConfiguration>();
    private List<ServiceConfiguration> globalServices = new ArrayList<ServiceConfiguration>();

    private String myIpAddress;
    private String myApplicationId;
    private String myInstanceId;
    private Boolean devMode;
    private String removedApplicationId;

    public String getMethodToCall() {
        return this.methodToCall;
    }
    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }
    public List<ServiceConfiguration> getGlobalServices() {
        return this.globalServices;
    }
    public void setGlobalServices(List<ServiceConfiguration> globalServices) {
        this.globalServices = globalServices;
    }
    public List<ServiceConfiguration> getPublishedServices() {
        return this.publishedServices;
    }
    public void setPublishedServices(List<ServiceConfiguration> publishedServices) {
        this.publishedServices = publishedServices;
    }
    public String getMyIpAddress() {
        return this.myIpAddress;
    }
    public void setMyIpAddress(String myIpAddress) {
        this.myIpAddress = myIpAddress;
    }
    public String getMyApplicationId() {
        return this.myApplicationId;
    }
    public void setMyApplicationId(String myApplicationId) {
        this.myApplicationId = myApplicationId;
    }
    public String getMyInstanceId() {
		return this.myInstanceId;
	}
	public void setMyInstanceId(String myInstanceId) {
		this.myInstanceId = myInstanceId;
	}
	public Boolean getDevMode() {
        return this.devMode;
    }
    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }
    public String getRemovedApplicationId() {
        return this.removedApplicationId;
    }
    public void setRemovedApplicationId(String removedApplicationId) {
        this.removedApplicationId = removedApplicationId;
    }

}
