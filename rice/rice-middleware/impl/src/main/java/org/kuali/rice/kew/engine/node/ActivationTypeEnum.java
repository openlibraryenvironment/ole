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
package org.kuali.rice.kew.engine.node;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.kew.api.KewApiConstants;

/**
 * Activation Type enum type which defines the two types of activation within the engine.
 * Sequential activation means that only a single request on a node will get activated at a time.
 * Parallel activation means that all requests on a node will be activated.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class ActivationTypeEnum implements Coded {
    /** Routing should process the associated ActionRequests in sequence */
    public static final ActivationTypeEnum SEQUENTIAL = new ActivationTypeEnum(KewApiConstants.ROUTE_LEVEL_SEQUENCE, KewApiConstants.ROUTE_LEVEL_SEQUENTIAL_NAME, KewApiConstants.ROUTE_LEVEL_SEQUENCE_LABEL);
    /** Routing should process the associated ActionRequests in parallel */
    public static final ActivationTypeEnum PARALLEL = new ActivationTypeEnum(KewApiConstants.ROUTE_LEVEL_PARALLEL, KewApiConstants.ROUTE_LEVEL_PARALLEL_NAME, KewApiConstants.ROUTE_LEVEL_PARALLEL_LABEL);
    /** Routing should process the associated ActionRequests in parallel according to priority */
    public static final ActivationTypeEnum PRIORITY_PARALLEL = new ActivationTypeEnum(KewApiConstants.ROUTE_LEVEL_PRIORITY_PARALLEL, KewApiConstants.ROUTE_LEVEL_PRIORITY_PARALLEL_NAME, KewApiConstants.ROUTE_LEVEL_PRIORITY_PARALLEL_LABEL);

    private final String code;
    private final String name;
    private final String label;

    private ActivationTypeEnum(String code, String name, String label) {
        this.code = code;
        this.name = name;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return "[ActivationTypeEnum: code=" + code + ", name=" + name + ", label=" + label + "]";
    }

    /**
     * Parses the code verbatim and returns the enum type that matches that code exactly
     * @param code the activation type code
     * @return the enum type
     * @throws IllegalArgumentException if code is null, or invalid
     */
    public static ActivationTypeEnum lookupCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Activation type code must be non-null");
        }
        if (SEQUENTIAL.code.equals(code)) {
            return SEQUENTIAL;
        } else if (PARALLEL.code.equals(code)) {
            return PARALLEL;
        } else if (PRIORITY_PARALLEL.code.equals(code)) {
            return PRIORITY_PARALLEL;
        } else {
            throw new IllegalArgumentException("Invalid activation code: '" + code + "'");
        }
    }

    /**
     * Parses the string and returns the enum type whose code, name, or label
     * matches the string regardless of case
     * @param string the activation type string
     * @return the enum type
     * @throws IllegalArgumentException if string is null, or invalid
     */
    public static ActivationTypeEnum parse(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Activation type string must be non-null");
        }
        if (SEQUENTIAL.code.equalsIgnoreCase(string) ||
            SEQUENTIAL.name.equalsIgnoreCase(string) ||
            SEQUENTIAL.label.equalsIgnoreCase(string)) {
            return SEQUENTIAL;
        } else if (PARALLEL.code.equalsIgnoreCase(string) ||
            PARALLEL.name.equalsIgnoreCase(string) ||
            PARALLEL.label.equalsIgnoreCase(string)) {
            return PARALLEL;
        } else if (PRIORITY_PARALLEL.code.equalsIgnoreCase(string) ||
            PRIORITY_PARALLEL.name.equalsIgnoreCase(string) ||
            PRIORITY_PARALLEL.label.equalsIgnoreCase(string)) {
            return PRIORITY_PARALLEL;
        } else {
            throw new IllegalArgumentException("Invalid activation type: '" + string + "'");
        }
    }
}
