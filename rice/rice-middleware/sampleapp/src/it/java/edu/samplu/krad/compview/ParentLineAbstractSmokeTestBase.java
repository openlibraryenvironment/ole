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
package edu.samplu.krad.compview;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class ParentLineAbstractSmokeTestBase extends WebDriverLegacyITBase {

    /**
     * /kr-krad/uicomponents?viewId=RichMessagesView&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?methodToCall=start&readOnlyFields=field91&viewId=UifCompView#UifCompView-Page7";
            
    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.  Bookmark Tests should override and return {@link ParentLineAbstractSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */    
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    protected void navigation() throws Exception {
        waitAndClickKRAD();
        waitAndClickByXpath(KITCHEN_SINK_XPATH);
        switchToWindow(KUALI_UIF_COMPONENTS_WINDOW_XPATH);
    }

    protected void testParentLineNav(Failable failable) throws Exception {
        navigation();
        testSubCollectionSize();
        passed();
    }

    protected void testParentLineBookmark(Failable failable) throws Exception {
        testSubCollectionSize();
        passed();
    }
  
}
