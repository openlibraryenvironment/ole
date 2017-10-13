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
package org.kuali.rice.scripts

import groovy.util.logging.Log
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Tests for the {@link ScaffoldGenerator} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class ScaffoldGeneratorTest {
    ScaffoldGenerator scaffold

    static def testResourceDir = "./src/test/resources/"
    static def scaffoldTestDir = testResourceDir + "ScaffoldGeneratorTest/"

    @Before
    void setUp() {
        def config = ConversionUtils.getConfig(testResourceDir + "test.config.properties")
        scaffold = new org.kuali.rice.scripts.ScaffoldGenerator(config)
    }

    @Test
    void testBuildSpringBeansValidation() {
        def spring_bean_xml_path_list = []
        def tmp_dir = System.getProperty("java.io.tmpdir")

        // create some test data
        spring_bean_xml_path_list.add(tmp_dir + "/src/main/resources/bookOrder/bookOrder.xml")
        spring_bean_xml_path_list.add(tmp_dir + "/src/main/resources/META-INF/web-fragment.xml")
        spring_bean_xml_path_list.add(tmp_dir + "/src/main/resources/test/bookOrder.xml")
        try {
            //scaffold.buildSpringBeansValidationTest(tmp_dir, spring_bean_xml_path_list)
        } catch (Exception e) {
            e.printStackTrace()
            Assert.fail("exception occurred in testing")
        }
    }

}
