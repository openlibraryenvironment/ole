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
package org.kuali.rice.krad.uif.container;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.ClientSideState;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.ComponentSecurity;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.QuickFinder;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Group that holds a collection of objects and configuration for presenting the
 * collection in the UI. Supports functionality such as add line, line actions,
 * and nested collections.
 *
 * <p>
 * Note the standard header/footer can be used to give a header to the
 * collection as a whole, or to provide actions that apply to the entire
 * collection
 * </p>
 *
 * <p>
 * For binding purposes the binding path of each row field is indexed. The name
 * property inherited from <code>ComponentBase</code> is used as the collection
 * name. The collectionObjectClass property is used to lookup attributes from
 * the data dictionary.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "collectionGroup-bean", parent = "Uif-CollectionGroupBase"),
        @BeanTag(name = "stackedCollectionGroup-bean", parent = "Uif-StackedCollectionGroup"),
        @BeanTag(name = "stackedCollectionSection-bean", parent = "Uif-StackedCollectionSection"),
        @BeanTag(name = "stackedCollectionSubSection-bean", parent = "Uif-StackedCollectionSubSection"),
        @BeanTag(name = "stackedSubCollection-withinSection-bean", parent = "Uif-StackedSubCollection-WithinSection"),
        @BeanTag(name = "stackedSubCollection-withinSubSection-bean",
                parent = "Uif-StackedSubCollection-WithinSubSection"),
        @BeanTag(name = "disclosure-stackedCollectionSection-bean", parent = "Uif-Disclosure-StackedCollectionSection"),
        @BeanTag(name = "disclosure-stackedCollectionSubSection-bean",
                parent = "Uif-Disclosure-StackedCollectionSubSection"),
        @BeanTag(name = "disclosure-stackedSubCollection-withinSection-bean",
                parent = "Uif-Disclosure-StackedSubCollection-WithinSection"),
        @BeanTag(name = "disclosure-stackedSubCollection-withinSubSection-bean",
                parent = "Uif-Disclosure-StackedSubCollection-WithinSubSection"),
        @BeanTag(name = "tableCollectionGroup-bean", parent = "Uif-TableCollectionGroup"),
        @BeanTag(name = "tableCollectionSection-bean", parent = "Uif-TableCollectionSection"),
        @BeanTag(name = "tableCollectionSubSection-bean", parent = "Uif-TableCollectionSubSection"),
        @BeanTag(name = "tableSubCollection-withinSection-bean", parent = "Uif-TableSubCollection-WithinSection"),
        @BeanTag(name = "tableSubCollection-withinSubSection-bean", parent = "Uif-TableSubCollection-WithinSubSection"),
        @BeanTag(name = "disclosure-tableCollectionSection-bean", parent = "Uif-Disclosure-TableCollectionSection"),
        @BeanTag(name = "disclosure-tableCollectionSubSection-bean",
                parent = "Uif-Disclosure-TableCollectionSubSection"),
        @BeanTag(name = "disclosure-tableSubCollection-withinSection-bean",
                parent = "Uif-Disclosure-TableSubCollection-WithinSection"),
        @BeanTag(name = "disclosure-tableSubCollection-withinSubSection-bean",
                parent = "Uif-Disclosure-TableSubCollection-WithinSubSection"),
        @BeanTag(name = "listCollectionGroup-bean", parent = "Uif-ListCollectionGroup"),
        @BeanTag(name = "listCollectionSection-bean", parent = "Uif-ListCollectionSection"),
        @BeanTag(name = "listCollectionSubSection-bean", parent = "Uif-ListCollectionSubSection"),
        @BeanTag(name = "documentNotesSection-bean", parent = "Uif-DocumentNotesSection"),
        @BeanTag(name = "lookupResultsCollectionSection-bean", parent = "Uif-LookupResultsCollectionSection"),
        @BeanTag(name = "maintenanceStackedCollectionSection-bean", parent = "Uif-MaintenanceStackedCollectionSection"),
        @BeanTag(name = "maintenanceStackedSubCollection-withinSection-bean",
                parent = "Uif-MaintenanceStackedSubCollection-WithinSection"),
        @BeanTag(name = "maintenanceTableCollectionSection-bean", parent = "Uif-MaintenanceTableCollectionSection"),
        @BeanTag(name = "maintenanceTableSubCollection-withinSection-bean",
                parent = "Uif-MaintenanceTableSubCollection-withinSection")})
public class CollectionGroup extends Group implements DataBinding {
    private static final long serialVersionUID = -6496712566071542452L;

    private Class<?> collectionObjectClass;

    private String propertyName;
    private BindingInfo bindingInfo;

    private boolean renderAddLine;
    private String addLinePropertyName;
    private BindingInfo addLineBindingInfo;

    private Message addLineLabel;
    private List<? extends Component> addLineItems;
    private List<Action> addLineActions;

    private boolean renderLineActions;
    private List<Action> lineActions;

    private boolean includeLineSelectionField;
    private String lineSelectPropertyName;

    private QuickFinder collectionLookup;

    private boolean renderInactiveToggleButton;
    @ClientSideState(variableName = "inactive")
    private boolean showInactiveLines;
    private CollectionFilter activeCollectionFilter;
    private List<CollectionFilter> filters;

