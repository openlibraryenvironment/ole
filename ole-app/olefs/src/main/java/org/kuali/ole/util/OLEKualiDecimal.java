package org.kuali.ole.util;

import org.kuali.rice.core.api.util.type.AbstractKualiDecimal;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;

/**
 * Created by sureshss on 4/1/17.
 */

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


/**
 * This class is a wrapper around java.math.BigDecimal. It exposes the only the
 * needed functionality of BigDecimal and uses a standard SCALE of 2 and
 * ROUND_BEHAVIOR of BigDecimal.ROUND_HALF_UP
 *
 * Members of this class are, like BigDecimal, immutable; even methods which
 * might be expected to change the value (like setScale, for example) actually
 * just return a new instance with the new value.
 */
public class OLEKualiDecimal extends KualiDecimal {

    private static final long serialVersionUID = 899528391396696786L;

    public static final OLEKualiDecimal ZERO = new OLEKualiDecimal(BigDecimal.ZERO);

    public static final int SCALE = 6;

    /**
     * No-arg constructor for serialization purposes
     */
    public OLEKualiDecimal() {
    }

    /**
     * This is the base constructor, used by constructors that take other types
     *
     * @param value
     *            String containing numeric value
     * @throws IllegalArgumentException
     *             if the given String is null
     */
    public OLEKualiDecimal(String value) {
        super(value, SCALE);
    }

    public OLEKualiDecimal(int value) {
        super(value, SCALE);
    }

    public OLEKualiDecimal(double value) {
        super(value, SCALE);
    }

    public OLEKualiDecimal(BigDecimal value) {
        super(value, SCALE);
    }

    public OLEKualiDecimal setScale() {
        return new OLEKualiDecimal(value, SCALE);
    }

    protected OLEKualiDecimal(String value, int scale) {
        super(value, scale);
    }

    protected OLEKualiDecimal(int value, int scale) {
        super(value, scale);
    }

    protected OLEKualiDecimal(double value, int scale) {
        super(value, scale);
    }

    protected OLEKualiDecimal(BigDecimal value, int scale) {
        super(value, scale);
    }


    @Override
    protected OLEKualiDecimal newInstance(String value) {
        return new OLEKualiDecimal(value);
    }

    @Override
    protected OLEKualiDecimal newInstance(double value) {
        return new OLEKualiDecimal(value);
    }

    @Override
    protected OLEKualiDecimal newInstance(double value, int scale) {
        return new OLEKualiDecimal(value, scale);
    }

    @Override
    protected OLEKualiDecimal newInstance(BigDecimal value) {
        return new OLEKualiDecimal(value);
    }

    @Override
    protected OLEKualiDecimal newInstance(BigDecimal value, int scale) {
        return new OLEKualiDecimal(value, scale);
    }




}
