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

import com.yahoo.platform.yui.compressor.CssCompressor;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Theme files processor for CSS files
 *
 * <p>
 * Merge contents are processed to rewrite any URLs (to images) for the changed path. CSS includes are not
 * rewritten and will not work correctly in the merged file. For minification, the YUI compressor is
 * used: <a href="http://yui.github.io/yuicompressor/">YUI Compressor</a>
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 * @see ThemeFilesProcessor
 * @see com.yahoo.platform.yui.compressor.CssCompressor
 */
public class ThemeCssFilesProcessor extends ThemeFilesProcessor {
    private static final Logger LOG = Logger.getLogger(ThemeCssFilesProcessor.class);

    protected int linebreak = -1;

    public ThemeCssFilesProcessor(String themeName, File themeDirectory, Properties themeProperties,
            Map<String, File> themePluginDirsMap, File workingDir, String projectVersion) {
        super(themeName, themeDirectory, themeProperties, themePluginDirsMap, workingDir, projectVersion);
    }

    /**
     * @see ThemeFilesProcessor#getFileTypeExtension()
     */
    @Override
    protected String getFileTypeExtension() {
        return ThemeBuilderConstants.FileExtensions.CSS;
    }

    /**
     * @see ThemeFilesProcessor#getExcludesConfigKey()
     */
    @Override
    protected String getExcludesConfigKey() {
        return ThemeBuilderConstants.ThemeConfiguration.CSS_EXCLUDES;
    }

    /**
     * @see ThemeFilesProcessor#getFileTypeDirectoryName()
     */
    @Override
    protected String getFileTypeDirectoryName() {
        return ThemeBuilderConstants.ThemeDirectories.STYLESHEETS;
    }

    /**
     * @see ThemeFilesProcessor#getFileListingConfigKey()
     */
    @Override
    protected String getFileListingConfigKey() {
        return ThemeBuilderConstants.DerivedConfiguration.THEME_CSS_FILES;
    }

    /**
     * @see ThemeFilesProcessor#addAdditionalFiles(java.util.List<java.io.File>)
     */
    @Override
    protected void addAdditionalFiles(List<File> themeFiles) {
        // no additional files
    }

    /**
     * Sorts the list of CSS files from the plugin and sub directories
     *
     * <p>
     * The sorting algorithm is as follows:
     *
     * <ol>
     * <li>Any files which match patterns configured by the property <code>cssLoadFirst</code></li>
     * <li>CSS files from plugin directories, first ordered by any files that match patterns configured with
     * <code>pluginCssLoadOrder</code>, followed by all remaining plugin files</li>
     * <li>CSS files from the theme subdirectory, first ordered by any files that match patterns configured
     * with <code>themeCssLoadOrder</code>, then any remaining theme files</li>
     * <li>Files that match patterns configured by the property <code>cssLoadLast</code>. Note any files that
     * match here will be excluded from any of the previous steps</li>
     * </ol>
     * </p>
     *
     * @see ThemeFilesProcessor#sortThemeFiles(java.util.List<java.io.File>, java.util.List<java.io.File>)
     */
    @Override
    protected List<File> sortThemeFiles(List<File> pluginFiles, List<File> subDirFiles) {
        List<String> loadCssFirst = getThemePropertyValue(ThemeBuilderConstants.ThemeConfiguration.CSS_LOAD_FIRST);
        List<String> loadCssLast = getThemePropertyValue(ThemeBuilderConstants.ThemeConfiguration.CSS_LOAD_LAST);

        List<String> pluginCssLoadOrder = getThemePropertyValue(
                ThemeBuilderConstants.ThemeConfiguration.PLUGIN_CSS_LOAD_ORDER);
        List<String> cssLoadOrder = getThemePropertyValue(
                ThemeBuilderConstants.ThemeConfiguration.THEME_CSS_LOAD_ORDER);

        return ThemeBuilderUtils.orderFiles(pluginFiles, subDirFiles, loadCssFirst, loadCssLast, pluginCssLoadOrder,
                cssLoadOrder);
    }

    /**
     * Processes the merge contents to rewrite any URLs necessary for the directory change
     *
     * @see ThemeFilesProcessor#processMergeFileContents(java.lang.String, java.io.File, java.io.File)
     */
    @Override
    protected String processMergeFileContents(String fileContents, File fileToMerge, File mergedFile)
            throws IOException {
        return rewriteCssUrls(fileContents, fileToMerge, mergedFile);
    }

    /**
     * Performs URL rewriting within the given CSS contents
     *
     * <p>
     * The given merge file (where the merge contents come from) and the merged file (where they are going to)
     * is used to determine the path difference. Once that path difference is found, the contents are then matched
     * to find any URLs. For each relative URL (absolute URLs are not modified), the path is adjusted and
     * replaced into the contents.
     *
     * ex. suppose the merged file is /plugins/foo/plugin.css, and the merged file is
     * /themes/mytheme/stylesheets/merged.css, the path difference will then be '../../../plugins/foo/'. So a URL
     * in the CSS contents of 'images/image.png' will get rewritten to '../../../plugins/foo/images/image.png'
     * </p>
     *
     * @param css contents to adjust URLs for
     * @param mergeFile file that provided the merge contents
     * @param mergedFile file the contents will be going to
     * @return css contents, with possible adjusted URLs
     * @throws IOException
     */
    protected String rewriteCssUrls(String css, File mergeFile, File mergedFile) throws IOException {
        String urlAdjustment = ThemeBuilderUtils.calculatePathToFile(mergedFile, mergeFile);

        if (StringUtils.isBlank(urlAdjustment)) {
            // no adjustment needed
            return css;
        }

        // match all URLs in css string and then adjust each one
        Pattern urlPattern = Pattern.compile(ThemeBuilderConstants.Patterns.CSS_URL_PATTERN);

        Matcher matcher = urlPattern.matcher(css);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String cssStatement = matcher.group();

            String cssUrl = null;
            if (matcher.group(1) != null) {
                cssUrl = matcher.group(1);
            } else {
                cssUrl = matcher.group(2);
            }

            if (cssUrl != null) {
                // only adjust URL if it is relative
                String modifiedUrl = cssUrl;

                if (!cssUrl.startsWith("/")) {
                    modifiedUrl = urlAdjustment + cssUrl;
                }

                String modifiedStatement = Matcher.quoteReplacement(cssStatement.replace(cssUrl, modifiedUrl));

                matcher.appendReplacement(sb, modifiedStatement);
            }
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Minifies the CSS contents from the given merged file into the minified file
     *
     * <p>
     * Minification is performed using the YUI Compressor compiler with no line break
     * </p>
     *
     * @see ThemeFilesProcessor#minify(java.io.File, java.io.File)
     * @see com.yahoo.platform.yui.compressor.CssCompressor
     */
    @Override
    protected void minify(File mergedFile, File minifiedFile) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        OutputStreamWriter writer = null;
        InputStreamReader reader = null;

        LOG.info("Populating minified CSS file: " + minifiedFile.getPath());

        try {
            out = new FileOutputStream(minifiedFile);
            writer = new OutputStreamWriter(out);

            in = new FileInputStream(mergedFile);
            reader = new InputStreamReader(in);

            CssCompressor compressor = new CssCompressor(reader);
            compressor.compress(writer, this.linebreak);

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
}
