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
public class AgendaLookUpAbstractSmokeTestBase extends MainTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Agenda%20Lookup&channelUrl="
     * + ITUtil.getBaseUrlString() + ITUtil.KRAD_LOOKUP_METHOD
     * + "org.kuali.rice.krms.impl.repository.AgendaBo"
     * + ITUtil.SHOW_MAINTENANCE_LINKS
     * + "&returnLocation=" + ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Agenda%20Lookup&channelUrl="
            + ITUtil.getBaseUrlString() + ITUtil.KRAD_LOOKUP_METHOD
            + "org.kuali.rice.krms.impl.repository.AgendaBo"
            + ITUtil.SHOW_MAINTENANCE_LINKS
            + "&returnLocation=" + ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
    /**
     * {@inheritDoc}
     * AGENDA_LOOKUP_LINK_TEXT
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return AGENDA_LOOKUP_LINK_TEXT;
    }

    public void testAgendaLookUpBookmark(Failable failable) throws Exception {
        agendaLookupAssertions();
        passed();
    }

    public void testAgendaLookUpNav(Failable failable) throws Exception {
        agendaLookupAssertions();
        passed();
    }

    public void testSearchEditBackNav(Failable failable) throws Exception {
        testSearchEditBack(this);
        passed();
    }

    public void testSearchSearchBackNav(Failable failable) throws Exception {
        testSearchSearchBack(this, "lookupCriteria[id]", "T1000");
        passed();
    }
}
