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

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * This class is a wrapper around java.math.BigDecimal. It exposes the only the
 * needed functionality of BigDecimal and uses a standard ROUND_BEHAVIOR of
 * BigDecimal.ROUND_HALF_UP
 * 
 * Members of this class are, like BigDecimal, immutable; even methods which
 * might be expected to change the value actually just return a new instance
 * with the new value.
 */
public abstract class AbstractKualiDecimal<T extends AbstractKualiDecimal> extends Number implements Comparable {
	
	public static final int ROUND_BEHAVIOR = BigDecimal.ROUND_HALF_UP;

    public static final KualiDecimal ZERO = new KualiDecimal(BigDecimal.ZERO);
    
	protected BigDecimal value;

	public AbstractKualiDecimal() {
	}
	
	/**
	 * This is the base constructor, used by constructors that take other types
	 * 
	 * @param value
	 *            String containing numeric value - defaults to zero
	 */
	public AbstractKualiDecimal(String value, int scale) {
		if (StringUtils.isBlank(value)) {
			this.value = BigDecimal.ZERO.setScale(scale,ROUND_BEHAVIOR);
		} else {
			this.value = new BigDecimal(value).setScale(scale, ROUND_BEHAVIOR);
		}
	}

	public AbstractKualiDecimal(int value, int scale) {
	    this.value = new BigDecimal(value).setScale(scale, ROUND_BEHAVIOR);
	}

	public AbstractKualiDecimal(double value, int scale) {
		this.value = new BigDecimal(value).setScale(scale, ROUND_BEHAVIOR);
	}

	public AbstractKualiDecimal(BigDecimal value, int scale) {
		this(value.toPlainString(), scale);
	}

	/**
	 * @param operand
	 * @return true if this AbstractKualiDecimal is less than the given
	 *         AbstractKualiDecimal
	 */
	public boolean isLessThan(AbstractKualiDecimal operand) {
		if (operand == null) {
			throw new IllegalArgumentException("invalid (null) operand");
		}

		return (this.compareTo(operand) == -1);
	}

	/**
	 * @param operand
	 * @return true if this AbstractKualiDecimal is greater than the given
	 *         AbstractKualiDecimal
	 */
	public boolean isGreaterThan(AbstractKualiDecimal operand) {
		if (operand == null) {
			throw new IllegalArgumentException("invalid (null) operand");
		}

		return (this.compareTo(operand) == 1);
	}

	/**
	 * @param operand
	 * @return true if this AbstractKualiDecimal is less than or equal to the
	 *         given AbstractKualiDecimal
	 */
	public boolean isLessEqual(AbstractKualiDecimal operand) {
		if (operand == null) {
			throw new IllegalArgumentException("invalid (null) operand");
		}

		return !isGreaterThan(operand);
	}

	/**
	 * @param operand
	 * @return true if this AbstractKualiDecimal is greater than or equal to the
	 *         given AbstractKualiDecimal
	 */
	public boolean isGreaterEqual(AbstractKualiDecimal operand) {
		if (operand == null) {
			throw new IllegalArgumentException("invalid (null) operand");
		}

		return !isLessThan(operand);
	}

	/**
	 * @return true if the given String can be used to construct a valid
	 *         AbstractKualiDecimal
	 */
	public static boolean isNumeric(String s) {
		boolean isValid = false;

		if (!StringUtils.isBlank(s)) {
			try {
				new BigDecimal(s);
				isValid = true;
			} catch (NumberFormatException e) {
			}
		}

		return isValid;
	}

	// Number methods
	/**
	 * @see java.lang.Number#doubleValue()
	 */
	@Override
	public double doubleValue() {
		return this.value.doubleValue();
	}

	/**
	 * @see java.lang.Number#floatValue()
	 */
	@Override
	public float floatValue() {
		return this.value.floatValue();
	}

	/**
	 * @see java.lang.Number#intValue()
	 */
	@Override
	public int intValue() {
		return this.value.intValue();
	}

	/**
	 * @see java.lang.Number#longValue()
	 */
	@Override
	public long longValue() {
		return this.value.longValue();
	}

	/**
	 * @return the value of this instance as a BigDecimal.
	 */
	public BigDecimal bigDecimalValue() {
		return this.value;
	}

	// Comparable methods
	/**
	 * Compares this AbstractKualiDecimal with the specified Object. If the
	 * Object is a AbstractKualiDecimal, this method behaves like
	 * java.lang.Comparable#compareTo(java.lang.Object).
	 * 
	 * Otherwise, it throws a <tt>ClassCastException</tt> (as KualiDecimals
	 * are comparable only to other KualiDecimals).
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		return compareTo((AbstractKualiDecimal) o);
	}
	
	/**
	 * Returns the result of comparing the values of this AbstractKualiDecimal
	 * and the given AbstractKualiDecimal.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(AbstractKualiDecimal k) {
		return this.value.compareTo(k.value);
	}

	// Object methods
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;

		if (obj instanceof AbstractKualiDecimal) {
			AbstractKualiDecimal k = (AbstractKualiDecimal) obj;

			// using AbstractKualiDecimal.compareTo instead of BigDecimal.equals
			// since
			// BigDecimal.equals only returns true if the
			// scale and precision are equal, rather than comparing the actual
			// (scaled) values
			equals = (this.compareTo(k) == 0);
		}

		return equals;
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.value.toString();
	}

	/**
	 * @return true if this T is less than zero
	 */
	public boolean isNegative() {
		return (this.compareTo(ZERO) == -1);
	}

