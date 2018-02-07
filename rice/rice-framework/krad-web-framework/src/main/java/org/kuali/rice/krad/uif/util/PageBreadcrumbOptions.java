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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.Map;

/**
 * BreadcrumbOptions specific to page.  Render options are only available at the page level.
 */
@BeanTag(name = "pageBreadcrumbOptions-bean", parent = "Uif-PageBreadcrumbOptions")
public class PageBreadcrumbOptions extends BreadcrumbOptions {
    private static final long serialVersionUID = -5666730356781875858L;

    //render options
    private boolean renderViewBreadcrumb;
    private boolean renderHomewardPathBreadcrumbs;
    private boolean renderPreViewBreadcrumbs;
    private boolean renderPrePageBreadcrumbs;
    private boolean renderParentLocations;

    /**
     * Setup the BreadcrumbOptions and BreadcrumbItem for a PageGroup.  To be called from performInitialization.
     *
     * @param view the page's View
     * @param model the model
     */
    @Override
    public void setupBreadcrumbs(View view, Object model) {
        BreadcrumbOptions viewBreadcrumbOptions = view.getBreadcrumbOptions();

        //inherit prePageBreadcrumbs, preViewBreadcrumbs, and overrides from the view if not set
        if (this.getHomewardPathBreadcrumbs() == null
                && viewBreadcrumbOptions != null
                && viewBreadcrumbOptions.getHomewardPathBreadcrumbs() != null) {
            this.setHomewardPathBreadcrumbs(viewBreadcrumbOptions.getHomewardPathBreadcrumbs());

            for (BreadcrumbItem item : this.getHomewardPathBreadcrumbs()) {
                view.assignComponentIds(item);
            }
        }

        if (this.getPrePageBreadcrumbs() == null
                && viewBreadcrumbOptions != null
                && viewBreadcrumbOptions.getPrePageBreadcrumbs() != null) {
            this.setPrePageBreadcrumbs(viewBreadcrumbOptions.getPrePageBreadcrumbs());

            for (BreadcrumbItem item : this.getPrePageBreadcrumbs()) {
                view.assignComponentIds(item);
            }
        }

        if (this.getPreViewBreadcrumbs() == null
                && viewBreadcrumbOptions != null
                && viewBreadcrumbOptions.getPreViewBreadcrumbs() != null) {
            this.setPreViewBreadcrumbs(viewBreadcrumbOptions.getPreViewBreadcrumbs());

            for (BreadcrumbItem item : this.getPreViewBreadcrumbs()) {
                view.assignComponentIds(item);
            }
        }

        if (this.getBreadcrumbOverrides() == null
                && viewBreadcrumbOptions != null
                && viewBreadcrumbOptions.getBreadcrumbOverrides() != null) {
            this.setBreadcrumbOverrides(viewBreadcrumbOptions.getBreadcrumbOverrides());

            for (BreadcrumbItem item : this.getBreadcrumbOverrides()) {
                view.assignComponentIds(item);
            }
        }
    }

    /**
     * Finalize the setup of the BreadcrumbOptions and the BreadcrumbItem for the PageGroup.  To be called from the
     * performFinalize method.
     *
     * @param view the page's View
     * @param model the model
     */
    @Override
    public void finalizeBreadcrumbs(View view, Object model, Container parent, BreadcrumbItem breadcrumbItem) {
        //set breadcrumbItem label same as the header, if not set
        if (StringUtils.isBlank(breadcrumbItem.getLabel()) && parent.getHeader() != null && StringUtils.isNotBlank(
                parent.getHeader().getHeaderText())) {
            breadcrumbItem.setLabel(parent.getHeader().getHeaderText());
        }

        //if label still blank, dont render
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

            //remove pageId because this should be set by the BreadcrumbItem setting
            requestParameters.remove("pageId");

            breadcrumbItem.getUrl().setRequestParameters(requestParameters);
        }

        //form key handling
        if (breadcrumbItem.getUrl().getFormKey() == null
                && model instanceof UifFormBase
                && ((UifFormBase) model).getFormKey() != null) {
            breadcrumbItem.getUrl().setFormKey(((UifFormBase) model).getFormKey());
        }

        //automatically set breadcrumbItem UifUrl properties below, if not set
        if (breadcrumbItem.getUrl().getControllerMapping() == null && model instanceof UifFormBase) {
            breadcrumbItem.getUrl().setControllerMapping(((UifFormBase) model).getControllerMapping());
        }

