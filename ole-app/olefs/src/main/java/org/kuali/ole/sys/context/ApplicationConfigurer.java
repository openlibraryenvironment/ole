/*
 * Copyright 2007 The Kuali Foundation
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

import java.io.File;
import java.io.IOException;

import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class ApplicationConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfigurer.class);

    public static void configureApplication() {
        prepareFileSystem();
    }

    protected static void prepareFileSystem() {
        prepareKeystore();
    }

    protected static void prepareKeystore() {
        String filename = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLE_FS_KEYSTORE_FILE_PROPERTY);
//        File file = new File(filename);
//        if (file.exists()) {
//            logger.info("Keystore file located at [" + file + "]");
//            return;
//        }
//        String location = PropertyLoadingFactoryBean.getBaseProperty(OLEConstants.OLE_FS_DEFAULT_KEYSTORE_FILE_LOCATION_PROPERTY);
//        try {
//            logger.info("Creating default keystore at [" + file + "] from [" + location + "]");
//            OLEPropertyUtils.copy(location, file);
//        }
//        catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
    }

}
