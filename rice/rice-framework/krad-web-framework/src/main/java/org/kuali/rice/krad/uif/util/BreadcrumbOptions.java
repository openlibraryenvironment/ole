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

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.web.form.HistoryFlow;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BreadcrumbOptions represents the options for the current view breadcrumbs that are displayed.
 *
 * <p>
 * This class allows
 * for complete override of all breadcrumbs, and ability to add breadcrumbs before the view and page breadcrumb items.
 * Important note: breadcrumbOptions for preViewBreadcrumbs, prePageBreadcrumbs, and
 * breadcrumbOverrides are inherited from the View if not explicitly set from the PageGroup level's breadcrumbOptions
 * (if they contain a value at the view level and the property is null at the page level - default behavior).
 * Explicitly providing an empty list or setting these properties at the PageGroup level will
 * override this inheritance.
 * </p>
 */
@BeanTag(name = "breadcrumbOptions-bean", parent = "Uif-BreadcrumbOptions")
public class BreadcrumbOptions implements Serializable {

    private static final long serialVersionUID = -6705552809624394000L;

    //custom breadcrumbs
    private List<BreadcrumbItem> homewardPathBreadcrumbs;
    private List<BreadcrumbItem> preViewBreadcrumbs;
    private List<BreadcrumbItem> prePageBreadcrumbs;
    private List<BreadcrumbItem> breadcrumbOverrides;

    /**
     * Sets up the history and breadcrumb configuration for this View.  Should be called from performInitialization.
     *
     * @param model the model
     */
    public void setupBreadcrumbs(View view, Object model) {
        if (model != null && model instanceof UifFormBase) {
            UifFormBase form = (UifFormBase) model;

            //flow is being tracked if there is a flowKey or the breadcrumbs widget is forcing it
            boolean usingFlow = StringUtils.isNotBlank(form.getFlowKey()) || (view.getBreadcrumbs() != null && view
                    .getBreadcrumbs().isUsePathBasedBreadcrumbs());

            //if using flow setup a new HistoryFlow for this view and set into the HistoryManager
            if (usingFlow && form.getHistoryManager() != null) {

                //use original request form key if present to match history flows stored in map (incase key changed)
                String formKey = form.getRequestedFormKey();
                if (StringUtils.isBlank(formKey)) {
                    formKey = form.getFormKey();
                    form.setRequestedFormKey(formKey);
                }

                //get the historyFlow for this view
                HistoryFlow historyFlow = form.getHistoryManager().process(form.getFlowKey(), formKey,
                        form.getRequestUrl());
                if (historyFlow != null) {
                    form.setHistoryFlow(historyFlow);
                    form.setFlowKey(historyFlow.getFlowKey());
                }
            }

            view.getBreadcrumbs().setUsePathBasedBreadcrumbs(usingFlow);

            //get the pastItems from the flow and set them so they can be picked up by the Breadcrumbs widget
            if (view.getBreadcrumbs() != null
                    && view.getBreadcrumbs().isUsePathBasedBreadcrumbs()
                    && form.getHistoryFlow().getPastItems() != null) {
                List<BreadcrumbItem> pastItems = form.getHistoryFlow().getPastItems();

                ComponentUtils.clearIds(pastItems);

                for (BreadcrumbItem item : pastItems) {
                    view.assignComponentIds(item);
                }

                view.setPathBasedBreadcrumbs(pastItems);
            }
        }
    }

