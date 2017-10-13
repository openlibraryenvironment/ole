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

import org.kuali.rice.krad.datadictionary.DataObjectEntry;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.MustOccurConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.ConstraintResolver;
import org.kuali.rice.krad.datadictionary.validation.constraint.resolver.MustOccurConstraintsResolver;

import java.util.HashMap;

/**
 * An object that looks up constraints for an object dictionary entry by constraint type. This can either by
 * instantiated by dependency
 * injection, in which case a map of class names to constraint resolvers can be injected, or the default map can be
 * constructed by
 * calling the init() method immediately after instantiation.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataObjectEntryConstraintProvider extends BaseConstraintProvider<DataObjectEntry> {

    @Override
    public void init() {
        resolverMap = new HashMap<String, ConstraintResolver<DataObjectEntry>>();
        resolverMap.put(MustOccurConstraint.class.getName(), new MustOccurConstraintsResolver<DataObjectEntry>());
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.provider.ConstraintProvider#isSupported(org.kuali.rice.krad.datadictionary.validation.capability.Constrainable)
     */
    @Override
    public boolean isSupported(Constrainable definition) {

        if (definition instanceof DataObjectEntry) {
            return true;
        }

        return false;
    }

}