        if (breadcrumbItem.getUrl().getViewId() == null) {
            breadcrumbItem.getUrl().setViewId(view.getId());
        }

        if (breadcrumbItem.getUrl().getPageId() == null) {
            breadcrumbItem.getUrl().setPageId(parent.getId());
        }
    }

    /**
     * Whether or not to render the view breadcrumb at this level
     *
     * @return true if rendering the view breadcrumb, false otherwise
     */
    @BeanTagAttribute(name = "renderViewBreadcrumb")
    public boolean isRenderViewBreadcrumb() {
        return renderViewBreadcrumb;
    }

    /**
     * Set renderViewBreadcrumb
     *
     * @param renderViewBreadcrumb
     */
    public void setRenderViewBreadcrumb(boolean renderViewBreadcrumb) {
        this.renderViewBreadcrumb = renderViewBreadcrumb;
    }

    /**
     * If true, render the homewardPathBreadcrumbs (if any are set), otherwise do not render them
     *
     * @return true if rendering homewardPathBreadcrumbs, false otherwise
     */
    @BeanTagAttribute(name = "renderHomewardPathBreadcrumbs")
    public boolean isRenderHomewardPathBreadcrumbs() {
        return renderHomewardPathBreadcrumbs;
    }

    /**
     * Set renderHomewardPathBreadcrumbs
     *
     * @param renderHomewardPathBreadcrumbs
     */
    public void setRenderHomewardPathBreadcrumbs(boolean renderHomewardPathBreadcrumbs) {
        this.renderHomewardPathBreadcrumbs = renderHomewardPathBreadcrumbs;
    }

    /**
     * If true, render the preViewBreadcrumbs (if any are set), otherwise do not render them
     *
     * @return true if rendering preViewBreadcrumbs, false otherwise
     */
    @BeanTagAttribute(name = "renderPreViewBreadcrumbs")
    public boolean isRenderPreViewBreadcrumbs() {
        return renderPreViewBreadcrumbs;
    }

    /**
     * Set renderPreViewBreadcrumbs
     *
     * @param renderPreViewBreadcrumbs
     */
    public void setRenderPreViewBreadcrumbs(boolean renderPreViewBreadcrumbs) {
        this.renderPreViewBreadcrumbs = renderPreViewBreadcrumbs;
    }

    /**
     * If true, render the prePageBreadcrumbs (if any are set), otherwise do not render them
     *
     * @return true if rendering prePageBreadcrumbs, false otherwise
     */
    @BeanTagAttribute(name = "renderPrePageBreadcrumbs")
    public boolean isRenderPrePageBreadcrumbs() {
        return renderPrePageBreadcrumbs;
    }

    /**
     * Set renderPrePageBreadcrumbs
     *
     * @param renderPrePageBreadcrumbs
     */
    public void setRenderPrePageBreadcrumbs(boolean renderPrePageBreadcrumbs) {
        this.renderPrePageBreadcrumbs = renderPrePageBreadcrumbs;
    }

    /**
     * If true, render the parent location breadcrumbs.  These BreadcrumbItems are automatically generated based on the
     * view's parentLocation property settings by traversing parent views/pages or based on a history path.
     *
     * @return true if rendering the parent location breadcrumbs, false otherwise
     */
    @BeanTagAttribute(name = "renderParentLocations")
    public boolean isRenderParentLocations() {
        return renderParentLocations;
    }

    /**
     * Set renderParentLocations
     *
     * @param renderParentLocations
     */
    public void setRenderParentLocations(boolean renderParentLocations) {
        this.renderParentLocations = renderParentLocations;
    }

    /**
     * Copies the properties over for the copy method.
     *
     * @param breadcrumbOptions The BreadcrumbOptions to copy
     */
    protected <T> void copyProperties(T breadcrumbOptions) {
        super.copyProperties(breadcrumbOptions);
        PageBreadcrumbOptions breadcrumbOptionsCopy = (PageBreadcrumbOptions) breadcrumbOptions;

        breadcrumbOptionsCopy.setRenderViewBreadcrumb(this.renderViewBreadcrumb);
        breadcrumbOptionsCopy.setRenderHomewardPathBreadcrumbs(this.renderHomewardPathBreadcrumbs);
        breadcrumbOptionsCopy.setRenderPreViewBreadcrumbs(this.renderPreViewBreadcrumbs);
        breadcrumbOptionsCopy.setRenderPrePageBreadcrumbs(this.renderPrePageBreadcrumbs);
        breadcrumbOptionsCopy.setRenderParentLocations(this.renderParentLocations);
    }
}
