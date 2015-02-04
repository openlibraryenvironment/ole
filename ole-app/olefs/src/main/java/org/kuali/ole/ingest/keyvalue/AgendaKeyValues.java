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
package org.kuali.ole.ingest.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.impl.repository.AgendaBo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * AgendaKeyValues is the value finder class
 */
public class AgendaKeyValues extends KeyValuesBase {

    private boolean blankOption;

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
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments Code and
     * Name.
     * @return  List<KeyValue>
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<AgendaBo> agendaBo = KRADServiceLocator.getBusinessObjectService().findAll(AgendaBo.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (AgendaBo typ : agendaBo) {
            keyValues.add(new ConcreteKeyValue(typ.getName(), typ.getName()));
        }
        return keyValues;
    }

}