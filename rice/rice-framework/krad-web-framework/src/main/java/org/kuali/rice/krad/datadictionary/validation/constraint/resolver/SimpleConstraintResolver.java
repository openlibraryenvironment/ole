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
package org.kuali.rice.krad.datadictionary.validation.constraint.resolver;

import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.SimpleConstrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleConstraintResolver<T extends Constrainable> implements ConstraintResolver<T> {

    /**
     * Return SimpleConstraint if SimpleConstrainable, otherwise return an empty list.
     *
     * @param definition Definition to extract a SimpleConstraint from
     * @param <C> SimpleConstraint
     * @return SimpleConstraint if SimpleConstrainable, otherwise return an empty list.
     */
    public <C extends Constraint> List<C> resolve(T definition) {
        if (definition instanceof SimpleConstrainable) {
            C simpleConstraint = (C) (((SimpleConstrainable) definition).getSimpleConstraint());
            return Collections.singletonList(simpleConstraint);
        } else {
            return new ArrayList<C>();
        }
    }
}
