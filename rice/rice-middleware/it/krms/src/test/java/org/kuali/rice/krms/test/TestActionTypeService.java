/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krms.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.type.ActionTypeService;

import javax.jws.WebParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Used to help test agendas
 * @author gilesp
 *
 */
public class TestActionTypeService implements ActionTypeService{

	private static final Set<String> actionsFired = new HashSet<String>();
	
	public static void resetActionsFired() {
		actionsFired.clear();
	}
	
	public static boolean actionFired(String name) {
		return actionsFired.contains(name);
	}
	
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        return new TestAction(actionDefinition.getName());
    }

    @Override
    public List<RemotableAttributeField> getAttributeFields(String id) {
        return Collections.emptyList();
    }

    @Override
    public List<RemotableAttributeError> validateAttributes(
            String id,
            Map<String, String> attributes
    ) throws RiceIllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RemotableAttributeError> validateAttributesAgainstExisting(
            String id,
            Map<String, String> newAttributes,
            Map<String, String> oldAttributes
    ) throws RiceIllegalArgumentException {
        throw new UnsupportedOperationException();
    }
    
    private class TestAction implements Action {

        private final String name;

        private TestAction(String name) {
            this.name = name;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            actionsFired.add(name);
        }

        /**
         * @see org.kuali.rice.krms.framework.engine.Action#executeSimulation(org.kuali.rice.krms.api.engine.ExecutionEnvironment)
         */
        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            throw new UnsupportedOperationException();
        }
    }
}
