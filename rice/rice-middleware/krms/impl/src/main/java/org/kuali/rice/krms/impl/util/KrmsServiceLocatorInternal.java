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
package org.kuali.rice.krms.impl.util;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.impl.provider.repository.RepositoryToEngineTranslator;
import org.kuali.rice.krms.impl.ui.CustomOperatorUiTranslator;

import javax.xml.namespace.QName;

/**
 * Like {@link org.kuali.rice.krms.api.KrmsApiServiceLocator} only for non-remotable.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KrmsServiceLocatorInternal {

    public static final String REPOSITORY_TO_ENGINE_TRANSLATOR = "repositoryToEngineTranslator";
    public static final String CUSTOM_OPERATOR_UI_TRANSLATOR = "rice.krms.customOperatorUiTranslator";

	public static final String KRMS_RUN_MODE_PROPERTY = "krms.mode";
	public static final String KRMS_MODULE_NAMESPACE = "KRMS";

	private static final Logger LOG = Logger.getLogger(KrmsServiceLocatorInternal.class);

	
	@SuppressWarnings("unchecked")
	public static <A> A getService(String serviceName) {
		return (A)getBean(serviceName, false);
	}
	
	public static <A> A getBean(String serviceName, boolean forceLocal) {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("Fetching service " + serviceName);
		}
        QName name = new QName(serviceName);
        RunMode krmsRunMode = RunMode.valueOf(ConfigContext.getCurrentContextConfig().getProperty(KRMS_RUN_MODE_PROPERTY));
        if (!forceLocal) {
            if (krmsRunMode == RunMode.REMOTE || krmsRunMode == RunMode.THIN) {
                name = new QName(KRMS_MODULE_NAMESPACE, serviceName);
            }
        }
		return GlobalResourceLoader.getResourceLoader().getService(name);
	}

    public static RepositoryToEngineTranslator getRepositoryToEngineTranslator() {
        return getBean(REPOSITORY_TO_ENGINE_TRANSLATOR, true);
    }

    public static CustomOperatorUiTranslator getCustomOperatorUiTranslator() {
        return getBean(CUSTOM_OPERATOR_UI_TRANSLATOR, true);
    }
	
}
