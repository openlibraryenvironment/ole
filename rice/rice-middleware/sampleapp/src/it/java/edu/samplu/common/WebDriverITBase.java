/*
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.thoughtworks.selenium.SeleneseTestBase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Base class for Selenium Webdriver integration tests
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class WebDriverITBase {

    public WebDriver driver;
    static ChromeDriverService chromeDriverService;

    /**
     * Returns the URL to be used with this test
     *
     * @return URL of the test
     */
    public abstract String getTestUrl();

    /**
     * Override in test to define a user other than admin
     * @return
     */
    public String getUserName() {
        return "admin";
    }

    @BeforeClass
    public static void createAndStartService() throws Exception {
        chromeDriverService = WebDriverUtil.chromeDriverCreateCheck();
        if (chromeDriverService != null) chromeDriverService.start();
    }


    /**
     * Setup the WebDriver test, login and load the tested web page
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        driver = WebDriverUtil.setUp(getUserName(), ITUtil.getBaseUrlString() + "/" + getTestUrl());
        WebDriverUtil.loginKradOrKns(driver, getUserName(), new Failable() {
            @Override
            public void fail(String message) {
                SeleneseTestBase.fail(message);
            }
        });
    }

    /**
     * Tear down the WebDriver test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        if (ITUtil.dontTearDownPropertyNotSet()) {
            driver.quit(); // TODO not tested with chrome, the service stop might need this check too
        }
    }

    /**
     * Tear down the WebDriver test
     *
     * @throws Exception
     */
    @AfterClass
    public static void stopService() throws Exception {
        if (chromeDriverService != null) {
            chromeDriverService.stop();
        }
    }

    /**
     * Check if an element is present
     *
     * <p>
     * This test takes a while due to the 'implicit wait' time.
     * </p>
     *
     * @param by The locating mechanism of the element
     * @return true if the element is present, false otherwise
     */
    public boolean isElementPresent(By by) {
        if (driver.findElements(by).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Quickly check if an element is present
     *
     * <p>
     * Just like {@link #isElementPresent(org.openqa.selenium.By)} but with a short 'implicit wait' time.  Use this only
     * if it is guaranteed that all elements are rendered.
     * </p>
     *
     * @param by The locating mechanism of the element
     * @return true if the element is present, false otherwise
     */
    public boolean isElementPresentQuick(By by) {
        driver.manage().timeouts().implicitlyWait(WebDriverUtil.SHORT_IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
        boolean result = isElementPresent(by);
        driver.manage().timeouts().implicitlyWait(WebDriverUtil.DEFAULT_IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
        return result;
    }

    /**
     * Assert that clicking an element causes a popup window with a specific URL
     *
     * @param by The locating mechanism of the element to be clicked
     * @param windowName The name of the popup window
     * @param url The URL of the popup window
     */
    public void assertPopUpWindowUrl(By by, String windowName, String url) {
        driver.findElement(by).click();
        String parentWindowHandle = driver.getWindowHandle();
        // wait page to be loaded
        driver.switchTo().window(windowName).findElements(By.tagName("head"));
        assertEquals(url, driver.getCurrentUrl());
        driver.switchTo().window(parentWindowHandle);
    }
    
    /**
     * 
     *
     * @param by The locating mechanism of the element
    */
    protected void waitFor(By by) throws InterruptedException {
        waitFor(by, "");
    }

    /**
     * 
     *
     * @param by The locating mechanism of the element
     * @param message User defined message to display
     */
    protected void waitFor(By by, String message) throws InterruptedException {
//        for (int second = 0;; second++) {
            Thread.sleep(1000);
//            if (second >= DEFAULT_WAIT_SEC) fail(by.toString() + " " + message + " " + DEFAULT_WAIT_SEC + " sec timeout.");
            try { driver.findElement(by);
                //break;
            } catch (Exception e) {}
//        }
    }

    /**
     * 
     *
     * @param by The locating mechanism of the element
     * @param text The text to type
    */
    protected void waitAndType(By by, String text) throws InterruptedException {
        waitFor(by, "");
        try {
            (driver.findElement(by)).sendKeys(text);
        } catch (Exception e) {
            fail(e.getMessage() + " " + by.toString() + " " + text);
            e.printStackTrace();
        }
    }
    
    /**
     * 
     *
     * @param by The locating mechanism of the element
     * @param text The text to type
     * @param message User defined message to display
    */
    protected void waitAndType(By by, String text, String message) throws InterruptedException {
        waitFor(by, "");
        try {
            (driver.findElement(by)).sendKeys(text);
        } catch (Exception e) {
            fail(e.getMessage() + " " + by.toString() + " " + text + "  "+message);
            e.printStackTrace();
        }
    }
    
    /**
     * 
     *
     * @param locator The locating mechanism of the element
     * @param text The text to type
    */
    protected void waitAndTypeByXpath(String locator, String text) throws InterruptedException {
        waitAndType(By.xpath(locator), text);
    }
    
    /**
     * 
     *
     * @param locator The locating mechanism of the element
     * @param text The text to type
     * @param message User defined message to display
    */
    protected void waitAndTypeByXpath(String locator, String text, String message) throws InterruptedException {
        waitAndType(By.xpath(locator), text, message);
    }
    
    /**
     * 
     *
     * @param name The name of the element
     * @param text The text to type
    */
    protected void waitAndTypeByName(String name, String text) throws InterruptedException {
        waitAndType(By.name(name), text);
    }
    
    /**
     * Clear the text written in an input field by name of an element
     *
     * @param name The name of the element
    */
    protected void clearTextByName(String name) throws InterruptedException {
        clearText(By.name(name));
    }
    
    /**
     * Clear the text written in an input field by xpath of an element
     *
     * @param locator The locating mechanism of the element
    */
    protected void clearTextByXpath(String locator) throws InterruptedException {
        clearText(By.xpath(locator));
    }
    
    /**
     * Clear the text written in an input field by xpath of an element
     *
     * @param by method used for finding the element
    */
    protected void clearText(By by)  throws InterruptedException {
        driver.findElement(by).clear();        
    }
    
    /**
     * Dismiss the javascript alert (clicking Cancel)
     *
    */
    protected void dismissAlert()
    {
        Alert alert = driver.switchTo().alert();
        //update is executed
        alert.dismiss();
    }
    
    /**
     * Accept the javascript alert (clicking OK)
     *
    */
    protected void acceptAlert()
    {
        Alert alert = driver.switchTo().alert();
        //update is executed
        alert.accept();
    }
    
    protected String getEval(String script)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String)js.executeScript(script);
    }
    
    /**
     * Switch to new window
     *
    */
    protected void switchWindow()
    {
        Set<String> winSet = driver.getWindowHandles();
        List<String> winList = new ArrayList<String>(winSet);
        String newTab = winList.get(winList.size() - 1);
        driver.switchTo().window(newTab);
    }
    
    /**
     * Get value of any attribute by using element name
     *
     *@param name name of an element
     *@param attribute the name of an attribute whose value is to be retrieved
    */
    protected String getAttributeByName(String name,String attribute) throws InterruptedException {
        return getAttribute(By.name(name),attribute);
    }
    
    /**
     * Get value of any attribute by using element xpath
     *
     *@param locator locating mechanism of an element
     *@param attribute the name of an attribute whose value is to be retrieved
    */
    protected String getAttributeByXpath(String locator,String attribute) throws InterruptedException {
        return getAttribute(By.xpath(locator),attribute);
    }
    
    /**
     * Get value of any attribute of an element
     *
     * @param by method used for finding the element
     *@param attribute the name of an attribute whose value is to be retrieved
    */
    protected String getAttribute(By by,String attribute)  throws InterruptedException {
        return driver.findElement(by).getAttribute(attribute);        
    }
    
    /**
     * 
     *
     * @param text text of the link
    */
    protected void waitAndClickByLinkText(String text) throws InterruptedException {
        waitAndClick(By.linkText(text),"");
    }

    /**
     * 
     *
     * @param text text of the link
     * @param message user defined message to display
    */
    protected void waitAndClickByLinkText(String text, String message) throws InterruptedException {
        waitAndClick(By.linkText(text), message);
    }
    
    /**
     * 
     *
     * @param by method used for finding the element
    */
    protected void waitAndClick(By by) throws InterruptedException {
        waitAndClick(by, "");
    }

    /**
     * 
     *
     * @param by method used for finding the element
     * @param message user defined message to display
    */
    protected void waitAndClick(By by, String message) throws InterruptedException {
        waitFor(by, message);
        try {
            (driver.findElement(by)).click();
        } catch (Exception e) {
            fail(e.getMessage() + " " + by.toString() + " " + message);
            e.printStackTrace();
        }
    }
    
    /**
     * 
     *
     * @param locator mechanism to locate element by xpath
    */
    protected void waitAndClick(String locator) throws InterruptedException {
        waitAndClick(locator, "");
    }
    
    /**
     * 
     *
     * @param locator mechanism to locate element by xpath
     * @param message user defined message to display
    */
    protected void waitAndClick(String locator, String message) throws InterruptedException {
        waitAndClick(By.cssSelector(locator), message);
    }

    /**
     * 
     *
     * @param locator mechanism to locate element by xpath
    */
    protected void waitForElementPresent(String locator) throws InterruptedException {
        waitFor(By.cssSelector(locator));
    }

    /**
     * 
     *
     * @param locator mechanism to locate element by xpath
    */    
    protected void waitForElementPresentByXpath(String locator) throws InterruptedException {
        waitFor(By.xpath(locator));
    }
    
    /**
     * 
     *
     * @param name name of an element
    */ 
    protected void waitForElementPresentByName(String name) throws InterruptedException {
        waitFor(By.name(name));
    }
    
    protected void checkForIncidentReport(Failable failable) {
        checkForIncidentReport("", failable, "");
    }

    protected void checkForIncidentReport(String locator, Failable failable) {
        checkForIncidentReport(locator, failable, "");
    }
    
    protected void checkForIncidentReport(String locator, Failable failable, String message) {
        ITUtil.checkForIncidentReport(driver.getPageSource(), locator, failable, message);
    }


}

