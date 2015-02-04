/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.validation.StandardNumberValidation;

import java.util.regex.Pattern;


public class StandardNumberValidationImpl implements StandardNumberValidation {

    public static final String ISBN_CONSTANT = "ISBN";
    public static final String ISSN_CONSTANT = "ISSN";
    public static final String OCLC_CONSTANT = "OCLC";
    public static final String INVALID_MSG = " number is invalid";
    public static final String INVALID_STANDARDNUMBERTYPE_MSG = "Standart Number type should be ISBN or ISSN";

    public boolean validateISBN(String input) {
        // D-DDD-DDDDD-X or DDD-D-DDD-DDDDD-X 
        if (input != null && ((input.length() == 13 && Pattern.matches("\\A\\d{1}\\-\\d{3}\\-\\d{5}\\-[X\\d]\\z", input)) ||
                (input.length() == 13 && Pattern.matches("\\A\\d{1}\\-\\d{4}\\-\\d{4}\\-[X\\d]\\z", input)) ||
                (input.length() == 17 && Pattern.matches("\\A\\d{3}\\-\\d{1}\\-\\d{2}\\-\\d{6}\\-[X\\d]\\z", input)) ||
                (input.length() == 17 && Pattern.matches("\\A\\d{3}\\-\\d{1}\\-\\d{3}\\-\\d{5}\\-[X\\d]\\z", input)))) {
            int tot = 0;
            int remainder = 0;
            char compChkDigit;

            input = input.replaceAll("-", "");

            switch (input.length()) {
                case 10:
                    int[] weightFactor = {10, 9, 8, 7, 6, 5, 4, 3, 2};
                    for (int i = 0; i <= 8; i++)
                        tot = tot + (Character.getNumericValue(input.charAt(i)) * weightFactor[i]);

                    remainder = (11 - (tot % 11)) % 11;

                    if (remainder < 10)
                        compChkDigit = Character.forDigit(remainder, 10);
                    else
                        compChkDigit = 'X';

                    if (compChkDigit == input.charAt(9))
                        return true;
                    else
                        return false;

                case 13:
                    int weight = 0;
                    for (int i = 0; i <= 11; i++) {
                        if (i % 2 == 0) weight = 1;
                        else weight = 3;
                        tot = tot + (Character.getNumericValue(input.charAt(i)) * weight);
                    }

                    remainder = (10 - (tot % 10)) % 10;
                    if (remainder < 10)
                        compChkDigit = Character.forDigit(remainder, 10);
                    else
                        compChkDigit = 'X';

                    if (compChkDigit == input.charAt(12))
                        return true;
                    else
                        return false;
            }
        }
        return false;
    }

    public boolean validateISSN(String input) {
        // NNNN-NNNX
        if (input != null && (input.length() == 9 && Pattern.matches("\\A\\d{4}\\-\\d{3}[X\\d]\\z", input))) {
            int tot = 0;
            char compChkDigit;
            int[] weightFactor = {8, 7, 6, 5, 4, 3, 2};

            input = input.replaceAll("-", "");

            for (int i = 0; i <= 6; i++)
                tot = tot + (Character.getNumericValue(input.charAt(i)) * weightFactor[i]);


            int remainder = (11 - (tot % 11)) % 11;

            if (remainder < 10)
                compChkDigit = Character.forDigit(remainder, 10);
            else
                compChkDigit = 'X';

            if (compChkDigit == input.charAt(7))
                return true;
            else
                return false;
        }
        return false;
    }

    public boolean validateOCLC(String input) {
        return true;
    }

    public boolean validateStandardNumbers(String inputType, String inputValue) {
        if (ISBN_CONSTANT.equalsIgnoreCase(inputType))
            return validateISBN(inputValue);
        else if (ISSN_CONSTANT.equalsIgnoreCase(inputType))
            return validateISSN(inputValue);
        else if (OCLC_CONSTANT.equalsIgnoreCase(inputType))
            return validateOCLC(inputValue);
        else
            return false;
    }

    public String isValidStandardNumbers(BibInfoBean bibInfoBean) {
        boolean ret = false;
        String standardNumber = bibInfoBean.getStandardNumber();
        String typeOfStandardNumber = bibInfoBean.getTypeOfStandardNumber();
        if (!StringUtils.isEmpty(standardNumber) && !StringUtils.isEmpty(typeOfStandardNumber)) {
            typeOfStandardNumber = bibInfoBean.getTypeOfStandardNumber().toUpperCase();
            if (typeOfStandardNumber.equals(ISBN_CONSTANT) || typeOfStandardNumber.equals(ISSN_CONSTANT)) {
                boolean validNumber = validateStandardNumbers(typeOfStandardNumber, standardNumber);
                StringBuffer buffer = new StringBuffer();
                if (!validNumber) {
                    if (!standardNumber.isEmpty())
                        buffer.append(typeOfStandardNumber + INVALID_MSG);
                }
                if (buffer.toString().isEmpty()) {
                    return "true";
                } else {
                    return buffer.toString();
                }
            } else {
                return INVALID_STANDARDNUMBERTYPE_MSG;
            }
        } else {
            return "";
        }
    }

}
