/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.docstore.utility;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.docstore.DocStoreConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

/**
 * Class to Doc Store Settings Util.
 *
 * @author Rajesh Chowdary K
 * @created Apr 9, 2012
 */
public class DocStoreSettingsUtil {

    private static final DocStoreSettingsUtil inst = new DocStoreSettingsUtil();
    private static final Logger logger = LoggerFactory.getLogger(DocStoreSettingsUtil.class);

    /**
     * Method to copyResources
     */
    public void copyResources() {
        try {
            File targetFolder = new File(DocStoreConstants.DOCSTORE_SETTINGS_DIR_PATH + File.separator + DocStoreConstants.RESOURCES_DIR_NAME);
            if (!targetFolder.exists() || (targetFolder.exists() && targetFolder.list().length <= 0)) {
                Enumeration<URL> resources = this.getClass().getClassLoader().getResources(DocStoreConstants.RESOURCES_DIR_NAME);
                boolean canItBeTaken = false;
                while (resources.hasMoreElements()) {
                    URL url = resources.nextElement();
                    try {
                        url.toURI();
                        canItBeTaken = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (canItBeTaken) {
                        logger.debug("Copying Settings from source @:" + url.getFile());
                        File sourceDir = new File(url.toURI());
                        FileUtils.copyDirectoryToDirectory(sourceDir, targetFolder.getParentFile());
                    }
                }
                logger.debug("Copied Settings @: " + DocStoreConstants.DOCSTORE_SETTINGS_DIR_PATH);
            } else
                logger.info("Using Existing Settings @: " + DocStoreConstants.DOCSTORE_SETTINGS_DIR_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DocStoreSettingsUtil() {
    }

    public static DocStoreSettingsUtil getInstance() {
        return inst;
    }

}
