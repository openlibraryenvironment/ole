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

import org.kuali.rice.krad.datadictionary.exporter.ExportMap;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.regex.Pattern;

/**
 * Abstraction of the regular expressions used to validate attribute values.
 */
@Deprecated
abstract public class CharacterLevelValidationPattern extends ValidationPattern {
    protected Pattern regexPattern;

    protected int maxLength = -1;
    protected int exactLength = -1;

    /**
     * Sets maxLength parameter for the associated regex.
     *
     * @param maxLength
     */
    public void setMaxLength(int maxLength) {
        if (this.exactLength != -1) {
            throw new IllegalStateException(
                    "illegal attempt to set maxLength after mutually-exclusive exactLength has been set");
        }

        this.maxLength = maxLength;
    }

    /**
     * @return current maxLength, or -1 if none has been set
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Sets exactLength parameter for the associated regex.
     *
     * @param exactLength
     */
    public void setExactLength(int exactLength) {
        if (this.maxLength != -1) {
            throw new IllegalStateException(
                    "illegal attempt to set exactLength after mutually-exclusive maxLength has been set");
        }

        this.exactLength = exactLength;
    }

    /**
     * @return current exactLength, or -1 if none has been set
     */
    public int getExactLength() {
        return exactLength;
    }

    /**
     * @return regular expression Pattern generated using the individual ValidationPattern subclass
     */
    final public Pattern getRegexPattern() {
        if (regexPattern == null) {
            String regexString = getRegexString();

            StringBuffer completeRegex = new StringBuffer("^");
            completeRegex.append(getRegexString());

            if (maxLength != -1) {
                completeRegex.append("{0," + maxLength + "}");
            } else if (exactLength != -1) {
                completeRegex.append("{" + exactLength + "}");
            } else {
                completeRegex.append("*");
            }

            completeRegex.append("$");

            regexPattern = Pattern.compile(completeRegex.toString());
        }
        return regexPattern;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.ValidationPattern#buildExportMap(java.lang.String)
     */
    final public ExportMap buildExportMap(String exportKey) {
        ExportMap exportMap = new ExportMap(exportKey);

        if (getMaxLength() != -1) {
            exportMap.set("maxLength", Integer.toString(getMaxLength()));
        } else if (getExactLength() != -1) {
            exportMap.set("exactLength", Integer.toString(getExactLength()));
        }

        extendExportMap(exportMap);

        return exportMap;
    }

    /**
     * Extends the given (parent class) exportMap as needed to represent subclass instances
     *
     * @param exportMap
     */
    abstract public void extendExportMap(ExportMap exportMap);

    @Override
    public String[] getValidationErrorMessageParameters(String attributeLabel) {
        if (getMaxLength() != -1) {
            return new String[]{attributeLabel, String.valueOf(getMaxLength())};
        }
        if (getExactLength() != -1) {
            return new String[]{attributeLabel, String.valueOf(getExactLength())};
        }
        return new String[]{attributeLabel};
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.validation.ValidationPattern#getValidationErrorMessageKey()
     */
    @Override
    public String getValidationErrorMessageKey() {
        StringBuilder buf = new StringBuilder();
        buf.append("error.format.").append(getClass().getName()).append(getValidationErrorMessageKeyOptions());
        if (getMaxLength() != -1) {
            buf.append(".maxLength");
        }
        if (getExactLength() != -1) {
            buf.append(".exactLength");
        }
        return buf.toString();
    }

    protected String getValidationErrorMessageKeyOptions() {
        return KRADConstants.EMPTY_STRING;
    }
}
