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
import edu.samplu.common.NavTemplateMethodSTBase;
import org.openqa.selenium.By;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class MainTmplMthdSTNavBase extends NavTemplateMethodSTBase {

    public static final String DATA_TABLES_INFO = "dataTables_info";

    @Override
    protected String getCreateNewLinkLocator() {
        return "Create New";
    }

    @Override
    protected String getMenuLinkLocator() {
        return "Main Menu";
    }

    protected void testSearchEditBack(Failable failable) throws Exception {
        waitAndClickSearch3();
        waitForElementPresentByClassName(DATA_TABLES_INFO);
        String pageBannerText = getText(By.className(DATA_TABLES_INFO));
        waitAndClickEdit();
        waitForElementPresentByClassName("uif-headerText-span");
        back();
        waitForElementPresentByClassName(DATA_TABLES_INFO);
        if (!pageBannerText.equals(getText(By.className(DATA_TABLES_INFO)))) {
            failable.fail("Going back from Search to Edit results not available");
        }
    }

    protected void testSearchSearchBack(Failable failable, String fieldName, String searchText) throws Exception {
        waitAndClickSearch3();
        waitForElementPresentByClassName(DATA_TABLES_INFO);
        String pageBannerText = getText(By.className(DATA_TABLES_INFO));
        waitAndTypeByName(fieldName, searchText);
        waitAndClickSearch3();
        waitForElementPresentByClassName(DATA_TABLES_INFO, "Problem with second search");
        back();
        waitForElementsPresentByClassName(DATA_TABLES_INFO, "Going back from Search to Search results not available");
        assertTextPresent("Going back from Search to Search results not available", pageBannerText);
    }
}