    private List<CollectionGroup> subCollections;
    private String subCollectionSuffix;

    private CollectionGroupBuilder collectionGroupBuilder;

    private int displayCollectionSize = -1;

    private boolean highlightNewItems;
    private boolean highlightAddItem;
    private String newItemsCssClass;
    private String addItemCssClass;

    private boolean renderAddBlankLineButton;
    private Action addBlankLineAction;
    private String addLinePlacement;

    private boolean renderSaveLineActions;
    private boolean addViaLightBox;
    private Action addViaLightBoxAction;

    private boolean useServerPaging = false;
    private int pageSize;
    private int displayStart = -1;
    private int displayLength = -1;
    private int filteredCollectionSize = -1;

    private List<String> totalColumns;

    public CollectionGroup() {
        renderAddLine = true;
        renderLineActions = true;
        renderInactiveToggleButton = true;
        highlightNewItems = true;
        highlightAddItem = true;
        addLinePlacement = "TOP";

        filters = Collections.emptyList();
        lineActions = Collections.emptyList();
        addLineItems = Collections.emptyList();
        addLineActions = Collections.emptyList();
        subCollections = Collections.emptyList();
    }

    /**
     * The following actions are performed:
     *
     * <ul>
     * <li>Set fieldBindModelPath to the collection model path (since the fields
     * have to belong to the same model as the collection)</li>
     * <li>Set defaults for binding</li>
     * <li>Default add line field list to groups items list</li>
     * <li>Sets default active collection filter if not set</li>
     * <li>Sets the dictionary entry (if blank) on each of the items to the
     * collection class</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        setFieldBindingObjectPath(getBindingInfo().getBindingObjectPath());

        super.performInitialization(view, model);

        if (bindingInfo != null) {
            bindingInfo.setDefaults(view, getPropertyName());
        }

        if (addLineBindingInfo != null) {
            // add line binds to model property
            if (StringUtils.isNotBlank(addLinePropertyName)) {
                addLineBindingInfo.setDefaults(view, getPropertyName());
                addLineBindingInfo.setBindingName(addLinePropertyName);
                if (StringUtils.isNotBlank(getFieldBindByNamePrefix())) {
                    addLineBindingInfo.setBindByNamePrefix(getFieldBindByNamePrefix());
                }
            }
        }

        for (Component item : getItems()) {
            if (item instanceof DataField) {
                DataField field = (DataField) item;

                if (StringUtils.isBlank(field.getDictionaryObjectEntry())) {
                    field.setDictionaryObjectEntry(collectionObjectClass.getName());
                }
            }
        }

        if ((addLineItems == null) || addLineItems.isEmpty()) {
            addLineItems = getItems();
        } else {
            for (Component addLineField : addLineItems) {
                if (!(addLineField instanceof DataField)) {
                    continue;
                }

                DataField field = (DataField) addLineField;

                if (StringUtils.isBlank(field.getDictionaryObjectEntry())) {
                    field.setDictionaryObjectEntry(collectionObjectClass.getName());
                }
            }
        }

        // if active collection filter not set use default
        if (this.activeCollectionFilter == null) {
            activeCollectionFilter = new ActiveCollectionFilter();
        }

        // set static collection path on items
        String collectionPath = "";
        if (StringUtils.isNotBlank(getBindingInfo().getCollectionPath())) {
            collectionPath += getBindingInfo().getCollectionPath() + ".";
        }
        if (StringUtils.isNotBlank(getBindingInfo().getBindByNamePrefix())) {
            collectionPath += getBindingInfo().getBindByNamePrefix() + ".";
        }
        collectionPath += getBindingInfo().getBindingName();

        List<DataField> collectionFields = ComponentUtils.getComponentsOfTypeDeep(getItems(), DataField.class);
        for (DataField collectionField : collectionFields) {
            collectionField.getBindingInfo().setCollectionPath(collectionPath);
        }

        List<DataField> addLineCollectionFields = ComponentUtils.getComponentsOfTypeDeep(addLineItems, DataField.class);
        for (DataField collectionField : addLineCollectionFields) {
            collectionField.getBindingInfo().setCollectionPath(collectionPath);
        }

        for (CollectionGroup collectionGroup : getSubCollections()) {
            collectionGroup.getBindingInfo().setCollectionPath(collectionPath);
        }

        // add collection entry to abstract classes
        if (!view.getObjectPathToConcreteClassMapping().containsKey(collectionPath)) {
            view.getObjectPathToConcreteClassMapping().put(collectionPath, getCollectionObjectClass());
        }
    }

    /**
     * Calls the configured <code>CollectionGroupBuilder</code> to build the
     * necessary components based on the collection data
     *
     * @see org.kuali.rice.krad.uif.container.ContainerBase#performApplyModel(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        // If we are using server paging, determine if a displayStart value has been set for this collection
        // and used that value as the displayStart
        if (model instanceof UifFormBase && this.isUseServerPaging()) {
            Object displayStart = ((UifFormBase) model).getExtensionData().get(
                    this.getId() + UifConstants.PageRequest.DISPLAY_START_PROP);

            if (displayStart != null) {
                this.setDisplayStart(((Integer) displayStart).intValue());
            }
        }

        // adds the script to the add line buttons to keep collection on the same page
        if (this.renderAddBlankLineButton) {
            if (this.addBlankLineAction == null) {
                this.addBlankLineAction = (Action) ComponentFactory.getNewComponentInstance(
                        ComponentFactory.ADD_BLANK_LINE_ACTION);
                view.assignComponentIds(this.addBlankLineAction);
            }

            if (addLinePlacement.equals(UifConstants.Position.BOTTOM.name())) {
                this.addBlankLineAction.setOnClickScript("writeCurrentPageToSession(this, 'last');");
            } else {
                this.addBlankLineAction.setOnClickScript("writeCurrentPageToSession(this, 'first');");
            }
        } else if (this.addViaLightBox) {
            if (this.addViaLightBoxAction == null) {
                this.addViaLightBoxAction = (Action) ComponentFactory.getNewComponentInstance(
                        ComponentFactory.ADD_VIA_LIGHTBOX_ACTION);
                view.assignComponentIds(this.addViaLightBoxAction);
            }

            if (this.addLinePlacement.equals(UifConstants.Position.BOTTOM.name())) {
                this.addViaLightBoxAction.setOnClickScript("writeCurrentPageToSession(this, 'last');");
            } else {
                this.addViaLightBoxAction.setOnClickScript("writeCurrentPageToSession(this, 'first');");
            }
        }

        pushCollectionGroupToReference();

        // if rendering the collection group, build out the lines
        if (isRender()) {
            getCollectionGroupBuilder().build(view, model, this);
        }

        // TODO: is this necessary to call again?
        // This may be necessary to call in case getCollectionGroupBuilder().build resets the context map
        pushCollectionGroupToReference();
    }

    /**
     * Sets a reference in the context map for all nested components to the collection group
     * instance, and sets name as parameter for an action fields in the group
     */
    protected void pushCollectionGroupToReference() {
        List<Component> components = getComponentsForLifecycle();
        components.addAll(getComponentPrototypes());

        ComponentUtils.pushObjectToContext(components, UifConstants.ContextVariableNames.COLLECTION_GROUP, this);

        List<Action> actions = ComponentUtils.getComponentsOfTypeDeep(components, Action.class);
        for (Action action : actions) {
            action.addActionParameter(UifParameters.SELLECTED_COLLECTION_PATH, this.getBindingInfo().getBindingPath());
        }
    }

