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
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "keyValuesFinder-bean")
public abstract class UifKeyValuesFinderBase extends KeyValuesBase implements UifKeyValuesFinder {

    private boolean addBlankOption;

    public UifKeyValuesFinderBase() {
        addBlankOption = true;
    }

    /**
     * @see UifKeyValuesFinder#getKeyValues()
     */
    public List<KeyValue> getKeyValues() {
        return null;
    }

    /**
     * @see UifKeyValuesFinder#getKeyValues(org.kuali.rice.krad.uif.view.ViewModel)
     */
    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        return getKeyValues();
    }

    /**
     * @see UifKeyValuesFinder#getKeyValues(org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.field.InputField)
     */
    @Override
    public List<KeyValue> getKeyValues(ViewModel model, InputField field){
        return getKeyValues(model);
    }

    /**
     * @see org.kuali.rice.krad.uif.control.UifKeyValuesFinder#isAddBlankOption()
     */
    @BeanTagAttribute(name="addBlankOption")
    public boolean isAddBlankOption() {
        return addBlankOption;
    }

    /**
     * Setter for the addBlankOption indicator
     *
     * @param addBlankOption
     */
    public void setAddBlankOption(boolean addBlankOption) {
        this.addBlankOption = addBlankOption;
    }
}
