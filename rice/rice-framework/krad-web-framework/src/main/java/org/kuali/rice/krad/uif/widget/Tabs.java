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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.ClientSideState;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.TabGroup;
import org.kuali.rice.krad.uif.view.View;

/**
 * Widget used for configuring tab options, use componentOptions for most options.
 * See http://jqueryui.com/demos/tabs/ for usable options
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "tabs-bean", parent = "Uif-Tabs")
public class Tabs extends WidgetBase {
    private static final long serialVersionUID = 2L;

    @ClientSideState(variableName = "activeTab")
    private String defaultActiveTabId;

    private UifConstants.Position position = UifConstants.Position.TOP;

    public Tabs() {
        super();
    }

    /**
     * The following is performed:
     *
     * <ul>
     * <li>If the active tab id is configured, set the active plugin option</li>
     * </ul>
     */
    @Override
    public void performFinalize(View view, Object model, Component component) {
        super.performFinalize(view, model, component);

        if (!(component instanceof TabGroup)) {
            throw new RuntimeException("Parent for tabs widget should be tab group, not " + component.getClass());
        }

        TabGroup tabGroup = (TabGroup) component;

        if (StringUtils.isNotBlank(defaultActiveTabId)) {
            // need to find the index of the item to set the plugin active option
            int index = 0;

            boolean found = false;
            for (Component tabComponent : tabGroup.getItems()) {
                if (StringUtils.equals(defaultActiveTabId, tabComponent.getId())) {
                    found = true;

                    break;
                }

                index += 1;
            }

            // if active tab index is set, add the plugin active option
            if (found) {
                Map<String, String> oTemplateOptions = this.getTemplateOptions();
                
                if (oTemplateOptions == null) {
                    setTemplateOptions(oTemplateOptions = new HashMap<String, String>());
                }
                
                oTemplateOptions.put(UifConstants.TabOptionKeys.ACTIVE, Integer.toString(index));
            }
        }
    }

    /**
     * Id for the group within the tab group that should be active (shown first), by default the first
     * group is active
     *
     * @return id for the group within the tab group that should be initially active
     */
    @BeanTagAttribute(name = "defaultActiveTabId")
    public String getDefaultActiveTabId() {
        return defaultActiveTabId;
    }

    /**
     * Setter for the active group id
     *
     * @param defaultActiveTabId
     */
    public void setDefaultActiveTabId(String defaultActiveTabId) {
        this.defaultActiveTabId = defaultActiveTabId;
    }

    /**
     * The position the tabs will appear related to the group, options are TOP, BOTTOM, RIGHT, or LEFT
     *
     * @return position for tabs
     */
    @BeanTagAttribute(name = "position")
    public UifConstants.Position getPosition() {
        return position;
    }

    /**
     * Setter for the tabs position
     *
     * @param position
     */
    public void setPosition(UifConstants.Position position) {
        this.position = position;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Tabs tabsCopy = (Tabs) component;
        tabsCopy.setDefaultActiveTabId(this.getDefaultActiveTabId());
        tabsCopy.setPosition(this.getPosition());
    }
}
