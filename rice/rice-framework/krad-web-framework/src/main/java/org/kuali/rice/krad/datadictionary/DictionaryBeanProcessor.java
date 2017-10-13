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
package org.kuali.rice.krad.datadictionary;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * API for classes that perform post processing of the dictionary bean definitions
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DictionaryBeanProcessor {

    /**
     * Invoked to process a root bean definition (a root bean definition is a top level bean)
     *
     * @param beanName name of the bean within the factory
     * @param beanDefinition bean definition to process
     */
    public void processRootBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * Invoked to process a nested bean definition (a bean definition that is a property value of another
     * bean definition)
     *
     * @param beanName name of the bean within the factory
     * @param beanDefinition bean definition to process
     * @param propertyName the name of the property which has the bean definition value
     * @param nestedBeanStack the stack of beans which contain the given bean
     */
    public void processNestedBeanDefinition(String beanName, BeanDefinition beanDefinition, String propertyName,
            Stack<BeanDefinitionHolder> nestedBeanStack);

    /**
     * Invoked to process a string property value (straight property value, not a string within a collection)
     *
     * @param propertyName name of the property whose string value is being processed
     * @param propertyValue string value for the property
     * @return String new property value (possibly modified)
     */
    public String processStringPropertyValue(String propertyName, String propertyValue,
            Stack<BeanDefinitionHolder> nestedBeanStack);

    /**
     * Invoked to process a collection value that is a bean definition
     *
     * @param beanName name of the bean within the factory
     * @param beanDefinition bean definition within the collection to process
     * @param propertyName the name of the property which has the collection value
     * @param nestedBeanStack the stack of beans which contain the given collection (and collection bean)
     */
    public void processCollectionBeanDefinition(String beanName, BeanDefinition beanDefinition, String propertyName,
            Stack<BeanDefinitionHolder> nestedBeanStack);

    /**
     * Invokes the processors to handle an array string value (which may be changed)
     *
     * @param propertyName name of the property that is being processed
     * @param propertyValue the array which contains the string
     * @param elementValue the string element value
     * @param elementIndex the index of the string within the array
     * @param nestedBeanStack the stack of bean containers, including the bean that contains the property
     * @return String new property value (possibly modified by processors)
     */
    public String processArrayStringPropertyValue(String propertyName, Object[] propertyValue, String elementValue,
            int elementIndex, Stack<BeanDefinitionHolder> nestedBeanStack);

    /**
     * Invokes the processors to handle an list string value (which may be changed)
     *
     * @param propertyName name of the property that is being processed
     * @param propertyValue the list which contains the string
     * @param elementValue the string element value
     * @param elementIndex the index of the string within the list
     * @param nestedBeanStack the stack of bean containers, including the bean that contains the property
     * @return String new property value (possibly modified by processors)
     */
    public String processListStringPropertyValue(String propertyName, List<?> propertyValue, String elementValue,
            int elementIndex, Stack<BeanDefinitionHolder> nestedBeanStack);

    /**
     * Invokes the processors to handle an set string value (which may be changed)
     *
     * @param propertyName name of the property that is being processed
     * @param propertyValue the set which contains the string
     * @param elementValue the string element value
     * @param nestedBeanStack the stack of bean containers, including the bean that contains the property
     * @return String new property value (possibly modified by processors)
     */
    public String processSetStringPropertyValue(String propertyName, Set<?> propertyValue, String elementValue,
            Stack<BeanDefinitionHolder> nestedBeanStack);

    /**
     * Invokes the processors to handle an map string value (which may be changed)
     *
     * @param propertyName name of the property that is being processed
     * @param propertyValue the map which contains the string
     * @param elementValue the string element value
     * @param elementKey the key for the string within the map
     * @param nestedBeanStack the stack of bean containers, including the bean that contains the property
     * @return String new property value (possibly modified by processors)
     */
    public String processMapStringPropertyValue(String propertyName, Map<?, ?> propertyValue, String elementValue,
            Object elementKey, Stack<BeanDefinitionHolder> nestedBeanStack);
}
