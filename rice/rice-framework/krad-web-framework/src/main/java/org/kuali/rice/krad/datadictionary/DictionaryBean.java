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
package org.kuali.rice.krad.datadictionary;

/**
 * Common interface for all objects that can be configured in the dictionary
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DictionaryBean {

    /**
     * Namespace code (often an application or module code) that dictionary bean is associated with
     *
     * <p>
     * Note this may be assigned through the bean definition itself, or associated by the module configuration
     * and its dictionary files
     * </p>
     *
     * @return String namespace code
     */
    public String getNamespaceCode();

    /**
     * A code within the namespace that identifies a component or group the bean is associated with
     *
     * @return String representing a component code
     */
    public String getComponentCode();
}
