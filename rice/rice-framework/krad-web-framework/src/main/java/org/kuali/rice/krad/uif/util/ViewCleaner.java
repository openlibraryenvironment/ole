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

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.container.PageGroup;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.layout.StackedLayoutManager;
import org.kuali.rice.krad.uif.layout.TableLayoutManager;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for trimming component instances for storage
 *
 * <p>
 * Invoked to trim the view instance before storing on the form as the post view. Certain information is keep
 * around to support post methods that need to operate on the previous view configuration. Examples include component
 * refresh and collection add/delete line.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewCleaner {

    /**
     * Cleans a view instance removing all pages except the current page and then invoking the view
     * index to perform cleaning on contained components
     *
     * @param view view instance to clean
     */
    public static void cleanView(View view) {
        view.setApplicationHeader(null);
        view.setApplicationFooter(null);
        view.setBreadcrumbs(null);
        view.setBreadcrumbOptions(null);
        view.setBreadcrumbItem(null);
        view.setParentLocation(null);
        view.setPathBasedBreadcrumbs(null);
        view.setNavigation(null);
        view.setPage(null);
        view.setNavigation(null);
        view.setAdditionalCssFiles(null);
        view.setAdditionalScriptFiles(null);
        view.setActionFlags(null);
        view.setEditModes(null);
        view.setViewMenuLink(null);
        view.setViewMenuLink(null);
        view.setPreLoadScript(null);
        view.setViewTemplates(new ArrayList<String>());
        view.setSessionPolicy(null);
        view.setViewHelperServiceClass(null);

        view.getViewIndex().clearIndexesAfterRender();

        // clear all view pages exception the current page
        PageGroup currentPage = view.getCurrentPage();
        cleanComponent(currentPage, view.getViewIndex());

        cleanComponent(view, view.getViewIndex());

        List<Component> pages = new ArrayList<Component>();
        pages.add(currentPage);

        view.setItems(pages);
    }

    /**
     * Cleans a component instance removing properties not needed for posting
     *
     * @param component instance to clean
     */
    public static void cleanComponent(Component component, ViewIndex viewIndex) {
        if (component == null) {
            return;
        }

        if (component.isForceSessionPersistence()) {
            return;
        }

        component.setTemplate(null);
        component.setTemplateName(null);
        component.setTitle(null);
        component.setProgressiveRender(null);
        component.setConditionalRefresh(null);
        component.setRefreshWhenChangedPropertyNames(null);
        component.setAdditionalComponentsToRefresh(null);
        component.setAlign(null);
        component.setValign(null);
        component.setWidth(null);
        component.setCellCssClasses(null);
        component.setCellStyle(null);
        component.setCellWidth(null);
        component.setStyle(null);
        component.setLibraryCssClasses(null);
        component.setCssClasses(null);
        component.setAdditionalCssClasses(null);
        component.setToolTip(null);
        component.setRenderedHtmlOutput(null);
        component.setComponentSecurity(null);
        component.setOnLoadScript(null);
        component.setOnUnloadScript(null);
        component.setOnCloseScript(null);
        component.setOnBlurScript(null);
        component.setOnChangeScript(null);
        component.setOnClickScript(null);
        component.setOnDblClickScript(null);
        component.setOnFocusScript(null);
        component.setOnSubmitScript(null);
        component.setOnKeyPressScript(null);
        component.setOnKeyUpScript(null);
        component.setOnKeyDownScript(null);
        component.setOnMouseOverScript(null);
        component.setOnMouseOutScript(null);
        component.setOnMouseUpScript(null);
        component.setOnMouseDownScript(null);
        component.setOnMouseMoveScript(null);
        component.setOnDocumentReadyScript(null);
        component.setComponentModifiers(null);
        component.setTemplateOptions(null);
        component.setTemplateOptionsJSString(null);
        component.setPropertyReplacers(null);

        if (!viewIndex.isIdForRefreshComponent(component.getId())) {
            component.setDataAttributes(null);
        }

        component.setPreRenderContent(null);
        component.setPostRenderContent(null);
        component.setExpressionGraph(null);
        component.setPropertyExpressions(null);

        // keep context for the view, page, and any component that can be refreshed, since they
        // are needed for that process
        if (!viewIndex.isIdForRefreshComponent(component.getId()) && !(component instanceof PageGroup)
                && !(component instanceof View)) {
            component.setContext(null);
        }

        if (component instanceof Container) {
            cleanContainer((Container) component, viewIndex);
        }

        if (component instanceof DataField) {
            cleanDataField((DataField) component);
        }

        if (component instanceof FieldGroup) {
            Component group = ((FieldGroup) component).getGroup();
            cleanComponent(group, viewIndex);
        }
    }

    /**
     * General purpose method to clean any container, removes all nested components except the items list
     *
     * @param container container instance to clean
     */
    protected static void cleanContainer(Container container, ViewIndex viewIndex) {
        container.setHeader(null);
        container.setFooter(null);
        container.setHelp(null);
        container.setInstructionalMessage(null);

        // keep validation messages for page, since they are refreshed with component refresh
        if (!(container instanceof PageGroup)) {
            container.setValidationMessages(null);
        }

        if (container.getItems() != null) {
            for (Component item : container.getItems()) {
                cleanComponent(item, viewIndex);
            }
        }

        if (container instanceof Group) {
            Group group = (Group) container;

            group.setDisclosure(null);
            group.setScrollpane(null);
        }

        if (container instanceof CollectionGroup) {
            cleanCollectionGroup((CollectionGroup) container, viewIndex);
        }
        else {
            container.setLayoutManager(null);
        }
    }

    /**
     * Cleans a collection group instance removing the items and collection prototypes (note add line fields
     * are keep around to support the add line action)
     *
     * @param collectionGroup collection group instance to clean
     */
    protected static void cleanCollectionGroup(CollectionGroup collectionGroup, ViewIndex viewIndex) {
        collectionGroup.setAddLineLabel(null);

        if (collectionGroup.getAddLineItems() != null) {
            for (Component item : collectionGroup.getAddLineItems()) {
                cleanComponent(item, viewIndex);
            }
        }

        collectionGroup.setAddLineActions(null);
        collectionGroup.setLineActions(null);
        collectionGroup.setActiveCollectionFilter(null);
        collectionGroup.setFilters(null);
        collectionGroup.setSubCollections(null);
        collectionGroup.setCollectionGroupBuilder(null);
        collectionGroup.setNewItemsCssClass(null);
        collectionGroup.setAddItemCssClass(null);
        collectionGroup.setAddBlankLineAction(null);
        collectionGroup.setAddViaLightBoxAction(null);

        if ((collectionGroup.getLayoutManager() != null) && (collectionGroup
                .getLayoutManager() instanceof TableLayoutManager) && !collectionGroup.isUseServerPaging()) {

            cleanTableLayoutManager((TableLayoutManager) collectionGroup.getLayoutManager());
        }
        else if ((collectionGroup.getLayoutManager() != null) && (collectionGroup
                        .getLayoutManager() instanceof StackedLayoutManager)) {
             collectionGroup.setLayoutManager(null);
        }
    }

    /**
     * Cleans a table layout manager instance removing unneeded prototypes
     *
     * @param tableLayoutManager table layout instance to clean
     */
    protected static void cleanTableLayoutManager(TableLayoutManager tableLayoutManager) {
        tableLayoutManager.setHeaderLabelPrototype(null);
        tableLayoutManager.setSequenceFieldPrototype(null);
        tableLayoutManager.setActionFieldPrototype(null);
        tableLayoutManager.setSubCollectionFieldGroupPrototype(null);
        tableLayoutManager.setSelectFieldPrototype(null);
        tableLayoutManager.setAddLineGroup(null);
        tableLayoutManager.setRowDetailsGroup(null);
        tableLayoutManager.setToggleAllDetailsAction(null);
        tableLayoutManager.setExpandDetailsActionPrototype(null);
        tableLayoutManager.setGroupingPropertyNames(null);
        tableLayoutManager.setTotalLabel(null);
        tableLayoutManager.setPageTotalLabel(null);
        tableLayoutManager.setGroupTotalLabelPrototype(null);
        tableLayoutManager.setColumnCalculations(null);
    }

    /**
     * Cleans an data field instance
     *
     * @param dataField data field instance to clean
     */
    protected static void cleanDataField(DataField dataField) {
        dataField.setLabel(null);
        dataField.setFieldLabel(null);
        dataField.setDictionaryAttributeName(null);
        dataField.setDictionaryObjectEntry(null);
        dataField.setForcedValue(null);
        dataField.setReadOnlyDisplayReplacementPropertyName(null);
        dataField.setReadOnlyDisplaySuffixPropertyName(null);
        dataField.setReadOnlyDisplayReplacement(null);
        dataField.setReadOnlyDisplaySuffix(null);
        dataField.setReadOnlyListDisplayType(null);
        dataField.setReadOnlyListDelimiter(null);
        dataField.setMaskFormatter(null);
        dataField.setAdditionalHiddenPropertyNames(null);
        dataField.setPropertyNamesForAdditionalDisplay(null);
        dataField.setInquiry(null);
        dataField.setHelp(null);

        if (dataField instanceof InputField) {
            cleanInputField((InputField) dataField);
        }
    }

    /**
     * Cleans an input field instance removing the control and inherited component properties
     *
     * @param inputField input field instance to clean
     */
    protected static void cleanInputField(InputField inputField) {
        inputField.setCustomValidatorClass(null);
        inputField.setValidCharactersConstraint(null);
        inputField.setCaseConstraint(null);
        inputField.setDependencyConstraints(null);
        inputField.setMustOccurConstraints(null);
        inputField.setControl(null);
        inputField.setOptionsFinder(null);
        inputField.setValidationMessages(null);
        inputField.setConstraintText(null);
        inputField.setInstructionalText(null);
        inputField.setInstructionalMessage(null);
        inputField.setConstraintMessage(null);
        inputField.setQuickfinder(null);
    }
}
