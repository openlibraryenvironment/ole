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

import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Tests action converter
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class ActionConverterTest {

    ActionConverter actionConverter
    def config

    static def testResourcesDir = "./src/test/resources/"
    static def strutsTestDir = testResourcesDir + "StrutsConverterTest/"

    @Before
    void setUp() {
        config = ConversionUtils.getConfig(testResourcesDir + "test.config.properties")
        actionConverter = new ActionConverter(config)
    }

    @Test
    void testBuildControllerBinding() {
        def formBeanData = ["name": "BookOrderForm", "class": "org.kuali.rice.kns.web.struts.form.BookOrderForm", "path": "org/kauli/rice/kns/struts/form"]
        def actionClass = ["package": "org.kuali.rice.test",
                "className": "SampleController",
                "parentClass": "UifControllerBase",
                "imports": ["org.kuali.rice.kns.web.struts.form.BookOrderForm"], "methods": []]

        def actionElement = config.map.binding.actionBean
        actionElement.controller.name = "org.kuali.rice.scripts.SampleController"
        def expectedControllerBinding = config.map.binding.uifcontroller

        def resultControllerBinding = actionConverter.buildControllerBinding(formBeanData, actionElement, actionClass)
        checkMapStructure("controller binding", expectedControllerBinding, resultControllerBinding)
        Assert.assertEquals("controller import size does not match " + resultControllerBinding.imports, 2, resultControllerBinding.imports.size())

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

}
