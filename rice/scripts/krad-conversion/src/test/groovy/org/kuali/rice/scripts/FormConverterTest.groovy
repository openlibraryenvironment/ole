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
 * This class tests the FormController class
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class FormConverterTest {

    FormConverter formConverter
    def config

    static def testResourcesDir = "./src/test/resources/"
    static def formTestDir = testResourcesDir + "FormConverterTest/"

    @Before
    void setUp() {
        config = ConversionUtils.getConfig(testResourcesDir + "test.config.properties")
        formConverter = new FormConverter(config)
    }

    @Test
    void testBuildUifForm() {
        def expectedFile = new File(formTestDir + "SampleUifForm.java")
        def expectedText = expectedFile.text
        def formBinding = ["package": "edu.sampleu.bookstore.document.web", "author": "rice", "className": "SampleUifForm", "accessModifier": "public", "parentClass": "TransactionalDocumentFormBase", interfaces: [], annotations: [], imports: ["edu.sampleu.bookstore.bo.BookOrder", "edu.sampleu.bookstore.document.BookOrderDocument", "org.kuali.rice.krad.web.form.TransactionalDocumentFormBase"], members: [[accessModifier: "private", nonAccessModifiers: ["static", "final"], fieldType: "final", "fieldName": "serialVersionUID", annotations: [], fieldValue: "-206564464059467788L"], ["accessModifier": "private", nonAccessModifiers: [], "fieldType": "BookOrder", "fieldName": "newBookOrder", annotations: [], fieldValue: null]], methods: [[accessModifier: "public", nonAccessModifiers: [], "returnType": "BookOrder", "methodName": "getNewBookOrder", parameters: [], exceptions: null, annotations: []], ["accessModifier": "public", nonAccessModifiers: [], "returnType": "void", "methodName": "setNewBookOrder", parameters: ["BookOrder newBookOrder"], exceptions: null, annotations: []], [accessModifier: "public", nonAccessModifiers: [], returnType: "BookOrderDocument", methodName: "getBookOrderDocument", parameters: [], exceptions: null, annotations: []]]]

        def resultText = formConverter.buildUifForm(formBinding)
        log.finer "uif form result text:\n" + resultText
        checkFileText("uif form", expectedText, resultText)
    }

    @Test
    void testBuildFormBindingData() {
        def formClassData = ["package": "edu.sampleu.bookstore.document.web", "author": "rice", "className": "SampleUifForm", "accessModifier": "public", "parentClass": "KualiTransactionalDocumentFormBase", interfaces: [], annotations: [], imports: ["edu.sampleu.bookstore.bo.BookOrder", "edu.sampleu.bookstore.document.BookOrderDocument", "org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase"], members: [[accessModifier: "private", nonAccessModifiers: ["static", "final"], fieldType: "final", "fieldName": "serialVersionUID", annotations: [], fieldValue: "-206564464059467788L"], ["accessModifier": "private", nonAccessModifiers: [], "fieldType": "BookOrder", "fieldName": "newBookOrder", annotations: [], fieldValue: null]], methods: [[accessModifier: "public", nonAccessModifiers: [], "returnType": "BookOrder", "methodName": "getNewBookOrder", parameters: [], exceptions: null, annotations: []], ["accessModifier": "public", nonAccessModifiers: [], "returnType": "void", "methodName": "setNewBookOrder", parameters: ["BookOrder newBookOrder"], exceptions: null, annotations: []], [accessModifier: "public", nonAccessModifiers: [], returnType: "BookOrderDocument", methodName: "getBookOrderDocument", parameters: [], exceptions: null, annotations: []]]]
        def resultFormBinding = formConverter.buildFormBinding(formClassData)
        log.finer "binding result for import " + resultFormBinding.imports
        Assert.assertEquals("import count matches", 3, resultFormBinding.imports.size())
        Assert.assertTrue("contained unmodified import", resultFormBinding.imports.any { it =~ /\.BookOrderDocument/ })
        Assert.assertTrue("contain legacy form class", !resultFormBinding.imports.any { it =~ /\.KualiTransactionalDocumentFormBase/ })
        Assert.assertTrue("contains new import form class", resultFormBinding.imports.any { it =~ /\.TransactionalDocumentFormBase/ })
    }

    @Test
    void testGetFormBeansData() {
        def strutsConfigPath = formTestDir + "struts-config.xml"
        def strutsConfig = StrutsConverter.parseStrutsConfig(strutsConfigPath)
        def formBeans = StrutsConverter.getFormBeans(strutsConfig)
        def formBeansData = formConverter.getFormBeanData("TravelDocumentForm2", formBeans)
        // check structure matches
        checkMapStructure("form bean data", config.map.binding.form_bean, formBeansData)
        // check class data matches
        Assert.assertEquals("form beans data class", "edu.sampleu.travel.web.form.TravelDocumentForm2", formBeansData.class)
    }

    @Test
    void testBuildFormBindingStructure() {
        def formClassData = config.map.binding.class
        def expectedFormBinding = config.map.binding.uifform

        formClassData.parentClass = "KualiForm"
        def resultFormBinding = formConverter.buildFormBinding(formClassData)
        checkMapStructure("form binding", expectedFormBinding, resultFormBinding)
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
