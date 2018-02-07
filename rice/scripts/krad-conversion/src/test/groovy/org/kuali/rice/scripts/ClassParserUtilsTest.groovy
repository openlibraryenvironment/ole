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
import org.junit.Test

/**
 * Tests for the {@link ClassParserUtils} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
public class ClassParserUtilsTest {

    static def testResourceDir = "./src/test/resources/"
    static def parserTestDir = testResourceDir + "ClassParserUtilsTest/"


    @Test
    void testParseActionClassFile() {
        def file = new File(parserTestDir + "SampleAction.java")
        def actionClass = ClassParserUtils.parseClassFile(file.text, false)

        Assert.assertEquals("edu.sampleu.bookstore.document.web", actionClass.package)
        Assert.assertEquals("imports found in class", 12, actionClass.imports.size())
        Assert.assertEquals("first import in class matches", "edu.sampleu.bookstore.bo.Book", actionClass.imports[0])
        Assert.assertEquals("class name matches", "SampleAction", actionClass.className)
        Assert.assertEquals("superclass matches", "KualiTransactionalDocumentActionBase", actionClass.parentClass)
        Assert.assertEquals("method count matches", 3, actionClass.methods.size())
        Assert.assertEquals("selected method exists", 1, actionClass.methods.findAll { it.methodName == "addBookOrder" }.size())

    }

    /**
     * test parsing a sample action form class
     * */
    @Test
    void testParseActionFormClass() {
        def file = new File(parserTestDir + "SampleForm.java")
        def actionFormData = ClassParserUtils.parseClassFile(file.text, false)
        Assert.assertEquals("form package contains", "edu.sampleu.bookstore.document.web", actionFormData.package)
        Assert.assertEquals("form imports count", 3, actionFormData.imports.size())
        Assert.assertEquals("form imports contains", "edu.sampleu.bookstore.bo.BookOrder", actionFormData.imports[0])
        Assert.assertEquals("form members count", 2, actionFormData.members.size())
        Assert.assertTrue("form members contains ", actionFormData.members.any { it.fieldName == "serialVersionUID" })
        Assert.assertEquals("form class contains", "SampleForm", actionFormData.className)
        Assert.assertEquals("form extends contains", "KualiTransactionalDocumentFormBase", actionFormData.parentClass)
        log.finer "public methods" + actionFormData.methods
        Assert.assertEquals("public methods count", 3, actionFormData.methods.findAll { it.accessModifier == "public" }.size())
        Assert.assertTrue("public methods contains", actionFormData.methods.any { it.accessModifier == "public" && it.methodName == "getNewBookOrder" })
        Assert.assertEquals("private methods count", 0, actionFormData.methods.findAll { it.accessModifier == "private" }.size())
    }

    @Test
    void testParseClassFieldWithValue() {
        def lineText = "  public Integer testcase = 1;"
        def field = ClassParserUtils.parseFieldDeclaration(lineText)
        Assert.assertNotNull("field name not null", field.fieldName)
        Assert.assertEquals("found correct field name", "testcase", field.fieldName)

    }

    @Test
    void testParseClassField() {
        def lineText = "  public Integer testcase;"
        def field = ClassParserUtils.parseFieldDeclaration(lineText)
        Assert.assertNotNull("field name not null", field.fieldName)
        Assert.assertNull("no field value set", field.fieldValue)
    }

    @Test
    void testParseMethodDeclaration() {
        def lineText = "  public final ActionForward addBookOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {"
        def methodElement = ClassParserUtils.parseMethodDeclaration(lineText, [])
        log.finer "method data is " + methodElement
        Assert.assertEquals("access modifier does not match", "public", methodElement.accessModifier)
        Assert.assertEquals("non access modifier does not match", 1, methodElement.nonAccessModifiers.size())
        Assert.assertEquals("non access modifier does not match", "final", methodElement.nonAccessModifiers[0])
        Assert.assertEquals("method name does not match", "addBookOrder", methodElement.methodName)
        Assert.assertEquals("return type does not match", "ActionForward", methodElement.returnType)
        Assert.assertEquals("parameter count does not match", 4, methodElement.parameters.size())
    }

    @Test
    void testParseClassFieldWithModifiers() {
        def lineText = "  public static final Integer testcase;"
        def field = ClassParserUtils.parseFieldDeclaration(lineText)
        Assert.assertNotNull("field name not null", field.fieldName)
        Assert.assertNull("no field value set", field.fieldValue)
        Assert.assertEquals("modifiers size does not match", 2, field.nonAccessModifiers.size())
    }


    @Test
    void testParseClassDeclaration() {
        def lineText = "  public class MyClass extends YourClass implements MyInterface,MyOtherInterface"
        def annotations = []
        def classElement = ClassParserUtils.parseClassDeclaration(lineText, annotations)
        Assert.assertEquals("parent class does not match", "public", classElement.accessModifier)
        Assert.assertEquals("class name does not match", "MyClass", classElement.className)
        Assert.assertEquals("parent class does not match", "YourClass", classElement.parentClass)
        Assert.assertEquals("interfaces does not match", ["MyInterface", "MyOtherInterface"], classElement.interfaces)
    }
}