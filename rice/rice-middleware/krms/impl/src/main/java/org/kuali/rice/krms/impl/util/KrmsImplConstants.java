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

public class KrmsImplConstants {

    /**
     * Prefix used to identify parameterized term specification ids in lists that otherwise contain
     * regular old term ids.
     */
    public static final String PARAMETERIZED_TERM_PREFIX = "parameterized:";
    public static final String CUSTOM_OPERATOR_PREFIX = "customOperator:";

    public static final String STUDENT_LOOKUP_VIEW = "StudentLookupView";
    public static final String STUDENT_INQUIRY_VIEW = "StudentInquiryView";

    public final static class WebPaths {
        public static final String AGENDA_INQUIRY_PATH = "krmsAgendaInquiry";
        public static final String AGENDA_EDITOR_PATH = "krmsAgendaEditor";
        public static final String AGENDA_STUDENT_INQUIRY_PATH = "krmsAgendaStudentInquiry";
        public static final String AGENDA_STUDENT_EDITOR_PATH = "krmsAgendaStudentEditor";
        public static final String PROPOSITION_PATH = "krmsProposition";
    }

    /**
     * This class contains constants associated with the KRMS Repository
     *
     * @author Kuali Rice Team (rice.collab@kuali.org)
     *
     */
    public static final class PropertyNames {

        public static final class Action {
            public static final String ACTION_ID ="actionId";
        }

        public static final class Agenda {
            public static final String ID = "id";
            public static final String AGENDA_ID ="agendaId";
        }

        public static final class Context {
            public static final String CONTEXT_ID ="contextId";
            public static final String NAME = "name";
            public static final String NAMESPACE = "namespace";
            public static final String ATTRIBUTE_BOS = "attributeBos";
        }
        
        public static final class TermResolver {
            public static final String TERM_RESOLVER_ID ="termResolverId";
            public static final String NAME = "name";
            public static final String NAMESPACE = "namespace";
            public static final String ATTRIBUTE_BOS = "attributeBos";
        }

        public static final class TermSpecification {
            public static final String TERM_SPECIFICATION_ID ="termSpecificationId";
            public static final String NAME = "name";
            public static final String NAMESPACE = "namespace";
            public static final String ATTRIBUTE_BOS = "attributeBos";
        }
        
        public static final class Term {
            public static final String TERM_ID ="termId";
            public static final String NAME = "name";
            public static final String NAMESPACE = "namespace";
            public static final String ATTRIBUTE_BOS = "attributeBos";
        }

        public static final class NaturalLanguageTemplate {
            public static final String NATURAL_LANGUAGE_TEMPLATE_ID ="naturalLanguageTemplateId";
        }

        public static final class NaturalLanguageUsage {
            public static final String NATURAL_LANGUAGE_USAGE_ID ="naturalLanguageUsageId";
        }

        public static final class ReferenceObjectBinding {
            public static final String REFERENCE_OBJECT_BINDING_ID ="referenceObjectBindingId";
        }

        public static final class Rule {
            public static final String RULE_ID ="ruleId";
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
    
}
