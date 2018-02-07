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

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.component.Ordered;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.layout.LayoutManager;
import org.kuali.rice.krad.uif.layout.TableLayoutManager;
import org.springframework.beans.BeanUtils;
import org.springframework.core.OrderComparator;

/**
 * ComponentUtils is a utility class providing methods to help create and modify <code>Component</code> instances
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComponentUtils {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ComponentUtils.class);

    public static <T extends Component> T copy(T component) {
        return copy(component, null);
    }

    public static <T extends Component> T copy(T component, String idSuffix) {
        T copy = component.copy();

        if (StringUtils.isNotBlank(idSuffix)) {
            updateIdsWithSuffixNested(copy, idSuffix);
        }

        return copy;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Object> T getNewInstance(T object) {
        T copy = null;
        try {
            copy = (T) object.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create new instance of class: " + object.getClass());
        }

        return copy;
    }

    /**
     * Equivalent to {@link #copyFieldList(java.util.List, String, String)} but does not copy the given list of fields
     * first.
     */
    public static <T extends Field> void bindAndIdFieldList(List<T> fields, String addBindingPrefix, String idSuffix) {
        updateIdsWithSuffixNested(fields, idSuffix);
        prefixBindingPath(fields, addBindingPrefix);
    }

    public static <T extends Field> List<T> copyFieldList(List<T> fields, String addBindingPrefix, String idSuffix) {
        List<T> copiedFieldList = copyFieldList(fields, idSuffix);

        prefixBindingPath(copiedFieldList, addBindingPrefix);

        return copiedFieldList;
    }

    public static <T extends Field> List<T> copyFieldList(List<T> fields, String idSuffix) {
        if (fields == null || fields.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<T> copiedFieldList = new ArrayList<T>(fields.size());

        for (T field : fields) {
            T copiedField = copy(field, idSuffix);
            copiedFieldList.add(copiedField);
        }

        return copiedFieldList;
    }

    public static <T extends Component> T copyComponent(T component, String addBindingPrefix, String idSuffix) {
        T copy = copy(component, idSuffix);

        prefixBindingPathNested(copy, addBindingPrefix);

        return copy;
    }

    public static <T extends Component> List<T> copyComponentList(List<T> components, String idSuffix) {
        if (components == null || components.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<T> copiedComponentList = new ArrayList<T>(components.size());

        for (T field : components) {
            T copiedComponent = copy(field, idSuffix);
            copiedComponentList.add(copiedComponent);
        }

        return copiedComponentList;
    }

    public static <T extends Component> List<T> getComponentsOfType(List<? extends Component> items,
            Class<T> componentType) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<T> typeComponents = Collections.emptyList();
        
        for (Component component : items) {
            
            if (!componentType.isInstance(component)) {
                continue;
            }

            if (typeComponents.isEmpty()) {
                typeComponents = new ArrayList<T>(items.size());
            }

            typeComponents.add(componentType.cast(component));
        }

        return typeComponents;
    }

    /**
     * Return the components of the specified type from the given component list
     *
     * <p>
     * Components that match, implement or are extended from the specified {@code componentType} are returned in
     * the result.  If a component is a parent to other components then these child components are searched for
     * matching component types as well.
     * </p>
     *
     * @param items list of components from which to search
     * @param componentType the class or interface of the component type to return
     * @param <T> the type of the components that are returned
     * @return List of matching components
     */
    public static <T extends Component> List<T> getComponentsOfTypeDeep(List<? extends Component> items,
            Class<T> componentType) {
        if (items == null) {
            return Collections.emptyList();
        }
        
        List<T> components = Collections.emptyList();
        
        Queue<Component> componentQueue = new LinkedList<Component>();
        componentQueue.addAll(items);

        while (!componentQueue.isEmpty()) {
            Component currentComponent = componentQueue.poll();
            if (currentComponent == null) {
                continue;
            }

            if (componentType.isInstance(currentComponent)) {
                if (components.isEmpty()) {
                    components = new ArrayList<T>();
                }

                components.add(componentType.cast(currentComponent));
            }
            
            componentQueue.addAll(currentComponent.getComponentsForLifecycle());
        }

        return components;
    }

    /**
     * Return the components of the specified type from the given component list
     *
     * <p>
     * Components that match, implement or are extended from the specified {@code componentType} are returned in
     * the result.  If a component is a parent to other components then these child components are searched for
     * matching component types as well.
     * </p>
     *
     * @param component The components to search
     * @param componentType the class or interface of the component type to return
     * @param <T> the type of the components that are returned
     * @return List of matching components
     */
    public static <T extends Component> List<T> getComponentsOfTypeDeep(Component component, Class<T> componentType) {
        return getComponentsOfTypeDeep(Collections.singletonList(component), componentType);
    }

    /**
     * Returns components of the given type that are direct children of the given component (only checks
     * one level) including itself
     *
     * @param component instance to get children for
     * @param componentType type for component to return
     * @param <T> type of component that will be returned
     * @return list of child components with the given type
     */
    public static <T extends Component> List<T> getComponentsOfTypeShallow(Component component,
            Class<T> componentType) {
        if (component == null) {
            return Collections.emptyList();
        }
        
        List<T> typeComponents = getNestedComponentsOfTypeShallow(component, componentType);
        
        if (componentType.isInstance(component)) {
            if (typeComponents.isEmpty()) {
                typeComponents = Collections.singletonList(componentType.cast(component));
            } else {
                typeComponents.add(0, componentType.cast(component));
            }
        }

        return typeComponents;
    }

    /**
     * Get nested components of the type specified one layer deep; this defers from getComponentsOfTypeShallow
     * because it does NOT include itself as a match if it also matches the type being requested
     *
     * @param component instance to get children for
     * @param componentType type for component to return
     * @param <T> type of component that will be returned
     * @return list of child components with the given type
     */
    public static <T extends Component> List<T> getNestedComponentsOfTypeShallow(Component component,
            Class<T> componentType) {
        if (component == null) {
            return Collections.emptyList();
        }

        List<T> typeComponents = Collections.emptyList();
        List<Component> nestedComponents = component.getComponentsForLifecycle();

        for (Component nested : nestedComponents) {
            
            if (!componentType.isInstance(nested)) {
                continue;
            }

            if (typeComponents.isEmpty()) {
                typeComponents = new ArrayList<T>();
            }

            typeComponents.add(componentType.cast(nested));
        }

        return typeComponents;
    }

    /**
     * Get all nested children of a given component.
     * 
     * @param component The component to search.
     * @return All nested children of the component.
     * @see Component#getComponentsForLifecycle()
     */
    public static List<Component> getAllNestedComponents(Component component) {
        if (component == null) {
            return Collections.emptyList();
        }
        
        List<Component> components = Collections.emptyList();
        Queue<Component> componentQueue = new LinkedList<Component>();
        componentQueue.offer(component);
        
        while (!componentQueue.isEmpty()) {
            Component currentComponent = componentQueue.poll();
            
            if (currentComponent == null) {
                continue;
            }

            if (currentComponent != component) {
                if (components.isEmpty()) {
                    components = new ArrayList<Component>();
                }

                components.add(currentComponent);
            }

            componentQueue.addAll(currentComponent.getComponentsForLifecycle());
        }

        return components;
    }

    /**
     * Searches for the component with the given id within the given list of components
     *
     * @param components list of components to search through
     * @param componentId id for the component to find
     * @return component found in the list or null
     */
    public static Component findComponentInList(List<Component> components, String componentId) {
        for (Component component : components) {
            if (component != null && StringUtils.equals(component.getId(), componentId)) {
                return component;
            }
        }

        return null;
    }

    /**
     * Finds the child component of the given parent component that has the required id
     *
     * @param parent parent component for component to find
     * @param nestedId id of the component to find
     * @return Component instance for child (if found) or null
     */
    public static Component findNestedComponentById(Component parent, String nestedId) {
        if (parent == null) {
            return null;
        }
        
        Queue<Component> componentQueue = new LinkedList<Component>();
        componentQueue.offer(parent);
        
        while (!componentQueue.isEmpty()) {
            Component child = componentQueue.poll();
            
            if (child == null) {
                continue;
            }

            if (child != parent && StringUtils.equals(nestedId, child.getId())) {
                return child;
            }

            componentQueue.addAll(child.getComponentsForLifecycle());
        }
        
        return null;
    }

    public static void prefixBindingPath(List<? extends Field> fields, String addBindingPrefix) {
        for (Field field : fields) {
            if (field instanceof DataBinding) {
                prefixBindingPath((DataBinding) field, addBindingPrefix);
            } else if ((field instanceof FieldGroup) && (((FieldGroup) field).getItems() != null)) {
                List<Field> groupFields = getComponentsOfTypeDeep(((FieldGroup) field).getItems(), Field.class);
                prefixBindingPath(groupFields, addBindingPrefix);
            }
        }
    }

    public static void prefixBindingPathNested(Component component, String addBindingPrefix) {
        if (component instanceof DataBinding) {
            if (LOG.isDebugEnabled()) {
                LOG.info("setting nested binding prefix '" + addBindingPrefix + "' on " + component);
            }
            prefixBindingPath((DataBinding) component, addBindingPrefix);
        }

        for (Component nested : component.getComponentsForLifecycle()) {
            if (nested != null) {
                prefixBindingPathNested(nested, addBindingPrefix);
            }
        }
    }

    public static void prefixBindingPath(DataBinding field, String addBindingPrefix) {
        String bindingPrefix = addBindingPrefix;
        if (StringUtils.isNotBlank(field.getBindingInfo().getBindByNamePrefix())) {
            bindingPrefix += "." + field.getBindingInfo().getBindByNamePrefix();
        }
        field.getBindingInfo().setBindByNamePrefix(bindingPrefix);
    }

    public static void updateIdsWithSuffixNested(List<? extends Component> components, String idSuffix) {
        for (Component component : components) {
            updateIdsWithSuffixNested(component, idSuffix);
        }
    }

    public static void updateIdsWithSuffixNested(Component component, String idSuffix) {
        updateIdWithSuffix(component, idSuffix);

        updateChildIdsWithSuffixNested(component, idSuffix);
    }

    public static void updateChildIdsWithSuffixNested(Component component, String idSuffix) {
        for (Component nested : component.getComponentsForLifecycle()) {
            if (nested != null) {
                updateIdsWithSuffixNested(nested, idSuffix);
            }
        }

        List<Component> propertyReplacerComponents = component.getPropertyReplacerComponents();
        if (propertyReplacerComponents != null) {
            for (Component nested : propertyReplacerComponents) {
                if (nested != null) {
                    updateIdsWithSuffixNested(nested, idSuffix);
                }
            }
        }
    }

    /**
     * Clear all ids from a component and its children.  If there are features that depend on a static id of this
     * component, this call may cause errors.
     *
     * @param component the component to clear all ids from
     */
    public static void clearIds(Component component) {
        component.setId(null);

        if (Container.class.isAssignableFrom(component.getClass())) {
            LayoutManager layoutManager = ((Container) component).getLayoutManager();
            layoutManager.setId(null);
        }

        for (Component nested : component.getComponentsForLifecycle()) {
            if (nested != null) {
                clearIds(nested);
            }
        }

        List<Component> propertyReplacerComponents = component.getPropertyReplacerComponents();
        if (propertyReplacerComponents != null) {
            for (Component nested : propertyReplacerComponents) {
                if (nested != null) {
                    clearIds(nested);
                }
            }
        }
    }

    public static void clearIds(List<? extends Component> components) {
        for (Component component : components) {
            if (component != null) {
                clearIds(component);
            }
        }
    }

    /**
     * add a suffix to the id
     *
     * @param component the component instance whose id will be changed
     * @param idSuffix the suffix to be appended
     */
    public static void updateIdWithSuffix(Component component, String idSuffix) {
        if (component != null && !StringUtils.isEmpty(idSuffix)) {
            component.setId(component.getId() + idSuffix);
        }

        if (component instanceof Container) {
            LayoutManager manager = ((Container) component).getLayoutManager();
            if (manager != null) {
                manager.setId(manager.getId() + idSuffix);
            }
        }
    }

    public static void setComponentsPropertyDeep(List<? extends Component> components, String propertyPath,
            Object propertyValue) {
        for (Component component : components) {
            setComponentPropertyDeep(component, propertyPath, propertyValue);
        }
    }

    public static void setComponentPropertyDeep(Component component, String propertyPath, Object propertyValue) {
        ObjectPropertyUtils.setPropertyValue(component, propertyPath, propertyValue, true);

        for (Component nested : component.getComponentsForLifecycle()) {
            if (nested != null) {
                setComponentPropertyDeep(nested, propertyPath, propertyValue);
            }
        }
    }

    public static List<String> getComponentPropertyNames(Class<? extends Component> componentClass) {
        List<String> componentProperties = new ArrayList<String>();

        PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(componentClass);
        for (int i = 0; i < properties.length; i++) {
            PropertyDescriptor descriptor = properties[i];
            if (descriptor.getReadMethod() != null) {
                componentProperties.add(descriptor.getName());
            }
        }

        return componentProperties;
    }

    /**
     * Sets a property on the given component and removes any expressions for that property so the value is not
     * overridden
     *
     * @param component component instance to set property on
     * @param propertyName name of property to set
     * @param propertyValue value to set property to
     */
    public static void setComponentPropertyFinal(Component component, String propertyName, Object propertyValue) {
        if (component == null) {
            return;
        }

        ObjectPropertyUtils.setPropertyValue(component, propertyName, propertyValue);

        if ((component.getPropertyExpressions() != null) && component.getPropertyExpressions().containsKey(
                propertyName)) {
            component.getPropertyExpressions().remove(propertyName);
        }
    }

    /**
     * places a key, value pair in each context map of a list of components
     *
     * @param components the list components
     * @param contextName a value to be used as a key to retrieve the object
     * @param contextValue the value to be placed in the context
     */
    public static void pushObjectToContext(List<? extends Component> components, String contextName,
            Object contextValue) {
        if (components == null || components.isEmpty()) {
            return;
        }
        
        Queue<Component> componentQueue = new LinkedList<Component>();
        componentQueue.addAll(components);
        
        while (!componentQueue.isEmpty()) {
            Component currentComponent = componentQueue.poll();
            
            if (currentComponent == null) {
                continue;
            }
            
            currentComponent.pushObjectToContext(contextName, contextValue);

            if (currentComponent instanceof Container) {
                LayoutManager layoutManager = ((Container) currentComponent).getLayoutManager();
                if (layoutManager != null) {
                    layoutManager.pushObjectToContext(contextName, contextValue);
                }
            }
            
            componentQueue.addAll(currentComponent.getComponentsForLifecycle());
        }
    }

    /**
     * pushes object to a component's context so that it is available from {@link Component#getContext()}
     *
     * <p>The component's nested components that are available via {@code Component#getComponentsForLifecycle}
     * are also updated recursively</p>
     *
     * @param component the component whose context is to be updated
     * @param contextName a value to be used as a key to retrieve the object
     * @param contextValue the value to be placed in the context
     */
    public static void pushObjectToContext(Component component, String contextName, Object contextValue) {
        if (component == null) {
            return;
        }

        pushObjectToContext(Collections.singletonList(component), contextName, contextValue);
    }

    /**
     * places a all entries from a map into each context map of a list of components
     *
     * @param components The list components.
     * @param sourceContext The source context map.
     */
    public static void pushAllToContext(List<? extends Component> components, Map<String, Object> sourceContext) {
        if (components == null || components.isEmpty()) {
            return;
        }
        
        Queue<Component> componentQueue = new LinkedList<Component>();
        componentQueue.addAll(components);
        
        while (!componentQueue.isEmpty()) {
            Component currentComponent = componentQueue.poll();
            
            if (currentComponent == null) {
                continue;
            }
            
            currentComponent.pushAllToContext(sourceContext);

            if (currentComponent instanceof Container) {
                LayoutManager layoutManager = ((Container) currentComponent).getLayoutManager();
                if (layoutManager != null) {
                    layoutManager.pushAllToContext(sourceContext);
                }
            }
            
            componentQueue.addAll(currentComponent.getComponentsForLifecycle());
        }
    }

    /**
     * pushes object to a component's context so that it is available from {@link Component#getContext()}
     *
     * <p>The component's nested components that are available via {@code Component#getComponentsForLifecycle}
     * are also updated recursively</p>
     *
     * @param component the component whose context is to be updated
     * @param sourceContext The source context map.
     */
    public static void pushAllToContext(Component component, Map<String, Object> sourceContext) {
        if (component == null) {
            return;
        }

        pushAllToContext(Collections.singletonList(component), sourceContext);
    }

    /**
     * update the contexts of the given components
     *
     * <p>calls {@link #updateContextForLine(org.kuali.rice.krad.uif.component.Component, Object, int, String)}
     * for each component</p>
     *
     * @param components the components whose components to update
     * @param collectionLine an instance of the data object for the line
     * @param lineIndex the line index
     * @param lineSuffix id suffix for components in the line to make them unique
     */
    public static void updateContextsForLine(List<? extends Component> components, Object collectionLine, int lineIndex,
            String lineSuffix) {
        for (Component component : components) {
            updateContextForLine(component, collectionLine, lineIndex, lineSuffix);
        }
    }

    /**
     * update the context map for the given component
     *
     * <p>The values of {@code UifConstants.ContextVariableNames.LINE} and {@code UifConstants.ContextVariableNames.INDEX}
     * are set to {@code collectionLine} and {@code lineIndex} respectively.</p>
     *
     * @param component the component whose context is to be updated
     * @param collectionLine an instance of the data object for the line
     * @param lineIndex the line index
     * @param lineSuffix id suffix for components in the line to make them unique
     */
    public static void updateContextForLine(Component component, Object collectionLine, int lineIndex,
            String lineSuffix) {
        Map<String, Object> toUpdate = new HashMap<String,Object>(4);
        toUpdate.put(UifConstants.ContextVariableNames.LINE, collectionLine);
        toUpdate.put(UifConstants.ContextVariableNames.INDEX, Integer.valueOf(lineIndex));
        toUpdate.put(UifConstants.ContextVariableNames.LINE_SUFFIX, lineSuffix);

        boolean isAddLine = (lineIndex == -1);
        toUpdate.put(UifConstants.ContextVariableNames.IS_ADD_LINE, isAddLine);
        pushAllToContext(component, toUpdate);
    }

    /**
     * Performs sorting logic of the given list of <code>Ordered</code>
     * instances by its order property
     *
     * <p>
     * Items list is sorted based on its order property. Lower order values are
     * placed higher in the list. If a item does not have a value assigned for
     * the order (or is equal to the default order of 0), it will be assigned
     * the a value based on the given order sequence integer. If two or more
     * items share the same order value, all but the last item found in the list
     * will be removed.
     * </p>
     *
     * @param items
     * @param defaultOrderSequence
     * @return List<Ordered> sorted items
     * @see org.kuali.rice.krad.uif.component.Component#getOrder()
     * @see @see org.springframework.core.Ordered
     */
    public static List<? extends Ordered> sort(List<? extends Ordered> items, int defaultOrderSequence) {
        if (items == null) {
            return null;
        }
        
        List<Ordered> orderedItems = new ArrayList<Ordered>(items.size());

        // do replacement for items with the same order property value
        Set<Integer> foundOrders = new HashSet<Integer>();

        // reverse the list, so items later in the list win
        for (int i = items.size()-1; i >= 0; i--) {
            Ordered component = items.get(i);
            int order = component.getOrder();

            // if order not set just add to list
            if (order == 0) {
                orderedItems.add(component);
            }
            // check if the order value has been used already
            else if (!foundOrders.contains(Integer.valueOf(order))) {
                orderedItems.add(component);
                foundOrders.add(Integer.valueOf(order));
            }
        }

        // now reverse the list back so we can assign defaults for items without
        // an order value
        for (int i = 0; i < items.size(); i++) {
            Ordered component = items.get(i);
            int order = component.getOrder();

            // if order property not set assign default
            if (order == 0) {
                defaultOrderSequence++;
                while (foundOrders.contains(Integer.valueOf(defaultOrderSequence))) {
                    defaultOrderSequence++;
                }
                component.setOrder(defaultOrderSequence);
            }
        }

        // now sort the list by its order property
        Collections.sort(orderedItems, new OrderComparator());

        return orderedItems;
    }

    /**
     * Gets all the input fields contained in this container, but also in
     * every sub-container that is a child of this container.  When called from the top level
     * View this will be every InputField across all pages.
     *
     * @return every InputField that is a child at any level of this container
     */
    public static List<InputField> getAllInputFieldsWithinContainer(Container container) {
        List<InputField> inputFields = new ArrayList<InputField>();

        for (Component c : container.getComponentsForLifecycle()) {
            if (c instanceof InputField) {
                inputFields.add((InputField) c);
            } else if (c instanceof Container) {
                inputFields.addAll(getAllInputFieldsWithinContainer((Container) c));
            } else if (c instanceof FieldGroup) {
                Container cb = ((FieldGroup) c).getGroup();

                inputFields.addAll(getAllInputFieldsWithinContainer(cb));
            }
        }

        return inputFields;
    }

    /**
     * Determines whether the given component contains an expression for the given property name
     *
     * @param component component instance to check for expressions
     * @param propertyName name of the property to determine if there is an expression for
     * @param collectionMatch if set to true will find an expressions for properties that start with the given
     * property name (for matching expressions on collections like prop[index] or prop['key'])
     * @return true if the component has an expression for the property name, false if not
     */
    public static boolean containsPropertyExpression(Component component, String propertyName,
            boolean collectionMatch) {
        boolean hasExpression = false;

        Map<String, String> propertyExpressions = component.getPropertyExpressions();

        if (collectionMatch) {
            for (String expressionPropertyName : propertyExpressions.keySet()) {
                if (expressionPropertyName.startsWith(propertyName)) {
                    hasExpression = true;
                }
            }
        } else if (propertyExpressions.containsKey(propertyName)) {
            hasExpression = true;
        }

        return hasExpression;
    }

    /**
     * Adjust nestingLevel properties for collections which use RichTable with forceLocalJsonData on and for all of its
     * potentially additional nested subcollections
     *
     * @param container container to traverse and update nested levels in
     * @param currentLevel the current nesting level, the initial call to this method should be 0
     */
    public static void adjustNestedLevelsForTableCollections(Container container, int currentLevel) {
        if (container != null
                && container instanceof CollectionGroup
                && container.getLayoutManager() != null
                && container.getLayoutManager() instanceof TableLayoutManager
                && ((TableLayoutManager) container.getLayoutManager()).getRichTable() != null
                && ((TableLayoutManager) container.getLayoutManager()).getRichTable().isRender()
                && ((TableLayoutManager) container.getLayoutManager()).getRichTable().isForceLocalJsonData()) {
            ((TableLayoutManager) container.getLayoutManager()).getRichTable().setNestedLevel(currentLevel);
            currentLevel++;
        }

        if (container != null) {
            List<Container> subContainers = ComponentUtils.getNestedComponentsOfTypeShallow(container, Container.class);
            for (Container subContainer : subContainers) {
                adjustNestedLevelsForTableCollections(subContainer, currentLevel);
            }

            List<FieldGroup> subFieldGroups = ComponentUtils.getNestedComponentsOfTypeShallow(container,
                    FieldGroup.class);
            for (FieldGroup fieldGroup : subFieldGroups) {
                if (fieldGroup != null) {
                    adjustNestedLevelsForTableCollections(fieldGroup.getGroup(), currentLevel);
                }
            }
        }
    }

}
