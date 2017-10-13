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
import org.kuali.common.util.Assert;
import org.kuali.common.util.execute.Executable;
import org.kuali.rice.krad.theme.postprocessor.ThemeCssFilesProcessor;
import org.kuali.rice.krad.theme.postprocessor.ThemeFilesProcessor;
import org.kuali.rice.krad.theme.postprocessor.ThemeJsFilesProcessor;
import org.kuali.rice.krad.theme.preprocessor.ThemePreProcessor;
import org.kuali.rice.krad.theme.util.NonHiddenDirectoryFilter;
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
 * Class that gets executed from the Spring context to build out view themes
 *
 * <p>
 * A view theme is a collection of assets that provides the base css and js for one or more views (see
 * {@link org.kuali.rice.krad.uif.view.ViewTheme}. The theme builder provides utilities for creating and
 * configuring themes that follow a standard directory convention.
 * </p>
 *
 * <p>
 * By default, the theme builder processes any directories under '/themes' as theme directories. Other
 * theme directories can be added through the property {@link #getAdditionalThemeDirectories()}
 *
 * The basic functions provided by the theme builder are:
 *
 * <ul>
 * <li>Overlay assets from a parent theme directory (if a parent is configured). Only assets that exist in
 * the parent directory but not in the child will be overlaid</li>
 * <li>Applies one or more configured {@link ThemePreProcessor} instances to the theme files. For example, Less
 * files are compiled to CSS files here by the {@link org.kuali.rice.krad.theme.preprocessor.LessThemePreProcessor}</li>
 * <li>Collects JS and CSS resources for the theme. This includes bringing in plugin resources and base KRAD script.
 * Resources can be filtered and ordered as needed</li>
 * <li>Perform merging and minification for each file type. During this the file types can perform additional
 * processing (for example, URL rewriting is done for CSS files)</li>
 * </ul>
 *
 * To just perform the first step (overlay parent assets), the property {@link #isSkipThemeProcessing()} can be set to
 * true. This is useful in development where an update to a parent file just needs pushed to the output directory.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.kuali.rice.krad.theme.ThemeBuilderOverlays
 * @see org.kuali.rice.krad.theme.preprocessor.ThemePreProcessor
 * @see org.kuali.rice.krad.theme.postprocessor.ThemeFilesProcessor
 */
public class ThemeBuilder implements Executable {
    private static final Logger LOG = Logger.getLogger(ThemeBuilder.class);

    private String webappSourceDir;
    private String themeBuilderOutputDir;

    private List<String> themeExcludes;

    private List<String> additionalThemeDirectories;
    private List<String> additionalPluginDirectories;

    private String projectVersion;

    private List<ThemePreProcessor> themePreProcessors;

    private Map<String, String> themeNamePathMapping;
    private Map<String, Properties> themeNamePropertiesMapping;

    private Map<String, String> pluginNamePathMapping;

    private boolean skipThemeProcessing;

    /**
     * Invoked from the spring context to execute the theme builder
     *
     * <p>
     * Invokes processing of the main theme builder functions, this includes:
     *
     * <ul>
     * <li>Copying assets from web source directory to the output directory</li>
     * <li>Retrieving all theme and plugin directories, then setting up convenience maps for acquiring paths</li>
     * <li>Iterating through each theme that should be built (those not excluded with {@link #getThemeExcludes()})</li>
     * <li>For each theme, invoking parent and additional directory overlays, then finally calling a helper method
     * to process the theme assets</li>
     * </ul>
     * </p>
     *
     * <p>
     * To just perform copying of the web assets, and parent/additional directory overlays, set the property
     * {@link #isSkipThemeProcessing()} to true
     * </p>
     */
    public void execute() {
        Assert.hasText(this.webappSourceDir, "Webapp source directory not set");

        LOG.info("View builder executed on " + this.webappSourceDir);

        try {
            ThemeBuilderOverlays.copyAssetsToWorkingDir(this.webappSourceDir, this.themeBuilderOutputDir,
                    this.additionalThemeDirectories, this.additionalPluginDirectories);
        } catch (IOException e) {
            throw new RuntimeException("Unable to copy assets to working directory", e);
        }

        List<File> themeDirectories = getThemeDirectories();
        List<File> pluginDirectories = getPluginDirectories();

        // build mappings for convenient access
        try {
            buildMappings(themeDirectories, pluginDirectories);
        } catch (IOException e) {
            throw new RuntimeException("Unable to build theme mappings", e);
        }

        // themes must be ordered so that we build the parents first, and therefore they have all their files
        // for overlaying to a child theme
        List<String> orderedThemes = orderThemesForBuilding();

        if (this.themeExcludes != null) {
            for (String themeToExclude : themeExcludes) {
                themeToExclude = themeToExclude.toLowerCase();

                if (orderedThemes.contains(themeToExclude)) {
                    orderedThemes.remove(themeToExclude);
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Skipping build for theme " + themeToExclude);
                }
            }
        }

        // note important that two iterations be done over the themes and not one, all the parent
        // and plugin assets need to be overlaid before processing is done on a theme
        for (String themeName : orderedThemes) {
            copyParentThemeConfig(themeName);

            Properties themeProperties = this.themeNamePropertiesMapping.get(themeName);

            String themePath = this.themeNamePathMapping.get(themeName);
            File themeDirectory = new File(themePath);

            ThemeBuilderOverlays.overlayParentAssets(themeName, themeDirectory, themeProperties,
                    this.themeNamePathMapping);

            ThemeBuilderOverlays.overlayAdditionalDirs(themeDirectory, themeProperties, this.webappSourceDir,
                    this.themeBuilderOutputDir);
        }

        if (this.skipThemeProcessing) {
            LOG.info("Skipping theme processing");

            return;
        }

        for (String themeName : orderedThemes) {
            processThemeAssets(themeName);
        }
    }

    /**
     * Retrieves the directories that should be processed as themes
     *
     * <p>
     * By default all directories in '/themes' are included as theme directories. Additional directories can
     * be included by setting {@link #getAdditionalThemeDirectories()}
     * </p>
     *
     * @return list of file objects pointing to the theme directories
     */
    protected List<File> getThemeDirectories() {
        List<File> themeDirectories = new ArrayList<File>();

        String defaultThemesDirectoryPath =
                this.themeBuilderOutputDir + ThemeBuilderConstants.DEFAULT_THEMES_DIRECTORY;

        File defaultThemesDirectory = new File(defaultThemesDirectoryPath);
        File[] defaultThemeDirectories = defaultThemesDirectory.listFiles(new NonHiddenDirectoryFilter());

        if (defaultThemeDirectories != null) {
            themeDirectories = Arrays.asList(defaultThemeDirectories);
        }

        if (this.additionalThemeDirectories != null) {
            List<File> additionalThemeDirs = ThemeBuilderUtils.getSubDirectories(new File(this.themeBuilderOutputDir),
                    this.additionalThemeDirectories);
            themeDirectories.addAll(additionalThemeDirs);
        }

        ThemeBuilderUtils.validateFileExistence(themeDirectories, "Invalid theme directory.");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Found theme directories: " + StringUtils.join(themeDirectories, ","));
        }

        return themeDirectories;
    }

    /**
     * Retrieves the directories that should be processed as plugins
     *
     * <p>
     * By default all directories in '/plugins' are included as plugins. Additional directories can
     * be included by setting {@link #getAdditionalPluginDirectories()}
     * </p>
     *
     * @return list of file objects pointing to the plugin directories
     */
    protected List<File> getPluginDirectories() {
        List<File> pluginDirectories = new ArrayList<File>();

        String defaultPluginsDirectoryPath =
                this.themeBuilderOutputDir + ThemeBuilderConstants.DEFAULT_PLUGINS_DIRECTORY;
        File defaultPluginsDirectory = new File(defaultPluginsDirectoryPath);

        File[] pluginDirs = defaultPluginsDirectory.listFiles(new NonHiddenDirectoryFilter());

        if (pluginDirs != null) {
            pluginDirectories = Arrays.asList(pluginDirs);
        }

        if (this.additionalPluginDirectories != null) {
            List<File> additionalPluginDirs = ThemeBuilderUtils.getSubDirectories(new File(this.themeBuilderOutputDir),
                    this.additionalPluginDirectories);
            pluginDirectories.addAll(additionalPluginDirs);
        }

        ThemeBuilderUtils.validateFileExistence(pluginDirectories, "Invalid plugin directory.");

        return pluginDirectories;
    }

    /**
     * Builds convenience maps (theme name to path map, theme name to properties mapping, and plugin
     * name to path mapping) for the given theme and plugin directories
     *
     * @param themeDirectories list of theme directories to build mappings for
     * @param pluginDirectories list of file directories to build mappings for
     * @throws IOException
     */
    protected void buildMappings(List<File> themeDirectories, List<File> pluginDirectories) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Building mappings");
        }

        this.themeNamePathMapping = new HashMap<String, String>();
        this.themeNamePropertiesMapping = new HashMap<String, Properties>();

        for (File themeDirectory : themeDirectories) {
            String themeName = themeDirectory.getName().toLowerCase();

            this.themeNamePathMapping.put(themeName, themeDirectory.getPath());

            Properties themeProperties = ThemeBuilderUtils.retrieveThemeProperties(themeDirectory.getPath());
            if (themeProperties == null) {
                themeProperties = new Properties();
            }

            this.themeNamePropertiesMapping.put(themeName, themeProperties);
        }

        this.pluginNamePathMapping = new HashMap<String, String>();

        for (File pluginDirectory : pluginDirectories) {
            String pluginName = pluginDirectory.getName().toLowerCase();

            this.pluginNamePathMapping.put(pluginName, pluginDirectory.getPath());
        }
    }

    /**
     * Builds a list containing theme names in the order for which they should be processed
     *
     * <p>
     * For the parent overlays to work correctly, the parent must be processed before the child. There can
     * be multiple parents in the hierarchy, so here we go through and figure out the correct order
     * </p>
     *
     * @return list of ordered theme names
     */
    protected List<String> orderThemesForBuilding() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Ordering themes for building");
        }

        List<String> orderedThemes = new ArrayList<String>();

        for (String themeName : this.themeNamePathMapping.keySet()) {
            String themePath = this.themeNamePathMapping.get(themeName);

            if (orderedThemes.contains(themeName)) {
                continue;
            }

            List<String> themeParents = getAllThemeParents(themeName, new ArrayList<String>());
            for (String themeParent : themeParents) {
                if (!orderedThemes.contains(themeParent)) {
                    orderedThemes.add(themeParent);
                }
            }

            orderedThemes.add(themeName);
        }

        return orderedThemes;
    }

    /**
     * Gets all parents (ancestors) for the given theme name
     *
     * <p>
     * The parent for a theme is determined by retrieving the theme's properties file, then pulling the
     * property with key 'parent'. Then the properties file for the parent theme is pulled and check to see if
     * it has a parent. So on until a theme is reached that does not have a parent
     * </p>
     *
     * @param themeName name of theme to retrieve parents for
     * @param themeParents list of parents that have been previously found (used to find circular references)
     * @return list of theme names that are parents to the given theme
     */
    protected List<String> getAllThemeParents(String themeName, List<String> themeParents) {
        Properties themeProperties = this.themeNamePropertiesMapping.get(themeName);
        if (themeProperties.containsKey(ThemeBuilderConstants.ThemeConfiguration.PARENT)) {
            String parentThemeName = themeProperties.getProperty(ThemeBuilderConstants.ThemeConfiguration.PARENT);

            if (StringUtils.isBlank(parentThemeName)) {
                return themeParents;
            }

            if (!this.themeNamePropertiesMapping.containsKey(parentThemeName)) {
                throw new RuntimeException("Invalid theme name for parent property: " + parentThemeName);
            }

            if (themeParents.contains(parentThemeName)) {
                throw new RuntimeException("Circular reference found for parent: " + parentThemeName);
            }

            themeParents.addAll(getAllThemeParents(parentThemeName, themeParents));

            themeParents.add(parentThemeName);
        }

        return themeParents;
    }

    /**
     * If the given theme has a parent, retrieve the theme properties (if exists) for the parent,
     * then for each config property copy the parent value to the child theme properties if missing
     *
     * @param themeName name of the theme to pull parent config for and copy
     */
    protected void copyParentThemeConfig(String themeName) {
        Properties themeProperties = this.themeNamePropertiesMapping.get(themeName);

        if (!themeProperties.containsKey(ThemeBuilderConstants.ThemeConfiguration.PARENT)) {
            return;
        }

        String parentThemeName = themeProperties.getProperty(ThemeBuilderConstants.ThemeConfiguration.PARENT);
        Properties parentThemeProperties = this.themeNamePropertiesMapping.get(parentThemeName);

        String[] propertiesToCopy = new String[] {ThemeBuilderConstants.ThemeConfiguration.LESS_INCLUDES,
                ThemeBuilderConstants.ThemeConfiguration.LESS_EXCLUDES,
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_INCLUDES,
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_EXCLUDES,
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_FILE_EXCLUDES,
                ThemeBuilderConstants.ThemeConfiguration.ADDITIONAL_OVERLAYS,
                ThemeBuilderConstants.ThemeConfiguration.CSS_LOAD_FIRST,
                ThemeBuilderConstants.ThemeConfiguration.CSS_LOAD_LAST,
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_JS_LOAD_ORDER,
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_CSS_LOAD_ORDER,
                ThemeBuilderConstants.ThemeConfiguration.THEME_JS_LOAD_ORDER,
                ThemeBuilderConstants.ThemeConfiguration.THEME_CSS_LOAD_ORDER,
                ThemeBuilderConstants.ThemeConfiguration.JS_LOAD_FIRST,
                ThemeBuilderConstants.ThemeConfiguration.JS_LOAD_LAST};

        for (String propertyKey : propertiesToCopy) {
            ThemeBuilderUtils.copyProperty(propertyKey, parentThemeProperties, themeProperties);
        }
    }

    /**
     * Performs the various steps to process the given theme
     *
     * <p>
     * The theme is processed first by applying any configured {@link org.kuali.rice.krad.theme.preprocessor.ThemePreProcessor}
     * instances (such as less processing). Once the pre processors are applied, the CSS and JS post processors are
     * then invoked to do the final processing
     *
     * After processing is complete the 'theme-derived.properties' file gets written to the theme directory, which
     * contains all the properties for the theme (set, inherited, derived)
     * </p>
     *
     * @param themeName name of the theme to process
     */
    protected void processThemeAssets(String themeName) {
        Properties themeProperties = this.themeNamePropertiesMapping.get(themeName);

        String themePath = this.themeNamePathMapping.get(themeName);
        File themeDirectory = new File(themePath);

        LOG.info("Processing assets for theme: " + themeName);

        // apply pre-processors which can modify the theme assets before they are collected
        if (this.themePreProcessors != null) {
            for (ThemePreProcessor preProcessor : this.themePreProcessors) {
                preProcessor.processTheme(themeName, themeDirectory, themeProperties);
            }
        }

        // apply processors for CSS and JS files to do final processing
        File workingDir = new File(this.themeBuilderOutputDir);

        Map<String, File> themePluginDirsMap = collectThemePluginDirs(themeProperties);

        ThemeFilesProcessor filesProcessor = new ThemeCssFilesProcessor(themeName, themeDirectory, themeProperties,
                themePluginDirsMap, workingDir, this.projectVersion);
        filesProcessor.process();

        filesProcessor = new ThemeJsFilesProcessor(themeName, themeDirectory, themeProperties,
                themePluginDirsMap, workingDir, this.projectVersion);
        filesProcessor.process();

        try {
            ThemeBuilderUtils.storeThemeProperties(themePath, themeProperties);
        } catch (IOException e) {
            throw new RuntimeException("Unable to update theme.properties file", e);
        }
    }

    /**
     * Helper method that filters the list of all plugins and returns those that should be used
     * with the theme
     *
     * <p>
     * Which plugins to include for a theme can be configured using the pluginIncludes and pluginExlcudes
     * property keys
     * </p>
     *
     * @param themeProperties properties file for the theme, used to retrieve the plugin configuration
     * @return map containing the plugins for the theme, map key is the plugin name and map value gives
     *         the plugin directory
     */
    protected Map<String, File> collectThemePluginDirs(Properties themeProperties) {
        Map<String, File> themePluginDirs = new HashMap<String, File>();

        String[] pluginIncludes = ThemeBuilderUtils.getPropertyValueAsArray(
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_INCLUDES, themeProperties);

        String[] pluginExcludes = ThemeBuilderUtils.getPropertyValueAsArray(
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_EXCLUDES, themeProperties);

        for (Map.Entry<String, String> pluginMapping : this.pluginNamePathMapping.entrySet()) {
            String pluginName = pluginMapping.getKey();

            if (ThemeBuilderUtils.inExcludeList(pluginName, pluginExcludes)) {
                continue;
            }

            if (ThemeBuilderUtils.inIncludeList(pluginName, pluginIncludes)) {
                themePluginDirs.put(pluginName, new File(pluginMapping.getValue()));
            }
        }

        themeProperties.put(ThemeBuilderConstants.DerivedConfiguration.THEME_PLUGIN_NAMES,
                StringUtils.join(themePluginDirs.keySet(), ","));

        return themePluginDirs;
    }

    /**
     * Map that associates theme names with their path, provided here for subclasses
     *
     * @return map of theme name/paths, map key is the theme name, map value is the theme path
     */
    protected Map<String, String> getThemeNamePathMapping() {
        return themeNamePathMapping;
    }

    /**
     * Map that associates theme names with their properties, provided here for subclasses
     *
     * @return map of theme name/properties, map key is the theme name, map value is the properties object
     */
    protected Map<String, Properties> getThemeNamePropertiesMapping() {
        return themeNamePropertiesMapping;
    }

    /**
     * Absolute path to the directory that contains the web application source
     *
     * <p>
     * Generally this is the base directory for the application/module, then /src/main/webapp
     * </p>
     *
     * <p>
     * If you are using the maven plugin this can be set by the maven property <code>webapp.source.dir</code>
     * </p>
     *
     * @return path to webapp source directory
     */
    public String getWebappSourceDir() {
        return webappSourceDir;
    }

    /**
     * Setter for the path to the webapp source
     *
     * @param webappSourceDir
     */
    public void setWebappSourceDir(String webappSourceDir) {
        if (StringUtils.isNotBlank(webappSourceDir)) {
            // trim off any trailing path separators
            if (webappSourceDir.endsWith(File.separator) || webappSourceDir.endsWith("/")) {
                webappSourceDir = webappSourceDir.substring(0, webappSourceDir.length() - 1);
            }
        }

        this.webappSourceDir = webappSourceDir;
    }

    /**
     * Absolute path to the directory the theme builder should output content to
     *
     * <p>
     * Generally this will be the output directory for the exploded war being created. However you can also
     * choose to output to a temporary directory, then copy the assets over at a later phase
     * </p>
     *
     * <p>
     * If you are using the maven plugin this can be set by the maven property <code>theme.builder.output.dir</code>
     * </p>
     *
     * @return path to the output directory
     */
    public String getThemeBuilderOutputDir() {
        return themeBuilderOutputDir;
    }

    /**
     * Setter for the path to the output directory
     *
     * @param themeBuilderOutputDir
     */
    public void setThemeBuilderOutputDir(String themeBuilderOutputDir) {
        if (StringUtils.isNotBlank(themeBuilderOutputDir)) {
            // trim off any trailing path separators
            if (themeBuilderOutputDir.endsWith(File.separator) || themeBuilderOutputDir.endsWith("/")) {
                themeBuilderOutputDir = themeBuilderOutputDir.substring(0, themeBuilderOutputDir.length() - 1);
            }
        }

        this.themeBuilderOutputDir = themeBuilderOutputDir;
    }

    /**
     * List of theme names that should be excluded from theme processing
     *
     * <p>
     * Directories for themes that are excluded will be copied to the output directory but no further
     * processing will occur on that theme.
     * </p>
     *
     * <p>
     * If your web application receives web overlays which include themes, they will already be processed.
     * Processing them again will result in duplicate content. Therefore you should exclude these themes using
     * this property
     * </p>
     *
     * <p>
     * If you are using the maven plugin this can be set by the maven property <code>theme.builder.excludes</code>
     * </p>
     *
     * @return list of excluded theme names
     */
    public List<String> getThemeExcludes() {
        return themeExcludes;
    }

    /**
     * Setter for the list of theme names to exclude from processing
     *
     * @param themeExcludes
     */
    public void setThemeExcludes(List<String> themeExcludes) {
        this.themeExcludes = themeExcludes;
    }

    /**
     * Convenience setter that takes a string and parses to populate the theme excludes list
     *
     * @param themeExcludes string containing theme names to exclude which are delimited using a comma
     */
    public void setThemeExcludesStr(String themeExcludes) {
        if (StringUtils.isNotBlank(themeExcludes)) {
            String[] themeExcludesArray = themeExcludes.split(",");
            this.themeExcludes = Arrays.asList(themeExcludesArray);
        }
    }

    /**
     * List of absolute paths to include as additional theme directories
     *
     * <p>
     * By default all directories under the web root folder <code>themes</code> are included. Other web
     * directories can be processed as themes by including their path in this list
     * </p>
     *
     * <p>
     * If you are using the maven plugin this can be set by the maven property <code>theme.builder.theme.adddirs</code>
     * </p>
     *
     * @return list of paths for additional themes
     */
    public List<String> getAdditionalThemeDirectories() {
        return additionalThemeDirectories;
    }

    /**
     * Setter for the list of additional theme directory paths
     *
     * @param additionalThemeDirectories
     */
    public void setAdditionalThemeDirectories(List<String> additionalThemeDirectories) {
        this.additionalThemeDirectories = additionalThemeDirectories;
    }

    /**
     * Convenience setter that takes a string and parses to populate the additional theme directories list
     *
     * @param additionalThemeDirectories string containing additional theme directories which are
     * delimited using a comma
     */
    public void setAdditionalThemeDirectoriesStr(String additionalThemeDirectories) {
        if (StringUtils.isNotBlank(additionalThemeDirectories)) {
            String[] additionalThemeDirectoriesArray = additionalThemeDirectories.split(",");
            this.additionalThemeDirectories = Arrays.asList(additionalThemeDirectoriesArray);
        }
    }

    /**
     * List of absolute paths to include as additional plugin directories
     *
     * <p>
     * By default all directories under the web root folder <code>plugins</code> are included. Other web
     * directories can be processed as plugins by including their path in this list
     * </p>
     *
     * <p>
     * If you are using the maven plugin this can be set by the maven property <code>theme.builder.plugin.adddirs</code>
     * </p>
     *
     * @return list of paths for additional plugins
     */
    public List<String> getAdditionalPluginDirectories() {
        return additionalPluginDirectories;
    }

    /**
     * Setter for the list of additional plugin directory paths
     *
     * @param additionalPluginDirectories
     */
    public void setAdditionalPluginDirectories(List<String> additionalPluginDirectories) {
        this.additionalPluginDirectories = additionalPluginDirectories;
    }

    /**
     * Convenience setter that takes a string and parses to populate the additional plugin directories list
     *
     * @param additionalPluginDirectories string containing additional plugin directories which are
     * delimited using a comma
     */
    public void setAdditionalPluginDirectoriesStr(String additionalPluginDirectories) {
        if (StringUtils.isNotBlank(additionalPluginDirectories)) {
            String[] additionalPluginDirectoriesArray = additionalPluginDirectories.split(",");
            this.additionalPluginDirectories = Arrays.asList(additionalPluginDirectoriesArray);
        }
    }

    /**
     * Version for the project that will be used to stamp the minified file
     *
     * <p>
     * In order to facilitate automatic downloads between project releases, the minified files are stamped with
     * the version number.
     * </p>
     *
     * <p>
     * If you are using the maven plugin this can be set by the maven property <code>project.version</code>
     * </p>
     *
     * @return version string for project
     */
    public String getProjectVersion() {
        return projectVersion;
    }

    /**
     * Setter for the project version
     *
     * @param projectVersion
     */
    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    /**
     * List of {@link ThemePreProcessor} instances that should be applied to the themes
     *
     * @return list of pre processors to apply
     */
    public List<ThemePreProcessor> getThemePreProcessors() {
        return themePreProcessors;
    }

    /**
     * Setter for the list of theme pre processors
     *
     * @param themePreProcessors
     */
    public void setThemePreProcessors(List<ThemePreProcessor> themePreProcessors) {
        this.themePreProcessors = themePreProcessors;
    }

    /**
     * Indicates whether processing of the themes should be skipped
     *
     * <p>
     * In development it can be useful to just update the output directory with the theme assets, and skip
     * processing such as Less and minification (which can be time consuming). Setting this flag to true will
     * skip processing of pre and post processors, just doing the overlay. By default this is false
     * </p>
     *
     * @return true if theme processing should be skipped, false if not
     */
    public boolean isSkipThemeProcessing() {
        return skipThemeProcessing;
    }

    /**
     * Setter to skip theme processing
     *
     * @param skipThemeProcessing
     */
    public void setSkipThemeProcessing(boolean skipThemeProcessing) {
        this.skipThemeProcessing = skipThemeProcessing;
    }

}
