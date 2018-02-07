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
package org.kuali.rice.krad.datadictionary.validation;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.krad.datadictionary.exporter.ExportMap;

import java.util.regex.Pattern;

/**
 * Abstraction of the regular expressions used to validate attribute values.
 */
@Deprecated
abstract public class FieldLevelValidationPattern extends ValidationPattern {
    protected Pattern regexPattern;

    /**
     * Uses the key returned by getConfigurationRegexKey to fetch the validationPattern's regex string from the
     * ConfigurationService
     *
     * @see org.kuali.rice.krad.datadictionary.validation.ValidationPattern#getRegexString()
     */
    protected String getRegexString() {
        return (String) CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                "validationPatternRegex." + getPatternTypeName());
    }

    /**
     * @return the key used to retrieve the validationPattern's type name, which is used as the suffix of the regex
     *         property key, as
     *         the type entry in the exportMap, etc.
     */
    abstract protected String getPatternTypeName();

    /**
     * @return regular expression Pattern generated using the individual ValidationPattern subclass
     */
    public final Pattern getRegexPattern() {
        if (regexPattern == null) {
            StringBuffer completeRegex = new StringBuffer("^");
            completeRegex.append(getRegexString());
            completeRegex.append("$");
            regexPattern = Pattern.compile(completeRegex.toString());
        }
        return regexPattern;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.ValidationPattern#buildExportMap(java.lang.String)
     */
    public ExportMap buildExportMap(String exportKey) {
        ExportMap exportMap = new ExportMap(exportKey);

        exportMap.set("type", getPatternTypeName());

        return exportMap;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.validation.ValidationPattern#getValidationErrorMessageKey()
     */
    @Override
    public String getValidationErrorMessageKey() {
        StringBuilder buf = new StringBuilder();
        buf.append("error.format.").append(getClass().getName());
        return buf.toString();
    }
}
