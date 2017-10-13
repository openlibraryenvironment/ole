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
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.uif.component.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for rendering a lightbox in the UI to display action links in dialog
 * popups
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "lightBox-bean", parent = "Uif-LightBox"),
        @BeanTag(name = "lightBoxPost-bean", parent = "Uif-LightBoxPost")})
public class LightBox extends WidgetBase {
    private static final long serialVersionUID = -4004284762546700975L;

    private String height;
    private String width;

    private boolean addAppParms;
    private boolean lookupReturnByScript;

    public LightBox() {
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

        if (StringUtils.isNotBlank(width) && !templateOptions.containsKey("width")) {
            templateOptions.put("width", width);
        }

        if (StringUtils.isNotBlank(height) && !templateOptions.containsKey("height")) {
            templateOptions.put("height", height);
        }

        return templateOptions;
    }

    /**
     * @return height of light box
     */
    @BeanTagAttribute(name = "height")
    public String getHeight() {
        return height;
    }

    /**
     * Setter for the height of the light box
     * Can be percentage. ie. 75%
     *
     * @param height
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * @return width of light box
     */
    @BeanTagAttribute(name = "width")
    public String getWidth() {
        return width;
    }

    /**
     * Setter for the width of the light box
     * Can be percentage. ie. 75%
     *
     * @param width
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Indicates that the light box link should have application parameters added to it.
     *
     * @return true if the link should have application parameters added, false otherwise
     */
    @BeanTagAttribute(name = "addAppParms")
    public boolean isAddAppParms() {
        return addAppParms;
    }

    /**
     * Setter for the addAppParms.
     *
     * @param addAppParms
     */
    public void setAddAppParms(boolean addAppParms) {
        this.addAppParms = addAppParms;
    }

    /**
     * @return the lookupReturnByScript flag
     */
    @BeanTagAttribute(name = "lookupReturnByScript")
    public boolean isLookupReturnByScript() {
        return lookupReturnByScript;
    }

    /**
     * Setter for the flag to indicate that lookups will return the value
     * by script and not a post
     *
     * @param lookupReturnByScript the lookupReturnByScript flag
     */
    public void setLookupReturnByScript(boolean lookupReturnByScript) {
        this.lookupReturnByScript = lookupReturnByScript;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        LightBox lightBoxCopy = (LightBox) component;
        lightBoxCopy.setHeight(this.getHeight());
        lightBoxCopy.setWidth(this.getWidth());
        lightBoxCopy.setAddAppParms(this.isAddAppParms());
        lightBoxCopy.setLookupReturnByScript(this.isLookupReturnByScript());
    }
}