    /**
     * Finalize the setup of the BreadcrumbOptions and the BreadcrumbItem for the View.  To be called from the
     * performFinalize method.
     *
     * @param model the model
     */
    public void finalizeBreadcrumbs(View view, Object model, Container parent, BreadcrumbItem breadcrumbItem) {
        //set breadcrumbItem label same as the header, if not set
        if (StringUtils.isBlank(breadcrumbItem.getLabel()) && view.getHeader() != null && !StringUtils.isBlank(
                view.getHeader().getHeaderText()) && model instanceof UifFormBase) {
            breadcrumbItem.setLabel(KRADUtils.generateUniqueViewTitle((UifFormBase) model, view));
        }

        //if label still blank, don't render
        if (StringUtils.isBlank(breadcrumbItem.getLabel())) {
            breadcrumbItem.setRender(false);
        }

        //special breadcrumb request param handling
        if (breadcrumbItem.getUrl().getControllerMapping() == null
                && breadcrumbItem.getUrl().getViewId() == null
                && model instanceof UifFormBase
                && breadcrumbItem.getUrl().getRequestParameters() == null
                && ((UifFormBase) model).getInitialRequestParameters() != null) {
            //add the current request parameters if controllerMapping, viewId, and requestParams are null
            //(this means that no explicit breadcrumbItem customization was set)
            Map<String, String> requestParameters = ((UifFormBase) model).getInitialRequestParameters();

            //remove ajax properties because breadcrumb should always be a full view request
            requestParameters.remove("ajaxReturnType");
            requestParameters.remove("ajaxRequest");

            //remove pageId so we can use special handling
            requestParameters.remove("pageId");

            breadcrumbItem.getUrl().setRequestParameters(requestParameters);
        }

        //form key handling
        if (breadcrumbItem.getUrl().getFormKey() == null
                && model instanceof UifFormBase
                && ((UifFormBase) model).getFormKey() != null) {
            breadcrumbItem.getUrl().setFormKey(((UifFormBase) model).getFormKey());
        }

        //automatically set breadcrumbItem UifUrl properties if not set
        if (breadcrumbItem.getUrl().getControllerMapping() == null && model instanceof UifFormBase) {
            breadcrumbItem.getUrl().setControllerMapping(((UifFormBase) model).getControllerMapping());
        }

        if (breadcrumbItem.getUrl().getViewId() == null) {
            breadcrumbItem.getUrl().setViewId(view.getId());
        }

        //explicitly set the page to default for the view breadcrumb when not using path based (path based will pick
        //up the breadcrumb pageId from the form data automatically)
        if (breadcrumbItem.getUrl().getPageId() == null && !view.getBreadcrumbs().isUsePathBasedBreadcrumbs()) {
            //set breadcrumb to default to the default page if an explicit page id for view breadcrumb is not set
            if (view.getEntryPageId() != null) {
                breadcrumbItem.getUrl().setPageId(view.getEntryPageId());
            } else if (view.isSinglePageView() && view.getPage() != null) {
                //single page
                breadcrumbItem.getUrl().setPageId(view.getPage().getId());
            } else if (!view.getItems().isEmpty() && view.getItems().get(0) != null) {
                //multi page
                breadcrumbItem.getUrl().setPageId(view.getItems().get(0).getId());
            }
        }

        //add to breadcrumbItem to current items if it is set to use in path based
        if (model instanceof UifFormBase && ((UifFormBase) model).getHistoryFlow() != null) {
            ((UifFormBase) model).getHistoryFlow().setCurrentViewItem(view.getBreadcrumbItem());
        }

    }

