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
package org.kuali.rice.coreservice.framework;

import org.kuali.rice.core.framework.impex.xml.XmlImpexRegistry;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

public class CoreFrameworkServiceLocator {

    public static final String PARAMETER_SERVICE = "parameterService";
    public static final String XML_IMPEX_REGISTRY = "xmlImpexRegistry";

    static <T> T getService(String serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }

    public static ParameterService getParameterService() {
        return getService(PARAMETER_SERVICE);
    }
    
    public static XmlImpexRegistry getXmlImpexRegistry() {
    	return getService(XML_IMPEX_REGISTRY);
    }
}
