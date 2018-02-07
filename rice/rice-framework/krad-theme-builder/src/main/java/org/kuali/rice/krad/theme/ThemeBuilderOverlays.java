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
package org.kuali.rice.krad.theme;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.FileUtils;
import org.kuali.common.util.Assert;
import org.kuali.rice.krad.theme.util.ThemeBuilderConstants;
import org.kuali.rice.krad.theme.util.ThemeBuilderUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Helper class for {@link ThemeBuilder} that performs the various overlays during the build process
 *
 * <p>
 * There are three main overlay (copy) processes executed during the theme building:
 *
 * <ol>
 * <li>Copy related assets from web app source to the configured output directory</li>
 * <li>Overlay assets from a parent theme to a child theme</li>
 * <li>Perform any additional overlays that are configured in a theme's properties file</li>
 * </ol>
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ThemeBuilderOverlays {
    private static final Logger LOG = Logger.getLogger(ThemeBuilder.class);

    /**
     * Invoked at the beginning of the build process to move assets from the web source directory to the
     * output directory, where they can be further processed
     *
     * <p>
     * Note: Not all web resources are copied, just the assets that are needed to build all themes. This includes
     * all the theme directories, plugin directories, and KRAD scripts directory
     * </p>
     *
     * @param webappSourceDir absolute path to the web source directory
     * @param themeBuilderOutputDir absolute path to the target directory, where assets will be copied
     * to and processed. If the directory does not exist it will be created
     * @param additionalThemeDirectories list of additional theme paths that should be copied to
     * the output directory
     * @param additionalPluginDirectories list of additional plugin paths that should be copied to
     * the output directory
     * @throws IOException
     */
    protected static void copyAssetsToWorkingDir(String webappSourceDir, String themeBuilderOutputDir,
            List<String> additionalThemeDirectories, List<String> additionalPluginDirectories)
            throws IOException {
        Assert.hasText(themeBuilderOutputDir, "Working directory for theme builder not set");

        File webappSource = new File(webappSourceDir);
        if (!webappSource.exists()) {
            throw new RuntimeException("Webapp source directory does not exist");
        }

        File workingDir = new File(themeBuilderOutputDir);
        if (!workingDir.exists()) {
            workingDir.mkdir();
        }

        workingDir.setWritable(true);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Copying script, theme, plugin resource to working dir: " + themeBuilderOutputDir);
        }

        ThemeBuilderUtils.copyDirectory(
                webappSourceDir + ThemeBuilderConstants.KRAD_SCRIPTS_DIRECTORY,
                themeBuilderOutputDir + ThemeBuilderConstants.KRAD_SCRIPTS_DIRECTORY);

        ThemeBuilderUtils.copyDirectory(
                webappSourceDir + ThemeBuilderConstants.DEFAULT_THEMES_DIRECTORY,
                themeBuilderOutputDir + ThemeBuilderConstants.DEFAULT_THEMES_DIRECTORY);

        if (additionalThemeDirectories != null) {
            for (String additionalThemeDirectory : additionalThemeDirectories) {
                ThemeBuilderUtils.copyDirectory(webappSourceDir + additionalThemeDirectory,
                        themeBuilderOutputDir + additionalThemeDirectory);
            }
        }

        ThemeBuilderUtils.copyDirectory(
                webappSourceDir + ThemeBuilderConstants.DEFAULT_PLUGINS_DIRECTORY,
                themeBuilderOutputDir + ThemeBuilderConstants.DEFAULT_PLUGINS_DIRECTORY);

        if (additionalPluginDirectories != null) {
            for (String additionalPluginDirectory : additionalPluginDirectories) {
                ThemeBuilderUtils.copyDirectory(webappSourceDir + additionalPluginDirectory,
                        themeBuilderOutputDir + additionalPluginDirectory);
            }
        }
    }

    /**
     * Overlays assets from a parent theme (if there is a parent) to a child theme
     *
     * <p>
     * If the given theme has a parent (determined by the parent property in the theme properties), all files
     * from the parent theme directory are copied to the given theme directory unless:
     *
     * <ul>
     * <li>A file exists in the child theme directory with the same name and relative path</li>
     * <li>The files has been exluded through the property <code>parentExcludes</code></li>
     * </ul>
     * </p>
     *
     * @param themeName name of the theme to be processed
     * @param themeDirectory directory for the theme (parent assets will be copied here)
     * @param themeProperties properties for the theme, used to retrieve the parent configuration and the
     * parent excludes
     * @param themeNamePathMapping mapping of theme names to theme paths, used to find the parent theme
     * directory path
     */
    protected static void overlayParentAssets(String themeName, File themeDirectory, Properties themeProperties,
            Map<String, String> themeNamePathMapping) {
        if (!themeProperties.containsKey(ThemeBuilderConstants.ThemeConfiguration.PARENT)) {
            return;
        }

        String parentThemeName = themeProperties.getProperty(ThemeBuilderConstants.ThemeConfiguration.PARENT);
        if (StringUtils.isBlank(parentThemeName)) {
            return;
        }

        LOG.info("Overlaying assets from parent " + parentThemeName + " to child " + themeName);

        String[] parentExcludes = ThemeBuilderUtils.getPropertyValueAsArray(
                ThemeBuilderConstants.ThemeConfiguration.PARENT_EXCLUDES, themeProperties);

        String parentThemePath = themeNamePathMapping.get(parentThemeName);

        File parentThemeDirectory = new File(parentThemePath);
        if (!parentThemeDirectory.exists()) {
            throw new RuntimeException("Parent theme does not exist at path: " + parentThemePath);
        }

        List<String> copyDirectoryExcludes = new ArrayList<String>();
        copyDirectoryExcludes.add(ThemeBuilderConstants.THEME_PROPERTIES_FILE);

        if (parentExcludes != null) {
            copyDirectoryExcludes.addAll(Arrays.asList(parentExcludes));
        }

        try {
            ThemeBuilderUtils.copyMissingContent(parentThemeDirectory, themeDirectory, copyDirectoryExcludes);
        } catch (IOException e) {
            throw new RuntimeException("Unable to copy parent theme directory", e);
        }
    }

    /**
     * Performs any additional overlays that have been configured for the theme (with the
     * <code>additionalOverlays</code> property)
     *
     * <p>
     * Additional overlays can take any directory or file from the web application, and move into the theme directory
     * or one of its subdirectores. This is useful if there are dependencies in script that needs to be moved so
     * they are present for the minified version
     * </p>
     *
     * @param themeDirectory directory for the theme where directories will be copied to
     * @param themeProperties properties for the theme to process, used to pull the additionalOverlays
     * configuration
     * @param webappSourceDir absolute path to the web source directory, if the source overlay directory is not
     * currently present in the output directory, we need to go back and pull it from source
     * @param themeBuilderOutputDir absolute path to the output directory, used to pull the source overly directory
     */
    protected static void overlayAdditionalDirs(File themeDirectory, Properties themeProperties, String webappSourceDir,
            String themeBuilderOutputDir) {
        if (!themeProperties.containsKey(ThemeBuilderConstants.ThemeConfiguration.ADDITIONAL_OVERLAYS)) {
            return;
        }

        String additionalOverlaysStr = themeProperties.getProperty(
                ThemeBuilderConstants.ThemeConfiguration.ADDITIONAL_OVERLAYS);

        Map<String, String> additionalOverlays = parseAdditionalOverlaysStr(additionalOverlaysStr);

        for (Map.Entry<String, String> overlayMapping : additionalOverlays.entrySet()) {
            String fromSource = overlayMapping.getKey();
            String toThemeDir = overlayMapping.getValue();

            if (StringUtils.isBlank(fromSource)) {
                throw new RuntimeException("Invalid additional overlay mapping, from directory is blank");
            }

            File sourceFile = null;
            if (fromSource.startsWith("/")) {
                // source is relative to web root, first try our working directory and if not there copy
                // from the original source
                sourceFile = new File(themeBuilderOutputDir + fromSource);
                if (!sourceFile.exists()) {
                    sourceFile = new File(webappSourceDir + fromSource);
                }
            } else {
                // source directory is relative to theme directory
                sourceFile = new File(themeDirectory, fromSource);
            }

            if (!sourceFile.exists()) {
                throw new RuntimeException(
                        "Source directory/file for additional overlay does not exist at " + sourceFile.getPath());
            }

            File targetDir = null;
            if (StringUtils.isBlank(toThemeDir)) {
                targetDir = themeDirectory;
            } else {
                if (toThemeDir.startsWith("/")) {
                    toThemeDir = toThemeDir.substring(1);
                }

                targetDir = new File(themeDirectory, toThemeDir);
            }

            if (!targetDir.exists()) {
                targetDir.mkdir();
            }

            try {
                if (sourceFile.isDirectory()) {
                    ThemeBuilderUtils.copyMissingContent(sourceFile, targetDir, null);
                } else {
                    File targetFile = new File(targetDir, sourceFile.getName());

                    FileUtils.copyFile(sourceFile, targetFile);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to perform additional overlay", e);
            }
        }
    }

    /**
     * Helper method that parses the configuration string for additional overlays into a Map where
     * the key is the source path and the map value is the target path
     *
     * <p>
     * Each mapping in the string should be separated by a comma. Within the mapping, the source path should
     * be given, followed by the target path in parenthesis
     *
     * ex. sourcePath(targetPath),sourcePath2(targetPath2)
     * </p>
     *
     * @param additionalOverlaysStr string to parse into additional overlay mappings
     * @return map of additional overlay mappings
     */
    protected static Map<String, String> parseAdditionalOverlaysStr(String additionalOverlaysStr) {
        Map<String, String> additionalOverlays = new HashMap<String, String>();

        if (StringUtils.isBlank(additionalOverlaysStr)) {
            return additionalOverlays;
        }

        String[] additionalOverlaysArray = additionalOverlaysStr.split(",");
        for (String additionalOverlay : additionalOverlaysArray) {
            String fromDir = "";
            String toDir = "";

            if (additionalOverlay.contains("(") && additionalOverlay.contains(")")) {
                fromDir = StringUtils.substringBefore(additionalOverlay, "(");
                toDir = StringUtils.substringBetween(additionalOverlay, "(", ")");
            } else {
                fromDir = additionalOverlay;
            }

            additionalOverlays.put(StringUtils.trimToEmpty(fromDir), StringUtils.trimToEmpty(toDir));
        }

        return additionalOverlays;
    }

}
