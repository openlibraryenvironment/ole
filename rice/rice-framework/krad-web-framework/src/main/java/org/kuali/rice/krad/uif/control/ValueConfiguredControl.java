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
package org.kuali.rice.krad.uif.control;

/**
 * Indicates <code>Control</code> types that can be configured with a static value to submit, as opposed to pulling
 * the value from the underlying property
 *
 * <p>
 * Examples of this are {@link CheckboxControl}, which can be configured with a value that will be submitted when the
 * checkbox is checked. For example, suppose we had a model property of type Set<String> that represents selected car
 * types. In the UI, we can present a list of available car types with a checkbox next to each. The value for the
 * each checkbox will be the model type of the associated role: 'Ford', 'GM', 'Honda'. For each checkbox selected the
 * associated value will be submitted and populated into the Set<String> on the model.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ValueConfiguredControl {

    /**
     * Retrieves the value that will be submitted with the control
     *
     * @return String control value
     */
    public String getValue();

    /**
     * Setter for the value that should be submitted with the control
     *
     * @param value
     */
    public void setValue(String value);
}
