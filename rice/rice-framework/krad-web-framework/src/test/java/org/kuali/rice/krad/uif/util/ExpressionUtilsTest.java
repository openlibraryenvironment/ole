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
package org.kuali.rice.krad.uif.util;

import org.junit.Test;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 * Test class for {@link ExpressionUtils}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExpressionUtilsTest {

    /**
     * Tests the moving of expressions from the configurable expression graph to the property expressions map
     *
     * <p>
     * Tests include nesting up to two levels and map properties
     * </p>
     */
    @Test
    public void testPopulatePropertyExpressionsFromGraph() {
        Map<String, String> expressionGraph = new HashMap<String, String>();

        expressionGraph.put("property1", "@{expr1}");
        expressionGraph.put("property2", "@{expr2}");
        expressionGraph.put("property3['key1']", "@{key1expr}");
        expressionGraph.put("property4.property1", "@{nexpr1}");
        expressionGraph.put("property4.property4.property3['key2']", "@{nkey2expr}");

        MockConfigurable configurable = new MockConfigurable();
        MockConfigurable configurable2 = new MockConfigurable();
        MockConfigurable configurable3 = new MockConfigurable();

        configurable2.setProperty4(configurable3);
        configurable.setProperty4(configurable2);

        configurable.setExpressionGraph(expressionGraph);

        ExpressionUtils.populatePropertyExpressionsFromGraph(configurable, false);

        assertEquals("Expression count not correct in root configurable", 3,
                configurable.getPropertyExpressions().size());
        assertEquals("Expression not correct for property1", "@{expr1}", configurable.getPropertyExpression(
                "property1"));
        assertEquals("Expression not correct for property2", "@{expr2}", configurable.getPropertyExpression(
                "property2"));
        assertEquals("Expression not correct for map property3", "@{key1expr}", configurable.getPropertyExpression(
                "property3['key1']"));

        assertEquals("Expression count not correct in nested configurable", 1,
                configurable.getProperty4().getPropertyExpressions().size());
        assertEquals("Expression not correct for nested property1", "@{nexpr1}",
                configurable.getProperty4().getPropertyExpression("property1"));

        assertEquals("Expression count not correct in two level nested configurable", 1,
                configurable.getProperty4().getProperty4().getPropertyExpressions().size());
        assertEquals("Expression not correct for nested map property3", "@{nkey2expr}",
                configurable.getProperty4().getProperty4().getPropertyExpression("property3['key2']"));
    }

    /**
     * Test the population of refresh expression graphs by the expression utility method
     */
    @Test
    public void testPopulatePropertyExpressionsFromGraph_RefreshGraphs() {
        Map<String, String> expressionGraph = new HashMap<String, String>();

        expressionGraph.put("property1", "@{expr1}");
        expressionGraph.put("property2", "@{expr2}");
        expressionGraph.put("property3['key1']", "@{key1expr}");
        expressionGraph.put("property4.property1", "@{nexpr1}");
        expressionGraph.put("property4.property4.property3['key2']", "@{nkey2expr}");

        MockConfigurable configurable = new MockConfigurable();
        MockConfigurable configurable2 = new MockConfigurable();
        MockConfigurable configurable3 = new MockConfigurable();

        configurable2.setProperty4(configurable3);
        configurable.setProperty4(configurable2);

        configurable.setExpressionGraph(expressionGraph);

        ExpressionUtils.populatePropertyExpressionsFromGraph(configurable, true);

        assertEquals("Refresh expression count not correct in root configurable", 5,
                configurable.getRefreshExpressionGraph().size());
        assertEquals("Refresh expression count not correct in nested configurable", 2,
                configurable.getProperty4().getRefreshExpressionGraph().size());
        assertEquals("Refresh expression count not correct in two level nested configurable", 1,
                configurable.getProperty4().getProperty4().getRefreshExpressionGraph().size());
    }

    /**
     * Mock class used to test expression handling
     */
    public class MockConfigurable extends UifDictionaryBeanBase {
        private String property1;
        private String property2;

        private Map<String, String> property3;
        private MockConfigurable property4;

        public MockConfigurable() {
            property3 = new HashMap<String, String>();
        }

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public String getProperty2() {
            return property2;
        }

        public void setProperty2(String property2) {
            this.property2 = property2;
        }

        public Map<String, String> getProperty3() {
            return property3;
        }

        public void setProperty3(Map<String, String> property3) {
            this.property3 = property3;
        }

        public MockConfigurable getProperty4() {
            return property4;
        }

        public void setProperty4(MockConfigurable property4) {
            this.property4 = property4;
        }
    }
}
