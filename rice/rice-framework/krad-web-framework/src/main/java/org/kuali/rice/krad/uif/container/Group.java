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

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.Disclosure;
import org.kuali.rice.krad.uif.widget.Scrollpane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Container that holds a list of <code>Field</code> or other <code>Group</code>
 * instances
 *
 * <p>
 * Groups can exist at different levels of the <code>View</code>, providing
 * conceptual groupings such as the page, section, and group. In addition, other
 * group types can be created to add behavior like collection support
 * </p>
 *
 * <p>
 * <code>Group</code> implementation has properties for defaulting the binding
 * information (such as the parent object path and a binding prefix) for the
 * fields it contains. During the phase these properties (if given) are set on
 * the fields contained in the <code>Group</code> that implement
 * <code>DataBinding</code>, unless they have already been set on the field.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "group-bean", parent = "Uif-GroupBase"),
        @BeanTag(name = "boxGroupBase-bean", parent = "Uif-BoxGroupBase"),
        @BeanTag(name = "verticalBoxGroup-bean", parent = "Uif-VerticalBoxGroup"),
        @BeanTag(name = "verticalBoxSection-bean", parent = "Uif-VerticalBoxSection"),
        @BeanTag(name = "verticalBoxSubSection-bean", parent = "Uif-VerticalBoxSubSection"),
        @BeanTag(name = "disclosure-verticalBoxSection-bean", parent = "Uif-Disclosure-VerticalBoxSection"),
        @BeanTag(name = "disclosure-verticalBoxSubSection-bean", parent = "Uif-Disclosure-VerticalBoxSubSection"),
        @BeanTag(name = "horizontalBoxGroup-bean", parent = "Uif-HorizontalBoxGroup"),
        @BeanTag(name = "horizontalBoxSection-bean", parent = "Uif-HorizontalBoxSection"),
        @BeanTag(name = "horizontalBoxSubSection-bean", parent = "Uif-HorizontalBoxSubSection"),
        @BeanTag(name = "disclosure-horizontalBoxSection-bean", parent = "Uif-Disclosure-HorizontalBoxSection"),
        @BeanTag(name = "disclosure-horizontalBoxSubSection-bean", parent = "Uif-Disclosure-HorizontalBoxSubSection"),
        @BeanTag(name = "gridGroup-bean", parent = "Uif-GridGroup"),
        @BeanTag(name = "gridSection-bean", parent = "Uif-GridSection"),
        @BeanTag(name = "gridSubSection-bean", parent = "Uif-GridSubSection"),
        @BeanTag(name = "disclosure-gridSection-bean", parent = "Uif-Disclosure-GridSection"),
        @BeanTag(name = "fixedCssGridGroup-bean", parent = "Uif-FixedCssGridGroup"),
        @BeanTag(name = "fixedCssGridSection-bean", parent = "Uif-FixedCssGridSection"),
        @BeanTag(name = "fixedCssGridSubSection-bean", parent = "Uif-FixedCssGridSubSection"),
        @BeanTag(name = "fluidCssGridGroup-bean", parent = "Uif-FluidCssGridGroup"),
        @BeanTag(name = "fluidCssGridSection-bean", parent = "Uif-FluidCssGridSection"),
        @BeanTag(name = "fluidCssGridSubSection-bean", parent = "Uif-FluidCssGridSubSection"),
        @BeanTag(name = "listGroup-bean", parent = "Uif-ListGroup"),
        @BeanTag(name = "listSection-bean", parent = "Uif-ListSection"),
        @BeanTag(name = "listSubSection-bean", parent = "Uif-ListSubSection"),
        @BeanTag(name = "disclosure-listSection-bean", parent = "Uif-Disclosure-ListSection"),
        @BeanTag(name = "disclosure-listSubSection-bean", parent = "Uif-Disclosure-ListSubSection"),
        @BeanTag(name = "collectionGridItem-bean", parent = "Uif-CollectionGridItem"),
        @BeanTag(name = "collectionVerticalBoxItem-bean", parent = "Uif-CollectionVerticalBoxItem"),
        @BeanTag(name = "collectionHorizontalBoxItem-bean", parent = "Uif-CollectionHorizontalBoxItem"),
        @BeanTag(name = "headerUpperGroup-bean", parent = "Uif-HeaderUpperGroup"),
        @BeanTag(name = "headerRightGroup-bean", parent = "Uif-HeaderRightGroup"),
        @BeanTag(name = "headerLowerGroup-bean", parent = "Uif-HeaderLowerGroup"),
        @BeanTag(name = "footer-bean", parent = "Uif-FooterBase"),
        @BeanTag(name = "formFooter-bean", parent = "Uif-FormFooter"),
        @BeanTag(name = "actionsGroup-bean", parent = "Uif-ActionsGroup"),
        @BeanTag(name = "disclosureActionsGroup-bean", parent = "Uif-DisclosureActionsGroup"),
        @BeanTag(name = "disclosureActions-reqMessageGroup-bean", parent = "Uif-DisclosureActions-ReqMessageGroup"),
        @BeanTag(name = "inactiveItemsActionsGroup-bean", parent = "Uif-InactiveItemsActionsGroup"),
        @BeanTag(name = "documentInfoGroup-bean", parent = "Uif-DocumentInfoGroup"),
        @BeanTag(name = "documentOverviewSection-bean", parent = "Uif-DocumentOverviewSection"),
        @BeanTag(name = "documentAdHocRecipientsSection-bean", parent = "Uif-DocumentAdHocRecipientsSection"),
        @BeanTag(name = "documentRouteLogSection-bean", parent = "Uif-DocumentRouteLogSection"),
        @BeanTag(name = "documentPageFooter-bean", parent = "Uif-DocumentPageFooter"),
        @BeanTag(name = "incidentDetailGroup-bean", parent = "Uif-IncidentDetailGroup"),
        @BeanTag(name = "incidentStackTraceGroup-bean", parent = "Uif-IncidentStackTraceGroup"),
        @BeanTag(name = "incidentReportFooter-bean", parent = "Uif-IncidentReportFooter"),
        @BeanTag(name = "initiatedDocumentFooter-bean", parent = "InitiatedDocumentFooter"),
        @BeanTag(name = "inquiryFooter-bean", parent = "Uif-InquiryFooter"),
        @BeanTag(name = "lookupCriteriaGroup-bean", parent = "Uif-LookupCriteriaGroup"),
        @BeanTag(name = "lookupPageHeaderGroup-bean", parent = "Uif-LookupPageHeaderGroup"),
        @BeanTag(name = "lookupCriteriaFooter-bean", parent = "Uif-LookupCriteriaFooter"),
        @BeanTag(name = "lookupResultsFooter-bean", parent = "Uif-LookupResultsFooter"),
        @BeanTag(name = "maintenanceGridGroup-bean", parent = "Uif-MaintenanceGridGroup"),
        @BeanTag(name = "maintenanceHorizontalBoxGroup-bean", parent = "Uif-MaintenanceHorizontalBoxGroup"),
        @BeanTag(name = "maintenanceVerticalBoxGroup-bean", parent = "Uif-MaintenanceVerticalBoxGroup"),
        @BeanTag(name = "maintenanceGridSection-bean", parent = "Uif-MaintenanceGridSection"),
        @BeanTag(name = "maintenanceGridSubSection-bean", parent = "Uif-MaintenanceGridSubSection"),
        @BeanTag(name = "maintenanceHorizontalBoxSection-bean", parent = "Uif-MaintenanceHorizontalBoxSection"),
        @BeanTag(name = "maintenanceVerticalBoxSection-bean", parent = "Uif-MaintenanceVerticalBoxSection"),
        @BeanTag(name = "maintenanceHorizontalBoxSubSection-bean", parent = "Uif-MaintenanceHorizontalBoxSubSection"),
        @BeanTag(name = "maintenanceVerticalBoxSubSection-bean", parent = "Uif-MaintenanceVerticalBoxSubSection")
})
public class Group extends ContainerBase {
    private static final long serialVersionUID = 7953641325356535509L;

