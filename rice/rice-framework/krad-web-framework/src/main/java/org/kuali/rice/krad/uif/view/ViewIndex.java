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
package org.kuali.rice.krad.uif.view;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ViewCleaner;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Holds component indexes of a <code>View</code> instance for convenient retrieval during the lifecycle
 * and persisting components for the refresh process
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewIndex implements Serializable {
    private static final long serialVersionUID = 4700818801272201371L;

    protected Map<String, Component> index;
    protected Map<String, DataField> dataFieldIndex;
    protected Set<String> idsToHoldInIndex;

    protected Map<String, CollectionGroup> collectionsIndex;

    protected Map<String, Component> initialComponentStates;

    protected Set<String> idsToHoldInitialState;

    protected Map<String, PropertyEditor> fieldPropertyEditors;
    protected Map<String, PropertyEditor> secureFieldPropertyEditors;
    protected Map<String, Integer> idSequenceSnapshot;
    protected Map<String, Map<String, String>> componentExpressionGraphs;

    protected Map<String, Map<String, Object>> postContext;

    /**
     * Constructs new instance
     */
    public ViewIndex() {
        index = new HashMap<String, Component>();
        dataFieldIndex = new HashMap<String, DataField>();
        idsToHoldInIndex = new HashSet<String>();
        collectionsIndex = new HashMap<String, CollectionGroup>();
        initialComponentStates = new HashMap<String, Component>();
        idsToHoldInitialState = new HashSet<String>();
        fieldPropertyEditors = new HashMap<String, PropertyEditor>();
        secureFieldPropertyEditors = new HashMap<String, PropertyEditor>();
        idSequenceSnapshot = new HashMap<String, Integer>();
        componentExpressionGraphs = new HashMap<String, Map<String, String>>();
        postContext = new HashMap<String, Map<String, Object>>();
    }

    /**
     * Walks through the View tree and indexes all components found. All components
     * are indexed by their IDs with the special indexing done for certain components
     *
     * <p>
     * <code>DataField</code> instances are indexed by the attribute path.
     * This is useful for retrieving the InputField based on the incoming
     * request parameter
     * </p>
     *
     * <p>
     * <code>CollectionGroup</code> instances are indexed by the collection
     * path. This is useful for retrieving the CollectionGroup based on the
     * incoming request parameter
     * </p>
     */
    protected void index(View view) {
        index = new HashMap<String, Component>();
        dataFieldIndex = new HashMap<String, DataField>();
        collectionsIndex = new HashMap<String, CollectionGroup>();
        fieldPropertyEditors = new HashMap<String, PropertyEditor>();
        secureFieldPropertyEditors = new HashMap<String, PropertyEditor>();

        indexComponent(view);
    }

    /**
     * Adds an entry to the main index for the given component. If the component
     * is of type <code>DataField</code> or <code>CollectionGroup</code> an
     * entry is created in the corresponding indexes for those types as well. Then
     * the #indexComponent method is called for each of the component's children
     *
     * <p>
     * If the component is already contained in the indexes, it will be replaced
     * </p>
     *
     * <p>
     * Special processing is done for DataField instances to register their property editor which will
     * be used for form binding
     * </p>
     *
     * @param component component instance to index
     */
    public void indexComponent(Component component) {
        if (component == null) {
            return;
        }

        index.put(component.getId(), component);

        if (component instanceof DataField) {
            DataField field = (DataField) component;
            dataFieldIndex.put(field.getBindingInfo().getBindingPath(), field);

            // pull out information we will need to support the form post
            if (component.isRender()) {
                if (field.hasSecureValue()) {
                    secureFieldPropertyEditors.put(field.getBindingInfo().getBindingPath(), field.getPropertyEditor());
                } else {
                    fieldPropertyEditors.put(field.getBindingInfo().getBindingPath(), field.getPropertyEditor());
                }
            }
        } else if (component instanceof CollectionGroup) {
            CollectionGroup collectionGroup = (CollectionGroup) component;
            collectionsIndex.put(collectionGroup.getBindingInfo().getBindingPath(), collectionGroup);
        }

        for (Component nestedComponent : component.getComponentsForLifecycle()) {
            indexComponent(nestedComponent);
        }
    }

    /**
     * Invoked after the view lifecycle or component refresh has run to clear indexes that are not
     * needed for the post
     */
    public void clearIndexesAfterRender() {
        // build list of factory ids for components whose initial or final state needs to be kept
        for (Component component : index.values()) {
            if (component == null) {
                continue;
            }

            if (component.isDisableSessionPersistence()) {
                continue;
            }

            if (component.isForceSessionPersistence() || canBeRefreshed(component)) {
                idsToHoldInitialState.add(component.getBaseId());
                idsToHoldInIndex.add(component.getId());
            }
            // if component is a collection we need to keep it for add/delete and other collection functions
            else if (component instanceof CollectionGroup) {
                idsToHoldInitialState.add(component.getBaseId());
                idsToHoldInIndex.add(component.getId());
            }
            else if ((component instanceof InputField)) {
                InputField inputField = (InputField) component;

                if ((inputField.getAttributeQuery() != null) || ((inputField.getSuggest() != null) && inputField
                        .getSuggest().isRender())) {
                    idsToHoldInIndex.add(component.getId());
                }
            }
        }

        // now filter the indexes to include only the components that we need (determined above)
        Map<String, Component> holdInitialComponentStates = new HashMap<String, Component>();
        for (String factoryId : initialComponentStates.keySet()) {
            if (idsToHoldInitialState.contains(factoryId)) {
                holdInitialComponentStates.put(factoryId, initialComponentStates.get(factoryId));
            }
        }
        initialComponentStates = holdInitialComponentStates;

        Map<String, Component> holdComponentStates = new HashMap<String, Component>();
        for (String id : index.keySet()) {
            if (idsToHoldInIndex.contains(id)) {
                Component component = this.index.get(id);

                // hold expressions for refresh (since they could have been pushed from a parent)
                if ((component.getRefreshExpressionGraph() != null) && !component.getRefreshExpressionGraph()
                        .isEmpty()) {
                    componentExpressionGraphs.put(component.getBaseId(), component.getRefreshExpressionGraph());
                }

                ViewCleaner.cleanComponent(component, this);

                holdComponentStates.put(id, component);
            }
        }
        index = holdComponentStates;

        for (CollectionGroup collectionGroup : collectionsIndex.values()) {
            ViewCleaner.cleanComponent(collectionGroup, this);
        }

        dataFieldIndex = new HashMap<String, DataField>();
    }

    /**
     * Indicates if the given component has configuration that it allows it to be refreshed
     *
     * @param component instance to check
     * @return true if component can be refreshed, false if not
     */
    protected boolean canBeRefreshed(Component component) {
        boolean canBeRefreshed = false;

        boolean hasRefreshCondition = StringUtils.isNotBlank(component.getProgressiveRender()) ||
                StringUtils.isNotBlank(component.getConditionalRefresh()) || (component.getRefreshTimer() > 0) ||
                (component.getRefreshWhenChangedPropertyNames() != null && !component
                        .getRefreshWhenChangedPropertyNames().isEmpty());

        canBeRefreshed = hasRefreshCondition || component.isRefreshedByAction() || component.isDisclosedByAction();

        return canBeRefreshed;
    }

    /**
     * Indicates whether the given component id is for a component maintained by the view index for
     * the refresh process
     *
     * @param componentId id for the component to check
     * @return true if the component id is for a refreshed component, false if not
     */
    public boolean isIdForRefreshComponent(String componentId) {
        return idsToHoldInIndex.contains(componentId);
    }

    /**
     * Retrieves a <code>Component</code> from the view index by Id
     *
     * @param id id for the component to retrieve
     * @return Component instance found in index, or null if no such component exists
     */
    public Component getComponentById(String id) {
        return index.get(id);
    }

    /**
     * Retrieves a <code>DataField</code> instance from the index
     *
     * @param propertyPath full path of the data field (from the form)
     * @return DataField instance for the path or Null if not found
     */
    public DataField getDataFieldByPath(String propertyPath) {
        return dataFieldIndex.get(propertyPath);
    }

    /**
     * Retrieves a <code>DataField</code> instance that has the given property name
     * specified (note this is not the full binding path and first match is returned)
     *
     * @param propertyName property name for field to retrieve
     * @return DataField instance found or null if not found
     */
    public DataField getDataFieldByPropertyName(String propertyName) {
        DataField dataField = null;

        for (DataField field : dataFieldIndex.values()) {
            if (StringUtils.equals(propertyName, field.getPropertyName())) {
                dataField = field;
                break;
            }
        }

        return dataField;
    }

    /**
     * Gets the Map that contains attribute field indexing information. The Map
     * key points to an attribute binding path, and the Map value is the
     * <code>DataField</code> instance
     *
     * @return data fields index map
     */
    public Map<String, DataField> getDataFieldIndex() {
        return this.dataFieldIndex;
    }

    /**
     * Gets the Map that contains collection indexing information. The Map key
     * gives the binding path to the collection, and the Map value givens the
     * <code>CollectionGroup</code> instance
     *
     * @return collection index map
     */
    public Map<String, CollectionGroup> getCollectionsIndex() {
        return this.collectionsIndex;
    }

    /**
     * Retrieves a <code>CollectionGroup</code> instance from the index
     *
     * @param collectionPath full path of the collection (from the form)
     * @return CollectionGroup instance for the collection path or Null if not
     *         found
     */
    public CollectionGroup getCollectionGroupByPath(String collectionPath) {
        return collectionsIndex.get(collectionPath);
    }

    /**
     * Preserves initial state of components needed for doing component refreshes
     *
     * <p>
     * Some components, such as those that are nested or created in code cannot be requested from the
     * spring factory to get new instances. For these a copy of the component in its initial state is
     * set in this map which will be used when doing component refreshes (which requires running just that
     * component's lifecycle)
     * </p>
     *
     * <p>
     * Map entries are added during the perform initialize phase from {@link org.kuali.rice.krad.uif.service.ViewHelperService}
     * </p>
     *
     * @return map with key giving the factory id for the component and the value the component instance
     */
    public Map<String, Component> getInitialComponentStates() {
        return initialComponentStates;
    }

    /**
     * Adds a copy of the given component instance to the map of initial component states keyed
     *
     * <p>
     * Component is only added if its factory id is not set yet (which would happen if it had a spring bean id
     * and we can get the state from Spring). Once added the factory id will be set to the component id
     * </p>
     *
     * @param component component instance to add
     */
    public void addInitialComponentStateIfNeeded(Component component) {
        if (StringUtils.isBlank(component.getBaseId())) {
            component.setBaseId(component.getId());
            initialComponentStates.put(component.getBaseId(), ComponentUtils.copy(component));
        }
    }

    /**
     * Setter for the map holding initial component states
     *
     * @param initialComponentStates
     */
    public void setInitialComponentStates(Map<String, Component> initialComponentStates) {
        this.initialComponentStates = initialComponentStates;
    }

    /**
     * Maintains configuration of properties that have been configured for the view (if render was set to
     * true) and there corresponding PropertyEdtior (if configured)
     *
     * <p>
     * Information is pulled out of the View during the lifecycle so it can be used when a form post is done
     * from the View. Note if a field is secure, it will be placed in the {@link #getSecureFieldPropertyEditors()} map
     * instead
     * </p>
     *
     * @return map of property path (full) to PropertyEditor
     */
    public Map<String, PropertyEditor> getFieldPropertyEditors() {
        return fieldPropertyEditors;
    }

    /**
     * Maintains configuration of secure properties that have been configured for the view (if render was set to
     * true) and there corresponding PropertyEdtior (if configured)
     *
     * <p>
     * Information is pulled out of the View during the lifecycle so it can be used when a form post is done
     * from the View. Note if a field is non-secure, it will be placed in the {@link #getFieldPropertyEditors()} map
     * instead
     * </p>
     *
     * @return map of property path (full) to PropertyEditor
     */
    public Map<String, PropertyEditor> getSecureFieldPropertyEditors() {
        return secureFieldPropertyEditors;
    }

    /**
     * Map of components ids to starting id sequences used for the component refresh process
     *
     * @return Map<String, Integer> key is component id and value is id sequence value
     */
    public Map<String, Integer> getIdSequenceSnapshot() {
        return idSequenceSnapshot;
    }

    /**
     * Adds a sequence value to the id snapshot map for the given component id
     *
     * @param componentId id for the component the id sequence value is associated it
     * @param sequenceVal current sequence value to insert into the snapshot
     */
    public void addSequenceValueToSnapshot(String componentId, int sequenceVal) {
        if (!idSequenceSnapshot.containsKey(componentId)){
            idSequenceSnapshot.put(componentId, sequenceVal);
        }
    }

    /**
     * Map of components with their associated expression graphs that will be used during
     * the component refresh process
     *
     * <p>
     * Because expressions that impact a component being refreshed might be on a parent component, a special
     * map needs to be held around that contains expressions that apply to the component and all its nested
     * components. This map is populated during the initial view processing and populating of the property
     * expressions from the initial expression graphs
     * </p>
     *
     * @return Map<String, Map<String, String>> key is component id and value is expression graph map
     * @see org.kuali.rice.krad.uif.util.ExpressionUtils#populatePropertyExpressionsFromGraph(org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean,
     *      boolean)
     */
    public Map<String, Map<String, String>> getComponentExpressionGraphs() {
        return componentExpressionGraphs;
    }

    /**
     * A map of state that is held in the session
     *
     * <p>
     * Instead of storing entire components in the session in order to retrieve information for posts, just the
     * state that is needed can be added to this map and then retrieve on the post through the posted view's index
     * </p>
     *
     * @return Map of post context which is a map of maps. First map is keyed by component id, then each map
     * value gives the context for that component
     */
    public Map<String, Map<String, Object>> getPostContext() {
        return postContext;
    }

    /**
     * Adds an entry to the post context for the given component
     *
     * @param componentId id of the component the context is associated with
     * @param entryKey key for the entry
     * @param entryValue value for the entry
     */
    public void addPostContextEntry(String componentId, String entryKey, Object entryValue) {
        Map<String, Object> componentContext = null;

        if (postContext.containsKey(componentId)) {
            componentContext = postContext.get(componentId);
        } else {
            componentContext = new HashMap<String, Object>();
            postContext.put(componentId, componentContext);
        }

        componentContext.put(entryKey, entryValue);
    }

    /**
     * Retrieves a context entry values for the given component and entry key
     *
     * @param componentId id of the component the entry is associated with
     * @param entryKey key for the entry
     * @return value associated with the entry, or null if entry is not found
     */
    public Object getPostContextEntry(String componentId, String entryKey) {
        Object entryValue = null;

        Map<String, Object> componentContext = null;

        if (postContext.containsKey(componentId)) {
            componentContext = postContext.get(componentId);

            entryValue = componentContext.get(entryKey);
        }

        return entryValue;
    }

    /**
     * Returns a clone of the view index.
     *
     * @return ViewIndex clone
     */
    public ViewIndex copy() {
        ViewIndex viewIndexCopy = new ViewIndex();

        if (this.index != null) {
            Map<String, Component> indexCopy = new HashMap<String, Component>();
            for (Map.Entry<String, Component> indexEntry : this.index.entrySet()) {
                indexCopy.put(indexEntry.getKey(), (Component) indexEntry.getValue().copy());
            }

            viewIndexCopy.index = indexCopy;
        }

        if (this.dataFieldIndex != null) {
            Map<String, DataField> dataFieldIndexCopy = new HashMap<String, DataField>();
            for (Map.Entry<String, DataField> indexEntry : this.dataFieldIndex.entrySet()) {
                dataFieldIndexCopy.put(indexEntry.getKey(), (DataField) indexEntry.getValue().copy());
            }

            viewIndexCopy.dataFieldIndex = dataFieldIndexCopy;
        }

        if (this.collectionsIndex != null) {
            Map<String, CollectionGroup> collectionsIndexCopy = new HashMap<String, CollectionGroup>();
            for (Map.Entry<String, CollectionGroup> indexEntry : this.collectionsIndex.entrySet()) {
                collectionsIndexCopy.put(indexEntry.getKey(), (CollectionGroup) indexEntry.getValue().copy());
            }

            viewIndexCopy.collectionsIndex = collectionsIndexCopy;
        }

        if (this.initialComponentStates != null) {
            Map<String, Component> initialComponentStatesCopy = new HashMap<String, Component>();
            for (Map.Entry<String, Component> indexEntry : this.initialComponentStates.entrySet()) {
                initialComponentStatesCopy.put(indexEntry.getKey(), (Component) indexEntry.getValue().copy());
            }

            viewIndexCopy.initialComponentStates = initialComponentStatesCopy;
        }

        if (this.fieldPropertyEditors != null) {
            viewIndexCopy.fieldPropertyEditors = new HashMap<String, PropertyEditor>(this.fieldPropertyEditors);
        }

        if (this.secureFieldPropertyEditors != null) {
            viewIndexCopy.secureFieldPropertyEditors = new HashMap<String, PropertyEditor>(
                    this.secureFieldPropertyEditors);
        }

        if (this.idSequenceSnapshot != null) {
            viewIndexCopy.idSequenceSnapshot = new HashMap<String, Integer>(this.idSequenceSnapshot);
        }

        if (this.componentExpressionGraphs != null) {
            viewIndexCopy.componentExpressionGraphs = new HashMap<String, Map<String, String>>(
                    this.componentExpressionGraphs);
        }

        if (this.postContext != null) {
            viewIndexCopy.postContext = new HashMap<String, Map<String, Object>>(this.postContext);
        }

        return viewIndexCopy;
    }

}
