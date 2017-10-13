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
package org.kuali.rice.ksb.messaging.quartz;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.util.KSBConstants;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.Properties;

/**
 * A factory bean which reads quartz-related properties from the Config system and
 * generates a Properites instance for use when configuration quartz.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class QuartzConfigPropertiesFactoryBean extends AbstractFactoryBean {

    private static final String QUARTZ_PREFIX = "ksb.org.quartz";
    private static final String QUARTZ_IS_CLUSTERED = QUARTZ_PREFIX + ".jobStore.isClustered";
    private static final String QUARTZ_TABLE_PREFIX = QUARTZ_PREFIX + ".jobStore.tablePrefix";
    
    @Override
    protected Object createInstance() throws Exception {
	Properties properties = new Properties();
	Properties configProps = ConfigContext.getCurrentContextConfig().getProperties();
	boolean useQuartzDatabase = Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.USE_QUARTZ_DATABASE));
	for (Object keyObj : configProps.keySet()) {
	    if (keyObj instanceof String) {
	    	String key = (String)keyObj;
	    	if (key.startsWith(QUARTZ_PREFIX) && !propertyShouldBeFiltered(useQuartzDatabase, key)) {
	    		properties.put(key.substring(4), configProps.get(key));
	    	}
	    }
	}
	return properties;
    }
    
    /**
     * When we aren't using the quartz database, prevents some of the parameters for quartz database mode from
     * being passed to quartz.  If we pass these to quartz when it's using a RAMJobStore, we get an error.  So
     * in order to allow us to provide good defaults in common-config-defaults.xml, we will filter these out
     * if useQuartzDatabase=false
     */
    protected boolean propertyShouldBeFiltered(boolean useQuartzDatabase, String propertyName) {
    	if (!useQuartzDatabase) {
    		if (propertyName.startsWith(QUARTZ_TABLE_PREFIX) || propertyName.startsWith(QUARTZ_IS_CLUSTERED)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    @Override
    public Class getObjectType() {
	return Properties.class;
    }

}
