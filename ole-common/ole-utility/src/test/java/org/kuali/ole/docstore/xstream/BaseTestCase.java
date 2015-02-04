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
package org.kuali.ole.docstore.xstream;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.kuali.ole.docstore.DocStoreConstants;

/**
 * User: Peri Subrahmanya
 * Date: 3/30/11
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class BaseTestCase {

    @Before
    public void setUp() throws Exception {
//        String path = getClass().getClassLoader().getResource(".").getPath();
//        System.out.println(path);
//        PropertyConfigurator.configure("log4j.properties");
//        String userHome = System.getProperty("user.home");
        System.getProperties().put("environment", "local");
        System.getProperties().put("docstore.properties.home", DocStoreConstants.TEST_DOC_STORE_PROP_HOME);
        System.setProperty("OLE_DOCSTORE_USE_DISCOVERY", Boolean.FALSE.toString());
//        System.getProperties().put("discovery.properties.file", userHome + "/ole-discovery.properties");
    }

}
