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
package org.kuali.rice.krad.demo.uif.options;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimpleTestKeyValues extends KeyValuesBase {

    private boolean blankOption;

    /**
     * This is a fake implementation of a key value finder, normally this would make a request to
     * a database to obtain the necessary values.  Used only for testing.
     *
     * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        if (blankOption) {
            keyValues.add(new ConcreteKeyValue("", ""));
        }

        keyValues.add(new ConcreteKeyValue("1", "Option 1"));
        keyValues.add(new ConcreteKeyValue("2", "Option 2"));
        keyValues.add(new ConcreteKeyValue("3", "Option 3"));
        keyValues.add(new ConcreteKeyValue("4", "Option 4"));
        keyValues.add(new ConcreteKeyValue("5", "Option 5"));

        return keyValues;
    }

    /**
     * @return the blankOption
     */
    public boolean isBlankOption() {
        return this.blankOption;
    }

    /**
     * @param blankOption the blankOption to set
     */
    public void setBlankOption(boolean blankOption) {
        this.blankOption = blankOption;
    }

}
