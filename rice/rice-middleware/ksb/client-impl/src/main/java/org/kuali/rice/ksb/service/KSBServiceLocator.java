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
package org.kuali.rice.ksb.service;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.kuali.rice.core.api.exception.RiceRemoteServiceConnectionException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.messaging.bam.service.BAMService;
import org.kuali.rice.ksb.messaging.exceptionhandling.ExceptionRoutingService;
import org.kuali.rice.ksb.messaging.service.MessageQueueService;
import org.kuali.rice.ksb.messaging.serviceexporters.ServiceExportManager;
import org.kuali.rice.ksb.messaging.threadpool.KSBScheduledPool;
import org.kuali.rice.ksb.messaging.threadpool.KSBThreadPool;
import org.kuali.rice.ksb.security.admin.service.JavaSecurityManagementService;
import org.kuali.rice.ksb.security.service.DigitalSignatureService;
import org.kuali.rice.ksb.util.KSBConstants;
import org.quartz.Scheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.List;


public class KSBServiceLocator {
    public static Object getService(String name) {
        return GlobalResourceLoader.getService(name);
    }

    public static EntityManagerFactory getMessageEntityManagerFactory() {
        return (EntityManagerFactory) getService(KSBConstants.ServiceNames.MESSAGE_ENTITY_MANAGER_FACTORY);
    }
    
    public static EntityManagerFactory getRegistryEntityManagerFactory() {
        return (EntityManagerFactory) getService(KSBConstants.ServiceNames.REGISTRY_ENTITY_MANAGER_FACTORY);
    }
    
    public static TransactionTemplate getTransactionTemplate() {
        return (TransactionTemplate) getService(KSBConstants.ServiceNames.TRANSACTION_TEMPLATE);
    }

    public static PlatformTransactionManager getPlatformTransactionManager() {
        return (PlatformTransactionManager) getService(KSBConstants.ServiceNames.TRANSACTION_MANAGER);
    }

    public static BAMService getBAMService() {
        return (BAMService) getService(KSBConstants.ServiceNames.BAM_SERVICE);
    }

    public static MessageQueueService getMessageQueueService() {
        return (MessageQueueService) getService(KSBConstants.ServiceNames.MESSAGE_QUEUE_SERVICE);
    }

    public static ExceptionRoutingService getExceptionRoutingService() {
        return (ExceptionRoutingService) getService(KSBConstants.ServiceNames.EXCEPTION_MESSAGING_SERVICE);
    }
    
    public static ServiceExportManager getServiceExportManager() {
    	return (ServiceExportManager) getService(KSBConstants.ServiceNames.SERVICE_EXPORT_MANAGER);
    }

    public static DigitalSignatureService getDigitalSignatureService() {
        return (DigitalSignatureService) getService(KSBConstants.ServiceNames.DIGITAL_SIGNATURE_SERVICE);
    }

    public static JavaSecurityManagementService getJavaSecurityManagementService() {
        return (JavaSecurityManagementService) getService(KSBConstants.ServiceNames.JAVA_SECURITY_MANAGEMENT_SERVICE);
    }

    public static KSBThreadPool getThreadPool() {
        return (KSBThreadPool) getService(KSBConstants.ServiceNames.THREAD_POOL_SERVICE);
    }

    public static KSBScheduledPool getScheduledPool() {
        return (KSBScheduledPool) getService(KSBConstants.ServiceNames.SCHEDULED_THREAD_POOL_SERVICE);
    }

    public static Bus getCXFBus(){
    	return (CXFBusImpl) getService(KSBConstants.ServiceNames.CXF_BUS);
    }

    public static List<Interceptor<? extends Message>> getInInterceptors() {
    	try {
    		return (List<Interceptor<? extends Message>>) getService(KSBConstants.ServiceNames.BUS_IN_INTERCEPTORS);
    	}
    	catch(RiceRemoteServiceConnectionException ex) {
    		// swallow this exception, means no bus wide interceptors defined
    		return null;
    	}
    }
    
    public static List<Interceptor<? extends Message>> getOutInterceptors() {
    	try {
    		return (List<Interceptor<? extends Message>>) getService(KSBConstants.ServiceNames.BUS_OUT_INTERCEPTORS);
    	}
    	catch(RiceRemoteServiceConnectionException ex) {
    		// swallow this exception, means no bus wide interceptors defined
    		return null;
    	}
    }

    public static DataSource getMessageDataSource() {
        return (DataSource) getService(KSBConstants.ServiceNames.MESSAGE_DATASOURCE);
    }

    public static DataSource getMessageNonTransactionalDataSource() {
        return (DataSource) getService(KSBConstants.ServiceNames.MESSAGE_NON_TRANSACTIONAL_DATASOURCE);
    }

    public static DataSource getRegistryDataSource() {
        return (DataSource) getService(KSBConstants.ServiceNames.REGISTRY_DATASOURCE);
    }

    public static Scheduler getScheduler() {
        return (Scheduler) getService(KSBConstants.ServiceNames.SCHEDULER);
    }

    public static BasicAuthenticationService getBasicAuthenticationService() {
        return (BasicAuthenticationService) getService(KSBConstants.ServiceNames.BASIC_AUTHENTICATION_SERVICE);
    }

}
