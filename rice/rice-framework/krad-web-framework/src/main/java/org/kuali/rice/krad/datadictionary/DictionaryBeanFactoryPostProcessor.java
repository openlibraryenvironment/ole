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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedArray;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Post processor for the data dictionary bean factory
 *
 * <p>
 * The 'driver' for other post processors. Essentially this iterates through each bean and its properties,
 * making calls to the message and expression processors
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DictionaryBeanFactoryPostProcessor {
    private static final Log LOG = LogFactory.getLog(DictionaryBeanFactoryPostProcessor.class);

    private DataDictionary dataDictionary;
    private ConfigurableListableBeanFactory beanFactory;

    private List<DictionaryBeanProcessor> beanProcessors;

    /**
     * Constructs a new processor for the given data dictionary and bean factory
     *
     * @param dataDictionary data dictionary instance that contains the bean factory
     * @param beanFactory bean factory to process
     */
    public DictionaryBeanFactoryPostProcessor(DataDictionary dataDictionary,
            ConfigurableListableBeanFactory beanFactory) {
        this.dataDictionary = dataDictionary;
        this.beanFactory = beanFactory;

        this.beanProcessors = new ArrayList<DictionaryBeanProcessor>();
        this.beanProcessors.add(new MessageBeanProcessor(dataDictionary, beanFactory));
    }

    /**
     * Iterates through all beans in the factory and invokes processing of root bean definitions
     *
     * @throws org.springframework.beans.BeansException
     */
    public void postProcessBeanFactory() throws BeansException {
        // check whether loading of external messages is enabled
        boolean loadExternalMessages = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsBoolean(
                "load.dictionary.external.messages");
        if (!loadExternalMessages) {
            return;
        }

        LOG.info("Beginning dictionary bean post processing");

        List<DictionaryBeanProcessor> beanProcessors = new ArrayList<DictionaryBeanProcessor>();
        beanProcessors.add(new MessageBeanProcessor(dataDictionary, beanFactory));

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            String beanName = beanNames[i];
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

            processRootBeanDefinition(beanName, beanDefinition);
        }

        LOG.info("Finished dictionary bean post processing");
    }

    /**
     * Invokes processors to handle the root bean definition then processes the bean properties
     *
     * @param beanName name of the bean within the factory
     * @param beanDefinition root bean definition to process
     */
    protected void processRootBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        for (DictionaryBeanProcessor beanProcessor : beanProcessors) {
            beanProcessor.processRootBeanDefinition(beanName, beanDefinition);
        }

        Stack<BeanDefinitionHolder> nestedBeanStack = new Stack<BeanDefinitionHolder>();
        nestedBeanStack.push(new BeanDefinitionHolder(beanDefinition, beanName));

        processBeanProperties(beanDefinition, nestedBeanStack);
    }

    /**
     * Invokes the processors to handle the given nested bean definition
     *
     * <p>
     * A check is also made to determine if the nested bean has a non-generated id which is not registered in the
     * factory, if so the bean is added as a registered bean (so it can be found by id)
     * </p>
     *
     * @param beanName name of the nested bean definition in the bean factory
     * @param beanDefinition nested bean definition to process
     * @param nestedPropertyPath the property path to the nested bean from the parent bean definition
     * @param isCollectionBean indicates whether the nested bean is in a collection, if so a different handler
     * method is called on the processors
     * @param nestedBeanStack the stack of bean containers(those beans which contain the bean)
     */
    public void processNestedBeanDefinition(String beanName, BeanDefinition beanDefinition, String nestedPropertyPath,
            boolean isCollectionBean, Stack<BeanDefinitionHolder> nestedBeanStack) {
        // if bean name is given and factory does not have it registered we need to add it (inner beans that
        // were given an id)
        if (StringUtils.isNotBlank(beanName) && !StringUtils.contains(beanName, "$") && !StringUtils.contains(beanName,
                "#") && !beanFactory.containsBean(beanName)) {
            ((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(beanName, beanDefinition);
        }

        // invoke the processors to handle the nested bean
        for (DictionaryBeanProcessor beanProcessor : beanProcessors) {
            if (isCollectionBean) {
                beanProcessor.processCollectionBeanDefinition(beanName, beanDefinition, nestedPropertyPath,
                        nestedBeanStack);
            } else {
                beanProcessor.processNestedBeanDefinition(beanName, beanDefinition, nestedPropertyPath,
                        nestedBeanStack);
            }
        }

        BeanDefinitionHolder nestedBeanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
        nestedBeanStack.push(nestedBeanDefinitionHolder);

        processBeanProperties(beanDefinition, nestedBeanStack);

        nestedBeanStack.pop();
    }

    /**
     * Invokes the processors to handle the string value (which may be changed)
     *
     * @param propertyName name of the property that is being processed
     * @param propertyValue the string property value to process
     * @param nestedBeanStack the stack of bean containers, including the bean that contains the property
     * @return String new property value (possibly modified by processors)
     */
    protected String processStringPropertyValue(String propertyName, String propertyValue,
            Stack<BeanDefinitionHolder> nestedBeanStack) {
        String processedStringValue = propertyValue;

        for (DictionaryBeanProcessor beanProcessor : beanProcessors) {
            processedStringValue = beanProcessor.processStringPropertyValue(propertyName, processedStringValue,
                    nestedBeanStack);
        }

        return processedStringValue;
    }

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
    protected String processArrayStringPropertyValue(String propertyName, Object[] propertyValue, String elementValue,
            int elementIndex, Stack<BeanDefinitionHolder> nestedBeanStack,
            List<DictionaryBeanProcessor> beanProcessors) {
        String processedStringValue = elementValue;

        for (DictionaryBeanProcessor beanProcessor : beanProcessors) {
            processedStringValue = beanProcessor.processArrayStringPropertyValue(propertyName, propertyValue,
                    elementValue, elementIndex, nestedBeanStack);
        }

        return processedStringValue;
    }

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
    protected String processListStringPropertyValue(String propertyName, List<?> propertyValue, String elementValue,
            int elementIndex, Stack<BeanDefinitionHolder> nestedBeanStack,
            List<DictionaryBeanProcessor> beanProcessors) {
        String processedStringValue = elementValue;

        for (DictionaryBeanProcessor beanProcessor : beanProcessors) {
            processedStringValue = beanProcessor.processListStringPropertyValue(propertyName, propertyValue,
                    elementValue, elementIndex, nestedBeanStack);
        }

        return processedStringValue;
    }

    /**
     * Invokes the processors to handle an set string value (which may be changed)
     *
     * @param propertyName name of the property that is being processed
     * @param propertyValue the set which contains the string
     * @param elementValue the string element value
     * @param nestedBeanStack the stack of bean containers, including the bean that contains the property
     * @return String new property value (possibly modified by processors)
     */
    protected String processSetStringPropertyValue(String propertyName, Set<?> propertyValue, String elementValue,
            Stack<BeanDefinitionHolder> nestedBeanStack, List<DictionaryBeanProcessor> beanProcessors) {
        String processedStringValue = elementValue;

        for (DictionaryBeanProcessor beanProcessor : beanProcessors) {
            processedStringValue = beanProcessor.processSetStringPropertyValue(propertyName, propertyValue,
                    elementValue, nestedBeanStack);
        }

        return processedStringValue;
    }

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
    protected String processMapStringPropertyValue(String propertyName, Map<?, ?> propertyValue, String elementValue,
            Object elementKey, Stack<BeanDefinitionHolder> nestedBeanStack,
            List<DictionaryBeanProcessor> beanProcessors) {
        String processedStringValue = elementValue;

        for (DictionaryBeanProcessor beanProcessor : beanProcessors) {
            processedStringValue = beanProcessor.processMapStringPropertyValue(propertyName, propertyValue,
                    elementValue, elementKey, nestedBeanStack);
        }

        return processedStringValue;
    }

    /**
     * Iterates through the properties defined for the bean definition and invokes helper methods to process
     * the property value
     *
     * @param beanDefinition bean definition whose properties will be processed
     * @param nestedBeanStack stack of beans which contain the given bean
     */
    protected void processBeanProperties(BeanDefinition beanDefinition, Stack<BeanDefinitionHolder> nestedBeanStack) {
        // iterate through properties and check for any configured message keys within the value
        MutablePropertyValues pvs = beanDefinition.getPropertyValues();
        PropertyValue[] pvArray = pvs.getPropertyValues();
        for (PropertyValue pv : pvArray) {
            Object newPropertyValue = null;
            if (isStringValue(pv.getValue())) {
                newPropertyValue = processStringPropertyValue(pv.getName(), getString(pv.getValue()), nestedBeanStack);
            } else {
                newPropertyValue = visitPropertyValue(pv.getName(), pv.getValue(), nestedBeanStack);
            }

            pvs.removePropertyValue(pv.getName());
            pvs.addPropertyValue(pv.getName(), newPropertyValue);
        }
    }

    /**
     * Determines if the property value is a bean or collection, and calls the appropriate helper method
     * to process further. Ultimately this invokes the processors to modify the property value if necessary
     *
     * @param propertyName name for the property being processed
     * @param propertyValue value for the property to process
     * @param nestedBeanStack stack of beans which contain the property
     * @return Object the new property value (which may be modified0
     */
    protected Object visitPropertyValue(String propertyName, Object propertyValue,
            Stack<BeanDefinitionHolder> nestedBeanStack) {
        if (isBeanDefinitionValue(propertyValue)) {
            String beanName = getBeanName(propertyValue);
            BeanDefinition beanDefinition = getBeanDefinition(propertyValue);

            processNestedBeanDefinition(beanName, beanDefinition, propertyName, false, nestedBeanStack);
        } else if (isCollectionValue(propertyValue)) {
            visitCollection(propertyValue, propertyName, nestedBeanStack);
        }

        return propertyValue;
    }

    /**
     * Determines what kind of collection (or array) the given value is and call handlers based on the determined type
     *
     * @param value collection value to process
     * @param propertyName name of the property which has the collection value
     * @param nestedBeanStack stack of bean containers which contains the collection property
     */
    protected void visitCollection(Object value, String propertyName, Stack<BeanDefinitionHolder> nestedBeanStack) {
        if (value instanceof Object[] || (value instanceof ManagedArray)) {
            visitArray(value, propertyName, nestedBeanStack);
        } else if (value instanceof List) {
            visitList((List<?>) value, propertyName, nestedBeanStack);
        } else if (value instanceof Set) {
            visitSet((Set<?>) value, propertyName, nestedBeanStack);
        } else if (value instanceof Map) {
            visitMap((Map<?, ?>) value, propertyName, nestedBeanStack);
        }
    }

    /**
     * Iterates through the array values and calls helpers to process the value
     *
     * @param array the array to process
     * @param propertyName name of the property which has the array value
     * @param nestedBeanStack stack of bean containers which contains the array property
     */
    protected void visitArray(Object array, String propertyName, Stack<BeanDefinitionHolder> nestedBeanStack) {
        Object[] arrayVal = null;
        if (array instanceof ManagedArray) {
            arrayVal = (Object[]) ((ManagedArray) array).getSource();
        } else {
            arrayVal = (Object[]) array;
        }

        Object[] newArray = new Object[arrayVal.length];
        for (int i = 0; i < arrayVal.length; i++) {
            Object elem = arrayVal[i];

            if (isStringValue(elem)) {
                elem = processArrayStringPropertyValue(propertyName, arrayVal, getString(elem), i, nestedBeanStack,
                        beanProcessors);
            } else {
                elem = visitPropertyValue(propertyName, elem, nestedBeanStack);
            }

            newArray[i] = elem;
        }

        if (array instanceof ManagedArray) {
            ((ManagedArray) array).setSource(newArray);
        } else {
            array = newArray;
        }
    }

    /**
     * Iterates through the list values and calls helpers to process the value
     *
     * @param listVal the list to process
     * @param propertyName name of the property which has the list value
     * @param nestedBeanStack stack of bean containers which contains the list property
     */
    protected void visitList(List<?> listVal, String propertyName, Stack<BeanDefinitionHolder> nestedBeanStack) {
        boolean isMergeEnabled = false;
        if (listVal instanceof ManagedList) {
            isMergeEnabled = ((ManagedList) listVal).isMergeEnabled();
        }

        ManagedList newList = new ManagedList();
        newList.setMergeEnabled(isMergeEnabled);

        for (int i = 0; i < listVal.size(); i++) {
            Object elem = listVal.get(i);

            if (isStringValue(elem)) {
                elem = processListStringPropertyValue(propertyName, listVal, getString(elem), i, nestedBeanStack,
                        beanProcessors);
            } else {
                elem = visitPropertyValue(propertyName, elem, nestedBeanStack);
            }

            newList.add(i, elem);
        }

        listVal.clear();
        listVal.addAll(newList);
    }

    /**
     * Iterates through the set values and calls helpers to process the value
     *
     * @param setVal the set to process
     * @param propertyName name of the property which has the set value
     * @param nestedBeanStack stack of bean containers which contains the set property
     */
    protected void visitSet(Set setVal, String propertyName, Stack<BeanDefinitionHolder> nestedBeanStack) {
        boolean isMergeEnabled = false;
        if (setVal instanceof ManagedSet) {
            isMergeEnabled = ((ManagedSet) setVal).isMergeEnabled();
        }

        ManagedSet newSet = new ManagedSet();
        newSet.setMergeEnabled(isMergeEnabled);

        for (Object elem : setVal) {
            if (isStringValue(elem)) {
                elem = processSetStringPropertyValue(propertyName, setVal, getString(elem), nestedBeanStack,
                        beanProcessors);
            } else {
                elem = visitPropertyValue(propertyName, elem, nestedBeanStack);
            }

            newSet.add(elem);
        }

        setVal.clear();
        setVal.addAll(newSet);
    }

    /**
     * Iterates through the map values and calls helpers to process the value
     *
     * @param mapVal the set to process
     * @param propertyName name of the property which has the map value
     * @param nestedBeanStack stack of bean containers which contains the map property
     */
    protected void visitMap(Map<?, ?> mapVal, String propertyName, Stack<BeanDefinitionHolder> nestedBeanStack) {
        boolean isMergeEnabled = false;
        if (mapVal instanceof ManagedMap) {
            isMergeEnabled = ((ManagedMap) mapVal).isMergeEnabled();
        }

        ManagedMap newMap = new ManagedMap();
        newMap.setMergeEnabled(isMergeEnabled);

        for (Map.Entry entry : mapVal.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();

            if (isStringValue(val)) {
                val = processMapStringPropertyValue(propertyName, mapVal, getString(val), key, nestedBeanStack,
                        beanProcessors);
            } else {
                val = visitPropertyValue(propertyName, val, nestedBeanStack);
            }

            newMap.put(key, val);
        }

        mapVal.clear();
        mapVal.putAll(newMap);
    }

    /**
     * Indicate whether the given value is a string or holds a string
     *
     * @param value value to test
     * @return boolean true if the value is a string, false if not
     */
    protected boolean isStringValue(Object value) {
        boolean isString = false;

        if (value instanceof TypedStringValue || (value instanceof String)) {
            isString = true;
        }

        return isString;
    }

    /**
     * Determines whether the given value is of String type and if so returns the string value
     *
     * @param value object value to check
     * @return String string value for object or null if object is not a string type
     */
    protected String getString(Object value) {
        String stringValue = null;

        if (value instanceof TypedStringValue || (value instanceof String)) {
            if (value instanceof TypedStringValue) {
                TypedStringValue typedStringValue = (TypedStringValue) value;
                stringValue = typedStringValue.getValue();
            } else {
                stringValue = (String) value;
            }
        }

        return stringValue;
    }

    /**
     * Indicate whether the given value is a bean definition (or holder)
     *
     * @param value value to test
     * @return boolean true if the value is a bean definition, false if not
     */
    protected boolean isBeanDefinitionValue(Object value) {
        boolean isBean = false;

        if ((value instanceof BeanDefinition) || (value instanceof BeanDefinitionHolder)) {
            isBean = true;
        }

        return isBean;
    }

    /**
     * Returns the given value as a bean definition (parsing from holder if necessary)
     *
     * @param value value to convert
     * @return BeanDefinition converted bean definition
     */
    protected BeanDefinition getBeanDefinition(Object value) {
        BeanDefinition beanDefinition = null;

        if ((value instanceof BeanDefinition) || (value instanceof BeanDefinitionHolder)) {
            if (value instanceof BeanDefinition) {
                beanDefinition = (BeanDefinition) value;
            } else {
                beanDefinition = ((BeanDefinitionHolder) value).getBeanDefinition();
            }
        }

        return beanDefinition;
    }

    /**
     * Gets the bean name from the given value which is assumed to be a bean definition holder
     *
     * @param value value retrieve bean name from
     * @return String bean name, or null if value is not a bean definition holder
     */
    protected String getBeanName(Object value) {
        String beanName = null;

        if (value instanceof BeanDefinitionHolder) {
            beanName = ((BeanDefinitionHolder) value).getBeanName();
        }

        return beanName;
    }

    /**
     * Indicate whether the given value is a collection
     *
     * @param value value to test
     * @return boolean true if the value is a collection, false if not
     */
    protected boolean isCollectionValue(Object value) {
        boolean isCollection = false;

        if (value instanceof Object[] || (value instanceof ManagedArray) || (value instanceof List) ||
                (value instanceof Set) || (value instanceof Map)) {
            isCollection = true;
        }

        return isCollection;
    }

    /**
     * Retrieves the data dictionary service using the KRAD service locator
     *
     * @return DataDictionaryService instance
     */
    protected DataDictionaryService getDataDictionaryService() {
        return KRADServiceLocatorWeb.getDataDictionaryService();
    }

}
