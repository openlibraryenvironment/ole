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
package org.kuali.rice.krad.dao.impl

import org.junit.Before
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.property.ConfigContext
import javax.xml.namespace.QName
import org.kuali.rice.core.api.resourceloader.ResourceLoader
import static org.junit.Assert.assertEquals
import org.junit.Test
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform
import org.apache.ojb.broker.query.Criteria
import org.kuali.rice.location.impl.country.CountryBo

/**
 * Tests LookupDaoOjb
 */
class LookupDaoOjbTest {

    String upperFunction = "UPPER";
    Criteria criteria = new Criteria();
    CountryBo countryBo = new CountryBo();
    LookupDaoOjb lookupDaoOjb = new LookupDaoOjb();

    @Before
    void setupFakeEnv() {
        def config = new JAXBConfigImpl();
        config.putProperty(CoreConstants.Config.APPLICATION_ID, "APPID");
        ConfigContext.init(config);

        GlobalResourceLoader.stop()
        GlobalResourceLoader.addResourceLoader([
                getName: { -> new QName("Foo", "Bar") },
                getService: { [ getUpperCaseFunction: { -> upperFunction } ] as DatabasePlatform },
                stop: {}
        ] as ResourceLoader)
    }

    @Test
    void testCaseSensitive() {
        //insensitive
        lookupDaoOjb.createCriteria(countryBo,"searchValue","code",true,false,criteria);
        String t = criteria.toString();
        assertEquals("[UPPER(code) LIKE SEARCHVALUE]",criteria.toString());

        //sensitive
        criteria = new Criteria();
        lookupDaoOjb.createCriteria(countryBo,"searchValue","code",false,false,criteria);
        assertEquals("[code LIKE searchValue]",criteria.toString());
    }

}
