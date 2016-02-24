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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;

import org.kuali.ole.deliver.defaultload.*;
import org.kuali.ole.deliver.drools.DroolsKieEngine;
import org.kuali.ole.oclc.OCLCNettyServerHandler;
import org.kuali.ole.select.document.service.OLEEncumberOpenRecurringOrdersService;
import org.kuali.ole.select.document.service.OLEPurchaseOrderBatchService;
import org.kuali.ole.service.NettyHandler;
import org.kuali.ole.service.NettyServer;
import org.kuali.ole.sip2.sip2Server.SIP2NettyServerHandler;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.web.listener.KualiInitializeListener;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;

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

        OLEEncumberOpenRecurringOrdersService encumberOpenRecurringOrdersService =  SpringContext.getBean(OLEEncumberOpenRecurringOrdersService.class);
        encumberOpenRecurringOrdersService.createRolloverDirectory();
        OLEPurchaseOrderBatchService olePurchaseOrderBatchService = (OLEPurchaseOrderBatchService)SpringContext.getService("olePurchaseOrderBatchService");
        olePurchaseOrderBatchService.createPOBADirectory();

        DocumentServiceImpl documentService = (DocumentServiceImpl) SpringContext.getBean("documentService");
        documentService.setDocumentDao((DocumentDao) SpringContext.getBean("documentDao"));
        if (ConfigContext.getCurrentContextConfig().getProperty("autoIngestDefaults").equals("true")) {
            LoadDefaultPatronsBean loadDefaultPatronsBean = GlobalResourceLoader.getService("loadDefaultPatronsBean");
            try {
                loadDefaultPatronsBean.loadDefaultPatrons(false);
                /*Copying Default rule files.*/
                copyDefaultRuleFiles();
            } catch (Exception e) {
                LOG.error(e, e);
            }
        }

        //This initializes the drools engine;
        DroolsKieEngine.getInstance().initKnowledgeBase();
        initializeNettyServerHandlers();

    }

    public void  initializeNettyServerHandlers() {
        Map<Integer, NettyHandler> nettyHandlerMap = new HashMap<>();
        boolean onLoadStartup = ConfigContext.getCurrentContextConfig().getBooleanProperty("sip2.startOnLoad");
        if (onLoadStartup) {
            String sip2PortNo = ConfigContext.getCurrentContextConfig().getProperty("sip2.port");
            String sip2ServerUrl = ConfigContext.getCurrentContextConfig().getProperty("sip2.url");
            nettyHandlerMap.put(Integer.valueOf(sip2PortNo), new SIP2NettyServerHandler(sip2ServerUrl));
        }

        String oclcPortNo = ConfigContext.getCurrentContextConfig().getProperty("oclc.port");
        String oclcServerUrl = ConfigContext.getCurrentContextConfig().getProperty("oclc.url");
        nettyHandlerMap.put(Integer.valueOf(oclcPortNo), new OCLCNettyServerHandler(oclcServerUrl));

        startNettyServers(nettyHandlerMap);
    }

    public void startNettyServers(Map<Integer, NettyHandler> nettyHandlerMap) {
        for (Iterator<Integer> iterator = nettyHandlerMap.keySet().iterator(); iterator.hasNext(); ) {
            Integer port = iterator.next();
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new NettyServer(port, nettyHandlerMap.get(port)));
            System.out.println(" **************** NETTY SERVER STARTED_SUCCESSFULLY ************************ ");
            LOG.info(" **************** NETTY SERVER STARTED_SUCCESSFULLY ************************ ");
        }
    }

    private void copyDefaultRuleFiles() {
        try {
            CopyDefaultRuleFilesBean copyDefaultRuleFilesBean = GlobalResourceLoader.getService("copyDefaultRuleFilesBean");
            copyDefaultRuleFilesBean.copyDefaultRuleFiles();
        } catch (IOException e) {
            LOG.error(e,e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info( "Shutting Down OLE Web Application");
        SpringContext.close();
        SpringContext.applicationContext = null;
        super.contextDestroyed(sce);
    }
}
