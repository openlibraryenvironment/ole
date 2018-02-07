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

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import com.saucelabs.saucerest.SauceREST;
import org.junit.Assert;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple {@link org.openqa.selenium.remote.RemoteWebDriver} test that demonstrates how to run your Selenium tests with <a href="http://saucelabs.com/ondemand">Sauce OnDemand</a>.
 *
 * This test also includes the <a href="">Sauce JUnit</a> helper classes, which will use the Sauce REST API to mark the Sauce Job as passed/failed.
 *
 * In order to use the {@link SauceOnDemandTestWatcher}, the test must implement the {@link SauceOnDemandSessionIdProvider} interface.
 *
 */
public class SauceLabsWebDriverHelper implements SauceOnDemandSessionIdProvider {

    /**
     * Use Saucelabs flag.  For ease of disabling saucelabs without having to remove other saucelabs property settings.
     * -Dremote.driver.saucelabs
     */
    public static final String REMOTE_DRIVER_SAUCELABS_PROPERTY = "remote.driver.saucelabs";

    /**
     * Saucelabs browser, default is Firefox.  See <a href="https://saucelabs.com/docs/platforms">Saucelabs Resources</a>
     * ff = Firefox
     * ie = Internet Explorer
     * chrome = Google Chrome
     * opera = Opera
     * android = Android
     * safari = Safari
     * ipad = IPad
     * iphone = IPhone
     * -Dsaucelabs.browser=
     */
    public static final String SAUCE_BROWSER_PROPERTY = "saucelabs.browser";

    /**
     * Suacelabs build, default is unknown.
     * -Drice.version=
     */
    public static final String SAUCE_BUILD_PROPERTY = "rice.version";

    /**
     * Create a unix shell script to download saucelab resources, default is false
     * Note - saucelabs history only goes back so far, if you run enough tests the resources will no longer
     * be available for downloading.
     * -Dsaucelabs.download.script=false
     */
    public static final String SAUCE_DOWNLOAD_SCRIPT_PROPERTY = "saucelabs.download.scripts";

    /**
     * Saucelabs idle timeout in seconds, default is 180
     * -Dsaucelabs.idle.timeout.seconds=
     */
    public static final String SAUCE_IDLE_TIMEOUT_SECONDS_PROPERTY = "saucelabs.idle.timeout.seconds";

    /**
     * Saucelabs key, required.
     * -Dsaucelabs.key=
     */
    public static final String SAUCE_KEY_PROPERTY = "saucelabs.key";

    /**
     * Saucelabs max duration in seconds, default is 480
     * -Dsaucelabs.max.duration.seconds=
     */
    public static final String SAUCE_MAX_DURATION_SECONDS_PROPERTY = "saucelabs.max.duration.seconds";

    /**
     * Saucelabs platform (OS) replace spaces with underscores, default is Linux.  See <a href="https://saucelabs.com/docs/platforms">Saucelabs Resources</a>
     * -Dsaucelabs.platform=
     */
    public static final String SAUCE_PLATFORM_PROPERTY = "saucelabs.platform";

    /**
     * Saucelabs ignore security domains in IE, which can introduce flakiness, default is true.  See <a href="http://code.google.com/p/selenium/wiki/FrequentlyAskedQuestions#Q:_The_does_not_work_well_on_Vista._How_do_I_get_it_to_work_as_e">InternetExplorerDriver FAQ</a>
     * -Dsaucelabs.ie.ignore.domains=false
     */
    public static final String SAUCE_IE_INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS_PROPERTY = "saucelabs.ie.ignore.domains";

    /**
     * Saucelabs popup disable setting, default is false (not disabled).  See <a href="https://saucelabs.com/docs/additional-config">DISABLE POPUP HANDLER</a>
     * -Dsaucelabs.pop.disable=
     */
    public static final String SAUCE_POPUP_PROPERTY = "saucelabs.pop.disable";

    /**
     * Saucelabs share setting, default is share.
     * -Dsaucelabs.share=
     */
    public static final String SAUCE_SHARE_PROPERTY = "saucelabs.share";

