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
 * BlockUI element is used within the view element for managing element/page blocking attributes
 *
 * <p>
 * Some basic options of the plugin are exposed through this class. Messages can be managed via
 * Action elements. See the jquery BlockUI plugin for more details.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "blockUI-bean", parent = "Uif-BlockUI")
public class BlockUI extends WidgetBase {

    private String blockingImageSource;

    public BlockUI() {
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

        if (StringUtils.isNotBlank(blockingImageSource) && !templateOptions.containsKey("blockingImage")) {
            templateOptions.put("blockingImage", blockingImageSource);
        }

        return templateOptions;
    }

    /**
     * Path to an image that will be rendered in the blocking overlay
     *
     * <p>
     * If specified, the image will be picked up and rendered before the blocking message in
     * the overlay. If not given just the message will be displayed
     * </p>
     *
     * @return url to the blocking image
     */
    @BeanTagAttribute(name="blockingImageSource")
    public String getBlockingImageSource() {
        return blockingImageSource;
    }

    /**
     * Setter for the url (source) of the blocking image to use (if any)
     *
     * @param blockingImageSource
     */
    public void setBlockingImageSource(String blockingImageSource) {
        this.blockingImageSource = blockingImageSource;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        BlockUI blockUICopy = (BlockUI) component;
        blockUICopy.setBlockingImageSource(this.blockingImageSource);
    }
}