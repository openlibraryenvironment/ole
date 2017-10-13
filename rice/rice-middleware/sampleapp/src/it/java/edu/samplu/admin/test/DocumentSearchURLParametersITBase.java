/*
 * Copyright 2006-2012 The Kuali Foundation
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

package edu.samplu.admin.test;

import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverITBase;
import edu.samplu.common.WebDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;
import static com.thoughtworks.selenium.SeleneseTestBase.fail;
import static com.thoughtworks.selenium.SeleneseTestCase.assertEquals;

/**
 * Tests docsearch url parameters
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersITBase extends WebDriverITBase {

    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    private static final String DOCUMENT_TYPE_NAME = "KualiNotification";
    private static final String ADVANCED_SEARCH_ONLY_FIELD = "applicationDocumentId";

    protected static final Map<String, String> CORE_FIELDS = new HashMap<String, String>();
    static {
        // basic
        CORE_FIELDS.put("documentTypeName", DOCUMENT_TYPE_NAME);
        CORE_FIELDS.put("documentId", "1234");
        CORE_FIELDS.put("initiatorPrincipalName", "CURRENT_USER");
        CORE_FIELDS.put("dateCreated", "11/11/11");

    }
    protected static final Map<String, String> BASIC_FIELDS = new HashMap<String, String>();
    static {
        BASIC_FIELDS.putAll(CORE_FIELDS);
        // searchable attrs
        BASIC_FIELDS.put("documentAttribute.notificationContentType", "testType");
        BASIC_FIELDS.put("documentAttribute.notificationChannel", "testChannel");
        BASIC_FIELDS.put("documentAttribute.notificationProducer", "testProducer");
        BASIC_FIELDS.put("documentAttribute.notificationPriority", "testPriority");
        BASIC_FIELDS.put("documentAttribute.notificationRecipients", "testRecipients");
        BASIC_FIELDS.put("documentAttribute.notificationSenders", "testSenders");
        BASIC_FIELDS.put("saveName", "testBasicSearchFields_saved_search");
        BASIC_FIELDS.put("isAdvancedSearch", "NO");
    }

    protected static final Map<String, String> ADVANCED_FIELDS = new HashMap<String, String>();
    static {
        ADVANCED_FIELDS.put("approverPrincipalName", "testApproverName");
        ADVANCED_FIELDS.put("viewerPrincipalName", "testViewerName");
        ADVANCED_FIELDS.put("applicationDocumentId", "testApplicationDocumentId");
        // ADVANCED_FIELDS.put("routeNodeName", "Adhoc Routing");
        ADVANCED_FIELDS.put("dateApproved", "01/01/01");
        ADVANCED_FIELDS.put("dateLastModified", "02/02/02");
        ADVANCED_FIELDS.put("dateFinalized", "03/03/03");
        ADVANCED_FIELDS.put("title", "test title");
        ADVANCED_FIELDS.put("isAdvancedSearch", "YES");
    }

    String getDocSearchURL(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry: params.entrySet()) {
            sb.append(URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue()) + "&");
        }
        return getDocSearchURL(sb.toString());
        
    }
    protected String getDocSearchURL(String params) {
        return ITUtil.getBaseUrlString() + "/kew/DocumentSearch.do?" + params;
    }
    
    private WebElement findElementByTagAndName(String tag, String name) {
        return driver.findElement(By.cssSelector(tag + "[name=" + name + "]"));
    }
    
    private WebElement findInput(String name) {
        return driver.findElement(By.xpath("//input[@name='" + name + "']"));
    }
    
    protected WebElement findModeToggleButton() {
        WebElement toggleAdvancedSearch = null;
        try {
            return driver.findElement(By.id("toggleAdvancedSearch"));
        } catch (NoSuchElementException e) {
            fail("toggleAdvancedSearch button not found");
            return null;
        }
    }

    protected void assertSearchDetailMode(WebElement e, boolean advanced) {
        assertEquals((advanced ? "basic" : "detailed") + " search", e.getAttribute("title"));

        try {
            findInput(ADVANCED_SEARCH_ONLY_FIELD);
            if (!advanced) fail("Advanced search field found in basic search");
        } catch (NoSuchElementException nsee) {
            if (advanced) fail("Advanced search field not found in advancedsearch");
        }
    }

    protected void assertInputValues(Map<String, String> fields) {
        boolean quickmode = false;
        for (Map.Entry<String, String> entry: fields.entrySet()) {
            String value = findInput(entry.getKey()).getAttribute("value");
            assertEquals("Field '" + entry.getKey() + "' expected '" + entry.getValue() + "' got '" + value + "'", entry.getValue(), value);
            if (!quickmode) { // do the first find slow to make sure the screen has finished loading, then do them fast, else some tests take minutes to run
                driver.manage().timeouts().implicitlyWait(WebDriverUtil.SHORT_IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
                quickmode = true;
            }
        }
        if (quickmode) {
            driver.manage().timeouts().implicitlyWait(WebDriverUtil.DEFAULT_IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
        }
    }

    protected void assertInputPresence(Map<String, String> fields, boolean present) {
        boolean quickmode = false;
        for (String name: fields.keySet()) {
            if (present) {
                assertTrue("Expected field '" + name + "' to be present", driver.findElements(By.name(name)).size() != 0);
            } else {
                assertEquals("Expected field '" + name + "' not to be present", 0, driver.findElements(By.name(name)).size());
            }
            if (!quickmode) { // do the first find slow to make sure the screen has finished loading, then do them fast, else some tests take minutes to run
                driver.manage().timeouts().implicitlyWait(WebDriverUtil.SHORT_IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
                quickmode = true;
            }
        }
        if (quickmode) {
            driver.manage().timeouts().implicitlyWait(WebDriverUtil.DEFAULT_IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
        }
    }
}
