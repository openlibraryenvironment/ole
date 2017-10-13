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
package org.kuali.rice.kew.impl.type;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Builds options list for all active {@link KewTypeBo} records
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KewTypeOptionsFinder extends UifKeyValuesFinderBase {

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> kewTypes = new ArrayList<KeyValue>();

        Collection<KewTypeBo> kewTypeBos = KRADServiceLocator.getBusinessObjectService().findAllOrderBy(KewTypeBo.class,
                "namespace", true);
        for (KewTypeBo typeBo : kewTypeBos) {
            if (typeBo.isActive()) {
                kewTypes.add(new ConcreteKeyValue(typeBo.getId(), typeBo.getNamespace() + " - " + typeBo.getName()));
            }
        }

        return kewTypes;
    }
}
