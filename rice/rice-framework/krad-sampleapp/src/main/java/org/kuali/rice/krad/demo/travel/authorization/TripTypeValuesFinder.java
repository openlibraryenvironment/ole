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
package org.kuali.rice.krad.demo.travel.authorization;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.demo.travel.authorization.dataobject.TripType;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TripTypeValuesFinder extends KeyValuesBase {

    public List getKeyValues() {
        List keyValues = new ArrayList();

        Collection<TripType> bos = KRADServiceLocator.getBusinessObjectService().findAll(TripType.class);

        keyValues.add(new ConcreteKeyValue("", ""));
        for (TripType typ : bos) {
            keyValues.add(new ConcreteKeyValue(typ.getCode(), typ.getName()));
        }

        return keyValues;
    }

}
