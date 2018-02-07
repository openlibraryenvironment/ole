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

import java.io.File;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO Setup as command line tool or implement gold standard/acceptance testing for the templated result.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NeustarJSTemplate extends FreemarkerSTBase {

    @Override
    public void fail(String message) {
        Assert.fail(message);
    }

    /**
     * This is ugly, I'm doing it to remove the loading property duplication, we should probably turn this class into a
     * command line tool and do extractions to support that, but isn't the current focus...
     * @return
     */
    @Override
    public String getTestUrl() {
        return null;
    }

    // File generation
    private String PROPS_LOCATION = System.getProperty("neustarJS.props.location", null);
    private String DEFAULT_PROPS_LOCATION = "NeustarJSTemplate/neustarJS.properties";

    // Templates for File Generation
    private static final String DIR_TMPL = "/NeustarJSTemplate/";
    private static final String TMPL_CONTENT = "CreateNewTmpl.ftl";

    private void buildFileList(Properties props) throws Exception {
        Integer pageCount= Integer.parseInt(props.getProperty("pageCount"));
        
        for(int count=1; count<= pageCount;count++ ){
            try {
                String subTitle= props.getProperty("page"+count);
                props.setProperty("pageId",""+ props.get("page")+count);
                
                // Setting props and building files of KRAD tab
                props.setProperty("viewId",""+ props.get("view"));                          
                File f1= new File("Temp" + File.separatorChar + "Env11 Kitchen Sink "+subTitle +" KRAD WebDriver.txt");
                writeTemplateToFile(f1, cfg.getTemplate(TMPL_CONTENT), props);

                // Setting props and building files of KRAD tab
                props.setProperty("viewId",""+ props.get("view"));
                File f2= new File("Temp" + File.separatorChar + "Env11 Kitchen Sink "+subTitle +" KNS WebDriver.txt");
                writeTemplateToFile(f2, cfg.getTemplate(TMPL_CONTENT), props);

            } catch( Exception e) {
                throw new Exception("Unable to generate files for upload", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     * {@link #DIR_TMPL}
     * @return
     */
    @Override
    protected String getTemplateDir() {
        return DIR_TMPL;
    }

    @Test
    public void testNeustarTemplating() throws Exception {
        // update properties with timestamp value if includeDTSinPrefix is true
        Properties props = loadProperties(PROPS_LOCATION, DEFAULT_PROPS_LOCATION);
        PropertiesUtils.systemPropertiesOverride(props, "NeustarJS");
        //Generate Files
        buildFileList(props);
        // TODO gold standard or acceptance testing on generated file.
    }
}
