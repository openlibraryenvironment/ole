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
package org.kuali.rice.core.api.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This is a class to hold constant values that will be used across multiple modules of Rice
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class RiceConstants {
	public static final String DB_PLATFORM = "dbPlatform";

    public static final String RICE_JPA_ENABLED = "rice.jpa.enabled";
    public static final String JPA_ENABLED_SUFFIX = ".jpa.enabled";
    
    public static final String SERVICES_TO_CACHE = "rice.resourceloader.servicesToCache";
    public static final String RICE_LOGGING_CONFIGURE = "rice.logging.configure";

    public static final String SPRING_TRANSACTION_MANAGER = "SPRING_TRANSACTION_MANAGER";

    public static final String ROOT_RESOURCE_LOADER_CONTAINER_NAME = "RootResourceLoaderContainer";
    public static final String DEFAULT_ROOT_RESOURCE_LOADER_NAME = "RootResourceLoader";

    public static final String RICE_CONFIGURER_CONFIG_NAME = "riceConfigurer";

    
    // Configuration Objects

	public static final String DATASOURCE_OBJ = "datasource";
	public static final String NON_TRANSACTIONAL_DATASOURCE_OBJ = "nonTransactionalDatasource";
	public static final String SERVER_DATASOURCE_OBJ = "serverDatasource";
	public static final String TRANSACTION_MANAGER_OBJ = "transactionManager";
	public static final String USER_TRANSACTION_OBJ = "userTransaction";
	public static final String M_BEANS = "mBeans";
	public static final String ALT_SPRING_FILE = "config.spring.file";
	public static final String ALT_OJB_FILE	= "config.obj.file";

	// JNDI configuration 
	
	public static final String TRANSACTION_MANAGER_JNDI = "transactionManager.jndi.location";
	public static final String USER_TRANSACTION_JNDI = "userTransaction.jndi.location";//"transactionManager.jndi.location";
    public static final String DATASOURCE_JNDI = "datasource.jndi.location";
    public static final String NON_TRANSACTIONAL_DATASOURCE_JNDI = "nonTransactional.datasource.jndi.location";
    public static final String SERVER_DATASOURCE_JNDI = "serverDatasource.jndi.location";

	
    // Default struts mapping forward key
    public static final String MAPPING_BASIC = "basic";
    
    // Struts mapping forward key for use when a module is locked
    public static final String MODULE_LOCKED_MAPPING = "moduleLocked";

    // Default date formatting
    public static final String SIMPLE_DATE_FORMAT_FOR_DATE = "MM/dd/yyyy";
    public static final String SIMPLE_DATE_FORMAT_FOR_TIME = "hh:mm a";
    public static final String DEFAULT_DATE_FORMAT_PATTERN = SIMPLE_DATE_FORMAT_FOR_TIME + " " + SIMPLE_DATE_FORMAT_FOR_DATE;

    public static DateFormat getDefaultDateFormat() {
        return new SimpleDateFormat(SIMPLE_DATE_FORMAT_FOR_DATE);
    }

    public static DateFormat getDefaultTimeFormat() {
        return new SimpleDateFormat(SIMPLE_DATE_FORMAT_FOR_TIME);
    }

    public static DateFormat getDefaultDateAndTimeFormat() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
    }

    //the maximum URL length supported (browsers, apache, tomcat, etc)
    //currently constrained by internet explorer
    public static final int MAXIMUM_URL_LENGTH = 2048;

	public static final long NO_WAIT = 0;

	public static final String RICE_JAXWS_TARGET_NAMESPACE_BASE = "http://rice.kuali.org/wsdl";
	
	private RiceConstants() {
		throw new UnsupportedOperationException("do not call");
	}
}
