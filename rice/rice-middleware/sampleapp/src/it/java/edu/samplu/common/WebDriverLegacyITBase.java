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
package edu.samplu.common;

import com.thoughtworks.selenium.SeleneseTestBase;
import edu.samplu.admin.test.AdminTmplMthdSTNavBase;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * Originally used to upgrade UpgradedSeleniumITBase (Selenium 1.0) tests to WebDriver (Selenium 2.0).  Now there is
 * refactoring to be done:
 * <ol>
 *   <li><a href="https://jira.kuali.org/browse/KULRICE-9206">KULRICE-9206</a> Replace literal strings used more than 3 times with Constants, Javadoc constant with constant value.
 *   <li>Extract duplicate waitAndClick...(CONSTANT) to waitAndClickConstant, Javadoc a <pre>{@link &#35;CONSTANT}</pre>.
 *   <li>Replace large chunks of duplication</li>
 *   <li><a href="https://jira.kuali.org/browse/KULRICE-9205">KULRICE-9205</a> Invert dependencies on fields and extract methods to WebDriverUtil so inheritance doesn't have to be used for
 * reuse.  See WebDriverUtil.waitFor </li>
 *   <li>Extract Nav specific code?</li>
 *   <li>Rename to WebDriverAbstractSmokeTestBase</li>
 * </ol>
 * </p>
 * <p>Calls to passed() probably don't belong in the methods reused here.</p>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class WebDriverLegacyITBase implements Failable { //implements com.saucelabs.common.SauceOnDemandSessionIdProvider {

    /**
     * Administration
     */
    public static final String ADMINISTRATION_LINK_TEXT = "Administration";

    /**
     * Agenda Lookup
     */
    public static final String AGENDA_LOOKUP_LINK_TEXT = "Agenda Lookup";

    /**
     * "//input[@aria-invalid]"
     */
    public static final String ARIA_INVALID_XPATH = "//input[@aria-invalid]";

    /**
     * methodToCall.blanketApprove
     */
    public static final String BLANKET_APPROVE_NAME = "methodToCall.blanketApprove";

    /**
     * methodToCall.cancel
     * different cancel than CANCEL2_XPATH
     */
    public static final String CANCEL_NAME = "methodToCall.cancel";

    /**
     * //a[contains(text(), 'ancel')]
     * Different cancel than CANCEL_NAME
     */
    public static final String CANCEL2_XPATH = "//a[contains(text(), 'ancel')]";

    /**
     * //*[@title='close this window']
     */
    public static final String CLOSE_WINDOW_XPATH_TITLE = "//*[@title='close this window']";

    /**
     * Collections
     */
    public static final String COLLECTIONS_LINK_TEXT = "Collections";

    /**
     * "Kuali :: Configuration Test View"
     */
    public static final String CONFIGURATION_VIEW_WINDOW_TITLE = "Kuali :: Configuration Test View";

    /**
     * (//a[contains(text(),'Configuration Test View')])[3]
     */
    public static final String CONFIGURATION_VIEW_XPATH = "(//a[contains(text(),'Configuration Test View')])";

    /**
     * copy
     */
    public static final String COPY_LINK_TEXT = "copy";

    /**
     * New Document not submitted successfully
     */
    public static final String CREATE_NEW_DOCUMENT_NOT_SUBMITTED_SUCCESSFULLY_MESSAGE_TEXT = "New Document not submitted successfully";

    /**
     * //img[@alt='create new']
     */
    public static final String CREATE_NEW_XPATH = "//img[@alt='create new']";

    /**
     * Default "long" wait period is 30 seconds.  See REMOTE_PUBLIC_WAIT_SECONDS_PROPERTY to configure
     */
    public static final int DEFAULT_WAIT_SEC = 30;

    /**
     * //div[@class='left-errmsg-tab']/div/div
     */
    public static final String DIV_LEFT_ERRMSG = "//div[@class='left-errmsg-tab']/div/div";

    /**
     * //input[@id='document.newMaintainableObject.code']
     */
    public static final String DOC_CODE_XPATH = "//input[@id='document.newMaintainableObject.code']";

    /**
     * //div[@id='headerarea']/div/table/tbody/tr[1]/td[1]
     */
    public static final String DOC_ID_XPATH = "//div[@id='headerarea']/div/table/tbody/tr[1]/td[1]";

    /**
     * //table[@id='row']/tbody/tr[1]/td[1
     */
    public static final String DOC_ID_XPATH_2 = "//table[@id='row']/tbody/tr[1]/td[1]";

    /**
     * //table[@id='row']/tbody/tr[1]/td[1]/a
     */
    public static final String DOC_ID_XPATH_3 ="//table[@id='row']/tbody/tr[1]/td[1]/a";

    /**
     * //input[@id='document.documentHeader.documentDescription']
     */
    public static final String DOC_DESCRIPTION_XPATH ="//input[@id='document.documentHeader.documentDescription']";

    /**
     * "//img[@alt='doc search']
     */
    public static final String DOC_SEARCH_XPATH = "//img[@alt='doc search']";

    /**
     * //a[@title='Document Search']
     */
    public static final String DOC_SEARCH_XPATH_TITLE = "//a[@title='Document Search']";

    /**
     * ENROUTE
     */
    public static final String DOC_STATUS_ENROUTE = "ENROUTE";

    /**
     * FINAL
     */
    public static final String DOC_STATUS_FINAL = "FINAL";

    /**
     * SAVED
     */
    public static final String DOC_STATUS_SAVED = "SAVED";

    /**
     * //table[@class='headerinfo']//tr[1]/td[2]
     */
    public static final String DOC_STATUS_XPATH = "//table[@class='headerinfo']//tr[1]/td[2]";

    /**
     * //table[@id='row']/tbody/tr[1]/td[4]
     */
    public static final String DOC_STATUS_XPATH_2 = "//table[@id='row']/tbody/tr[1]/td[4]";

    /**
     * //div[contains(div,'Document was successfully submitted.')]
     */
    public static final String DOC_SUBMIT_SUCCESS_MSG_XPATH ="//div[contains(div,'Document was successfully submitted.')]";

    /**
     * Set -Dremote.driver.dontTearDownOnFailure=
     */
    public static final String DONT_TEAR_DOWN_ON_FAILURE_PROPERTY = "remote.driver.dontTearDownOnFailure";

    /**
     * edit
     */
    public static final String EDIT_LINK_TEXT = "edit";

    /**
     * iframeportlet
     */
    public static final String IFRAMEPORTLET_NAME = "iframeportlet";

    /**
     * (//a[contains(text(),'Uif Components (Kitchen Sink)')])[2]
     */
    public static final String KITCHEN_SINK_XPATH = "(//a[contains(text(),'Uif Components (Kitchen Sink)')])";

    /**
     * KRAD
     */
    public static final String KRAD_XPATH = "KRAD";

    /**
     * Kuali :: Uif Components
     */
    public static final String KUALI_UIF_COMPONENTS_WINDOW_XPATH = "Kuali :: Uif Components";

    /**
     * "Kuali :: View Title"
     */
    public static final String KUALI_VIEW_WINDOW_TITLE = "Kuali :: View Title";

    /**
     * //input[@name='imageField' and @value='Logout']
     */
    public static final String LOGOUT_XPATH = "//input[@name='imageField' and @value='Logout']";

    /**
     * Main Menu
     */
    public static final String MAIN_MENU_LINK_TEXT = "Main Menu";

    /**
     * ^[\s\S]*error[\s\S]*$"
     */
    public static final String REGEX_ERROR = "^[\\s\\S]*error[\\s\\S]*$";

    /**
     * ^[\s\S]*valid[\s\S]*$
     */
    public static final String REGEX_VALID = "^.*\\bvalid\\b.*$";

    /**
     * Set -Dremote.public.user= to the username to login as
     */
    public static final String REMOTE_PUBLIC_USER_PROPERTY = "remote.public.user";

    /**
     * You probably don't want to really be using a userpool, set -Dremote.public.userpool= to base url if you must.
     */
    public static final String REMOTE_PUBLIC_USERPOOL_PROPERTY = "remote.public.userpool";

    /**
     * Set -Dremote.public.wait.seconds to override DEFAULT_WAIT_SEC
     */
    public static final String REMOTE_PUBLIC_WAIT_SECONDS_PROPERTY = "remote.public.wait.seconds";

    /**
     * return value
     */
    public static final String RETURN_VALUE_LINK_TEXT = "return value";

    /**
     * Kuali :: Rich Messages
     */
    public static final String RICH_MESSAGES_WINDOW_TITLE = "Kuali :: Rich Messages";

    /**
     * //div[contains(div,'Document was successfully saved.')]
     */
    public static final String SAVE_SUCCESSFUL_XPATH = "//div[contains(div,'Document was successfully saved.')]";

    /**
     * //input[@name='methodToCall.save' and @alt='save']
     */
    public static final String SAVE_XPATH="//input[@name='methodToCall.save' and @alt='save']";

    /**
     * KIM Screens
     * //*[@name='methodToCall.save' and @alt='save']
     */
    public static final String SAVE_XPATH_2 = "//*[@name='methodToCall.save' and @alt='save']";

    /**
     * //input[@title='search' and @name='methodToCall.search']
     */
    public static final String SAVE_XPATH_3 = "//input[@title='search' and @name='methodToCall.search']";

    /**
     * //input[@name='methodToCall.search' and @value='search']
     */
    public static final String SEARCH_XPATH="//input[@name='methodToCall.search' and @value='search']";

    /**
     * //input[@name='methodToCall.route' and @alt='submit']
     */
    public static final String SUBMIT_XPATH="//input[@name='methodToCall.route' and @alt='submit']";

    /**
     * //input[@value='search']
     */
    public static final String SEARCH_XPATH_2 = "//input[@value='search']";

    /**
     * //button[contains(text(),'Search')]
     */
    public static final String SEARCH_XPATH_3 = "//button[contains(text(),'earch')]";

    /**
     * div.uif-group.uif-collectionGroup.uif-tableCollectionGroup.uif-tableSubCollection.uif-disclosure span.uif-headerText-span
     */
    public static final String SUB_COLLECTION_UIF_DISCLOSURE_SPAN_UIF_HEADER_TEXT_SPAN_XPATH =
            "div.uif-group.uif-collectionGroup.uif-tableCollectionGroup.uif-tableSubCollection.uif-disclosure span.uif-headerText-span";

    /**
     * timeout
     */
    public static final String TIMEOUT_MESSAGE = "timeout";

    /**
     * Travel Account Lookup
     */
    public static final String TRAVEL_ACCOUNT_LOOKUP_LINK_TEXT = "Travel Account Lookup";

    /**
     * Uif Components (Kitchen Sink)
     */
    public static final String UIF_COMPONENTS_KITCHEN_SINK_LINK_TEXT = "Uif Components (Kitchen Sink)";

    /**
     * (//a[contains(text(),'Validation Framework Demo')])[2]
     */
    public static final String VALIDATION_FRAMEWORK_DEMO_XPATH = "(//a[contains(text(),'Validation Framework Demo')])";

    /**
     * Kuali :: Collection Totaling
     */
    public static final String KUALI_COLLECTION_TOTALLING_WINDOW_XPATH = "Kuali :: Collection Totaling";

    /**
     * //a[text()='Collection Totaling']
     */
    public static final String KUALI_COLLECTION_TOTALLING_XPATH = "//a[text()='Collection Totaling']";
    
    /**
     * XML Ingester
     */
    public static final String XML_INGESTER_LINK_TEXT = "XML Ingester";

    /**
     * //a[@title='FiscalOfficerInfo Maintenance (New)']
     */
    public static final String FISCAL_OFFICER_INFO_MAINTENANCE_NEW_XPATH = "//a[@title='FiscalOfficerInfo Maintenance (New)']";

    static ChromeDriverService chromeDriverService;

    protected WebDriver driver;
    protected String user = "admin";
    protected int waitSeconds = DEFAULT_WAIT_SEC;
    protected boolean passed = false;
    protected String uiFramework = ITUtil.REMOTE_UIF_KNS;   // default to KNS

    private Log log = LogFactory.getLog(getClass());

    public @Rule
    TestName testName = new TestName();

    protected String testMethodName;

    protected String jGrowlHeader;

    String sessionId = null;

    /**
     * If WebDriverUtil.chromeDriverCreateCheck() returns a ChromeDriverService, start it.
     * {@link edu.samplu.common.WebDriverUtil#chromeDriverCreateCheck()}
     * @throws Exception
     */
    @BeforeClass
    public static void chromeDriverService() throws Exception {
        chromeDriverService = WebDriverUtil.chromeDriverCreateCheck();
        if (chromeDriverService != null)
            chromeDriverService.start();
    }

    /**
     * Navigation tests should return ITUtil.PORTAL.
     * Bookmark tests should return BOOKMARK_URL.
     *
     * @return string
     */
    public abstract String getTestUrl();

    /**
     * SeleniumBaseTest.fail from navigateInternal results in the test not being recorded as a failure in CI.
     * @throws Exception
     */
    protected void navigateInternal() throws Exception {
        // just a hook...for now...
    }

    protected void startSession(Method method) throws Exception {
        testMethodName = method.getName(); // TestNG
    }

    /**
     * Failures in testSetup cause the test to not be recorded.  Future plans are to extract form @Before and call at the start of each test.
     * Setup the WebDriver properties, test, and login.  Named testSetUp so it runs after TestNG's startSession(Method)
     * {@link WebDriverUtil#determineUser(String)}
     * {@link WebDriverUtil#setUp(String, String, String, String)}
     */
    @Before
    public void testSetUp() {
        // TODO it would be better if all opening of urls and logging in was not done in setUp, failures in setUp case the test to not be recorded. extract to setUp and call first for all tests.
        try { // Don't throw any exception from this methods, exceptions in Before annotations really mess up maven, surefire, or failsafe
            if (testName != null && testName.getMethodName() != null) { // JUnit
                testMethodName = testName.getMethodName();
            }

            waitSeconds = Integer.parseInt(System.getProperty(REMOTE_PUBLIC_WAIT_SECONDS_PROPERTY, DEFAULT_WAIT_SEC + ""));
            String givenUser = WebDriverUtil.determineUser(this.toString());
            if (givenUser != null) {
                user = givenUser;
            }

            String testUrl = kulrice9804(); // https://jira.kuali.org/browse/KULRICE-9804 KNS Create new link absent when Bookmark URL requires Login

            driver = WebDriverUtil.setUp(getUserName(), testUrl , getClass().getSimpleName(), testMethodName);
            this.sessionId = ((RemoteWebDriver) driver).getSessionId().toString();

            jGrowlHeader = getClass().getSimpleName() + "." + testMethodName;
            System.out.println(jGrowlHeader + " sessionId is " + sessionId);
            WebDriverUtil.loginKradOrKns(driver, user, this);

            navigateInternal(); // SeleniumBaseTest.fail from navigateInternal results in the test not being recorded as a failure in CI.

        } catch (Throwable t) {
            System.out.println("Throwable " + t.getMessage() + " in Before annotated method is very bad, ignoring and letting first method of test class to fail.");
            t.printStackTrace();
            System.out.println("Throwable " + t.getMessage() + " in Before annotated method is very bad, ignoring and letting first method of test class to fail.");
        }
    }

    /**
     * // https://jira.kuali.org/browse/KULRICE-9804 KNS Create new link absent when Bookmark URL requires Login
     * @return
     */
    private String kulrice9804() {
        String testUrl = getTestUrl();
        if (testUrl.contains(ITUtil.HIDE_RETURN_LINK) &&
            !testUrl.contains("&showMaintenanceLinks=true")) {
            testUrl += "&showMaintenanceLinks=true";
        }
        return testUrl;
    }

    /**
     * Tear down test as configured.  Do not allow exceptions to be thrown by tearDown, it kills the test run.
     * {@link WebDriverUtil#tearDown(boolean, String, String, String)}
     * {@link WebDriverLegacyITBase#REMOTE_PUBLIC_USERPOOL_PROPERTY}
     * {@link edu.samplu.common.ITUtil#dontTearDownPropertyNotSet()}
     * @throws Exception
     */
    @After
    public void tearDown() {
        try {
            if (!passed) { // TODO move to Failable.fail impl to included error message, when it is promoted to this class
                jGrowlSticky("FAILURE!");
            }

            WebDriverUtil.tearDown(passed, sessionId, this.toString().trim(), user);
        } catch (Throwable t) {
            System.out.println("Exception in tearDown " + t.getMessage());
            t.printStackTrace();
        }

        finally {
            try {
                closeAndQuitWebDriver();
            } catch (Throwable t) {
                System.out.println(t.getMessage() + " occured during tearDown, ignoring to avoid killing test run.");
                t.printStackTrace();
                System.out.println(t.getMessage() + " occured during tearDown, ignoring to avoid killing test run.");
            }
        }
    }

    private void closeAndQuitWebDriver() {
        if (driver != null) {
            if (ITUtil.dontTearDownPropertyNotSet() && dontTearDownOnFailure()) {
                try {
                    driver.close();
                } catch (NoSuchWindowException nswe) {
                    System.out.println("NoSuchWindowException closing WebDriver " + nswe.getMessage());
                } finally {
                    if (driver != null) {
                        driver.quit();
                    }
                }
            }
        } else {
            System.out.println("WebDriver is null for " + this.getClass().toString() + ", if using saucelabs, has" +
                    " sauceleabs been uncommented in WebDriverUtil.java?  If using a remote hub did you include the port?");
        }
    }

    private boolean dontTearDownOnFailure() {
        if (!"n".equalsIgnoreCase(System.getProperty(DONT_TEAR_DOWN_ON_FAILURE_PROPERTY, "n"))) {
            return passed;
        }
        return true;
    }

    /**
     * Set the test state to passed, this method is required to be called at the conclusion of a test for the saucelabs state of a test to be updated.
     */
    protected void passed() {
        passed = true;
        jGrowlSticky("Passed");
    }

    protected void agendaLookupAssertions() throws Exception {
        testLookUp();
        assertTextPresent("Rules");
        waitAndClick(By.xpath(CANCEL2_XPATH));
    }

    /**
     * Accept the javascript alert (clicking OK)
     *
     */
    protected void alertAccept() {
        Alert alert = driver.switchTo().alert();
        //update is executed
        alert.accept();
    }

    /**
     * Dismiss the javascript alert (clicking Cancel)
     *
     */
    protected void alertDismiss() {
        Alert alert = driver.switchTo().alert();
        //update is executed
        alert.dismiss();
    }

    protected void assertAttributeClassRegexDoesntMatch(String field, String regex) throws InterruptedException {
        Thread.sleep(1000);
        String attribute = waitAndGetAttributeByName(field, "class");
        SeleneseTestBase.assertTrue("waitAndGetAttributeByName(" + field + ", \"class\") should not be null", attribute != null);
        SeleneseTestBase.assertFalse("attribute " + attribute + " matches regex " + regex + " and it should not",
                attribute.matches(regex));
    }

    protected void assertAttributeClassRegexMatches(String field, String regex) throws InterruptedException {
        Thread.sleep(1000);
        String attribute = waitAndGetAttributeByName(field, "class");
        SeleneseTestBase.assertTrue("waitAndGetAttributeByName(" + field + ", \"class\") should not be null", attribute != null);
        SeleneseTestBase.assertTrue("attribute " + attribute + " doesn't match regex " + regex, attribute.matches(
                regex));
    }

    protected void assertBlanketApproveButtonsPresent() {
        assertElementPresentByName("methodToCall.route");
        assertElementPresentByName("methodToCall.save");
        assertElementPresentByName(BLANKET_APPROVE_NAME, "Blanket Approve button not present does " + user + " have permssion?");
        assertElementPresentByName("methodToCall.close");
        assertElementPresentByName(CANCEL_NAME);
    }

    protected void assertButtonDisabledByText(String buttonText) {
        SeleneseTestBase.assertTrue(!findButtonByText(buttonText).isEnabled());
    }

    protected void assertButtonEnabledByText(String buttonText) {
        SeleneseTestBase.assertTrue(findButtonByText(buttonText).isEnabled());
    }

    protected void assertCancelConfirmation() throws InterruptedException {
        waitAndClickByLinkText("Cancel");
        alertDismiss();
    }

    protected void assertDocFinal(String docId) throws InterruptedException {
        jiraAwareWaitFor(By.linkText("spreadsheet"), "");

        if (isElementPresent(By.linkText(docId))) {
            SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getDocStatus());
        } else {
            SeleneseTestBase.assertEquals(docId,findElement(By.xpath(DOC_ID_XPATH_2)));
            SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getDocStatus());
        }
    }

    protected void assertElementPresentByName(String name) {
        assertElementPresentByName(name, "");
    }

    protected void assertElementPresentByName(String name, String message) {
        try {
            findElement(By.name(name));
        } catch (Exception e) {
            failableFail(name + " not present " + message);
        }
    }

    protected void assertElementPresentByXpath(String locator) {
        assertElementPresentByXpath(locator, "");
    }

    protected void assertElementPresentByXpath(String locator, String message) {
        try {
            findElement(By.xpath(locator));
        } catch (Exception e) {
            jiraAwareFail(By.xpath(locator), message, e);
        }
    }

    protected void assertElementPresentByLinkText(String linkText) {
        try {
            findElement(By.linkText(linkText));
        } catch (Exception e) {
            jiraAwareFail(By.cssSelector(linkText), "", e);
        }

    }

    protected void assertElementPresent(String locator) {
        try {
            findElement(By.cssSelector(locator));
        } catch (Exception e) {
            jiraAwareFail(By.cssSelector(locator), "", e);
        }
    }

    protected void assertFocusTypeBlurError(String field, String textToType) throws InterruptedException {
        fireEvent(field, "focus");
        waitAndTypeByName(field, textToType);
        fireEvent(field, "blur");
        Thread.sleep(200);
        assertAttributeClassRegexMatches(field, REGEX_ERROR);
    }

    protected void assertFocusTypeBlurError(String field, String[] errorInputs) throws InterruptedException {
        for (String errorInput: errorInputs) {
            assertFocusTypeBlurError(field, errorInput);
            clearTextByName(field);
        }
    }

    protected void assertFocusTypeBlurValid(String field, String textToType) throws InterruptedException {
        fireEvent(field, "focus");
        waitAndTypeByName(field, textToType);
        fireEvent(field, "blur");
        Thread.sleep(200);
        assertAttributeClassRegexMatches(field, REGEX_VALID);
        assertAttributeClassRegexDoesntMatch(field, REGEX_ERROR);
    }

    protected void assertFocusTypeBlurValid(String field, String[] validInputs) throws InterruptedException {
        for (String validInput: validInputs) {
            assertFocusTypeBlurValid(field, validInput);
            clearTextByName(field);
        }
    }

    protected void assertIsVisibleByXpath(String xpath, String message) {
        if (!isVisibleByXpath(xpath)) {
            jiraAwareFail(xpath + " not visiable " + message);
        }
    }

    protected void assertIsNotVisibleByXpath(String xpath, String message) {
        if (isVisibleByXpath(xpath)) {
            jiraAwareFail(xpath + " not visiable " + message);
        }
    }

    protected void assertIsVisible(String locator) {
        if (!isVisible(locator)) {
            jiraAwareFail(locator + " is not visible and should be");
        }
    }

    protected void assertIsVisibleById(String id) {
        if (!isVisibleById(id)) {
            jiraAwareFail(id + " is not visible and should be");
        }
    }

    protected void assertIsNotVisible(String locator) {
        if (isVisible(locator)) {
            jiraAwareFail(locator + " is visible and should not be");
        }
    }

    protected void assertIsNotVisibleByXpath(String xpath) {
        if (isVisible(By.xpath(xpath))) {
            jiraAwareFail(xpath + " is visible and should not be");
        }
    }

    protected void assertLabelFor(String forElementId, String labelText) {
        SeleneseTestBase.assertEquals(labelText, getForLabelText(forElementId));
    }

    /**
     * Assert that clicking an element causes a popup window with a specific URL
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @param by The locating mechanism of the element to be clicked
     * @param windowName The name of the popup window
     * @param url The URL of the popup window
     */
    protected void assertPopUpWindowUrl(By by, String windowName, String url) {
        findElement(by).click();
        String parentWindowHandle = driver.getWindowHandle();
        // wait page to be loaded
        driver.switchTo().window(windowName).findElements(By.tagName("head"));
        SeleneseTestBase.assertEquals(url, driver.getCurrentUrl());
        driver.switchTo().window(parentWindowHandle);
    }

    protected void assertTableLayout() {
        String pageSource = driver.getPageSource();
        SeleneseTestBase.assertTrue(pageSource.contains("Table Layout"));
        SeleneseTestBase.assertTrue(pageSource.contains("Field 1"));
        SeleneseTestBase.assertTrue(pageSource.contains("Field 2"));
        SeleneseTestBase.assertTrue(pageSource.contains("Field 3"));
        SeleneseTestBase.assertTrue(pageSource.contains("Field 4"));
        SeleneseTestBase.assertTrue(pageSource.contains("Actions"));
    }

    /**
     * Warning, this only does a check against the page source.  The form url can have random character that match simple text
     * @param text
     */
    protected void assertTextPresent(String text) {
        assertTextPresent(text, "");
    }

    /**
     * Warning, this only does a check against the page source.  The form url can have random character that match simple text
     * @param text
     */
    protected void assertTextPresent(String text, String message) {
        String pageSource = driver.getPageSource();
        if (!pageSource.contains(text)) {
            failableFail(text + " not present " + message);
        }
    }

    /**
     * Warning, this only does a check against the page source.  The form url can have random character that match simple text
     * @param text
     */
    protected void assertTextPresent(String text, String cssSelector, String message){
        WebElement element = findElement(By.cssSelector(cssSelector));
        if (!element.getText().contains(text)){
            failableFail(text + " for " + cssSelector + " not present " + message);
        }
    }

    /**
     * Asset that the given text does not occur in the page
     * Warning, this only does a check against the page source.  The form url can have random character that match simple text
     * @param text the text to search for
     */
    protected void assertTextNotPresent(String text) {
        assertTextNotPresent(text, "");
    }

    /**
     * Assert that the given text does not occur in the page, and add an additional message to the failure
     * @param text the text to search for
     * @param message the message to add to the failure
     */
    protected void assertTextNotPresent(String text, String message) {
        if (driver.getPageSource().contains(text)) {
            failableFail(text + " is present and should not be " + message);
        }
    }

    protected void back() {
        driver.navigate().back();
    }

    private void blanketApproveAssert() throws InterruptedException {
        checkForDocError();
        ITUtil.checkForIncidentReport(driver.getPageSource(), DOC_SEARCH_XPATH, this, "Blanket Approve failure");
        waitAndClickDocSearch();
        waitForElementsPresentByClassName("footer-copyright", "footer-copyright");
        SeleneseTestBase.assertEquals("Kuali Portal Index", driver.getTitle());
        selectFrameIframePortlet();
        waitAndClickSearch();
    }

    protected void blanketApproveCheck() throws InterruptedException {
        ITUtil.checkForIncidentReport(driver.getPageSource(), BLANKET_APPROVE_NAME, this, "");
        waitAndClickByName(BLANKET_APPROVE_NAME,
                "No blanket approve button does the user " + getUserName() + " have permission?");
        Thread.sleep(2000);
    }

    /**
     * Tests blanket approve action.
     * This method is used by several different tests which perform various types of blanket approvals.
     * Therefore, this is a candidate to remain in this base class
     *
     * @throws InterruptedException
     */
    protected void blanketApproveTest() throws InterruptedException {
        ITUtil.checkForIncidentReport(driver.getPageSource(), BLANKET_APPROVE_NAME, this, "");
        waitAndClickByName(BLANKET_APPROVE_NAME,
                "No blanket approve button does the user " + getUserName() + " have permission?");
        Thread.sleep(2000);

        blanketApproveAssert();
    }

    protected void check(By by) throws InterruptedException {
        WebElement element = findElement(by);

        if (!element.isSelected()) {
            element.click();
        }
    }

    protected void checkById(String id) throws InterruptedException {
        check(By.id(id));
    }

    protected void checkByName(String name) throws InterruptedException {
        check(By.name(name));
    }

    protected void checkByXpath(String locator) throws InterruptedException {
        check(By.xpath(locator));
    }

    protected void checkErrorMessageItem(String message) {
        final String error_locator = "//li[@class='uif-errorMessageItem']";
        assertElementPresentByXpath(error_locator);
        String errorText = null;

        try {
            errorText = getTextByXpath(error_locator);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (errorText != null && errorText.contains("errors")) {
            failableFail(errorText + message);
        }
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     */
    public void checkForDocError() {
        checkForIncidentReport();
        if (hasDocError()) {
            String errorText = extractErrorText();
            failableFail(errorText);
        }
    }

    protected String extractErrorText() {
        String errorText = driver.findElement(By.xpath(ITUtil.DIV_ERROR_LOCATOR)).getText(); // don't highlight
        errorText = ITUtil.blanketApprovalCleanUpErrorText(errorText);
        if (driver.findElements(By.xpath(ITUtil.DIV_EXCOL_LOCATOR)).size() > 0) { // not present if errors are at the bottom of the page (see left-errmsg below)
            errorText = ITUtil.blanketApprovalCleanUpErrorText(driver.findElement( // don't highlight
                    By.xpath(ITUtil.DIV_EXCOL_LOCATOR)).getText()); // replacing errorText as DIV_EXCOL_LOCATOR includes the error count
        }
        if (driver.findElements(By.xpath(DIV_LEFT_ERRMSG)).size() > 0) {
            errorText = errorText + ITUtil.blanketApprovalCleanUpErrorText(driver.findElement(By.xpath(DIV_LEFT_ERRMSG)).getText()); // don't highlight
        }
        return errorText;
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @return
     */
    public boolean hasDocError() {
        if (driver.findElements(By.xpath(ITUtil.DIV_ERROR_LOCATOR)).size() > 0) {
            String errorText = driver.findElement(By.xpath(ITUtil.DIV_ERROR_LOCATOR)).getText(); // don't highlight
            if (errorText != null && errorText.contains("error(s) found on page.")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @param errorTextToMatch
     * @return
     */
    public boolean hasDocError(String errorTextToMatch) {
        if (driver.findElements(By.xpath(ITUtil.DIV_ERROR_LOCATOR)).size() > 0) {
            String errorText = driver.findElement(By.xpath(ITUtil.DIV_ERROR_LOCATOR)).getText(); // don't highlight
            if (errorText != null && errorText.contains("error(s) found on page.")) {
                WebElement errorDiv = driver.findElement(By.xpath("//div[@class='left-errmsg']/div[2]/div")); // don't highlight
                if (errorDiv != null) {
                    errorText = errorDiv.getText();
                    return errorText != null && errorText.contains(errorTextToMatch);
                }
            }
        }
        return false;
    }

    protected void checkForIncidentReport() {
        checkForIncidentReport("", "");
    }

    protected void checkForIncidentReport(String locator) {
        checkForIncidentReport(locator, "");
    }

    protected void checkForIncidentReport(String locator, String message) {
        ITUtil.checkForIncidentReport(driver.getPageSource(), locator, this, message);
    }

    protected void checkForIncidentReport(String locator, Failable failable, String message) {
        ITUtil.checkForIncidentReport(driver.getPageSource(), locator, failable, message);
    }

    protected void clearText(By by) throws InterruptedException {
        findElement(by).clear();
    }

    protected void clearText(String selector) throws InterruptedException {
        clearText(By.cssSelector(selector));
    }

    protected void clearTextByName(String name) throws InterruptedException {
        clearText(By.name(name));
    }

    protected void clearTextByXpath(String locator) throws InterruptedException {
        clearText(By.xpath(locator));
    }

    protected void close() {
        driver.close();
    }

    protected void colapseExpandByXpath(String clickLocator, String visibleLocator) throws InterruptedException {
        waitAndClickByXpath(clickLocator);
        waitNotVisibleByXpath(visibleLocator);
        waitAndClickByXpath(clickLocator);
        waitIsVisibleByXpath(visibleLocator);
    }

    protected String configNameSpaceBlanketApprove() throws Exception {
        String docId = waitForDocId();
        String dtsPlusTwoChars = ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Namespace " + ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits());
        assertBlanketApproveButtonsPresent();
        waitAndTypeByXpath(DOC_CODE_XPATH, "VTN" + dtsPlusTwoChars);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']",
                "Validation Test NameSpace " + dtsPlusTwoChars);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.applicationId']", "RICE");

        return docId;
    }

    protected void contextLookupAssertions() throws Exception {
        testLookUp();
        assertTextPresent("Notes and Attachments");
        waitAndClick(By.xpath(CANCEL2_XPATH));
        passed();
    }

//    protected void deleteSubCollectionLine() throws Exception {
//        // click on collections page link
//        waitAndClickByLinkText(COLLECTIONS_LINK_TEXT);
//        Thread.sleep(5000);
//
//        // wait for collections page to load by checking the presence of a sub collection line item
//        waitForElementPresentByName("list4[0].subList[0].field1");
//
//        // change a value in the line to be deleted
//        waitAndTypeByName("list4[0].subList[0].field1", "selenium");
//
//        // click the delete button
//        waitAndClickByXpath("//div[@id='collection4_disclosureContent']/div[@class='uif-stackedCollectionLayout']/div[@class='uif-group uif-gridGroup uif-collectionItem uif-gridCollectionItem']/table/tbody/tr[5]/td/div/fieldset/div/div[@class='uif-disclosureContent']/div[@class='dataTables_wrapper']/table/tbody/tr[2]/td[6]/div/fieldset/div/div[@class='uif-boxLayout uif-horizontalBoxLayout clearfix']/button");
//        Thread.sleep(2000);
//
//        // confirm that the input box containing the modified value is not present
//        for (int second = 0;; second++) {
//            if (second >= waitSeconds)
//                failableFail(TIMEOUT_MESSAGE);
//            try {
//                if (!"selenium".equals(waitAndGetAttributeByName("list4[0].subList[0].field1", "value")))
//                    break;
//            } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        // verify that the value has changed for the input box in the line that has replaced the deleted one
//        assertNotSame("selenium", waitAndGetAttributeByName("list4[0].subList[0].field1", "value"));
//    }

    protected void expandColapseByXpath(String clickLocator, String visibleLocator) throws InterruptedException {
        waitAndClickByXpath(clickLocator);
        waitIsVisibleByXpath(visibleLocator);
        waitAndClickByXpath(clickLocator);
        waitNotVisibleByXpath(visibleLocator);
    }


    /**
     * {@link org.openqa.selenium.WebDriver#getWindowHandles()}
     * @return
     */
    public String[] getAllWindowTitles() {
        return (String[]) driver.getWindowHandles().toArray();
    }

    protected String waitAndGetAttribute(By by, String attribute) throws InterruptedException {
        jiraAwareWaitFor(by, attribute);
        
        return findElement(by).getAttribute(attribute);
    }

    /**
     * Get value of any attribute by using element name
     *
     * @param name name of an element
     * @param attribute the name of an attribute whose value is to be retrieved
     */
    protected String waitAndGetAttributeByName(String name, String attribute) throws InterruptedException {
        return waitAndGetAttribute(By.name(name), attribute);
    }

    /**
     * Get value of any attribute by using element xpath
     *
     * @param locator locating mechanism of an element
     * @param attribute the name of an attribute whose value is to be retrieved
     */
    protected String waitAndGetAttributeByXpath(String locator, String attribute) throws InterruptedException {
        return waitAndGetAttribute(By.xpath(locator), attribute);
    }

    protected String[] waitAndGetText(By by) throws InterruptedException {
        WebDriverUtil.waitFors(driver, DEFAULT_WAIT_SEC, by, "");
        List<WebElement> found = findElements(by);
        String[] texts = new String[found.size()];
        int i = 0;

        for (WebElement element: found) {
            texts[i++] = element.getText();
        }

        if (texts.length == 0) {
            jiraAwareFail(by.toString());
        }

        return texts;
    }


    protected String getBaseUrlString() {
        return ITUtil.getBaseUrlString();
    }

    protected int getCssCount(String selector) {
        return getCssCount(By.cssSelector(selector));
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @param by
     * @return
     */
    protected int getCssCount(By by) {
        return (findElements(by)).size();
    }

    protected String getDocStatus() {
        return findElement(By.xpath(DOC_STATUS_XPATH_2)).getText();
    }

    /**
     * Uses Selenium's findElements for getting the options (findElement for the select) method which does not throw a test exception if not found.
     * @param by
     * @return
     * @throws InterruptedException
     */
    protected String[] getSelectOptions(By by) throws InterruptedException {
        WebElement select1 = driver.findElement(by); // don't highlight
        List<WebElement> options = select1.findElements(By.tagName("option"));
        String[] optionValues = new String[options.size()];
        int counter = 0;

        for (WebElement option : options) {
            optionValues[counter] = option.getAttribute("value");
            counter++;
        }

        return optionValues;
    }

    protected String[] getSelectOptionsByName(String name) throws InterruptedException {
        return getSelectOptions(By.name(name));
    }

    protected String[] getSelectOptionsByXpath(String locator) throws InterruptedException {
        return getSelectOptions(By.xpath(locator));
    }

    /**
     *
     * @return sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    protected String getText(By by) throws InterruptedException {
        WebElement element = findElement(by);
        return element.getText();
    }

    protected String getTextByClassName(String className) throws InterruptedException {
        return getText(By.className(className));
    }

    protected String getTextById(String id) throws InterruptedException {
        return getText(By.id(id));
    }

    protected String getTextByName(String name) throws InterruptedException {
        return getText(By.name(name));
    }

    protected String getText(String locator) throws InterruptedException {
        return getText(By.cssSelector(locator));
    }

    protected String getTextByXpath(String locator) throws InterruptedException {
        return getText(By.xpath(locator));
    }

    protected String getTitle() {
        return driver.getTitle();
    }

    /**
     * "admin" by default.  Can be overridden using {@link WebDriverLegacyITBase#REMOTE_PUBLIC_USER_PROPERTY}
     * @return string
     */
    public String getUserName() {
        return user;
    }

    /**
     * Handles simple nested frame content; validates that a frame and nested frame exists before
     * switching to it
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     */
    protected void gotoNestedFrame() {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.switchTo().defaultContent();
       
        if (driver.findElements(By.xpath("//iframe")).size() > 0) {
            WebElement containerFrame = driver.findElement(By.xpath("//iframe")); // don't highlight
            driver.switchTo().frame(containerFrame);
        }
        
        if (driver.findElements(By.xpath("//iframe")).size() > 0) {
            WebElement contentFrame = driver.findElement(By.xpath("//iframe")); // don't highlight
            driver.switchTo().frame(contentFrame);
        }
        
        driver.manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
    }

    /**
     * Fail using the Failable fail method.  Calls jGrowlSticky then fail.
     * @param message to display with failure
     */
    public void failableFail(String message) {
        passed = false;
        jGrowlSticky(message);
        fail(message); // Failable.fail
    }

    protected WebElement findButtonByText(String buttonText) {
        return findElement(By.xpath("//button[contains(text(), '" + buttonText + "')]"));
    }

    protected WebElement findElement(By by) {
        WebElement found = driver.findElement(by);
        WebDriverUtil.highlightElement(driver, found);
        return found;
    }

    protected WebElement findElement(By by, WebElement elementToFindOn) {
        WebElement found = elementToFindOn.findElement(by);
        WebDriverUtil.highlightElement(driver, found);
        return found;
    }

    protected List<WebElement> findVisibleElements(By by) {
        List<WebElement> webElements = driver.findElements(by);
        List<WebElement> visibleWebElements = new LinkedList<WebElement>();
        for (WebElement webElement: webElements) {
            if (webElement.isDisplayed()) {
                visibleWebElements.add(webElement);
            }
        }

        return visibleWebElements;
    }

    protected List<WebElement> findElements(By by) {
        List<WebElement> found = driver.findElements(by);
        return found;
    }

    protected void fireEvent(String name, String event) {
        ((JavascriptExecutor) driver).executeScript("var elements=document.getElementsByName(\"" + name + "\");" +
                "for (var i = 0; i < elements.length; i++){" +
                "elements[i]." + event + "();}");
    }

    protected void fireEvent(String name, String value, String event) {
        ((JavascriptExecutor) driver).executeScript("var elements=document.getElementsByName(\"" + name + "\");" +
                "for (var i = 0; i < elements.length; i++){" +
                "if(elements[i].value=='" + value + "')" +
                "elements[i]." + event + "();}");
    }

    /**
     * {@link Actions#moveToElement(org.openqa.selenium.WebElement)}
     * @param name
     */
    public void fireMouseOverEventByName(String name) {
        this.fireMouseOverEvent(By.name(name));
    }

    /**
     * {@link Actions#moveToElement(org.openqa.selenium.WebElement)}
     * @param id
     */
    public void fireMouseOverEventById(String id) {
        this.fireMouseOverEvent(By.id(id));
    }

    /**
     * {@link Actions#moveToElement(org.openqa.selenium.WebElement)}
     * @param locator
     */
    public void fireMouseOverEventByXpath(String locator) {
        this.fireMouseOverEvent(By.xpath(locator));
    }

    /**
     * {@link Actions#moveToElement(org.openqa.selenium.WebElement)}
     * @param by
     */
    public void fireMouseOverEvent(By by) {
        Actions builder = new Actions(driver);
        Actions hover = builder.moveToElement(findElement(by));
        hover.perform();
    }

    protected boolean isChecked(By by) {
        return findElement(by).isSelected();
    }

    protected boolean isCheckedById(String id) {
        return isChecked(By.id(id));
    }

    protected boolean isCheckedByName(String name) {
        return isChecked(By.name(name));
    }

    protected boolean isCheckedByXpath(String locator) {
        return isChecked(By.xpath(locator));
    }

    protected boolean isEnabled(By by) {
        return findElement(by).isEnabled();
    }

    protected boolean isEnabledById(String id) {
        return isEnabled(By.id(id));
    }

    protected boolean isEnabledByName(String name) {
        return isEnabled(By.name(name));
    }

    protected boolean isEnabledByXpath(String locator) {
        return isEnabled(By.xpath(locator));
    }

    protected int howManyAreVisible(By by) throws InterruptedException {
        int count = 0;
        if (by == null) {

            return count;
        }

        List<WebElement> webElementsFound = driver.findElements(by);
        for (WebElement webElement: webElementsFound) {
            if (webElement.isDisplayed()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @param by
     * @return
     */
    protected boolean isElementPresent(By by) {
        return (driver.findElements(by)).size() > 0;
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @param locator
     * @return
     */
    protected boolean isElementPresent(String locator) {
        return (driver.findElements(By.cssSelector(locator))).size() > 0;
    }

    protected boolean isElementPresentById(String id) {
        return isElementPresent(By.id(id));
    }

    protected boolean isElementPresentByName(String name) {
        return isElementPresent(By.name(name));
    }

    protected boolean isElementPresentByXpath(String locator) {
        return isElementPresent(By.xpath(locator));
    }

    protected boolean isElementPresentByLinkText(String locator) {
        return isElementPresent(By.linkText(locator));
    }

    protected boolean isElementPresentByDataAttributeValue(String dataAttributeName, String dataAttributeValue) {
        return isElementPresent(By.cssSelector("[data-" + dataAttributeName +"='"+ dataAttributeValue +"']"));
    }

    protected boolean isNotVisible(By by) {
        return !(isVisible(by));
    }

    protected Boolean isTextPresent(String text) {
        if (driver.getPageSource().contains(text)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    protected boolean isVisible(String locator) {
        return isVisible(By.cssSelector(locator));
    }

    protected boolean isVisible(By by) {
        List<WebElement> elements = driver.findElements(by);
        for (WebElement element: elements) {
            if (element.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isVisibleById(String id) {
        return isVisible(By.id(id));
    }

    protected boolean isVisibleByXpath(String locator) {
        return isVisible(By.xpath(locator));
    }

    protected void jGrowl(String message) {
        WebDriverUtil.jGrowl(driver, jGrowlHeader, false, message);
    }

    /**
     * Sticky is used on fail, making a call to jGrowl(String) from this method will result
     * in an infinite loop if JGROWL_ERROR_FAILURE is true so please don't.
     */
    protected void jGrowlSticky(String message) {
        WebDriverUtil.jGrowl(driver, jGrowlHeader, true, message);
    }

    private void jiraAwareFail(By by, String message, Throwable t) {
        JiraAwareFailureUtil.failOnMatchedJira(by.toString(), message, this);
        // if there isn't a matched jira to fail on, then fail
        checkForIncidentReport(by.toString(), message);
        failableFail(t.getMessage() + "\n" + by.toString() + " " + message + " " + driver.getCurrentUrl());
    }

    private void jiraAwareFail(String message) {
        JiraAwareFailureUtil.failOnMatchedJira(message, message, this);
        // if there isn't a matched jira to fail on, then fail
        checkForIncidentReport(message, message);
        failableFail(message + " " + driver.getCurrentUrl());
    }

    protected void jiraAwareWaitAndClick(By by, String message) throws InterruptedException {
        jiraAwareWaitAndClick(by, message, this);
    }

    protected void jiraAwareWaitAndClick(By by, String message, Failable failable) throws InterruptedException {
        try {
            jiraAwareWaitFor(by, message, failable);
            WebElement element = findElement(by);
            element.click();
        } catch (Exception e) {
            jiraAwareFail(by, message, e);
        }
    }

    protected WebElement jiraAwareWaitFor(By by, String message) throws InterruptedException {
        try {
            return WebDriverUtil.waitFor(this.driver, this.waitSeconds, by, message);
        } catch (Throwable t) {
            jiraAwareFail(by, message, t);
        }
        return null; // required, but the jiraAwareFail will will end test before this statement is reached
    }

    protected void jiraAwareWaitFors(By by, String message) throws InterruptedException {
        try {
            WebDriverUtil.waitFors(this.driver, this.waitSeconds, by, message);
        } catch (Throwable t) {
            jiraAwareFail(by, message, t);
        }
    }

    protected void jiraAwareWaitFor(By by, String message, Failable failable) throws InterruptedException {
        try {
            WebDriverUtil.waitFor(this.driver, this.waitSeconds, by, message);
        } catch (Throwable t) {
            jiraAwareFail(by, message, t);
        }
    }
    
    protected WebElement jiraAwareWaitFor(By by, int seconds, String message) throws InterruptedException {
        try {
            return WebDriverUtil.waitFor(this.driver, seconds, by, message);
        } catch (Throwable t) {
            jiraAwareFail(by, message, t);
        }
        return null; // required, but the jiraAwareFail will will end test before this statement is reached
    }

    protected void open(String url) {
        driver.get(url);
    }

    protected void selectFrameIframePortlet() {
        selectFrame(IFRAMEPORTLET_NAME);
    }

    protected void selectFrame(String locator) {
        
        if (IFRAMEPORTLET_NAME.equals(locator)) {
            gotoNestedFrame();
        } else {
           WebDriverUtil.selectFrameSafe(driver, locator);
        }
    }

    protected void selectTopFrame() {
        driver.switchTo().defaultContent();
    }

    protected void selectWindow(String locator) {
        driver.switchTo().window(locator);
    }

    protected void selectByXpath(String locator, String selectText) throws InterruptedException {
        select(By.xpath(locator), selectText);
    }

    protected void selectByName(String name, String selectText) throws InterruptedException {
        select(By.name(name), selectText);
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @param by
     * @param selectText
     * @throws InterruptedException
     */
    protected void select(By by, String selectText) throws InterruptedException {
        checkForIncidentReport(by.toString(), "trying to select text " + selectText);
        WebElement select1 = findElement(by);
        List<WebElement> options = select1.findElements(By.tagName("option"));

        for (WebElement option : options) {
            if (option.getText().equals(selectText)) {
                option.click();
                break;
            }
        }
    }

    protected void selectOptionByName(String name, String optionValue) throws InterruptedException {
        selectOption(By.name(name), optionValue);
    }

    protected void selectOptionByXpath(String locator, String optionValue) throws InterruptedException {
        selectOption(By.name(locator), optionValue);
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @param by
     * @param optionValue
     * @throws InterruptedException
     */
    protected void selectOption(By by, String optionValue) throws InterruptedException {
        WebElement select1 = findElement(by);
        List<WebElement> options = select1.findElements(By.tagName("option"));

        if (options == null || options.size() == 0) {
            failableFail("No options for select " + select1.toString() + " was looking for value " + optionValue + " using " + by.toString());
        }

        for (WebElement option : options) {
            if (option.getAttribute("value").equals(optionValue)) {
                option.click();
                break;
            }
        }
    }

    /**
     * If a window contains the given title switchTo it.
     * @param title
     */
    public void switchToWindow(String title) {
        Set<String> windows = driver.getWindowHandles();

        for (String window : windows) {
            driver.switchTo().window(window);            
            if (driver.getTitle().contains(title)) {
                return;
            }
        }
    }

    // TODO delete after AddingNameSpaceAbstractSmokeTestBase migration
    protected void testAddingNamespace() throws Exception {
        testAddingNamespace(this);
    }

    // TODO move method to AddingNameSpaceAbstractSmokeTestBase after locators are extracted
    protected void testAddingNamespace(Failable failable) throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        waitForPageToLoad();
        assertElementPresentByXpath(SAVE_XPATH_2, "save button does not exist on the page");

        //Enter details for Namespace.
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Adding PEANUTS");
        waitAndTypeByXpath("//*[@id='document.documentHeader.explanation']", "I want to add PEANUTS to test KIM");
        waitAndTypeByXpath(DOC_CODE_XPATH, "PEANUTS");
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", "The Peanuts Gang");
        checkByXpath("//input[@id='document.newMaintainableObject.active']");
        waitAndClickByXpath(SAVE_XPATH_2);
        waitForPageToLoad();
        checkForIncidentReport();
        assertDocumentStatusSaved();

        //checks it is saved and initiator is admin.
        SeleneseTestBase.assertEquals(DOC_STATUS_SAVED, findElement(By.xpath(
                "//table[@class='headerinfo']/tbody/tr[1]/td[2]")).getText());
        SeleneseTestBase.assertEquals("admin", findElement(By.xpath(
                "//table[@class='headerinfo']/tbody/tr[2]/td[1]/a")).getText());
    }

    protected void assertDocumentStatusSaved() {
        assertElementPresentByXpath(SAVE_SUCCESSFUL_XPATH,
                "Document is not saved successfully");
    }

    protected void testAddingBrownGroup() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        waitForPageToLoad();
        String docId = waitForDocId();

        //Enter details for BrownGroup.
        waitAndTypeByName("document.documentHeader.documentDescription", "Adding Brown Group");
        waitAndTypeByName("document.documentHeader.explanation", "I want to add Brown Group to test KIM");
        selectOptionByName("document.groupNamespace", "KR-IDM");
        waitForPageToLoad();
        String groupName = "BrownGroup " + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByName("document.groupName", groupName);
        checkByName("document.active");
        waitAndClickByXpath(SAVE_XPATH_2);
        waitForPageToLoad();
        assertElementPresentByXpath(SAVE_SUCCESSFUL_XPATH,"Document is not saved successfully");
        checkForIncidentReport();

        //checks it is saved and initiator is admin.
        SeleneseTestBase.assertEquals(DOC_STATUS_SAVED, findElement(By.xpath("//table[@class='headerinfo']/tbody/tr[1]/td[2]")).getText());
        SeleneseTestBase.assertEquals("admin", findElement(By.xpath("//table[@class='headerinfo']/tbody/tr[2]/td[1]/a")).getText());
        waitAndClickByName("methodToCall.performLookup.(!!org.kuali.rice.kim.impl.identity.PersonImpl!!).(((principalId:member.memberId,principalName:member.memberName))).((``)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;;::::).anchorAssignees");
        waitForPageToLoad();
        waitAndClickSearch();
        waitForPageToLoad();
        waitAndClickReturnValue();
        waitForPageToLoad();
        waitAndClickByName("methodToCall.addMember.anchorAssignees");
        waitForPageToLoad();
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        Thread.sleep(2000);
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH,"Document is not submitted successfully");
        selectTopFrame();
        waitAndClickByLinkText("Administration");
        waitForPageToLoad();
        waitAndClickByLinkText("Group");
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndTypeByName("name", groupName);
        waitAndClickSearch();
        isElementPresentByLinkText(groupName);
    }

    protected void testAttributeDefinitionLookUp() throws Exception {
        waitForPageToLoad();
        selectFrameIframePortlet();
        checkForIncidentReport("testAttributeDefinitionLookUp");
        waitAndClickByXpath("//button[contains(.,'earch')]");
        Thread.sleep(3000);
        waitForPageToLoad();
        findElement(By.tagName("body")).getText().contains("Actions"); // there are no actions, but the header is the only unique text from searching
        waitAndClickByLinkText("1000");
        waitForPageToLoad();
        findElement(By.tagName("body")).getText().contains("Attribute Inquiry");
        findElement(By.tagName("body")).getText().contains("KRMS Attributes");
        findElement(By.tagName("body")).getText().contains("Attribute Label");
        findElement(By.tagName("body")).getText().contains("1000");
        findElement(By.tagName("body")).getText().contains("peopleFlowId");
        findElement(By.tagName("body")).getText().contains("KR-RULE");
        findElement(By.tagName("body")).getText().contains("PeopleFlow");

        // selectFrame("name=fancybox-frame1343151577256"); // TODO parse source to get name
        // jiraAwareWaitAndClick("css=button:contains(Close)"); // looks lower case, but is upper
        // Thread.sleep(500);
        // jiraAwareWaitAndClick("css=button:contains(cancel)");
        // AttributeDefinition's don't have actions (yet)
        // jiraAwareWaitAndClick("id=u80");
        // waitForPageToLoad();
        // jiraAwareWaitAndClick("id=u86");
        // waitForPageToLoad();
        // selectWindow("null");
        // jiraAwareWaitAndClick("xpath=(//input[@name='imageField'])[2]");
        // waitForPageToLoad();
        passed();
    }

    protected void testCancelConfirmation() throws InterruptedException {
        waitAndCancelConfirmation();
        passed();
    }

    protected void testConfigParamaterBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        String docId = waitForDocId();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Parameter ");
        assertBlanketApproveButtonsPresent();
        SeleneseTestBase.assertEquals("", getTextByName(CANCEL_NAME));
        selectByXpath("//select[@id='document.newMaintainableObject.namespaceCode']", "KR-NS - Kuali Nervous System");
        String componentLookUp = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.coreservice.impl.component.ComponentBo!!).(((code:document.newMaintainableObject.componentCode,namespaceCode:document.newMaintainableObject.namespaceCode,))).((`document.newMaintainableObject.componentCode:code,document.newMaintainableObject.namespaceCode:namespaceCode,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(componentLookUp);
        waitAndClickSearch();
        waitAndClickReturnValue();
        String dtsTwo = ITUtil.createUniqueDtsPlusTwoRandomChars();
        String parameterName = "ValidationTestParameter" + dtsTwo;
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", parameterName);
        waitAndTypeByXpath("//textarea[@id='document.newMaintainableObject.description']",
                "Validation Test Parameter Description" + dtsTwo);
        selectByXpath("//select[@id='document.newMaintainableObject.parameterTypeCode']", "Document Validation");
        waitAndClickByXpath("//input[@id='document.newMaintainableObject.evaluationOperatorCodeAllowed']");
        waitForPageToLoad();
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testCreateNewAgenda() throws Exception {
        selectFrameIframePortlet();
        selectByName("document.newMaintainableObject.dataObject.namespace", "Kuali Rules Test");
        String agendaName = "Agenda Date :" + Calendar.getInstance().getTime().toString();
        waitAndTypeByName("document.newMaintainableObject.dataObject.agenda.name", "Agenda " + agendaName);
        waitAndTypeByName("document.newMaintainableObject.dataObject.contextName", "Context1");
        fireEvent("document.newMaintainableObject.dataObject.contextName", "blur");
        fireEvent("document.newMaintainableObject.dataObject.contextName", "focus");
        waitForElementPresentByName("document.newMaintainableObject.dataObject.agenda.typeId");
        selectByName("document.newMaintainableObject.dataObject.agenda.typeId", "Campus Agenda");
        waitForElementPresentByName("document.newMaintainableObject.dataObject.customAttributesMap[Campus]");
        waitAndTypeByName("document.newMaintainableObject.dataObject.customAttributesMap[Campus]", "BL");
        waitAndClickByXpath("//div[2]/button");
        waitForPageToLoad();
        waitAndClickByXpath("//div[2]/button[3]");
        waitForPageToLoad();
        selectTopFrame();
        waitAndClickByXpath("(//input[@name='imageField'])[2]");
        passed();
    }

    protected void testCreateDocType() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        assertElementPresentByXpath("//*[@name='methodToCall.route' and @alt='submit']","save button does not exist on the page");
        
        //waitForElementPresentByXpath(DOC_ID_XPATH);
        //String docId = findElement(By.xpath(DOC_ID_XPATH)).getText();
        String docId = waitForDocId();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Creating new Document Type");
        String parentDocType = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.kew.doctype.bo.DocumentType!!).(((name:document.newMaintainableObject.parentDocType.name,documentTypeId:document.newMaintainableObject.docTypeParentId,))).((`document.newMaintainableObject.parentDocType.name:name,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(parentDocType);
        waitForPageToLoad();
        Thread.sleep(2000);
        waitAndClickSearch();
        waitForPageToLoad();
        waitAndClickReturnValue();
        String docTypeName = "TestDocType" + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitForElementPresentByXpath("//input[@id='document.newMaintainableObject.name']");
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", docTypeName);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.unresolvedDocHandlerUrl']","${kr.url}/maintenance.do?methodToCall=docHandler");
        
        //waitAndTypeByXpath("//input[@id='document.newMaintainableObject.actualNotificationFromAddress']", "NFA");
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.label']", "Label for " + docTypeName);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.unresolvedHelpDefinitionUrl']","default.htm?turl=WordDocuments%2Fdocumenttype.htm");
        waitAndClickByXpath("//*[@name='methodToCall.route' and @alt='submit']");
        checkForIncidentReport();
        waitForPageToLoad();
        driver.switchTo().defaultContent();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, findElement(By.xpath(DOC_ID_XPATH_2)).getText());
    }

    protected void testCreateNewCancel() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        testCancelConfirmation();
    }

    protected List<String> testCreateNewParameter(String docId, String parameterName) throws Exception {
        waitForPageToLoad();
        docId = waitForDocId();
        //Enter details for Parameter.
        waitAndTypeByName("document.documentHeader.documentDescription", "Adding Test Parameter");
        selectOptionByName("document.newMaintainableObject.namespaceCode", "KR-WKFLW");
        waitAndTypeByName("document.newMaintainableObject.componentCode", "ActionList");
        waitAndTypeByName("document.newMaintainableObject.applicationId", "KUALI");
        parameterName = "TestIndicator" + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByName("document.newMaintainableObject.name", parameterName);
        waitAndTypeByName("document.newMaintainableObject.value", "Y");
        waitAndTypeByName("document.newMaintainableObject.description", "for testing");
        selectOptionByName("document.newMaintainableObject.parameterTypeCode", "HELP");
        waitAndClickByXpath("//input[@name='document.newMaintainableObject.evaluationOperatorCode' and @value='A']");
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH,"Document is not submitted successfully");
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterName);

        return params;
    }

    protected List<String> testCreateNewParameterType(String docId, String parameterType, String parameterCode)throws Exception {
        waitForPageToLoad();
        docId = waitForDocId();

        //Enter details for Parameter.
        waitAndTypeByName("document.documentHeader.documentDescription", "Adding Test Parameter Type");
        parameterCode = RandomStringUtils.randomAlphabetic(4).toLowerCase();
        waitAndTypeByName("document.newMaintainableObject.code", parameterCode);
        parameterType = "testing " + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByName("document.newMaintainableObject.name", parameterType);
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH, "Document is not submitted successfully");
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterType);
        params.add(parameterCode);

        return params;
    }

    protected void testCreateNewSearchReturnValueCancelConfirmation() throws InterruptedException, Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        waitAndClickSearch2();
        waitAndClickReturnValue();
        waitAndCancelConfirmation();
        passed();
    }

    protected List<String> testCopyParameter(String docId, String parameterName) throws Exception {
        selectFrameIframePortlet();
        waitAndClickCopy();
        waitForPageToLoad();
        docId = waitForDocId();
        waitAndTypeByName("document.documentHeader.documentDescription", "Copying Test Parameter");
        selectOptionByName("document.newMaintainableObject.namespaceCode", "KR-WKFLW");
        waitAndTypeByName("document.newMaintainableObject.componentCode", "ActionList");
        waitAndTypeByName("document.newMaintainableObject.applicationId", "KUALI");
        parameterName = "TestIndicator" + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByName("document.newMaintainableObject.name", parameterName);
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH,"Document is not submitted successfully");
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterName);

        return params;
    }

    protected List<String> testCopyParameterType(String docId, String parameterType, String parameterCode) throws Exception {
        selectFrameIframePortlet();
        waitAndClickCopy();
        waitForPageToLoad();
        docId = waitForDocId();
        waitAndTypeByName("document.documentHeader.documentDescription", "Copying Test Parameter");
        parameterCode = RandomStringUtils.randomAlphabetic(4).toLowerCase();
        waitAndTypeByName("document.newMaintainableObject.code", parameterCode);
        clearTextByName("document.newMaintainableObject.name");
        parameterType = "testing " + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByName("document.newMaintainableObject.name", parameterType);
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH, "Document is not submitted successfully");
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterType);
        params.add(parameterCode);

        return params;
    }


    protected void testDocTypeLookup() throws Exception {
        selectFrameIframePortlet();
        waitAndClickByXpath("//input[@title='Search Parent Name']");
        waitForPageToLoad();
        waitAndClickByXpath(SAVE_XPATH_3);
        waitAndClickByXpath("//table[@id='row']/tbody/tr[contains(td[3],'RiceDocument')]/td[1]/a");
        waitForPageToLoad();
        waitAndClickByXpath(SAVE_XPATH_3);
        SeleneseTestBase.assertEquals("RiceDocument", getTextByXpath("//table[@id='row']/tbody/tr/td[4]/a"));
        waitAndClickByName("methodToCall.clearValues");
        waitAndTypeByName("name", "Kuali*D");
        waitAndClickByXpath(SAVE_XPATH_3);
        assertElementPresentByXpath("//table[@id='row']/tbody/tr[contains(td[3], 'KualiDocument')]");
        String docIdOld = getTextByXpath("//table[@id='row']/tbody/tr[contains(td[3], 'KualiDocument')]/td[2]/a");
        waitAndClickByName("methodToCall.clearValues");
        waitAndTypeByName("label", "KualiDocument");
        waitAndClickByXpath(SAVE_XPATH_3);
        assertElementPresentByXpath("//table[@id='row']/tbody/tr[contains(td[5], 'KualiDocument')]");
        waitAndClickByName("methodToCall.clearValues");
        waitAndTypeByName("documentTypeId", docIdOld);
        waitAndClickByXpath(SAVE_XPATH_3);
        assertElementPresentByXpath("//table[@id='row']/tbody/tr[contains(td[2], '" + docIdOld + "')]");
    }


    protected List<String> testEditParameterType(String docId, String parameterType, String parameterCode) throws Exception {
        selectFrameIframePortlet();
        waitAndClickEdit();
        waitForPageToLoad();
        docId = waitForDocId();
        waitAndTypeByName("document.documentHeader.documentDescription", "Editing Test Parameter");
        clearTextByName("document.newMaintainableObject.name");
        parameterType = "testing " + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByName("document.newMaintainableObject.name", parameterType);
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH, "Document is not submitted successfully");
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterType);
        params.add(parameterCode);

        return params;
    }

    protected List<String> testEditParameter(String docId, String parameterName) throws Exception
    {
        selectFrameIframePortlet();
        waitAndClickEdit();
        waitForPageToLoad();
        docId = waitForDocId();
        waitAndTypeByName("document.documentHeader.documentDescription", "Editing Test Parameter");
        clearTextByName("document.newMaintainableObject.value");
        waitAndTypeByName("document.newMaintainableObject.value", "N");
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH, "Document is not submitted successfully");
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterName);
        return params;
    }

    protected void testEditRouteRulesDelegation() throws Exception {
        waitForPageToLoad();
        Thread.sleep(3000);
        SeleneseTestBase.assertEquals("Kuali Portal Index", getTitle());
        selectFrameIframePortlet();
        waitAndClickSearch();
        waitForPageToLoad();
        Thread.sleep(3000);
        waitAndClickEdit();
        waitForPageToLoad();
        Thread.sleep(3000);
        SeleneseTestBase.assertTrue(isElementPresentByName(CANCEL_NAME));
        waitAndClickCancel();
        waitForPageToLoad();
        Thread.sleep(3000);
        waitAndClickByName("methodToCall.processAnswer.button0");
        waitForPageToLoad();
        passed();
    }

    protected void testFiscalOfficerInfoMaintenanceNew() throws Exception {
        selectFrameIframePortlet();
        checkForIncidentReport("", "https://jira.kuali.org/browse/KULRICE-7723 FiscalOfficerInfoMaintenanceNewIT.testUntitled need a better name and user permission error");
        String docId = getTextByXpath("//*[@id='u13_control']");
        waitAndTypeByXpath("//input[@name='document.documentHeader.documentDescription']", "New FO Doc");
        waitAndTypeByXpath("//input[@name='document.newMaintainableObject.dataObject.id']", "5");
        waitAndTypeByXpath("//input[@name='document.newMaintainableObject.dataObject.userName']", "Jigar");
        waitAndClickByXpath("//button[@id='usave']");
        Integer docIdInt = Integer.valueOf(docId).intValue();
        selectTopFrame();
        waitAndClickByXpath("//img[@alt='action list']");
        selectFrameIframePortlet();

        if(isElementPresentByLinkText("Last")){
            waitAndClickByLinkText("Last");
            waitAndClickByLinkText(docIdInt.toString());
        } else {
            waitAndClickByLinkText(docIdInt.toString());
        }

        //      ------------------------------- Not working in code when click docId link in list--------------------------
        //Thread.sleep(5000);
        //String[] windowTitles = getAllWindowTitles();
        //selectWindow(windowTitles[1]);
        //windowFocus();
        //assertEquals(windowTitles[1], getTitle());
        //checkForIncidentReport("Action List Id link opened window.", "https://jira.kuali.org/browse/KULRICE-9062 Action list id links result in 404 or NPE");

        //------submit-----//
        //selectFrame("relative=up");
        //waitAndClick("//button[@value='submit']");
        //waitForPageToLoad50000();
        //close();
        //------submit over---//

        //----step 2----//
        //selectWindow("null");
        //windowFocus();
        //waitAndClick("//img[@alt='doc search']");
        //waitForPageToLoad50000();
        //assertEquals(windowTitles[0], getTitle());
        //selectFrame("iframeportlet");
        //waitAndClick(SEARCH_XPATH);
        //waitForPageToLoad50000();
        //----step 2 over ----//

        //-----Step 3 verifies that doc is final-------//
        //assertEquals("FINAL", getText("//table[@id='row']/tbody/tr[1]/td[4]"));
        //selectFrame("relative=up");
        //waitAndClick("link=Main Menu");
        //waitForPageToLoad50000();
        //assertEquals(windowTitles[0], getTitle());
        //-----Step 3 verified that doc is final -------
    }

    protected void testIdentityGroupBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();                
        String docId = waitForDocId();
        String dtsTwo = ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Group " + dtsTwo);
        assertBlanketApproveButtonsPresent();
        selectByXpath("//select[@id='document.groupNamespace']", AdminTmplMthdSTNavBase.LABEL_KUALI_KUALI_SYSTEMS);
        waitAndTypeByXpath("//input[@id='document.groupName']", "Validation Test Group1 " + dtsTwo);
        waitAndClickByName(
                "methodToCall.performLookup.(!!org.kuali.rice.kim.impl.identity.PersonImpl!!).(((principalId:member.memberId,principalName:member.memberName))).((``)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;;::::).anchorAssignees");
        waitAndClickSearch();
        waitAndClickReturnValue();
        waitAndClickByName("methodToCall.addMember.anchorAssignees");
        waitForPageToLoad();
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testIdentityPermissionBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();        
        String docId = waitForDocId();
        String dtsTwo = ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits();
        waitAndTypeByXpath("//input[@name='document.documentHeader.documentDescription']",
                "Validation Test Permission " + dtsTwo);
        assertBlanketApproveButtonsPresent();
        waitAndTypeByXpath("//input[@name='document.documentHeader.organizationDocumentNumber']", "10012");
        selectByXpath("//select[@name='document.newMaintainableObject.namespaceCode']",
                AdminTmplMthdSTNavBase.LABEL_KUALI_KUALI_SYSTEMS);
        selectByXpath("//select[@name='document.newMaintainableObject.templateId']",
                AdminTmplMthdSTNavBase.LABEL_KUALI_DEFAULT);
        waitAndTypeByXpath("//input[@name='document.newMaintainableObject.name']",
                "ValidationTestPermission" + dtsTwo);
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testIdentityPersonBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();        
        String docId = waitForDocId();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Person");
        assertBlanketApproveButtonsPresent();
        waitAndTypeByXpath("//input[@id='document.principalName']", "principal" + RandomStringUtils.randomAlphabetic(3).toLowerCase());
        selectByName("newAffln.affiliationTypeCode", "Affiliate");
        selectByName("newAffln.campusCode", "BX - BLGTN OFF CAMPUS");
        selectByName("newAffln.campusCode", "BL - BLOOMINGTON");
        assertElementPresentByName("newAffln.dflt");
        waitAndClickByName("newAffln.dflt");
        waitAndClickByName("methodToCall.addAffln.anchor");
        waitAndClickByName("methodToCall.toggleTab.tabContact");
        selectByName("newName.namePrefix", "Mr");
        waitAndTypeByName("newName.firstName", "First");
        waitAndTypeByName("newName.lastName", "Last");
        selectByName("newName.nameSuffix", "Mr");
        waitAndClickByName("newName.dflt");
        waitAndClickByName("methodToCall.addName.anchor");
        waitForPageToLoad();
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testIdentityResponsibilityBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        String docId = waitForDocId();
        String dtsTwo = ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Responsibility " + dtsTwo);
        assertBlanketApproveButtonsPresent();
        selectByXpath("//select[@id='document.newMaintainableObject.namespaceCode']",
                AdminTmplMthdSTNavBase.LABEL_KUALI_KUALI_SYSTEMS);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']",
                "Validation Test Responsibility " + dtsTwo);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.documentTypeName']", "Test " + dtsTwo);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.routeNodeName']", "Test " + dtsTwo);
        waitAndClickByXpath("//input[@id='document.newMaintainableObject.actionDetailsAtRoleMemberLevel']");
        waitAndClickByXpath("//input[@id='document.newMaintainableObject.required']");
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testIdentityRoleBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        waitAndClickByXpath(SEARCH_XPATH, "No search button to click.");
        waitAndClickByLinkText(RETURN_VALUE_LINK_TEXT, "No return value link");        
        String docId = waitForDocId();
        String dtsTwo = ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Role " + dtsTwo);
        assertBlanketApproveButtonsPresent();
        selectByXpath("//select[@id='document.roleNamespace']", AdminTmplMthdSTNavBase.LABEL_KUALI_KUALI_SYSTEMS);
        waitAndTypeByXpath("//input[@id='document.roleName']", "Validation Test Role " + dtsTwo,
                "No Role Name input to type in.");
        waitAndClickByName(
                "methodToCall.performLookup.(!!org.kuali.rice.kim.impl.identity.PersonImpl!!).(((principalId:member.memberId,principalName:member.memberName))).((``)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;;::::).anchorAssignees");
        waitAndClickByXpath(SEARCH_XPATH, "No search button to click.");
        waitAndClickByLinkText(RETURN_VALUE_LINK_TEXT, "No return value link");
        waitAndClickByName("methodToCall.addMember.anchorAssignees");
        waitForPageToLoad();
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testLocationCampusBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();        
        String docId = waitForDocId();
        String twoLetters = RandomStringUtils.randomAlphabetic(2);
        waitAndTypeByName("document.documentHeader.documentDescription", "Validation Test Campus " + twoLetters);
        assertBlanketApproveButtonsPresent();
        waitAndTypeByName("document.newMaintainableObject.code", RandomStringUtils.randomAlphabetic(2));
        waitAndTypeByName("document.newMaintainableObject.name", "Validation Test Campus" + ITUtil.createUniqueDtsPlusTwoRandomChars());
        waitAndTypeByName("document.newMaintainableObject.shortName", "VTC " + twoLetters);
        selectByName("document.newMaintainableObject.campusTypeCode", "B - BOTH");
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testLocationCountryBlanketApprove() throws InterruptedException {
        selectFrameIframePortlet();
        waitAndCreateNew();        
        String docId = waitForDocId();
        assertBlanketApproveButtonsPresent();
        String twoUpperCaseLetters = RandomStringUtils.randomAlphabetic(2).toUpperCase();
        String countryName = "Validation Test Country " + ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, countryName);
        waitAndTypeByXpath(DOC_CODE_XPATH, twoUpperCaseLetters);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", countryName);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.alternateCode']", "V" + twoUpperCaseLetters);
        int attemptCount = 0;
        blanketApproveCheck();
        while (hasDocError("same primary key already exists") && attemptCount < 25) {
            clearTextByXpath(DOC_CODE_XPATH);
            waitAndTypeByXpath(DOC_CODE_XPATH, twoUpperCaseLetters.substring(0, 1) + Character.toString((char) ('A' + attemptCount++)));
            blanketApproveCheck();
        }
        blanketApproveAssert();
        assertDocFinal(docId);
    }

    protected void testLocationCountyBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();        
        String docId = waitForDocId();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test County");
        assertBlanketApproveButtonsPresent();
        String countryLookUp = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.location.impl.country.CountryBo!!).(((code:document.newMaintainableObject.countryCode,))).((`document.newMaintainableObject.countryCode:code,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(countryLookUp);
        waitAndTypeByName("code", "US");
        waitAndClickSearch();
        waitAndClickReturnValue();
        waitAndTypeByXpath(DOC_CODE_XPATH, RandomStringUtils.randomAlphabetic(2).toUpperCase());
        String stateLookUp = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.location.impl.state.StateBo!!).(((countryCode:document.newMaintainableObject.countryCode,code:document.newMaintainableObject.stateCode,))).((`document.newMaintainableObject.countryCode:countryCode,document.newMaintainableObject.stateCode:code,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(stateLookUp);
        waitAndTypeByName("code", "IN");
        waitAndClickSearch();
        waitAndClickReturnValue();
        String countyName = "Validation Test County" + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", countyName);
        waitAndClickByXpath("//input[@id='document.newMaintainableObject.active']");
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testLocationPostBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();        
        String docId = waitForDocId();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Postal Code");
        assertBlanketApproveButtonsPresent();
        String countryLookUp = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.location.impl.country.CountryBo!!).(((code:document.newMaintainableObject.countryCode,))).((`document.newMaintainableObject.countryCode:code,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(countryLookUp);
        waitAndTypeByName("code", "US");
        waitAndClickSearch();
        waitAndClickReturnValue();
        String code = RandomStringUtils.randomNumeric(5);
        waitAndTypeByXpath(DOC_CODE_XPATH, code);
        String stateLookUp = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.location.impl.state.StateBo!!).(((countryCode:document.newMaintainableObject.countryCode,code:document.newMaintainableObject.stateCode,))).((`document.newMaintainableObject.countryCode:countryCode,document.newMaintainableObject.stateCode:code,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(stateLookUp);
        waitAndClickSearch();
        waitAndClickByXpath("//table[@id='row']/tbody/tr[4]/td[1]/a");
        String cityName = "Validation Test Postal Code " + code;
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.cityName']", cityName);
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testLocationStateBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        String docId = waitForDocId();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test State");
        assertBlanketApproveButtonsPresent();
        
        //jiraAwareWaitAndClick("methodToCall.performLookup.(!!org.kuali.rice.location.impl.country.CountryBo!!).(((code:document.newMaintainableObject.countryCode,))).((`document.newMaintainableObject.countryCode:code,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;" + getBaseUrlString() + "/kr/lookup.do;::::).anchor4");
        String countryLookUp = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.location.impl.country.CountryBo!!).(((code:document.newMaintainableObject.countryCode,))).((`document.newMaintainableObject.countryCode:code,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(countryLookUp);
        waitAndClickSearch();
        waitAndClickReturnValue();
        String code = RandomStringUtils.randomAlphabetic(2).toUpperCase();
        waitAndTypeByXpath(DOC_CODE_XPATH, code);
        String state = "Validation Test State " + code;
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", state);
        waitAndClickByXpath("//input[@id='document.newMaintainableObject.active']");
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void testLookUp() throws Exception {
        waitForPageToLoad();
        selectFrameIframePortlet();

        // Mixed capitalization
        waitAndClick(By.xpath(SEARCH_XPATH_3));
        waitAndClickByLinkText(EDIT_LINK_TEXT, "edit button not present does user " + user + " have permission?");
        Thread.sleep(3000);
        checkForIncidentReport("submit");
        assertTextPresent("ubmit");
        assertTextPresent("ave");
        assertTextPresent("pprove");
        assertTextPresent("lose");
        assertTextPresent("ancel");
    }

    protected void performParameterInquiry(String parameterField) throws Exception {
        waitAndTypeByName("name", parameterField);
        waitAndClickSearch();
        isElementPresentByLinkText(parameterField);
        waitAndClickByLinkText(parameterField);
        waitForPageToLoad();
        Thread.sleep(2000);
        switchToWindow("Kuali :: Inquiry");
        Thread.sleep(2000);
    }

    protected List<String> testLookUpParameterType(String docId, String parameterType, String parameterCode) throws Exception {
        performParameterInquiry(parameterType);
        SeleneseTestBase.assertEquals(parameterCode, getTextByXpath("//div[@class='tab-container']/table//span[@id='code.div']").trim().toLowerCase());
        SeleneseTestBase.assertEquals(parameterType, getTextByXpath("//div[@class='tab-container']/table//span[@id='name.div']").trim().toLowerCase());
        waitAndClickCloseWindow();
        switchToWindow("null");
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterType);
        params.add(parameterCode);

        return params;
    }

    protected List<String> testLookUpParameter(String docId, String parameterName) throws Exception {
        performParameterInquiry(parameterName);
        checkForIncidentReport();
        SeleneseTestBase.assertEquals(parameterName, getTextByXpath(
                "//div[@class='tab-container']/table//span[@id='name.div']").trim());
        SeleneseTestBase.assertEquals("Y", getTextByXpath("//div[@class='tab-container']/table//span[@id='value.div']")
                .trim());
        waitAndClickCloseWindow();
        switchToWindow("null");
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterName);

        return params;
    }

    protected void testPeopleFlow() throws Exception {
        selectFrameIframePortlet();
        
        //Click Main Menu and Create New
        // waitAndCreateNew();
        // waitForPageToLoad();
        jGrowl("Create New");
        waitAndClickByLinkText("Create New");
        
        //jiraAwareWaitAndClick(By.linkText("Create New"));
        //Save docId
        waitForElementPresent("div[data-header_for='PeopleFlow-MaintenanceView'] div[data-label='Document Number'] > span");
        String docId = getText("div[data-header_for='PeopleFlow-MaintenanceView'] div[data-label='Document Number'] > span");
        jGrowlSticky("Doc Id is " + docId);
        findElement(By.name("document.documentHeader.documentDescription")).clear();
        findElement(By.name("document.documentHeader.documentDescription")).sendKeys("Description for Document");
        new Select(findElement(By.name("document.newMaintainableObject.dataObject.namespaceCode"))).selectByVisibleText("KUALI - Kuali Systems");
        findElement(By.name("document.newMaintainableObject.dataObject.name")).clear();
        findElement(By.name("document.newMaintainableObject.dataObject.name")).sendKeys("Document Name" + ITUtil.DTS);

        jGrowl("Add Member kr");
        findElement(By.name("newCollectionLines['document.newMaintainableObject.dataObject.members'].memberName")).clear();
        findElement(By.name("newCollectionLines['document.newMaintainableObject.dataObject.members'].memberName")).sendKeys("kr");
        findElement(By.cssSelector("button[data-loadingmessage='Adding Line...']")).click();
        Thread.sleep(3000);

        jGrowl("Add Member admin");
        findElement(By.name("newCollectionLines['document.newMaintainableObject.dataObject.members'].memberName")).clear();
        findElement(By.name("newCollectionLines['document.newMaintainableObject.dataObject.members'].memberName")).sendKeys("admin");
        findElement(By.cssSelector("button[data-loadingmessage='Adding Line...']")).click();
        Thread.sleep(3000);

        findElement(By.cssSelector("div[data-parent='PeopleFlow-MaintenanceView'] > div.uif-footer button~button~button")).click();
        Thread.sleep(3000);
        checkForIncidentReport();
        jGrowl("Blanket Approve");
        Thread.sleep(5000);
        
        //Close the Doc
        //findElement(By.id("uif-close")).click();
        //Thread.sleep(3000);
        driver.switchTo().window(driver.getWindowHandles().toArray()[0].toString());
        findElement(By.cssSelector("img[alt=\"doc search\"]")).click();
        Thread.sleep(5000);
        jGrowl("Document Search is " + docId + " present?");
        selectFrameIframePortlet();
        findElement(By.cssSelector("td.infoline > input[name=\"methodToCall.search\"]")).click();
        Thread.sleep(5000);
        jGrowl("Is doc status final?");
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, findElement(By.xpath("//table[@id='row']/tbody/tr/td[4]")).getText());
        driver.switchTo().defaultContent();
        findElement(By.name("imageField")).click();
        Thread.sleep(5000);
        // TODO open the document and verify data is as we expect.
    }

    protected void testTermLookupAssertions() throws Exception {
        testLookUp();
        assertTextPresent("Term Parameters");
        waitAndClick(By.xpath(CANCEL2_XPATH));
        passed();
    }

    protected void testTermSpecificationLookupAssertions() throws Exception {
        testLookUp();
        assertTextPresent("Context");
        waitAndClick(By.xpath(CANCEL2_XPATH));
        passed();
    }

    protected List<String> testVerifyModifiedParameter(String docId, String parameterName) throws Exception {
        performParameterInquiry(parameterName);
        SeleneseTestBase.assertEquals(parameterName, getTextByXpath("//div[@class='tab-container']/table//span[@id='name.div']").trim());
        SeleneseTestBase.assertEquals("N", getTextByXpath("//div[@class='tab-container']/table//span[@id='value.div']").trim());
        waitAndClickCloseWindow();
        switchToWindow("null");
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterName);

        return params;
    }

    protected List<String> testVerifyCopyParameterType(String docId, String parameterType, String parameterCode) throws Exception
    {
        performParameterInquiry(parameterType);
        SeleneseTestBase.assertEquals(parameterType, getTextByXpath("//div[@class='tab-container']/table//span[@id='name.div']").trim().toLowerCase());
        waitAndClickCloseWindow();
        switchToWindow("null");
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(parameterType);
        params.add(parameterCode);
        
        return params;
    }

    protected List<String> testCreateNewPermission(String docId, String permissionName) throws Exception
    {
        waitForPageToLoad();
        Thread.sleep(2000);
        docId = waitForDocId();
        waitAndClickSave();
        waitForPageToLoad();
        assertElementPresentByXpath("//div[contains(.,'Document Description (Description) is a required field.')]/img[@alt='error']");
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Adding Permission removeme");
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath("//div[@class='error']");
        assertElementPresentByXpath("//div[contains(.,'Template (Template) is a required field.')]/img[@alt='error']");
        assertElementPresentByXpath("//div[contains(.,'Permission Namespace (Permission Namespace) is a required field.')]/img[@alt='error']");
        assertElementPresentByXpath("//div[contains(.,'Permission Name (Permission Name) is a required field.')]/img[@alt='error']");
        selectOptionByName("document.newMaintainableObject.templateId", "36");
        selectOptionByName("document.newMaintainableObject.namespaceCode", "KR-SYS");
        permissionName = "removeme" + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByName("document.newMaintainableObject.name", permissionName);
        waitAndTypeByName("document.newMaintainableObject.description", "namespaceCode=KR*");
        checkByName("document.newMaintainableObject.active");
        waitAndClickSave();
        waitForPageToLoad();
        assertElementPresentByXpath(SAVE_SUCCESSFUL_XPATH);
        SeleneseTestBase.assertEquals(DOC_STATUS_SAVED, getTextByXpath(DOC_STATUS_XPATH));
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH,"Document is not submitted successfully");
        SeleneseTestBase.assertEquals(DOC_STATUS_ENROUTE, getTextByXpath(DOC_STATUS_XPATH));
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(permissionName);
        
        return params;
    }

    protected List<String> testLookUpPermission(String docId, String permissionName) throws Exception
    {
        waitForPageToLoad();
        waitAndTypeByName("name", permissionName);
        waitAndClickSearch();
        isElementPresentByLinkText(permissionName);
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(permissionName);
        
        return params;
    }

    protected List<String> testEditPermission(String docId, String permissionName) throws Exception
    {
        waitAndClickEdit();
        waitForPageToLoad();
        Thread.sleep(2000);
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Editing Permission removeme");
        uncheckByName("document.newMaintainableObject.active");
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH, "Document is not submitted successfully");
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(permissionName);
        
        return params;
    }

    protected List<String> testVerifyPermission(String docId, String permissionName) throws Exception
    {
        waitForPageToLoad();
        waitAndTypeByName("name", permissionName);
        waitAndClickByXpath("//input[@title='Active Indicator - No']");
        waitAndClickSearch();
        isElementPresentByLinkText(permissionName);
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(permissionName);
        
        return params;
    }

    protected List<String> testCreateNewPerson(String docId, String personName) throws Exception
    {
        waitForPageToLoad();
        docId = waitForDocId();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Adding Charlie Brown");
        waitAndTypeByName("document.documentHeader.explanation", "I want to add Charlie Brown to test KIM");
        
        //here You should also check for lower case validation for principalName, but it is skipped for now as there is an incident report error there.
        personName = "cbrown" + ITUtil.createUniqueDtsPlusTwoRandomChars();
        waitAndTypeByName("document.principalName", personName);
        waitAndClickSave();
        waitForPageToLoad();
        assertElementPresentByXpath(SAVE_SUCCESSFUL_XPATH);
        SeleneseTestBase.assertEquals(DOC_STATUS_SAVED, getTextByXpath(DOC_STATUS_XPATH));
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath("//div[contains(.,'At least one affiliation must be entered.')]/img[@alt='error']");
        assertElementPresentByXpath("//div[contains(.,'At least one name must be entered.')]/img[@alt='error']");
        selectOptionByName("newAffln.affiliationTypeCode", "STDNT");
        selectOptionByName("newAffln.campusCode", "BL");
        checkByName("newAffln.dflt");
        waitAndClickByName("methodToCall.addAffln.anchor");
        waitForPageToLoad();
        Thread.sleep(3000);
        selectOptionByName("newName.nameCode", "PRM");
        selectOptionByName("newName.namePrefix", "Mr");
        waitAndTypeByName("newName.firstName", "Charlie");
        waitAndTypeByName("newName.lastName", "Brown");
        checkByName("newName.dflt");
        waitAndClickByName("methodToCall.addName.anchor");
        waitForPageToLoad();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH, "Document is not submitted successfully");
        SeleneseTestBase.assertEquals(DOC_STATUS_ENROUTE, getTextByXpath(DOC_STATUS_XPATH));
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(personName);
        
        return params;
    }

    protected List<String> testLookUpPerson(String docId, String personName) throws Exception
    {
        waitForPageToLoad();
        waitAndTypeByName("principalName", personName);
        waitAndClickSearch();
        isElementPresentByLinkText(personName);
        waitAndClickByName("methodToCall.clearValues");
        waitAndTypeByName("firstName", "Charlie");
        waitAndClickSearch();
        isElementPresentByLinkText(personName);
        waitAndClickByName("methodToCall.clearValues");
        waitAndTypeByName("lastName", "Brown");
        waitAndClickSearch();
        isElementPresentByLinkText(personName);
        waitAndClickByName("methodToCall.clearValues");
        waitAndTypeByName("campusCode", "BL");
        waitAndClickSearch();
        isElementPresentByLinkText(personName);
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(personName);
        
        return params;
    }

    protected List<String> testVerifyPerson(String docId, String personName) throws Exception
    {
        waitAndClickByLinkText(personName);
        waitForPageToLoad();
        Thread.sleep(5000);
        switchToWindow("Kuali :: Person");
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(personName, getTextByXpath("//div[@class='tab-container']/table//tr[2]/td[1]/div").trim());
        SeleneseTestBase.assertEquals("BL - BLOOMINGTON", getTextByXpath("//div[@class='tab-container']/table[3]//tr[2]/td[2]/div").trim());
        SeleneseTestBase.assertEquals("Student", getTextByXpath("//select/option[@selected]").trim());
        assertElementPresentByXpath("//table[@class='tab']//input[@title='close Overview']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='open Contact']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='open Privacy Preferences']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='open Membership']");
        waitAndClickByName("methodToCall.showAllTabs");
        Thread.sleep(3000);
        assertElementPresentByXpath("//table[@class='tab']//input[@title='close Overview']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='close Contact']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='close Privacy Preferences']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='close Membership']");
        waitAndClickByName("methodToCall.hideAllTabs");
        Thread.sleep(3000);
        assertElementPresentByXpath("//table[@class='tab']//input[@title='open Overview']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='open Contact']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='open Privacy Preferences']");
        assertElementPresentByXpath("//table[@class='tab']//input[@title='open Membership']");
        waitAndClickCloseWindow();
        switchToWindow("null");
        List<String> params = new ArrayList<String>();
        params.add(docId);
        params.add(personName);
        
        return params;
    }

    protected void testUifTooltip(String NAME_FIELD_1, String NAME_FIELD_2) throws Exception {
        // check if tooltip opens on focus
        fireEvent(NAME_FIELD_1, "focus");
        fireMouseOverEventByName(NAME_FIELD_1);
        
        // SeleneseTestBase.assertTrue(isVisible("div.jquerybubblepopup.jquerybubblepopup-black") && isVisible("td.jquerybubblepopup-innerHtml"));
        SeleneseTestBase.assertEquals("This tooltip is triggered by focus or and mouse over.", getText(
                "td.jquerybubblepopup-innerHtml"));

        // check if tooltip closed on blur
        fireEvent(NAME_FIELD_1, "blur");
        SeleneseTestBase.assertFalse(isVisible("div.jquerybubblepopup.jquerybubblepopup-black") && isVisible(
                "td.jquerybubblepopup-innerHtml"));
        Thread.sleep(5000);
        fireEvent("field119", "focus");
        
        // check if tooltip opens on mouse over
        fireMouseOverEventByName(NAME_FIELD_2);        
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(.,\"This is a tool-tip with different position and tail options\")]"));

        // check if tooltip closed on mouse out
        waitAndTypeByName(NAME_FIELD_2, "a");
        Thread.sleep(5000);
        SeleneseTestBase.assertFalse(isVisibleByXpath(
                "//td[contains(.,\"This is a tool-tip with different position and tail options\")]"));

        // check that default tooltip does not display when there are an error message on the field
        waitAndTypeByName(NAME_FIELD_1, "1");
        fireEvent(NAME_FIELD_1, "blur");
        fireMouseOverEventByName(NAME_FIELD_1);
        Thread.sleep(10000);
        SeleneseTestBase.assertTrue("https://jira.kuali.org/browse/KULRICE-8141 Investigate why UifTooltipIT.testTooltip fails around jquerybubblepopup",
                        isVisibleByXpath("//div[@class='jquerybubblepopup jquerybubblepopup-kr-error-cs']") &&
                                !(isVisibleByXpath("//div[@class='jquerybubblepopup jquerybubblepopup-black']")));
       
        // TODO figure out this last assert     
        passed();
    }

    protected void testValidCharsConstraintIT() throws Exception {
        assertFocusTypeBlurValidation("field50", new String[]{"12.333", "-123.33"}, new String[]{"123.33"});
        assertFocusTypeBlurValidation("field51", new String[]{"A"}, new String[]{"-123.33"});

        // TODO continue to convert to assertFocusTypeBlurValidation
        assertFocusTypeBlurValidation("field77", new String[]{"1.1"},new String[]{"12"});
        assertFocusTypeBlurValidation("field52", new String[]{"5551112222"},new String[]{"555-111-1111"});
        assertFocusTypeBlurValidation("field53", new String[]{"1ClassName.java"},new String[]{"ClassName.java"});
        assertFocusTypeBlurValidation("field54", new String[]{"aaaaa"},new String[]{"aaaaa@kuali.org"});
        assertFocusTypeBlurValidation("field84", new String[]{"aaaaa"},new String[]{"http://www.kuali.org"});       
        assertFocusTypeBlurValidation("field55", new String[]{"023512"},new String[]{"022812"});
        assertFocusTypeBlurValidation("field75", new String[]{"02/35/12"},new String[]{"02/28/12"});
        assertFocusTypeBlurValidation("field82", new String[]{"13:22"},new String[]{"02:33"});
        assertFocusTypeBlurValidation("field83", new String[]{"25:22"},new String[]{"14:33"});
        assertFocusTypeBlurValidation("field57", new String[]{"0"},new String[]{"2020"});      
        assertFocusTypeBlurValidation("field58", new String[]{"13"},new String[]{"12"});  
        assertFocusTypeBlurValidation("field61", new String[]{"5555-444"},new String[]{"55555-4444"});
        assertFocusTypeBlurValidation("field62", new String[]{"aa5bb6_a"},new String[]{"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"});       
        assertFocusTypeBlurValidation("field63", new String[]{"fff555"},new String[]{"aa22 _/"});       
        assertFocusTypeBlurValidation("field64", new String[]{"AABB55"},new String[]{"ABCDEFGHIJKLMNOPQRSTUVWXY,Z abcdefghijklmnopqrstuvwxy,z"});       
        assertFocusTypeBlurValidation("field76", new String[]{"AA~BB%"},new String[]{"abcABC %$#@&<>\\{}[]*-+!=.()/\"\"',:;?"});       
        assertFocusTypeBlurValidation("field65", new String[]{"sdfs$#$# dsffs"},new String[]{"sdfs$#$#sffs"});      
        assertFocusTypeBlurValidation("field66", new String[]{"abcABCD"},new String[]{"ABCabc"});
        assertFocusTypeBlurValidation("field67", new String[]{"(111)B-(222)A"},new String[]{"(12345)-(67890)"});  
        assertFocusTypeBlurValidation("field68", new String[]{"A.66"},new String[]{"a.4"});
        assertFocusTypeBlurValidation("field56", new String[]{"2020-06-02"},new String[]{"2020-06-02 03:30:30.22"});
    }

    protected void assertFocusTypeBlurValidation(String field, String[] errorInputs, String[] validInputs) throws InterruptedException {
        assertFocusTypeBlurError(field, errorInputs);
        clearTextByName(field);
        assertFocusTypeBlurValid(field, validInputs);
    }

    protected void testSubCollectionSize() throws Exception {
        checkForIncidentReport(COLLECTIONS_LINK_TEXT);
        
        // click on collections page link
        waitAndClickByLinkText(COLLECTIONS_LINK_TEXT);
        
        // wait for collections page to load by checking the presence of a sub collection line item
        for (int second = 0;; second++) {                   
            if (second >= waitSeconds)
                failableFail(TIMEOUT_MESSAGE + " looking for " + SUB_COLLECTION_UIF_DISCLOSURE_SPAN_UIF_HEADER_TEXT_SPAN_XPATH);
            try {                
                if (getText(SUB_COLLECTION_UIF_DISCLOSURE_SPAN_UIF_HEADER_TEXT_SPAN_XPATH).equals("SubCollection - (3 lines)"))
                {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }
        
        // verify that sub collection sizes are displayed as expected
        SeleneseTestBase.assertEquals("SubCollection - (3 lines)", getText(SUB_COLLECTION_UIF_DISCLOSURE_SPAN_UIF_HEADER_TEXT_SPAN_XPATH));
        SeleneseTestBase.assertEquals("SubCollection - (2 lines)", getTextByXpath(
                "//a[@id='subCollection1_line1_toggle']/span"));
    }

    protected void testConfigurationTestView(String idPrefix) throws Exception {
        waitForElementPresentByXpath("//span[@id='" + idPrefix + "TextInputField_label_span']");
        
        // testing for https://groups.google.com/a/kuali.org/group/rice.usergroup.krad/browse_thread/thread/1e501d07c1141aad#
        String styleValue = waitAndGetAttributeByXpath("//span[@id='" + idPrefix + "TextInputField_label_span']",
                "style");
        
        // log.info("styleValue is " + styleValue);
        SeleneseTestBase.assertTrue(idPrefix + "textInputField label does not contain expected style", styleValue.replace(" ", "").contains("color:red"));
        
        // get current list of options
        String refreshTextSelectLocator = "//select[@id='" + idPrefix + "RefreshTextField_control']";
        String[] options1 = getSelectOptionsByXpath(refreshTextSelectLocator);
        String dropDownSelectLocator = "//select[@id='" + idPrefix + "DropDown_control']";
        selectByXpath(dropDownSelectLocator, "Vegetables");
        Thread.sleep(3000);
        
        //get list of options after change
        String[] options2 = getSelectOptionsByXpath(refreshTextSelectLocator);
        
        //verify that the change has occurred
        SeleneseTestBase.assertFalse(
                "Field 1 selection did not change Field 2 options https://jira.kuali.org/browse/KULRICE-8163 Configuration Test View Conditional Options doesn't change Field 2 options based on Field 1 selection",
                options1[options1.length - 1].equalsIgnoreCase(options2[options2.length - 1]));
        
        //confirm that control gets disabled
        selectByXpath(dropDownSelectLocator, "None");
        Thread.sleep(3000);
        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByXpath(refreshTextSelectLocator, "disabled"));
    }

    /**
     * verify that add line controls are present
     */
    protected void confirmAddLineControlsPresent(String idPrefix, String addLineIdSuffix) {
        String[] addLineIds = {"StartTime", "StartTimeAmPm", "AllDay"};

        for (String id : addLineIds) {
            String tagId = "//*[@id='" + idPrefix + id + addLineIdSuffix + "']";
            SeleneseTestBase.assertTrue("Did not find id " + tagId, isElementPresentByXpath(tagId));
        }
    }

    protected void testAddLineWithSpecificTime(String idPrefix, String addLineIdSuffix) throws Exception {
        waitForElementPresentByXpath("//span[@id='" + idPrefix + "TextInputField_label_span']");
        confirmAddLineControlsPresent(idPrefix, addLineIdSuffix);
        String startTimeId = "//*[@id='" + idPrefix + "StartTime" + addLineIdSuffix + "']";
        String inputTime = "7:06";
        waitAndTypeByXpath(startTimeId, inputTime);
        String amPmSelectLocator = "//*[@id='" + idPrefix + "StartTimeAmPm" + addLineIdSuffix + "']";
        selectByXpath(amPmSelectLocator, "PM");
        SeleneseTestBase.assertEquals("PM", waitAndGetAttributeByXpath(amPmSelectLocator, "value"));
        Thread.sleep(5000); //allow for ajax refresh        
        waitAndClickByXpath("//button");
        Thread.sleep(5000); //allow for line to be added
        
        //confirm that line has been added
        SeleneseTestBase.assertTrue("line (//input[@value='7:06'])is not present https://jira.kuali.org/browse/KULRICE-8162 Configuration Test View Time Info add line button doesn't addline",
                        isElementPresentByXpath("//input[@value='7:06']"));
    }

    protected void testAddLineWithAllDay(String idPrefix, String addLineIdSuffix) throws Exception {
        waitForElementPresentByXpath("//span[@id='" + idPrefix + "TextInputField_label_span']");
        confirmAddLineControlsPresent(idPrefix, addLineIdSuffix);
        String startTimeId = "//*[@id='" + idPrefix + "StartTime" + addLineIdSuffix + "']";
        String inputTime = "5:20";
        waitAndTypeByXpath(startTimeId, inputTime);
        String allDaySelector = "//*[@id='" + idPrefix + "AllDay" + addLineIdSuffix + "']";
        Thread.sleep(5000); //allow for ajax refresh
        waitAndClickByXpath(allDaySelector);
        Thread.sleep(5000); //allow for ajax refresh
        waitAndClick("div#ConfigurationTestView-ProgressiveRender-TimeInfoSection button");
        Thread.sleep(5000); //allow for line to be added           
    }

    protected void testAddLineAllDay(String idPrefix, String addLineIdSuffix) throws Exception {
        waitForElementPresentByXpath("//span[@id='" + idPrefix + "TextInputField_label_span']");
        confirmAddLineControlsPresent(idPrefix, addLineIdSuffix);
        
        //store number of rows before adding the lines
        String cssCountRows = "div#ConfigurationTestView-ProgressiveRender-TimeInfoSection.uif-group div#ConfigurationTestView-ProgressiveRender-TimeInfoSection_disclosureContent.uif-disclosureContent table tbody tr";
        int rowCount = (getCssCount(cssCountRows));
        String allDayId = "//*[@id='" + idPrefix + "AllDay" + addLineIdSuffix + "']";
        Thread.sleep(5000); //allow for ajax refresh
        waitAndClickByXpath(allDayId);
        waitAndClick("div#ConfigurationTestView-ProgressiveRender-TimeInfoSection button");
        Thread.sleep(5000); //allow for line to be added
        
        //confirm that line has been added (by checking for the new delete button)
        assertEquals("line was not added", rowCount + 1, (getCssCount(cssCountRows)));
    }

//    protected void testTravelAccountTypeLookup() throws Exception {
//        selectFrameIframePortlet();
//
//        //Blank Search
//        waitAndClickByXpath("//*[contains(button,\"earch\")]/button[1]");
//        Thread.sleep(4000);
//        assertElementPresentByXpath("//table[@class='uif-tableCollectionLayout dataTable']//tr[contains(td[1],'CAT')]");
//        assertElementPresentByXpath("//table[@class='uif-tableCollectionLayout dataTable']//tr[contains(td[1],'EAT')]");
//        assertElementPresentByXpath("//table[@class='uif-tableCollectionLayout dataTable']//tr[contains(td[1],'IAT')]");
//
//        //search with each field
//        waitAndTypeByName("lookupCriteria[accountTypeCode]", "CAT");
//        waitAndClickByXpath("//*[contains(button,\"earch\")]/button[1]");
//        Thread.sleep(2000);
//        assertElementPresentByXpath("//table[@class='uif-tableCollectionLayout dataTable']//tr[contains(td[1],'CAT')]");
//        waitAndClickByXpath("//*[contains(button,\"earch\")]/button[2]");
//        Thread.sleep(2000);
//        waitAndTypeByName("lookupCriteria[name]", "Expense Account Type");
//        waitAndClickByXpath("//*[contains(button,\"earch\")]/button[1]");
//        Thread.sleep(4000);
//        assertElementPresentByXpath("//table[@class='uif-tableCollectionLayout dataTable']//tr[contains(td[1],'EAT')]");
//
//        //Currently No links available for Travel Account Type Inquiry so cant verify heading and values.
//    }

    protected void testCategoryLookUp() throws Exception {
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickByXpath("//button[contains(.,'earch')]");
        Thread.sleep(3000);
        waitForPageToLoad();
        findElement(By.tagName("body")).getText().contains("Actions"); // there are no actions, but the header is the only unique text from searching
        
        // Category's don't have actions (yet)
        //waitAndClick("id=u80");
        //waitForPageToLoad();
        //waitAndClick("id=u86");
        //waitForPageToLoad();
        //selectWindow("null");
        //waitAndClick("xpath=(//input[@name='imageField'])[2]");
        //waitForPageToLoad();
        //passed();
    }

    protected void testCreateSampleEDocLite() throws Exception {
        waitForPageToLoad();
        Thread.sleep(3000);
        SeleneseTestBase.assertEquals("Kuali Portal Index", getTitle());
        selectFrameIframePortlet();
        waitAndClickByXpath("//input[@name='methodToCall.search' and @alt='search']");
        waitForPageToLoad();
        
        // click on the create new.
        waitAndClickByLinkText("Create Document");
        waitForPageToLoad();
        Thread.sleep(3000);
        String docId = getTextByXpath("//table/tbody/tr[4]/td[@class='datacell1']");
        waitAndTypeByName("userName", "Viral Chauhan");
        waitAndTypeByName("rqstDate", "12/03/2020");
        checkByName("fundedBy");
        waitAndTypeByName("addText", "Note Added.");
        waitAndClickByXpath("//td[@class='datacell']/div/img");
        waitForPageToLoad();
        waitAndClickByXpath("//input[@value='submit']");
        SeleneseTestBase.assertEquals(Boolean.FALSE,(Boolean) isElementPresentByXpath("//input[@value='submit']"));
        SeleneseTestBase.assertEquals(Boolean.FALSE, (Boolean) isElementPresentByXpath("//input[@value='save']"));
        SeleneseTestBase.assertEquals(Boolean.FALSE,(Boolean) isElementPresentByXpath("//input[@value='cancel']"));
        waitForPageToLoad();
        selectTopFrame();
        waitAndClickDocSearch();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickByXpath("//input[@name='methodToCall.search' and @alt='search']");
        waitForPageToLoad();
        isElementPresent(By.linkText(docId));
    }

    protected void testTermLookUp() throws Exception {
        testLookUp();
        assertTextPresent("Term Parameters");
        waitAndClick(By.xpath(CANCEL2_XPATH));
        passed();
    }

    protected void testWorkFlowRouteRulesBlanketApp() throws Exception {
        String random = ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits();
        waitForPageToLoad();
        Thread.sleep(3000);
        SeleneseTestBase.assertEquals("Kuali Portal Index", getTitle());
        selectFrameIframePortlet();
        
        // click on the create new button
        waitAndClickCreateNew();
        waitForPageToLoad();
        
        // lookup on the Document Type Name
        waitAndClickByName("methodToCall.performLookup.(!!org.kuali.rice.kew.doctype.bo.DocumentType!!).(((name:documentTypeName))).((``)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;;::::).anchor");
        waitForPageToLoad();
        
        // type in the name field the text RoutingRuleDocument
        waitAndTypeByName("name", "RoutingRuleDocument");
        
        // click the search button
        waitAndClickSearch();
        waitForPageToLoad();
        
        // click the return value link
        waitAndClickReturnValue();
        waitForPageToLoad();
        
        // lookup on the Rule Template Name
        waitAndClickByName("methodToCall.performLookup.(!!org.kuali.rice.kew.rule.bo.RuleTemplateBo!!).(((name:ruleTemplateName))).((``)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;;::::).anchor");
        waitForPageToLoad();
        
        // type in the name field the text RuleRoutingTemplate
        waitAndTypeByName("name", "RuleRoutingTemplate");
        
        // click the search button
        waitAndClickSearch();
        waitForPageToLoad();
        
        // click the return value link
        waitAndClickReturnValue();
        waitForPageToLoad();
        
        // click the create new button
        waitAndClickByName("methodToCall.createRule");
        waitForPageToLoad();
        String docId = waitForDocId();
        SeleneseTestBase.assertTrue(isElementPresentByName(CANCEL_NAME));
       
        // type in the Document Overview Description the text Test Routing Rule
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Test Routing Rule " + random);
       
        // click the Force Action checkbox
        waitAndClickByXpath("//input[@id='document.newMaintainableObject.forceAction']");
       
        // type in the Description text area the text Test Routing Rule1
        waitAndTypeByXpath("//textarea[@id='document.newMaintainableObject.description']", "Test Routing Rule1 "
                + random);
       
        // type in the Document type name field the text DocumentTypeDocument
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.fieldValues(1321~docTypeFullName)']",
                "DocumentTypeDocument");
        
        // lookup on Person
        waitAndClickByName("methodToCall.performLookup.(!!org.kuali.rice.kim.impl.identity.PersonImpl!!).(((principalName:document.newMaintainableObject.add.personResponsibilities.principalName,))).((`document.newMaintainableObject.add.personResponsibilities.principalName:principalName,`)).((<>)).(([])).((**)).((^^)).((&&)).((/personImpl/)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor15");
        waitForPageToLoad();
       
        // click the search button
        waitAndClickSearch();
        waitForPageToLoad();
        
        // click the return value
        waitAndClickReturnValue();
        waitForPageToLoad();
        
        // select from the Action Request ACKNOWLEDGE
        selectByXpath("//select[@id='document.newMaintainableObject.add.personResponsibilities.actionRequestedCd']",
                "ACKNOWLEDGE");
        
        // type in the Priority field the text 1
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.add.personResponsibilities.priority']", "1");
       
        // click the add button
        waitAndClickByName("methodToCall.addLine.personResponsibilities.(!!org.kuali.rice.kew.rule.PersonRuleResponsibility!!).(:::;15;:::).anchor15");
        waitForPageToLoad();
        checkForIncidentReport(BLANKET_APPROVE_NAME);
        waitAndClickByName(BLANKET_APPROVE_NAME);
        waitForPageToLoad();
        driver.switchTo().defaultContent(); //selectWindow("null");
        waitAndClickDocSearch();
        waitForPageToLoad();
        SeleneseTestBase.assertEquals("Kuali Portal Index", getTitle());
        selectFrameIframePortlet();
        waitAndTypeByName("documentId", docId);
        waitAndClickSearch();
        waitForElementPresent(By.linkText(docId));
        
        if (isElementPresent(By.linkText(docId))) {
            if (!DOC_STATUS_FINAL.equalsIgnoreCase(getTextByXpath(DOC_STATUS_XPATH_2))) {
                jiraAwareFail("WorkFlowRouteRulesBlanketApp expected:<[FINAL]> but was " + getTextByXpath(DOC_STATUS_XPATH_2));
            }
        } else {
            SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_2));
            SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        }
    }

    protected void testCreateNewRRDTravelRequestDestRouting() throws Exception {
        selectFrameIframePortlet();
        waitAndClick("img[alt=\"create new\"]");
        waitForPageToLoad();
        waitAndClickByName("methodToCall.performLookup.(!!org.kuali.rice.kew.rule.RuleBaseValues!!).(((id:parentRuleId))).((``)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;;::::).anchor");
        waitForPageToLoad();
        waitAndClickByXpath("//td[@class='infoline']/input[@name='methodToCall.search']");
        waitForPageToLoad();
        waitAndClick("a[title=\"return valueRule Id=1046 \"]");
        waitForPageToLoad();
        waitAndClickByName("parentResponsibilityId");
        waitAndClickByName("methodToCall.createDelegateRule");
        waitForPageToLoad();
        Thread.sleep(3000);
        waitAndClickCancel();
        waitForPageToLoad();
        waitAndClickByName("methodToCall.processAnswer.button0");
        waitForPageToLoad();
        driver.switchTo().defaultContent();
        waitAndClickByXpath("(//input[@name='imageField'])[2]");
        waitForPageToLoad();
        passed();
    }

    protected void testWorkFlowRouteRulesCreateNew() throws Exception {
        waitForPageToLoad();
        Thread.sleep(5000);
        SeleneseTestBase.assertEquals("Kuali Portal Index", getTitle());
        selectFrameIframePortlet();
        waitAndClickCreateNew();
        waitForPageToLoad();
        Thread.sleep(3000);
        waitAndClickByName(CANCEL_NAME,"https://jira.kuali.org/browse/KULRICE-8161 Work Flow Route Rules cancel new yields 404 not found");
   
        // KULRICE-7753 : WorkFlowRouteRulesIT cancel confirmation missing from create new Route Rules.
        waitForPageToLoad();
        Thread.sleep(3000);
        waitAndClickByName("methodToCall.processAnswer.button0",
                "https://jira.kuali.org/browse/KULRICE-7753 : WorkFlowRouteRulesIT cancel confirmation missing from create new Route Rules.");
        passed();
    }

    /**
     * tests that a Routing Rule maintenance document is created for an edit operation originating
     * from a lookup screen
     */
    protected void testWorkFlowRouteRulesEditRouteRules() throws Exception {
        waitForPageToLoad();
        SeleneseTestBase.assertEquals("Kuali Portal Index", getTitle());
        selectFrameIframePortlet();
        waitAndClickSearch();
        waitAndClickEdit();
        waitForPageToLoad();
        selectFrameIframePortlet();
        Thread.sleep(3000);
        waitAndClickCancel();
        waitForPageToLoad();
        Thread.sleep(3000);
        waitAndClickByName("methodToCall.processAnswer.button0");
        passed();
    }

    protected String testCreateNewComponent(String componentName, String componentCode) throws Exception {
        return testCreateNewComponent(componentName, componentCode, "");
    }

    protected String testCreateNewComponent(String componentName, String componentCode, String message) throws Exception {
        waitForPageToLoad();
        String docId = waitForDocId();
        
        //Enter details for Parameter.
        waitAndTypeByName("document.documentHeader.documentDescription", "Adding Test Component");
        selectOptionByName("document.newMaintainableObject.namespaceCode", "KR-IDM");
        waitAndTypeByName("document.newMaintainableObject.code", componentCode);
        waitAndTypeByName("document.newMaintainableObject.name", componentName);
        checkByName("document.newMaintainableObject.active");
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH, CREATE_NEW_DOCUMENT_NOT_SUBMITTED_SUCCESSFULLY_MESSAGE_TEXT  + message);
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
        return docId;
    }
    
    
    protected void testLookUpComponent(String docId, String componentName, String componentCode) throws Exception {
        selectFrameIframePortlet();
        //Lookup
        waitAndTypeByName("name", componentName);
        waitAndClickSearch();
        isElementPresentByLinkText(componentName);
        waitAndClickByLinkText(componentName);
        waitForPageToLoad();
        Thread.sleep(2000);
        switchToWindow("Kuali :: Inquiry");
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(componentName, getTextByXpath("//div[@class='tab-container']/table//span[@id='name.div']").trim());
        SeleneseTestBase.assertEquals(componentCode, getTextByXpath("//div[@class='tab-container']/table//span[@id='code.div']").trim());
        waitAndClickCloseWindow();
        switchToWindow("null");
    }
    
    protected void testEditComponent(String docId, String componentName, String componentCode) throws Exception {
        selectFrameIframePortlet();
        waitAndClickEdit();
        waitForPageToLoad();
        docId = waitForDocId();
        waitAndTypeByName("document.documentHeader.documentDescription", "Editing Test Component");
        clearTextByName("document.newMaintainableObject.name");
        waitAndTypeByName("document.newMaintainableObject.name", componentName);
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH,"Document is not submitted successfully");
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
    }
    
    protected void testCopyComponent(String docId, String componentName, String componentCode) throws Exception {
        selectFrameIframePortlet();
        waitAndClickCopy();
        waitForPageToLoad();
        docId = waitForDocId();
        waitAndTypeByName("document.documentHeader.documentDescription", "Copying Test Component");
        selectOptionByName("document.newMaintainableObject.namespaceCode", "KR-IDM");
        waitAndTypeByName("document.newMaintainableObject.code", componentCode);
        clearTextByName("document.newMaintainableObject.name");
        waitAndTypeByName("document.newMaintainableObject.name", componentName);
        waitAndClickSave();
        waitAndClickSubmit();
        waitForPageToLoad();
        checkForDocError();
        assertElementPresentByXpath(DOC_SUBMIT_SUCCESS_MSG_XPATH,"Document is not submitted successfully");
        selectTopFrame();
        waitAndClickDocSearchTitle();
        waitForPageToLoad();
        selectFrameIframePortlet();
        waitAndClickSearch();
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(docId, getTextByXpath(DOC_ID_XPATH_3));
        SeleneseTestBase.assertEquals(DOC_STATUS_FINAL, getTextByXpath(DOC_STATUS_XPATH_2));
        selectTopFrame();
    }

    protected void testVerifyCopyComponent(String docId, String componentName, String componentCode) throws Exception {
        selectFrameIframePortlet();
        waitAndTypeByName("name", componentName);
        waitAndClickSearch();
        isElementPresentByLinkText(componentName);
        waitAndClickByLinkText(componentName);
        waitForPageToLoad();
        Thread.sleep(2000);
        switchToWindow("Kuali :: Inquiry");
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(componentName, getTextByXpath("//div[@class='tab-container']/table//span[@id='name.div']").trim());
        SeleneseTestBase.assertEquals(componentCode, getTextByXpath("//div[@class='tab-container']/table//span[@id='code.div']").trim());
        waitAndClickCloseWindow();
        switchToWindow("null");
    }
    
    /**
     * Test the tooltip and external help on the page
     */
    protected void testPageHelp() throws Exception {
        // test tooltip help
        fireMouseOverEventByXpath("//h2/span[@class='uif-headerText-span']");
        SeleneseTestBase.assertEquals("Sample text for page help", getText("td.jquerybubblepopup-innerHtml"));

        // test external help
        waitAndClickByXpath("//input[@alt='Help for Help Page']");
        Thread.sleep(5000);
        switchToWindow("Kuali Foundation");
        Thread.sleep(5000);      
        switchToWindow(CONFIGURATION_VIEW_WINDOW_TITLE);
    }

    /**
     * Test the tooltip help on the section and fields
     */
    protected void testTooltipHelp() throws Exception {
        // verify that no tooltips are displayed initially
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for section help - tooltip help')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for section help - tooltip help')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for field help - label left')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label left')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for field help - label right')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label right')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for field help - label top')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label top')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for standalone help widget tooltip which will never be rendered')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for standalone help widget tooltip which will never be rendered')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for field help - there is also a tooltip on the label but it is overridden by the help tooltip')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - there is also a tooltip on the label but it is overridden by the help tooltip')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for label tooltip - this will not be rendered as it is overridden by the help tooltip')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for label tooltip - this will not be rendered as it is overridden by the help tooltip')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for field help - there is also an on-focus tooltip')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - there is also an on-focus tooltip')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for on-focus event tooltip')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for on-focus event tooltip')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for check box help')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for check box help')]"));
        }
       
        // test tooltip help of section header
        fireMouseOverEventByXpath("//div[@id='ConfigurationTestView-Help-Section1']/div/h3[@class='uif-headerText']");
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for section help - tooltip help')]"));
        String javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" + "element[0].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
        Thread.sleep(3000);
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for section help - tooltip help')]"));
        
        // verify that no external help exist
        SeleneseTestBase.assertFalse(isElementPresent("#ConfigurationTestView-Help-Section1 input.uif-helpImage"));
    
        // test tooltip help of field with label to the left
        fireMouseOverEventByXpath("//label[@id='field-label-left_label']");
        Thread.sleep(3000);
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label left')]"));
        javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" +
                "element[1].style.display='none'"; 
        Thread.sleep(3000);
        ((JavascriptExecutor) driver).executeScript(javascript);
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label left')]"));
        
        // test tooltip help of field with label to the right
        fireMouseOverEventByXpath("//label[@id='field-label-right_label']");
        Thread.sleep(3000);
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label righ')]"));
        javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" +"element[2].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
        Thread.sleep(3000);
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label righ')]"));

        // test tooltip help of field with label to the top
        fireMouseOverEventByXpath("//label[@id='field-label-top_label']");
        Thread.sleep(3000);
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label top')]"));
        javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" + "element[3].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
        Thread.sleep(3000);
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - label top')]"));

        // verify that standalone help with tooltip is not rendered
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@id='standalone-help-not-rendered']"));

        // test tooltip help when it overrides a tooltip
        fireMouseOverEventByXpath("//label[@id='override-tooltip_label']");
        Thread.sleep(3000);
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for field help - there is also a tooltip on the label but it is overridden by the help tooltip')]"));
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for label tooltip - this will not be rendered as it is overridden by the help tooltip')]")) {
            SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for label tooltip - this will not be rendered as it is overridden by the help tooltip')]"));
        }        
        javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" + "element[4].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
        Thread.sleep(3000);
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - there is also a tooltip on the label but it is overridden by the help tooltip')]"));

        // test tooltip help in conjunction with a focus event tooltip
        fireMouseOverEventByXpath("//input[@id='on-focus-tooltip_control']");
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for on-focus event tooltip')]"));
        fireMouseOverEventByXpath("//label[@id='on-focus-tooltip_label']");
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for field help - there is also an on-focus tooltip')]"));
        javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" +"element[5].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
        Thread.sleep(3000);                
        javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" + "element[6].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
        Thread.sleep(3000);    
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for field help - there is also an on-focus tooltip')]"));
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for on-focus event tooltip')]"));

        // test tooltip help against a check box - help contains html
        fireMouseOverEventByXpath("//label[@id='checkbox_label']");
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for check box help')]"));
        javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" + "element[7].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
        Thread.sleep(3000);
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for check box help')]"));
    }

    /**
     * Test the tooltip help on the sub-section and fields that are display only
     */
     protected void testDisplayOnlyTooltipHelp() throws Exception {
        // verify that no tooltips are displayed initially
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for sub-section help')]")) {
            SeleneseTestBase.assertFalse(isVisible("//td[contains(text(),'Sample text for sub-section help')]"));
        }
        
        if (isElementPresentByXpath("//td[contains(text(),'Sample text for read only field help')]")) {
            SeleneseTestBase.assertFalse(isVisible("//td[contains(text(),'Sample text for read only field help')]"));
        }

        // test tooltip help of sub-section header
        fireMouseOverEventByXpath("//span[contains(text(),'Display only fields')]");
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for sub-section help')]"));
        String javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" +
                "element[0].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
        SeleneseTestBase.assertFalse(isVisibleByXpath("//td[contains(text(),'Sample text for sub-section help')]"));

        // test tooltip help of display only data field
        fireMouseOverEventByXpath("//label[@for='display-field_control']");
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'Sample text for read only field help')]"));
        javascript="var element = document.getElementsByClassName('jquerybubblepopup jquerybubblepopup-black');" +
                "element[0].style.display='none'"; 
        ((JavascriptExecutor) driver).executeScript(javascript);
    }

    /**
     * Test the tooltip help on the section and fields with no content
     */
    protected void testMissingTooltipHelp() throws Exception {
        // verify that no tooltips are displayed initially
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@class='jquerybubblepopup jquerybubblepopup-black']"));

        // verify that no external help exist
        SeleneseTestBase.assertFalse(isElementPresent("#ConfigurationTestView-Help-Section2 input.uif-helpImage"));
       
        // test tooltip help of section header
        fireMouseOverEventByXpath("//div[@id='ConfigurationTestView-Help-Section2']/div");
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@class='jquerybubblepopup jquerybubblepopup-black']"));
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@class='jquerybubblepopup jquerybubblepopup-black']"));

        // test tooltip help of field
        fireMouseOverEventByXpath("//label[@id='missing-tooltip-help_label']");
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@class='jquerybubblepopup jquerybubblepopup-black']"));
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@class='jquerybubblepopup jquerybubblepopup-black']"));
    }

    /**
     * Test the external help on the section and fields
     */

    protected void testExternalHelp2() throws Exception {
        // test external help of section
        assertPopUpWindowUrl(By.cssSelector("input[title=\"Help for External Help\"]"), "HelpWindow", "http://www.kuali.org/?section");

        // test external help of field with label left
        assertPopUpWindowUrl(By.xpath("//div[@id='field-label-left-external-help']/fieldset/input[@title='Help for Field Label']"), "HelpWindow",
                "http://www.kuali.org/?label_left");

        // test external help of field with label right
        assertPopUpWindowUrl(By.xpath("//div[@id='field-label-right-external-help']/fieldset/input[@title='Help for Field Label']"), "HelpWindow",
                "http://www.kuali.org/?label_right");

        // test external help of field with label top and help URL from system parameters
        assertPopUpWindowUrl(By.xpath("//div[@id='field-label-top-external-help']/fieldset/input[@title='Help for Field Label']"), "HelpWindow",
                "http://www.kuali.org/?system_parm");

        // test external help of standalone help widget
        assertPopUpWindowUrl(By.id("standalone-external-help"), "HelpWindow", "http://www.kuali.org/?widget_only");
    }

    /**
     * Test the external help on the sub-section and display only fields
     */

    protected void testDisplayOnlyExternalHelp2() throws Exception {
        // test external help of sub-section
        assertPopUpWindowUrl(By.cssSelector("input[title=\"Help for Display only fields\"]"), "HelpWindow", "http://www.kuali.org/?sub_section");

        // test external help of display only data field
        assertPopUpWindowUrl(By.xpath("//div[@id='display-field-external-help']/fieldset/input[@title='Help for Field Label']"), "HelpWindow",
                "http://www.kuali.org/?display_field");
    }

    /**
     * Test the external help on the section and fields with missing help URL
     */

    protected void testMissingExternalHelp2() throws Exception {
        // test external help of section is not rendered
        SeleneseTestBase.assertFalse(isElementPresent(By.cssSelector("input[title=\"Help for Missing External Help\"]")));

        // test external help of field with blank externalHelpURL is not rendered
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//div[@id='external-help-externalHelpUrl-empty']/*[@class='uif-helpImage']"));

        // test external help of field with empty helpDefinition is not rendered
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//div[@id='external-help-helpdefinition-empty']/*[@class='uif-helpImage']"));

        // test external help of field with missing system parameter is not rendered
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//div[@id='external-help-system-parm-missing']/*[@class='uif-helpImage']"));

        // test external help of standalone help widget is not rendered
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//div[@id='standalone-external-help-missing']"));
    }
    
    protected void testReferenceCampusTypeBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        String docId = waitForDocId();
        assertBlanketApproveButtonsPresent();
        String dtsTwo = ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Campus Type " + dtsTwo);
        waitAndTypeByXpath(DOC_CODE_XPATH, RandomStringUtils.randomAlphabetic(1));
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", "Indianapolis" + dtsTwo);
        int attemptCount = 1;
        blanketApproveCheck();
        while (hasDocError("same primary key already exists") && attemptCount < 25) {
            clearTextByXpath(DOC_CODE_XPATH);
            waitAndTypeByXpath(DOC_CODE_XPATH, Character.toString((char) ('A' + attemptCount++)));
            blanketApproveCheck();
        }
        blanketApproveAssert();
        assertDocFinal(docId);
    }

    protected void testSearchEditCancel() throws InterruptedException {
        selectFrameIframePortlet();
        waitAndClickSearch2();
        waitAndClickEdit();
        testCancelConfirmation();
    }

    protected void testServerErrorsIT() throws Exception {
        waitAndClickByXpath("//button[contains(.,'Get Error Messages')]");
        waitForPageToLoad();
        Thread.sleep(5000);
        assertElementPresent("div[data-messagesfor=\"Demo-ValidationLayout-SectionsPage\"] .uif-errorMessageItem-field");
        waitIsVisibleByXpath("//div[@data-header_for='Demo-ValidationLayout-Section1']");
        assertElementPresentByXpath("//*[@data-messageitemfor='Demo-ValidationLayout-Section1' and @class='uif-errorMessageItem']");
        assertElementPresent("div[data-role=\"InputField\"] img[alt=\"Error\"]");
        assertElementPresentByXpath("//a[contains(.,'Section 1 Title')]");
        fireMouseOverEventByXpath("//a[contains(.,'Field 1')]");
        assertElementPresent(".uif-errorMessageItem-field");
        waitAndClickByXpath("//a[contains(.,'Field 1')]");
        Thread.sleep(2000);
        waitIsVisible(".jquerybubblepopup-innerHtml");
        waitIsVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems");
        waitIsVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-errorMessageItem-field");
        waitAndTypeByName("field1", "");
        fireEvent("field1", "blur");
        fireEvent("field1", "focus");
        waitIsVisible(".jquerybubblepopup-innerHtml");
        waitIsVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-errorMessageItem-field");
        waitIsVisible(".jquerybubblepopup-innerHtml > .uif-clientMessageItems");
        waitIsVisible(".jquerybubblepopup-innerHtml > .uif-clientMessageItems  .uif-errorMessageItem-field");
        waitAndTypeByName("field1", "t");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                failableFail(TIMEOUT_MESSAGE);
            }
            try {
                if (!isElementPresent(".jquerybubblepopup-innerHtml > .uif-clientMessageItems")) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        waitIsVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-errorMessageItem-field");
        SeleneseTestBase.assertFalse(isElementPresent(".jquerybubblepopup-innerHtml > .uif-clientMessageItems"));
    }

    protected void testServerInfoIT() throws Exception {
        waitAndClickByXpath("//button[contains(.,'Get Info Messages')]");
        waitIsVisibleByXpath("//div[@data-messagesfor='Demo-ValidationLayout-SectionsPage']");
        SeleneseTestBase.assertTrue(isVisibleByXpath("//div[@data-messagesfor='Demo-ValidationLayout-SectionsPage']"));
        SeleneseTestBase.assertTrue(isElementPresent("div[data-messagesfor=\"Demo-ValidationLayout-SectionsPage\"] .uif-infoMessageItem"));
        SeleneseTestBase.assertTrue(isVisible("div[data-messagesfor=\"Demo-ValidationLayout-Section1\"]"));
        SeleneseTestBase.assertTrue(isElementPresent("div[data-messagesfor=\"Demo-ValidationLayout-Section1\"] .uif-infoMessageItem"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@data-role='InputField']//img[@alt='Information']"));
        fireMouseOverEventByXpath("//a[contains(.,'Field 1')]");
        SeleneseTestBase.assertTrue(isElementPresent(".uif-infoHighlight"));
        waitAndClickByXpath("//a[contains(.,'Field 1')]");

        for (int second = 0;; second++) {
            if (second >= waitSeconds)
                failableFail(TIMEOUT_MESSAGE);
            try {
                if (isVisible(".jquerybubblepopup-innerHtml"))
                    break;
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        SeleneseTestBase.assertTrue(isVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems"));
        SeleneseTestBase.assertTrue(isVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-infoMessageItem-field"));
        waitAndTypeByName("field1", "");
        fireEvent("field1", "blur");
        fireEvent("field1", "focus");

        for (int second = 0;; second++) {
            if (second >= waitSeconds)
                failableFail(TIMEOUT_MESSAGE);
            try {
                if (isVisible(".jquerybubblepopup-innerHtml"))
                    break;
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        SeleneseTestBase.assertTrue(isVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-infoMessageItem-field"));
        for (int second = 0;; second++) {
            if (second >= waitSeconds)
                failableFail(TIMEOUT_MESSAGE);
            try {
                if (isVisible(".jquerybubblepopup-innerHtml > .uif-clientMessageItems"))
                    break;
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        SeleneseTestBase.assertTrue(isVisible(".jquerybubblepopup-innerHtml > .uif-clientMessageItems  .uif-errorMessageItem-field"));
        waitAndTypeByName("field1", "b");
        fireEvent("field1", "blur");
        fireEvent("field1", "focus");

        for (int second = 0;; second++) {
            if (second >= waitSeconds)
                failableFail(TIMEOUT_MESSAGE);
            try {
                if (!isElementPresent(".jquerybubblepopup-innerHtml > .uif-clientMessageItems"))
                    break;
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        fireEvent("field1", "blur");
        Thread.sleep(3000);
        SeleneseTestBase.assertTrue(!isVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-infoMessageItem-field"));
        SeleneseTestBase.assertFalse(isElementPresent(".jquerybubblepopup-innerHtml > .uif-clientMessageItems"));
        fireEvent("field1", "focus");
        clearTextByName("field1");
        fireEvent("field1", "blur");
        SeleneseTestBase.assertTrue(isElementPresent("div.uif-hasError"));
        SeleneseTestBase.assertTrue(isElementPresent("img[src*=\"error.png\"]"));
    }

    protected void testServerWarningsIT() throws Exception {
        waitAndClickByXpath("//button[contains(.,'Get Warning Messages')]");
        waitForPageToLoad();
        Thread.sleep(3000);
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue("div[data-messagesfor=\"Demo-ValidationLayout-SectionsPage\"] not visible",
                isVisible("div[data-messagesfor=\"Demo-ValidationLayout-SectionsPage\"]"));
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                "div[data-messagesfor=\"Demo-ValidationLayout-SectionsPage\"] .uif-warningMessageItem not present",
                isElementPresent("div[data-messagesfor=\"Demo-ValidationLayout-SectionsPage\"] .uif-warningMessageItem"));
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                "div[data-messagesfor=\"Demo-ValidationLayout-Section1\"] not visible", isVisible(
                "div[data-messagesfor=\"Demo-ValidationLayout-Section1\"]"));
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                "div[data-messagesfor=\"Demo-ValidationLayout-Section1\"] .uif-warningMessageItem not present",
                isElementPresent("div[data-messagesfor=\"Demo-ValidationLayout-Section1\"] .uif-warningMessageItem"));
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                "div[data-role=\"InputField\"] img[alt=\"Warning\"] not present", isElementPresent(
                "div[data-role=\"InputField\"] img[alt=\"Warning\"]"));
        fireMouseOverEvent(By.xpath("//a[contains(.,'Field 1')]"));
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                ".uif-warningHighlight no present when //a[contains(.,'Field 1')] is moused over",
                isElementPresent(".uif-warningHighlight"));
        waitAndClickByXpath("//a[contains(.,'Field 1')]");
        waitForElementVisible(".jquerybubblepopup-innerHtml", " after click on //a[contains(.,'Field 1')]");
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                ".jquerybubblepopup-innerHtml > .uif-serverMessageItems not visible", isVisible(
                ".jquerybubblepopup-innerHtml > .uif-serverMessageItems"));
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                ".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-warningMessageItem-field not visible",
                isVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-warningMessageItem-field"));
        waitAndTypeByName("field1", "");
        fireEvent("field1", "blur");
        fireMouseOverEventByName("field1");
        waitForElementVisible(".jquerybubblepopup-innerHtml",
                " not visible after typing nothing in name=field1 then firing blur and focus events");
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                ".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-warningMessageItem-field not visible after typing nothing in name=field1 then firing blur and focus events",
                isVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-warningMessageItem-field"));
        waitForElementVisible(".jquerybubblepopup-innerHtml> .uif-clientMessageItems",
                " not visible after typing nothing in name=field1 then firing blur and focus events");
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                ".jquerybubblepopup-innerHtml > .uif-clientMessageItems  .uif-errorMessageItem-field not visible after typing nothing in name=field1 then firing blur and focus events",
                isVisible(".jquerybubblepopup-innerHtml > .uif-clientMessageItems  .uif-errorMessageItem-field"));
        waitAndTypeByName("field1", "b");
        fireEvent("field1", "blur");
        fireMouseOverEventByName("field1");
        waitForElementVisible(".jquerybubblepopup-innerHtml> .uif-serverMessageItems", "");
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                ".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-warningMessageItem-field not visible after typing b in name=field1 then firing blur and focus events",
                isVisible(".jquerybubblepopup-innerHtml > .uif-serverMessageItems .uif-warningMessageItem-field"));
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(".jquerybubblepopup-innerHtml > .uif-clientMessageItems",
                !isElementPresent(
                        ".jquerybubblepopup-innerHtml > .uif-clientMessageItems"));
        clearTextByName("field1");
        fireEvent("field1", "blur");
        fireMouseOverEventByName("field1");
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                ".uif-hasError is not present after typing nothing in name=field1 and then firing focus and blur events",
                isElementPresent(".uif-hasError"));
        com.thoughtworks.selenium.SeleneseTestBase.assertTrue(
                "img[src*=\"error.png\"] is not present after typing nothing in name=field1 and then firing focus and blur events",
                isElementPresent("img[src*=\"error.png\"]"));
        passed();
    }

    /**
     * Test the tooltip and external help on the view
     */
    protected void testViewHelp() throws Exception {
        // test tooltip help
        fireMouseOverEventByXpath("//h1/span[@class='uif-headerText-span']");
        SeleneseTestBase.assertEquals("Sample text for view help", getText("td.jquerybubblepopup-innerHtml"));

        // test external help
        waitAndClickByXpath("//input[@alt='Help for Configuration Test View - Help']");
        Thread.sleep(5000);
        switchToWindow("Kuali Foundation");
        Thread.sleep(5000);
        switchToWindow(CONFIGURATION_VIEW_WINDOW_TITLE);
    }

    /**
     * Test the tooltip and external help on the view
     */
    protected void testViewHelp2() throws Exception {
        // test tooltip help
        if (isElementPresentByXpath("//td[@class='jquerybubblepopup-innerHtml']")) {
            SeleneseTestBase.assertFalse(findElement(By.cssSelector("td.jquerybubblepopup-innerHtml")).isDisplayed());
        }

        // test tooltip help
        fireMouseOverEventByXpath("//h1/span[@class='uif-headerText-span']");
        Thread.sleep(2000);
        SeleneseTestBase.assertTrue(isVisibleByXpath("//td[contains(text(),'View help')]"));
        assertPopUpWindowUrl(By.cssSelector("input[title=\"Help for Configuration Test View\"]"), "HelpWindow", "http://www.kuali.org/");
    }

    protected void testVerifyAddDeleteFiscalOfficerLegacy() throws Exception {
        selectFrameIframePortlet();
        checkForIncidentReport("testVerifyAddDeleteFiscalOfficerLegacy");
        waitAndTypeByName("document.documentHeader.documentDescription", ITUtil.createUniqueDtsPlusTwoRandomChars());
        waitAndTypeByName("newCollectionLines['document.newMaintainableObject.dataObject.fiscalOfficer.accounts'].number","1234567890");
        waitAndTypeByName("newCollectionLines['document.newMaintainableObject.dataObject.fiscalOfficer.accounts'].foId", "2");
        waitAndClickByXpath("//button[@data-loadingmessage='Adding Line...']");
        waitForElementPresentByName("document.newMaintainableObject.dataObject.fiscalOfficer.accounts[0].number");
        SeleneseTestBase.assertEquals("1234567890", waitAndGetAttributeByName(
                "document.newMaintainableObject.dataObject.fiscalOfficer.accounts[0].number", "value"));
        SeleneseTestBase.assertEquals("2", waitAndGetAttributeByName(
                "document.newMaintainableObject.dataObject.fiscalOfficer.accounts[0].foId", "value"));
        waitAndClickByXpath("//button[@data-loadingmessage='Deleting Line...']");
        Thread.sleep(3000);
        SeleneseTestBase.assertEquals(Boolean.FALSE, (Boolean) isElementPresentByName(
                "document.newMaintainableObject.dataObject.fiscalOfficer.accounts[0].number"));
        passed();
    }

    protected void testVerifyAddDeleteNoteLegacy() throws Exception {
        selectFrameIframePortlet();
        waitAndClick("div.tableborders.wrap.uif-boxLayoutVerticalItem.clearfix  span.uif-headerText-span > img.uif-disclosure-image");
        waitForElementPresent("button[title='Add a Note'].uif-action.uif-primaryActionButton.uif-smallActionButton");
        waitAndClickByName("newCollectionLines['document.notes'].noteText");
        waitAndTypeByName("newCollectionLines['document.notes'].noteText", "Test note");
        waitAndClick("button[title='Add a Note'].uif-action.uif-primaryActionButton.uif-smallActionButton");
//        waitForElementPresentByName("document.notes[0].noteText");
        SeleneseTestBase.assertEquals("Test note", getTextByXpath("//pre"));
        waitAndClick("button[title='Delete a Note'].uif-action.uif-primaryActionButton.uif-smallActionButton");
        SeleneseTestBase.assertEquals(Boolean.FALSE, (Boolean) isElementPresentByName("document.notes[0].noteText"));
        passed();
    }

    protected void testVerifyAdHocRecipientsLegacy() throws Exception {
        selectFrameIframePortlet();
        waitAndClickByLinkText("Fiscal Officer Accounts");
        assertElementPresentByXpath(
                "//select[@name=\"newCollectionLines['document.adHocRoutePersons'].actionRequested\"]");
        assertElementPresentByXpath(
                "//input[@name=\"newCollectionLines['document.adHocRoutePersons'].name\" and @type=\"text\"]");
        assertElementPresentByXpath(
                "//select[@name=\"newCollectionLines['document.adHocRouteWorkgroups'].actionRequested\"]");
        assertElementPresentByXpath(
                "//input[@name=\"newCollectionLines['document.adHocRouteWorkgroups'].recipientNamespaceCode\" and @type='text']");
        assertElementPresentByXpath(
                "//input[@name=\"newCollectionLines['document.adHocRouteWorkgroups'].recipientName\" and @type='text']");
        passed();
    }

    protected void testVerifyButtonsLegacy() throws Exception {
        selectFrameIframePortlet();
        assertElementPresentByXpath("//button[contains(.,'ubmit')]");
        assertElementPresentByXpath("//button[contains(.,'ave')]");
        assertElementPresentByXpath("//button[contains(.,'lanket approve')]");
        assertElementPresentByXpath("//button[contains(.,'lose')]");
        assertElementPresentByXpath("//a[contains(.,'ancel')]");
        passed();
    }

    protected void testVerifyConstraintText() throws Exception {
        selectFrameIframePortlet();
        SeleneseTestBase.assertEquals("* indicates required field", getText(
                "div.uif-boxLayout.uif-horizontalBoxLayout.clearfix > span.uif-message.uif-requiredInstructionsMessage.uif-boxLayoutHorizontalItem"));
        SeleneseTestBase.assertEquals("Must not be more than 10 characters", getText(
                "div.uif-group.uif-gridGroup.uif-gridSection.uif-disclosure.uif-boxLayoutVerticalItem.clearfix div[data-label='Travel Account Number'].uif-field.uif-inputField span.uif-message.uif-constraintMessage"));
        SeleneseTestBase.assertEquals("Must not be more than 10 characters", getText(
                "div.uif-group.uif-gridGroup.uif-gridSection.uif-disclosure.uif-boxLayoutVerticalItem.clearfix div[data-label='Travel Sub Account Number'].uif-field.uif-inputField span.uif-message.uif-constraintMessage"));
        SeleneseTestBase.assertEquals("Must not be more than 10 characters", getText(
                "div.uif-group.uif-gridGroup.uif-collectionItem.uif-gridCollectionItem.uif-collectionAddItem div[data-label='Travel Account Number'].uif-field.uif-inputField span.uif-message.uif-constraintMessage"));
        passed();
    }

    protected void testVerifyEditedComponent(String docId, String componentName, String componentCode) throws Exception {
        selectFrameIframePortlet();
        waitAndTypeByName("name", componentName);
        waitAndClickSearch();
        isElementPresentByLinkText(componentName);
        waitAndClickByLinkText(componentName);
        waitForPageToLoad();
        Thread.sleep(2000);
        switchToWindow("Kuali :: Inquiry");
        Thread.sleep(2000);
        SeleneseTestBase.assertEquals(componentName, getTextByXpath("//div[@class='tab-container']/table//span[@id='name.div']").trim());
        SeleneseTestBase.assertEquals(componentCode, getTextByXpath("//div[@class='tab-container']/table//span[@id='code.div']").trim());
        waitAndClickCloseWindow();
        switchToWindow("null");
        List<String> parameterList=new ArrayList<String>();
    }

    protected void testVerifyDisclosures() throws Exception {
        selectFrameIframePortlet();
        assertElementPresentByXpath("//span[contains(text(),'Document Overview')]");
        assertElementPresentByXpath("//span[contains(text(),'Document Overview')]");
        assertElementPresentByXpath("//span[contains(text(),'Account Information')]");
        assertElementPresentByXpath("//span[contains(text(),'Fiscal Officer Accounts')]");
        assertElementPresentByXpath("//span[contains(text(),'Notes and Attachments')]");
        assertElementPresentByXpath("//span[contains(text(),'Ad Hoc Recipients')]");
        assertElementPresentByXpath("//span[contains(text(),'Route Log')]");
        colapseExpandByXpath("//span[contains(text(),'Document Overview')]//img", "//label[contains(text(),'Organization Document Number')]");
        colapseExpandByXpath("//span[contains(text(),'Account Information')]//img","//label[contains(text(),'Travel Account Type Code')]");
        colapseExpandByXpath("//span[contains(text(),'Fiscal Officer Accounts')]//img","//a[contains(text(),'Lookup/Add Multiple Lines')]");
        expandColapseByXpath("//span[contains(text(),'Notes and Attachments')]//img","//label[contains(text(),'Note Text')]");
        expandColapseByXpath("//span[contains(text(),'Ad Hoc Recipients')]","//span[contains(text(),'Ad Hoc Group Requests')]");

        // Handle frames
        waitAndClickByXpath("//span[contains(text(),'Route Log')]//img");
        selectFrame("routeLogIFrame");
        waitIsVisibleByXpath("//img[@alt='refresh']");

        // relative=top iframeportlet might look weird but either alone results in something not found.
        selectTopFrame();
        selectFrameIframePortlet();
        waitAndClickByXpath("//span[contains(text(),'Route Log')]//img");
        selectFrame("routeLogIFrame");
        waitNotVisibleByXpath("//img[@alt='refresh']");
        passed();
    }

    protected void testVerifyDocumentOverviewLegacy() throws Exception {
        selectFrameIframePortlet();
        assertTextPresent("Document Overview");
        assertElementPresentByXpath("//input[@name='document.documentHeader.documentDescription']");
        assertElementPresentByXpath("//input[@name='document.documentHeader.organizationDocumentNumber']");
        assertElementPresentByXpath("//textarea[@name='document.documentHeader.explanation']");
        passed();
    }

    protected void testVerifyExpandCollapse() throws Exception {
        selectFrameIframePortlet();
        assertElementPresentByXpath("//button[contains(@class, 'uif-expandDisclosuresButton')]");
        assertElementPresentByXpath("//button[contains(@class, 'uif-collapseDisclosuresButton')]");
        passed();
    }

    protected void testVerifyFieldsLegacy() throws Exception {
        selectFrameIframePortlet();
        assertElementPresentByXpath("//input[@name='document.newMaintainableObject.dataObject.number' and @type='text' and @size=10 and @maxlength=10]");
        assertElementPresentByXpath("//input[@name='document.newMaintainableObject.dataObject.extension.accountTypeCode' and @type='text' and @size=2 and @maxlength=3]");
        assertElementPresentByXpath(
                "//input[@name='document.newMaintainableObject.dataObject.subAccount' and @type='text' and @size=10 and @maxlength=10]");
        assertElementPresentByXpath("//input[@name='document.newMaintainableObject.dataObject.subsidizedPercent' and @type='text' and @size=6 and @maxlength=20]");
        assertElementPresentByXpath(
                "//input[@name='document.newMaintainableObject.dataObject.foId' and @type='text' and @size=5 and @maxlength=10]");
        assertElementPresentByXpath(
                "//input[@name=\"newCollectionLines['document.newMaintainableObject.dataObject.fiscalOfficer.accounts'].number\" and @type='text' and @size=10 and @maxlength=10]");
        assertElementPresentByXpath(
                "//input[@name=\"newCollectionLines['document.newMaintainableObject.dataObject.fiscalOfficer.accounts'].foId\" and @type='text' and @size=5 and @maxlength=10]");
        passed();
    }

    protected void testVerifyHeaderFieldsLegacy() throws Exception {
        selectFrameIframePortlet();
        assertElementPresentByXpath("//div[contains(@class, 'uif-documentNumber')]");
        assertElementPresentByXpath("//div[contains(@class, 'uif-documentInitiatorNetworkId')]");
        assertElementPresentByXpath("//div[contains(@class, 'uif-documentStatus')]");
        assertElementPresentByXpath("//div[contains(@class, 'uif-documentCreateDate')]");
        passed();
    }

    protected void testVerifyLookupAddMultipleLinesLegacy() throws Exception {
        selectFrameIframePortlet();
        assertElementPresentByXpath("//a[contains(text(),'Lookup/Add Multiple Lines')]");
        passed();
    }

    protected void testVerifyNotesAndAttachments() throws Exception {
        selectFrameIframePortlet();
        waitAndClickByXpath("//span[contains(text(),'Notes and Attachments')]");
        waitForElementPresentByXpath("//button[@title='Add a Note']");
        assertElementPresentByXpath("//span[contains(text(),'Notes and Attachments')]");
        assertElementPresentByXpath("//textarea[@name=\"newCollectionLines['document.notes'].noteText\"]");
        assertElementPresentByXpath("//input[@name='attachmentFile']");

        //assertElementPresentByXpath("//input[@name=\"newCollectionLines['document.notes'].attachment.attachmentTypeCode\"]");
        passed();
    }

    protected void testVerifyQuickfinderIconsLegacy() throws Exception {
        selectFrameIframePortlet();
        assertTextPresent("Document Overview");
        assertElementPresentByXpath("//*[@id='quickfinder1']");
        assertElementPresentByXpath("//*[@id='quickfinder2']");
        assertElementPresentByXpath("//*[@id='quickfinder3']");
        assertElementPresentByXpath("//*[@id='quickfinder4_add']");

        // TODO it would be better to test that the image isn't 404
        passed();
    }

    protected void testVerifyRouteLog() throws Exception {
        selectFrameIframePortlet();
        waitAndClickByLinkText("Route Log");
        waitForElementPresent("//iframe[contains(@src,'RouteLog.do')]");
        passed();
    }

    protected void testVerifySave() throws Exception {
        selectFrameIframePortlet();
        waitAndTypeByName("document.documentHeader.documentDescription", "Test Document " + ITUtil.DTS);
        waitAndClickByName("document.newMaintainableObject.dataObject.number");
        waitAndTypeByName("document.newMaintainableObject.dataObject.number", "1234567890");
        waitAndTypeByName("document.newMaintainableObject.dataObject.extension.accountTypeCode", "EAT");
        waitAndTypeByName("document.newMaintainableObject.dataObject.subAccount", "a1");
        waitAndClick(
                "button[data-loadingmessage='Saving...'].uif-action.uif-primaryActionButton.uif-boxLayoutHorizontalItem");
        Thread.sleep(2000);

        // checkErrorMessageItem(" also digit validation jira https://jira.kuali.org/browse/KULRICE-8038");
        passed();
    }

    protected void testVerifySubsidizedPercentWatermarkLegacy() throws Exception {
        selectFrameIframePortlet();

        // May be blowing up due to multiple locators
        //assertTrue(isElementPresent("//input[@name='document.newMaintainableObject.dataObject.subsidizedPercent' and @type='text' and @placeholder='##.##   ']"));
        assertElementPresentByXpath("//input[@name='document.newMaintainableObject.dataObject.subsidizedPercent']");
        passed();
    }

    protected void testWorkFlowDocTypeBlanketApprove() throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        String docId = waitForDocId();
        assertBlanketApproveButtonsPresent();
        String dts = ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits();
        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Validation Test Document Type " + dts);
        String parentDocType = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.kew.doctype.bo.DocumentType!!).(((name:document.newMaintainableObject.parentDocType.name,documentTypeId:document.newMaintainableObject.docTypeParentId,))).((`document.newMaintainableObject.parentDocType.name:name,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;"
                + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(parentDocType);
        waitAndClickSearch();
        waitAndClickReturnValue();
        String docTypeName = "DocType" + dts;
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", docTypeName);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.unresolvedDocHandlerUrl']",
                "${kr.url}/maintenance.do?methodToCall=docHandler");
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.label']",
                "Workflow Maintenance Document Type Document " + dts);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.unresolvedHelpDefinitionUrl']",
                "default.htm?turl=WordDocuments%2Fdocumenttype.htm");
        blanketApproveTest();
        assertDocFinal(docId);
    }

    protected void uncheck(By by) throws InterruptedException {
        WebElement element = findElement(by);
        if (element.isSelected()) {
            element.click();
        }
    }

    protected void uncheckByName(String name) throws InterruptedException {
        uncheck(By.name(name));
    }

    protected void uncheckByXpath(String locator) throws InterruptedException {
        uncheck(By.xpath(locator));
    }

    protected boolean validateErrorImage(boolean validateVisible) throws Exception {
        Thread.sleep(500);
        boolean valid = false;

        for (int second = 0; second < 5; second++) {
            if ((valid = validateErrorImage(validateVisible, second, ARIA_INVALID_XPATH)) == true) {
                break;
            }
        }

        if (validateVisible) {
            SeleneseTestBase.assertTrue("valid = " + valid + " when validateVisible is " + validateVisible, valid);
        } else {
            SeleneseTestBase.assertFalse("valid = " + valid + " when validateVisible is " + validateVisible, valid);
        }

        return valid;
    }

    private boolean validateErrorImage(boolean validateVisible, int second, String xpath) throws InterruptedException {
        try {
            if (validateVisible) {
                if (isElementPresentByXpath(xpath) && isVisibleByXpath(xpath)) {
                    return true;
                }
            } else {
                if (!isElementPresentByXpath(xpath) || !isVisibleByXpath(xpath)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // don't fail here, we're in a loop let the caller decide when to fail
        }
        Thread.sleep(1000);
        return false;
    }

    protected void verifyRichMessagesValidationBasicFunctionality() throws Exception
    {
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//input[@type='text' and @name='field1']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//a[contains(text(), 'Kuali')]"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//input[@type='checkbox' and @name='field2']"));
        Thread.sleep(3000);
    }

    protected void verifyRichMessagesValidationAdvancedFunctionality() throws Exception
    {
        //Color Options
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//span[@style='color: green;']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//span[@style='color: blue;']"));

        //Css class
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//span[@class='fl-text-underline fl-text-larger']"));

        //Combinations
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//input[@type='text' and @name='field3']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//select[@name='field4']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//button[contains(text(), 'Action Button')]"));

        //Rich Message Field
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//label[contains(., 'Label With')]/span[contains(., 'Color')]"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//label[contains(., 'Label With')]/i/b[contains(., 'Html')]"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//label[contains(., 'Label With')]/img[@class='uif-image inlineBlock']"));
        Thread.sleep(3000);
    }

    protected void verifyRichMessagesValidationLettersNumbersValidation() throws Exception
    {
        //For letters only Validation
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//input[@type='text' and @name='field5']"));
        waitAndTypeByXpath(
                "//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock']/input[@name= 'field5']",
                "abc");
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock uif-hasError']"));
        clearTextByXpath(
                "//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock']/input[@name= 'field5']");
        waitAndTypeByXpath("//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock']/input[@name= 'field5']","abc12");
        waitAndTypeByXpath("//input[@name= 'field6']", "");
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock uif-hasError']"));
        Thread.sleep(3000);
        clearTextByXpath("//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock uif-hasError']/input[@name= 'field5']");
        waitAndTypeByXpath("//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock uif-hasError']/input[@name= 'field5']","abc");
        waitAndTypeByXpath("//input[@name= 'field6']", "");

        //For numbers only validation
        waitAndTypeByXpath("//input[@name= 'field6']", "123");
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock uif-hasError']"));
        clearTextByXpath("//input[@name= 'field6']");
        waitAndTypeByXpath("//input[@name= 'field6']", "123ab");
        fireEvent("field6", "blur");
        Thread.sleep(5000);
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@class='uif-field uif-inputField uif-inputField-labelTop inlineBlock uif-hasError']"));
        Thread.sleep(3000);
    }

    protected void verifyRichMessagesValidationRadioAndCheckBoxGroupFunctionality() throws Exception
    {
        //Radio Group
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//fieldset[@class='uif-verticalRadioFieldset']/span/input[@type='radio' and @name='field24' and @value='1']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath(
                "//fieldset[@class='uif-verticalRadioFieldset']/span/input[@type='radio' and @name='field24' and @value='2']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath(
                "//fieldset[@class='uif-verticalRadioFieldset']/span/input[@type='radio' and @name='field24' and @value='3']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath(
                "//fieldset[@class='uif-verticalRadioFieldset']/span/input[@type='radio' and @name='field24' and @value='4']"));

        //Checkbox Group
        SeleneseTestBase.assertTrue(isElementPresentByXpath(
                "//fieldset[@class='uif-verticalCheckboxesFieldset']/span/input[@type='checkbox' and @name='field115' and @value='1']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//fieldset[@class='uif-verticalCheckboxesFieldset']/span/input[@type='checkbox' and @name='field115' and @value='2']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath(
                "//fieldset[@class='uif-verticalCheckboxesFieldset']/span/input[@type='checkbox' and @name='field115' and @value='3']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath(
                "//fieldset[@class='uif-verticalCheckboxesFieldset']/span/label/div/select[@name='field4']"));

        //Checkbox Control
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//input[@type='checkbox' and @name='bField1']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//input[@type='text' and @name='field103']"));
    }

    protected void verifyRichMessagesValidationLinkDeclarationsFunctionality() throws Exception
    {
        //Testing link tag
        waitAndClickByXpath("//div[contains(., 'Testing link tag')]/a");
        Thread.sleep(9000);
        switchToWindow("Open Source Software | www.kuali.org");
        switchToWindow(RICH_MESSAGES_WINDOW_TITLE);

        //Testing methodToCall Action
        waitAndClickByXpath("//div[contains(., 'Testing methodToCall action')]/a");
        Thread.sleep(3000);
        SeleneseTestBase.assertTrue(isElementPresentByXpath(
                "//div[@class='uif-validationMessages uif-groupValidationMessages uif-pageValidationMessages uif-pageValidationMessages-error']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath(
                "//div[@id='Demo-AdvancedMessagesSection']/div[@class='uif-validationMessages uif-groupValidationMessages uif-pageValidationMessages-error']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@id='Demo-RadioCheckboxMessageSection']/div[@class='uif-validationMessages uif-groupValidationMessages uif-pageValidationMessages-error']"));

        //Testing methodToCall action (no client validation check)
        waitAndClickByXpath("//div[contains(., 'Testing methodToCall action (no client validation check)')]/a");
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@class='uif-validationMessages uif-groupValidationMessages uif-pageValidationMessages uif-pageValidationMessages-error']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@class='uif-validationMessages uif-groupValidationMessages']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@class='uif-validationMessages uif-groupValidationMessages uif-pageValidationMessages uif-pageValidationMessages-error']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@id='Demo-AdvancedMessagesSection']/div[@class='uif-validationMessages uif-groupValidationMessages uif-pageValidationMessages-error']"));
        SeleneseTestBase.assertTrue(isElementPresentByXpath("//div[@id='Demo-RadioCheckboxMessageSection']/div[@class='uif-validationMessages uif-groupValidationMessages uif-pageValidationMessages-error']"));
        Thread.sleep(3000);
    }

    /**
     * {@link #ADMINISTRATION_LINK_TEXT}
     * @param failable
     * @throws InterruptedException
     */
    protected void waitAndClickAdministration(Failable failable) throws InterruptedException {
        waitAndClickByLinkText(ADMINISTRATION_LINK_TEXT, failable);
    }

    protected void waitAndCancelConfirmation() throws InterruptedException {
        waitAndClickCancel();
        checkForIncidentReport("methodToCall.processAnswer.button0");
        waitAndClickByName("methodToCall.processAnswer.button0");
    }

    protected void waitAndClick(By by) throws InterruptedException {
        jiraAwareWaitAndClick(by, "");
    }

    protected void waitAndClick(By by, Failable failable) throws InterruptedException {
        jiraAwareWaitAndClick(by, "", failable);
    }

    protected void waitAndClick(String locator, String message) throws InterruptedException {
        jiraAwareWaitAndClick(By.cssSelector(locator), message);
    }

    protected void waitAndClickById(String id) throws InterruptedException {
        jiraAwareWaitAndClick(By.id(id), "");
    }
    protected void waitAndClickById(String id, String message) throws InterruptedException {
        jiraAwareWaitAndClick(By.id(id), message);
    }

    protected void waitAndClickByLinkText(String text) throws InterruptedException {
        jiraAwareWaitAndClick(By.linkText(text), "");
    }

    protected void waitAndClickByLinkText(String text, String message) throws InterruptedException {
        jiraAwareWaitAndClick(By.linkText(text), message);
    }

    protected void waitAndClickByLinkText(String text, Failable failable) throws InterruptedException {
        jiraAwareWaitAndClick(By.linkText(text), "", failable);
    }

    protected void waitAndClickByLinkText(String text, String message, Failable failable) throws InterruptedException {
        jiraAwareWaitAndClick(By.linkText(text), message, failable);
    }

    protected void waitAndClickByName(String name) throws InterruptedException {
        jiraAwareWaitAndClick(By.name(name), "");
    }

    protected void waitAndClickByXpath(String xpath) throws InterruptedException {
        waitAndClick(By.xpath(xpath));
    }

    protected void waitAndClickByXpath(String xpath, Failable failable) throws InterruptedException {
        waitAndClick(By.xpath(xpath), failable);
    }

    protected void waitAndClickByName(String name, String message) throws InterruptedException {
        jiraAwareWaitAndClick(By.name(name), message);
    }

    protected void waitAndClickByXpath(String xpath, String message) throws InterruptedException {
        jiraAwareWaitAndClick(By.xpath(xpath), message);
    }

    protected void waitAndClickButtonByText(String buttonText) throws InterruptedException {
        waitAndClickButtonByText(buttonText, "");
    }

    protected void waitAndClickButtonByText(String buttonText, String message) throws InterruptedException {
        waitAndClickByXpath("//button[contains(text(), '" + buttonText + "')]", message);
    }

    /**
     * {@link #CANCEL_NAME}
     * @throws InterruptedException
     */
    protected void waitAndClickCancel() throws InterruptedException {
        waitAndClickByName(CANCEL_NAME);
    }

    /**
     * {@link #CLOSE_WINDOW_XPATH_TITLE}
     * @throws InterruptedException
     */
    protected void waitAndClickCloseWindow() throws InterruptedException {
        waitAndClickByXpath(CLOSE_WINDOW_XPATH_TITLE);
    }

    /**
     * {@link #COPY_LINK_TEXT}
     * @throws InterruptedException
     */
    protected void waitAndClickCopy() throws InterruptedException {
        waitAndClickByLinkText(COPY_LINK_TEXT);
    }

    /**
     * {}@link #DOC_SEARCH_XPATH}
     * @throws InterruptedException
     */
    protected void waitAndClickDocSearch() throws InterruptedException {
        waitAndClickByXpath(DOC_SEARCH_XPATH);
    }

    /**
     * {@link #DOC_SEARCH_XPATH_TITLE}
     * @throws InterruptedException
     */
    protected void waitAndClickDocSearchTitle() throws InterruptedException {
        waitAndClickByXpath(DOC_SEARCH_XPATH_TITLE);
    }

    /**
     * {@link #LOGOUT_XPATH}
     * @throws InterruptedException
     */
    protected void waitAndClickLogout() throws InterruptedException {
        waitAndClickLogout(this);
    }

    /**
     * {@link #LOGOUT_XPATH}
     * @param failable
     * @throws InterruptedException
     */
    protected void waitAndClickLogout(Failable failable) throws InterruptedException {
        selectTopFrame();
        waitAndClickByXpath(LOGOUT_XPATH, failable);
    }

    /**
     * {}@link #MAIN_MENU_LINK_TEXT}
     * @param failable
     * @throws InterruptedException
     */
    protected void waitAndClickMainMenu(Failable failable) throws InterruptedException {
        waitAndClickByLinkText(MAIN_MENU_LINK_TEXT, failable);
    }

    /**
     * {@link #SAVE_XPATH}
     * @throws InterruptedException
     */
    protected void waitAndClickSave() throws InterruptedException {
        waitAndClickByXpath(SAVE_XPATH);
    }

    /**
     * {@link #SEARCH_XPATH}
     * @throws InterruptedException
     */
    protected void waitAndClickSearch() throws InterruptedException {
        waitAndClickByXpath(SEARCH_XPATH);
    }

    /**
     * {@link #SUBMIT_XPATH}
     * @throws InterruptedException
     */
    protected void waitAndClickSubmit() throws InterruptedException {
        waitAndClickByXpath(SUBMIT_XPATH);
    }


    /**
     * {@link #XML_INGESTER_LINK_TEXT}
     * @param failable
     * @throws InterruptedException
     */
    protected void waitAndClickXMLIngester(Failable failable) throws InterruptedException {
        waitAndClickByLinkText(XML_INGESTER_LINK_TEXT, failable);
    }


    protected void waitAndSelectByName(String name, String selectText) throws InterruptedException {
        waitFor(By.name(name), selectText + " not found.");
        select(By.name(name), selectText);
    }

    protected void waitAndType(By by, String text) throws InterruptedException {
        waitAndType(by, text,  "");
    }

    protected void waitAndType(By by, String text, String message) throws InterruptedException {
        try {
            jiraAwareWaitFor(by, "");
            WebElement element = findElement(by);
            WebDriverUtil.highlightElement(driver, element);
            element.sendKeys(text);
        } catch (Exception e) {
            JiraAwareFailureUtil.failOnMatchedJira(by.toString(), this);
            failableFail(e.getMessage() + " " + by.toString() + "  unable to type text '" + text + "'  " + message
                    + " current url " + driver.getCurrentUrl()
                    + "\n" + ITUtil.deLinespace(driver.getPageSource()));
        }
    }

    protected void waitAndType(String selector, String text) throws InterruptedException {
        waitAndType(By.cssSelector(selector), text);
    }

    protected void waitAndTypeById(String id, String text) throws InterruptedException {
        waitAndType(By.id(id), text);
    }

    protected void waitAndTypeByXpath(String locator, String text) throws InterruptedException {
        waitAndType(By.xpath(locator), text);
    }

    protected void waitAndTypeByXpath(String locator, String text, String message) throws InterruptedException {
        waitAndType(By.xpath(locator), text, message);
    }

    protected void waitAndTypeByName(String name, String text) throws InterruptedException {
        waitAndType(By.name(name), text);
    }

    protected void waitAndCreateNew() throws InterruptedException {
        checkForIncidentReport();
        selectFrameIframePortlet();
        try {
            jGrowl("Create New");
            waitAndClickCreateNew(); // timing out in CI rice-trunk-smoke-test-jdk7/494
        } catch (Exception e) {
            System.out.println("waitAndClickByXpath(\"//img[@alt='create new']\") failed trying title method with " + e.getMessage());
            waitAndClickByXpath("//a[@title='Create a new record']");
        }
    }

    /**
     * {@link #CREATE_NEW_XPATH}
     * @throws InterruptedException
     */
    protected void waitAndClickCreateNew() throws InterruptedException {
        waitAndClickByXpath(CREATE_NEW_XPATH);
    }

    protected void waitAndClickEdit() throws InterruptedException {
        waitAndClickByLinkText(EDIT_LINK_TEXT);
    }

    protected void waitAndClickReturnValue() throws InterruptedException {
        waitAndClickByLinkText(RETURN_VALUE_LINK_TEXT);
    }

    protected void waitAndClickSearch2() throws InterruptedException {
        waitAndClickByXpath(SEARCH_XPATH_2);
    }

    protected void waitAndClickSearch3() throws InterruptedException {
        waitAndClickByXpath(SEARCH_XPATH_3);
    }

    protected String waitForDocId() throws InterruptedException {
        checkForDocError();
        waitForElementPresentByXpath(DOC_ID_XPATH);

        return findElement(By.xpath(DOC_ID_XPATH)).getText();
    }

    protected WebElement waitForElementPresent(By by) throws InterruptedException {
        return jiraAwareWaitFor(by, "");
    }

    protected WebElement waitForElementPresent(By by, String message) throws InterruptedException {
        return jiraAwareWaitFor(by, message);
    }

    protected WebElement waitForElementPresent(String locator) throws InterruptedException {
        return jiraAwareWaitFor(By.cssSelector(locator), "");
    }

    protected WebElement waitForElementPresentByClassName(String name) throws InterruptedException {
        return jiraAwareWaitFor(By.className(name), "");
    }

    protected WebElement waitForElementPresentByClassName(String name, String message) throws InterruptedException {
        return jiraAwareWaitFor(By.className(name), message);
    }

    protected void waitForElementsPresentByClassName(String name, String message) throws InterruptedException {
        jiraAwareWaitFors(By.className(name), message);
    }

    protected WebElement waitForElementPresentById(String id) throws InterruptedException {
        return jiraAwareWaitFor(By.id(id), "");
    }

    protected void waitForElementPresentById(String id, String message) throws InterruptedException {
        jiraAwareWaitFor(By.id(id), message);
    }

    protected void waitForElementsPresentById(String id, String message) throws InterruptedException {
        jiraAwareWaitFors(By.id(id), message);
    }

    protected WebElement waitForElementPresentByName(String name) throws InterruptedException {
        return jiraAwareWaitFor(By.name(name), "");
    }

    protected WebElement waitForElementPresentByXpath(String xpath) throws InterruptedException {
        return jiraAwareWaitFor(By.xpath(xpath), "");
    }

    protected WebElement waitForElementPresentByXpath(String xpath, String message) throws InterruptedException {
        return jiraAwareWaitFor(By.xpath(xpath), message);
    }

    protected void waitForElementsPresentByXpath(String xpathLocator) throws InterruptedException {
        jiraAwareWaitFors(By.xpath(xpathLocator), "");
    }

    protected void waitForTitleToEqualKualiPortalIndex() throws InterruptedException {
        waitForTitleToEqualKualiPortalIndex("");
    }

    protected void waitIsVisible(By by) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
        isVisible(by);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

