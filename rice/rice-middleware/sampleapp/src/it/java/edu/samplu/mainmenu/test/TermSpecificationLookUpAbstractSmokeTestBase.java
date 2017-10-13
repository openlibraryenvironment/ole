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
package edu.samplu.mainmenu.test;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TermSpecificationLookUpAbstractSmokeTestBase extends MainTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Term%20Specification%20Lookup&channelUrl="
     *   + ITUtil.getBaseUrlString() + ITUtil.KRAD_LOOKUP_METHOD
     *   + "org.kuali.rice.krms.impl.repository.TermSpecificationBo"
     *   + ITUtil.SHOW_MAINTENANCE_LINKS
     *   + "&returnLocation=" + ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Term%20Specification%20Lookup&channelUrl="
            + ITUtil.getBaseUrlString() + ITUtil.KRAD_LOOKUP_METHOD
            + "org.kuali.rice.krms.impl.repository.TermSpecificationBo"
            + ITUtil.SHOW_MAINTENANCE_LINKS
            + "&returnLocation=" + ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
    /**
     * {@inheritDoc}
     * Term Specification Lookup
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Term Specification Lookup";
    }

    public void testTermSpecificationLookUpBookmark(Failable failable) throws Exception {
        testTermSpecificationLookupAssertions();
        passed();
    }
    public void testTermSpecificationLookUpNav(Failable failable) throws Exception {
        testTermSpecificationLookupAssertions();
        passed();
    }
}
