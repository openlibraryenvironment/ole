/*
 * Copyright 2013 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.select.service.OleUrlResolver;
import org.kuali.ole.service.impl.OleLocationWebServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OleLocationKeyValueFinder extends KeyValuesBase {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
            .getLogger(OleLocationKeyValueFinder.class);
    private transient OleUrlResolver oleUrlResolver;

    /**
     * This method returns list of key value pairs for Item Locations.
     *
     * @see org.kuali.rice.kns.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @Override
    public List getKeyValues() {
        LOG.debug("Inside getKeyValues of OleLocationKeyValueFinder");
        OleLocationWebServiceImpl oleLocationWebService = new OleLocationWebServiceImpl();
        List labels = new ArrayList();
        List locations = oleLocationWebService.getItemLocation();
        for (Iterator iter = locations.iterator(); iter.hasNext(); ) {
            String location = (String) iter.next();
            labels.add(new ConcreteKeyValue(location, location));
        }
        return labels;
    }

    /**
     * This method returns the location web service url
     *
     * @return url
     */
    public String getLocationURL() {
        return ConfigContext.getCurrentContextConfig().getProperty("location.web.service.url");
    }

}
