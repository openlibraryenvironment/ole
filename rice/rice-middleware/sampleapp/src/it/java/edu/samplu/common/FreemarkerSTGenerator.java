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
public class FreemarkerSTGenerator {
    private static Configuration cfg = new Configuration();

    // Templates for File Generation
    private static String DIR_TMPL = "/Gen/";

    //Configuration
    private static TemplateLoader templateLoader = new ClassTemplateLoader(FreemarkerSTGenerator.class, DIR_TMPL);

    private static String STJUNITBASE_TMPL = "STJUnitBase.ftl";
    private static String STJUNITBKMRKGEN_TMPL = "STJUnitBkMrkGen.ftl";
    private static String STJUNITNAVGEN_TMPL = "STJUnitNavGen.ftl";
    private static String STNGBASE_TMPL = "STNGBase.ftl";
    private static String STNGBKMRKGEN_TMPL = "STNGBkMrkGen.ftl";
    private static String STNGNAVGEN_TMPL = "STNGNavGen.ftl";

    public static void main(String[] args) throws Exception {
        cfg.setTemplateLoader(templateLoader);

        String propsLocation = "/GenFiles/Group.properties";
        if (args.length > 0) {
            propsLocation = "/GenFiles/" + args[0];
        }

        //Here we can prepare a list of template & properties file and can iterate to generate files dynamically on single run.
        createFile(propsLocation, STJUNITBASE_TMPL);
        createFile(propsLocation, STJUNITBKMRKGEN_TMPL);
        createFile(propsLocation, STJUNITNAVGEN_TMPL);
        createFile(propsLocation, STNGBASE_TMPL);
        createFile(propsLocation, STNGBKMRKGEN_TMPL);
        createFile(propsLocation, STNGNAVGEN_TMPL);
    }

    private static void createFile(String propLocation, String template) throws Exception {
        try {
            InputStream in = FreemarkerSTGenerator.class.getResourceAsStream(propLocation);
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