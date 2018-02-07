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
package org.kuali.rice.krms.impl.util;

import org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionBo;

/**
 * This class contains constants associated with the KRMS Repository 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KRMSPropertyConstants {

	public static final class Action {
		public static final String TYPE ="dataObject.agendaItemLineRuleAction.typeId";
        public static final String NAME ="dataObject.agendaItemLineRuleAction.name";
        public static final String DESCRIPTION ="dataObject.agendaItemLineRuleAction.description";
	}

	public static final class Agenda {
        public static final String NAME = "dataObject.agenda.name";
        public static final String CONTEXT = "dataObject.contextName";
        public static final String TYPE ="dataObject.agenda.typeId";
	}

    public static final class AgendaEditor {
        // used for building binding paths to the custom attributes map
        public static final String CUSTOM_ATTRIBUTES_MAP = "dataObject.customAttributesMap";
        public static final String CUSTOM_RULE_ACTION_ATTRIBUTES_MAP = "dataObject.customRuleActionAttributesMap";
    }
	
	public static final class Context {
		public static final String CONTEXT_ID ="dataObject.id";
		public static final String NAME = "dataObject.name";
		public static final String NAMESPACE = "dataObject.namespace";
		public static final String ATTRIBUTE_BOS = "attributeBos";
	}

    public static final class Term {
        public static final String TERM_ID ="dataObject.id";
        public static final String TERM_SPECIFICATION_ID ="dataObject.specificationId";
        public static final String DESCRIPTION = "dataObject.description";
    }

    public static final class TermSpecification {
        public static final String CATEGORY = "dataObject.categoryId";
        public static final String CONTEXT = "dataObject.contextId";
        public static final String TERM_SPECIFICATION_ID ="dataObject.id";
        public static final String NAME = "dataObject.name";
    }

	public static final class Rule {
		public static final String RULE_ID ="ruleId";
        public static final String NAME = "dataObject.agendaItemLine.rule.name";
        public static final String TYPE = "dataObject.agendaItemLine.rule.typeId";
        public static final String PROPOSITION_TREE_GROUP_ID = "RuleEditorView-Tree";
	}

	public static final class KrmsAttributeDefinition {
		public static final String NAME = "name";
		public static final String NAMESPACE = "namespace";		
	}

	public static final class BaseAttribute {
		public static final String ATTRIBUTE_DEFINITION_ID = "attributeDefinitionId";
		public static final String VALUE = "value";
		public static final String ATTRIBUTE_DEFINITION = "attributeDefinition";
	}

}
