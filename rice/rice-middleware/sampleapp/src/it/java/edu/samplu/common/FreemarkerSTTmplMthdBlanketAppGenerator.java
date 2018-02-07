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
import java.io.InputStream;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FreemarkerSTTmplMthdBlanketAppGenerator {
    private static Configuration cfg = new Configuration();

    // Templates for File Generation
    private static String DIR_TMPL = "/Gen/";

    //Configuration
    private static TemplateLoader templateLoader = new ClassTemplateLoader(FreemarkerSTTmplMthdBlanketAppGenerator.class, DIR_TMPL);
    
    public static void main(String[] args) throws Exception {
        cfg.setTemplateLoader(templateLoader);

        String propLocation = "/GenFiles/AdminTmplMthdBlanketAppSTNavBase.properties";
        if (args.length > 0) {
            propLocation = "/GenFiles/" + args[0];
        }

        String template = "TmplMthdBlanketAppSTNavBase.ftl";

        //Here we can prepare a list of template & properties file and can iterate to generate files dynamically on single run.
        createFile(propLocation, template);
    }

    private static void createFile(String propLocation, String template) throws Exception {
        try {
            InputStream in = FreemarkerSTTmplMthdBlanketAppGenerator.class.getResourceAsStream(propLocation);
            File f1 = new File("src" + File.separatorChar + "it" + File.separatorChar + "resources"
                    + File.separatorChar + "GenFiles" + File.separatorChar
                    + propLocation.substring(propLocation.lastIndexOf("/"), propLocation.lastIndexOf("."))
                    + template.substring(0, template.length() - 4) + ".java");
            FreemarkerUtil.ftlWrite(f1, cfg.getTemplate(template), in);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to generate files", e);
        }
    }
}