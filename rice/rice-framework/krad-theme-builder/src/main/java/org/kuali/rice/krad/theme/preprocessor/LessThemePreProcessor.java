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
package org.kuali.rice.krad.theme.preprocessor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.theme.util.ThemeBuilderConstants;
import org.kuali.rice.krad.theme.util.ThemeBuilderUtils;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
import org.lesscss.LessSource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Pre processor that picks up Less files in the theme directory and compiles to CSS
 *
 * <p>
 * Less files are compiled using the Apache lesscss compiler
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.lesscss.LessCompiler
 */
public class LessThemePreProcessor implements ThemePreProcessor {
    private static final Logger LOG = Logger.getLogger(LessThemePreProcessor.class);

    /**
     * Processes Less files that should be included for the given theme
     *
     * <p>
     * The list of Less files for the theme is collected by a helper method then iterated over and compiled
     * using the less compiler. The list of Less files that were processed is written as a property in the theme
     * properties for direct Less support in development mode
     * </p>
     *
     * @param themeName name of the theme to process
     * @param themeDirectory directory containing the theme assets
     * @param themeProperties properties for the theme containing its configuration
     * @see #getLessFileNamesForTheme(java.lang.String, java.io.File, java.util.Properties, java.io.File)
     */
    public void processTheme(String themeName, File themeDirectory, Properties themeProperties) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Performing Less compilation for theme " + themeName);
        }

        File stylesheetsDirectory = new File(themeDirectory, ThemeBuilderConstants.ThemeDirectories.STYLESHEETS);
        if (!stylesheetsDirectory.exists()) {
            throw new RuntimeException("Stylesheets directory does not exist for theme: " + themeName);
        }

        List<String> lessFileNames = getLessFileNamesForTheme(themeName, themeDirectory, themeProperties,
                stylesheetsDirectory);

        LessCompiler lessCompiler = new LessCompiler();

        // not compressing here since that will be done later in the build process
        lessCompiler.setCompress(false);

        for (String lessFileName : lessFileNames) {
            LOG.info("compiling less file: " + lessFileName);

            File sourceLessFile = new File(stylesheetsDirectory, lessFileName);
            File compiledLessFile = new File(stylesheetsDirectory, lessFileName.replace(
                    ThemeBuilderConstants.FileExtensions.LESS, ThemeBuilderConstants.FileExtensions.CSS));

            try {
                LessSource lessSource = new LessSource(sourceLessFile);

                lessCompiler.compile(lessSource, compiledLessFile, true);
            } catch (IOException e) {
                throw new RuntimeException("Error while compiling LESS source: " + lessFileName, e);
            } catch (LessException e) {
                throw new RuntimeException("Error while compiling LESS source: " + lessFileName, e);
            }
        }

        // add list of less files to theme properties so it can be picked up by the view theme
        // runtime in development mode
        themeProperties.put(ThemeBuilderConstants.DerivedConfiguration.THEME_LESS_FILES,
                StringUtils.join(lessFileNames, ","));
    }

    /**
     * Builds the list of Less files names that should be processed for the given theme
     *
     * <p>
     * All files with the <code>.less</code> extension that are in the theme's <code>stylesheets</code>
     * directory are picked up as part of the theme (this includes files that are overlaid from a parent).
     *
     * All subdirectories of stylesheets are also picked up, with the exception of the <code>include</code>
     * subdirectory. Other exclusions can be configured using the <code>lessExcludes</code> property in the
     * theme's properties file
     * </p>
     *
     * @param themeName name of the theme to pull less files for
     * @param themeDirectory directory containing the theme's assets
     * @param themeProperties config properties for the theme
     * @param stylesheetsDirectory theme directory which contains the stylesheets, less files will be
     * picked up here
     * @return list of less file names (any path is relative to the stylesheets directory)
     */
    protected List<String> getLessFileNamesForTheme(String themeName, File themeDirectory, Properties themeProperties,
            File stylesheetsDirectory) {
        String[] lessIncludes = ThemeBuilderUtils.getPropertyValueAsArray(
                ThemeBuilderConstants.ThemeConfiguration.LESS_INCLUDES, themeProperties);

        if ((lessIncludes == null) || (lessIncludes.length == 0)) {
            lessIncludes = new String[1];

            lessIncludes[0] = ThemeBuilderConstants.Patterns.ANT_MATCH_ALL + ThemeBuilderConstants.FileExtensions.LESS;
        }

        String[] lessExcludes = ThemeBuilderUtils.getPropertyValueAsArray(
                ThemeBuilderConstants.ThemeConfiguration.LESS_EXCLUDES, themeProperties);

        lessExcludes = ThemeBuilderUtils.addToArray(lessExcludes,
                ThemeBuilderConstants.ThemeDirectories.INCLUDES + ThemeBuilderConstants.Patterns.ANT_MATCH_DIR);

        return ThemeBuilderUtils.getDirectoryContents(stylesheetsDirectory, lessIncludes, lessExcludes);
    }
}
