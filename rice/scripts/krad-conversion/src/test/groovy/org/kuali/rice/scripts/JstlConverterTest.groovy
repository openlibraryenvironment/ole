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
 * Tests for the {@link JstlConverter} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class JstlConverterTest

{
    static def testResourceDir = "./src/test/resources/"
    static def jstlTestDir = testResourceDir + "JstlConverterTest/"
    def configFilePath
    def config

    @Before
    void setUp() {
        configFilePath = testResourceDir + "test.config.properties"
        config = ConversionUtils.getConfig(configFilePath)
    }

    @Test
    void testProcessJspFile() {
        def filePath = jstlTestDir + "DocumentPage.jsp"
        def jspRoot = JspParserUtils.parseJspFile(filePath)
        def jspDataMap = JstlConverter.transformPage(jspRoot, config.map.convert.jsp_to_tag)
        // TODO start adding assertions to check structure and data
        // checkMapStructure("jsp data", config.map.binding.jsp_data, jspDataMap)
    }

    @Test
    void testBuildViewBinding() {
        // TODO: pull data from test.config.properties rather than include
        def formBean = ["id": "", "name": "", "title": "", "class": "UifFormBase", "path": "/bookOrder", "type": "", "header": "/** test **/"]
        def actionClass = ["package": "org.kuali.rice.test",
                "className": "SampleController",
                "parentClass": "UifControllerBase", "uriGenId": "sample",
                "imports": ["org.kuali.new.class"], "methods": []]
        def jspFileData = ["id": actionClass.uriGenId + "View", "title": actionClass.uriGenId + "View", name: actionClass.uriGenId + "View", beans: ["bookstore-bookOrders-parentBean"], pages: [["name": "defaultId",
                "text": '''<bean id='defaultId' parent='Uif-DocumentPage'><property name='items'><bean parent='Uif-DocumentOverviewSection' /></property></bean>''']]]
        def viewBinding = JstlConverter.buildViewBinding(jspFileData, formBean, actionClass)
        log.finer "expected view binding - " + config.map.binding.uifview
        log.finer "result view binding  - " + viewBinding
        checkMapStructure("view binding", config.map.binding.uifview, viewBinding)
    }



    @Test
    void testBuildUifView() {
        def expectedFile = new File(jstlTestDir + "SampleUifView.xml")
        def expectedText = expectedFile.text

        def viewBinding = [beanId: "SampleView",
                viewType: "Uif-FormView", viewId: "SampleView", viewName: "SampleView", viewTitle: "Sample View",
                headerText: "", entryPageId: "SampleView-Page1", formClass: "UifFormBase",
                beans: ["bookstore-bookOrders-parentBean"], items: [],
                pages: [[name: "SampleView-Page1", text: "<bean id='SampleView-Page1' parent='Uif-DocumentPage'><property name='items'><bean parent='bookstore-bookOrders-parentBean' /></property></bean>"]],]
        //def viewBinding = ["package":"org.kuali.rice.test", "className":"SampleController", "parentClass":"UifControllerBase", "uriGenId":"sample", "imports":["org.kuali.new.class"], "methods":[]]
        def fileText = JstlConverter.buildUifView(viewBinding)
        checkFileText("uif view", expectedText, fileText)
    }

    // helper functions
    /**
     *
     * @param structureTypeName
     * @param expectedMap
     * @param resultMap
     */
    private void checkMapStructure(structureTypeName, expectedMap, resultMap) {
        expectedMap.keySet().each { key -> Assert.assertTrue("map does not contain key: " + key, resultMap.containsKey(key)) }
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
