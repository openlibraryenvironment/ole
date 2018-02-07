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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.uif.UifConstants;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

/**
 * ValidationUtils provides static utility methods for validation processing
 *
 * <p>Inherited from Kuali Student and adapted extensively</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ValidationUtils {

    /**
     * constructs a path by appending the attribute name to the provided path
     *
     * @param attributePath - a string representation of specifically which attribute (at some depth) is being accessed
     * @param attributeName - the attribute name
     * @return the path
     */
    public static String buildPath(String attributePath, String attributeName) {
        if (StringUtils.isNotBlank(attributeName)) {
            if (StringUtils.isNotBlank(attributePath)) {
                return new StringBuilder(attributePath).append(".").append(attributeName).toString();
            }

            return attributeName;
        }
        return attributePath;
    }

    /**
     * Used to get the rightmost index value of an attribute path.
     *
     * @param attributePath
     * @return the right index of value of attribute path, -1 if path has no index
     */
    public static int getLastPathIndex(String attributePath) {
        int index = -1;

        int leftBracket = attributePath.lastIndexOf("[");
        int rightBracket = attributePath.lastIndexOf("]");

        if (leftBracket > 0 && rightBracket > leftBracket) {
            String indexString = attributePath.substring(leftBracket + 1, rightBracket);
            try {
                index = Integer.valueOf(indexString).intValue();
            } catch (NumberFormatException e) {
                // Will just return -1
            }
        }

        return index;
    }

    /**
     * compares the value provided by the user and the one specified by the {@code WhenConstraint}
     *
     * @param fieldValue - the value found in the field specified by a {@code CaseConstraint}'s {@code propertyName}
     * @param whenValue - the value specified by a {@code WhenConstraint}
     * @param dataType - the data type of the field which caseConstraint's propertyName refers to
     * @param operator - the relationship to check between the {@code fieldValue} and the {@code whenValue}
     * @param isCaseSensitive - whether string comparison will be carried out in a case sensitive fashion
     * @param dateTimeService - used to convert strings to dates
     * @return
     */
    public static boolean compareValues(Object fieldValue, Object whenValue, DataType dataType, String operator,
            boolean isCaseSensitive, DateTimeService dateTimeService) {

        boolean result = false;
        Integer compareResult = null;

        if (UifConstants.CaseConstraintOperators.HAS_VALUE.equalsIgnoreCase(operator)) {
            if (fieldValue == null) {
                return "false".equals(whenValue.toString().toLowerCase());
            }
            if (fieldValue instanceof String && ((String) fieldValue).isEmpty()) {
                return "false".equals(whenValue.toString().toLowerCase());
            }
            if (fieldValue instanceof Collection && ((Collection<?>) fieldValue).isEmpty()) {
                return "false".equals(whenValue.toString().toLowerCase());
            }
            return "true".equals(whenValue.toString().toLowerCase());
        }
        // Convert objects into appropriate data types
        if (null != dataType) {
            if (DataType.STRING.equals(dataType)) {
                String v1 = getString(fieldValue);
                String v2 = getString(whenValue);

                if (!isCaseSensitive) {
                    v1 = v1.toUpperCase();
                    v2 = v2.toUpperCase();
                }

                compareResult = v1.compareTo(v2);
            } else if (DataType.INTEGER.equals(dataType)) {
                Integer v1 = getInteger(fieldValue);
                Integer v2 = getInteger(whenValue);
                compareResult = v1.compareTo(v2);
            } else if (DataType.LONG.equals(dataType)) {
                Long v1 = getLong(fieldValue);
                Long v2 = getLong(whenValue);
                compareResult = v1.compareTo(v2);
            } else if (DataType.DOUBLE.equals(dataType)) {
                Double v1 = getDouble(fieldValue);
                Double v2 = getDouble(whenValue);
                compareResult = v1.compareTo(v2);
            } else if (DataType.FLOAT.equals(dataType)) {
                Float v1 = getFloat(fieldValue);
                Float v2 = getFloat(whenValue);
                compareResult = v1.compareTo(v2);
            } else if (DataType.BOOLEAN.equals(dataType)) {
                Boolean v1 = getBoolean(fieldValue);
                Boolean v2 = getBoolean(whenValue);
                compareResult = v1.compareTo(v2);
            } else if (DataType.DATE.equals(dataType)) {
                Date v1 = getDate(fieldValue, dateTimeService);
                Date v2 = getDate(whenValue, dateTimeService);
                compareResult = v1.compareTo(v2);
            }
        }

        if (null != compareResult) {
            if ((UifConstants.CaseConstraintOperators.EQUALS.equalsIgnoreCase(operator) || UifConstants
                    .CaseConstraintOperators.GREATER_THAN_EQUAL.equalsIgnoreCase(operator) || UifConstants
                    .CaseConstraintOperators.LESS_THAN_EQUAL.equalsIgnoreCase(operator)) && 0 == compareResult) {
                result = true;
            }

            if ((UifConstants.CaseConstraintOperators.NOT_EQUAL.equalsIgnoreCase(operator) || UifConstants
                    .CaseConstraintOperators.NOT_EQUALS.equalsIgnoreCase(operator) || UifConstants
                    .CaseConstraintOperators.GREATER_THAN.equalsIgnoreCase(operator)) && compareResult >= 1) {
                result = true;
            }

            if ((UifConstants.CaseConstraintOperators.NOT_EQUAL.equalsIgnoreCase(operator) || UifConstants
                    .CaseConstraintOperators.NOT_EQUALS.equalsIgnoreCase(operator) || UifConstants
                    .CaseConstraintOperators.LESS_THAN.equalsIgnoreCase(operator)) && compareResult <= -1) {
                result = true;
            }
        }

        return result;
    }

    /**
     * converts the provided object into an integer
     *
     * @param o - the object to convert
     * @return the integer value
     */
    public static Integer getInteger(Object o) {
        Integer result = null;
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        String s = o.toString();
        if (s != null && s.trim().length() > 0) {
            result = Integer.valueOf(s.trim());
        }
        return result;
    }

    /**
     * converts the provided object into a long
     *
     * @param o - the object to convert
     * @return the long value
     */
    public static Long getLong(Object o) {
        Long result = null;
        if (o instanceof Long) {
            return (Long) o;
        }
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        String s = o.toString();
        if (s != null && s.trim().length() > 0) {
            result = Long.valueOf(s.trim());
        }
        return result;
    }

    /**
     * converts the provided object into an float
     *
     * @param o - the object to convert
     * @return the float value
     */
    public static Float getFloat(Object o) {
        Float result = null;
        if (o instanceof Float) {
            return (Float) o;
        }
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }
        String s = o.toString();
        if (s != null && s.trim().length() > 0) {
            result = Float.valueOf(s.trim());
        }
        return result;
    }

    /**
     * converts the provided object into a double
     *
     * @param o - the object to convert
     * @return the double value
     */
    public static Double getDouble(Object o) {
        Double result = null;
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o).doubleValue();
        }
        if (o instanceof Double) {
            return (Double) o;
        }
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        String s = o.toString();
        if (s != null && s.trim().length() > 0) {
            result = Double.valueOf(s.trim());
        }
        return result;
    }

    /**
     * determines whether the provided object is a date and tries to converts non-date values
     *
     * @param object - the object to convert/cast into a date
     * @param dateTimeService - used to convert strings to dates
     * @return a date object
     * @throws IllegalArgumentException
     */
    public static Date getDate(Object object, DateTimeService dateTimeService) throws IllegalArgumentException {
        Date result = null;
        if (object instanceof Date) {
            return (Date) object;
        }
        if (object == null) {
            return null;
        }
        String s = object.toString();
        if (s != null && s.trim().length() > 0) {
            try {
                result = dateTimeService.convertToDate(s.trim());
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return result;
    }

    /**
     * converts the provided object into a string
     *
     * @param o - the object to convert
     * @return the string value
     */
    public static String getString(Object o) {
        if (o instanceof String) {
            return (String) o;
        }
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * converts the provided object into a boolean
     *
     * @param o - the object to convert
     * @return the boolean value
     */
    public static Boolean getBoolean(Object o) {
        Boolean result = null;
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        if (o == null) {
            return null;
        }
        String s = o.toString();
        if (s != null && s.trim().length() > 0) {
            result = Boolean.parseBoolean(s.trim());
        }
        return result;
    }

    /**
     * checks whether the string contains non-whitespace characters
     *
     * @param string
     * @return true if the string contains at least one none-whitespace character, false otherwise
     */
    public static boolean hasText(String string) {

        if (string == null || string.length() < 1) {
            return false;
        }
        int stringLength = string.length();

        for (int i = 0; i < stringLength; i++) {
            char currentChar = string.charAt(i);
            if (' ' != currentChar || '\t' != currentChar || '\n' != currentChar) {
                return true;
            }
        }

        return false;
    }

    /**
     * checks whether the provided object is null or empty
     *
     * @param value - the object to check
     * @return true if the object is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(Object value) {
        return value == null || (value instanceof String && StringUtils.isBlank(((String) value).trim()));
    }

    /**
     * defines possible result values of a comparison operation
     */
    public static enum Result {
        VALID, INVALID, UNDEFINED
    }

    ;

    /**
     * attempts to convert the provided value to the given dataType
     *
     * @param value - the object to convert
     * @param dataType - the data type to convert into
     * @param dateTimeService - used to convert strings to dates
     * @return the converted value if null or successful, otherwise throws an exception
     * @throws AttributeValidationException
     */
    public static Object convertToDataType(Object value, DataType dataType,
            DateTimeService dateTimeService) throws AttributeValidationException {
        Object returnValue = value;

        if (null == value) {
            return null;
        }

        switch (dataType) {
            case BOOLEAN:
                if (!(value instanceof Boolean)) {
                    returnValue = Boolean.valueOf(value.toString());

                    // Since the Boolean.valueOf is exceptionally loose - it basically takes any string and makes it false
                    if (!value.toString().equalsIgnoreCase("TRUE") && !value.toString().equalsIgnoreCase("FALSE")) {
                        throw new AttributeValidationException("Value " + value.toString() + " is not a boolean!");
                    }
                }
                break;
            case INTEGER:
                if (!(value instanceof Number)) {
                    returnValue = Integer.valueOf(value.toString());
                }
                break;
            case LONG:
                if (!(value instanceof Number)) {
                    returnValue = Long.valueOf(value.toString());
                }
                break;
            case DOUBLE:
                if (!(value instanceof Number)) {
                    returnValue = Double.valueOf(value.toString());
                }
                if (((Double) returnValue).isNaN()) {
                    throw new AttributeValidationException("Infinite Double values are not valid!");
                }
                if (((Double) returnValue).isInfinite()) {
                    throw new AttributeValidationException("Infinite Double values are not valid!");
                }
                break;
            case FLOAT:
                if (!(value instanceof Number)) {
                    returnValue = Float.valueOf(value.toString());
                }
                if (((Float) returnValue).isNaN()) {
                    throw new AttributeValidationException("NaN Float values are not valid!");
                }
                if (((Float) returnValue).isInfinite()) {
                    throw new AttributeValidationException("Infinite Float values are not valid!");
                }
                break;
            case TRUNCATED_DATE:
            case DATE:
                if (!(value instanceof Date)) {
                    try {
                        returnValue = dateTimeService.convertToDate(value.toString());
                    } catch (ParseException pe) {
                        throw new AttributeValidationException("Value " + value.toString() + " is not a date!");
                    }
                }
                break;
            case STRING:
        }

        return returnValue;
    }

    /**
     * checks whether the provided value is greater than the limit given
     *
     * @param value - the object to check
     * @param limit - the limit to use
     * @param <T>
     * @return one of the values in {@link  Result}
     */
    public static <T> Result isGreaterThan(T value, Comparable<T> limit) {
        return limit == null ? Result.UNDEFINED : (limit.compareTo(value) < 0 ? Result.VALID : Result.INVALID);
    }

    /**
     * checks whether the provided value is greater than or equal to the limit given
     *
     * @param value - the object to check
     * @param limit - the limit to use
     * @param <T>
     * @return one of the values in {@link  Result}
     */
    public static <T> Result isGreaterThanOrEqual(T value, Comparable<T> limit) {
        return limit == null ? Result.UNDEFINED : (limit.compareTo(value) <= 0 ? Result.VALID : Result.INVALID);
    }

    /**
     * checks whether the provided value is less than the limit given
     *
     * @param value - the object to check
     * @param limit - the limit to use
     * @param <T>
     * @return one of the values in {@link  Result}
     */
    public static <T> Result isLessThan(T value, Comparable<T> limit) {
        return limit == null ? Result.UNDEFINED : (limit.compareTo(value) > 0 ? Result.VALID : Result.INVALID);
    }

    /**
     * checks whether the provided value is greater than the limit given
     *
     * @param value - the object to check
     * @param limit - the limit to use
     * @param <T>
     * @return one of the values in {@link  Result}
     */
    public static <T> Result isLessThanOrEqual(T value, Comparable<T> limit) {
        return limit == null ? Result.UNDEFINED : (limit.compareTo(value) >= 0 ? Result.VALID : Result.INVALID);
    }

    /**
     * converts a path into an array of its path components
     *
     * @param fieldPath - a string representation of specifically which attribute (at some depth) is being accessed
     * @return the array of path components
     */
    public static String[] getPathTokens(String fieldPath) {
        return (fieldPath != null && fieldPath.contains(".") ? fieldPath.split("\\.") : new String[]{fieldPath});
    }

}

