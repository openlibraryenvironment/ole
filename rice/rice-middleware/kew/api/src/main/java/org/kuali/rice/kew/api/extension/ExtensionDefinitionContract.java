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
package org.kuali.rice.kew.api.extension;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.Map;

/**
 * Defines an extension to some component of Kuali Enterprise Workflow.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ExtensionDefinitionContract extends Identifiable, Versioned {

    String getName();

    String getApplicationId();

    String getLabel();

    String getDescription();

    /**
     * The "type" of extension definition.  For example, attributes types are defined in:
     * {@link org.kuali.rice.kew.api.KewApiConstants#RULE_ATTRIBUTE_TYPES}
     * @see org.kuali.rice.kew.api.KewApiConstants#RULE_ATTRIBUTE_TYPES
     * @return the extension definition type
     */
    String getType();

    /**
     * Retrieves the resource descriptor for this extension.  This gives the calling code the
     * information it needs to locate and execute the extension resource if it needs to. In practice
     * this is a fully qualified class name.
     *
     * @return the resource descriptor for this extension, this value should never be blank or null
     */
    String getResourceDescriptor();

    /**
     * Returns a list of key/value settings that the extension was statically configured with when defined.
     * @return a list of key/value settings that the extension was statically configured with when defined.
     */
    Map<String, String> getConfiguration();

}
