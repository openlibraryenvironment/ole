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
package org.kuali.rice.ken.dao;

import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

/**
 * Convenience test case implementation that just stores the BusinessObjectDao bean
 * in a protected member field for ease of use
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(Mode.CLEAR_DB)
public abstract class BusinessObjectDaoTestCaseBase extends KENTestCase {
    protected GenericDao businessObjectDao;

    /**
     * @see org.kuali.rice.test.BaselineTestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        businessObjectDao = services.getGenericDao();
    }
}
