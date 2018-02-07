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
package org.kuali.rice.core.api.util.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;


public class KualiInteger extends Number implements Comparable {
    public static final int ROUND_BEHAVIOR = KualiDecimal.ROUND_BEHAVIOR;
    public static final int SCALE = 0;

    public static KualiInteger ZERO = new KualiInteger(0);

    private final BigInteger value;

    /**
     * Constructor - only accepts a string representation of the value.
     * 
     * This is done to prevent unexpected inaccuracy by conversion to and from floating-point values.
     * 
     * @param value String containing numeric value
     * @throws IllegalArgumentException if the given String is null
     */
    public KualiInteger(String value) {
        if (value == null) {
            throw new IllegalArgumentException("invalid (null) String in KualiInteger constructor");
        }

        this.value = new BigInteger(value);
    }

    /**
     * Initializes this instance to the given integer value with integer arithmetic.
     */
    public KualiInteger(long value) {
        this.value = BigInteger.valueOf(value);
    }

    /**
     * Simple constructor, copies in the given BigInteger as the value for the instance.
     * 
     * @param value BigInteger to be used as basis for value
     * @throws IllegalArgumentException if the given BigDecimal is null
     */
    public KualiInteger(BigInteger value) {
        if (value == null) {
            throw new IllegalArgumentException("invalid (null) BigDecimal in KualiDecimal constructor");
        }

        this.value = value;
    }

    /**
     * Simple constructor, copies in the given BigInteger as the value for the instance.
     * 
     * @param value BigInteger to be used as basis for value
     * @throws IllegalArgumentException if the given BigDecimal is null
     */
    public KualiInteger(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("invalid (null) BigDecimal in KualiDecimal constructor");
        }