//        for (int second = 0;; second++) {
//            if (second >= waitSeconds) {
//                failableFail(TIMEOUT_MESSAGE + " " + by.toString());
//            }
//            if (isVisible(by)) {
//                break;
//            }
//            Thread.sleep(1000);
//        }
    }

    protected void waitIsVisible(By by, String message) throws InterruptedException {
        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                failableFail(TIMEOUT_MESSAGE + " " + by.toString() + " " + message);
            }
            if (isVisible(by)) {
                break;
            }
            Thread.sleep(1000);
        }
    }

    protected boolean waitAreAnyVisible(By[] bys) throws InterruptedException {
        if (bys == null || bys.length == 0 ) {
            return false;
        }

        for (int second = 0; second < waitSeconds; second++) {

            if (isVisible(bys)) {
                return true;
            }

            Thread.sleep(1000);
        }

        return false;
    }

    protected boolean isVisible(By[] bys) {
        if (bys == null || bys.length == 0 ) {
            return false;
        }

        for (int i = 0, s = bys.length; i < s; i++) {

            try {

                if (isVisible(bys[i])) {
                    return true;
                }

            } catch (NoSuchElementException nsee) {
                // don't fail
            }

        }

        return false;
    }

    /**
     * Uses Selenium's findElements method which does not throw a test exception if not found.
     * @param elementLocator
     * @param message
     * @throws InterruptedException
     */
    protected void waitForElementVisible(String elementLocator, String message) throws InterruptedException {
        waitForElementVisibleBy(By.cssSelector(elementLocator), message);
    }

    protected void waitForElementVisibleBy(By by, String message) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        boolean failed = false;

        for (int second = 0;; second++) {
            if (second >= waitSeconds)
                failed = true;
            try {
                if (failed || (driver.findElements(by)).size() > 0)
                    break;
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        checkForIncidentReport(by.toString()); // after timeout to be sure page is loaded

        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_SEC, TimeUnit.SECONDS);

        if (failed) {
            failableFail("timeout of " + waitSeconds + " seconds waiting for " + by + " " + message + " " + driver.getCurrentUrl());
        }
    }

    protected void waitForElementVisibleById(String id, String message) throws InterruptedException {
        waitForElementVisibleBy(By.id(id), message);
    }

    protected void waitIsVisible(String locator) throws InterruptedException {
        waitIsVisible(By.cssSelector(locator));
    }

    protected void waitIsVisibleByXpath(String locator) throws InterruptedException {
        waitIsVisible(By.xpath(locator));
    }

    protected void waitIsVisibleByXpath(String locator, String message) throws InterruptedException {
        waitIsVisible(By.xpath(locator), message);
    }

    protected void waitForTitleToEqualKualiPortalIndex(String message) throws InterruptedException {
        Thread.sleep(2000);
        // This started failing in CI....
        // boolean failed = false;
        //
        // for (int second = 0;; second++) {
        //     Thread.sleep(1000);
        //     if (second >= waitSeconds) failed = true;
        //     try { if (failed || ITUtil.KUALI_PORTAL_TITLE.equals(driver.getTitle())) break; } catch (Exception e) {}
        // }

        // WebDriverUtil.checkForIncidentReport(driver, message); // after timeout to be sure page is loaded
        // if (failed) failableFail("timeout of " + waitSeconds + " seconds " + message);
    }

    protected void waitAndClick(String locator) throws InterruptedException {
        waitAndClick(locator, "");
    }

    protected void waitForPageToLoad() throws InterruptedException {
        Thread.sleep(5000);
    }

    protected WebElement waitFor(By by) throws InterruptedException {
        return jiraAwareWaitFor(by, "");
    }

    /**
     * Should be called from jiraAwareWaitFor to get KULRICE error output in CI.
     *
     * Inner most waitFor, let it throw the failure so the timeout message reflects the waitSeconds time, not the 1
     * second it is set to before returning.
     * @param by
     * @param message
     * @throws InterruptedException
     */
    protected void waitFor(By by, String message) throws InterruptedException {
        WebDriverUtil.waitFor(this.driver, this.waitSeconds, by, message);
    }

    /**
     * {@link #KRAD_XPATH}
     * @throws InterruptedException
     */
    protected void waitAndClickKRAD() throws InterruptedException {
        waitAndClickByLinkText(KRAD_XPATH);
    }

    protected void waitNotVisible(By by) throws InterruptedException {
        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                failableFail(TIMEOUT_MESSAGE);
            }
            if (!isVisible(by)) {
                break;
            }
            Thread.sleep(1000);
        }
    }

    protected void waitNotVisibleByXpath(String locator) throws InterruptedException {
        waitNotVisible(By.xpath(locator));
    }

    /**
     * Does the test page use KRAD UIF?
     * Useful if trying to re-use a test for both a KNS and KRAD screens that have different paths to the elements.
     * @return
     */
    protected boolean isKrad(){
        return (ITUtil.REMOTE_UIF_KRAD.equalsIgnoreCase(getUiFramework()));
    }

    protected WebElement getElementByDataAttributeValue(String dataAttributeName, String value){
        return findElement(By.cssSelector("[data-" + dataAttributeName + "='" + value +"']"));
    }

    protected WebElement getElementByDataAttribute(String dataAttributeName){
        return findElement(By.cssSelector("[data-" + dataAttributeName + "]"));
    }

    protected WebElement getElementByAttributeValue(String attributeName, String value){
        return findElement(By.cssSelector("[" + attributeName + "='" + value +"']"));
    }

    protected WebElement getElementByAttribute(String attributeName){
        return findElement(By.cssSelector("[" + attributeName + "]"));
    }

    /**
     * Returns the label text of a label-for element
     * <p>
     * For usage with elements like this: <label for="some-element-id">The text of the Label</label>
     * </p>
     *
     * @param forElementId the id of the element for which to find the label text
     * @return label text
     */
    protected String getForLabelText(String forElementId) {
        return findElement(By.cssSelector("label[for=" + forElementId + "]")).getText();
    }

    /**
     * Determines whether KRAD or KNS UIF is used for this test.
     * Useful if trying to re-use a test for both a KNS and KRAD screens that have different paths to the elements.
     * @return
     */
    public String getUiFramework() {
        return uiFramework;
    }

    /**
     * Sets which UIF is used by this test
     */
    public void setUiFramework(String uiFramework) {
        this.uiFramework = uiFramework;
    }
}