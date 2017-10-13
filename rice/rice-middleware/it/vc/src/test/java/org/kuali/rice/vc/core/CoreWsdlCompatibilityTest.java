/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.vc.core;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.rice.vc.test.WsdlCompareTestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CoreWsdlCompatibilityTest extends WsdlCompareTestCase {
    private static final Logger LOG = Logger.getLogger(CoreWsdlCompatibilityTest.class);
    private static final String MODULE_NAME = "core";

    public CoreWsdlCompatibilityTest() {
        super(MODULE_NAME);
    }

    @Test
    public void compareCoreServiceWsdls() {
        File[] files = new File("../../" + getModuleName() + "-service/api/target/wsdl").listFiles();
        compareWsdlFiles(files);
    }

    @Test
    public void compareCoreWsdls() {
        File[] files = new File("../../" + getModuleName() + "/api/target/wsdl").listFiles();
        assertTrue("There should be wsdls to compare", files != null  && files.length > 0);
        if (StringUtils.equals("2.0.1", getPreviousVersion())
                && files != null) {
            //hack to remove test for CacheAdminService, because 2.0.1's wdsl for that was generated incorrectly
            List<File> fileList = new ArrayList<File>();
            for (File file : files) {
                if (!file.getName().equals("CacheAdminService.wsdl")) {
                    fileList.add(file);
                }
            }
            files = fileList.toArray(new File[]{});
        }
        if (files.length > 0) {
            compareWsdlFiles(files);
        }
    }


}
