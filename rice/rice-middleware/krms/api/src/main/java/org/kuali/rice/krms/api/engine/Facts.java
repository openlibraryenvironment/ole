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
package org.kuali.rice.krms.api.engine;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.mo.ModelObjectComplete;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Parameter object for the {@link org.kuali.rice.krms.api.engine.Engine} used to pass in mappings from Term to value
 * (aka facts).  In rule parlance, a fact is a concrete value of a term.  Intuitively this relationship is one of
 * definition and instance, similar to a parameter definition (e.g. int count) for a function (or method) in a
 * programming language and a parameter value (e.g. 5).</p>
 *
 * <p>{@link Facts} is immutable, and has a private constructor.  Use the inner {@link Builder} class to construct.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class Facts implements ModelObjectComplete, Serializable {

    private static final long serialVersionUID = -1448089944850300846L;

    /**
     * empty facts object
     */
    public static final Facts EMPTY_FACTS = new Facts(Builder.create());

    private Map<Term, Object> factMap;

    private Facts() {
        // private no-args constructor forces use of builder
    }

    private Facts(Builder b) {
        // copy the map to avoid surprises
        this.factMap = new HashMap<Term, Object>(b.factMap);
    }

    /**
     * @return the Map of Terms to fact values.  May be empty, will never be null.  The returned map is unmodifiable.
     */
    public Map<Term, Object> getFactMap() {
        return Collections.unmodifiableMap(factMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Facts facts = (Facts) o;

        if (factMap != null ? !factMap.equals(facts.factMap) : facts.factMap != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return factMap != null ? factMap.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Facts{" + "factMap=" + factMap + '}';
    }

    /**
     *    Builder for a {@link Facts} parameter object
     */
    public static class Builder {
        private Map<Term, Object> factMap = new HashMap<Term, Object>();

        private Builder() {
            // private constructor forces use of static factory
        }

        /**
         * Static factory method to produce instances of this {@link Builder} class
         * @return
         */
        public static Builder create() {
            return new Builder();
        }

        /**
         * Add a fact mapping from the name and parameter map combination to the fact value
         * @param termName the name of the term.  Must not be empty or null.
         * @param termParameters any parameters for the term.  May be null or empty.
         * @param factValue the concrete value for the term
         */
        public Builder addFact(String termName, Map<String, String> termParameters, Object factValue) {
            if (StringUtils.isEmpty(termName)) {
                throw new IllegalArgumentException("termName must not be null or empty");
            }
            factMap.put(new Term(termName, termParameters), factValue);
            return this;
        }

        /**
         * Add a fact mapping from the term name to the fact value
         * @param termName the name of the term.  Must not be empty or null.
         * @param factValue the concrete value for the term
         */
        public Builder addFact(String termName, Object factValue) {
            addFact(termName, null, factValue);
            return this;
        }

        /**
         * Add a fact mapping from the {@link Term} to the fact value
         * @param term the term that this fact is a value for.  Must not be null.
         * @param factValue the fact value
         */
        public Builder addFact(Term term, Object factValue) {
            if (term == null) {
                throw new IllegalArgumentException("term must not be null");
            }
            factMap.put(term, factValue);
            return this;
        }

        /**
         * Add facts in bulk to this Facts parameter object
         * @param facts the map of Terms to fact values.  May be null, in that case this call is a no op.
         */
        public Builder addFactsByTerm(Map<Term, Object> facts) {
            if (facts != null) {
                factMap.putAll(facts);
            }
            return this;
        }

        /**
         * Add facts in bulk to this Facts parameter object
         * @param facts the map of term names to fact values.  May be null, in that case this call is a no op.
         */
        public Builder addFactsByName(Map<String, Object> facts) {
            if (facts != null) {
                for (Map.Entry<String, Object> entry : facts.entrySet()) {
                    factMap.put(new Term(entry.getKey()), entry.getValue());
                }
            }
            return this;
        }

        /**
         * return a {@link Facts} parameter object spawned from this {@link Builder}
         */
        public Facts build() {
            if (factMap.isEmpty()) {
                return EMPTY_FACTS;
            } else {
                return new Facts(this);
            }
        }

    }


}
