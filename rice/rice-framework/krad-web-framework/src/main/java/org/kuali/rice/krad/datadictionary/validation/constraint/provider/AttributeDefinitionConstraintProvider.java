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

import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.CaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.DataTypeConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.LengthConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.MustOccurConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.SimpleConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.CaseConstraintResolver;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.ConstraintResolver;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.DefinitionConstraintResolver;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.MustOccurConstraintsResolver;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.PrerequisiteConstraintsResolver;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.SimpleConstraintResolver;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.ValidCharactersConstraintResolver;
import org.kuali.rice.krad.uif.field.InputField;

import java.util.HashMap;

/**
 * AttributeDefinitionConstraintProvider looks up constraints for attribute definitions by constraint type
 *
 * <p> This can either by instantiated by dependency
 * injection, in which case a map of class names to constraint resolvers can be injected, or the default map can be
 * constructed by
 * calling the init() method immediately after instantiation.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AttributeDefinitionConstraintProvider extends BaseConstraintProvider<AttributeDefinition> {

    @Override
    public void init() {
        resolverMap = new HashMap<String, ConstraintResolver<AttributeDefinition>>();
        resolverMap.put(SimpleConstraint.class.getName(), new SimpleConstraintResolver<AttributeDefinition>());
        resolverMap.put(CaseConstraint.class.getName(), new CaseConstraintResolver<AttributeDefinition>());
        resolverMap.put(DataTypeConstraint.class.getName(), new DefinitionConstraintResolver<AttributeDefinition>());
        resolverMap.put(LengthConstraint.class.getName(), new DefinitionConstraintResolver<AttributeDefinition>());
        resolverMap.put(ValidCharactersConstraint.class.getName(),
                new ValidCharactersConstraintResolver<AttributeDefinition>());
        resolverMap.put(PrerequisiteConstraint.class.getName(),
                new PrerequisiteConstraintsResolver<AttributeDefinition>());
        resolverMap.put(MustOccurConstraint.class.getName(), new MustOccurConstraintsResolver<AttributeDefinition>());
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.provider.ConstraintProvider#isSupported(org.kuali.rice.krad.datadictionary.validation.capability.Constrainable)
     */
    @Override
    public boolean isSupported(Constrainable definition) {

        if (definition instanceof AttributeDefinition || definition instanceof InputField) {
            return true;
        }

        return false;
    }

}
