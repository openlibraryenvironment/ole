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
package org.kuali.rice.krad.uif;

import org.junit.Test;
import org.kuali.rice.krad.test.TestDictionaryBean;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.test.KRADTestCase;
import org.kuali.rice.krad.test.TestDictionaryConfig;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test methods for the {@link org.kuali.rice.krad.uif.util.UifBeanFactoryPostProcessor} class
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@TestDictionaryConfig(
        namespaceCode = "KR-NS",
        dataDictionaryFiles = "classpath:org/kuali/rice/krad/uif/UifBeanFactoryPostProcessorTestBeans.xml")
public class UifBeanFactoryPostProcessorTest extends KRADTestCase {

    /**
     * Verifies overriding of a property that exists in the parent bean definition as an expression on
     * a nested bean
     *
     * <p>
     * For example, suppose our parent bean has a nested bean named 'nestedBean' with property foo, that has
     * an expression value. The child bean then sets the property 'nestedBean.foo' to a value. We need to
     * verify the expression is removed from the nested parent bean
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testOverrideOfNestedBeanExpression() throws Exception {
        // test expression is captured
        InputField inputField = (InputField) getTestDictionaryObject("testNestedExpressionOverride2");
        assertNotNull("No bean exists with id: testNestedExpressionOverride2", inputField);

        assertNotNull("Expression not in graph", inputField.getExpressionGraph().get("inquiry.render"));

        // one level of nesting, parent with nested notation
        inputField = (InputField) getTestDictionaryObject("testNestedExpressionOverride3");
        assertNotNull("No bean exists with id: testNestedExpressionOverride3", inputField);

        assertTrue("Child property did not override", inputField.getInquiry().isRender());
        assertNull("Parent nested bean expression still in expression graph", inputField.getExpressionGraph().get(
                "inquiry.render"));

        // two levels of nesting
        TestDictionaryBean testBean = (TestDictionaryBean) getTestDictionaryObject("testNestedExpressionOverride5");
        assertNotNull("No bean exists with id: testNestedExpressionOverride5", testBean);

        assertEquals("Child property did not override", "old school",
                testBean.getReference1().getReference1().getProperty1());
        assertNull("Parent nested bean expression still in expression graph", testBean.getExpressionGraph().get(
                "reference1.reference1.property1"));
    }

    /**
     * Tests merging of maps when the map entries contain expressions
     *
     * @throws Exception
     */
    @Test
    public void testMergingOfMapExpressions() throws Exception {
        TestDictionaryBean testBean = (TestDictionaryBean) getTestDictionaryObject("testExpressionMapMerging2");
        assertNotNull("No bean exists with id: testExpressionMapMerging2", testBean);

        assertTrue("Merged map is not correct size (2)", testBean.getMap1().size() == 2);
        assertTrue("Merged map does not contain key2", testBean.getMap1().containsKey("key2"));
        assertTrue("Merged map does not contain key3)", testBean.getMap1().containsKey("key3"));

        assertTrue("Expression count not correct for merged map", testBean.getExpressionGraph().size() == 2);
        assertEquals("Bean does not contain expression for property key1", "@{expr1}",
                testBean.getExpressionGraph().get("map1['key1']"));
        assertEquals("Bean does not contain expression for property key4", "@{expr4}",
                testBean.getExpressionGraph().get("map1['key4']"));
    }

    /**
     * Tests non merging of maps when the map entries contain expressions
     *
     * @throws Exception
     */
    @Test
    public void testNonMergingOfMapExpressions() throws Exception {
        TestDictionaryBean testBean = (TestDictionaryBean) getTestDictionaryObject("testExpressionMapNonMerging");
        assertNotNull("No bean exists with id: testExpressionMapNonMerging", testBean);

        assertTrue("Non-Merged map is not correct size (1)", testBean.getMap1().size() == 1);
        assertTrue("Non-Merged map does not contain key3)", testBean.getMap1().containsKey("key3"));

        assertTrue("Expression count not correct for non-merged map", testBean.getExpressionGraph().size() == 1);
        assertEquals("Bean does not contain expression for property key4", "@{expr4}",
                testBean.getExpressionGraph().get("map1['key4']"));
    }

    /**
     * Tests merging of maps where the child bean is nested within a list
     *
     * TODO: this test is currently failing due to spring support of nested map merging
     *
     * @throws Exception
     */
    public void testNestedListExpressions() throws Exception {
        TestDictionaryBean testBean = (TestDictionaryBean) getTestDictionaryObject("testListBeanExpressionMerging");
        assertNotNull("No bean exists with id: testListBeanExpressionMerging", testBean);

        Map<String, String> mergedMap = testBean.getListReference1().get(0).getReference1().getMap1();

        assertTrue("Merged map is not correct size (2)", mergedMap.size() == 2);
        assertTrue("Merged map does not contain key2", mergedMap.containsKey("key2"));
        assertTrue("Merged map does not contain key3)", mergedMap.containsKey("key3"));

        TestDictionaryBean rootListBean = testBean.getListReference1().get(0);

        assertTrue("Expression count not correct for merged map", rootListBean.getExpressionGraph().size() == 2);
        assertEquals("Bean does not contain expression for property key1", "@{expr1}",
                rootListBean.getExpressionGraph().get("reference1.map1['key1']"));
        assertEquals("Bean does not contain expression for property key1", "@{expr4}",
                rootListBean.getExpressionGraph().get("reference1.map1['key4']"));
    }

