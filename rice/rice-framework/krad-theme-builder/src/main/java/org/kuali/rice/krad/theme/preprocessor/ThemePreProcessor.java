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

import java.io.File;
import java.util.Properties;

/**
 * Theme pre processors are registered with {@link org.kuali.rice.krad.theme.ThemeBuilder} and invoked on
 * each theme processed
 *
 * <p>
 * Pre processors are invoked after overlays for the theme have been applied, but before any merging and
 * minification. Therefore they can create or modify assets for the theme
 * </p>
 *
 * <p>
 * Pre processors may also have configuration that is supplied through the theme properties
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ThemePreProcessor {

    /**
     * Invoked to perform processing on the given theme
     *
     * @param themeName name of the theme to process
     * @param themeDirectory directory containing the theme assets
     * @param themeProperties properties for the theme containing its configuration
     */
    public void processTheme(String themeName, File themeDirectory, Properties themeProperties);
}
