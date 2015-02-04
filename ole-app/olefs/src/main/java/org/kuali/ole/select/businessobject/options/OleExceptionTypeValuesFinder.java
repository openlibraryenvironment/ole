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

import org.kuali.ole.select.businessobject.OleExceptionType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class returns list of key value pairs for Ole Exception Type.
 */
public class OleExceptionTypeValuesFinder extends KeyValuesBase {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleExceptionTypeValuesFinder.class);

    /**
     * This method returns list of key value pairs for Ole Exception Type.
     *
     * @see org.kuali.rice.kns.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @Override
    public List getKeyValues() {
        LOG.debug("Inside getKeyValues of OleExceptionTypeValuesFinder");
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection notes = boService.findAll(OleExceptionType.class);
        List labels = new ArrayList();
        for (Iterator iter = notes.iterator(); iter.hasNext(); ) {
            OleExceptionType exceptionType = (OleExceptionType) iter.next();
            labels.add(new ConcreteKeyValue(exceptionType.getExceptionTypeId().toString(), exceptionType.getExceptionType()));
        }
        return labels;
    }

}
