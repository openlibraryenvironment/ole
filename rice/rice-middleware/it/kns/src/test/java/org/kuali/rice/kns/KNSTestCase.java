/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kns;

import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.krad.test.KRADTestCase;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * Test base class for legacy KNS Tests
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KNSTestCase extends KRADTestCase {

    @Override
    protected Lifecycle getLoadApplicationLifecycle() {
        List<String> resourceLocations = new ArrayList<String>();
        resourceLocations.add("classpath:KRADTestHarnessSpringBeans.xml");
        resourceLocations.add("classpath:KNSTestSpringBeans.xml");

        SpringResourceLoader springResourceLoader = new SpringResourceLoader(new QName("KRADTestResourceLoader"),
                resourceLocations, null);
        springResourceLoader.setParentSpringResourceLoader(getTestHarnessSpringResourceLoader());

        return springResourceLoader;
    }
}
