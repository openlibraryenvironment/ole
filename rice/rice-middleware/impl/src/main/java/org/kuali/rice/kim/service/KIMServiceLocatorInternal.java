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
package org.kuali.rice.kim.service;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.impl.group.GroupInternalService;
import org.kuali.rice.kim.service.dao.UiDocumentServiceDAO;

import javax.xml.namespace.QName;

/**
 * Service locator for KIM.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KIMServiceLocatorInternal {

	private static final Logger LOG = Logger.getLogger(KIMServiceLocatorInternal.class);

	public static final String KIM_RUN_MODE_PROPERTY = "kim.mode";

    public static final String KIM_UI_DOCUMENT_SERVICE = "kimUiDocumentService";
	public static final String GROUP_INTERNAL_SERVICE = "groupInternalService";
    public static final String UI_DOCUMENT_SERVICE_DAO = "uiDocumentServiceDAO";

    public static Object getService(String serviceName) {
		return getBean(serviceName);
	}

	public static Object getBean(String serviceName) {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("Fetching service " + serviceName);
		}
        QName name = new QName(serviceName);
        RunMode kimRunMode = RunMode.valueOf(ConfigContext.getCurrentContextConfig().getProperty(KIM_RUN_MODE_PROPERTY));
        if (kimRunMode == RunMode.REMOTE || kimRunMode == RunMode.THIN) {
            name = new QName(KimConstants.KIM_MODULE_NAMESPACE, serviceName);
        }
        return GlobalResourceLoader.getResourceLoader().getService(name);
	}

    public static UiDocumentService getUiDocumentService() {
    	return (UiDocumentService)getService(KIM_UI_DOCUMENT_SERVICE);
    }

    public static GroupInternalService getGroupInternalService() {
        return (GroupInternalService)getService(GROUP_INTERNAL_SERVICE);
    }

    public static UiDocumentServiceDAO getUiDocumentServiceDAO() {
        return (UiDocumentServiceDAO) getBean(UI_DOCUMENT_SERVICE_DAO);
    }
}
