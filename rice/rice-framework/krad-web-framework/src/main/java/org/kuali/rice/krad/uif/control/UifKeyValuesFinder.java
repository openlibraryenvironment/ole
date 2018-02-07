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

import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.List;

/**
 * Values finder that can taken the {@link org.kuali.rice.krad.uif.view.ViewModel} that provides data to the view
 * for conditionally setting the valid options
 *
 * <p>
 * Values finder also allows configuration for a blank option that will be added by the framework
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface UifKeyValuesFinder extends KeyValuesFinder {

    /**
     * Builds a list of key values representations for valid value selections using the given view model
     * to retrieve values from other fields and conditionally building the options
     *
     * @param model object instance containing the view data
     *
     * @return List of KeyValue objects
     */
    public List<KeyValue> getKeyValues(ViewModel model);

    /**
     * Builds a list of key values representations for valid value selections using the given view model
     * to retrieve values from other fields and conditionally building the options
     *
     * @param model  object instance containing the view data
     * @param field  object instance containing the field data of the key value lookup
     *
     * @return List of KeyValue objects
     */
    public List<KeyValue> getKeyValues(ViewModel model, InputField field);

    /**
     * Indicates whether a blank option should be included as a valid option
     *
     * @return boolean true if the blank option should be given, false if not
     */
    public boolean isAddBlankOption();
}
