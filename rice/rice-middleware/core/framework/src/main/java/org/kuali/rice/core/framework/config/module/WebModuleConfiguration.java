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
package org.kuali.rice.core.framework.config.module;

import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Includes configuration for a web module of Kuali Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WebModuleConfiguration {

    private final String moduleName;
    private List<String> webSpringFiles;

    public WebModuleConfiguration(String moduleName) {
        this.moduleName = moduleName;
        this.webSpringFiles = new ArrayList<String>();
    }

    public String getWebModuleBaseUrl() {
		return ConfigContext.getCurrentContextConfig().getProperty(getModuleName().toLowerCase() + ".url");
	}

    public String getWebModuleStrutsConfigurationFiles() {
		return ConfigContext.getCurrentContextConfig().getProperty("rice." + getModuleName().toLowerCase() + ".struts.config.files");
	}

    public String getWebModuleStrutsConfigName() {
        return "config/" + getModuleName().toLowerCase();
    }
    
    public String getModuleName() {
        return moduleName;
    }

    public List<String> getWebSpringFiles() {
        return webSpringFiles;
    }

    public void setWebSpringFiles(List<String> webSpringFiles) {
        this.webSpringFiles = webSpringFiles;
    }

}
