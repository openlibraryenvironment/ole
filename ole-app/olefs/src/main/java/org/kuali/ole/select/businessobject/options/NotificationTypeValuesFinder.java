/*
 * Copyright 2007 The Kuali Foundation
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

import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.List;


/**
 * This class creates a new finder for our forms view (creates a drop-down of {@link SufficientFundsCode}s)
 */
public class NotificationTypeValuesFinder extends KeyValuesBase {

    @Override
    public List getKeyValues() {
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        labels.add(new ConcreteKeyValue(OLEConstants.BLOCK_USR_KEY, OLEConstants.BLOCK_USR_VAL));
        labels.add(new ConcreteKeyValue(OLEConstants.NOT_USR_KEY, OLEConstants.NOT_USR_VAL));
        labels.add(new ConcreteKeyValue(OLEConstants.ROU_USR_KEY, OLEConstants.ROU_USR_VAL));
        labels.add(new ConcreteKeyValue(OLEConstants.WAR_USR_KEY, OLEConstants.WAR_USR_VAL));
        return labels;
    }
}

