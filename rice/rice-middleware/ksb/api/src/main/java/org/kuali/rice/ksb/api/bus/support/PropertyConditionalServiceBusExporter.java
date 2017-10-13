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
package org.kuali.rice.ksb.api.bus.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;

/**
 * A ServiceBusExporter which only exports the service if the specified property is set
 * to true.
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class PropertyConditionalServiceBusExporter extends ServiceBusExporter {

	private List<String> exportIf = new ArrayList<String>();
	private List<String> exportUnless = new ArrayList<String>();
	private boolean exportIfPropertyNotSet = true;

	public void afterPropertiesSet() {
		if (shouldRemoteThisService()) {
			super.afterPropertiesSet();
		}
	}
	
	protected boolean shouldRemoteThisService() {
		if (exportIf.isEmpty() && exportUnless.isEmpty()) {
			return true;
		}
		boolean remoteThisService = false;
		String serviceValue = null;
		// Check the value in the clients config file for services in the list
		// of property named 'exportIf' loaded by Spring.
		// if any are ="true" then set boolean to true and exit loop, so far the
		// service will be published.
		for (String expIf : exportIf) {
			serviceValue = ConfigContext.getCurrentContextConfig().getProperty(expIf);
			// if any are true, set boolean and exit loop.
			if (!StringUtils.isBlank(serviceValue)) {
				remoteThisService = Boolean.parseBoolean(serviceValue);
				if (remoteThisService) {
					break;
				}
			} else if (exportIfPropertyNotSet) {
				remoteThisService = true;
				break;
			}
		}
		// Check a second list, if any are ="true" DON"T publish the service.
		for (String expUnless : exportUnless) {
			serviceValue = ConfigContext.getCurrentContextConfig()
					.getProperty(expUnless);
			// if any are true, set boolean and exit loop.
			if (!StringUtils.isBlank(serviceValue)) {
				remoteThisService = Boolean.parseBoolean(serviceValue);
				if (remoteThisService) {
					remoteThisService = false;
					break;
				}
			}
		}
		return remoteThisService;
	}

	public List<String> getExportIf() {
		return this.exportIf;
	}

	public void setExportIf(List<String> exportIf) {
		this.exportIf = exportIf;
	}

	public List<String> getExportUnless() {
		return this.exportUnless;
	}

	public void setExportUnless(List<String> exportUnless) {
		this.exportUnless = exportUnless;
	}

	public boolean isExportIfPropertyNotSet() {
		return this.exportIfPropertyNotSet;
	}

	public void setExportIfPropertyNotSet(boolean exportIfPropertyNotSet) {
		this.exportIfPropertyNotSet = exportIfPropertyNotSet;
	}

}
