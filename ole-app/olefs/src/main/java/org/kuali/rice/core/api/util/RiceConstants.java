package org.kuali.rice.core.api.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.kuali.rice.core.api.CoreApiServiceLocator;
/**
 * Created with IntelliJ IDEA.
 * User: gayathria
 * Date: 3/24/14
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
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
    public static final String SIMPLE_DATE_FORMAT_FOR_DATE = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString("info.DateFormat");//"dd/MM/yyyy";
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