    /**
     * The homewardPathBreadcrumbs represent the path to "Home" location, these appear before anything else - including
     * parentLocation/path based breadcrumbs.
     *
     * @return the homewardPathBreadcrumbs to render
     */
    @BeanTagAttribute(name = "homewardPathBreadcrumbs", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<BreadcrumbItem> getHomewardPathBreadcrumbs() {
        return homewardPathBreadcrumbs;
    }

    /**
     * Set the homewardPathBreadcrumbs
     *
     * @param homewardPathBreadcrumbs
     */
    public void setHomewardPathBreadcrumbs(List<BreadcrumbItem> homewardPathBreadcrumbs) {
        this.homewardPathBreadcrumbs = homewardPathBreadcrumbs;
    }

    /**
     * The preViewBreadcrumbs list represents BreadcrumbItems that will be shown before the View's BreadcrumbItem,
     * but after any parent location breadcrumbs/path based breadcrumbs (if in use)
     *
     * @return the preViewBreadcrumbs to render
     */
    @BeanTagAttribute(name = "preViewBreadcrumbs", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<BreadcrumbItem> getPreViewBreadcrumbs() {
        return preViewBreadcrumbs;
    }

    /**
     * Set the preViewBreadcrumbs
     *
     * @param preViewBreadcrumbs
     */
    public void setPreViewBreadcrumbs(List<BreadcrumbItem> preViewBreadcrumbs) {
        this.preViewBreadcrumbs = preViewBreadcrumbs;
    }

    /**
     * The prePageBreadcrumbs list represents BreadcrumbItems that will be shown before the PageGroup's BreadcrumbItem,
     * but after the View's BreadcrumbItem.
     *
     * @return the preViewBreadcrumbs to render
     */
    @BeanTagAttribute(name = "prePageBreadcrumbs", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<BreadcrumbItem> getPrePageBreadcrumbs() {
        return prePageBreadcrumbs;
    }

    /**
     * Set the prePageBreadcrumbs
     *
     * @param prePageBreadcrumbs
     */
    public void setPrePageBreadcrumbs(List<BreadcrumbItem> prePageBreadcrumbs) {
        this.prePageBreadcrumbs = prePageBreadcrumbs;
    }

    /**
     * The breadcrumbOverrides are a complete override for all breadcrumbs shown expect for parent location/path
     * breadcrumbs.
     *
     * <p>
     * The BreadcrumbItems set in this list will be used instead of any View, PageGroup, preViewBreadcrumbs, or
     * prePageBreadcrumbs BreadcrumbItems already set.  Each item can be customized fully.  If
     * parent location/path breadcrumbs should also not be shown, set renderParentLocations to false.
     * All other render options set in BreadcrumbOptions will be ignored/not apply as a result of setting this override
     * list.
     * </p>
     *
     * @return the breadcrumbOverride list
     */
    @BeanTagAttribute(name = "breadcrumbOverrides", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<BreadcrumbItem> getBreadcrumbOverrides() {
        return breadcrumbOverrides;
    }

    /**
     * Set the breadcrumbOverrides list
     *
     * @param breadcrumbOverrides
     */
    public void setBreadcrumbOverrides(List<BreadcrumbItem> breadcrumbOverrides) {
        this.breadcrumbOverrides = breadcrumbOverrides;
    }

    /**
     * Returns a copy of the attribute query.
     *
     * @return BreadcrumbOptions copy of the component
     */
    public <T> T copy() {
        T copiedClass = null;
        try {
            copiedClass = (T) this.getClass().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException();
        }

        copyProperties(copiedClass);

        return copiedClass;
    }

    /**
     * Copies the properties over for the copy method.
     *
     * @param breadcrumbOptions The BreadcrumbOptions to copy
     */
    protected <T> void copyProperties(T breadcrumbOptions) {
        BreadcrumbOptions breadcrumbOptionsCopy = (BreadcrumbOptions) breadcrumbOptions;

        if (breadcrumbOverrides != null) {
            List<BreadcrumbItem> breadcrumbOverrides = Lists.newArrayListWithExpectedSize(
                    getBreadcrumbOverrides().size());
            for (BreadcrumbItem breadcrumbOverride : this.breadcrumbOverrides) {
                breadcrumbOverrides.add((BreadcrumbItem) breadcrumbOverride.copy());
            }

            breadcrumbOptionsCopy.setBreadcrumbOverrides(breadcrumbOverrides);
        }

        if (homewardPathBreadcrumbs != null) {
            List<BreadcrumbItem> homewardPathBreadcrumbs = Lists.newArrayListWithExpectedSize(
                    getHomewardPathBreadcrumbs().size());
            for (BreadcrumbItem homewardPathBreadcrumb : this.homewardPathBreadcrumbs) {
                homewardPathBreadcrumbs.add((BreadcrumbItem) homewardPathBreadcrumb.copy());
            }

            breadcrumbOptionsCopy.setHomewardPathBreadcrumbs(homewardPathBreadcrumbs);
        }

        if (prePageBreadcrumbs != null) {
            List<BreadcrumbItem> prePageBreadcrumbs = Lists.newArrayListWithExpectedSize(
                    getPrePageBreadcrumbs().size());
            for (BreadcrumbItem prePageBreadcrumb : this.prePageBreadcrumbs) {
                prePageBreadcrumbs.add((BreadcrumbItem) prePageBreadcrumb.copy());
            }

            breadcrumbOptionsCopy.setPrePageBreadcrumbs(prePageBreadcrumbs);
        }

        if (preViewBreadcrumbs != null) {
            List<BreadcrumbItem> preViewBreadcrumbs = Lists.newArrayListWithExpectedSize(
                    getPreViewBreadcrumbs().size());
            for (BreadcrumbItem preViewBreadcrumb : this.preViewBreadcrumbs) {
                preViewBreadcrumbs.add((BreadcrumbItem) preViewBreadcrumb.copy());
            }

            breadcrumbOptionsCopy.setPreViewBreadcrumbs(preViewBreadcrumbs);
        }
    }
}
