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
package org.kuali.rice.krad.theme.util;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.SelectorUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Utility methods for the view builder module
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ThemeBuilderUtils {

    /**
     * Retrieve the {@link Properties} object loaded from the theme.properties file found in the given
     * theme directory
     *
     * @param themeDirectory directory for the theme to pull properties file from
     * @return Properties object loaded with the theme configuration, or null if the properties file
     *         does not exist
     * @throws IOException
     */
    public static Properties retrieveThemeProperties(String themeDirectory) throws IOException {
        Properties themeProperties = null;

        FileInputStream fileInputStream = null;

        try {
            File propertiesFile = new File(themeDirectory, ThemeBuilderConstants.THEME_PROPERTIES_FILE);

            if (propertiesFile.exists()) {
                fileInputStream = new FileInputStream(propertiesFile);

                themeProperties = new Properties();
                themeProperties.load(fileInputStream);
            }
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }

        return themeProperties;
    }

    /**
     * Stores the given properties object in a file named <code>theme-derived.properties</code> within the
     * given theme directory
     *
     * @param themeDirectory directory the properties file should be created in
     * @param themeProperties properties that should be written to the properties file
     * @throws IOException
     */
    public static void storeThemeProperties(String themeDirectory, Properties themeProperties) throws IOException {
        File propertiesFile = new File(themeDirectory, ThemeBuilderConstants.THEME_DERIVED_PROPERTIES_FILE);

        // need to remove file if already exists so the new properties will be written
        if (propertiesFile.exists()) {
            FileUtils.forceDelete(propertiesFile);
        }

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(propertiesFile);

            themeProperties.store(fileWriter, null);
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    /**
     * Retrieves the value for the property with the given key from the properties object, as a list of
     * strings (by splitting the value on commas)
     *
     * @param key key for the property to retrieve
     * @param properties properties object to pull property from
     * @return list of strings parsed from the property value
     */
    public static List<String> getPropertyValueAsList(String key, Properties properties) {
        List<String> propertyValueList = null;

        String[] propertyValueArray = getPropertyValueAsArray(key, properties);
        if (propertyValueArray != null) {
            propertyValueList = new ArrayList<String>();

            for (String value : propertyValueArray) {
                propertyValueList.add(value);
            }
        }

        return propertyValueList;
    }

    /**
     * Retrieves the value for the property with the given key from the properties object, as an array of
     * strings (by splitting the value on commas)
     *
     * @param key key for the property to retrieve
     * @param properties properties object to pull property from
     * @return array of strings parsed from the property value
     */
    public static String[] getPropertyValueAsArray(String key, Properties properties) {
        String[] propertyValue = null;

        if (properties.containsKey(key)) {
            String propertyValueString = properties.getProperty(key);

            if (!StringUtils.isBlank(propertyValueString)) {
                propertyValue = propertyValueString.split(",");
            }
        }

        return propertyValue;
    }

    /**
     * Copies the property key/value from the source properties to the target properties if a property with the
     * same key does not exist in the target properties
     *
     * @param propertyKey key of the property to copy
     * @param sourceProperties properties to pull the property from
     * @param targetProperties properties to copy the property to
     */
    public static void copyProperty(String propertyKey, Properties sourceProperties, Properties targetProperties) {
        if (targetProperties != null && targetProperties.containsKey(propertyKey) && StringUtils.isNotBlank(
                targetProperties.getProperty(propertyKey))) {
            return;
        }

        if (sourceProperties != null && sourceProperties.containsKey(propertyKey)) {
            String propertyValue = sourceProperties.getProperty(propertyKey);

            if (targetProperties == null) {
                targetProperties = new Properties();
            }

            targetProperties.put(propertyKey, propertyValue);
        }
    }

    /**
     * Iterates through each file in the given list and verifies the file exists, if not a runtime
     * exception is thrown with the provided message
     *
     * @param filesToValidate list of files to check existence for
     * @param exceptionMessage message for runtime exception if a file is found that does not exist
     */
    public static void validateFileExistence(List<File> filesToValidate, String exceptionMessage) {
        if (filesToValidate == null) {
            return;
        }

        for (File file : filesToValidate) {
            if (!file.exists()) {
                throw new RuntimeException(exceptionMessage + " Path: " + file.getPath());
            }
        }
    }

    /**
     * Indicates whether there is a file with the given name within the given directory
     *
     * @param directory directory to check for file
     * @param fileName name of the file to check for
     * @return true if there is a file in the directory, false if not
     */
    public static boolean directoryContainsFile(File directory, String fileName) {
        boolean containsFile = false;

        List<String> directoryContents = getDirectoryContents(directory, null, null);

        for (String directoryFile : directoryContents) {
            String directoryFilename = FileUtils.filename(directoryFile);

            if (directoryFilename.equals(fileName)) {
                containsFile = true;
            }
        }

        return containsFile;
    }

    /**
     * Retrieves a list of files that are in the given directory, possibly filtered by the list of include
     * patterns or exclude patterns
     *
     * @param baseDirectory directory to retrieve files from
     * @param includes list of patterns to match against for files to include, can include Ant patterns
     * @param excludes list of patterns to match for excluded files, can include Ant patterns
     * @return list of files within the directory that match all given patterns
     */
    public static List<File> getDirectoryFiles(File baseDirectory, String[] includes, String[] excludes) {
        List<File> directoryFiles = new ArrayList<File>();

        List<String> directoryFileNames = getDirectoryFileNames(baseDirectory, includes, excludes);

        for (String fileName : directoryFileNames) {
            directoryFiles.add(new File(baseDirectory, fileName));
        }

        return directoryFiles;
    }

    /**
     * Retrieves a list of file names that are in the given directory, possibly filtered by the list of include
     * patterns or exclude patterns
     *
     * @param baseDirectory directory to retrieve file names from
     * @param includes list of patterns to match against for file names to include, can include Ant patterns
     * @param excludes list of patterns to match for excluded file names, can include Ant patterns
     * @return list of file names within the directory that match all given patterns
     */
    public static List<String> getDirectoryFileNames(File baseDirectory, String[] includes, String[] excludes) {
        List<String> files = new ArrayList<String>();

        DirectoryScanner scanner = new DirectoryScanner();

        if (includes != null) {
            scanner.setIncludes(includes);
        }

        if (excludes != null) {
            scanner.setExcludes(excludes);
        }

        scanner.setCaseSensitive(false);
        scanner.addDefaultExcludes();
        scanner.setBasedir(baseDirectory);

        scanner.scan();

        for (String includedFilename : scanner.getIncludedFiles()) {
            files.add(includedFilename);
        }

        return files;
    }

    /**
     * Get the sub directories of the given directory that have the given names
     *
     * @param baseDirectory directory containing the sub directories
     * @param subDirectoryNames list of sub directory names to return
     * @return list of Files pointing to the sub directories
     */
    public static List<File> getSubDirectories(File baseDirectory, List<String> subDirectoryNames) {
        List<File> subDirs = null;

        if (subDirectoryNames != null) {
            subDirs = new ArrayList<File>();

            for (String pluginName : subDirectoryNames) {
                subDirs.add(new File(baseDirectory, pluginName));
            }
        }

        return subDirs;
    }

    /**
     * Retrieves a list of files and directories that are in the given directory, possibly filtered by the
     * list of include patterns or exclude patterns
     *
     * @param baseDirectory directory to retrieve files and directories from
     * @param includes list of patterns to match against for files to include, can include Ant patterns
     * @param excludes list of patterns to match for excluded files, can include Ant patterns
     * @return list of files within the directory that match all given patterns
     */
    public static List<String> getDirectoryContents(File baseDirectory, String[] includes, String[] excludes) {
        List<String> contents = new ArrayList<String>();

        DirectoryScanner scanner = new DirectoryScanner();

        if (includes != null) {
            scanner.setIncludes(includes);
        }

        if (excludes != null) {
            scanner.setExcludes(excludes);
        }

        scanner.setCaseSensitive(false);
        scanner.addDefaultExcludes();
        scanner.setBasedir(baseDirectory);

        scanner.scan();

        for (String includedDirectory : scanner.getIncludedDirectories()) {
            contents.add(includedDirectory);
        }

        for (String includedFilename : scanner.getIncludedFiles()) {
            contents.add(includedFilename);
        }

        return contents;
    }

    /**
     * Copies all the contents from the directory given by the source path to the directory given by the
     * target path
     *
     * <p>
     * If source directory does not exist nothing is performed. The target directory will be created if it
     * does not exist. Any hidden directories (directory names that start with ".") will be deleted from the
     * target directory
     * </p>
     *
     * @param sourceDirectoryPath absolute path to the source directory
     * @param targetDirectoryPath absolute path to the target directory
     * @throws IOException
     */
    public static void copyDirectory(String sourceDirectoryPath, String targetDirectoryPath)
            throws IOException {
        File sourceDir = new File(sourceDirectoryPath);

        if (!sourceDir.exists()) {
            return;
        }

        File targetDir = new File(targetDirectoryPath);
        if (targetDir.exists()) {
            // force removal so the copy starts clean
            FileUtils.forceDelete(targetDir);
        }

        targetDir.mkdir();

        FileUtils.copyDirectoryStructure(sourceDir, targetDir);

        // remove hidden directories from the target
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(targetDir);

        scanner.scan();

        for (String includedDirectory : scanner.getIncludedDirectories()) {
            File subdirectory = new File(targetDir, includedDirectory);

            if (subdirectory.exists() && subdirectory.isDirectory()) {
                if (subdirectory.getName().startsWith(".")) {
                    FileUtils.forceDelete(subdirectory);
                }
            }
        }
    }

    /**
     * Copies all content (files and directories) from the source directory to the target directory, except content
     * that already exists in the target directory (same name and path), in other words it does not override any
     * existing content
     *
     * <p>
     * Files from the source directory can be excluded from the copying by setting one or more patterns in the
     * source excludes list
     * </p>
     *
     * @param sourceDirectory directory to copy content from
     * @param targetDirectory directory to copy content to
     * @param sourceExcludes list of patterns to match on for source exclusions
     * @throws IOException
     */
    public static void copyMissingContent(File sourceDirectory, File targetDirectory, List<String> sourceExcludes)
            throws IOException {
        String[] copyExcludes = null;

        if ((sourceExcludes != null) && !sourceExcludes.isEmpty()) {
            copyExcludes = new String[sourceExcludes.size()];

            copyExcludes = sourceExcludes.toArray(copyExcludes);
        }

        List<String> sourceDirectoryContents = getDirectoryContents(sourceDirectory, null, copyExcludes);
        List<String> targetDirectoryContents = getDirectoryContents(targetDirectory, null, null);

        for (String sourceContent : sourceDirectoryContents) {
            if (targetDirectoryContents.contains(sourceContent)) {
                continue;
            }

            // copy file to target
            File sourceFile = new File(sourceDirectory, sourceContent);
            File targetFile = new File(targetDirectory, sourceContent);

            if (sourceFile.isDirectory()) {
                targetFile.mkdir();
            } else {
                FileUtils.copyFile(sourceFile, targetFile);
            }
        }
    }

    /**
     * Determines if one of the given patterns matches the given name, or if the include list is null
     * or empty the file will be included
     *
     * @param name string to match
     * @param includes list of string patterns to match on
     * @return true if the name is a match, false if not
     */
    public static boolean inIncludeList(String name, String[] includes) {
        if ((includes == null) || (includes.length == 0)) {
            return true;
        }

        for (String include : includes) {
            if (SelectorUtils.matchPath(include, name, false)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if one of the given patterns matches the given name, or if the exclude list is null
     * or empty the file will not be excluded
     *
     * @param name string to match
     * @param excludes list of string patterns to match on
     * @return true if the name is a match, false if not
     */
    public static boolean inExcludeList(String name, String[] excludes) {
        if ((excludes == null) || (excludes.length == 0)) {
            return false;
        }

        for (String exclude : excludes) {
            if (SelectorUtils.matchPath(exclude, name, false)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Iterates through the given list of patterns and checks whether the pattern ends with the given
     * extension or a wildcard, if not the extension is appended to the pattern
     *
     * @param patterns array of patterns to check and append to if necessary
     * @param extension string extension to check for and append if necessary
     */
    public static void addExtensionToPatterns(String[] patterns, String extension) {
        if (patterns == null) {
            return;
        }

        for (int i = 0; i < patterns.length; i++) {
            String pattern = patterns[i];

            if (!(pattern.endsWith("*") || pattern.endsWith(extension))) {
                patterns[i] = pattern + extension;
            }
        }
    }

    /**
     * Builds a list of strings that hold the path from each given file relative to the parent
     * directory
     *
     * @param parentDirectory directory to build path from
     * @param files list of files to build relative paths for
     * @return list of strings containing the relative paths
     */
    public static List<String> getRelativePaths(File parentDirectory, List<File> files) {
        List<String> relativePaths = new ArrayList<String>();

        for (File file : files) {
            relativePaths.add(getRelativePath(parentDirectory, file));
        }

        return relativePaths;
    }

    /**
     * Returns the path of the given file relative to the parent directory
     *
     * @param parentDirectory directory to build path from
     * @param file file to build relative paths for
     * @return string containing the relative path
     */
    public static String getRelativePath(File parentDirectory, File file) {
        String relativePath = null;

        String parentPath = parentDirectory.getPath();
        String childPath = file.getPath();

        if (childPath.startsWith(parentPath + File.separator)) {
            relativePath = childPath.substring(parentPath.length() + 1);
        }

        // switch path separators
        relativePath = relativePath.replaceAll("\\\\", "/");

        return relativePath;
    }

    /**
     * Calculates the path from the first file to the second
     *
     * <p>
     * Assumes there is a common base directory somewhere in the path of both files. Once it finds that base
     * directory, builds the path starting at the from file to it, then adds the path from the base directory
     * to the target file
     * </p>
     *
     * @param fromFile file whose path is the starting point
     * @param toFile file whose path is the ending point
     * @return string containing the path
     */
    public static String calculatePathToFile(File fromFile, File toFile) {
        String pathToFile = "";

        int directoriesUp = 0;
        String parentPath = fromFile.getParent();

        while ((parentPath != null) && !fileMatchesPath(parentPath, toFile)) {
            File parent = new File(parentPath);

            parentPath = parent.getParent();
            directoriesUp += 1;
        }

        if (parentPath != null) {
            for (int i = 0; i < directoriesUp; i++) {
                pathToFile += "../";
            }

            String remainingPath = toFile.getPath().replace(parentPath, "");

            if (remainingPath.startsWith(File.separator)) {
                remainingPath = remainingPath.substring(1);
            }

            // switch path separators
            remainingPath = remainingPath.replaceAll("\\\\", "/");

            // remove file name from path
            if (remainingPath.contains("/")) {
                int separatorIndex = remainingPath.lastIndexOf("/");
                remainingPath = remainingPath.substring(0, separatorIndex + 1);
            } else {
                // file in same directory, no remaining path
                remainingPath = null;
            }

            if (remainingPath != null) {
                pathToFile += remainingPath;
            }
        }

        return pathToFile;
    }

    /**
     * Indicates whether the given file is withing the given path (file's path starts with the given path), note
     * this does not check whether the file exists
     *
     * @param path path to check for
     * @param file file whose path should be checked
     * @return true if the file is contained in the path, false if not
     */
    protected static boolean fileMatchesPath(String path, File file) {
        return file.getPath().startsWith(path);
    }

    /**
     * Orders the list of plugin files and sub directory files according to the given patterns
     *
     * @param pluginFiles list of plugin files to order
     * @param subDirFiles list of sub directory files to order
     * @param loadFirstPatterns list of patterns for files that should be ordered first
     * @param loadLastPatterns list of patterns for files that should be ordered last
     * @param pluginLoadOrder list of patterns for ordering the plugin files
     * @param subDirLoadOrder list of patterns for ordering the sub directory files
     * @return list containing all of the given plugin and sub directory files ordered by the given patterns
     */
    public static List<File> orderFiles(List<File> pluginFiles, List<File> subDirFiles, List<String> loadFirstPatterns,
            List<String> loadLastPatterns, List<String> pluginLoadOrder, List<String> subDirLoadOrder) {
        List<File> orderedFiles = new ArrayList<File>();

        List<File> allThemeFiles = new ArrayList<File>();
        if (pluginFiles != null) {
            allThemeFiles.addAll(pluginFiles);
        }

        if (subDirFiles != null) {
            allThemeFiles.addAll(subDirFiles);
        }

        // build end of the ordered list since those should take priority
        List<File> endFiles = new ArrayList<File>();

        if (loadLastPatterns != null) {
            for (String pattern : loadLastPatterns) {
                endFiles.addAll(matchFiles(allThemeFiles, pattern));
            }
        }

        // build beginning of the ordered list
        if (loadFirstPatterns != null) {
            for (String pattern : loadFirstPatterns) {
                List<File> matchedFiles = matchFiles(allThemeFiles, pattern);
                matchedFiles.removeAll(endFiles);

                orderedFiles.addAll(matchedFiles);
            }
        }

        // add plugin files that have been configured to load before other plugin files
        if (pluginLoadOrder != null) {
            for (String pattern : pluginLoadOrder) {
                List<File> matchedFiles = matchFiles(pluginFiles, pattern);
                matchedFiles.removeAll(endFiles);
                matchedFiles.removeAll(orderedFiles);

                orderedFiles.addAll(matchedFiles);
            }
        }

        // add remaining plugin files
        if (pluginFiles != null) {
            for (File pluginFile : pluginFiles) {
                if (!orderedFiles.contains(pluginFile) && !endFiles.contains(pluginFile)) {
                    orderedFiles.add(pluginFile);
                }
            }
        }

        // add sub dir files that have been configured to load before other sub dir files
        if (subDirLoadOrder != null) {
            for (String pattern : subDirLoadOrder) {
                List<File> matchedFiles = matchFiles(subDirFiles, pattern);
                matchedFiles.removeAll(endFiles);
                matchedFiles.removeAll(orderedFiles);

                orderedFiles.addAll(matchedFiles);
            }
        }

        // add remaining sub dir files
        if (subDirFiles != null) {
            for (File subDirFile : subDirFiles) {
                if (!orderedFiles.contains(subDirFile) && !endFiles.contains(subDirFile)) {
                    orderedFiles.add(subDirFile);
                }
            }
        }

        // now add the end files in reverse to the ordered list
        File[] endFileArray = new File[endFiles.size()];
        endFileArray = endFiles.toArray(endFileArray);

        for (int i = endFileArray.length - 1; i >= 0; i--) {
            orderedFiles.add(endFileArray[i]);
        }

        return orderedFiles;
    }

    /**
     * Iterates through the list of files and returns those files whose names matches the given pattern
     *
     * @param filesToMatch list of files to match
     * @param pattern pattern to match on
     * @return list of files whose name that match the pattern
     */
    public static List<File> matchFiles(List<File> filesToMatch, String pattern) {
        List<File> matchedFiles = new ArrayList<File>();

        for (File file : filesToMatch) {
            if (isMatch(file, pattern)) {
                matchedFiles.add(file);
            }
        }

        return matchedFiles;
    }

    /**
     * Indicates whether the base name for the given file (name without path and the extension) matches
     * the given pattern
     *
     * @param file file to match
     * @param pattern pattern to match on
     * @return true if the file name matches the pattern, false if not
     */
    public static boolean isMatch(File file, String pattern) {
        boolean isMatch = false;

        String fileBasename = FileUtils.basename(file.getName());
        if (fileBasename.endsWith(".")) {
            fileBasename = fileBasename.substring(0, fileBasename.length() - 1);
        }

        if (SelectorUtils.matchPath(pattern, fileBasename, false)) {
            isMatch = true;
        }

        return isMatch;
    }

    /**
     * Returns a list of files from the given list of files, that are contained within one of the given
     * list of directories
     *
     * @param files list of files to filter
     * @param directories list of directories to filter by
     * @return list of files that are contained in the directories
     */
    public static List<File> getContainedFiles(List<File> files, List<File> directories) {
        List<File> directoryFiles = new ArrayList<File>();

        for (File directory : directories) {
            for (File file : files) {
                if (ThemeBuilderUtils.directoryContainsFile(directory, file.getName())) {
                    directoryFiles.add(file);
                }
            }
        }

        return directoryFiles;
    }

    /**
     * Adds the string to the end of the array of strings, or creates a new array containing the string
     * if the array parameter is null
     *
     * @param array string array to add to
     * @param stringToAdd string to add
     * @return array containing all the original array elements plus the string
     */
    public static String[] addToArray(String[] array, String stringToAdd) {
        String[] arrayToAdd = null;

        if (stringToAdd != null) {
            arrayToAdd = new String[1];
            arrayToAdd[0] = stringToAdd;
        }

        return addToArray(array, arrayToAdd);
    }

    /**
     * Adds the second array of strings to the end of the first array of strings, or creates a new array
     * containing the second array elements if the first does not exist
     *
     * <p>
     * Note: Can't use org.apache.commons.lang.ArrayUtils#addAll(java.lang.Object[], java.lang.Object[]) because it
     * doesn't allow String arrays to be passed in. Latest version of ArrayUtils uses generics and does
     * </p>
     *
     * @param array array to add to
     * @param arrayToAdd array to add
     * @return array containing all the strings from both arrays
     */
    public static String[] addToArray(String[] array, String[] arrayToAdd) {
        if (array == null) {
            return arrayToAdd;
        } else if (arrayToAdd == null) {
            return array;
        }

        int combinedArrayLength = array.length + arrayToAdd.length;

        String[] combinedArray = new String[combinedArrayLength];

        for (int i = 0; i < array.length; i++) {
            combinedArray[i] = array[i];
        }

        for (int i = 0; i < arrayToAdd.length; i++) {
            combinedArray[i + array.length] = arrayToAdd[i];
        }

        return combinedArray;
    }

    /**
     * Builds a string formed with the name for each file in the list delimited by commas
     *
     * @param list list to join names for
     * @return string containing all the file names
     */
    public static String joinFileList(List<File> list) {
        String joinedString = null;

        if (list != null) {
            joinedString = "";

            for (File file : list) {
                if (!"".equals(joinedString)) {
                    joinedString += ",";
                }

                joinedString += file.getName();
            }
        }

        return joinedString;
    }

}