    /**
     * New collection lines are handled in the framework by maintaining a map on
     * the form. The map contains as a key the collection name, and as value an
     * instance of the collection type. An entry is created here for the
     * collection represented by the <code>CollectionGroup</code> if an instance
     * is not available (clearExistingLine will force a new instance). The given
     * model must be a subclass of <code>UifFormBase</code> in order to find the
     * Map.
     *
     * @param model Model instance that contains the new collection lines Map
     * @param clearExistingLine boolean that indicates whether the line should be set to a
     * new instance if it already exists
     */
    public void initializeNewCollectionLine(View view, Object model, CollectionGroup collectionGroup,
            boolean clearExistingLine) {
        getCollectionGroupBuilder().initializeNewCollectionLine(view, model, collectionGroup, clearExistingLine);
    }

    /**
     * @see org.kuali.rice.krad.uif.container.ContainerBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(addLineLabel);
        components.add(collectionLookup);
        components.add(addBlankLineAction);
        components.add(addViaLightBoxAction);

        // remove the containers items because we don't want them as children
        // (they will become children of the layout manager as the rows are created)
        for (Component item : getItems()) {
            if (components.contains(item)) {
                components.remove(item);
            }
        }

        return components;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#getComponentPrototypes()
     */
    @Override
    public List<Component> getComponentPrototypes() {
        List<Component> components = super.getComponentPrototypes();

        components.addAll(lineActions);
        components.addAll(addLineActions);
        components.addAll(getItems());
        components.addAll(getSubCollections());

        // iterate through addLineItems to make sure we have not already
        // added them as prototypes (they could have been copied from add lines)
        if (addLineItems != null) {
            for (Component addLineItem : addLineItems) {
                if (!components.contains(addLineItem)) {
                    components.add(addLineItem);
                }
            }
        }

        return components;
    }

    /**
     * Object class the collection maintains. Used to get dictionary information
     * in addition to creating new instances for the collection when necessary
     *
     * @return collection object class
     */
    @BeanTagAttribute(name = "collectionObjectClass")
    public Class<?> getCollectionObjectClass() {
        return this.collectionObjectClass;
    }

