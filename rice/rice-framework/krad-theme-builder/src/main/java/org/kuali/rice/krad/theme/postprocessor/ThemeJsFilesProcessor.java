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

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.theme.util.ThemeBuilderConstants;
import org.kuali.rice.krad.theme.util.ThemeBuilderUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;

/**
 * Theme files processor for JavaScript files
 *
 * <p>
 * Merge contents are checked for a trailing semi-colon, and altered if not found to contain one. For
 * minification, the Google Closure compiler is used: <a link="https://developers.google.com/closure/">Google
 * Closure</a>
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see ThemeFilesProcessor
 * @see com.google.javascript.jscomp.Compiler
 */
public class ThemeJsFilesProcessor extends ThemeFilesProcessor {
    private static final Logger LOG = Logger.getLogger(ThemeJsFilesProcessor.class);

    public ThemeJsFilesProcessor(String themeName, File themeDirectory, Properties themeProperties,
            Map<String, File> themePluginDirsMap, File workingDir, String projectVersion) {
        super(themeName, themeDirectory, themeProperties, themePluginDirsMap, workingDir, projectVersion);
    }

    /**
     * @see ThemeFilesProcessor#getFileTypeExtension()
     */
    @Override
    protected String getFileTypeExtension() {
        return ThemeBuilderConstants.FileExtensions.JS;
    }

    /**
     * @see ThemeFilesProcessor#getExcludesConfigKey()
     */
    @Override
    protected String getExcludesConfigKey() {
        return ThemeBuilderConstants.ThemeConfiguration.JS_EXCLUDES;
    }

    /**
     * @see ThemeFilesProcessor#getFileTypeDirectoryName()
     */
    @Override
    protected String getFileTypeDirectoryName() {
        return ThemeBuilderConstants.ThemeDirectories.SCRIPTS;
    }

    /**
     * @see ThemeFilesProcessor#getFileListingConfigKey()
     */
    @Override
    protected String getFileListingConfigKey() {
        return ThemeBuilderConstants.DerivedConfiguration.THEME_JS_FILES;
    }

    /**
     * Adds JS files from the krad scripts directory to the theme file list
     *
     * @see ThemeFilesProcessor#addAdditionalFiles(java.util.List<java.io.File>)
     */
    @Override
    protected void addAdditionalFiles(List<File> themeFiles) {
        File kradScriptDir = new File(this.workingDir, ThemeBuilderConstants.KRAD_SCRIPTS_DIRECTORY);

        themeFiles.addAll(ThemeBuilderUtils.getDirectoryFiles(kradScriptDir, getFileIncludes(), null));
    }

    /**
     * Sorts the list of JS files from the plugin and sub directories
     *
     * <p>
     * The sorting algorithm is as follows:
     *
     * <ol>
     * <li>Any files which match patterns configured by the property <code>jsLoadFirst</code></li>
     * <li>JS files from plugin directories, first ordered by any files that match patterns configured with
     * <code>pluginJsLoadOrder</code>, followed by all remaining plugin files</li>
     * <li>KRAD script files, in the order retrieved from {@link #retrieveKradScriptLoadOrder()}</li>
     * <li>JS files from the theme subdirectory, first ordered by any files that match patterns configured
     * with <code>themeJsLoadOrder</code>, then any remaining theme files</li>
     * <li>Files that match patterns configured by the property <code>jsLoadLast</code>. Note any files that
     * match here will be excluded from any of the previous steps</li>
     * </ol>
     * </p>
     *
     * @see ThemeFilesProcessor#sortThemeFiles(java.util.List<java.io.File>, java.util.List<java.io.File>)
     * @see #retrieveKradScriptLoadOrder()
     */
    @Override
    protected List<File> sortThemeFiles(List<File> pluginFiles, List<File> subDirFiles) {
        List<String> loadJsFirst = getThemePropertyValue(ThemeBuilderConstants.ThemeConfiguration.JS_LOAD_FIRST);
        List<String> loadJsLast = getThemePropertyValue(ThemeBuilderConstants.ThemeConfiguration.JS_LOAD_LAST);

        List<String> pluginJsLoadOrder = getThemePropertyValue(
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_JS_LOAD_ORDER);
        List<String> jsLoadOrder = getThemePropertyValue(ThemeBuilderConstants.ThemeConfiguration.THEME_JS_LOAD_ORDER);

        // krad scripts should go before theme js files, order for these is configured in a load.properties file
        List<String> kradScriptOrder = null;
        try {
            kradScriptOrder = retrieveKradScriptLoadOrder();
        } catch (IOException e) {
            throw new RuntimeException("Unable to pull KRAD load order property key", e);
        }

        if (kradScriptOrder != null) {
            if (jsLoadOrder == null) {
                jsLoadOrder = new ArrayList<String>();
            }

            jsLoadOrder.addAll(0, kradScriptOrder);
        }

        return ThemeBuilderUtils.orderFiles(pluginFiles, subDirFiles, loadJsFirst, loadJsLast, pluginJsLoadOrder,
                jsLoadOrder);
    }