    private String fieldBindByNamePrefix;
    private String fieldBindingObjectPath;

    private Disclosure disclosure;
    private Scrollpane scrollpane;

    private List<? extends Component> items;

    /**
     * Default Constructor
     */
    public Group() {
        items = Collections.emptyList();
    }

    /**
     * The following actions are performed:
     *
     * <ul>
     * <li>Sets the bindByNamePrefix if blank on any InputField and
     * FieldGroup instances within the items List</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        for (Component component : getItems()) {
            // append group's field bind by name prefix (if set) to each
            // attribute field's binding prefix
            if (component instanceof DataBinding) {
                DataBinding dataBinding = (DataBinding) component;

                if (StringUtils.isNotBlank(getFieldBindByNamePrefix())) {
                    String bindByNamePrefixToSet = getFieldBindByNamePrefix();

                    if (StringUtils.isNotBlank(dataBinding.getBindingInfo().getBindByNamePrefix())) {
                        bindByNamePrefixToSet += "." + dataBinding.getBindingInfo().getBindByNamePrefix();
                    }
                    dataBinding.getBindingInfo().setBindByNamePrefix(bindByNamePrefixToSet);
                }

                if (StringUtils.isNotBlank(fieldBindingObjectPath) && StringUtils.isBlank(
                        dataBinding.getBindingInfo().getBindingObjectPath())) {
                    dataBinding.getBindingInfo().setBindingObjectPath(fieldBindingObjectPath);
                }
            }
            // set on FieldGroup's group to recursively set AttributeFields
            else if (component instanceof FieldGroup) {
                FieldGroup fieldGroup = (FieldGroup) component;

                if (fieldGroup.getGroup() != null) {
                    if (StringUtils.isBlank(fieldGroup.getGroup().getFieldBindByNamePrefix())) {
                        fieldGroup.getGroup().setFieldBindByNamePrefix(fieldBindByNamePrefix);
                    }
                    if (StringUtils.isBlank(fieldGroup.getGroup().getFieldBindingObjectPath())) {
                        fieldGroup.getGroup().setFieldBindingObjectPath(fieldBindingObjectPath);
                    }
                }
            } else if (component instanceof Group) {
                Group subGroup = (Group) component;
                if (StringUtils.isNotBlank(getFieldBindByNamePrefix())) {
                    if (StringUtils.isNotBlank(subGroup.getFieldBindByNamePrefix())) {
                        subGroup.setFieldBindByNamePrefix(
                                getFieldBindByNamePrefix() + "." + subGroup.getFieldBindByNamePrefix());
                    } else {
                        subGroup.setFieldBindByNamePrefix(getFieldBindByNamePrefix());
                    }
                }
                if (StringUtils.isNotBlank(getFieldBindingObjectPath())) {
                    if (StringUtils.isNotBlank(subGroup.getFieldBindingObjectPath())) {
                        subGroup.setFieldBindingObjectPath(
                                getFieldBindingObjectPath() + "." + subGroup.getFieldBindingObjectPath());
                    } else {
                        subGroup.setFieldBindingObjectPath(getFieldBindingObjectPath());
                    }
                }
            }
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(disclosure);
        components.add(scrollpane);

        return components;
    }

    /**
     * @see org.kuali.rice.krad.uif.container.Container#getSupportedComponents()
     */
    @Override
    public Set<Class<? extends Component>> getSupportedComponents() {
        Set<Class<? extends Component>> supportedComponents = new HashSet<Class<? extends Component>>();
        supportedComponents.add(Field.class);
        supportedComponents.add(Group.class);

        return supportedComponents;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#getComponentTypeName()
     */
    @Override
    public String getComponentTypeName() {
        return "group";
    }

    /**
     * Binding prefix string to set on each of the groups <code>DataField</code> instances
     *
     * <p>
     * As opposed to setting the bindingPrefix on each attribute field instance,
     * it can be set here for the group. During initialize the string will then
     * be set on each attribute field instance if the bindingPrefix is blank and
     * not a form field
     * </p>
     *
     * @return String binding prefix to set
     */
    @BeanTagAttribute(name = "fieldBindByNamePrefix")
    public String getFieldBindByNamePrefix() {
        return this.fieldBindByNamePrefix;
    }

    /**
     * Setter for the field binding prefix
     *
     * @param fieldBindByNamePrefix
     */
    public void setFieldBindByNamePrefix(String fieldBindByNamePrefix) {
        this.fieldBindByNamePrefix = fieldBindByNamePrefix;
    }

    /**
     * Object binding path to set on each of the group's
     * <code>InputField</code> instances
     *
     * <p>
     * When the attributes of the group belong to a object whose path is
     * different from the default then this property can be given to set each of
     * the attributes instead of setting the model path on each one. The object
     * path can be overridden at the attribute level. The object path is set to
     * the fieldBindingObjectPath during the initialize phase.
     * </p>
     *
     * @return String model path to set
     * @see org.kuali.rice.krad.uif.component.BindingInfo#getBindingObjectPath()
     */
    @BeanTagAttribute(name = "fieldBindingObjectPath")
    public String getFieldBindingObjectPath() {
        return this.fieldBindingObjectPath;
    }

    /**
     * Setter for the field object binding path
     *
     * @param fieldBindingObjectPath
     */
    public void setFieldBindingObjectPath(String fieldBindingObjectPath) {
        this.fieldBindingObjectPath = fieldBindingObjectPath;
    }

    /**
     * Disclosure widget that provides collapse/expand functionality for the
     * group
     *
     * @return Disclosure instance
     */
    @BeanTagAttribute(name = "Disclosure", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Disclosure getDisclosure() {
        return this.disclosure;
    }

    /**
     * Setter for the group's disclosure instance
     *
     * @param disclosure
     */
    public void setDisclosure(Disclosure disclosure) {
        this.disclosure = disclosure;
    }

    /**
     * Scrollpane widget that provides scrolling functionality for the
     * group
     *
     * @return Scrollpane instance
     */
    @BeanTagAttribute(name = "scrollpane", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Scrollpane getScrollpane() {
        return this.scrollpane;
    }

    /**
     * Setter for the group's scrollpane instance
     *
     * @param scrollpane
     */
    public void setScrollpane(Scrollpane scrollpane) {
        this.scrollpane = scrollpane;
    }

    /**
     * @see org.kuali.rice.krad.uif.container.ContainerBase#getItems()
     */
    @Override
    @BeanTagAttribute(name = "items", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<? extends Component> getItems() {
        return this.items;
    }

    /**
     * Setter for the Group's list of components
     *
     * @param items
     */
    @Override
    public void setItems(List<? extends Component> items) {
        this.items = items;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Group groupCopy = (Group) component;
        groupCopy.setFieldBindByNamePrefix(this.fieldBindByNamePrefix);
        groupCopy.setFieldBindingObjectPath(this.fieldBindingObjectPath);

        if (this.disclosure != null) {
            groupCopy.setDisclosure((Disclosure) this.disclosure.copy());
        }

        if (this.scrollpane != null) {
            groupCopy.setScrollpane((Scrollpane) this.scrollpane.copy());
        }

        if (this.items != null) {
            List<Component> items = new ArrayList<Component>();
            for (Component itemComponent : this.items) {
                items.add((Component) itemComponent.copy());
                groupCopy.setItems(items);
            }
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean(this);

        // Checks that no invalid items are present
        for (int i = 0; i < getItems().size(); i++) {
            if (getItems().get(i).getClass() == PageGroup.class || getItems().get(i).getClass()
                    == NavigationGroup.class) {
                String currentValues[] = {"item(" + i + ").class =" + getItems().get(i).getClass()};
                tracer.createError("Items in Group cannot be PageGroup or NaviagtionGroup", currentValues);
            }
        }

        // Checks that the layout manager is set
        if (getLayoutManager() == null) {
            if (Validator.checkExpressions(this, "layoutManager")) {
                String currentValues[] = {"layoutManager = " + getLayoutManager()};
                tracer.createError("LayoutManager must be set", currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    /**
     * Determine the group should be rendered on initial load, or if a loading message should be rendered instead.
     * 
     * @return True if a loading message should be rendered, false if the group should be rendered now.
     */
    public boolean isRenderLoading() {
        return disclosure != null && disclosure.isAjaxRetrievalWhenOpened()
                && (!disclosure.isRender() || !disclosure.isDefaultOpen());
    }

}
