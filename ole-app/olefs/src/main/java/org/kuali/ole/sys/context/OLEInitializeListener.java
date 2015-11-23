/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.sys.context;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletContextEvent;

import org.kuali.ole.deliver.defaultload.*;
import org.kuali.ole.deliver.drools.DroolsEngine;
import org.kuali.ole.deliver.drools.DroolsKieEngine;
import org.kuali.ole.ingest.LoadDefaultIngestProfileBean;
import org.kuali.ole.select.document.service.OLEEncumberOpenRecurringOrdersService;
import org.kuali.ole.select.document.service.OLEPurchaseOrderBatchService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.web.listener.KualiInitializeListener;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;

public class OLEInitializeListener extends KualiInitializeListener {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEInitializeListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Initializing Web Context");
        LOG.info("Calling KualiInitializeListener.contextInitialized");
        super.contextInitialized(sce);
        LOG.info("Completed KualiInitializeListener.contextInitialized");
        Log4jConfigurer.configureLogging(true);
        ApplicationConfigurer.configureApplication();

        // the super implementation above will handle the loading of Spring

        SpringContext.finishInitializationAfterRiceStartup();
        LOG.info("Loaded Spring Context from the following locations: " + Arrays.asList(getContext().getConfigLocations()));

        SpringContext.initMemoryMonitor();
        SpringContext.initMonitoringThread();
        SpringContext.initScheduler();

        /*Copying Default rule files.*/
        try {
            CopyDefaultRuleFilesBean copyDefaultRuleFilesBean = GlobalResourceLoader.getService("copyDefaultRuleFilesBean");
            copyDefaultRuleFilesBean.copyDefaultRuleFiles(false);
        } catch (IOException e) {
            LOG.error(e,e);
        }

        //This initializes the drools engine;
        DroolsKieEngine.getInstance().initKnowledgeBase();

        OLEEncumberOpenRecurringOrdersService encumberOpenRecurringOrdersService =  SpringContext.getBean(OLEEncumberOpenRecurringOrdersService.class);
        encumberOpenRecurringOrdersService.createRolloverDirectory();
        OLEPurchaseOrderBatchService olePurchaseOrderBatchService = (OLEPurchaseOrderBatchService)SpringContext.getService("olePurchaseOrderBatchService");
        olePurchaseOrderBatchService.createPOBADirectory();

        DocumentServiceImpl documentService = (DocumentServiceImpl) SpringContext.getBean("documentService");
        documentService.setDocumentDao((DocumentDao) SpringContext.getBean("documentDao"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info( "Shutting Down OLE Web Application");
        SpringContext.close();
        SpringContext.applicationContext = null;
        super.contextDestroyed(sce);
    }
}
