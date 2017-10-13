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

/**
 * Contains constants used by the theme builder module
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ThemeBuilderConstants {
    public static final String DEFAULT_THEMES_DIRECTORY = "/themes";
    public static final String DEFAULT_PLUGINS_DIRECTORY = "/plugins";
    public static final String KRAD_SCRIPTS_DIRECTORY = "/krad/scripts";
    public static final String THEME_PROPERTIES_FILE = "theme.properties";
    public static final String THEME_DERIVED_PROPERTIES_FILE = "theme-derived.properties";

    public static final String KRAD_SCRIPT_LOAD_PROPERTIES_FILE = "load.properties";
    public static final String LOAD_ORDER_PROPERTY_KEY = "scriptLoadOrder";

    public static final String MIN_FILE_SUFFIX = ".min";
    public static final String KRAD_SCRIPT_PREFIX = "krad.";

    /**
     * Processed file extensions
     */
    public static class FileExtensions {
        public static final String LESS = ".less";
        public static final String CSS = ".css";
        public static final String JS = ".js";
    }

    /**
     * Directories that are expected to be present in the theme directory
     */
    public static class ThemeDirectories {
        public static final String IMAGES = "images";
        public static final String SCRIPTS = "scripts";
        public static final String STYLESHEETS = "stylesheets";
        public static final String INCLUDES = "includes";
    }

    /**
     * Valid property keys in the theme properties file
     */
    public static class ThemeConfiguration {
        public static final String CSS_EXCLUDES = "cssExcludes";
        public static final String JS_EXCLUDES = "jsExcludes";
        public static final String LESS_INCLUDES = "lessIncludes";
        public static final String LESS_EXCLUDES = "lessExcludes";
        public static final String PARENT = "parent";
        public static final String PARENT_EXCLUDES = "parentExcludes";
        public static final String PLUGIN_INCLUDES = "pluginIncludes";
        public static final String PLUGIN_EXCLUDES = "pluginExcludes";
        public static final String PLUGIN_FILE_EXCLUDES = "pluginFileExcludes";
        public static final String ADDITIONAL_OVERLAYS = "additionalOverlays";
        public static final String CSS_LOAD_FIRST = "cssLoadFirst";
        public static final String CSS_LOAD_LAST = "cssLoadLast";
        public static final String JS_LOAD_FIRST = "jsLoadFirst";
        public static final String JS_LOAD_LAST = "jsLoadLast";
        public static final String PLUGIN_JS_LOAD_ORDER = "pluginJsLoadOrder";
        public static final String PLUGIN_CSS_LOAD_ORDER = "pluginCssLoadOrder";
        public static final String THEME_JS_LOAD_ORDER = "themeJsLoadOrder";
        public static final String THEME_CSS_LOAD_ORDER = "themeCssLoadOrder";
    }

    /**
     * Derived property keys that are added to the theme properties
     */
    public static class DerivedConfiguration {
        public static final String THEME_PLUGIN_NAMES = "themePluginNames";
        public static final String THEME_LESS_FILES = "themeLessFiles";
        public static final String THEME_CSS_FILES = "themeCssFiles";
        public static final String THEME_JS_FILES = "themeJsFiles";
    }

    /**
     * Patterns used to do string matching
     */
    public static class Patterns {
        public static final String CSS_URL_PATTERN =
                "(?is)src\\b\\s*=\\s*['\"](.*?)['\"]|url\\b\\s*\\(\\s*['\"]?(.*?)['\"]?\\s*\\)";
        public static final String JS_SEMICOLON_PATTERN = "(?s).*;\\s*$";
        public static final String ANT_MATCH_ALL = "**/*";
        public static final String ANT_MATCH_DIR = "/*";
    }
}