    /**
     * Builds a list of KRAD script file names that indicates the order they should be loaded in
     *
     * <p>
     * Populates a properties object from the file {@link org.kuali.rice.krad.theme.util.ThemeBuilderConstants#KRAD_SCRIPT_LOAD_PROPERTIES_FILE}
     * located in the KRAD script directory. Then pulls the value for the property org.kuali.rice.krad.theme.util.ThemeBuilderConstants#LOAD_ORDER_PROPERTY_KEY
     * to get the configured file load order.
     *
     * The KRAD scripts directory is then listed to get the remaining files names which are added at the end
     * of the file list
     * </p>
     *
     * @return list of KRAD file names (not including path or file extension)
     * @throws IOException
     */
    protected List<String> retrieveKradScriptLoadOrder() throws IOException {
        List<String> scriptLoadOrder = new ArrayList<String>();

        File kradScriptsDir = new File(this.workingDir, ThemeBuilderConstants.KRAD_SCRIPTS_DIRECTORY);

        File loadPropertiesFile = new File(kradScriptsDir, ThemeBuilderConstants.KRAD_SCRIPT_LOAD_PROPERTIES_FILE);
        if (!loadPropertiesFile.exists()) {
            throw new RuntimeException("load.properties file not found in KRAD scripts directory");
        }

        Properties loadProperties = null;

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(loadPropertiesFile);

            loadProperties = new Properties();
            loadProperties.load(fileInputStream);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }

        // pull the load order property from properties file
        if (loadProperties.containsKey(ThemeBuilderConstants.LOAD_ORDER_PROPERTY_KEY)) {
            scriptLoadOrder = ThemeBuilderUtils.getPropertyValueAsList(ThemeBuilderConstants.LOAD_ORDER_PROPERTY_KEY,
                    loadProperties);
        }

        // get remaining files from the directory
        List<String> scriptFileNames = ThemeBuilderUtils.getDirectoryFileNames(kradScriptsDir, null, null);
        if (scriptFileNames != null) {
            for (String scriptFileName : scriptFileNames) {
                // remove file extension
                String baseScriptFileName = StringUtils.substringBeforeLast(scriptFileName, ".");

                if (!scriptLoadOrder.contains(baseScriptFileName)) {
                    scriptLoadOrder.add(baseScriptFileName);
                }
            }
        }

        return scriptLoadOrder;
    }

    /**
     * Checks the given file contents to determine if the last character is a semicolon, if not the contents
     * are appended with a semicolon to prevent problems when other content is appended
     *
     * @see ThemeFilesProcessor#processMergeFileContents(java.lang.String, java.io.File, java.io.File)
     */
    @Override
    protected String processMergeFileContents(String fileContents, File fileToMerge, File mergedFile)
            throws IOException {
        if ((fileContents != null) && !fileContents.matches(
                ThemeBuilderConstants.Patterns.JS_SEMICOLON_PATTERN)) {
            fileContents += ";";
        }

        return fileContents;
    }

    /**
     * Minifies the JS contents from the given merged file into the minified file
     *
     * <p>
     * Minification is performed using the Google Closure compiler, using
     * com.google.javascript.jscomp.CompilationLevel#SIMPLE_OPTIMIZATIONS and EcmaScript5 language level
     * </p>
     *
     * @see ThemeFilesProcessor#minify(java.io.File, java.io.File)
     * @see com.google.javascript.jscomp.Compiler
     */
    @Override
    protected void minify(File mergedFile, File minifiedFile) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        OutputStreamWriter writer = null;
        InputStreamReader reader = null;

        LOG.info("Populating minified JS file: " + minifiedFile.getPath());

        try {
            out = new FileOutputStream(minifiedFile);
            writer = new OutputStreamWriter(out);

            in = new FileInputStream(mergedFile);
            reader = new InputStreamReader(in);

            CompilerOptions options = new CompilerOptions();
            CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
            options.setLanguageIn(CompilerOptions.LanguageMode.ECMASCRIPT5);
            options.setExtraAnnotationNames(ignoredAnnotations());

            SourceFile input = SourceFile.fromInputStream(mergedFile.getName(), in);
            List<SourceFile> externs = Collections.emptyList();

            Compiler compiler = new Compiler();
            compiler.compile(externs, Arrays.asList(input), options);

            writer.append(compiler.toSource());
            writer.flush();
        } finally {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Build a Set of annotations for the compiler to ignore in jsdoc blocks
     *
     * @return Iterable<String>
     */
    protected Set<String> ignoredAnnotations() {
        Set<String> annotations = new HashSet<String>();
        annotations.add("dtopt");
        annotations.add("result");
        annotations.add("cat");
        annotations.add("parm");

        return annotations;
    }

}
