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
package org.kuali.rice.vc.kew;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.rice.vc.test.WsdlCompareTestCase;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KewWsdlCompatibilityTest extends WsdlCompareTestCase {
    private static final Logger LOG = Logger.getLogger(KewWsdlCompatibilityTest.class);
    private static final String MODULE_NAME = "kew";

    public KewWsdlCompatibilityTest() {
        super(MODULE_NAME);
    }

    @Test
    public void compareKewApiWsdls() {
        String wsdlDirectory = "../../" + getModuleName() + "/api/target/wsdl";
        File[] files = new File(wsdlDirectory).listFiles();
        if (files == null) throw new RuntimeException("can't find wsdls at " + wsdlDirectory + " from " + (new File(".")).getAbsolutePath());
        compareWsdlFiles(files);
    }


    @Test
    public void compareKewFrameworkWsdls() {
        String wsdlDirectory = "../../" + getModuleName() + "/framework/target/wsdl";
        File[] files = new File(wsdlDirectory).listFiles();
        if (files == null) throw new RuntimeException("can't find wsdls at " + wsdlDirectory + " from " + (new File(".")).getAbsolutePath());
        compareWsdlFiles(files);
    }

    @Override
    protected Map<String, List<WsdlCompareTestCase.VersionTransition>> getWsdlVersionTransitionBlacklists() {
        Map<String, List<WsdlCompareTestCase.VersionTransition>> blacklist =
                new HashMap<String, List<WsdlCompareTestCase.VersionTransition>>(super.getWsdlVersionTransitionBlacklists());

        blacklist.put("ImmediateEmailReminderQueue",
                Arrays.asList(
                        new WsdlCompareTestCase.VersionTransition("2.1.0", "2.1.1"),
                        new WsdlCompareTestCase.VersionTransition("2.0.2", "2.1.1")
                ));

        blacklist.put("ActionListService",
                Arrays.asList(
                        new WsdlCompareTestCase.VersionTransition("2.1.0", "2.1.1"),
                        new WsdlCompareTestCase.VersionTransition("2.0.2", "2.1.1")
                ));

        blacklist.put("WorkflowDocumentService",
                Arrays.asList(
                        new WsdlCompareTestCase.VersionTransition("2.1.2", "2.2.0"),
                        new WsdlCompareTestCase.VersionTransition("2.2.0", "2.2.1")
                ));

        return blacklist;
    }
}