        this.value = value.setScale(SCALE, ROUND_BEHAVIOR).toBigInteger();
    }

    /**
     * Simple constructor, rounds the given KualiDecimal according to the RoundingMode
     * 
     * @param value KualiDecmial to be used as basis for value
     * @param roundingMode RoundingMode for converting to Integer
     * @throws IllegalArgumentException if the given KualiDecmial or RoundingMode is null
     */
    public KualiInteger(KualiDecimal value, RoundingMode roundingMode) {
        if (value == null) {
            throw new IllegalArgumentException("invalid (null) KualiDecimal in KualiInteger constructor");
        }
        if (roundingMode == null) {
            throw new IllegalArgumentException("invalid (null) RoundingMode in KualiInteger constructor");
        }

        this.value = value.bigDecimalValue().round(new MathContext(0, roundingMode)).toBigInteger();
    }

    /**
     * Wraps BigDecimal's add method to accept and return KualiDecimal instances instead of BigDecimals, so that users of the class
     * don't have to typecast the return value.
     * 
     * @param addend
     * @return result of adding the given addend to this value
     * @throws IllegalArgumentException if the given addend is null
     */
    public KualiInteger add(KualiInteger addend) {
        if (addend == null) {
            throw new IllegalArgumentException("invalid (null) addend");
        }

        BigInteger sum = this.value.add(addend.value);
        return new KualiInteger(sum);
    }

    /**
     * Wraps BigDecimal's subtract method to accept and return KualiDecimal instances instead of BigDecimals, so that users of the
     * class don't have to typecast the return value.
     * 
     * @param subtrahend
     * @return result of the subtracting the given subtrahend from this value
     * @throws IllegalArgumentException if the given subtrahend is null
     */
    public KualiInteger subtract(KualiInteger subtrahend) {
        if (subtrahend == null) {
            throw new IllegalArgumentException("invalid (null) subtrahend");
        }

        BigInteger difference = this.value.subtract(subtrahend.value);
        return new KualiInteger(difference);
    }

    /**
     * Wraps BigDecimal's multiply method to accept and return KualiInteger instances instead of BigDecimals, so that users of the
     * class don't have to typecast the return value.
     * 
     * @param multiplicand
     * @return result of multiplying this value by the given multiplier
     * @throws IllegalArgumentException if the given multiplier is null
     */
    public KualiInteger multiply(KualiInteger multiplier) {
        if (multiplier == null) {
            throw new IllegalArgumentException("invalid (null) multiplier");
        }

        BigInteger product = this.value.multiply(multiplier.value);
        return new KualiInteger(product);
    }

    public KualiInteger multiply(BigDecimal multiplier) {
        if (multiplier == null) {
            throw new IllegalArgumentException("invalid (null) multiplier");
        }

        BigDecimal product = multiplier.multiply(new BigDecimal(this.value));
        return new KualiInteger(product);
    }

    public KualiInteger multiply(KualiDecimal multiplier) {
        return multiply(multiplier.bigDecimalValue());
    }


    public BigDecimal divide(BigDecimal dividend) {
        if (dividend == null) {
            throw new IllegalArgumentException("invalid (null) dividend");
        }

        return this.bigDecimalValue().divide(dividend, 8, ROUND_BEHAVIOR);
    }

    public BigDecimal divide(KualiInteger dividend) {
        if (dividend == null) {
            throw new IllegalArgumentException("invalid (null) dividend");
        }

        return divide(dividend.bigDecimalValue());
    }

    // Number methods
    /**
     * @see java.lang.Number#doubleValue()
     */
    public double doubleValue() {
        return this.value.doubleValue();
    }

    /**
     * @see java.lang.Number#floatValue()
     */
    public float floatValue() {
        return this.value.floatValue();
    }

    /**
     * @see java.lang.Number#intValue()
     */
    public int intValue() {
        return this.value.intValue();
    }

    /**
     * @see java.lang.Number#longValue()
     */
    public long longValue() {
        return this.value.longValue();
    }

    /**
     * @return the value of this instance as a BigDecimal.
     */
    public BigInteger bigIntegerValue() {
        return this.value;
    }

    /**
     * @return the value of this instance as a BigDecimal.
     */
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(this.value);
    }

    /**
     * @return the value of this instance as a BigDecimal.
     */
    public KualiDecimal kualiDecimalValue() {
        return new KualiDecimal(this.bigDecimalValue());
    }


    /**
     * @param operand
     * @return true if this KualiDecimal is less than the given KualiDecimal
     */
    public boolean isLessThan(KualiInteger operand) {
        if (operand == null) {
            throw new IllegalArgumentException("invalid (null) operand");
        }

        return (this.compareTo(operand) == -1);
    }

    /**
     * @param operand
     * @return true if this KualiDecimal is greater than the given KualiDecimal
     */
    public boolean isGreaterThan(KualiInteger operand) {
        if (operand == null) {
            throw new IllegalArgumentException("invalid (null) operand");
        }

        return (this.compareTo(operand) == 1);
    }

    /**
     * @param operand
     * @return true if this KualiDecimal is less than or equal to the given KualiDecimal
     */
    public boolean isLessEqual(KualiInteger operand) {
        if (operand == null) {
            throw new IllegalArgumentException("invalid (null) operand");
        }

        return !isGreaterThan(operand);
    }

    /**
     * @param operand
     * @return true if this KualiDecimal is greater than or equal to the given KualiDecimal
     */
    public boolean isGreaterEqual(KualiInteger operand) {
        if (operand == null) {
            throw new IllegalArgumentException("invalid (null) operand");
        }

        return !isLessThan(operand);
    }

    /**
     * @return true if this KualiDecimal is less than zero
     */
    public boolean isNegative() {
        return (this.compareTo(ZERO) == -1);
    }

    /**
     * @return true if this KualiDecimal is greater than zero
     */
    public boolean isPositive() {
        return (this.compareTo(ZERO) == 1);
    }


    /**
     * @return true if this KualiDecimal is equal to zero
     */
    public boolean isZero() {
        return (this.compareTo(ZERO) == 0);
    }


    /**
     * @return true if this KualiDecimal is not equal to zero
     */
    public boolean isNonZero() {
        return !this.isZero();
    }

    /**
     * @return a KualiInteger with the same scale and a negated value (iff the value is non-zero)
     */
    public KualiInteger negated() {
        return multiply(new KualiInteger("-1"));
    }


    // Comparable methods
    /**
     * Compares this KualiInteger with the specified Object. If the Object is a KualiInteger, this method behaves like
     * java.lang.Comparable#compareTo(java.lang.Object).
     * 
     * Otherwise, it throws a <tt>ClassCastException</tt> (as KualiIntegers are comparable only to other KualiIntegers).
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        return compareTo((KualiInteger) o);
    }

    /**
     * Returns the result of comparing the values of this KualiInteger and the given KualiInteger.
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(KualiInteger k) {
        return this.value.compareTo(k.value);
    }


    // Object methods
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        boolean equals = false;

        if (obj instanceof KualiInteger) {
            KualiInteger k = (KualiInteger) obj;

            // using KualiInteger.compareTo instead of BigDecimal.equals since BigDecimal.equals only returns true if the
            // scale and precision are equal, rather than comparing the actual (scaled) values
            equals = (this.compareTo(k) == 0);
        }

        return equals;
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.value.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.value.toString();
    }

}
