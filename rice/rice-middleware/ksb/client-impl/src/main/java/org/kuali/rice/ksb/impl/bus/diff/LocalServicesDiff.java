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
package org.kuali.rice.ksb.impl.bus.diff;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.impl.bus.LocalService;

/**
 * TODO... 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class LocalServicesDiff {

	private final List<ServiceInfo> servicesToRemoveFromRegistry;
	private final List<LocalService> localServicesToPublish;
	private final Map<LocalService, ServiceInfo> localServicesToUpdate;
		
	public LocalServicesDiff(List<ServiceInfo> servicesToRemoveFromRegistry,
			List<LocalService> localServicesToPublish,
			Map<LocalService, ServiceInfo> localServicesToUpdate) {
		this.servicesToRemoveFromRegistry = servicesToRemoveFromRegistry == null ? Collections.<ServiceInfo>emptyList() : servicesToRemoveFromRegistry;
		this.localServicesToPublish = localServicesToPublish == null ? Collections.<LocalService>emptyList() : localServicesToPublish;
		this.localServicesToUpdate = localServicesToUpdate == null ? Collections.<LocalService, ServiceInfo>emptyMap() : localServicesToUpdate;
	}

	public List<ServiceInfo> getServicesToRemoveFromRegistry() {
		return this.servicesToRemoveFromRegistry;
	}

	public List<LocalService> getLocalServicesToPublish() {
		return this.localServicesToPublish;
	}
	
	public Map<LocalService, ServiceInfo> getLocalServicesToUpdate() {
		return this.localServicesToUpdate;
	}

}
