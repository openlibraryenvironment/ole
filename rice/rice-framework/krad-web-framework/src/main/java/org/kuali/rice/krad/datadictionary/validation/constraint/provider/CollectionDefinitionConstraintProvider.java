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

import org.kuali.rice.krad.datadictionary.CollectionDefinition;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.CollectionSizeConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ExistenceConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.ConstraintResolver;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.DefinitionConstraintResolver;

import java.util.HashMap;

/**
 * An object that looks up constraints for attribute definitions by constraint type. This can either by instantiated by
 * dependency
 * injection, in which case a map of class names to constraint resolvers can be injected, or the default map can be
 * constructed by
 * calling the init() method immediately after instantiation.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CollectionDefinitionConstraintProvider extends BaseConstraintProvider<CollectionDefinition> {

    @Override
    public void init() {
        resolverMap = new HashMap<String, ConstraintResolver<CollectionDefinition>>();
        resolverMap.put(ExistenceConstraint.class.getName(), new DefinitionConstraintResolver<CollectionDefinition>());
        resolverMap.put(CollectionSizeConstraint.class.getName(),
                new DefinitionConstraintResolver<CollectionDefinition>());
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.provider.ConstraintProvider#isSupported(org.kuali.rice.krad.datadictionary.validation.capability.Constrainable)
     */
    @Override
    public boolean isSupported(Constrainable definition) {

        if (definition instanceof CollectionDefinition) {
            return true;
        }

        return false;
    }

}
