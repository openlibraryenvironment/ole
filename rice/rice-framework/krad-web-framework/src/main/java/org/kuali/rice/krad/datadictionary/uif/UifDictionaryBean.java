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
package org.kuali.rice.krad.datadictionary.uif;

import org.kuali.rice.krad.datadictionary.DictionaryBean;

import java.util.Map;

/**
 * Marks any class that can be configured through the UIF dictionary
 *
 * <p>
 * Indicates behavior that must be supported by an Class that can be configured through
 * the UIF dictionary, such as property expressions.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface UifDictionaryBean extends DictionaryBean {

    /**
     * Map of expressions that were configured for the object's graph
     *
     * <p>
     * During processing of the UIF configuration, expressions are pulled out and placed into this map for the
     * component graph. A component graph contains the component and all one to one nested components (but not those
     * that are contained in collections, each of these begins another graph). The expressions are placed at the root
     * component level instead of the actual nested component for handling of nested property configuration and
     * overridding
     * </p>
     *
     * <p>
     * The expression graph map key gives the property name (possibly nested) the expression was configured on, and the
     * map value gives the expression. During the view lifecycle,
     * see {@link org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl#performComponentApplyModel(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.component.Component, java.lang.Object)},
     * the expressions are moved to the {@link #getPropertyExpressions()} map for the configurable they should be
     * evaluated on
     * </p>
     *
     * @return Map<String, String> map of expressions contained on the configurable graph
     */
    public Map<String, String> getExpressionGraph();

    /**
     * Setter for the map of expressions contained on the configurable graph
     *
     * @param expressionGraph
     */
    public void setExpressionGraph(Map<String, String> expressionGraph);

    /**
     * Map of expressions that should apply when the component is refresh
     *
     * <p>
     * Expressions may exist on a parent component that impact the component state when it is being refreshed. These
     * expressions are pulled out and placed into this map for evaluation during the refresh process
     * </p>
     *
     * @return Map<String, String> key is property name to set and value is the expression to evaluate
     */
    public Map<String, String> getRefreshExpressionGraph();

    /**
     * Setter for the component's refresh expression graph
     *
     * @param refreshExpressionGraph
     */
    public void setRefreshExpressionGraph(Map<String, String> refreshExpressionGraph);

    /**
     * Map of expressions that should be evaluated to conditionally set a property on the component
     *
     * <p>
     * When configuring a component property through XML an expression can be given using the @{} placeholder. During
     * the loading of the XML any such expressions are captured and placed into this Map, with the property they apply
     * to set as the Map key. The expressions are then evaluated during the apply model phase and the result is set as
     * the property value.
     * </p>
     *
     * <p>
     * Note after the expression is picked up, the property configuration is removed. Thus the property in the
     * component will only have its default object value until the expression is evaluated
     * </p>
     *
     * @return Map<String, String> map of expressions where key is property name and value is expression to evaluate
     */
    public Map<String, String> getPropertyExpressions();

    /**
     * Setter for the Map of property expressions
     *
     * @param propertyExpressions
     */
    public void setPropertyExpressions(Map<String, String> propertyExpressions);

    /**
     * Returns the expression configured for the property with the given name
     *
     * @return String expression for property or null if expression is not configured
     * @see org.kuali.rice.krad.uif.component.Component#getPropertyExpressions()
     */
    public String getPropertyExpression(String propertyName);
}
