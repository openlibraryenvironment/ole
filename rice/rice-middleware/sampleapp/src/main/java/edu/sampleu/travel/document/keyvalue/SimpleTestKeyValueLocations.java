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
package edu.sampleu.travel.document.keyvalue;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.util.UrlInfo;
import org.kuali.rice.krad.uif.util.UifKeyValueLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for KeyValueLocations KeyValuesFinder
 */
public class SimpleTestKeyValueLocations extends SimpleTestKeyValues {

    /**
     * This is a fake implementation of a key value finder, normally this would make a request to
     * a database to obtain the necessary values.  Used only for testing.
     *
     * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        if (this.isBlankOption()) {
            keyValues.add(new ConcreteKeyValue("", ""));
        }

        String baseUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString("krad.url");

        keyValues.add(new UifKeyValueLocation("Google", "Google", new UrlInfo("http://www.google.com")));
        keyValues.add(new UifKeyValueLocation("Kuali", "Kuali", new UrlInfo("http://www.kuali.org")));
        keyValues.add(new UifKeyValueLocation("Jira", "Jira", new UrlInfo("http://jira.kuali.org")));
        keyValues.add(new UifKeyValueLocation("Config", "Config Test View",
                new UrlInfo(baseUrl,"/uicomponents","ConfigurationTestView","start")));

        return keyValues;
    }
}
