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
package org.kuali.ole.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.kuali.ole.docstore.process.DocStoreCamelContext;
import org.kuali.ole.docstore.utility.DocStoreSettingsUtil;
import org.kuali.ole.service.DocumentStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 11/3/11
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentStoreContextListner implements ServletContextListener {
    private Logger logger   = LoggerFactory.getLogger(DocumentStoreContextListner.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DocStoreSettingsUtil.getInstance().copyResources();
        } catch (Exception e) {
           logger.error(e.getMessage() , e);
        }
        DocStoreCamelContext.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        DocStoreCamelContext.getInstance().stop();
        new DocumentStoreService().shutdownRepository();
    }
}
