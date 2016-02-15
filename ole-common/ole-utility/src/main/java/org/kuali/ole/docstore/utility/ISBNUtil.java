package org.kuali.ole.docstore.utility;

import org.kuali.ole.docstore.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 4/6/12
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ISBNUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DocStoreSettingsUtil.class);

    public String normalizeISBN(Object isbn) throws OleException {
        String value = (String) isbn;
        if (value != null) {
            String modifiedValue = getModifiedString(value);
            int len = modifiedValue.length();
            if (len == 13) {
                return modifiedValue;
            } else if (len == 10) {
                String regex = "[0-9]{9}[xX]{1}";
                String regexNum = "[0-9]{10}";
                value = getIsbnRegexValue(regexNum, modifiedValue);
                if (value.length() == 0) {
                    value = getIsbnRegexValue(regex, modifiedValue);
                }
                if (value.length() > 0) {
                    value = calculateIsbnValue(value);
                }
            } else {
                throw new OleException("Invalid ISBN Value: " + isbn);
            }
            if (value.length() == 0) {
                throw new OleException("Normalization failed" + isbn);
            }
        }
        return value;
    }

    private String getModifiedString(String value) {
        String modifiedValue = value;
        if (modifiedValue.contains("(") && modifiedValue.contains(")")) {
            String parenthesesValue = modifiedValue
                    .substring(modifiedValue.indexOf("("), modifiedValue.lastIndexOf(")") + 1);
            modifiedValue = modifiedValue.replace(parenthesesValue, "");
        }
        modifiedValue = modifiedValue.replaceAll("[-:\\s]", "");
        if(modifiedValue.length() > 13 ){
            modifiedValue = modifiedValue.substring(0,10);
        }
        return modifiedValue;
    }

    private String getIsbnRegexValue(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        String matchingValue = null;
        if (matcher.find()) {
            matchingValue = matcher.group(0);
            matchingValue = value.substring(0, 9);
        } else {
            matchingValue = "";
        }
        return matchingValue;
    }

    private String calculateIsbnValue(String value) {
        String num = value;
        if (num.length() == 9) {
            num = "978" + num;
            try {
                num = getNormalizedIsbn(num);
            } catch (Exception e) {
                LOG.error("Unable to normalize the modified ISBN value " + value + e.getMessage());
                num = "";
            }
        }
        return num;
    }

    private String getNormalizedIsbn(String value) {
        String normalizeIsbn = value;
        int count = 0;
        int multiple = 1;
        for (int i = 0; i < value.length(); i++) {
            Character c = new Character(value.charAt(i));
            int j = Integer.parseInt(c.toString());
            int sum = j * multiple;
            count = count + sum;
            if (i != 0 && i % 2 != 0) {
                multiple = 1;
            } else {
                multiple = 3;
            }
        }
        count = count % 10;
        if (count == 0) {
            count = 0;
        } else {
            count = 10 - count;
        }
        normalizeIsbn = normalizeIsbn + Integer.toString(count);
        return normalizeIsbn;
    }
}