    /**
     * Saucelabs user
     * -Dsaucelabs.user=
     */
    public static final String SAUCE_USER_PROPERTY = "saucelabs.user";

    /**
     * Browser Version. See <a href="https://saucelabs.com/docs/platforms">Saucelabs Resources</a>
     * 0 or null is current version of <b>Chrome</b>.  If using a browser other than Chrome this must be set else an Exception will be thrown.
     * -Dsaucelabs.version=
     */
    public static final String SAUCE_VERSION_PROPERTY = "saucelabs.browser.version";

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(System.getProperty(SAUCE_USER_PROPERTY), System.getProperty(SAUCE_KEY_PROPERTY));

    private WebDriver driver;

    private String sessionId;

    /**
     * Saucelabs setup
     * @param className
     * @param testName
     * @throws Exception
     */
    public void setUp(String className, String testName) throws Exception {
        if (System.getProperty(REMOTE_DRIVER_SAUCELABS_PROPERTY) == null) { // dup guard so WebDriverUtil doesn't have to be used.
            return;
        }

        if (System.getProperty(SAUCE_USER_PROPERTY) == null || System.getProperty(SAUCE_KEY_PROPERTY) == null) {
            Assert.fail("-D" + SAUCE_USER_PROPERTY + " and -D" + SAUCE_KEY_PROPERTY + " must be set to saucelabs user and access key.");
        }

        DesiredCapabilities capabilities = null;
        if ("ff".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY))) {
            capabilities = DesiredCapabilities.firefox();
        } else if ("ie".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY)))  {
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                System.getProperty(SAUCE_IE_INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS_PROPERTY, "true"));
        } else if ("chrome".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY)))  {
            capabilities = DesiredCapabilities.chrome();
        } else if ("opera".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY)))  {
            capabilities = DesiredCapabilities.opera();
        } else if ("android".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY)))  {
            capabilities = DesiredCapabilities.android();
        } else if ("safari".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY)))  {
            capabilities = DesiredCapabilities.safari();
        } else if ("ipad".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY)))  {
            capabilities = DesiredCapabilities.ipad();
        } else if ("iphone".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY)))  {
            capabilities = DesiredCapabilities.iphone();
        } else {
            capabilities = DesiredCapabilities.firefox();
        }

        String version = System.getProperty(SAUCE_VERSION_PROPERTY);
        if (version == null || "0".equals(version)) { // Blank or 0 leaves version blank for use with chrome

            if (!"chrome".equalsIgnoreCase(System.getProperty(SAUCE_BROWSER_PROPERTY))) {
                throw new RuntimeException("Blank or 0 version for a browser not chrome " + System.getProperty(SAUCE_BROWSER_PROPERTY));
            }

            capabilities.setCapability("version", ""); // saucelabs requires blank for chrome (latest version)
        } else {
            capabilities.setCapability("version", version); // saucelabs requires blank for chrome (latest version)
        }

        capabilities.setCapability("platform", System.getProperty(SAUCE_PLATFORM_PROPERTY, Platform.UNIX.toString()).replaceAll("_", " "));
        capabilities.setCapability("idle-timeout", Integer.parseInt(System.getProperty(SAUCE_IDLE_TIMEOUT_SECONDS_PROPERTY, "180")));
        capabilities.setCapability("max-duration", Integer.parseInt(System.getProperty(SAUCE_MAX_DURATION_SECONDS_PROPERTY, "480")));
        capabilities.setCapability("name",  className + "." + testName + "-" + ITUtil.DTS);
        capabilities.setCapability("disable-popup-handler", System.getProperty(SAUCE_POPUP_PROPERTY, "false"));
        capabilities.setCapability("public", System.getProperty(SAUCE_SHARE_PROPERTY, "share"));

        this.driver = new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        this.sessionId = ((RemoteWebDriver)driver).getSessionId().toString();

        // TODO it would be better to do these at tear down, passing state could then be included in names, but requires more parameters
        if ("true".equals(System.getProperty(SAUCE_DOWNLOAD_SCRIPT_PROPERTY, "false"))) {
            try {
                String dir = determineSaveDir(className, testName);
                String resources = "mkdir " + dir + " ; cd " + dir + " ; \n"
                        + curlSaveResourceString(className, testName, "selenium-server.log") + " ; \n"
                        + curlSaveResourceString(className, testName, "video.flv") + " ; \n"
                        //                    + wgetnSaveResourceString(className, testName) + " ; \n"
                        + "cd ../\n";
                System.out.println(resources);
                writeFile("SauceLabsResources" + dir + ".sh", resources);
            } catch (Exception e) {
                System.out.println("Exception while writing SauceLabsResources.sh " + e.getMessage());
                System.out.println(curlSaveResourceString(className, testName, "selenium-server.log"));
                System.out.println(curlSaveResourceString(className, testName, "video.flv"));
                //          System.out.println(curlSaveResourceString(className, testName, "XXXXscreenshot.png (where XXXX is a number between 0000 and 9999)")); // TODO
            }
        }
    }

    /**
     * Do Suacelabs related teardown things.  Mostly flag the tests as passed or failed.
     * @param passed
     * @param sessionId
     * @param sauceUser
     * @param sauceKey
     * @throws Exception
     */
    public static void tearDown(boolean passed, String sessionId, String sauceUser, String sauceKey) throws Exception {
        if (sessionId != null && System.getProperty(REMOTE_DRIVER_SAUCELABS_PROPERTY) != null) { // dup guard so WebDriverUtil doesn't have to be used
            SauceREST client = new SauceREST(sauceUser, sauceKey);
            /* Using a map of udpates:
            * (http://saucelabs.com/docs/sauce-ondemand#alternative-annotation-methods)
            */
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("passed", passed);
            updates.put("build", System.getProperty(SAUCE_BUILD_PROPERTY, "unknown"));
            client.updateJobInfo(sessionId, updates);

            if (passed) {
                System.out.println("Registering session passed " + sessionId);
                client.jobPassed(sessionId);
            } else {
                System.out.println("Registering session failed " + sessionId);
                client.jobFailed(sessionId);
            }

            Thread.sleep(5000); // give the client message a chance to get processed on saucelabs side
        }
    }

    private String curlSaveResourceString(String className, String testName, String resource) {
        return "curl -o " + deriveResourceBaseNames(className, testName, resource)
                + " -u " + authentication.getUsername() + ":" + authentication.getAccessKey()
                + " http://saucelabs.com/rest/" + authentication.getUsername()+ "/jobs/" + sessionId + "/results/" + resource;
    }

    private String deriveResourceBaseNames(String className, String testName, String resource) {
        return className + "." + testName + "-"
                + System.getProperty(SAUCE_PLATFORM_PROPERTY, Platform.UNIX.toString()) + "-"
                + System.getProperty(SAUCE_BROWSER_PROPERTY) + "-"
                + System.getProperty(SAUCE_VERSION_PROPERTY) + "-"
                + System.getProperty(WebDriverLegacyITBase.REMOTE_PUBLIC_USER_PROPERTY, "admin") + "-"
                + System.getProperty(SAUCE_BUILD_PROPERTY, "unknown_build") + "-"
                + ITUtil.DTS + "-"
                + resource;
    }

    /**
     * Returns the driver
     * @return WebDriver
     */
    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    // Seems like sceenshot downloading has changed, this doesn't work anymore
    private String wgetnSaveResourceString(String className, String testName) {
        String dir = determineSaveDir(className, testName);
        // http://www.jwz.org/hacks/wgetn
        return "wgetn https://saucelabs.com/rest/" + authentication.getUsername()+ "/jobs/"
                + sessionId + "/results/%04dscreenshot.jpg 0 50";
    }

    private String determineSaveDir(String className, String testName) {
        String dir = deriveResourceBaseNames(className, testName, "");
        dir = dir.substring(0, dir.length() -1);
        return dir;
    }

    private void writeFile(String fileName, String content) throws IOException {
        File file = new File(fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.flush();
        bw.close();
    }
}
