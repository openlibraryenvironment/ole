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
package org.kuali.rice.krad.uif.service.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.inquiry.Inquirable;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.service.PersistenceStructureService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.component.ComponentSecurity;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.container.LightTable;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Label;
import org.kuali.rice.krad.uif.field.ActionField;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.layout.TableLayoutManager;
import org.kuali.rice.krad.uif.util.ViewCleaner;
import org.kuali.rice.krad.uif.view.DefaultExpressionEvaluator;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.kuali.rice.krad.uif.view.ViewAuthorizer;
import org.kuali.rice.krad.uif.view.ViewPresentationController;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.ClientSideState;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.component.PropertyReplacer;
import org.kuali.rice.krad.uif.component.RequestParameter;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.control.Control;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.field.RemoteFieldsHolder;
import org.kuali.rice.krad.uif.layout.LayoutManager;
import org.kuali.rice.krad.uif.modifier.ComponentModifier;
import org.kuali.rice.krad.uif.service.ViewDictionaryService;
import org.kuali.rice.krad.uif.service.ViewHelperService;
import org.kuali.rice.krad.uif.util.BooleanMap;
import org.kuali.rice.krad.uif.util.CloneUtils;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ExpressionUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.util.ProcessLogger;
import org.kuali.rice.krad.uif.util.ScriptUtils;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.uif.widget.Inquiry;
import org.kuali.rice.krad.uif.widget.Widget;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.GrowlMessage;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.valuefinder.ValueFinder;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.util.ClassUtils;
import org.springframework.util.MethodInvoker;

/**
 * Default Implementation of <code>ViewHelperService</code>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewHelperServiceImpl implements ViewHelperService, Serializable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ViewHelperServiceImpl.class);

    private transient BusinessObjectService businessObjectService;
    private transient PersistenceService persistenceService;
    private transient PersistenceStructureService persistenceStructureService;
    private transient DataDictionaryService dataDictionaryService;
    private transient ExpressionEvaluator expressionEvaluator;
    private transient ViewDictionaryService viewDictionaryService;
    private transient ConfigurationService configurationService;

    /**
     * Uses reflection to find all fields defined on the <code>View</code> instance that have the
     * <code>RequestParameter</code> annotation (which indicates the field may be populated by the
     * request).
     * 
     * <p>
     * For each field found, if there is a corresponding key/value pair in the request parameters,
     * the value is used to populate the field. In addition, any conditional properties of
     * <code>PropertyReplacers</code> configured for the field are cleared so that the request
     * parameter value does not get overridden by the dictionary conditional logic
     * </p>
     * 
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#populateViewFromRequestParameters(org.kuali.rice.krad.uif.view.View,
     *      java.util.Map)
     */
    @Override
    public void populateViewFromRequestParameters(View view, Map<String, String> parameters) {

        // build Map of property replacers by property name so that we can remove them
        // if the property was set by a request parameter
        Map<String, Set<PropertyReplacer>> viewPropertyReplacers = new HashMap<String, Set<PropertyReplacer>>();
        List<PropertyReplacer> propertyReplacerSource = view.getPropertyReplacers();
        if (propertyReplacerSource != null) {
            for (PropertyReplacer replacer : propertyReplacerSource) {
                String replacerPropertyName = replacer.getPropertyName();
                Set<PropertyReplacer> propertyReplacers = viewPropertyReplacers.get(replacerPropertyName);

                if (propertyReplacers == null) {
                    viewPropertyReplacers.put(replacerPropertyName,
                            propertyReplacers = new HashSet<PropertyReplacer>());
                }

                propertyReplacers.add(replacer);
            }
        }

        Map<String, Annotation> annotatedFields = CloneUtils.getFieldsWithAnnotation(view.getClass(),
                RequestParameter.class);

        // for each request parameter allowed on the view, if the request contains a value use
        // to set on View, and clear and conditional expressions or property replacers for that field
        Map<String, String> viewRequestParameters = new HashMap<String, String>();
        for (String fieldToPopulate : annotatedFields.keySet()) {
            RequestParameter requestParameter = (RequestParameter) annotatedFields.get(fieldToPopulate);

            // use specified parameter name if given, else use field name to retrieve parameter value
            String requestParameterName = requestParameter.parameterName();
            if (StringUtils.isBlank(requestParameterName)) {
                requestParameterName = fieldToPopulate;
            }

            if (!parameters.containsKey(requestParameterName)) {
                continue;
            }

            String fieldValue = parameters.get(requestParameterName);
            if (StringUtils.isNotBlank(fieldValue)) {
                viewRequestParameters.put(requestParameterName, fieldValue);
                ObjectPropertyUtils.setPropertyValue(view, fieldToPopulate, fieldValue);

                // remove any conditional configuration so value is not
                // overridden later during the apply model phase
                if (view.getPropertyExpressions().containsKey(fieldToPopulate)) {
                    view.getPropertyExpressions().remove(fieldToPopulate);
                }

                if (viewPropertyReplacers.containsKey(fieldToPopulate)) {
                    Set<PropertyReplacer> propertyReplacers = viewPropertyReplacers.get(fieldToPopulate);
                    for (PropertyReplacer replacer : propertyReplacers) {
                        view.getPropertyReplacers().remove(replacer);
                    }
                }
            }
        }

        view.setViewRequestParameters(viewRequestParameters);
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        view.assignComponentIds(view);

        // increment the id sequence so components added later to the static view components
        // will not conflict with components on the page when navigation happens
        view.setIdSequence(100000);
        performComponentInitialization(view, model, view);

        // initialize the expression evaluator impl
        getExpressionEvaluator().initializeEvaluationContext(model);

        // get the list of dialogs from the view and then set the refreshedByAction on the dialog to true.
        // This will leave the component in the viewIndex to be updated using an AJAX call
        // TODO: Figure out a better way to store dialogs only if it is rendered using an ajax request
        for (Component dialog : view.getDialogs()) {
            dialog.setRefreshedByAction(true);
        }
    }

