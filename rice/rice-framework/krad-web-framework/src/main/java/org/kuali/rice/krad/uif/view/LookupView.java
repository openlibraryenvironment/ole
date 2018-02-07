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

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.RequestParameter;
import org.kuali.rice.krad.uif.control.Control;
import org.kuali.rice.krad.uif.control.TextAreaControl;
import org.kuali.rice.krad.uif.control.TextControl;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.field.LookupInputField;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * View type for lookups
 *
 * <p>
 * Supports doing a search against a data object class or performing a more advanced query. The view
 * type is primarily made up of two groups, the search (or criteria) group and the results group. Many
 * options are supported on the view to enable/disable certain features, like what actions are available
 * on the search results.
 * </p>
 *
 * <p>
 * Works in conjunction with <code>LookupableImpl</code> which customizes the view and carries out the
 * business functionality
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "lookupView-bean", parent = "Uif-LookupView")
public class LookupView extends FormView {
    private static final long serialVersionUID = 716926008488403616L;

    private Class<?> dataObjectClassName;

    private Group criteriaGroup;
    private CollectionGroup resultsGroup;

    private List<Component> criteriaFields;
    private List<Component> resultFields;
    private List<String> defaultSortAttributeNames;

    protected boolean defaultSortAscending = true;

    @RequestParameter
    private boolean hideReturnLinks = false;
    @RequestParameter
    private boolean suppressActions = false;
    @RequestParameter
    private boolean showMaintenanceLinks = false;
    @RequestParameter
    private boolean multipleValuesSelect = false;
    @RequestParameter
    private boolean renderLookupCriteria = true;
    @RequestParameter
    private boolean renderSearchButtons = true;
    @RequestParameter
    private boolean renderHeader = true;

    @RequestParameter
    private String returnTarget;

    @RequestParameter
    private boolean returnByScript;

    private boolean triggerOnChange;

    private Integer resultSetLimit = null;
    private Integer multipleValuesSelectResultSetLimit = null;

    private String maintenanceUrlMapping;

    private FieldGroup rangeFieldGroupPrototype;

    private Message rangedToMessage;

    private boolean autoAddActiveCriteria;

    private List<String> additionalSecurePropertyNames;

    public LookupView() {
        super();

        setViewTypeName(ViewType.LOOKUP);
        setApplyDirtyCheck(false);
        setTriggerOnChange(false);
        setAutoAddActiveCriteria(true);
        
        additionalSecurePropertyNames = new ArrayList<String>();
    }

    /**
     * The following initialization is performed:
     *
     * <ul>
     * <li>Set the abstractTypeClasses map for the lookup object path</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.container.ContainerBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {

        boolean isInactivatableClass = Inactivatable.class.isAssignableFrom(dataObjectClassName);

        if (autoAddActiveCriteria && isInactivatableClass) {
            autoAddActiveCriteria();
        }

        initializeGroups();

        // since we don't have these as prototypes need to assign ids here
        view.assignComponentIds(getCriteriaGroup());
        view.assignComponentIds(getResultsGroup());

        if (getItems().isEmpty()) {
            setItems(Arrays.asList(getCriteriaGroup(), getResultsGroup()));
        }

        super.performInitialization(view, model);

        // if this is a multi-value lookup, don't show return column
        if (multipleValuesSelect) {
            hideReturnLinks = true;
        }

        getObjectPathToConcreteClassMapping().put(UifPropertyPaths.LOOKUP_CRITERIA, getDataObjectClassName());
        if (StringUtils.isNotBlank(getDefaultBindingObjectPath())) {
            getObjectPathToConcreteClassMapping().put(getDefaultBindingObjectPath(), getDataObjectClassName());
        }
    }

    /**
     * Adds the 'active' property criteria to the criteria fields if the BO is inactivatable
     */
    private void autoAddActiveCriteria() {
        boolean hasActiveCriteria = false;

        for (Component field : getCriteriaFields()) {
            if (((InputField)field).getPropertyName().equals("active")) {
                hasActiveCriteria = true;
            }
        }

        if (!hasActiveCriteria) {
            AttributeDefinition attributeDefinition = KRADServiceLocatorWeb.getDataDictionaryService().getAttributeDefinition(
                    dataObjectClassName.getName(), "active");
            LookupInputField activeField = new LookupInputField();

            if (attributeDefinition == null) {
                activeField = (LookupInputField)ComponentFactory.getNewComponentInstance("Uif-LookupActiveInputField");
            }else{
                activeField = (LookupInputField)ComponentFactory.getNewComponentInstance("Uif-LookupCriteriaInputField");
                activeField.setPropertyName("active");
                activeField.copyFromAttributeDefinition(this, attributeDefinition);
            }

            getCriteriaFields().add(activeField);
        }
    }

