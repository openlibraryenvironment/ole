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
package edu.samplu.admin.test;

import edu.samplu.common.Failable;
import edu.samplu.common.FreemarkerSTBase;
import edu.samplu.common.ITUtil;
import edu.samplu.common.PropertiesUtils;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Tests uploads of new users and group.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public abstract class XMLIngesterAbstractSmokeTestBase extends FreemarkerSTBase {

    /**
     * http://env12.rice.kuali.org/portal.do?channelTitle=XML%20Ingester&channelUrl=http://env12.rice.kuali.org/kew/../core/Ingester.do
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=XML%20Ingester&channelUrl="
            + ITUtil.getBaseUrlString() + "/kew/../core/Ingester.do";

    // File generation
    private String PROPS_LOCATION = System.getProperty("xmlingester.props.location", null);
    private static final String DEFAULT_PROPS_LOCATION = "XML/xmlingester.properties";

    // Templates for File Generation
    private static final String DIR_TMPL = "/XML/";
    private static final String TMPL_USER_CONTENT = "SimpleUserContent.ftl";
    private static final String TMPL_GROUP_CONTENT = "SimpleGroupContent.ftl";

    /**
     *
     * @param name of temp file to create
     * @return tempFile
     * @throws IOException
     */
    protected abstract File newTempFile(String name) throws IOException;

    /**
     * {@inheritDoc}
     * {@link #DIR_TMPL}
     * @return
     */
    @Override
    protected String getTemplateDir() {
        return DIR_TMPL;
    }

    /**
     * Nav tests start at {@link ITUtil#PORTAL}.  Bookmark Tests should override and return {@link XMLIngesterAbstractSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    /**
     * "admin" xml ingestion requires admin permissions.
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getUserName() {
        return "admin";
    }

    /**
     * go to the getMenuLinkLocator() Menu and click the getLinkLocator()
     */
    protected void navigate(Failable failable) throws Exception {
        selectTopFrame();
        waitAndClickAdministration(failable);
        waitForTitleToEqualKualiPortalIndex();
        waitAndClickXMLIngester(failable);
        selectFrameIframePortlet();
        checkForIncidentReport("XML Ingester", failable, "");
    }

    /**
     * Navigate to the page under test and call {@link #testIngestion}
     *
     * @param failable {@link edu.samplu.common.Failable}
     * @throws Exception
     */
    protected void testIngestionNav(Failable failable) throws Exception {
        navigate(failable);
        testIngestion(failable);
        passed();
    }

    protected void testIngestionBookmark(Failable failable) throws Exception {
        testIngestion(failable);
        passed();
    }

    /**
     * Based on load user and groups manual tests; dynamically generates user and group file
     * and loads into the xml ingester screen.
     * This test should suffice for both KRAD and KNS versions of the ingester screen.
     *
     *
     */
    protected void testIngestion(Failable failable) throws Exception {
        selectFrameIframePortlet();
        List<File> fileUploadList = buildFileUploadList();
        int cnt = 0;

        for(File file : fileUploadList) {
            String path = file.getAbsolutePath().toString();
            if (isKrad()){
                driver.findElement(By.name("files[" + cnt + "]")).sendKeys(path);
            } else {
                driver.findElement(By.name("file[" + cnt + "]")).sendKeys(path);
            }
            cnt++;
        }

        // Click the Upload Button
        if (isKrad()){
            waitAndClickByXpath("//button");
        } else {
            waitAndClickByXpath("//*[@id='imageField']");
        }

        // confirm all files were uploaded successfully
        Thread.sleep(1000);
        for(File file: fileUploadList) {
            assertTextPresent("Ingested xml doc: " + file.getName());
        }
    }

    protected List<File> buildFileUploadList() throws Exception {
        List<File> fileUploadList = new ArrayList<File>();
        try {
            // update properties with timestamp value if includeDTSinPrefix is true
            Properties props = loadProperties(PROPS_LOCATION, DEFAULT_PROPS_LOCATION);
            if(props.get("userIncludeDTSinPrefix") != null
                    && "true".equalsIgnoreCase((String) props.get("userIncludeDTSinPrefix"))) {
                props.setProperty("userPrefix", "" + props.get("userPrefix") + ITUtil.DTS);
            }
            PropertiesUtils.systemPropertiesOverride(props, "XMLIngester");

            // build files and add to array
            fileUploadList.add(
                    writeTemplateToFile(newTempFile("loadtest-users.xml"), cfg.getTemplate(TMPL_USER_CONTENT), props));
            fileUploadList.add(
                    writeTemplateToFile(newTempFile("loadtest-group.xml"), cfg.getTemplate(TMPL_GROUP_CONTENT), props));
        } catch( Exception e) {
            throw new Exception("Unable to generate files for upload", e);
        }

        return fileUploadList;
    }
}
