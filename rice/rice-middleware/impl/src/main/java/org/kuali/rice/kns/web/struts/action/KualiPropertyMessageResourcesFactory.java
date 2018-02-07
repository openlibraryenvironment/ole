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
package org.kuali.rice.kns.web.struts.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResourcesFactory;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * A custom MessageResourceFactory that delegates to the ConfigurationService's pre-loaded properties.
 *
 * <p>
 * This factory can be used in struts-config.xml files by specifying a factory attribute in the <message-resources/>
 * tag
 *
 * Example:
 * <message-resources
 * factory="KualiPropertyMessageResourcesFactory"
 * parameter="SampleApplicationResources" />
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KualiPropertyMessageResourcesFactory extends PropertyMessageResourcesFactory {
    private static final long serialVersionUID = 9045578011738154255L;

    /**
     * Uses KualiPropertyMessageResources, which allows multiple property files to be loaded into the default message
     * set
     *
     * @see org.apache.struts.util.MessageResourcesFactory#createResources(java.lang.String)
     */
    @Override
    public MessageResources createResources(String config) {
        if (StringUtils.isBlank(config)) {
            final String propertyConfig = (String) ConfigContext.getCurrentContextConfig().getProperties().get(
                    KRADConstants.MESSAGE_RESOURCES);
            config = removeSpacesAround(propertyConfig);
        }

        return new KualiPropertyMessageResources(this, config, this.returnNull);
    }

    /**
     * Removes the spaces around the elements on a csv list of elements
     *
     * <p>
     * A null input will return a null output.
     * </p>
     *
     * @param csv a list of elements in csv format e.g. foo, bar, baz
     * @return a list of elements in csv format without spaces e.g. foo,bar,baz
     */
    private String removeSpacesAround(String csv) {
        if (csv == null) {
            return null;
        }

        final StringBuilder result = new StringBuilder();
        for (final String value : csv.split(",")) {
            if (!"".equals(value.trim())) {
                result.append(value.trim());
                result.append(",");
            }
        }

        //remove trailing comma
        int i = result.lastIndexOf(",");
        if (i != -1) {
            result.deleteCharAt(i);
        }

        return result.toString();
    }

}
