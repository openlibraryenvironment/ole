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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.theme.util.ThemeBuilderConstants;
import org.kuali.rice.krad.theme.util.ThemeBuilderUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Base class for the JS and CSS theme file processors (or post-processors) that act on a given theme
 *
 * <p>
 * The post processing of JS and CSS files is orchestrated in this base class with the specific configuration
 * for each provided through abstract methods. This {@link #process()} method performs the major calls of
 * the process
 * </p>
 *
 * <p>
 * Base class also provides some helper methods such as {@link #getPropertyValueAsPluginDirs(java.lang.String)}
 * and {@link #addMissingPluginDirs(java.util.List<java.io.File>)}
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class ThemeFilesProcessor {
    private static final Logger LOG = Logger.getLogger(ThemeFilesProcessor.class);

    protected static final String PLUGIN_FILES_KEY = "plugin";
    protected static final String SUBDIR_FILES_KEY = "subdir";

    protected String themeName;
    protected File themeDirectory;
    protected Properties themeProperties;
    protected Map<String, File> themePluginDirsMap;
    protected File workingDir;
    protected String projectVersion;

    public ThemeFilesProcessor(String themeName, File themeDirectory, Properties themeProperties,
            Map<String, File> themePluginDirsMap, File workingDir, String projectVersion) {
        this.themeName = themeName;
        this.themeDirectory = themeDirectory;
        this.themeProperties = themeProperties;
        this.themePluginDirsMap = themePluginDirsMap;
        this.workingDir = workingDir;
        this.projectVersion = projectVersion;
    }

    /**
     * Carries out the theme files post process
     *
     * <p>
     * Processing of each file type includes the following:
     *
     * <ul>
     * <li>Collect the theme files for each type</li>
     * <li>Perform a sorting process on the files to form the correct order of sourcing or merging</li>
     * <li>Merge all the files for the type into a single file. Here subclasses can perform alterations on
     * the merged contents</li>
     * <li>Minifiy the merged file using a compressor that is appropriate for the file type</li>
     * <li>Write out the listing of files for the type to the theme's properties file</li>
     * </ul>
     * </p>
     *
     * <p>
     * Any {@link IOException} that occur are caught and thrown as runtime exceptions
     * </p>
     */
    public void process() {
        Map<String, List<File>> themeFilesMap = collectThemeFiles();

        // perform any custom sorting configured in the theme properties
        List<File> themeFiles = sortThemeFiles(themeFilesMap.get(PLUGIN_FILES_KEY), themeFilesMap.get(
                SUBDIR_FILES_KEY));

        File mergedFile = createMergedFile(false);
        try {
            mergeFiles(themeFiles, mergedFile);
        } catch (IOException e) {
            throw new RuntimeException("Exception encountered while merging files for type: "
                    + getFileTypeExtension());
        }

        File minifiedFile = createMergedFile(true);
        try {
            minify(mergedFile, minifiedFile);
        } catch (IOException e) {
            throw new RuntimeException("Exception encountered while minifying files for type: "
                    + getFileTypeExtension());
        }

        // add listing of file paths for this type to the theme properties
        // (to be read in dev mode by the view theme)
        List<String> themeFilePaths = ThemeBuilderUtils.getRelativePaths(this.workingDir, themeFiles);

        this.themeProperties.put(getFileListingConfigKey(), StringUtils.join(themeFilePaths, ","));
    }

    /**
     * Collects the file names to include for the theme, separated by whether they come from a plugin directory
     * or the theme directory
     *
     * <p>
     * First all plugin directories that are included for the theme are listed based on the include for that
     * file type {@link #getFileIncludes()}. Individual plugin files can be excluded with the property
     * <code>pluginFileExcludes</code>, or the global excludes for the file type {@link #getFileExcludes()}
     *
     * Next the subdirectory of the theme that holds the file type, given by {@link #getFileTypeSubDirectory()},
     * is listed to pick up include files. Again the global file includes and excludes for the type is used
     *
     * Finally, subclasses can add additional files by implementing {@link #addAdditionalFiles(java.util.List)}
     * </p>
     *
     * @return map containing an entry for plugin file names, and theme file names. Keys are given by
     *         {@link #PLUGIN_FILES_KEY} and {@link #SUBDIR_FILES_KEY}
     * @see #getFileIncludes()
     * @see #getFileExcludes()
     * @see #getFileTypeSubDirectory()
     * @see #addAdditionalFiles(java.util.List)
     */
    protected Map<String, List<File>> collectThemeFiles() {
        Map<String, List<File>> themeFiles = new HashMap<String, List<File>>();

        String[] fileIncludes = getFileIncludes();
        String[] fileExcludes = getFileExcludes();

        // add files from plugins first
        String[] pluginFileExcludes = null;
        if (this.themeProperties.containsKey(ThemeBuilderConstants.ThemeConfiguration.PLUGIN_FILE_EXCLUDES)) {
            pluginFileExcludes = ThemeBuilderUtils.getPropertyValueAsArray(
                    ThemeBuilderConstants.ThemeConfiguration.PLUGIN_FILE_EXCLUDES, this.themeProperties);
        }

        // global file excludes should also apply to plugins
        if (fileExcludes != null) {
            if (pluginFileExcludes == null) {
                pluginFileExcludes = fileExcludes;
            } else {
                pluginFileExcludes = ThemeBuilderUtils.addToArray(pluginFileExcludes, fileExcludes);
            }
        }

        // for convenience we don't require the extension on the patterns, but it must be added before
        // we use the patterns for matching
        ThemeBuilderUtils.addExtensionToPatterns(fileIncludes, getFileTypeExtension());
        ThemeBuilderUtils.addExtensionToPatterns(fileExcludes, getFileTypeExtension());
        ThemeBuilderUtils.addExtensionToPatterns(pluginFileExcludes, getFileTypeExtension());

        List<File> pluginThemeFiles = new ArrayList<File>();
        for (Map.Entry<String, File> pluginMapping : this.themePluginDirsMap.entrySet()) {
            String pluginName = pluginMapping.getKey();
            File pluginDirectory = pluginMapping.getValue();

            // adjust plugin file excludes to not include the top directory
            String[] adjustedFileExcludes = null;
            if (pluginFileExcludes != null) {
                adjustedFileExcludes = new String[pluginFileExcludes.length];

                for (int i = 0; i < pluginFileExcludes.length; i++) {
                    adjustedFileExcludes[i] = StringUtils.removeStart(pluginFileExcludes[i], pluginName + "/");
                }
            }

            pluginThemeFiles.addAll(ThemeBuilderUtils.getDirectoryFiles(pluginDirectory, fileIncludes,
                    adjustedFileExcludes));
        }

        themeFiles.put(PLUGIN_FILES_KEY, pluginThemeFiles);

        // now add files in the subdirectory for this file type directory
        List<File> subDirThemeFiles = new ArrayList<File>();

        subDirThemeFiles.addAll(ThemeBuilderUtils.getDirectoryFiles(getFileTypeSubDirectory(), fileIncludes,
                fileExcludes));

        // allow additional files to be added based on the file type
        addAdditionalFiles(subDirThemeFiles);

        themeFiles.put(SUBDIR_FILES_KEY, subDirThemeFiles);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Found " + subDirThemeFiles.size() + "file(s) for theme " + this.themeName);
        }

        return themeFiles;
    }

    /**
     * Builds array of patterns used to find files to include for the type, by default picks up all
     * files that have the extension for the type being processed
     *
     * @return array of string patterns to include
     * @see #getFileTypeExtension()
     */
    protected String[] getFileIncludes() {
        return new String[] {"**/*" + getFileTypeExtension()};
    }

    /**
     * Builds array of patterns used to exclude files to include for the type
     *
     * <p>
     * Each file type has a configuration property where exclude patterns can be listed. This property
     * key is retrieved by {@link #getExcludesConfigKey()}, and then split by the comma to get the array
     * of patterns
     * </p>
     *
     * @return array of string patterns to exclude
     * @see #getExcludesConfigKey();
     */
    protected String[] getFileExcludes() {
        String[] excludes = null;

        if (this.themeProperties.containsKey(getExcludesConfigKey())) {
            String excludesString = this.themeProperties.getProperty(getExcludesConfigKey());

            excludes = excludesString.split(",");
        }

        return excludes;
    }

    /**
     * Returns the File object that points to the theme subdirectory that contains files for the
     * file type
     *
     * <p>
     * Sub directory is formed by finding the directory with name {@link #getFileTypeDirectoryName()} within
     * the theme directory
     * </p>
     *
     * @return sub directory for the file type
     * @see #getFileTypeDirectoryName()
     */
    protected File getFileTypeSubDirectory() {
        File subDirectory = new File(this.themeDirectory, getFileTypeDirectoryName());

        if (!subDirectory.exists()) {
            throw new RuntimeException(
                    "Directory for file type " + getFileTypeDirectoryName() + " does not exist for theme: "
                            + this.themeName);
        }

        return subDirectory;
    }

    /**
     * Creates a new file that will hold the merged or minified contents
     *
     * <p>
     * The merged file name is constructed by taking the theme name, concatenated with "." and the project version.
     * To form the minified file name, the min suffix ".min" is appended to the merged file name
     * </p>
     *
     * @param minified indicates whether to add the minified suffix
     * @return file object pointing to the merged or minified file
     */
    protected File createMergedFile(boolean minified) {
        String mergedFileName = this.themeName + "." + this.projectVersion;

        if (minified) {
            mergedFileName += ThemeBuilderConstants.MIN_FILE_SUFFIX;
        }

        mergedFileName += getFileTypeExtension();

        return new File(getFileTypeSubDirectory(), mergedFileName);
    }

    /**
     * Merges the content from the list of files into the given merge file
     *
     * <p>
     * Contents are read for each file in the order they appear in the files list. Before adding the contents
     * to the merged file, the method {@link #processMergeFileContents(java.lang.String, java.io.File, java.io.File)}
     * is invoked to allow subclasses to alter the contents
     * </p>
     *
     * @param filesToMerge list of files whose content should be merged
     * @param mergedFile file that should receive the merged content
     * @throws IOException
     */
    protected void mergeFiles(List<File> filesToMerge, File mergedFile) throws IOException {
        OutputStream out = null;
        OutputStreamWriter outWriter = null;
        InputStreamReader reader = null;

        LOG.info("Creating merged file: " + mergedFile.getPath());

        try {
            out = new FileOutputStream(mergedFile);
            outWriter = new OutputStreamWriter(out);

            for (File fileToMerge : filesToMerge) {
                reader = new FileReader(fileToMerge);

                String fileContents = IOUtils.toString(reader);
                if ((fileContents == null) || "".equals(fileContents)) {
                    continue;
                }

                fileContents = processMergeFileContents(fileContents, fileToMerge, mergedFile);

                outWriter.append(fileContents);
                outWriter.flush();
            }
        } finally {
            if (out != null) {
                out.close();
            }

            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Extension (ex. 'css') for the file type being processed
     *
     * @return file type extension
     */
    protected abstract String getFileTypeExtension();

    /**
     * Name of the directory relative to the theme directory which contains files for the type
     *
     * @return directory name
     */
    protected abstract String getFileTypeDirectoryName();

    /**
     * Key for the property within the theme's properties file that can be configured to exlcude files
     * of the type being processed
     *
     * @return property key for file type excludes
     */
    protected abstract String getExcludesConfigKey();

    /**
     * Key for the property that will be written to the theme derived properties to list the included files
     * for the file type
     *
     * @return property key for file type listing
     */
    protected abstract String getFileListingConfigKey();

    /**
     * Invoked during the collection of files to allow additional files to be added to the theme's list
     *
     * @param themeFiles list of additional files to included for the theme
     */
    protected abstract void addAdditionalFiles(List<File> themeFiles);

    /**
     * Invoked to build the final sorted list of files for the type, files from plugins and from the theme's
     * sub directory are passed separately so special treatment can be given to those for sorting
     *
     * @param pluginFiles list of files that will be included and come from a plugin directory
     * @param subDirFiles list of files that will be included and come from the theme subdirectory
     * @return list of all files to include for the theme in the correct source order
     */
    protected abstract List<File> sortThemeFiles(List<File> pluginFiles, List<File> subDirFiles);

    /**
     * Invoked during the merge process to alter the given file contents before they are appended to the
     * merge file
     *
     * @param fileContents contents of the file that will be added
     * @param fileToMerge file the contents were pulled from
     * @param mergedFile file receiving the merged contents
     * @return file contents to merge (possibly altered)
     * @throws IOException
     */
    protected abstract String processMergeFileContents(String fileContents, File fileToMerge, File mergedFile)
            throws IOException;

    /**
     * Invoked after the merged file has been created to create the minified version
     *
     * @param mergedFile file containing the merged contents
     * @param minifiedFile file created to receive the minified contents
     * @throws IOException
     */
    protected abstract void minify(File mergedFile, File minifiedFile) throws IOException;

    /**
     * Helper method that retrieves the value for the given property from the theme's properties as
     * a list of strings
     *
     * @param propertyKey key for the property to retrieve the value for
     * @return list of string values parsed from the property value
     */
    protected List<String> getThemePropertyValue(String propertyKey) {
        return ThemeBuilderUtils.getPropertyValueAsList(propertyKey, this.themeProperties);
    }

    /**
     * Helper method that retrieves the value for the given property from the theme's properties as a
     * list of file objects that point to plugin directories
     *
     * @param propertyKey key for the property to retrieve the value for
     * @return list of files (plugin directories) parsed from the property value
     */
    protected List<File> getPropertyValueAsPluginDirs(String propertyKey) {
        List<File> pluginDirs = null;

        List<String> pluginNames = ThemeBuilderUtils.getPropertyValueAsList(propertyKey, this.themeProperties);

        if (pluginNames != null && !pluginNames.isEmpty()) {
            pluginDirs = new ArrayList<File>();

            for (String pluginName : pluginNames) {
                pluginName = pluginName.toLowerCase();

                if (!this.themePluginDirsMap.containsKey(pluginName)) {
                    throw new RuntimeException(
                            "Invalid plugin name: " + pluginName + " in configuration for property " + propertyKey);
                }

                pluginDirs.add(this.themePluginDirsMap.get(pluginName));
            }
        }

        return pluginDirs;
    }

    /**
     * Helper method to add any plugin directories for the theme being processed to the given list if they
     * are not already contained in the list
     *
     * @param pluginList list of plugin directories to complete
     * @return list of plugin directories that includes all plugins for the theme
     */
    protected List<File> addMissingPluginDirs(List<File> pluginList) {
        List<File> allPluginDirs = new ArrayList<File>();

        if (pluginList != null) {
            allPluginDirs.addAll(pluginList);
        }

        Collection<File> allPlugins = this.themePluginDirsMap.values();
        for (File pluginDir : allPlugins) {
            if (!allPluginDirs.contains(pluginDir)) {
                allPluginDirs.add(pluginDir);
            }
        }

        return allPluginDirs;
    }
}
