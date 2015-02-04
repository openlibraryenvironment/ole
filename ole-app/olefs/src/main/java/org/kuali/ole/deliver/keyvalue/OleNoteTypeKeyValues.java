/**
 * Copyright 2005-2012 The Kuali Foundation
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
package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.OlePatronNoteType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OleNoteTypeKeyValues returns PatronNoteTypeId and PatronNoteTypeName for OlePatronNoteType.
 */

public class OleNoteTypeKeyValues extends KeyValuesBase {

    private boolean blankOption;

    /**
     * Gets the boolean value of blankOption property
     *
     * @return blankOption
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

    /**
     * This method will populate the patronNoteTypeId as a key and patronNoteTypeName as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OlePatronNoteType> agendaBo = KRADServiceLocator.getBusinessObjectService().findAll(OlePatronNoteType.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OlePatronNoteType typ : agendaBo) {
            keyValues.add(new ConcreteKeyValue(typ.getPatronNoteTypeId(), typ.getPatronNoteTypeName()));
        }
        return keyValues;
    }

}