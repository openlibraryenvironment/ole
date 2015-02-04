/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DefaultValueForValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        String systemUserRoleId = KimApiServiceLocator.getRoleService().getRoleIdByNamespaceCodeAndName(
                OLEConstants.CoreModuleNamespaces.OLE, OleSelectConstant.SYSTEM_USER_ROLE_NAME);

        if (KimApiServiceLocator.getRoleService().principalHasRole(
                GlobalVariables.getUserSession().getPrincipalId(),
                Collections.singletonList(systemUserRoleId), new HashMap<String, String>())) {
            keyValues.add(new ConcreteKeyValue(OleSelectConstant.DEFAULT_VALUE_ROLE, OleSelectConstant.DEFAULT_VALUE_ROLE));
            keyValues.add(new ConcreteKeyValue(OleSelectConstant.DEFAULT_VALUE_SYSTEM, OleSelectConstant.DEFAULT_VALUE_SYSTEM));
        } else {
            keyValues.add(new ConcreteKeyValue(OleSelectConstant.DEFAULT_VALUE_USER, OleSelectConstant.DEFAULT_VALUE_USER));
        }
        return keyValues;
    }

}
