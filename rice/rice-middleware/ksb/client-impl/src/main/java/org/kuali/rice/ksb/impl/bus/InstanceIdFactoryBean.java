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
package org.kuali.rice.ksb.impl.bus;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.ksb.util.KSBConstants;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * A factory bean which is used to produce an instance id for the service bus.
 * 
 * <p>The value for the instance id is determined in the following order of preferences:
 * 
 * <ol>
 *   <li>If {@code instanceId} is set on this factory bean, that value will be used.</li> 
 *   <li>If {@link KSBConstants.Config#INSTANCE_ID} has been set in the configuration context, that value will be used.</li>
 *   <li>If none of the above, the instance id will be a combination of this application's namespace plus ip address.</li>
 * </ol>
 * 
 * <p>In the case that the instance id is generated, the application id will be pulled
 * from the configuration context using the {@link KSBConstants.Config#INSTANCE_ID} configuration parameter which
 * should always have a value.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class InstanceIdFactoryBean extends AbstractFactoryBean<String> {

	private String instanceId;
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	@Override
	protected String createInstance() throws Exception {
		if (StringUtils.isBlank(instanceId)) {
			this.instanceId = loadInstanceIdFromConfig();
		}
		if (StringUtils.isBlank(instanceId)) {
			String applicationId = CoreConfigHelper.getApplicationId();
			String ipNumber = RiceUtilities.getIpNumber();
			this.instanceId = applicationId + "-" + ipNumber;
		}
        ConfigContext.getCurrentContextConfig().putProperty(KSBConstants.Config.INSTANCE_ID, this.instanceId);
		return this.instanceId;
	}
	
	protected String loadInstanceIdFromConfig() {
		return ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.INSTANCE_ID);
	}

	@Override
	public Class<String> getObjectType() {
		return String.class;
	}

	
	
}