	/**
	 * @return true if this T is greater than zero
	 */
	public boolean isPositive() {
		return (this.compareTo(ZERO) == 1);
	}

	/**
	 * @return true if this T is equal to zero
	 */
	public boolean isZero() {
		return (this.compareTo(ZERO) == 0);
	}

	/**
	 * @return a T with the same scale and the absolute value
	 */
	public T abs() {
		T absolute = null;

		if (isNegative()) {
			absolute = negated();
		} else {
			absolute = newInstance(this.value, this.value.scale());
		}

		return absolute;
	}

	/**
	 * @return true if this T is not equal to zero
	 */
	public boolean isNonZero() {
		return !this.isZero();
	}

	/**
	 * Wraps BigDecimal's add method to accept and return T instances instead of
	 * BigDecimals, so that users of the class don't have to typecast the return
	 * value.
	 * 
	 * @param addend
	 * @return result of adding the given addend to this value
	 * @throws IllegalArgumentException
	 *             if the given addend is null
	 */
	public T add(T addend) {
		if (addend == null) {
			throw new IllegalArgumentException("invalid (null) addend");
		}

		BigDecimal sum = this.value.add(addend.value);
		return newInstance(sum, sum.scale());
	}

	/**
	 * Wraps BigDecimal's subtract method to accept and return T instances
	 * instead of BigDecimals, so that users of the class don't have to typecast
	 * the return value.
	 * 
	 * @param subtrahend
	 * @return result of the subtracting the given subtrahend from this value
	 * @throws IllegalArgumentException
	 *             if the given subtrahend is null
	 */
	public T subtract(T subtrahend) {
		if (subtrahend == null) {
			throw new IllegalArgumentException("invalid (null) subtrahend");
		}

		BigDecimal difference = this.value.subtract(subtrahend.value);
		return newInstance(difference, difference.scale());
	}

	/**
	 * Wraps BigDecimal's multiply method to accept and return T instances
	 * instead of BigDecimals, so that users of the class don't have to typecast
	 * the return value.
	 * 
	 * @param multiplicand
	 * @return result of multiplying this value by the given multiplier
	 * @throws IllegalArgumentException
	 *             if the given multiplier is null
	 */
	public T multiply(T multiplier) {
	    return multiply(multiplier, true);
	}

    /**
     * Overloaded multiply method where we can specify if we need to preserve the precision of the result
     * 
     * @param multiplicand
     * @param applyScale
     * @return result of multiplying this value by the given multiplier
     * @throws IllegalArgumentException
     *             if the given multiplier is null
     */
   public T multiply(T multiplier, boolean applyScale) {
        if (multiplier == null) {
            throw new IllegalArgumentException("invalid (null) multiplier");
        }

        BigDecimal product = this.value.multiply(multiplier.value);  
        return newInstance(product, applyScale ? this.value.scale() : product.scale());
    }

	/**
	 * This method calculates the mod between to T values by first casting to
	 * doubles and then by performing the % operation on the two primitives.
	 * 
	 * @param modulus
	 *            The other value to apply the mod to.
	 * @return result of performing the mod calculation
	 * @throws IllegalArgumentException
	 *             if the given modulus is null
	 */
	public T mod(T modulus) {
	    return mod(modulus, true);
	}

    /**
     * Overloaded mod method where we can specify if we want to preserve the result's precision
     * 
     * @param modulus
     *            The other value to apply the mod to.
     * @param applyScale
     * @return result of performing the mod calculation
     * @throws IllegalArgumentException
     *             if the given modulus is null
     */
  
    public T mod(T modulus, boolean applyScale) {
        if (modulus == null) {
            throw new IllegalArgumentException("invalid (null) modulus");
        }
        double difference = this.value.doubleValue() % modulus.doubleValue();

        int scaleToApply = applyScale ? this.value.scale() : new BigDecimal(difference).scale();
        
        //return (T) newInstance(new BigDecimal(difference).setScale(scaleToApply, BigDecimal.ROUND_UNNECESSARY), scaleToApply);
        return (T) newInstance(new BigDecimal(difference), scaleToApply);
    }


	/**
	 * Wraps BigDecimal's divide method to enforce the default Kuali rounding
	 * behavior
	 * 
	 * @param divisor
	 * @return result of dividing this value by the given divisor
	 * @throws an
	 *             IllegalArgumentException if the given divisor is null
	 */
	public T divide(T divisor) {
		return divide(divisor, true);
	}

	/**
	 * @return a T with the same scale and a negated value (iff the value is
	 *         non-zero)
	 */
	public T negated() {
		return multiply(newInstance("-1"));
	}

	public T divide(T divisor, boolean applyScale) {
		if (divisor == null) {
			throw new IllegalArgumentException("invalid (null) divisor");
		}
		BigDecimal quotient = this.value.divide(divisor.value, ROUND_BEHAVIOR);

		T result = newInstance(quotient, applyScale ? this.value.scale() : quotient.scale());
		return result;
	}

	protected abstract T newInstance(String value);

	protected abstract T newInstance(double value);
	
	protected abstract T newInstance(double value, int scale);

	protected abstract T newInstance(BigDecimal value);

	protected abstract T newInstance(BigDecimal value, int scale);
}
