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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.CssConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.layout.LayoutManager;
import org.kuali.rice.krad.uif.view.View;

/**
 * Decorates a group with scroll functionality
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "scrollpane-bean", parent = "Uif-Scrollpane")
public class Scrollpane  extends WidgetBase {
    private static final long serialVersionUID = 3853028195825084261L;

    private String height;

    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        buildCSSforScrollPane(parent);
    }

    private void buildCSSforScrollPane(Component parent) {
        LayoutManager layoutManager = ((Group) parent).getLayoutManager();
        if (StringUtils.isNotBlank(getHeight())) {
            if (!StringUtils.contains(layoutManager.getStyle(), CssConstants.HEIGHT)) {
                layoutManager.appendToStyle(CssConstants.HEIGHT + getHeight() +";");
            }

            if (!StringUtils.contains(layoutManager.getStyle(), CssConstants.OVERFLOW)) {
                layoutManager.appendToStyle(CssConstants.OVERFLOW + "auto;");
            }
        }
    }

    /**
     * Height the content should take up in the group
     *
     * <p>
     * If the content size exceeds the height then a scroll bar will be shown.
     * </p>
     * <p>
     * e.g. '30%' or '55px'
     * </p>
     *
     * @return Content height of the group
     */
    @BeanTagAttribute(name="height")
    public String getHeight() {
        return height;
    }

    /**
     * Setter for the group height
     *
     * @param height
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Scrollpane scrollpaneCopy = (Scrollpane) component;
        scrollpaneCopy.setHeight(this.getHeight());
    }

}
