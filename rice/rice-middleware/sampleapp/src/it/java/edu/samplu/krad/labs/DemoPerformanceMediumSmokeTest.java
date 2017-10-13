package edu.samplu.krad.labs;

import edu.samplu.common.SmokeTestBase;
import org.junit.Test;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class DemoPerformanceMediumSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/labs?viewId=Lab-PerformanceMedium
     */
    public static final String BOOKMARK_URL = "/kr-krad/labs?viewId=Lab-PerformanceMedium";

    /**
     * /kr-krad/labs?viewId=Lab-PerformanceMedium&pageId=Lab-Performance-Page1#Lab-Performance-Page2
     */
    public static final String BOOKMARK_URL_2 = "/kr-krad/labs?viewId=Lab-PerformanceMedium&pageId=Lab-Performance-Page1#Lab-Performance-Page2&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickByLinkText("Performance Medium");
    }

    @Test
    public void testPerformanceMediumBookmark() throws Exception {
        testPerformanceMedium();
        navigateToSecondPage(); // how to bookmark the second page?
        //        driver.navigate().to(ITUtil.getBaseUrlString() + BOOKMARK_URL_2);
        passed();
    }

    @Test
    public void testPerformanceMediumNav() throws Exception {
        testPerformanceMedium();
        navigateToSecondPage();
        passed();
    }

    private void navigateToSecondPage() throws InterruptedException {
        waitAndClickByLinkText("Page 2");
        waitForBottomButton();
    }

    private void waitForBottomButton() throws InterruptedException {
        waitForElementsPresentByXpath("//button[contains(text(), 'Refresh - Non-Ajax')]");
    }

    protected void testPerformanceMedium()throws Exception {
        waitForBottomButton();
    }
}
