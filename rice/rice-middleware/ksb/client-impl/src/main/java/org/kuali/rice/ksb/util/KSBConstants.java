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
package org.kuali.rice.ksb.util;


/**
 * This is a file for constants used by the KSB module of Rice
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class KSBConstants {

    /**
     * Configuration Parameters
     */
    public static final class Config {
        public static final String MESSAGE_PERSISTENCE = "message.persistence";
        public static final String MESSAGING_OFF = "message.off";
        public static final String MESSAGE_DELIVERY = "message.delivery";
        public static final String INJECTED_EXCEPTION_MESSAGE_SCHEDULER_KEY = "message.injected.scheduler";
        public static final String FIXED_POOL_SIZE = "ksb.fixedPoolSize";
        public static final String ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_KEY = "RouteQueue.maxRetryAttempts";
        public static final String ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_OVERRIDE_KEY = "RouteQueue.maxRetryAttemptsOverride";
        public static final String ROUTE_QUEUE_TIME_INCREMENT_KEY = "RouteQueue.timeIncrement";
        public static final String IMMEDIATE_EXCEPTION_ROUTING = "Routing.ImmediateExceptionRouting";
        public static final String ALLOW_SYNC_EXCEPTION_ROUTING = "rice.ksb.allowSyncExceptionRouting";
        public static final String KSB_ALLOW_SELF_SIGNED_SSL = "rice.ksb.config.allowSelfSignedSSL";
        public static final String KSB_MESSAGE_DATASOURCE = "rice.ksb.message.datasource";
        public static final String KSB_MESSAGE_DATASOURCE_JNDI = "rice.ksb.message.datasource.jndi.location";
        public static final String KSB_MESSAGE_NON_TRANSACTIONAL_DATASOURCE = "rice.ksb.message.nonTransactional.datasource";
        public static final String KSB_MESSAGE_NON_TRANSACTIONAL_DATASOURCE_JNDI = "rice.ksb.message.nonTransactional.datasource.jndi.location";
        public static final String KSB_REGISTRY_DATASOURCE = "rice.ksb.registry.datasource";
        public static final String KSB_REGISTRY_DATASOURCE_JNDI = "rice.ksb.registry.datasource.jndi.location";
        public static final String KSB_BAM_DATASOURCE = "rice.ksb.bam.datasource";
        public static final String KSB_BAM_DATASOURCE_JNDI = "rice.ksb.bam.datasource.jndi.location";
        public static final String USE_QUARTZ_DATABASE = "useQuartzDatabase";
        public static final String KSB_ALTERNATE_ENDPOINTS = "ksb.alternateEndpoints";
        public static final String KSB_ALTERNATE_ENDPOINT_LOCATIONS = "ksb.alternateEndpointLocations";
        public static final String RESTFUL_SERVICE_PATH = "rice.ksb.restfulServicePath";
        public static final String INSTANCE_ID = "rice.ksb.bus.instanceId";
        public static final String REGISTRY_SERVICE_URL = "rice.ksb.registry.serviceUrl";
        public static final String WEB_FORCE_ENABLE = "rice.ksb.web.forceEnable";


    	private Config() {
    		throw new UnsupportedOperationException("do not call");
    	}
    }
    
    // messaging constants
    
    public static final String MESSAGING_SYNCHRONOUS = "synchronous";
    public static final String ROUTE_QUEUE_QUEUED = "Q";
    public static final String ROUTE_QUEUE_EXCEPTION = "E";
    public static final String ROUTE_QUEUE_ROUTING = "R";
    public static final String ROUTE_QUEUE_EXCEPTION_LABEL = "EXCEPTION";
    public static final String ROUTE_QUEUE_ROUTING_LABEL = "ROUTING";
    public static final String ROUTE_QUEUE_QUEUED_LABEL = "QUEUED";    
    public static final Integer ROUTE_QUEUE_DEFAULT_PRIORITY = new Integer(5);
    public static final String ROUTE_QUEUE_FILTER_SUFFIX = "Filter";
    
    // custom http header keys
    public static final String DIGITAL_SIGNATURE_HEADER = "KEW_DIGITAL_SIGNATURE";
    public static final String KEYSTORE_ALIAS_HEADER = "KEW_KEYSTORE_ALIAS";
    public static final String KEYSTORE_CERTIFICATE_HEADER = "KEW_CERTIFICATE_ALIAS";
    
    public static final class ServiceNames {
    	public static final String BAM_SERVICE = "rice.ksb.bamService";
    	public static final String BUS_IN_INTERCEPTORS = "ksbInInterceptors";
        public static final String BUS_OUT_INTERCEPTORS = "ksbOutInterceptors";
    	public static final String CXF_BUS = "cxf";
    	public static final String DIGITAL_SIGNATURE_SERVICE = "rice.ksb.digitalSignatureService";
    	public static final String ENCRYPTION_SERVICE = "enEncryptionService";
        public static final String EXCEPTION_MESSAGING_SERVICE = "rice.ksb.exceptionMessagingService";
        public static final String JAVA_SECURITY_MANAGEMENT_SERVICE = "rice.ksb.javaSecurityManagementService";
        public static final String JTA_TRANSACTION_MANAGER = "jtaTransactionManager";
        public static final String MESSAGE_DATASOURCE = "rice.ksb.messageDataSource";
        public static final String MESSAGE_ENTITY_MANAGER_FACTORY = "rice.ksb.messageEntityManagerFactory";
        public static final String MESSAGE_QUEUE_SERVICE = "rice.ksb.messageQueueService";
        public static final String MESSAGE_NON_TRANSACTIONAL_DATASOURCE = "rice.ksb.messageNonTransactionalDataSource";
    	public static final String REGISTRY_DATASOURCE = "rice.ksb.registryDataSource";
    	public static final String REGISTRY_ENTITY_MANAGER_FACTORY = "rice.ksb.registryEntityManagerFactory";
    	public static final String SERVICE_EXPORT_MANAGER = "rice.ksb.serviceExportManager";
        public static final String SCHEDULED_THREAD_POOL_SERVICE = "rice.ksb.scheduledThreadPool";
        public static final String SCHEDULER = "rice.ksb.scheduler";
        public static final String THREAD_POOL_SERVICE = "rice.ksb.threadPool";
        public static final String TRANSACTION_MANAGER = "transactionManager";
        public static final String TRANSACTION_TEMPLATE = "transactionTemplate";
        public static final String BASIC_AUTHENTICATION_SERVICE = "basicAuthenticationService";

    	private ServiceNames() {
    		throw new UnsupportedOperationException("do not call");
    	}
    }
    
	private KSBConstants() {
		throw new UnsupportedOperationException("do not call");
	}

}