    protected void initializeGroups() {
        if (renderLookupCriteria && (getCriteriaGroup() != null) && (getCriteriaGroup().getItems().isEmpty())) {
            getCriteriaGroup().setItems(getCriteriaFields());
        }

        if (getResultsGroup() != null) {
            if ((getResultsGroup().getItems().isEmpty()) && (getResultFields() != null)) {
                getResultsGroup().setItems(getResultFields());
            }
            if (getResultsGroup().getCollectionObjectClass() == null) {
                getResultsGroup().setCollectionObjectClass(getDataObjectClassName());
            }
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.container.ContainerBase#performApplyModel(View, Object,
     * org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        LookupForm lookupForm = (LookupForm) model;

        if (!renderSearchButtons) {
            criteriaGroup.getFooter().setRender(false);
        }

        if (!renderLookupCriteria) {
            criteriaGroup.setRender(false);
        }

        if (!renderHeader) {
            getHeader().setRender(false);
        }

        setupLookupCriteriaFields(view, model);

        // Get the search action button for trigger on change and trigger on enter
        Group actionGroup = criteriaGroup.getFooter();
        Action searchButton = findSearchButton(actionGroup.getItems());

        // Only add trigger on script if an action with methodToCall search exists
        if (searchButton != null) {
            String searchButtonId = searchButton.getId();

            for (Component criteriaField : criteriaGroup.getItems()) {
                addTriggerScripts(searchButtonId, criteriaField);
            }
        }

        super.performApplyModel(view, model, parent);
    }

    /**
     * @see org.kuali.rice.krad.uif.container.ContainerBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        // force session persistence of criteria fields so we can validate the search input
        List<InputField> fields = ComponentUtils.getComponentsOfTypeDeep(criteriaGroup, InputField.class);
        for (InputField field : fields) {
            field.setForceSessionPersistence(true);
        }
    }

    /**
     * Adds an on change script to fields with the isTriggerOnChange set to true. Also prevents adds script to execute
     * search on enter when focus is in a criteris field
     *
     * @param searchButtonId the id of the search button
     * @param criteriaField that the script will be added to
     */
    private void addTriggerScripts(String searchButtonId, Component criteriaField) {
        if (criteriaField instanceof LookupInputField) {

            criteriaField.setOnKeyPressScript("if(e.which == 13) { e.preventDefault();jQuery('#" + searchButtonId + "' ).click();}");

            if (isTriggerOnChange() || ((LookupInputField)criteriaField).isTriggerOnChange()) {
                criteriaField.setOnChangeScript("jQuery('#" + searchButtonId + "' ).click();");
            }
        }
    }

    /**
     * Finds an Action with the search methodToCall from a list of Actions
     *
     * @param componentList list of components
     * @return the Action component with methodToCall of search
     */
    private Action findSearchButton(List<? extends Component> componentList) {
        List<? extends Action> actionList = ComponentUtils.getComponentsOfType(componentList, Action.class);
        for (Action action : actionList) {
            String methodToCall = action.getMethodToCall();
            if (methodToCall != null && methodToCall.equals("search")) {
                return action;
            }
        }
        return null;
    }

    /**
     * Helper method to do any lookup specific changes to the criteria fields
     */
    private void setupLookupCriteriaFields(View view, Object model) {
        HashMap<Integer, Component> dateRangeFieldMap = new HashMap<Integer, Component>();

        ExpressionEvaluator expressionEvaluator =
                view.getViewHelperService().getExpressionEvaluator();

        int rangeIndex = 0;
        for (Component criteriaField : criteriaGroup.getItems()) {
            // Set the max length on the controls to allow for wildcards
            Control control = ((InputField)criteriaField).getControl();
            if (control instanceof TextControl) {
                ((TextControl) control).setMaxLength(null);
            } else if (control instanceof TextAreaControl) {
                ((TextAreaControl) control).setMaxLength(null);
            }

            if (((LookupInputField)criteriaField).isRanged()) {
                // Create field group
                FieldGroup rangeFieldGroup = ComponentUtils.copy(rangeFieldGroupPrototype, criteriaField.getId());
                rangeFieldGroup.setLabel(((LookupInputField)criteriaField).getLabel());

                // Evaluate and set the required property and reset the required message on the 'to' label
                expressionEvaluator.evaluatePropertyExpression(view, criteriaField.getContext(), criteriaField,
                        "required", true);
                rangeFieldGroup.setRequired(criteriaField.getRequired());
                ((LookupInputField) criteriaField).getFieldLabel().setRequiredMessage(new Message());

                // Evaluate and set the render property
                expressionEvaluator.evaluatePropertyExpression(view, criteriaField.getContext(), criteriaField,
                        UifPropertyPaths.RENDER, true);
                rangeFieldGroup.setRender(criteriaField.isRender());

                List<Component> fieldGroupItems = new ArrayList<Component>();

                // Create a new from date field
                LookupInputField fromDate = (LookupInputField) ComponentUtils.copy(criteriaField,
                        KRADConstants.LOOKUP_DEFAULT_RANGE_SEARCH_LOWER_BOUND_LABEL);
                fromDate.getBindingInfo().setBindingName(
                        KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + fromDate.getPropertyName());
                fromDate.setPropertyName(
                        KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + fromDate.getPropertyName());

                // Set the criteria fields labels
                fromDate.setLabel("");
                fromDate.getFieldLabel().setRenderColon(false);
                ((LookupInputField)criteriaField).getFieldLabel().setRender(false);

                // Add the cirteria fields to the field group
                fieldGroupItems.add(fromDate);
                fieldGroupItems.add(rangedToMessage);
                fieldGroupItems.add(criteriaField);
                rangeFieldGroup.setItems(fieldGroupItems);

                // Add fieldgroup to map with index as key
                dateRangeFieldMap.put(rangeIndex, rangeFieldGroup);
            }

            rangeIndex++;
        }

        // Replace original fields with range fieldgroups
        List<Component> itemList = (List<Component>)criteriaGroup.getItems();
        for (Integer index : dateRangeFieldMap.keySet()) {
                itemList.set(index, dateRangeFieldMap.get(index));
        }

        criteriaGroup.setItems(itemList);
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#getComponentPrototypes()
     */
    @Override
    public List<Component> getComponentPrototypes() {
        List<Component> components = super.getComponentPrototypes();

        components.add(rangeFieldGroupPrototype);
        components.add(rangedToMessage);

        return components;
    }

    public void applyConditionalLogicForFieldDisplay() {
        // TODO: work into view lifecycle
        //	    LookupViewHelperService lookupViewHelperService = (LookupViewHelperService) getViewHelperService();
        //		Set<String> readOnlyFields = lookupViewHelperService.getConditionallyReadOnlyPropertyNames();
        //		Set<String> requiredFields = lookupViewHelperService.getConditionallyRequiredPropertyNames();
        //		Set<String> hiddenFields = lookupViewHelperService.getConditionallyHiddenPropertyNames();
        //		if ( (readOnlyFields != null && !readOnlyFields.isEmpty()) ||
        //			 (requiredFields != null && !requiredFields.isEmpty()) ||
        //			 (hiddenFields != null && !hiddenFields.isEmpty())
        //			) {
        //			for (Field field : getResultsGroup().getItems()) {
        //				if (InputField.class.isAssignableFrom(field.getClass())) {
        //					InputField attributeField = (InputField) field;
        //					if (readOnlyFields != null && readOnlyFields.contains(attributeField.getBindingInfo().getBindingName())) {
        //						attributeField.setReadOnly(true);
        //					}
        //					if (requiredFields != null && requiredFields.contains(attributeField.getBindingInfo().getBindingName())) {
        //						attributeField.setRequired(Boolean.TRUE);
        //					}
        //					if (hiddenFields != null && hiddenFields.contains(attributeField.getBindingInfo().getBindingName())) {
        //						attributeField.setControl(LookupInquiryUtils.generateCustomLookupControlFromExisting(HiddenControl.class, null));
        //					}
        //				}
        //	        }
        //		}
    }

    /**
     * Class name for the object the lookup applies to
     *
     * <p>
     * The object class name is used to pick up a dictionary entry which will
     * feed the attribute field definitions and other configuration. In addition
     * it is to configure the <code>Lookupable</code> which will carry out the
     * lookup action
     * </p>
     *
     * @return lookup data object class
     */
    @BeanTagAttribute(name="dataObjectClassName")
    public Class<?> getDataObjectClassName() {
        return this.dataObjectClassName;
    }

    /**
     * Setter for the object class name
     *
     * @param dataObjectClassName
     */
    public void setDataObjectClassName(Class<?> dataObjectClassName) {
        this.dataObjectClassName = dataObjectClassName;
    }

    /**
     * @return the hideReturnLinks
     */
    @BeanTagAttribute(name="hideReturnLinks")
    public boolean isHideReturnLinks() {
        return this.hideReturnLinks;
    }

    /**
     * @param hideReturnLinks the hideReturnLinks to set
     */
    public void setHideReturnLinks(boolean hideReturnLinks) {
        this.hideReturnLinks = hideReturnLinks;
    }

    /**
     * @return the suppressActions
     */
    @BeanTagAttribute(name="isSuppressActions")
    public boolean isSuppressActions() {
        return this.suppressActions;
    }

    /**
     * @param suppressActions the suppressActions to set
     */
    public void setSuppressActions(boolean suppressActions) {
        this.suppressActions = suppressActions;
    }

    /**
     * @return the showMaintenanceLinks
     */
    @BeanTagAttribute(name="showMaintenanceLinks")
    public boolean isShowMaintenanceLinks() {
        return this.showMaintenanceLinks;
    }

    /**
     * @param showMaintenanceLinks the showMaintenanceLinks to set
     */
    public void setShowMaintenanceLinks(boolean showMaintenanceLinks) {
        this.showMaintenanceLinks = showMaintenanceLinks;
    }

    /**
     * Indicates whether multiple values select should be enabled for the lookup
     *
     * <p>
     * When set to true, the select field is enabled for the lookup results group that allows the user
     * to select one or more rows for returning
     * </p>
     *
     * @return true if multiple values should be enabled, false otherwise
     */
    @BeanTagAttribute(name="multipleValueSelect")
    public boolean isMultipleValuesSelect() {
        return multipleValuesSelect;
    }

    /**
     * Setter for the multiple values select indicator
     *
     * @param multipleValuesSelect
     */
    public void setMultipleValuesSelect(boolean multipleValuesSelect) {
        this.multipleValuesSelect = multipleValuesSelect;
    }

    @BeanTagAttribute(name="criteriaGroup",type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Group getCriteriaGroup() {
        return this.criteriaGroup;
    }

    public void setCriteriaGroup(Group criteriaGroup) {
        this.criteriaGroup = criteriaGroup;
    }

    @BeanTagAttribute(name="resultsGroup",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public CollectionGroup getResultsGroup() {
        return this.resultsGroup;
    }

    public void setResultsGroup(CollectionGroup resultsGroup) {
        this.resultsGroup = resultsGroup;
    }

    @BeanTagAttribute(name="criteriaFields",type= BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Component> getCriteriaFields() {
        return this.criteriaFields;
    }

    public void setCriteriaFields(List<Component> criteriaFields) {
        this.criteriaFields = criteriaFields;
    }

    @BeanTagAttribute(name="resultFields",type= BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Component> getResultFields() {
        return this.resultFields;
    }

    public void setResultFields(List<Component> resultFields) {
        this.resultFields = resultFields;
    }

    @BeanTagAttribute(name="defaultSortAttributeNames",type= BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getDefaultSortAttributeNames() {
        return this.defaultSortAttributeNames;
    }

    public void setDefaultSortAttributeNames(List<String> defaultSortAttributeNames) {
        this.defaultSortAttributeNames = defaultSortAttributeNames;
    }

    @BeanTagAttribute(name="defaultSortAscending")
    public boolean isDefaultSortAscending() {
        return this.defaultSortAscending;
    }

    public void setDefaultSortAscending(boolean defaultSortAscending) {
        this.defaultSortAscending = defaultSortAscending;
    }

    /**
     * Retrieves the maximum number of records that will be listed
     * as a result of the lookup search
     *
     * @return Integer result set limit
     */
    @BeanTagAttribute(name="resultSetLimit")
    public Integer getResultSetLimit() {
        return resultSetLimit;
    }

    /**
     * Setter for the result list limit
     *
     * @param resultSetLimit Integer specifying limit
     */
    public void setResultSetLimit(Integer resultSetLimit) {
        this.resultSetLimit = resultSetLimit;
    }

    /**
     * Indicates whether a result set limit has been specified for the
     * view
     *
     * @return true if this instance has a result set limit
     */
    public boolean hasResultSetLimit() {
        return (resultSetLimit != null);
    }

    /**
     * Retrieves the maximum number of records that will be listed
     * as a result of the multiple values select lookup search
     *
     * @return multiple values select result set limit
     */
    @BeanTagAttribute(name="multipleValuesSelectResultSetLimit")
    public Integer getMultipleValuesSelectResultSetLimit() {
        return multipleValuesSelectResultSetLimit;
    }

    /**
     * Setter for the multiple values select result set limit
     *
     * @param multipleValuesSelectResultSetLimit Integer specifying limit
     */
    public void setMultipleValuesSelectResultSetLimit(Integer multipleValuesSelectResultSetLimit) {
        this.multipleValuesSelectResultSetLimit = multipleValuesSelectResultSetLimit;
    }

    /**
     * Indicates whether a multiple values select result
     * set limit has been specified for the view
     *
     * @return true if this instance has a multiple values select result set limit
     */
    public boolean hasMultipleValuesSelectResultSetLimit() {
        return (multipleValuesSelectResultSetLimit != null);
    }

    /**
     * @param returnTarget the returnTarget to set
     */
    public void setReturnTarget(String returnTarget) {
        this.returnTarget = returnTarget;
    }

    /**
     * @return the returnTarget
     */
    @BeanTagAttribute(name="returnTarget")
    public String getReturnTarget() {
        return returnTarget;
    }

    /**
     * @return the returnByScript
     */
    @BeanTagAttribute(name="returnByScript")
    public boolean isReturnByScript() {
        return returnByScript;
    }

    /**
     * Setter for the flag to indicate that lookups will return the value
     * by script and not a post
     *
     * @param returnByScript the returnByScript flag
     */
    public void setReturnByScript(boolean returnByScript) {
        this.returnByScript = returnByScript;
    }

    /**
     * String that maps to the maintenance controller for the maintenance document (if any) associated with the
     * lookup data object class
     *
     * <p>
     * Mapping will be used to build the maintenance action links (such as edit, copy, and new). If not given, the
     * default maintenance mapping will be used
     * </p>
     *
     * @return mapping string
     */
    @BeanTagAttribute(name="maintenanceUrlMapping")
    public String getMaintenanceUrlMapping() {
        return maintenanceUrlMapping;
    }

    /**
     * Setter for the URL mapping string that will be used to build up maintenance action URLs
     *
     * @param maintenanceUrlMapping
     */
    public void setMaintenanceUrlMapping(String maintenanceUrlMapping) {
        this.maintenanceUrlMapping = maintenanceUrlMapping;
    }

    /**
     * Indicates that the action buttons like search in the criteria section should be rendered
     *
     * @return boolean
     */
    public boolean isRenderSearchButtons() {
        return renderSearchButtons;
    }

    /**
     * Setter for the render search buttons flag
     *
     * @param renderSearchButtons
     */
    public void setRenderSearchButtons(boolean renderSearchButtons) {
        this.renderSearchButtons = renderSearchButtons;
    }

    /**
     * Indicates whether the lookup criteria group should be rendered
     *
     * <p>
     * Defaults to true. Can be set as bean property or passed as a request parameter in the lookup url.
     * </p>
     *
     * @return boolean
     */
    public boolean isRenderLookupCriteria() {
        return renderLookupCriteria;
    }

    /**
     * Setter for the lookup criteria group render flag
     *
     * @param renderLookupCriteria
     */
    public void setRenderLookupCriteria(boolean renderLookupCriteria) {
        this.renderLookupCriteria = renderLookupCriteria;
    }

    /**
     * Indicates whether the lookup header should be rendered
     *
     * <p>
     * Defaults to true. Can be set as bean property or passed as a request parameter in the lookup url.
     * </p>
     *
     * @return boolean
     */
    public boolean isRenderHeader() {
        return renderHeader;
    }

    /**
     * Setter for the header render flag
     *
     * @param renderHeader
     */
    public void setRenderHeader(boolean renderHeader) {
        this.renderHeader = renderHeader;
    }

    /**
     * Indicates that the search must execute on changing of a value in all lookup input fields
     *
     * @return boolean
     */
    public boolean isTriggerOnChange() {
        return triggerOnChange;
    }

    /**
     * Setter for the trigger search on change flag
     *
     * @param triggerOnChange
     */
    public void setTriggerOnChange(boolean triggerOnChange) {
        this.triggerOnChange = triggerOnChange;
    }

    /**
     * The field group prototype that will be copied and used for range fields
     *
     * @return FieldGroup
     */
    public FieldGroup getRangeFieldGroupPrototype() {
        return rangeFieldGroupPrototype;
    }

    /**
     * Setter for the range FieldGroup prototype
     *
     * @param rangeFieldGroupPrototype
     */
    public void setRangeFieldGroupPrototype(FieldGroup rangeFieldGroupPrototype) {
        this.rangeFieldGroupPrototype = rangeFieldGroupPrototype;
    }

    /**
     * Indicates whether the 'active' criteria field must be added automatically for Inactivatable BO's
     *
     * @return boolean
     */
    public boolean isAutoAddActiveCriteria() {
        return autoAddActiveCriteria;
    }

    /**
     * Setter for the flag that indicates whether the 'active' criteria field must be added automatically for
     * Inactivatable BO's
     *
     * @param autoAddActiveCriteria
     */
    public void setAutoAddActiveCriteria(boolean autoAddActiveCriteria) {
        this.autoAddActiveCriteria = autoAddActiveCriteria;
    }

    /**
     * List of secure property names that are in addition to the
     * {@link org.kuali.rice.krad.uif.component.ComponentSecurity} or
     * {@link org.kuali.rice.krad.datadictionary.AttributeSecurity} attributes.
     *
     * @return list of secure property names
     */
    @BeanTagAttribute(name = "additionalSecurePropertyNames", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getAdditionalSecurePropertyNames() {
        return additionalSecurePropertyNames;
    }

    /**
     * @see LookupView#getAdditionalSecurePropertyNames()
     */
    public void setAdditionalSecurePropertyNames(List<String> additionalSecurePropertyNames) {
        this.additionalSecurePropertyNames = additionalSecurePropertyNames;
    }

    /**
     * The Message to render between the two range fields for ranged criteria fields
     *
     * @return
     */
    public Message getRangedToMessage() {
        return rangedToMessage;
    }

    /**
     * Setter for the Message rendered between the two range fields for ranged criteria fields
     *
     * @param rangedToMessage
     */
    public void setRangedToMessage(Message rangedToMessage) {
        this.rangedToMessage = rangedToMessage;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        LookupView lookupViewCopy = (LookupView) component;

        if (this.dataObjectClassName != null) {
            lookupViewCopy.setDataObjectClassName(this.getDataObjectClassName());
        }

        if (this.criteriaGroup != null) {
            lookupViewCopy.setCriteriaGroup((Group) this.getCriteriaGroup().copy());
        }

        if (this.resultsGroup != null) {
            lookupViewCopy.setResultsGroup((CollectionGroup) this.getResultsGroup().copy());
        }

        if (this.criteriaFields != null) {
            List<Component> criteriaFieldsCopy = Lists.newArrayListWithExpectedSize(criteriaFields.size());
            for (Component criteriaField : criteriaFields) {
                criteriaFieldsCopy.add((Component) criteriaField.copy());
            }
            lookupViewCopy.setCriteriaFields(criteriaFieldsCopy);
        }

        if (this.resultFields != null) {
            List<Component> resultFieldsCopy = Lists.newArrayListWithExpectedSize(resultFields.size());
            for (Component resultField : resultFields) {
                resultFieldsCopy.add((Component) resultField.copy());
            }
            lookupViewCopy.setResultFields(resultFieldsCopy);
        }

        if (this.defaultSortAttributeNames != null) {
            lookupViewCopy.setDefaultSortAttributeNames(new ArrayList<String>(defaultSortAttributeNames));
        }

        lookupViewCopy.setDefaultSortAscending(this.isDefaultSortAscending());
        lookupViewCopy.setHideReturnLinks(this.hideReturnLinks);
        lookupViewCopy.setSuppressActions(this.suppressActions);
        lookupViewCopy.setShowMaintenanceLinks(this.showMaintenanceLinks);
        lookupViewCopy.setMaintenanceUrlMapping(this.maintenanceUrlMapping);
        lookupViewCopy.setMultipleValuesSelect(this.multipleValuesSelect);
        lookupViewCopy.setRenderLookupCriteria(this.renderLookupCriteria);
        lookupViewCopy.setRenderSearchButtons(this.renderSearchButtons);
        lookupViewCopy.setRenderHeader(this.renderHeader);
        lookupViewCopy.setResultSetLimit(this.resultSetLimit);
        lookupViewCopy.setReturnTarget(this.returnTarget);
        lookupViewCopy.setTriggerOnChange(this.triggerOnChange);
        lookupViewCopy.setResultSetLimit(this.resultSetLimit);
        lookupViewCopy.setMultipleValuesSelectResultSetLimit(this.multipleValuesSelectResultSetLimit);
        lookupViewCopy.setMaintenanceUrlMapping(this.maintenanceUrlMapping);

        if (this.rangeFieldGroupPrototype != null) {
            lookupViewCopy.setRangeFieldGroupPrototype((FieldGroup) this.rangeFieldGroupPrototype.copy());
        }

        if (this.rangedToMessage != null) {
            lookupViewCopy.setRangedToMessage((Message) this.rangedToMessage.copy());
        }

        lookupViewCopy.setAutoAddActiveCriteria(this.autoAddActiveCriteria);

        if (this.additionalSecurePropertyNames != null) {
            lookupViewCopy.setAdditionalSecurePropertyNames(additionalSecurePropertyNames);
        }
    }

}
