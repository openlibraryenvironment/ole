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
 * Tests for the {@link StrutsConverter} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class StrutsConverterTest {

    StrutsConverter struts
    def config

    static def testResourcesDir = "./src/test/resources/"
    static def strutsTestDir = testResourcesDir + "StrutsConverterTest/"

    @Before
    void setUp() {
        config = ConversionUtils.getConfig(testResourcesDir + "test.config.properties")
        struts = new StrutsConverter(config)
    }

    @Test
    void testBuildController() {
        def expectedFile = new File(strutsTestDir + "SampleController.java")
        def expectedText = expectedFile.text
        def controllerBinding = ["header": null,
                "author": "rice",
                "package": "org.kuali.rice.test",
                "className": "SampleController",
                "uri": "/bookOrder",
                "parentClassName": "UifControllerBase",
                "formClassName": "UifFormBase",
                "oldActionClass": "SampleAction",
                "imports": ["org.kuali.new.class"],
                "actionMethods": [["methodName": "addBookOrder"]],
                "privateMethods": [["methodName": "processResults"]]]
        def fileText = StrutsConverter.buildController(controllerBinding)
        log.finer "comparing\n---\nexpectedText:\n" + expectedText + "\n---\nfileText:\n" + fileText + "\n---\n" //Assert.assertEquals("line count matches", expectedText.readLines().size(),fileText.readLines().size())
        (0..<expectedText.readLines().size()).each {
            Assert.assertEquals("line " + it + " matches", expectedText.readLines()[it], fileText.readLines()[it])
        }
    }


    // helper functions
    /**
     * compares the structure of the two maps to make sure the data is being processed properly
     *
     * @param structureTypeName
     * @param expectedMap
     * @param resultMap
     */
    private void checkMapStructure(structureTypeName, expectedMap, resultMap) {
        expectedMap.keySet().each { key -> Assert.assertTrue("result map missing key: " + key, resultMap.containsKey(key)) }
        resultMap.keySet().each { key -> Assert.assertTrue("result map contains extra key: " + key, expectedMap.containsKey(key)) }
        Assert.assertEquals(structureTypeName + " map structure count does not match", expectedMap.keySet().size(), resultMap.keySet().size())
    }

    /**
     * simple line by line check of the file text
     *
     * @param fileType
     * @param expectedText
     * @param resultText
     */
    private void checkFileText(fileType, expectedText, resultText) {
        (0..<expectedText.readLines().size()).each {
            Assert.assertEquals(fileType + " line " + it + " matches", expectedText.readLines()[it], resultText.readLines()[it])
        }
    }

}