/**
     * Performs the complete component lifecycle on the component passed in, in this order:
     * performComponentInitialization, performComponentApplyModel, and performComponentFinalize.
     *
     * @see {@link org.kuali.rice.krad.uif.service.ViewHelperService#performComponentLifecycle(
     *org.kuali.rice.krad.uif.view.View, java.lang.Object, org.kuali.rice.krad.uif.component.Component,
     *      java.lang.String)
     * @see {@link #performComponentInitialization(org.kuali.rice.krad.uif.view.View, Object,
     *      org.kuali.rice.krad.uif.component.Component)}
     * @see {@link #performComponentApplyModel(View, Component, Object)}
     * @see {@link #performComponentFinalize(View, Component, Object, Component, Map)}
     */
    public void performComponentLifecycle(View view, Object model, Component component, String origId) {
        Component origComponent = view.getViewIndex().getComponentById(origId);

        view.assignComponentIds(component);

        Map<String, Object> origContext = origComponent.getContext();

        Component parent = origContext == null ? null : (Component) origContext
                .get(UifConstants.ContextVariableNames.PARENT);

        // update context on all components within the refresh component to catch context set by parent
        if (origContext != null) {
            component.pushAllToContext(origContext);

            List<Component> nestedComponents = ComponentUtils.getAllNestedComponents(component);
            for (Component nestedComponent : nestedComponents) {
                nestedComponent.pushAllToContext(origContext);
            }
        }

        // make sure the dataAttributes are the same as original
        component.setDataAttributes(origComponent.getDataAttributes());

        // initialize the expression evaluator
        view.getViewHelperService().getExpressionEvaluator().initializeEvaluationContext(model);

        // the expression graph for refreshed components is captured in the view index (initially it might expressions
        // might have come from a parent), after getting the expression graph then we need to populate the expressions
        // on the configurable for which they apply
        Map<String, String> expressionGraph = view.getViewIndex().getComponentExpressionGraphs().get(
                component.getBaseId());
        component.setExpressionGraph(expressionGraph);
        ExpressionUtils.populatePropertyExpressionsFromGraph(component, false);

        // binding path should stay the same
        if (component instanceof DataBinding) {
            ((DataBinding) component).setBindingInfo(((DataBinding) origComponent).getBindingInfo());
            ((DataBinding) component).getBindingInfo().setBindingPath(
                    ((DataBinding) origComponent).getBindingInfo().getBindingPath());
        }

        // copy properties that are set by parent components in the full view lifecycle
        if (component instanceof Field) {
            ((Field) component).setLabelRendered(((Field) origComponent).isLabelRendered());
        }

        if (origComponent.isRefreshedByAction()) {
            component.setRefreshedByAction(true);
        }

        // reset data if needed
        if (component.isResetDataOnRefresh()) {
            // TODO: this should handle groups as well, going through nested data fields
            if (component instanceof DataField) {
                // TODO: should check default value

                // clear value
                ObjectPropertyUtils.initializeProperty(model,
                        ((DataField) component).getBindingInfo().getBindingPath());
            }
        }

        performComponentInitialization(view, model, component);

        // adjust IDs for suffixes that might have been added by a parent component during the full view lifecycle
        String suffix = StringUtils.replaceOnce(origComponent.getId(), origComponent.getBaseId(), "");
        if (StringUtils.isNotBlank(suffix)) {
            ComponentUtils.updateIdWithSuffix(component, suffix);
            ComponentUtils.updateChildIdsWithSuffixNested(component, suffix);
        }

        // for collections that are nested in the refreshed group, we need to adjust the binding prefix and
        // set the sub collection id prefix from the original component (this is needed when the group being
        // refreshed is part of another collection)
        if (component instanceof Group || component instanceof FieldGroup) {
            List<CollectionGroup> origCollectionGroups = ComponentUtils.getComponentsOfTypeShallow(origComponent,
                    CollectionGroup.class);
            List<CollectionGroup> collectionGroups = ComponentUtils.getComponentsOfTypeShallow(component,
                    CollectionGroup.class);

            for (int i = 0; i < collectionGroups.size(); i++) {
                CollectionGroup origCollectionGroup = origCollectionGroups.get(i);
                CollectionGroup collectionGroup = collectionGroups.get(i);

                String prefix = origCollectionGroup.getBindingInfo().getBindByNamePrefix();
                if (StringUtils.isNotBlank(prefix) && StringUtils.isBlank(
                        collectionGroup.getBindingInfo().getBindByNamePrefix())) {
                    ComponentUtils.prefixBindingPath(collectionGroup, prefix);
                }

                String lineSuffix = origCollectionGroup.getSubCollectionSuffix();
                collectionGroup.setSubCollectionSuffix(lineSuffix);
            }

            // Handle LightTables, as well
            List<LightTable> origLightTables = ComponentUtils.getComponentsOfTypeShallow(origComponent,
                    LightTable.class);
            List<LightTable> lightTables = ComponentUtils.getComponentsOfTypeShallow(component,
                    LightTable.class);

            for (int i = 0; i < lightTables.size(); i++) {
                LightTable origLightTable = origLightTables.get(i);
                LightTable lightTable = lightTables.get(i);

                String prefix = origLightTable.getBindingInfo().getBindByNamePrefix();
                if (StringUtils.isNotBlank(prefix) && StringUtils.isBlank(
                        lightTable.getBindingInfo().getBindByNamePrefix())) {
                    ComponentUtils.prefixBindingPath(lightTable, prefix);
                }
            }
        }

        // if disclosed by action and request was made, make sure the component will display
        if (component.isDisclosedByAction()) {
            ComponentUtils.setComponentPropertyFinal(component, UifPropertyPaths.RENDER, true);
            ComponentUtils.setComponentPropertyFinal(component, UifPropertyPaths.HIDDEN, false);
        }

        performComponentApplyModel(view, component, model, new HashMap<String, Integer>());
        view.getViewIndex().indexComponent(component);

        // adjust nestedLevel property on some specific collection cases
        if (component instanceof Container) {
            ComponentUtils.adjustNestedLevelsForTableCollections((Container) component, 0);
        } else if (component instanceof FieldGroup) {
            ComponentUtils.adjustNestedLevelsForTableCollections(((FieldGroup) component).getGroup(), 0);
        }

        performComponentFinalize(view, component, model, parent);

        // make sure id, binding, and label settings stay the same as initial
        if (component instanceof Group || component instanceof FieldGroup) {
            List<Component> nestedGroupComponents = ComponentUtils.getAllNestedComponents(component);
            List<Component> originalNestedGroupComponents = ComponentUtils.getAllNestedComponents(origComponent);

            for (Component nestedComponent : nestedGroupComponents) {
                Component origNestedComponent = ComponentUtils.findComponentInList(originalNestedGroupComponents,
                        nestedComponent.getId());

                if (origNestedComponent != null) {
                    // update binding
                    if (nestedComponent instanceof DataBinding) {
                        ((DataBinding) nestedComponent).setBindingInfo(
                                ((DataBinding) origNestedComponent).getBindingInfo());
                        ((DataBinding) nestedComponent).getBindingInfo().setBindingPath(
                                ((DataBinding) origNestedComponent).getBindingInfo().getBindingPath());
                    }

                    // update label rendered flag
                    if (nestedComponent instanceof Field) {
                        ((Field) nestedComponent).setLabelRendered(((Field) origNestedComponent).isLabelRendered());
                    }

                    if (origNestedComponent.isRefreshedByAction()) {
                        nestedComponent.setRefreshedByAction(true);
                    }
                }
            }
        }

        // get script for generating growl messages
        String growlScript = buildGrowlScript(view);
        ((ViewModel) model).setGrowlScript(growlScript);

        view.getViewIndex().indexComponent(component);
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#spawnSubLifecyle(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component,
     *      org.kuali.rice.krad.uif.component.Component, java.lang.String, java.lang.String)
     */
    @Override
    public void spawnSubLifecyle(View view, Object model, Component component, Component parent, String startPhase,
            String endPhase) {
        if (component == null) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Spawning sub-lifecycle for component: " + component.getId());
        }

        if (StringUtils.isBlank(component.getId())) {
            view.assignComponentIds(component);
        }

        if (StringUtils.isBlank(startPhase)) {
            startPhase = UifConstants.ViewPhases.INITIALIZE;
        } else if (!UifConstants.ViewPhases.INITIALIZE.equals(startPhase) && !UifConstants.ViewPhases.APPLY_MODEL
                .equals(startPhase) && !UifConstants.ViewPhases.FINALIZE.equals(startPhase)) {
            throw new RuntimeException("Invalid start phase given: " + startPhase);
        }

        if (StringUtils.isBlank(endPhase)) {
            endPhase = UifConstants.ViewPhases.FINALIZE;
        } else if (!UifConstants.ViewPhases.INITIALIZE.equals(endPhase) && !UifConstants.ViewPhases.APPLY_MODEL.equals(
                endPhase) && !UifConstants.ViewPhases.FINALIZE.equals(endPhase)) {
            throw new RuntimeException("Invalid end phase given: " + endPhase);
        }

        if (UifConstants.ViewPhases.INITIALIZE.equals(startPhase)) {
            performComponentInitialization(view, model, component);
            view.getViewIndex().indexComponent(component);

            startPhase = UifConstants.ViewPhases.APPLY_MODEL;
        }

        if (UifConstants.ViewPhases.INITIALIZE.equals(endPhase)) {
            return;
        }

        component.pushObjectToContext(UifConstants.ContextVariableNames.PARENT, parent);

        if (UifConstants.ViewPhases.APPLY_MODEL.equals(startPhase)) {
            performComponentApplyModel(view, component, model, new HashMap<String, Integer>());
            view.getViewIndex().indexComponent(component);
        }

        if (UifConstants.ViewPhases.APPLY_MODEL.equals(endPhase)) {
            return;
        }

        performComponentFinalize(view, component, model, parent);
        view.getViewIndex().indexComponent(component);
    }

    /**
     * Performs initialization of a component by these steps:
     * 
     * <ul>
     * <li>For <code>DataField</code> instances, set defaults from the data dictionary.</li>
     * <li>Invoke the initialize method on the component. Here the component can setup defaults and
     * do other initialization that is specific to that component.</li>
     * <li>Invoke any configured <code>ComponentModifier</code> instances for the component.</li>
     * <li>Call the component to get the List of components that are nested within and recursively
     * call this method to initialize those components.</li>
     * <li>Call custom initialize hook for service overrides</li>
     * </ul>
     * 
     * <p>
     * Note the order various initialize points are called, this can sometimes be an important
     * factor to consider when initializing a component
     * </p>
     * 
     * @throws RiceRuntimeException if the component id or factoryId is not specified
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#performComponentInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    public void performComponentInitialization(View view, Object model, Component component) {
        if (component == null) {
            return;
        }

        if (StringUtils.isBlank(component.getId())) {
            throw new RiceRuntimeException("Id is not set, this should not happen unless a component is misconfigured");
        }

        // TODO: duplicate ID check

        LOG.debug("Initializing component: " + component.getId() + " with type: " + component.getClass());

        // add initial state to the view index for component refreshes
        if (!(component instanceof View)) {
            view.getViewIndex().addInitialComponentStateIfNeeded(component);
        }

        // the component can have an expression graph for which the expressions need pulled to
        // the list the expression service will evaluate
        ExpressionUtils.populatePropertyExpressionsFromGraph(component, true);

        // invoke component to initialize itself after properties have been set
        component.performInitialization(view, model);

        // move expressions on property replacers and component modifiers
        List<PropertyReplacer> componentPropertyReplacers = component.getPropertyReplacers();
        if (componentPropertyReplacers != null) {
            for (PropertyReplacer replacer : componentPropertyReplacers) {
                ExpressionUtils.populatePropertyExpressionsFromGraph(replacer, true);
            }
        }

        List<ComponentModifier> componentModifiers = component.getComponentModifiers();
        if (componentModifiers != null) {
            for (ComponentModifier modifier : component.getComponentModifiers()) {
                ExpressionUtils.populatePropertyExpressionsFromGraph(modifier, true);
            }
        }

        // for attribute fields, set defaults from dictionary entry
        if (component instanceof DataField) {
            initializeDataFieldFromDataDictionary(view, (DataField) component);
        }

        if (component instanceof Container) {
            // invoke hook point for adding components through code
            addCustomContainerComponents(view, model, (Container) component);

            // process any remote fields holder that might be in the containers items, collection items will get
            // processed as the lines are being built
            if (!(component instanceof CollectionGroup)) {
                processAnyRemoteFieldsHolder(view, model, (Container) component);
            }
        }

        // for collection groups set defaults from dictionary entry
        if (component instanceof CollectionGroup) {
            // TODO: initialize from dictionary
        }

        // invoke initialize service hook
        performCustomInitialization(view, component);

        // invoke component modifiers setup to run in the initialize phase
        runComponentModifiers(view, component, null, UifConstants.ViewPhases.INITIALIZE);

        // initialize nested components
        for (Component nestedComponent : component.getComponentsForLifecycle()) {
            performComponentInitialization(view, model, nestedComponent);
        }

        // initialize component prototypes
        for (Component nestedComponent : component.getComponentPrototypes()) {
            performComponentInitialization(view, model, nestedComponent);
        }
    }

    /**
     * Iterates through the containers configured items checking for
     * <code>RemotableFieldsHolder</code>, if found the holder is invoked to retrieved the remotable
     * fields and translate to attribute fields. The translated list is then inserted into the
     * container item list at the position of the holder
     * 
     * @param view view instance containing the container
     * @param model object instance containing the view data
     * @param container container instance to check for any remotable fields holder
     */
    protected void processAnyRemoteFieldsHolder(View view, Object model, Container container) {
        List<Component> processedItems = new ArrayList<Component>();

        // check for holders and invoke to retrieve the remotable fields and translate
        // translated fields are placed into the container item list at the position of the holder
        for (Component item : container.getItems()) {
            if (item instanceof RemoteFieldsHolder) {
                List<InputField> translatedFields = ((RemoteFieldsHolder) item).fetchAndTranslateRemoteFields(view,
                        model, container);
                processedItems.addAll(translatedFields);
            } else {
                processedItems.add(item);
            }
        }

        // updated container items
        container.setItems(processedItems);
    }

    /**
     * Sets properties of the <code>InputField</code> (if blank) to the corresponding attribute
     * entry in the data dictionary
     * 
     * @param view view instance containing the field
     * @param field data field instance to initialize
     */
    protected void initializeDataFieldFromDataDictionary(View view, DataField field) {
        AttributeDefinition attributeDefinition = null;

        String dictionaryAttributeName = field.getDictionaryAttributeName();
        String dictionaryObjectEntry = field.getDictionaryObjectEntry();

        // if entry given but not attribute name, use field name as attribute
        // name
        if (StringUtils.isNotBlank(dictionaryObjectEntry) && StringUtils.isBlank(dictionaryAttributeName)) {
            dictionaryAttributeName = field.getPropertyName();
        }

        // if dictionary entry and attribute set, attempt to find definition
        if (StringUtils.isNotBlank(dictionaryAttributeName) && StringUtils.isNotBlank(dictionaryObjectEntry)) {
            attributeDefinition = getDataDictionaryService().getAttributeDefinition(dictionaryObjectEntry,
                    dictionaryAttributeName);
        }

        // if definition not found, recurse through path
        if (attributeDefinition == null) {
            String propertyPath = field.getBindingInfo().getBindingPath();
            if (StringUtils.isNotBlank(field.getBindingInfo().getCollectionPath())) {
                propertyPath = field.getBindingInfo().getCollectionPath();
                if (StringUtils.isNotBlank(field.getBindingInfo().getBindByNamePrefix())) {
                    propertyPath += "." + field.getBindingInfo().getBindByNamePrefix();
                }
                propertyPath += "." + field.getBindingInfo().getBindingName();
            }

            attributeDefinition = findNestedDictionaryAttribute(view, field, null, propertyPath);
        }

        // if a definition was found, initialize field from definition
        if (attributeDefinition != null) {
            field.copyFromAttributeDefinition(view, attributeDefinition);
        }

        // if control still null, assign default
        if (field instanceof InputField) {
            InputField inputField = (InputField) field;
            if (inputField.getControl() == null) {
                Control control = ComponentFactory.getTextControl();
                view.assignComponentIds(control);

                inputField.setControl(control);
            }
        }
    }

    /**
     * Recursively drills down the property path (if nested) to find an AttributeDefinition, the
     * first attribute definition found will be returned
     * 
     * <p>
     * e.g. suppose parentPath is 'document' and propertyPath is 'account.subAccount.name', first
     * the property type for document will be retrieved using the view metadata and used as the
     * dictionary entry, with the propertyPath as the dictionary attribute, if an attribute
     * definition exists it will be returned. Else, the first part of the property path is added to
     * the parent, making the parentPath 'document.account' and the propertyPath 'subAccount.name',
     * the method is then called again to perform the process with those parameters. The recursion
     * continues until an attribute field is found, or the propertyPath is no longer nested
     * </p>
     * 
     * @param view view instance containing the field
     * @param field field we are attempting to find a supporting attribute definition for
     * @param parentPath parent path to use for getting the dictionary entry
     * @param propertyPath path of the property relative to the parent, to use as dictionary
     *        attribute and to drill down on
     * @return AttributeDefinition if found, or Null
     */
    protected AttributeDefinition findNestedDictionaryAttribute(View view, DataField field, String parentPath,
            String propertyPath) {
        AttributeDefinition attributeDefinition = null;

        // attempt to find definition for parent and property
        String dictionaryAttributeName = propertyPath;
        String dictionaryObjectEntry = null;

        if (field.getBindingInfo().isBindToMap()) {
            parentPath = "";
            if (!field.getBindingInfo().isBindToForm() && StringUtils.isNotBlank(
                    field.getBindingInfo().getBindingObjectPath())) {
                parentPath = field.getBindingInfo().getBindingObjectPath();
            }
            if (StringUtils.isNotBlank(field.getBindingInfo().getBindByNamePrefix())) {
                if (StringUtils.isNotBlank(parentPath)) {
                    parentPath += "." + field.getBindingInfo().getBindByNamePrefix();
                } else {
                    parentPath = field.getBindingInfo().getBindByNamePrefix();
                }
            }

            dictionaryAttributeName = field.getBindingInfo().getBindingName();
        }

        if (StringUtils.isNotBlank(parentPath)) {
            Class<?> dictionaryModelClass = ViewModelUtils.getPropertyTypeByClassAndView(view, parentPath);
            if (dictionaryModelClass != null) {
                dictionaryObjectEntry = dictionaryModelClass.getName();

                attributeDefinition = getDataDictionaryService().getAttributeDefinition(dictionaryObjectEntry,
                        dictionaryAttributeName);
            }
        }

        // if definition not found and property is still nested, recurse down
        // one level
        if ((attributeDefinition == null) && StringUtils.contains(propertyPath, ".")) {
            String nextParentPath = StringUtils.substringBefore(propertyPath, ".");
            if (StringUtils.isNotBlank(parentPath)) {
                nextParentPath = parentPath + "." + nextParentPath;
            }
            String nextPropertyPath = StringUtils.substringAfter(propertyPath, ".");

            return findNestedDictionaryAttribute(view, field, nextParentPath, nextPropertyPath);
        }

        // if a definition was found, update the fields dictionary properties
        if (attributeDefinition != null) {
            field.setDictionaryAttributeName(dictionaryAttributeName);
            field.setDictionaryObjectEntry(dictionaryObjectEntry);
        }

        return attributeDefinition;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#performApplyModel(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performApplyModel(View view, Object model) {
        ProcessLogger.trace("apply-model:" + view.getId());

        // apply default values if they have not been applied yet
        if (!((ViewModel) model).isDefaultsApplied()) {
            applyDefaultValues(view, view, model);
            ((ViewModel) model).setDefaultsApplied(true);
        }

        // get action flag and edit modes from authorizer/presentation controller
        retrieveEditModesAndActionFlags(view, (UifFormBase) model);

        // set view context for conditional expressions
        setViewContext(view, model);

        ProcessLogger.trace("apply-comp-model:" + view.getId());
        Map<String, Integer> visitedIds = new HashMap<String, Integer>();
        performComponentApplyModel(view, view, model, visitedIds);
        ProcessLogger.trace("apply-model-end:" + view.getId());
    }

    /**
     * Invokes the configured <code>PresentationController</code> and </code>Authorizer</code> for
     * the view to get the exported action flags and edit modes that can be used in conditional
     * logic
     * 
     * @param view view instance that is being built and presentation/authorizer pulled for
     * @param model Object that contains the model data
     */
    protected void retrieveEditModesAndActionFlags(View view, UifFormBase model) {
        ViewPresentationController presentationController = view.getPresentationController();
        ViewAuthorizer authorizer = view.getAuthorizer();

        Set<String> actionFlags = presentationController.getActionFlags(view, model);
        Set<String> editModes = presentationController.getEditModes(view, model);

        // if user session is not established cannot invoke authorizer
        if (GlobalVariables.getUserSession() != null) {
            Person user = GlobalVariables.getUserSession().getPerson();

            actionFlags = authorizer.getActionFlags(view, model, user, actionFlags);
            editModes = authorizer.getEditModes(view, model, user, editModes);
        }

        view.setActionFlags(new BooleanMap(actionFlags));
        view.setEditModes(new BooleanMap(editModes));
    }

    /**
     * Sets up the view context which will be available to other components through their context
     * for conditional logic evaluation
     * 
     * @param view view instance to set context for
     * @param model object containing the view data
     */
    protected void setViewContext(View view, Object model) {
        view.pushAllToContext(getPreModelContext(view));

        // evaluate view expressions for further context
        for (Entry<String, String> variableExpression : view.getExpressionVariables().entrySet()) {
            String variableName = variableExpression.getKey();
            Object value = getExpressionEvaluator().evaluateExpression(view.getContext(),
                    variableExpression.getValue());
            view.pushObjectToContext(variableName, value);
        }
    }

    /**
     * Returns the general context that is available before the apply model phase (during the
     * initialize phase)
     * 
     * @param view view instance for context
     * @return context map
     */
    protected Map<String, Object> getPreModelContext(View view) {
        Map<String, Object> context = new HashMap<String, Object>();

        context.put(UifConstants.ContextVariableNames.VIEW, view);
        context.put(UifConstants.ContextVariableNames.VIEW_HELPER, this);

        Map<String, String> properties = CoreApiServiceLocator.getKualiConfigurationService().getAllProperties();
        context.put(UifConstants.ContextVariableNames.CONFIG_PROPERTIES, properties);
        context.put(UifConstants.ContextVariableNames.CONSTANTS, KRADConstants.class);
        context.put(UifConstants.ContextVariableNames.UIF_CONSTANTS, UifConstants.class);

        return context;
    }

    /**
     * Applies the model data to a component of the View instance
     * 
     * <p>
     * The component is invoked to to apply the model data. Here the component can generate any
     * additional fields needed or alter the configured fields. After the component is invoked a
     * hook for custom helper service processing is invoked. Finally the method is recursively
     * called for all the component children
     * </p>
     * 
     * @param view view instance the component belongs to
     * @param component the component instance the model should be applied to
     * @param model top level object containing the data
     * @param visitedIds tracks components ids that have been seen for adjusting duplicates
     */
    protected void performComponentApplyModel(View view, Component component, Object model,
            Map<String, Integer> visitedIds) {
        if (component == null) {
            return;
        }

        // ProcessLogger.ntrace("comp-model:", ":" + component.getClass().getSimpleName(), 500);
        // ProcessLogger.countBegin("comp-model");

        // set context on component for evaluating expressions
        component.pushAllToContext(getCommonContext(view, component));

        List<PropertyReplacer> componentPropertyReplacers = component.getPropertyReplacers();
        if (componentPropertyReplacers != null) {
            for (PropertyReplacer replacer : componentPropertyReplacers) {
                getExpressionEvaluator().evaluateExpressionsOnConfigurable(view, replacer, component.getContext());
            }
        }

        List<ComponentModifier> componentModifiers = component.getComponentModifiers();
        if (componentModifiers != null) {
            for (ComponentModifier modifier : component.getComponentModifiers()) {
                getExpressionEvaluator().evaluateExpressionsOnConfigurable(view, modifier, component.getContext());
            }
        }

        getExpressionEvaluator().evaluateExpressionsOnConfigurable(view, component, component.getContext());

        // evaluate expressions on component security
        ComponentSecurity componentSecurity = component.getComponentSecurity();
        getExpressionEvaluator().evaluateExpressionsOnConfigurable(view, componentSecurity, component.getContext());

        // evaluate expressions on the binding info object
        if (component instanceof DataBinding) {
            BindingInfo bindingInfo = ((DataBinding) component).getBindingInfo();
            getExpressionEvaluator().evaluateExpressionsOnConfigurable(view, bindingInfo, component.getContext());
        }

        // set context evaluate expressions on the layout manager
        if (component instanceof Container) {
            LayoutManager layoutManager = ((Container) component).getLayoutManager();

            if (layoutManager != null) {
                layoutManager.pushAllToContext(getCommonContext(view, component));
                layoutManager.pushObjectToContext(UifConstants.ContextVariableNames.PARENT, component);
                layoutManager.pushObjectToContext(UifConstants.ContextVariableNames.MANAGER, layoutManager);

                getExpressionEvaluator().evaluateExpressionsOnConfigurable(view, layoutManager,
                        layoutManager.getContext());

                layoutManager.setId(adjustIdIfNecessary(layoutManager.getId(), visitedIds));
            }
        }

        // sync the component with previous client side state
        syncClientSideStateForComponent(component, ((ViewModel) model).getClientStateForSyncing());

        // invoke authorizer and presentation controller to set component state
        applyAuthorizationAndPresentationLogic(view, component, (ViewModel) model);

        // adjust ids for duplicates if necessary
        //component.setId(adjustIdIfNecessary(component.getId(), visitedIds));

        // invoke component to perform its conditional logic
        Map<String, Object> parentContext = component.getContext();
        Component parent = parentContext == null ? null : (Component) parentContext
                .get(UifConstants.ContextVariableNames.PARENT);

        component.performApplyModel(view, model, parent);

        // invoke service override hook
        performCustomApplyModel(view, component, model);

        // invoke component modifiers configured to run in the apply model phase
        runComponentModifiers(view, component, model, UifConstants.ViewPhases.APPLY_MODEL);

        // ProcessLogger.countEnd("comp-model", view.getId() + " " + component.getClass() + " " + component.getId());

        // get children and recursively perform conditional logic
        Queue<Component> nested = new LinkedList<Component>();
        for (Component nestedComponent : component.getComponentsForLifecycle()) {
            if (nestedComponent != null) {
                nested.offer(nestedComponent);
            }
        }

        if (!nested.isEmpty()) {
            // ProcessLogger.countBegin("comp-nest");
            while (!nested.isEmpty()) {
                Component nestedComponent = nested.poll();
                nestedComponent.pushObjectToContext(UifConstants.ContextVariableNames.PARENT, component);
                performComponentApplyModel(view, nestedComponent, model, visitedIds);
            }
            // ProcessLogger.countEnd("comp-nest", view.getId() + " " + component.getClass() + " " + component.getId());
        }
    }

    /**
     * Checks against the visited ids to see if the id is duplicate, if so it is adjusted to make an
     * unique id by appending an unique sequence number
     * 
     * @param id id to adjust if necessary
     * @param visitedIds tracks components ids that have been seen for adjusting duplicates
     * @return original or adjusted id
     */
    protected String adjustIdIfNecessary(String id, Map<String, Integer> visitedIds) {
        String adjustedId = id;

        if (visitedIds.containsKey(id)) {
            Integer nextAdjustSeq = visitedIds.get(id);
            adjustedId = id + nextAdjustSeq;

            // verify the adjustedId does not already exist
            while (visitedIds.containsKey(adjustedId)) {
                nextAdjustSeq = nextAdjustSeq + 1;
                adjustedId = id + nextAdjustSeq;
            }

            visitedIds.put(adjustedId, Integer.valueOf(1));

            nextAdjustSeq = nextAdjustSeq + 1;
            visitedIds.put(id, nextAdjustSeq);
        } else {
            visitedIds.put(id, Integer.valueOf(1));
        }

        return adjustedId;
    }

    /**
     * Invokes the view's configured {@link ViewAuthorizer} and {@link ViewPresentationController}
     * to set state of the component
     * 
     * <p>
     * The following authorization is done here: Fields: edit, view, required, mask, and partial
     * mask Groups: edit and view Actions: take action
     * </p>
     * 
     * <p>
     * Note additional checks are also done for fields that are part of a collection group. This
     * authorization is found in {@link org.kuali.rice.krad.uif.container.CollectionGroupBuilder}
     * </p>
     * 
     * @param view view instance the component belongs to and from which the authorizer and
     *        presentation controller will be pulled
     * @param component component instance to authorize
     * @param model model object containing the data for the view
     */
    protected void applyAuthorizationAndPresentationLogic(View view, Component component, ViewModel model) {
        ViewPresentationController presentationController = view.getPresentationController();
        ViewAuthorizer authorizer = view.getAuthorizer();

        // if user session is not established cannot perform authorization
        if (GlobalVariables.getUserSession() == null) {
            return;
        }

        Person user = GlobalVariables.getUserSession().getPerson();

        // if component not flagged for render no need to check auth and controller logic
        if (!component.isRender()) {
            return;
        }

        // check top level view edit authorization
        if (component instanceof View) {
            if (!view.isReadOnly()) {
                boolean canEditView = authorizer.canEditView(view, model, user);
                if (canEditView) {
                    canEditView = presentationController.canEditView(view, model);
                }
                view.setReadOnly(!canEditView);
            }
        }

        // perform group authorization and presentation logic
        else if (component instanceof Group) {
            Group group = (Group) component;

            // if group is not hidden, do authorization for viewing the group
            if (!group.isHidden()) {
                boolean canViewGroup = authorizer.canViewGroup(view, model, group, group.getId(), user);
                if (canViewGroup) {
                    canViewGroup = presentationController.canViewGroup(view, model, group, group.getId());
                }
                group.setHidden(!canViewGroup);
                group.setRender(canViewGroup);
            }

            // if group is editable, do authorization for editing the group
            if (!group.isReadOnly()) {
                boolean canEditGroup = authorizer.canEditGroup(view, model, group, group.getId(), user);
                if (canEditGroup) {
                    canEditGroup = presentationController.canEditGroup(view, model, group, group.getId());
                }
                group.setReadOnly(!canEditGroup);
            }
        }

        // perform field authorization and presentation logic
        else if (component instanceof Field && !(component instanceof ActionField)) {
            Field field = (Field) component;

            String propertyName = null;
            if (field instanceof DataBinding) {
                propertyName = ((DataBinding) field).getPropertyName();
            }

            // if field is not hidden, do authorization for viewing the field
            if (!field.isHidden()) {
                boolean canViewField = authorizer.canViewField(view, model, field, propertyName, user);
                if (canViewField) {
                    canViewField = presentationController.canViewField(view, model, field, propertyName);
                }
                field.setHidden(!canViewField);
                field.setRender(canViewField);
            }

            // if field is not readOnly, check edit authorization
            if (!field.isReadOnly()) {
                // check field edit authorization
                boolean canEditField = authorizer.canEditField(view, model, field, propertyName, user);
                if (canEditField) {
                    canEditField = presentationController.canEditField(view, model, field, propertyName);
                }
                field.setReadOnly(!canEditField);
            }

            // if field is not already required, invoke presentation logic to determine if it should be
            if ((field.getRequired() == null) || !field.getRequired().booleanValue()) {
                boolean fieldIsRequired = presentationController.fieldIsRequired(view, model, field, propertyName);
            }

            if (field instanceof DataField) {
                DataField dataField = (DataField) field;

                // check mask authorization
                boolean canUnmaskValue = authorizer.canUnmaskField(view, model, dataField, dataField.getPropertyName(),
                        user);
                if (!canUnmaskValue) {
                    dataField.setApplyMask(true);
                    dataField.setMaskFormatter(dataField.getDataFieldSecurity().getAttributeSecurity().
                            getMaskFormatter());
                } else {
                    // check partial mask authorization
                    boolean canPartiallyUnmaskValue = authorizer.canPartialUnmaskField(view, model, dataField,
                            dataField.getPropertyName(), user);
                    if (!canPartiallyUnmaskValue) {
                        dataField.setApplyMask(true);
                        dataField.setMaskFormatter(
                                dataField.getDataFieldSecurity().getAttributeSecurity().getPartialMaskFormatter());
                    }
                }
            }
        }

        // perform action authorization and presentation logic
        else if (component instanceof ActionField || component instanceof Action) {
            Action action = null;
            if (component instanceof ActionField) {
                action = ((ActionField) component).getAction();
            } else {
                action = (Action) component;
            }

            boolean canTakeAction = authorizer.canPerformAction(view, model, action, action.getActionEvent(),
                    action.getId(), user);
            if (canTakeAction) {
                canTakeAction = presentationController.canPerformAction(view, model, action, action.getActionEvent(),
                        action.getId());
            }
            action.setRender(canTakeAction);
        }

        // perform widget authorization and presentation logic
        else if (component instanceof Widget) {
            Widget widget = (Widget) component;

            // if widget is not hidden, do authorization for viewing the widget
            if (!widget.isHidden()) {
                boolean canViewWidget = authorizer.canViewWidget(view, model, widget, widget.getId(), user);
                if (canViewWidget) {
                    canViewWidget = presentationController.canViewWidget(view, model, widget, widget.getId());
                }
                widget.setHidden(!canViewWidget);
                widget.setRender(canViewWidget);
            }

            // if widget is not readOnly, check edit authorization
            if (!widget.isReadOnly()) {
                boolean canEditWidget = authorizer.canEditWidget(view, model, widget, widget.getId(), user);
                if (canEditWidget) {
                    canEditWidget = presentationController.canEditWidget(view, model, widget, widget.getId());
                }
                widget.setReadOnly(!canEditWidget);
            }
        }
    }

    /**
     * Runs any configured <code>ComponentModifiers</code> for the given component that match the
     * given run phase and who run condition evaluation succeeds
     * 
     * <p>
     * If called during the initialize phase, the performInitialization method will be invoked on
     * the <code>ComponentModifier</code> before running
     * </p>
     * 
     * @param view view instance for context
     * @param component component instance whose modifiers should be run
     * @param model model object for context
     * @param runPhase current phase to match on
     */
    protected void runComponentModifiers(View view, Component component, Object model, String runPhase) {
        List<ComponentModifier> componentModifiers = component.getComponentModifiers();
        if (componentModifiers == null) {
            return;
        }

        for (ComponentModifier modifier : component.getComponentModifiers()) {
            // if run phase is initialize, invoke initialize method on modifier first
            if (StringUtils.equals(runPhase, UifConstants.ViewPhases.INITIALIZE)) {
                modifier.performInitialization(view, model, component);
            }

            // check run phase matches
            if (StringUtils.equals(modifier.getRunPhase(), runPhase)) {
                // check condition (if set) evaluates to true
                boolean runModifier = true;
                if (StringUtils.isNotBlank(modifier.getRunCondition())) {
                    Map<String, Object> context = new HashMap<String, Object>();
                    context.put(UifConstants.ContextVariableNames.COMPONENT, component);
                    context.put(UifConstants.ContextVariableNames.VIEW, view);

                    String conditionEvaluation = getExpressionEvaluator().evaluateExpressionTemplate(context,
                            modifier.getRunCondition());
                    runModifier = Boolean.parseBoolean(conditionEvaluation);
                }

                if (runModifier) {
                    modifier.performModification(view, model, component);
                }
            }
        }
    }

    /**
     * Gets global objects for the context map and pushes them to the context for the component
     * 
     * @param view view instance for component
     * @param component component instance to push context to
     */
    public Map<String, Object> getCommonContext(View view, Component component) {
        Map<String, Object> context = new HashMap<String, Object>();

        Map<String, Object> viewContext = view.getContext();
        if (viewContext != null) {
            context.putAll(view.getContext());
        }

        context.put(UifConstants.ContextVariableNames.THEME_IMAGES, view.getTheme().getImageDirectory());
        context.put(UifConstants.ContextVariableNames.COMPONENT, component);

        return context;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performFinalize(View view, Object model) {
        // get script for generating growl messages
        String growlScript = buildGrowlScript(view);
        ((ViewModel) model).setGrowlScript(growlScript);

        performComponentFinalize(view, view, model, null);

        String clientStateScript = buildClientSideStateScript(view, model);
        view.setPreLoadScript(ScriptUtils.appendScript(view.getPreLoadScript(), clientStateScript));

        setExpressionEvaluator(null);
    }

    /**
     * Builds script that will initialize configuration parameters and component state on the client
     * 
     * <p>
     * Here client side state is initialized along with configuration variables that need exposed to
     * script
     * </p>
     * 
     * @param view view instance that is being built
     * @param model model containing the client side state map
     */
    protected String buildClientSideStateScript(View view, Object model) {
        Map<String, Object> clientSideState = ((ViewModel) model).getClientStateForSyncing();

        // script for initializing client side state on load
        String clientStateScript = "";
        if (!clientSideState.isEmpty()) {
            clientStateScript = ScriptUtils.buildFunctionCall(UifConstants.JsFunctions.INITIALIZE_VIEW_STATE,
                    clientSideState);
        }

        // add necessary configuration parameters
        String kradImageLocation = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                UifConstants.ConfigProperties.KRAD_IMAGES_URL);
        clientStateScript += ScriptUtils.buildFunctionCall(UifConstants.JsFunctions.SET_CONFIG_PARM,
                UifConstants.ClientSideVariables.KRAD_IMAGE_LOCATION, kradImageLocation);

        String kradURL = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                UifConstants.ConfigProperties.KRAD_URL);
        clientStateScript += ScriptUtils.buildFunctionCall(UifConstants.JsFunctions.SET_CONFIG_PARM,
                UifConstants.ClientSideVariables.KRAD_URL, kradURL);

        String applicationURL = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ConfigParameters.APPLICATION_URL);
        clientStateScript += ScriptUtils.buildFunctionCall(UifConstants.JsFunctions.SET_CONFIG_PARM,
                UifConstants.ClientSideVariables.APPLICATION_URL, applicationURL);

        return clientStateScript;
    }

    /**
     * Generates formatted table data based on the posted view results and format type
     * 
     * @param view view instance where the table is located
     * @param model top level object containing the data
     * @param tableId id of the table being generated
     * @param formatType format which the table should be generated in
     * @return
     */
    public String buildExportTableData(View view, Object model, String tableId, String formatType) {
        // load table format elements used for generated particular style
        Map<String, String> exportTableFormatOptions = getExportTableFormatOptions(formatType);
        String startTable = exportTableFormatOptions.get("startTable");
        String endTable = exportTableFormatOptions.get("endTable");

        Component component = view.getViewIndex().getComponentById(tableId);
        StringBuilder tableRows = new StringBuilder("");

        // table layout manager is needed for header and gathering field data
        if (component instanceof CollectionGroup && ((CollectionGroup) component)
                .getLayoutManager() instanceof TableLayoutManager) {

            CollectionGroup collectionGroup = (CollectionGroup) component;
            TableLayoutManager layoutManager = (TableLayoutManager) collectionGroup.getLayoutManager();
            List<Label> headerLabels = layoutManager.getHeaderLabels();
            List<Field> rowFields = layoutManager.getAllRowFields();
            int numberOfColumns = layoutManager.getNumberOfColumns();
            List<Integer> ignoredColumns = findIgnoredColumns(layoutManager, collectionGroup);

            // append table header data as first row
            if (headerLabels.size() > 0) {
                List<String> labels = new ArrayList<String>();
                for (Label label : headerLabels) {
                    labels.add(label.getLabelText());
                }

                tableRows.append(buildExportTableRow(labels, exportTableFormatOptions, ignoredColumns));
            }

            // load all subsequent rows to the table
            if (rowFields.size() > 0) {
                List<String> columnData = new ArrayList<String>();
                for (Field field : rowFields) {
                    columnData.add(KRADUtils.getSimpleFieldValue(model, field));
                    if (columnData.size() >= numberOfColumns) {
                        tableRows.append(buildExportTableRow(columnData, exportTableFormatOptions, ignoredColumns));
                        columnData.clear();
                    }
                }
            }
        }

        return startTable + tableRows.toString() + endTable;
    }

    /**
     * Helper method used to build formatted table row data for export
     * 
     * @return
     */
    protected String buildExportTableRow(List<String> columnData, Map<String, String> tableFormatOptions,
            List<Integer> ignoredColumns) {
        String startRow = tableFormatOptions.get("startRow");
        String endRow = tableFormatOptions.get("endRow");
        String startColumn = tableFormatOptions.get("startColumn");
        String endColumn = tableFormatOptions.get("endColumn");
        boolean appendLastColumn = Boolean.valueOf(tableFormatOptions.get("appendLastColumn"));
        int columnIndex = 0;

        StringBuilder builder = new StringBuilder();
        for (String columnItem : columnData) {
            boolean displayColumn = !ignoredColumns.contains(columnIndex);
            if (displayColumn) {
                builder.append(startColumn + columnItem + endColumn);
            }
            if (columnIndex >= columnData.size() - 1 && !appendLastColumn) {
                builder.delete(builder.length() - endColumn.length(), builder.length());
            }
            columnIndex++;
        }

        return startRow + builder.toString() + endRow;
    }

    /**
     * Helper function to determine whether if column should be displayed. Used to help extract
     * columns used in screen format such as action or select that is not needed for export.
     * 
     * @param layoutManager
     * @param collectionGroup
     * @return
     */
    private List<Integer> findIgnoredColumns(TableLayoutManager layoutManager, CollectionGroup collectionGroup) {
        List<Integer> ignoreColumns = new ArrayList<Integer>();
        int actionColumnIndex = layoutManager.getActionColumnIndex();
        int numberOfColumns = layoutManager.getNumberOfColumns();
        boolean renderActions = collectionGroup.isRenderLineActions() && !collectionGroup.isReadOnly();
        boolean renderSelectField = collectionGroup.isIncludeLineSelectionField();
        boolean renderSequenceField = layoutManager.isRenderSequenceField();

        if (renderActions || renderSelectField || renderSequenceField) {
            int shiftColumn = 0;

            if (renderSelectField) {
                ignoreColumns.add(shiftColumn);
                shiftColumn++;
            }
            if (renderSequenceField) {
                ignoreColumns.add(shiftColumn);
                shiftColumn++;
            }
            if (renderActions) {
                if (actionColumnIndex == 1) {
                    ignoreColumns.add(shiftColumn);
                } else if (actionColumnIndex == -1) {
                    ignoreColumns.add(numberOfColumns - 1);
                } else if (actionColumnIndex > 1) {
                    ignoreColumns.add(actionColumnIndex);
                }
            }
        }
        return ignoreColumns;
    }

    /**
     * Identify table formatting elements based on formatType. Defaults to txt format if not found
     * 
     * @param formatType
     * @return
     */
    protected Map<String, String> getExportTableFormatOptions(String formatType) {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("contentType", "text/plain");
        map.put("formatType", "txt");
        map.put("startTable", "");
        map.put("endTable", "");
        map.put("startRow", "");
        map.put("endRow", "\n");
        map.put("startColumn", "");
        map.put("endColumn", ", ");
        map.put("appendLastColumn", "false");

        if ("csv".equals(formatType)) {
            map.put("contentType", "text/csv");
            map.put("formatType", "csv");
            map.put("startTable", "");
            map.put("endTable", "");
            map.put("startRow", "");
            map.put("endRow", "\n");
            map.put("startColumn", "");
            map.put("endColumn", ", ");
            map.put("appendLastColumn", "false");

        } else if ("xls".equals(formatType)) {
            map.put("contentType", "application/vnd.ms-excel");
            map.put("formatType", "xls");
            map.put("startTable", "");
            map.put("endTable", "");
            map.put("startRow", "");
            map.put("endRow", "\n");
            map.put("startColumn", "\"");
            map.put("endColumn", "\"\t");
            map.put("appendLastColumn", "true");

        } else if ("xml".equals(formatType)) {
            map.put("contentType", "application/xml");
            map.put("formatType", "xml");
            map.put("startTable", "<table>\n");
            map.put("endTable", "</table>\n");
            map.put("startRow", "  <row>\n");
            map.put("endRow", "  </row>\n");
            map.put("startColumn", "    <column>");
            map.put("endColumn", "</column>\n");
            map.put("appendLastColumn", "true");

        }

        return map;
    }

    /**
     * Builds JS script that will invoke the show growl method to display a growl message when the
     * page is rendered
     * 
     * <p>
     * A growl call will be created for any explicit growl messages added to the message map.
     * </p>
     * 
     * <p>
     * Growls are only generated if @{link
     * org.kuali.rice.krad.uif.view.View#isGrowlMessagingEnabled()} is enabled. If not, the growl
     * messages are set as info messages for the page
     * </p>
     * 
     * @param view view instance for which growls are being generated
     * @return JS script string for generated growl messages
     */
    protected String buildGrowlScript(View view) {
        String growlScript = "";

        MessageService messageService = KRADServiceLocatorWeb.getMessageService();

        MessageMap messageMap = GlobalVariables.getMessageMap();
        for (GrowlMessage growl : messageMap.getGrowlMessages()) {
            if (view.isGrowlMessagingEnabled()) {
                String message = messageService.getMessageText(growl.getNamespaceCode(), growl.getComponentCode(),
                        growl.getMessageKey());

                if (StringUtils.isNotBlank(message)) {
                    if (growl.getMessageParameters() != null) {
                        message = message.replace("'", "''");
                        message = MessageFormat.format(message, (Object[]) growl.getMessageParameters());
                    }

                    // escape single quotes in message or title since that will cause problem with plugin
                    message = message.replace("'", "\\'");

                    String title = growl.getTitle();
                    if (StringUtils.isNotBlank(growl.getTitleKey())) {
                        title = messageService.getMessageText(growl.getNamespaceCode(), growl.getComponentCode(),
                                growl.getTitleKey());
                    }
                    title = title.replace("'", "\\'");

                    growlScript =
                            growlScript + "showGrowl('" + message + "', '" + title + "', '" + growl.getTheme() + "');";
                }
            } else {
                ErrorMessage infoMessage = new ErrorMessage(growl.getMessageKey(), growl.getMessageParameters());
                infoMessage.setNamespaceCode(growl.getNamespaceCode());
                infoMessage.setComponentCode(growl.getComponentCode());

                messageMap.putInfoForSectionId(KRADConstants.GLOBAL_INFO, infoMessage);
            }
        }

        return growlScript;
    }

    /**
     * Update state of the given component and does final preparation for rendering
     * 
     * @param view view instance the component belongs to
     * @param component the component instance that should be updated
     * @param model top level object containing the data
     * @param parent parent component for the component being finalized
     */
    protected void performComponentFinalize(View view, Component component, Object model, Component parent) {
        if (component == null) {
            return;
        }

        // implement readonly request overrides
        ViewModel viewModel = (ViewModel) model;
        if ((component instanceof DataBinding) && view.isSupportsRequestOverrideOfReadOnlyFields() && !viewModel
                .getReadOnlyFieldsList().isEmpty()) {
            String propertyName = ((DataBinding) component).getPropertyName();
            if (viewModel.getReadOnlyFieldsList().contains(propertyName)) {
                component.setReadOnly(true);
            }
        }

        // invoke configured method finalizers
        invokeMethodFinalizer(view, component, model);

        // invoke component to update its state
        component.performFinalize(view, model, parent);

        // invoke service override hook
        performCustomFinalize(view, component, model, parent);

        // invoke component modifiers setup to run in the finalize phase
        runComponentModifiers(view, component, model, UifConstants.ViewPhases.FINALIZE);

        // add the components template to the views list of components
        if (!component.isSelfRendered() && StringUtils.isNotBlank(component.getTemplate()) &&
                !view.getViewTemplates().contains(component.getTemplate())) {
            view.getViewTemplates().add(component.getTemplate());
        }

        if (component instanceof Container) {
            LayoutManager layoutManager = ((Container) component).getLayoutManager();

            if ((layoutManager != null) && !view.getViewTemplates().contains(layoutManager.getTemplate())) {
                view.getViewTemplates().add(layoutManager.getTemplate());
            }
        }

        // get components children and recursively update state
        for (Component nestedComponent : component.getComponentsForLifecycle()) {
            performComponentFinalize(view, nestedComponent, model, component);
        }
    }

    /**
     * Updates the properties of the given component instance with the value found from the
     * corresponding map of client state (if found)
     * 
     * @param component component instance to update
     * @param clientSideState map of state to sync with
     */
    protected void syncClientSideStateForComponent(Component component, Map<String, Object> clientSideState) {
        // find the map of state that was sent for component (if any)
        Map<String, Object> componentState = null;
        if (component instanceof View) {
            componentState = clientSideState;
        } else {
            if (clientSideState.containsKey(component.getId())) {
                componentState = (Map<String, Object>) clientSideState.get(component.getId());
            }
        }

        // if state was sent, match with fields on the component that are annotated to have client state
        if ((componentState != null) && (!componentState.isEmpty())) {
            Map<String, Annotation> annotatedFields = CloneUtils.getFieldsWithAnnotation(component.getClass(),
                    ClientSideState.class);

            for (Entry<String, Annotation> annotatedField : annotatedFields.entrySet()) {
                ClientSideState clientSideStateAnnot = (ClientSideState) annotatedField.getValue();

                String variableName = clientSideStateAnnot.variableName();
                if (StringUtils.isBlank(variableName)) {
                    variableName = annotatedField.getKey();
                }

                if (componentState.containsKey(variableName)) {
                    Object value = componentState.get(variableName);
                    ObjectPropertyUtils.setPropertyValue(component, annotatedField.getKey(), value);
                }
            }
        }
    }

    /**
     * Invokes the finalize method for the component (if configured) and sets the render output for
     * the component to the returned method string (if method is not a void type)
     * 
     * @param view view instance that contains the component
     * @param component component to run finalize method for
     * @param model top level object containing the data
     */
    protected void invokeMethodFinalizer(View view, Component component, Object model) {
        String finalizeMethodToCall = component.getFinalizeMethodToCall();
        MethodInvoker finalizeMethodInvoker = component.getFinalizeMethodInvoker();

        if (StringUtils.isBlank(finalizeMethodToCall) && (finalizeMethodInvoker == null)) {
            return;
        }

        if (finalizeMethodInvoker == null) {
            finalizeMethodInvoker = new MethodInvoker();
        }

        // if method not set on invoker, use finalizeMethodToCall, note staticMethod could be set(don't know since
        // there is not a getter), if so it will override the target method in prepare
        if (StringUtils.isBlank(finalizeMethodInvoker.getTargetMethod())) {
            finalizeMethodInvoker.setTargetMethod(finalizeMethodToCall);
        }

        // if target class or object not set, use view helper service
        if ((finalizeMethodInvoker.getTargetClass() == null) && (finalizeMethodInvoker.getTargetObject() == null)) {
            finalizeMethodInvoker.setTargetObject(view.getViewHelperService());
        }

        // setup arguments for method
        List<Object> additionalArguments = component.getFinalizeMethodAdditionalArguments();
        if (additionalArguments == null) {
            additionalArguments = new ArrayList<Object>();
        }

        Object[] arguments = new Object[2 + additionalArguments.size()];
        arguments[0] = component;
        arguments[1] = model;

        int argumentIndex = 1;
        for (Object argument : additionalArguments) {
            argumentIndex++;
            arguments[argumentIndex] = argument;
        }
        finalizeMethodInvoker.setArguments(arguments);

        // invoke finalize method
        try {
            LOG.debug("Invoking finalize method: "
                    + finalizeMethodInvoker.getTargetMethod()
                    + " for component: "
                    + component.getId());
            finalizeMethodInvoker.prepare();

            Class<?> methodReturnType = finalizeMethodInvoker.getPreparedMethod().getReturnType();
            if (StringUtils.equals("void", methodReturnType.getName())) {
                finalizeMethodInvoker.invoke();
            } else {
                String renderOutput = (String) finalizeMethodInvoker.invoke();

                component.setSelfRendered(true);
                component.setRenderedHtmlOutput(renderOutput);
            }
        } catch (Exception e) {
            LOG.error("Error invoking finalize method for component: " + component.getId(), e);
            throw new RuntimeException("Error invoking finalize method for component: " + component.getId(), e);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#cleanViewAfterRender(org.kuali.rice.krad.uif.view.View)
     */
    @Override
    public void cleanViewAfterRender(View view) {
        ViewCleaner.cleanView(view);
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#refreshReferences(Object, String)
     */
    public void refreshReferences(Object model, String referencesToRefresh) {
        for (String reference : StringUtils.split(referencesToRefresh, KRADConstants.REFERENCES_TO_REFRESH_SEPARATOR)) {
            if (StringUtils.isBlank(reference)) {
                continue;
            }

            //ToDo: handle add line

            if (ObjectUtils.isNestedAttribute(reference)) {
                String parentPath = ObjectUtils.getNestedAttributePrefix(reference);
                Object parentObject = ObjectPropertyUtils.getPropertyValue(model, parentPath);
                String referenceObjectName = ObjectUtils.getNestedAttributePrimitive(reference);

                if (parentObject == null) {
                    LOG.warn("Unable to refresh references for " + referencesToRefresh +
                            ". Object not found in model. Nothing refreshed.");
                    continue;
                }

                refreshReference(parentObject, referenceObjectName);
            } else {
                refreshReference(model, reference);
            }
        }
    }

    /**
     * Perform a database or data dictionary based refresh of a specific property object
     * 
     * <p>
     * The object needs to be of type PersistableBusinessObject.
     * </p>
     * 
     * @param parentObject parent object that references the object to be refreshed
     * @param referenceObjectName property name of the parent object to be refreshed
     */
    private void refreshReference(Object parentObject, String referenceObjectName) {
        if (!(parentObject instanceof PersistableBusinessObject)) {
            LOG.warn("Could not refresh reference " + referenceObjectName + " off class " + parentObject.getClass()
                    .getName() + ". Class not of type PersistableBusinessObject");
            return;
        }

        if (getPersistenceStructureService().hasReference(parentObject.getClass(), referenceObjectName)
                || getPersistenceStructureService().hasCollection(parentObject.getClass(), referenceObjectName)) {
            // refresh via database mapping
            getPersistenceService().retrieveReferenceObject(parentObject, referenceObjectName);
        } else if (getDataDictionaryService().hasRelationship(parentObject.getClass().getName(), referenceObjectName)) {
            // refresh via data dictionary mapping
            Object referenceObject = ObjectUtils.getPropertyValue(parentObject, referenceObjectName);
            if (!(referenceObject instanceof PersistableBusinessObject)) {
                LOG.warn("Could not refresh reference " + referenceObjectName + " off class " + parentObject.getClass()
                        .getName() + ". Class not of type PersistableBusinessObject");
                return;
            }

            referenceObject = getBusinessObjectService().retrieve((PersistableBusinessObject) referenceObject);
            if (referenceObject == null) {
                LOG.warn("Could not refresh reference " + referenceObjectName + " off class " + parentObject.getClass()
                        .getName() + ".");
                return;
            }

            try {
                ObjectUtils.setObjectProperty(parentObject, referenceObjectName, referenceObject);
            } catch (Exception e) {
                LOG.error("Unable to refresh persistable business object: " + referenceObjectName + "\n" + e
                        .getMessage());
                throw new RuntimeException(
                        "Unable to refresh persistable business object: " + referenceObjectName + "\n" + e
                                .getMessage());
            }
        } else {
            LOG.warn("Could not refresh reference " + referenceObjectName + " off class " + parentObject.getClass()
                    .getName() + ".");
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#processCollectionAddLine(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, java.lang.String)
     */
    @Override
    public void processCollectionAddLine(View view, Object model, String collectionPath) {
        // get the collection group from the view
        CollectionGroup collectionGroup = view.getViewIndex().getCollectionGroupByPath(collectionPath);
        if (collectionGroup == null) {
            logAndThrowRuntime("Unable to get collection group component for path: " + collectionPath);
        }

        // get the collection instance for adding the new line
        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(model, collectionPath);
        if (collection == null) {
            logAndThrowRuntime("Unable to get collection property from model for path: " + collectionPath);
        }

        // now get the new line we need to add
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object addLine = ObjectPropertyUtils.getPropertyValue(model, addLinePath);
        if (addLine == null) {
            logAndThrowRuntime("Add line instance not found for path: " + addLinePath);
        }

        processBeforeAddLine(view, collectionGroup, model, addLine);

        // validate the line to make sure it is ok to add
        boolean isValidLine = performAddLineValidation(view, collectionGroup, model, addLine);
        if (isValidLine) {
            // TODO: should check to see if there is an add line method on the
            // collection parent and if so call that instead of just adding to
            // the collection (so that sequence can be set)
            addLine(collection, addLine, collectionGroup.getAddLinePlacement().equals(
                    UifConstants.Position.TOP.name()));

            // make a new instance for the add line
            collectionGroup.initializeNewCollectionLine(view, model, collectionGroup, true);
        }

        ((UifFormBase) model).getAddedCollectionItems().add(addLine);

        processAfterAddLine(view, collectionGroup, model, addLine, isValidLine);
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#processCollectionSaveLine(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, java.lang.String, int)
     */
    @Override
    public void processCollectionSaveLine(View view, Object model, String collectionPath, int selectedLineIndex) {
        // get the collection group from the view
        CollectionGroup collectionGroup = view.getViewIndex().getCollectionGroupByPath(collectionPath);
        if (collectionGroup == null) {
            logAndThrowRuntime("Unable to get collection group component for path: " + collectionPath);
        }

        // get the collection instance for adding the new line
        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(model, collectionPath);
        if (collection == null) {
            logAndThrowRuntime("Unable to get collection property from model for path: " + collectionPath);
        }

        // TODO: look into other ways of identifying a line so we can deal with
        // unordered collections
        if (collection instanceof List) {
            Object saveLine = ((List<Object>) collection).get(selectedLineIndex);

            processBeforeSaveLine(view, collectionGroup, model, saveLine);

            ((UifFormBase) model).getAddedCollectionItems().remove(saveLine);

            processAfterSaveLine(view, collectionGroup, model, saveLine);

        } else {
            logAndThrowRuntime("Only List collection implementations are supported for the delete by index method");
        }

    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#processCollectionAddBlankLine(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, java.lang.String)
     */
    @Override
    public void processCollectionAddBlankLine(View view, Object model, String collectionPath) {
        // get the collection group from the view
        CollectionGroup collectionGroup = view.getViewIndex().getCollectionGroupByPath(collectionPath);
        if (collectionGroup == null) {
            logAndThrowRuntime("Unable to get collection group component for path: " + collectionPath);
        }

        // get the collection instance for adding the new line
        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(model, collectionPath);
        if (collection == null) {
            logAndThrowRuntime("Unable to get collection property from model for path: " + collectionPath);
        }

        Object newLine = ObjectUtils.newInstance(collectionGroup.getCollectionObjectClass());
        applyDefaultValuesForCollectionLine(view, model, collectionGroup, newLine);
        addLine(collection, newLine, collectionGroup.getAddLinePlacement().equals("TOP"));

        ((UifFormBase) model).getAddedCollectionItems().add(newLine);
    }

    /**
     * Add addLine to collection while giving derived classes an opportunity to override for things
     * like sorting.
     * 
     * @param collection the Collection to add the given addLine to
     * @param addLine the line to add to the given collection
     * @param insertFirst indicates if the item should be inserted as the first item
     */
    protected void addLine(Collection<Object> collection, Object addLine, boolean insertFirst) {
        if (insertFirst && (collection instanceof List)) {
            ((List) collection).add(0, addLine);
        } else {
            collection.add(addLine);
        }
    }

    /**
     * Performs validation on the new collection line before it is added to the corresponding
     * collection
     * 
     * @param view view instance that the action was taken on
     * @param collectionGroup collection group component for the collection
     * @param addLine new line instance to validate
     * @param model object instance that contain's the views data
     * @return true if the line is valid and it should be added to the collection, false if it was
     *         not valid and should not be added to the collection
     */
    protected boolean performAddLineValidation(View view, CollectionGroup collectionGroup, Object model,
            Object addLine) {
        boolean isValid = true;

        // TODO: this should invoke rules, subclasses like the document view
        // should create the document add line event

        return isValid;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#processCollectionDeleteLine(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, java.lang.String, int)
     */
    public void processCollectionDeleteLine(View view, Object model, String collectionPath, int lineIndex) {
        // get the collection group from the view
        CollectionGroup collectionGroup = view.getViewIndex().getCollectionGroupByPath(collectionPath);
        if (collectionGroup == null) {
            logAndThrowRuntime("Unable to get collection group component for path: " + collectionPath);
        }

        // get the collection instance for adding the new line
        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(model, collectionPath);
        if (collection == null) {
            logAndThrowRuntime("Unable to get collection property from model for path: " + collectionPath);
        }

        // TODO: look into other ways of identifying a line so we can deal with
        // unordered collections
        if (collection instanceof List) {
            Object deleteLine = ((List<Object>) collection).get(lineIndex);

            // validate the delete action is allowed for this line
            boolean isValid = performDeleteLineValidation(view, collectionGroup, deleteLine);
            if (isValid) {
                ((List<Object>) collection).remove(lineIndex);
                processAfterDeleteLine(view, collectionGroup, model, lineIndex);
            }
        } else {
            logAndThrowRuntime("Only List collection implementations are supported for the delete by index method");
        }
    }

    /**
     * Performs validation on the collection line before it is removed from the corresponding
     * collection
     * 
     * @param view view instance that the action was taken on
     * @param collectionGroup collection group component for the collection
     * @param deleteLine line that will be removed
     * @return true if the action is allowed and the line should be removed, false if the line
     *         should not be removed
     */
    protected boolean performDeleteLineValidation(View view, CollectionGroup collectionGroup, Object deleteLine) {
        boolean isValid = true;

        // TODO: this should invoke rules, sublclasses like the document view
        // should create the document delete line event

        return isValid;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl#processMultipleValueLookupResults
     */
    public void processMultipleValueLookupResults(View view, Object model, String collectionPath,
            String lookupResultValues) {
        // if no line values returned, no population is needed
        if (StringUtils.isBlank(lookupResultValues)) {
            return;
        }

        // retrieve the collection group so we can get the collection class and collection lookup
        CollectionGroup collectionGroup = view.getViewIndex().getCollectionGroupByPath(collectionPath);
        if (collectionGroup == null) {
            throw new RuntimeException("Unable to find collection group for path: " + collectionPath);
        }

        Class<?> collectionObjectClass = collectionGroup.getCollectionObjectClass();
        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(model,
                collectionGroup.getBindingInfo().getBindingPath());
        if (collection == null) {
            Class<?> collectionClass = ObjectPropertyUtils.getPropertyType(model,
                    collectionGroup.getBindingInfo().getBindingPath());
            collection = (Collection<Object>) ObjectUtils.newInstance(collectionClass);
            ObjectPropertyUtils.setPropertyValue(model, collectionGroup.getBindingInfo().getBindingPath(), collection);
        }

        Map<String, String> fieldConversions = collectionGroup.getCollectionLookup().getFieldConversions();
        List<String> toFieldNamesColl = new ArrayList(fieldConversions.values());
        Collections.sort(toFieldNamesColl);
        String[] toFieldNames = new String[toFieldNamesColl.size()];
        toFieldNamesColl.toArray(toFieldNames);

        // first split to get the line value sets
        String[] lineValues = StringUtils.split(lookupResultValues, ",");

        // for each returned set create a new instance of collection class and populate with returned line values
        for (String lineValue : lineValues) {
            Object lineDataObject = null;

            // TODO: need to put this in data object service so logic can be reused
            ModuleService moduleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(
                    collectionObjectClass);
            if (moduleService != null && moduleService.isExternalizable(collectionObjectClass)) {
                lineDataObject = moduleService.createNewObjectFromExternalizableClass(collectionObjectClass.asSubclass(
                        ExternalizableBusinessObject.class));
            } else {
                lineDataObject = ObjectUtils.newInstance(collectionObjectClass);
            }

            // apply default values to new line
            applyDefaultValuesForCollectionLine(view, model, collectionGroup, lineDataObject);

            String[] fieldValues = StringUtils.splitByWholeSeparatorPreserveAllTokens(lineValue, ":");
            if (fieldValues.length != toFieldNames.length) {
                throw new RuntimeException(
                        "Value count passed back from multi-value lookup does not match field conversion count");
            }

            // set each field value on the line
            for (int i = 0; i < fieldValues.length; i++) {
                String fieldName = toFieldNames[i];
                ObjectPropertyUtils.setPropertyValue(lineDataObject, fieldName, fieldValues[i]);
            }

            // TODO: duplicate identifier check

            collection.add(lineDataObject);
        }
    }

    /**
     * Finds the <code>Inquirable</code> configured for the given data object class and delegates to
     * it for building the inquiry URL
     * 
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#buildInquiryLink(java.lang.Object,
     *      java.lang.String, org.kuali.rice.krad.uif.widget.Inquiry)
     */
    public void buildInquiryLink(Object dataObject, String propertyName, Inquiry inquiry) {
        Inquirable inquirable = getViewDictionaryService().getInquirable(dataObject.getClass(), inquiry.getViewName());
        if (inquirable != null) {
            inquirable.buildInquirableLink(dataObject, propertyName, inquiry);
        } else {
            // TODO: should we really not render the inquiry just because the top parent doesn't have an inquirable?
            // it is possible the path is nested and there does exist an inquiry for the property
            // inquirable not found, no inquiry link can be set
            inquiry.setRender(false);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#applyDefaultValuesForCollectionLine(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.CollectionGroup, java.lang.Object)
     */
    public void applyDefaultValuesForCollectionLine(View view, Object model, CollectionGroup collectionGroup,
            Object line) {
        // retrieve all data fields for the collection line
        List<DataField> dataFields = ComponentUtils.getComponentsOfTypeDeep(collectionGroup.getAddLineItems(),
                DataField.class);
        for (DataField dataField : dataFields) {
            String bindingPath = "";
            if (StringUtils.isNotBlank(dataField.getBindingInfo().getBindByNamePrefix())) {
                bindingPath = dataField.getBindingInfo().getBindByNamePrefix() + ".";
            }
            bindingPath += dataField.getBindingInfo().getBindingName();

            populateDefaultValueForField(view, line, dataField, bindingPath);
        }
    }

    /**
     * Iterates through the view components picking up data fields and applying an default value
     * configured
     * 
     * @param view view instance we are applying default values for
     * @param component component that should be checked for default values
     * @param model model object that values should be set on
     */
    protected void applyDefaultValues(View view, Component component, Object model) {
        if (component == null) {
            return;
        }

        // if component is a data field apply default value
        if (component instanceof DataField) {
            DataField dataField = ((DataField) component);

            // need to make sure binding is initialized since this could be on a page we have not initialized yet
            dataField.getBindingInfo().setDefaults(view, dataField.getPropertyName());

            populateDefaultValueForField(view, model, dataField, dataField.getBindingInfo().getBindingPath());
        }

        for (Component nested : component.getComponentsForLifecycle()) {
            applyDefaultValues(view, nested, model);
        }

        // if view, need to add all pages since only one will be on the lifecycle
        if (component instanceof View) {
            for (Component nested : ((View) component).getItems()) {
                applyDefaultValues(view, nested, model);
            }
        }

    }

    /**
     * Applies the default value configured for the given field (if any) to the line given object
     * property that is determined by the given binding path
     * 
     * <p>
     * Checks for a configured default value or default value class for the field. If both are
     * given, the configured static default value will win. In addition, if the default value
     * contains an el expression it is evaluated against the initial context
     * </p>
     * 
     * @param view view instance the field belongs to
     * @param object object that should be populated
     * @param dataField field to check for configured default value
     * @param bindingPath path to the property on the object that should be populated
     */
    protected void populateDefaultValueForField(View view, Object object, DataField dataField, String bindingPath) {
        // check if dataField is somehow null
        if (dataField == null) {
            return;
        }

        // check for configured default value
        String defaultValue = dataField.getDefaultValue();
        Object[] defaultValues = dataField.getDefaultValues();

        if (!ObjectPropertyUtils.isReadableProperty(object, bindingPath)
                || !ObjectPropertyUtils.isWritableProperty(object, bindingPath)){
            return;
        }

        Object currentValue = ObjectPropertyUtils.getPropertyValue(object, bindingPath);

        // Default value only applies when the value being set is null (ie, has no value on the form)
        if (currentValue != null) {
            return;
        }

        if (StringUtils.isBlank(defaultValue) && (dataField.getDefaultValueFinderClass() != null)) {
            ValueFinder defaultValueFinder = ObjectUtils.newInstance(dataField.getDefaultValueFinderClass());
            defaultValue = defaultValueFinder.getValue();
        }

        if (StringUtils.isBlank(defaultValue)) {
            String defaultValuesExpression = null;

            // Check for expression, this would exist in a comma seperated list case that uses expressions
            if (dataField.getExpressionGraph() != null &&
                    dataField.getExpressionGraph().containsKey(UifConstants.ComponentProperties.DEFAULT_VALUES)) {
                defaultValuesExpression = dataField.getExpressionGraph().get(
                        UifConstants.ComponentProperties.DEFAULT_VALUES);
            }

            // evaluate and set if defaultValues are backed by an expression
            if (defaultValuesExpression != null && getExpressionEvaluator().containsElPlaceholder(
                    defaultValuesExpression)) {
                Map<String, Object> context = getPreModelContext(view);
                context.putAll(dataField.getContext());
                defaultValuesExpression = expressionEvaluator.replaceBindingPrefixes(view, object,
                        defaultValuesExpression);

                ExpressionEvaluator expressionEvaluator = getExpressionEvaluator();
                defaultValuesExpression = expressionEvaluator.evaluateExpressionTemplate(context,
                        defaultValuesExpression);

                ObjectPropertyUtils.setPropertyValue(object, bindingPath, defaultValuesExpression);
            } else if (defaultValues != null) {
                ObjectPropertyUtils.setPropertyValue(object, bindingPath, defaultValues);
            }
        } else {
            // check for expression in defaultValue
            if (dataField.getExpressionGraph() != null &&
                    dataField.getExpressionGraph().containsKey(UifConstants.ComponentProperties.DEFAULT_VALUE)) {
                defaultValue = dataField.getExpressionGraph().get(UifConstants.ComponentProperties.DEFAULT_VALUE);
            }

            // populate default value if given and path is valid
            if (StringUtils.isNotBlank(defaultValue)) {
                // evaluate if defaultValue is backed by an expression
                if (getExpressionEvaluator().containsElPlaceholder(defaultValue)) {
                    Map<String, Object> context = getPreModelContext(view);
                    context.putAll(dataField.getContext());
                    defaultValue = expressionEvaluator.replaceBindingPrefixes(view, object, defaultValue);

                    ExpressionEvaluator expressionEvaluator = getExpressionEvaluator();
                    defaultValue = expressionEvaluator.evaluateExpressionTemplate(context, defaultValue);
                }

                ObjectPropertyUtils.setPropertyValue(object, bindingPath, defaultValue);
            }
        }
    }

    /**
     * Hook for creating new components with code and adding them to a container
     * 
     * <p>
     * Subclasses can override this method to check for one or more containers by id and then adding
     * components created in code. This is invoked before the initialize method on the container
     * component, so the full lifecycle will be run on the components returned.
     * </p>
     * 
     * <p>
     * New components instances can be retrieved using {@link ComponentFactory}
     * </p>
     * 
     * @param view view instance the container belongs to
     * @param model object containing the view data
     * @param container container instance to add components to
     */
    protected void addCustomContainerComponents(View view, Object model, Container container) {

    }

    /**
     * Hook for service overrides to perform custom initialization on the component
     * 
     * @param view view instance containing the component
     * @param component component instance to initialize
     */
    protected void performCustomInitialization(View view, Component component) {

    }

    /**
     * Hook for service overrides to perform custom apply model logic on the component
     * 
     * @param view view instance containing the component
     * @param component component instance to apply model to
     * @param model Top level object containing the data (could be the model or a top level business
     *        object, dto)
     */
    protected void performCustomApplyModel(View view, Component component, Object model) {

    }

    /**
     * Hook for service overrides to perform custom component finalization
     * 
     * @param view view instance containing the component
     * @param component component instance to update
     * @param model Top level object containing the data
     * @param parent Parent component for the component being finalized
     */
    protected void performCustomFinalize(View view, Component component, Object model, Component parent) {

    }

    /**
     * Hook for service overrides to process the new collection line before it is added to the
     * collection
     * 
     * @param view view instance that is being presented (the action was taken on)
     * @param collectionGroup collection group component for the collection the line will be added
     *        to
     * @param model object instance that contain's the views data
     * @param addLine the new line instance to be processed
     */
    protected void processBeforeAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine) {

    }

    /**
     * Hook for service overrides to process the new collection line after it has been added to the
     * collection
     * 
     * @param view view instance that is being presented (the action was taken on)
     * @param collectionGroup collection group component for the collection the line that was added
     * @param model object instance that contain's the views data
     * @param addLine the new line that was added
     * @param isValidLine indicates if the line is valid
     */
    protected void processAfterAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine,
            boolean isValidLine) {

    }

    /**
     * Hook for service overrides to process the save collection line before it is validated
     * 
     * @param view view instance that is being presented (the action was taken on)
     * @param collectionGroup collection group component for the collection
     * @param model object instance that contain's the views data
     * @param addLine the new line instance to be processed
     */
    protected void processBeforeSaveLine(View view, CollectionGroup collectionGroup, Object model, Object addLine) {

    }

    /**
     * Hook for service overrides to process the save collection line after it has been validated
     * 
     * @param view view instance that is being presented (the action was taken on)
     * @param collectionGroup collection group component for the collection
     * @param model object instance that contains the views data
     * @param addLine the new line that was added
     */
    protected void processAfterSaveLine(View view, CollectionGroup collectionGroup, Object model, Object addLine) {

    }

    /**
     * Hook for service overrides to process the collection line after it has been deleted
     * 
     * @param view view instance that is being presented (the action was taken on)
     * @param collectionGroup collection group component for the collection the line that was added
     * @param model object instance that contains the views data
     * @param lineIndex index of the line that was deleted
     */
    protected void processAfterDeleteLine(View view, CollectionGroup collectionGroup, Object model, int lineIndex) {

    }

    /**
     * Log the error and throw a new runtime exception
     * 
     * @param message - the error message (both to log and throw as a new exception)
     */
    protected void logAndThrowRuntime(String message) {
        LOG.error(message);
        throw new RuntimeException(message);
    }

    /**
     * Gets the business object service
     *
     * @return business object service
     */
    public BusinessObjectService getBusinessObjectService() {
        if (this.businessObjectService == null) {
            this.businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     * Set the business object service
     *
     * @param businessObjectService
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Gets the persistence service
     *
     * @return persistence service
     */
    public PersistenceService getPersistenceService() {
        if (this.persistenceService == null) {
            this.persistenceService = KRADServiceLocator.getPersistenceService();
        }

        return this.persistenceService;
    }

    /**
     * Set the persistence service
     *
     * @param persistenceService
     */
    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * Get the persistence structure service
     *
     * @return persistence structure service
     */
    public PersistenceStructureService getPersistenceStructureService() {
        if (this.persistenceStructureService == null) {
            this.persistenceStructureService = KRADServiceLocator.getPersistenceStructureService();
        }
        return this.persistenceStructureService;
    }

    /**
     * Set the persistence structure service
     *
     * @param persistenceStructureService
     */
    public void setPersistenceStructureService(PersistenceStructureService persistenceStructureService) {
        this.persistenceStructureService = persistenceStructureService;
    }

    /**
     * Gets the data dictionary service
     * 
     * @return data dictionary service
     */
    protected DataDictionaryService getDataDictionaryService() {
        if (this.dataDictionaryService == null) {
            this.dataDictionaryService = KRADServiceLocatorWeb.getDataDictionaryService();
        }

        return this.dataDictionaryService;
    }

    /**
     * Sets the data dictionary service
     * 
     * @param dataDictionaryService
     */
    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    /**
     * Gets the expression evaluator service
     * 
     * @return expression evaluator service
     */
    public ExpressionEvaluator getExpressionEvaluator() {
        if (this.expressionEvaluator == null) {
            this.expressionEvaluator = new DefaultExpressionEvaluator();
        }

        return this.expressionEvaluator;
    }

    /**
     * Sets the expression evaluator service
     * 
     * @param expressionEvaluator
     */
    public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    /**
     * Gets the view dictionary service
     * 
     * @return view dictionary service
     */
    public ViewDictionaryService getViewDictionaryService() {
        if (this.viewDictionaryService == null) {
            this.viewDictionaryService = KRADServiceLocatorWeb.getViewDictionaryService();
        }
        return this.viewDictionaryService;
    }

    /**
     * Sets the view dictionary service
     * 
     * @param viewDictionaryService
     */
    public void setViewDictionaryService(ViewDictionaryService viewDictionaryService) {
        this.viewDictionaryService = viewDictionaryService;
    }

    /**
     * Gets the configuration service
     * 
     * @return configuration service
     */
    public ConfigurationService getConfigurationService() {
        if (this.configurationService == null) {
            this.configurationService = CoreApiServiceLocator.getKualiConfigurationService();
        }
        return this.configurationService;
    }

    /**
     * Sets the configuration service
     * 
     * @param configurationService
     */
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
