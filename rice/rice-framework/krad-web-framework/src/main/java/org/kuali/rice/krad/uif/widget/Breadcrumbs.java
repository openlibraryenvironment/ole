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
package org.kuali.rice.krad.uif.widget;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.util.ExpressionUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.component.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The breadcrumb widget contains various settings for setting up
 * Breadcrumb/History support on the view
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "breadcrumbs-bean", parent = "Uif-Breadcrumbs")
public class Breadcrumbs extends WidgetBase {
    private static final long serialVersionUID = -2864287914665842251L;

    private boolean displayBreadcrumbsWhenOne;
    private boolean usePathBasedBreadcrumbs;

    public Breadcrumbs() {
        super();
    }

    /**
     * The following updates are done here:
     *
     * <ul>
     * <li>Evaluate expression on homeward path list</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.Component#performApplyModel(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);
    }

    /**
     * If false, breadcrumbs will not be displayed if only one breadcrumb is
     * going to be shown, this improves visual clarity of the page
     *
     * @return the displayBreadcrumbsWhenOne
     */
    @BeanTagAttribute(name = "displayBreadcrumbsWhenOne")
    public boolean isDisplayBreadcrumbsWhenOne() {
        return this.displayBreadcrumbsWhenOne;
    }

    /**
     * Set displayBreadcrumbsWhenOne
     *
     * @param displayBreadcrumbsWhenOne the displayBreadcrumbsWhenOne to set
     */
    public void setDisplayBreadcrumbsWhenOne(boolean displayBreadcrumbsWhenOne) {
        this.displayBreadcrumbsWhenOne = displayBreadcrumbsWhenOne;
    }

    /**
     * If set to true, the breadcrumbs on the View will always be path-based (history backed)
     *
     * @return true if using path based breadcrumbs, false otherwise
     */
    @BeanTagAttribute(name = "usePathBasedBreadcrumbs")
    public boolean isUsePathBasedBreadcrumbs() {
        return usePathBasedBreadcrumbs;
    }

    /**
     * Set usePathBasedBreadcrumbs to true to use path-based breadcrumbs
     *
     * @param usePathBasedBreadcrumbs
     */
    public void setUsePathBasedBreadcrumbs(boolean usePathBasedBreadcrumbs) {
        this.usePathBasedBreadcrumbs = usePathBasedBreadcrumbs;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Breadcrumbs breadcrumbsCopy = (Breadcrumbs) component;
        breadcrumbsCopy.setDisplayBreadcrumbsWhenOne(this.displayBreadcrumbsWhenOne);
        breadcrumbsCopy.setUsePathBasedBreadcrumbs(this.usePathBasedBreadcrumbs);
    }
}
