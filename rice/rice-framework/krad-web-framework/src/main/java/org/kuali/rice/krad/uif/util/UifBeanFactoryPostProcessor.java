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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedArray;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Post processes the bean factory to handle UIF property expressions and IDs on inner beans
 *
 * <p>
 * Conditional logic can be implemented with the UIF dictionary by means of property expressions. These are
 * expressions that follow SPEL and can be given as the value for a property using the @{} placeholder. Since such
 * a value would cause an exception when creating the object if the property is a non-string type (value cannot be
 * converted), we need to move those expressions to a Map for processing, and then remove the original property
 * configuration containing the expression. The expressions are then evaluated during the view apply model phase and
 * the result is set as the value for the corresponding property.
 * </p>
 *
 * <p>
 * Spring will not register inner beans with IDs so that the bean definition can be retrieved through the factory,
 * therefore this post processor adds them as top level registered beans
 * </p>
 *
 * TODO: convert to dictionary bean processor
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private static final Log LOG = LogFactory.getLog(UifBeanFactoryPostProcessor.class);

    /**
     * Iterates through all beans in the factory and invokes processing
     *
     * @param beanFactory bean factory instance to process
     * @throws org.springframework.beans.BeansException
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Set<String> processedBeanNames = new HashSet<String>();

        LOG.info("Beginning post processing of bean factory for UIF expressions");

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            String beanName = beanNames[i];
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

            processBeanDefinition(beanName, beanDefinition, beanFactory, processedBeanNames);
        }

        LOG.info("Finished post processing of bean factory for UIF expressions");
    }

    /**
     * Processes a top level (non nested) bean definition for expressions
     *
     * <p>
     * A bean that is non nested (or top of a collection) will hold all the expressions for the graph. A new
     * expression graph is initialized and expressions are collected as the bean and all its children are processed.
     * The expression graph is then set as a property on the top bean definition
     * </p>
     *
     * @param beanName name of the bean to process
     * @param beanDefinition bean definition to process
     * @param beanFactory factory holding all the bean definitions
     * @param processedBeanNames set of bean names that have already been processed
     */
    protected void processBeanDefinition(String beanName, BeanDefinition beanDefinition,
            ConfigurableListableBeanFactory beanFactory, Set<String> processedBeanNames) {
        Class<?> beanClass = getBeanClass(beanDefinition, beanFactory);
        if ((beanClass == null) || !UifDictionaryBean.class.isAssignableFrom(beanClass) || processedBeanNames.contains(
                beanName)) {
              return;
        }

        // process bean definition and all nested definitions for expressions
        ManagedMap<String, String> expressionGraph = new ManagedMap<String, String>();
        MutablePropertyValues pvs = beanDefinition.getPropertyValues();
        if (pvs.contains(UifPropertyPaths.EXPRESSION_GRAPH)) {
            expressionGraph = (ManagedMap<String, String>) pvs.getPropertyValue(UifPropertyPaths.EXPRESSION_GRAPH)
                    .getValue();
            if (expressionGraph == null) {
                expressionGraph = new ManagedMap<String, String>();
            }
        }

        expressionGraph.setMergeEnabled(false);
        processNestedBeanDefinition(beanName, beanDefinition, "", expressionGraph, beanFactory, processedBeanNames);

        // add property for expression graph
        pvs = beanDefinition.getPropertyValues();
        pvs.addPropertyValue(UifPropertyPaths.EXPRESSION_GRAPH, expressionGraph);
    }

    /**
     * If the bean class is type UifDictionaryBean, iterate through configured property values
     * and check for expressions
     *
     * @param beanName name of the bean in the factory (only set for top level beans, not nested)
     * @param beanDefinition bean definition to process for expressions
     * @param nestedPropertyName
     * @param expressionGraph
     * @param beanFactory bean factory being processed
     * @param processedBeanNames
     */
    protected void processNestedBeanDefinition(String beanName, BeanDefinition beanDefinition,
            String nestedPropertyName, Map<String, String> expressionGraph,
            ConfigurableListableBeanFactory beanFactory, Set<String> processedBeanNames) {
        Class<?> beanClass = getBeanClass(beanDefinition, beanFactory);
        if ((beanClass == null) || !UifDictionaryBean.class.isAssignableFrom(beanClass) || processedBeanNames.contains(
                beanName)) {
            return;
        }

        LOG.debug("Processing bean name '" + beanName + "'");

        Map<String, String> parentExpressionGraph = getExpressionGraphFromParent(beanDefinition.getParentName(),
                beanFactory, processedBeanNames);

        // process expressions on property values
        MutablePropertyValues pvs = beanDefinition.getPropertyValues();
        PropertyValue[] pvArray = pvs.getPropertyValues();
        for (PropertyValue pv : pvArray) {
            if (pv.getName().equals(UifPropertyPaths.EXPRESSION_GRAPH)) {
                continue;
            }

            String propertyPath = pv.getName();
            if (StringUtils.isNotBlank(nestedPropertyName)) {
                propertyPath = nestedPropertyName + "." + propertyPath;
            }

            // for reloading, need to remove the property from the previously loaded bean definition
            if (expressionGraph.containsKey(propertyPath)) {
                expressionGraph.remove(propertyPath);
            }

            if (hasExpression(pv.getValue())) {
                // process expression
                String strValue = getStringValue(pv.getValue());
                expressionGraph.put(propertyPath, strValue);

                // remove property value so expression will not cause binding exception
                pvs.removePropertyValue(pv.getName());
            } else {
                // process nested objects
                Object newValue = processPropertyValue(propertyPath, pv.getName(), pv.getValue(), beanDefinition,
                        parentExpressionGraph, expressionGraph, beanFactory, processedBeanNames);

                pvs.removePropertyValue(pv.getName());
                pvs.addPropertyValue(pv.getName(), newValue);
            }

            // removed expression (if exists) from parent map since the property was set on child
            if (parentExpressionGraph.containsKey(pv.getName())) {
                parentExpressionGraph.remove(pv.getName());
            }
        }

        // if nested bean set expression graph to null so it is not inherited from parent definition
        if (StringUtils.isNotBlank(nestedPropertyName)) {
            pvs.addPropertyValue(UifPropertyPaths.EXPRESSION_GRAPH, null);
        }

        // add remaining expressions from parent to expression graph
        for (Map.Entry<String, String> parentExpression : parentExpressionGraph.entrySet()) {
            String expressionPath = parentExpression.getKey();
            if (StringUtils.isNotBlank(nestedPropertyName)) {
                expressionPath = nestedPropertyName + "." + expressionPath;
            }

            if (!expressionGraph.containsKey(expressionPath)) {
                expressionGraph.put(expressionPath, parentExpression.getValue());
            }
        }

        // if bean name is given and factory does not have it registered we need to add it (inner beans that
        // were given an id)
        if (StringUtils.isNotBlank(beanName) && !StringUtils.contains(beanName, "$") && !StringUtils.contains(beanName,
                "#") && !beanFactory.containsBean(beanName)) {
            ((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(beanName, beanDefinition);
        }

        if (StringUtils.isNotBlank(beanName)) {
            processedBeanNames.add(beanName);
        }
    }

    /**
     * Retrieves the class for the object that will be created from the bean definition. Since the class might not
     * be configured on the bean definition, but by a parent, each parent bean definition is recursively checked for
     * a class until one is found
     *
     * @param beanDefinition bean definition to get class for
     * @param beanFactory bean factory that contains the bean definition
     * @return class configured for the bean definition, or null
     */
    protected Class<?> getBeanClass(BeanDefinition beanDefinition, ConfigurableListableBeanFactory beanFactory) {
        if (StringUtils.isNotBlank(beanDefinition.getBeanClassName())) {
            try {
                return Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                // swallow exception and return null so bean is not processed
                return null;
            }
        } else if (StringUtils.isNotBlank(beanDefinition.getParentName())) {
            BeanDefinition parentBeanDefinition = beanFactory.getBeanDefinition(beanDefinition.getParentName());
            if (parentBeanDefinition != null) {
                return getBeanClass(parentBeanDefinition, beanFactory);
            }
        }

        return null;
    }

    /**
     * Retrieves the expression graph map set on the bean with given name. If the bean has not been processed
     * by the bean factory post processor, that is done before retrieving the map
     *
     * @param parentBeanName name of the parent bean to retrieve map for (if empty a new map will be returned)
     * @param beanFactory bean factory to retrieve bean definition from
     * @param processedBeanNames set of bean names that have been processed so far
     * @return expression graph map from parent or new instance
     */
    protected Map<String, String> getExpressionGraphFromParent(String parentBeanName,
            ConfigurableListableBeanFactory beanFactory, Set<String> processedBeanNames) {
        Map<String, String> expressionGraph = new HashMap<String, String>();
        if (StringUtils.isBlank(parentBeanName) || !beanFactory.containsBeanDefinition(parentBeanName)) {
            return expressionGraph;
        }

        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(parentBeanName);
        if (!processedBeanNames.contains(parentBeanName)) {
            processBeanDefinition(parentBeanName, beanDefinition, beanFactory, processedBeanNames);
        }

        MutablePropertyValues pvs = beanDefinition.getPropertyValues();
        PropertyValue propertyExpressionsPV = pvs.getPropertyValue(UifPropertyPaths.EXPRESSION_GRAPH);
        if (propertyExpressionsPV != null) {
            Object value = propertyExpressionsPV.getValue();
            if ((value != null) && (value instanceof ManagedMap)) {
                expressionGraph.putAll((ManagedMap) value);
            }
        }

        return expressionGraph;
    }

    /**
     * Checks whether the given property value is of String type, and if so whether it contains the expression
     * placholder(s)
     *
     * @param propertyValue value to check for expressions
     * @return true if the property value contains expression(s), false if it does not
     */
    protected boolean hasExpression(Object propertyValue) {
        if (propertyValue != null) {
            // if value is string, check for el expression
            String strValue = getStringValue(propertyValue);
            if (strValue != null) {
                String elPlaceholder = StringUtils.substringBetween(strValue, UifConstants.EL_PLACEHOLDER_PREFIX,
                        UifConstants.EL_PLACEHOLDER_SUFFIX);
                if (StringUtils.isNotBlank(elPlaceholder)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Processes the given property name/value pair for complex objects, such as bean definitions or collections,
     * which if found will be processed for contained property expression values
     *
     * @param nestedPropertyName nested path of the property whose value is being processed
     * @param propertyName name of the property in the bean definition being processed
     * @param propertyValue value to check
     * @param beanDefinition bean definition the property belongs to
     * @param parentExpressionGraph map that holds property expressions for the parent bean definition, used for
     * merging
     * @param expressionGraph map that holds property expressions for the bean definition being processed
     * @param beanFactory bean factory that contains the bean definition being processed
     * @param processedBeanNames set of bean names that have been processed so far
     * @return new value to set for property
     */
    protected Object processPropertyValue(String nestedPropertyName, String propertyName, Object propertyValue,
            BeanDefinition beanDefinition, Map<String, String> parentExpressionGraph,
            Map<String, String> expressionGraph, ConfigurableListableBeanFactory beanFactory,
            Set<String> processedBeanNames) {
        boolean clearExpressionsForNull = false;
        if (propertyValue instanceof TypedStringValue) {
            TypedStringValue typedStringValue = (TypedStringValue) propertyValue;

            String value = typedStringValue.getValue();
            if (value == null) {
                clearExpressionsForNull = true;
            }
        } else if (propertyValue == null) {
            clearExpressionsForNull = true;
        }

        // if property is object and set to null, clear any parent expressions for the property
        if (clearExpressionsForNull) {
            removeExpressionsByPrefix(nestedPropertyName, expressionGraph);
            removeExpressionsByPrefix(propertyName, parentExpressionGraph);

            return propertyValue;
        }

        // process nested bean definitions
        if ((propertyValue instanceof BeanDefinition) || (propertyValue instanceof BeanDefinitionHolder)) {
            String beanName = null;
            BeanDefinition beanDefinitionValue;
            if (propertyValue instanceof BeanDefinition) {
                beanDefinitionValue = (BeanDefinition) propertyValue;
            } else {
                beanDefinitionValue = ((BeanDefinitionHolder) propertyValue).getBeanDefinition();
                beanName = ((BeanDefinitionHolder) propertyValue).getBeanName();
            }

            // since overriding the entire bean, clear any expressions from parent that start with the bean property
            removeExpressionsByPrefix(nestedPropertyName, expressionGraph);
            removeExpressionsByPrefix(propertyName, parentExpressionGraph);

            processNestedBeanDefinition(beanName, beanDefinitionValue, nestedPropertyName, expressionGraph, beanFactory,
                    processedBeanNames);

            return propertyValue;
        }

        // recurse into collections
        if (propertyValue instanceof Object[]) {
            visitArray(nestedPropertyName, parentExpressionGraph, expressionGraph, (Object[]) propertyValue, beanFactory,
                    processedBeanNames);
        } else if (propertyValue instanceof List) {
            visitList(nestedPropertyName, propertyName, beanDefinition, parentExpressionGraph, expressionGraph,
                    (List) propertyValue, beanFactory, processedBeanNames);
        } else if (propertyValue instanceof Set) {
            visitSet(nestedPropertyName, parentExpressionGraph, expressionGraph, (Set) propertyValue, beanFactory,
                    processedBeanNames);
        } else if (propertyValue instanceof Map) {
            visitMap(nestedPropertyName, parentExpressionGraph, expressionGraph, (Map) propertyValue, beanFactory,
                    processedBeanNames);
        }

        // others (primitive) just return value as is
        return propertyValue;
    }

    /**
     * Removes entries from the given expressions map whose key starts with the given prefix
     *
     * @param propertyNamePrefix prefix to search for and remove
     * @param expressionGraph map of property expressions to filter
     */
    protected void removeExpressionsByPrefix(String propertyNamePrefix, Map<String, String> expressionGraph) {
        Map<String, String> adjustedExpressionGraph = new HashMap<String, String>();
        for (String propertyName : expressionGraph.keySet()) {
            if (!propertyName.startsWith(propertyNamePrefix + ".")) {
                adjustedExpressionGraph.put(propertyName, expressionGraph.get(propertyName));
            }
        }

        expressionGraph.clear();
        expressionGraph.putAll(adjustedExpressionGraph);
    }

    /**
     * Determines whether the given value is of String type and if so returns the string value
     *
     * @param value object value to check
     * @return string value for object or null if object is not a string type
     */
    protected String getStringValue(Object value) {
        if (value instanceof TypedStringValue) {
            TypedStringValue typedStringValue = (TypedStringValue) value;
            return typedStringValue.getValue();
        } else if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected void visitArray(String propertyName, Map<String, String> parentExpressionGraph,
            Map<String, String> expressionGraph, Object array, ConfigurableListableBeanFactory beanFactory,
            Set<String> processedBeanNames) {
        Object newArray = null;
        Object[] arrayVal = null;

        boolean isMergeEnabled = false;
        if (array instanceof ManagedArray) {
            isMergeEnabled = ((ManagedArray) array).isMergeEnabled();
            arrayVal = (Object[]) ((ManagedArray) array).getSource();

            newArray = new ManagedArray(((ManagedArray) array).getElementTypeName(), arrayVal.length);
            ((ManagedArray) newArray).setMergeEnabled(isMergeEnabled);
        } else {
            arrayVal = (Object[]) array;
            newArray = new Object[arrayVal.length];
        }

        for (int i = 0; i < arrayVal.length; i++) {
            Object elem = arrayVal[i];
            String elemPropertyName = propertyName + "[" + i + "]";

            if (hasExpression(elem)) {
                String strValue = getStringValue(elem);
                expressionGraph.put(elemPropertyName, strValue);
                arrayVal[i] = null;
            } else {
                // process set value bean definition as a top level bean
                if ((elem instanceof BeanDefinition) || (elem instanceof BeanDefinitionHolder)) {
                    String beanName = null;
                    BeanDefinition beanDefinition;
                    if (elem instanceof BeanDefinition) {
                        beanDefinition = (BeanDefinition) elem;
                    } else {
                        beanDefinition = ((BeanDefinitionHolder) elem).getBeanDefinition();
                        beanName = ((BeanDefinitionHolder) elem).getBeanName();
                    }

                    processBeanDefinition(beanName, beanDefinition, beanFactory, processedBeanNames);
                }

                arrayVal[i] = elem;
            }

            if (isMergeEnabled && parentExpressionGraph.containsKey(elemPropertyName)) {
                parentExpressionGraph.remove(elemPropertyName);
            }
        }

        // determine if we need to clear any parent expressions for this list
        if (!isMergeEnabled) {
            // clear any expressions that match the property name minus index
            Map<String, String> adjustedParentExpressionGraph = new HashMap<String, String>();
            for (Map.Entry<String, String> parentExpression : parentExpressionGraph.entrySet()) {
                if (!parentExpression.getKey().startsWith(propertyName + "[")) {
                    adjustedParentExpressionGraph.put(parentExpression.getKey(), parentExpression.getValue());
                }
            }

            parentExpressionGraph.clear();
            parentExpressionGraph.putAll(adjustedParentExpressionGraph);
        }

        if (array instanceof ManagedArray) {
            ((ManagedArray) array).setSource(newArray);
        } else {
            array = newArray;
        }
    }

    @SuppressWarnings("unchecked")
    protected void visitList(String nestedPropertyName, String propertyName, BeanDefinition beanDefinition,
            Map<String, String> parentExpressionGraph, Map<String, String> expressionGraph, List listVal,
            ConfigurableListableBeanFactory beanFactory, Set<String> processedBeanNames) {
        boolean isMergeEnabled = false;
        if (listVal instanceof ManagedList) {
            isMergeEnabled = ((ManagedList) listVal).isMergeEnabled();
        }

        ManagedList newList = new ManagedList();
        newList.setMergeEnabled(isMergeEnabled);

        // if merging, need to find size of parent list so we can know which element to set
        // when evaluating expressions
        int parentListSize = 0;
        if (isMergeEnabled && StringUtils.isNotBlank(beanDefinition.getParentName())) {
            BeanDefinition parentBeanDefinition = beanFactory.getMergedBeanDefinition(beanDefinition.getParentName());
            PropertyValue parentListPropertyValue = parentBeanDefinition.getPropertyValues().getPropertyValue(
                    propertyName);
            if (parentListPropertyValue != null) {
                List parentList = (List) parentListPropertyValue.getValue();
                parentListSize = parentList.size();
            }
        }

        for (int i = 0; i < listVal.size(); i++) {
            Object elem = listVal.get(i);

            int elementPosition = i + parentListSize;
            String elemPropertyName = nestedPropertyName + "[" + elementPosition + "]";

            if (hasExpression(elem)) {
                String strValue = getStringValue(elem);

                expressionGraph.put(elemPropertyName, strValue);
                newList.add(i, null);
            } else {
                // process list value bean definition as a top level bean
                if ((elem instanceof BeanDefinition) || (elem instanceof BeanDefinitionHolder)) {
                    String beanName = null;
                    BeanDefinition beanDefinitionValue;
                    if (elem instanceof BeanDefinition) {
                        beanDefinitionValue = (BeanDefinition) elem;
                    } else {
                        beanDefinitionValue = ((BeanDefinitionHolder) elem).getBeanDefinition();
                        beanName = ((BeanDefinitionHolder) elem).getBeanName();
                    }

                    processBeanDefinition(beanName, beanDefinitionValue, beanFactory, processedBeanNames);
                }

                newList.add(i, elem);
            }
        }

        // determine if we need to clear any parent expressions for this list
        if (!isMergeEnabled) {
            // clear any expressions that match the property name minus index
            Map<String, String> adjustedParentExpressionGraph = new HashMap<String, String>();
            for (Map.Entry<String, String> parentExpression : parentExpressionGraph.entrySet()) {
                if (!parentExpression.getKey().startsWith(nestedPropertyName + "[")) {
                    adjustedParentExpressionGraph.put(parentExpression.getKey(), parentExpression.getValue());
                }
            }

            parentExpressionGraph.clear();
            parentExpressionGraph.putAll(adjustedParentExpressionGraph);
        }

        listVal.clear();
        listVal.addAll(newList);
    }

    @SuppressWarnings("unchecked")
    protected void visitSet(String propertyName, Map<String, String> parentPropertyExpressions,
            Map<String, String> propertyExpressions, Set setVal, ConfigurableListableBeanFactory beanFactory,
            Set<String> processedBeanNames) {
        boolean isMergeEnabled = false;
        if (setVal instanceof ManagedSet) {
            isMergeEnabled = ((ManagedSet) setVal).isMergeEnabled();
        }

        ManagedSet newSet = new ManagedSet();
        newSet.setMergeEnabled(isMergeEnabled);

        for (Object elem : setVal) {
            if (hasExpression(elem)) {
                String strValue = getStringValue(elem);
                propertyExpressions.put(propertyName + ExpressionEvaluator.EMBEDDED_PROPERTY_NAME_ADD_INDICATOR,
                        strValue);
            } else {
                // process set value bean definition as a top level bean
                if ((elem instanceof BeanDefinition) || (elem instanceof BeanDefinitionHolder)) {
                    String beanName = null;
                    BeanDefinition beanDefinition;
                    if (elem instanceof BeanDefinition) {
                        beanDefinition = (BeanDefinition) elem;
                    } else {
                        beanDefinition = ((BeanDefinitionHolder) elem).getBeanDefinition();
                        beanName = ((BeanDefinitionHolder) elem).getBeanName();
                    }

                    processBeanDefinition(beanName, beanDefinition, beanFactory, processedBeanNames);
                }

                newSet.add(elem);
            }
        }

        // determine if we need to clear any parent expressions for this list
        if (!isMergeEnabled) {
            // clear any expressions that match the property name minus index
            Map<String, String> adjustedParentExpressions = new HashMap<String, String>();
            for (Map.Entry<String, String> parentExpression : parentPropertyExpressions.entrySet()) {
                if (!parentExpression.getKey().startsWith(
                        propertyName + ExpressionEvaluator.EMBEDDED_PROPERTY_NAME_ADD_INDICATOR)) {
                    adjustedParentExpressions.put(parentExpression.getKey(), parentExpression.getValue());
                }
            }

            parentPropertyExpressions.clear();
            parentPropertyExpressions.putAll(adjustedParentExpressions);
        }

        setVal.clear();
        setVal.addAll(newSet);
    }

    @SuppressWarnings("unchecked")
    protected void visitMap(String propertyName, Map<String, String> parentExpressionGraph,
            Map<String, String> expressionGraph, Map<?, ?> mapVal, ConfigurableListableBeanFactory beanFactory,
            Set<String> processedBeanNames) {
        boolean isMergeEnabled = false;
        if (mapVal instanceof ManagedMap) {
            isMergeEnabled = ((ManagedMap) mapVal).isMergeEnabled();
        }

        ManagedMap newMap = new ManagedMap();
        newMap.setMergeEnabled(isMergeEnabled);

        for (Map.Entry entry : mapVal.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();

            String keyStr = getStringValue(key);
            String elemPropertyName = propertyName + "['" + keyStr + "']";

            if (hasExpression(val)) {
                String strValue = getStringValue(val);
                expressionGraph.put(elemPropertyName, strValue);
            } else {
                // process map value bean definition as a top level bean
                if ((val instanceof BeanDefinition) || (val instanceof BeanDefinitionHolder)) {
                    String beanName = null;
                    BeanDefinition beanDefinition;
                    if (val instanceof BeanDefinition) {
                        beanDefinition = (BeanDefinition) val;
                    } else {
                        beanDefinition = ((BeanDefinitionHolder) val).getBeanDefinition();
                        beanName = ((BeanDefinitionHolder) val).getBeanName();
                    }

                    processBeanDefinition(beanName, beanDefinition, beanFactory, processedBeanNames);
                }

                newMap.put(key, val);
            }

            if (isMergeEnabled && parentExpressionGraph.containsKey(elemPropertyName)) {
                parentExpressionGraph.remove(elemPropertyName);
            }
        }

        if (!isMergeEnabled) {
            // clear any expressions that match the property minus key
            Map<String, String> adjustedParentExpressionGraph = new HashMap<String, String>();
            for (Map.Entry<String, String> parentExpression : parentExpressionGraph.entrySet()) {
                if (!parentExpression.getKey().startsWith(propertyName + "[")) {
                    adjustedParentExpressionGraph.put(parentExpression.getKey(), parentExpression.getValue());
                }
            }

            parentExpressionGraph.clear();
            parentExpressionGraph.putAll(adjustedParentExpressionGraph);
        }

        mapVal.clear();
        mapVal.putAll(newMap);
    }
}
