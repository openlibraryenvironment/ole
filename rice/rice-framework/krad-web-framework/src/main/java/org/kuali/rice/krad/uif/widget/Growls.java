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

import java.util.HashMap;
import java.util.Map;

/**
 * Growls sets up settings for growls global to the current view and its pages
 *
 * <p>
 * Some basic options of the plugin are exposed through this class, however additional options
 * can be passed through setComponentOptions as usual. However, the header and theme option is set
 * by the growl processing in PageGroup automatically. See the jquery jGrowl plugin for more details.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "growls-bean", parent = "Uif-Growls")
public class Growls extends WidgetBase {
    private static final long serialVersionUID = -8701090110933484411L;

    private boolean sticky;
    private int timeShown;
    private String position;

    public Growls() {
        super();
    }

    /**
     * Override to add property values to the template options
     *
     * @see org.kuali.rice.krad.uif.component.Component#getTemplateOptions()
     */
    @Override
    public Map<String, String> getTemplateOptions() {
        Map<String, String> templateOptions = super.getTemplateOptions();

        if (templateOptions == null) {
            super.setTemplateOptions(templateOptions = new HashMap<String, String>());
        }

        if (!templateOptions.containsKey("sticky")) {
            templateOptions.put("sticky", Boolean.toString(sticky));
        }
        if (!templateOptions.containsKey("life")) {
            templateOptions.put("life", Integer.toString(timeShown));
        }
        if (StringUtils.isNotBlank(position) && !templateOptions.containsKey("position")) {
            templateOptions.put("position", position);
        }

        return templateOptions;
    }

    /**
     * If true, the growl will stick to the page until the user dismisses it
     *
     * @return the sticky
     */
    @BeanTagAttribute(name="sticky")
    public boolean isSticky() {
        return this.sticky;
    }

    /**
     * @param sticky the sticky to set
     */
    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    /**
     * The time growls are shown in milliseconds
     *
     * @return the timeShown
     */
    @BeanTagAttribute(name="timeShown")
    public int getTimeShown() {
        return this.timeShown;
    }

    /**
     * @param timeShown the timeShown to set
     */
    public void setTimeShown(int timeShown) {
        this.timeShown = timeShown;
    }

    /**
     * The position for the growls to appear in the window
     * There are five options available: top-left, top-right, bottom-left, bottom-right, center.
     *
     * @return the position
     */
    @BeanTagAttribute(name="position")
    public String getPosition() {
        return this.position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Growls growlsCopy = (Growls) component;
        growlsCopy.setSticky(this.isSticky());
        growlsCopy.setTimeShown(this.getTimeShown());
        growlsCopy.setPosition(this.getPosition());
    }
}
