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
package org.kuali.rice.kew.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * Looks up Strings from the Config and System Parameters.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ConfigStringLookup extends StrLookup {
	
	private final String applicationId;
	
	public ConfigStringLookup() {
        this(null);
	}
	
	public ConfigStringLookup(String applicationId) {
		this.applicationId = applicationId;
	}

    @Override
    public String lookup(String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            return null;
        }

        String paramValue = null;
        // check system parameters first
        if (StringUtils.isBlank(applicationId)) {
            paramValue = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(
                    KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, propertyName);
        } else {
            ParameterKey parameterKey = ParameterKey.create(applicationId, KewApiConstants.KEW_NAMESPACE,
                    KRADConstants.DetailTypes.ALL_DETAIL_TYPE, propertyName);
            paramValue = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameterValueAsString(parameterKey);
        }

        if (paramValue == null) {
            paramValue = ConfigContext.getCurrentContextConfig().getProperty(propertyName);
        }
        return paramValue;
    }

}
