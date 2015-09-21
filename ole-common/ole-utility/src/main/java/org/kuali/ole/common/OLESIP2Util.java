package org.kuali.ole.common;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by gayathria on 15/12/14.
 */
public class OLESIP2Util {

    /*
    Used to get default currency instance
     */

    public static Currency getDefaultCurrency() {
        return Currency.getInstance(Locale.getDefault());
    }

    /**
     * Converts the given boolean to string. false = 0, true = 1.
     *
     * @param value boolean value
     * @return false = 0, true = 1
     */
    public static String bool2Int(boolean value) {
        if (value) {
            return "1";
        }
        return "0";
    }

    /**
     * Converts the given boolean to character. false = N, true = Y.
     *
     * @param value boolean value
     * @return false = N, true = Y
     */
    public static String bool2Char(boolean value) {
        if (value) {
            return "Y";
        }
        return "N";
    }

    /**
     * Converts the given boolean to character. false = ' ', true = 'Y'.
     *
     * @param value boolean value
     * @return false = ' ', true = 'Y'
     */
    public static String bool2CharEmpty(boolean value) {
        if (value) {
            return "Y";
        }
        return " ";
    }

    /**
     * Converts the given integer to a fixed length string by adding
     * leading zeros to the given number that its length equals to the
     * given length.
     *
     * @param value  integer value to be converted
     * @param length length of the output string
     * @return string presentation of the given integer
     */
    public static String intToFixedLengthString(int value, int length) {
        return String.format("%0" + length + "d", value);
    }
}
