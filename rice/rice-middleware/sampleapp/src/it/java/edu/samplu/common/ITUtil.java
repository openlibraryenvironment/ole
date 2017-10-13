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

import org.apache.commons.lang.RandomStringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * TODO:
 * <ol>
 *   <li>Keep WebDriver dependencies out of this class, those should be in {@link WebDriverUtil}</li>
 *   <li>Keep JUnit or TestNG dependencies out of in this class.</li>
 *   <li>Extract Hub specific logic?/li>
 *   <li>Rename to SmokeTestUtil or such</li>
 * </ol>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ITUtil {

    /**
     * http://localhost:8080/kr-dev
     */
    public static final String DEFAULT_BASE_URL = "http://localhost:8080/kr-dev";

    /**
     * http://localhost:8080/krad-dev
     */
    public static final String DEFAULT_BASE_URL_KRAD = "http://localhost:8080/krad-dev";

    /**
     * //div[@class='error']"
     */
    public static final String DIV_ERROR_LOCATOR = "//div[@class='error']";

    /**
     * //div[@class='msg-excol']
     */
    public static final String DIV_EXCOL_LOCATOR = "//div[@class='msg-excol']";

    /**
     * remote.driver.dontTearDown
     */
    public static final String DONT_TEAR_DOWN_PROPERTY = "remote.driver.dontTearDown";

    /**
     * Calendar.getInstance().getTimeInMillis() + ""
     */
    public static final String DTS = Calendar.getInstance().getTimeInMillis() + "";

    /**
     * Calendar.getInstance().getTimeInMillis() + "" + RandomStringUtils.randomAlphabetic(2).toLowerCase()
     * @Deprecated {@link ITUtil#createUniqueDtsPlusTwoRandomChars()}
     */
    public static final String DTS_TWO = Calendar.getInstance().getTimeInMillis() + "" + RandomStringUtils.randomAlphabetic(2).toLowerCase();

    /**
     *  &hideReturnLink=true
     */
    public static final String HIDE_RETURN_LINK =  "&hideReturnLink=true";

    /**
     *  &hideReturnLink=false
     */
    public static final String HIDE_RETURN_LINK_FALSE =  "&hideReturnLink=false";

    /**
     * remote.public.hub
     */
    public static final String HUB_PROPERTY = "remote.public.hub";

    /**
     * remote.public.driver
     */
    public static final String HUB_DRIVER_PROPERTY = "remote.public.driver";

    /**
     * http://localhost:4444/wd/hub
     */
    public static final String HUB_URL_PROPERTY = "http://localhost:4444/wd/hub";

    /**
     * /kr-krad/lookup?methodToCall=start&dataObjectClassName=
     */
    public static final String KRAD_LOOKUP_METHOD =  "/kr-krad/lookup?methodToCall=start&dataObjectClassName=";

    /**
     * /kr/lookup.do?methodToCall=start&businessObjectClassName=
     */
    public static final String KNS_LOOKUP_METHOD =  "/kr/lookup.do?methodToCall=start&businessObjectClassName=";

    /**
     * /kr-krad/kradsampleapp?viewId=KradSampleAppHome
     */
    public static final String KRAD_PORTAL = "/kr-krad/kradsampleapp?viewId=KradSampleAppHome";

    /**
     * /kr-krad/kradsampleapp?viewId=KradSampleAppHome
     */
    public static final String KRAD_PORTAL_URL = ITUtil.getBaseUrlString() + KRAD_PORTAL;

    /**
     * /kr-krad/labs?viewId=LabsMenuView
     */
    public static final  String LABS = "/kr-krad/labs?viewId=LabsMenuView";

    /**
     * ITUtil.getBaseUrlString() + LABS
     */
    public static final String LABS_URL = ITUtil.getBaseUrlString() + LABS;

    /**
     * /portal.do
     */
    public static final String PORTAL = "/portal.do";

    /**
     * ITUtil.getBaseUrlString() + ITUtil.PORTAL
     */
    public static final String PORTAL_URL =  ITUtil.getBaseUrlString() + ITUtil.PORTAL;

    /**
     * URLEncoder.encode(PORTAL_URL)
     */
    public static final String PORTAL_URL_ENCODED = URLEncoder.encode(PORTAL_URL);

    /**
     *  &showMaintenanceLinks=true
     */
    public static final String SHOW_MAINTENANCE_LINKS =  "&showMaintenanceLinks=true";

    /**
     * remote.public.url
     */
    public static final String REMOTE_PUBLIC_URL_PROPERTY = "remote.public.url";

    /**
     * remote.autologin
     */
    public static final String REMOTE_AUTOLOGIN_PROPERTY = "remote.autologin";

    /**
     * KRAD
     */
    public static final String REMOTE_UIF_KRAD = "KRAD";

    /**
     * KNS
     */
    public static final String REMOTE_UIF_KNS  = "KNS";

    /**
     * &docFormKey=
     */
    public static final String DOC_FORM_KEY = "&docFormKey=";
    

    public static String blanketApprovalCleanUpErrorText(String errorText) {
        errorText = errorText.replace("* required field", "").replace("\n", " ").trim(); // bit of extra ui text we don't care about
        return errorText;
    }

    public static String createUniqueDtsPlusTwoRandomChars() {
        return Calendar.getInstance().getTimeInMillis() + "" + RandomStringUtils.randomAlphabetic(2).toLowerCase();
    }

    // The Document Description contains 9 continuous digits or 9 digits grouped in the following pattern: ###-##-####, which may represent a Tax Number.
    // The Document Description is not secure and its contents may be viewed by other application users. Please revise the Document Description to not contain digits in those patterns.
    public static String createUniqueDtsPlusTwoRandomCharsNot9Digits() {
        String dtsTwo = ITUtil.createUniqueDtsPlusTwoRandomChars();
        dtsTwo = dtsTwo.substring(0, 5) + dtsTwo.substring(13, 14) + dtsTwo.substring(6, 12);
        return dtsTwo;
    }

    protected static void checkForIncidentReport(String contents, String linkLocator, Failable failable, String message) {
        if (contents == null) { //guard clause
            return;
        }

        if (incidentReported(contents)) {
            try {
                processIncidentReport(contents, linkLocator, failable, message);
            } catch (IndexOutOfBoundsException e) {
                failable.fail(
                        "\nIncident report detected "
                                + message
                                + " but there was an exception during processing: "
                                + e.getMessage()
                                + "\nStack Trace from processing exception"
                                + stackTrace(e)
                                + "\nContents that triggered exception: "
                                + deLinespace(contents));
            }
        }

        if (contents.contains("HTTP Status 404")) {
            failable.fail("\nHTTP Status 404 " + linkLocator + " " + message + " " + "\ncontents:" + contents);
        }

        if (contents.contains("Java backtrace for programmers:") || contents.contains("Java stack trace (for programmers):")) { // freemarker exception
            try {
                processFreemarkerException(contents, linkLocator, failable, message);
            } catch (IndexOutOfBoundsException e) {
                failable.fail("\nFreemarker exception detected "
                        + message
                        + " but there was an exception during processing: "
                        + e.getMessage()
                        + "\nStack Trace from processing exception"
                        + stackTrace(e)
                        + "\nContents that triggered exception: "
                        + deLinespace(contents));
            }
        }

        if (contents.contains("Document Expired")) { // maybe Firefox specific
            failable.fail("Document Expired message.");
        }
    }

    public static String deLinespace(String contents) {
        while (contents.contains("\n\n")) {
            contents = contents.replaceAll("\n\n", "\n");
        }
        return contents;
    }

    /**
     * Setting the JVM arg remote.driver.dontTearDown to y or t leaves the browser window open when the test has completed.  Valuable when debugging, updating, or creating new tests.
     * When implementing your own tearDown method rather than an inherited one, it is a common courtesy to include this check and not stop and shutdown the browser window to make it easy debug or update your test.
     * {@code }
     * @return true if the dontTearDownProperty is not set.
     */
    public static boolean dontTearDownPropertyNotSet() {
        return System.getProperty(DONT_TEAR_DOWN_PROPERTY) == null ||
                "f".startsWith(System.getProperty(DONT_TEAR_DOWN_PROPERTY).toLowerCase()) ||
                "n".startsWith(System.getProperty(DONT_TEAR_DOWN_PROPERTY).toLowerCase());
    }

    private static String extractIncidentReportInfo(String contents, String linkLocator, String message) {
        String chunk =  contents.substring(contents.indexOf("Incident Feedback"), contents.lastIndexOf("</div>") );
        String docId = chunk.substring(chunk.lastIndexOf("Document Id"), chunk.indexOf("View Id"));
        docId = docId.substring(0, docId.indexOf("</span>"));
        docId = docId.substring(docId.lastIndexOf(">") + 2, docId.length());

        String viewId = chunk.substring(chunk.lastIndexOf("View Id"), chunk.indexOf("Error Message"));
        viewId = viewId.substring(0, viewId.indexOf("</span>"));
        viewId = viewId.substring(viewId.lastIndexOf(">") + 2, viewId.length());

        String stackTrace = chunk.substring(chunk.lastIndexOf("(only in dev mode)"), chunk.length());
        stackTrace = stackTrace.substring(stackTrace.indexOf("<pre>") + 5, stackTrace.length());
        stackTrace = stackTrace.substring(0, stackTrace.indexOf("</"));

        //            System.out.println(docId);
        //            System.out.println(viewId);
        //            System.out.println(stackTrace);
        return "\nIncident report "
                + message
                + " navigating to "
                + linkLocator
                + " : View Id: "
                + viewId.trim()
                + " Doc Id: "
                + docId.trim()
                + "\nStackTrace: "
                + stackTrace.trim().replace(" at ", "");
    }

    private static String extractIncidentReportKim(String contents, String linkLocator, String message) {
        String chunk =  contents.substring(contents.indexOf("id=\"headerarea\""), contents.lastIndexOf("</div>") );
        String docIdPre = "type=\"hidden\" value=\"";
        String docId = chunk.substring(chunk.indexOf(docIdPre) + docIdPre.length(), chunk.indexOf("\" name=\"documentId\""));

        String stackTrace = chunk.substring(chunk.lastIndexOf("name=\"displayMessage\""), chunk.length());
        String stackTracePre = "value=\"";
        stackTrace = stackTrace.substring(stackTrace.indexOf(stackTracePre) + stackTracePre.length(), stackTrace.indexOf("name=\"stackTrace\"") - 2);

        return "\nIncident report "
                + message
                + " navigating to "
                + linkLocator
                + " Doc Id: "
                + docId.trim()
                + "\nStackTrace: "
                + stackTrace.trim().replace(" at ", "");
    }

    public static void failOnInvalidUserName(String userName, String contents, Failable failable) {
        if (contents.indexOf("Invalid") > -1) {
            failable.fail("Invalid Login " + userName);
        }
    }
/*
    public static void failOnMatchedJira(String contents) {
        Iterator<String> iter = jiraMatches.keySet().iterator();
        String key = null;

        while (iter.hasNext()) {
            key = iter.next();
            if (contents.contains(key)) {
                SeleneseTestBase.fail(JIRA_BROWSE_URL + jiraMatches.get(key));
            }
        }
    }
*/

    private static void failWithReportInfo(String contents, String linkLocator, Failable failable, String message) {
        final String incidentReportInformation = extractIncidentReportInfo(contents, linkLocator, message);
        failable.fail(incidentReportInformation);
    }

/*
    private static void failWithReportInfoForKim(String contents, String linkLocator, String message) {
        final String kimIncidentReport = extractIncidentReportKim(contents, linkLocator, message);
        SeleneseTestBase.fail(kimIncidentReport);
    }
*/
    private static void failWithReportInfoForKim(String contents, String linkLocator, Failable failable, String message) {
        final String kimIncidentReport = extractIncidentReportKim(contents, linkLocator, message);
        failable.fail(kimIncidentReport);
    }

    /**
     * In order to run as a smoke test the ability to set the baseUrl via the JVM arg remote.public.url is required.
     * Trailing slashes are trimmed.  If the remote.public.url does not start with http:// it will be added.
     * @return http://localhost:8080/kr-dev by default else the value of remote.public.url
     */
    public static String getBaseUrlString() {
        String baseUrl = System.getProperty(REMOTE_PUBLIC_URL_PROPERTY);
        if (baseUrl == null) {
            baseUrl = DEFAULT_BASE_URL;
        }
        baseUrl = prettyHttp(baseUrl);
        return baseUrl;
    }

    public static String getHTML(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";

        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * In order to run as a smoke test under selenium grid the ability to set the hubUrl via the JVM arg remote.public.hub is required.
     * Trailing slashes are trimmed.  If the remote.public.hub does not start with http:// it will be added.
     * @return http://localhost:4444/wd/hub by default else the value of remote.public.hub
     */
    public static String getHubUrlString() {
        String hubUrl = System.getProperty(HUB_PROPERTY);
        if (hubUrl == null) {
            hubUrl = HUB_URL_PROPERTY;
        }
        hubUrl = prettyHttp(hubUrl);
        if (!hubUrl.endsWith("/wd/hub")) {
            hubUrl = hubUrl + "/wd/hub";
        }
        return hubUrl;
    }

    private static boolean incidentReported(String contents) {
        return contents != null &&
                contents.contains("Incident Report") &&
                !contents.contains("portal.do?channelTitle=Incident%20Report") && // Incident Report link on sampleapp KRAD tab
                !contents.contains("portal.do?channelTitle=Incident Report") &&   // Incident Report link on sampleapp KRAD tab IE8
                !contents.contains("uitest?viewId=Travel-testView2") &&
                !contents.contains("SeleniumException"); // selenium timeouts have Incident Report in them
    }

    /**
     * Append http:// if not present.  Remove trailing /
     * @param baseUrl
     * @return
     */
    public static String prettyHttp(String baseUrl) {

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        if (!baseUrl.startsWith("http")) {
            baseUrl = "http://" + baseUrl;
        }

        return baseUrl;
    }

    private static void processFreemarkerException(String contents, String linkLocator, Failable failable, String message) {
        JiraAwareFailureUtil.failOnMatchedJira(contents, failable);
        String stackTrace = contents.substring(contents.indexOf("Error: on line"), contents.indexOf("more<") - 1);
        failable.fail(
                "\nFreemarker Exception " + message + " navigating to " + linkLocator + "\nStackTrace: " + stackTrace
                        .trim().replace(" at ", ""));
    }

/*
    private static void processIncidentReport(String contents, String linkLocator, String message) {
        failOnMatchedJira(contents);

        if (contents.indexOf("Incident Feedback") > -1) {
            failWithReportInfo(contents, linkLocator, message);
        }

        if (contents.indexOf("Incident Report") > -1) { // KIM incident report
            failWithReportInfoForKim(contents, linkLocator, message);
        }

        SeleneseTestBase.fail("\nIncident report detected " + message + "\n Unable to parse out details for the contents that triggered exception: " + deLinespace(
                contents));
    }

    private static void failWithReportInfo(String contents, String linkLocator, String message) {
        final String incidentReportInformation = extractIncidentReportInfo(contents, linkLocator, message);
        SeleneseTestBase.fail(incidentReportInformation);
    }
*/

    protected static void processIncidentReport(String contents, String linkLocator, Failable failable, String message) {
        JiraAwareFailureUtil.failOnMatchedJira(contents, failable);

        if (contents.indexOf("Incident Feedback") > -1) {
            failWithReportInfo(contents, linkLocator, failable, message);
        }

        if (contents.indexOf("Incident Report") > -1) { // KIM incident report
            failWithReportInfoForKim(contents, linkLocator, failable, message);
        }

        failable.fail("\nIncident report detected "
                + message
                + "\n Unable to parse out details for the contents that triggered exception: "
                + deLinespace(contents));
    }

    /**
     * Write the given stack trace into a String remove the ats in an attempt to not cause Jenkins problems.
     * @param throwable whose stack trace to return
     * @return String of the given throwable's stack trace.
     */
    public static String stackTrace(Throwable throwable) {
        StringWriter wrt = new StringWriter();
        PrintWriter pw = new PrintWriter(wrt);
        throwable.printStackTrace(pw);
        pw.flush();
        return wrt.toString().replace(" at " ,"");
    }
}