    /**
     * Setter for the collection object class
     *
     * @param collectionObjectClass
     */
    public void setCollectionObjectClass(Class<?> collectionObjectClass) {
        this.collectionObjectClass = collectionObjectClass;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.DataBinding#getPropertyName()
     */
    @BeanTagAttribute(name = "propertyName")
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Setter for the collections property name
     *
     * @param propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Determines the binding path for the collection. Used to get the
     * collection value from the model in addition to setting the binding path
     * for the collection attributes
     *
     * @see org.kuali.rice.krad.uif.component.DataBinding#getBindingInfo()
     */
    @BeanTagAttribute(name = "bindingInfo", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BindingInfo getBindingInfo() {
        return this.bindingInfo;
    }

    /**
     * Setter for the binding info instance
     *
     * @param bindingInfo
     */
    public void setBindingInfo(BindingInfo bindingInfo) {
        this.bindingInfo = bindingInfo;
    }

    /**
     * Action fields that should be rendered for each collection line. Example
     * line action is the delete action
     *
     * @return line action fields
     */
    @BeanTagAttribute(name = "lineActions", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Action> getLineActions() {
        return this.lineActions;
    }

    /**
     * Setter for the line action fields list
     *
     * @param lineActions
     */
    public void setLineActions(List<Action> lineActions) {
        this.lineActions = lineActions;
    }

    /**
     * Indicates whether the action column for the collection should be rendered
     *
     * @return true if the actions should be rendered, false if not
     * @see #getLineActions()
     */
    @BeanTagAttribute(name = "renderLineActions")
    public boolean isRenderLineActions() {
        return this.renderLineActions;
    }

    /**
     * Setter for the render line actions indicator
     *
     * @param renderLineActions
     */
    public void setRenderLineActions(boolean renderLineActions) {
        this.renderLineActions = renderLineActions;
    }

    /**
     * Indicates whether an add line should be rendered for the collection
     *
     * @return true if add line should be rendered, false if it should
     *         not be
     */
    @BeanTagAttribute(name = "renderAddLine")
    public boolean isRenderAddLine() {
        return this.renderAddLine;
    }

    /**
     * Setter for the render add line indicator
     *
     * @param renderAddLine
     */
    public void setRenderAddLine(boolean renderAddLine) {
        this.renderAddLine = renderAddLine;
    }

    /**
     * Convenience getter for the add line label field text. The text is used to
     * label the add line when rendered and its placement depends on the
     * <code>LayoutManager</code>
     *
     * <p>
     * For the <code>TableLayoutManager</code> the label appears in the sequence
     * column to the left of the add line fields. For the
     * <code>StackedLayoutManager</code> the label is placed into the group
     * header for the line.
     * </p>
     *
     * @return add line label
     */
    public String getAddLabel() {
        if (getAddLineLabel() != null) {
            return getAddLineLabel().getMessageText();
        }

        return null;
    }

    /**
     * Setter for the add line label text
     *
     * @param addLabelText
     */
    public void setAddLabel(String addLabelText) {
        if (getAddLineLabel() != null) {
            getAddLineLabel().setMessageText(addLabelText);
        }
    }

    /**
     * <code>Message</code> instance for the add line label
     *
     * @return add line Message
     * @see #getAddLabel
     */
    @BeanTagAttribute(name = "addLineLabel", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Message getAddLineLabel() {
        return this.addLineLabel;
    }

    /**
     * Setter for the <code>Message</code> instance for the add line label
     *
     * @param addLineLabel
     * @see #getAddLabel
     */
    public void setAddLineLabel(Message addLineLabel) {
        this.addLineLabel = addLineLabel;
    }

    /**
     * Name of the property that contains an instance for the add line. If set
     * this is used with the binding info to create the path to the add line.
     * Can be left blank in which case the framework will manage the add line
     * instance in a generic map.
     *
     * @return add line property name
     */
    @BeanTagAttribute(name = "addLinePropertyName")
    public String getAddLinePropertyName() {
        return this.addLinePropertyName;
    }

    /**
     * Setter for the add line property name
     *
     * @param addLinePropertyName
     */
    public void setAddLinePropertyName(String addLinePropertyName) {
        this.addLinePropertyName = addLinePropertyName;
    }

    /**
     * <code>BindingInfo</code> instance for the add line property used to
     * determine the full binding path. If add line name given
     * {@link #getAddLabel} then it is set as the binding name on the
     * binding info. Add line label and binding info are not required, in which
     * case the framework will manage the new add line instances through a
     * generic map (model must extend UifFormBase)
     *
     * @return BindingInfo add line binding info
     */
    @BeanTagAttribute(name = "addLineBindingInfo", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BindingInfo getAddLineBindingInfo() {
        return this.addLineBindingInfo;
    }

    /**
     * Setter for the add line binding info
     *
     * @param addLineBindingInfo
     */
    public void setAddLineBindingInfo(BindingInfo addLineBindingInfo) {
        this.addLineBindingInfo = addLineBindingInfo;
    }

    /**
     * List of <code>Component</code> instances that should be rendered for the
     * collection add line (if enabled). If not set, the default group's items
     * list will be used
     *
     * @return add line field list
     * @see CollectionGroup#performInitialization(org.kuali.rice.krad.uif.view.View, java.lang.Object)
     */
    @BeanTagAttribute(name = "addLineItems", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<? extends Component> getAddLineItems() {
        return this.addLineItems;
    }

    /**
     * Setter for the add line field list
     *
     * @param addLineItems
     */
    public void setAddLineItems(List<? extends Component> addLineItems) {
        this.addLineItems = addLineItems;
    }

    /**
     * Action fields that should be rendered for the add line. This is generally
     * the add action (button) but can be configured to contain additional
     * actions
     *
     * @return add line action fields
     */
    @BeanTagAttribute(name = "addLineActions", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Action> getAddLineActions() {
        return this.addLineActions;
    }

    /**
     * Setter for the add line action fields
     *
     * @param addLineActions
     */
    public void setAddLineActions(List<Action> addLineActions) {
        this.addLineActions = addLineActions;
    }

    /**
     * Indicates whether lines of the collection group should be selected by rendering a
     * field for each line that will allow selection
     *
     * <p>
     * For example, having the select field enabled could allow selecting multiple lines from a search
     * to return (multi-value lookup)
     * </p>
     *
     * @return true if select field should be rendered, false if not
     */
    @BeanTagAttribute(name = "includeLineSelectionField")
    public boolean isIncludeLineSelectionField() {
        return includeLineSelectionField;
    }

    /**
     * Setter for the render selected field indicator
     *
     * @param includeLineSelectionField
     */
    public void setIncludeLineSelectionField(boolean includeLineSelectionField) {
        this.includeLineSelectionField = includeLineSelectionField;
    }

    /**
     * When {@link #isIncludeLineSelectionField()} is true, gives the name of the property the select field
     * should bind to
     *
     * <p>
     * Note if no prefix is given in the property name, such as 'form.', it is assumed the property is
     * contained on the collection line. In this case the binding path to the collection line will be
     * appended. In other cases, it is assumed the property is a list or set of String that will hold the
     * selected identifier strings
     * </p>
     *
     * <p>
     * This property is not required. If not the set the framework will use a property contained on
     * <code>UifFormBase</code>
     * </p>
     *
     * @return property name for select field
     */
    @BeanTagAttribute(name = "lineSelectPropertyName")
    public String getLineSelectPropertyName() {
        return lineSelectPropertyName;
    }

    /**
     * Setter for the property name that will bind to the select field
     *
     * @param lineSelectPropertyName
     */
    public void setLineSelectPropertyName(String lineSelectPropertyName) {
        this.lineSelectPropertyName = lineSelectPropertyName;
    }

    /**
     * Instance of the <code>QuickFinder</code> widget that configures a multi-value lookup for the collection
     *
     * <p>
     * If the collection lookup is enabled (by the render property of the quick finder), {@link
     * #getCollectionObjectClass()} will be used as the data object class for the lookup (if not set). Field
     * conversions need to be set as usual and will be applied for each line returned
     * </p>
     *
     * @return instance configured for the collection lookup
     */
    @BeanTagAttribute(name = "collectionLookup", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public QuickFinder getCollectionLookup() {
        return collectionLookup;
    }

    /**
     * Setter for the collection lookup quickfinder instance
     *
     * @param collectionLookup
     */
    public void setCollectionLookup(QuickFinder collectionLookup) {
        this.collectionLookup = collectionLookup;
    }

    /**
     * Indicates whether inactive collections lines should be displayed
     *
     * <p>
     * Setting only applies when the collection line type implements the
     * <code>Inactivatable</code> interface. If true and showInactive is
     * set to false, the collection will be filtered to remove any items
     * whose active status returns false
     * </p>
     *
     * @return true to show inactive records, false to not render inactive records
     */
    @BeanTagAttribute(name = "showInactiveLines")
    public boolean isShowInactiveLines() {
        return showInactiveLines;
    }

    /**
     * Setter for the show inactive indicator
     *
     * @param showInactiveLines boolean show inactive
     */
    public void setShowInactiveLines(boolean showInactiveLines) {
        this.showInactiveLines = showInactiveLines;
    }

    /**
     * Collection filter instance for filtering the collection data when the
     * showInactive flag is set to false
     *
     * @return CollectionFilter
     */
    @BeanTagAttribute(name = "activeCollectionFilter", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public CollectionFilter getActiveCollectionFilter() {
        return activeCollectionFilter;
    }

    /**
     * Setter for the collection filter to use for filter inactive records from the
     * collection
     *
     * @param activeCollectionFilter CollectionFilter instance
     */
    public void setActiveCollectionFilter(CollectionFilter activeCollectionFilter) {
        this.activeCollectionFilter = activeCollectionFilter;
    }

    /**
     * List of {@link CollectionFilter} instances that should be invoked to filter the collection before
     * displaying
     *
     * @return List<CollectionFilter>
     */
    @BeanTagAttribute(name = "filters", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<CollectionFilter> getFilters() {
        return filters;
    }

    /**
     * Setter for the List of collection filters for which the collection will be filtered against
     *
     * @param filters
     */
    public void setFilters(List<CollectionFilter> filters) {
        this.filters = filters;
    }

    /**
     * List of <code>CollectionGroup</code> instances that are sub-collections
     * of the collection represented by this collection group
     *
     * @return sub collections
     */
    @BeanTagAttribute(name = "subCollections", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<CollectionGroup> getSubCollections() {
        return this.subCollections;
    }

    /**
     * Setter for the sub collection list
     *
     * @param subCollections
     */
    public void setSubCollections(List<CollectionGroup> subCollections) {
        this.subCollections = subCollections;
    }

    /**
     * Suffix for IDs that identifies the collection line the sub-collection belongs to
     *
     * <p>
     * Built by the framework as the collection lines are being generated
     * </p>
     *
     * @return id suffix for sub-collection
     */
    public String getSubCollectionSuffix() {
        return subCollectionSuffix;
    }

    /**
     * Setter for the sub-collection suffix (used by framework, should not be
     * set in configuration)
     *
     * @param subCollectionSuffix
     */
    public void setSubCollectionSuffix(String subCollectionSuffix) {
        this.subCollectionSuffix = subCollectionSuffix;
    }

    /**
     * Collection Security object that indicates what authorization (permissions) exist for the collection
     *
     * @return CollectionGroupSecurity instance
     */
    public CollectionGroupSecurity getCollectionGroupSecurity() {
        return (CollectionGroupSecurity) super.getComponentSecurity();
    }

    /**
     * Override to assert a {@link CollectionGroupSecurity} instance is set
     *
     * @param componentSecurity instance of CollectionGroupSecurity
     */
    @Override
    public void setComponentSecurity(ComponentSecurity componentSecurity) {
        if ((componentSecurity != null) && !(componentSecurity instanceof CollectionGroupSecurity)) {
            throw new RiceRuntimeException(
                    "Component security for CollectionGroup should be instance of CollectionGroupSecurity");
        }

        super.setComponentSecurity(componentSecurity);
    }

    @Override
    protected Class<? extends ComponentSecurity> getComponentSecurityClass() {
        return CollectionGroupSecurity.class;
    }

    /**
     * <code>CollectionGroupBuilder</code> instance that will build the
     * components dynamically for the collection instance
     *
     * @return CollectionGroupBuilder instance
     */
    @BeanTagAttribute(name = "collectionGroupBuilder", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public CollectionGroupBuilder getCollectionGroupBuilder() {
        if (this.collectionGroupBuilder == null) {
            this.collectionGroupBuilder = new CollectionGroupBuilder();
        }
        return this.collectionGroupBuilder;
    }

    /**
     * Setter for the collection group building instance
     *
     * @param collectionGroupBuilder
     */
    public void setCollectionGroupBuilder(CollectionGroupBuilder collectionGroupBuilder) {
        this.collectionGroupBuilder = collectionGroupBuilder;
    }

    /**
     * @param renderInactiveToggleButton the showHideInactiveButton to set
     */
    public void setRenderInactiveToggleButton(boolean renderInactiveToggleButton) {
        this.renderInactiveToggleButton = renderInactiveToggleButton;
    }

    /**
     * @return the showHideInactiveButton
     */
    @BeanTagAttribute(name = "renderInactiveToggleButton")
    public boolean isRenderInactiveToggleButton() {
        return renderInactiveToggleButton;
    }

    /**
     * The number of records to display for a collection
     *
     * @return int
     */
    @BeanTagAttribute(name = "displayCollectionSize")
    public int getDisplayCollectionSize() {
        return this.displayCollectionSize;
    }

    /**
     * Setter for the display collection size
     *
     * @param displayCollectionSize
     */
    public void setDisplayCollectionSize(int displayCollectionSize) {
        this.displayCollectionSize = displayCollectionSize;
    }

    /**
     * Indicates whether new items should be styled with the #newItemsCssClass
     *
     * @return true if new items must be highlighted
     */
    @BeanTagAttribute(name = "highlightNewItems")
    public boolean isHighlightNewItems() {
        return highlightNewItems;
    }

    /**
     * Setter for the flag that allows for different styling of new items
     *
     * @param highlightNewItems
     */
    public void setHighlightNewItems(boolean highlightNewItems) {
        this.highlightNewItems = highlightNewItems;
    }

    /**
     * The css style class that will be added on new items
     *
     * @return the new items css style class
     */
    @BeanTagAttribute(name = "newItemsCssClass")
    public String getNewItemsCssClass() {
        return newItemsCssClass;
    }

    /**
     * Setter for the new items css style class
     *
     * @param newItemsCssClass
     */
    public void setNewItemsCssClass(String newItemsCssClass) {
        this.newItemsCssClass = newItemsCssClass;
    }

    /**
     * The css style class that will be added on the add item group or row
     *
     * @return the add item group or row css style class
     */
    @BeanTagAttribute(name = "addItemCssClass")
    public String getAddItemCssClass() {
        return addItemCssClass;
    }

    /**
     * Setter for the add item css style class
     *
     * @param addItemCssClass
     */
    public void setAddItemCssClass(String addItemCssClass) {
        this.addItemCssClass = addItemCssClass;
    }

    /**
     * Indicates whether the add item group or row should be styled with the #addItemCssClass
     *
     * @return true if add item group or row must be highlighted
     */
    @BeanTagAttribute(name = "highlightAddItem")
    public boolean isHighlightAddItem() {
        return highlightAddItem;
    }

    /**
     * Setter for the flag that allows for different styling of the add item group or row
     *
     * @param highlightAddItem
     */
    public void setHighlightAddItem(boolean highlightAddItem) {
        this.highlightAddItem = highlightAddItem;
    }

    /**
     * Indicates that a button will be rendered that allows the user to add blank lines to the collection
     *
     * <p>
     * The button will be added separately from the collection items. The default add line wil not be rendered. The
     * action of the button will call the controller, add the blank line to the collection and do a component refresh.
     * </p>
     *
     * @return boolean
     */
    @BeanTagAttribute(name = "renderAddBlankLineButton")
    public boolean isRenderAddBlankLineButton() {
        return renderAddBlankLineButton;
    }

    /**
     * Setter for the flag indicating that the add blank line button must be rendered
     *
     * @param renderAddBlankLineButton
     */
    public void setRenderAddBlankLineButton(boolean renderAddBlankLineButton) {
        this.renderAddBlankLineButton = renderAddBlankLineButton;
    }

    /**
     * The add blank line {@link Action} field rendered when renderAddBlankLineButton is true
     *
     * @return boolean
     */
    @BeanTagAttribute(name = "addBlankLineAction", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Action getAddBlankLineAction() {
        return addBlankLineAction;
    }

    /**
     * Setter for the add blank line {@link Action} field
     *
     * @param addBlankLineAction
     */
    public void setAddBlankLineAction(Action addBlankLineAction) {
        this.addBlankLineAction = addBlankLineAction;
    }

    /**
     * Indicates the add line placement
     *
     * <p>
     * Valid values are 'TOP' or 'BOTTOM'. The default is 'TOP'. When the value is 'BOTTOM' the blank line will be
     * added
     * to the end of the collection.
     * </p>
     *
     * @return the add blank line action placement
     */
    @BeanTagAttribute(name = "addLinePlacement")
    public String getAddLinePlacement() {
        return addLinePlacement;
    }

    /**
     * Setter for the add line placement
     *
     * @param addLinePlacement add line placement string
     */
    public void setAddLinePlacement(String addLinePlacement) {
        this.addLinePlacement = addLinePlacement;
    }

    /**
     * Indicates whether the save line actions should be rendered
     *
     * @return boolean
     */
    @BeanTagAttribute(name = "renderSaveLineActions")
    public boolean isRenderSaveLineActions() {
        return renderSaveLineActions;
    }

    /**
     * Setter for the flag indicating whether the save actions should be rendered
     *
     * @param renderSaveLineActions
     */
    public void setRenderSaveLineActions(boolean renderSaveLineActions) {
        this.renderSaveLineActions = renderSaveLineActions;
    }

    /**
     * Indicates that a add action should be rendered and that the add group be displayed in a lightbox
     *
     * @return boolean
     */
    @BeanTagAttribute(name = "addViaLightBox")
    public boolean isAddViaLightBox() {
        return addViaLightBox;
    }

    /**
     * Setter for the flag to indicate that add groups should be displayed in a light box
     *
     * @param addViaLightBox
     */
    public void setAddViaLightBox(boolean addViaLightBox) {
        this.addViaLightBox = addViaLightBox;
    }

    /**
     * The {@link Action} that will be displayed that will open the add line group in a lightbox
     *
     * @return Action
     */
    @BeanTagAttribute(name = "addViaLightBoxAction", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Action getAddViaLightBoxAction() {
        return addViaLightBoxAction;
    }

    /**
     * Setter for the add line via lightbox {@link Action}
     *
     * @param addViaLightBoxAction
     */
    public void setAddViaLightBoxAction(Action addViaLightBoxAction) {
        this.addViaLightBoxAction = addViaLightBoxAction;
    }

    /**
     * Gets useServerPaging, the flag that indicates whether server side paging is enabled.  Defaults to false.
     *
     * @return true if server side paging is enabled.
     */
    @BeanTagAttribute(name = "useServerPaging")
    public boolean isUseServerPaging() {
        return useServerPaging;
    }

    /**
     * Sets useServerPaging, the flag indicating whether server side paging is enabled.
     *
     * @param useServerPaging the useServerPaging value to set
     */
    public void setUseServerPaging(boolean useServerPaging) {
        this.useServerPaging = useServerPaging;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets the displayStart, the index of the first item to display on the page (assuming useServerPaging is enabled).
     *
     * <p>if this field has not been set, the returned value will be -1</p>
     *
     * @return the index of the first item to display, or -1 if unset
     */
    public int getDisplayStart() {
        return displayStart;
    }

    /**
     * Sets the displayStart, the index of the first item to display on the page (assuming useServerPaging is enabled).
     *
     * @param displayStart the displayStart to set
     */
    public void setDisplayStart(int displayStart) {
        this.displayStart = displayStart;
    }

    /**
     * Gets the displayLength, the number of items to display on the page (assuming useServerPaging is enabled).
     *
     * <p>if this field has not been set, the returned value will be -1</p>
     *
     * @return the number of items to display on the page, or -1 if unset
     */
    public int getDisplayLength() {
        return displayLength;
    }

    /**
     * Sets the displayLength, the number of items to display on the page (assuming useServerPaging is enabled).
     *
     * @param displayLength the displayLength to set
     */
    public void setDisplayLength(int displayLength) {
        this.displayLength = displayLength;
    }

    /**
     * Gets the number of un-filtered elements from the model collection.
     *
     * <p>if this field has not been set, the returned value will be -1</p>
     *
     * @return the filtered collection size, or -1 if unset
     */
    public int getFilteredCollectionSize() {
        return filteredCollectionSize;
    }

    /**
     * Sets the number of un-filtered elements from the model collection.
     *
     * <p>This value is used for display and rendering purposes, it has no effect on the model collection</p>
     *
     * @param filteredCollectionSize the filtered collection size
     */
    public void setFilteredCollectionSize(int filteredCollectionSize) {
        this.filteredCollectionSize = filteredCollectionSize;
    }

    /**
     * @return list of total columns
     */
    @BeanTagAttribute(name = "addTotalColumns")
    protected List<String> getTotalColumns() {
        return totalColumns;
    }

    /**
     * Setter for the total columns
     *
     * @param totalColumns
     */
    protected void setTotalColumns(List<String> totalColumns) {
        this.totalColumns = totalColumns;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean(this);

        // Checking if collectionObjectClass is set
        if (getCollectionObjectClass() == null) {
            if (Validator.checkExpressions(this, "collectionObjectClass")) {
                String currentValues[] = {"collectionObjectClass = " + getCollectionObjectClass()};
                tracer.createWarning("CollectionObjectClass is not set (disregard if part of an abstract)",
                        currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        CollectionGroup collectionGroupCopy = (CollectionGroup) component;

        collectionGroupCopy.setDisplayCollectionSize(this.displayCollectionSize);
        collectionGroupCopy.setActiveCollectionFilter(this.activeCollectionFilter);

        if (this.addBlankLineAction != null) {
            collectionGroupCopy.setAddBlankLineAction((Action) this.addBlankLineAction.copy());
        }

        collectionGroupCopy.setAddItemCssClass(this.addItemCssClass);

        if (addLineItems != null && !addLineItems.isEmpty()) {
            List<Component> addLineItemsCopy = new ArrayList<Component>();

            for (Component addLineItem : this.addLineItems) {
                addLineItemsCopy.add((Component) addLineItem.copy());
            }

            collectionGroupCopy.setAddLineItems(addLineItemsCopy);
        }

        if (addLineActions != null && !addLineActions.isEmpty()) {
            List<Action> addLineActionsCopy = new ArrayList<Action>();

            for (Action addLineAction : this.addLineActions) {
                addLineActionsCopy.add((Action) addLineAction.copy());
            }

            collectionGroupCopy.setAddLineActions(addLineActionsCopy);
        }

        if (this.addLineBindingInfo != null) {
            collectionGroupCopy.setAddLineBindingInfo((BindingInfo) this.addLineBindingInfo.copy());
        }

        if (this.addLineLabel != null) {
            collectionGroupCopy.setAddLineLabel((Message) this.addLineLabel.copy());
        }

        collectionGroupCopy.setAddLinePlacement(this.addLinePlacement);
        collectionGroupCopy.setAddLinePropertyName(this.addLinePropertyName);
        collectionGroupCopy.setAddViaLightBox(this.addViaLightBox);

        if (this.addViaLightBoxAction != null) {
            collectionGroupCopy.setAddViaLightBoxAction((Action) this.addViaLightBoxAction.copy());
        }

        if (this.bindingInfo != null) {
            collectionGroupCopy.setBindingInfo((BindingInfo) this.bindingInfo.copy());
        }

        if (this.collectionLookup != null) {
            collectionGroupCopy.setCollectionLookup((QuickFinder) this.collectionLookup.copy());
        }

        collectionGroupCopy.setCollectionObjectClass(this.collectionObjectClass);
        
        if (this.filters != null && !this.filters.isEmpty()) {
            collectionGroupCopy.setFilters(new ArrayList<CollectionFilter>(this.filters));
        }
        
        collectionGroupCopy.setHighlightAddItem(this.highlightAddItem);
        collectionGroupCopy.setHighlightNewItems(this.highlightNewItems);
        collectionGroupCopy.setIncludeLineSelectionField(this.includeLineSelectionField);
        collectionGroupCopy.setUseServerPaging(this.useServerPaging);
        collectionGroupCopy.setPageSize(this.pageSize);
        collectionGroupCopy.setDisplayStart(this.displayStart);
        collectionGroupCopy.setDisplayLength(this.displayLength);

        if (lineActions != null && !lineActions.isEmpty()) {
            List<Action> lineActions = new ArrayList<Action>();
            for (Action lineAction : this.lineActions) {
                lineActions.add((Action) lineAction.copy());
            }

            collectionGroupCopy.setLineActions(lineActions);
        }
        
        collectionGroupCopy.setLineSelectPropertyName(this.lineSelectPropertyName);
        collectionGroupCopy.setNewItemsCssClass(this.newItemsCssClass);
        collectionGroupCopy.setPropertyName(this.propertyName);
        collectionGroupCopy.setRenderAddBlankLineButton(this.renderAddBlankLineButton);
        collectionGroupCopy.setRenderAddLine(this.renderAddLine);
        collectionGroupCopy.setRenderInactiveToggleButton(this.renderInactiveToggleButton);
        collectionGroupCopy.setActiveCollectionFilter(this.activeCollectionFilter);
        collectionGroupCopy.setFilters(this.filters);

        collectionGroupCopy.setRenderLineActions(this.renderLineActions);
        collectionGroupCopy.setRenderSaveLineActions(this.renderSaveLineActions);
        collectionGroupCopy.setShowInactiveLines(this.showInactiveLines);

        if (subCollections != null && !subCollections.isEmpty()) {
            List<CollectionGroup> subCollectionsCopy = new ArrayList<CollectionGroup>();
            for (CollectionGroup subCollection : this.subCollections) {
                subCollectionsCopy.add((CollectionGroup) subCollection.copy());
            }

            collectionGroupCopy.setSubCollections(subCollectionsCopy);
        }
        collectionGroupCopy.setSubCollectionSuffix(this.subCollectionSuffix);

        if (this.totalColumns != null) {
            collectionGroupCopy.setTotalColumns(new ArrayList<String>(this.totalColumns));
        }
    }
}
