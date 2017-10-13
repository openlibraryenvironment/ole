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
package org.kuali.rice.krad.datadictionary.validation.constraint.provider;

import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.ConstraintResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseConstraintProvider implements a simple in memory storage map of constraint resolvers
 *
 * <p>This provides a convenient base class
 * from which other constraint providers can be derived.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
public abstract class BaseConstraintProvider<T extends Constrainable> implements ConstraintProvider<T> {

    protected Map<String, ConstraintResolver<T>> resolverMap;

    /**
     * initializes the constraints
     *
     * <p>By doing initialization here, and not in a constructor, constraints are only placed in memory when they are
     * utilized.</p>
     */
    public void init() {
        if (resolverMap == null) {
            resolverMap = new HashMap<String, ConstraintResolver<T>>();
        }

    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.provider.ConstraintProvider#getConstraints(org.kuali.rice.krad.datadictionary.validation.capability.Constrainable,
     *      java.lang.Class)
     */
    @Override
    public List<Constraint> getConstraints(T definition, Class<? extends Constraint> constraintType) {
        if (resolverMap == null) {
            init();
        }

        ConstraintResolver<T> resolver = resolverMap.get(constraintType.getName());

        if (resolver == null) {
            return null;
        }

        return resolver.resolve(definition);
    }

    /**
     * @return the resolverMap
     */
    public Map<String, ConstraintResolver<T>> getResolverMap() {
        return this.resolverMap;
    }

    /**
     * @param resolverMap the resolverMap to set
     */
    public void setResolverMap(Map<String, ConstraintResolver<T>> resolverMap) {
        this.resolverMap = resolverMap;
    }

}
