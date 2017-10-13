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
package edu.samplu.admin.config.namespace.pending.fyi;

import edu.samplu.admin.config.namespace.pending.PendingBase;

import edu.samplu.common.ITUtil;
import org.junit.Test;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PersonPendingFYISmokeTest extends PendingBase {

    /**
     * ITUtil.PORTAL+"?channelTitle=Namespace&channelUrl="+ITUtil.getBaseUrlString()+ITUtil..KNS_LOOKUP_METHOD
     * +"org.kuali.rice.coreservice.impl.namespace.NamespaceBo&docFormKey=88888888&returnLocation="
     * +ITUtil.PORTAL_URL+ITUtil.HIDE_RETURN_LINK;
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL+"?channelTitle=Namespace&channelUrl="
            + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD
            + "org.kuali.rice.coreservice.impl.namespace.NamespaceBo" + "&docFormKey=88888888&returnLocation="
            + ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    protected void testPersonPendingFYI() throws Exception {
        fillInNamespaceOverview("Test Namespace 2", "SUACTION2", "SUACTION2", "KUALI");

        String[][] personsActions = new String[][] {{"frank", "FYI"}, {"eric", "ACKNOWLEDGE"}, {"fran", "APPROVE"}};
        fillInAddAdHocPersons(personsActions);

        String docId = submitAndLookupDoc();

        assertSuperPerson("frank", "FYI", docId);
    }

    @Test
    public void testPersonPendingFYIBookmark() throws Exception {
        testPersonPendingFYI();
        passed();
    }

    @Test
    public void testPersonPendingFYINav() throws Exception {
        testPersonPendingFYI();
        passed();
    }
}
