package edu.samplu.krad.demo.uif.library;

import com.thoughtworks.selenium.SeleneseTestBase;
import edu.samplu.common.Failable;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public abstract class DemoLibraryNavigationBase extends DemoLibraryBase {

    protected void assertNavigationView(String linkText, String supportTitleText) throws Exception {
        waitAndClickByLinkText(linkText);
        waitForElementPresentByClassName("uif-viewHeader-supportTitle");
        SeleneseTestBase.assertTrue(getTextByClassName("uif-viewHeader-supportTitle").contains(supportTitleText));
    }


    protected void testNavigationView() throws Exception {
        assertNavigationView("Page 2", "Test Course 2");
        assertNavigationView("Page 3", "Test Course 3");
        assertNavigationView("Page 1", "Test Course 1");
    }

    public void testNavigationViewBookmark(Failable failable) throws Exception {
        testNavigationView();
        passed();
    }
}
