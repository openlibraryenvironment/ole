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

import java.util.List;

/**
 * Performs overrides on the fields of a Data Dictionary bean.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface BeanOverride {
    /**
     * Return the name of the bean to perform the override.
     *
     * @return
     */
    public String getBeanName();

    /**
     * Returns the list of fields to perform the override.
     */
    public List<FieldOverride> getFieldOverrides();

    /**
     * Perform the override logic on the specific bean.
     */
    public void performOverride(Object bean);
}
