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
package org.kuali.ole.base;

import org.apache.log4j.PropertyConfigurator;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.util.DocStoreEnvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * User: Peri Subrahmanya
 * Date: 3/30/11
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseTestCase {
    protected            DocStoreEnvUtil docStoreEnvUtil = new DocStoreEnvUtil();
    private static final Logger          LOG             = LoggerFactory.getLogger(BaseTestCase.class);

    public BaseTestCase() {
        try {
            URL resource = getClass().getResource("/org/kuali/ole/log4j.properties");
            File file = new File(resource.toURI());
            PropertyConfigurator.configure(resource);
            System.getProperties().put("app.environment", "local");
            docStoreEnvUtil.setVendor("derby");
            docStoreEnvUtil.setProperties(new Properties());
            docStoreEnvUtil.initTestEnvironment();
            docStoreEnvUtil.printEnvironment();
            System.setProperty("OLE_DOCSTORE_USE_DISCOVERY", Boolean.FALSE.toString());
        } catch (Exception e) {
            LOG.info("Exception : " + e);
        }
    }

    protected void setUp() throws Exception {
        try {
            String path = getClass().getClassLoader().getResource(".").getPath();
            //PropertyConfigurator.configure("log4j.properties");
            docStoreEnvUtil.initTestEnvironment();
            docStoreEnvUtil.printEnvironment();
            System.getProperties().put("app.environment", "local");
           // System.getProperties().put("docstore.properties.home", DocStoreConstants.TEST_DOC_STORE_PROP_HOME);
            System.setProperty("OLE_DOCSTORE_USE_DISCOVERY", Boolean.FALSE.toString());
            //  System.setProperty(SolrServerManager.SOLR_SERVER_EMBEDDED, "true");
        }
        catch (Exception e) {
            LOG.info(e.getMessage() , e);
        }

    }

    public String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }

}
