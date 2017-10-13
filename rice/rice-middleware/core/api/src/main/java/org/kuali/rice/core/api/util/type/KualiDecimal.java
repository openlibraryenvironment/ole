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


/**
 * This class is a wrapper around java.math.BigDecimal. It exposes the only the
 * needed functionality of BigDecimal and uses a standard SCALE of 2 and
 * ROUND_BEHAVIOR of BigDecimal.ROUND_HALF_UP
 * 
 * Members of this class are, like BigDecimal, immutable; even methods which
 * might be expected to change the value (like setScale, for example) actually
 * just return a new instance with the new value.
 */
public class KualiDecimal extends AbstractKualiDecimal<KualiDecimal> {

    private static final long serialVersionUID = 899528391396696786L;

    public static final KualiDecimal ZERO = new KualiDecimal(BigDecimal.ZERO);
    
    public static final int SCALE = 2;
    
    /**
     * No-arg constructor for serialization purposes
     */
    public KualiDecimal() {
    }
    
	/**
	 * This is the base constructor, used by constructors that take other types
	 * 
	 * @param value
	 *            String containing numeric value
	 * @throws IllegalArgumentException
	 *             if the given String is null
	 */
	public KualiDecimal(String value) {
		super(value, SCALE);
	}

	public KualiDecimal(int value) {
		super(value, SCALE);
	}

	public KualiDecimal(double value) {
		super(value, SCALE);
	}

	public KualiDecimal(BigDecimal value) {
		super(value, SCALE);
	}
	
	public KualiDecimal setScale() {
		return new KualiDecimal(value, SCALE);
	}

	protected KualiDecimal(String value, int scale) {
		super(value, scale);
	}

	protected KualiDecimal(int value, int scale) {
		super(value, scale);
	}

	protected KualiDecimal(double value, int scale) {
		super(value, scale);
	}

	protected KualiDecimal(BigDecimal value, int scale) {
		super(value, scale);
	}

	@Override
	protected KualiDecimal newInstance(String value) {
		return new KualiDecimal(value);
	}

	@Override
	protected KualiDecimal newInstance(double value) {
		return new KualiDecimal(value);
	}

    @Override
    protected KualiDecimal newInstance(double value, int scale) {
        return new KualiDecimal(value, scale);
    }  

	@Override
	protected KualiDecimal newInstance(BigDecimal value) {
		return new KualiDecimal(value);
	}

	@Override
	protected KualiDecimal newInstance(BigDecimal value, int scale) {
		return new KualiDecimal(value, scale);
	}

}
