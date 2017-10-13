/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.theme.postprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krad.theme.ThemeTestConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

/**
 * Test cases for {@link org.kuali.rice.krad.theme.postprocessor.ThemeCssFilesProcessor}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ThemeCssFilesProcessorTest {

    protected ThemeCssFilesProcessor cssFilesProcessor;

    @Before
    public void setUp() throws Exception {
        cssFilesProcessor = new ThemeCssFilesProcessor(ThemeTestConstants.THEME_NAME,
                ThemeTestConstants.THEME_DIRECTORY, new Properties(), new HashMap<String, File>(),
                ThemeTestConstants.WORKING_DIR, ThemeTestConstants.PROJECT_VERSION);
    }

    /**
     * Test URLs are correctly rewritten within a CSS string
     *
     * @throws Exception
     */
    @Test
    public void testRewriteCssUrls() throws Exception {
        String cssString = "#fancybox-loading, .fancybox-close, .fancybox-prev span, .fancybox-next span {\n"
                + "\tbackground-image: url('fancybox_sprite.png');\n"
                + "} " + ".infoGrowl{\n"
                + "\tbackground: url(\"images/information-frame.png\") no-repeat scroll 5px 5px transparent;\n"
                + "\tpadding-left: 25px;\n"
                + "}";

        String expectedCssString = "#fancybox-loading, .fancybox-close, .fancybox-prev span, .fancybox-next span {\n"
                + "\tbackground-image: url('../subDir2/fancybox_sprite.png');\n"
                + "} " + ".infoGrowl{\n"
                + "\tbackground: url(\"../subDir2/images/information-frame.png\") no-repeat scroll 5px 5px transparent;\n"
                + "\tpadding-left: 25px;\n"
                + "}";

        File mergedFile = new File("/basedir/subDir1/merge.css");
        File mergeFile = new File("/basedir/subDir2/foo.css");

        String rewriteCssString = cssFilesProcessor.rewriteCssUrls(cssString, mergeFile, mergedFile);

        Assert.assertEquals("Url not rewritten correctly in css string", expectedCssString, rewriteCssString);
    }
}
