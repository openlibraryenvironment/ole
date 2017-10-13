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

import edu.samplu.common.ITUtil;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.By;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * tests uploads of new users and group
 *
 * <pre>mvn -f rice-middleware/sampleapp/pom.xml -Pstests failsafe:integration-test -Dremote.public.url=env7.rice.kuali.org -Dit.test=XMLIngester -DXMLIngester.groupId=2008 -DXMLIngester.userIncludeDTSinPrefix=false -DXMLIngester.userCntBegin=0 -DXMLIngester.userCnt=600  -DXMLIngester.userPrefix=loadtester -Dremote.driver.dontTearDown=y</pre>
 *
 * @deprecated XMLIngesterSTJUnitNavGen
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XMLIngester extends AdminTmplMthdSTNavBase {

    protected final Logger LOG = Logger.getLogger(getClass());

    // File generation
    private Configuration cfg;
    private String PROPS_LOCATION = System.getProperty("xmlingester.props.location", null);
    private String DEFAULT_PROPS_LOCATION = "XML/xmlingester.properties";

    // Templates for File Generation
    private static final String DIR_TMPL = "/XML/";
    private static final String TMPL_USER_CONTENT = "SimpleUserContent.ftl";
    private static final String TMPL_GROUP_CONTENT = "SimpleGroupContent.ftl";

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Ignore
    @Override
    public void testCreateNewCancel() throws Exception {}

    @Ignore
    @Override
    public void testEditCancel() throws Exception {}

    @Override
    public void fail(String message) {
        Assert.fail(message);
    }

    @Override
    protected String getLinkLocator() {
        return "XML Ingester";
    }

    @Override
    public String getUserName() {
        return "admin"; // xml ingestion requires admin permissions
    }

    @Override
    public void testSetUp() {
        super.testSetUp();
        // generated load users and group resources
        cfg = new Configuration();
        cfg.setTemplateLoader(new ClassTemplateLoader(getClass().getClassLoader().getClass(), DIR_TMPL));
    }

    private List<File> buildFileUploadList() throws Exception {
        List<File> fileUploadList = new ArrayList<File>();
        try {
            // update properties with timestamp value if includeDTSinPrefix is true
            Properties props = loadProperties(PROPS_LOCATION, DEFAULT_PROPS_LOCATION);
            if(props.get("userIncludeDTSinPrefix") != null
                    && "true".equalsIgnoreCase((String) props.get("userIncludeDTSinPrefix"))) {
                props.setProperty("userPrefix", "" + props.get("userPrefix") + ITUtil.DTS);
            }
            systemPropertiesOverride(props);

            // build files and add to array
            fileUploadList.add(
                    writeTemplateToFile(
                            folder.newFile("loadtest-users.xml"), cfg.getTemplate(TMPL_USER_CONTENT), props));
            fileUploadList.add(
                    writeTemplateToFile(
                            folder.newFile("loadtest-group.xml"), cfg.getTemplate(TMPL_GROUP_CONTENT), props));
        } catch( Exception e) {
            throw new Exception("Unable to generate files for upload", e);
        }
        return fileUploadList;
    }

    /**
     * -DXMLIngester.userCnt=176 will override the userCnt in property files.
     * @param props
     */
    private void systemPropertiesOverride(Properties props) {
        Enumeration<?> names = props.propertyNames();
        Object nameObject;
        String name;
        while (names.hasMoreElements()) {
            nameObject = names.nextElement();
            if (nameObject instanceof String) {
                name = (String)nameObject;
                props.setProperty(name, System.getProperty("XMLIngester." + name, props.getProperty(name)));
            }
        }
    }

    /**
     * Based on load user and groups manual tests; dynamically generates user and group file
     * and loads into the xml ingester screen
     *
     */
    @Test
    public void testXMLIngesterSuccessfulFileUpload() throws Exception {
        List<File> fileUploadList = buildFileUploadList();
        navigate();
        int cnt = 0;
        for(File file : fileUploadList) {
            String path = file.getAbsolutePath().toString();
            driver.findElement(By.name("file[" + cnt + "]")).sendKeys(path);
            cnt++;
        }
        waitAndClickByXpath("//*[@id='imageField']");

        // confirm all files were uploaded successfully
        for(File file: fileUploadList) {
            assertTextPresent("Ingested xml doc: " + file.getName());
        }
        passed();
    }

    /**
     * Loads properties from user defined properties file, if not available uses resource file
     *
     * @return
     * @throws IOException
     */
    private Properties loadProperties(String fileLocation, String resourceLocation) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        if(fileLocation != null) {
            in = new FileInputStream(fileLocation);
        } else {
            in = getClass().getClassLoader().getResourceAsStream(resourceLocation);
        }
        if(in != null) {
            props.load(in);
            in.close();
        }
        return props;
    }

    /**
     * writes processed template  to file
     *
     * @param file
     * @param template
     * @param props
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    private File writeTemplateToFile(File file, Template template, Properties props) throws IOException, TemplateException {
        String output = FreeMarkerTemplateUtils.processTemplateIntoString(template, props);
        LOG.debug("Generated File Output: " + output);
        FileUtils.writeStringToFile(file, output);
        return file;
    }
}
