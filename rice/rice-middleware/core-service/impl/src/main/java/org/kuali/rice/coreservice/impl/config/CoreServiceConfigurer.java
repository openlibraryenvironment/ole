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
package org.kuali.rice.coreservice.impl.config;

import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows for configuring a client to integrate with the "core services" module in Kuali Rice.
 *
 * <p>The CoreServiceConfigurer supports two run modes:
 *   <ol>
 *       <li>REMOTE - loads the client which interacts remotely with the services</li>
 *       <li>LOCAL - loads the service implementations and web components locally</li>
 *   </ol>
 * </p>
 *
 * <p>Client applications should generally only use "remote" run mode (which is the default).</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CoreServiceConfigurer extends ModuleConfigurer {

    private static final String MODULE_NAME = "coreservice";

    public CoreServiceConfigurer() {
        super(MODULE_NAME);
        setValidRunModes(Arrays.asList(RunMode.REMOTE, RunMode.LOCAL));
    }

    @Override
	public List<String> getPrimarySpringFiles() {
        List<String> springFileLocations = new ArrayList<String>();
        if (RunMode.REMOTE == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "CoreServiceRemoteSpringBeans.xml");
        } else if (RunMode.LOCAL == getRunMode()) {
            springFileLocations.add(getDefaultConfigPackagePath() + "CoreServiceLocalSpringBeans.xml");
        }
		return springFileLocations;
	}

}