    /**
     * Tests list property types with expressions, including non-inheritance, inheritance with and without merging
     *
     * @throws Exception
     */
    @Test
    public void testListExpressions() throws Exception {
        // test expressions with no inheritance
        TestDictionaryBean testBean = (TestDictionaryBean) getTestDictionaryObject("testListExpressionMerging");
        assertNotNull("No bean exists with id: testListExpressionMerging", testBean);

        List<String> list1 = testBean.getList1();
        assertTrue("List with expressions is not correct size", list1.size() == 6);
        assertEquals("Second value in list is not correct", "val1", list1.get(0));
        assertEquals("Fifth value in list is not correct", "val5", list1.get(4));

        assertTrue("Expression graph for inheritance list not correct size", testBean.getExpressionGraph().size() == 4);
        assertEquals("First expression in expression graph not correct", "@{expr2} before val",
                testBean.getExpressionGraph().get("list1[1]"));
        assertEquals("Second expression in expression graph not correct", "@{expr3}", testBean.getExpressionGraph().get(
                "list1[2]"));
        assertEquals("Third expression in expression graph not correct", "@{expr4}", testBean.getExpressionGraph().get(
                "list1[3]"));
        assertEquals("Fourth expression in expression graph not correct", "@{expr6}", testBean.getExpressionGraph().get(
                "list1[5]"));
    }

    /**
     * TODO: this test is currently failing due to spring support of nested merging
     *
     * @throws Exception
     */
    public void testNestedListMerging() throws Exception {
        TestDictionaryBean testBean = (TestDictionaryBean) getTestDictionaryObject("testListMerging2");
        assertNotNull("No bean exists with id: testListMerging2", testBean);

        List<String> list1 = testBean.getReference1().getList1();
        assertTrue("List with expressions is not correct size", list1.size() == 4);
        assertEquals("First value in list not correct", "val1", list1.get(0));
        assertEquals("Second value in list not correct", "val2", list1.get(0));
        assertEquals("Third value in list not correct", "val3", list1.get(0));
        assertEquals("Fourth value in list not correct", "val4", list1.get(0));
    }

    /**
     * Tests the postProcessBeanFactory method using beans with simple inheritance
     *
     * @throws Exception
     */
    @Test
    public void testPostProcessBeanFactoryWithSimpleInheritanceSucceeds() throws Exception {
        TestDictionaryBean simpleBean1 = (TestDictionaryBean) getTestDictionaryObject("testSimpleBean1");
        TestDictionaryBean simpleBean2 = (TestDictionaryBean) getTestDictionaryObject("testSimpleBean2");

        assertEquals("Bean does not have the correct property3 value", simpleBean1.getExpressionGraph().get(
                "property3"), "@{1 eq 1}");
        assertNull("Bean should not have a property3 value", simpleBean2.getExpressionGraph().get("property3"));
    }

    /**
     * Tests the postProcessBeanFactory method using beans with inheritance and nested properties
     *
     * @throws Exception
     */
    @Test
    public void testPostProcessBeanFactoryWithSimpleNestingSucceeds() throws Exception {
        TestDictionaryBean simpleBean1 = (TestDictionaryBean) getTestDictionaryObject("testSimpleBean1");
        TestDictionaryBean simpleBean4 = (TestDictionaryBean) getTestDictionaryObject("testSimpleBean4");

        assertEquals("Bean does not have the correct property3 value", simpleBean1.getExpressionGraph().get(
                "property3"), "@{1 eq 1}");
        assertNull("Bean should not have a property3 value", simpleBean4.getExpressionGraph().get("property3"));
    }

    /**
     * Tests that nested bean definitions are processed ok since the bean name is derived from the class attribute
     * and bean names that exist in the processed beans map are skipped
     */
    @Test
    public void testBeanDefinitionNaming() {
        TestDictionaryBean testListBeanDefinitionNaming = (TestDictionaryBean) getTestDictionaryObject(
                "testListBeanDefinitionNaming");

        TestDictionaryBean uifTestBeanObject = testListBeanDefinitionNaming.getListReference1().get(0);
        assertTrue("expression graph should have a property1", uifTestBeanObject.getExpressionGraph().containsKey(
                "property1"));

        TestDictionaryBean uifTestBeanObject1 = testListBeanDefinitionNaming.getListReference1().get(1);
        assertTrue("expression graph should have a property1", uifTestBeanObject1.getExpressionGraph().containsKey(
                "property1"));
    }

}
